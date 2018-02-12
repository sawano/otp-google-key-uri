#!/bin/bash -e

pushd "${BASH_SOURCE%/*}/../.."

echo "Executing Maven command in: '$PWD'"

BUILD_IMAGE="sawano/ubuntu-openjdk8:0.0.1"

docker run -i --rm -v $PWD:/opt/javabuild "${BUILD_IMAGE}" /bin/bash -c "cd /opt/javabuild; ./mvnw clean verify"
