package infotainment.Model;

import java.util.ArrayList;
import java.util.Arrays;

public class DataFilter {

    ArrayList<Integer> SamplingList;
    ArrayList<Double> AverageList;


    public DataFilter() {
        SamplingList = new ArrayList<>(); //Arrays.asList(1,2,3,4,5)
        AverageList = new ArrayList<>();

    }
    public Double AvgDouble(ArrayList<Double> intArr) {
        Double sum = 0.0;
        if(!intArr.isEmpty()) {
            for(Double elem : intArr) {
                sum += elem;
            }
            return sum / intArr.size();
        }
        return sum;

    }
    public Double AvgInt(ArrayList<Integer> intArr) {
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

    */

}
