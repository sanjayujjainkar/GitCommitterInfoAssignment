package com.pof.gitcommitterinfo.data;

import com.pof.gitcommitterinfo.data.model.CommitterInfo;
import com.pof.gitcommitterinfo.data.model.GitCommit;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface RetrofitService {

    String GITURL = "https://api.github.com/";

    @GET("repos/definitelytyped/definitelytyped/commits")
    Call<List<GitCommit>> getGitCommits();

    @GET
    Call<CommitterInfo> getCommitterDetail(@Url String url);
}
