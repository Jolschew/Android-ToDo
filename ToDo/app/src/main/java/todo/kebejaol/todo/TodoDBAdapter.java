package todo.kebejaol.todo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

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
    public static final String DB_CREATE_TODO = "CREATE TABLE IF NOT EXISTS "+ "TODO"+ "( " +"ID"+" integer primary key autoincrement,"+ "NAME  text,DESCRIPTION text); ";

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
    // TODO userspezifisch WHERE
    public String[] getEntries ()
    {
       // Cursor cursor = db.query("TODO", null, "NAME=?", new String[]{name}, null, null, null);
        Cursor cursor = db.rawQuery("SELECT * FROM TODO", null);
      //  Cursor cursor = db.query("", new String[] {"ID", "NAME", "DESCRIPTION"}, "", null, null, null, null);
        cursor.moveToFirst();

        ArrayList<String> names = new ArrayList<String>();

        ArrayList<String[]> todos = new ArrayList<String[]>();

        while(!cursor.isAfterLast()) {
            names.add(cursor.getString(cursor.getColumnIndex("NAME")));

            String tmpTodo[] = new String[]{
                    cursor.getString(cursor.getColumnIndex("ID")),
                    cursor.getString(cursor.getColumnIndex("NAME")),
                    //cursor.getString(cursor.getColumnIndex("DESCRIPTION")) TODO description funktioniert nicht
            };

            todos.add(tmpTodo);

            // tmpTodo Ausgabe, (funktioniert bereits korrekt)
            // TODO in Overview.java das return korrekt behandeln
            for(String c: tmpTodo){
                System.out.println(c);
            }

            cursor.moveToNext();
        }

        cursor.close();


        return todos.toArray(new String[todos.size()]);



        //If User does not exist
       // if (cursor.getCount() == 1)
       // {
          //  cursor.close();
         //   return "Dieser Nutzer existiert nicht!";
        //}
       // cursor.moveToFirst();
       // String password = cursor.getString(cursor.getColumnIndex("DESCRIPTION"));
      //  cursor.close();
    //    return password;
    }


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
