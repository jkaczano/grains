package model;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.RadioButton;

import java.util.Random;

import static java.lang.Math.abs;

/**
 * Created by jacek_000 on 2018-06-17.
 */
public class GameLogic {

    int sizeOfCell;
    int rows, columns;
    double canvasHeight, canvasWidth;
    int grainsCount;
    public String choice;
    Boolean period = true;

    RadioButton periodRadioButton;
    public Board board;
    public Drawing drawing;
    public GraphicsContext graphicsContext;

    public GameLogic(int sizeOfCell, double canvasHeight, double canvasWidth, Canvas canvas, int grainsCount, String choice) {
        graphicsContext = canvas.getGraphicsContext2D();
        this.sizeOfCell = sizeOfCell;
        this.canvasHeight = canvasHeight;
        this.canvasWidth = canvasWidth;
        this.rows = (int)canvasHeight / sizeOfCell;
        this.columns = (int)canvasWidth / sizeOfCell;
        this.grainsCount = grainsCount;
        this.choice = choice;

        board = new Board(rows, columns);
        drawing = new Drawing(graphicsContext, canvasHeight, canvasWidth, sizeOfCell, grainsCount);
    }

    public void fillBoardStateString(int i, int j, String text) {
        board.board[i][j].setState(text);
    }

    public Board calculateIterationGrains() {
        Board newBoard = new Board(rows, columns);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {

                if (board.board[i][j].state != ""&&board.board[i][j].intrusion!=0) {
                    newBoard.board[i][j].state = board.board[i][j].state;
                    switch (choice) {

                        case "Moore": {
                            mooreSurroundingFill(board, newBoard, i, j);
                            break;
                        }
                        case "Von Neumann": {
                            vonNeumannSurrounding(board, newBoard, i, j);
                            break;
                        }
                        default: {
                            vonNeumannSurrounding(board, newBoard, i, j);
                            break;
                        }
                    }


                }

            }
        }
        return newBoard;
    }


     private void fillNewBoard(Board board, Board newBoard, String grainName, int x, int y) {
        int tmpX;
        int tmpY;
        tmpX = x;
        tmpY = y;

            if (x == -1) tmpX = 0;
            if (x == columns) tmpX = columns - 1;
            if (y == -1) tmpY = 0;
            if (y == rows) tmpY = rows - 1;

        if (board.board[tmpX][tmpY].state == "")// && newBoard.board[tmpX][tmpY].state == "")
            newBoard.board[tmpX][tmpY].state = grainName;
    }


    private void mooreSurroundingFill(Board board, Board newBoard, int i, int j) {
        int startX = i - 1;
        int startY = j - 1;
        int endX = i + 1;
        int endY = j + 1;

        String grainName = board.board[i][j].state;

        int tmpX, tmpY;
        for (int x = startX; x <= endX; x++) {
            for (int y = startY; y <= endY; y++) {
                fillNewBoard(board, newBoard, grainName, x, y);

            }
        }
    }

    private void vonNeumannSurrounding(Board board, Board newBoard, int i, int j) {

        int leftX = i;
        int leftY = j - 1;
        int upX = i - 1;
        int upY = j;
        int rightX = i;
        int rightY = j + 1;
        int downX = i + 1;
        int downY = j;
        String grainName = board.board[i][j].state;
        period = periodRadioButton.isSelected();
        if (period) {
            if (leftY == -1) leftY = rows - 1;
            if (leftY == rows) leftY = 0;
            if (upX == -1) upX = columns - 1;
            if (upX == columns) upX = 0;
            if (rightY == rows) rightY = 0;
            if (rightY == -1) rightY = rows - 1;
            if (downX == -1) downX = columns - 1;
            if (downX == columns) downX = 0;

        } else {
            if (leftY == -1) leftY = 0;
            if (leftY == rows) leftY = rows - 1;
            if (upX == -1) upX = 0;
            if (upX == columns) upX = columns - 1;
            if (rightY == rows) rightY = rows - 1;
            if (rightY == -1) rightY = 0;
            if (downX == -1) downX = 0;
            if (downX == columns) downX = columns - 1;
        }

        String grainNameTmp = board.board[leftX][leftY].state;
        if (grainNameTmp == "") {
            newBoard.board[leftX][leftY].state = grainName;
        }
        grainNameTmp = board.board[upX][upY].state;
        if (grainNameTmp == "") {
            newBoard.board[upX][upY].state = grainName;
        }
        grainNameTmp = board.board[rightX][rightY].state;
        if (grainNameTmp == "") {
            newBoard.board[rightX][rightY].state = grainName;
        }

        grainNameTmp = board.board[downX][downY].state;
        if (grainNameTmp == "") {
            newBoard.board[downX][downY].state = grainName;
        }

    }

    public void randomGrains() {
        String grainName = "grain";
        Random random = new Random();
        System.out.println("rows = " + rows);
        System.out.println("columns = " + columns);
        for (int i = 1; i <= grainsCount; i++) {
            int x = abs(random.nextInt() % (rows - 2) + 1);
            int y = abs(random.nextInt() % (columns - 2) + 1);
            System.out.println("x = " + x + "y = " + y);
            board.board[x][y].state = grainName + i;
            System.out.println(board.board[x][y].state);

        }
        drawing.drawBoardString(board,1);
    }

    public void intrusion() {
        String grainName = "grain";
        Random random = new Random();
        System.out.println("rows = " + rows);
        System.out.println("columns = " + columns);
        for (int i = 1; i <= grainsCount; i++) {
            int x = abs(random.nextInt() % (rows - 2) + 1);
            int y = abs(random.nextInt() % (columns - 2) + 1);
            System.out.println("x = " + x + "y = " + y);
            board.board[x][y].state = grainName + i;
            board.board[x][y].intrusion = 0;
            System.out.println(board.board[x][y].state);

        }
        drawing.drawBoardString(board,5);
    }
}
