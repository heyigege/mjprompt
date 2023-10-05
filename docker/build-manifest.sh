#!/bin/bash
set -e -u -o pipefail

if [ $# -lt 1 ]; then
  echo 'version is required'
  exit 1
fi

VERSION=$1

echo "create manifest..."
docker manifest create heyigege/mjprompt:${VERSION} heyigege/mjprompt-amd64:${VERSION} heyigege/mjprompt-arm64v8:${VERSION}

echo "annotate amd64..."
docker manifest annotate heyigege/mjprompt:${VERSION} heyigege/mjprompt-amd64:${VERSION} --os linux --arch amd64

echo "annotate arm64v8..."
docker manifest annotate heyigege/mjprompt:${VERSION} heyigege/mjprompt-arm64v8:${VERSION} --os linux --arch arm64 --variant v8

echo "push manifest..."
docker manifest push heyigege/mjprompt:${VERSION}
