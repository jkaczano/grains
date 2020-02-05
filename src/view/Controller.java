package view;

import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import model.Board;
import model.Cell;
import model.GameLogic;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.Button;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

import static java.lang.Math.abs;
import static model.Drawing.colorHashMap;

//import java.awt.*;

/**
 * Created by jacek_000 on 2018-06-17.
 */
public class Controller {

    int sizeOfCell;
    int canvasHeight, canvasWidth;
    int grainsCount, intrSize;
    Thread thread;
    @FXML
    Canvas canvas;
    @FXML
    TextField x;
    @FXML
    TextField y;
    @FXML
    TextField intr;
    @FXML
    TextField rand;
    @FXML
    TextField grainsCounttxt;
    @FXML
    ComboBox comboBox;
    @FXML
    ChoiceBox structure;
    @FXML
    ProgressBar progressBar;
    @FXML
    javafx.scene.control.Label percentage;
    @FXML
    TextField monteCarlo;
    @FXML
    RadioButton hetero;
    @FXML
    RadioButton homo;
    @FXML
    RadioButton cons;
    @FXML
    RadioButton incr;
    @FXML
    RadioButton atbos;

    Board board;

    public GraphicsContext graphicsContext;
    GameLogic gameLogic;
    int i = 0,p=0,random=0;
    private boolean running = true;

    @FXML
    private void initialize(){
        graphicsContext = canvas.getGraphicsContext2D();
        x.setText("300");
        y.setText("300");
        grainsCounttxt.setText("10");
        comboBox.setItems(FXCollections.observableArrayList(
                "Square",
                "Circle"
        ));
        structure.setItems(FXCollections.observableArrayList(
                "SUB",
                "DP"
        ));
    }

    @FXML
    public void handleGenerateButton(){
        sizeOfCell = 1;
        canvasHeight = Integer.parseInt(x.getText());
        canvasWidth = Integer.parseInt(y.getText());
        intrSize = Integer.parseInt(intr.getText());
        grainsCount = Integer.parseInt(grainsCounttxt.getText());
        String choice = "Moore";
        String intrType = String.valueOf(comboBox.getValue());
        String structs = String.valueOf(structure.getValue());
        random = Integer.parseInt(rand.getText());
        gameLogic = new GameLogic(sizeOfCell, canvasHeight, canvasWidth, canvas, grainsCount, choice,intrSize,intrType,random,structs);
        gameLogic.drawing.clearBoard();
        drawOnCanvas();
        for (int i = 0; i < 300; i++) {
            for (int j = 0; j < 300; j++) {
                gameLogic.board.board[i][j] = new Cell();
                gameLogic.board.board[i][j].state="grain0";
            }
        }
    }

