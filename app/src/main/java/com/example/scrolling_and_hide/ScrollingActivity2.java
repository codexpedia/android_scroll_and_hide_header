package com.example.scrolling_and_hide;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * hacking trick
 * Put RecyclerView and header in a FrameLayout
 * Add an empty header to the RecyclerView list
 * Put the header you want to hide and show on top of the RecyclerView to cover the empty header
 * Listen to the addOnScrollListener on the RecyclerView, then hide or show the header accordingly
 *
 * Reference: https://github.com/mzgreen/HideOnScrollExample
 */
public class ScrollingActivity2 extends AppCompatActivity {

    LinearLayout llHeader;
    RecyclerView rvNumbers;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling2);

        llHeader = (LinearLayout) findViewById(R.id.ll_header);
        rvNumbers = (RecyclerView) findViewById(R.id.rv_numbers);

        String [] numbers = {"One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine"};
        rvNumbers.setLayoutManager(new LinearLayoutManager(this));
        RecyclerViewAdapter recyclerAdapter = new RecyclerViewAdapter(numbers);
        rvNumbers.setAdapter(recyclerAdapter);

        rvNumbers.addOnScrollListener(new HidingScrollListener() {
            @Override
            public void onHide() {
                hideHeader();
            }
            @Override
            public void onShow() {
                showHeader();
            }
        });
    }

    private void hideHeader() {
        llHeader.animate().translationY(-llHeader.getHeight()).setInterpolator(new AccelerateInterpolator(2));
    }

    private void showHeader() {
        llHeader.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2));
    }

    class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
        private static final int TYPE_HEADER = 2;
        private static final int TYPE_ITEM = 1;

        String listData[];

        public RecyclerViewAdapter(String data[]){
            this.listData = data;
        }

        @Override
        public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            if (viewType == TYPE_ITEM) {
                final View view = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
                return new RecyclerViewAdapter.ViewHolder(view);
            } else if (viewType == TYPE_HEADER) {
                final View view = LayoutInflater.from(context).inflate(R.layout.empty_header, parent, false);
                return new RecyclerViewAdapter.ViewHolder(view);
            }
            throw new RuntimeException("There is no type that matches the type " + viewType + " + make sure your using types correctly");

        }

        @Override
        public void onBindViewHolder(RecyclerViewAdapter.ViewHolder holder, int position) {
            if (!isPositionHeader(position)) {
                holder.tvNumber.setText(listData[position - 1]); // header trick
            }
        }

        @Override
        public int getItemCount() {
            return getBasicItemCount() + 1; // header trick
        }

        public int getBasicItemCount() {
            return listData == null ? 0 : listData.length;
        }

        @Override
        public int getItemViewType(int position) {
            if (isPositionHeader(position)) {
                return TYPE_HEADER;
            }

            return TYPE_ITEM;
        }

        private boolean isPositionHeader(int position) {
            return position == 0;
        }


        class ViewHolder extends RecyclerView.ViewHolder {
            public TextView tvNumber;
            public ViewHolder(View v) {
                super(v);
                tvNumber = (TextView) v.findViewById(R.id.tv_number);
            }
        }
    }


     abstract class HidingScrollListener extends RecyclerView.OnScrollListener {

        private static final int HIDE_THRESHOLD = 20;

        private int mScrolledDistance = 0;
        private boolean mControlsVisible = true;


        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            int firstVisibleItem = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();

            if (firstVisibleItem == 0) {
                if(!mControlsVisible) {
                    onShow();
                    mControlsVisible = true;
                }
            } else {
                if (mScrolledDistance > HIDE_THRESHOLD && mControlsVisible) {
                    onHide();
                    mControlsVisible = false;
                    mScrolledDistance = 0;
                } else if (mScrolledDistance < -HIDE_THRESHOLD && !mControlsVisible) {
                    onShow();
                    mControlsVisible = true;
                    mScrolledDistance = 0;
                }
            }
            if((mControlsVisible && dy>0) || (!mControlsVisible && dy<0)) {
                mScrolledDistance += dy;
            }
        }

        public abstract void onHide();
        public abstract void onShow();
    }

}
