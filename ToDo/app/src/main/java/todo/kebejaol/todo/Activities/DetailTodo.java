package todo.kebejaol.todo.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import todo.kebejaol.todo.Model.TodoDBAdapter;
import todo.kebejaol.todo.ListViewAdapter.Contact;
import todo.kebejaol.todo.ListViewAdapter.DetailviewAdapter;
import todo.kebejaol.todo.R;

/**
 * Created by Jan on 19.06.2016.
 */
public class DetailTodo extends Activity {

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

        final TextView tvAddHeading = (TextView) findViewById(R.id.tvDetailHeading);
        final EditText etName = (EditText) findViewById(R.id.etDetailName);
        final EditText etExpirationDate = (EditText) findViewById(R.id.etDetailExpirationDate);
        final EditText etExpirationTime = (EditText) findViewById(R.id.etDetailExpirationTime);
        final EditText etDescription = (EditText) findViewById(R.id.etDetailDescription);
        final CheckBox cbIsFavourite = (CheckBox) findViewById(R.id.cbIsFavourite);
        final CheckBox cbIsFinished = (CheckBox) findViewById(R.id.cbIsFinished);
        final Button bDelete = (Button) findViewById(R.id.bDelete);
        final Button bChangeTodo = (Button) findViewById(R.id.bChangeTodo);
        final Button bCancel = (Button) findViewById(R.id.bChangeCancel);
        final Button bAddContact = (Button) findViewById(R.id.bAddContact);
        final ListView lvContacts = (ListView) findViewById(R.id.lvContacts);

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

        // Create Listview with Contacts
        ArrayList<String[]> contacts;
        todoDBAdapter.open();
        contacts = todoDBAdapter.getContactsFromToDo(id);
        DetailviewAdapter adapter = new DetailviewAdapter(this, generateData(contacts));
        lvContacts.setAdapter(adapter);

        //Create Datepicker Dialog for etExpirationDate
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            //Help Method for Datepicker
            public void updateLabel() {
                etExpirationDate.setText(dateFormat.format(calendar.getTime()));
            }

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
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


        //onFocusChangeListener for showing Datepicker on first click (onclick would show it on second click)
        if (etExpirationDate != null) {
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


        //onFocusChangeListener for showing Timepicker on first click
        if (etExpirationTime != null) {
            etExpirationTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {

                @Override
                public void onFocusChange(View view, boolean b) {
                    new TimePickerDialog(DetailTodo.this, time, calendar
                            .get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE),
                            true).show();
                }
            });
        }//On CLick Expiration Time


        // Back Button Listener
        if (bCancel != null) {
            bCancel.setOnClickListener(new View.OnClickListener() {

                //Button Click cancel
                public void onClick(View view) {
                    // change activity to overview
                    Intent overviewIntent = new Intent(DetailTodo.this, Overview.class);
                    DetailTodo.this.startActivity(overviewIntent);
                }
            });
        } // bAddCancel != 0


        // Delete Button Listener
        if (bDelete != null) {
            bDelete.setOnClickListener(new View.OnClickListener() {

                //Button Click cancel
                public void onClick(View view) {
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
        } // bDelete != 0

        //ChangeTo-Do Listener
        if (bChangeTodo != null) {
            bChangeTodo.setOnClickListener(new View.OnClickListener() {

                //Button Click Bestätigen
                public void onClick(View view) {
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
                                    String expirationDateTime = todoDBAdapter.formatDateTimeToMysql(expirationDate, expirationTime);
                                    String isFavourite = "0";
                                    String isFinished = "0";
                                    if (cbIsFavourite.isChecked()) {
                                        isFavourite = "1";
                                    }
                                    if (cbIsFinished.isChecked()) {
                                        isFinished = "1";
                                    }

                                    // write user data in db
                                    todoDBAdapter.open();
                                    todoDBAdapter.updateEntry(id, name, description, expirationDateTime, isFavourite, isFinished);
                                    todoDBAdapter.close();

                                    // show Toast and change activity to Overview
                                    Toast toast = Toast.makeText(getApplicationContext(), R.string.DetailTodo_info_successful_change, Toast.LENGTH_LONG);
                                    toast.show();
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


        //AddContact-Button Listener
        if (bAddContact != null) {
            bAddContact.setOnClickListener(new View.OnClickListener() {

                //Button Click Bestätigen
                public void onClick(View view) {
                    Intent overviewIntent = new Intent(DetailTodo.this, AddContact.class);
                    overviewIntent.putExtra("todoID", detailIntent.getExtras().getString("id"));
                    DetailTodo.this.startActivity(overviewIntent);
                }// onClick

            }); // setOnclickListener
        } // bAddContact != null
    }


    // Convert Data from Database to Contact-Objects
    private ArrayList<Contact> generateData(ArrayList<String[]> contacts) {
        ArrayList<Contact> items = new ArrayList<Contact>();
        for (String[] c : contacts) {
            // add Items ( id,  name,  hasPhone,  todoID,  phoneNumber,  mail)  to Listview
            items.add(new Contact(c[0], c[1], c[2], c[3], c[4], c[5]));
        }
        return items;
    }
}
