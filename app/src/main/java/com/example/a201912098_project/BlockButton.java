package com.example.a201912098_project;

import android.content.Context;
import android.graphics.Color;
import android.widget.Button;

class BlockButton extends Button {
    public BlockButton(Context context, int x, int y){
        super(context);
        this.x = x;
        this.y = y;
        this.mine = false;
        this.flag = false;
        this.isNeighborMines = 0;
        this.blocks += 1;
    };

    private int x = 0;
    private int y = 0;
    private boolean mine;
    private boolean flag;
    private int isNeighborMines;
    private static int flags = 10;
    private static int blocks = -flags;

    public int get_X() {
        return this.x;
    }

    public int get_Y() {
        return this.y;
    }

    public static int getBlocks() {
        return blocks;
    }

    public int getFlags() {
        return flags;
    }

    public boolean isFlag() {
        return this.flag;
    }

    public boolean isMine() {
        return this.mine;
    }

    public void setMine(boolean mine) {
        this.mine = mine;
    }

    public int getIsNeighborMines() {
        return isNeighborMines;
    }

    public void setIsNeighborMines(int isNeighborMines) {
        this.isNeighborMines = isNeighborMines;
    }

    public void toggleFlag() {
        if(flag) {
            setText("");
            this.flags += 1;
        }
        else {
            setText("+");
            this.flags -= 1;
        }
        this.flag = !flag;
    }
    public boolean breakBlock() {
        this.blocks -= 1;
        if (mine) {
            return true;
        }
        else {
            this.setClickable(false);
            setBackgroundColor(Color.WHITE);
            if(isNeighborMines == 0)
                setText("");
            else
                setText("" + isNeighborMines);
            return false;
        }
    }
}