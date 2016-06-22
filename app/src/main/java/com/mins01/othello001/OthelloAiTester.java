package com.mins01.othello001;

import android.media.SoundPool;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.ads.AdView;

import java.util.ArrayList;

/**
 * Created by mins01 on 2016. 6. 22..
 */
public class OthelloAiTester {

    ArrayList<View> boxes = null;
    //--
    private int color = 1; //0:없음,1:흑,2:
    //-- 세팅값
    private int blackUser = 0; //0:user 1:AI
    private int whiteUser = 2; //0:user 1:AI LV1 , 2:LV2
    private int autoHint = 1;
    private int useSound = 1;
    private int gameing = 0; //게임 진핸중인가?
    Handler handlerAI = null;
    AdView mAdView = null;

    private String[] stoneLabel = {"empty", "black", "white"};

    private TextView[] gameMsgs = {null,null,null};

    private Othello othGame = new Othello();

    private SoundPool soundPool = null;
    private int soundId_tick = 0;
    private int soundId_cheer = 0;
    private int soundId_wow = 0;
    private int streamId = 0;


    OthelloAiImpl ai_b;
    OthelloAiImpl ai_w;
    public static void main(String args[]) {
        System.out.println("테스트");
        OthelloAiTester tester = new OthelloAiTester();
        tester.init_main();

    }

    public void init_main(){
        ai_b = new OthelloAiLv01();
        ai_w = new OthelloAiLv01();
        othGame = new Othello();

        //handlerAI = new android.os.Handler();
        setGameMsg("init_main");
        startGame();
    }


    private String lastGameMsg = "";
    public void setGameMsg(String msg) {
//        gameMsgs[2].setText(gameMsgs[1].getText());
//        gameMsgs[1].setText(gameMsgs[0].getText());
//        gameMsgs[0].setText(msg);
        System.out.println(msg);
    }

    /**
     * 게임 최초 시작
     */
    public void startGame() {
        gameing = 1;
        stopActionAI();
        setGameMsg("");
        setGameMsg("");
//        setGameMsg(getString(R.string.othello_start_game));
        othGame.board.clear();

        color = 1;
        drawBoard();
//        return;
//        turnEnd();
        //printTest();
        actionAI();
    }

    public void gameover() {
        gameing = 0;
        stopActionAI();
//        Toast.makeText(this, "게임오버", Toast.LENGTH_LONG).show();
//        showDialogForGameover();
//        setGameMsg(getString(R.string.othello_gameover));
        int[] cnts = othGame.board.getCount();
        setGameMsg("RESULT : ");


        setGameMsg("EMPTY : "+cnts[0]);
        setGameMsg("BLACK : "+cnts[1]);
        setGameMsg("WHITE : "+cnts[2]);
        setGameMsg("GAME OVER");


    }

    public void stopActionAI(){
        //handlerAI.removeCallbacksAndMessages(null);
    }
    public void actionAI() {
        setGameMsg("actionAI-0");
        if(gameing==0){
            setGameMsg("actionAI-fail");
            return;
        }
        setGameMsg("actionAI-1");
        if ((color == 1 )
                || (color == 2)
                ) {

            stopActionAI();
            callAI();
//            setGameMsg(getString(R.string.othello_actionAI));
//            handlerAI.postDelayed(
//                    new Runnable() {
//                        @Override
//                        public void run() {
//                            callAI();
//                        }
//                    }
//                    , 1000);
        }
    }

    /**
     * 턴을 넘김
     */
    public void turnEnd() {
        ArrayList<Stone> ablePos = null;
        if (color == 1) {
            ablePos = othGame.board.getAblePos(2);
        } else if (color == 2) {
            ablePos = othGame.board.getAblePos(1);
        }
        if (ablePos.size() == 0) { //더이상 놓을 수 없네.
//            setGameMsg(getString(R.string.othello_turnend_0,stoneLabel[color == 1 ? 2 : 1]));
//            setGameMsg(getString(R.string.othello_turnend_1,stoneLabel[color]));
//            Log.d("turnEnd","turnEnd");

            if (color == 1) {
                ablePos = othGame.board.getAblePos(1);
            } else if (color == 2) {
                ablePos = othGame.board.getAblePos(2);
            }
            if (ablePos.size() == 0) { //스킵해도 더이상 놓을 수 없네.
                gameover();
            }
        } else {
            color = color == 1 ? 2 : 1; //돌 색 바꿈
        }
        actionAI();
    }

    public void drawBoard() {
        int[][] board = othGame.board.board;
        Stone lastStone = othGame.board.lastStone;
        System.out.println(othGame.board.toString());


        syncInfo();
    }
    public void syncInfo() {

    }
    public void callAI(){
        setGameMsg("callAI-0");
        int lv = color == 1?blackUser:whiteUser; //0이면 사람, 그이상이면 AI;
        Stone stone = null;
        if(color==1){
            setGameMsg("callAI-0-1");
            stone = ai_b.getNextStone(this.othGame,color);
        }else if(color==2){
            setGameMsg("callAI-2");
            stone = ai_w.getNextStone(this.othGame,color);
        }
        putStone(stone.x, stone.y, stone.color);
        drawBoard();
    }

    public void putStone(int x, int y, int color) {
        boolean b = othGame.putStone(x, y, color);

        setGameMsg("onclickBox-putStone"+ othGame.msg);
//        Log.d("onclickBox-putStone", othGame.msg);
        if (b) {
            if( x==0 && y==0 ||
                    x==0 && y==7 ||
                    x==7 && y==0 ||
                    x==7 && y==7
                    ){
//                playSoundWow();
            }else{
//                playSoundTick();
            }

//            setGameMsg(getString(R.string.othello_putstone_true, x + 1, y + 1, stoneLabel[color]));
            setGameMsg( (x + 1)+","+(y + 1)+","+color);
            turnEnd();
        } else {
//            setGameMsg(getString(R.string.othello_putstone_false, x + 1, y + 1, stoneLabel[color]));

        }
    }
}




