package org.rmj.guanzongroup.useraccount.Activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.rmj.guanzongroup.useraccount.R;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class Activity_ClientInfo_QR extends AppCompatActivity {
    private ZXingScannerView scanner;
    private String qrResult; // Variable to store QR code value
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clientifo_qr);

        scanner = findViewById(R.id.scanner);
        Dexter.withContext(this)
                .withPermission(Manifest.permission.CAMERA)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        scanner.setResultHandler(rawResult -> {

                            String qrResult = rawResult.toString();
                            String[] result = qrResult.split("»");

                            Intent loIntent = new Intent();
                            loIntent.putExtra("ClientID", result[0]);
                            loIntent.putExtra("SourceCD", result[1]);
                            loIntent.putExtra("SourceNo", result[2]);

                            setResult(1, loIntent);
                            finish();
                        });
                        scanner.startCamera();
                    }
                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        Toast.makeText(Activity_ClientInfo_QR.this, "Please allow the permission", Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                    }
                })
                .check();
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {
        // Use the stored QR code value (qrResult) as needed
        if (qrResult != null && !qrResult.isEmpty()) {
            // Do something with the QR code value
            Log.d("QR Result", qrResult);
        }
        finish();
    }
}