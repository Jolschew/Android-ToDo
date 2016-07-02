package todo.kebejaol.todo.ListViewAdapter;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import todo.kebejaol.todo.Activities.AddContact;
import todo.kebejaol.todo.Activities.AddTodo;
import todo.kebejaol.todo.Activities.DetailTodo;
import todo.kebejaol.todo.Activities.Overview;
import todo.kebejaol.todo.Database.TodoDBAdapter;
import todo.kebejaol.todo.R;

public class DetailviewAdapter extends ArrayAdapter<Contact> {

    private final Context context;
    private final ArrayList<Contact> itemsArrayList;

    public DetailviewAdapter(Context context, ArrayList<Contact> itemsArrayList) {

        super(context, R.layout.row_detailview, itemsArrayList);

        this.context = context;
        this.itemsArrayList = itemsArrayList;
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {


        final TodoDBAdapter todoDBAdapter = new TodoDBAdapter(context);
        // 1. Create inflater
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // 2. Get rowView from inflater
        View rowView = inflater.inflate(R.layout.row_detailview, parent, false);
        rowView.setEnabled(false);

        TextView name = (TextView) rowView.findViewById(R.id.name);
        Button bDeleteContact = (Button) rowView.findViewById(R.id.bDeleteContact);
        Button bSMS = (Button) rowView.findViewById(R.id.bSMS);
        Button bMail = (Button) rowView.findViewById(R.id.bMail);

        final String contactID = itemsArrayList.get(position).getId();
        final String contactName = itemsArrayList.get(position).getName();
        final String contactHasPhone = itemsArrayList.get(position).getHasPhone();
        final String contactPhoneNumber = itemsArrayList.get(position).getPhoneNumber();
        final String contactEmail = itemsArrayList.get(position).getMail();
        name.setText(contactName);
        if(contactHasPhone.equals("1"))
        {
            bSMS.setVisibility(View.VISIBLE);
        }
        if(!contactEmail.equals(""))
        {
            bMail.setVisibility(View.VISIBLE);
        }

        if(bDeleteContact != null)
        {
            bDeleteContact.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    todoDBAdapter.open();
                    todoDBAdapter.deleteContact(contactID);
                    todoDBAdapter.close();
                    Toast toast = Toast.makeText(context.getApplicationContext(), R.string.DetailTodo_info_successful_contact_delete, Toast.LENGTH_SHORT);
                    toast.show();
                    Intent overviewIntent = new Intent(context, Overview.class);
                    context.startActivity(overviewIntent);
                }
            });
        }

        if(bSMS != null)
        {
            bSMS.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    call(contactPhoneNumber);
                }
            });
        }
        if(bMail != null)
        {
            bMail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sendEmail(contactEmail);
                }
            });
        }

        return rowView;
    }

    protected void call(String number) {
        Uri uri = Uri.parse("smsto:"+ number);
        Intent sendIntent = new Intent(Intent.ACTION_SENDTO, uri);
        sendIntent.putExtra("sms_body", "Denk an deinen TODO");
        context.startActivity(sendIntent);
    }

    protected void sendEmail(String mail) {


        String uriText =
                "mailto:"  +mail+
                        "?subject=" + Uri.encode("ToDo für dich") +
                        "&body=" + Uri.encode("Denk an deinen ToDo");

        Uri uri = Uri.parse(uriText);

        Intent sendIntent = new Intent(Intent.ACTION_SENDTO);
        sendIntent.setData(uri);
        context.startActivity(Intent.createChooser(sendIntent, "Send email"));

    }

}

