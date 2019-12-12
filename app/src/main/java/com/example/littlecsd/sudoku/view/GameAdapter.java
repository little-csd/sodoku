package com.example.littlecsd.sudoku.view;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.littlecsd.sudoku.R;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * The adapter and controller of sudoku
 * contains all info of this game
 * provide collision checker, highlight function and so on.
 */
public class GameAdapter extends RecyclerView.Adapter<GameAdapter.GameHolder> {

    private static final String TAG = "GameAdapter";
    private static final int MAXN = 511;
    private GameAdapter.GameHolder clicked;
    private List<Point> data;
    private int[][] map = new int[9][9];
    private boolean[] canChange = new boolean[81];
    private int[] row = new int[9];
    private int[] col = new int[9];
    private int[] block = new int[9];
    private int curNum = 0;
    private boolean canInput = true;
    private Resources resources;

    public GameAdapter(Context context, List<Point> data) {
        this.data = data;
        resources = context.getResources();
    }

    @NonNull
    @Override
    public GameHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.block_item, parent, false);
        GameHolder holder = new GameHolder(view);
        view.setOnClickListener(v -> {
            if (holder == clicked) {
                clicked = null;
                view.setBackgroundColor(resources.getColor(obtainColor(holder.getAdapterPosition())));
            } else {
                if (clicked != null) {
                    clicked.itemView.setBackgroundColor(
                            resources.getColor(obtainColor(clicked.getAdapterPosition())));
                }
                clicked = holder;
                view.setBackgroundColor(resources.getColor(R.color.clicked));
            }
        });
        return holder;

    }

    @Override
    public void onBindViewHolder(@NonNull GameHolder holder, int position) {
        Point point = data.get(position);
        holder.setValue(point.getValue());
        holder.itemView.setBackgroundColor(resources.getColor(obtainColor(position)));
    }

    public void click(int number) {
        if (clicked == null || number == 0 || !canInput) {
            return;
        }
        int position = clicked.getAdapterPosition();
        if (!canChange[position]) return;

        int x = position / 9;
        int y = position % 9;
        if (!canSet(x, y, number)) {
            int lastNum = map[x][y];
            Timer timer = new Timer();
            GameHolder curHolder = clicked;
            canInput = false;
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    if (curHolder != null) curHolder.setValue(lastNum);
                    canInput = true;
                }
            };
            timer.schedule(task, 1000);
        } else {
            setNum(x, y, number);
        }
        clicked.setValue(number);
    }

    public int[][] getMap() {
        return map;
    }

    public void setMap(int[][] map) {
        curNum = 0;
        this.map = map;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                int pos = i * 9 + j;
                if (map[i][j] > 0) {
                    canChange[pos] = false;
                    curNum++;
                } else {
                    canChange[pos] = true;
                }
                int t = i / 3 * 3 + j / 3;
                int xx = 1 << (map[i][j] - 1);
                row[i] |= xx;
                col[j] |= xx;
                block[t] |= xx;
            }
        }
    }

    private boolean canSet(int x, int y, int num) {
        int pos = x / 3 * 3 + y / 3;
        int value = 1 << (num - 1);
        return !((value & row[x]) > 0 || (value & col[y]) > 0 || (value & block[pos]) > 0);
    }

    private void setNum(int x, int y, int num) {
        int lastNum = map[x][y];
        map[x][y] = num;
        int pos = x / 3 * 3 + y / 3;
        int xx = 1 << (num - 1);
        if (lastNum > 0) {
            int yy = 1 << (lastNum - 1);
            row[x] ^= yy;
            col[y] ^= yy;
            block[pos] ^= yy;
        } else {
            curNum++;
        }
        row[x] |= xx;
        col[y] |= xx;
        block[pos] |= xx;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    private int obtainColor(int position) {
        int x = position / 9;
        int y = position % 9;
        int block = x / 3 * 3 + y / 3;
        if ((block & 1) > 0) {
            return R.color.color1;
        } else {
            return R.color.color2;
        }
    }

    public boolean isFull() {
        return curNum == 81;
    }

    class GameHolder extends RecyclerView.ViewHolder {

        private TextView textView;
        private int value;

        public GameHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.block_text);
        }

        public void setValue(int value) {
            this.value = value;
            textView.setText(value > 0 ? String.valueOf(value) : "");
        }
    }
}
