package com.rivancic.nfc;

import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Activity that enables to write the vCard data to the NFC tag.
 * https://en.wikipedia.org/wiki/VCard#vCard_4.0
 */
public class WriteVCardActivity extends AppCompatActivity {

    Button writeBtn;
    EditText lastNameEt;
    EditText firstNameEt;
    EditText emailEt;
    EditText titleEt;
    EditText urlEt;
    String messageToWrite;
    boolean writeMode;
    NfcAdapter nfcAdapter;
    PendingIntent nfcPendingIntent;
    IntentFilter[] writeTagFilters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.write_vcard_activity);
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        firstNameEt = (EditText) findViewById(R.id.first_name_et);
        lastNameEt = (EditText) findViewById(R.id.last_name_et);
        emailEt = (EditText) findViewById(R.id.email_et);
        titleEt = (EditText) findViewById(R.id.title_et);
        urlEt = (EditText) findViewById(R.id.url_et);
        writeBtn = (Button) findViewById(R.id.write_btn);
        writeBtn.setOnClickListener(new ButtonWriteClick());
        nfcPendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
    }


    private String builVCardNfcMessage() {

        String result = "";
        String beginVcard = "BEGIN:VCARD\n";
        String versionVcard = "VERSION:4.0\n";
        String nameVcard = String.format("N:%s;%s\n", lastNameEt.getText().toString(), firstNameEt.getText().toString());
        String emailVcard = String.format("EMAIL:%s\n",emailEt.getText().toString());
        String titleVcard = String.format("TITLE:%s\n",titleEt.getText().toString());
        String base64ImageData = "";
        String photoVcard = String.format("PHOTO:data:image/jpeg;base64,%s\n", base64ImageData);
        String urlVcard = String.format("URL:%s\n", urlEt.getText().toString());
        String endVcard = "END:VCARD";

        result = beginVcard +
                versionVcard +
                nameVcard +
                emailVcard +
                titleVcard +
                urlVcard +
                endVcard;

        return result;
    }

    class ButtonWriteClick implements View.OnClickListener {

        @Override
        public void onClick(View v) {

            // NFC: Write id to tag
            messageToWrite = builVCardNfcMessage();
            enableTagWriteMode();

            new AlertDialog.Builder(WriteVCardActivity.this).setTitle("Touch tag to write")
                    .setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            disableTagWriteMode();
                        }
                    }).create().show();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        // Tag writing mode
        if (writeMode && NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())) {
            Tag detectedTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            // "application/com.rivancic.nfc"
            if (NfcUtils.writeTag(NfcUtils.getMediaMessageAsNdef(messageToWrite, "text/vcard"),
                    detectedTag)) {
                Toast.makeText(this, "Success: Wrote placeid to nfc tag", Toast.LENGTH_LONG)
                        .show();
                // NfcUtils.soundNotify(this);
            } else {
                Toast.makeText(this, "Write failed", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void disableTagWriteMode() {
        writeMode = false;
        nfcAdapter.disableForegroundDispatch(this);
    }

    private void enableTagWriteMode() {
        writeMode = true;
        IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        writeTagFilters = new IntentFilter[]{tagDetected};
        nfcAdapter.enableForegroundDispatch(this, nfcPendingIntent, writeTagFilters, null);
    }
}
