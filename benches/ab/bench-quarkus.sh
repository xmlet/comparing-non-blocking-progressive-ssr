cd ../../ || exit

./gradlew runQuarkus -DbenchTimeout=1 -Dorg.gradle.jvmargs="-Xms512M -Xmx16g" -Djdk.tracePinnedThreads > ../benches/ab/quarkus.log &
PID_GRADLE=$!

cd benches/ab || exit

sleep 1
while ! grep -m1 'Starting Quarkus Application' < quarkus.log; do
    sleep 1
done

PID_QUARKUS=$(grep -oP 'on PID \K[0-9]+' quarkus.log)

echo ":::::::::::::::::::::::::::::::     Gradle running PID = $PID_GRADLE"
echo ":::::::::::::::::::::::::::::::     Quarkus running PID = $PID_QUARKUS"

#
# Define routes for benchmark
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
   presentations/reactive/htmlFlow
   stocks/rocker
   stocks/jstachio
   stocks/pebble
   stocks/freemarker
   stocks/trimou
#   stocks/velocity
   stocks/thymeleaf
   stocks/htmlFlow
   stocks/kotlinx
   stocks/reactive/htmlFlow
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
./run-ab.sh "${ROUTES[@]}" | tee quarkus-results.log


# Gracefully terminate the Spring Boot application.
# It will send a SIGTERM corresponding to Exit code 143.
kill $PID_GRADLE
kill $PID_QUARKUS

# Wait for the process to exit
wait $PID_GRADLE


echo ":::::::::::::::::::::::::::::::     Sync Bench Done"

cd ../../ || exit

./gradlew runQuarkusVirtual -Dorg.gradle.jvmargs="-Xms512M -Xmx16g" -DbenchTimeout=1 > ../benches/ab/quarkus.log &
PID_GRADLE=$!

cd benches/ab || exit

sleep 1
while ! grep -m1 'Starting Quarkus Application' < quarkus.log; do
    sleep 1
done

PID_QUARKUS=$(grep -oP 'on PID \K[0-9]+' quarkus.log)

echo ":::::::::::::::::::::::::::::::     Gradle running PID = $PID_GRADLE"
echo ":::::::::::::::::::::::::::::::     Quarkus running PID = $PID_QUARKUS"

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
./run-ab.sh "${ROUTES[@]}" | tee quarkus-results-virtual.log

if [ "$GH" != "true" ]; then
  kill $PID_GRADLE
  kill $PID_QUARKUS

  # Wait for the process to exit
  wait $PID_GRADLE
fi

echo ":::::::::::::::::::::::::::::::     Virtual Bench Done"