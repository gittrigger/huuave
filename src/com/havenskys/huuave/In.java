package com.havenskys.huuave;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.SubMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.TextView;
import android.widget.Toast;
import javax.microedition.khronos.opengles.GL;
import com.havenskys.galaxy.SqliteWrapper;
import com.havenskys.galaxy.DataProvider;
import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import java.util.Random;
import android.net.Uri;
import android.content.ContentValues;
import android.content.SharedPreferences.Editor;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.graphics.PixelFormat;
import android.view.MotionEvent;
import android.view.View.OnTouchListener;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.egl.EGL10;
//import com.webghost.DbAdapter;
//import com.webghost.SyncProcessing;
//import com.webghost.WebServer;

public class In extends Activity implements InRenderer.AnimationCallback {
	/** Called when the activity is first created. */

	int mGLworkid = -1;

	private World mkGLwork() {
		World world = new World();
		world.mTrimx = -50f; // distance
		world.mTrimy = -10f;
		world.mTrimz = 10f;
		// world.mPivotx = 30.0f;//
		// world.mPivoty = 30.0f;//y tall
		// world.mPivotz = 30.0f;

		int t = 1;
		int j;
		for (t = 0; t < mPurpose.length; t++) {
			if (mPurpose[t] != null) {
			} else {
				mPurpose[t] = new InPurpose();

				mGLworkid = t;
				mA[t] = InPurpose.kAxisX;
				// mPurpose[t].setPivot(30f,30f,30f);
				mPart[t][0] = new InPartShape(world, -10.7f, -10.2f, -13.7f,
						14.5f, 16.7f, 14.5f);

				mPart[t][0].x = -50f;
				mPart[t][0].y = -10f;
				mPart[t][0].z = 10f;

				// mPart[t][0]
				for (j = 0; j < 6; j++) {
					if (j == 0 || j == 5) {
						mPart[t][0].setFaceColor(j, black);
						continue;
					}
					mPart[t][0].setFaceColor(j, orange);
				}
				world.addShape(mPart[t][0]);

				// t++;
				// mPurpose[t] = new InPurpose();
				// mA[t] = InPurpose.kAxisY;
				// mPart[t][0] = mPart[t-1][0];
				// lbbrtf
				// angle(y-9.400001,x-202.20007,z0.0) camera(-1.4,-13.6,-87.0)
				// world.addShape(mPart[t-1][0]);
				break;
			}
		}

		updateInPurpose();
		world.generate();
		return world;
	}

	// orbit
	private World mkConsole() {

		World world = new World();
		world.mTrimx = 0f; // distance
		world.mTrimy = 0f;
		world.mTrimz = 0f;
		world.mPivotx = -110f;//
		world.mPivoty = -10f;// y tall
		world.mPivotz = 0f;

		int t = 1;
		for (t = 0; t < mPurpose.length; t++) {
			if (mPurpose[t] != null) {
				continue;
			} else {
				console = t;
				mPurpose[t] = new InPurpose();
				mA[t] = InPurpose.kAxisY;
				mPurpose[t].setAngle(mA[t], 0f);
				// mPurposeU[t] = 0f;
				// mPurposeU[t] = 10f;
				// mPart[t][0] = new InPartShape(world,0,90,108f,52f,125,110f);
				// mPart[t][1] = new InPartShape(world,(0f),(60),(108f), (52f),
				// (85.0f), (110f) );
				// mPart[t][2] = new InPartShape(world,(52f),(60),(108f),
				// (140f), (62.0f), (110f) );
				// mPart[t][3] = new InPartShape(world,(52f),(54),(108f),
				// (140f), (56.0f), (110f) );
				mPart[t][0] = new InPartShape(world, (0f), (0), (108f), (52f),
						(50.0f), (110f));
				// mPart[t][5] = new InPartShape(world,(0f),(-50f),(108f),
				// (52f), (25.0f), (110f) );
				int j = 1;
				for (int i = 0; i <= 0 && mPart[t] != null; i++) {
					for (j = 0; j < 6; j++) {
						if (j == 0 || j == 5) {
							mPart[t][i].setFaceColor(j, black);
							continue;
						}
						mPart[t][i].setFaceColor(j, orange);
					}
				}
				world.addShape(mPart[t][0]);
				// world.addShape(mPart[t][1]);
				// world.addShape(mPart[t][2]);
				// world.addShape(mPart[t][3]);
				// world.addShape(mPart[t][4]);
				// world.addShape(mPart[t][5]);

				break;
			}
		}

		updateInPurpose();
		world.generate();
		return world;
	}

	// orbit
	// console

