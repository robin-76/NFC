package com.example.nfc;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.NfcManager;
import android.nfc.Tag;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.InputType;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;


public class Scan extends AppCompatActivity {
    private NfcAdapter adapter;
    private PendingIntent mPendingIntent;

    private ArrayList<Etudiant> list;

    private String nom, prenom;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        Intent intent = getIntent();
        list = (ArrayList<Etudiant>) intent.getSerializableExtra("list");

        NfcManager manager = (NfcManager) this.getSystemService(Context.NFC_SERVICE);
        adapter = manager.getDefaultAdapter();

        nom = "";
        prenom = "";

        if (adapter != null) {
            if (adapter.isEnabled()) {
                if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())) {
                    Tag tagFromIntent = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
                }
            }

            mPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this,
                    getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.enableForegroundDispatch(this, mPendingIntent, null, null);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (adapter != null)
            adapter.disableForegroundDispatch(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String uid = getTagInfo(intent);
        updateHeure(uid);
    }

    private String getTagInfo(Intent intent) {
        byte[] uid = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID);
        return new BigInteger(uid).toString(16);
    }

    private void updateHeure(String uid) {
        boolean condition = false;

        for (Etudiant etudiant : list) {
            if (etudiant.getUid().equals(uid)) {
                condition = true;
                prenom = etudiant.getPrenom();
                nom = etudiant.getNom();
                finish(uid);
                break;
            }
        }

        if (!condition) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Informations de l'étudiant");

            final EditText editPrenom = new EditText(this);
            editPrenom.setHint("Prénom ...");
            final EditText editNom = new EditText(this);
            editNom.setHint("Nom ...");

            editPrenom.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
            editNom.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);

            LinearLayout layout = new LinearLayout(this);
            layout.setOrientation(LinearLayout.VERTICAL);
            layout.addView(editPrenom);
            layout.addView(editNom);
            builder.setView(layout);

            builder.setPositiveButton("Ok", (dialog, whichButton) -> {
                prenom = editPrenom.getText().toString();
                nom = editNom.getText().toString();

                finish(uid);
            });

            builder.setNegativeButton("Cancel", (dialog, whichButton) -> dialog.cancel());
            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    public void finish(String uid) {
        Calendar date = Calendar.getInstance();
        int hour = date.get(Calendar.HOUR_OF_DAY);
        int minute = date.get(Calendar.MINUTE);

        Intent intent = new Intent();
        intent.putExtra("uid", uid);
        if (minute < 10)
            intent.putExtra("heure", hour + "h0" + minute);
        else
            intent.putExtra("heure", hour + "h" + minute);
        intent.putExtra("prenom", prenom);
        intent.putExtra("nom", nom);

        setResult(RESULT_OK, intent);
        this.finish();
    }
}
