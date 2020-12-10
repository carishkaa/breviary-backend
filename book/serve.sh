#!/usr/bin/env bash

cd $(dirname "$0")

if [ '!' -d node_modules ]
then
    npm install
    npm run gitbook-install
fi

if command -v inoticoming > /dev/null
then
    kill_them() {
        kill $processes
    }

    trap kill_them EXIT

    processes=
    inoticoming --foreground ..                --suffix .md touch book.js \; &
    processes="$processes $!"
    inoticoming --foreground ../docs           --suffix .md touch book.js \; &
    processes="$processes $!"
    npm run serve &
    processes="$processes $!"

    wait %1 %2 %3
else
    npm run serve
fi