	private World mkWorld() {

		int i, j;
		int t = 1;
		int one = 0x10000;
		int half = 0x08000;

		World world = new World();
		world.mTrimx = 2f;
		// left bottom(X) back right top front
		// mPart[0][0] = new InPartShape(world, 3.5f, 1.76f, 2.0f, 3.76f, 2.0f,
		// 2.76f);
		/*
		 * mPart[0][1] = new InPartShape(world, 3.0f, 4.0f, 3.0f, 3.3f, 5.0f,
		 * 4.0f); mPart[0][2] = new InPartShape(world, 3.0f, 4.1f, 4.5f, 3.3f,
		 * 5.1f, 6.5f); mPart[0][3] = new InPartShape(world, 3.0f, 4.2f, 7.0f,
		 * 3.3f, 5.2f, 8.0f); mPart[0][4] = new InPartShape(world, 3.0f, 2.5f,
		 * 3.0f, 3.3f, 3.5f, 4.0f); mPart[0][5] = new InPartShape(world, 3.0f,
		 * 2.6f, 4.5f, 3.3f, 3.6f, 6.5f); mPart[0][6] = new InPartShape(world,
		 * 3.0f, 2.7f, 7.0f, 3.3f, 3.7f, 8.0f); // mPart[0][0] = new
		 * InPartShape(world, -5.0f, -1.5f, -1.0f, 5.5f, -0.2f, 1.1f);
		 * 
		 * mPart[4][0] = new InPartShape(world, 3.5f, 2.0f, -2.0f, 3.76f, 3.0f,
		 * -0.76f); mPart[4][1] = new InPartShape(world, 3.0f, 4.0f, 3.0f, 3.3f,
		 * 5.0f, 4.0f); mPart[4][2] = new InPartShape(world, 3.0f, 4.1f, 4.5f,
		 * 3.3f, 5.1f, 6.5f); mPart[4][3] = new InPartShape(world, 3.0f, 4.2f,
		 * 7.0f, 3.3f, 5.2f, 8.0f); mPart[4][4] = new InPartShape(world, 3.0f,
		 * 2.5f, 3.0f, 3.3f, 3.5f, 4.0f); mPart[4][5] = new InPartShape(world,
		 * 3.0f, 2.6f, 4.5f, 3.3f, 3.6f, 6.5f); mPart[4][6] = new
		 * InPartShape(world, 3.0f, 2.7f, 7.0f, 3.3f, 3.7f, 8.0f); //
		 * mPart[4][0] = new InPartShape(world, 3.5f, 2.0f, -2.0f, 3.76f, 3.0f,
		 * -0.76f); //
		 */
		// mPart[1][0] = new InPartShape(world, -11.0f, 0.32f, -1.0f, -10.32f,
		// 1.0f, -0.32f);
		// mPart[0][0] = new InPartShape(world, 2.5f, 3.5f, 9.5f, 3.5f, 6.5f,
		// 0.5f);
		// mPart[0][1] = new InPartShape(world, 3.5f, 3.76f, 9.5f, 4.5f, 2.0f,
		// 2.0f);
		// mPart[0][0] = new InPartShape(world, -0.7f, 3.5f, 9.5f, 0.7f, 6.5f,
		// 0.5f);
		// mPart[1][0] = new InPartShape(world, -0.7f, 3.5f, 9.5f, 0.7f, 6.5f,
		// 0.5f);

		// x left-right/
		mPart[0][0] = new InPartShape(world, -4.7f, -4.7f, -24.4f, 4.5f, 4.5f,
				-24.7f);
		mPart[0][1] = new InPartShape(world, -4.7f, -4.7f, -54.4f, 4.5f, 4.5f,
				-54.7f);
		mPart[0][2] = new InPartShape(world, -4.7f, -4.7f, -154.4f, 4.5f, 4.5f,
				-154.7f);
		// z in-out
		mPart[1][0] = new InPartShape(world, -4.7f, -25.3f, -3.7f, 4.5f,
				-25.0f, 4.5f);
		mPart[1][1] = new InPartShape(world, -4.7f, -55.3f, -3.7f, 4.5f,
				-55.0f, 4.5f);
		mPart[1][2] = new InPartShape(world, -4.7f, -155.3f, -3.7f, 4.5f,
				-155.0f, 4.5f);
		// y up-down
		mPart[2][0] = new InPartShape(world, -4.7f, -24.7f, -4.7f, 4.5f,
				-24.4f, 4.5f);
		mPart[2][1] = new InPartShape(world, -4.7f, -54.7f, -4.7f, 4.5f,
				-54.4f, 4.5f);
		mPart[2][2] = new InPartShape(world, -4.7f, -154.7f, -4.7f, 4.5f,
				-154.4f, 4.5f);

		// platform
		mPart[3][0] = new InPartShape(world, -62.7f, 16.2f, -64.7f, -24.5f,
				16.7f, -24.5f);
		// mPart[4][0] = new InPartShape(world, 2f, 4f, 9f, 4f, 7f, 9f);
		// mPart[5][0] = new InPartShape(world, -1f, 4f, 9f, 1f, 7f, 9f);
		// mPart[3][0] = new InPartShape(world, -5.0f, -4.0f, -5.0f, 5.0f,
		// -3.5f, 5.0f);

		// mXthing = new InPurpose(InPurpose.kAxisX);
		// mBthing = new InPurpose(InPurpose.kAxisY);
		mPurpose[0] = new InPurpose();
		mA[0] = (InPurpose.kAxisY);// spin horizontally
		mPurpose[1] = new InPurpose();
		mA[1] = (InPurpose.kAxisZ);
		mPurpose[2] = new InPurpose();
		mA[2] = (InPurpose.kAxisX);
		mPurpose[3] = new InPurpose();
		mA[3] = (InPurpose.kAxisX);
		// mPurpose[0].mAxis =
		// InPurpose.kAxisX;mPurpose[0].setAngle(155);mPurpose[0].mAxis =
		// InPurpose.kAxisY;
		// mPurpose[1].mAxis =
		// InPurpose.kAxisY;mPurpose[1].setAngle(155);mPurpose[1].mAxis =
		// InPurpose.kAxisZ;
		// mPurpose[2].mAxis =
		// InPurpose.kAxisZ;mPurpose[2].setAngle(155);mPurpose[2].mAxis =
		// InPurpose.kAxisX;

		// mPurpose[4] = new InPurpose(InPurpose.kAxisY);
		// mPurpose[5] = new InPurpose(InPurpose.kAxisZ);
		lathing = 3;

		t = lathing;

		/*
		 * for(int g=0;g<GRAPH_SIZE;g++){ t++;mPart[t][0] = new
		 * InPartShape(world
		 * ,-1.5f,4f+(g*0.15f)+(g*0.12f),7.8f+(g*g*0.015f),1.5f,
		 * 8f+(g*0.15f)+(g*0.12f),8f+(g*g*0.015f)); } t = lathing; for(int
		 * g=0;g<GRAPH_SIZE;g++){ t++;mPurpose[t] = new
		 * InPurpose(InPurpose.kAxisY); mPurposeU[t] = g*g* 0.25f;
		 * mPurpose[t].setAngle(mPurposeU[t]); }//
		 */

		for (int g = 0; g < GRAPH_SIZE; g++) {// left bottom back -- right top
												// front
			t++;
			mPart[t][0] = new InPartShape(world, -1.5f, 4f - (g * 0.15f)
					- (g * 1.12f), 9.2f + (g * g * 0.15f), 1.5f, 8f
					- (g * 0.15f) - (g * 1.12f), 10.8f + (g * g * 0.15f));
			mPurposeU[t] = g * g * 5.25f;
			mPurpose[t] = new InPurpose();
			mA[t] = InPurpose.kAxisY;
			mPurpose[t].setAngle(mA[t], mPurposeU[t]);

		}

		/*
		 * for(int g=0;g<GRAPH_SIZE;g++){//left bottom back -- right top front
		 * t++; mPart[t][0] = new
		 * InPartShape(world,-1f+(g*5.5f),-3f+(g*0.5f),22.8f-(g*0.2f),
		 * 5f+(g*5.5f),2f+(g*0.5f),23f-(g*0.2f)); mPurposeU[t] = g*g*0.25f;
		 * mPurpose[t] = new InPurpose();mA[t] = (InPurpose.kAxisY);
		 * mPurpose[t].setAngle(mA[t],mPurposeU[t]); //world.loadTexture((GL10)
		 * mPurpose[t],mCtx,R.drawable.redt); }//
		 */

		orbitlimit = t;

		// */

		// mPurpose[t].setAngle(f*0.15f);
		// mPart[t][0] = new
		// InPartShape(world,-1f,4f+g*0.1f,8.8f+g*0.1f,1f,7f+g*0.1f,9f+g*0.1f);

		// mPart[t][0].glRotate(0.45f * g, 0, 1, 0);
		// mPurpose[t].setAngle(0.15f*g);
		// for(j=0;j<6;j++){mPart[t][g].setFaceColor(j,white);}
		// world.addShape(mPart[t][g]);

		// mPurpose[2].setAngle(10);
		for (t = 0; t < mPurpose.length; t++) {
			InPurpose thing = mPurpose[t];
			if (thing != null) {

				for (i = 0; i < mPart[t].length; i++) {
					InPartShape cube = mPart[t][i];
					if (cube != null) {

						// for(j=0;j<6;j++){cube.setFaceColor(j,white);}

						if (t > 1) {
							cube.setFaceColor(5, orange);
							cube.setFaceColor(1, white);
							cube.setFaceColor(0, green);
							cube.setFaceColor(2, white);
							cube.setFaceColor(3, white);
							cube.setFaceColor(4, white);

						} else {
							cube.setFaceColor(0, green);// bottom
							cube.setFaceColor(1, black);// back
							cube.setFaceColor(2, yellow);// right
							cube.setFaceColor(3, orange);// left
							cube.setFaceColor(4, red);// front
							cube.setFaceColor(5, blue);// top

						}

						world.addShape(cube);

					}
				}

			}
		}

		// for(j=0;j<6;j++){if(j==0||j==5){mPart[2][0].setFaceColor(j,white);continue;}mPart[2][0].setFaceColor(j,blue);}
		// for(t =
		// 0;t<4;t++){for(j=0;j<6;j++){if(j==0||j==5){mPart[t][0].setFaceColor(j,black);continue;}mPart[t][0].setFaceColor(j,orange);}}
		// for(j=0;j<6;j++){if(j==0||j==5){mPart[2][2].setFaceColor(j,white);continue;}mPart[2][2].setFaceColor(j,yellow);}
		// for(j=0;j<6;j++){if(j==0||j==5){mPart[3][0].setFaceColor(j,white);continue;}mPart[3][0].setFaceColor(j,orange);}

		updateInPurpose();

		world.generate();
		return world;

	}

