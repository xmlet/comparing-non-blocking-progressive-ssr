package benchmark.repository

import benchmark.model.Stock
import io.reactivex.rxjava3.core.Observable

class StockRepositoryMem(
    private val timeout: Long,
) : StockRepository {
    private val stocksList = mutableListOf<Stock>()
    private val stocksReactive = Observable.fromIterable(stocksList)

    override fun findAllReactive(): Observable<Stock> =
        if (timeout == 0L) {
            stocksReactive
        } else {
            stocksReactive.interleaved(timeout)
        }

    override fun findAllIterable(): Iterable<Stock> =
        if (timeout == 0L) {
            stocksList
        } else {
            stocksReactive.interleaved(timeout).blockingIterable()
        }

    init {
        stocksList.add(Stock("Adobe Systems", "Adobe Systems Inc.", "http://www.adobe.com", "ADBE", 39.26, 0.13, 0.33))
        stocksList.add(Stock("Advanced Micro Devices", "Advanced Micro Devices Inc.", "http://www.amd.com", "AMD", 16.22, 0.17, 1.06))
        stocksList.add(Stock("Amazon.com", "Amazon.com Inc", "http://www.amazon.com", "AMZN", 36.85, -0.23, -0.62))
        stocksList.add(Stock("Apple", "Apple Inc.", "http://www.apple.com", "AAPL", 85.38, -0.87, -1.01))
        stocksList.add(Stock("BEA Systems", "BEA Systems Inc.", "http://www.bea.com", "BEAS", 12.46, 0.09, 0.73))
        stocksList.add(Stock("CA", "CA, Inc.", "http://www.ca.com", "CA", 24.66, 0.38, 1.57))
        stocksList.add(Stock("Cisco Systems", "Cisco Systems Inc.", "http://www.cisco.com", "CSCO", 26.35, 0.13, 0.5))
        stocksList.add(Stock("Dell", "Dell Corp.", "http://www.dell.com/", "DELL", 23.73, -0.42, -1.74))
        stocksList.add(Stock("eBay", "eBay Inc.", "http://www.ebay.com", "EBAY", 31.65, -0.8, -2.47))
        stocksList.add(Stock("Google", "Google Inc.", "http://www.google.com", "GOOG", 495.84, 7.75, 1.59))
        stocksList.add(Stock("Hewlett-Packard", "Hewlett-Packard Co.", "http://www.hp.com", "HPQ", 41.69, -0.02, -0.05))
        stocksList.add(Stock("IBM", "International Business Machines Corp.", "http://www.ibm.com", "IBM", 97.45, -0.06, -0.06))
        stocksList.add(Stock("Intel", "Intel Corp.", "http://www.intel.com", "INTC", 20.53, -0.07, -0.34))
        stocksList.add(Stock("Juniper Networks", "Juniper Networks, Inc", "http://www.juniper.net/", "JNPR", 18.96, 0.5, 2.71))
        stocksList.add(Stock("Microsoft", "Microsoft Corp", "http://www.microsoft.com", "MSFT", 30.6, 0.15, 0.49))
        stocksList.add(Stock("Oracle", "Oracle Corp.", "http://www.oracle.com", "ORCL", 17.15, 0.17, 1.1))
        stocksList.add(Stock("SAP", "SAP AG", "http://www.sap.com", "SAP", 46.2, -0.16, -0.35))
        stocksList.add(Stock("Seagate Technology", "Seagate Technology", "http://www.seagate.com/", "STX", 27.35, -0.36, -1.3))
        stocksList.add(Stock("Sun Microsystems", "Sun Microsystems Inc.", "http://www.sun.com", "SUNW", 6.33, -0.01, -0.16))
        stocksList.add(Stock("Yahoo", "Yahoo! Inc.", "http://www.yahoo.com", "YHOO", 28.04, -0.17, -0.6))
    }
}
