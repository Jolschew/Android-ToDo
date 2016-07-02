package todo.kebejaol.todo.Activities;

import android.content.ContentResolver;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

import todo.kebejaol.todo.Database.TodoDBAdapter;
import todo.kebejaol.todo.ListViewAdapter.Contact;
import todo.kebejaol.todo.ListViewAdapter.ContactListAdapter;
import todo.kebejaol.todo.R;

public class AddContact extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);

        //Get ToDo-ID from Overview
        final Intent detailIntent = getIntent();
        String todoID = detailIntent.getExtras().getString("todoID");


        final TodoDBAdapter todoDBAdapter = new TodoDBAdapter(this).open();
        ContentResolver resolver = getContentResolver();

        final ListView lvOverview = (ListView) findViewById(R.id.lvOverviewContact);
        final Button bCancel = (Button) findViewById(R.id.bCancel);

        ArrayList<String[]> contacts;
        contacts = todoDBAdapter.getContactsFromPhoneList(resolver, todoID);
        todoDBAdapter.close();
        ContactListAdapter adapter = new ContactListAdapter(this, generateData(contacts));
        lvOverview.setAdapter(adapter);

        if(bCancel != null)
        {
            bCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent addTodoIntent = new Intent(AddContact.this, Overview.class);
                    AddContact.this.startActivity(addTodoIntent);
                }
            });
        }

    }

    private ArrayList<Contact> generateData(ArrayList<String[]> contacts) {
        ArrayList<Contact> items = new ArrayList<Contact>();
        for (String[] c : contacts) {
            // add Items (1=Name, 3= Expiration_Date, 4= Favorit, 5=is_finished)  to Listview
            items.add(new Contact(c[0], c[1], c[2], c[3], c[4], c[5]));
        }


        return items;
    }
}
