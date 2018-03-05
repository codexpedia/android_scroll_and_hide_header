package com.example.scrolling_and_hide;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

/**
 * hacking trick
 * Put ListView and header in a FrameLayout
 * Add an empty header to the ListView list
 * Put the header you want to hide and show on top of the ListView to cover the empty header
 * Listen to the addOnScrollListener on the ListView, then hide or show the header accordingly
 */
public class ScrollingActivity3 extends AppCompatActivity {

    LinearLayout llHeader;
    ListView rvNumbers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling3);

        llHeader = (LinearLayout) findViewById(R.id.ll_header);
        rvNumbers = (ListView) findViewById(R.id.rv_numbers);

        String [] numbers = {"One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine"};

        ItemArrayAdapter itemArrayAdapter = new ItemArrayAdapter(this, R.layout.list_item, numbers);
        rvNumbers.setAdapter(itemArrayAdapter);

        rvNumbers.setOnScrollListener(new OnScrollPositionChangedListener() {
            @Override
            public void onScrollPositionChanged(int scrollYPosition) {
                Log.d("scroll position", "y position changes: " + scrollYPosition);
            }

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

    public class ItemArrayAdapter extends ArrayAdapter<String> {
        private static final int VIEW_TYPE_COUNT = 2;
        private static final int HEADER_ITEM = 0;
        private static final int LIST_ITEM = 1;


        String[] itemList;

        private int listItemLayout;
        public ItemArrayAdapter(Context context, int layoutId, String[] itemList) {
            super(context, layoutId, itemList);
            listItemLayout = layoutId;
            this.itemList = itemList;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (getItemViewType(position) == HEADER_ITEM) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.empty_header, parent, false);
            } else {
                int pos = position - 1; // header trick
                String item = getItem(pos);

                // Check if an existing view is being reused, otherwise inflate the view
                ViewHolder viewHolder;
                if (convertView == null) {
                    viewHolder = new ViewHolder();
                    LayoutInflater inflater = LayoutInflater.from(getContext());
                    convertView = inflater.inflate(listItemLayout, parent, false);
                    viewHolder.item = (TextView) convertView.findViewById(R.id.tv_number);
                    convertView.setTag(viewHolder); // view lookup cache stored in tag
                } else {
                    viewHolder = (ViewHolder) convertView.getTag();
                }

                // Populate the data into the template view using the data object
                viewHolder.item.setText(item);
            }

            // Return the completed view to render on screen
            return convertView;
        }

        @Override
        public int getViewTypeCount() {
            return VIEW_TYPE_COUNT;
        }

        @Override
        public int getItemViewType(int position) {
            if (position == 0) {
                return HEADER_ITEM;
            } else {
                return LIST_ITEM;
            }
        }

        @Override
        public int getCount() {
            return getBasicItemCount() + 1; // header trick
        }

        public int getBasicItemCount() {
            return itemList == null ? 0 : itemList.length;
        }


        // The ViewHolder, only one item for simplicity and demonstration purposes, you can put all the views inside a row of the list into this ViewHolder
         class ViewHolder {
            TextView item;
        }
    }



    public abstract class OnScrollPositionChangedListener implements AbsListView.OnScrollListener {
        int scrollAmount;
        int prevIndex;
        int prevViewPos;
        int prevViewHeight;
        @Override
        public void onScroll(AbsListView v, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            try {
                View currView = v.getChildAt(0);
                int currViewPos = Math.round(currView.getTop());
                int diffViewPos = prevViewPos - currViewPos;
                int currViewHeight = currView.getHeight();
                scrollAmount += diffViewPos;
                if (firstVisibleItem > prevIndex) {
                    scrollAmount += prevViewHeight;
                    onHide();
                } else if (firstVisibleItem < prevIndex) {
                    scrollAmount -= currViewHeight;
                    onShow();
                }
                prevIndex = firstVisibleItem;
                prevViewPos = currViewPos;
                prevViewHeight = currViewHeight;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                onScrollPositionChanged(scrollAmount);
            }
        }
        @Override public void onScrollStateChanged(AbsListView absListView, int i) {}
        public abstract void onScrollPositionChanged(int scrollYPosition);
        public abstract void onHide();
        public abstract void onShow();
    }

}
