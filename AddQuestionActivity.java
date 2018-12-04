
/*
*
*       Anthony Ronca - CodePath Android Mobile Workshop
*
*       This Intent is passed data from the MainActivity and returns new or modified data
*
*
*      //VARIABLES USED://
*
*       #EditTexts
*
*           questionField    -   Listens for data input in "question" EditText
*           answerField      -   Listens for data input in "correct answer" EditText
*           answerField2     -   Listens for data input in "wrong answer" EditText
*           answerField3     -   Listens for data input in "wrong answer" EditText
*
*
*       #StringHolders
*
*           editQ            -   Stores data input in questionField
*           editA            -   stores data input in answerField
*           editA2           -   stores data input in answerField2
*           editA3           -   stores data input in answerField3
*
*
*       #Objects
*
*           newQuestion      -   passes data from questionField to MainActivity
*           newAnswer        -   passes data from answerField to MainActivity
*           newAnswer2       -   passes data from answerField2 to MainActivity
*           newAnswer3       -   passes data from answerField3 to MainActivity

*/



package com.example.anthony.anthonyronca_codepathlab1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


public class AddQuestionActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_question2);

        //  Retrieves the data passed from original question and Answer
        String editQ = getIntent().getStringExtra("stringKey1");
        String editA = getIntent().getStringExtra("stringKey2");
        String editA2 = getIntent().getStringExtra("stringKey3");
        String editA3 = getIntent().getStringExtra("stringKey4");

        // Puts data in EditTexts so user can see original card
        EditText newQuestion = (EditText) findViewById(R.id.questionField);
        EditText newAnswer = (EditText) findViewById(R.id.answerField);
        EditText newAnswer2 = (EditText) findViewById(R.id.answerField2);
        EditText newAnswer3 = (EditText) findViewById(R.id.answerField3);

        newQuestion.setText(editQ);
        newAnswer.setText(editA);
        newAnswer2.setText(editA2);
        newAnswer3.setText(editA3);


        newQuestion.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Fires right as the text is being changed (even supplies the range of text)
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // Fires right before text is changing
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Fires right after the text has changed
                findViewById(R.id.questionField);
            }


        });

        //Listener for new Answer
        newAnswer.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Fires right as the text is being changed (even supplies the range of text)
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // Fires right before text is changing
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Fires right after the text has changed
                findViewById(R.id.answerField);
            }


        });


    }

    //cancels adding another flashcard
    public void goBack(View view) {
        Intent goBack = new Intent();
        startActivity(goBack);
    }


    // ADDS FLASHCARD
    public void changeQ(View view) {
        Intent data = new Intent();


        final EditText newQuestion = findViewById(R.id.questionField);
        final EditText newAnswer = findViewById(R.id.answerField);
        final EditText newAnswer2 = findViewById(R.id.answerField2);
        final EditText newAnswer3 = findViewById(R.id.answerField3);

        // Input data is then attached to keywords and sent to MainActivity
        data.putExtra("String1", newQuestion.getText().toString());
        data.putExtra("String2", newAnswer.getText().toString());
        data.putExtra("String3", newAnswer2.getText().toString());
        data.putExtra("String4", newAnswer3.getText().toString());


        // Data validation statement prevents null cards
        if (newQuestion.getText().toString().isEmpty() || (newAnswer.getText().toString().isEmpty())) {

            Toast.makeText(getApplicationContext(),
                    "ERROR - Please enter a question and answers", Toast.LENGTH_SHORT).show();

        } else {

            setResult(RESULT_OK, data);
            finish();
        }

    }


    // Edits an existing card
    public void editQ(View view) {

        Intent edits = new Intent();

        // Loads data from listener
        final EditText newQuestion = findViewById(R.id.questionField);
        final EditText newAnswer = findViewById(R.id.answerField);
        final EditText newAnswer2 = findViewById(R.id.answerField2);
        final EditText newAnswer3 = findViewById(R.id.answerField3);

        // Sends the data to MainActivity using Keywords
        edits.putExtra("String1", newQuestion.getText().toString());
        edits.putExtra("String2", newAnswer.getText().toString());
        edits.putExtra("String3", newAnswer2.getText().toString());
        edits.putExtra("String4", newAnswer3.getText().toString());


        // Data validation preventing null cards
        if (newQuestion.getText().toString().isEmpty() || (newAnswer.getText().toString().isEmpty())) {

            Toast.makeText(getApplicationContext(),
                    "ERROR - Please enter a question and answers", Toast.LENGTH_SHORT).show();

        } else {

            setResult(RESULT_OK, edits);
            finish();
        }

    }

}


