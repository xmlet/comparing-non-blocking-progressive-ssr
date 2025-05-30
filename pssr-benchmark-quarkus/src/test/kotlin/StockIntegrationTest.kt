import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.TestInstance
import java.net.URI
import java.util.Arrays
import java.util.Locale
import java.util.stream.Collectors
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@QuarkusTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
open class StockIntegrationTest {
    init {
        System.setProperty("benchTimeout", "10")
    }

    @Test
    fun testRockerTemplate() {
        val response = getResponse("/stocks/rocker")
        assertNotNull(response)
        val trimmedResponse = trimLines(response)
        val trimmedExpected = trimLines(wellFormedHtmlAssertion())
        assertEquals(trimmedExpected, trimmedResponse)
    }

    @Test
    fun testRockerTemplateOk() {
        RestAssured.given()
            .`when`()
            .get(URI.create("/stocks/rocker"))
            .then()
            .statusCode(200)
    }

    @Test
    fun testJstachioTemplate() {
        val response = getResponse("/stocks/jstachio")
        assertNotNull(response)
        val trimmedResponse = trimLines(response)
        val trimmedExpected = trimLines(wellFormedHtmlAssertion())
        assertEquals(trimmedExpected, trimmedResponse)
    }

    @Test
    fun testJstachioTemplateOk() {
        RestAssured.given()
            .`when`()
            .get(URI.create("/stocks/jstachio"))
            .then()
            .statusCode(200)
    }

    @Test
    fun testQuteTemplateOk() {
        RestAssured.given()
            .`when`()
            .get(URI.create("/stocks/reactive/qute"))
            .then()
            .statusCode(200)
    }

    @Test
    fun testQuteTemplate() {
        val response = getResponse("/stocks/reactive/qute")
        assertNotNull(response)
        val trimmedResponse = trimLines(response)
        val trimmedExpected = trimLines(wellFormedHtmlAssertion())
        assertEquals(trimmedExpected, trimmedResponse)
    }

    @Test
    fun testHtmlFlowReactiveTemplate() {
        val response = getResponse("/stocks/reactive/htmlFlow")
        assertNotNull(response)
        val trimmedResponse = trimLines(response)
        val trimmedExpected = trimLines(wellFormedHtmlAssertion())
        assertEquals(trimmedExpected, trimmedResponse)
    }

    @Test
    fun testHtmlFlowReactiveTemplateOk() {
        RestAssured.given()
            .`when`()
            .get(URI.create("/stocks/reactive/htmlFlow"))
            .then()
            .statusCode(200)
    }

    @Test
    fun testPebbleTemplate() {
        val response = getResponse("/stocks/pebble")
        assertNotNull(response)
        val trimmedResponse = trimLines(response)
        val trimmedExpected = trimLines(wellFormedHtmlAssertion())
        assertEquals(trimmedExpected, trimmedResponse)
    }

    @Test
    fun testPebbleTemplateOk() {
        RestAssured.given()
            .`when`()
            .get(URI.create("/stocks/pebble"))
            .then()
            .statusCode(200)
    }

    @Test
    fun testFreemarkerTemplate() {
        val response = getResponse("/stocks/freemarker")
        assertNotNull(response)
        val trimmedResponse = trimLines(response)
        val trimmedExpected = trimLines(wellFormedHtmlAssertion())
        assertEquals(trimmedExpected, trimmedResponse)
    }

    @Test
    fun testFreemarkerTemplateOk() {
        RestAssured.given()
            .`when`()
            .get(URI.create("/stocks/freemarker"))
            .then()
            .statusCode(200)
    }

    @Test
    fun testTrimouTemplate() {
        val response = getResponse("/stocks/trimou")
        assertNotNull(response)
        val trimmedResponse = trimLines(response)
        val trimmedExpected = trimLines(wellFormedHtmlAssertion())
        assertEquals(trimmedExpected, trimmedResponse)
    }

    @Test
    fun testTrimouTemplateOk() {
        RestAssured.given()
            .`when`()
            .get(URI.create("/stocks/trimou"))
            .then()
            .statusCode(200)
    }

    @Test
    fun testVelocityTemplate() {
        val response = getResponse("/stocks/velocity")
        assertNotNull(response)
        val trimmedResponse = trimLines(response)
        val trimmedExpected = trimLines(wellFormedHtmlAssertion())
        assertEquals(trimmedExpected, trimmedResponse)
    }

    @Test
    fun testVelocityTemplateOk() {
        RestAssured.given()
            .`when`()
            .get(URI.create("/stocks/velocity"))
            .then()
            .statusCode(200)
    }

