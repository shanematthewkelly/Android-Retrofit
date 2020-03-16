package com.example.ca2_retrofit.Fingerprint;

import android.Manifest;
import android.app.KeyguardManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.ca2_retrofit.Passcode.HandlerActivity;
import com.example.ca2_retrofit.R;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public class FingerAuth extends AppCompatActivity {

    private KeyStore keyStore;
    private static final String KEY_NAME="ClimAwareFingerPrint";
    private Cipher cipher;
    private ConstraintLayout fingerprintCanvas;
    private TextView usepasscode, linktoface;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle myToggle;
    private ImageView backarrow2;

    @RequiresApi(api = Build.VERSION_CODES.M)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //make the activity on full screen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_finger_auth);





        //hide action bar
        getSupportActionBar().hide();

        Animation SmallToBig = AnimationUtils.loadAnimation(this, R.anim.smalltobig);

        fingerprintCanvas = findViewById(R.id.fingerprintCanvas);
        fingerprintCanvas.setAnimation(SmallToBig);
        usepasscode = findViewById(R.id.usepasscode);


        usepasscode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent usePasscode = new Intent(getApplicationContext(), HandlerActivity.class);
                startActivity(usePasscode);
                finish();
            }
        });

        KeyguardManager keyguardManager = (KeyguardManager)getSystemService(KEYGUARD_SERVICE);
        FingerprintManager fingerprintManager = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            fingerprintManager = (FingerprintManager)getSystemService(FINGERPRINT_SERVICE);
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED)

            return;

        if (!fingerprintManager.isHardwareDetected())
            Toast.makeText(this, "Fingerprint authertication not enabled", Toast.LENGTH_SHORT).show();

        else {
            if (!fingerprintManager.hasEnrolledFingerprints())
                Toast.makeText(this, "Register at least one fingeerprint in Settings", Toast.LENGTH_SHORT).show();

            else {
                if (!keyguardManager.isKeyguardSecure())
                    Toast.makeText(this, "Lock screen security not enabled in Settings", Toast.LENGTH_SHORT).show();
                else {
                    generateKey();

                    if (cipherInit()){

                        FingerprintManager.CryptoObject cryptoObject = new FingerprintManager.CryptoObject(cipher);
                        FingerprintHandler helper = new FingerprintHandler(this);
                        helper.startAuth(fingerprintManager, cryptoObject);


                    }
                }
            }
        }

    }




    private boolean cipherInit() {

        try {
            cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES+"/"+KeyProperties.BLOCK_MODE_CBC+"/"+KeyProperties.ENCRYPTION_PADDING_PKCS7);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }

            try {
                keyStore.load(null);
                SecretKey key = (SecretKey)keyStore.getKey(KEY_NAME, null);
                cipher.init(Cipher.ENCRYPT_MODE, key);
                return true;

            } catch (IOException ex) {
                ex.printStackTrace();
                return false;

            } catch (NoSuchAlgorithmException ex) {
                ex.printStackTrace();
                return false;

            } catch (UnrecoverableKeyException ex) {
                ex.printStackTrace();
                return false;

            } catch (KeyStoreException ex) {
                ex.printStackTrace();
                return false;

            } catch (InvalidKeyException ex) {
                ex.printStackTrace();
                return false;

            } catch (CertificateException ex) {
                ex.printStackTrace();
                return false;
            }

        }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void generateKey() {

        try {
            keyStore = KeyStore.getInstance("AndroidKeyStore");
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }

        KeyGenerator keyGenerator = null;

        try {
            keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        }

        try {
            keyStore.load(null);
            keyGenerator.init(new KeyGenParameterSpec.Builder(KEY_NAME, KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    .setUserAuthenticationRequired(true)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7).build()
            );
            keyGenerator.generateKey();

        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        catch (InvalidAlgorithmParameterException e) {

            e.printStackTrace();
        }


    }

    @Override
    public void onBackPressed() {

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();

        }
    }



}

