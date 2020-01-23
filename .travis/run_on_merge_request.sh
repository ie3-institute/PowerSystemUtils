#!/usr/bin/env sh

chmod u+x gradlew
echo "Merge PR into master. Calling all tests + creating reports."
./gradlew --refresh-dependencies clean spotlessCheck pmdMain pmdTest spotbugsMain spotbugsTest allTests jacocoTestReport jacocoTestCoverageVerification
