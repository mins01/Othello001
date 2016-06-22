package com.mins01.othello001;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //-- 광고관련
        MobileAds.initialize(getApplicationContext(), "ca-app-pub-5804424351292113~9445730601");
        mAdView = (AdView) findViewById(R.id.adView);
        //--테스트용
//        AdRequest.Builder adRequest = new AdRequest.Builder().addTestDevice("8E96431A849F374C14CA8979AAC2902E");
//        AdRequest aaa = adRequest.build();
//        mAdView.loadAd(aaa);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        //-- 라벨관련
        stoneLabel[0] = getString(R.string.othello_stone_label_0);
        stoneLabel[1] = getString(R.string.othello_stone_label_1);
        stoneLabel[2] = getString(R.string.othello_stone_label_2);

        //-- 사운드 관련
        initSoundPool();
        //-- 초기화
        init();

    }


    /**
     * Verify device's API before to load soundpool
     * @return
     */
    @SuppressWarnings("deprecation")
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void initSoundPool() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();

            soundPool = new SoundPool.Builder()
                    .setMaxStreams(3)
                    .setAudioAttributes(audioAttributes)
                    .build();
        } else {
            soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        }
        soundId_tick = soundPool.load(this,R.raw.tick,1);
        soundId_cheer = soundPool.load(this,R.raw.cheer2,1);
        soundId_wow = soundPool.load(this,R.raw.wow,1);

    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onStart() {
        super.onStart();


    }

    @Override
    protected void onResume() {
        super.onResume();
        mAdView.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mAdView.pause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAdView.destroy();
    }

    public void printTest() {
        TextView testText = (TextView) findViewById(R.id.testText);
        assert testText != null;
        testText.setText(othGame.board.toString());
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_show_setting_dialog:
                Log.d("onClick", "button_show_setting_dialog");
                showDialogForSetting();
                break;
            case R.id.button_change_color_black:
                Log.d("onClick", "button_change_color_black");
                this.color = 1;
                syncInfo();
                break;
            case R.id.button_change_color_white:
                Log.d("onClick", "button_change_color_white");
                this.color = 2;
                syncInfo();
                break;
            case R.id.button_auto_next:
                autoNext();
                break;
            case R.id.buttonClear:
                Log.d("onClick", "buttonClear");
                Log.d("clear", othGame.board.toString());
                othGame.board.clear();
//                othGame.putStone(5,3,1);
                Log.d("clear", othGame.board.toString());
                break;
            case R.id.buttonAblePos:
                Log.d("onClick", "buttonAblePos");
//                othGame.board.clear();
                ArrayList<Stone> ablePos = othGame.board.getAblePos(1);
                Log.d("ablePos.size()", ablePos.size() + ":");
                for (Stone stone : ablePos) {
                    Log.d("buttonAblePos", stone.toString());
                }
                Log.d("clear", othGame.board.toString());
                break;
            case R.id.button_restart:
                Log.d("onClick", "button_restart");
                startGame();
                break;
            case R.id.button_gameover:
                Log.d("onClick", "button_gameover");
                showDialogForGameover();
                break;
            case R.id.btn_test_set_board:
                Log.d("onClick", "btn_test_set_board");
                int[][] board = {
                        {0,0,0,0,0,0,0,0},
                        {0,0,0,0,0,0,0,0},
                        {0,0,0,0,0,0,0,0},
                        {0,0,0,0,0,0,0,0},
                        {0,0,0,0,0,0,0,0},
                        {0,0,0,0,0,0,0,0},
                        {2,0,0,0,0,0,0,0},
                        {1,2,0,0,0,0,0,0},
                };
                othGame.board.setBoard(board);
                drawBoard();
                break;
            case R.id.btn_test_ai:
                Log.d("onClick", "btn_test_ai");
                OthelloAiLv02 ai = new OthelloAiLv02();
                Stone stone = ai.getNextStone(this.othGame,color);
                //putStone(stone.x, stone.y, stone.color);
                break;
            case R.id.btn_hint:
                showHint();
                break;
        }

    }

    private static ArrayList<View> getViewsByClassName(ViewGroup root, String className) {
        ArrayList<View> views = new ArrayList<View>();
        final int childCount = root.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = root.getChildAt(i);
            if (child instanceof ViewGroup) {
                views.addAll(getViewsByClassName((ViewGroup) child, className));
            }

            if (child.getClass().getName().toString().indexOf(className) != -1) {
                views.add(child);
            }
//            final Object tagObj = child.getTag();
//            if (tagObj != null && tagObj.equals(tag)) {
//                views.add(child);
//            }

        }
        return views;
    }

    public void init() {
        gameMsgs[0] =(TextView) findViewById(R.id.game_msg_0);
        gameMsgs[1] =(TextView) findViewById(R.id.game_msg_1);
        gameMsgs[2] =(TextView) findViewById(R.id.game_msg_2);
        handlerAI = new android.os.Handler();
        TableLayout othello_board_table = (TableLayout) findViewById(R.id.main_othello_board);
        boxes = this.getViewsByClassName(othello_board_table, "FrameLayout");
        Log.d("Boxes.size", boxes.size() + ":");
        initSetTagForBoxes(boxes);
        settingDialog = createDialogForSetting();
        gameoverDialog = createDialogForGameover();
        startGame();


    }

    private void initSetTagForBoxes(ArrayList<View> boxes) {
        int i = 0;
        for (View box : boxes) {
            int x = i % 8;
            int y = (int) Math.floor(i / 8);
            box.setTag((new HolderForBox(x, y, box, box.findViewById(R.id.viewStone))));
            i++;
        }
    }

    /**
     * 게임 최초 시작
     */
    public void startGame() {
        gameing = 1;
        stopActionAI();
        setGameMsg("");
        setGameMsg("");
        setGameMsg(getString(R.string.othello_start_game));
        othGame.clear();

        color = 1;
        drawBoard();
//        turnEnd();
        //printTest();
        actionAI();
    }

    public void gameover() {
        gameing = 0;
        stopActionAI();
//        Toast.makeText(this, "게임오버", Toast.LENGTH_LONG).show();
        showDialogForGameover();
        setGameMsg(getString(R.string.othello_gameover));
    }

    public void stopActionAI(){
        handlerAI.removeCallbacksAndMessages(null);
    }
    public void actionAI() {
        if(gameing==0){
            return;
        }
        if ((color == 1 && blackUser > 0)
                || (color == 2 && whiteUser > 0)
                ) {

            stopActionAI();
            setGameMsg(getString(R.string.othello_actionAI));
            handlerAI.postDelayed(
                    new Runnable() {
                        @Override
                        public void run() {
                            callAI();
                        }
                    }
                    , 1000);
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
            setGameMsg(getString(R.string.othello_turnend_0,stoneLabel[color == 1 ? 2 : 1]));
            setGameMsg(getString(R.string.othello_turnend_1,stoneLabel[color]));
            Log.d("turnEnd","turnEnd");
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

    /**
     * 칸을 눌렸을때 동작시킴.
     *
     * @param
     */
    public void onclickBox(View box) {
        if(color ==1 && blackUser>0){
            return;
        }else if(color ==2 && whiteUser>0){
            return;
        }

        HolderForBox hfb = (HolderForBox) box.getTag();
        Log.d("onclickBox", hfb.x + ":" + hfb.y);
        putStone(hfb.x, hfb.y, color);

        drawBoard();
    }

    public void putStone(int x, int y, int color) {
        boolean b = othGame.putStone(x, y, color);


        Log.d("onclickBox-putStone", othGame.msg);
        if (b) {
            if( x==0 && y==0 ||
                x==0 && y==7 ||
                x==7 && y==0 ||
                x==7 && y==7
                    ){
                playSoundWow();
            }else{
                playSoundTick();
            }

            setGameMsg(getString(R.string.othello_putstone_true, x + 1, y + 1, stoneLabel[color]));
            turnEnd();
        } else {
            setGameMsg(getString(R.string.othello_putstone_false, x + 1, y + 1, stoneLabel[color]));

        }
    }
    public void stopSound(){
        if(streamId>0) {
            soundPool.stop(streamId);
        }
    }
    public void playSoundTick_4(){
        if(useSound==0){
            return;
        }
        stopSound();
        streamId = soundPool.play(soundId_tick, 1.0F, 1.0F,  1,  4,  1.0F);
    }
    public void playSoundTick(){
        if(useSound==0){
            return;
        }
        stopSound();
        streamId = soundPool.play(soundId_tick, 1.0F, 1.0F,  1,  0,  1.0F);
    }
    public void playSoundCheer(){
        if(useSound==0){
            return;
        }
        stopSound();
        streamId = soundPool.play(soundId_cheer, 1.0F, 1.0F,  1,  0,  1.0F);
    }
    public void playSoundWow(){
        if(useSound==0){
            return;
        }
        stopSound();
        streamId = soundPool.play(soundId_wow, 1.0F, 1.0F,  1,  0,  1.0F);
    }

    /**
     * 힌트 보이기
     */
    public void showHint(){
        ArrayList<Stone> ablePos = othGame.board.getAblePos(color);
        View box = null;
        for(Stone stone:ablePos){
//            Log.d("xxxxxx",stone.x+stone.y*8+" ");
            box = boxes.get(stone.x+stone.y*8);
            box.setBackgroundResource(R.drawable.othello_board_box_hint);
        }
    }


    public void drawBoard() {
        int[][] board = othGame.board.board;
        HolderForBox hfb = null;
        Stone lastStone = othGame.board.lastStone;
        for (View box : boxes) {
            hfb = (HolderForBox) box.getTag();
            TextView tv = ((TextView) hfb.viewStone);
            int res = 0;
            switch (board[hfb.y][hfb.x]) {

                case 1:
                    res = R.drawable.othello_stone_black;
                    break;
                case 2:
                    res = R.drawable.othello_stone_white;
                    break;
                case 0:
                default:
                    res = R.drawable.othello_stone_empty;
                    break;
            }
            tv.setBackgroundResource(res);

//            if(board[hfb.y][hfb.x]==0){
//                tv.setText("");
//            }else{
//                tv.setText("["+board[hfb.y][hfb.x]+"]");
//            }
//            tv.setText("["+hfb.x+","+hfb.y+"]");
            if (lastStone.x == hfb.x && lastStone.y == hfb.y) {
                box.setBackgroundResource(R.drawable.othello_board_box_last);
            } else {
                box.setBackgroundResource(R.drawable.othello_board_box);
            }
        }
        Log.e("autoHint",autoHint+":");
        if(autoHint==1){
            showHint();
        }


        syncInfo();
    }
    public void callAI(){
        int lv = color == 1?blackUser:whiteUser; //0이면 사람, 그이상이면 AI;
        Stone stone = null;
        switch (lv){
            case 1:
                OthelloAiLv01 ai = new OthelloAiLv01();
                stone = ai.getNextStone(this.othGame,color);
                putStone(stone.x, stone.y, stone.color);
                break;
            case 2:
                OthelloAiLv02 ai02 = new OthelloAiLv02();
                stone = ai02.getNextStone(this.othGame,color);
                putStone(stone.x, stone.y, stone.color);
                break;
        }
        drawBoard();
    }
    /**
     * 놓을 수 있는 랜덤 위치로.
     */
    public void autoNext() {
        ArrayList<Stone> ablePos = othGame.board.getAblePos(color);
        if (ablePos.size() > 0) {

            int idx = (int) Math.floor(Math.random() * (ablePos.size()));
            putStone(ablePos.get(idx).x, ablePos.get(idx).y, color);
            drawBoard();
        } else {
            Log.d("autoNext", "END");
        }

    }

    public void syncInfo() {
        if (color == 1) {

            (findViewById(R.id.linear_info_black)).setBackgroundResource(R.drawable.othello_info_selected);
            (findViewById(R.id.linear_info_white)).setBackgroundResource(R.drawable.othello_stone_empty);
        } else {
            (findViewById(R.id.linear_info_black)).setBackgroundResource(R.drawable.othello_stone_empty);
            (findViewById(R.id.linear_info_white)).setBackgroundResource(R.drawable.othello_info_selected);
        }
        int[] cnts = othGame.board.getCount();
        ((TextView) findViewById(R.id.count_black)).setText(cnts[1] + "");
        ((TextView) findViewById(R.id.count_white)).setText(cnts[2] + "");
    }

    //----------AlertDialog
    public AlertDialog settingDialog = null;
    public AlertDialog gameoverDialog = null;

    public AlertDialog createDialogForSetting() {
        AlertDialog.Builder ab = new AlertDialog.Builder(this);
        final View innerView = getLayoutInflater().inflate(R.layout.dialog_setting, null);

        ((Switch) innerView.findViewById(R.id.switch_hint)).setOnCheckedChangeListener(
                new Switch.OnCheckedChangeListener(){

                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        MainActivity.this.onCheckedChanged(buttonView, isChecked);
                    }
                }
        );
        ((Switch) innerView.findViewById(R.id.switch_use_sound)).setOnCheckedChangeListener(
                new Switch.OnCheckedChangeListener(){

                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        MainActivity.this.onCheckedChanged(buttonView, isChecked);
                    }
                }
        );

        ((Spinner)innerView.findViewById(R.id.spinner_user_1)).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

                MainActivity.this.onCheckedChanged(parent, view, position,id);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        ((Spinner)innerView.findViewById(R.id.spinner_user_2)).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

                MainActivity.this.onCheckedChanged(parent,view,  position,id);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        ab.setTitle(getString(R.string.othello_setting));
        ab.setView(innerView);
        ab.setPositiveButton("Restart", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                startGame();
                setDismiss(settingDialog);
            }
        });
