package infotainment.Model;

import android.graphics.Color;

import infotainment.contract.MainActivityContract;

public class MainActivityModel implements MainActivityContract.Model{

    private int startClr;
    private int endClr;

    public MainActivityModel() {
        startClr = Color.GREEN;
        endClr = Color.RED;
    }

    @Override
    public String getData() {

        return "Starting";
    }

    public float getEcoRatio() {
        return 0.5f;
    }

    @Override
    public int getStartColor() {
        return startClr;
    }
    @Override
    public int getEndColor() {
        return endClr;
    }

    @Override
    public void setStartColor(int newStartClr) {
        startClr = newStartClr;
    }

    @Override
    public void setEndColor(int newEndClr) {
        endClr = newEndClr;
    }
}
