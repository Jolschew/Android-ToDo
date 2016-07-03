package todo.kebejaol.todo.Activities;

import android.app.Application;

/**
 * Created by Jan on 29.06.16.
 * Help-Class for Global Email-Address
 */
public class User extends Application {

    String username = null;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
