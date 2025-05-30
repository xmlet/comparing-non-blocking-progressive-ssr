package benchmark.view.presentations

import benchmark.model.Presentation
import htmlflow.HtmlFlow
import htmlflow.HtmlView
import htmlflow.HtmlViewAsync
import htmlflow.HtmlViewSuspend
import htmlflow.dyn
import htmlflow.html
import htmlflow.suspending
import htmlflow.viewSuspend
import io.reactivex.rxjava3.core.Observable
import kotlinx.coroutines.flow.Flow
import org.xmlet.htmlapifaster.EnumRelType
import org.xmlet.htmlapifaster.body
import org.xmlet.htmlapifaster.div
import org.xmlet.htmlapifaster.h1
import org.xmlet.htmlapifaster.head
import org.xmlet.htmlapifaster.link
import org.xmlet.htmlapifaster.meta
import org.xmlet.htmlapifaster.title

object PresentationsHtmlFlow {
    val htmlFlowTemplate: HtmlViewAsync<Observable<Presentation>> =
        HtmlFlow.viewAsync<Observable<Presentation>?> { view ->
            view.html {
                attrLang("en-us")
                head {
                    meta { attrCharset("UTF-8") }
                    meta { attrName("viewport").attrContent("width=device-width, initial-scale=1.0") }
                    meta { addAttr("http-equiv", "X-UA-Compatible").attrContent("IE=Edge") }
                    title { text("JFall 2013 Presentations - HtmlFlow") }
                    link { attrRel(EnumRelType.STYLESHEET).attrHref("/webjars/bootstrap/5.3.0/css/bootstrap.min.css") }
                }
                body {
                    div {
                        attrClass("container")
                        div {
                            attrClass("pb-2 mt-4 mb-3 border-bottom")
                            h1 { text("JFall 2013 Presentations - HtmlFlow") }
                        }
                        await { div, model: Observable<Presentation>, onCompletion ->
                            model
                                .doOnNext { presentationFragmentAsync.renderAsync(it).thenApply { frag -> div.raw(frag) } }
                                .doOnComplete { onCompletion.finish() }
                                .subscribe()
                        }
                    }
                }
            }
        }.threadSafe()

    val htmlFlowTemplateSuspending: HtmlViewSuspend<Flow<Presentation>> =
        viewSuspend<Flow<Presentation>> {
            html {
                attrLang("en-us")
                head {
                    meta { attrCharset("UTF-8") }
                    meta { attrName("viewport").attrContent("width=device-width, initial-scale=1.0") }
                    meta { addAttr("http-equiv", "X-UA-Compatible").attrContent("IE=Edge") }
                    title { text("JFall 2013 Presentations - HtmlFlow") }
                    link { attrRel(EnumRelType.STYLESHEET).attrHref("/webjars/bootstrap/5.3.0/css/bootstrap.min.css") }
                }
                body {
                    div {
                        attrClass("container")
                        div {
                            attrClass("pb-2 mt-4 mb-3 border-bottom")
                            h1 { text("JFall 2013 Presentations - HtmlFlow") }
                        }
                        suspending { model: Flow<Presentation> ->
                            model.collect {
                                presentationFragmentAsync.renderAsync(it).thenApply { frag -> raw(frag) }
                            }
                        }
                    }
                }
            }
        }.threadSafe()

    val htmlFlowTemplateSync: HtmlView<Observable<Presentation>> =
        HtmlFlow.view<Observable<Presentation>> { view ->
            view.html {
                attrLang("en-us")
                head {
                    meta { attrCharset("UTF-8") }
                    meta { attrName("viewport").attrContent("width=device-width, initial-scale=1.0") }
                    meta { addAttr("http-equiv", "X-UA-Compatible").attrContent("IE=Edge") }
                    title { text("JFall 2013 Presentations - HtmlFlow") }
                    link { attrRel(EnumRelType.STYLESHEET).attrHref("/webjars/bootstrap/5.3.0/css/bootstrap.min.css") }
                }
                body {
                    div {
                        attrClass("container")
                        div {
                            attrClass("pb-2 mt-4 mb-3 border-bottom")
                            h1 { text("JFall 2013 Presentations - HtmlFlow") }
                        }
                        dyn { model: Observable<Presentation> ->
                            model.doOnNext {
                                presentationFragmentAsync.renderAsync(it).thenApply { frag -> raw(frag) }
                            }.blockingLast()
                        }
                    }
                }
            }
        }.threadSafe()

    val htmlFlowTemplateIter: HtmlView<Iterable<Presentation>> =
        HtmlFlow.view<Iterable<Presentation>> { view ->
            view
                .html {
                    attrLang("en-us")
                    head {
                        meta { attrCharset("UTF-8") }
                        meta { attrName("viewport").attrContent("width=device-width, initial-scale=1.0") }
                        meta { addAttr("http-equiv", "X-UA-Compatible").attrContent("IE=Edge") }
                        title { text("JFall 2013 Presentations - HtmlFlow") }
                        link { attrRel(EnumRelType.STYLESHEET).attrHref("/webjars/bootstrap/5.3.0/css/bootstrap.min.css") }
                    }
                    body {
                        div {
                            attrClass("container")
                            div {
                                attrClass("pb-2 mt-4 mb-3 border-bottom")
                                h1 { text("JFall 2013 Presentations - HtmlFlow") }
                            }
                            dyn { model: Iterable<Presentation> ->
                                model.forEach { raw(presentationFragmentSync.render(it)) }
                            }
                        }
                    }
                }
        }.threadSafe()

    /**
     * Use fragment.renderAsync() rather than render() to create a new StringBuilder() (note render() reuse the same StringBuilder()).
     * Note render reuse a per-thread visitor from TLS than is indistinct among different coroutines.
     */
    private val presentationFragmentAsync: HtmlViewAsync<Presentation> =
        HtmlFlow.viewAsync<Presentation> { view ->
            view.div().attrClass("card mb-3 shadow-sm rounded")
                .div().attrClass("card-header")
                .h5()
                .attrClass("card-title")
                .dyn { presentation: Presentation -> raw(presentation.title + " - " + presentation.speakerName) }
                .`__`() // h5
                .`__`() // div
                .div()
                .attrClass("card-body")
                .dyn { presentation: Presentation -> raw(presentation.summary) }
                .`__`() // div
                .`__`() // div
        }.threadSafe()

    private val presentationFragmentSync =
        HtmlFlow.view<Presentation> { view ->
            view.div().attrClass("card mb-3 shadow-sm rounded")
                .div().attrClass("card-header")
                .h5()
                .attrClass("card-title")
                .dyn { presentation: Presentation -> raw(presentation.title + " - " + presentation.speakerName) }
                .`__`() // h5
                .`__`() // div
                .div()
                .attrClass("card-body")
                .dyn { presentation: Presentation -> raw(presentation.summary) }
                .`__`() // div
                .`__`() // div
        }.threadSafe()
}
