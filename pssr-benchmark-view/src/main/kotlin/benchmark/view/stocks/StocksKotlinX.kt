package benchmark.view.stocks

import benchmark.model.Stock
import benchmark.view.appendable.AppendableSink
import io.reactivex.rxjava3.core.Observable
import kotlinx.html.LinkMedia
import kotlinx.html.LinkRel
import kotlinx.html.TBODY
import kotlinx.html.a
import kotlinx.html.body
import kotlinx.html.classes
import kotlinx.html.h1
import kotlinx.html.head
import kotlinx.html.html
import kotlinx.html.link
import kotlinx.html.meta
import kotlinx.html.script
import kotlinx.html.stream.appendHTML
import kotlinx.html.strong
import kotlinx.html.style
import kotlinx.html.table
import kotlinx.html.tbody
import kotlinx.html.td
import kotlinx.html.th
import kotlinx.html.thead
import kotlinx.html.title
import kotlinx.html.tr
import kotlinx.html.unsafe

object StocksKotlinX {
    private val STOCKS_CSS =
        "/*<![CDATA[*/\n" +
            "body {\n" +
            "\tcolor: #333333;\n" +
            "\tline-height: 150%;\n" +
            "}\n" +
            "\n" +
            "thead {\n" +
            "\tfont-weight: bold;\n" +
            "\tbackground-color: #CCCCCC;\n" +
            "}\n" +
            "\n" +
            ".odd {\n" +
            "\tbackground-color: #FFCCCC;\n" +
            "}\n" +
            "\n" +
            ".even {\n" +
            "\tbackground-color: #CCCCFF;\n" +
            "}\n" +
            "\n" +
            ".minus {\n" +
            "\tcolor: #FF0000;\n" +
            "}\n" +
            "\n" +
            "/*]]>*/"

    fun kotlinXReactive(
        sink: AppendableSink,
        stocks: Observable<Stock>,
    ) {
        sink.appendHTML().html {
            head {
                title("Stock Prices")
                meta {
                    httpEquiv = "Content-Type"
                    content = "text/html; charset=UTF-8"
                }
                link {
                    rel = "shortcut icon"
                    href = "/images/favicon.ico"
                }
                link {
                    rel = LinkRel.stylesheet
                    type = "text/css"
                    href = "/CSS/style.css"
                    media = LinkMedia.all
                }
                script {
                    type = "text/javascript"
                    src = "/js/util.js"
                }
                style {
                    unsafe {
                        +STOCKS_CSS
                    }
                }
            }
            body {
                h1 { +"Stock Prices - KotlinX" }
                table {
                    thead {
                        tr {
                            th { +"#" }
                            th { +"Symbol" }
                            th { +"Name" }
                            th { +"Price" }
                            th { +"Change" }
                            th { +"Ratio" }
                        }
                    }
                    tbody {
                        Observable.zip(stocks, Observable.range(0, Int.MAX_VALUE)) { stock, idx -> StockDto(stock, idx + 1) }
                            .doOnNext { dto -> stockFragment(dto) }
                            .doOnComplete { sink.close() }
                            .subscribe()
                    }
                }
            }
        }
    }

    fun kotlinXSync(
        sink: Appendable,
        stocks: Observable<Stock>,
    ) {
        sink.appendHTML().html {
            head {
                title("Stock Prices")
                meta {
                    httpEquiv = "Content-Type"
                    content = "text/html; charset=UTF-8"
                }
                link {
                    rel = "shortcut icon"
                    href = "/images/favicon.ico"
                }
                link {
                    rel = LinkRel.stylesheet
                    type = "text/css"
                    href = "/CSS/style.css"
                    media = LinkMedia.all
                }
                script {
                    type = "text/javascript"
                    src = "/js/util.js"
                }
                style {
                    unsafe {
                        +STOCKS_CSS
                    }
                }
            }
            body {
                h1 { +"Stock Prices - KotlinX" }
                table {
                    thead {
                        tr {
                            th { +"#" }
                            th { +"Symbol" }
                            th { +"Name" }
                            th { +"Price" }
                            th { +"Change" }
                            th { +"Ratio" }
                        }
                    }
                    tbody {
                        Observable.zip(stocks, Observable.range(0, Int.MAX_VALUE)) { stock, idx -> StockDto(stock, idx + 1) }
                            .doOnNext { dto -> stockFragment(dto) }
                            .blockingLast()
                    }
                }
            }
        }
    }

    fun kotlinXIterable(
        sink: Appendable,
        stocks: Iterable<Stock>,
    ) {
        sink.appendHTML().html {
            head {
                title("Stock Prices")
                meta {
                    httpEquiv = "Content-Type"
                    content = "text/html; charset=UTF-8"
                }
                link {
                    rel = "shortcut icon"
                    href = "/images/favicon.ico"
                }
                link {
                    rel = LinkRel.stylesheet
                    type = "text/css"
                    href = "/CSS/style.css"
                    media = LinkMedia.all
                }
                script {
                    type = "text/javascript"
                    src = "/js/util.js"
                }
                style {
                    unsafe {
                        +STOCKS_CSS
                    }
                }
            }
            body {
                h1 { +"Stock Prices - KotlinX" }
                table {
                    thead {
                        tr {
                            th { +"#" }
                            th { +"Symbol" }
                            th { +"Name" }
                            th { +"Price" }
                            th { +"Change" }
                            th { +"Ratio" }
                        }
                    }
                    tbody {
                        stocks.forEachIndexed { index, stock ->
                            val dto = StockDto(stock, index + 1)
                            stockFragment(dto)
                        }
                    }
                }
            }
        }
    }

    private fun TBODY.stockFragment(stock: StockDto) {
        tr {
            if (stock.index % 2 == 0) {
                classes = setOf("even")
            } else {
                classes = setOf("odd")
            }
            td { +stock.index.toString() }
            td {
                a(href = "/stocks/${stock.stock.symbol}") {
                    +stock.stock.symbol
                }
            }
            td {
                a(href = stock.stock.url) {
                    +stock.stock.name
                }
            }
            td {
                strong { +stock.stock.price.toString() }
            }
            td {
                val change = stock.stock.change
                if (change < 0) classes = setOf("minus")
                +change.toString()
            }
            td {
                val ratio = stock.stock.ratio
                if (ratio < 0) classes = setOf("minus")
                +ratio.toString()
            }
        }
    }
}
