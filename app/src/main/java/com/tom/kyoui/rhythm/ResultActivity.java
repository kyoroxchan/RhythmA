package com.tom.kyoui.kyoroxchan;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class ResultActivity extends AppCompatActivity {

    private ArrayList<Integer> mTapTimeList1;
    private ArrayList<Integer> mTapTimeList2;
    private Button newTrak;
    private Button oldTrak;

    TextView resultTextView,resultTextView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        resultTextView = (TextView) findViewById(R.id.resultTextView);
        resultTextView2 = (TextView) findViewById(R.id.resultTextView2);
        newTrak = (Button) findViewById(R.id.button4);
        oldTrak = (Button) findViewById(R.id.button3);

        Bundle extras = getIntent().getExtras();
        mTapTimeList1 = extras.getIntegerArrayList("1");
        mTapTimeList2 = extras.getIntegerArrayList("2");

        for (int i = 0; i < mTapTimeList1.size(); i++) {
            Log.d("mTapTimeList1", i + ":" + mTapTimeList1.get(i));
        }

        for (int i = 0; i < mTapTimeList2.size(); i++) {
            Log.d("mTapTimeList2", i + ":" + mTapTimeList2.get(i));
        }

        double result = dtw();
        String formatedresult = String.format("%.2f", result);
        resultTextView.setText(formatedresult + "%");

        if(result == 100){

            resultTextView2.setText("一心同体");

        }else if(result >= 80 && result < 100){

            resultTextView2.setText("最高の相性");

        }else if(result >= 50 && result < 80){

            resultTextView2.setText("いい感じの相性");

        }else if(result >= 20 && result < 50){

            resultTextView2.setText("それなりの相性");

        }else if(result >=1 && result < 20){

            resultTextView2.setText("悲しい相性");

        }else if(result >=0 && result < 1){

            resultTextView2.setText("最悪の相性");

        }

    }

    public void newTrak(View v) {
        Intent data = new Intent();
        data.putExtra("isnewtrak", 1);
        setResult(RESULT_OK, data);
        finish();
    }

    public void oldTrak(View v) {
        Intent data = new Intent();
        data.putExtra("isoldtrak", 2);
        setResult(RESULT_OK, data);
        finish();
    }

    private double dtw() {
        int len1 = mTapTimeList1.size() + 1;
        int len2 = mTapTimeList2.size() + 1;
        double inf = 9999999;
        double[][] matrix = new double[len1][len2];
        // 初期化
        for (int i = 0; i < len1; i++) {
            for (int j = 0; j < len2; j++) {
                matrix[i][j] = 0;
            }
        }

        //縦横をinfで埋める
        for (int i = 0; i < len2; i++) {
            matrix[0][i] = inf;
        }
        for (int i = 0; i < len1; i++) {
            matrix[i][0] = inf;
        }
        matrix[0][0] = 0;

        double sum = 0;

        //costの計算
        for (int i = 0; i < len1 - 1; i++) {
            for (int j = 0; j < len2 - 1; j++) {
                double x = (double) mTapTimeList1.get(i) / 10000.0;
                double y = (double) mTapTimeList2.get(j) / 10000.0;
                sum += 1 / (Math.abs(x - y) + 1);
//
//                double cost = Math.sqrt(x * x + y * y);
//                double value = Math.min(matrix[i][j + 1], matrix[i + 1][j]);
//                double min = Math.min(value, matrix[i][j]);
//                matrix[i + 1][j + 1] = cost + min;
            }
        }
        double cost2 = matrix[len1 - 1][len2 - 1];
        return sum / ((len1 - 1) * (len2 - 1)) *100;
    }

}
