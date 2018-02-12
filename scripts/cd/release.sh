#!/bin/bash -e

pushd "${BASH_SOURCE%/*}/../.."

JAVA_HOME=$(/usr/libexec/java_home -v 1.8)
export JAVA_HOME="${JAVA_HOME}"

echo "Releasing new version..."
./mvnw -Prelease release:clean
./mvnw -Prelease release:prepare
./mvnw -Prelease release:perform
