package com.pof.gitcommitterinfo.ui;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextSwitcher;
import android.widget.TextView;

import com.pof.gitcommitterinfo.R;
import com.pof.gitcommitterinfo.util.Utility;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class CustomTextSwitcher extends TextSwitcher {

    private String[] stringArray = new String[2];
    private Context context;
    private TextView textView;
    private Disposable disposable;


    public CustomTextSwitcher(Context context) {
        super(context);
        this.context = context;
        initTextSwitcher();

    }

    public CustomTextSwitcher(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initTextSwitcher();
    }

    public void setTextsToSwitch(String[] stringArray) {
        this.stringArray = stringArray;
        setText(stringArray[0]);
    }

    private void initTextSwitcher() {
        textView = new TextView(context);
        setFactory(new ViewFactory() {
            @Override
            public View makeView() {
                textView = new TextView(context); //getContext()
                textView.setTextColor(Color.BLACK);
                textView.setTextSize(16);
                textView.setMaxLines(1);
                textView.setEllipsize(TextUtils.TruncateAt.END);
                textView.setText(stringArray[0]);
                //textView.setGravity(Gravity.CENTER_HORIZONTAL);
                return textView;
            }
        });

        setInAnimation(context, R.anim.inanimation);
        setOutAnimation(context, R.anim.outanimation);

    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        subscribeToPeriodicObservable();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        unSubscribeToPeriodicObservable();
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        if(View.VISIBLE == visibility) {
            subscribeToPeriodicObservable();
        } else {
            unSubscribeToPeriodicObservable();
        }
    }

    private void subscribeToPeriodicObservable() {
        disposable = Utility.getPeriodicObservable()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Consumer<Integer>() {
                @Override
                public void accept(Integer it) throws Exception {
                    if(!stringArray[0].equals(stringArray[1])) {
                        CustomTextSwitcher.this.setText(stringArray[it]);
                        unSubscribeToPeriodicObservable();
                    }


                }
            });
    }

    private void unSubscribeToPeriodicObservable() {
        if(disposable!=null) {
            disposable.dispose();
            disposable = null;
        }
    }

}
