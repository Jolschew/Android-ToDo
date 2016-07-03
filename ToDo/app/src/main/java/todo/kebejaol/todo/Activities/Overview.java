package todo.kebejaol.todo.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import todo.kebejaol.todo.Model.TodoDBAdapter;
import todo.kebejaol.todo.ListViewAdapter.Todo;
import todo.kebejaol.todo.ListViewAdapter.OverviewAdapter;
import todo.kebejaol.todo.R;

/**
 * Created by Jan on 15.06.2016.
 *
 */
public class Overview extends AppCompatActivity {

    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);

        final User globaleVariable = (User) getApplicationContext();
        final String email = globaleVariable.getUsername();

        final ListView lvOverview = (ListView) findViewById(R.id.lvOverview);
        final Button bOverviewAdd = (Button) findViewById(R.id.bOverviewAdd);


        final TodoDBAdapter todoDBAdapter = new TodoDBAdapter(this).open();

        //Get Sorting from Overview and sort by chosen Type
        final Intent detailIntent = getIntent();
        //standard sort
        ArrayList<String[]> todos;
        // Check if sorting-Key exists (Null-Pointer)
        if (detailIntent.getExtras() != null) {
            if (detailIntent.getExtras().get("sorting").equals("date")) {
                todos = todoDBAdapter.getEntriesByDate(email);
            } else if (detailIntent.getExtras().get("sorting").equals("is_favourite")) {
                todos = todoDBAdapter.getEntriesByIsFavourite(email);
            } else {
                todos = todoDBAdapter.getEntriesByIsFinished(email);
            }
        } else {
            todos = todoDBAdapter.getEntriesByIsFinished(email);
        }
        //Close Database Cursor
        todoDBAdapter.close();
        //ArrayList<String[]> todos = todoDBAdapter.getEntriesByFavourite();

        OverviewAdapter adapter = new OverviewAdapter(this, generateData(todos));
        lvOverview.setAdapter(adapter);

        final CharSequence[] items = {"ist erledigt", "ist Favorit", "Datum"};

        //Add Button onClickListener
        if (bOverviewAdd != null) {
            bOverviewAdd.setOnClickListener(new View.OnClickListener() {

                public void onClick(View view) {
                    // change activity to addTodo
                    Intent addTodoIntent = new Intent(Overview.this, AddTodo.class);
                    Overview.this.startActivity(addTodoIntent);
                }
            });
        }

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    } // onCreate

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

    // Convert Data from Database to Todo-Objects
    private ArrayList<Todo> generateData(ArrayList<String[]> todos) {
        ArrayList<Todo> items = new ArrayList<Todo>();
        for (String[] c : todos) {
            // add Items (1=Name, 3= Expiration_Date, 4= Favorit, 5=is_finished)  to Listview
            items.add(new Todo(c[0], c[1], c[3], c[4], c[5]));
        }
        return items;
    }

    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_listfragment, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent detailTodoIntent = new Intent(Overview.this, Overview.class);
        switch (item.getItemId()) {
            case R.id.item1:
                detailTodoIntent.putExtra("sorting", "is_finished");
                Overview.this.startActivity(detailTodoIntent);
                return true;
            case R.id.item2:
                detailTodoIntent.putExtra("sorting", "is_favourite");
                Overview.this.startActivity(detailTodoIntent);
                return true;
            case R.id.item3:
                detailTodoIntent.putExtra("sorting", "date");
                Overview.this.startActivity(detailTodoIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
