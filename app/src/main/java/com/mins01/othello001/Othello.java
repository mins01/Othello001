package com.mins01.othello001;

//import android.util.Log;

import java.util.ArrayList;

/**
 * Created by mins01 on 2016-06-10.
 * 오델로 게임 데이터용 모델
 * 실제로 DB쓰는거 아니다. 아니야.
 */
public class Othello {
    public ArrayList<Stone> history;
    public Board board = new Board();
    public Othello(){
        history = new ArrayList<>();
        init();

    }

    public String msg = "";
    public int msg_int = 0;

    public void init(){
        // 판 초기화
        board.clear();
        // 히스토리 초기화
        history.clear();
    }

    public boolean putStone(int x,int y,int color){
        Stone stone = new Stone(x, y, color);
        this.history.add(stone);
        ArrayList<int[]> preTurnPos = board.putStone(stone);
        //Log.d("putStone",x+":"+y+":"+color);
        return preTurnPos.size()>0;
    }
    public void clear(){
        this.history.clear();;
        this.board.clear();
    }



    /**
     * 게임판
     */
    public class Board{
        public int[][] board = {
                {0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0},
                {0,0,0,0,0,0,0,0},
        };
        public Stone lastStone = null;
        public Board(){

        }
        public void clear(){
            //Log.d("clear","!");
            for(int i = 0,m=this.board.length;i<m;i++){
                for(int i2 = 0,m2=this.board[i].length;i2<m2;i2++) {
                    this.board[i][i2] = 0;
                }
            }
            this.board[3][3] = 1;
            this.board[4][4] = 1;
            this.board[3][4] = 2;
            this.board[4][3] = 2;

//            this.board[3][4] = 1;
//            this.board[4][3] = 2;
//            this.board[3][2] = 1;
//            this.board[4][2] = 1;
//            this.board[5][2] = 1;
//            this.board[5][3] = 1;
//            this.board[5][4] = 1;

            this.lastStone = new Stone(-1,-1,-1);
        }
        public String toString(){
            StringBuilder strb = new StringBuilder();
            strb.append("-:0,1,2,3,4,5,6,7,\n");

            for(int y = 0,m=this.board.length;y<m;y++){
                strb.append(y);
                strb.append(":");
                for(int x = 0,m2=this.board[y].length;x<m2;x++) {
                    strb.append(this.board[y][x]);
                    strb.append(",");
                }
                strb.append("\n");
            }

            return strb.toString();
        }
        public int[] getCount(){
            int[] cnts = {0,0,0};
            for(int y = 0,m=this.board.length;y<m;y++){
                for(int x = 0,m2=this.board[y].length;x<m2;x++) {
                    cnts[this.board[y][x]]++;

                }
            }
            return cnts;
        }

        public void setBoard(int[][] board){
            for(int y = 0,m=this.board.length;y<m;y++){
                System.arraycopy(board[y],0,this.board[y],0,board[y].length);
            }
        }
        public int[][] getBoard(){
            int[][] board = {
                    {0,0,0,0,0,0,0,0},
                    {0,0,0,0,0,0,0,0},
                    {0,0,0,0,0,0,0,0},
                    {0,0,0,0,0,0,0,0},
                    {0,0,0,0,0,0,0,0},
                    {0,0,0,0,0,0,0,0},
                    {0,0,0,0,0,0,0,0},
                    {0,0,0,0,0,0,0,0},
            };
            for(int y = 0,m=this.board.length;y<m;y++){
                System.arraycopy(this.board[y],0,board[y],0,this.board[y].length);
            }
            return board;
        }

        /**
         * 돌을 놓는다,
         * @param stone
         * @return 영향받는 좌표들
         */
        public ArrayList<int[]> putStone(Stone stone){
            //board[stone.y][stone.x] = stone.color;
            ArrayList<int[]> preTurnPos = getPreTurnPos(stone);
            if(preTurnPos.size()>0){

                //Log.d("돌 놓기",stone.toString());
                board[stone.y][stone.x] = stone.color;
                //Log.d("뒤집기-"+stone.color, preTurnPos.size()+":");
                lastStone.setStone(stone.x,stone.y,stone.color);
                for (int[] pos:preTurnPos
                     ) {
                    board[pos[1]][pos[0]] = stone.color;
                    //Log.d("반대돌뒤집기",pos[0]+":"+pos[1]+":"+ stone.color);
                }
            }
            return preTurnPos;
        }

