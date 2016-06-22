package com.mins01.othello001;

/**
 * Created by mins01 on 2016-06-10.
 * 오델로 AI
 * 실제로 DB쓰는거 아니다. 아니야.
 */
public interface OthelloAiImpl {
//   public String aiName = "";
   public Stone getNextStone(Othello othGame,int color);
   public String getAiName();
}
