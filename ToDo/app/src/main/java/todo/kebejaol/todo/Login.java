package todo.kebejaol.todo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Login extends AppCompatActivity{



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final LoginDBAdapter loginDBAdapter = new LoginDBAdapter(this).open();

        final EditText etEmail = (EditText) findViewById(R.id.etEmail);
        final EditText etPassword = (EditText) findViewById(R.id.etPasswort);
        final Button bLogin = (Button) findViewById((R.id.bLogin));
        final TextView tvRegisterLink = (TextView) findViewById(R.id.tvRegister);

        // TODO hack loeschen!
        etEmail.setText("a@a.a");
        etPassword.setText("111111");

        if (bLogin != null) {

            bLogin.setOnClickListener(new View.OnClickListener(){

                public void onClick(View view){

                    String email = etEmail.getText().toString();
                    String password = etPassword.getText().toString();

                    if(email.equals("") || password.equals("")){

                    } else {

                        // get saved password from db by email
                        String storedPassword = loginDBAdapter.getEntry(email);

                        if(storedPassword.equals(password)){

                            Intent overviewIntent = new Intent(Login.this, Overview.class);
                            Login.this.startActivity(overviewIntent);

                        } else {

                            Toast toast = Toast.makeText(getApplicationContext(), R.string.login_error, Toast.LENGTH_LONG);
                            toast.show();

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

} // class
