package com.pof.gitcommitterinfo.ui;

import android.annotation.SuppressLint;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pof.gitcommitterinfo.R;
import com.pof.gitcommitterinfo.data.GitCommitRepository;
import com.pof.gitcommitterinfo.data.model.CommitterInfo;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailFragment extends Fragment {

    private View view;
    private int yDelta;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_detail_layout, container, false);
        initView();
        return view;
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initView() {

        Bundle bundle = getArguments();
        String url = bundle.getString("url");
        LiveData<String> created_at = getCreatedDate(url);

        RelativeLayout relativeLayout = view.findViewById(R.id.detail_relativeLayout);
        //RelativeLayout.LayoutParams layoutParams =  (RelativeLayout.LayoutParams)relativeLayout.getLayoutParams();
        ImageView imageView = view.findViewById(R.id.detail_imageView);
        ImageView cancel = view.findViewById(R.id.detail_cancelbtn);
        TextView date = view.findViewById(R.id.detail_date);
        TextView login = view.findViewById(R.id.detail_login);
        TextView sha = view.findViewById(R.id.detail_sha);
        TextView message = view.findViewById(R.id.detail_message);
        final TextView creationdate = view.findViewById(R.id.detail_created_at);
        //this.createdat = view.findViewById(R.id.detail_created_at);

        Picasso.with(getActivity()).load(bundle.getString("avtar")).into(imageView);
        date.setText(bundle.getString("date"));
        login.setText(bundle.getString("login"));
        sha.setText(bundle.getString("sha"));
        message.setText(bundle.getString("message"));
        final String htmlurl = bundle.getString("html_url");

        created_at.observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String createdat) {
                if(!createdat.isEmpty()) {
                    creationdate.setText("Created at:"+createdat);
                } else {
                    creationdate.setText("Error fetching date...");
                }

            }
        });

        //Open html-url in bwowser on tap of image of committer.
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(htmlurl));
                getActivity().startActivity(i);
            }
        });
        //dismiss the detail fragment on tap of cancel button.
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.anim.slide_up, R.anim.slide_down)
                        .remove(DetailFragment.this)
                        .commit();
                ((MainActivity)getActivity()).onDetailFragmentDismiss();
            }
        });

        //set the layout at bottom of screen.
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams)view.getLayoutParams();
        layoutParams.topMargin = screenHeight() - layoutParams.height;
        layoutParams.rightMargin = 0;
        layoutParams.bottomMargin = 0;
        relativeLayout.setLayoutParams(layoutParams);

        //Dragging of layout
        relativeLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {

                final int y = (int) event.getRawY();

                switch (event.getAction() & MotionEvent.ACTION_MASK) {

                    case MotionEvent.ACTION_DOWN:
                        FrameLayout.LayoutParams lParams = (FrameLayout.LayoutParams)view.getLayoutParams();
                        yDelta = y - lParams.topMargin;
                        break;


                    case MotionEvent.ACTION_MOVE:
                        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams)view.getLayoutParams();
                        layoutParams.topMargin = y - yDelta;
                        layoutParams.rightMargin = 0;
                        layoutParams.bottomMargin = 0;
                        view.setLayoutParams(layoutParams);
                        break;

                }
                return true;
            }
        });

    }

    private int screenHeight() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    private LiveData<String> getCreatedDate(String url) {
        final MutableLiveData<String> liveData = new MutableLiveData<>();
        GitCommitRepository.getInstance().getRetrofitService()
                .getCommitterDetail(url)
                .enqueue(new Callback<CommitterInfo>() {
                    @Override
                    public void onResponse(Call<CommitterInfo> call, Response<CommitterInfo> response) {
                        liveData.setValue(response.body().getCreated_at());
                        //createdat.append(response.body().getCreated_at());
                    }

                    @Override
                    public void onFailure(Call<CommitterInfo> call, Throwable t) {
                        Log.d("DetailFragment", t.getStackTrace().toString());
                        ErrorHandling.showToast(getActivity(),"Error fetching creation date");
                    }
                });
        return liveData;
    }

}
