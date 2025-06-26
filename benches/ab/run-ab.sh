#!/bin/bash

REQUESTS=20
THREADS=(1 25 50 100 200 500 1000 2500)
ITERATIONS=2

for ths in "${THREADS[@]}"; do   # For each number of threads
  for path in "$@"; do           # For each path in line argument
    for ((n=0;n<$ITERATIONS;n++)); do
      total_requests=$((REQUESTS * ths))
      result=$(ab -q -s 240 -n $total_requests -c $ths http://localhost:8080/$path | grep "Requests per second")
      echo "$path::$ths:$result"
    done
  done
done