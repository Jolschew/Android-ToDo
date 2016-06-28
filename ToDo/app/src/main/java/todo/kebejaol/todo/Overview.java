package todo.kebejaol.todo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;


public class Overview extends AppCompatActivity {

    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);

        final ListView lvOverview = (ListView) findViewById(R.id.lvOverview);
        final Button bOverviewAdd = (Button) findViewById(R.id.bOverviewAdd);
        final Button bSortList = (Button) findViewById(R.id.bSortList);

        final TodoDBAdapter todoDBAdapter = new TodoDBAdapter(this).open();

        //Get Sorting from Overview and sort by chosen Type
        final Intent detailIntent = getIntent();
        //standard sort
        ArrayList<String[]> todos = todoDBAdapter.getEntriesByDate();
        // Check if sorting-Key exists (Null-Pointer)
        if(detailIntent.getExtras() != null){
            if(detailIntent.getExtras().get("sorting").equals("date"))
            {
                todos = todoDBAdapter.getEntriesByDate();
            }
            else if(detailIntent.getExtras().get("sorting").equals("is_favourite"))
            {
                todos = todoDBAdapter.getEntriesByIsFavourite();
            }
            else
            {
                todos = todoDBAdapter.getEntriesByIsFinished();
            }
        }
        else
        {
            todos = todoDBAdapter.getEntriesByIsFinished();
        }
        //Close Database Cursor
        todoDBAdapter.close();
        //ArrayList<String[]> todos = todoDBAdapter.getEntriesByFavourite();

        OverviewAdapter adapter = new OverviewAdapter(this, generateData(todos));
        lvOverview.setAdapter(adapter);

        final AlertDialog chooseSorting ;
        final CharSequence[] items = {"ist erledigt", "ist Favorit", "Datum"};

        if ( bOverviewAdd != null) {

            bOverviewAdd.setOnClickListener(new View.OnClickListener() {

                public void onClick(View view){

                    // change activity to addTodo
                    Intent addTodoIntent = new Intent(Overview.this, AddTodo.class);
                    Overview.this.startActivity(addTodoIntent);

                }
            });
        }

        if ( bSortList != null) {
            bSortList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    new AlertDialog.Builder(Overview.this)
                            .setTitle("ToDo löschen")
                            .setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int item) {
                                    Intent detailTodoIntent = new Intent(Overview.this, Overview.class);
                                    String sorting;
                                    switch(item)
                                    {
                                        case 0:
                                            sorting = "is_finished";
                                            detailTodoIntent.putExtra("sorting",sorting);
                                            Overview.this.startActivity(detailTodoIntent);
                                            break;
                                        case 1:
                                            sorting = "is_favourite";
                                            detailTodoIntent.putExtra("sorting",sorting);
                                            Overview.this.startActivity(detailTodoIntent);
                                            break;
                                        case 2:
                                            sorting = "date";
                                            detailTodoIntent.putExtra("sorting",sorting);
                                            Overview.this.startActivity(detailTodoIntent);
                                            break;

                                    }
                                }
                            })
                            .show();

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


    private ArrayList<Item> generateData(ArrayList<String[]> todos) {
        ArrayList<Item> items = new ArrayList<Item>();
        for (String[] c : todos) {
            // add Items (1=Name, 3= Expiration_Date, 4= Favorit, 5=is_finished)  to Listview
            items.add(new Item(c[0], c[1], c[3], c[4],c[5]));
        }


        return items;
    }



}
