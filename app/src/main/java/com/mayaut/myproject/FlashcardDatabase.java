package com.mayaut.myproject;

import android.content.Context;
import androidx.room.Room;
import java.util.List;

public class FlashcardDatabase {
    private final AppDatabase database;

    FlashcardDatabase(Context context) {
        database = Room.databaseBuilder(context.getApplicationContext(),
                        AppDatabase.class, "flashcard-database").allowMainThreadQueries().build();
    }
    public void initFirstCard() {
        if (database.flashcardDao().getAll().isEmpty()) {
            insertCard(new Flashcard("Who is the 44th President of the United States", "Barack Obama"));
        }
    }
    public void updateCard(Flashcard flashcard) {
        database.flashcardDao().update(flashcard);
    }
    public void insertCard(Flashcard flashcard) {
        database.flashcardDao().insertAll(flashcard);
    }
    public List<Flashcard> getAllCards() {
        return database.flashcardDao().getAll();
    }
    public void deleteCard(String flashcardQuestion) {
        List<Flashcard> allCards = database.flashcardDao().getAll();
        for (Flashcard f : allCards) {
            if (f.getQuestion().equals(flashcardQuestion)) {
                database.flashcardDao().delete(f);
            }
        }
    }
    public void deleteAll() {
        for (Flashcard f : database.flashcardDao().getAll()) {
            database.flashcardDao().delete(f);
        }
    }
}
