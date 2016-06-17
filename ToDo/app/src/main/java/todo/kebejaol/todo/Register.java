package todo.kebejaol.todo;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

public class Register extends AppCompatActivity {


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final LoginDBAdapter loginDBAdapter = new LoginDBAdapter(this).open();

        final EditText etEmail = (EditText) findViewById(R.id.etEmail);
        final EditText etPassword = (EditText) findViewById(R.id.etPasswort);
        final EditText etPassword2 = (EditText) findViewById(R.id.etPasswort2);
        final TextView tvRegisterError = (TextView) findViewById(R.id.tvRegisterError);
        final TextView tvLoginLink = (TextView) findViewById(R.id.tvLogin);
        final Button bRegister = (Button) findViewById(R.id.bRegister);

        if (bRegister != null) {

            bRegister.setOnClickListener(new View.OnClickListener() {

                public void onClick(View view){

                    String email = etEmail.getText().toString().trim();
                    String password = etPassword.getText().toString();
                    String password2 = etPassword2.getText().toString();

                    if(email.equals("") || password.equals("") || password2.equals("")){

                        tvRegisterError.setText(R.string.registration_error_empty_string);
                        tvRegisterError.setVisibility(View.VISIBLE);

                    } else if(password.length() != 6){ // TODO magic number

                        tvRegisterError.setText(R.string.registration_error_invalid_password_length);
                        tvRegisterError.setVisibility(View.VISIBLE);

                    } else if( ! password.equals(password2)){

                        tvRegisterError.setText(R.string.registration_error_password_equals);
                        tvRegisterError.setVisibility(View.VISIBLE);

                    } else {

                        // write user data in db
                        // TODO error handling
                        loginDBAdapter.insertEntry(email, password);

                        Toast toast = Toast.makeText(getApplicationContext(), R.string.registration_info_successful_registration, Toast.LENGTH_LONG);
                        toast.show();

                        // change activity to login or overview
                        Intent overviewIntent = new Intent(Register.this, Overview.class);
                        Register.this.startActivity(overviewIntent);

                    }
                } // onClick
            }); // setOnclickListener
        } // bRegister != null


        // Create Intent to Login View
        if (tvLoginLink != null) {

            tvLoginLink.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent loginIntent = new Intent(Register.this, Login.class);
                    Register.this.startActivity(loginIntent);
                }

            });

        }

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    } //onCreate

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Register Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://todo.kebejaol.todo/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Register Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://todo.kebejaol.todo/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    /* TODO

    @Override
    protected void onDestroy(){
        super.onDestroy();
        loginDBAdapter.close();
    }

    */

} // class
