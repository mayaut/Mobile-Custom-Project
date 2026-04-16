package com.mayaut.myproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.animation.Animator;
import android.content.Intent;
import android.view.ViewAnimationUtils;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.snackbar.Snackbar;
import java.util.List;
import java.util.Random;

public class FlashcardsFragment extends Fragment {
    FlashcardDatabase flashcardDatabase;
    List<Flashcard> allFlashcards;
    int cardIndex = 0;
    TextView flashcardQuestion, flashcardAnswer;
    TextView wrongAnswer1, wrongAnswer2, correctAnswer;
    ImageView nextButton, prevButton, deleteButton, resetButton;
    ImageView toggle, addButton, editButton;
    boolean isShowingAnswers = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_flashcards, container, false);

        flashcardQuestion = view.findViewById(R.id.flashcard_question);
        flashcardAnswer = view.findViewById(R.id.flashcard_answer);
        wrongAnswer1 = view.findViewById(R.id.answer1);
        wrongAnswer2 = view.findViewById(R.id.answer2);
        correctAnswer = view.findViewById(R.id.answer3);

        nextButton = view.findViewById(R.id.next_card);
        prevButton = view.findViewById(R.id.prev_card);
        deleteButton = view.findViewById(R.id.delete_card);
        resetButton = view.findViewById(R.id.reset);
        toggle = view.findViewById(R.id.toggle_choices_visibility);
        addButton = view.findViewById(R.id.add_card);
        editButton = view.findViewById(R.id.edit_card);

        flashcardDatabase = new FlashcardDatabase(requireContext());
        allFlashcards = flashcardDatabase.getAllCards();

        if (allFlashcards != null && !allFlashcards.isEmpty()) {
            updateUI(0);
        }

        //flip flashcard
        flashcardQuestion.setOnClickListener(v -> {
            flashcardQuestion.setVisibility(View.INVISIBLE);
            flashcardAnswer.setVisibility(View.VISIBLE);
        });
        flashcardAnswer.setOnClickListener(v -> {
            flashcardQuestion.setVisibility(View.VISIBLE);
            flashcardAnswer.setVisibility(View.INVISIBLE);
        });

        //next card
        nextButton.setOnClickListener(v -> {
            if (allFlashcards.size() > 0) {
                cardIndex = (cardIndex + 1) % allFlashcards.size();
                updateUI(cardIndex);
                resetMultipleChoice();
            }
        });

        prevButton.setOnClickListener(v -> {
            if (allFlashcards.size() > 0) {
                cardIndex = (cardIndex - 1 + allFlashcards.size()) % allFlashcards.size();
                updateUI(cardIndex);
                resetMultipleChoice();
            }
        });

        deleteButton.setOnClickListener(v -> {
            if (!allFlashcards.isEmpty()) {
                flashcardDatabase.deleteCard(flashcardQuestion.getText().toString());
                allFlashcards = flashcardDatabase.getAllCards();
                if (!allFlashcards.isEmpty()) {
                    cardIndex = 0;
                    updateUI(cardIndex);
                }
            }
        });

        resetButton.setOnClickListener(v -> {
            resetMultipleChoice();
            revealQuestion();
            wrongAnswer1.setVisibility(View.VISIBLE);
            wrongAnswer2.setVisibility(View.VISIBLE);
            correctAnswer.setVisibility(View.VISIBLE);
        });

        toggle.setOnClickListener(v -> {
            isShowingAnswers = !isShowingAnswers;

            int visibility;
            if (isShowingAnswers) {
                visibility = View.VISIBLE; }
            else {
                visibility = View.INVISIBLE; }

            wrongAnswer1.setVisibility(visibility);
            wrongAnswer2.setVisibility(visibility);
            correctAnswer.setVisibility(visibility);
        });

        addButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AddCard.class);
            Toast.makeText(requireContext(), "hello!", Toast.LENGTH_SHORT).show();
        });

        editButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AddCard.class);
            Toast.makeText(requireContext(), "im hungry", Toast.LENGTH_SHORT).show();
        });

        correctAnswer.setOnClickListener(v -> {
            correctAnswer.setBackgroundColor(getResources().getColor(R.color.green_color, null));
        });

        wrongAnswer1.setOnClickListener(v-> {
            wrongAnswer1.setBackgroundColor(getResources().getColor(R.color.red_color, null));
            correctAnswer.setBackgroundColor(getResources().getColor(R.color.green_color, null));
        });

        wrongAnswer2.setOnClickListener(v-> {
            wrongAnswer2.setBackgroundColor(getResources().getColor(R.color.red_color, null));
            correctAnswer.setBackgroundColor(getResources().getColor(R.color.green_color, null));
        });


        return view;
    }

    private void revealQuestion() {
        flashcardQuestion.setVisibility(View.VISIBLE);
        flashcardAnswer.setVisibility(View.INVISIBLE);
    }

    private void updateUI(int index) {
        if(allFlashcards == null || allFlashcards.isEmpty()) {
                flashcardQuestion.setText("hey add a flashcard buddy");
                flashcardAnswer.setText("press the + to add one");
                wrongAnswer1.setText("");
                wrongAnswer2.setText("");
                correctAnswer.setText("");
                return;
        }
        Flashcard card = allFlashcards.get(index);
        flashcardQuestion.setText(card.getQuestion());
        flashcardAnswer.setText(card.getAnswer());
        wrongAnswer1.setText(card.getWrongAnswer1());
        wrongAnswer2.setText(card.getWrongAnswer2());
        correctAnswer.setText(card.getAnswer());
    }
    private void resetMultipleChoice() {
        wrongAnswer1.setBackgroundColor(getResources().getColor(R.color.reset_color, null));
        wrongAnswer2.setBackgroundColor(getResources().getColor(R.color.reset_color, null));
        correctAnswer.setBackgroundColor(getResources().getColor(R.color.reset_color, null));
//
//        wrongAnswer1.setTextColor(getResources().getColor(R.color.black, null));
//        wrongAnswer2.setTextColor(getResources().getColor(R.color.black, null));
//        correctAnswer.setTextColor(getResources().getColor(R.color.black, null));
    }
}