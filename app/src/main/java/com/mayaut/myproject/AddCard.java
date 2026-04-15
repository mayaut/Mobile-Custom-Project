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

        Intent intent = getIntent();
        String questionString = intent.getStringExtra(QUESTION_KEY);
        String answerString = intent.getStringExtra(ANSWER_KEY);
        String wrong1String = intent.getStringExtra(WRONG_ANSWER1_KEY);
        String wrong2String = intent.getStringExtra(WRONG_ANSWER2_KEY);

        if (questionString != null) {
            questionField.setText(questionString);
        }
        if (answerString != null) {
            answerField.setText(answerString);
        }
        if (wrong1String != null) {
            wrongAnswerField1.setText(wrong1String);
        }
        if (wrong2String != null) {
            wrongAnswerField2.setText(wrong2String);
        }

        cancelButton.setOnClickListener(view -> {
            finish();
        });

        saveButton.setOnClickListener(v -> {
                String question = questionField.getText().toString().trim();
                String answer = answerField.getText().toString().trim();
                String wrong1 = wrongAnswerField1.getText().toString().trim();
                String wrong2 = wrongAnswerField2.getText().toString().trim();

            if (question.isEmpty() || answer.isEmpty()) {
                Toast.makeText(this,
                        "input your flashcard details pls!",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            Intent data = new Intent();
            data.putExtra(QUESTION_KEY, question);
            data.putExtra(ANSWER_KEY, answer);
            data.putExtra(WRONG_ANSWER1_KEY, wrong1);
            data.putExtra(WRONG_ANSWER2_KEY, wrong2);

            setResult(RESULT_OK, data);
            finish();
            });
    }
}
