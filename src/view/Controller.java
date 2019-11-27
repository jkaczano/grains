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
import model.Drawing;
import model.GameLogic;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

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
    Board board;

    public GraphicsContext graphicsContext;
    GameLogic gameLogic;
    int i = 1,p=0,random=0;
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
    }

    public void drawOnCanvas() {

        i = grainsCount+1;
        String phase = String.valueOf(structure.getValue());
        canvas.addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                double x = event.getX();
                double y = event.getY();
                gameLogic.phase((int)x,(int)y,p);
                System.out.println(p+" canvas");
                p++;
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
                javafx.scene.paint.Color fx = Drawing.colorHashMap.get(key);
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
        for(int i=1;i<canvasWidth-1;i++) {
            for (int j = 1; j < canvasHeight-1; j++) {
                if(!onList(i,j)){
                    gameLogic.board.board[i][j].state="";
                }
                else
                    gameLogic.board.board[i][j].state="grain1";

            }
        }
        gameLogic.drawing.drawBoardString(gameLogic.board,1);
    }

}
