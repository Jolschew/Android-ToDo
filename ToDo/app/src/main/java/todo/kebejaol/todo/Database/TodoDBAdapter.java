package todo.kebejaol.todo.Database;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract;

import java.util.ArrayList;

import todo.kebejaol.todo.Activities.User;

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
    public static final String DB_CREATE_TODO = "CREATE TABLE IF NOT EXISTS " + "todo" + "( " + "id" + " integer primary key autoincrement," + "name  text,description text, expiration_date datetime, is_favourite text, is_finished text, email text); ";
    public static final String DB_CREATE_CONTACTS = "CREATE TABLE IF NOT EXISTS " + "contact" + "( " + "id" + " integer primary key autoincrement," + " name  text, has_phone text, todo text, mail text, phone_number text); ";


    //TODO Delete this constant, if not nescessary anymore
    public static final String DB_DROP_TABLE = "DROP TABLE contact";

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

        //Create  Table To-do and Contacts
        db.execSQL(DB_CREATE_TODO);
        db.execSQL(DB_CREATE_CONTACTS);
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
    public void insertEntry(String email, String name, String description, String expiration_date, String isFavourite)
    {
        ContentValues todoValues = new ContentValues();
        // Assign Values to DB
        todoValues.put("email", email);
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
    public ArrayList<String[]> getEntriesByDate(String email)
    {
        Cursor cursor = db.rawQuery("SELECT * FROM todo WHERE email= '"+ email +"' ORDER BY expiration_date ASC", null);
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

    public ArrayList<String[]> getEntriesByIsFinished(String email)
    {
        Cursor cursor = db.rawQuery("SELECT * FROM todo WHERE email= '"+ email +"' ORDER BY cast(is_finished as unsigned) DESC, expiration_date ASC", null);
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

    public ArrayList<String[]> getEntriesByIsFavourite(String email)
    {
        Cursor cursor = db.rawQuery("SELECT * FROM todo WHERE email = '"+ email +"' ORDER BY cast(is_favourite as unsigned) DESC, expiration_date ASC", null);
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
        return todo;
    }

    public String formatDateTimeToMysql(String date, String time)
    {
        String year, month, day, datetime;
        day = date.substring(0,2);
        month = date.substring(3,5);
        year = date.substring(6);
        datetime = year + "-" + month + "-" + day + " " + time;
        return datetime;
    }

    public String getTimeFromMysql(String datetime)
    {
        String time = datetime.substring(11,16); //Cut Date and Seconds
        return time;
    }

    public String getDateFromMysql(String datetime)
    {
        String year, month, day, date;
        day = datetime.substring(8,10);
        month = datetime.substring(5,7);
        year = datetime.substring(0,4);
        date = day + "." + month + "." + year;
        return date;
    }


    public ArrayList<String[]> getContactsFromPhoneList(ContentResolver resolver, String todoID)
    {

        ArrayList<String[]> contacts = new ArrayList<String[]>();


        Cursor cursor = resolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

        while (cursor.moveToNext()){
            String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            String mail = "";
            String phoneNumber = "";
            String hasPhoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

            //Get Mail Address from Contact ID
            Cursor emailCur = resolver.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null, ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?", new String[]{id}, null);
            while (emailCur.moveToNext()) {
                 mail = emailCur.getString(
                        emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
            }
            emailCur.close();

            //Get Phone Number from COnatct ID
            Cursor phoneCur = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);
            while (phoneCur.moveToNext()) {
                phoneNumber = phoneCur.getString(
                        phoneCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            }
            phoneCur.close();

            String tmpTodo[] = new String[]{id,name,hasPhoneNumber, todoID, mail,phoneNumber};
            contacts.add(tmpTodo);



        }
        cursor.close();
        return contacts;
    }

    public void addContactToTodo(String todoId, String name, String hasNumber, String mail, String phone_number)
    {
        //Check if Contact is in To-do already
        if(!isContactInToDo(todoId, name))
        {
            ContentValues todoValues = new ContentValues();
            // Assign Values to DB
            todoValues.put("todo", todoId);
            todoValues.put("name", name);
            todoValues.put("mail", mail);
            todoValues.put("has_phone", hasNumber);
            todoValues.put("phone_number", phone_number);
            //Insert row in table
            db.insert("contact", null, todoValues);
        }


    }

    public boolean isContactInToDo(String todoID, String name)
    {
        Cursor cursor = db.rawQuery("SELECT * FROM contact WHERE todo = '" + todoID + "' AND name = '" + name + "' ", null);
        if(cursor.getCount() == 0)
        {
            return false;
        }
        return true;
    }

    public ArrayList<String[]> getContactsFromToDo(String toDoID) {
        Cursor cursor = db.rawQuery("SELECT * FROM contact WHERE todo = '" + toDoID + "' ", null);
        cursor.moveToFirst();

        ArrayList<String[]> contacts = new ArrayList<String[]>();

        while (!cursor.isAfterLast()) {
            String tmpContact[] = new String[]{
                    cursor.getString(cursor.getColumnIndex("id")),
                    cursor.getString(cursor.getColumnIndex("name")),
                    cursor.getString(cursor.getColumnIndex("has_phone")),
                    cursor.getString(cursor.getColumnIndex("todo")),
                    cursor.getString(cursor.getColumnIndex("mail")),
                    cursor.getString(cursor.getColumnIndex("phone_number")),
            };
            contacts.add(tmpContact);
            cursor.moveToNext();
        }
        cursor.close();
        return contacts;
    }

    public void deleteContact(String id){
        String contact = "id=?";
        db.delete("contact", contact, new String[]{id});
    }



}
