package be.trewep.lettercijferapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private MutableLiveData<Integer> number = new MutableLiveData<Integer>();


    Timer t = new Timer();
    private static int PERIOD = 1000;
    //lettersapplication copy
    TextView tvLetter;
    LetterViewModel viewModel;

    LettersFragment lettersFragment;
    NumbersFragment numbersFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        number.setValue(0);

        GridLayout cardGridLayout = findViewById(R.id.gridlayout);

        for (int i = 1; i<=6; i++) {
            View cardView = getLayoutInflater().inflate(R.layout.number_card, cardGridLayout, false);
            TextView tv = cardView.findViewById(R.id.number_card_text);
            tv.setText(Integer.toString(i));
            cardGridLayout.addView(cardView);
            final int j = i;

            number.observe(this, number -> {
                tv.setText(Integer.toString(number * j));
            });
        }

        ProgressBar pb = findViewById(R.id.progress_bar);
        number.observe(this , number -> {
            pb.setProgress(number);
        });
        //lettersapplication copy

        viewModel = new ViewModelProvider(this).get(LetterViewModel.class);
        lettersFragment = new LettersFragment();
        numbersFragment = new NumbersFragment();

        tvLetter = findViewById(R.id.tv_letter);

        viewModel.getLetters().observe(this, character ->
                tvLetter.setText(character.toString()));

        viewModel.getNumbers().observe(this, number ->
                tvLetter.setText(number.toString()));

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.layout_a, lettersFragment)
                .commit();
    }

    public void add(View w)  {
        number.setValue(number.getValue() + 1);
    }

    public void startTimer(View w)  {
        t.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                number.postValue(number.getValue() + 1);
            }
        }, 2000, PERIOD);
    }


    //lettersapplication copy
    public void toLetters(View v) {
        viewModel.nextRound();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.layout_a, lettersFragment)
                .commit();
    }

    public void toNumbers(View v) {
        viewModel.clearNumbers();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.layout_a, numbersFragment)
                .commit();
    }
}