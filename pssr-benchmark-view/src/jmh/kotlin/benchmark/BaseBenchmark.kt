package benchmark

import benchmark.repository.PresentationRepository
import benchmark.repository.StockRepository
import org.openjdk.jmh.annotations.BenchmarkMode
import org.openjdk.jmh.annotations.Fork
import org.openjdk.jmh.annotations.Measurement
import org.openjdk.jmh.annotations.Mode
import org.openjdk.jmh.annotations.OutputTimeUnit
import org.openjdk.jmh.annotations.Scope
import org.openjdk.jmh.annotations.State
import org.openjdk.jmh.annotations.Warmup
import java.util.Arrays
import java.util.Locale
import java.util.concurrent.TimeUnit
import java.util.stream.Collectors
import kotlin.test.assertEquals


@Fork(5)
@Warmup(iterations = 5)
@Measurement(iterations = 10)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.SECONDS)
@State(Scope.Benchmark)
open class BaseBenchmark {
    protected fun getContext(
        stockRepository: StockRepository,
        presentationRepository: PresentationRepository
    ): Map<String, Any> {
        return mutableMapOf<String, Any>().apply {
            this["stocks"] = stockRepository.findAllIterable()
            this["stocksReactive"] = stockRepository.findAllReactive()
            this["presentations"] = presentationRepository.findAllIterable()
            this["presentationsReactive"] = presentationRepository.findAllReactive()
        }
    }

    open fun validateStocks(response: String) {
        return assertEquals(
            trimLines(htmlResponseStocks),
            trimLines(response),
        )
    }

    open fun validatePresentations(response: String) {
        return assertEquals(
            trimLines(htmlResponsePresentations),
            trimLines(response),
        )
    }

