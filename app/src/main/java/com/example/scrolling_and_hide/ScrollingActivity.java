package com.example.scrolling_and_hide;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ScrollingActivity extends AppCompatActivity {

    RecyclerView rvNumbers;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);

        String [] numbers = {"CoordinatorLayout RecyclerView", "CoordinatorLayout RecyclerView Header Trick", "CoordinatorLayout ListView Header Trick", "DrawerLayout CoordinatorLayout RecyclerView", "CustomScrollAndHideView", "Six", "Seven", "Eight", "Nine", "Ten", "Eleven", "Twelve", "Thirteen", "Fifteen", "Sixteen", "Seventeen", "Eighteen", "Nineteen"};

        rvNumbers = (RecyclerView) findViewById(R.id.rv_numbers);
        rvNumbers.setLayoutManager(new LinearLayoutManager(this));
        rvNumbers.setAdapter(new RecyclerViewAdapter(numbers));
    }

    class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
        String listData[];

        public RecyclerViewAdapter(String data[]){
            this.listData = data;
        }

        @Override
        public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
            RecyclerViewAdapter.ViewHolder vh = new RecyclerViewAdapter.ViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(RecyclerViewAdapter.ViewHolder holder, final int position) {
            holder.tvNumber.setText(listData[position]);
            holder.tvNumber.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (position == 1) {
                        startActivity(new Intent(ScrollingActivity.this, ScrollingActivity2.class));
                    } else if (position == 2) {
                        startActivity(new Intent(ScrollingActivity.this, ScrollingActivity3.class));
                    } else if (position == 3) {
                        startActivity(new Intent(ScrollingActivity.this, ScrollingActivity4.class));
                    } else if (position == 4) {
                        startActivity(new Intent(ScrollingActivity.this, ScrollingActivity5.class));
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return listData.length;
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            public TextView tvNumber;
            public ViewHolder(View v) {
                super(v);
                tvNumber = (TextView) v.findViewById(R.id.tv_number);
            }
        }
    }
}
