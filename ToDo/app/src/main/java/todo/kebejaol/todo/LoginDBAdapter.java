package todo.kebejaol.todo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Jan on 25.04.16.
 *
 * Handle CRUD Operations
 */
public class LoginDBAdapter {
    public static final String DB_NAME = "login.db";
    static final int DB_VERSION = 1;
    static final int NAME_COLUMN = 1;

    //Database Creation Statement
    public static final String DB_CREATE_LOGIN = "create table "+ "LOGIN"+ "( " +"ID"+" integer primary key autoincrement,"+ "EMAIL  text,PASSWORD text); ";

    //instance of database
    public SQLiteDatabase db;
    //Context of application using database
    private final Context context;

    private DBHelper dbHelper;

    public LoginDBAdapter(Context context) {
        this.context = context;
        dbHelper = new DBHelper(this.context, DB_NAME, null, DB_VERSION);
    }

    public LoginDBAdapter open() throws SQLException
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
    public void insertEntry(String eMail, String password)
    {
        ContentValues registerValues = new ContentValues();
        // Assign Values to DB
        registerValues.put("EMAIL", eMail);
        registerValues.put("PASSWORD", password);

        //Insert row in table
        db.insert("LOGIN", null, registerValues);
    }

    // READ
    public String getEntry (String eMail)
    {
        Cursor cursor = db.query("LOGIN", null, "EMAIL=?", new String[]{eMail}, null, null, null);

        //If User does not exist
        if (cursor.getCount() < 1)
        {
            cursor.close();
            return "Dieser Nutzer existiert nicht!";
        }
        cursor.moveToFirst();
        String password = cursor.getString(cursor.getColumnIndex("PASSWORD"));
        cursor.close();
        return password;
    }

    // UPDATE
    public void updateEntry(String eMail, String password)
    {
        ContentValues updatedValues = new ContentValues();
        updatedValues.put("EMAIL", eMail);
        updatedValues.put("PASSWORD", password);

        String user = "EMAIL=?";
        db.update("LOGIN", updatedValues, user, new String[]{eMail});   

    }

    // DELETE
    public int deleteEntry(String eMail)
    {
        String user = "EMAIL=?";
        int numberofEntriesDeleted = db.delete("LOGIN", user, new String[]{eMail});
        return numberofEntriesDeleted;
    }


}
