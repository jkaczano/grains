package model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.HashMap;
import java.util.Random;

/**
 * Created by jacek_000 on 2018-06-17.
 */
public class Drawing {
    private GraphicsContext graphicsContext;
    double canvasHeight, canvasWidth;
    int sizeOfCell;
    int grainCount;
    public static HashMap<String, Color> colorHashMap;

    public Drawing(GraphicsContext graphicsContext, double canvasHeight, double canvasWidth, int sizeOfCell, int grainCount) {
        this.graphicsContext = graphicsContext;
        this.canvasHeight = canvasHeight;
        this.canvasWidth = canvasWidth;
        this.sizeOfCell = sizeOfCell;
        this.grainCount = grainCount;

        randomColors();
    }




    public void drawBoardString(Board board,int size){
        String key = "grain1";
        for (int i = 0; i < canvasHeight/sizeOfCell; i++) {
            for (int j = 0; j < canvasWidth/sizeOfCell; j++) {
                if(board.board[i][j].intrusion==1)
                    graphicsContext.setFill(Color.BLACK);
                else{
                    key = board.board[i][j].state;
                    graphicsContext.setFill(colorHashMap.get(key));
                }
                graphicsContext.fillRect(i * sizeOfCell, j * sizeOfCell,sizeOfCell*size, sizeOfCell * size);
            }
        }
    }
    public void drawBoardString2(Board board,int size){
        String key = "grain1";
        for (int i = 0; i < canvasHeight/sizeOfCell; i++) {
            for (int j = 0; j < canvasWidth/sizeOfCell; j++) {
                if(board.nucleon[i][j].state!="grain0"){

                    key = board.nucleon[i][j].state;
                    graphicsContext.setFill(colorHashMap.get(key));

                graphicsContext.fillRect(i * sizeOfCell, j * sizeOfCell,sizeOfCell*size, sizeOfCell * size);
            }}
        }
    }
    public void drawBoardStringHash(Board board,int size,HashMap<String,Color> map){
        String key = "grain1";
        for (int i = 0; i < canvasHeight/sizeOfCell; i++) {
            for (int j = 0; j < canvasWidth/sizeOfCell; j++) {
                if(board.board[i][j].intrusion==1)
                    graphicsContext.setFill(Color.BLACK);
                else{
                    key = board.board[i][j].state;
                    graphicsContext.setFill(map.get(key));
                }
                graphicsContext.fillRect(i * sizeOfCell, j * sizeOfCell,sizeOfCell*size, sizeOfCell * size);
            }
        }
    }
    public void randomColors(){
        Random random = new Random();
        colorHashMap = new HashMap<String, Color>();
        int i=2;
        colorHashMap.put("grain0",Color.rgb(255,255,255));
        colorHashMap.put("grain1",Color.rgb(255,102,204));
        while( i<= 9+1) {
            int r = Math.abs(random.nextInt()%255);
            int g = Math.abs(random.nextInt()%255);
            int b = Math.abs(random.nextInt()%255);
            if(r!=255&&g!=102&b!=204) {
                Color color = Color.rgb(r, g, b);
                String name = "grain" + i;
                colorHashMap.put(name, color);
            i++;
            }
                }
        //colorHashMap.put("", Color.WHITE);
    }

    public void addOnClickGrainColor(int grainCount){
        Random random = new Random();
        int r = Math.abs(random.nextInt()%255);
        int g = Math.abs(random.nextInt()%255);
        int b = Math.abs(random.nextInt()%255);
        Color color = Color.rgb(r,g,b);
        String name = "grain"+grainCount;
        colorHashMap.put(name, color);
    }

    public void clearBoard(){
        graphicsContext.setFill(Color.WHITE);
        graphicsContext.fillRect(0, 0, canvasHeight, canvasWidth);

    }

    public void drawEnergy(Board board){
        HashMap<String, Color> energyHashMap = new HashMap<String, Color>();
        Cell[][] brd = new Cell[300][300];
        for (int i = 0; i < 300; i++) {
            for (int j = 0; j < 300; j++) {
                brd[i][j] = new Cell();
                brd[i][j].state=board.board[i][j].state;
            }
        }

        for(int i=0;i<9;i++){
            Color color = Color.rgb(0,0,60 + 20*i);
            energyHashMap.put("grain"+i,color);
        }

        String name="grain0";
        for (int i = 0; i < 300; i++) {
            for (int j = 0; j < 300; j++){
                board.board[i][j].state="grain"+board.energy[i][j].energy;
            }
        }
        drawBoardStringHash(board,1,energyHashMap);
        for (int i = 0; i < 300; i++) {
            for (int j = 0; j < 300; j++) {

               board.board[i][j].state= brd[i][j].state;
            }
        }
    }


    ////////////////////////////////////////////////////////////////////////
    public void drawNucleons(Board board,int nucl) {
        for(int i=11;i<20;i++){
            Color color = Color.rgb(60+20*(i-11),0,0);
            colorHashMap.put("grain"+i,color);
        }

        drawBoardString2(board,1);
    }
}
