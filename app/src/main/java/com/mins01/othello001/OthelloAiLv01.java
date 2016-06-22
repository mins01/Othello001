package com.mins01.othello001;

//import android.util.Log;

import java.util.ArrayList;

/**
 * Created by mins01 on 2016-06-10.
 * 오델로 AI
 * 실제로 DB쓰는거 아니다. 아니야.
 */
public class OthelloAiLv01 implements OthelloAiImpl {
   public String aiName = "AI_LV_01";

   @Override
   public Stone getNextStone(Othello othGame,int color){
      ArrayList<Stone> ablePos = othGame.board.getAblePos(color);
      if (ablePos.size() > 0) {

         int idx = (int) Math.floor(Math.random() * (ablePos.size()));
         return ablePos.get(idx);
      } else {
//         Log.d("autoNext", "END");

      }
      return null;

   }

   @Override
   public String getAiName() {
      return aiName;
   }

}
