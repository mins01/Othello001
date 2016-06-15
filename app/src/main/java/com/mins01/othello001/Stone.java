package com.mins01.othello001;

/**
 * Created by mins01 on 2016-06-10.
 * 돌을 지정하는거다.
 */
public class Stone {
    public int x = -1;
    public int y = -1;
    public int color = -1;
    public Stone(int x ,int y , int color){
        setStone(x, y, color);
    }

    /**
     *
     * @param x position X (0~7) , -1 = skip
     * @param y position Y (0~7) , -1 = skip
     * @param color : 0= empty, 1 = block , 2 = white
     */
    public void setStone(int x ,int y , int color){
        pos(x,y);
        this.color = color;
    }
    public void pos(int x, int y){
        this.x = x;
        this.y = y;
    }

    public int[] toInts(){
        int[] v = {x,y,color};
        return v;
    }
    public String toString(){
        return x+","+y+","+color;
    }
}