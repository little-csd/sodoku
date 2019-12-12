package com.example.littlecsd.sudoku;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

/**
 * The Entry of program.
 */
public class EntryActivity extends AppCompatActivity {

    private int minCount;
    private int maxCount;
    private boolean clickable = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);

        Button btnEasy = findViewById(R.id.button_easy);
        Button btnNormal = findViewById(R.id.button_normal);
        Button btnHard = findViewById(R.id.button_hard);
        btnEasy.setOnClickListener(v -> {
            minCount = 32;
            maxCount = 40;
            start();
        });
        btnNormal.setOnClickListener(v -> {
            minCount = 25;
            maxCount = 32;
            start();
        });
        btnHard.setOnClickListener(v -> {
            minCount = 15;
            maxCount = 25;
            start();
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        clickable = true;
    }

    private void start() {
        if (!clickable) return;
        clickable = false;
        int delta = maxCount - minCount;
        int num = (int) (Math.random() * delta) + minCount;
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("num", num);
        // start game
        startActivity(intent);
    }
}
