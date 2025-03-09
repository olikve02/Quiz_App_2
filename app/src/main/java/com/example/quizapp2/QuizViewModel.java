package com.example.quizapp2;

import android.app.Application;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class QuizViewModel extends AndroidViewModel {
    private final AppDatabase db;
    private final LiveData<List<Image>> allImages; // Fetch stored images
    private final MutableLiveData<Question> currentQuestion = new MutableLiveData<>();
    private MutableLiveData<Integer> score = new MutableLiveData<>(0);
    public List<Question> generatedQuestions = new ArrayList<>(); // Store dynamically created questions

    public QuizViewModel(@NonNull Application application) {
        super(application);
        db = AppDatabase.getDatabase(application);
        allImages = db.imageDao().getAllImages();

        generateQuestions(); // Create questions from images
    }

    public LiveData<List<Image>> getAllImages() {
        return allImages;
    }

    public LiveData<Question> getCurrentQuestion() {
        return currentQuestion;
    }

    public void loadNewQuestion() {
        if (generatedQuestions.isEmpty()) {
            Log.e("QuizViewModel", "❌ No questions available! Cannot load a new question.");
            return; // ✅ Stops the crash if no questions exist
        }

        Collections.shuffle(generatedQuestions);
        Question nextQuestion = generatedQuestions.get(0);

        if (nextQuestion == null) { // ✅ Ensure we don't crash on null
            Log.e("QuizViewModel", "❌ Selected question is null! Aborting.");
            return;
        }

        Log.d("QuizViewModel", "🔄 Loading new question: " + nextQuestion.getCorrectAnswer());
        currentQuestion.postValue(nextQuestion);
    }

    public boolean checkAnswer(String answer) {
        boolean correct = answer.equals(currentQuestion.getValue().getCorrectAnswer());
        if (correct) {
            score.setValue(score.getValue() + 1);
        }
        return correct;
    }

    public int getScore() {
        return score.getValue();
    }

    public void generateQuestions() {
        allImages.observeForever(images -> {
            if (images.size() < 3) {
                Log.e("QuizViewModel", "❌ Not enough images to create questions!");
                return;
            }

            List<Question> questions = new ArrayList<>();
            Random random = new Random();

            for (Image image : images) {
                String correctAnswer = image.getName();

                // Pick two random wrong answers (ensuring they're not the same as the correct answer)
                List<String> wrongAnswers = new ArrayList<>();
                while (wrongAnswers.size() < 2) {
                    Image randomImage = images.get(random.nextInt(images.size()));
                    if (!randomImage.getName().equals(correctAnswer) && !wrongAnswers.contains(randomImage.getName())) {
                        wrongAnswers.add(randomImage.getName());
                    }
                }

                Question question = new Question(image.getUriString(), correctAnswer, wrongAnswers.get(0), wrongAnswers.get(1));
                questions.add(question);
            }

            generatedQuestions = questions;
            Log.d("QuizViewModel", "✅ Generated " + generatedQuestions.size() + " questions.");
        });
    }
}
