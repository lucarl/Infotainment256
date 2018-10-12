package infotainment.Model;

import android.renderscript.Sampler;
import android.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class DataFilter {

    ArrayList<Integer> SamplingList;
    ArrayList<Double> AverageList;


    public DataFilter() {
        // Short term list
        SamplingList = new ArrayList<>(); //Arrays.asList(1,2,3,4,5)
        // Long term list
        AverageList = new ArrayList<>();
    }

    public int getRand(){
        Random rand = new Random();
        return rand.nextInt(50);
    }

    // interface function for other programs to call to input data.
    public void dataInput(Character c, int inData) {
        if (AverageList.size() >= 10) {
            // send average to database
            // external.function(AvgDouble(AverageList));
            db.insert(new Pair<Character, Double>(c, AvgDouble(AverageList)));
            AverageList.clear();
        }
        if (SamplingList.size() >= 10){
            AverageList.add(AvgInt(SamplingList));
            SamplingList.clear();
        }
        SamplingList.add(inData);


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
    private Double AvgInt(ArrayList<Integer> intArr) {
        Integer sum = 0;
        if(!intArr.isEmpty()) {
            for(int elem : intArr) {
                sum += elem;
            }
            return sum.doubleValue() / intArr.size();
        }
        return sum.doubleValue();

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
