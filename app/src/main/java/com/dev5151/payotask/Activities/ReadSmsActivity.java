package com.dev5151.payotask.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.dev5151.payotask.Models.Sms;
import com.dev5151.payotask.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ReadSmsActivity extends AppCompatActivity {

    Button fetchMessage;
    List<Sms> smsList = new ArrayList<>();
    DatabaseReference messageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_s_m_s);

        fetchMessage = findViewById(R.id.fetch_message);
        messageRef = FirebaseDatabase.getInstance().getReference().child("sms");

        fetchMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (checkSelfPermission(Manifest.permission.READ_SMS)!= PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.READ_SMS},1);
                    return;
                }else{
                    Intent intent = new Intent(getApplicationContext(), DisplaySmsActivity.class);
                    Bundle args = new Bundle();
                    smsList = getAllSms();
                    pushToServer(smsList);
                    args.putSerializable("ARRAYLIST", (Serializable) smsList);
                    intent.putExtra("BUNDLE", args);
                    startActivity(intent);
                    finish();
                }

            }
        });

    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(getApplicationContext(), DisplaySmsActivity.class);
                    Bundle args = new Bundle();
                    smsList = getAllSms();
                    pushToServer(smsList);
                    args.putSerializable("ARRAYLIST", (Serializable) smsList);
                    intent.putExtra("BUNDLE", args);
                    startActivity(intent);
                    finish();

                } else {

                    Toast.makeText(getApplicationContext(),"Access to read messages needed",Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'switch' lines to check for other
            // permissions this app might request
        }
    }

    public List<Sms> getAllSms() {
        List<Sms> lstSms = new ArrayList<Sms>();
        List<Sms> financeSmsList = new ArrayList<>();

        Sms objSms = new Sms();
        Uri message = Uri.parse("content://sms/inbox");
        ContentResolver cr = getContentResolver();

        Cursor c = cr.query(message, null, null, null, null);
        c.moveToFirst();
        int totalSMS = c.getCount();

        if (c.moveToFirst()) {
            for (int i = 0; i < totalSMS; i++) {

                objSms = new Sms();
                objSms.setId(c.getString(c.getColumnIndexOrThrow("_id")));
                objSms.setAddress(c.getString(c
                        .getColumnIndexOrThrow("address")));
                objSms.setMsg(c.getString(c.getColumnIndexOrThrow("body")));
                objSms.setReadState(c.getString(c.getColumnIndex("read")));
                objSms.setTime(c.getString(c.getColumnIndexOrThrow("date")));
                if (c.getString(c.getColumnIndexOrThrow("type")).contains("1")) {
                    objSms.setFolderName("inbox");
                } else {
                    objSms.setFolderName("sent");
                }
                objSms.set_tag(null);

                //lstSms.add(objSms);

                if (objSms.getMsg().toLowerCase().contains("spent") || objSms.getMsg().toLowerCase().contains("debited")
                        || objSms.getMsg().toLowerCase().contains("credited")) {

                    financeSmsList.add(objSms);
                }

                c.moveToNext();
            }
        }
        c.close();

        return financeSmsList;
    }

    private void pushToServer(final List<Sms> messageList) {
        final List<Sms> newList = new ArrayList<>();
        final List<Sms>list=new ArrayList<>();
        messageRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        Sms sms = dataSnapshot1.getValue(Sms.class);
                        list.add(sms);
                    }
                    newList.addAll(list);
                    for (Sms message : messageList) {
                        if (!list.contains(message)) {
                            newList.add(message);
                        }
                    }
                    messageRef.setValue(newList);

                } else {
                    messageRef.setValue(messageList);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}
