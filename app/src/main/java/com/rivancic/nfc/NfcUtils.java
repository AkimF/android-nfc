package com.rivancic.nfc;

import android.content.Context;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.Parcelable;

import java.io.IOException;
import java.util.logging.Logger;

/**
 * The utils class for reading and writing data with the NFC protocol.
 * Created by rivancic on 01/11/15.
 */
public class NfcUtils {

    private static Logger LOG = Logger.getLogger(NfcUtils.class.getName());

    /**
     *
     * @return true if this device has NfcAdapter support this means it supports NFC technology,
     * otherwise return false.
     */
    public static boolean hasNFCSupport(Context context) {
        boolean result = true;
        NfcAdapter myNfcAdapter = NfcAdapter.getDefaultAdapter(context);
        if (myNfcAdapter == null) {
            result = false;
        }
        return result;
    }

    public static NdefMessage[] getNdefMessages(Intent intent) {
        // Parse the intent
        NdefMessage[] msgs = null;
        String action = intent.getAction();
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
            Parcelable[] rawMsgs =
                    intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            if (rawMsgs != null) {
                msgs = new NdefMessage[rawMsgs.length];
                for (int i = 0; i < rawMsgs.length; i++) {
                    msgs[i] = (NdefMessage) rawMsgs[i];
                }
            } else {
                // Unknown tag type
                byte[] empty = new byte[]{};
                NdefRecord record =
                        new NdefRecord(NdefRecord.TNF_UNKNOWN, empty, empty, empty);
                NdefMessage msg = new NdefMessage(new NdefRecord[]{
                        record
                });
                msgs = new NdefMessage[]{
                        msg
                };
            }
        } else {
            LOG.severe("Unknown intent.");
        }
        return msgs;
    }

    /**
     * Writes an NdefMessage to a NFC tag
     *
     * @param message that should be written to the tag.
     * @param tag
     * @return
     */
    public static boolean writeTag(NdefMessage message, Tag tag) {
        int size = message.toByteArray().length;
        try {
            Ndef ndef = Ndef.get(tag);
            if (ndef != null) {
                ndef.connect();
                if (!ndef.isWritable()) {
                    return false;
                }
                if (ndef.getMaxSize() < size) {
                    return false;
                }
                ndef.writeNdefMessage(message);
                return true;
            } else {
                NdefFormatable format = NdefFormatable.get(tag);
                if (format != null) {
                    try {
                        format.connect();
                        format.format(message);
                        return true;
                    } catch (IOException e) {
                        return false;
                    }
                } else {
                    return false;
                }
            }
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Converts a String into a NdefMessage in application/com.rivancic.nfc MIMEtype.
     * <p/>
     * for writing Places
     */
    public static NdefMessage getMessageAsNdef(String messageToWrite) {

        byte[] textBytes = messageToWrite.getBytes();
        NdefRecord textRecord = new NdefRecord(NdefRecord.TNF_MIME_MEDIA,
                "application/com.rivancic.nfc".getBytes(), new byte[]{}, textBytes);
        return new NdefMessage(new NdefRecord[]{textRecord});
    }
}
