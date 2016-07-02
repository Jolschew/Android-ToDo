package todo.kebejaol.todo.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import todo.kebejaol.todo.Database.LoginDBAdapter;
import todo.kebejaol.todo.R;
import todo.kebejaol.todo.Webservice.Webservice;

public class Login extends AppCompatActivity{

    //private ProcessDialog progressDialog;

    Webservice webService = new Webservice();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Global varible to get Username on all Acivities
        final User globalUsername = ((User)getApplicationContext());



        final LoginDBAdapter loginDBAdapter = new LoginDBAdapter(this);
        final EditText etEmail = (EditText) findViewById(R.id.etEmail);
        final EditText etPassword = (EditText) findViewById(R.id.etPasswort);
        final Button bLogin = (Button) findViewById((R.id.bLogin));
        final TextView tvRegisterLink = (TextView) findViewById(R.id.tvRegister);
        final TextView tvErrorMessage = (TextView) findViewById(R.id.tvErrorMessage);
        final TextView tvConnectionError1 = (TextView) findViewById(R.id.tvConnectionError1);
        final TextView tvConnectionError2 = (TextView) findViewById(R.id.tvConnectionError2);

        // Check if there is a connection to the Webserver
        if(!webService.isConnection())
        {
            tvConnectionError1.setVisibility(View.VISIBLE);
            tvConnectionError2.setVisibility(View.VISIBLE);
        }

        bLogin.setEnabled(false);
        TextWatcher tw = new TextWatcher() {
            public void afterTextChanged(Editable s){

            }
            public void  beforeTextChanged(CharSequence s, int start, int count, int after){

            }
            public void  onTextChanged (CharSequence s, int start, int before,int count) {
                tvErrorMessage.setVisibility(View.INVISIBLE);

                if (etEmail.getText().toString().length() > 0 && etPassword.getText().toString().length() > 0) {
                    bLogin.setBackgroundColor(Color.rgb(4,180,49));
                    bLogin.setEnabled(true);

                }
            }
        };

        //Define Kelistener for Edittext
        etEmail.addTextChangedListener(tw);
        etPassword.addTextChangedListener(tw);
        // TODO hack loeschen!
        etEmail.setText("jolschew@web.de");
        etPassword.setText("111111");

        if (bLogin != null) {

            bLogin.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    if(bLogin.isEnabled()) {
                        //Check if Email-Field is empty or non-Emailfield
                        boolean isValidMail = isValidMail(etEmail.getText().toString());
                        if (etEmail.getText().toString().equals("") || !isValidMail) {
                            tvErrorMessage.setVisibility(View.VISIBLE);
                        } else if (etPassword.getText().toString().equals("")) {
                            Toast toast = Toast.makeText(getApplicationContext(), R.string.login_error_empty_password, Toast.LENGTH_LONG);
                            toast.show();
                        } else {
                            // get saved password from db by email
                            loginDBAdapter.open();
                            String storedPassword = loginDBAdapter.getEntry(etEmail.getText().toString());

                            if (storedPassword.equals(etPassword.getText().toString())) {
                                globalUsername.setUsername(etEmail.getText().toString());

                                // TODO send User ID to Overview and save it with To-Dos
                                Toast toast = Toast.makeText(getApplicationContext(), R.string.login_success, Toast.LENGTH_LONG);
                                toast.show();
                                Intent overviewIntent = new Intent(Login.this, Overview.class);
                                Login.this.startActivity(overviewIntent);
                            } else {
                                Toast toast = Toast.makeText(getApplicationContext(), R.string.login_error, Toast.LENGTH_LONG);
                                toast.show();
                            }
                        }
                    }
                }
            });
        }

        if(tvRegisterLink != null){

            // Create Intent to Register View
            tvRegisterLink.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent registerIntent = new Intent(Login.this, Register.class);
                    Login.this.startActivity(registerIntent);
                }
            });

        }
    } // onCreate

    /* TODO
    @Override
    protected void onDestroy(){
        super.onDestroy();
        loginDBAdapter.close();
    }
    */

    // Email Validator
    boolean isValidMail(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }


} // class
