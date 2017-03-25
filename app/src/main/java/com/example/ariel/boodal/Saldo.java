package com.example.ariel.boodal;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.ariel.boodal.Object.SaldoObject;
import com.example.ariel.boodal.adapter.SaldoAdapter;

import java.util.ArrayList;

public class Saldo extends AppCompatActivity {

    private ArrayList<SaldoObject> allItems = new ArrayList<SaldoObject>();
    RecyclerView.LayoutManager layoutManager;
    RecyclerView recyclerView;
    SaldoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saldo);

        allItems.add(new SaldoObject("Top UP","hari ini",20000));
        initViews();
    }

        private void initViews() {
            recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
            layoutManager = new GridLayoutManager(getApplicationContext(), 1);
            recyclerView.setLayoutManager(layoutManager);
            adapter = new SaldoAdapter(allItems,getApplicationContext());
            recyclerView.setAdapter(adapter);
        }
    }
