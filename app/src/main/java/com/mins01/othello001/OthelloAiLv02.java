package com.mins01.othello001;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by mins01 on 2016-06-10.
 * 오델로 AI : 본인이 놓은 위치에 대해서만 제일 높은 점수 위치 찾기 (1턴만 체크함)
 */
public class OthelloAiLv02 implements OthelloAiImpl {
    private int[][] boardScore = {
           {100,-50,1,1,1,1,-50,100},
           {-50,-50,1,1,1,1,-50,-50},
           {5  ,1  ,5,1,1,1,5  ,5  },
           {1  ,1  ,1,1,1,1,1  ,1  },
           {1  ,1  ,1,1,1,1,1  ,1  },
           {5  ,1  ,5,1,1,1,5  ,5  },
           {-50,-50,1,1,1,1,-50,-50},
           {100,-50,1,1,1,1,-50,100},
    };
    private Othello othtemp = null;
    public OthelloAiLv02(){
      othtemp = new Othello();
    }
    public Stone getNextStone(Othello othGame,int color){
        Log.d("OthelloAiLv02","getNextStone");
        ArrayList<Stone> ablePos = othGame.board.getAblePos(color);
        int score = -1;
        if (ablePos.size() > 0) {
            ArrayList<ScoreWithStone> tmpArr = new ArrayList<>(ablePos.size());

            for(Stone stone:ablePos){
               score = simulatePutSone(othGame.board.getBoard(),stone);
                tmpArr.add((new ScoreWithStone(score,stone)));
            }
//            Log.e("소팅전",tmpArr.get(0).stone.toString());
            Collections.sort(tmpArr, new Comparator<ScoreWithStone>(){
                public int compare(ScoreWithStone obj1, ScoreWithStone obj2)
                {
                    return obj2.score-obj1.score ;
                }
            });
//            Log.e("소팅후",tmpArr.get(0).stone.toString());
            //-- 같은 최고 점수를 다시 뽑아내내다.
            int maxScore = tmpArr.get(0).score;
            ArrayList<ScoreWithStone> randArr = new ArrayList<>();
            for(ScoreWithStone t:tmpArr){
                if(t.score == maxScore){
                    randArr.add(t);
                }
            }
            return randArr.get((int)Math.floor(Math.random()*randArr.size())).stone;
        } else {
        }
        Log.d("getNextStone",othGame.board.toString());

        return null;
    }
    public int simulatePutSone(int[][] board,Stone stone){

        othtemp.board.setBoard(board);
        othtemp.board.putStone(stone);
        int score = getScore(othtemp.board.getBoard(),stone.color);
        return score;
    }
    public int getScore(int[][] board,int color){
        int score = 0;
        int[][] boardScore = getScoreBoard();
        //꼭지점을 차지하였을땐 점수표가 바뀐다.
        if(board[0][0]==color){
            boardScore[1][0] = 5;
            boardScore[0][1] = 5;
        }
        if(board[0][7]==color){
            boardScore[6][0] = 5;
            boardScore[1][7] = 5;
        }
        if(board[7][0]==color){
            boardScore[0][6] = 5;
            boardScore[7][1] = 5;
        }
        if(board[7][7]==color){
            boardScore[7][6] = 5;
            boardScore[6][7] = 5;
        }
        for(int y=0,m=board.length;y<m;y++){
            for(int x=0,m2=board[y].length;x<m2;x++){
                if(board[y][x] == color){
                    score+=boardScore[y][x];
                }
            }
        }
        return score;
    }
    public int[][] getScoreBoard(){
        int[][] boardScore = {
                {0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0},
        };
        for(int y = 0,m=this.boardScore.length;y<m;y++){
            System.arraycopy(this.boardScore[y],0,boardScore[y],0,this.boardScore[y].length);
        }
        return boardScore;
    }
    class ScoreWithStone{
        int score = 0;
        Stone stone = null;
        public ScoreWithStone(int score,Stone stone){
            this.score = score;
            this.stone = stone;
        }

    }
}
