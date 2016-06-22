#!/bin/bash
# sh java_OthelloAiTest.sh com.mins01.othello001.OthelloAiLv01 com.mins01.othello001.OthelloAiLv02 10
cd /tmp/Othello001
#java com.mins01.othello001.OthelloAiTester com.mins01.othello001.OthelloAiLv01 com.mins01.othello001.OthelloAiLv01
java com.mins01.othello001.OthelloAiTester $1 $2 $3
cd -