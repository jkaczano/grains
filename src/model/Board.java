package model;

/**
 * Created by jacek_000 on 2018-06-17.
 */
public class Board {

    public Cell[][] board;
    int rows;
    int columns;

    public Board(int rows,int columns)
    {
    this.rows=rows;
    this.columns=columns;

    board = new Cell[rows][columns];
    for(int i=0;i<rows;i++)
    {
        for(int j=0;j<columns;j++)
        {
            board[i][j]=new Cell();
            board[i][j].fillCell("");
        }
    }

    }

    public void clearBoard(){
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                board[i][j].fillCell("");
                //board[i][j].intrusion=0;
            }
        }
    }
}
