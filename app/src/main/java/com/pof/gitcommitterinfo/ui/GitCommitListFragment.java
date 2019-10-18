package com.pof.gitcommitterinfo.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.pof.gitcommitterinfo.R;
import com.pof.gitcommitterinfo.controller.GitCommitViewModel;
import com.pof.gitcommitterinfo.data.model.GitCommit;

import java.util.List;

public class GitCommitListFragment extends Fragment implements ItemClickListener {

    private GitCommitListAdapter gitCommitListAdapter;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private TextView txtErrorMsg;
    private Callback callback;
    public static String TAG = "GitCommitListFragment";
    //private View selectedRow;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_commit_list, container, false);
        gitCommitListAdapter = new GitCommitListAdapter(getActivity(),this);
        progressBar = view.findViewById(R.id.progressBar);
        txtErrorMsg = view.findViewById(R.id.txtError);
        recyclerView = view.findViewById(R.id.commit_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(gitCommitListAdapter);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final GitCommitViewModel viewModel = ViewModelProviders.of(this).get(GitCommitViewModel.class);

        viewModel.getGitCommitsLiveData().observe(this, new Observer<List<GitCommit>>() {
            @Override
            public void onChanged(@Nullable List<GitCommit> gitCommits) {
                if(gitCommits!=null) {
                    gitCommitListAdapter.updateGitCommitList(gitCommits);
                    Log.d("GitCommitListFragment", "adapter updated with new list");
                } else {
                    ErrorHandling.showToast(getActivity(),"Something went wrong...Try again later");
                    txtErrorMsg.setVisibility(View.VISIBLE);
                }
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        MainActivity mainActivity = (MainActivity)context;
        callback = (Callback)mainActivity;
    }

    @Override
    public void onListItemClick(View view, GitCommit gitCommit) {
        showDetail(gitCommit);
    }

    public void resetListView() {
        gitCommitListAdapter.resetListView();
    }

    private void showDetail(GitCommit gitCommit) {
        Bundle bundle = new Bundle();
        bundle.putString("sha", gitCommit.getSha());
        bundle.putString("message",gitCommit.getCommit().getMessage());
        bundle.putString("date",gitCommit.getCommit().getCommitter().getDate());
        bundle.putString("login",gitCommit.getCommitter().getLogin());
        bundle.putString("avtar",gitCommit.getCommitter().getAvatar_url());
        bundle.putString("html_url",gitCommit.getCommitter().getHtml_url());
        bundle.putString("url",gitCommit.getCommitter().getUrl());

        callback.call(bundle);

    }

    public interface Callback {
        void call(Bundle bundle);
    }

}
