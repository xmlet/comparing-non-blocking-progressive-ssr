package benchmark.controller.presentations.sync

import java.io.OutputStream
import java.io.OutputStreamWriter
import java.io.Writer
import java.nio.CharBuffer
import java.nio.charset.Charset
import java.nio.charset.CharsetEncoder

fun OutputStream.outputStreamWriter(): OutputStreamWriter {
    return OutputStreamWriter2(this)
}

class OutputStreamWriter2(
    private val out: OutputStream,
) : OutputStreamWriter(out) {
    private val encoder: CharsetEncoder = Charset.defaultCharset().newEncoder()

    override fun write(b: Int) {
        out.write(b)
    }

    override fun write(cbuf: CharArray) {
        val byteBuffer = encoder.encode(CharBuffer.wrap(cbuf))
        out.write(byteBuffer.array(), 0, byteBuffer.limit())
    }

    override fun write(str: String) {
        val byteBuffer = encoder.encode(CharBuffer.wrap(str))
        out.write(byteBuffer.array(), 0, byteBuffer.limit())
    }

    override fun write(
        cbuf: CharArray,
        off: Int,
        len: Int,
    ) {
        val byteBuffer = encoder.encode(CharBuffer.wrap(cbuf, off, len))
        out.write(byteBuffer.array(), 0, byteBuffer.limit())
    }

    override fun write(
        str: String,
        off: Int,
        len: Int,
    ) {
        val byteBuffer = encoder.encode(CharBuffer.wrap(str, off, off + len))
        out.write(byteBuffer.array(), 0, byteBuffer.limit())
    }

    override fun append(c: Char): Writer {
        val byteBuffer = encoder.encode(CharBuffer.wrap(charArrayOf(c)))
        out.write(byteBuffer.array(), 0, byteBuffer.limit())
        return this
    }

    override fun append(csq: CharSequence?): Writer {
        if (csq != null) {
            val byteBuffer = encoder.encode(CharBuffer.wrap(csq))
            out.write(byteBuffer.array(), 0, byteBuffer.limit())
        }
        return this
    }

    override fun flush() {
        out.flush()
    }

    override fun append(
        csq: CharSequence?,
        start: Int,
        end: Int,
    ): Writer {
        if (csq != null) {
            val byteBuffer = encoder.encode(CharBuffer.wrap(csq, start, end))
            out.write(byteBuffer.array(), 0, byteBuffer.limit())
        }
        return this
    }

    override fun close() {
        out.close()
    }
}
