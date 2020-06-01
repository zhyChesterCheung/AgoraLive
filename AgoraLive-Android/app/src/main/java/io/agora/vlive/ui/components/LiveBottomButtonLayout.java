package io.agora.vlive.ui.components;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import io.agora.vlive.AgoraLiveApplication;
import io.agora.vlive.R;

public class LiveBottomButtonLayout extends RelativeLayout implements View.OnClickListener {
    public static final int ROLE_AUDIENCE = 1;
    public static final int ROLE_HOST = 2;
    public static final int ROLE_OWNER = 3;

    public interface LiveBottomButtonListener {
        void onLiveBottomLayoutShowMessageEditor();
    }

    private AppCompatImageView mCancel;
    private AppCompatImageView mMore;
    private AppCompatImageView mFun1;
    private AppCompatImageView mFun2;
    private AppCompatTextView mInputText;
    private int mRole;
    private boolean mVirtualImage;

    private LiveBottomButtonListener mListener;

    public LiveBottomButtonLayout(Context context) {
        super(context);
    }

    public LiveBottomButtonLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LiveBottomButtonLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void init(boolean lightMode, boolean isVirtualImage) {
        int layout = lightMode ?
                R.layout.live_bottom_button_layout_light :
                R.layout.live_bottom_button_layout;
        LayoutInflater.from(getContext()).inflate(layout, this, true);
        mFun1 = findViewById(R.id.live_bottom_btn_fun1);
        mFun2 = findViewById(R.id.live_bottom_btn_fun2);
        mFun2.setVisibility(isVirtualImage ? View.GONE : View.VISIBLE);
        mInputText = findViewById(R.id.live_bottom_message_input_hint);
        mInputText.setOnClickListener(this);
        mCancel = findViewById(R.id.live_bottom_btn_close);
        mMore = findViewById(R.id.live_bottom_btn_more);
        mVirtualImage = isVirtualImage;
    }

    public void init() {
        init(false, false);
    }

    public void setRole(int role) {
        mRole = role;

        if (mRole == ROLE_OWNER) {
            mFun1.setImageResource(R.drawable.live_bottom_button_music);
            mFun2.setVisibility(mVirtualImage ? View.GONE : View.VISIBLE);
            mFun2.setImageResource(R.drawable.live_bottom_button_beauty);
        } else if (mRole == ROLE_HOST) {
            mFun2.setVisibility(mVirtualImage ? View.GONE : View.VISIBLE);
            mFun2.setImageResource(R.drawable.live_bottom_button_beauty);
            mFun1.setImageResource(R.drawable.live_bottom_btn_present);
        } else if (mRole == ROLE_AUDIENCE) {
            mFun2.setVisibility(View.GONE);
            mFun1.setImageResource(R.drawable.live_bottom_btn_present);
        }
    }

    public void setLiveBottomButtonListener(LiveBottomButtonListener listener) {
        mListener = listener;
    }

    public void setMusicPlaying(boolean playing) {
        if (mRole == ROLE_OWNER) mFun1.setActivated(playing);
    }

    public void setBeautyEnabled(boolean enabled) {
        if (mRole == ROLE_OWNER || mRole == ROLE_HOST) mFun2.setActivated(enabled);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = getResources().getDimensionPixelSize(R.dimen.live_bottom_layout_height);
        setMeasuredDimension(width, height);
        int heightSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightSpec);
    }

    /**
     * Clear all global settings that are no longer needed
     * after leaving the live room
     * @param application
     */
    public void clearStates(AgoraLiveApplication application) {
        application.config().setCurrentMusicIndex(-1);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.live_bottom_message_input_hint) {
            if (mListener != null) mListener.onLiveBottomLayoutShowMessageEditor();
        }
    }
}