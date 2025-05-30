package benchmark.view.stocks

import benchmark.model.Stock
import io.jstach.jstache.JStache
import io.jstach.jstache.JStacheConfig
import io.jstach.jstache.JStacheFlags
import io.jstach.jstache.JStacheLambda
import io.jstach.jstachio.escapers.PlainText
import java.io.IOException
import java.io.OutputStream

object JStachioView {
    var stocksTemplate: JStachioStocksTemplate = JStachioStocksTemplate.of()

    @Throws(IOException::class)
    fun stocksWrite(
        model: StocksModel?,
        out: OutputStream?,
    ) {
        stocksTemplate.write(model, out)
    }

    @JStache(path = "templates/jstachio/stocks.jstachio.html", name = "JStachioStocksTemplate")
    @JStacheConfig(contentType = PlainText::class)
    @JStacheFlags(flags = [JStacheFlags.Flag.NO_NULL_CHECKING])
    class StocksModel(val stockItems: Iterable<Stock>) {
        @JStacheLambda
        fun Stock.isPositive(): Boolean {
            return this.change > 0
        }

        @JStacheLambda
        fun Int.isEven(): Boolean {
            return this % 2 == 0
        }
    }
}
