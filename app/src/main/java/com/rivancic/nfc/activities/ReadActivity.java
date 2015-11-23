package com.rivancic.nfc.activities;

import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.rivancic.nfc.NfcUtils;
import com.rivancic.nfc.R;

/**
 * This activity will be invoked with the Intent dispatch system. If no other activity has higher
 * priority of getting the NFC Intent. The filter is more specific.
 */
public class ReadActivity extends AppCompatActivity {

    View noTagV;
    TextView tagValueTv;
    View readV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.read_activity);
        tagValueTv = (TextView) findViewById(R.id.nfc_value);
        noTagV = findViewById(R.id.no_nfc_tv);
        readV = findViewById(R.id.nfc_read_v);
    }

    /**
     * The value of the intent is set by the dispatch system. It should carry the NDEF message
     * because the intent filter definition form the Android Manifest file.
     */
    @Override
    protected void onResume() {
        super.onResume();
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
            NdefMessage[] messages = NfcUtils.getNdefMessages(getIntent());
            byte[] payload = messages[0].getRecords()[0].getPayload();
            String id = new String(payload);
            noTagV.setVisibility(View.GONE);
            readV.setVisibility(View.VISIBLE);
            tagValueTv.setText(id);
        }
    }
}
