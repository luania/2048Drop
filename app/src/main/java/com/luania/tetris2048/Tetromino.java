package com.luania.tetris2048;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Tetromino {

    public static Tetromino random() {
        int type = (int) Math.round(Math.random() * 12);
        List<Cell> cells = new ArrayList<>();
        switch (type) {
            case 0:
                cells.add(new Cell(2, 1, 2));
                cells.add(new Cell(3, 1, 4));
                cells.add(new Cell(4, 1, 2));
                cells.add(new Cell(5, 1, 4));
                break;
            case 1:
                cells.add(new Cell(3, 0, 2));
                cells.add(new Cell(4, 0, 4));
                cells.add(new Cell(3, 1, 4));
                cells.add(new Cell(4, 1, 2));
                break;
            case 2:
                cells.add(new Cell(3, 0, 2));
                cells.add(new Cell(3, 1, 4));
                cells.add(new Cell(4, 1, 2));
                cells.add(new Cell(5, 1, 4));
                break;
            case 3:
                cells.add(new Cell(4, 0, 2));
                cells.add(new Cell(3, 1, 2));
                cells.add(new Cell(4, 1, 4));
                cells.add(new Cell(5, 1, 2));
                break;
            case 4:
                cells.add(new Cell(5, 0, 2));
                cells.add(new Cell(3, 1, 4));
                cells.add(new Cell(4, 1, 2));
                cells.add(new Cell(5, 1, 4));
                break;
            case 5:
                cells.add(new Cell(3, 0, 2));
                cells.add(new Cell(4, 0, 4));
                cells.add(new Cell(2, 1, 2));
                cells.add(new Cell(3, 1, 4));
                break;
            case 6:
                cells.add(new Cell(3, 0, 2));
                cells.add(new Cell(4, 0, 4));
                cells.add(new Cell(4, 1, 2));
                cells.add(new Cell(5, 1, 4));
                break;
            case 7:
                cells.add(new Cell(3, 1, 2));
                cells.add(new Cell(4, 1, 4));
                cells.add(new Cell(4, 0, 2));
                break;
            case 8:
                cells.add(new Cell(3, 1, 4));
                cells.add(new Cell(4, 1, 2));
                cells.add(new Cell(3, 0, 2));
                break;
            case 9:
                cells.add(new Cell(2, 1, 2));
                cells.add(new Cell(3, 1, 4));
                cells.add(new Cell(4, 1, 2));
                break;
            case 10:
                cells.add(new Cell(3, 1, 2));
                cells.add(new Cell(4, 1, 4));
                break;
            case 11:
                cells.add(new Cell(3, 0, 2));
                cells.add(new Cell(3, 1, 4));
                break;
            case 12:
                cells.add(new Cell(3, 1, 2));
                break;
            default:
                break;
        }
        Tetromino tetromino = new Tetromino(cells);
        tetromino.sort();
        return tetromino;
    }

    private List<Cell> cells;

    public Tetromino(List<Cell> cells) {
        this.cells = cells;
    }

    public List<Cell> getCells() {
        return cells;
    }

    public void setCells(List<Cell> cells) {
        this.cells = cells;
    }

    public void left() {
        boolean canDo = true;
        for(Cell cell: cells){
            if(cell.getX() == 0){
                canDo = false;
            }
        }
        if(canDo){
            for(Cell cell: cells){
                cell.left();
            }
        }
    }
    public void right(int limit) {
        boolean canDo = true;
        for(Cell cell: cells){
            if(cell.getX() == limit){
                canDo = false;
            }
        }
        if(canDo) {
            for (Cell cell : cells) {
                cell.right();
            }
        }
    }

    public void sort() {
        Collections.sort(cells, new Comparator<Cell>() {
            @Override
            public int compare(Cell cell, Cell t1) {
                return t1.getY() - cell.getY();
            }
        });
    }
}
