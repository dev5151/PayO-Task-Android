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
    Double expense = 0.0, income = 0.0;
    int[] colorArray;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        Intent intent=getIntent();
        expense=intent.getDoubleExtra("expense",0);
        income=intent.getDoubleExtra("income",0);

        pieChart = findViewById(R.id.pie_chart);
        colorArray = new int[]{Color.RED, Color.GREEN};

        setData(expense,income);
    }

    private void setData(Double expense, Double income) {
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

    private ArrayList<PieEntry> dataValues(Double expense, Double income) {
        ArrayList<PieEntry> dataVals = new ArrayList<>();
        dataVals.add(new PieEntry(expense.floatValue(), "Total Expense"));
        dataVals.add(new PieEntry(income.floatValue(),"Total Income"));
        return dataVals;
    }
}