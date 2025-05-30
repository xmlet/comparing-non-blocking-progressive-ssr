cd ../../ || exit

./gradlew runMVC -DbenchTimeout=1 -Dorg.gradle.jvmargs="-Xms512M -Xmx16g" > ../benches/jmeter/spring-mvc.log &

PID_GRADLE=$!

cd benches/jmeter || exit

sleep 1
while ! grep -m1 'Tomcat started on port 8080' < spring-mvc.log; do
    sleep 1
done

PID_MVC=$(grep -oP 'with PID \K[0-9]+' spring-mvc.log)

echo ":::::::::::::::::::::::::::::::     Spring running PID = $PID_MVC"
echo ":::::::::::::::::::::::::::::::     Gradle running PID = $PID_GRADLE"

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
kill $PID_GRADLE
kill "$PID_MVC"

# Wait for the process to exit
wait $PID_GRADLE


cd ../../ || exit

./gradlew runMVCVirtual -DbenchTimeout=1 -Dorg.gradle.jvmargs="-Xms512M -Xmx16g" > ../benches/jmeter/spring-mvc.log &

PID_GRADLE=$!

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
  kill $PID_GRADLE
  kill "$PID_MVC"

  # Wait for the process to exit
  wait $PID_GRADLE
fi

