package com.rivancic.nfc.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.rivancic.nfc.NfcUtils;
import com.rivancic.nfc.R;

public class MainActivity extends AppCompatActivity {

    Button openReadExplicitBtn;
    Button openWriteBtn;
    Button openWriteVCardBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        openReadExplicitBtn = (Button) findViewById(R.id.open_read_explicit_btn);
        openWriteBtn = (Button) findViewById(R.id.open_write_btn);
        openWriteVCardBtn = (Button) findViewById(R.id.open_write_vcard_btn);
        openWriteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, WriteActivity.class));
            }
        });
        openWriteVCardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, WriteVCardActivity.class));
            }
        });
        openReadExplicitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ReadExplicitActivity.class));
            }
        });

        if(!NfcUtils.hasNFCSupport(this)) {
            Toast.makeText(this, "NFC is not available for the device.", Toast.LENGTH_LONG).show();
            openReadExplicitBtn.setVisibility(View.GONE);
            openWriteBtn.setVisibility(View.GONE);
        }
    }
}
