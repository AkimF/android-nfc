package com.rivancic.nfc;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class ReadExplicitActivity extends AppCompatActivity {

    View noTagV;
    TextView tagValueTv;
    View readV;
    PendingIntent nfcPendingIntent;
    IntentFilter[] writeTagFilters;
    NfcAdapter nfcAdapter;

    @Override
    protected void onPause() {
        super.onPause();
        nfcAdapter.disableForegroundDispatch(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.read_activity);
        tagValueTv = (TextView) findViewById(R.id.nfc_value);
        noTagV = findViewById(R.id.no_nfc_tv);
        readV = findViewById(R.id.nfc_read_v);
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        nfcPendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        enableTagWriteMode();
    }

    private void enableTagWriteMode() {
        // writeMode = true;
        IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        writeTagFilters = new IntentFilter[]{tagDetected};
        nfcAdapter.enableForegroundDispatch(this, nfcPendingIntent, writeTagFilters, null);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        // Tag writing mode
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())) {
            NdefMessage[] messages = NfcUtils.getNdefMessages(intent);
            byte[] payload = messages[0].getRecords()[0].getPayload();
            String id = new String(payload);
            noTagV.setVisibility(View.GONE);
            readV.setVisibility(View.VISIBLE);
            tagValueTv.setText(id);
        }
    }
}
