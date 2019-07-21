package com.example.twitterclone;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.shashank.sony.fancytoastlib.FancyToast;

public class MainActivity extends AppCompatActivity
{
    private EditText edtLogin_Email,edtLogin_password;
    private Button btnLogin;
    private TextView txtSignUp;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtLogin_Email=(EditText)findViewById(R.id.edt_name);
        edtLogin_password=(EditText)findViewById(R.id.edt_password);
        btnLogin=(Button)findViewById(R.id.button);
        txtSignUp=(TextView)findViewById(R.id.textViewSignup);

        edtLogin_password.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode== KeyEvent.KEYCODE_ENTER
                        && event.getAction()==KeyEvent.ACTION_DOWN){
                    onClick(v);

                }
                return false;
            }
        });


        txtSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,SignUpActivity.class);
                startActivity(intent);
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new ProgressDialog(MainActivity.this);
                dialog.setMessage("please wait...");
                dialog.show();
                if (edtLogin_Email.getText().toString().equals("")){
                    FancyToast.makeText(MainActivity.this,"Email is mandatory!",FancyToast.LENGTH_SHORT,FancyToast.INFO,true).show();
                    dialog.dismiss();
                }
                else if (edtLogin_password.getText().toString().equals("")){
                    FancyToast.makeText(MainActivity.this,"Password is mandatory!",FancyToast.LENGTH_SHORT,FancyToast.INFO,true).show();
                    dialog.dismiss();
                }

                else {
                    ParseUser.logInInBackground(edtLogin_Email.getText()
                                    .toString(), edtLogin_password.getText().toString(),
                            new LogInCallback() {
                                @Override
                                public void done(ParseUser user, ParseException e) {
                                    if (user != null && e == null) {
                                        FancyToast.makeText(MainActivity.this, user.get("username") + " is Successfully SignUp", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, true).show();
                                        Intent intent = new Intent(MainActivity.this, UsersActivity.class);
                                        startActivity(intent);
                                        dialog.dismiss();
                                    } else {
                                        FancyToast.makeText(MainActivity.this, e.getMessage(), FancyToast.LENGTH_LONG, FancyToast.ERROR, true).show();

                                    }
                                    dialog.dismiss();

                                }
                            });
                }
            }
        });

    }

    private void onClick(View v)
    {

    }

    public void backgroundClick(View view){
        try {
            InputMethodManager input_method_service = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            input_method_service.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

}
