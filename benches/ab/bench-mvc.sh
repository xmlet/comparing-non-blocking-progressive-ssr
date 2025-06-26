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

java -Xms512M -Xmx16g -DbenchTimeout=5 -Dspring.threads.virtual.enabled=false -jar pssr-benchmark-spring-mvc/build/libs/pssr-benchmark-spring-mvc-1.0-SNAPSHOT.jar > benches/ab/spring-mvc.log &
PID_SPRING=$!

echo "Starting Spring MVC application with PID $PID_SPRING"

cd benches/ab || exit

sleep 1
while ! grep -m1 'Tomcat started on port 8080' < spring-mvc.log; do
    sleep 1
done

echo ":::::::::::::::::::::::::::::::     Spring running PID = $PID_SPRING"

#
# Define routes for benchmark
#
#
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

#
# Warm up all paths in 3 iterations each.
#
echo "##########################################"
echo "############# WARM UP ####################"
echo "##########################################"
for path in "${ROUTES[@]}"; do
  ab -n 1000 -c 32 http://localhost:8080/$path
done

#
# Run Bench
#
echo "##########################################"
echo "############# RUN BENCH ##################"
echo "##########################################"
./run-ab.sh "${ROUTES[@]}" | tee spring-mvc-results.log


# Gracefully terminate the Spring Boot application.
# It will send a SIGTERM corresponding to Exit code 143.
kill $PID_SPRING

# Wait for the process to exit
wait $PID_SPRING

echo ":::::::::::::::::::::::::::::::     Sync Bench Done"

cd ../../ || exit

java -Xms512M -Xmx16g -DbenchTimeout=5 -Dspring.threads.virtual.enabled=true -jar pssr-benchmark-spring-mvc/build/libs/pssr-benchmark-spring-mvc-1.0-SNAPSHOT.jar > benches/ab/spring-mvc.log &
PID_SPRING=$!

cd benches/ab || exit

sleep 1
while ! grep -m1 'Tomcat started on port 8080' < spring-mvc.log; do
    sleep 1
done

echo ":::::::::::::::::::::::::::::::     Spring running PID = $PID_SPRING"

#
# Warm up all paths in 3 iterations each.
#
echo "##########################################"
echo "############# WARM UP ####################"
echo "##########################################"
for path in "${ROUTES[@]}"; do
  ab -n 1000 -c 32 http://localhost:8080/$path
done

#
# Run Bench
#
echo "##########################################"
echo "############# RUN BENCH ##################"
echo "##########################################"
./run-ab.sh "${ROUTES[@]}" | tee spring-mvc-virtual-results.log

if [ "$GH" != "true" ]; then
  kill $PID_SPRING

  # Wait for the process to exit
  wait $PID_SPRING
fi

echo ":::::::::::::::::::::::::::::::     Virtual Bench Done"