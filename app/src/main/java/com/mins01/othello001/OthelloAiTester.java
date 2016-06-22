package com.mins01.othello001;


import java.util.ArrayList;

/**
 * AI테스트용파일
 * Created by mins01 on 2016. 6. 22..
 */
public class OthelloAiTester {


    //--
    private int color = 1; //0:없음,1:흑,2:
    //-- 세팅값
    private int blackUser = 0; //0:user 1:AI
    private int whiteUser = 2; //0:user 1:AI LV1 , 2:LV2
    private int autoHint = 1;
    private int useSound = 1;
    private int gameing = 0; //게임 진핸중인가?

    private String[] stoneLabel = {"empty", "black", "white"};

    private Othello othGame = new Othello();

    private int soundId_tick = 0;
    private int soundId_cheer = 0;
    private int soundId_wow = 0;
    private int streamId = 0;
    private ArrayList<Integer> results = null;


    OthelloAiImpl ai_b;
    OthelloAiImpl ai_w;
    public static void main(String args[]) throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        System.out.println("테스트");
        OthelloAiTester tester = new OthelloAiTester();
        try {
            tester.init_main(args);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }

    }

    public void init_main(String args[]) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        setGameMsg(args.length+":");
        results = new ArrayList<>();
        if(args.length >= 1 && args[0] != null){
            Class c1 = Class.forName(args[0]);
            setGameMsg("Black AI :"+args[0]);

            ai_b = (OthelloAiImpl) c1.newInstance();
        }else{
            ai_b = new OthelloAiLv01();
        }
        if(args.length >= 2 && args[1] != null){
            setGameMsg("White AI :"+args[1]);
            Class c2 = Class.forName(args[1]);
            ai_w = (OthelloAiImpl) c2.newInstance();
        }else{
            ai_w = new OthelloAiLv02();
        }

        int loop_cnt = 1;
        if(args.length >= 3 && args[2] != null){
//            setGameMsg(args[2]);
            loop_cnt = Integer.parseInt(args[2]);
        }

        othGame = new Othello();

        //handlerAI = new android.os.Handler();
        setGameMsg("loop : "+loop_cnt);
        while(loop_cnt-- > 0){
            startGame();
        }
        printResults();

    }
    public void printResults(){
        int[] cnt = {0,0,0};
        for(Integer result:results){
            cnt[(int)result]++;
        }
        setGameMsg("");
        setGameMsg("AI Black : "+ai_b.getClass().getSimpleName());
        setGameMsg("AI White : "+ai_w.getClass().getSimpleName());
        setGameMsg("Loop : "+results.size());
        setGameMsg("Draw : "+cnt[0]);
        setGameMsg("WIN black : "+cnt[1]);
        setGameMsg("WIN white : "+cnt[2]);

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
//        setGameMsg("");
//        setGameMsg("");
        othGame.clear();

        color = 1;
        drawBoard();
        actionAI();
    }

    public void gameover() {
        gameing = 0;
        stopActionAI();
//        Toast.makeText(this, "게임오버", Toast.LENGTH_LONG).show();
//        showDialogForGameover();
//        setGameMsg(getString(R.string.othello_gameover));
        int[] cnts = othGame.board.getCount();

//        setGameMsg("");
//        setGameMsg("GAME OVER");
//        setGameMsg("A.I : "+ai_b.getAiName()+" vs "+ai_w.getAiName());
        if(cnts[1]==cnts[2]){
//            setGameMsg("RESULT : DRAW");
            results.add(new Integer(0));
        }else if(cnts[1] > cnts[2]) {
//            setGameMsg("RESULT : WIN BLACK ("+ai_b.getAiName()+")");
            results.add(new Integer(1));
        }else{
//            setGameMsg("RESULT : WIN WHITE ("+ai_w.getAiName()+")");
            results.add(new Integer(2));
        }



//        setGameMsg("TURN : "+othGame.history.size());
//        setGameMsg("EMPTY : "+cnts[0]);
//        setGameMsg("BLACK : "+cnts[1]);
//        setGameMsg("WHITE : "+cnts[2]);
//        setGameMsg("GAME OVER");


    }

    public void stopActionAI(){
        //handlerAI.removeCallbacksAndMessages(null);
    }
    public void actionAI() {
//        setGameMsg("actionAI-0");
        if(gameing==0){
//            setGameMsg("actionAI-fail");
            return;
        }
//        setGameMsg("actionAI-1");
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
//        System.out.println(othGame.board.toString());


//        syncInfo();
    }
    public void syncInfo() {

    }
    public void callAI(){
//        setGameMsg("callAI-0");
        int lv = color == 1?blackUser:whiteUser; //0이면 사람, 그이상이면 AI;
        Stone stone = null;
        if(color==1){
//            setGameMsg("callAI-0-1");
            stone = ai_b.getNextStone(this.othGame,color);
        }else if(color==2){
//            setGameMsg("callAI-2");
            stone = ai_w.getNextStone(this.othGame,color);
        }
        putStone(stone.x, stone.y, stone.color);
//        drawBoard();
    }

    public void putStone(int x, int y, int color) {
        boolean b = othGame.putStone(x, y, color);
        drawBoard();

//        setGameMsg("putStone"+ othGame.msg);
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
//            setGameMsg( (x + 1)+","+(y + 1)+","+color);
            turnEnd();
        } else {
//            setGameMsg(getString(R.string.othello_putstone_false, x + 1, y + 1, stoneLabel[color]));

        }
    }
}




