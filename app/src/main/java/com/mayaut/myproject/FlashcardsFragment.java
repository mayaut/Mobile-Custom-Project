package com.mayaut.myproject;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Intent;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FlashcardsFragment extends Fragment {
    FlashcardDatabase flashcardDatabase;
    List<Flashcard> allFlashcards;
    int cardIndex = 0;
    TextView flashcardQuestion, flashcardAnswer;
    TextView wrongAnswer1, wrongAnswer2, correctAnswer;
    ImageView nextButton, prevButton, deleteButton, resetButton;
    ImageView toggle, addButton, editButton;
    boolean isShowingAnswers = false;
    ActivityResultLauncher<Intent> addOrEditCardLauncher;
    String currentCardUuid = null;

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
        flashcardDatabase.initFirstCard();
        allFlashcards = flashcardDatabase.getAllCards();
        addOrEditCardLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() != Activity.RESULT_OK || result.getData() == null) {
                        return;
                    }
                    Intent data = result.getData();
                    String question = data.getStringExtra(AddCard.QUESTION_KEY);
                    String answer = data.getStringExtra(AddCard.ANSWER_KEY);
                    String wrong1 = data.getStringExtra(AddCard.WRONG_ANSWER1_KEY);
                    String wrong2 = data.getStringExtra(AddCard.WRONG_ANSWER2_KEY);

                    if (question == null || answer == null) {
                        return;
                    }

                    boolean isEdit = currentCardUuid != null;
                    Flashcard card = new Flashcard(question, answer, wrong1, wrong2);
                    if (isEdit) {
                        card.setUuid(currentCardUuid);
                        flashcardDatabase.updateCard(card);
                    } else {
                        flashcardDatabase.insertCard(card);
                    }

                    allFlashcards = flashcardDatabase.getAllCards();
                    if (allFlashcards.isEmpty()) {
                        cardIndex = 0;
                    } else if (isEdit) {
                        int updatedIndex = findCardIndexByUuid(currentCardUuid);
                        cardIndex = updatedIndex >= 0 ? updatedIndex : 0;
                    } else {
                        cardIndex = allFlashcards.size() - 1;
                    }
                    updateUI(cardIndex);
                    resetMultipleChoice();
                    currentCardUuid = null;
                }
        );

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
                Flashcard cardToDelete = allFlashcards.get(cardIndex);
                flashcardDatabase.deleteCard(cardToDelete);
                allFlashcards = flashcardDatabase.getAllCards();
                if (!allFlashcards.isEmpty()) {
                    cardIndex = Math.min(cardIndex, allFlashcards.size() - 1);
                    updateUI(cardIndex);
                    resetMultipleChoice();
                } else {
                    updateUI(0);
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
            currentCardUuid = null;
            Intent intent = new Intent(getActivity(), AddCard.class);
            addOrEditCardLauncher.launch(intent);
        });

        editButton.setOnClickListener(v -> {
            if (allFlashcards.isEmpty()) {
                Toast.makeText(requireContext(), "No card to edit yet", Toast.LENGTH_SHORT).show();
                return;
            }
            Flashcard currentCard = allFlashcards.get(cardIndex);
            currentCardUuid = currentCard.getUuid();
            Intent intent = new Intent(getActivity(), AddCard.class);
            intent.putExtra(AddCard.QUESTION_KEY, currentCard.getQuestion());
            intent.putExtra(AddCard.ANSWER_KEY, currentCard.getAnswer());
            intent.putExtra(AddCard.WRONG_ANSWER1_KEY, currentCard.getWrongAnswer1());
            intent.putExtra(AddCard.WRONG_ANSWER2_KEY, currentCard.getWrongAnswer2());
            addOrEditCardLauncher.launch(intent);
        });

        correctAnswer.setOnClickListener(v -> handleAnswerSelection(correctAnswer));
        wrongAnswer1.setOnClickListener(v -> handleAnswerSelection(wrongAnswer1));
        wrongAnswer2.setOnClickListener(v -> handleAnswerSelection(wrongAnswer2));


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
        List<String> answers = new ArrayList<>();
        answers.add(card.getAnswer());
        answers.add(card.getWrongAnswer1() == null || card.getWrongAnswer1().trim().isEmpty() ? "Option A" : card.getWrongAnswer1());
        answers.add(card.getWrongAnswer2() == null || card.getWrongAnswer2().trim().isEmpty() ? "Option B" : card.getWrongAnswer2());
        Collections.shuffle(answers);
        wrongAnswer1.setText(answers.get(0));
        wrongAnswer2.setText(answers.get(1));
        correctAnswer.setText(answers.get(2));
        revealQuestion();
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

    private void handleAnswerSelection(TextView selectedAnswer) {
        if (allFlashcards == null || allFlashcards.isEmpty()) {
            return;
        }
        resetMultipleChoice();
        String rightAnswer = allFlashcards.get(cardIndex).getAnswer();
        boolean isCorrect = rightAnswer.equals(selectedAnswer.getText().toString());

        if (isCorrect) {
            selectedAnswer.setBackgroundColor(getResources().getColor(R.color.green_color, null));
        } else {
            selectedAnswer.setBackgroundColor(getResources().getColor(R.color.red_color, null));
            highlightCorrectAnswer(rightAnswer);
        }
    }

    private void highlightCorrectAnswer(String rightAnswer) {
        TextView[] answerViews = new TextView[]{wrongAnswer1, wrongAnswer2, correctAnswer};
        for (TextView answerView : answerViews) {
            if (rightAnswer.equals(answerView.getText().toString())) {
                answerView.setBackgroundColor(getResources().getColor(R.color.green_color, null));
                return;
            }
        }
    }

    private int findCardIndexByUuid(String uuid) {
        if (uuid == null || allFlashcards == null) {
            return -1;
        }
        for (int i = 0; i < allFlashcards.size(); i++) {
            if (uuid.equals(allFlashcards.get(i).getUuid())) {
                return i;
            }
        }
        return -1;
    }
}