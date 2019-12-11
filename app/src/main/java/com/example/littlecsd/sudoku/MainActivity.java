package com.example.littlecsd.sudoku;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.littlecsd.sudoku.process.Generator;
import com.example.littlecsd.sudoku.solve.DLX;
import com.example.littlecsd.sudoku.view.Callback;
import com.example.littlecsd.sudoku.view.Divider;
import com.example.littlecsd.sudoku.view.GameAdapter;
import com.example.littlecsd.sudoku.view.NumberAdapter;
import com.example.littlecsd.sudoku.view.Point;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements Callback {

    private static final String TAG = "MainActivity";
    private Generator generator = new Generator();
    private DLX solver = new DLX();
    private NumberAdapter numberAdapter;
    private GameAdapter gameAdapter;
    private Timer timer;
    private TextView textView;
    private long startTime;
    private int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        count = intent.getIntExtra("num", 20);
        init();
    }

    private void init() {
        RecyclerView gameView = findViewById(R.id.game);
        RecyclerView numberView = findViewById(R.id.keyboard);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 9) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        gameView.setLayoutManager(gridLayoutManager);
        numberView.setLayoutManager(new GridLayoutManager(this, 3));

        List<Point> data = new ArrayList<>();
        int[][] map;
        while (true) {
            map = generator.generate(count);
            if (solver.solve(map, false) != null) {
                break;
            }
        }
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                Point point = new Point(i, j, map[i][j]);
                data.add(point);
            }
        }

        gameAdapter = new GameAdapter(this, data);
        gameAdapter.setMap(map);
        gameView.setAdapter(gameAdapter);
        gameView.addItemDecoration(new Divider(this));
        numberAdapter = new NumberAdapter();
        numberView.setAdapter(numberAdapter);
        numberAdapter.setCallback(this);

        textView = findViewById(R.id.time);
        startTime = System.currentTimeMillis();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                long curTime = System.currentTimeMillis();
                long delta = curTime - startTime;
                textView.setText(timeToString(delta / 1000));
            }
        };
        timer = new Timer();
        timer.schedule(task, 0, 100L);
    }

    @Override
    public void onClick(int number) {
        gameAdapter.click(number);
        print(gameAdapter.getMap());
    }

    private void print(int[][] map) {
        for (int i = 0; i < map.length; i++) {
            Log.i(TAG, Arrays.toString(map[i]));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timer.cancel();
    }

    private String timeToString(long time) {
        long hour = time / 3600;
        long minute = (time % 3600) / 60;
        long second = time % 60;
        String sh = hour < 10 ? "0" + hour : String.valueOf(hour);
        String sm = minute < 10 ? "0" + minute : String.valueOf(minute);
        String ss = second < 10 ? "0" + second : String.valueOf(second);
        return sh + ":" + sm + ":" + ss;
    }
}
