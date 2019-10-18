package com.pof.gitcommitterinfo.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pof.gitcommitterinfo.R;
import com.pof.gitcommitterinfo.data.model.GitCommit;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class GitCommitListAdapter extends RecyclerView.Adapter<GitCommitListAdapter.ViewHolder>{

    private List<GitCommit> listGitCommits = new ArrayList<GitCommit>();
    private Context context;
    private ItemClickListener itemClickListener;
    private boolean isDetailViewOpened;
    private View itemSelected;

    public GitCommitListAdapter(Context context, ItemClickListener itemClickListener) {
        this.context = context;
        this.itemClickListener = itemClickListener;
    }


    public void updateGitCommitList(List<GitCommit> gitCommits) {
        listGitCommits = gitCommits;
        notifyDataSetChanged();
    }

    public void resetListView() {
        this.isDetailViewOpened = false;
        this.itemSelected.setBackgroundColor(context.getResources().getColor(R.color.colorWhile));
    }


    @NonNull
    @Override
    public GitCommitListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewtype) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View listItem= layoutInflater.inflate(R.layout.git_commit_row, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        Log.d("GitCommitListAdapter", "Viewholder created");
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull GitCommitListAdapter.ViewHolder viewHolder, int position) {
        final GitCommit gitCommit = listGitCommits.get(position);
        Picasso.with(context)
                .load(gitCommit.getCommitter().getAvatar_url())
                .fit().centerCrop()
                .error(R.drawable.defaultimage)
                .into(viewHolder.imageView);
        viewHolder.message.setText(gitCommit.getCommit().getMessage());
        String [] date_login = {gitCommit.getCommit().getCommitter().getDate(),gitCommit.getCommitter().getLogin()};
        //Switching within alternate row
        if(position%2 != 0) {
            date_login[1]  = gitCommit.getCommit().getCommitter().getDate();
        }
        viewHolder.date_loginname.setTextsToSwitch(date_login);
    }

    @Override
    public int getItemCount() {
        return listGitCommits.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;
        private TextView message;
        private CustomTextSwitcher date_loginname;
        private RelativeLayout relativeLayout;

        private ViewHolder(@NonNull final View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView);
            message = itemView.findViewById(R.id.message);
            date_loginname = itemView.findViewById(R.id.date_loginname);
            relativeLayout = itemView.findViewById(R.id.relativeLayout);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(itemClickListener!=null && !isDetailViewOpened) {
                        itemClickListener.onListItemClick(itemView, listGitCommits.get(getAdapterPosition()));
                        relativeLayout.setBackgroundResource(R.color.colorGreen);
                        isDetailViewOpened = true;
                        itemSelected = itemView;
                    }
                }
            });
        }
    }

}
