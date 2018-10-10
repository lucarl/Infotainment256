package infotainment.contract;

public interface MainActivityContract {

    interface View {

        void initView();
        void setViewData(String data);
        void updateBackgroundColor(int startClr, int endClr, float ratio);
    }

    interface Model {

        String getData();
        float getEcoRatio();
        int getStartColor();
        int getEndColor();
        void setStartColor(int newStartClr);
        void setEndColor(int newEndClr);

    }

    interface Presenter {

        void onClick(android.view.View view);
        void toggleEcoDriving();
    }

}
