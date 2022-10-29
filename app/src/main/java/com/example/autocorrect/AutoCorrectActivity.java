package com.example.autocorrect;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Point;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Button;
import android.os.Bundle;
import android.widget.GridLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedList;

public class AutoCorrectActivity extends AppCompatActivity {
    Button backButton;
    DatabaseManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.auto_correct);

        dbManager = new DatabaseManager( this );

        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(view -> finish());
        updateView( );
    }

    // Build a View dynamically with all the candies
    public void updateView() {
        ScrollView scrollView = (ScrollView)findViewById(R.id.scrollView);;
        scrollView.removeAllViews();
        LinkedList<String> corrected = dbManager.selectAll( );

        // create ScrollView and GridLayout
        GridLayout grid = new GridLayout( this );
        grid.setRowCount( corrected.size( ) + 1);
        grid.setColumnCount( 5 );

        // create arrays of components
        TextView[] ids = new TextView[corrected.size( )];
        EditText [][] wordPairs = new EditText[corrected.size( )][2];
        Button [] updates = new Button[corrected.size( )];
        Button [] deletes = new Button[corrected.size( )];
        UpdateHandler update = new UpdateHandler( );
        DeleteHandler delete = new DeleteHandler( );

        // retrieve width of screen
        Point size = new Point( );
        getWindowManager( ).getDefaultDisplay( ).getSize( size );
        int width = size.x;

        int i = -1;

        for (String curr: corrected) {
            // create the TextView for the candy's id
            i++;
            ids[i] = new TextView( this );
            ids[i].setGravity( Gravity.CENTER );

            ids[i].setText( String.valueOf(i+1));

            wordPairs[i][0] = new EditText( this );
            wordPairs[i][1] = new EditText( this );

            wordPairs[i][0].setText( curr.split(" ")[1]   );
            wordPairs[i][1].setText( curr.split(" ")[2] );

            wordPairs[i][0].setId( 10 * Integer.parseInt(curr.split(" ")[0] ));
            wordPairs[i][1].setId( 10 * Integer.parseInt(curr.split(" ")[0] ) + 1 );

            // create the button
            updates[i] = new Button( this );
            updates[i].setText( "Update" );
            updates[i].setId( Integer.parseInt(curr.split(" ")[0] ) );

            // set up event handling
            updates[i].setOnClickListener(update);

            // create the button
            deletes[i] = new Button( this );
            deletes[i].setText( "X" );

            deletes[i].setId( Integer.parseInt(curr.split(" ")[0] ) );

            // set up event handling
            deletes[i].setOnClickListener(delete);
        }

        for(int j = 0; j < corrected.size(); j++){
            // add the elements to grid
            grid.addView( ids[j], ( int ) ( width * .1 ),
                    ViewGroup.LayoutParams.WRAP_CONTENT );

            grid.addView( wordPairs[j][0], ( int ) ( width * .3 ),
                    ViewGroup.LayoutParams.WRAP_CONTENT );

            grid.addView( wordPairs[j][1], ( int ) ( width * .3 ),
                    ViewGroup.LayoutParams.WRAP_CONTENT );

            grid.addView( updates[j], ( int ) ( width * .2 ),
                    ViewGroup.LayoutParams.WRAP_CONTENT );

            grid.addView( deletes[j], ( int ) ( width * .1 ),
                    ViewGroup.LayoutParams.WRAP_CONTENT );
        }

        // add new word to grid
        EditText newC =  new EditText(this);
        EditText newI =  new EditText(this);
        Button add =  new Button(this);

        TextView id = new TextView(this);
        add.setText( "ADD" );

        grid.addView( id, ( int ) ( width * .1 ),
                ViewGroup.LayoutParams.WRAP_CONTENT );

        grid.addView( newI, ( int ) ( width * .3 ),
                ViewGroup.LayoutParams.WRAP_CONTENT );

        grid.addView( newC, ( int ) ( width * .3 ),
                ViewGroup.LayoutParams.WRAP_CONTENT );

        grid.addView( add, ( int ) ( width * .2 ),
                ViewGroup.LayoutParams.WRAP_CONTENT );

        add.setOnClickListener(new View.OnClickListener()
           {
               @Override
               public void onClick(View v) {
                   String c = newC.getText().toString();
                   String i = newI.getText().toString();
                   if(c.trim().length() == 0 || i.trim().length() == 0){
                       Toast.makeText(AutoCorrectActivity.this, "Can't add empty string",
                               Toast.LENGTH_SHORT).show();
                       return;
                   }
                   Correct newWord = new Correct(2, i, c);
                   dbManager.insert(newWord);
                   Toast.makeText(AutoCorrectActivity.this, "word added",
                           Toast.LENGTH_SHORT).show();
                   updateView();
               }
           });

        scrollView.addView(grid);
    }

    private class UpdateHandler implements View.OnClickListener {
        public void onClick(View v) {
            // retrieve correct and misspelled word
            int id = v.getId();
            EditText correctET = (EditText) findViewById(10 * id);
            EditText misspelledET = (EditText) findViewById(10 * id + 1);
            String correctStr = correctET.getText().toString();
            String misspelledStr = misspelledET.getText().toString();

            // update word in database
            dbManager.updateById(id, correctStr, misspelledStr);
            Toast.makeText(AutoCorrectActivity.this, "word updated",
                    Toast.LENGTH_SHORT).show();
            updateView();
        }
    }

    private class DeleteHandler implements View.OnClickListener {
        public void onClick(View v) {
            int id = v.getId();

            // delete word in database
            dbManager.deleteById(id);
            Toast.makeText(AutoCorrectActivity.this, "word deleted",
                    Toast.LENGTH_SHORT).show();
            updateView();
        }
    }

}
