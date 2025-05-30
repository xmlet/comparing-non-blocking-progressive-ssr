#!/bin/bash

INPUT_BLOCKING="spring-mvc-results.log"
INPUT_VIRTUAL="spring-mvc-virtual-results.log"

PRESENTATIONS_CSV="presentations-mvc.csv"
STOCKS_CSV="stocks-mvc.csv"

TMP_PRESENTATIONS=$(mktemp)
TMP_STOCKS=$(mktemp)

extract_data() {
  local file="$1"
  local label="$2"

  while IFS= read -r line; do
    [[ -z "$line" ]] && continue

    route=$(echo "$line" | cut -d':' -f1)
    concurrency=$(echo "$line" | cut -d':' -f3 | awk '{print $1}')
    req_per_sec=$(echo "$line" | grep -oP '[0-9.]+(?= req/s)')

    # Extract engine from route
    engine=$(echo "$route" | cut -d'/' -f2)

    if [[ "$route" == presentations/* ]]; then
      echo "$engine,$concurrency,$req_per_sec,$label" >> "$TMP_PRESENTATIONS"
    elif [[ "$route" == stocks/* ]]; then
      echo "$engine,$concurrency,$req_per_sec,$label" >> "$TMP_STOCKS"
    fi
  done < "$file"
}

extract_data "$INPUT_BLOCKING" "Blocking"
extract_data "$INPUT_VIRTUAL" "Virtual"

./aggregate.sh "$TMP_PRESENTATIONS" "$PRESENTATIONS_CSV"
./aggregate.sh "$TMP_STOCKS" "$STOCKS_CSV"

# Clean up
rm "$TMP_PRESENTATIONS" "$TMP_STOCKS"

echo "Generated wide format CSV files:"
echo "- $PRESENTATIONS_CSV"
echo "- $STOCKS_CSV"

gnuplot plots/mvc.plot
echo "Generated plots using gnuplot."