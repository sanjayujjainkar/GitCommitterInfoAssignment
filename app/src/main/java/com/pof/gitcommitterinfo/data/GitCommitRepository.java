package com.pof.gitcommitterinfo.data;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import com.pof.gitcommitterinfo.data.model.CommitterInfo;
import com.pof.gitcommitterinfo.data.model.GitCommit;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GitCommitRepository {

    private static GitCommitRepository gitCommitRepository;
    private RetrofitService retrofitService;
    private CommitterInfo committerInfo;
    private String TAG = GitCommitRepository.class.getSimpleName();

    private GitCommitRepository(){

        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .build();

        //initialise retrofit and instantiate RetrofitService
        Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(RetrofitService.GITURL)
                            .client(okHttpClient)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();


        retrofitService = retrofit.create(RetrofitService.class);
    }

    public RetrofitService getRetrofitService() {
        return retrofitService;
    }

    //Singleton instance
    public synchronized static GitCommitRepository getInstance() {
        if(gitCommitRepository == null) {
            gitCommitRepository = new GitCommitRepository();
        }
        return gitCommitRepository;
    }


    public LiveData<List<GitCommit>> getGitCommits() {
        final MutableLiveData<List<GitCommit>> liveData = new MutableLiveData<>();

        retrofitService.getGitCommits().enqueue(new Callback<List<GitCommit>>() {
            @Override
            public void onResponse(Call<List<GitCommit>> call, Response<List<GitCommit>> response) {
                liveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<List<GitCommit>> call, Throwable t) {
                Log.d(TAG, t.getStackTrace().toString());
                liveData.setValue(null);
            }
        });
        return liveData;
    }

}
