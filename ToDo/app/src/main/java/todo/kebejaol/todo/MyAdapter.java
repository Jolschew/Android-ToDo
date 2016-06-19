package todo.kebejaol.todo;

/**
 * Created by Jan on 18.06.16.
 */

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class MyAdapter extends ArrayAdapter<Item> {

    private final Context context;
    private final ArrayList<Item> itemsArrayList;

    public MyAdapter(Context context, ArrayList<Item> itemsArrayList) {

        super(context, R.layout.row, itemsArrayList);

        this.context = context;
        this.itemsArrayList = itemsArrayList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // 1. Create inflater
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // 2. Get rowView from inflater
        View rowView = inflater.inflate(R.layout.row, parent, false);

        // 3. Get the two text view from the rowView
        TextView nameView = (TextView) rowView.findViewById(R.id.name);
        TextView expirationView = (TextView) rowView.findViewById(R.id.expirationDate);
        CheckBox isFavouriteView = (CheckBox) rowView.findViewById(R.id.cbIsFavourite);
        CheckBox isFinishedView = (CheckBox) rowView.findViewById(R.id.cbIsFinished);


        // 4. Set the text for textView
        nameView.setText(itemsArrayList.get(position).getTodoName());
        expirationView.setText(itemsArrayList.get(position).getExpirationDate());
        if(itemsArrayList.get(position).getIsFavourite().equals("1"))
        {

            isFavouriteView.setChecked(true);
        }
        if(itemsArrayList.get(position).getIsFinished().equals("1"))
        {

            isFinishedView.setChecked(true);
        }



        // 5. return rowView
        return rowView;
    }
}