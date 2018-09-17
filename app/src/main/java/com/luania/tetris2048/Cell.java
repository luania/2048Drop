package com.luania.tetris2048;

public class Cell {
    private int x;
    private int y;
    private int num;

    public Cell(int x, int y, int num) {
        this.x = x;
        this.y = y;
        this.num = num;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void left() {
        this.x --;
    }
    public void right() {
        this.x++;
    }

    public void left(int num) {
        this.x -= num;
    }
    public void right(int num) {
        this.x += num;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}
