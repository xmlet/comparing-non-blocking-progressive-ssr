#!/bin/bash

cd ../../ || exit

./gradlew jmhJar > benches/jmh/gradle-build.log

java -Xms512M -Xmx16g -jar pssr-benchmark-view/build/libs/pssr-benchmark-view-1.0-SNAPSHOT-jmh.jar \
  -i 4 -wi 4 -f 1 -r 2s -w 2s \
  -rff results/results-jmh-stocks.csv \
  -rf csv -tu ms \
  stocks | tee benches/jmh/jmh-results.log

java -Xms512M -Xmx16g -jar pssr-benchmark-view/build/libs/pssr-benchmark-view-1.0-SNAPSHOT-jmh.jar \
  -i 4 -wi 4 -f 1 -r 2s -w 2s \
  -rff results/results-jmh-presentations.csv \
  -rf csv -tu ms \
  presentations | tee -a benches/jmh/jmh-results.log