	// float mCurrentAngle, mEndAngle;
	// float mAngleIncrement;

	// InPurpose mXthing, mBthing;
	private void updateInPurpose() {
		int t, i, j, k;
		InPurpose thing;
		InPart[] shapes;
		// if(mAthing != null){
		// int ak = 0;
		// InPart[] allashapes = mPurpose[lathing].mShapes;
		// InPart[] allbshapes = mBthing.mShapes;
		for (t = 0; t < mPurpose.length && mPurpose[t] != null; t++) {
			shapes = mPurpose[t].mShapes;
			for (i = 0, k = 0; i < mPart[t].length && mPart[t][i] != null; i++) {
				shapes[k++] = mPart[t][i];
				// allbshapes[ak] = cube;
				// allashapes[ak] = cube;
				// ak++;
			}

		}
		// }
	}

	// orbit

	Handler SensorService = new Handler() {
		boolean running = false;

		public void handleMessage(Message msg) {
			final Bundle bdl = msg.getData();

			SharedPreferences mReg = getSharedPreferences("Preferences",
					MODE_WORLD_READABLE);
			if (!mReg.contains("waveopt")) {
				Editor mEdt = mReg.edit();
				mEdt.putBoolean("wave", true);
				mEdt.putBoolean("waveopt", true);
				mEdt.commit();
			} else {
				if (!mReg.contains("wave")) {
					return;
				}
			}
			if (running) {
				return;
			}

			running = true;
			Thread tx = new Thread() {
				boolean mStable = true;
				int position = 0;
				float[] lastvalues;
				long smooth = 34;// long smoothtext = 32;//String cn = "";

				public void run() {

					SensorEventListener or = new SensorEventListener() {

						// SharedPreferences mReg =
						// getSharedPreferences("Preferences",
						// MODE_WORLD_READABLE);

						public void onAccuracyChanged(Sensor arg0, int arg1) {
							// TODO Auto-generated method stub

						}

						// float mStable0 = 1;
						public void onSensorChanged(SensorEvent event) {
							// TODO Auto-generated method stub

							// if(smooth > SystemClock.uptimeMillis() ||
							// !getListView().hasFocus() ){return;}
							if (smooth > SystemClock.uptimeMillis()) {
								return;
							}

							smooth = SystemClock.uptimeMillis() + 130;// bdl.getInt("sensorspeed",250);
							float[] values = event.values;
							float valence = 0;

							if (lastvalues == null) {
								Log.w(G, "Loading Initial Sensor Values");
								lastvalues = values;
								for (int b = 0; b < values.length; b++) {
									lastvalues[b] = 0;
								}
							}
							// if(getListView().isShown() ||
							// getListView().hasFocus()){}else{Log.e(G,"List isn't shown and nofocus, sensor watch close");wayGo.sendEmptyMessage(2);}

							if (lastvalues != null
									&& values.length == lastvalues.length) {
								int b = 2;
								// {valence =
								// (lastvalues[b]>values[b]?lastvalues[b]-values[b]:values[b]-lastvalues[b]);}
								// float valence2 = 1f;{b = 1;
								// valence2 =
								// (lastvalues[b]>values[b]?lastvalues[b]-values[b]:values[b]-lastvalues[b]);}
								// float valence3 = 1f;{b = 1;
								// valence3 =
								// (lastvalues[b]>values[b]?lastvalues[b]-values[b]:values[b]-lastvalues[b]);}
								// if(valence == 0 || valence2 == 0){return;}

								// if(){
								for (b = 0; b < values.length; b++) {
									// float o = lastvalues[b];
									valence = (lastvalues[b] > values[b] ? lastvalues[b]
											- values[b]
											: values[b] - lastvalues[b]);
									// if(lastvalues[b] == values[b]){continue;}
									// Log.i(G,"Sensor Orientation ["+b+"] "+lastvalues[b]+" to "+values[b]+" position("+position+") last("+lastposition+") valence "
									// + valence);
									lastvalues[b] = values[b];
									if (b == 0) {// left right roll
										// orbit
										for (int t = lathing + 1; t < mPurpose.length
												&& t < orbitlimit
												&& mPurpose[t] != null
												&& !mAnPause; t++) {
											mPurposeU[t] = values[b]
													* (t * t * -0.035f);
										}
										mPurposeU[mGLworkid] = (values[b]) * 0.8f;

										mPurposeU[1] = (values[b])
												* TOUCH_SCALE_FACTOR; // rotate

										// mPurpose[1].setAngle(values[b] *
										// TOUCH_SCALE_FACTOR);
										// mPurpose[3].setEt (
										// mPurpose[3].x+values[b]*15*TOUCH_SCALE_FACTOR
										// , mPurpose[3].y, mPurpose[3].z );
										// camera y(up down) x(left-right)
										// z(in-out)
										// mRenderer.mAngleX = values[b] *
										// TOUCH_SCALE_FACTOR;//mRenderer.mLAngleX-((mRenderer.mAngleX
										// <
										// values[b]?values[b]-mRenderer.mAngleX:mRenderer.mAngleX-values[b])*0.25f);
										// mRenderer.mCameraX =
										// values[b]*(5*5*-0.035f);
										// mWorlds[1].mPivotx +=
										// mPurposeU[3] = values[b] *
										// TOUCH_SCALE_FACTOR;
										// mBthing.setAngle(values[b]*0.15f);

									} else if (b == 1) {

										mPurposeU[0] = (values[b])
												* TOUCH_SCALE_FACTOR;

										// mPurpose[0].setAngle(values[b] *
										// TOUCH_SCALE_FACTOR);

										// mCurrentAngle = values[b]*0.15f;
										// mAthing.setAngle(mCurrentAngle);
										// for(int w=0; w < mWorlds.length &&
										// mWorlds[w] != null; w++){
										// }
										// mRenderer.mAngleY = valence * 36;
										// //(values[b]*26.5f)+mRAngleY;
										// mRenderer.mAngleY =
										// values[b]*TOUCH_SCALE_FACTOR;//
										// mRenderer.mLAngleY-((mRenderer.mAngleY
										// <
										// values[b]?values[b]-mRenderer.mAngleY:mRenderer.mAngleY-values[b])*0.25f);

									} else if (b == 2) {

										mPurposeU[2] = (values[b])
												* TOUCH_SCALE_FACTOR;

										// mPurpose[2].setAngle(values[b] *
										// TOUCH_SCALE_FACTOR);

										if (mAnPause) {
											// mRenderer.mAngleX =
											// (values[b]*-1.5f)+mRAngleX;
										}

										// mRenderer.mAngleX =
										// mRAngleX-(values[b]*-4.5f)-46;
										// mRenderer.mAngleY =
										// mRAngleY+(values[b]*-4.5f);

									}

								}
								// if(position != lastposition){
								// lastvalues = values;
								// }
								// }
							}
						}

						// smoothtext = SystemClock.uptimeMillis() + 1750;
						// String cn =
						// "("+event.sensor.getName()+"+"+values.length+")"+(int)values[0]+(values.length>0?"\n"+(int)values[1]:"")+""+(values.length>1?"\n"+(int)values[2]:"");//+""+(values.length>2?"\n"+(int)values[3]:"")+"";
						// {Message ml = new Message(); Bundle bl = new
						// Bundle(); bl.putInt("id",
						// bdl.getInt("morsv"));bl.putString("text",
						// cn);bl.putInt("color", Color.argb(200, 250, 250,
						// 255));ml.setData(bl);setText.sendMessageDelayed(ml,pRate);}
						// {Message ml = new Message(); Bundle bl = new
						// Bundle(); bl.putInt("id",
						// bdl.getInt("morsv"));bl.putString("text",
						// cn);bl.putInt("color", Color.argb(240, 250, 250,
						// 255));ml.setData(bl);setText.sendMessageDelayed(ml,pRate<100?pRate*4:(int)(pRate+(pRate/4)));}

					};
					{
						Message ml = new Message();
						Bundle bl = new Bundle();
						bl.putString("text", "Getting Sensor Provider");
						ml.setData(bl);
						logoly.sendMessage(ml);
					}

					SensorManager sm = null;
					try {
						sm = (SensorManager) mCtx
								.getSystemService(SENSOR_SERVICE);
					} finally {
					}

					{
						Message ml = new Message();
						Bundle bl = new Bundle();
						bl.putString("text", "Registering Sensor Service");
						ml.setData(bl);
						logoly.sendMessage(ml);
					}
					sm.registerListener(
							or,
							sm.getDefaultSensor(SensorManager.SENSOR_ORIENTATION),
							SensorManager.SENSOR_DELAY_GAME);
					{
						Message ml = new Message();
						Bundle bl = new Bundle();
						bl.putString("text", "Wave Sensor Service");
						ml.setData(bl);
						logoly.sendMessage(ml);
					}
					// easyStatus("Wave Ready");
					/*
					 * try {
					 * 
					 * //for(;;Thread.sleep(1750)){if(!mReg.contains("wave")){
					 * easyStatus("Wave Off");break;}if(getListView().isShown()
					 * || getListView().hasFocus()){}else{Log.e(G,
					 * "List isn't shown and nofocus, sensor watch close"
					 * );wayGo.sendEmptyMessage(2);break;}}
					 * }catch(InterruptedException e){Thread.interrupted();} //
					 */

				}
			};
			tx.start();
		}
	};

