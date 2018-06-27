package me.jntalley.todoapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    // implements model as List <String>
    ArrayList<String> items;
    ArrayAdapter<String> itemsAdapter;
    ListView lvItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //obtain a reference to the ListView
        lvItems = (ListView) findViewById(R.id.lvItems);
        //inititalize the items list
        readItems();
        //initialize the adapter using above list
        itemsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        //wire the adapter
        lvItems.setAdapter(itemsAdapter);

//        //ADD INITIAL ITEMS
//        items.add("Submit Assignment to Github");
//        items.add("Finish Assignment 1");

        //set up listener on creation
        setupListViewListener();
    }

    public void onAddItem(View v) {
        //reference to EditText
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
        // grab EditText content as a string
        String itemText =etNewItem.getText().toString();
        //add item to list
        itemsAdapter.add(itemText);
        //store updated list
        writeItems();
        //clear the EditText
        etNewItem.setText("");
        //display to user
        Toast.makeText(getApplicationContext(), "Item added to list", Toast.LENGTH_SHORT).show();
    }

    private void setupListViewListener() {
        //set the ListViews long click function
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                items.remove(position);
                itemsAdapter.notifyDataSetChanged();
                Log.i("MainActivity", "Removed item " + position);
                //store updated list
                writeItems();
                return true;
            }
        });
    }

    private File getDataFile() {
        return new File(getFilesDir(), "todo.txt");
    }

    // read the items out of above file system
    private void readItems() {
        try {
            // create the array using file content
            items = new ArrayList<String>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        } catch (IOException e) {
            // print error
            e.printStackTrace();
            // reset list
            items = new ArrayList<>();
        }
    }

    // write items to  filesystem
    private void writeItems() {
        try {
            // save the item list as a line-delimited text file
            FileUtils.writeLines(getDataFile(), items);
        } catch (IOException e) {
            // print the error
            e.printStackTrace();
        }
    }
}
