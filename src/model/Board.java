package model;

/**
 * Created by jacek_000 on 2018-06-17.
 */
public class Board {

    public Cell[][] board;
    int rows;
    int columns;
    public Cell[][] energy;
    public Cell[][] nucleon;
    public Board(int rows,int columns)
    {
    this.rows=rows;
    this.columns=columns;
    energy = new Cell[rows][columns];
    board = new Cell[rows][columns];
    nucleon = new Cell[rows][columns];
    for(int i=0;i<rows;i++)
    {
        for(int j=0;j<columns;j++)
        {
            board[i][j]=new Cell();
            board[i][j].fillCell("");
            energy[i][j]=new Cell();
            energy[i][j].fillCell("");
            nucleon[i][j]=new Cell();
            nucleon[i][j].fillCell("grain0");
            nucleon[i][j].isrecry=false;
        }
    }

    }

    public void clearBoard(){
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                board[i][j].fillCell("grain0");
                //board[i][j].intrusion=0;
            }
        }
    }
}
