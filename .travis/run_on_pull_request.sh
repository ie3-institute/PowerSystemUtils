#!/usr/bin/env sh

chmod u+x gradlew
echo "Building PR $TRAVIS_PULL_REQUEST."
echo "Skipping integration tests in pull request builds."
./gradlew --refresh-dependencies clean spotlessCheck pmdMain pmdTest spotbugsMain spotbugsTest allTests jacocoTestReport jacocoTestCoverageVerification -x integrationTest
