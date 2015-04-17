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
package com.toaker.layeredcover.processor;

import android.view.View;
import android.widget.Scroller;

import com.toaker.common.tlog.TLog;

/**
 * Decorator for LayeredCover-master
 *
 * @author Toaker [Toaker](ToakerQin@gmail.com)
 *         [Toaker](http://www.toaker.com)
 * @Description:
 * @Time Create by 2015/4/15 14:21
 */
public class CoverProcessor {

    protected static int VERSION = 1;

    protected static final boolean DEBUG = true;

    // log tag
    protected static String LOG_TAG = "CoverProcessor-handle" + ++VERSION;

    private View mChildView;

    private int mPosition;

    private float mDistanceTopY;

    private float mOffsetY;

    private float mDownY;

    private float mCriterion;

    private float   mScale;

    private Scroller mScroll;

    private boolean isUp = true;

    public  CoverProcessor(View child,int position,Scroller mScroll){
        this.mPosition = position;
        this.mChildView = child;
        this.mScroll  = mScroll;
    }

    public void disposeDown(float x,float y){
        this.mDownY = y;
    }

    public void disposeMove(float x,float y){
        mDownY = y;
        scrollBy(y - mDownY);
        if(DEBUG){
            TLog.i(LOG_TAG, "Touch MOVE Scale:%s -- P:%s Top:%s", getScale(),mPosition,mChildView.getTop());
        }
    }

    public void scrollBy(float y) {
        setOffsetY(Math.round(y * getScale()));
        if(isBottomEdges() ||isTopEdges()){
            return;
        }
        if(mChildView.getTop() + getOffsetY() < 0){
            setOffsetY(0 - mChildView.getTop());
        }
        if(mChildView.getTop() + getOffsetY() > mDistanceTopY){
            setOffsetY(mDistanceTopY - mChildView.getTop());
        }
        isUp = getOffsetY() > 0;
        mChildView.offsetTopAndBottom(Math.round(getOffsetY() * 0.6f));
    }

    public int getLimit(){
        return isUp?-10:10;
    }

    public void fling(int velocityY){
        mScroll.fling(0,mChildView.getTop(),0,velocityY,0,0,0, (int) mDistanceTopY);
    }

    public Scroller getScroller() {
        return mScroll;
    }

    public float getScale() {
        if(mPosition == 0){
            this.mScale = mPosition;
        }else {
            this.mScale = mDistanceTopY / mCriterion;
        }
        return mScale;
    }

    public void onLayout(int l, int t, int r, int b) {
        mChildView.layout(l,t,r,b);
    }

    public void setCriterion(float mCriterion) {
        this.mCriterion = mCriterion;
    }

    public boolean isTopEdges(){
        return mChildView.getTop() < 0;
    }

    public boolean isBottomEdges(){
        return mChildView.getTop() > mDistanceTopY;
    }

    public void setDistanceTopY(float distanceTopY){
        this.mDistanceTopY = distanceTopY;
    }

    public float getDistanceTopY() {
        return mDistanceTopY;
    }

    public float getOffsetY() {
        return mOffsetY;
    }

    public void setOffsetY(float mOffsetY) {
        this.mOffsetY = mOffsetY;
    }

    public View getChildView() {
        return mChildView;
    }

    public int getMeasuredHeight(){
        return mChildView.getMeasuredHeight();
    }
}
