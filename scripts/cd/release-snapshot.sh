#!/bin/bash -e

pushd "${BASH_SOURCE%/*}/../.."

JAVA_HOME=$(/usr/libexec/java_home -v 1.8)
export JAVA_HOME="${JAVA_HOME}"

echo "Releasing SNAPSHOT..."
./mvnw -Prelease deploy

