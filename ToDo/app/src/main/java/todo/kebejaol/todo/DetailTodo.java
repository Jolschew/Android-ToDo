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
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DetailTodo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_todo);

        //Get ID from Overview
        final Intent detailIntent = getIntent();
        String id = detailIntent.getExtras().getString("id");

        final TodoDBAdapter todoDBAdapter = new TodoDBAdapter(this).open();

        // Find complete Entry By ID
        String[] todo = todoDBAdapter.getEntry(id);

        //Close Database Cursor
        todoDBAdapter.close();

        final EditText etName = (EditText) findViewById(R.id.etDetailName);
        final EditText etExpirationDate = (EditText) findViewById(R.id.etDetailExpirationDate);
        final EditText etDescription = (EditText) findViewById(R.id.etDetailDescription);
        final CheckBox cbIsFavourite = (CheckBox) findViewById(R.id.cbIsFavourite);
        final CheckBox cbIsFinished = (CheckBox) findViewById(R.id.cbIsFinished);
        final Button bDelete = (Button) findViewById(R.id.bDelete);
        final Button bChangeTodo = (Button) findViewById(R.id.bChangeTodo);
        final Button bCancel = (Button) findViewById(R.id.bChangeCancel);


        //Fill Fields of Activity
        etName.setText(todo[1]);
        etDescription.setText(todo[2]);
        etExpirationDate.setText(todo[3]);
        if (todo[4].equals("1")) {
            cbIsFavourite.setChecked(true);
        }
        if (todo[5].equals("1")) {
            cbIsFinished.setChecked(true);
        }


        //Help Method for Datepicker

        //Create Datepicker Dialog for etExpirationDate
        final Calendar myCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            //Help Method for Datepicker
            public void updateLabel() {
                String myFormat = "yyyy.MM.dd"; // TODO change Format, if there is time
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.GERMANY);

                etExpirationDate.setText(sdf.format(myCalendar.getTime()));
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


        if (etExpirationDate != null) {
            //onFocusChangeListener for showing Datepicker on first click (onclick would show it on second click)
            etExpirationDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {

                @Override
                public void onFocusChange(View view, boolean b) {
                    new DatePickerDialog(DetailTodo.this, date, myCalendar
                            .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                            myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                }

                //Edit Text Expiration_Date click
                //public void onFocusChange(View view) {

            });
        }// expirationDate != null

        if ( bCancel != null) {

            bCancel.setOnClickListener(new View.OnClickListener() {

                //Button Click cancel
                public void onClick(View view){
                    // change activity to overview
                    Intent overviewIntent = new Intent(DetailTodo.this, Overview.class);
                    DetailTodo.this.startActivity(overviewIntent);
                }
            });
        } // bAddCancel != 0

        if ( bDelete != null) {

            bDelete.setOnClickListener(new View.OnClickListener() {

                //Button Click cancel
                public void onClick(View view){
                    // change activity to overview
                    String id = detailIntent.getExtras().getString("id");
                    todoDBAdapter.open();
                    todoDBAdapter.deleteEntry(id);
                    Toast toast = Toast.makeText(getApplicationContext(), R.string.detailTodo_info_successful_delete, Toast.LENGTH_LONG);
                    toast.show();
                    Intent overviewIntent = new Intent(DetailTodo.this, Overview.class);
                    DetailTodo.this.startActivity(overviewIntent);
                }
            });
        } // bAddCancel != 0


        if (bChangeTodo != null) {

            bChangeTodo.setOnClickListener(new View.OnClickListener() {

                //Button Click Bestätigen
                public void onClick(View view){
                    String id = detailIntent.getExtras().getString("id");
                    String name = etName.getText().toString();
                    String description = etDescription.getText().toString();
                    String expirationDate = etExpirationDate.getText().toString();
                    String isFavourite = "0";
                    String isFinished = "0";
                    if(cbIsFavourite.isChecked())
                    {
                        isFavourite = "1";
                    }
                    if(cbIsFinished.isChecked())
                    {
                        isFinished = "1";
                    }

                    // write user data in db

                    todoDBAdapter.open();
                    todoDBAdapter.updateEntry(id, name, description, expirationDate, isFavourite, isFinished);

                    Toast toast = Toast.makeText(getApplicationContext(), R.string.DetailTodo_info_successful_change, Toast.LENGTH_LONG);
                    toast.show();

                    // change activity to login or overview
                    Intent overviewIntent = new Intent(DetailTodo.this, Overview.class);
                    DetailTodo.this.startActivity(overviewIntent);

                }// onClick

            }); // setOnclickListener
        } // bChange != null

    }
}
