package todo.kebejaol.todo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

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
    public static final String DB_CREATE_TODO = "create table "+ "TODO"+ "( " +"ID"+" integer primary key autoincrement,"+ "NAME  text,DESCRIPTION text); ";

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
    public void insertEntry(String name, String description)
    {
        ContentValues todoValues = new ContentValues();
        // Assign Values to DB
        todoValues.put("NAME", name);
        todoValues.put("DESCRIPTION", description);

        //Insert row in table
        db.insert("TODO", null, todoValues);
    }

    // READ
    // TODO make Todos readable
  /*  public String getEntry (String name)
    {
        Cursor cursor = db.query("TODO", null, "NAME=?", new String[]{name}, null, null, null);

        //If User does not exist
        if (cursor.getCount() == 1)
        {
            cursor.close();
            return "Dieser Nutzer existiert nicht!";
        }
        cursor.moveToFirst();
        String password = cursor.getString(cursor.getColumnIndex("DESCRIPTION"));
        cursor.close();
        return password;
    }
    */

    // UPDATE
    public void updateEntry(String name, String description)
    {
        ContentValues updatedValues = new ContentValues();
        updatedValues.put("NAME", name);
        updatedValues.put("Description", description);

        String todo = "NAME=?";
        db.update("TODO", updatedValues, todo, new String[]{name});

    }

    // DELETE
    public int deleteEntry(String name)
    {
        String todo = "NAME=?";
        int numberofEntriesDeleted = db.delete("TODO", todo, new String[]{name});
        return numberofEntriesDeleted;
    }

}
