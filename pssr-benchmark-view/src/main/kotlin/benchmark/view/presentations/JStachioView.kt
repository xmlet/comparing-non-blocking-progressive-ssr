package benchmark.view.presentations

import benchmark.model.Presentation
import io.jstach.jstache.JStache
import io.jstach.jstache.JStacheConfig
import io.jstach.jstache.JStacheFlags
import io.jstach.jstachio.escapers.PlainText
import java.io.IOException
import java.io.OutputStream

object JStachioView {
    var presentationsTemplate: JStachioPresentationsTemplate = JStachioPresentationsTemplate.of()

    @Throws(IOException::class)
    fun presentationsWrite(
        model: PresentationsModel?,
        out: OutputStream?,
    ) {
        presentationsTemplate.write(model, out)
    }

    @JStache(path = "templates/jstachio/presentations.jstachio.html", name = "JStachioPresentationsTemplate")
    @JStacheConfig(contentType = PlainText::class)
    @JStacheFlags(flags = [JStacheFlags.Flag.NO_NULL_CHECKING])
    class PresentationsModel(presentationItems: Iterable<Presentation>) {
        val presentationItems: Iterable<Presentation> = presentationItems
    }
}
