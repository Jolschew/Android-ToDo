package todo.kebejaol.todo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Created by Jan on 17.06.16.
 *
 * Handle CRUD Operations
 */
public class TodoDBAdapter {
    public static final String DB_NAME = "todo.db";
    static final int DB_VERSION = 1;
    static final int NAME_COLUMN = 1;

    //Database Creation Statement
    public static final String DB_CREATE_TODO = "CREATE TABLE IF NOT EXISTS " + "todo" + "( " + "id" + " integer primary key autoincrement," + "name  text,description text, expiration_date date, is_favourite text, is_finished text); ";

    //TODO Delete this constant, if not nescessary anymore
    public static final String DB_DROP_TABLE = "DROP TABLE todo";

    //instance of database
    public SQLiteDatabase db;
    //Context of application using database
    private final Context context;

    private DBHelper dbHelper;

    public TodoDBAdapter(Context context) {
        this.context = context;
        dbHelper = new DBHelper(this.context, DB_NAME, null, DB_VERSION);
    }

    public TodoDBAdapter open() throws SQLException
    {
        db = dbHelper.getWritableDatabase();
        //TODO Remove Drop Table
      //   db.execSQL(DB_DROP_TABLE);

        //Create  Table Todo
        db.execSQL(DB_CREATE_TODO);
        return this;
    }
    public void close()
    {
        db.close();
    }

    public SQLiteDatabase getDBInstance()
    {
        return db;
    }

    // CREATE
    public void insertEntry(String name, String description, String expiration_date, String isFavourite)
    {
        ContentValues todoValues = new ContentValues();
        // Assign Values to DB
        todoValues.put("name", name);
        todoValues.put("description", description);
        todoValues.put("expiration_date", expiration_date);
        todoValues.put("is_favourite", isFavourite);
        todoValues.put("is_finished", "0");

        //Insert row in table
        db.insert("todo", null, todoValues);
    }

    // READ
    // TODO userspezifisch WHERE
    public ArrayList<String[]> getEntriesByDate()
    {
        Cursor cursor = db.rawQuery("SELECT * FROM todo ORDER BY expiration_date ASC", null);
        cursor.moveToFirst();

        ArrayList<String[]> todos = new ArrayList<String[]>();

        while(!cursor.isAfterLast()) {


            String tmpTodo[] = new String[]{
                    cursor.getString(cursor.getColumnIndex("id")),
                    cursor.getString(cursor.getColumnIndex("name")),
                    cursor.getString(cursor.getColumnIndex("description")),
                    cursor.getString(cursor.getColumnIndex("expiration_date")),
                    cursor.getString(cursor.getColumnIndex("is_favourite")),
                    cursor.getString(cursor.getColumnIndex("is_finished")),

            };

            todos.add(tmpTodo);

            cursor.moveToNext();
        }

        cursor.close();


        return todos;
    }

    public ArrayList<String[]> getEntriesByIsFinished()
    {
        Cursor cursor = db.rawQuery("SELECT * FROM todo ORDER BY expiration_date ASC", null);   //TODO Order by is finished
        cursor.moveToFirst();

        ArrayList<String[]> todos = new ArrayList<String[]>();

        while(!cursor.isAfterLast()) {


            String tmpTodo[] = new String[]{
                    cursor.getString(cursor.getColumnIndex("id")),
                    cursor.getString(cursor.getColumnIndex("name")),
                    cursor.getString(cursor.getColumnIndex("description")),
                    cursor.getString(cursor.getColumnIndex("expiration_date")),
                    cursor.getString(cursor.getColumnIndex("is_favourite")),
                    cursor.getString(cursor.getColumnIndex("is_finished")),

            };

            todos.add(tmpTodo);

            cursor.moveToNext();
        }

        cursor.close();


        return todos;
    }

    public ArrayList<String[]> getEntriesByFavourite()
    {
        Cursor cursor = db.rawQuery("SELECT * FROM todo", null);
        cursor.moveToFirst();

        ArrayList<String[]> todos = new ArrayList<String[]>();

        while(!cursor.isAfterLast()) {


            String tmpTodo[] = new String[]{
                    cursor.getString(cursor.getColumnIndex("id")),
                    cursor.getString(cursor.getColumnIndex("name")),
                    cursor.getString(cursor.getColumnIndex("description")),
                    cursor.getString(cursor.getColumnIndex("expiration_date")),
                    cursor.getString(cursor.getColumnIndex("is_favourite")),
                    cursor.getString(cursor.getColumnIndex("is_finished")),

            };

            todos.add(tmpTodo);

            cursor.moveToNext();
        }

        cursor.close();


        return todos;
    }


    // UPDATE
    public void updateEntry(String id, String name, String description, String expirationDate, String isFavourite, String isFinished)
    {
        ContentValues updatedValues = new ContentValues();
        updatedValues.put("name", name);
        updatedValues.put("description", description);
        updatedValues.put("expiration_date", expirationDate);
        updatedValues.put("is_favourite", isFavourite);
        updatedValues.put("is_finished", isFinished);

        String user = "id=?";
        db.update("todo", updatedValues, user, new String[]{id});

    }

    // DELETE
    public int deleteEntry(String id)
    {
        String todo = "id=?";
        int numberofEntriesDeleted = db.delete("todo", todo, new String[]{id});
        return numberofEntriesDeleted;
    }

    public void updateIsFavourite(String id, String isFavourite)
    {
        ContentValues updatedValues = new ContentValues();
        updatedValues.put("is_favourite", isFavourite);

        String user = "id=?";
        db.update("todo", updatedValues, user, new String[]{id});
        System.out.println("Works");
    }

    public void updateIsFinished(String id, String isFinished) {
        ContentValues updatedValues = new ContentValues();
        updatedValues.put("is_finished", isFinished);

        String user = "id=?";
        db.update("todo", updatedValues, user, new String[]{id});
    }

    /**
     *
     * @param id
     * @return To-do Array {id, name, description, expiration_date, is_favourite, is_finished}
     */
    public String[] getEntry(String id)
    {
        Cursor cursor = db.rawQuery("SELECT * FROM todo WHERE id = "+ id, null);
        cursor.moveToFirst();

        String todo[] = null;

        if (cursor != null)
        {
            todo = new String[]{cursor.getString(0), cursor.getString(1), cursor.getString(2),
                   cursor.getString(3), cursor.getString(4), cursor.getString(5)};
        }
        System.out.println(todo[1]);
        return todo;
    }
}
