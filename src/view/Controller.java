package view;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
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
    int grainsCount;
    Thread thread;
    @FXML
    Canvas canvas;
    @FXML
    TextField x;
    @FXML
    TextField y;
    @FXML
    TextField grainsCounttxt;
    Board board;

    public GraphicsContext graphicsContext;
    GameLogic gameLogic;
    int i = 1;
    private boolean running = true;

    @FXML
    private void initialize(){
        graphicsContext = canvas.getGraphicsContext2D();
        x.setText("300");
        y.setText("300");
        grainsCounttxt.setText("10");
    }

    @FXML
    public void handleGenerateButton(){
        sizeOfCell = 1;
        canvasHeight = Integer.parseInt(x.getText());
        canvasWidth = Integer.parseInt(y.getText());
        grainsCount = Integer.parseInt(grainsCounttxt.getText());
        String choice = "Moore";

        gameLogic = new GameLogic(sizeOfCell, canvasHeight, canvasWidth, canvas, grainsCount, choice);
        gameLogic.drawing.clearBoard();
        drawOnCanvas();
    }

    public void drawOnCanvas() {

        i = grainsCount+1;

        canvas.addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                double x = event.getX();
                double y = event.getY();

                int xPosition = (int) (x / sizeOfCell);
                int yPosition = (int) (y / sizeOfCell);

                gameLogic.fillBoardStateString(xPosition, yPosition, "grain" + i);
                System.out.println("state of cell = " + xPosition + " " + yPosition + "  " + gameLogic.board.board[xPosition][yPosition].state);
                i++;
                gameLogic.drawing.addOnClickGrainColor(i);
                gameLogic.drawing.drawBoardString(gameLogic.board,1);
            }
        });
    }


    @FXML
    void handleStartButton() throws InterruptedException {
        running = true;
        thread = new Thread(() -> {
            while (running) {
                Platform.runLater(() -> startFunction());
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    private void startFunction() {
            gameLogic.choice = "Moore";
            gameLogic.board = gameLogic.calculateIterationGrains();
            gameLogic.drawing.drawBoardString(gameLogic.board,1);
        System.out.println("sdf");
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
    }

    @FXML
    public void importTxt(){

    }
    @FXML
    public void intrusion(){
        gameLogic.intrusion();
    }

}