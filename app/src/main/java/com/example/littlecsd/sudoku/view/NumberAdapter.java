package com.example.littlecsd.sudoku.view;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.littlecsd.sudoku.R;

import static android.content.ContentValues.TAG;

public class NumberAdapter extends RecyclerView.Adapter<NumberAdapter.NumberHolder> {

    private int clickNum = 0;
    private Callback callback;

    @SuppressLint("ClickableViewAccessibility")
    @NonNull
    @Override
    public NumberHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.number_item, parent, false);
        NumberHolder holder = new NumberHolder(view);
        view.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_UP:
                    if (callback != null && clickNum != 0) {
                        callback.onClick(clickNum);
                    }
                    clickNum = 0;
                    holder.textView.setBackgroundColor(view.getResources().getColor(R.color.noClickNumber));
                    break;
                case MotionEvent.ACTION_DOWN:
                    clickNum = holder.value;
                    holder.textView.setBackgroundColor(view.getResources().getColor(R.color.clicked));
                    break;
                case MotionEvent.ACTION_CANCEL:
                    clickNum = 0;
                    holder.textView.setBackgroundColor(view.getResources().getColor(R.color.noClickNumber));
                    break;
            }
            return true;
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull NumberHolder holder, int position) {
        holder.setValue(position + 1);
    }

    @Override
    public int getItemCount() {
        return 9;
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    class NumberHolder extends RecyclerView.ViewHolder {

        private TextView textView;
        private int value;

        public NumberHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.number_text);
        }

        public void setValue(int value) {
            this.value = value;
            textView.setText(String.valueOf(value));
        }
    }
}
