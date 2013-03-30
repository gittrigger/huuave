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

import javax.microedition.khronos.opengles.GL10;

import android.graphics.Paint;
import android.util.Log;

public class GSprite {
	public GSprite() {
		mText = "";
		mLabelMaker = null;
	}

	private GL10 gl22;

	public void initialize(GL10 gl, Paint paint) {

		int height = roundUpPower2((int) paint.getFontSpacing());
		final float interDigitGaps = 9 * 1.0f;
		int width = roundUpPower2((int) (interDigitGaps + paint
				.measureText(sStrike)));
		mLabelMaker = new LabelMaker(true, width, height);
		mLabelMaker.initialize(gl);
		mLabelMaker.beginAdding(gl);
		for (int i = 0; i < sStrike.length(); i++) {
			String digit = sStrike.substring(i, i + 1);
			mLabelId[i] = mLabelMaker.add(gl, digit, paint);
			mWidth[i] = (int) Math.ceil(mLabelMaker.getWidth(i));
			mHeight[i] = (int) Math.ceil(mLabelMaker.getHeight(i));
		}
		mLabelMaker.endAdding(gl);
		gl22 = gl;
	}

	private int ia(char letter) {

		for (int i = 0; i < sStrike.length(); i++) {
			if (sStrike.charAt(i) == letter) {
				return i;
			}

			// if(letter.contentEquals(sStrike.substring(i, i+1))){return i;}
		}

		return 35;

	}

	public void shutdown(GL10 gl) {

		if (gl == null && gl22 != null) {
			mLabelMaker.shutdown(gl22);
		} else if (gl != null) {
			mLabelMaker.shutdown(gl);
		}
		// mLabelMaker = null;
	}

	/**
	 * Find the smallest power of two >= the input value. (Doesn't work for
	 * negative numbers.)
	 */
	private int roundUpPower2(int x) {
		x = x - 1;
		x = x | (x >> 1);
		x = x | (x >> 2);
		x = x | (x >> 4);
		x = x | (x >> 8);
		x = x | (x >> 16);
		return x + 1;
	}

	public void setValue(int value) {
		mText = format(value);
	}

	public void setValue(String value) {
		mText = value;
	}

	private float p1 = -1f;

	private final static String G = "Spriter";

	public void draw(GL10 gl, float x, float y, float viewWidth,
			float viewHeight) {
		if (p1 == -1f) {
			p1 = x;
		}
		int length = mText.length();

		if (gl == null) {
			Log.e(G, "gl null");
			return;
		}
		if (mLabelMaker == null) {
			Log.e(G, "mLabelMaker null");
			return;
		}
		mLabelMaker.beginDrawing(gl, viewWidth, viewHeight);

		int r = 0;
		for (int i = 0; i < length; i++) {
			char c = mText.charAt(i);

			int digit = ia(c);// - '0';

			if (c == '\n') {

				x = p1;
				y -= mHeight[digit];

				continue;
			} else if (x + mWidth[digit] > viewWidth) {

				x = p1;
				y -= mHeight[digit];

			}

			if (digit > mLabelId.length - 1) {
				Log.w("Spriter", "sr() digit(" + digit + ") ");
			} else {
				try {
					mLabelMaker.draw(gl, x, y, mLabelId[digit]);

				} catch (IllegalArgumentException ea) {
					Log.e("Spriter", "sr() draw failure");
					ea.printStackTrace();
				}
			}

			x += mWidth[digit];

		}

		try {
			mLabelMaker.endDrawing(gl);
		} catch (IllegalArgumentException ea) {
			Log.e("Spriter", "sr() draw failure");
			ea.printStackTrace();
		}

		gl22 = gl;

	}

	public float height() {
		float hx = 0f;
		for (int i = 0; i < mText.length(); i++) {
			hx += mHeight[i];
		}
		return hx;
	}

	public float width() {
		float width = 0.0f;
		int length = mText.length();
		for (int i = 0; i < length; i++) {
			// char c = mText.charAt(i);
			width += mWidth[i];
		}
		return width;
	}

	private String format(int value) {
		return Integer.toString(value);
	}

	private LabelMaker mLabelMaker;
	private String mText;
	private int[] mHeight = new int[127];
	private int[] mWidth = new int[127];
	private int[] mLabelId = new int[127];
	private final static String sStrike = "0123456789abcdefghijklmnopqrstuvwxyz:-.;\"\\[]{},<>/?=+-_)(*&^%$#@!~`'| ABCDEFGHIJKLMNOPQRSTUVWXYZ\n";
}
