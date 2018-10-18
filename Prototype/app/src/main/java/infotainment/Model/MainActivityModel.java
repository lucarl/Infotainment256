package infotainment.Model;

import android.util.Log;

import infotainment.contract.MainActivityContract;

public class MainActivityModel implements MainActivityContract.Model{

    private int ecoCal;

    @Override
    public String getData() {
        /* test here */

        return "Starting";
    }

    @Override
    public int getecoCal(){

        return ecoCal;
    }

    @Override
    public void dataInput(Character c, int d) {
        
    }

    @Override
    public void setecoCal(double lambda, double tilt, double [] resArr, double ref){

        ecoCal = ecoCal(lambda, tilt, resArr, ref);
    }

    private int ecoCal(double lambda, double tilt, double [] resArr, double ref){

        double res;

        res = Math.pow(Math.cos(Math.toRadians(tilt)),1.5) * Math.pow(lambda,2);
        resArr = updateResarr(resArr, res);     //Uppdaterar resultatarrayen med nya värdet

        int ratio = (int)Math.round((100*(sum(resArr)/resArr.length)/ref)/42);

        ecoLogger(resArr, ratio);
        if(ratio>41){ return 41;

        }else if(ratio<0){return 0;}

        else{return ratio;}

    }

    void ecoLogger(double[] resArr, double ratio) {
        for(int i=0; i<resArr.length; i++) {

            String numberAsString = Double.toString(resArr[i]);
            Log.d("Ratio" + i, numberAsString);

        }
        String numberAsString = Double.toString(ratio);
        Log.d("Ratio", numberAsString);
    }

    public static double sum(double...values) {
        double result = 0;
        for (double value:values)
            result += value;
        return result;
    }

    public static double[] updateResarr(double []resArr, double newVal){

        double[] newArr = new double[resArr.length];
        for(int i = 1; i<resArr.length; i++){

            newArr[i-1]=resArr[i];
        }

        newArr[resArr.length-1] = newVal;
        return newArr;
    }
}

