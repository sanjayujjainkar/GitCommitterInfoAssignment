package com.pof.gitcommitterinfo.ui;

import android.view.View;

import com.pof.gitcommitterinfo.data.model.GitCommit;

public interface ItemClickListener {
    void onListItemClick(View view, GitCommit gitCommit);
}
