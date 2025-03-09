package com.example.quizapp2;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuizFragment extends Fragment {
    private QuizViewModel quizViewModel;
    private ImageView quizImage;
    private Button btn1, btn2, btn3;
    private TextView scoreText;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.quiz_fragment, container, false);

        quizImage = view.findViewById(R.id.quizImage);
        btn1 = view.findViewById(R.id.btn1);
        btn2 = view.findViewById(R.id.btn2);
        btn3 = view.findViewById(R.id.btn3);
        scoreText = view.findViewById(R.id.scoreText);

        quizViewModel = new ViewModelProvider(requireActivity()).get(QuizViewModel.class);

        quizViewModel.getCurrentQuestion().observe(getViewLifecycleOwner(), this::updateUI);

        // ✅ Ensure questions exist before trying to load one
        quizViewModel.getAllImages().observe(getViewLifecycleOwner(), images -> {
            if (images != null && images.size() >= 3) {

                quizViewModel.generateQuestions();
                quizViewModel.loadNewQuestion(); // ✅ Only called when images exist
            }
        });

        return view;
    }

    private void updateUI(Question question) {
        if (question == null) {
            return;
        }
        quizImage.setImageURI(Uri.parse(question.getImageUri()));

        List<String> answers = new ArrayList<>(); // ✅ Modifiable list
        answers.add(question.getCorrectAnswer());
        answers.add(question.getWrongAnswer1());
        answers.add(question.getWrongAnswer2());

        Collections.shuffle(answers); // ✅ Now it works!
        btn1.setText(answers.get(0));
        btn2.setText(answers.get(1));
        btn3.setText(answers.get(2));

        btn1.setOnClickListener(v -> checkAnswer(btn1.getText().toString()));
        btn2.setOnClickListener(v -> checkAnswer(btn2.getText().toString()));
        btn3.setOnClickListener(v -> checkAnswer(btn3.getText().toString()));

        scoreText.setText("Score: " + quizViewModel.getScore());
    }

    private void checkAnswer(String selectedAnswer) {
        boolean correct = quizViewModel.checkAnswer(selectedAnswer);
        if (correct) {
            scoreText.setText("✅ Correct! Score: " + quizViewModel.getScore());
            quizViewModel.loadNewQuestion();
        } else {
            scoreText.setText("❌ Wrong! Score: " + quizViewModel.getScore());
        }
    }
}