	OnTouchListener listtouch = new OnTouchListener() {
		public boolean onTouch(View v, MotionEvent ev) {

			int act = ev.getAction();
			boolean record = false;
			float x = ev.getX();
			float y = ev.getY();
			if (act == MotionEvent.ACTION_DOWN) {
				mp1x = x;
				mp1y = y;

				if (mAnPause) {
					mRelease = true;
				} else {
					mAnPause = true;
					record = true;
					Log.w(G, "GL View angle(y" + mRenderer.mAngleY + ",x"
							+ mRenderer.mAngleX + ",z" + mRenderer.mAngleZ
							+ ") camera(" + mRenderer.mLCameraY + ","
							+ mRenderer.mLCameraX + "," + mRenderer.mLCameraZ
							+ ")");
				}
			}

			else if (act == MotionEvent.ACTION_UP) {
				if (mLMove > SystemClock.uptimeMillis() - 800) {
					reani.sendEmptyMessageDelayed(1, 2000);
				}
				if (mRelease) {
					mAnPause = false;
					mRelease = false;
					record = true;
					Log.w(G, "GL View angle(y" + mRenderer.mAngleY + ",x"
							+ mRenderer.mAngleX + ",z" + mRenderer.mAngleZ
							+ ") camera(" + mRenderer.mLCameraY + ","
							+ mRenderer.mLCameraX + "," + mRenderer.mLCameraZ
							+ ")");
				}
			}

			if (mAnPause && act == MotionEvent.ACTION_DOWN) {
				// mRenderer.mAngleX = mRenderer.mLAngleX;
				// mRenderer.mAngleY = mRenderer.mLAngleY;
				// mRenderer.mAngleZ = mRenderer.mLAngleZ;
				// mRenderer.mCameraX = mRenderer.mLCameraX;
				// mRenderer.mCameraY = mRenderer.mLCameraY;
				// mRenderer.mCameraZ = mRenderer.mLCameraZ;

			}
			if (record) {
				mRecentY = mRenderer.mLCameraY;
				mRecentX = mRenderer.mLCameraX;
				mRecentZ = mRenderer.mLCameraZ;
				mRAngleY = mRenderer.mLAngleY;
				mRAngleX = mRenderer.mLAngleX;
				mRAngleZ = mRenderer.mLAngleZ;

				ContentValues ins = new ContentValues();
				ins.put("aux", mRenderer.mLAngleX);
				ins.put("auy", mRenderer.mLAngleY);
				ins.put("auz", mRenderer.mLAngleZ);
				ins.put("cux", mRenderer.mLCameraX);
				ins.put("cuz", mRenderer.mLCameraZ);
				ins.put("cuy", mRenderer.mLCameraY);
				Uri source = SqliteWrapper.insert(mCtx, getContentResolver(),
						Uri.parse("content://com.havenskys.wave/perspective"),
						ins);
				Log.i(TAG, "Created " + source.toString());
			}

			if (act == MotionEvent.ACTION_MOVE) {

				// if(mLMove > SystemClock.uptimeMillis()-200){
				// mHome = false;
				// mAtHome = false;
				// }

				float dx = mp1x - x;
				float ox = x > mp1x ? x - mp1x : mp1x - x;
				float dy = mp1y - y;
				float oy = y > mp1y ? y - mp1y : mp1y - y;

				if (mp1x < x) {
					dx++;
				}
				if (mp1y < y) {
					dy++;
				}

				if (oy > .01f && ox > .01f) {
					if (oy > 2.1f && ox > 2.1f) {
						mRenderer.mCameraZ = mRenderer.mLCameraZ + dy * -1.5f
								+ dx * -1.5f;
						// Log.w(G,"[dx("+dx+") ox("+ox+") "+mp1x+"->"+x+"]                      [dy("+dy+") oy("+oy+") "+mp1y+"->"+y+"]");
					} else if (oy > ox) {
						// mRenderer.mAngleZ = mRenderer.mLAngleZ + (float)(dy *
						// 1.5 );
						mRenderer.mAngleY = mRenderer.mLAngleY
								+ (float) (dy * 3.5);

						Log.w(G, "                      [dy(" + dy + ") oy("
								+ oy + ") " + mp1y + "->" + y + "]     c("
								+ mRenderer.mAngleY + ")");
					} else {
						mRenderer.mAngleX = mRenderer.mLAngleX
								+ (float) (dx * -4.5);
						// Log.w(G,"[dx("+dx+") ox("+ox+") "+mp1x+"->"+x+"]                      ");
					}

					if (ox > 1.5f || oy > 1.5f) {
						mLMove = SystemClock.uptimeMillis();
						mHome = false;
						mAtHome = false;
					}

				}

				mp1x = x;
				mp1y = y;

				// if( !mHome && mAtHome ){return true;}
				// if(mHome && (ox > 5f || oy > 5f) ) { mHome = false; mAtHome =
				// false; }else if(mHome){return true;}

				// if( mRelease && (ox > 5 || oy > 5) ){mRelease =
				// false;}else{return true;}

				// if( oy > ox && oy > .3f ){}

				/*
				 * else if(oy > ox){ float zd = (mRenderer.mLCameraZ <= -1?(-1 *
				 * mRenderer.mLCameraZ):mRenderer.mLCameraZ); mRenderer.mCameraX
				 * = mRenderer.mLCameraX + dy * TOUCH_SCALE_FACTOR * zd ==
				 * 0?1:zd; if(mRenderer.mLCameraX > 80){mRenderer.mCameraX =
				 * 80;} if(mRenderer.mLCameraX < -120){mRenderer.mCameraX =
				 * -120;} } else //
				 */
				// if(ox > oy && ox > .3f ){

				// }else{

				// }

				// if(ox > 3 || oy > 5 ) {return false;}else{return true;}
				// return false;
			}
			// */

			return false;

		}
	};

