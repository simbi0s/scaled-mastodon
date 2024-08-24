#!/bin/bash


cd post-gateway || exit

docker build -t post-gateway .

if [ $? -eq 0 ]; then
  echo "======== Post-gateway build successful. ========"
else
  echo "======== Post-gateway build failed. ========"
  exit 1
fi

cd ../mconnector || exit

docker build -t mconnector .

if [ $? -eq 0 ]; then
  echo "======== Mconnector build successful. ========"
else
  echo "======== Mconnector build failed. ========"
  exit 1
fi
