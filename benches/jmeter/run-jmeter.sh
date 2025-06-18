#!/bin/bash

for path in "$@"; do

  jmeter -n -t bench-plan.jmx -Jendpoint="$path" -l results-tmp.csv > jmeter.log

  awk -F',' '
  NR > 1 {
    label=$3
    if (label != "Warmup") {
        start_time[label] = (start_time[label] == "" || $1 < start_time[label]) ? $1 : start_time[label]
        end_time[label] = ($1 + $2 > end_time[label]) ? $1 + $2 : end_time[label]
        count[label]++
    }
  }
   END {
      for (label in count) {
        duration_sec = (end_time[label] - start_time[label]) / 1000
        throughput = (duration_sec > 0) ? count[label] / duration_sec : "NA"
        labels_and_samples[label] = count[label]
        label_info[label] = sprintf("%s: %.2f req/s (%d samples over %.2f sec)", label, throughput, count[label], duration_sec)
      }

      PROCINFO["sorted_in"] = "@val_num_asc"
      for (label in labels_and_samples) {
        print label_info[label]
      }
    }
  ' results-tmp.csv

  rm results-tmp.csv
done