	// orbit
	// mRenderer.mAngleY = (float) ((int) mRenderer.mAngleY);
	// mRenderer.mAngleZ = (float) ((int) mRenderer.mAngleZ);
	// mRenderer.mAngleX = (float) ((int) mRenderer.mAngleX); mRenderer.mCameraY
	// = (float) ((int) mRenderer.mCameraY);
	// mRenderer.mCameraZ = (float) ((int) mRenderer.mCameraZ);
	// mRenderer.mCameraX = (float) ((int) mRenderer.mCameraX);

	// Log.w(G,"dx("+dx+") "+mPreviousX+"->"+x+" \t\t - dy("+dy+") "+mPreviousY+"->"+y+"");
	// if(mRenderer.mLCameraZ < -120f || mRenderer.mLCameraZ > 120f || mAtHome
	// ){mHome = false; mAtHome = true;mRenderer.mCameraZ =
	// m1Cz;mRenderer.mCameraX = m1Cx;mRenderer.mCameraY =
	// m1Cy;mRenderer.mAngleX = m1Ax;mRenderer.mAngleY = m1Ay;mRenderer.mAngleZ
	// = m1Az;}
	// if ( (mRenderer.mLAngleX > 65 && mRenderer.mLAngleX < 115) ||
	// (mRenderer.mLAngleX < 25 && mRenderer.mLAngleX > 335) ){ }
	// mRenderer.mCameraZ = mRenderer.mCameraZ + dy * TOUCH_SCALE_FACTOR * 1.12f
	// * (mRenderer.mLCameraZ <= -1?(-1 *
	// mRenderer.mLCameraZ):mRenderer.mLCameraZ);
	// mRenderer.mAngleY += dy * TOUCH_SCALE_FACTOR * -1 * (mPreviousX<x?1:-1);
	// mRenderer.mCameraX = mRenderer.mCameraX + dx * TOUCH_SCALE_FACTOR * (1 +
	// ( -0.02f * (mRenderer.mLCameraZ <= -1?(-1 *
	// mRenderer.mLCameraZ):mRenderer.mLCameraZ)));
	// Log.w(G,"touch " + mRenderer.mLAngleX + "("+dx+") " + mRenderer.mLAngleY
	// + "("+dy+")");
	// requestRender();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		mCtx = this;

		mGLSurfaceView = new GLSurfaceView(this);

		mWorlds[0] = mkWorld();
		mWorlds[1] = mkGLwork();
		mWorlds[2] = mkConsole();

		mRenderer = new InRenderer(this, mWorlds);
		// mRenderer.setAngle(mCurrentAngle);

		mGLSurfaceView.setGLWrapper(new GLSurfaceView.GLWrapper() {
			public GL wrap(GL gl) {
				return new MatrixTrackingGL(gl);
			}
		});
		// mGLSurfaceView.setRenderer(new SpriteTextRenderer(this));
		mGLSurfaceView.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
		mGLSurfaceView.getHolder().setFormat(PixelFormat.TRANSLUCENT);

		mRenderer.mLCameraY = mRecentY;
		mRenderer.mLCameraX = mRecentX;
		mRenderer.mLCameraZ = mRecentZ;
		mRenderer.mLAngleY = mRAngleY;
		mRenderer.mLAngleX = mRAngleX;
		mRenderer.mLAngleZ = mRAngleZ;

		mGLSurfaceView.setRenderer(mRenderer);

		setContentView(mGLSurfaceView);

		mGLSurfaceView.setFocusableInTouchMode(true);
		registerForContextMenu(mGLSurfaceView);
		mGLSurfaceView.setOnTouchListener(listtouch);

		// mGLView = new GLView(this);
		// mGLSurfaceView.setRenderer(new SpriteTextRenderer(this));
		// Data Storage
		// mDataStore = new DbAdapter(this);
		// mDataStore.loadDb();
		// setContentView(mGLView);
		// setContentView(R.layout.main);

		// mText = (TextView) findViewById(R.id.text);
		// mText.append("\n\nStart>");
		/*
		 * {Message msg = new Message();Bundle b = new
		 * Bundle();b.putString("message", "Mode " +
		 * mode);msg.setData(b);mToastHandler.sendMessage(msg);} //
		 */

		{
			SensorService.sendEmptyMessageDelayed(2, pRate);
		}

