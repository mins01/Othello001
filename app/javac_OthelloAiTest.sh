#!/bin/bash
if [ -d "/tmp/Othello001" ]
then
    echo "to /tmp/Othello001"
else
    mkdir /tmp/Othello001
fi
javac -d /tmp/Othello001 ./src/main/java/com/mins01/othello001/Stone.java ./src/main/java/com/mins01/othello001/Othello*.java -Xlint:unchecked
sh ./java_OthelloAiTest.sh