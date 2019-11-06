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

        for (int i = 0; i < canvasHeight/sizeOfCell; i++) {
            for (int j = 0; j < canvasWidth/sizeOfCell; j++) {
                String key = board.board[i][j].state;

                graphicsContext.setFill(colorHashMap.get(key));
                graphicsContext.fillRect(i * sizeOfCell, j * sizeOfCell,sizeOfCell*size, sizeOfCell * size);
            }
        }
    }

    public void randomColors(){
        Random random = new Random();
        colorHashMap = new HashMap<String, Color>();
        for (int i = 1; i <= grainCount; i++) {
            int r = Math.abs(random.nextInt()%255);
            int g = Math.abs(random.nextInt()%255);
            int b = Math.abs(random.nextInt()%255);
            Color color = Color.rgb(r,g,b);
            String name = "grain"+i;
            colorHashMap.put(name, color);
        }
        colorHashMap.put("", Color.WHITE);
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
}