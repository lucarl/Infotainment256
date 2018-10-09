package infotainment.Model;

import infotainment.contract.MainActivityContract;

public class MainActivityModel implements MainActivityContract.Model{


    @Override
    public String getData() {
        DataFilter df = new DataFilter();

        df.SamplingList.add(1);
        df.SamplingList.add(2);
        df.SamplingList.add(3);
        df.SamplingList.add(4);
        df.SamplingList.add(5);
        df.AverageList.add(df.AvgInt(df.SamplingList));
        System.out.println("Sampling list" + df.SamplingList);
        System.out.println("Average of Sampling list" + df.AvgInt(df.SamplingList));
        System.out.println("Average list" + df.AverageList);
        df.SamplingList.clear();
        System.out.println("Removed all from Sampling list" + df.SamplingList);

        return "hej";
    }
}

