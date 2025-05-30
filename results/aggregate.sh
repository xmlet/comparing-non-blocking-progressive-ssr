#!/bin/bash

tmpfile="$1"
output_csv="$2"

awk -F',' '
{
  key = $4 "," $2
  sum[key] += $3
  count[key] += 1
}
END {
  for (k in sum) {
    avg = sum[k] / count[k]
    split(k, parts, ",")
    label = parts[1]
    concurrency = parts[2]
    print label "," concurrency "," avg
  }
}' "$tmpfile" | sort -t',' -k2n > "${tmpfile}.agg"

awk -F',' '
BEGIN {
  delete concurrency_levels
  delete labels
}
{
  data[$2][$1] = $3
  concurrency_levels[$2] = $2
  labels[$1] = 1
}
END {
  n_conc = asorti(concurrency_levels, conc_sorted, "@val_num_asc")
  n_labels = asorti(labels, label_sorted)

  # Transpose: Approaches as rows, concurrency levels as columns
  printf "Approach"
  for (i = 1; i <= n_conc; i++) {
    printf ",%s", conc_sorted[i]
  }
  printf "\n"

  for (j = 1; j <= n_labels; j++) {
    label = label_sorted[j]
    printf "%s", label
    for (i = 1; i <= n_conc; i++) {
      conc = conc_sorted[i]
      if (data[conc][label] != "") {
        printf ",%s", data[conc][label]
      } else {
        printf ",0"
      }
    }
    printf "\n"
  }
}' "${tmpfile}.agg" > "$output_csv"

rm "${tmpfile}.agg"