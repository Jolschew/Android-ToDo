package todo.kebejaol.todo;


import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.OverScroller;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

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
        final CheckBox cbIsFavourite = (CheckBox) findViewById(R.id.cbIsFavourite);
        final Button bAddTodo = (Button) findViewById(R.id.bAddTodo);
        final Button bAddCancel = (Button) findViewById(R.id.bAddCancel);



        //Create Datepicker Dialog for etExpirationDate
        final Calendar myCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        //Help Method for Datepicker
        public void updateLabel() {
            String myFormat = "yyyy.MM.dd"; // TODO change Format, if there is time
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.GERMANY);

            etAddExpirationDate.setText(sdf.format(myCalendar.getTime()));
        }

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };


        if (etAddExpirationDate != null) {
            //onFocusChangeListener for showing Datepicker on first click (onclick would show it on second click)
            etAddExpirationDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {

                @Override
                public void onFocusChange(View view, boolean b) {
                    new DatePickerDialog(AddTodo.this, date, myCalendar
                            .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                            myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                }

                //Edit Text Expiration_Date click
                //public void onFocusChange(View view) {

            });
        }


        if ( bAddCancel != null) {

            bAddCancel.setOnClickListener(new View.OnClickListener() {

                //Button Click cancel
                public void onClick(View view){

                    // change activity to overview
                    Intent overviewIntent = new Intent(AddTodo.this, Overview.class);
                    AddTodo.this.startActivity(overviewIntent);



                }
            });
        } // bAddCancel != 0


        if (bAddTodo != null) {

            bAddTodo.setOnClickListener(new View.OnClickListener() {

                //Button Click Bestätigen
                public void onClick(View view){

                    String name = etAddName.getText().toString();
                    String description = etAddDescription.getText().toString();
                    String expirationDate = etAddExpirationDate.getText().toString();
                    String isFavourite = "";
                    if(cbIsFavourite.isChecked())
                    {
                        isFavourite = "1";
                    }
                    else
                    {
                        isFavourite= "0";
                    }

                    // write user data in db
                    // TODO error handling
                    todoDBAdapter.insertEntry(name, description, expirationDate, isFavourite);

                    Toast toast = Toast.makeText(getApplicationContext(), R.string.registration_info_successful_registration, Toast.LENGTH_LONG);
                    toast.show();

                    // change activity to login or overview
                    Intent overviewIntent = new Intent(AddTodo.this, Overview.class);
                    AddTodo.this.startActivity(overviewIntent);

                }// onClick

            }); // setOnclickListener
        } // bRegister != null




    }//onCreate



}
