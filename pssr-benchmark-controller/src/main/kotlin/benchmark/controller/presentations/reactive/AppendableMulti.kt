package benchmark.controller.presentations.reactive

import io.smallrye.mutiny.Multi
import java.io.Closeable
import io.smallrye.mutiny.subscription.MultiEmitter
import java.util.concurrent.LinkedBlockingDeque
import java.util.concurrent.atomic.AtomicBoolean

class AppendableMulti : Appendable, Closeable {

    @Volatile
    private var emitter: MultiEmitter<in String>? = null

    private val queue = LinkedBlockingDeque<String>()
    private val closed = AtomicBoolean(false)

    fun toMulti(): Multi<String> {
        return Multi.createFrom().emitter { em ->
            emitter = em
            drain()
        }
    }

    override fun append(csq: CharSequence): Appendable {
        val item = csq.toString()
        if (emitter != null && !closed.get()) {
            emitter!!.emit(item)
        } else {
            queue.offer(item)
        }
        return this
    }

    override fun append(csq: CharSequence, start: Int, end: Int): Appendable {
        return append(csq.subSequence(start, end))
    }

    override fun append(c: Char): Appendable {
        return append(c.toString())
    }

    override fun close() {
        closed.set(true)
        drain()
    }

    private fun drain() {
        val em = emitter ?: return
        do {
            while (true) {
                val item = queue.poll() ?: break
                em.emit(item)
            }
            if (closed.get() && queue.isEmpty()) {
                em.complete()
                break
            }
        } while (queue.isNotEmpty() && !closed.get())
    }
}
