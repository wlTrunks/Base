package com.inter.trunks.base.component;

import android.graphics.PorterDuff;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;
import com.inter.trunks.base.R;

public class ProgressableUIComponent extends UIComponent implements Progressable {
    protected View progressView;
    protected View rootView;
    private boolean sendEvents = true;
    private boolean needHideRootView = true;
    private int progressViewColor = R.color.colorAccent;

    @Override
    public void onViewCreated(View layout) {
        super.onViewCreated(layout);
        progressView = layout.findViewById(R.id.progress);
        rootView =layout.findViewById(R.id.root);
        if (progressView instanceof ProgressBar) {
            ((ProgressBar) progressView).getIndeterminateDrawable()
                    .setColorFilter(getProgressColor(), PorterDuff.Mode.SRC_IN);
        }
    }

    @Override
    public void begin() {
        if (sendEvents)
        progressView.setVisibility(View.VISIBLE);
        if (needHideRootView) rootView.setVisibility(View.GONE);
    }

    @Override
    public void end() {
        if (sendEvents)
        progressView.setVisibility(View.GONE);
        rootView.setVisibility(View.VISIBLE);
        Animation animation = getAnimationForContent();
        rootView.startAnimation(animation);
    }

    public ProgressableUIComponent setProgressViewColor(int progressViewColor) {
        this.progressViewColor = progressViewColor;
        return this;
    }

    public ProgressableUIComponent setNeedHideRootView(boolean needHideRootView) {
        this.needHideRootView = needHideRootView;
        return this;
    }

    public void setSendEvents(boolean b) {
        sendEvents = b;
    }

    protected Animation getAnimationForContent() {
        return AnimationUtils.loadAnimation(getContext(), R.anim.fadein);
    }

    protected int getProgressColor() {
        int result = 0;
        try {
            result = getContext().getResources().getColor(progressViewColor);
        } catch (Exception ignored) {
        }
        return result;
    }
}
