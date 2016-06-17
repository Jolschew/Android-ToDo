package todo.kebejaol.todo;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.OverScroller;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

/**
 * Created by Kevin on 15.06.2016.
 */
public class AddTodo extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_todo);
        final TodoDBAdapter todoDBAdapter = new TodoDBAdapter(this).open();
        final TextView tvAddHeading = (TextView) findViewById(R.id.tvAddHeading);
        final EditText etAddName = (EditText) findViewById(R.id.etAddName);
        final EditText etAddDescription = (EditText) findViewById(R.id.etAddDescription);
        final EditText etAddExpirationDate = (EditText) findViewById(R.id.etAddExpirationDate);
        final Button bAddTodo = (Button) findViewById(R.id.bAddTodo);
        final Button bAddCancel = (Button) findViewById(R.id.bAddCancel);


        if ( bAddCancel != null) {

            bAddCancel.setOnClickListener(new View.OnClickListener() {

                public void onClick(View view){

                    // change activity to overview
                    Intent overviewIntent = new Intent(AddTodo.this, Overview.class);
                    AddTodo.this.startActivity(overviewIntent);



                }
            });
        } // bAddCancel != 0

        if (bAddTodo != null) {

            bAddTodo.setOnClickListener(new View.OnClickListener() {

                public void onClick(View view){

                    String name = etAddName.getText().toString();
                    String description = etAddDescription.getText().toString();



                    // write user data in db
                    // TODO error handling
                    todoDBAdapter.insertEntry(name, description);

                    Toast toast = Toast.makeText(getApplicationContext(), R.string.registration_info_successful_registration, Toast.LENGTH_LONG);
                    toast.show();

                    // change activity to login or overview
                    Intent overviewIntent = new Intent(AddTodo.this, Overview.class);
                    AddTodo.this.startActivity(overviewIntent);

                }// onClick

            }); // setOnclickListener
        } // bRegister != null



    }

}
