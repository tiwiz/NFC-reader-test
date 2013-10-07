package mobile.tiwiz.NFC_Reader_Test;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.widget.TextView;
import org.ndeftools.Message;
import org.ndeftools.Record;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.List;

public class NFCActivity extends Activity {
    /**
     * Called when the activity is first created.
     */

    TextView txtResults;
    NfcAdapter nfcAdapter;
    PendingIntent nfcPendingIntent;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        txtResults = (TextView) findViewById(R.id.txtNFCResults);

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        nfcPendingIntent = PendingIntent.getActivity(this,0, new Intent(this, this.getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),0);

    }

    public void enableForegroundMode(){

        IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        IntentFilter[] tagFilters = new IntentFilter[]{tagDetected};
        nfcAdapter.enableForegroundDispatch(this,nfcPendingIntent,tagFilters,null);

        Log.d("TAG_TEST","***********************\nAbilitato il foreground mode\n***********************");

    }

    public void disableForegroundMode(){

        nfcAdapter.disableForegroundDispatch(this);
        Log.d("TAG_TEST","***********************\nDisabilitato il foreground mode\n***********************");

    }

    @Override
    public void onResume(){
        super.onResume();
        enableForegroundMode();
    }

    @Override
    public void onPause(){
        super.onPause();
        disableForegroundMode();
    }

    @Override
    public void onNewIntent(Intent intent) { // this method is called when an NFC tag is scanned

        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())) {
            //takes NDEF Messages received in input
            Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            if(null != rawMsgs){
                NdefMessage message = (NdefMessage) rawMsgs[0];
                byte[] payload = message.getRecords()[0].getPayload();

                String strPayload = new String(payload, Charset.forName("UTF-8"));
                if(strPayload.contains("arteatesina.it")){
                    String output = "";
                    String[] temp = strPayload.split("=");
                    //checks if id has been provided
                    if(temp.length>1){
                        String weddingID = temp[1];
                        output += ("***********************\nFormato valido\n***********************");
                        output += ("ID: " + weddingID);
                    }else{
                        output += ("***********************\nFormato NON valido\n***********************");
                    }
                    Log.d("TAG_TEST","Output:\n\n" + output);
                }else
                    Log.d("TAG_TEST","NO!");
            }
        } else {
            // ignore
        }
    }
}
