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
    public static final String DB_CREATE_TODO = "CREATE TABLE IF NOT EXISTS " + "TODO" + "( " + "ID" + " integer primary key autoincrement," + "NAME  text,DESCRIPTION text, EXPIRATION_DATE text); ";

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
        // db.execSQL(DB_DROP_TABLE);

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
    public void insertEntry(String name, String description, String expiration_date)
    {
        ContentValues todoValues = new ContentValues();
        // Assign Values to DB
        todoValues.put("NAME", name);
        todoValues.put("DESCRIPTION", description);
        todoValues.put("EXPIRATION_DATE", expiration_date);

        //Insert row in table
        db.insert("TODO", null, todoValues);
    }

    // READ
    // TODO userspezifisch WHERE
    public ArrayList<String[]> getEntries()
    {
        Cursor cursor = db.rawQuery("SELECT * FROM TODO", null);
        cursor.moveToFirst();

        ArrayList<String[]> todos = new ArrayList<String[]>();

        while(!cursor.isAfterLast()) {


            String tmpTodo[] = new String[]{
                    cursor.getString(cursor.getColumnIndex("ID")),
                    cursor.getString(cursor.getColumnIndex("NAME")),
                    cursor.getString(cursor.getColumnIndex("DESCRIPTION")),
                    cursor.getString(cursor.getColumnIndex("EXPIRATION_DATE")),
                    //  cursor.getString(cursor.getColumnIndex("IS_FAVOURITE")), // TODO not implemented yet

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


        return todos;//.toArray(new String[todos.size()]);



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
    public void updateEntry(String name, String description, String expirationDate)
    {
        ContentValues updatedValues = new ContentValues();
        updatedValues.put("NAME", name);
        updatedValues.put("DESCRIPTION", description);
        updatedValues.put("EXPIRATION_DATE", expirationDate);

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
