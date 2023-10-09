#!/bin/bash
set -e -u -o pipefail

if [ $# -lt 1 ]; then
  echo 'version is required'
  exit 1
fi

VERSION=$1
ARCH=amd64

if [ $# -ge 2 ]; then
  ARCH=$2
fi

JAR_FILE_COUNT=$(find "../target/" -maxdepth 1 -name '*.jar' | wc -l)
if [ $JAR_FILE_COUNT == 0  ]; then
    echo "jar file not found, please execute: mvn clean package"
    exit 1
fi

JAR_FILE_NAME=$(ls ../target/*.jar|grep -v source)
echo ${JAR_FILE_NAME}

cp ${JAR_FILE_NAME} ./app.jar

docker build . -t mjprompt:${VERSION}

rm -rf  app.jar

docker tag mjprompt:${VERSION} registry.cn-beijing.aliyuncs.com/heyi_docker/mjprompt:${VERSION}
docker push  registry.cn-beijing.aliyuncs.com/heyi_docker/mjprompt:${VERSION}

docker tag mjprompt:${VERSION} heyigege/mjprompt-${ARCH}:${VERSION}
docker push heyigege/mjprompt-${ARCH}:${VERSION}
