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

java -Xms512M -Xmx16g -DbenchTimeout=5 -Dspring.threads.virtual.enabled=false -jar pssr-benchmark-spring-mvc/build/libs/pssr-benchmark-spring-mvc-1.0-SNAPSHOT.jar > benches/jmeter/spring-mvc.log &

cd benches/jmeter || exit

sleep 1
while ! grep -m1 'Tomcat started on port 8080' < spring-mvc.log; do
    sleep 1
done

PID_MVC=$(grep -oP 'with PID \K[0-9]+' spring-mvc.log)

echo ":::::::::::::::::::::::::::::::     Spring running PID = $PID_MVC"

ROUTES=(
   presentations/rocker
   presentations/jstachio
   presentations/pebble
   presentations/freemarker
   presentations/trimou
#   presentations/velocity
   presentations/thymeleaf
   presentations/htmlFlow
   presentations/kotlinx
   stocks/rocker
   stocks/jstachio
   stocks/pebble
   stocks/freemarker
   stocks/trimou
#   stocks/velocity
   stocks/thymeleaf
   stocks/htmlFlow
   stocks/kotlinx
)

echo "##########################################"
echo "############# RUN BENCH ##################"
echo "##########################################"
./run-jmeter.sh "${ROUTES[@]}" | tee spring-mvc-results.log

# Gracefully terminate the Spring Boot application.
# It will send a SIGTERM corresponding to Exit code 143.
kill "$PID_MVC"

# Wait for the process to exit
wait "$PID_MVC"

cd ../../ || exit

java -Xms512M -Xmx16g -DbenchTimeout=5 -Dspring.threads.virtual.enabled=true -jar pssr-benchmark-spring-mvc/build/libs/pssr-benchmark-spring-mvc-1.0-SNAPSHOT.jar > benches/jmeter/spring-mvc.log &

cd benches/jmeter || exit

sleep 1
while ! grep -m1 'Tomcat started on port 8080' < spring-mvc.log; do
    sleep 1
done

PID_MVC=$(grep -oP 'with PID \K[0-9]+' spring-mvc.log)

echo ":::::::::::::::::::::::::::::::     Spring running PID = $PID_MVC"
echo ":::::::::::::::::::::::::::::::     Gradle running PID = $PID_GRADLE"

echo "##########################################"
echo "############# RUN BENCH ##################"
echo "##########################################"
./run-jmeter.sh "${ROUTES[@]}" | tee spring-mvc-virtual-results.log

# Gracefully terminate the Spring Boot application when running on local machine.
# It will send a SIGTERM corresponding to Exit code 143.
if [ "$GH" != "true" ]; then
  kill "$PID_MVC"

  # Wait for the process to exit
  wait $PID_MVC
fi

