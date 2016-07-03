package todo.kebejaol.todo.ListViewAdapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import todo.kebejaol.todo.Activities.Overview;
import todo.kebejaol.todo.Model.TodoDBAdapter;
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

        //check if User has Number and Mail and enable Buttons if true
        if (contactHasPhone.equals("1")) {
            bSMS.setVisibility(View.VISIBLE);
        }
        if (!contactEmail.equals("")) {
            bMail.setVisibility(View.VISIBLE);
        }

        //OnClickListener for Delete Button
        if (bDeleteContact != null) {
            bDeleteContact.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //delete Contact from Table
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

        if (bSMS != null) {
            bSMS.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sendSMS(contactPhoneNumber);
                }
            });
        }
        if (bMail != null) {
            bMail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sendEmail(contactEmail);
                }
            });
        }
        return rowView;
    }

    // start SMS-Activity if Button pressed
    protected void sendSMS(String number) {
        Uri uri = Uri.parse("smsto:" + number);
        Intent sendIntent = new Intent(Intent.ACTION_SENDTO, uri);
        sendIntent.putExtra("sms_body", "Denk an deinen TODO");
        context.startActivity(sendIntent);
    }

    // start Email-Activity if Button pressed
    protected void sendEmail(String mail) {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", mail, null));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Dein ToDo");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Denk an dein ToDo");
        context.startActivity(Intent.createChooser(emailIntent, "E-Mail"));
    }

}

