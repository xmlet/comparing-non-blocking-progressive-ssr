#!/usr/bin/gnuplot

set terminal pngcairo size 1000,600 enhanced font 'Verdana,10'
set output 'presentations-quarkus.png'

set datafile separator ","

set style data histogram
set style histogram clustered gap 1
set style fill solid border -1
set boxwidth 0.9

set title "Presentations Results - Quarkus"
set xlabel "Approach"
set ylabel "req/sec"
set grid ytics

set key outside right top vertical Left reverse noenhanced autotitle columnhead

plot "presentations-quarkus.csv" using 2:xtic(1) title columnheader(2), \
     for [i=3:*] '' using i title columnheader(i)

set output 'stocks-quarkus.png'
set title "Stocks Results - Quarkus"
set xlabel "Approach"
plot "stocks-quarkus.csv" using 2:xtic(1) title columnheader(2), \
     for [i=3:*] '' using i title columnheader(i)