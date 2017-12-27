#!/usr/bin/env bash

RASPBERRY_PI="pi@192.168.1.111"

gradle clean build
scp ./build/libs/CaClock.jar "$RASPBERRY_PI":~/srv/CaClock.jar
ssh "$RASPBERRY_PI" -t "sudo java -jar -Djava.library.path=/home/pi/test/rpiws28114j/src/main/java/de/cacodaemon/rpiws28114j/ ~/srv/CaClock.jar"