    protected val htmlResponsePresentations = """
                <!DOCTYPE html>
                <html lang="en-us">
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
                    <title>JFall 2013 Presentations - Thymeleaf</title>
                    <link rel="stylesheet" href="/webjars/bootstrap/5.3.0/css/bootstrap.min.css">
                </head>
                <body>
                	<div class="container">
                        <div class="pb-2 mt-4 mb-3 border-bottom">
                    <h1>JFall 2013 Presentations - Thymeleaf</h1>
                </div>
                        <div class="card mb-3 shadow-sm rounded">
                            <div class="card-header">
                                <h5 class="card-title">Shootout! Template engines on the JVM - Jeroen Reijn</h5>
                            </div>
                            <div class="card-body">Are you still using JavaServer Pages as your main template language? With the popularity of template engines for other languages like Ruby and Scala and the shift in doing more MVC in the browser there are quite some new and interesting new template languages available for the JVM. During this session we will take a look at the less known, but quite interesting new template engines and see how they compare with the industries standards.</div>
                        </div><div class="card mb-3 shadow-sm rounded">
                            <div class="card-header">
                                <h5 class="card-title">HoneySpider Network: a Java based system to hunt down malicious websites - Niels van Eijck</h5>
                            </div>
                            <div class="card-body">Legitimate websites such as news sites happen to get compromised by attackers injecting malicious content. The aim of these so-called &#8220;watering hole attacks&#8221; is to infect as many visitors of a website as possible, and are sometimes even targeted at a specific group of individuals. It is increasingly important to detect these infections at an early stage.<br/><br/>HoneySpider Network to the rescue!<br/><br/>It is a Java based open source framework that automatically scans website urls, analyses the results and reports on any malware detected.<br/>Attend this talk to gain a better understanding of malware detection and client honeypots and get an overview of the HoneySpider Network&#8217;s architecture, its code and its plugins it uses. A live demo is also included!</div>
                        </div><div class="card mb-3 shadow-sm rounded">
                            <div class="card-header">
                                <h5 class="card-title">Building scalable network applications with Netty - Jaap ter Woerds</h5>
                            </div>
                            <div class="card-body">Since the introduction of the Java NIO API&apos;s with Java 4, developers have access to modern operating system facilities to perform asynchronous IO. Using these facilities it is possible to write networking application that that serve thousands of connected clients efficiently. Unfortunately, the NIO API&apos;s are quite low level and require a fair share of boilerplate to get started.<br/><br/>In this presentation, I will introduce the Netty framework and how its architecture helps you as a developer stay focused on the interesting parts of your network application. At the end of the presentation I will give some real world examples and show how we use Netty in the architecture of our mobile messaging platform XMS.</div>
                        </div><div class="card mb-3 shadow-sm rounded">
                            <div class="card-header">
                                <h5 class="card-title">Opening - Bert Ertman</h5>
                            </div>
                            <div class="card-body">De openingssessie van de conferentie met aandacht voor de dag zelf en nieuws vanuit de NLJUG. De sessie wordt gepresenteerd door Bert Ertman.</div>
                        </div><div class="card mb-3 shadow-sm rounded">
                            <div class="card-header">
                                <h5 class="card-title">Keynote door ING - Amir Arroni</h5>
                            </div>
                            <div class="card-body">Keynote van ING, gepresenteerd door Amir Arooni en Peter Jacobs.</div>
                        </div><div class="card mb-3 shadow-sm rounded">
                            <div class="card-header">
                                <h5 class="card-title">Keynote door Oracle - Sharat Chander</h5>
                            </div>
                            <div class="card-body">Keynote van Oracle, gepresenteerd door Sharat Chander.</div>
                        </div><div class="card mb-3 shadow-sm rounded">
                            <div class="card-header">
                                <h5 class="card-title">Reactieve applicaties ? klaar voor te toekomst - Allard Buijze</h5>
                            </div>
                            <div class="card-body">De technische eisen aan webapplicaties veranderen in hoog tempo. Enkele jaren geleden nog gebruikten de grootere applicaties enkele tientallen servers en werden response tijden van een seconde en onderhoudsvensters van enkele uren nog geaccepteerd. Tegenwoordig moeten applicaties 100% beschikbaar zijn, terwijl de gebruiker in enkele milliseconden antwoord wil krijgen. Om pieken in gebruik op te kunnen vangen moeten de applicaties op duizenden processoren in een cloud omgeving kunnen draaien.<br/><br/>De tekortkomingen van de huidige standaard architectuurprincipes kunnen worden opgevangen door een zogenaamde &#8220;reactive architecture&#8221;. Reactieve applicaties bezitten een aantal eigenschappen waardoor ze beter kunnen omgaan met opschalen, bestand zijn tegen fouten en bovendien efficienter gebruik maken van beschikbare server-bronnen.<br/><br/>In deze presentatie laat Allard zien hoe deze eigenschappen gerealiseerd kunnen worden en welke reeds bekende architectuurpatronen en frameworks hieraan een bijdrage leveren.</div>
                        </div><div class="card mb-3 shadow-sm rounded">
                            <div class="card-header">
                                <h5 class="card-title">HTML 5 Geolocation + WebSockets + Scalable JavaEE Backend === Awesome Realtime Location Aware Applications - Shekhar Gulati</h5>
                            </div>
                            <div class="card-body">Location Aware apps are everywhere and we use them heavily in our day to day life. You have seen the stuff that Foursquare has done with spatial and you want some of that hotness for your app. But, where to start? In this session, we will build a location aware app using HTML 5 on the client and scalable JavaEE + MongoDB on the server side. HTML 5 GeoLocation API help us to find user current location and MongoDB offers Geospatial indexing support which provides an easy way to get started and enables a variety of location-based applications - ranging from field resource management to social check-ins. Next we will add realtime capabilities to our application using Pusher. Pusher provides scalable WebSockets as a service. The Java EE 6 backend will be built using couple of Java EE 6 technologies -- JAXRS and CDI. Finally , we will deploy our Java EE application on OpenShift -- Red Hat&apos;s public, scalable Platform as a Service.</div>
                        </div><div class="card mb-3 shadow-sm rounded">
                            <div class="card-header">
                                <h5 class="card-title">Retro Gaming with Lambdas - Stephen Chin</h5>
                            </div>
                            <div class="card-body">Lambda expressions are coming in Java 8 and dramatically change the programming model.  They allow new functional programming patterns that were not possible before, increasing the expressiveness and power of the Java language.<br/><br/>In this university session, you will learn how to take advantage of the new lambda-enabled Java 8 APIs by building out a retro video game in JavaFX.<br/><br/>Some of the Java 8 features you will learn about include enhanced collections, functional interfaces, simplified event handlers, and the new stream API.  Start using these in your application today leveraging the latest OpenJDK builds so you can prepare for the future Java 8 release.</div>
                        </div><div class="card mb-3 shadow-sm rounded">
                            <div class="card-header">
                                <h5 class="card-title">Data Science with R for Java Developers - Sander Mak</h5>
                            </div>
                            <div class="card-body">Understanding data is increasingly important to create cutting-edge applications. A whole new data science field is emerging, with the open source R language as a leading technology. This statistical programming language is specifically designed for analyzing and understanding data.<br/><br/>In this session we approach R from the perspective of Java developers. How do you get up to speed quickly, what are the pitfalls to look out for?  Also we discuss how to bridge the divide between the R language and the JVM. After this session you can use your new skills to explore an exciting world of data analytics and machine learning! </div>
                        </div>
                    </div>
                </body>
                </html>
                """.trimIndent()



