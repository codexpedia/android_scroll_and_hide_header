package com.example.scrolling_and_hide;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * hide and show header using CoordinatorLayout AppBarLayout and RecyclerView
 */
public class ScrollingActivity4Fragment extends Fragment {
    RecyclerView rvNumbers;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_scrolling4_fragment, container, false);

        rvNumbers = (RecyclerView) view.findViewById(R.id.rv_numbers);

        String [] numbers = {"One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine"};

        ItemArrayAdapter itemArrayAdapter = new ItemArrayAdapter(R.layout.list_item, numbers);
        rvNumbers.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvNumbers.setItemAnimator(new DefaultItemAnimator());
        rvNumbers.setAdapter(itemArrayAdapter);

        return view;
    }

    public class ItemArrayAdapter extends RecyclerView.Adapter<ItemArrayAdapter.ViewHolder> {

        private int listItemLayout;
        private String[] itemList;
        public ItemArrayAdapter(int layoutId, String[] itemList) {
            listItemLayout = layoutId;
            this.itemList = itemList;
        }

        @Override
        public int getItemCount() {
            return itemList == null ? 0 : itemList.length;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(listItemLayout, parent, false);
            ViewHolder myViewHolder = new ViewHolder(view);
            return myViewHolder;
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int listPosition) {
            TextView item = holder.item;
            item.setText(itemList[listPosition]);
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            public TextView item;
            public ViewHolder(View itemView) {
                super(itemView);
                item = (TextView) itemView.findViewById(R.id.tv_number);
            }
        }
    }


}