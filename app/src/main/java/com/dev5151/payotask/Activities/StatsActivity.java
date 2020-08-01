package com.dev5151.payotask.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.dev5151.payotask.R;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;

public class StatsActivity extends AppCompatActivity {

    private PieChart pieChart;
    Integer expense = 0, income = 0;
    int[] colorArray;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        Intent intent=getIntent();
        expense=intent.getIntExtra("expense",0);
        income=intent.getIntExtra("income",0);

        pieChart = findViewById(R.id.pie_chart);
        colorArray = new int[]{Color.RED, Color.GREEN};

        setData(expense,income);
    }

    private void setData(Integer expense, Integer income) {
        PieDataSet pieDataSet = new PieDataSet(dataValues(expense, income), "");
        pieDataSet.setColors(colorArray);
        PieData pieData = new PieData(pieDataSet);
        pieData.setValueTextColor(Color.WHITE);
        pieData.setValueTextSize(18);
        pieChart.setEntryLabelTextSize(20);
        pieChart.setData(pieData);
        pieChart.setDrawEntryLabels(true);
        pieChart.setUsePercentValues(false);
        pieChart.getDescription().setEnabled(false);
        pieChart.setHoleColor(30);
        pieChart.setNoDataTextColor(Color.LTGRAY);
        pieChart.setEntryLabelColor(Color.WHITE);
        pieChart.setEntryLabelTextSize(15);
        pieChart.getLegend().setEnabled(false);
        pieChart.animateY(1000, Easing.EaseInOutCubic);
        pieChart.invalidate();
    }

    private ArrayList<PieEntry> dataValues(Integer expense, Integer income) {
        ArrayList<PieEntry> dataVals = new ArrayList<>();
        dataVals.add(new PieEntry(expense, "Total Expense"));
        dataVals.add(new PieEntry(income,"Total Income"));
        return dataVals;
    }
}