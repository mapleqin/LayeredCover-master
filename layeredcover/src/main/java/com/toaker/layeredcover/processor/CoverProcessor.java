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

/**
 * Decorator for LayeredCover-master
 *
 * @author Toaker [Toaker](ToakerQin@gmail.com)
 *         [Toaker](http://www.toaker.com)
 * @Description:
 * @Time Create by 2015/4/15 14:21
 */
public class CoverProcessor {

    private float mDistanceTopY;

    private float mChildViewHeight;

    private float mOffsetY;

    private float mCurrentY;

    private float mDownY;

    private float mLastY;

    public void disposeDown(float x,float y){
        this.mDownY = y - mLastY;
    }

    public void disposeMove(float x,float y){
        setOffsetY(mDistanceTopY / (mDownY - y));
        setCurrentY(mCurrentY + getOffsetY());
        mLastY = y;
    }

    public boolean isTopEdges(){
        return getCurrentY() <= 0;
    }

    public boolean isBottomEdges(){
        return getCurrentY() >= mDistanceTopY;
    }

    public boolean isShieldTouch(){
        return mCurrentY <= 0;
    }

    public void setDistanceTopY(float distanceTopY){
        this.mDistanceTopY = distanceTopY;
    }

    public float getDistanceTopY() {
        return mDistanceTopY;
    }

    public float getChildViewHeight() {
        return mChildViewHeight;
    }

    public void setChildViewHeight(float mChildViewHeight) {
        this.mChildViewHeight = mChildViewHeight;
    }

    public float getOffsetY() {
        return mOffsetY;
    }

    public void setOffsetY(float mOffsetY) {
        this.mOffsetY = mOffsetY;
    }

    public float getCurrentY() {
        return mCurrentY;
    }

    public void setCurrentY(float mCurrentY) {
        this.mCurrentY = mCurrentY;
    }

    public static CoverProcessor emptyCoverProcessor(){
        return new CoverProcessor();
    }
}
