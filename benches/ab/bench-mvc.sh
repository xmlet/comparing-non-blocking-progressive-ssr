cd ../../ || exit

./gradlew runMVC -DbenchTimeout=1 -Dorg.gradle.jvmargs="-Xms512M -Xmx16g" > ../benches/ab/spring-mvc.log &
PID_GRADLE=$!

cd benches/ab || exit

sleep 1
while ! grep -m1 'Tomcat started on port 8080' < spring-mvc.log; do
    sleep 1
done

echo ":::::::::::::::::::::::::::::::     Gradle running PID = $PID_GRADLE"

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
kill $PID_GRADLE

# Wait for the process to exit
wait $PID_GRADLE

echo ":::::::::::::::::::::::::::::::     Sync Bench Done"

cd ../../ || exit

./gradlew runMVCVirtual -DbenchTimeout=1 -Dorg.gradle.jvmargs="-Xms512M -Xmx16g" > ../benches/ab/spring-mvc.log &
PID_GRADLE=$!

cd benches/ab || exit

sleep 1
while ! grep -m1 'Tomcat started on port 8080' < spring-mvc.log; do
    sleep 1
done

echo ":::::::::::::::::::::::::::::::     Gradle running PID = $PID_GRADLE"

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
  kill $PID_GRADLE

  # Wait for the process to exit
  wait $PID_GRADLE
fi

echo ":::::::::::::::::::::::::::::::     Virtual Bench Done"