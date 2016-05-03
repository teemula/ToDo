package laakso.teemu.todo;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Paint;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<TodoItem> todos = new ArrayList<>();
    ListView listView;
    String filename = "Todos";
    ArrayAdapter<TodoItem> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get ListView object from xml
        listView = (ListView) findViewById(R.id.list);

        // Load list of todos from internal storage
        loadfile();

        // Define a new Adapter & assign adapter to ListView
        listView.setAdapter(adapter = new ArrayAdapter<TodoItem>(this,
                R.layout.row_layout, R.id.label, todos) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView textView = (TextView) super.getView(position, convertView, parent);

                if(todos.get(position).isDone()) {
                    textView.setBackgroundResource(R.color.done);
                    textView.setPaintFlags(textView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                }
                else {
                    textView.setBackgroundResource(R.color.undone);
                    textView.setPaintFlags(textView.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
                }
                return textView;
            }

        });


        /* Remove item with long click */
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                todos.remove(position);
                adapter.notifyDataSetChanged();
                savefile();
                return true;
            }
        });

        /* ListView click listener marks item done / undone */
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView textView = (TextView) view;
                // If item is done, mark it undone
                if(todos.get(position).isDone()) {
                    view.setBackgroundResource(R.color.undone);
                    textView.setPaintFlags(textView.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
                    todos.get(position).setDone(false);
                }
                // If item is undone, mark it done
                else {
                    view.setBackgroundResource(R.color.done);
                    textView.setPaintFlags(textView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    todos.get(position).setDone(true);
                }
                savefile();
            }
        });

    }

    /**
     * Add item to the list
     */
    public void addItem(View view) {
        showInputDialog();
        savefile();
    }

    /**
     * Save file to internal storage
     */
    public void savefile(){
        FileOutputStream fos;
        try {
            fos = openFileOutput(filename, Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(todos);
            fos.close();
            oos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Load file from internal storage
     */
    public void loadfile(){
        FileInputStream fis;
        try {
            fis = openFileInput(filename);
            ObjectInputStream ois = new ObjectInputStream(fis);
            todos = (ArrayList<TodoItem>) ois.readObject();
            ois.close();
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Ask new TodoItem from user
     */
    public void showInputDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("To do:");

        // Set up the input
        final EditText input = new EditText(this);
        // Specify the type of input expected
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        /* Set up the buttons */
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String todoInput = "";
                todoInput = input.getText().toString();
                // Add new item to the list and notify the adapter
                if (todoInput != "") {
                    todos.add(new TodoItem(todoInput));
                }
                adapter.notifyDataSetChanged();
                savefile();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }
}