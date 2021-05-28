package com.example.todo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
//import android.os.FileUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.todo.ItemsAdapter;
import com.example.todo.R;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
{
    public static final String Key_Item_Text = "item_text";
    public static final String Key_Item_Position = "item_position";
    public static final int Edit_Text_Code = 20;

    List<String> items;

    Button btAdd;
    EditText etItems;
    RecyclerView rvItem;
    ItemsAdapter itemsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btAdd = findViewById(R.id.btnAdd);
        etItems = findViewById(R.id.etItem);
        rvItem = findViewById(R.id.rvItems);

        loadItems();

        ItemsAdapter.OnlongClickListener onLongClickListener = new ItemsAdapter.OnlongClickListener()
        {
            @Override
            public void OnItemLongClicker(int Position)
            {
                // Delete the item from the mode
                items.remove(Position);
                // notify the adapter
                itemsAdapter.notifyItemRemoved(Position);
                Toast.makeText(getApplicationContext(), "Item was removed", Toast.LENGTH_SHORT).show();
                saveItems();
            }
        };

        ItemsAdapter.OnClickListener onClickListener = new ItemsAdapter.OnClickListener()
        {
            @Override
            public void onItemClicked(int position)
            {
                Log.d("MainActivity","Single Click at position" + position);
                // Create the new activity
                Intent i = new Intent(MainActivity.this, EditActivity.class);
                // pass the data that is being edited
                i.putExtra(Key_Item_Text,items.get(position));
                i.putExtra(Key_Item_Position,position);
                // display the activity
                startActivityForResult(i,Edit_Text_Code);
            }
        };

        itemsAdapter = new ItemsAdapter(items, onLongClickListener,onClickListener);
        rvItem.setAdapter(itemsAdapter);
        rvItem.setLayoutManager(new LinearLayoutManager(this));

        btAdd.setOnClickListener (new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String todoItem = etItems.getText().toString();
                //Add item to the Model
                 items.add(todoItem);
                 //Notify adpater that an item is inserted
                itemsAdapter.notifyItemInserted(items.size() - 1);
                etItems.setText("");
                Toast.makeText(getApplicationContext(), "Item was added", Toast.LENGTH_SHORT).show();
                saveItems();
            }
        });
    }

    // Handle the result of the edit activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
       if(resultCode == RESULT_OK && requestCode == Edit_Text_Code)
       {
           //Retreive the update text value
           String itemText = data.getStringExtra(Key_Item_Text);
           //extract the original position of the edited  item from the position key
           int position = data.getExtras().getInt(Key_Item_Position);

           //update the model at the right position with new item text
           items.set(position,itemText);
           //notify the adpter
           itemsAdapter.notifyItemChanged(position);
           //persist the changes
           saveItems();
           Toast.makeText(getApplicationContext(), "Item has updated Successfully", Toast.LENGTH_SHORT).show();
       }else
       {
           Log.w("MainActivity","Unknown call to onActivityResult");
       }

    }

    private File getDataFile()
    {
        return new File(getFilesDir(),"data.txt");
    }

    //this funtion will load items by reading every line of the data file
    @SuppressWarnings("rawtypes")
    private void loadItems()
    {
        try
        {
            items = new ArrayList<>(FileUtils.readLines(getDataFile(),Charset.defaultCharset()));
        } catch (IOException e)
        {
            Log.e("MainActivity","Error reading items",e);
            items = new ArrayList<>();
        }
    }

    //this funtion saves item by writting them into the dat file
    private void saveItems()
    {
        try {
            FileUtils.writeLines(getDataFile(),items);
        } catch (IOException e) {
            Log.e("MainActivity","Error writting items",e);
        }
    }
}