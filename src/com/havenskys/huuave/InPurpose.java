/*
 * Copyright (C) 2008 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.havenskys.huuave;
import android.util.Log;

public class InPurpose {

	/**
	 * Initializes m4 matrix
	 *
	 * 
	 */
	
	public InPurpose() {
		// start with identity matrix for transformation
		
		mTransform.setIdentity();
	}
        private static String G = "InPurpose";	
	public void startAnimation() {
                mAnimating = true;
		for (int i = 0; i < mShapes.length && mShapes[i] != null; i++) {
		mShapes[i].startAnimation();
		}
	}

	public void endAnimation() {
                mAnimating = false;
		for (int i = 0; i < mShapes.length; i++) {
			InPart shape = mShapes[i];
			if (shape != null) {
				shape.endAnimation();
			}	
		}
	}

public float x = 0f;public float y = 0f;public float z = 0f;
public float mTl1 = (float) Math.PI * 2f;
// orbit
public void setEt(float xi, float yi, float zi){

float[][] m = mTransform.m;

//Log.i(G,"(x"+xi+",y"+yi+",z"+zi+")          0("+m[0][0]+","+m[0][1]+","+m[0][2]+") 1("+m[1][0]+","+m[1][1]+","+m[1][2]+") 2("+m[2][0]+","+m[2][1]+","+m[2][2]+")");

m[0][2] = zi;
m[0][1] = yi;
m[0][0] = xi;

m[1][2] = zi;
m[1][1] = yi;
m[1][0] = xi;

m[2][2] = zi;
m[2][1] = yi;
m[2][0] = xi;
//x = xi; y = yi; z = zi;

/*
		for (int i = 0; i < mShapes.length && mShapes[i] != null; i++) {
//Log.w(G,"shape "+i+" (x"+mShapes[i].x+",y"+mShapes[i].y+",z"+mShapes[i].z+")");
		mShapes[i].z = zi;
		mShapes[i].y = yi;
		mShapes[i].x = xi;
                }

//*/
		for (int i = 0; i < mShapes.length && mShapes[i] != null; i++) {
		mShapes[i].animateTransform(mTransform);
		}
}

        public float mPivotz = 0f;
        public float mPivotx = 0f;	
        public float mPivoty = 0f;	
        
        /**
        * set public pivot offsets for rotation
        * 
        * @param pivotx x
        * @param pivoty y
        * @param pivotz z
        */
        public void setPivot(float pivotx, float pivoty, float pivotz){
        
        	mPivotz = pivotz;mPivoty = pivoty;mPivotx = pivotx;
        	
        }
        
        public void setAngle(int axis, float angle) {
		// normalize the angle
//while(angle < 0.0f){angle+=mTl1;}
//while(angle >= mTl1){angle-=mTl1;}

        	mAxis = axis;
        	mAngle = angle;

		float sin = (float)Math.sin(angle);
		float cos = (float)Math.cos(angle);

                		

		float[][] m = mTransform.m;
		switch (mAxis) {
			case kAxisX:
				m[1][1] = cos;
				m[1][2] = sin;
				m[2][1] = -sin;
				m[2][2] = cos;
				m[0][0] = 1f;
				m[0][1] = m[0][2] = m[1][0] = m[2][0] = 0f;
				break;
			case kAxisY:
				m[0][0] = cos;
				m[0][2] = sin;
				m[2][0] = -sin;
				m[2][2] = cos;
				m[1][1] = 1f;
				m[0][1] = m[1][0] = m[1][2] = m[2][1] = 0f;
				break;
			case kAxisZ:
				m[0][0] = cos;
				m[0][1] = sin;
				m[1][0] = -sin;
				m[1][1] = cos;
				m[2][2] = 1f;
				m[2][0] = m[2][1] = m[0][2] = m[1][2] = 0f;
				break;


		}

//if(mPivotx > 0){
//Log.i(G,"(x"+mPivotx+",y"+mPivoty+",z"+mPivotz+")          0("+m[0][0]+","+m[0][1]+","+m[0][2]+") 1("+m[1][0]+","+m[1][1]+","+m[1][2]+") 2("+m[2][0]+","+m[2][1]+","+m[2][2]+")");


//*
//                m[0][0] += mPivotx;//* m[0][0]+0.0001f/1000f;	
//                m[0][1] += mPivotx* m[0][1]+0.0001f/1000f;	
//                m[0][2] += mPivotx* m[0][1]+0.0001f/1000f;	
//                m[1][0] += mPivotx* m[1][0]+0.0001f/1000f;	
//                m[1][1] += mPivotx;// m[1][1]+0.0001f/1000f;	
//                m[1][2] += mPivotx;// m[1][1]+0.0001f/1000f;	
//                m[2][0] += mPivotx;// m[2][0]+0.0001f/1000f;	
//                m[2][1] += mPivotz;// m[2][1]+0.0001f/1000f;	
//                m[2][2] += mPivotx;//* m[2][1]+0.0001f/1000f;	
//		//*/
//}
                for (int i = 0; i < mShapes.length; i++) {
			InPart shape = mShapes[i];
			if (shape != null) {
				shape.animateTransform(mTransform);
			}
		}
	}
	
	InPart[] mShapes = new InPart[1001];
	M4 mTransform = new M4();
	float mAngle;
	boolean mAnimating = false;

	// which axis do we rotate around?
	// 0 for X, 1 for Y, 2 for Z
	int mAxis;
	static public final int kAxisX = 0;
	static public final int kAxisY = 1;
	static public final int kAxisZ = 2;	
}
