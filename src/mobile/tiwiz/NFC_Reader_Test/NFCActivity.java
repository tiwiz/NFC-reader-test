package mobile.tiwiz.NFC_Reader_Test;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

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
            txtResults.setText("TAG Rilevato\n***********************");
        } else {
            // ignore
        }
    }
}
