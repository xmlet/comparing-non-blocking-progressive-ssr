package benchmark.view.stocks

import benchmark.model.Stock
import htmlflow.HtmlFlow
import htmlflow.HtmlPage
import htmlflow.HtmlView
import htmlflow.HtmlViewAsync
import htmlflow.HtmlViewSuspend
import htmlflow.dyn
import htmlflow.html
import htmlflow.suspending
import htmlflow.viewSuspend
import io.reactivex.rxjava3.core.Observable
import kotlinx.coroutines.flow.Flow
import org.xmlet.htmlapifaster.EnumHttpEquivType
import org.xmlet.htmlapifaster.EnumMediaType
import org.xmlet.htmlapifaster.EnumRelType
import org.xmlet.htmlapifaster.EnumTypeContentType
import org.xmlet.htmlapifaster.EnumTypeScriptType
import org.xmlet.htmlapifaster.a
import org.xmlet.htmlapifaster.body
import org.xmlet.htmlapifaster.h1
import org.xmlet.htmlapifaster.head
import org.xmlet.htmlapifaster.link
import org.xmlet.htmlapifaster.meta
import org.xmlet.htmlapifaster.script
import org.xmlet.htmlapifaster.strong
import org.xmlet.htmlapifaster.style
import org.xmlet.htmlapifaster.table
import org.xmlet.htmlapifaster.tbody
import org.xmlet.htmlapifaster.td
import org.xmlet.htmlapifaster.th
import org.xmlet.htmlapifaster.thead
import org.xmlet.htmlapifaster.title
import org.xmlet.htmlapifaster.tr

object StocksHtmlFlow {
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

    val htmlFlowTemplate: HtmlViewAsync<Observable<Stock>> =
        HtmlFlow.viewAsync<Observable<Stock>> { page ->
            page.html {
                head {
                    title { text("Stock Prices") }
                    meta {
                        attrHttpEquiv(EnumHttpEquivType.CONTENT_TYPE)
                        attrContent("text/html; charset=UTF-8")
                    }
                    link {
                        addAttr("rel", "shortcut icon")
                        attrHref("/images/favicon.ico")
                    }
                    link {
                        attrRel(EnumRelType.STYLESHEET)
                        attrType(EnumTypeContentType.TEXT_CSS)
                        attrHref("/CSS/style.css")
                        attrMedia(EnumMediaType.ALL)
                    }
                    script {
                        attrType(EnumTypeScriptType.TEXT_JAVASCRIPT)
                        attrSrc("/js/util.js")
                    }
                    style {
                        raw(STOCKS_CSS)
                    }
                }
                body {
                    h1 { text("Stock Prices - HtmlFlow") }
                    table {
                        thead {
                            tr {
                                th { text("#") }
                                th { text("Symbol") }
                                th { text("Name") }
                                th { text("Price") }
                                th { text("Change") }
                                th { text("Ratio") }
                            }
                        }
                        tbody {
                            await { tbody, model: Observable<Stock>, onCompletion ->
                                var index = 0
                                model
                                    .doOnNext {
                                        val dto = StockDto(it, ++index)
                                        stockPartialAsync.renderAsync(dto).thenApply { frag -> tbody.raw(frag) }
                                    }
                                    .doOnComplete { onCompletion.finish() }
                                    .subscribe()
                            }
                        }
                    }
                }
            }
        }.threadSafe()

    val htmlFlowTemplateSuspending: HtmlViewSuspend<Flow<Stock>> =
        viewSuspend<Flow<Stock>> {
            html {
                head {
                    title { text("Stock Prices") }
                    meta {
                        attrHttpEquiv(EnumHttpEquivType.CONTENT_TYPE)
                        attrContent("text/html; charset=UTF-8")
                    }
                    link {
                        addAttr("rel", "shortcut icon")
                        attrHref("/images/favicon.ico")
                    }
                    link {
                        attrRel(EnumRelType.STYLESHEET)
                        attrType(EnumTypeContentType.TEXT_CSS)
                        attrHref("/CSS/style.css")
                        attrMedia(EnumMediaType.ALL)
                    }
                    script {
                        attrType(EnumTypeScriptType.TEXT_JAVASCRIPT)
                        attrSrc("/js/util.js")
                    }
                    style { raw(STOCKS_CSS) }
                }
                body {
                    h1 { text("Stock Prices - HtmlFlow") }
                    table {
                        thead {
                            tr {
                                th { text("#") }
                                th { text("Symbol") }
                                th { text("Name") }
                                th { text("Price") }
                                th { text("Change") }
                                th { text("Ratio") }
                            }
                        }
                        tbody {
                            suspending { model: Flow<Stock> ->
                                var index = 0
                                model.collect {
                                    val dto = StockDto(it, ++index)
                                    stockPartialAsync.renderAsync(dto).thenApply { frag -> raw(frag) }
                                }
                            }
                        }
                    }
                }
            }
        }.threadSafe()

