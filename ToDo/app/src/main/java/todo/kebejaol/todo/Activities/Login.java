package todo.kebejaol.todo.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import todo.kebejaol.todo.Model.LoginDBAdapter;
import todo.kebejaol.todo.R;
import todo.kebejaol.todo.Webservice.Webservice;

/**
 * Created by Jan on 25.04.16.
 *
 */
public class Login extends Activity {

    Webservice webService = new Webservice();
    final LoginDBAdapter loginDBAdapter = new LoginDBAdapter(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Global varible to get Username on all Acivities
        final User globalUsername = ((User) getApplicationContext());

        // Make Java-Objects from Layout-Fields
        final EditText etEmail = (EditText) findViewById(R.id.etEmail);
        final EditText etPassword = (EditText) findViewById(R.id.etPasswort);
        final Button bLogin = (Button) findViewById((R.id.bLogin));
        final TextView tvRegisterLink = (TextView) findViewById(R.id.tvRegister);
        final TextView tvErrorMessage = (TextView) findViewById(R.id.tvErrorMessage);
        final TextView tvConnectionError1 = (TextView) findViewById(R.id.tvConnectionError1);
        final TextView tvConnectionError2 = (TextView) findViewById(R.id.tvConnectionError2);

        // Check if there is a connection to the Webserver
        if (!webService.isConnection()) {
            tvConnectionError1.setVisibility(View.VISIBLE);
            tvConnectionError2.setVisibility(View.VISIBLE);
        }
        //Set Login Button disabled and enable it when EmailET and PasswordET are set (onTextChanged)
        bLogin.setEnabled(false);
        TextWatcher tw = new TextWatcher() {
            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tvErrorMessage.setVisibility(View.INVISIBLE);

                if (etEmail.getText().toString().length() > 0 && etPassword.getText().toString().length() > 0) {
                    bLogin.setBackgroundColor(Color.rgb(4, 180, 49));
                    bLogin.setEnabled(true);
                }
            }
        };

        //Define Keylistener for Edittext
        etEmail.addTextChangedListener(tw);
        etPassword.addTextChangedListener(tw);

        //OnClickListener fo Login Button
        if (bLogin != null) {
            bLogin.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    if (bLogin.isEnabled()) {
                        //Check if Email-Field is empty or non-Emailfield
                        boolean isValidMail = isValidMail(etEmail.getText().toString());
                        if (etEmail.getText().toString().equals("") || !isValidMail) {
                            tvErrorMessage.setText("Bitte geben Sie eine gültige E-Mail Adresse an");
                            tvErrorMessage.setVisibility(View.VISIBLE);
                        } else if (etPassword.getText().toString().equals("")) {
                            Toast toast = Toast.makeText(getApplicationContext(), R.string.login_error_empty_password, Toast.LENGTH_LONG);
                            toast.show();
                        } else {
                            //define global Username to get Username on all Activities
                            globalUsername.setUsername(etEmail.getText().toString());

                            //start AsyncTask for Login Process
                            AsyncTaskRunner runner = new AsyncTaskRunner();
                            runner.execute(etPassword.getText().toString(), etEmail.getText().toString());
                        }
                    }
                }
            });
        }

        // Start Register Intent on RegisterTV Click
        if (tvRegisterLink != null) {
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


    // Email Validator
    boolean isValidMail(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }


    // Inner AsyncTask Class for Login Button Handling
    private class AsyncTaskRunner extends AsyncTask<String, String, String> {

        private String resp;
        private boolean acceptPassword;
        ProgressDialog progressDialog;
        final TextView tvErrorMessage = (TextView) findViewById(R.id.tvErrorMessage);

        @Override
        protected String doInBackground(String... params) {
            try {
                String password = params[0];
                String user = params[1];
                loginDBAdapter.open();
                String storedPassword = loginDBAdapter.getEntry(user);
                loginDBAdapter.close();
                Thread.sleep(2000);
                if (storedPassword.equals(password)) {
                    acceptPassword = true;
                } else {
                    acceptPassword = false;
                    Toast toast = Toast.makeText(getApplicationContext(), R.string.login_error, Toast.LENGTH_LONG);
                    toast.show();
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return resp;
        }


        @Override
        protected void onPostExecute(String result) {
            if (acceptPassword) {
                Toast toast = Toast.makeText(getApplicationContext(), R.string.login_success, Toast.LENGTH_LONG);
                toast.show();
                Intent overviewIntent = new Intent(Login.this, Overview.class);
                Login.this.startActivity(overviewIntent);
            } else {
                tvErrorMessage.setText("Logindaten nicht gültig");
                tvErrorMessage.setVisibility(View.VISIBLE);
            }
            progressDialog.dismiss();
        }


        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(Login.this, "Bitte Warten", "Eingabedaten werden überprüft");
        }


        @Override
        protected void onProgressUpdate(String... text) {
        }
    }


} // class
