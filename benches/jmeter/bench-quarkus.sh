#!/bin/bash

cd ../.. || exit

./gradlew build > benches/ab/gradle-build.log 2>&1 &
PID_GRADLE=$!

while ! grep -qE 'BUILD SUCCESSFUL|BUILD FAILED' benches/ab/gradle-build.log; do
  if ! ps -p $PID_GRADLE > /dev/null; then
    echo "Gradle build process has terminated unexpectedly."
    exit 1
  fi
  sleep 1
done

java -Xms512M -Xmx16g -DbenchTimeout=5 -Dquarkus.virtual-threads.enabled=false -jar pssr-benchmark-quarkus/build/libs/pssr-benchmark-quarkus-1.0-SNAPSHOT-all.jar > benches/jmeter/quarkus.log &

cd benches/jmeter || exit

sleep 1
while ! grep -m1 'Starting Quarkus Application' < quarkus.log; do
    sleep 1
done

PID_QUARKUS=$(grep -oP 'on PID \K[0-9]+' quarkus.log)

echo ":::::::::::::::::::::::::::::::     Gradle running PID = $PID_GRADLE"
echo ":::::::::::::::::::::::::::::::     Quarkus running PID = $PID_QUARKUS"

ROUTES=(
  presentations/rocker
  presentations/jstachio
  presentations/pebble
  presentations/freemarker
  presentations/trimou
#  presentations/velocity
  presentations/thymeleaf
  presentations/htmlFlow
  presentations/kotlinx
  presentations/reactive/htmlFlow
  stocks/rocker
  stocks/jstachio
  stocks/pebble
  stocks/freemarker
  stocks/trimou
#  stocks/velocity
  stocks/thymeleaf
  stocks/htmlFlow
  stocks/kotlinx
  stocks/reactive/htmlFlow
)

#
# Run Bench
#
echo "##########################################"
echo "############# RUN BENCH ##################"
echo "##########################################"
./run-jmeter.sh "${ROUTES[@]}" | tee quarkus-results.log


# Gracefully terminate the Quarkus application.
# It will send a SIGTERM corresponding to Exit code 143.
kill $PID_QUARKUS

# Wait for the process to exit
wait $PID_QUARKUS


echo ":::::::::::::::::::::::::::::::     Sync Bench Done"

cd ../../ || exit

java -Xms512M -Xmx16g -DbenchTimeout=5 -Dquarkus.virtual-threads.enabled=true -jar pssr-benchmark-quarkus/build/libs/pssr-benchmark-quarkus-1.0-SNAPSHOT-all.jar > benches/jmeter/quarkus.log &

cd benches/jmeter || exit

sleep 1
while ! grep -m1 'Starting Quarkus Application' < quarkus.log; do
    sleep 1
done

PID_QUARKUS=$(grep -oP 'on PID \K[0-9]+' quarkus.log)

echo ":::::::::::::::::::::::::::::::     Gradle running PID = $PID_GRADLE"
echo ":::::::::::::::::::::::::::::::     Quarkus running PID = $PID_QUARKUS"

#
# Run Bench
#
echo "##########################################"
echo "############# RUN BENCH ##################"
echo "##########################################"
./run-jmeter.sh "${ROUTES[@]}" | tee quarkus-results-virtual.log

if [ "$GH" != "true" ]; then
  kill $PID_QUARKUS

  # Wait for the process to exit
  wait $PID_QUARKUS
fi

echo ":::::::::::::::::::::::::::::::     Virtual Bench Done"