    public void drawOnCanvas() {

       // i = grainsCount+1;
        String phase = String.valueOf(structure.getValue());

        canvas.addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {

                @Override
                public void handle (MouseEvent event){

                int x = (int) event.getX();
                int y = (int) event.getY();
                //gameLogic.phase((int)x,(int)y,p);
                //System.out.println(p+" canvas");
                //p++;
                //        System.out.println(gameLogic.board.board[x][y].state);
                gameLogic.ph=gameLogic.board.board[x][y].state;
                    System.out.println(gameLogic.ph);
            }
        });

    }


    @FXML
    void handleStartButton(){
        startFunction();
    }


    private void startFunction() {
            gameLogic.phases= new String[20];
            gameLogic.choice = "Moore";
            while(!gameLogic.cntr(gameLogic.board)) {
                gameLogic.board = gameLogic.calculateIterationGrains();
            }
            gameLogic.drawing.drawBoardString(gameLogic.board,1);
            System.out.println("growth finished");
    }

    @FXML
    public void handleRandomGrains(){
        gameLogic.randomGrains();
    }

    @FXML
    public void handleStopButton(){
        running = false;
        thread.interrupt();

    }

    @FXML
    public void saveImage(){
        BufferedImage image = new BufferedImage(canvasWidth,canvasHeight,BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();

        for (int i = 0; i < canvasHeight/sizeOfCell; i++) {
            for (int j = 0; j < canvasWidth/sizeOfCell; j++) {

                    String key;
                key = gameLogic.board.board[i][j].state;
                javafx.scene.paint.Color fx = colorHashMap.get(key);
                    java.awt.Color color = new java.awt.Color((float) fx.getRed(),
                        (float) fx.getGreen(),
                        (float) fx.getBlue(),
                        (float) fx.getOpacity());
                    g.setColor(color);
                    g.fillRect(i,j,1,1);
            }
        }


        File file = new File("grain.png");
        try {
            ImageIO.write(image,"png",file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("image saved");
    }


    @FXML
    public void importImage(){
        int id=2;
        BufferedImage img = new BufferedImage(1,1,BufferedImage.TYPE_INT_RGB);
        File image = new File("grain.png");
        try {
            img = ImageIO.read(image);
        } catch (IOException e) {
            e.printStackTrace();
        }
        canvas.setHeight(img.getHeight());
        canvas.setWidth(img.getWidth());
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.drawImage(convertToFxImage(img),0,0);

//        for (int i = 0; i < gameLogic.rows; i++) {
//            for (int j = 0; j < gameLogic.columns; j++) {
//                int colorRGB = img.getRGB(i, j);
//                int r = (colorRGB & 0x00ff0000) >> 16;
//                int g = (colorRGB & 0x0000ff00) >> 8;
//                int b = colorRGB & 0x000000ff;
//                Color color = new Color.rgb(r,g,b);
//            }
//        }
    }

    private static javafx.scene.image.Image convertToFxImage(BufferedImage image) {
        WritableImage wr = null;
        if (image != null) {
            wr = new WritableImage(image.getWidth(), image.getHeight());
            PixelWriter pw = wr.getPixelWriter();
            for (int x = 0; x < image.getWidth(); x++) {
                for (int y = 0; y < image.getHeight(); y++) {
                    pw.setArgb(x, y, image.getRGB(x, y));
                }
            }
        }
        return new ImageView(wr).getImage();
    }
    @FXML
    public void saveTxt(){
        File txt = new File("micro.txt");
        try (PrintWriter out = new PrintWriter("micro.txt")) {
            out.print(canvasHeight+" ");
            out.println(canvasWidth);
            for(int i=0;i<canvasWidth;i++){
                for(int j=0;j<canvasHeight;j++)
                {
                    out.println(i+" "+j+" "+gameLogic.board.board[i][j].state);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println("Text saved");
    }

    @FXML
    public void importTxt(){

    }
    @FXML
    public void intrusion(){
        gameLogic.grainsCount = Integer.parseInt(grainsCounttxt.getText());
        gameLogic.intrSize = Integer.parseInt(intr.getText());
        gameLogic.intrType = String.valueOf(comboBox.getValue());
        gameLogic.board = gameLogic.intrusion(gameLogic.cntr(gameLogic.board));
    }
    @FXML
    public void sub(){
        for(int i=1;i<canvasWidth-1;i++) {
            for (int j = 1; j < canvasHeight-1; j++) {
                if(!onList(i,j)){
                    gameLogic.board.board[i][j].state="";
                }
                else
                    gameLogic.board.board[i][j].noGrow=true;

            }
        }
        gameLogic.drawing.drawBoardString(gameLogic.board,1);
    }

    public boolean onList(int x,int y)
    {
        for(int i=1;i<p;i++) {
            if (gameLogic.board.board[x][y].state == gameLogic.phases[i]) {
                return true;
            }
        }
        return false;
    }
    @FXML
    public void allBoundaries(){
        for(int i=1;i<canvasHeight-1;i++){
            for(int j=1;j<canvasWidth-1;j++){
                for (int u = i-1; u <= i+1; u++) {
                    for (int o = j-1; o <= j+1; o++) {
                        if(gameLogic.board.board[i][j].state!=gameLogic.board.board[u][o].state)
                            gameLogic.board.board[i][j].intrusion=1;
                    }
                }
            }
        }
        gameLogic.drawing.drawBoardString(gameLogic.board,1);
    }

    @FXML
    public void clearInside()
    {
        double gb=0;
        for(int i=1;i<canvasHeight-1;i++){
            for(int j=1;j<canvasWidth-1;j++){
                if( gameLogic.board.board[i][j].intrusion!=1)
                    gameLogic.board.board[i][j].state="";
                else
                    gb++;
            }}
            gb=gb/89000 * 100;
        gb=Math.round(gb);
            percentage.setText(gb+"%");
        gameLogic.drawing.drawBoardString(gameLogic.board,1);
    }

    @FXML
    public void nBounds(){
        for(int i=1;i<canvasWidth-1;i++) {
            for (int j = 1; j < canvasHeight-1; j++) {
                if(!onList(i,j)){
                    gameLogic.board.board[i][j].state="";
                }
                else
                    gameLogic.board.board[i][j].noGrow=true;

            }
        }
        for(int i=1;i<canvasHeight-1;i++){
            for(int j=1;j<canvasWidth-1;j++){
                for (int u = i-1; u <= i+1; u++) {
                    for (int o = j-1; o <= j+1; o++) {
                        if(gameLogic.board.board[i][j].state!=gameLogic.board.board[u][o].state)
                            gameLogic.board.board[i][j].intrusion=1;
                    }
                }
            }
        }
        gameLogic.drawing.drawBoardString(gameLogic.board,1);
    }
    @FXML
    public void dualPhase(){
        //gameLogic.drawing.colorHashMap.put("", javafx.scene.paint.Color.WHITE);
        Cell[][] brd = new Cell[300][300];
        for (int i = 0; i < 300; i++) {
            for (int j = 0; j < 300; j++) {
                brd[i][j] = new Cell();
                //if(board.board[i][j].state != "")
                   // brd[i][j].state=board.board[i][j].state;
            }
        }
        for(int i=1;i<canvasWidth-1;i++) {
            for (int j = 1; j < canvasHeight-1; j++) {
                if(gameLogic.board.board[i][j].state==gameLogic.ph){
                    //brd[i][j].state="grain1";
                    gameLogic.board.board[i][j].dp=true;
                    gameLogic.board.board[i][j].noGrow=true;
                    gameLogic.nogrow++;
                }
                else {
                    brd[i][j].state = "grain0";
                }

            }
        }
        for (int i = 0; i < 300; i++) {
            for (int j = 0; j < 300; j++) {
                gameLogic.board.board[i][j].state = brd[i][j].state;
            }}
        gameLogic.drawing.drawBoardString(gameLogic.board,1);
    }

    @FXML
    public void drawMonteCarlo(){
        int grains = Integer.parseInt(monteCarlo.getText());
        gameLogic.monteDraw(grains);
        System.out.println("monte");
    }
    @FXML
    public void runMC(){
        for(int i=0;i<10;i++)
        gameLogic.calculateEnergy();
    }
    @FXML
    public void handleEnergy()
    {
        if(homo.isSelected()){
            for (int i = 0; i < 300; i++) {
                for (int j = 0; j < 300; j++) {
                 gameLogic.board.energy[i][j].energy=5;
                }
            }
        }
        if(hetero.isSelected()){
            for (int i = 1; i < 300-1; i++) {
                for (int j = 1; j < 300-1; j++) {
                    for (int u = i-1; u <= i+1; u++) {
                        for (int o = j-1; o <= j+1; o++) {
                            if(gameLogic.board.board[i][j].state!=gameLogic.board.board[u][o].state)
                                gameLogic.board.energy[i][j].energy=7;
                            else
                                gameLogic.board.energy[i][j].energy=2;
                        }
                    }
                }
            }
        }
        gameLogic.drawing.drawEnergy(gameLogic.board);
    }
    @FXML
    public void backToStruct(){
        gameLogic.drawing.drawBoardString(gameLogic.board,1);
    }

    @FXML
    public void handleSRX(){
        Random random = new Random();
        for (int i = 0; i < 300; i++) {
            for (int j = 0; j < 300; j++) {
                gameLogic.board.nucleon[i][j]=gameLogic.board.board[i][j];
            }
        }
        if(cons.isSelected()){ //losowanie nowych nukleonow
            for (int i = 2; i <= 10; i++) {
                int x = abs(random.nextInt() % (300 - 3)) + 1;
                int y = abs(random.nextInt() % (300 - 3)) + 1;
                gameLogic.board.nucleon[x][y].isrecry=true;
            }
        }
        int ener=0;
        for (int i = 1; i < 298; i++) { //liczenie energii przed
            for (int j = 1; j < 298; j++) {
                for (int u = i-1; u <= i+1; u++) {
                    for (int o = j-1; o <= j+1; o++) {
                        if(u!=o&&gameLogic.board.nucleon[u][o].isrecry==true){
                            gameLogic.board.nucleon[i][j].isrecry=true;
                        }}}}}
//                            for (int p = i-1; p <= i+1; p++) {
//                                for (int l = j-1; l <= j+1; l++) {
//                                    if(p!=l&&gameLogic.board.board[p][l].state==gameLogic.board.board[i][j].state)
//                                        ener++;
//                                }}
//                            gameLogic.board.board[i][j].isrecry=true;
//                        }
//                    }
//                }
//                gameLogic.board.energy[i][j].energy+=ener;
//                ener=0;
//            }
//        }
//
//
//        for (int i = 1; i < 298; i++) { //liczenie energii przed
//            for (int j = 1; j < 298; j++) {gameLogic.board.nucleon[i][j].isrecry=true;
//            }}
//
//        for (int i = 1; i < 298; i++) { //liczenie energii po
//            for (int j = 1; j < 298; j++) {
//
//                        if(gameLogic.board.nucleon[i][j].isrecry==true){
//                              for (int p = i-1; p <= i+1; p++) {
//                                for (int l = j-1; l <= j+1; l++) {
//                                    if(p!=l&&gameLogic.board.board[p][l].state==gameLogic.board.board[i][j].state)
//                                        ener++;
//                                }}
//                        }
//                gameLogic.board.board[i][j].energy+=ener;
//                ener=0;
//                if(gameLogic.board.board[i][j].energy-gameLogic.board.energy[i][j].energy<=0)gameLogic.board.nucleon[i][j].isrecry=false;
//                    }
//                }

        gameLogic.drawing.drawNucleons(gameLogic.board,gameLogic.nucl);
        gameLogic.nucl++;
            }

}
