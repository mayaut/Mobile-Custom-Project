package com.mayaut.myproject;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
public class AddCard extends AppCompatActivity {
    public static final String QUESTION_KEY = "question_key";
    public static final String ANSWER_KEY = "answer_key";
    public static final String WRONG_ANSWER1_KEY = "wrong_answer1_key";
    public static final String WRONG_ANSWER2_KEY = "wrong_answer2_key";

    ImageView cancelButton, saveButton;
    EditText questionField, answerField, wrongAnswerField1, wrongAnswerField2;
    String questionString, answerString, wrongAnswer1String, wrongAnswer2String;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.make_flashcard);

        cancelButton = findViewById(R.id.cancel_make);
        saveButton = findViewById(R.id.save_button);

       questionField = findViewById(R.id.editQuestion);
       answerField = findViewById(R.id.editAnswer);

       wrongAnswerField1 = findViewById(R.id.editIncorrectAnswer1);
        wrongAnswerField2 = findViewById(R.id.editIncorrectAnswer2);

        questionString = getIntent().getStringExtra(QUESTION_KEY);
         answerString = getIntent().getStringExtra(ANSWER_KEY);
        wrongAnswer1String = getIntent().getStringExtra(WRONG_ANSWER1_KEY);
        wrongAnswer2String = getIntent().getStringExtra(WRONG_ANSWER2_KEY);

        cancelButton.setOnClickListener(view -> {
            finish(); });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent data = new Intent();
                String inputQuestion = ((EditText) findViewById(R.id.editQuestion)).getText().toString();
                String thirdAnswer = ((EditText) findViewById(R.id.editAnswer)).getText().toString();
                String firstAnswer = ((EditText) findViewById(R.id.editIncorrectAnswer1)).getText().toString();
                String secondAnswer = ((EditText) findViewById(R.id.editIncorrectAnswer2)).getText().toString();

                if (inputQuestion.equals("") || thirdAnswer.equals("")) {
                    Toast.makeText(AddCard.this, "Please make sure you've filled out all required fields!", Toast.LENGTH_SHORT).show();
                } else {
                    data.putExtra(QUESTION_KEY, inputQuestion);
                    data.putExtra(ANSWER_KEY, thirdAnswer);
                    data.putExtra(WRONG_ANSWER1_KEY, firstAnswer);
                    data.putExtra(WRONG_ANSWER2_KEY, secondAnswer);
                    setResult(RESULT_OK, data);
                    finish();
                }
            }
        });

        if (questionString != null && answerString != null) {
            questionField.setText(questionString);
            answerField.setText(answerString);
            wrongAnswerField1.setText(wrongAnswer1String);
            wrongAnswerField2.setText(wrongAnswer2String);
        }
    }
}
