package infotainment.Model;

import android.graphics.Color;

import java.util.Random;

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
        Random rand = new Random();
        return rand.nextFloat();
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
