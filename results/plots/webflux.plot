#!/usr/bin/gnuplot

# Plot Presentations Results
set terminal pngcairo size 1000,600 enhanced font 'Verdana,10'
set output 'presentations-webflux.png'

set datafile separator ","

set style data histogram
set style histogram clustered gap 1
set style fill solid border -1
set boxwidth 1

set title "Presentations Results - Spring WebFlux"
set xlabel "Approach"
set ylabel "req/sec"
set grid ytics

set key title "Concurrent Users" outside right top vertical Left reverse noenhanced autotitle columnhead

set palette defined (1 '#e74c3c', 2 '#3498db', 3 '#2ecc71', 4 '#f39c12', 5 '#9b59b6')

plot "presentations-webflux.csv" using 2:xtic(1) title columnheader(2), \
     for [i=3:*] '' using i title columnheader(i)

# Plot Stocks Results
set output 'stocks-webflux.png'
set title "Stocks Results - Spring WebFlux"
set xlabel "Approach"

plot "stocks-webflux.csv" using 2:xtic(1) title columnheader(2), \
     for [i=3:*] '' using i title columnheader(i)