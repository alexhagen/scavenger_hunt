package io.github.alexhagen.scavenger_hunt;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class qr_scan extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    public ZXingScannerView mScannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_scan);
        View v = findViewById(R.id.qrscanbox);
        Log.d("EXECUTION", "about to start the qrscanner");
        this.QrScanner(v);
        Log.d("EXECUTION", "supposedly started the qrscanner");
    }

    public void QrScanner(View view){
        mScannerView = new ZXingScannerView(this);   // Programmatically initialize the scanner view
        setContentView(mScannerView);
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();         // Start camera
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();   // Stop camera on pause
    }

    @Override
    public void handleResult(Result rawResult) {
        // Do something with the result here

        Log.e("handler", rawResult.getText()); // Prints scan results
        Log.e("handler", rawResult.getBarcodeFormat().toString()); // Prints the scan format (qrcode)

        // show the scanner result into dialog box.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Scan Result");
        String message = rawResult.getText();
        builder.setMessage(message);
        AlertDialog alert1 = builder.create();
        alert1.show();

        if (message.equals("external")) {
            Log.d("EXECUTION", "external clue");
            Intent mapIntent = new Intent(qr_scan.this, external_clue.class);
            qr_scan.this.startActivity(mapIntent);
        } else {
            Log.d("EXECUTION", "not string external");
            Log.d("VALUE", message);
        }
        // If you would like to resume scanning, call this method below:
        // mScannerView.resumeCameraPreview(this);
    }
}
