package com.pof.gitcommitterinfo.controller;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.pof.gitcommitterinfo.data.GitCommitRepository;
import com.pof.gitcommitterinfo.data.model.GitCommit;

import java.util.List;

public class GitCommitViewModel extends AndroidViewModel {

    private final LiveData<List<GitCommit>> gitCommitsLiveData;

    public GitCommitViewModel(@NonNull Application application) {
        super(application);
        gitCommitsLiveData = GitCommitRepository.getInstance().getGitCommits();
    }

    public LiveData<List<GitCommit>> getGitCommitsLiveData() {
        return gitCommitsLiveData;
    }
}
