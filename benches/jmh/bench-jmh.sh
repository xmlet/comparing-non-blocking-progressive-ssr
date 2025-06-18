cd ../../ || exit

./gradlew benchJMHStocks -Dorg.gradle.jvmargs="-Xms512M -Xmx16g" | tee benches/jmh/jmh-results.log

./gradlew benchJMHPresentations -Dorg.gradle.jvmargs="-Xms512M -Xmx16g" | tee -a benches/jmh/jmh-results.log