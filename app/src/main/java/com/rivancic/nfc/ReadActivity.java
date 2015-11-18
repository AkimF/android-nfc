package com.rivancic.nfc;

import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_read, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
