package todo.kebejaol.todo;

/**
 * Created by Jan on 18.06.16.
 */

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

public class OverviewAdapter extends ArrayAdapter<Item> {

    private final Context context;
    private final ArrayList<Item> itemsArrayList;

    public OverviewAdapter(Context context, ArrayList<Item> itemsArrayList) {

        super(context, R.layout.row, itemsArrayList);

        this.context = context;
        this.itemsArrayList = itemsArrayList;
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {

       // final TodoDBAdapter todoDBAdapter = new TodoDBAdapter(context).open();

        // 1. Create inflater
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // 2. Get rowView from inflater
        View rowView = inflater.inflate(R.layout.row, parent, false);

        // 3. Get the two text view from the rowView
        TextView nameView = (TextView) rowView.findViewById(R.id.name);
        TextView expirationView = (TextView) rowView.findViewById(R.id.expirationDate);
        TextView idView = (TextView) rowView.findViewById(R.id.id);

        final CheckBox isFavouriteView = (CheckBox) rowView.findViewById(R.id.cbIsFavourite);
        final CheckBox isFinishedView = (CheckBox) rowView.findViewById(R.id.cbIsFinished);


        // 4. Set the text for textView
        nameView.setText(itemsArrayList.get(position).getTodoName());
        expirationView.setText(itemsArrayList.get(position).getExpirationDate());
        idView.setText(itemsArrayList.get(position).getId());
        if(itemsArrayList.get(position).getIsFavourite().equals("1"))
        {

            isFavouriteView.setChecked(true);
        }
        if(itemsArrayList.get(position).getIsFinished().equals("1"))
        {

            isFinishedView.setChecked(true);
        }




        // On Item-Click got to Detail View
       rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // System.out.println(itemsArrayList.get(position).getId());
                String id = itemsArrayList.get(position).getId();

                Intent detailTodoIntent = new Intent(context,DetailTodo.class);
                // Send ID with Intent to DetailTodo
                detailTodoIntent.putExtra("id",id);
                context.startActivity(detailTodoIntent);
            }
        });

        //On Favourite Checkbox-Click change Favourite Status of Todo
        isFavouriteView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                final TodoDBAdapter todoDBAdapter = new TodoDBAdapter(context).open();
                String id = itemsArrayList.get(position).getId();
                String isChecked = "0";
                if(isFavouriteView.isChecked())
                {
                    isChecked = "1";
                }
                todoDBAdapter.updateIsFavourite(id, isChecked);
                todoDBAdapter.close();
                Toast toast = Toast.makeText(context, R.string.Overview_info_favourite_change, Toast.LENGTH_LONG);
                toast.show();

            }
        });

        isFinishedView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                final TodoDBAdapter todoDBAdapter = new TodoDBAdapter(context).open();
                String id = itemsArrayList.get(position).getId();
                String isChecked = "0";
                if(isFinishedView.isChecked())
                {
                    isChecked = "1";
                }
                todoDBAdapter.updateIsFinished(id, isChecked);
                todoDBAdapter.close();
                Toast toast = Toast.makeText(context, R.string.Overview_info_finished_change, Toast.LENGTH_LONG);
                toast.show();

            }
        });




        // 5. return rowView
        return rowView;
    }




}