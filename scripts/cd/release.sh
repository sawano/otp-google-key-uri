#!/bin/bash -e

pushd "${BASH_SOURCE%/*}/../.."

JAVA_HOME=$(/usr/libexec/java_home -v 1.8)
export JAVA_HOME="${JAVA_HOME}"

echo "Releasing new version..."
./mvnw release:clean
./mvnw release:prepare
./mvnw release:perform
