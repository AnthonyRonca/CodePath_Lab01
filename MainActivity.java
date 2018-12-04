/*
*
*       Anthony Ronca - CodePath Android Mobile Workshop
*
*       This program is a flashcard App that takes user input, validates it, then creates a card
*       used to study. Once created, this card can be edited and sifted through. User has the option
*       to choose a card randomly, by number, or by clicking next/prev. User can challenge themselves
*       by hiding the multiple choice answers. Multiple choice answers also change. All optionals
*       completed.
*
*
*      //VARIABLES USED://
*
*       #DISPLAY
*
*          Question                     -   Stores displayed Question
*          Answer                       -   stores displayed Answer
*          possibleAnswer1              -   Stores correct answer
*          possibleAnswer2              -   Stores wrong answer
*          possibleAnswer3              -   Stores wrong answer
*
*
*       #FLASHCARDS
*
*          flashcardDatabase            -   Data type storing Q, A, and 2 wrong A's
*          allFlashcards                -   Array storing flashcardDatabase elements(cards)
*          currentCardDisplayedIndex    -   Index that keeps track of what card is being used
*          cardToEdit                   -   Current flashcard to be updated then replace previous
*          addFlashCard                 -   Creates new flashCard then adds it to database
*          newFlashCard                 -   New object created
*          editFlashCard                -   Element accessed by index is replaced
*
*
*       #BUTTONS
*
*          next                         -   Adds 1 to count, thus showing next card
*          prev                         -   removes 1 from count, thus showing previous card
*          delete                       -   Deletes card from flashcardDatabase
*          random                       -   Returns random card*
*          toggledVisibility            -   Button allows user to hide/un-hide answers
*          questionVisibility           -   Determines if user is view Question or Answer
*          selection1                   -   Jumps to Question 1 (index 0)
*          selection2                   -   Jumps to Question 2 (index 1)
*          selection3                   -   Jumps to Question 3 (index 2)
*
*
*       #ANIMATIONS/
*
*          leftOutAnim                  -   Left in animation on next
*          rightInAnim                  -   Right out animation on next
*          rotate                       -   Rotates Question/Answer on click
*          countDownTimer               -   Timer Widget, starts when a new question appears
*          mProgressBar                 -   Animation to show time being passed
*
*
*       #TEXTVIEW LOADERS
*
*          newQ                         -   Loads Question into flashcard
*          newA                         -   Loads right answer into flashcard
*          newA1                        -   Loads right answer into flashcard, same as newA
*          newA2                        -   Loads wrong answer into flashcard
*          newA3                        -   Loads wrong answer into flashcard
*
*
*
*
*
*/

package com.example.anthony.anthonyronca_codepathlab1;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.plattysoft.leonids.ParticleSystem;

import java.util.List;
import java.util.Objects;
import java.util.Random;


public class MainActivity extends AppCompatActivity {

    private Boolean questionVisibility = true;  // Question is visible
    private Boolean toggledVisibility = true;   // User has not toggled Answers to invisible


    // allows flashcardDatabase to be accessed from outside onCreate scope
    FlashcardDatabase flashcardDatabase;

    List<Flashcard> allFlashcards; // Creates an array of flashCards

    int currentCardDisplayedIndex = 0; // stores card being viewed

    Flashcard cardToEdit; // Points to flashCard user is viewing when clicking edit

    CountDownTimer countDownTimer; // Timer widget whenever user sees a new question
    ProgressBar mProgressBar; // Progress bar widget that looks cool
    CountDownTimer mCountDownTimer; // Timer object
    int i = 0; // Count for progress bar


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        flashcardDatabase = new FlashcardDatabase(getApplicationContext());

        allFlashcards = flashcardDatabase.getAllCards(); // Checks database for saved cards first


        // Initializes timer/progressBar
        mProgressBar = (ProgressBar)findViewById(R.id.timerbar);
        mProgressBar.setProgress(i);
        countDownTimer = new CountDownTimer(10000, 1000) {
            public void onTick(long millisUntilFinished) {
                ((TextView) findViewById(R.id.timer2)).setText("" + millisUntilFinished / 1000);
                mProgressBar.setProgress((int)i*100/(10000/1000));
            }

            public void onFinish() {
                i++;
                mProgressBar.setProgress(100);
            }
        };

        // If there are no previously saved flashcards a default card is displayed
        if (allFlashcards != null && allFlashcards.size() > 0) {
            ((TextView) findViewById(R.id.Question)).setText(allFlashcards.get(0).getQuestion());
            ((TextView) findViewById(R.id.Answer)).setText(allFlashcards.get(0).getAnswer());
        }


