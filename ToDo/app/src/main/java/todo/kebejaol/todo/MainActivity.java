package todo.kebejaol.todo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity
{

    Button bRegister;
    TextView bLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // get Button References
        bRegister = (Button) findViewById(R.id.bRegister);
        bLogin = (TextView) findViewById(R.id.bLogin);

        // set OnClick Listener to Register Button and start Register Activity
        bRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent loginIntent = new Intent(MainActivity.this, Register.class);
                MainActivity.this.startActivity(loginIntent);
            }
        });

       // set OnClick Listener to Login Button and start Login Activity
        bLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent loginIntent = new Intent(MainActivity.this, Login.class);
                MainActivity.this.startActivity(loginIntent);
            }
        });


    }


}
