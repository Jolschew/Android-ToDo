package todo.kebejaol.todo.ListViewAdapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import todo.kebejaol.todo.Activities.DetailTodo;
import todo.kebejaol.todo.Model.TodoDBAdapter;
import todo.kebejaol.todo.R;

/**
 * Created by Jan on 28.06.2016.
 */
public class ContactListAdapter extends ArrayAdapter<Contact> {

    private final Context context;
    private final ArrayList<Contact> itemsArrayList;

    public ContactListAdapter(Context context, ArrayList<Contact> itemsArrayList) {
        super(context, R.layout.row_contact, itemsArrayList);
        this.context = context;
        this.itemsArrayList = itemsArrayList;
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        // 1. Create inflater
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // 2. Get rowView from inflater
        View rowView = inflater.inflate(R.layout.row_contact, parent, false);

        rowView.setEnabled(false);
        TextView name = (TextView) rowView.findViewById(R.id.name);
        TextView hasNumber = (TextView) rowView.findViewById(R.id.tvHasNumber);
        Button bAddContact = (Button) rowView.findViewById(R.id.bAddContact);

        final String todoID = itemsArrayList.get(position).getTodoID();
        final String contactName = itemsArrayList.get(position).getName();
        final String contactHasPhone = itemsArrayList.get(position).getHasPhone();
        final String contactMail = itemsArrayList.get(position).getMail();
        final String contactPhoneNumber = itemsArrayList.get(position).getPhoneNumber();
        name.setText(contactName);
        hasNumber.setText(contactHasPhone);


        //Add Contact-Listener
        if (bAddContact != null) {
            bAddContact.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final TodoDBAdapter todoDBAdapter = new TodoDBAdapter(context).open();
                    todoDBAdapter.addContactToTodo(todoID, contactName, contactHasPhone, contactMail, contactPhoneNumber);
                    todoDBAdapter.close();

                    // show Toast and change activity to detailview
                    Toast toast = Toast.makeText(context.getApplicationContext(), R.string.DetailTodo_info_successful_contact_add, Toast.LENGTH_SHORT);
                    toast.show();
                    Intent detailTodoIntent = new Intent(context, DetailTodo.class);
                    // Send ID with Intent to DetailTodo
                    detailTodoIntent.putExtra("id", todoID);
                    context.startActivity(detailTodoIntent);
                }
            });
        }
        return rowView;
    }
}

