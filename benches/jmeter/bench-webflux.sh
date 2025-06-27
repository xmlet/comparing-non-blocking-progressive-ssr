#!/bin/bash

cd ../../ || exit

./gradlew build > benches/ab/gradle-build.log 2>&1 &
PID_GRADLE=$!

while ! grep -qE 'BUILD SUCCESSFUL|BUILD FAILED' benches/ab/gradle-build.log; do
  if ! ps -p $PID_GRADLE > /dev/null; then
    echo "Gradle build process has terminated unexpectedly."
    exit 1
  fi
  sleep 1
done

java -Xms512M -Xmx16g -DbenchTimeout=5 -jar pssr-benchmark-spring-webflux/build/libs/pssr-benchmark-spring-webflux-1.0-SNAPSHOT.jar > benches/jmeter/spring-webflux.log &
PID_WEBFLUX=$!

cd benches/jmeter || exit

sleep 1
while ! grep -m1 'Netty started on port 8080' < spring-webflux.log; do
    sleep 1
done

ROUTES=(
     presentations/thymeleaf
  #   presentations/thymeleaf/sync
     presentations/thymeleaf/virtualSync
     presentations/htmlFlow
     presentations/htmlFlow/suspending
     presentations/htmlFlow/sync
     presentations/htmlFlow/virtualSync
  #   presentations/kotlinx
  #   presentations/kotlinx/sync
     presentations/kotlinx/virtualSync
     presentations/rocker/sync
     presentations/rocker/virtualSync
     presentations/jstachio/sync
     presentations/jstachio/virtualSync
  #   presentations/pebble/sync
     presentations/pebble/virtualSync
  #   presentations/freemarker/sync
     presentations/freemarker/virtualSync
  #   presentations/trimou/sync
     presentations/trimou/virtualSync
  #   presentations/velocity/sync
#     presentations/velocity/virtualSync
     presentations/thymeleaf
     stocks/thymeleaf
  #   stocks/thymeleaf/sync
     stocks/thymeleaf/virtualSync
     stocks/htmlFlow
     stocks/htmlFlow/suspending
     stocks/htmlFlow/sync
     stocks/htmlFlow/virtualSync
#     stocks/kotlinx
  #   stocks/kotlinx/sync
     stocks/kotlinx/virtualSync
     stocks/rocker/sync
     stocks/rocker/virtualSync
     stocks/jstachio/sync
     stocks/jstachio/virtualSync
  #   stocks/pebble/sync
     stocks/pebble/virtualSync
  #   stocks/freemarker/sync
     stocks/freemarker/virtualSync
  #   stocks/trimou/sync
     stocks/trimou/virtualSync
  #   stocks/velocity/sync
#     stocks/velocity/virtualSync
)


echo ":::::::::::::::::::::::::::::::     Spring running PID = $PID_WEBFLUX"

echo "##########################################"
echo "############# RUN BENCH ##################"
echo "##########################################"

./run-jmeter.sh "${ROUTES[@]}" | tee webflux-results.log

# Gracefully terminate the Spring Boot application when running on local machine.
# It will send a SIGTERM corresponding to Exit code 143.
if [ "$GH" != "true" ]; then
  kill "$PID_WEBFLUX"

  # Wait for the process to exit
  wait $PID_WEBFLUX
fi