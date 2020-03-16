package com.example.ca2_retrofit.Passcode;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.ca2_retrofit.Core.MainActivity;
import com.example.ca2_retrofit.Fingerprint.FingerAuth;
import com.example.ca2_retrofit.R;


public class PasscodeActivity extends AppCompatActivity {

    private EditText passcodeInput;
    private Button passcodeconfirmation;
    private String passcode;
    private TextView num1, num2, num3, num4, num5, num6, num7, num8, num9, num0;
    private ImageView deleteInput, gotofingerprint, backarrow;
    ConstraintLayout mainConstraintPasscode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //make the activity on full screen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_passcode);

        //hide action bar
        getSupportActionBar().hide();

        //animation setup
        Animation SmallToBig = AnimationUtils.loadAnimation(this, R.anim.smalltobig);

        //loads the passcode
        SharedPreferences settings = getSharedPreferences("PREFS", 0);
        passcode = settings.getString("passcode", "");

        //init views
        passcodeInput = findViewById(R.id.passcodeInput);
        passcodeconfirmation = findViewById(R.id.passcodeconfirmation);
        num1 = findViewById(R.id.num1);
        num2 = findViewById(R.id.num2);
        num3 = findViewById(R.id.num3);
        num4 = findViewById(R.id.num4);
        num5 = findViewById(R.id.num5);
        num6 = findViewById(R.id.num6);
        num7 = findViewById(R.id.num7);
        num8 = findViewById(R.id.num8);
        num9 = findViewById(R.id.num9);
        num0 = findViewById(R.id.num0);
        deleteInput = findViewById(R.id.deleteInput);
        gotofingerprint = findViewById(R.id.gotofingerprint);
        backarrow = findViewById(R.id.backarrow);
        mainConstraintPasscode = findViewById(R.id.mainConstraintPasscode);

        mainConstraintPasscode.setAnimation(SmallToBig);


        num1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passcodeInput.setText(passcodeInput.getText().insert(passcodeInput.getText().length(), "1"));
            }
        });

        num2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passcodeInput.setText(passcodeInput.getText().insert(passcodeInput.getText().length(), "2"));
            }
        });

        num3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passcodeInput.setText(passcodeInput.getText().insert(passcodeInput.getText().length(), "3"));
            }
        });

        num4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passcodeInput.setText(passcodeInput.getText().insert(passcodeInput.getText().length(), "4"));
            }
        });

        num5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passcodeInput.setText(passcodeInput.getText().insert(passcodeInput.getText().length(), "5"));
            }
        });

        num6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passcodeInput.setText(passcodeInput.getText().insert(passcodeInput.getText().length(), "6"));
            }
        });

        num7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passcodeInput.setText(passcodeInput.getText().insert(passcodeInput.getText().length(), "7"));
            }
        });

        num8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passcodeInput.setText(passcodeInput.getText().insert(passcodeInput.getText().length(), "8"));
            }
        });

        num9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passcodeInput.setText(passcodeInput.getText().insert(passcodeInput.getText().length(), "9"));
            }
        });

        num0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passcodeInput.setText(passcodeInput.getText().insert(passcodeInput.getText().length(), "0"));
            }
        });

        deleteInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (passcodeInput.length() == 0) {
                    //do nothing
                } else {
                    passcodeInput.setText(passcodeInput.getText().delete(passcodeInput.getText().length()-1,passcodeInput.getText().length()));
                }


            }
        });

        gotofingerprint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent fingerprintActivity = new Intent(getApplicationContext(), FingerAuth.class);
                startActivity(fingerprintActivity);
                finish();
            }
        });

        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent fingerprintActivity = new Intent(getApplicationContext(), FingerAuth.class);
                startActivity(fingerprintActivity);
                finish();
            }
        });

        passcodeconfirmation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String PasscodeInput = passcodeInput.getText().toString();

                if (PasscodeInput.equals(passcode)) {

                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();

                } else {

                    Toast.makeText(PasscodeActivity.this, "Incorrect Passcode", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        //go to new screen
        Intent intent = new Intent(getApplicationContext(), FingerAuth.class);
        startActivity(intent);
        finish();

    }


}
