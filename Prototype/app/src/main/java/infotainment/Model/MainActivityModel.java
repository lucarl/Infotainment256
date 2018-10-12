package infotainment.Model;

import infotainment.contract.MainActivityContract;

public class MainActivityModel implements MainActivityContract.Model{


    @Override
    public String getData() {

        DataFilter df = new DataFilter();

        return "hej";
    }
}

