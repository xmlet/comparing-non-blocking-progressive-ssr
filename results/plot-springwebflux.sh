#!/bin/bash

INPUT="webflux-results.log"

PRESENTATIONS_CSV="presentations-webflux.csv"
STOCKS_CSV="stocks-webflux.csv"

TMP_PRESENTATIONS=$(mktemp)
TMP_STOCKS=$(mktemp)

extract_data() {
  local file="$1"

  while IFS= read -r line; do
    [[ -z "$line" ]] && continue

    route=$(echo "$line" | cut -d':' -f1)
    concurrency=$(echo "$line" | cut -d':' -f3 | awk '{print $1}')
    req_per_sec=$(echo "$line" | grep -oP '[0-9.]+(?= req/s)')

    if [[ "$route" == *"/htmlFlow/suspending"* ]]; then
      label_out="HtmlFlow-Susp"
    elif [[ "$route" == *"/jstachio/virtualSync"* ]]; then
      label_out="JStachio-Virtual"
    elif [[ "$route" == *"/thymeleaf" ]]; then
      label_out="Thymeleaf-Rx"
    elif [[ "$route" == *"/sync"* ]]; then
      label_out="Blocking"
    elif [[ "$route" == *"/virtualSync"* ]]; then
      label_out="Virtual"
    else
      continue
    fi

    engine=$(echo "$route" | cut -d'/' -f2)

    if [[ "$route" == presentations/* ]]; then
      echo "$engine,$concurrency,$req_per_sec,$label_out" >> "$TMP_PRESENTATIONS"
    elif [[ "$route" == stocks/* ]]; then
      echo "$engine,$concurrency,$req_per_sec,$label_out" >> "$TMP_STOCKS"
    fi
  done < "$file"
}

extract_data "$INPUT"

./aggregate.sh "$TMP_PRESENTATIONS" "$PRESENTATIONS_CSV"
./aggregate.sh "$TMP_STOCKS" "$STOCKS_CSV"

rm "$TMP_PRESENTATIONS" "$TMP_STOCKS"

echo "Generated wide format CSV files:"
echo "- $PRESENTATIONS_CSV"
echo "- $STOCKS_CSV"

gnuplot plots/webflux.plot

echo "Generated plots using gnuplot."