package com.example.ca2_retrofit.Fingerprint;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.CancellationSignal;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import com.example.ca2_retrofit.Core.MainActivity;

@RequiresApi(api = Build.VERSION_CODES.M)
class FingerprintHandler extends FingerprintManager.AuthenticationCallback {

    private Context context;
    private MainActivity mActivity;


    public FingerprintHandler(Context context) {
        this.context = context;
    }

    public void startAuth(FingerprintManager fingerprintManager, FingerprintManager.CryptoObject cryptoObject) {

        CancellationSignal cancellationSignal = new CancellationSignal();
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED)
            return;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            fingerprintManager.authenticate(cryptoObject, cancellationSignal, 0, this, null);
        }
    }

    @Override
    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
        super.onAuthenticationSucceeded(result);

        //If authentication is successful, start the MainActivity class which starts the News Fragment
        Toast.makeText(context, "Authenticated", Toast.LENGTH_SHORT).show();
        context.startActivity(new Intent(context, MainActivity.class));

    }


    @Override
    public void onAuthenticationFailed() {
        super.onAuthenticationFailed();
        Toast.makeText(context, "Fingerprint Not Recognised", Toast.LENGTH_LONG).show();
    }




}
