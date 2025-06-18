#!/bin/bash

process_csv() {
    local input_file="$1"
    local output_file="$2"

    awk -F',' 'BEGIN { OFS = "," }
    NR == 1 {
        print $0
        next
    }
    {
        gsub(/^"|"$/, "", $1)  # Remove leading/trailing quotes
        if ($1 ~ /^benchmark\./) {
            split($1, parts, ".")
            if (length(parts) == 3) {
                $1 = parts[2]
            }
        }
        $1 = "\"" $1 "\""  # Re-wrap in quotes to preserve CSV format
        print
    }' "$input_file" > "$output_file"
}

process_csv "results-jmh-presentations.csv" "results-jmh-presentations.tmp"
process_csv "results-jmh-stocks.csv" "results-jmh-stocks.tmp"

mv "results-jmh-presentations.tmp" "results-jmh-presentations.csv"
mv "results-jmh-stocks.tmp" "results-jmh-stocks.csv"

gnuplot plots/jmh.plot
