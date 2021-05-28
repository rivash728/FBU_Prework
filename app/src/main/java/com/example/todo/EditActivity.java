package com.example.todo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditActivity extends AppCompatActivity
{
    EditText edItem;
    Button btSave;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        edItem = findViewById(R.id.edItem);
        btSave = findViewById(R.id.btSave);

        getSupportActionBar().setTitle("Edit item");

        edItem.setText(getIntent().getStringExtra(MainActivity.Key_Item_Text));

        // when the user is done editing, they click the save button
        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // create an intent which will contain the results
                Intent intent = new Intent();
                // pass the data (result of editing)
                intent.putExtra(MainActivity.Key_Item_Text,edItem.getText().toString());
                intent.putExtra(MainActivity.Key_Item_Position,getIntent().getExtras().getInt(MainActivity.Key_Item_Position));
                // set the result
                setResult(RESULT_OK,intent);
                // finish activity, close the screen and go back
                finish();
            }
        });
    }
}