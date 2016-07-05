package todo.kebejaol.todo.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import todo.kebejaol.todo.Model.TodoDBAdapter;
import todo.oljan.todo.R;

/**
 * Created by Jan on 15.06.2016.
 *
 */
public class AddTodo extends Activity {

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_todo);

        //get Email-Adress
        final User globaleVariable = (User) getApplicationContext();
        final String email = globaleVariable.getUsername();

        final Calendar calendar = Calendar.getInstance();
        final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        final TodoDBAdapter todoDBAdapter = new TodoDBAdapter(this).open();
        final EditText etAddName = (EditText) findViewById(R.id.etAddName);
        final EditText etAddDescription = (EditText) findViewById(R.id.etAddDescription);
        final EditText etAddExpirationDate = (EditText) findViewById(R.id.etAddExpirationDate);
        final EditText etAddExpirationTime = (EditText) findViewById(R.id.etAddExpirationTime);
        final CheckBox cbIsFavourite = (CheckBox) findViewById(R.id.cbIsFavourite);
        final Button bAddTodo = (Button) findViewById(R.id.bAddTodo);
        final Button bAddCancel = (Button) findViewById(R.id.bAddCancel);

        etAddExpirationDate.setText(dateFormat.format(calendar.getTime()));
        etAddExpirationTime.setText(timeFormat.format(calendar.getTime()));

        //Create Datepicker Dialog for etExpirationDate

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            //Help Method for Datepicker to format Date
            public void updateLabel() {
                etAddExpirationDate.setText(dateFormat.format(calendar.getTime()));
            }

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };

        final TimePickerDialog.OnTimeSetListener time = new TimePickerDialog.OnTimeSetListener() {
            //Help Method for Datepicker to format Date
            public void updateLabel() {
                etAddExpirationTime.setText(timeFormat.format(calendar.getTime()));
            }

            @Override
            public void onTimeSet(TimePicker view, int hour, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY, hour);
                calendar.set(Calendar.MINUTE, minute);
                updateLabel();
            }

        };

        //On CLick Expiration Date
        if (etAddExpirationDate != null) {
            //onFocusChangeListener for showing Datepicker on first click
            etAddExpirationDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {

                @Override
                public void onFocusChange(View view, boolean b) {
                    new DatePickerDialog(AddTodo.this, date, calendar
                            .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                            calendar.get(Calendar.DAY_OF_MONTH)).show();
                }

            });
        }//On CLick Expiration Date


        //On CLick Expiration Time
        if (etAddExpirationTime != null) {
            //onFocusChangeListener for showing Timepicker on first click
            etAddExpirationTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {

                @Override
                public void onFocusChange(View view, boolean b) {
                    new TimePickerDialog(AddTodo.this, time, calendar
                            .get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE),
                            true).show();
                }
            });
        }//On CLick Expiration Time


        if (bAddCancel != null) {
            bAddCancel.setOnClickListener(new View.OnClickListener() {

                //Button Click cancel
                public void onClick(View view) {
                    // change activity to overview
                    Intent overviewIntent = new Intent(AddTodo.this, Overview.class);
                    AddTodo.this.startActivity(overviewIntent);
                }
            });
        } // bAddCancel != 0

        // On Button CLick Add
        if (bAddTodo != null) {
            bAddTodo.setOnClickListener(new View.OnClickListener() {

                //Button Click Bestätigen
                public void onClick(View view) {
                    new AlertDialog.Builder(AddTodo.this)
                            .setTitle("ToDo anlegen")
                            .setMessage("Wollen Sie den ToDo wirklich anlegen?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    String name = etAddName.getText().toString();
                                    String description = etAddDescription.getText().toString();
                                    String expirationTime = etAddExpirationTime.getText().toString() + ":00";
                                    String expirationDate = etAddExpirationDate.getText().toString();
                                    String expirationDateTime = todoDBAdapter.formatDateTimeToMysql(expirationDate, expirationTime);

                                    String isFavourite;
                                    if (cbIsFavourite.isChecked()) {
                                        isFavourite = "1";
                                    } else {
                                        isFavourite = "0";
                                    }

                                    // write user data in db
                                    todoDBAdapter.insertEntry(email, name, description, expirationDateTime, isFavourite);
                                    todoDBAdapter.close();


                                    // show Toast and change activity to overview
                                    Toast toast = Toast.makeText(getApplicationContext(), R.string.addTodo_info_successful_add, Toast.LENGTH_LONG);
                                    toast.show();
                                    Intent overviewIntent = new Intent(AddTodo.this, Overview.class);
                                    AddTodo.this.startActivity(overviewIntent);
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast toast = Toast.makeText(getApplicationContext(), R.string.addTodo_info_cancel_add, Toast.LENGTH_LONG);
                                    toast.show();
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();


                }// onClick

            }); // setOnclickListener
        } // bAddTodo != null

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }//onCreate

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "AddTodo Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
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
                "AddTodo Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://todo.kebejaol.todo/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}
