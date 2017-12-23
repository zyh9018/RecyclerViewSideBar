package com.zyh.widget.recyclerviewsidebar.sidebar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.support.annotation.DrawableRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.SectionIndexer;
import android.widget.TextView;

/**
 * SideBar forRecyclerView only support with LinearLayoutManager
 * <p>
 * Created by zyh on 2017/12/20.
 */
public class RecyclerViewSidebar extends View {
    private Paint mPaint;
    private float mHeight;
    private RecyclerView mRecyclerView;
    private TextView mFloatLetterTextView;
    private Context mContext;
    public static final int DEFAULT_SIDE_TEXT_SIZE = 10;
    private int mSelectedSideBarResId;

    public void setRecyclerView(RecyclerView recyclerView) {
        mRecyclerView = recyclerView;
    }

    public RecyclerViewSidebar(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        init();
    }

    private String[] sections;

    private void init() {
        sections =
                new String[]{"↑", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q",
                        "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "#"};
        // default color and text size
        mSelectedSideBarResId = Color.LTGRAY;
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.GRAY);
        mPaint.setTextAlign(Align.CENTER);
        mPaint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, DEFAULT_SIDE_TEXT_SIZE, mContext.getResources().getDisplayMetrics()));
    }

    public void setSideTextSize(float size) {
        if (size != mPaint.getTextSize()) {
            mPaint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, size, mContext.getResources().getDisplayMetrics()));
        }
    }

    public void setSideTextColor(int color) {
        mPaint.setColor(color);
    }

    public void setFloatLetterTextView(TextView mFloatLetterTextView) {
        this.mFloatLetterTextView = mFloatLetterTextView;
    }

    public void setSelectedSideBarColor(@DrawableRes int resId) {
        this.mSelectedSideBarResId = resId;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float center = getWidth() / 2;
        mHeight = getHeight() / sections.length;
        for (int i = sections.length - 1; i > -1; i--) {
            canvas.drawText(sections[i], center, mHeight * (i + 1), mPaint);
        }
    }

    private int sectionForPoint(float y) {
        int index = (int) (y / mHeight);
        if (index < 0) {
            index = 0;
        }
        if (index > sections.length - 1) {
            index = sections.length - 1;
        }
        return index;
    }

    private void setHeaderTextAndScroll(MotionEvent event) {
        if (mRecyclerView == null) {
            return;
        }
        String headerString = sections[sectionForPoint(event.getY())];
        if (mFloatLetterTextView != null) {
            mFloatLetterTextView.setText(headerString);
        }

        RecyclerView.LayoutManager layoutManager = mRecyclerView.getLayoutManager();
        if (layoutManager == null) {
            return;
        }

        RecyclerView.Adapter recyclerViewAdapter = mRecyclerView.getAdapter();
        if (recyclerViewAdapter instanceof SectionIndexer) {
            SectionIndexer adapter = (SectionIndexer) recyclerViewAdapter;
            String[] adapterSections = (String[]) adapter.getSections();
            try {
                for (int i = adapterSections.length - 1; i > -1; i--) {
                    if (adapterSections[i].equals(headerString)) {
                        if (layoutManager instanceof LinearLayoutManager) {
                            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
                            int positionForSection = adapter.getPositionForSection(i);
                            if (headerString.equals(sections[0])) {
                                positionForSection = 0;
                            }
                            linearLayoutManager.scrollToPositionWithOffset(positionForSection, 0);
                        } else {
                            throw new IllegalArgumentException("only used in RecyclerView with LinearLayoutManager");
                        }
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            throw new IllegalArgumentException("you must implement SectionIndexer in your adapter");
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                setHeaderTextAndScroll(event);
                if (mFloatLetterTextView != null) {
                    mFloatLetterTextView.setVisibility(View.VISIBLE);
                }
                setBackgroundResource(mSelectedSideBarResId);
                return true;
            }
            case MotionEvent.ACTION_MOVE: {
                setHeaderTextAndScroll(event);
                return true;
            }
            case MotionEvent.ACTION_UP:
                if (mFloatLetterTextView != null) {
                    mFloatLetterTextView.setVisibility(View.INVISIBLE);
                }
                setBackgroundColor(Color.TRANSPARENT);
                return true;
            case MotionEvent.ACTION_CANCEL:
                if (mFloatLetterTextView != null) {
                    mFloatLetterTextView.setVisibility(View.INVISIBLE);
                }
                setBackgroundColor(Color.TRANSPARENT);
                return true;
        }
        return super.onTouchEvent(event);
    }
}
