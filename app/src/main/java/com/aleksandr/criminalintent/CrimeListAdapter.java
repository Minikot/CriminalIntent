package com.aleksandr.criminalintent;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.aleksandr.criminalintent.model.Crime;

import java.util.ArrayList;

/**
 * Created by Aleksandr on 23.02.17.
 */

public class CrimeListAdapter extends RecyclerView.Adapter<CrimeListAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Crime> crimes;

    public CrimeListAdapter(Context context, ArrayList<Crime> crimes) {
        this.context = context;
        this.crimes = crimes;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_item_crime, parent, false);
        return new ViewHolder(itemView);
    }

    //NO position final!!! USE holder.getAdapterPosition()!!!

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bindTo(context, crimes.get(holder.getAdapterPosition()));

    }

    @Override
    public int getItemCount() {
        return crimes.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        View view;
        private TextView tvTitle;
        private TextView tvData;
        private CheckBox cbSolved;


        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            cbSolved = (CheckBox) itemView.findViewById(R.id.cb_solved);
            tvData = (TextView) itemView.findViewById(R.id.tv_data);

        }

        public void bindTo(final Context context, final Crime crime){
            tvTitle.setText(crime.getTitle());
            tvData.setText(crime.getDate(CommonUtils.loadDateFormat(context)));
            cbSolved.setChecked(crime.isSolved());

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(CrimePagerActivity.newIntent(context, crime.getUuid()));
                }
            });


        }

    }
}
