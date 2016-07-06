package todo.kebejaol.todo.Activities;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

import todo.kebejaol.todo.Model.TodoDBAdapter;
import todo.kebejaol.todo.ListViewAdapter.Contact;
import todo.kebejaol.todo.ListViewAdapter.ContactListAdapter;
import todo.kebejaol.todo.R;

/**
 * Created by Jan on 28.06.2016.
 */
public class AddContact extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);

        //Get To-Do-ID from Overview
        final Intent detailIntent = getIntent();
        String todoID = detailIntent.getExtras().getString("todoID");

        final TodoDBAdapter todoDBAdapter = new TodoDBAdapter(this).open();
        ContentResolver resolver = getContentResolver();

        final ListView lvOverview = (ListView) findViewById(R.id.lvOverviewContact);
        final Button bCancel = (Button) findViewById(R.id.bCancel);

        // Create Listview and fill with available Contacts from Phone
        ArrayList<String[]> contacts;
        contacts = todoDBAdapter.getContactsFromPhoneList(resolver, todoID);
        todoDBAdapter.close();
        ContactListAdapter adapter = new ContactListAdapter(this, generateData(contacts));
        lvOverview.setAdapter(adapter);

        // Cancel Button Listener
        if (bCancel != null) {
            bCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent addTodoIntent = new Intent(AddContact.this, Overview.class);
                    AddContact.this.startActivity(addTodoIntent);
                }
            });
        }
    }

    // Convert Data from Database to Contact-Objects
    private ArrayList<Contact> generateData(ArrayList<String[]> contacts) {
        ArrayList<Contact> items = new ArrayList<Contact>();
        for (String[] c : contacts) {
            // add Items (1=Name, 3= Expiration_Date, 4= Favorit, 5=is_finished)  to Listview
            items.add(new Contact(c[0], c[1], c[2], c[3], c[4], c[5]));
        }
        return items;
    }
}
