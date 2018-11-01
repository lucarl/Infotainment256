package infotainment.Model;

import android.renderscript.Sampler;
import android.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import infotainment.Model.db.Db;


public class DataFilter {

    ArrayList<Integer> SamplingList_r;
    ArrayList<Integer> AverageList_r;

    ArrayList<Integer> SamplingList_e;
    ArrayList<Integer> AverageList_e;


    public DataFilter()
    {
        SamplingList_r = new ArrayList<>();
        AverageList_r = new ArrayList<>();

        SamplingList_e = new ArrayList<>();
        AverageList_e = new ArrayList<>();
    }

    public int getRand(){
        Random rand = new Random();
        return rand.nextInt(50);
    }

    // interface function for other programs to call to input data.
    public void dataInput(Character c, int inData) {
        switch (c) {
            case 'r':
                dataInput_r(c, inData);
                break;
            case 'e':
                dataInput_e(c, inData);
                break;
        }
    }

    public void dataInput_r(Character c, int inData) {
        if (AverageList_r.size() >= 10) {
            // send average to database
            // external.function(AvgDouble(AverageList));
            Db.insertData(new Pair<Character, Integer>(c, AvgInt(AverageList_r)));
            AverageList_r.clear();
        }
        if (SamplingList_r.size() >= 10){
            AverageList_r.add(AvgInt(SamplingList_r));
            SamplingList_r.clear();
        }
        SamplingList_r.add(inData);
    }

    public void dataInput_e(Character c, int inData) {
        if (AverageList_e.size() >= 10) {
            // send average to database
            // external.function(AvgDouble(AverageList));
            Db.insertData(new Pair<Character, Integer>(c, AvgInt(AverageList_e)));
            AverageList_e.clear();
        }
        if (SamplingList_e.size() >= 10){
            AverageList_e.add(AvgInt(SamplingList_e));
            SamplingList_e.clear();
        }
        SamplingList_e.add(inData);
    }

    // Gets average value off array of Doubles
    private Double AvgDouble(ArrayList<Double> intArr) {
        Double sum = 0.0;
        if(!intArr.isEmpty()) {
            for(Double elem : intArr) {
                sum += elem;
            }
            return sum / intArr.size();
        }
        return sum;

    }
    // Gets average value off array of Integers
    private Integer AvgInt(ArrayList<Integer> intArr) {
        Integer sum = 0;
        if(!intArr.isEmpty()) {
            for(int elem : intArr) {
                sum += elem;
            }
            return sum / intArr.size();
        }
        return sum;

    }

    /*
    input data method

    fill SamplingList until 10th element
    if (arr.length < 10)
        arr.add
    else


    perform average and put value in AverageList

    fill AverageList until 10th element

    perform average on AverageList and output to db

    timer object to inject values via data input method

    */

}
