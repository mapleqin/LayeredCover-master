/*******************************************************************************
 * Copyright 2013-2014 Toaker LayeredCover-master
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.toaker.layeredcover;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;

import com.toaker.common.tlog.TLog;
import com.toaker.layeredcover.processor.CoverProcessor;

import java.util.ArrayList;
import java.util.List;

/**
 * Decorator for LayeredCover-master
 *
 * @author Toaker [Toaker](ToakerQin@gmail.com)
 *         [Toaker](http://www.toaker.com)
 * @Description:
 * @Time Create by 2015/4/14 13:16
 */
public class LayeredCoverLayout extends ViewGroup {

    protected static int VERSION = 1;

    protected static final boolean DEBUG = true;

    // log tag
    protected static String LOG_TAG = "LayeredCover-layout" + ++VERSION;

    protected List<CoverProcessor> mProcessors = new ArrayList<>();

    protected MotionEvent mDownMotionEvent;

    private float mCriterion = 1;

    protected MotionEvent mLastMoveMotionEvent;

    public LayeredCoverLayout(Context context) {
        super(context);
    }

    public LayeredCoverLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LayeredCoverLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        mProcessors.clear();
        for(int i=0;i<getChildCount();i++){
            mProcessors.add(new CoverProcessor(getChildAt(i),i));
        }
        super.onFinishInflate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if(DEBUG){
            TLog.d(LOG_TAG, "onMeasure width:%s height:%s padding: %s %s %s %s"
                    , getMeasuredWidth(), getMeasuredHeight(), getPaddingLeft(), getPaddingTop()
                    , getPaddingRight(), getPaddingBottom());
        }
        for (CoverProcessor processor:mProcessors){
            onMeasureChild(processor.getChildView(),widthMeasureSpec,heightMeasureSpec);
        }
    }

    /**
     * measure Child View;
     * @param child
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    private void onMeasureChild(View child, int widthMeasureSpec, int heightMeasureSpec) {
        if(child == null){
            if(DEBUG){
                TLog.d(LOG_TAG, "onMeasureChild Child View the not NULL");
            }
            return;
        }
        MarginLayoutParams marginLayoutParams = getMarginLayoutParams(child);;
        if(DEBUG){
            TLog.d(LOG_TAG, "onMeasureChild margin params : %s %s %s %s", marginLayoutParams.leftMargin, marginLayoutParams.topMargin
                    , marginLayoutParams.rightMargin, marginLayoutParams.bottomMargin);
        }
        int mChildWidthMeasureSpec = getChildMeasureSpec(widthMeasureSpec,child.getPaddingLeft() + child.getPaddingRight()
                                                         + marginLayoutParams.leftMargin + marginLayoutParams.rightMargin,marginLayoutParams.width);
        int mChildHeightMeasureSpec = getChildMeasureSpec(heightMeasureSpec,child.getPaddingTop() + child.getPaddingBottom()
                                                         + marginLayoutParams.topMargin + marginLayoutParams.bottomMargin,marginLayoutParams.height);

        if(DEBUG){
            TLog.d(LOG_TAG, "onMeasureChild %s widthMeasureSpec:%s heightMeasureSpec:%s", child, mChildWidthMeasureSpec, mChildHeightMeasureSpec);
        }
        child.measure(mChildWidthMeasureSpec,mChildHeightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if(DEBUG){
            TLog.d(LOG_TAG, "onLayout %s %s %s %s", l, t, r, b);
        }
        int marginTop = 0;
        int index = 0;
        for (CoverProcessor processor:mProcessors){
            MarginLayoutParams marginLayoutParams = getMarginLayoutParams(processor.getChildView());
            int left = getPaddingLeft() + marginLayoutParams.leftMargin;
            int top  = getPaddingTop() + marginLayoutParams.topMargin + marginTop;
            int right = left + processor.getChildView().getMeasuredWidth();
            int bottom = top + processor.getChildView().getMeasuredHeight();

            // child View layout;
            processor.onLayout(left, top, right, bottom);
            if(index == 0){
                this.mCriterion = processor.getMeasuredHeight();
            }
            processor.setCriterion(mCriterion);
            processor.setDistanceTopY(marginTop);
            TLog.d(LOG_TAG, "marginTop is %s", marginTop);

            TLog.d(LOG_TAG, "ChildViewHeight is %s", processor);
            marginTop += marginLayoutParams.topMargin + marginLayoutParams.bottomMargin + processor.getMeasuredHeight();
            index ++;
            if(DEBUG){
                TLog.d(LOG_TAG, "onLayout Child View %s  layout: %s %s %s %s  marginTop:%s", processor, left, top, right, bottom, marginTop);
            }
        }
    }

    /**
     *
     * @param event
     * @return
     */
    protected boolean dispatchTouchEventParent(MotionEvent event){
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                if(DEBUG){
                    TLog.i(LOG_TAG,"Touch Event %s  x:%s  y:%s",event.getAction(),event.getX(),event.getY());
                }
                return true;

            case MotionEvent.ACTION_MOVE:
                mLastMoveMotionEvent = event;
                if(DEBUG){
                   TLog.i(LOG_TAG,"Touch MOVE x:%s  y:%s",event.getX(),event.getY());
                }
                for (CoverProcessor processor:mProcessors){
                    processor.disposeMove(event.getX(),event.getY());
                }
                invalidate();
                return true;

            case MotionEvent.ACTION_DOWN:
                mDownMotionEvent = event;
                for (CoverProcessor processor:mProcessors){
                    processor.disposeDown(event.getX(),event.getY());
                }
                if(DEBUG){
                    TLog.i(LOG_TAG,"Touch Down x:%s  y:%s",event.getX(),event.getY());
                }
                return true;
        }
        return dispatchTouchEventParent(event);
    }

    /**
     * Processor the Child View move;
     */
    private void moveChildView() {
//        for(int i= 0;i<mChildViews.size();i++){
//            View view = mChildViews.get(i);
//            CoverProcessor processor = mProcessors.get(i);
//            //if(!processor.isTopEdges() && !processor.isBottomEdges()){
//                view.offsetTopAndBottom((int) processor.getOffsetY());
//            //}
//
//            if(DEBUG){
//                //TLog.i(LOG_TAG,"moveChildView offset : %s",processor.getOffsetY());
//            }
//        }
//        invalidate();
    }

    /**
     *
     */
    protected void dispatchCancelTouchEvent(){
       if(DEBUG){
           //TLog.i(LOG_TAG,"dispatchCancelTouchEvent");
       }
        MotionEvent event = MotionEvent.obtain(mDownMotionEvent.getDownTime(), mDownMotionEvent.getEventTime() + ViewConfiguration.getLongPressTimeout(), MotionEvent.ACTION_CANCEL,
                mDownMotionEvent.getX(), mDownMotionEvent.getY(), mDownMotionEvent.getMetaState());
        dispatchTouchEventParent(event);
    }

    /**
     *
     */
    protected void dispatchDownTouchEvent(){
        if(DEBUG){
            //TLog.i(LOG_TAG,"dispatchDownTouchEvent");
        }
        MotionEvent event = MotionEvent.obtain(mLastMoveMotionEvent.getDownTime(), mLastMoveMotionEvent.getEventTime(), MotionEvent.ACTION_DOWN,
                mLastMoveMotionEvent.getX(), mLastMoveMotionEvent.getY(), mLastMoveMotionEvent.getMetaState());
        dispatchTouchEventParent(event);
    }

    /**
     * Get View MarginLayoutParams
     * @param child
     * @return
     */
    protected MarginLayoutParams getMarginLayoutParams(View child){
        if(child == null || child.getLayoutParams() == null){
            return null;
        }
        if(child.getLayoutParams() instanceof MarginLayoutParams){
            return (MarginLayoutParams) child.getLayoutParams();
        }else {
            return new MarginLayoutParams(child.getLayoutParams());
        }
    }

    public static class LayoutParams extends MarginLayoutParams {

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        @SuppressWarnings({"unused"})
        public LayoutParams(MarginLayoutParams source) {
            super(source);
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }
    }
}
