package com.mobitrack.mobi.ui.main.payment;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mobitrack.mobi.R;
import com.mobitrack.mobi.model.Step;

import java.util.ArrayList;
import java.util.List;

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.StepHolder> {


    private List<Step> stepList;
    private Context context;
    private LayoutInflater inflater;

    public StepAdapter(Context context) {
        this.context = context;
        this.stepList = new ArrayList<>();
        this.inflater = LayoutInflater.from(context);

        initList();
    }

    private void initList(){
        String[] titleArr = context.getResources().getStringArray(R.array.title_list);
        String[] contentArr = context.getResources().getStringArray(R.array.content_list);

        for (int i = 0;i<titleArr.length;i++){
            stepList.add(new Step(titleArr[i],contentArr[i]));
        }
    }

    @NonNull
    @Override
    public StepHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_step,parent,false);
        return new StepHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StepHolder holder, int position) {
        Step step = stepList.get(position);

        holder.tvTitle.setText(step.getTitle());
        holder.tvContent.setText(step.getContent());
    }

    @Override
    public int getItemCount() {
        return stepList.size();
    }

    class StepHolder extends RecyclerView.ViewHolder{

        TextView tvTitle,tvContent;

        public StepHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.title);
            tvContent = itemView.findViewById(R.id.content);
        }
    }
}
