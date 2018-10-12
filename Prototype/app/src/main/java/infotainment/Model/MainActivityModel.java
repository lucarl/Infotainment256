package infotainment.Model;

import infotainment.Model.db.logDb;
import infotainment.contract.MainActivityContract;

public class MainActivityModel implements MainActivityContract.Model{


    @Override
    public String getData() {


        return "hej";
    }

    public void dbStuff(logDb db)
    {
        DataFilter df = new DataFilter(db);
    }
}