        // Object to toggle answers to invisible
        ImageButton toggleIcon =(ImageButton)findViewById(R.id.toggleButton);


        toggleIcon.setOnClickListener(new View.OnClickListener() {
            @Override

            //when Question is clicked run this block
            public void onClick(View V) {


                // validate that the question is currently visible
                if (toggledVisibility) {
                    // set question to invisible and answer to visible, showing answer
                    findViewById(R.id.possibleAnswer1).setVisibility(View.INVISIBLE);
                    findViewById(R.id.possibleAnswer2).setVisibility(View.INVISIBLE);
                    findViewById(R.id.possibleAnswer3).setVisibility(View.INVISIBLE);
                    // set questionVisibility to false to signify it is hidden
                    toggledVisibility = false;

                } else {

                    // set question to invisible and answer to visible, showing answer
                    findViewById(R.id.possibleAnswer1).setVisibility(View.VISIBLE);
                    findViewById(R.id.possibleAnswer2).setVisibility(View.VISIBLE);
                    findViewById(R.id.possibleAnswer3).setVisibility(View.VISIBLE);
                    // set questionVisibility to false to signify it is hidden
                    toggledVisibility = true;
                }

            }
        }); // toggle



    // selects Question text view and detects if it is clicked
    findViewById(R.id.Question).setOnClickListener(new View.OnClickListener() {
        @Override

        //when Question is clicked run this block
        public void onClick(View V) {
            // validate that the question is currently visible
            if (questionVisibility) {
                // set question to invisible and answer to visible, showing answer
                findViewById(R.id.Answer).setVisibility(View.VISIBLE);
                findViewById(R.id.Question).setVisibility(View.INVISIBLE);
                // set questionVisibility to false to signify it is hidden
                questionVisibility = false;



                // WHOOOOOOOOOOOA, fast rotation
                RotateAnimation rotate = new RotateAnimation(0, 360,
                        Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                        0.5f);

                rotate.setDuration(1200);
                findViewById(R.id.Answer).setAnimation(rotate);
            }

        }
    });

        // selects Answer text view and detects if it is clicked
        findViewById(R.id.Answer).setOnClickListener(new View.OnClickListener() {
            @Override
            // validate that the Answer is currently visible
            public void onClick(View V) {
                if (!questionVisibility) {
                    // set Answer to invisible and Question to visible, showing question again
                    findViewById(R.id.Question).setVisibility(View.VISIBLE);
                    findViewById(R.id.Answer).setVisibility(View.INVISIBLE);
                    // set questionVisibility to true to signify it is NOT hidden
                    questionVisibility = true;
                    //

                }
            }
        });



        // selects Answer text view and detects if it is clicked
        findViewById(R.id.possibleAnswer1).setOnClickListener(new View.OnClickListener() {
            @Override
            // validate that the Answer is currently visible
            public void onClick(View V) {
                    // set Answer to invisible and Question to visible, showing question again
                    findViewById(R.id.possibleAnswer1).setBackgroundColor((Color.parseColor("#5eaf71")));
                    findViewById(R.id.possibleAnswer2).setBackgroundColor((Color.parseColor("#ea5e61")));
                    findViewById(R.id.possibleAnswer3).setBackgroundColor((Color.parseColor("#ea5e61")));

                new ParticleSystem(MainActivity.this, 300, R.drawable.confetti, 3000)
                        .setSpeedRange(0.2f, 0.5f)
                        .oneShot(findViewById(R.id.possibleAnswer1), 500);
            }
        }); // END possibleAnswer1 onClick

