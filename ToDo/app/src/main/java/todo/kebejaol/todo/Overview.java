package todo.kebejaol.todo;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.ListActivity;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;


public class Overview extends AppCompatActivity {

    private GoogleApiClient client;




    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);

        final TodoDBAdapter todoDBAdapter = new TodoDBAdapter(this).open();
        final ListView lvOverview = (ListView) findViewById(R.id.lvOverview);


        final Button bOverviewAdd = (Button) findViewById(R.id.bOverviewAdd);

        //standard sort
        ArrayList<String[]> todos = todoDBAdapter.getEntriesByDate();

        //Todo impleent favourite sortig here!
        //ArrayList<String[]> todos = todoDBAdapter.getEntriesByFavourite();

        MyAdapter adapter = new MyAdapter(this, generateData(todos));
        lvOverview.setAdapter(adapter);



        if ( bOverviewAdd != null) {

            bOverviewAdd.setOnClickListener(new View.OnClickListener() {

                public void onClick(View view){

                    // change activity to addTodo
                    Intent addTodoIntent = new Intent(Overview.this, AddTodo.class);
                    Overview.this.startActivity(addTodoIntent);

                }
            });
        }

        if( lvOverview != null){
            System.out.println("WURST");
            lvOverview.setOnItemClickListener(new AdapterView.OnItemClickListener() {


                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    CheckBox cbIsFavorite = (CheckBox)view.findViewById(R.id.cbIsFavourite);
                    CheckBox cbIsFinished = (CheckBox)view.findViewById(R.id.cbIsFinished);
                    System.out.println("WURST");

                    Toast toast = Toast.makeText(getApplicationContext(), R.string.addTodo_info_successful_registration, Toast.LENGTH_LONG);
                    toast.show();
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
            items.add(new Item(c[1], c[3], c[4],c[5]));
        }


        return items;
    }


}
