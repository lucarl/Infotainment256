package infotainment.Model;

import android.util.Log;

import java.util.Arrays;

import infotainment.contract.MainActivityContract;

public class MainActivityModel implements MainActivityContract.Model{

    private int ecoCal;
    private int ecoPointsRef = 45;             //Ansatt referensvärde

    private double resArr[] = new double[50];   //storleken bestämmer hur stor snittet är i ecoCal

    private int temp=0;

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
    public void setecoCal(double lambda, double tilt){

        if (temp==0){

            Arrays.fill(resArr, ecoPointsRef);
            temp=1;

        }

        ecoCal = ecoCal(lambda, tilt, resArr, ecoPointsRef);
    }

    private int ecoCal(double lambda, double tilt, double [] resArr, double ref){

        double newres;

        newres = Math.pow(Math.cos(Math.toRadians(tilt)),1.5) * Math.pow(lambda,2);
        // Uppdaterar resultatarrayen med nya värdet
        resArr = updateResarr(resArr, newres);

        int ratio = (int)Math.round((25*(sum(resArr)/resArr.length)/ref)/42);

        ecoLogger(resArr, ratio);
        if(ratio>41){ return 41;

        }else if(ratio<0){return 0;}

        else{return ratio;}

    }

    void ecoLogger(double[] resArr, double ratio) {
        for(int i=0; i<resArr.length; i++) {

            String numberAsString = Double.toString(resArr[i]);
            Log.d("Resarr" + i, numberAsString);

        }
        String numberAsString = Double.toString(ratio);
        Log.d("ratio", numberAsString);
    }

    public static double sum(double...values) {
        double result = 0;
        for (double value:values)
            result += value;
        return result;
    }

    public static double[] updateResarr(double []resArr, double newVal){

        for(int i = 1; i<resArr.length; i++){

            resArr[i-1]=resArr[i];

        }
        resArr[resArr.length-1] = newVal;
        return resArr;
    }
}