        findViewById(R.id.possibleAnswer2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View V) {
                    // set Answer to invisible and Question to visible, showing question again
                    findViewById(R.id.possibleAnswer1).setBackgroundColor((Color.parseColor("#5eaf71")));
                    findViewById(R.id.possibleAnswer2).setBackgroundColor((Color.parseColor("#ea5e61")));
                    // set questionVisibility to true to signify it is NOT hidden
            }
        }); // END possibleAnswer2 onClick

        findViewById(R.id.possibleAnswer3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View V) {
                    findViewById(R.id.possibleAnswer1).setBackgroundColor((Color.parseColor("#5eaf71")));
                    findViewById(R.id.possibleAnswer3).setBackgroundColor((Color.parseColor("#ea5e61")));
            }

        }); // END possibleAnswer3 onClick

        // END SHOW ANSWER

        findViewById(R.id.selection1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View V) {

                findViewById(R.id.possibleAnswer1).setBackgroundColor((Color.parseColor("#dcd8d8")));
                findViewById(R.id.possibleAnswer2).setBackgroundColor((Color.parseColor("#dcd8d8")));
                findViewById(R.id.possibleAnswer3).setBackgroundColor((Color.parseColor("#dcd8d8")));
            }

        });




        findViewById(R.id.next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Dramatic animation on question/answer click
                final Animation leftOutAnim = AnimationUtils.loadAnimation(v.getContext(), R.anim.left_in);
                final Animation rightInAnim = AnimationUtils.loadAnimation(v.getContext(), R.anim.right_in);

                leftOutAnim.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        // this method is called when the animation first starts
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        findViewById(R.id.Question).startAnimation(rightInAnim);
                        // this method is called when the animation is finished playing
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                        // we don't need to worry about this method
                    }
                });

                findViewById(R.id.Question).startAnimation(leftOutAnim);

                // Hides correct answer when showing a new question
                findViewById(R.id.possibleAnswer1).setBackgroundColor((Color.parseColor("#dcd8d8")));
                findViewById(R.id.possibleAnswer2).setBackgroundColor((Color.parseColor("#dcd8d8")));
                findViewById(R.id.possibleAnswer3).setBackgroundColor((Color.parseColor("#dcd8d8")));

                startTimer();


                currentCardDisplayedIndex++;

                // make sure we don't get an IndexOutOfBoundsError if we are viewing the last indexed card in our list
                if (currentCardDisplayedIndex >= allFlashcards.size() - 1) {
                    currentCardDisplayedIndex = 0;

                }

                    findViewById(R.id.Question).setVisibility(View.VISIBLE);
                    findViewById(R.id.Answer).setVisibility(View.INVISIBLE);
                    // set questionVisibility to true to signify it is NOT hidden
                    questionVisibility = true;

                    // set the question and answer TextViews with data from the database
                    setFlashcardDatabase();

            }
        });

        findViewById(R.id.previous).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Animation on previous question
                final Animation leftOutAnim = AnimationUtils.loadAnimation(v.getContext(), R.anim.left_in);
                final Animation rightInAnim = AnimationUtils.loadAnimation(v.getContext(), R.anim.right_in);

                leftOutAnim.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        // this method is called when the animation first starts
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        findViewById(R.id.Question).startAnimation(rightInAnim);
                        // this method is called when the animation is finished playing
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                        // we don't need to worry about this method
                    }
                });

                findViewById(R.id.Question).startAnimation(leftOutAnim);

                // Hides correct answer when showing a new question
                findViewById(R.id.possibleAnswer1).setBackgroundColor((Color.parseColor("#dcd8d8")));
                findViewById(R.id.possibleAnswer2).setBackgroundColor((Color.parseColor("#dcd8d8")));
                findViewById(R.id.possibleAnswer3).setBackgroundColor((Color.parseColor("#dcd8d8")));
                startTimer();


                // advance our pointer index so we can show the previous card
                currentCardDisplayedIndex--;

                    // make sure we don't get an IndexOutOfBoundsError if we are viewing the last indexed card in our list
                    if (currentCardDisplayedIndex <= allFlashcards.size() - 1) {
                        currentCardDisplayedIndex = allFlashcards.size() - 1;

                    }

                    findViewById(R.id.Question).setVisibility(View.VISIBLE);
                    findViewById(R.id.Answer).setVisibility(View.INVISIBLE);
                    // set questionVisibility to true to signify it is NOT hidden
                    questionVisibility = true;

                    // set the question and answer TextViews with data from the database
                    setFlashcardDatabase();
            }



        });


        findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Validate data to prevent out of bounds
                if (allFlashcards.size() <= 0) {

                    Toast.makeText(getApplicationContext(),
                            "No Other FlashCards", Toast.LENGTH_SHORT).show();
                }

                else {

                    // Pre-Defined function that removes card from array
                    flashcardDatabase.deleteCard(((TextView) findViewById(R.id.Question)).getText().toString());

                    // List is updated
                    allFlashcards = flashcardDatabase.getAllCards();

                    Toast.makeText(getApplicationContext(),
                    "Flashcard Deleted", Toast.LENGTH_SHORT).show();

                    // Index displays next card after deletion
                    currentCardDisplayedIndex--;

                    // make sure we don't get an IndexOutOfBoundsError if we are viewing the last indexed card in our list
                    if (currentCardDisplayedIndex <= allFlashcards.size() - 1) {
                        currentCardDisplayedIndex = 0;
                    }

                    findViewById(R.id.Question).setVisibility(View.VISIBLE);
                    findViewById(R.id.Answer).setVisibility(View.INVISIBLE);
                    // set questionVisibility to true to signify it is NOT hidden
                    questionVisibility = true;

                    // set the question and answer TextViews with data from the database
                    setFlashcardDatabase();
                    startTimer();
                }

            }
        });

        findViewById(R.id.selection1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTimer();
                // advance our pointer index so we can show the next card
                currentCardDisplayedIndex = 0;

                // make sure we don't get an IndexOutOfBoundsError if we are viewing the last indexed card in our list
                if (currentCardDisplayedIndex > allFlashcards.size() - 1) {
                    currentCardDisplayedIndex = 0;


                }

                findViewById(R.id.Question).setVisibility(View.VISIBLE);
                findViewById(R.id.Answer).setVisibility(View.INVISIBLE);
                // set questionVisibility to true to signify it is NOT hidden
                questionVisibility = true;

                // set the question and answer TextViews with data from the database
                setFlashcardDatabase();
            }
        });

        findViewById(R.id.selection2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTimer();
                // advance our pointer index so we can show the next card
                currentCardDisplayedIndex = 1;

                // make sure we don't get an IndexOutOfBoundsError if we are viewing the last indexed card in our list
                if (currentCardDisplayedIndex > allFlashcards.size() - 1) {
                    currentCardDisplayedIndex = 0;


                }

                findViewById(R.id.Question).setVisibility(View.VISIBLE);
                findViewById(R.id.Answer).setVisibility(View.INVISIBLE);
                // set questionVisibility to true to signify it is NOT hidden
                questionVisibility = true;

                // set the question and answer TextViews with data from the database
                setFlashcardDatabase();
        }
        });

        findViewById(R.id.selection3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTimer();

                // advance our pointer index so we can show the next card
                currentCardDisplayedIndex = 2;

                // make sure we don't get an IndexOutOfBoundsError if we are viewing the last indexed card in our list
                if (currentCardDisplayedIndex > allFlashcards.size() - 1) {
                    currentCardDisplayedIndex = 0;

                }

                findViewById(R.id.Question).setVisibility(View.VISIBLE);
                findViewById(R.id.Answer).setVisibility(View.INVISIBLE);
                // set questionVisibility to true to signify it is NOT hidden
                questionVisibility = true;

                // set the question and answer TextViews with data from the database
                setFlashcardDatabase();
            }
        });

        findViewById(R.id.random).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startTimer();

                currentCardDisplayedIndex = getRandomNumber(0, allFlashcards.size() - 1);

                // make sure we don't get an IndexOutOfBoundsError if we are viewing the last indexed card in our list
                if (currentCardDisplayedIndex > allFlashcards.size() - 1) {

                }

                findViewById(R.id.Question).setVisibility(View.VISIBLE);
                findViewById(R.id.Answer).setVisibility(View.INVISIBLE);
                // set questionVisibility to true to signify it is NOT hidden
                questionVisibility = true;

                // set the question and answer TextViews with data from the database
                setFlashcardDatabase();
            }
        });


    } // scope of onCreate



    public void addQuestion(View view){
        Intent addFlashCard = new Intent(MainActivity.this, AddQuestionActivity.class);

        // Loads data into variables then passes it into next screen
        TextView editQ = findViewById(R.id.Question);
        TextView editA = findViewById(R.id.Answer);
        TextView editA2 = findViewById(R.id.possibleAnswer2);
        TextView editA3 = findViewById(R.id.possibleAnswer3);


        //  Passes data to be added in the addFlashCard Activity
        addFlashCard.putExtra("stringKey1", editQ.getText().toString());
        addFlashCard.putExtra("stringKey2", editA.getText().toString());
        // also pass other questions
        addFlashCard.putExtra("stringKey3", editA2.getText().toString());
        addFlashCard.putExtra("stringKey4", editA3.getText().toString());

        // Expecting results
        MainActivity.this.startActivityForResult(addFlashCard, 1);
        overridePendingTransition(R.anim.right_in, R.anim.left_in);
    }

    public void editQuestion(View view){
        Intent editFlashCard = new Intent(MainActivity.this, AddQuestionActivity.class);

        // Loads data into variables then passes it into next screen
        TextView editQ = findViewById(R.id.Question);
        TextView editA = findViewById(R.id.Answer);
        TextView editA2 = findViewById(R.id.possibleAnswer2);
        TextView editA3 = findViewById(R.id.possibleAnswer3);

        //  Passes data to be editted in the addFlashCard Activity
        editFlashCard.putExtra("stringKey1", editQ.getText().toString());
        editFlashCard.putExtra("stringKey2", editA.getText().toString());
        // also pass other questions
        editFlashCard.putExtra("stringKey3", editA2.getText().toString());
        editFlashCard.putExtra("stringKey4", editA3.getText().toString());


        // Sets the card being edited based on count
        cardToEdit = allFlashcards.get(currentCardDisplayedIndex);

        // Expecting results
        MainActivity.this.startActivityForResult(editFlashCard, 2);
        overridePendingTransition(R.anim.right_in, R.anim.left_in);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {


            Snackbar.make(findViewById(R.id.Question),
                    "Flashcard added successfully",
                    Snackbar.LENGTH_SHORT)
                    .show();

                // Strings that store data passed from AddQuestion intent "data"
                String string1 = Objects.requireNonNull(data.getExtras()).getString("String1");
                String string2 = data.getExtras().getString("String2");
                String string3 = data.getExtras().getString("String3");
                String string4 = data.getExtras().getString("String4");

                // Creates TextView variables representing the nodes that display the card
                final TextView newQ = findViewById(R.id.Question);
                final TextView newA = findViewById(R.id.Answer);
                final TextView newA1 = findViewById(R.id.possibleAnswer1);
                final TextView newA2 = findViewById(R.id.possibleAnswer2);
                final TextView newA3 = findViewById(R.id.possibleAnswer3);

                // Assign new values to displayed question
                newQ.setText(string1);
                newA.setText(string2);
                newA1.setText(string2);
                newA2.setText(string3);
                newA3.setText(string4);

            // New card is created
            Flashcard newFlashCard = new Flashcard(string1,string2);

            newFlashCard.setQuestion(string1);
            newFlashCard.setAnswer(string2);
            newFlashCard.setWrongAnswer1(string3);
            newFlashCard.setWrongAnswer2(string4);

            // New card is inserted into list
            flashcardDatabase.insertCard(newFlashCard);

            // database is updated
            allFlashcards = flashcardDatabase.getAllCards();


        }

        if (requestCode == 2 && resultCode == RESULT_OK) {



            Snackbar.make(findViewById(R.id.Question),
                    "Flashcard Edited successfully",
                    Snackbar.LENGTH_SHORT)
                    .show();

            // Strings that store data passed from AddQuestion intent "data"
            String string1 = data.getExtras().getString("String1");
            String string2 = data.getExtras().getString("String2");
            String string3 = data.getExtras().getString("String3");
            String string4 = data.getExtras().getString("String4");

            // Creates TextView variables representing the nodes that display the card, loads strings
            final TextView newQ = findViewById(R.id.Question);
            final TextView newA = findViewById(R.id.Answer);
            final TextView newA1 = findViewById(R.id.possibleAnswer1);
            final TextView newA2 = findViewById(R.id.possibleAnswer2);
            final TextView newA3 = findViewById(R.id.possibleAnswer3);

            // Assign new values to displayed question
            newQ.setText(string1);
            newA.setText(string2);
            newA1.setText(string2);
            newA2.setText(string3);
            newA3.setText(string4);

            // CardToEdit is modifyed instead of being added or deleted
            cardToEdit.setQuestion(string1);
            cardToEdit.setAnswer(string2);
            cardToEdit.setWrongAnswer1(string3);
            cardToEdit.setWrongAnswer2(string4);


            // Shows updates
            flashcardDatabase.updateCard(cardToEdit);

            allFlashcards = flashcardDatabase.getAllCards();

        }


    }



    private void startTimer() {
        countDownTimer.cancel();
        countDownTimer.start();
    }

    // Takes two int parameters and returns a random int between them
    public int getRandomNumber(int minNumber, int maxNumber) {
        Random rand = new Random();
        return rand.nextInt((maxNumber - minNumber) + 1) + minNumber;
    }

    public void setFlashcardDatabase(){

        // set the question and answer TextViews with data from the database

        ((TextView) findViewById(R.id.Question)).setText(allFlashcards.get(currentCardDisplayedIndex).getQuestion()); // only get an error when accessing the database
        ((TextView) findViewById(R.id.Answer)).setText(allFlashcards.get(currentCardDisplayedIndex).getAnswer());
        ((TextView) findViewById(R.id.possibleAnswer1)).setText(allFlashcards.get(currentCardDisplayedIndex).getAnswer());
        ((TextView) findViewById(R.id.possibleAnswer2)).setText(allFlashcards.get(currentCardDisplayedIndex).getWrongAnswer1());
        ((TextView) findViewById(R.id.possibleAnswer3)).setText(allFlashcards.get(currentCardDisplayedIndex).getWrongAnswer2());

    }



} // main

