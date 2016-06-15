package todo.kebejaol.todo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Overview extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);

        final Button bAddTodo = (Button) findViewById(R.id.bAddTodo);

        if ( bAddTodo != null) {

            bAddTodo.setOnClickListener(new View.OnClickListener() {

                public void onClick(View view){

                    // change activity to login or overview
                    Intent addTodoIntent = new Intent(Overview.this, AddTodo.class);
                    Overview.this.startActivity(addTodoIntent);



                }
            });
        }
    }
}