		Thread t = new Thread() {
			public void run() {

				Log.w(TAG, "Thread Sync Processing");
				// SyncProcessing syncProcess = new
				// SyncProcessing(getApplicationContext());
				// Log.w(TAG,"reviewRecent()");
				// syncProcess.setToastHandler(mToastHandler);
				// syncProcess.owaCalendar();

				// syncProcess.verifyCalendar();
				// syncProcess.verifyEvent();
				// syncProcess.androidDataPrint("content://calendar/calendars");
				// syncProcess.androidDataPrint("content://contacts/people","number_key is not null AND last_time_contacted > "
				// + ( (System.currentTimeMillis() - (90*24*60*60*1000))/1000)
				// );
				// syncProcess.androidDataPrint("content://calendar/events","calendar_id != 2");
				// syncProcess.androidDataPrint("content://calendar/attendees","calendar_id != 2");
				// syncProcess.androidDataPrint("content://calendar/reminders");
				// syncProcess.androidDataPrint("content://calendar/calendar_alerts");
				// syncProcess.androidDataPrint("content://contacts/organizations");

			}
		};
		t.start();

	}

	@Override
	public boolean onTrackballEvent(MotionEvent e) {

		if (mSelected > -1 && mPurpose[mSelected] != null) {

			mPurpose[mSelected].setAngle(InPurpose.kAxisX, e.getX() * 45);
		} else {

			mRenderer.mAngleX += e.getX() * TRACKBALL_SCALE_FACTOR * 1.2f;// *
																			// (mRenderer.mLCameraZ
																			// <=
																			// -1?(-1
																			// *
																			// mRenderer.mLCameraZ):mRenderer.mLCameraZ);
		}

		// Log.w(G,"trackball " + mRenderer.mLCameraX + "("+(e.getX() *
		// TRACKBALL_SCALE_FACTOR)+") " + mRenderer.mLCameraY + "("+(e.getY() *
		// TRACKBALL_SCALE_FACTOR)+")");
		// mRenderer.mAngleY += e.getY() * TRACKBALL_SCALE_FACTOR * -1.2f;// *
		// (mRenderer.mLCameraZ <= -1?(-1 *
		// mRenderer.mLCameraZ):mRenderer.mLCameraZ);
		// requestRender();
		// reani.sendEmptyMessageDelayed(1,pRate);
		return true;
	}

	// orbit

	Handler reani = new Handler() {
		public void handleMessage(Message msg) {
			mAnPause = false;
		}
	};

	@Override
	protected void onPause() {
		super.onPause();
		mGLSurfaceView.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		mGLSurfaceView.onResume();
	}

	private Handler logoly = new Handler() {
		public void handleMessage(Message msg) {
			Bundle bx = msg.getData();
			int l = bx.getInt("l");
			String text = bx.getString("text");
			switch (l) {
			case 2:
				Log.e(G, ":" + text);
				break;
			case 3:
				Log.w(G, ":" + text);
				break;
			default:
				Log.i(G, ":" + text);
				break;
			}
		}
	};

	public void animate() {
		// change our angle of view

		// mRenderer.setAngle(mRenderer.getAngle() - 3.3f);
		// mRenderer.setAngle(mCurrentAngle);

		// mWorlds[2].mPivotz = mRenderer.mLCameraZ;
		// mWorlds[2].mPivoty = mRenderer.mLCameraY;
		// mWorlds[2].mPivotx = mRenderer.mLCameraX;

		if (mHome && !mAtHome && mRenderer.mLCameraZ == m1Cz
				&& mRenderer.mLCameraX == m1Cx && mRenderer.mLCameraY == m1Cy
				&& mRenderer.mAngleX == m1Ax && mRenderer.mAngleY == m1Ay
				&& mRenderer.mAngleZ == m1Az) {
			{
				Message msg = new Message();
				Bundle b = new Bundle();
				b.putString("message", "Home");
				msg.setData(b);
				mToastHandler.sendMessage(msg);
			}
			mHome = false;
			mAtHome = true;
		}

		if ((mRenderer.mLCameraZ < -3250f || mRenderer.mLCameraZ > 3250f
				|| mHome || mAtHome)
				&& !(mRenderer.mLCameraZ == m1Cz && mRenderer.mLCameraX == m1Cx && mRenderer.mLCameraY == m1Cy)) {
			mHome = true;
			mRenderer.mCameraZ = m1Cz;
			mRenderer.mCameraX = m1Cx;
			mRenderer.mCameraY = m1Cy;
			mRenderer.mAngleX = m1Ax;
			mRenderer.mAngleY = m1Ay;
			mRenderer.mAngleZ = m1Az;
		}

		/*
		 * if(mRenderer.mLCameraZ < 0){ if(mRenderer.mLAngleX < 180 ){
		 * mRenderer.mAngleX = mRenderer.mLAngleX + 180; } }else{
		 * 
		 * if(mRenderer.mLAngleX > 180){ mRenderer.mAngleX = mRenderer.mLAngleX
		 * - 180; }
		 * 
		 * }//
		 */

		// orbit

		int t, j, i, k;
		float incm = 0;
		float it = 0;
		float od = 0f;
		for (t = 0; t < mPurpose.length; t++) {
			if (mPurpose[t] != null) {

				if (t == console) {
					// Log.w(G,"camera-console (x"+mPurpose[t].x+",y"+mPurpose[t].y+",z"+mPurpose[t].z+") p(x"+mWorlds[2].mPivotx+",y"+mWorlds[2].mPivoty+",z"+mWorlds[2].mPivotz+") ");
					// mWorlds[2].mTrimx = mRenderer.mLCameraX;
					// mWorlds[2].mTrimy = mRenderer.mLCameraY;
					// mWorlds[2].mTrimz = mRenderer.mLCameraZ-5;
					// mPurpose[console].setEt(mRenderer.mLCameraX-1f,
					// mRenderer.mLCameraY-1f, (mRenderer.mLCameraZ-1f));
					// mPurposeU[console] = (mRenderer.mAngleY-180);
					// mPurpose[console].setAngle(mRenderer.mAngleX-180);
				}

				if (mAnPause && t > lathing) {
					mPurposeU[t] = mPurpose[t].mAngle;
					continue;
				} else {
					if (mPurposeU[t] < 0 && mPurposeU[t] > -1) {
						mPurposeU[t] = 0;
					}
					if (mPurposeU[t] != mPurpose[t].mAngle) {

						// normalize float size to two places
						if (!mPurpose[t].mAnimating) {
							mPurpose[t].startAnimation();
							int h4 = (int) (mPurposeU[t] * 1000f);
							mPurposeU[t] = (float) h4 * 0.001f;
						}

						od = (mPurposeU[t] > mPurpose[t].mAngle ? (mPurposeU[t] - mPurpose[t].mAngle)
								: (mPurpose[t].mAngle - mPurposeU[t]));// (mPurpose[t].mAngle
																		// *(float)
																		// Math.PI/2f);
						incm = od * 0.01f;

						if (incm > 0.05) {
							// incm = 0.05f;
							if (od < 0.2f) {
								incm = 0.2f;
							}
							// if(od < 0.3f){incm *= 1.1f;}
							// if(od >= 10f){incm *= 1.1f;}
							// if(od >= 100f){incm *= 1.1f;}
							// else if(od < 10f){incm *= 0.14f;}
							// else if(od < 100f){incm *= 1.2f;}
							// else if(od < 500f){incm *= 0.08f;}

							if (mPurpose[t].mAngle == mPurposeU[t]) {
								it = mPurpose[t].mAngle;
								it = mPurposeU[t];
							} else if (mPurpose[t].mAngle < mPurposeU[t]) {
								it = mPurpose[t].mAngle + incm;
							} else {
								it = mPurpose[t].mAngle - incm;
								// if(mPurposeU[t] < 0.02f){mPurposeU[t] =
								// 0.02f;}
							}
							// if(it < 0.01f && mPurposeU[t] < 0.01f){
							// it = 0f; mPurposeU[t] = 0f;
							// }
							// if(it < 0 && mPurpose[t].mAngle > 1){

							// }

							if (it >= mPurposeU[t] - incm
									&& it <= mPurposeU[t] + incm) {
								mPurposeU[t] = mPurpose[t].mAngle;

								mPurpose[t].setAngle(mA[t], mPurpose[t].mAngle);// mPurpose[t].mAngle);
								mPurpose[t].endAnimation();
								// mPurpose[t].mAnimating = false;
							} else {
								// if(t > orbitlimit){
								// Log.w(G,"animate() "+t+" od("+od+")" + incm +
								// "+" + mPurpose[t].mAngle + " in "+it+" to " +
								// mPurposeU[t]);
								// }
								// if(orbitlimit > t){
								// Log.w(G,"od("+od+") incm("+incm+") ["+mPurpose[t].mAngle+"    ^"+(mPurpose[t].mAngle>mPurposeU[t]?mPurpose[t].mAngle-mPurposeU[t]:mPurposeU[t]-mPurpose[t].mAngle)+"^       "+mPurposeU[t]+"]");
								// }
								mPurpose[t].setAngle(mA[t], it);

							}
							// InPart[] ground;for(i = 0, k=0; i <
							// mPart[t].length && mPart[t][i] != null;i++)
							// {ground = mPurpose[t].mShapes;ground[k++] =
							// mPart[t][i];}
							continue;
						}

					}
				}
				// Log.w(G,"animate() "+t+" " + mPurpose[t].mAngle + " in " +
				// mPurposeU[t]);

				mPurposeU[t] = mPurpose[t].mAngle;

			} else {
				break;
			}
		}
		updateInPurpose();

		// */

		// mCurrentAngle += mAngleIncrement;
		// mCurrentThing.setAngle(mCurrentAngle);

	}

	private Handler mToastHandler = new Handler() {
		public void handleMessage(Message msg) {
			// Log.i(TAG,"mToastHandler()");
			Bundle b = msg.getData();
			String message = b.getString("message");
			Toast.makeText(In.this, message, Toast.LENGTH_SHORT).show();
		}
		/*
		 * Message msg = new Message(); Bundle b = new Bundle();
		 * b.putString("message", "Mode " + mode); msg.setData(b);
		 * mToastHandler.sendMessage(msg); //
		 */

	};

	private Handler mHostAction = new Handler() {
		public void handleMessage(Message msg) {
			// Log.i(TAG,"mToastHandler()");
			Bundle b = msg.getData();
			String message = b.getString("message");
			Toast.makeText(In.this, message, Toast.LENGTH_SHORT).show();
		}
		/*
		 * Message msg = new Message(); Bundle b = new Bundle();
		 * b.putString("message", "Mode " + mode); msg.setData(b);
		 * mHostAction.sendMessage(msg); //
		 */

	};

	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, 1, 4, "Make");
		// menu.add(0, MENU_DELETE, 0, R.string.menu_delete);
		// menu.add(0, 2, 0, "Things");
		SubMenu tx = menu.addSubMenu(0, 2, 2, "Things");
		// tx.add(2,801,0,"alpha");
		for (int t = 0; t < mWorlds.length && mWorlds[t] != null; t++) {
			tx.add(2, 800 + t, t, "" + t);
		}

		menu.add(0, 3, 0, "What");

		// menu.add(0, MENU_EDIT, 0, R.string.menu_edit);
		return true;
	}

	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case 1: {
			Message ml = new Message();
			Bundle bl = new Bundle();
			bl.putString("message", "Make");
			ml.setData(bl);
			mToastHandler.sendMessage(ml);
		}
			return true;
		case 2: {
			Message ml = new Message();
			Bundle bl = new Bundle();
			bl.putString("message", "Things");
			ml.setData(bl);
			mToastHandler.sendMessage(ml);
		}
			return true;
		case 3: {
			Message ml = new Message();
			Bundle bl = new Bundle();
			bl.putString("message", "What");
			ml.setData(bl);
			mToastHandler.sendMessage(ml);
		}
			Intent d2 = new Intent(mCtx, com.havenskys.galaxy.Motion.class);
			// d2.putExtra("code413", true);
			// d2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			// d2.addFlags(d2.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
			startActivity(d2);
			return true;
		}

		if (item.getItemId() >= 800 && item.getItemId() < 1900) {
			selectWorld(item.getItemId() - 800);
			return true;
		}

		return super.onMenuItemSelected(featureId, item);
	}

	public void selectWorld(int t) {

		mSelected = t;

		float mx = mWorlds[t].mPivotx;
		float my = mWorlds[t].mPivoty;
		float mz = mWorlds[t].mPivotz;

		float tx = mWorlds[t].mTrimx;
		float ty = mWorlds[t].mTrimy;
		float tz = mWorlds[t].mTrimz;

		// {Message ml= new Message(); Bundle bl = new Bundle();
		// bl.putString("message",""+t+" ("+mx+","+my+","+mz+") ("+tx+","+ty+","+tz+")");
		// ml.setData(bl); mToastHandler.sendMessage(ml);}

		setCamera(my, mz, mx - 35, 3000);

	}

	public void setCamera(float x, float y, float z, int msec) {

		{
			Message ml = new Message();
			Bundle bl = new Bundle();
			bl.putFloat("x", x);
			bl.putFloat("y", y);
			bl.putFloat("z", z);
			ml.setData(bl);
			mCamera.sendMessageDelayed(ml, msec);
		}

	}

	Handler mCamera = new Handler() {
		public void handleMessage(Message msg) {
			Bundle b = msg.getData();

			mHome = false;
			mAtHome = false;

			float x = b.getFloat("x");
			float y = b.getFloat("y");
			float z = b.getFloat("z");

			mRenderer.mCameraX = x;
			mRenderer.mCameraY = y;
			mRenderer.mCameraZ = z;
			mRenderer.mAngleX = m1Ax;
			mRenderer.mAngleY = m1Ay;
			mRenderer.mAngleZ = m1Az;

		}
	};

	// Long Press
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);

		if (mLMove > SystemClock.uptimeMillis() - 1200) {
			return;
		}

		{
			SubMenu cx = menu.addSubMenu(0, 4, 0, "");// +mPreviousX+","+mPreviousY);
			{
				cx.add(4, 3000, 0, "Camera X " + mRenderer.mCameraX);
			}// s.add(3000,3100,0,((int)(mRenderer.mCameraX)+10)+"");s.add(3000,3100,1,((int)(mRenderer.mCameraX)-10)+"");s.add(3000,3100,0,""+0);s.add(3000,3100,2,m1Cx+"");}
			{
				cx.add(4, 3001, 0, "Camera Y " + mRenderer.mCameraY);
			}// s.add(3000,3101,0,((int)(mRenderer.mCameraY)+10)+"");s.add(3000,3101,1,((int)(mRenderer.mCameraY)-10)+"");s.add(3000,3101,0,""+0);s.add(3000,3101,2,m1Cy+"");}
			{
				cx.add(4, 3002, 0, "Camera Z " + mRenderer.mCameraZ);
			}// s.add(3000,3102,0,((int)(mRenderer.mCameraZ)+10)+"");s.add(3000,3102,1,((int)(mRenderer.mCameraZ)-10)+"");s.add(3000,3102,0,""+0);s.add(3000,3102,2,m1Cz+"");}

			{
				cx.add(4, 3010, 0, "Angle X " + mRenderer.mAngleX);
			}
			{
				cx.add(4, 3011, 0, "Angle Y " + mRenderer.mAngleY);
			}
			{
				cx.add(4, 3012, 0, "Angle Z " + mRenderer.mAngleZ);
			}
		}

		{
			SubMenu mm = menu.addSubMenu(0, 1, 4, "Make");
			mm.clearHeader();
			mm.add(1, 2000, 0, "Build");
			mm.add(1, 2001, 1, "SQL");
			mm.add(1, 2002, 2, "Algorithm");
		}

		{
			SubMenu tx = menu.addSubMenu(0, 2, 2, "Things");
			tx.clearHeader();
			for (int t = 0; t < mWorlds.length && mWorlds[t] != null; t++) {
				tx.add(2, 800 + t, t, "" + t);
			}
		}

		menu.add(0, 3, 1, "What");

		if (mSelected > -1) {
			int g = mSelected;
			SubMenu mx = menu.addSubMenu(0, 5, 8, "" + mSelected + "");
			mx.add(5, 4000, 0, "Pivot X " + mWorlds[g].mPivotx);
			mx.add(5, 4001, 1, "Pivot Y " + mWorlds[g].mPivoty);
			mx.add(5, 4002, 2, "Pivot Z " + mWorlds[g].mPivotz);

			mx.add(5, 4010, 3, "Trim X " + mWorlds[g].mTrimx);
			mx.add(5, 4001, 4, "Trim Y " + mWorlds[g].mTrimy);
			mx.add(5, 4002, 5, "Trim Z " + mWorlds[g].mTrimz);

		} else {
			SubMenu mx = menu.addSubMenu(0, 5, 8, " ");
			mx.add(5, 800, 0, "Thing 0");

		}

		// menu.add(0, 4, 0, "List Unresolved from Remote");
		// SubMenu sz = menu.addSubMenu(0, 6, 0, " ");
		// sz.add(6, 6000, 0, "Add SQL");
		// sz.add(6, 6001, 0, "");
		// menu.add(0, 2, 0, "Things");
	}

	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 1: // alpha Long Press
		{
			Message ml = new Message();
			Bundle bl = new Bundle();
			bl.putString("message", "Make");
			ml.setData(bl);
			mToastHandler.sendMessage(ml);
		}
			// AdapterContextMenuInfo info = (AdapterContextMenuInfo)
			// item.getMenuInfo();
			{
				Intent i = new Intent(mCtx, com.havenskys.galaxy.Motion.class);
				startActivity(i);
			}
			return true;
		case 2: {
			Message ml = new Message();
			Bundle bl = new Bundle();
			bl.putString("message", "Things");
			ml.setData(bl);
			mToastHandler.sendMessage(ml);
		}
			// AdapterContextMenuInfo info2 = (AdapterContextMenuInfo)
			// item.getMenuInfo();
			// mDbHelper.deleteEntry(info2.id);
			// loadList();
			// { Message msg = new Message();
			// Bundle b2 = new Bundle();
			// b2.putString("message", "Clearing Cache Data");
			// msg.setData(b2);
			// mToastHandler.sendMessage(msg);}
			// mDataStore.updateEntry("syncContent", "status", 0,
			// "contentType=\"owa_calendar\"");
			return true;
		case 3: {
			Message ml = new Message();
			Bundle bl = new Bundle();
			bl.putString("message", "Now");
			ml.setData(bl);
			mToastHandler.sendMessage(ml);
		}
			{
				Intent i = new Intent(mCtx, com.havenskys.galaxy.Motion.class);
				startActivity(i);
			}
			// AdapterContextMenuInfo info3 = (AdapterContextMenuInfo)
			// item.getMenuInfo();
			// Intent i2 = new Intent(this, DialogList.class);
			// i2.putExtra("mode", 1);
			// startActivityForResult(i2, 1);
			return true;
		case 4:
			// AdapterContextMenuInfo info3 = (AdapterContextMenuInfo)
			// item.getMenuInfo();
			// Intent i3 = new Intent(this, DialogList.class);
			// i3.putExtra("mode", 2);
			// startActivityForResult(i3, 1);
			return true;
		}

		if (item.getItemId() >= 800 && item.getItemId() < 1900) {
			selectWorld(item.getItemId() - 800);
			return true;
		}
		if (item.getItemId() >= 3000 && item.getItemId() < 4000) {

			{
				Message ml = new Message();
				Bundle bl = new Bundle();
				bl.putString("message", "" + item.getTitle());
				ml.setData(bl);
				mToastHandler.sendMessage(ml);
			}
			return true;
		}

		return super.onContextItemSelected(item);

	}

	int lastposition = 0;
	int pRate = 73;

	Long mLMove = SystemClock.uptimeMillis();
	private boolean mHome = true;
	private boolean mAtHome = false;
	private final float TOUCH_SCALE_FACTOR = 180.0f / 320;
	private final float TRACKBALL_SCALE_FACTOR = 42.0f;
	private final int GRAPH_SIZE = 10;

	// angle(y0.0,x-369.0,z0.0) camera(7.0,-2.0,-81.0)
	// angle(y-29.400002,x180.0,z0.0) camera(-1.4,-13.6,-87.7
	// angle(y177.0,x1.36,z0.0) camera(3.3991036,-0.72494906,-10.051575)
	// angle(y177.98003,x1.36,z0.0) camera(14.520627,-15.449471,41.601036)
	// angle(y60.375317,x1.3610773,z0.0) camera(-4.6,0.0,-31.969954)
	float mRAngleY = 0f;
	float mRAngleX = 50f;
	float mRAngleZ = 0;
	float mRecentY = 50f;
	float mRecentX = 0f;
	float mRecentZ = -150f;
	float m1Ay = 28f;
	float m1Ax = -148f;
	float m1Az = 0;
	float m1Cy = 0f;
	float m1Cx = 0f;
	float m1Cz = -35f;
	boolean mAnPause = false;
	boolean mRelease = false;
	float mp1x = 0f;
	float mp1y = 0f;

	private static String TAG = "In";
	private static String G = "In";
	private TextView mText;
	// private DbAdapter mDataStore;
	// private GLView mGLView;
	private Context mCtx;
	private GLSurfaceView mGLSurfaceView;
	private InRenderer mRenderer;

	InPurpose[] mPurpose = new InPurpose[201];
	float[] mPurposeU = new float[201];
	InPartShape[][] mPart = new InPartShape[201][201];
	World[] mWorlds = new World[2001];
	int[] mA = new int[2001];

	int one = 0x10000;
	int half = 0x08000;
	GLColor black = new GLColor(0, 0, 0);
	GLColor red = new GLColor(one, 0, 0);
	GLColor green = new GLColor(0, one, 0);
	GLColor blue = new GLColor(0, 0, one);
	GLColor yellow = new GLColor(one, one, 0);
	GLColor orange = new GLColor(one, half, 0);
	GLColor white = new GLColor(one, one, one);

	int lathing = 0;
	public int orbitlimit = 1;
	int console = 1;
	int mSelected = -1;

}
