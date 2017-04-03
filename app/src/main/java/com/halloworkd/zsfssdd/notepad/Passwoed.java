package com.halloworkd.zsfssdd.notepad;

import android.Manifest;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.os.CancellationSignal;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.method.TransformationMethod;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

@RequiresApi(api = Build.VERSION_CODES.M)
public class Passwoed extends AppCompatActivity {

    EditText password_edittext = null;
    AppCompatButton btn_login;
    String password;
    CheckBox checkBox;
    FingerprintManager manager;
    KeyguardManager mKeygruard;
    TextView fingerprint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passwoed);

        btn_login = (AppCompatButton) findViewById(R.id.btn_login);
        password_edittext = (EditText) findViewById(R.id.password_input);
        checkBox = (CheckBox) findViewById(R.id.CheckBox_seepassword);
        fingerprint = (TextView) findViewById(R.id.hint);


        password = prefUtils.getString("password", "8bnwg5hd3w55iofp58khbp6yv", MyApplication.getContext());

        
        int api = Build.VERSION.SDK_INT;
        if (api >= Build.VERSION_CODES.M){
            if (isFinger()){
                fingerprint.setVisibility(View.VISIBLE);
                startListening(null);













            }else {
                fingerprint.setVisibility(View.GONE);
            }
        }else {
            fingerprint.setVisibility(View.GONE);
        }

        
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pow = password_edittext.getText().toString();
                if (pow.length() == 0){
                    password_edittext.setError(Passwoed.this.getString(R.string.password_is_null));

                }else {
                    if (pow.equals(password)){
                        MyApplication.isLock = false;
                        finish();
                    }else {
                        password_edittext.setError(Passwoed.this.getString(R.string.password_is_wrong));

                    }
                }
            }
        });

        
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked){
                    TransformationMethod method =  PasswordTransformationMethod.getInstance();
                    password_edittext.setTransformationMethod(method);
                }else {
                    HideReturnsTransformationMethod method = HideReturnsTransformationMethod.getInstance();
                    password_edittext.setTransformationMethod(method);
                }
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            MainActivity.Main_instance.finish();
            finish();
        }
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public boolean isFinger(){

        mKeygruard = (KeyguardManager) this.getSystemService(Context.KEYGUARD_SERVICE);
        manager = (FingerprintManager) this.getSystemService(Context.FINGERPRINT_SERVICE);
         
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, getString(R.string.fingerprint_permission), Toast.LENGTH_SHORT).show();
            return false;
        }

         
        if (!manager.isHardwareDetected()) {
 
            return false;
        }

         
        if (!mKeygruard.isKeyguardSecure()) {

            return false;
        }

        
        if (!manager.hasEnrolledFingerprints()) {

            return false;
        }

        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void startListening(FingerprintManager.CryptoObject crypto){
        android.os.CancellationSignal mcanellation = new android.os.CancellationSignal();
        FingerprintManager.AuthenticationCallback mCallback = new FingerprintManager.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, CharSequence errString) {
                Toast.makeText(Passwoed.this, errString, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
                Toast.makeText(Passwoed.this, helpString, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {


                MyApplication.isLock = false;
                finish();
            }

            @Override
            public void onAuthenticationFailed() {
                Toast.makeText(Passwoed.this, getString(R.string.fingerprint_fail), Toast.LENGTH_SHORT).show();
            }
        };
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED){

            return;
        }
        manager.authenticate(crypto, mcanellation, 0, mCallback, null);
    }

}
