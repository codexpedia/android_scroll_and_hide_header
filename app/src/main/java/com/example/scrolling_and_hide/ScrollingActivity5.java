package com.example.scrolling_and_hide;

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

// https://github.com/mzgreen/HideOnScrollExample
public class ScrollingActivity5 extends AppCompatActivity {

    RecyclerView rvNumbers;
    LinearLayout llHeader;
    int headerHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling5);


        rvNumbers = (RecyclerView) findViewById(R.id.rv_numbers);
        llHeader = (LinearLayout) findViewById(R.id.ll_header_container);


        llHeader.post(new Runnable() {
            @Override
            public void run() {
                headerHeight = llHeader.getMeasuredHeight();
                initListView();
            }
        });







    }

    private void initListView() {
        String [] numbers = {"One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten", "Eleven", "Twelve", "Thirteen", "Fifteen", "Sixteen", "Seventeen", "Eighteen", "Nineteen"};
        rvNumbers.setPadding(rvNumbers.getPaddingLeft(), headerHeight, rvNumbers.getPaddingRight(), rvNumbers.getPaddingBottom());
        rvNumbers.setLayoutManager(new LinearLayoutManager(this));
        RecyclerViewAdapter recyclerAdapter = new RecyclerViewAdapter(numbers);
        rvNumbers.setAdapter(recyclerAdapter);
        rvNumbers.addOnScrollListener(new HidingScrollListener(headerHeight) {

            @Override
            public void onMoved(int distance) {
                llHeader.setTranslationY(-distance);
            }

            @Override
            public void onShow() {
                llHeader.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
            }

            @Override
            public void onHide() {
                llHeader.animate().translationY(-headerHeight).setInterpolator(new AccelerateInterpolator(2)).start();
            }

        });
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



    public abstract class HidingScrollListener extends RecyclerView.OnScrollListener {

        private static final float HIDE_THRESHOLD = 60;
        private static final float SHOW_THRESHOLD = 200;

        private int mToolbarOffset = 0;
        private boolean mControlsVisible = true;
        private int mHeaderHeight;
        private int mTotalScrolledDistance;

        public HidingScrollListener(int headerHeight) {
            mHeaderHeight = headerHeight;
        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);

            if(newState == RecyclerView.SCROLL_STATE_IDLE) {
                if(mTotalScrolledDistance < mHeaderHeight) {
                    setVisible();
                } else {
                    if (mControlsVisible) {
                        if (mToolbarOffset > HIDE_THRESHOLD) {
                            setInvisible();
                        } else {
                            setVisible();
                        }
                    } else {
                        if ((mHeaderHeight - mToolbarOffset) > SHOW_THRESHOLD) {
                            setVisible();
                        } else {
                            setInvisible();
                        }
                    }
                }
            }

        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            clipToolbarOffset();
            onMoved(mToolbarOffset);

            if((mToolbarOffset < mHeaderHeight && dy>0) || (mToolbarOffset >0 && dy<0)) {
                mToolbarOffset += dy;
            }
            if (mTotalScrolledDistance < 0) {
                mTotalScrolledDistance = 0;
            } else {
                mTotalScrolledDistance += dy;
            }
        }

        private void clipToolbarOffset() {
            if(mToolbarOffset > mHeaderHeight) {
                mToolbarOffset = mHeaderHeight;
            } else if(mToolbarOffset < 0) {
                mToolbarOffset = 0;
            }
        }

        private void setVisible() {
            if(mToolbarOffset > 0) {
                onShow();
                mToolbarOffset = 0;
            }
            mControlsVisible = true;
        }

        private void setInvisible() {
            if(mToolbarOffset < mHeaderHeight) {
                onHide();
                mToolbarOffset = mHeaderHeight;
            }
            mControlsVisible = false;
        }

        public abstract void onMoved(int distance);
        public abstract void onShow();
        public abstract void onHide();
    }




}
