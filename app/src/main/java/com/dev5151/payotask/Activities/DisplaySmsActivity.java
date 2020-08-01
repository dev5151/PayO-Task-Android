package com.dev5151.payotask.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.dev5151.payotask.Adapters.SmsAdapter;
import com.dev5151.payotask.Models.Sms;
import com.dev5151.payotask.R;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DisplaySmsActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    List<Sms> smsArrayList;
    ExtendedFloatingActionButton extendedFloatingActionButton;
    Integer income = 0, expense = 0;
    String amtString = null;
    DatabaseReference messageRef;
    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_sms);

        recyclerView = findViewById(R.id.recycler_view);
        mAuth=FirebaseAuth.getInstance();
        messageRef = FirebaseDatabase.getInstance().getReference().child("sms");
        extendedFloatingActionButton = findViewById(R.id.fab);
        smsArrayList = fetchList();
        fetchTransactionData(smsArrayList);
        recyclerView.setAdapter(new SmsAdapter(this, smsArrayList));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        extendedFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), StatsActivity.class);
                intent.putExtra("expense", expense);
                intent.putExtra("income", income);
                startActivity(intent);
            }
        });
    }

    private List<Sms> fetchList() {
        Intent intent = getIntent();
        Bundle args = intent.getBundleExtra("BUNDLE");
        ArrayList<Sms> smsList = (ArrayList<Sms>) args.getSerializable("ARRAYLIST");
        return smsList;
    }

    private void fetchTransactionData(List<Sms> smsList) {

        for (Sms sms : smsList) {
            String body = sms.getMsg();
            if (body.toLowerCase().contains("debited") || body.toLowerCase().contains("spent")) {
                Pattern p = Pattern.compile("Rs.\\s[0-9]+");
                Pattern p1 = Pattern.compile("Rs.\\s[0-9]+.00");
                Matcher m = p.matcher(body);
                if (m.find()) {
                    amtString = m.group(0);
                    expense = expense + Integer.parseInt(amtString.substring(4));
                }
                Matcher matcher = p1.matcher(body);
                if (matcher.find()) {
                    amtString = m.group(0);
                    expense = expense + Integer.parseInt(amtString.substring(4));
                }
            } else if (body.toLowerCase().contains("credited")) {
                Pattern p = Pattern.compile("Rs.\\s[0-9]+");
                Pattern p1 = Pattern.compile("Rs.\\s[0-9]+.00");
                Matcher m = p.matcher(body);
                if (m.find()) {
                    amtString = m.group(0);
                    income = income + Integer.parseInt(amtString.substring(4));
                }
                Matcher matcher = p1.matcher(body);
                if (matcher.find()) {
                    amtString = m.group(0);
                    income = income + Integer.parseInt(amtString.substring(4));
                }
            }
        }
        Log.i("Expense", expense + "");
        Log.i("Income", income + "");
    }

    private void fetchSmsFromServer() {
        final List<Sms> list = new ArrayList<>();
        messageRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        Sms sms = dataSnapshot1.getValue(Sms.class);
                        list.add(sms);
                    }
                } else {
                    list.addAll(fetchList());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}