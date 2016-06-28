package todo.kebejaol.todo;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
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
        final Calendar calendar = Calendar.getInstance();
        final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        // Find complete Entry By ID
        String[] todo = todoDBAdapter.getEntry(id);

        //Close Database Cursor
        todoDBAdapter.close();

        final EditText etName = (EditText) findViewById(R.id.etDetailName);
        final EditText etExpirationDate = (EditText) findViewById(R.id.etDetailExpirationDate);
        final EditText etExpirationTime = (EditText) findViewById(R.id.etDetailExpirationTime);
        final EditText etDescription = (EditText) findViewById(R.id.etDetailDescription);
        final CheckBox cbIsFavourite = (CheckBox) findViewById(R.id.cbIsFavourite);
        final CheckBox cbIsFinished = (CheckBox) findViewById(R.id.cbIsFinished);
        final Button bDelete = (Button) findViewById(R.id.bDelete);
        final Button bChangeTodo = (Button) findViewById(R.id.bChangeTodo);
        final Button bCancel = (Button) findViewById(R.id.bChangeCancel);
        String date2, time2;
        date2 = todoDBAdapter.getDateFromMysql(todo[3]);
        time2 = todoDBAdapter.getTimeFromMysql(todo[3]);
        etExpirationDate.setText(date2);
        etExpirationTime.setText(time2);

        //Fill Fields of Activity
        etName.setText(todo[1]);
        etDescription.setText(todo[2]);

        if (todo[4].equals("1")) {
            cbIsFavourite.setChecked(true);
        }
        if (todo[5].equals("1")) {
            cbIsFinished.setChecked(true);
        }



        //Create Datepicker Dialog for etExpirationDate
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            //Help Method for Datepicker
            public void updateLabel() {
                etExpirationDate.setText(dateFormat.format(calendar.getTime()));
            }

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };


        //Create Timepicker Dialog for etExpirationTime
        final TimePickerDialog.OnTimeSetListener time = new TimePickerDialog.OnTimeSetListener() {

            //Help Method for Datepicker to format Date
            public void updateLabel() {
                etExpirationTime.setText(timeFormat.format(calendar.getTime()));
            }

            @Override
            public void onTimeSet(TimePicker view, int hour, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY, hour);
                calendar.set(Calendar.MINUTE, minute);
                updateLabel();
            }

        };


        if (etExpirationDate != null) {
            //onFocusChangeListener for showing Datepicker on first click (onclick would show it on second click)
            etExpirationDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {

                @Override
                public void onFocusChange(View view, boolean b) {
                    new DatePickerDialog(DetailTodo.this, date, calendar
                            .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                            calendar.get(Calendar.DAY_OF_MONTH)).show();
                }

                //Edit Text Expiration_Date click
                //public void onFocusChange(View view) {

            });
        }// expirationDate != null

        if (etExpirationTime != null) {
            //onFocusChangeListener for showing Timepicker on first click
            etExpirationTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {


                @Override
                public void onFocusChange(View view, boolean b) {
                    new TimePickerDialog(DetailTodo.this, time, calendar
                            .get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE),
                            true).show();
                }
            });
        }//On CLick Expiration Time

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

                    // Show Alert Box and Ask if User want's to proceed
                    new AlertDialog.Builder(DetailTodo.this)
                            .setTitle("ToDo löschen")
                            .setMessage("Wollen Sie den Todo wirklich löschen?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // change activity to overview
                                    String id = detailIntent.getExtras().getString("id");
                                    todoDBAdapter.open();
                                    todoDBAdapter.deleteEntry(id);
                                    Toast toast = Toast.makeText(getApplicationContext(), R.string.detailTodo_info_successful_delete, Toast.LENGTH_LONG);
                                    toast.show();
                                    Intent overviewIntent = new Intent(DetailTodo.this, Overview.class);
                                    DetailTodo.this.startActivity(overviewIntent);
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast toast = Toast.makeText(getApplicationContext(), R.string.detailTodo_info_cancel_delete, Toast.LENGTH_LONG);
                                    toast.show();
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();


                }
            });
        } // bAddCancel != 0


        if (bChangeTodo != null) {

            bChangeTodo.setOnClickListener(new View.OnClickListener() {

                //Button Click Bestätigen
                public void onClick(View view){

                    // Show Alert Box and Ask if User want's to proceed
                    new AlertDialog.Builder(DetailTodo.this)
                            .setTitle("ToDo ändern")
                            .setMessage("Wollen Sie den ToDo wirklich ändern?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    String id = detailIntent.getExtras().getString("id");
                                    String name = etName.getText().toString();
                                    String description = etDescription.getText().toString();
                                    String expirationDate = etExpirationDate.getText().toString();
                                    String expirationTime = etExpirationTime.getText().toString() + ":00";
                                    String expirationDateTime = todoDBAdapter.formatDateTimeToMysql(expirationDate,expirationTime);
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
                                    todoDBAdapter.updateEntry(id, name, description, expirationDateTime, isFavourite, isFinished);
                                    todoDBAdapter.close();
                                    Toast toast = Toast.makeText(getApplicationContext(), R.string.DetailTodo_info_successful_change, Toast.LENGTH_LONG);
                                    toast.show();

                                    // change activity to login or overview
                                    Intent overviewIntent = new Intent(DetailTodo.this, Overview.class);
                                    DetailTodo.this.startActivity(overviewIntent);
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast toast = Toast.makeText(getApplicationContext(), R.string.DetailTodo_info_cancel_change, Toast.LENGTH_LONG);
                                    toast.show();
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();



                }// onClick

            }); // setOnclickListener
        } // bChange != null

    }





}