        /**
         * 해당 색으로 놓을 수 있는 위치(힌팅)
         * @return
         */
        public ArrayList<Stone> getAblePos(int color){
            ArrayList<Stone> ablePos = new ArrayList<>();
            ArrayList<int[]> preTurnPos = new ArrayList<>();
            Stone stone = new Stone(-1,-1,color);
            for(int y = 0,m=this.board.length;y<m;y++){
                for(int x = 0,m2=this.board[y].length;x<m2;x++) {
                    if(this.board[y][x]==0){
                        stone.pos(x,y);
                        preTurnPos = getPreTurnPos(stone);
                        if(preTurnPos.size() > 0){
                            ablePos.add(new Stone(x,y,color));
                        }
                    }
                }
            }
            return ablePos;
        }
        /**
         * 지정 위치에 돌을 놓았을때 영향 받는 좌표들
         * @param stone
         * @return
         */
        public ArrayList<int[]> getPreTurnPos(Stone stone){
            ArrayList<int[]> preTurnPos = new ArrayList<>();
            // 위치에 놓인 돌은 없는가?
            if(board[stone.y][stone.x]!=0) return preTurnPos;
            // 현재 위치에 놓을 수 있는가?
            int[][] dirs = {{0,-1},{1,-1},{1,0},{1,1},{0,1},{-1,1},{-1,0},{-1,-1}};

            for (int[] dir:dirs ) {
                preTurnPos.addAll(getPreTurnPosDir(stone,dir));
               // Log.d("XXX",preTurnPos.toString());
            }
            return preTurnPos;
        }

        /**
         * 지정 반향으로 영향을 영향을 미치는가?
         * @param stone
         * @param dir
         * @return
         */
        public ArrayList<int[]> getPreTurnPosDir(Stone stone ,int[] dir){
            int[] curr = stone.toInts();
            int color = stone.color;
            int colorR = color==1?2:1;
            int[] pos = new int[2];
            int check1 = 0; //0:최초, 1:최초 접근 위치의 색이 다름을 통과, 2:같은 색이 나올때까지.
            ArrayList<int[]> preTurnPos = new ArrayList<>(); //뒤집어질 돌의 포지션

            curr[0]+=dir[0];
            curr[1]+=dir[1];
            int limit = 100;
            while (-1 < curr[0] && curr[0] < 8
                    && -1 < curr[1] && curr[1] < 8 && limit-- > 0 ){
//                Log.d("XXXX",curr[0]+":"+curr[1]);
                if(check1 == 0){
                    if(board[curr[1]][curr[0]] != colorR){ //본래 색과 반대색이 아님(즉, 같은색 또는 비어있음)
                        return preTurnPos;
                    }else{
                        check1 = 1;
                        pos = new int[2];
                        pos[0]=curr[0];
                        pos[1]=curr[1];
                        preTurnPos.add(pos);
//                        Log.d(" preTurnPos.add 1",pos[0]+":"+pos[1]);
                    }
                }else if(check1 == 1){
                    if(board[curr[1]][curr[0]] == colorR) {
                        pos = new int[2];
                        pos[0]=curr[0];
                        pos[1]=curr[1];
                        preTurnPos.add(pos);
//                        Log.d(" preTurnPos.add 2",pos[0]+":"+pos[1]);
                    }else if(board[curr[1]][curr[0]] == 0) {
                        break;
                    }else if(board[curr[1]][curr[0]] == color) {
                        check1 = 2;
                        break;
                    }
                }
                curr[0]+=dir[0];
                curr[1]+=dir[1];
            };
            
            if(check1!=2){
                preTurnPos.clear();
            }

            return preTurnPos;
        }

    }


}
