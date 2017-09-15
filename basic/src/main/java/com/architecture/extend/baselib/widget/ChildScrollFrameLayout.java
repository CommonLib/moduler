package com.architecture.extend.baselib.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

/**
 * @author:dongpo 创建时间: 7/26/2016
 * 描述:
 * 修改:
 */
public class ChildScrollFrameLayout extends FrameLayout {
    public ChildScrollFrameLayout(Context context) {
        super(context);
    }

    public ChildScrollFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ChildScrollFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean canScrollHorizontally(int direction) {
        View child = getChildAt(0);
        if (child != null) {
            return child.canScrollHorizontally(direction);
        }
        return super.canScrollHorizontally(direction);
    }

    @Override
    public boolean canScrollVertically(int direction) {
        View child = getChildAt(0);
        if (child != null) {
            return child.canScrollVertically(direction);
        }
        return super.canScrollVertically(direction);
    }
}
