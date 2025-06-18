
# Set terminal and output
set terminal pngcairo enhanced font 'Arial,12' size 1200,800
set output 'benchmark_results.png'

# Configure the plot
set xlabel "Template Engine" font 'Arial,12'
set ylabel "Templates rendered per ms (ops/ms)" font 'Arial,12'

# Set data separator
set datafile separator ","

# Configure x-axis
set xtics rotate by -45
set xtics font 'Arial,10'

# Configure y-axis (linear scale like in the image)
unset logscale y
set yrange [0:*]
set format y "%.0f"

# Grid and styling
set grid ytics linetype 0 linewidth 0.5
set style fill solid 0.8
set boxwidth 0.6

# Use a single blue color like in the image
set style line 1 lc rgb '#4A90E2' lt 0 lw 0

###############################

# PRESENTATIONS

##############################

set title "Presentations" font 'Arial,16'
set output 'jmh_results_presentations.png'

stats 'results-jmh-presentations.csv' using 0 nooutput
set xrange [STATS_min : STATS_max + 1]

# Plot the data with error bars
plot 'results-jmh-presentations.csv' using 0:5:6:xtic(1) with boxerrorbars \
     linestyle 1 notitle, \
     '' using 0:5:(sprintf("%.0f", $5)) with labels offset 0,1 font 'Arial,10' notitle

###############################

# STOCKS

##############################

set title "Stocks" font 'Arial,16'
set output 'jmh_results_stocks.png'

plot 'results-jmh-stocks.csv' using 0:5:6:xtic(1) with boxerrorbars \
     linestyle 1 notitle, \
     '' using 0:5:(sprintf("%.0f", $5)) with labels offset 0,1 font 'Arial,10' notitle