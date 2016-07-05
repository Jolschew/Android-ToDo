package todo.kebejaol.todo.ListViewAdapter;

/**
 * Created by Jan on 18.06.16.
 *
 */

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import todo.kebejaol.todo.Activities.DetailTodo;
import todo.kebejaol.todo.Model.TodoDBAdapter;
import todo.oljan.todo.R;

public class OverviewAdapter extends ArrayAdapter<Todo> {

    private final Context context;
    private final ArrayList<Todo> itemsArrayList;

    public OverviewAdapter(Context context, ArrayList<Todo> itemsArrayList) {
        super(context, R.layout.row, itemsArrayList);
        this.context = context;
        this.itemsArrayList = itemsArrayList;
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {

        // final TodoDBAdapter todoDBAdapter = new TodoDBAdapter(context).open();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        // 1. Create inflater
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // 2. Get rowView from inflater
        View rowView = inflater.inflate(R.layout.row, parent, false);

        final TextView nameView = (TextView) rowView.findViewById(R.id.name);
        final TextView expirationView = (TextView) rowView.findViewById(R.id.expirationDate);
        final TextView expirationTimeView = (TextView) rowView.findViewById(R.id.expirationTime);
        final TextView idView = (TextView) rowView.findViewById(R.id.id);
        final CheckBox isFavouriteView = (CheckBox) rowView.findViewById(R.id.cbIsFavourite);
        final CheckBox isFinishedView = (CheckBox) rowView.findViewById(R.id.cbIsFinished);

        String date, time;
        final TodoDBAdapter todoDBAdapter = new TodoDBAdapter(context).open();
        date = todoDBAdapter.getDateFromMysql(itemsArrayList.get(position).getExpirationDate());
        time = todoDBAdapter.getTimeFromMysql(itemsArrayList.get(position).getExpirationDate()) + " Uhr";
        todoDBAdapter.close();

        //Date for Check if To-do-Date is before Today
        Date checkDate = null;
        try {
            checkDate = df.parse(itemsArrayList.get(position).getExpirationDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //Change Color of ListItem if Date is before today
        if (!isBeforeToday(checkDate)) {
            nameView.setTextColor(Color.rgb(255, 0, 0));
            expirationTimeView.setTextColor(Color.rgb(255, 0, 0));
            expirationView.setTextColor(Color.rgb(255, 0, 0));
        }

        nameView.setText(itemsArrayList.get(position).getTodoName());
        expirationView.setText(date);
        expirationTimeView.setText(time);
        idView.setText(itemsArrayList.get(position).getId());
        if (itemsArrayList.get(position).getIsFavourite().equals("1")) {
            isFavouriteView.setChecked(true);
        }
        if (itemsArrayList.get(position).getIsFinished().equals("1")) {
            isFinishedView.setChecked(true);
        }


        // On Item-Click got to Detail View
        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // System.out.println(itemsArrayList.get(position).getId());
                String id = itemsArrayList.get(position).getId();
                Intent detailTodoIntent = new Intent(context, DetailTodo.class);
                // Send ID with Intent to DetailTodo
                detailTodoIntent.putExtra("id", id);
                context.startActivity(detailTodoIntent);
            }
        });

        //On Favourite Checkbox-Click change Favourite Status of To-do
        isFavouriteView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String id = itemsArrayList.get(position).getId();
                String isChecked = "0";
                if (isFavouriteView.isChecked()) {
                    isChecked = "1";
                }
                todoDBAdapter.open();
                todoDBAdapter.updateIsFavourite(id, isChecked);
                todoDBAdapter.close();
                Toast toast = Toast.makeText(context, R.string.Overview_info_favourite_change, Toast.LENGTH_SHORT);
                toast.show();

            }
        });

        //On Finished Checkbox-Click change Favourite Status of To-do
        isFinishedView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String id = itemsArrayList.get(position).getId();
                String isChecked = "0";
                if (isFinishedView.isChecked()) {
                    isChecked = "1";
                }
                todoDBAdapter.open();
                todoDBAdapter.updateIsFinished(id, isChecked);
                todoDBAdapter.close();
                Toast toast = Toast.makeText(context, R.string.Overview_info_finished_change, Toast.LENGTH_SHORT);
                toast.show();

            }
        });
        return rowView;
    }

    public boolean isBeforeToday(Date date) {
        Date today = Calendar.getInstance().getTime();
        if (date.compareTo(today) < 0) {
            return false;
        }
        return true;
    }


}