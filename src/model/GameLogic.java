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

    public int sizeOfCell,intrSize;
    int rows, columns;
    double canvasHeight, canvasWidth;
    public int grainsCount;
    public String choice,intrType;
    Boolean period = true;

    RadioButton periodRadioButton;
    public Board board;
    public Drawing drawing;
    public GraphicsContext graphicsContext;

    public GameLogic(int sizeOfCell, double canvasHeight, double canvasWidth, Canvas canvas, int grainsCount, String choice,int intrSize,String intrType) {
        graphicsContext = canvas.getGraphicsContext2D();
        this.sizeOfCell = sizeOfCell;
        this.canvasHeight = canvasHeight;
        this.canvasWidth = canvasWidth;
        this.rows = (int)canvasHeight / sizeOfCell;
        this.columns = (int)canvasWidth / sizeOfCell;
        this.grainsCount = grainsCount;
        this.choice = choice;
        this.intrSize = intrSize;
        this.intrType = intrType;
        board = new Board(rows, columns);
        drawing = new Drawing(graphicsContext, canvasHeight, canvasWidth, sizeOfCell, grainsCount);
    }

    public void fillBoardStateString(int i, int j, String text) {
        board.board[i][j].setState(text);
    }

    public Board calculateIterationGrains() {
        Cell[][] brd = new Cell[rows][columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                brd[i][j] = new Cell();
                if(board.board[i][j].state != "")
                brd[i][j].state=board.board[i][j].state;
            }
            }

        for (int i = 1; i < rows-1; i++) {
            for (int j = 1; j < columns-1; j++) {
                if (board.board[i][j].state == "") {
                    brd[i][j].state=energy(i,j);
                }
            }
        }

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                    board.board[i][j].state=brd[i][j].state;
            }
        }
        return board;
    }


    private String energy(int x,int y){

        int[] sameCellCount = new int[8];
        for(int i=0;i<8;i++)
        {sameCellCount[i]=0;}
        int startX=x-1,startY=y-1,endX=x+1,endY=y+1;
        Random random = new Random();
        String state = "";
        for (int m = x - 1; m <= x + 1; m++) {
            for (int n = y - 1; n <= y + 1; n++) {
            if(m!=x || n!=y)
            {
                for (int u = startX; u <= endX; u++) {
                    for (int o = startY; o <= endY; o++) {
                        if(board.board[u][o].state!=""&&board.board[m][n].state!=""&&u!=o) {
                            if (board.board[u][o].state == board.board[m][n].state && m == x - 1 && n == y - 1)
                                sameCellCount[0]++;
                            if (board.board[u][o].state == board.board[m][n].state && m == x && n == y - 1)
                                sameCellCount[1]++;
                            if (board.board[u][o].state == board.board[m][n].state && m == x + 1 && n == y - 1)
                                sameCellCount[2]++;
                            if (board.board[u][o].state == board.board[m][n].state && m == x - 1 && n == y)
                                sameCellCount[3]++;
                            if (board.board[u][o].state == board.board[m][n].state && m == x + 1 && n == y)
                                sameCellCount[4]++;
                            if (board.board[u][o].state == board.board[m][n].state && m == x - 1 && n == y + 1)
                                sameCellCount[5]++;
                            if (board.board[u][o].state == board.board[m][n].state && m == x && n == y + 1)
                                sameCellCount[6]++;
                            if (board.board[u][o].state == board.board[m][n].state && m == x + 1 && n == y + 1)
                                sameCellCount[7]++;
                        }
                    }
                }
            }
            }}

        int max=0,index=-1;
        for(int i=0;i<8;i++)
        {
            if(sameCellCount[i]>=max) {
                max = sameCellCount[i];
                index = i;
            }
        }
        //if(index>=0) {
            try {
                switch (index) {
                    case 0: {
                        state = board.board[x - 1][y - 1].state;
                        break;
                    }
                    case 1: {
                        state = board.board[x][y-1].state;
                        break;
                    }
                    case 2: {
                        state = board.board[x + 1][y - 1].state;
                        break;
                    }
                    case 3: {
                        state = board.board[x-1][y ].state;
                        break;
                    }
                    case 4: {
                        state = board.board[x+1][y ].state;
                        break;
                    }
                    case 5: {
                        state = board.board[x - 1][y + 1].state;
                        break;
                    }
                    case 6: {
                        state = board.board[x ][y+1].state;
                        break;
                    }
                    case 7: {
                        state = board.board[x + 1][y + 1].state;
                        break;
                    }
                    default: {
                        state = "";
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        //}
        return state;
    }

    private String Energy(int x, int y) {
        String state="";
        Random random = new Random();
        int[] sameCellCount = new int[8];
        for (int startX = x - 1; startX <= x + 1; startX++) {
            for (int startY = y - 1; startY <= y + 1; startY++) {
                int tmpX = startX;
                int tmpY = startY;
                if (period) {
                    if (tmpX == -1) tmpX = columns - 1;
                    if (tmpX == columns) tmpX = 0;
                    if (tmpY == -1) tmpY = rows - 1;
                    if (tmpY == rows) tmpY = 0;
                } else {
                    if (tmpX == -1) tmpX = 0;
                    if (tmpX == columns) tmpX = columns - 1;
                    if (tmpY == -1) tmpY = 0;
                    if (tmpY == rows) tmpY = rows - 1;
                }
                if (x == 0 && y == 0) {
                    if (board.board[tmpX][tmpY].state == board.board[x + 1][y].state) sameCellCount[6]++;
                    if (board.board[tmpX][tmpY].state == board.board[x + 1][y + 1].state) sameCellCount[7]++;
                    if (board.board[tmpX][tmpY].state == board.board[x][y + 1].state) sameCellCount[4]++;
                }
                if (x == 0 && y != 0 && y != columns - 1) {
                    if (board.board[tmpX][tmpY].state == board.board[x][y - 1].state) sameCellCount[3]++;
                    if (board.board[tmpX][tmpY].state == board.board[x][y + 1].state) sameCellCount[4]++;
                    if (board.board[tmpX][tmpY].state == board.board[x + 1][y - 1].state) sameCellCount[5]++;
                    if (board.board[tmpX][tmpY].state == board.board[x + 1][y].state) sameCellCount[6]++;
                    if (board.board[tmpX][tmpY].state == board.board[x + 1][y + 1].state) sameCellCount[7]++;
                }
                if (x == 0 && y == columns - 1) {
                    if (board.board[tmpX][tmpY].state == board.board[x][y - 1].state) sameCellCount[3]++;
                    if (board.board[tmpX][tmpY].state == board.board[x + 1][y - 1].state) sameCellCount[5]++;
                    if (board.board[tmpX][tmpY].state == board.board[x + 1][y].state) sameCellCount[6]++;
                }
                if (x != 0 && x != rows - 1 && y == 0) {
                    if (board.board[tmpX][tmpY].state == board.board[x - 1][y].state) sameCellCount[1]++;
                    if (board.board[tmpX][tmpY].state == board.board[x - 1][y + 1].state) sameCellCount[2]++;
                    if (board.board[tmpX][tmpY].state == board.board[x][y + 1].state) sameCellCount[4]++;
                    if (board.board[tmpX][tmpY].state == board.board[x + 1][y].state) sameCellCount[6]++;
                    if (board.board[tmpX][tmpY].state == board.board[x + 1][y + 1].state) sameCellCount[7]++;
                }
                if (y == 0 && x == rows - 1) {
                    if (board.board[tmpX][tmpY].state == board.board[x][y + 1].state) sameCellCount[4]++;
                    if (board.board[tmpX][tmpY].state == board.board[x - 1][y + 1].state) sameCellCount[2]++;
                    if (board.board[tmpX][tmpY].state == board.board[x - 1][y].state) sameCellCount[1]++;
                }
                if (x == rows - 1 && y != 0 && y != columns - 1) {
                    if (board.board[tmpX][tmpY].state == board.board[x - 1][y - 1].state) sameCellCount[0]++;
                    if (board.board[tmpX][tmpY].state == board.board[x - 1][y].state) sameCellCount[1]++;
                    if (board.board[tmpX][tmpY].state == board.board[x - 1][y + 1].state) sameCellCount[2]++;
                    if (board.board[tmpX][tmpY].state == board.board[x][y - 1].state) sameCellCount[3]++;
                    if (board.board[tmpX][tmpY].state == board.board[x][y + 1].state) sameCellCount[4]++;
                }
                if (x == rows - 1 && y == columns - 1) {
                    if (board.board[tmpX][tmpY].state == board.board[x - 1][y - 1].state) sameCellCount[0]++;
                    if (board.board[tmpX][tmpY].state == board.board[x - 1][y].state) sameCellCount[1]++;
                    if (board.board[tmpX][tmpY].state == board.board[x][y - 1].state) sameCellCount[3]++;
                }
                if (y == columns - 1 && x != 0 && x != rows - 1) {
                    if (board.board[tmpX][tmpY].state == board.board[x - 1][y - 1].state) sameCellCount[0]++;
                    if (board.board[tmpX][tmpY].state == board.board[x - 1][y].state) sameCellCount[1]++;
                    if (board.board[tmpX][tmpY].state == board.board[x][y - 1].state) sameCellCount[3]++;
                    if (board.board[tmpX][tmpY].state == board.board[x + 1][y - 1].state) sameCellCount[5]++;
                    if (board.board[tmpX][tmpY].state == board.board[x + 1][y].state) sameCellCount[6]++;
                }
                if (x != 0 && x != rows - 1 && y != 0 && y != columns - 1) {
                    if (board.board[tmpX][tmpY].state == board.board[x - 1][y - 1].state) sameCellCount[0]++;
                    if (board.board[tmpX][tmpY].state == board.board[x - 1][y].state) sameCellCount[1]++;
                    if (board.board[tmpX][tmpY].state == board.board[x - 1][y + 1].state) sameCellCount[2]++;
                    if (board.board[tmpX][tmpY].state == board.board[x][y - 1].state) sameCellCount[3]++;
                    if (board.board[tmpX][tmpY].state == board.board[x][y + 1].state) sameCellCount[4]++;
                    if (board.board[tmpX][tmpY].state == board.board[x + 1][y - 1].state) sameCellCount[5]++;
                    if (board.board[tmpX][tmpY].state == board.board[x + 1][y].state) sameCellCount[6]++;
                    if (board.board[tmpX][tmpY].state == board.board[x + 1][y + 1].state) sameCellCount[7]++;
                }
            }

            int[] max = new int[8];
            max[0] = sameCellCount[0];
            int c = 0, m;
            for (int i = 1; i < 8; i++) {
                if (sameCellCount[i] > max[0]) {
                    for (int j = 0; j < 8; j++) {
                        max[j] = 0;
                    }
                    max[0] = i;
                    c = 1;
                }
                if (sameCellCount[i] == max[0]) {
                    max[c] = i;
                    c++;
                }
            }
            if (max.length > 1) {
                m = max[abs(random.nextInt() % max.length)];
            } else {
                m = max[0];
            }

            try {
                switch (m) {
                    case 0: {
                        state = board.board[x - 1][y - 1].state;break;}
                    case 1:{
                        state = board.board[x - 1][y].state;break;}
                    case 2:{
                        state = board.board[x - 1][y + 1].state;break;}
                    case 3:{
                        state = board.board[x][y - 1].state;break;}
                    case 4:{
                        state = board.board[x][y + 1].state;break;}
                    case 5:{
                        state = board.board[x + 1][y - 1].state;break;}
                    case 6:{
                        state = board.board[x + 1][y].state;break;}
                    case 7:{
                        state = board.board[x + 1][y + 1].state;break;}
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        return state;
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

    int black=0;

    public Board intrusion(Boolean finish) {
        System.out.println(finish);
        //String grainName = "grain";
        Random random = new Random();
        int x=0,y=0,counter;
        //System.out.println("rows = " + rows);
        //System.out.println("columns = " + columns);
        for (int i = 1; i <= grainsCount; i++) {
            if(finish)
            {
                counter=0;

                while(counter==0){
                    x = abs(random.nextInt() % (rows - 3) + 2);
                    y = abs(random.nextInt() % (columns - 3) + 2);
                    int startX = x - 1;
                    int startY = y - 1;
                    int endX = x + 1;
                    int endY = y + 1;

                    for (int u = startX; u <= endX; u++) {
                        for (int o = startY; o <= endY; o++) {
                            if(board.board[x][y].state!=board.board[u][o].state)
                                counter++;
                        }
                    }
                    System.out.println(counter);
                }
            }
            else {
                x = abs(random.nextInt() % (rows - 2) + 1);
                y = abs(random.nextInt() % (columns - 2) + 1);
            }
            System.out.println("x = " + x + "y = " + y);

            if(intrType=="Square") {
                for (int k = (int)(x - intrSize/2.0); k < x + intrSize/2.0; k++) {
                    for (int m = (int)(y - intrSize/2.0); m < y + intrSize/2.0; m++) {
                        if(k<canvasHeight && k>1 && m>1 && m<canvasWidth) {
                            board.board[k][m].state = "i";
                            board.board[k][m].intrusion = 1;
                            black++;
                        }
                    }
                }
            }
            else{
                double dist=0;
                for (int k = x - intrSize; k < x + intrSize; k++) {
                    for (int m = y - intrSize; m < y + intrSize; m++) {
                        dist=Math.sqrt((x-k)*(x-k)+(y-m)*(y-m));
                        if(dist<=intrSize && k>1 && m>1 && k<canvasHeight && m<canvasWidth) {
                            board.board[k][m].state = "i";
                            board.board[k][m].intrusion = 1;
                        black++;
                        }
                    }
                }
            }

            System.out.println(board.board[x][y].state);
        }
        drawing.drawBoardString(board,5);
        return board;
    }

    public boolean cntr(Board brd){
        int counter = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if(brd.board[i][j].state==""){
                    counter++;
                }
            }
        }
        System.out.println(counter);
        if(counter>black+1196)
            return false;
        else
            return true;
    }
}
