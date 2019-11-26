package model;

/**
 * Created by jacek_000 on 2018-06-17.
 */
public class Cell {
    public String state;
    public int intrusion;
    public boolean noGrow;
    public Cell()
    {
        state ="";
        noGrow=false;
    }

    public void fillCell(String str)
    {
        state=str;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getState() {
        return state;
    }
}