//
        ab.setNegativeButton("close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                setDismiss(settingDialog);
            }
        });

        return ab.create();
    }

    public void showDialogForSetting() {
        Log.d("showDialogForSetting", blackUser + ":" + whiteUser);
        settingDialog.show();

        ((Switch) settingDialog.findViewById(R.id.switch_hint)).setChecked(this.autoHint==1);
        ((Switch) settingDialog.findViewById(R.id.switch_use_sound)).setChecked(this.useSound==1);
        ((Spinner) settingDialog.findViewById(R.id.spinner_user_1)).setSelection(this.blackUser);
        ((Spinner) settingDialog.findViewById(R.id.spinner_user_2)).setSelection(this.whiteUser);
    }

    //-- 게임오버용
    public AlertDialog createDialogForGameover() {
        AlertDialog.Builder ab = new AlertDialog.Builder(this);
        final View innerView = getLayoutInflater().inflate(R.layout.dialog_gameover, null);


        ab.setTitle(getString(R.string.othello_result));
        ab.setView(innerView);
        ab.setNegativeButton("close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                stopSound();
                setDismiss(gameoverDialog);
            }
        });

        return ab.create();
    }

    public void showDialogForGameover() {
        Log.d("showDialogForSetting", blackUser + ":" + whiteUser);
        playSoundCheer();
        gameoverDialog.show();
        int[] cnts = othGame.board.getCount();

        ((TextView) gameoverDialog.findViewById(R.id.count_black)).setText(String.format("%d", cnts[1]));
        ((TextView) gameoverDialog.findViewById(R.id.count_white)).setText(String.format("%d", cnts[2]));
        int result = 0;
        if (cnts[1] > cnts[2]) {
            result = 1;
        } else if (cnts[1] < cnts[2]) {
            {
                result = 2;
            }
        }
        String msg = null;
        View temp0 = gameoverDialog.findViewById(R.id.dialog_gameover_stone_0);
        View temp1 = gameoverDialog.findViewById(R.id.dialog_gameover_stone_1);
        View temp2 = gameoverDialog.findViewById(R.id.dialog_gameover_stone_2);
        switch (result) {
            case 0:
                msg = getString(R.string.othello_result_0);
                temp0.setVisibility(View.VISIBLE);
                temp1.setVisibility(View.GONE);
                temp2.setVisibility(View.GONE);
                break;
            case 1:
                msg = getString(R.string.othello_result_1);
                temp0.setVisibility(View.GONE);
                temp1.setVisibility(View.VISIBLE);
                temp2.setVisibility(View.GONE);

                break;
            case 2:
                msg = getString(R.string.othello_result_2);
                temp0.setVisibility(View.GONE);
                temp1.setVisibility(View.GONE);
                temp2.setVisibility(View.VISIBLE);
                break;
        }
        ((TextView) gameoverDialog.findViewById(R.id.dialog_gameover_text01)).setText(msg);
    }

    private void setDismiss(Dialog dialog) {
        if (dialog != null && dialog.isShowing())
            dialog.dismiss();
    }
    public void onCheckedChanged(View parent, View view, int position, long id) { // 스피너
        Log.e("onCheckedChanged",parent.getId()+","+position+","+id+","+R.id.spinner_user_2);
        switch (parent.getId()) {
            case R.id.spinner_user_1:
                this.blackUser = position;
                break;
            case R.id.spinner_user_2:
                this.whiteUser = position;
                break;
        }
        drawBoard();
    }
    public void onCheckedChanged(CompoundButton arg0, boolean arg1) { // 스위치버튼
        switch (arg0.getId()) {
            case R.id.switch_hint:
                this.autoHint = arg1?1:0;
                break;
            case R.id.switch_use_sound:
                this.useSound = arg1?1:0;
                break;

        }
        drawBoard();
    }
    private String lastGameMsg = "";
    public void setGameMsg(String msg) {
        gameMsgs[2].setText(gameMsgs[1].getText());
        gameMsgs[1].setText(gameMsgs[0].getText());
        gameMsgs[0].setText(msg);
    }
    //-- 메뉴 관련
    //-- 메뉴관련
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.developer) {
            Toast.makeText(this, getString(R.string.menu_developer_msg), Toast.LENGTH_SHORT).show();
            Uri uri = Uri.parse("http://www.mins01.com");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
            return true;
        } else if (id == R.id.app_version) {
        PackageInfo pInfo;
        try {
            pInfo = getPackageManager().getPackageInfo(
                    this.getPackageName(), 0);
            int versionCode = pInfo.versionCode;
            String versionName = pInfo.versionName;
            Toast.makeText(this, "Version " + versionName + " (" + versionCode + ")", Toast.LENGTH_SHORT).show();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return true;
        }else if (id == R.id.readme) {
            AlertDialog.Builder ab = new AlertDialog.Builder(this);
            ab.setTitle("Readme");
            ab.setMessage(getString(R.string.readme_text));
            ab.setPositiveButton("ok", null);
            ab.show();

        }

        return super.onOptionsItemSelected(item);
    }

    //--광고관련
    private void initAdMob() {
        AdRequest adRequest = new AdRequest.Builder().build();
//        adView.loadAd(adRequest);
    }


    private long backKeyPressedTime = 0;
    private Toast toast;

    public void onBackPressed() {
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            showGuide();
            return;
        }
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            this.finish();
            toast.cancel();
        }
    }
    public void showGuide() {
        showToast(getString(R.string.exit_press_once_more));
    }
    public void showToast(String msg){
        if(toast!=null){
            toast.cancel();
        }

        toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
        toast.show();
    }

    class HolderForBox {
        public int x = -1;
        public int y = -1;
        public View viewStone = null;
        public View box = null;

        public HolderForBox(int x, int y, View box, View viewStone) {
            this.x = x;
            this.y = y;
            this.box = box;
            this.viewStone = viewStone;
        }
    }

}