    val htmlFlowTemplateSync: HtmlView<Observable<Stock>> =
        HtmlFlow.view<Observable<Stock>> { page ->
            page.html {
                head {
                    title { text("Stock Prices") }
                    meta {
                        attrHttpEquiv(EnumHttpEquivType.CONTENT_TYPE)
                        attrContent("text/html; charset=UTF-8")
                    }
                    link {
                        addAttr("rel", "shortcut icon")
                        attrHref("/images/favicon.ico")
                    }
                    link {
                        attrRel(EnumRelType.STYLESHEET)
                        attrType(EnumTypeContentType.TEXT_CSS)
                        attrHref("/CSS/style.css")
                        attrMedia(EnumMediaType.ALL)
                    }
                    script {
                        attrType(EnumTypeScriptType.TEXT_JAVASCRIPT)
                        attrSrc("/js/util.js")
                    }
                    style { raw(STOCKS_CSS) }
                }
                body {
                    h1 { text("Stock Prices - HtmlFlow") }
                    table {
                        thead {
                            tr {
                                th { text("#") }
                                th { text("Symbol") }
                                th { text("Name") }
                                th { text("Price") }
                                th { text("Change") }
                                th { text("Ratio") }
                            }
                        }
                        tbody {
                            dyn { model: Observable<Stock> ->
                                var index = 0
                                model
                                    .doOnNext {
                                        val dto = StockDto(it, ++index)
                                        stockPartialAsync.renderAsync(dto).thenApply { frag -> raw(frag) }
                                    }
                                    .blockingLast()
                            }
                        }
                    }
                }
            }
        }.threadSafe()

    val htmlFlowTemplateIter: HtmlView<Iterable<Stock>> =
        HtmlFlow.view<Iterable<Stock>> { page ->
            page.html {
                head {
                    title { text("Stock Prices") }
                    meta {
                        attrHttpEquiv(EnumHttpEquivType.CONTENT_TYPE)
                        attrContent("text/html; charset=UTF-8")
                    }
                    link {
                        addAttr("rel", "shortcut icon")
                        attrHref("/images/favicon.ico")
                    }
                    link {
                        attrRel(EnumRelType.STYLESHEET)
                        attrType(EnumTypeContentType.TEXT_CSS)
                        attrHref("/CSS/style.css")
                        attrMedia(EnumMediaType.ALL)
                    }
                    script {
                        attrType(EnumTypeScriptType.TEXT_JAVASCRIPT)
                        attrSrc("/js/util.js")
                    }
                    style { raw(STOCKS_CSS) }
                }
                body {
                    h1 { text("Stock Prices - HtmlFlow") }
                    table {
                        thead {
                            tr {
                                th { text("#") }
                                th { text("Symbol") }
                                th { text("Name") }
                                th { text("Price") }
                                th { text("Change") }
                                th { text("Ratio") }
                            }
                        }
                        tbody {
                            dyn { model: Iterable<Stock> ->
                                model.forEachIndexed { index, stock ->
                                    val dto = StockDto(stock, index + 1)
                                    raw(stockPartialSync.render(dto))
                                }
                            }
                        }
                    }
                }
            }
        }.threadSafe()

    private val stockPartialSync: HtmlView<StockDto?> =
        HtmlFlow.view<StockDto> { view -> view.stockFragment() }.threadSafe()

    private val stockPartialAsync =
        HtmlFlow.viewAsync<StockDto> { view -> view.stockFragment() }.threadSafe()

    fun HtmlPage.stockFragment() {
        tr()
            .dyn { model: StockDto ->
                if (model.index % 2 == 0) {
                    attrClass("even")
                } else {
                    attrClass("odd")
                }
            }
            .td { dyn { model: StockDto -> raw(model.index.toString()) } }
            .td {
                a {
                    dyn { model: StockDto ->
                        attrHref("/stocks/${model.stock.symbol}")
                        raw(model.stock.symbol)
                    }
                }
            }
            .td {
                a {
                    dyn { model: StockDto ->
                        attrHref(model.stock.url)
                        raw(model.stock.name)
                    }
                }
            }
            .td {
                strong {
                    dyn { model: StockDto ->
                        raw(model.stock.price.toString())
                    }
                }
            }
            .td {
                dyn { model: StockDto ->
                    val change = model.stock.change
                    if (change < 0) {
                        attrClass("minus")
                    }
                    raw(change.toString())
                }
            }
            .td {
                dyn { model: StockDto ->
                    val ratio = model.stock.ratio
                    if (ratio < 0) {
                        attrClass("minus")
                    }
                    raw(ratio.toString())
                }
            }
            .`__`() // tr
    }
}
