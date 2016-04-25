package todo.kebejaol.todo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Jan on 25.04.16.
 *
 * Create new Database if no DB exists
 */
public class DBHelper extends SQLiteOpenHelper
{

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version)
    {
        super(context, name, factory, version);
    }


    // Called when no DB exists
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {
        // call Constant-String "DBCREATE" from LoginDBAdapter
        sqLiteDatabase.execSQL(LoginDBAdapter.DB_CREATE);
    }


    // Called when DB Version of the DB needs to be upgraded to current version
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1)
    {
        //DROP old DB and CREATE a new one
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS" + "TEMPLATE");
        onCreate(sqLiteDatabase);
    }
}
