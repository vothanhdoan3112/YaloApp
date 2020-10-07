package com.example.yalochatapp;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;



import java.net.URISyntaxException;
import java.util.Timer;
import java.util.TimerTask;

public class RegisterActivity extends AppCompatActivity {
    private Button btnBack;
    private Button btnReg;
    private EditText txtPhoneNum, txtPassword, txtRePassword;

    private Dialog dialog;

    private final String URL_SERVER = "http://18.139.222.43:3000";

    private Socket mSocket;
    {
        try {
            mSocket = IO.socket(URL_SERVER);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSocket.connect();
        setContentView(R.layout.activity_register);
        btnBack = findViewById(R.id.btnBack);
        btnReg = findViewById(R.id.btnNext);
        txtPhoneNum = findViewById(R.id.txtPhone);
        txtPassword = findViewById(R.id.txtPassword);
        txtRePassword = findViewById(R.id.txtRePassword);

        dialog = new Dialog(RegisterActivity.this);

        /***/
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this,WelcomeActivity.class);
                startActivity(intent);
            }
        });

        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mSocket.emit("regAcc",txtPhoneNum.getText(),txtPassword.getText());



                mSocket.on("IsRegSuccess", new Emitter.Listener() {
                    @Override
                    public void call(final Object... args) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                boolean flag = (Boolean) args[0];
                                System.out.println(flag);
                                if(flag){
                                    Toast toast = Toast.makeText(RegisterActivity.this, "Đăng ký thành công", Toast.LENGTH_SHORT);
                                    toast.setGravity(Gravity.BOTTOM |Gravity.CENTER_VERTICAL,50,50);
                                    toast.show();
                                    new Timer().schedule(new TimerTask() {
                                        @Override
                                        public void run() {
                                            Intent intent = new Intent(RegisterActivity.this,WelcomeActivity.class);
                                            startActivity(intent);
                                        }
                                    }, 2000);
                                }

                            }
                        });
                    }
                });


            }
        });


    }
    private boolean isValidate() {
        if(txtPhoneNum.length()>10 || txtPhoneNum.length() < 9){
            return false;
        }
        if(txtPassword.length()>30 || txtPhoneNum.length() < 8){
            return false;
        }
        if(!txtRePassword.getText().equals(txtPassword.getText())){
            return false;
        }
        return true;
    }
    Emitter.Listener onRegComplete;
}