    @Test
    fun testThymeleafTemplate() {
        val response = getResponse("/stocks/thymeleaf")
        assertNotNull(response)
        val trimmedResponse = trimLines(response)
        val trimmedExpected = trimLines(wellFormedHtmlAssertion())
        assertEquals(trimmedExpected, trimmedResponse)
    }

    @Test
    fun testThymeleafTemplateOk() {
        RestAssured.given()
            .`when`()
            .get(URI.create("/stocks/thymeleaf"))
            .then()
            .statusCode(200)
    }

    @Test
    fun testKotlinxTemplate() {
        val response = getResponse("/stocks/kotlinx")
        assertNotNull(response)
        val trimmedResponse = trimLines(response)
        val trimmedExpected = trimLines(wellFormedHtmlAssertion().replace("<!DOCTYPE html>", ""))
        assertEquals(trimmedExpected, trimmedResponse)
    }

    @Test
    fun testKotlinxTemplateOk() {
        RestAssured.given()
            .`when`()
            .get(URI.create("/stocks/kotlinx"))
            .then()
            .statusCode(200)
    }

    @Test
    fun testHtmlFlowTemplate() {
        val response = getResponse("/stocks/htmlFlow")
        assertNotNull(response)
        val trimmedResponse = trimLines(response)
        val trimmedExpected = trimLines(wellFormedHtmlAssertion())
        assertEquals(trimmedExpected, trimmedResponse)
    }

    @Test
    fun testHtmlFlowTemplateOk() {
        RestAssured.given()
            .`when`()
            .get(URI.create("/stocks/htmlFlow"))
            .then()
            .statusCode(200)
    }

//    @DisplayName("Should generate html for each template")
//    @ParameterizedTest
//    @MethodSource("htmlTemplates")
//    fun test_endpoint_for_template_for_response(r: RouteAndExpected) {
//        val response = getResponse(r.route.toString())
//
//        assertNotNull(response)
//
//        val trimmedResponse = trimLines(response)
//        val trimmedExpected = trimLines(r.expected.toString())
//
//        assertNotNull(trimmedResponse)
//        assertNotNull(trimmedExpected)
//        assertEquals(trimmedExpected, trimmedResponse)
//    }

//    @DisplayName("Should return 200 ok status code for all requests")
//    @ParameterizedTest
//    @MethodSource("htmlTemplates")
//    fun test_endpoint_for_template_ok(r: RouteAndExpected) {
//        RestAssured.given()
//            .`when`()
//            .get(URI.create("${r.route}"))
//            .then()
//            .statusCode(200)
//    }

//    private fun htmlTemplates(): Stream<Arguments?> {
//        return Stream.of(
//            Arguments.of(
//                RouteAndExpected("/stocks/rocker", wellFormedHtmlAssertion()),
//            ),
//            Arguments.of(
//                RouteAndExpected("/stocks/jstachio", wellFormedHtmlAssertion()),
//            ),
//            Arguments.of(
//                RouteAndExpected("/stocks/pebble", wellFormedHtmlAssertion()),
//            ),
//            Arguments.of(
//                RouteAndExpected("/stocks/freemarker", wellFormedHtmlAssertion()),
//            ),
//            Arguments.of(
//                RouteAndExpected("/stocks/trimou", wellFormedHtmlAssertion()),
//            ),
//            Arguments.of(
//                RouteAndExpected("/stocks/velocity", wellFormedHtmlAssertion()),
//            ),
//            Arguments.of(
//                RouteAndExpected("/stocks/thymeleaf", wellFormedHtmlAssertion()),
//            ),
//            Arguments.of(
//                RouteAndExpected("/stocks/kotlinx", wellFormedHtmlAssertion().replace("<!DOCTYPE html>", "")),
//            ),
//            Arguments.of(
//                RouteAndExpected("/stocks/htmlFlow", wellFormedHtmlAssertion()),
//            ),
//        )
//    }

    private fun getResponse(route: String): String {
        return RestAssured.given()
            .`when`()
            .get(URI.create(route))
            .then()
            .extract()
            .asString()
    }

    @AfterAll
    fun tearDown() {
        System.clearProperty("benchTimeout")
    }

    data class RouteAndExpected(val route: String?, val expected: String?)

    companion object {
        private fun trimLines(lines: String): String {
            val nl = "\n"
            return Arrays.stream<String>(
                lines
                    .replace("<", System.lineSeparator() + "<")
                    .replace(">", ">" + System.lineSeparator())
                    .split(nl.toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray(),
            )
                .map<String?> { line: String? -> line!!.trim { it <= ' ' }.replace("\t", "").lowercase(Locale.getDefault()) }
                .filter { line: String? -> !line!!.isEmpty() && !line.contains("stock prices") } // Skip title that is different for each template
                .collect(Collectors.joining(nl))
        }

        private fun wellFormedHtmlAssertion(): String {
            return """
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
    }
}
