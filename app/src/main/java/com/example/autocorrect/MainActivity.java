package com.example.autocorrect;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.os.Bundle;

import java.util.LinkedList;

public class MainActivity extends AppCompatActivity {
    Button autoCorrect;
    EditText typed;
    boolean once;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DatabaseManager dbManager = new DatabaseManager( this);

        typed = (EditText) findViewById(R.id.typed);
        autoCorrect = (Button) findViewById(R.id.autoCorrect);
        once = true;
        typed.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {

                String curr = typed.getText().toString();

                if(curr.length() > 0 && curr.charAt(curr.length()-1) == ' ' && once) {
                    String[] currArr = curr.split(" ");

                    LinkedList<String> corrected = dbManager.selectAll();

                    for (String word : corrected) {
                        if (java.util.Arrays.asList(currArr).contains(word.split(" ")[1])) {
                            int index = java.util.Arrays.asList(currArr).indexOf(word.split(" ")[1]);
                            currArr[index] = word.split(" ")[2];
                        }
                    }

                    StringBuilder str = new StringBuilder();

                    for (int i = 0; i < currArr.length; i++) {
                        str.append(currArr[i]);
                        str.append(" ");
                    }

                    once = false;
                    typed.setText(str.toString());
                    int position = str.toString().length();
                    Editable etext = typed.getText();
                    Selection.setSelection(etext, position);
                }else{
                    once = true;
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        autoCorrect.setOnClickListener(view -> switchActivities());

    }

    private void switchActivities() {
        Intent switchActivityIntent = new Intent(this, AutoCorrectActivity.class);
        startActivity(switchActivityIntent);
    }
}