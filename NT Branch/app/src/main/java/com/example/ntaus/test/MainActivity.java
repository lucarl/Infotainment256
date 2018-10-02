package com.example.ntaus.test;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.LogPrinter;
import android.view.View;
import android.widget.EditText;

import static android.graphics.Color.parseColor;



public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        double lambda=6;        //  l/100km
        //double velocity=80;
        double tilt = 15;
        //int gear=5;

        changeColor(ecoCal(lambda,tilt));

    }

    public void changeColor(int x){

        final View cl = findViewById(R.id.v);

        String[] colArr = {"#66ff33","#6eff30","#75ff2e","#7dff2b","#85ff29","#8cff26","#94ff24","#9cff21","#a3ff1f","#abff1c","#b2ff1a","#baff17","#c2ff14","#c9ff12","#d1ff0f","#d9ff0d","#e0ff0a","#e8ff08","#f0ff05","#f7ff03","#ffff00","#ffff66","#fff261","#ffe65c","#ffd957","#ffcc52","#ffbf4c","#ffb247","#ffa642","#ff993d","#ff8c38","#ff8033","#ff732e","#ff6629","#ff5924","#ff4d1f","#ff401a","#ff3314","#ff260f","#ff190a","#ff0d05","#ff0000"};
        EditText mEdit = (EditText) findViewById(R.id.editText);
        mEdit.setText(colArr[x]);
        cl.setBackgroundColor(parseColor(colArr[x]));


    }

    public int ecoCal(double lambda, double tilt){

        double res;
        double ref=36;       //Ansatt referensvärde

        res = Math.pow(Math.cos(Math.toRadians(tilt)),1.5) * Math.pow(lambda,2);

        int ratio = (int)Math.round((100*res/ref)/42);


        //////LOGGAR////

        String rest = Integer.toString((int) (Math.cos(Math.toRadians(tilt*1.5))*100));
        Log.d("cosinus",rest);

        String resref = Integer.toString((int) Math.round(res/ref));
        Log.d("resref",resref);

        String numberAsString = Integer.toString(ratio);
        Log.d("Ratio",numberAsString);


        //////////////////////////

        if(ratio>41){ return 41;

        }else if(ratio<0){return 0;}

        else{return ratio;}

    }
}