    protected fun trimLines(lines: String): String {
        val nl = "\n"
        return Arrays.stream<String>(
            lines
                .replace("<", System.lineSeparator() + "<")
                .replace(">", ">" + System.lineSeparator())
                .split(nl.toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray(),
        )
            .map<String?> { line: String? -> line!!.trim { it <= ' ' }.replace("\t", "").lowercase(Locale.getDefault()) }
            .filter { line: String? -> line!!.isNotEmpty() && !line.contains("stock prices") && !line.contains("jfall 2013 presentations") }
            .collect(Collectors.joining(nl))
    }


    protected val htmlResponseStocks = """
                <!DOCTYPE html>
                <html>
                <head>
                <title>Stock Prices</title>
                <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
                <link rel="shortcut icon" href="/images/favicon.ico">
                <link rel="stylesheet" type="text/css" href="/css/style.css" media="all">
                <script type="text/javascript" src="/js/util.js"></script>
                <style>
                /*<![CDATA[*/
                body {
                	color: #333333;
                	line-height: 150%;
                }

                thead {
                	font-weight: bold;
                	background-color: #CCCCCC;
                }

                .odd {
                	background-color: #FFCCCC;
                }

                .even {
                	background-color: #CCCCFF;
                }

                .minus {
                	color: #FF0000;
                }

                /*]]>*/
                </style>

                </head>

                <body>

                	<h1>Stock Prices</h1>

                	<table>
                		<thead>
                			<tr>
                				<th>#</th>
                				<th>symbol</th>
                				<th>name</th>
                				<th>price</th>
                				<th>change</th>
                				<th>ratio</th>
                			</tr>
                		</thead>
                		<tbody>
                			<tr class="odd">
                				<td>1</td>
                				<td><a href="/stocks/ADBE">ADBE</a></td>
                				<td><a href="http://www.adobe.com">Adobe Systems</a></td>
                				<td><strong>39.26</strong></td>

                				<td>0.13</td>
                				<td>0.33</td>
                			</tr>
                			<tr class="even">
                				<td>2</td>
                				<td><a href="/stocks/AMD">AMD</a></td>
                				<td><a href="http://www.amd.com">Advanced Micro Devices</a></td>
                				<td><strong>16.22</strong></td>

                				<td>0.17</td>
                				<td>1.06</td>
                			</tr>
                			<tr class="odd">
                				<td>3</td>
                				<td><a href="/stocks/AMZN">AMZN</a></td>
                				<td><a href="http://www.amazon.com">Amazon.com</a></td>
                				<td><strong>36.85</strong></td>

                				<td class="minus">-0.23</td>
                				<td class="minus">-0.62</td>
                			</tr>
                			<tr class="even">
                				<td>4</td>
                				<td><a href="/stocks/AAPL">AAPL</a></td>
                				<td><a href="http://www.apple.com">Apple</a></td>
                				<td><strong>85.38</strong></td>

                				<td class="minus">-0.87</td>
                				<td class="minus">-1.01</td>
                			</tr>
                			<tr class="odd">
                				<td>5</td>
                				<td><a href="/stocks/BEAS">BEAS</a></td>
                				<td><a href="http://www.bea.com">BEA Systems</a></td>
                				<td><strong>12.46</strong></td>

                				<td>0.09</td>
                				<td>0.73</td>
                			</tr>
                			<tr class="even">
                				<td>6</td>
                				<td><a href="/stocks/CA">CA</a></td>
                				<td><a href="http://www.ca.com">CA</a></td>
                				<td><strong>24.66</strong></td>

                				<td>0.38</td>
                				<td>1.57</td>
                			</tr>
                			<tr class="odd">
                				<td>7</td>
                				<td><a href="/stocks/CSCO">CSCO</a></td>
                				<td><a href="http://www.cisco.com">Cisco Systems</a></td>
                				<td><strong>26.35</strong></td>

                				<td>0.13</td>
                				<td>0.5</td>
                			</tr>
                			<tr class="even">
                				<td>8</td>
                				<td><a href="/stocks/DELL">DELL</a></td>
                				<td><a href="http://www.dell.com/">Dell</a></td>
                				<td><strong>23.73</strong></td>

                				<td class="minus">-0.42</td>
                				<td class="minus">-1.74</td>
                			</tr>
                			<tr class="odd">
                				<td>9</td>
                				<td><a href="/stocks/EBAY">EBAY</a></td>
                				<td><a href="http://www.ebay.com">eBay</a></td>
                				<td><strong>31.65</strong></td>

                				<td class="minus">-0.8</td>
                				<td class="minus">-2.47</td>
                			</tr>
                			<tr class="even">
                				<td>10</td>
                				<td><a href="/stocks/GOOG">GOOG</a></td>
                				<td><a href="http://www.google.com">Google</a></td>
                				<td><strong>495.84</strong></td>

                				<td>7.75</td>
                				<td>1.59</td>
                			</tr>
                			<tr class="odd">
                				<td>11</td>
                				<td><a href="/stocks/HPQ">HPQ</a></td>
                				<td><a href="http://www.hp.com">Hewlett-Packard</a></td>
                				<td><strong>41.69</strong></td>

                				<td class="minus">-0.02</td>
                				<td class="minus">-0.05</td>
                			</tr>
                			<tr class="even">
                				<td>12</td>
                				<td><a href="/stocks/IBM">IBM</a></td>
                				<td><a href="http://www.ibm.com">IBM</a></td>
                				<td><strong>97.45</strong></td>

                				<td class="minus">-0.06</td>
                				<td class="minus">-0.06</td>
                			</tr>
                			<tr class="odd">
                				<td>13</td>
                				<td><a href="/stocks/INTC">INTC</a></td>
                				<td><a href="http://www.intel.com">Intel</a></td>
                				<td><strong>20.53</strong></td>

                				<td class="minus">-0.07</td>
                				<td class="minus">-0.34</td>
                			</tr>
                			<tr class="even">
                				<td>14</td>
                				<td><a href="/stocks/JNPR">JNPR</a></td>
                				<td><a href="http://www.juniper.net/">Juniper Networks</a></td>
                				<td><strong>18.96</strong></td>

                				<td>0.5</td>
                				<td>2.71</td>
                			</tr>
                			<tr class="odd">
                				<td>15</td>
                				<td><a href="/stocks/MSFT">MSFT</a></td>
                				<td><a href="http://www.microsoft.com">Microsoft</a></td>
                				<td><strong>30.6</strong></td>

                				<td>0.15</td>
                				<td>0.49</td>
                			</tr>
                			<tr class="even">
                				<td>16</td>
                				<td><a href="/stocks/ORCL">ORCL</a></td>
                				<td><a href="http://www.oracle.com">Oracle</a></td>
                				<td><strong>17.15</strong></td>

                				<td>0.17</td>
                				<td>1.1</td>
                			</tr>
                			<tr class="odd">
                				<td>17</td>
                				<td><a href="/stocks/SAP">SAP</a></td>
                				<td><a href="http://www.sap.com">SAP</a></td>
                				<td><strong>46.2</strong></td>

                				<td class="minus">-0.16</td>
                				<td class="minus">-0.35</td>
                			</tr>
                			<tr class="even">
                				<td>18</td>
                				<td><a href="/stocks/STX">STX</a></td>
                				<td><a href="http://www.seagate.com/">Seagate Technology</a></td>
                				<td><strong>27.35</strong></td>

                				<td class="minus">-0.36</td>
                				<td class="minus">-1.3</td>
                			</tr>
                			<tr class="odd">
                				<td>19</td>
                				<td><a href="/stocks/SUNW">SUNW</a></td>
                				<td><a href="http://www.sun.com">Sun Microsystems</a></td>
                				<td><strong>6.33</strong></td>

                				<td class="minus">-0.01</td>
                				<td class="minus">-0.16</td>
                			</tr>
                			<tr class="even">
                				<td>20</td>
                				<td><a href="/stocks/YHOO">YHOO</a></td>
                				<td><a href="http://www.yahoo.com">Yahoo</a></td>
                				<td><strong>28.04</strong></td>

                				<td class="minus">-0.17</td>
                				<td class="minus">-0.6</td>
                			</tr>
                		</tbody>
                	</table>

                </body>
                </html>
                """.trimIndent()
}