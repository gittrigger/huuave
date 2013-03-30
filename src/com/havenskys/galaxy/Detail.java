package com.havenskys.galaxy;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.RedirectHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import com.havenskys.galaxy.R;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ProviderInfo;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.TextUtils;
import android.text.method.TransformationMethod;
import android.text.util.Linkify;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SubMenu;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ImageView.ScaleType;

public class Detail extends Activity {

	private static String G = "Detail";
	
	private LinearLayout mContent; LinearLayout mR;
	//private TextView mTitle, mDate;
	//private LinearLayout mLinearLayout;
	private String mLink;
	//private String mLookup;
	//private SharedPreferences mReg;
	//private Editor mEdt;
	private Context mCtx;

	int coloroff = Color.argb(205, 70, 70, 148);
	int coloron = Color.argb(205, 40, 40, 98);
	int colorred = Color.argb(205,48,0,0);
	int textcolor = Color.argb(255,205,200,120);
	int colorm2 = Color.argb(205,75,10,50);
	int colorm1 = Color.argb(205, 75, 55, 20);
	int colorm3 = Color.argb(205, 120, 0, 10);
	int colorpageon = Color.argb(205, 200, 20, 40);
	int colorpagein = Color.argb(205, 120, 0, 10);
	int colorpageoff = Color.argb(205, 0, 0, 10);
	int colorbase = Color.argb(205,0,0,10);
	Uri geturi = null;
	int wHeight = 2; int wWidth = 2;
	@Override
	protected void onResume() {
	super.onResume();
		
	wHeight = getWindowManager().getDefaultDisplay().getHeight();
	wWidth = getWindowManager().getDefaultDisplay().getWidth();
	Log.w(G,"Resume " + wWidth + "x" + wHeight);
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//mLog = new Custom(this);
		mCtx = this;
		wHeight = getWindowManager().getDefaultDisplay().getHeight();
		wWidth = getWindowManager().getDefaultDisplay().getWidth();
		
		setContentView(R.layout.space);
		Bundle bdl = getIntent().getExtras();
		geturi = Uri.parse(bdl.getString("uri"));
		Log.w(G,"Showing URI " + geturi.toString());
		{s01.sendEmptyMessage(2);}
	}
	
	ScrollView s; LinearLayout tlh, clh, l; RelativeLayout r, b; EditText ed;
	Handler s01 = new Handler(){
		public void handleMessage(Message msg){
		//Bundle bdl = msg.getData();
		
		s = (ScrollView) findViewById(R.id.scroller);
		s.setBackgroundColor(Color.argb(190, 0, 0, 0));
		r = (RelativeLayout) findViewById(R.id.base);
		b = (RelativeLayout) findViewById(R.id.body);
		//r.setBackgroundColor(Color.argb(150, 201, 224, 219));

		l = new LinearLayout(mCtx);
		RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(-1,-2);
		l.setId((int)SystemClock.uptimeMillis());
		l.setLayoutParams(rlp);
		l.setOrientation(LinearLayout.VERTICAL);
		r.addView(l);
		
		ImageView gr = (ImageView) findViewById(R.id.ground);
		gr.setClickable(true);gr.setFocusableInTouchMode(true);
		gr.setOnTouchListener(new OnTouchListener(){public boolean onTouch(View v, MotionEvent ev) {
			Log.w(G,"Motion " + ev.getAction() );
			if(ev.getAction() == MotionEvent.ACTION_MOVE && ev.getHistorySize() > 1){
				float x = ev.getX();
				float lx = ev.getHistoricalX(1);
				int d = (int) ((lx-x)*2);
				ImageView iv = (ImageView) v;iv.setPadding(0,d,0,0);
			}
			
			return false;}});
			
		// TITLE
		tlh = new LinearLayout(mCtx);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1,-2);
		tlh.setId(l.getId()+1);
		tlh.setLayoutParams(lp);
		tlh.setOrientation(LinearLayout.HORIZONTAL);
		l.addView(tlh);
		
		
		// COLUMNS
		clh = new LinearLayout(mCtx);
		clh.setId(tlh.getId()+1);
		clh.setLayoutParams(lp);
		clh.setOrientation(LinearLayout.VERTICAL);
		l.addView(clh);
		
		
		//mContent.setVisibility(View.INVISIBLE);
		//mHideData.sendEmptyMessageDelayed(1, 10);
		
		//mReg = getSharedPreferences("Preferences", MODE_WORLD_WRITEABLE);
        //mEdt = mReg.edit();
        //String url = mReg.getString("url","");
        //mEdt.putLong("id", id); mEdt.commit();


		
	
	
		Log.w(G,"loadRecord uri("+geturi+")");
		{Message ml = new Message(); Bundle bl = new Bundle(); bl.putString("uri", geturi.toString()); ml.setData(bl); loadRecord.sendMessage(ml); }
		
		}
	};


	
	
	private Handler setFocusOn = new Handler(){public void handleMessage(Message msg){
		Bundle bl = msg.getData();int id = bl.getInt("id");
		TextView v = (TextView) findViewById(id); if(v!=null){ v.requestFocusFromTouch();}
	}};
	private Handler setFocusOnR = new Handler(){public void handleMessage(Message msg){
		Bundle bl = msg.getData();int id = bl.getInt("id");
		RelativeLayout v = (RelativeLayout) findViewById(id); if(v!=null){ v.requestFocusFromTouch();}
	}};
	private Handler editReady = new Handler(){
		public void handleMessage(Message msg){
			Bundle bdl = msg.getData();
			int cv = bdl.getInt("cid");
			int v = bdl.getInt("id");
			String column = bdl.getString("column");
			String content = bdl.getString("content");
		
			//Toast.makeText(mCtx, "edit " + column, 1800).show();
		
			
			
			if(cv > 0){
			
				RelativeLayout rl = (RelativeLayout) findViewById(v);
				if(rl.getVisibility() == View.GONE){
					RelativeLayout r1 = (RelativeLayout) findViewById(cv);
					if(r1.getChildCount() > 5){
						ImageView i1 = (ImageView) r1.getChildAt(4);i1.setVisibility(View.INVISIBLE);
						LinearLayout i2 = (LinearLayout) r1.getChildAt(5);i2.setVisibility(View.INVISIBLE);
						rl.setVisibility(View.VISIBLE);
						Log.w(G,"rl " + rl.getChildCount() );
					

						
						
					}
				}else{
					RelativeLayout r1 = (RelativeLayout) findViewById(cv);
					if(r1.getChildCount() > 5){
						ImageView i1 = (ImageView) r1.getChildAt(4);i1.setVisibility(View.VISIBLE);
						LinearLayout i2 = (LinearLayout) r1.getChildAt(5);i2.setVisibility(View.VISIBLE);
						rl.setVisibility(View.GONE);
					}
				}
			}
			
			
			if(column.length() == 0){//not editable
				
			}
			
			
		}
	};
	
	/*
	private Handler mHideData = new Handler(){
		public void handleMessage(Message msg){
			Bundle bdl = msg.getData();
			//String html = bdl.containsKey("html") ? bdl.getString("html") : "";
			//mContent.loadData(html, "text/html", "UTF-8");
			mContent.setVisibility(View.INVISIBLE);
		}
	};//*/

   // private void loadRecord(long id) {
    	int mTextadd = 2;
    	private Handler loadRecord = new Handler(){
    		public void handleMessage(Message msg){
    			Bundle bdl = msg.getData();
    			//Uri geturi = Uri.parse(bdl.getString("uri"));
    			final long moment = bdl.getLong("moment");
    	
				
		//
		// CUSOMIZED		
		//		
		Cursor lCursor = SqliteWrapper.query(mCtx, mCtx.getContentResolver(), geturi, //Uri.withAppendedPath(DataProvider.CONTENT_URI,"moment"), 
        		//new String[] { "_id", "address", "body", "strftime(\"%Y-%m-%d %H:%M:%S\", date, \"unixepoch\", \"localtime\") as date" },
        		//strftime("%Y-%m-%d %H:%M:%S"
        		new String[] {"*"},
				//new String[] { "_id", "address", "body", "date" },
        		"_id > -1000",
        		null, 
        		null);
		// EC
		
		if( lCursor != null ){
			startManagingCursor(lCursor);
			if ( lCursor.moveToFirst() ){
				long unq = SystemClock.uptimeMillis();
				Log.w(G,"Queried nothing and got columns " + lCursor.getColumnCount());
				String[] cn = lCursor.getColumnNames();
				String allvalues = "";
				int fluff = 0;
				for(int ci = 0; ci < cn.length; ci++){
					String value = lCursor.getString(ci);
					allvalues += cn[ci]+"\n"+ value +"\n\n";
					fluff += cn[ci].length() +3;
				}

				{
				RelativeLayout cr = new RelativeLayout(mCtx);
				LinearLayout.LayoutParams lcr = new LinearLayout.LayoutParams(-1,86);
				
				{TextView name = new TextView(mCtx);
				RelativeLayout.LayoutParams rl = new RelativeLayout.LayoutParams(-2,-2);
				name.setId((int)unq++);
				name.setLayoutParams(rl);
				name.setText(geturi.toString()+"\n" + (allvalues.length() - fluff) + " bytes\n");
				
				/*
				
				List<PackageInfo> lpi = mCtx.getPackageManager().getInstalledPackages(android.content.pm.PackageManager.GET_META_DATA | android.content.pm.PackageManager.GET_UNINSTALLED_PACKAGES | PackageManager.GET_ACTIVITIES | android.content.pm.PackageManager.GET_INTENT_FILTERS | android.content.pm.PackageManager.GET_PROVIDERS);
				Log.i(G,"installed applications " + lpi.size() );
				for(int ui = 0; ui < lpi.size(); ui++){
					PackageInfo b = lpi.get(ui);
					String label = "";
					//try{
						
						//PackageInfo b = null;
						//try {b = getPackageManager().getPackageInfo(ba.packageName, PackageManager.GET_ACTIVITIES | PackageManager.GET_RESOLVED_FILTER | PackageManager.GET_PROVIDERS );} catch (NameNotFoundException e) {e.printStackTrace();}
						label = (String) getPackageManager().getApplicationLabel(b.applicationInfo);
						//Intent jump = getPackageManager().getLaunchIntentForPackage(b.packageName);
						//label = getResources().getString(b.applicationInfo.labelRes);
					//}catch(Resources.NotFoundException rnf){label = b.packageName;}
					//label = getResources().getString(b.sharedUserLabel);
					Log.w(G,"application " + b.packageName + "\t " + b.versionName + "\t " + b.applicationInfo.icon + "\t " + label );
					
					
					
					try{
					if(b.activities != null){
					ActivityInfo[] ai = b.activities; 
					if(ai != null){
					for(int iai = 0; iai < ai.length; iai++){
						//name.append(ai[iai].name + " " + ai[iai].targetActivity + " " + ai[iai].processName + "\n");
					
						//ActivityInfo x = mCtx.getPackageManager().getActivityInfo(ComponentName.unflattenFromString(ai[iai].name), PackageManager.GET_INTENT_FILTERS);
						Log.w(G,"\ta\t"+ai[iai].name + " " + x.name + " " + x.processName + " " + x.targetActivity + " " + x.permission);
					}
					}
					}
					
					
					if( b.providers != null){
					ProviderInfo[] ai2 = b.providers;
					if(ai2 != null){
					for(int iai = 0; iai < ai2.length; iai++){
						//name.append(ai2[iai].name + " " + ai2[iai].targetActivity + " " + ai[iai].processName + "\n");
						Log.w(G,"\tp\t"+ai2[iai].name + " " + ai2[iai].authority);
					}
					}
					}
					
				}catch (NullPointerException cee){} catch (NameNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				}
					
				PackageInfo packageInfo = null;
				try {
					packageInfo = mCtx.getPackageManager().getPackageInfo("com.havenskys.galaxy", PackageManager.GET_ACTIVITIES | android.content.pm.PackageManager.GET_INTENT_FILTERS | android.content.pm.PackageManager.GET_PROVIDERS);
				} catch (NameNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				ActivityInfo[] ai = packageInfo.activities;
				for(int iai = 0; iai < ai.length; iai++){
					name.append(ai[iai].name + " " + ai[iai].targetActivity + " " + ai[iai].processName + "\n");
					Log.w(G,ai[iai].name + " " + ai[iai].targetActivity + " " + ai[iai].processName);
				}
				
				
				//*/
				
				
				name.setEllipsize(TextUtils.TruncateAt.MIDDLE);
				name.setPadding(15,7,15,7);
				name.setMaxLines(2);
				name.setClickable(false);
				name.setTextSize((float)14);
				name.setTextColor(textcolor);
				
				EditText et = new EditText(mCtx);
				et.setId((int)unq++);
				et.setLayoutParams(rl);
				et.setText(allvalues);
				et.setVisibility(View.GONE);
				final int ed1 = et.getId();
				
				lcr.setMargins(0,1,0,0);
				cr.setId((int)unq++);
				cr.setLayoutParams(lcr);
				cr.setBackgroundColor(colorm1);
				cr.setFocusable(true);
				cr.setClickable(true);
				cr.setOnFocusChangeListener(new OnFocusChangeListener(){public void onFocusChange(View v, boolean has){if(has){v.setBackgroundColor(coloron);}else{v.setBackgroundColor(colorm1);}}});
				//cr.setOnTouchListener(new OnTouchListener(){public boolean onTouch(View v, MotionEvent ev) {if(ev.getAction() == MotionEvent.ACTION_DOWN){RelativeLayout rf = (RelativeLayout) v;rf.setBackgroundColor(coloron);}else if(ev.getAction() == MotionEvent.ACTION_UP){RelativeLayout rf = (RelativeLayout) v;rf.setBackgroundColor(coloroff);}else if(ev.getAction() == MotionEvent.ACTION_OUTSIDE || ev.getAction() == MotionEvent.ACTION_CANCEL){RelativeLayout rf = (RelativeLayout) v;rf.setBackgroundColor(colorred);}return false;}});
				final String allvaluesc = allvalues;
				cr.setOnClickListener(new OnClickListener(){public void onClick(View v){Bundle bl = new Bundle(); bl.putString("column", ""); bl.putString("content",allvaluesc); bl.putInt("cid", 0); bl.putInt("id", ed1); Message ml = new Message(); ml.setData(bl); editReady.sendMessageDelayed(ml,50);}});
				cr.setOnLongClickListener(new OnLongClickListener(){public boolean onLongClick(View v){Intent d2 = new Intent(mCtx,Motion.class);
				d2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				//d2.addFlags(d2.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
				startActivity(d2);
				wayGo.sendEmptyMessage(2);return true;}});
			
				//cr.addView(et);
				cr.addView(name);}
				
				clh.addView(cr);
				}
				
				
				for(int ci = 0; ci < cn.length; ci++){
			
					String value = "";
					String type = "";
										
					value = lCursor.getString(ci);type = "string";
					if(value == null){  value = Long.toString(lCursor.getLong(ci)); type = "Long"; }
					if(value == null){  value = Integer.toString(lCursor.getInt(ci)); type = "Int"; }
					if(value == null){type = "N/A";}

					RelativeLayout cr = new RelativeLayout(mCtx);
					LinearLayout.LayoutParams lcr = new LinearLayout.LayoutParams(-1,86);
					lcr.setMargins(0,1,0,0);
					cr.setId((int)unq++);
					cr.setLayoutParams(lcr);
					cr.setBackgroundColor(coloroff);
					cr.setFocusable(true);
					cr.setClickable(true);

					RelativeLayout cr2 = new RelativeLayout(mCtx);
					LinearLayout.LayoutParams lcr2 = new LinearLayout.LayoutParams(-1,-2);//240);
					lcr2.setMargins(0,0,0,0);
					cr2.setId((int)unq++);
					cr2.setLayoutParams(lcr2);
					cr2.setBackgroundColor(colorm3);
					
					RelativeLayout cr3 = new RelativeLayout(mCtx);
					RelativeLayout.LayoutParams lcr3 = new RelativeLayout.LayoutParams(-1,-2);//-1);
					lcr3.setMargins(0,0,0,0);
					cr3.setId((int)unq++);
					cr3.setPadding(15,7,15,7);
					cr3.setLayoutParams(lcr3);
					cr3.setBackgroundColor(colorm3);
					cr2.addView(cr3);
					
					
					final int part1 = cr.getId();
					final int part2 = cr2.getId();
					
					
					// TITLE
					{TextView name = new TextView(mCtx);
					RelativeLayout.LayoutParams rl = new RelativeLayout.LayoutParams(-2,-2);
					name.setId((int)unq++);
					name.setLayoutParams(rl);
					name.setText(cn[ci]);
					name.setPadding(15,7,15,7);
					name.setTextSize((float)22);
					name.setTextColor(textcolor);
					cr.addView(name);}
				
					int etid = 0;int ebid = 2;
					{EditText name = new EditText(mCtx);
					RelativeLayout.LayoutParams rl = new RelativeLayout.LayoutParams(-1,-2);
					name.setId((int)unq++);
					rl.setMargins(15,7,15,0);
					name.setLayoutParams(rl);
					name.setText(value);
					name.setMaxHeight(154);
					name.setHorizontallyScrolling(true);
					name.setPadding(3,3,3,3);
					name.setTextSize((float)14);
					cr3.addView(name);etid = name.getId();}
				
					{Button name = new Button(mCtx);
					RelativeLayout.LayoutParams rl = new RelativeLayout.LayoutParams(-1,55);
					rl.addRule(RelativeLayout.BELOW,etid);
					rl.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM,-1);
					name.setId((int)unq++);
					//rl.setMargins(15,7,15,7);
					name.setLayoutParams(rl);
					name.setMaxHeight(55);
					name.setText("Save");
					name.setPadding(15,7,15,7);
					name.setTextSize((float)14);
					cr3.addView(name);ebid = name.getId();}
					
					{
					
					RelativeLayout.LayoutParams rx = new RelativeLayout.LayoutParams(-2,-2);
					rx.addRule(RelativeLayout.BELOW,ebid);
					LinearLayout rm = new LinearLayout(mCtx);
					rm.setLayoutParams(rx);
		        	rm.setOrientation(LinearLayout.VERTICAL);
		        	cr3.addView(rm);
		        	LinearLayout rml = null;TextView rt = null;
		        	LinearLayout.LayoutParams rtt = new LinearLayout.LayoutParams(-2,-2);
		        	rtt.setMargins(1,1,0,0);
		        	int rowcnt = 0;int colcnt = 0;int rows = 0;
		        	String[] cu = value.split(" ");
		        	for(int i = 0; i < cu.length;i++){
		        	colcnt++;
		        	if(colcnt == 1){
		        		rowcnt++;
		        		rml = new LinearLayout(mCtx);
		        		LinearLayout.LayoutParams rp = new LinearLayout.LayoutParams(-1,-2);
		        		rp.setMargins(0,1,0,0);
		        		rml.setBackgroundColor(Color.argb(105,0,0,0));
		        		rml.setLayoutParams(rp);
		        		rml.setOrientation(LinearLayout.HORIZONTAL);
		        		rml.setId((int)unq++);
		        		rm.addView(rml);
		        		rows = 0;
		        	}
		        	
		        	rt = new TextView(mCtx);
		        	rt.setLayoutParams(rtt);
		        	rt.setId((int)unq++);
		        	rt.setText(cu[i].trim());
		        	rt.setTextSize((float)14);
		        	rt.setLines(1);
		        	rt.setPadding(3,1,3,1);
		        	rt.setBackgroundColor(Color.argb(105,0,0,90));
		        	rt.setTextColor(textcolor);
		        	
		        	
		        	rml.addView(rt);
		        	rows += rt.getWidth() + 1;
		        	if(cu[i].matches(".*(\n).*")){
			        colcnt=0;
			        continue;
		        	}
			        	
		        	if( colcnt > 5 || ( rows > wWidth-150 && colcnt > 1 ) ){
		        	i--;colcnt=0;rt.setVisibility(View.GONE);
		        	continue;
		        	}
		        	
		        	
		        	}//for
				}
		        	/*/
					{TextView name = new TextView(mCtx);
					RelativeLayout.LayoutParams rl = new RelativeLayout.LayoutParams(-2,-2);
					rl.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
					name.setId((int)unq++);
					name.setLayoutParams(rl);
					
					
					name.setText(type);
					name.setPadding(13,7,13,7);
					name.setTextSize((float)18);
					name.setTextColor(textcolor);
					cr.addView(name);}//*/
					
					
					{TextView name = new TextView(mCtx);
					RelativeLayout.LayoutParams rl = new RelativeLayout.LayoutParams(-1,-1);
					//rl.addRule(RelativeLayout.ALIGN_PARENT_LEFT);rl.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM,-1);
					name.setId((int)unq++);
					name.setLayoutParams(rl);
					//name.setBackgroundColor(colorm1);
					name.setEllipsize(TextUtils.TruncateAt.MIDDLE);
					name.setText(value);
					name.setLines(2);
					name.setGravity(Gravity.BOTTOM);
					name.setPadding(15,37,15,7);
					name.setTextSize((float)14);
					name.setTextColor(textcolor);
					cr.addView(name);}
					
					
					
					
					{
					final String valuec = value;
					final String namec = cn[ci];
					cr.setOnFocusChangeListener(new OnFocusChangeListener(){public void onFocusChange(View v, boolean has){if(has){v.setBackgroundColor(coloron);}else{v.setBackgroundColor(coloroff);}}});
					//cr.setOnTouchListener(new OnTouchListener(){public boolean onTouch(View v, MotionEvent ev) {if(ev.getAction() == MotionEvent.ACTION_DOWN){RelativeLayout rf = (RelativeLayout) v;rf.setBackgroundColor(coloron);}else if(ev.getAction() == MotionEvent.ACTION_UP){RelativeLayout rf = (RelativeLayout) v;rf.setBackgroundColor(coloroff);}else if(ev.getAction() == MotionEvent.ACTION_OUTSIDE || ev.getAction() == MotionEvent.ACTION_CANCEL){RelativeLayout rf = (RelativeLayout) v;rf.setBackgroundColor(colorred);}return false;}});
					cr.setOnClickListener(new OnClickListener(){public void onClick(View v){Bundle bl = new Bundle(); bl.putString("column", namec); bl.putString("content",valuec); bl.putInt("cid", part1); bl.putInt("id", part2); Message ml = new Message(); ml.setData(bl); editReady.sendMessageDelayed(ml,50);}});
					

					//Bundle bl = new Bundle(); bl.putString("column", namec); bl.putString("content",valuec); bl.putInt("cid", part1); bl.putInt("id", part2); Message ml = new Message(); ml.setData(bl); editReady.sendMessageDelayed(ml,50);}});
					
					
					cr.setOnLongClickListener(new OnLongClickListener(){public boolean onLongClick(View v){Intent d2 = new Intent(mCtx,Motion.class);
					d2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					//d2.addFlags(d2.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
					startActivity(d2);
					wayGo.sendEmptyMessage(2);return true;}});
					
					
					//;
					
					

					}
					
					
					
				
					clh.addView(cr);
					clh.addView(cr2);
					
					
					{ImageView i1 = new ImageView(mCtx);
			        i1.setLayoutParams(new RelativeLayout.LayoutParams(-2,-2));
			        i1.setScaleType(ScaleType.MATRIX);i1.setImageResource(R.drawable.flatpearl);
			        if(ci == 0){i1.setVisibility(View.GONE);}
			        cr.addView(i1);}	
		        	
					{ImageView i2 = new ImageView(mCtx);
			        RelativeLayout.LayoutParams bli = new RelativeLayout.LayoutParams(-2,-2);
			        bli.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);bli.addRule(RelativeLayout.ALIGN_PARENT_TOP,-1);
			        i2.setLayoutParams(bli);
			        i2.setScaleType(ScaleType.MATRIX);i2.setImageResource(R.drawable.flatpearl2);
			        if(ci == 0){i2.setVisibility(View.GONE);}
			        cr.addView(i2);}
			        
			        {ImageView i3 = new ImageView(mCtx);
			        RelativeLayout.LayoutParams bli = new RelativeLayout.LayoutParams(-2,-2);
			        bli.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);bli.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM,-1);
			        i3.setLayoutParams(bli);
			        i3.setScaleType(ScaleType.MATRIX);i3.setImageResource(R.drawable.flatpearlb);
			        if(ci == cn.length-1){i3.setVisibility(View.GONE);}
			        cr.addView(i3);}
			        
		        	{ImageView i4 = new ImageView(mCtx);
			        RelativeLayout.LayoutParams rp = new RelativeLayout.LayoutParams(-2,-1);
			        LinearLayout bi1 = new LinearLayout(mCtx);
			        bi1.setGravity(Gravity.BOTTOM);
			        bi1.setLayoutParams(rp);
			        i4.setLayoutParams(new LinearLayout.LayoutParams(-2,-2));
			        bi1.setOrientation(LinearLayout.HORIZONTAL);
			        i4.setPadding(0, 0, 0, 0);
			        i4.setScaleType(ScaleType.MATRIX);i4.setImageResource(R.drawable.flatpearl3);
			        bi1.addView(i4);
			        if(ci == cn.length-1){i4.setVisibility(View.GONE);}
			        cr.addView(bi1);}
					
		        	
		        	
					{ImageView i3 = new ImageView(mCtx);
			        RelativeLayout.LayoutParams bli = new RelativeLayout.LayoutParams(-2,-2);
			        bli.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);bli.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM,-1);
			        i3.setLayoutParams(bli);
			        i3.setScaleType(ScaleType.MATRIX);i3.setImageResource(R.drawable.flatpearlb);
			        cr2.addView(i3);}
			        
		        	{ImageView i4 = new ImageView(mCtx);
			        RelativeLayout.LayoutParams rp = new RelativeLayout.LayoutParams(-2,-1);
			        LinearLayout bi1 = new LinearLayout(mCtx);
			        bi1.setGravity(Gravity.BOTTOM);
			        bi1.setLayoutParams(rp);
			        i4.setLayoutParams(new LinearLayout.LayoutParams(-2,-2));
			        bi1.setOrientation(LinearLayout.HORIZONTAL);
			        i4.setPadding(0, 0, 0, 0);
			        i4.setScaleType(ScaleType.MATRIX);i4.setImageResource(R.drawable.flatpearl3);
			        bi1.addView(i4);
			        cr2.addView(bi1);}
		        	
		        	
		        	cr2.setVisibility(View.GONE);
		        	
		        	
		        	
					
				}
			
			
			}
			//mBrowser.addJavascriptInterface(new AndroidBridge(), "android");
		}
    	}
    };

    File getfilepath(){
    	File file = null;
    	Log.w(G,"External Storage Status("+Environment.getExternalStorageState()+") cache("+Environment.getDownloadCacheDirectory().getAbsolutePath()+") ("+Environment.getDataDirectory().getAbsolutePath()+")");
		if(Environment.getExternalStorageState().contentEquals(Environment.MEDIA_MOUNTED)){
			file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "wave");
		}else{
			file = new File(Environment.getDownloadCacheDirectory().getAbsolutePath());
		}
		return file;
    }
    	
    OnFocusChangeListener imagefocus = new OnFocusChangeListener(){public void onFocusChange(View v, boolean has){if(has){v.setBackgroundColor(coloron);}else{v.setBackgroundColor(coloroff);}}};
    OnFocusChangeListener linkfocus = new OnFocusChangeListener(){public void onFocusChange(View v, boolean has){if(has){v.setBackgroundColor(coloron);}else{v.setBackgroundColor(coloroff);}}};
    OnFocusChangeListener textfocus = new OnFocusChangeListener(){public void onFocusChange(View v, boolean has){if(has){v.setBackgroundColor(coloron);}else{v.setBackgroundColor(coloroff);}}};
    private Handler setText = new Handler(){public void handleMessage(Message msg){Bundle bl = msg.getData();int id = bl.getInt("id");String t = bl.getString("text");try{TextView v = (TextView) findViewById(id);if(t!=null){if(bl.containsKey("color")){int co = bl.getInt("color",Color.BLACK);v.setTextColor(co);}v.setText(Uri.decode(t));}}catch(ClassCastException e){Log.e(G,"Wrong target for text " + t);}/*int x = bl.getInt("x",10); int y = bl.getInt("y",10); int size = bl.getInt("size",10);v.setPadding(x-size/2, y-size/2, 0, 0);/**/}};
    int baralpha = 150;
    private Handler fadeBar = new Handler(){public void handleMessage(Message msg){int id = msg.what; RelativeLayout r1 = (RelativeLayout) findViewById(id); r1.setBackgroundColor(Color.argb(baralpha, 0, 0, 50)); baralpha -= 10; if(baralpha >= 0){fadeBar.sendEmptyMessageDelayed(id,72);} }};
    
    public Handler handleImageSize = new Handler(){
    	public void handleMessage(Message msg){
    		Bundle bdl = msg.getData();
    		int id = bdl.getInt("iid");
    		int up = bdl.getInt("up");
    		ImageView iv = (ImageView) findViewById(id);
    	
    		
    		int hm = getWindowManager().getDefaultDisplay().getHeight();
    		int m = getWindowManager().getDefaultDisplay().getWidth();
    		int mx = m;
    		int j = iv.getHeight();
    		int h = iv.getWidth();
    		if(h >= m-10){return;}
    		if(h < 30 ){iv.setVisibility(View.GONE);if(up > 0){RelativeLayout v = (RelativeLayout) findViewById(up);v.setVisibility(View.GONE);}return;}
    		if(h < 80 ){m = 80;}//iv.setVisibility(View.GONE);return;}
    		if(h < (hm<m?hm:m)){iv.setLayoutParams(new RelativeLayout.LayoutParams(mx,-2));}//(hm<m?hm:m)
    		else if(j > hm){iv.setLayoutParams(new RelativeLayout.LayoutParams(-2,hm));}//hm
    		
    	}
    };
    public Handler setImage = new Handler(){
    	public void handleMessage(Message msg){
    		Bundle bdl = msg.getData();
    		int id = bdl.getInt("id");
    		int upid = bdl.getInt("up");
    		String loc = bdl.getString("storloc");
    		SharedPreferences mReg = getSharedPreferences("Preferences", MODE_WORLD_READABLE);
    		String filename = mReg.getString(loc, "");
    	
    		if(filename.length() == 0){return;}
    		ImageView i1 = (ImageView) findViewById(id);
    		if(i1 == null){Toast.makeText(mCtx, "Image Crush", 1750).show();return;}
    		//Drawable d1 = new Drawable();
    		//Bitmap b1 = Bitmap.createBitmap(img);
    		//i1.setImageBitmap(new Bitmap));
    	
    		
    		//Toast.makeText(mCtx, "Image file("+filename+")", 2750).show();
    	
    		//try{
    			
    			//InputStream fi = mCtx.openFileInput(filename);
    			//Bitmap bb = new byte[1024000];fi.read(bb);
    			File file = null;
    			file = getfilepath();
    			/*
    			Log.w(G,"External Storage Status("+Environment.getExternalStorageState()+")  cache("+Environment.getDownloadCacheDirectory().getAbsolutePath()+") ("+Environment.getDataDirectory().getAbsolutePath()+")");
    			if(Environment.getExternalStorageState().contentEquals(Environment.MEDIA_MOUNTED)){
    				file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "wave");
    			}else{
    				file = new File(Environment.getDownloadCacheDirectory().getAbsolutePath());
    			}//*/
    			
    			//File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "wave");	
    	try{
    	/*
    		File filetn = new File(file.getAbsolutePath()+"/tn_"+filename);
    		if(!filetn.exists()){
    			Drawable dx = Drawable.createFromPath(file.getAbsolutePath()+"/"+filename);
    			Rect rt = dx.getBounds();
    			Bitmap b3 = Bitmap.createBitmap(300, 300, Bitmap.Config.ARGB_8888);
    			Canvas c3 = new Canvas(b3);

    			dx.setBounds(0, 0, 300, 300);
    			dx.draw(c3);

    			ByteArrayOutputStream os = new ByteArrayOutputStream();
    			b3.compress(Bitmap.CompressFormat.PNG, 0, os);
    			byte[] array = os.toByteArray();
    			Bitmap bn = BitmapFactory.decodeByteArray(array, 0, array.length);
    			
    				Bitmap b31 = null;
    				b31 = Bitmap.createScaledBitmap(b3, 300, 300, true);
    			
    				ByteBuffer bix = ByteBuffer.allocate(1024 * 1024 * 3); 
    				b31.copyPixelsToBuffer(bix);
    				
    				//byte[] bix = new byte[1];
    				//b31..getPixels(bix, 0, b31.getWidth(), b31.getWidth(), b31.getHeight(), b31.getWidth(), b31.getHeight());
    				File file1 = new File(file, "tn_"+filename);
    				try {
						file1.createNewFile();
						FileOutputStream fios = new FileOutputStream(file1);
	    				fios.write(bix.array());
	    				fios.flush();
	    				fios.close();
    				} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
    				
    			
    			}
    			//*/
    		
    		File filei = new File(file.getAbsolutePath()+"/"+filename);
    		if(filei.exists()){
    			Drawable db = Drawable.createFromPath(filei.getAbsolutePath());
    			i1.setImageDrawable(db);
    			i1.setAlpha(255);
    			
    			{Message ml = new Message(); Bundle bl = new Bundle(); bl.putInt("iid",i1.getId()); bl.putInt("up",upid); ml.setData(bl);handleImageSize.sendMessageDelayed(ml,50);}
    		}else{
    			i1.setImageResource(R.drawable.iget_error);
    			i1.setAlpha(255);
    		}
    		
    			
    	}catch(OutOfMemoryError me){
    		
    		return;
    	}
    			//Bitmap bm = new Bitmap().copyPixelsFromBuffer(bb);
    		//}catch (IOException e){Log.e(G,"img "+e.getLocalizedMessage());}
    		//i1.setImageBitmap(bm)
    		
    	}
    };
    
    
    public Handler flipAlpha = new Handler(){
		int flip = 1;
    	public void handleMessage(Message msg){final Bundle bx = msg.getData();
			int id = bx.getInt("id");
			ImageView i1 = (ImageView) findViewById(id);
			if(flip == 1){
				flip = 2;
				i1.setImageResource(R.drawable.iget_1);
			}else if(flip == 2){
				flip = 3;
				i1.setImageResource(R.drawable.iget_2o);
			}else if(flip == 3){
				flip = 4;
				i1.setImageResource(R.drawable.iget_3o);
			}else if(flip == 4){
				flip = 1;
				i1.setImageResource(R.drawable.iget_2o);
			}
				
		}
    };
    private DefaultHttpClient mHC;
	public Handler mGet = new Handler(){
		public void handleMessage(Message msg){final Bundle bx = msg.getData();
		
		if(mHC == null){mHC = new DefaultHttpClient();}
		
		final String dest = bx.getString("dest");
		final String loc = bx.getString("storloc");
		final String titlr = bx.getString("title");final String procg = bx.getString("procg");
		final Long moment = bx.getLong("moment");
		
		if( dest == null || dest.length() == 0 ){
			Log.e(G,"Blocked empty get request: Destination titled " + titlr + " intended to " + loc);
			return;
		}
		final Bundle bdl = new Bundle(bx);
		Thread mt = new Thread(){
			
			public void run(){//SharedPreferences mReg = getSharedPreferences("Preferences", MODE_WORLD_WRITEABLE);
			/*if(mReg.contains(loc) && mReg.contains(loc+U_SAVED)){
				if( mReg.getLong(loc+U_SAVED, 10) > (System.currentTimeMillis()-bx.getLong("age",18000))){
					{Message mxm = new Message(); Bundle bxb = new Bundle(); bxb.putString("title",procg + " " + titlr);bxb.putString("subtitle",(int)(System.currentTimeMillis() - mReg.getLong(loc+U_SAVED, 33))/1000+" Second Cache " + titlr +" for "+loc+".\n"+dest ); mxm.setData(bxb);easyViewerHandler.sendMessageDelayed(mxm,10);}
					{Message mxx = new Message();mxx.setData(bx);taskDone.sendMessageDelayed(mxx, 30);}
					return;
				}
			}//*/
			
	
		//Thread tx = new Thread(){public void run(){
			final String dest = bdl.getString("dest");String who = bdl.getString("who");
			final String loc = bdl.getString("storloc");
			final String titlr = bdl.getString("title");String procg = bdl.getString("procg");
	
			
			//easyStatus("Acquiring " + titlr +"\n"+dest);
			//{Message mxm = new Message(); Bundle bxb = new Bundle(); bxb.putString("title",procg);bxb.putString("subtitle", ); mxm.setData(bxb);easyViewerHandler.sendMessageDelayed(mxm,10);}
			
			
		final long sh = SystemClock.uptimeMillis();
		HttpGet httpget = new HttpGet(dest);
		String mUrl = httpget.getURI().toString();
		
		//Log.w(G,"safeHttpGet() 1033 getURI("+httpget.getURI()+") for " + who);
		if( httpget.getURI().toString() == "" ){
			//{Message mx = new Message(); Bundle bx = new Bundle();bx.putString("subtitle","Blocked empty destination get.");bx.putString("title",procg+" "+titlr);mx.setData(bx);easyViewerHandler.sendMessageDelayed(mx,pRate);}
			return;
		}
		
		String responseCode = ""; //String mHP = "";
		//CookieStore c = mHC.getCookieStore();
		//mHC = new DefaultHttpClient();mHC.setCookieStore(c);
		CookieStore cs = (mHC != null) ? mHC.getCookieStore(): new DefaultHttpClient().getCookieStore();
		DefaultHttpClient mHC = new DefaultHttpClient();
		SharedPreferences mReg = mCtx.getSharedPreferences("Preferences", MODE_WORLD_WRITEABLE);
		String cshort = mReg.getString("lastcookies","");
		String[] clist = cshort.split("\n");ContentValues cg = new ContentValues();
		for(int h=0; h < clist.length; h++){
			String[] c = clist[h].split(" ",2);
			if(c.length == 2 && c[0].length() > 3){if(cg.containsKey(c[0]) == false){
				//cg.put(c[0], c[1]);
				Cookie logonCookie = new BasicClientCookie(c[0], c[1].replaceAll("; expires=null", ""));
				//Log.w(G,"Carry Cookie mGet2 " + c[0] + ":"+c[1] + " expires("+logonCookie.getExpiryDate()+")" + " path("+logonCookie.getPath()+") domain("+logonCookie.getDomain()+")");
				cs.addCookie(logonCookie);//TODO
			}}
		}
		
		mHC.setCookieStore(cs);
		
		try {
			
			
			
			mHC.setRedirectHandler(new RedirectHandler(){
	
				public URI getLocationURI(HttpResponse arg0,
						HttpContext arg1) throws ProtocolException {
					
					if( arg0.containsHeader("Location")){
					String url = arg0.getFirstHeader("Location").getValue();
					//Log.w(G,"getLocationURI url("+url+")  " + arg0.getStatusLine().getReasonPhrase() + ": " + arg1.toString());
					//mUrl = url;mUrl("+mUrl+")
					URI uri = URI.create(url);
					
					//{Message mxm = new Message(); Bundle bxb = new Bundle();bxb.putString("string", loc+"url");bxb.putString(loc+"url",url );mxm.setData(bxb);setrefHandler.sendMessageDelayed(mxm,10);}
					//mEdt.putString(loc+"url", url); mEdt.commit();
					return uri;
					}else{
						return null;
					}
				}
	
				public boolean isRedirectRequested(HttpResponse arg0,
						HttpContext arg1) {//Log.w(G,"isRedirectRequested " + arg0.getStatusLine().getReasonPhrase() + ": " + arg1.toString() + " ");
						if( arg0.containsHeader("Location") ){
							String url = arg0.getFirstHeader("Location").getValue();
							//{Message mxm = new Message(); Bundle bxb = new Bundle();bxb.putString("string", loc+"url");bxb.putString(loc+"url",url );mxm.setData(bxb);setrefHandler.sendMessageDelayed(mxm,10);}
							//Log.w(G,"isRedirectRequested url(" + url+") ");
							//mEdt.putString(loc+"url", url); mEdt.commit();
							return true;
						}
					return false;
				}
				
			});
	
			//{Message mxm = new Message(); Bundle bxb = new Bundle();bxb.putString("string", loc+"url");bxb.putString(loc+"url",mUrl );mxm.setData(bxb);setrefHandler.sendMessageDelayed(mxm,1);}
			//Log.w(G,"safeHttpGet() 1044 httpclient.execute() mUrl("+mUrl+") for " + who);
			long freememory = Runtime.getRuntime().freeMemory();
			if(freememory < 102400){Editor mEdt = mReg.edit();mEdt.putString(loc, "");mEdt.commit();return;}
			//{Message mxm = new Message(); Bundle bxb = new Bundle(); bxb.putString("text","Downloading into RAM\n"+(freememory/1024)+" Kb free"); mxm.setData(bxb);easyStatusHandler.sendMessageDelayed(mxm,10);}
			HttpResponse mHR = mHC.execute(httpget);
			//reply[2] = mReg.getString(loc+"url", mUrl);mUrl = reply[2];
			//if(getListView().isShown() || getListView().hasFocus()){}else{Log.e(G,"List isn't shown and nofocus, sensor watch close");wayGo.sendEmptyMessage(2);}
			if( mHR != null ){
		        Log.w(G,"safeHttpGet() 436 " + mHR.getStatusLine() + " " + " for " + who);
				//{Message mxm = new Message(); Bundle bxb = new Bundle(); bxb.putString("text","Server says "+mHR.getStatusLine().getStatusCode() + " "+mHR.getStatusLine().getReasonPhrase()); mxm.setData(bxb);easyStatusHandler.sendMessageDelayed(mxm,10);}
				//easyStatus();
				
		        Log.w(G,mHR.getStatusLine().getStatusCode() + " " + mHR.getStatusLine().getReasonPhrase()+" safeHttpGet() 440 response.getEntity() for " + who);
		        HttpEntity mHE = mHR.getEntity();
	
		        if (mHE != null) {
			        //byte[] bytes = ;
		        	Log.w(G,"safeHttpGet() 445 byte[] to EntityUtils.toByteArray(mHE) expect 448");
		        	//freememory = Runtime.getRuntime().freeMemory();
		        	
		        	byte[] mhpb = EntityUtils.toByteArray(mHE);
		        	//String mhpb = EntityUtils.toString(mHE);
		        	//easyStatus("Downloaded into RAM\n"+ (mhpb.length()>1024?(mhpb.length()/1024)+" Kb":(mhpb.length())+" b" ));
		        	Log.w(G,"safeHttpGet() 448 mhpb("+mhpb.length+") to String for " + who);
		        	
		        	
		        	try{

		        		Editor mEdt = mReg.edit();
		        		
		        		String path = "";
                        File file = null;
                        file = getfilepath();
                        /*if(Environment.getExternalStorageState().contentEquals(Environment.MEDIA_MOUNTED)){
            				file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "wave");
            			}else{
            				file = new File(Environment.getDownloadCacheDirectory().getAbsolutePath());
            			}//*/
                        
                        //File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "wave");
                        
                        if( file.mkdirs() ){path = file.getAbsolutePath();}
                        //mEdt.putString(loc, "");mEdt.commit();
                        
		        		//String filename = this.timeStampFormat.format(new Date());
                        //SharedPreferences mReg = getSharedPreferences("Preferences", MODE_WORLD_WRITEABLE);
                        
                        String imgname = SystemClock.uptimeMillis() + (mHE.getContentType().getValue().contains("jp")?".jpg":".png");
                        
                        mEdt.putString(loc, imgname);
                        //File f1 = new File(file,imgname);"/sdcard/wave/"+
                        //FileOutputStream fios = mCtx.openFileOutput(imgname,MODE_WORLD_WRITEABLE);
                        
                        
		        		//ContentValues values = new ContentValues();
                        //values.put(MediaColumns.TITLE, loc);
                        //values.put(ImageColumns.DESCRIPTION, "Wave");
                        //Uri uri = mCtx.getContentResolver().insert(Media.EXTERNAL_CONTENT_URI, values);
                        //Log.w(G,"Preparing to utter " + loc + " at uri " + uri.toString());
                        //Log.i(G, "ComputerStart() 1524 writting picture to disk name("+imgname+") type("+mHE.getContentType()+") encoding("+mHE.getContentEncoding()+")");
                        //Bitmap p = null;
                        //p.compress(CompressFormat.PNG, 100, fios);
                        File file1 = new File(file, imgname);
                        file1.createNewFile();
                        FileOutputStream fios = new FileOutputStream(file1);
                        fios.write(mhpb);
                        fios.flush();
                        fios.close();
for(int in=1;in<5;in++){ if(mReg.contains("image_"+moment+"_"+in)){continue;}mEdt.putString("image_"+moment+"_"+in,file.getAbsolutePath() + "/"+imgname);break;}mEdt.commit();
                        //Editor mEdt = mReg.edit();
                        
                        
                        
                        //{Message ml = new Message(); Bundle bl = new Bundle(); bl.putInt("id", imgid);bl.putString("file", loc); ml.setData(bl); setImage.sendMessage(ml);}
                        
                        //mPictureCallback = new ImageCaptureCallback(getContentResolver().openOutputStream(uri));
                        //Log.w(G,"takePicture");
                        //if( this.camera == null ){Log.e(G,"c");return;}
                        //if( mShutterCallback  == null ){Log.e(G,"msc");}if( mPictureCallback == null ){Log.e(G,"mpr");}if( mPictureCallback == null ){Log.e(G,"mpc");}
                        //camera.stopPreview();
                        //this.camera.takePicture(mShutterCallback,mPictureCallbackRaw,mPictureCallback);// this.camDemo);
                        //this.camera.release();

		        	}catch(OutOfMemoryError ex){Log.e(G,"Out of Memory");
	                }catch(Exception ex){
	                        Log.e(G,"Exception " + ex.getLocalizedMessage());
	                        ex.printStackTrace();
	                }

		        	
		        	//mhpb = null;
		        	//mHP = new String(EntityUtils.toByteArray(mHE));
			            
		        //Log.w(G,"safeHttpGet() 1056 ");
			        final CookieStore cs2 = mHC.getCookieStore();
			        //Thread tc = new Thread(){public void run(){
			        //mHO = mHC.getCookieStore().getCookies();
			        
			      	List<Cookie> cl2 = cs2.getCookies();
			      	
			      	Bundle co = new Bundle();String cshort2 = "";
			      	for(int i = cl2.size()-1; i >= 0; i--){
			      		Cookie c3 = cl2.get(i);
			      		if(co.containsKey(c3.getName())){continue;}
			      		co.putInt(c3.getName(), 1);
			      	//ContentValues hh = new ContentValues();
			      	//for(int i = 0; i < cl2.size(); i++){//< cl2.size()
			      		//if(mReg.getInt("ask", 1)>3){easyStatus("Cookie "+(i+1));}
			      		//Cookie c3 = cl2.get(i);
			      		//if( cshort2.contains(c3.getName()+" ")){continue;}
			      		//Log.w(G, "mGet2 safeHttpGet() Cookie(): "+c3.getName() + " " + c3.getValue() + " (" + c3.getDomain()+" p" + c3.isPersistent()+" s" + c3.isSecure() +" " + c3.getPath()+" " +c3.getVersion() +")");
			      		//cshort2 += c3.getName() +" " + c3.getValue() +(!c3.getValue().contains("expires")?c3.getExpiryDate()!=null?"; expires="+c3.getExpiryDate():"":"")+"\n";
			      		cshort2 += c3.getName() +" " + c3.getValue()+(c3.getExpiryDate()!=null?"; expires="+c3.getExpiryDate():"")+(c3.getPath()!=null?"; path="+c3.getPath():"")+(c3.getDomain()!=null?"; domain="+c3.getDomain():"")+"\n";
			      	}
			      	if(cshort2.length() > 0 ){
			      		//final String s = cshort2; 
			      		
			      		Editor mEdt = mReg.edit();
			      		mEdt.putString("lastcookies", cshort2);
			      		mEdt.commit();
			      		//Log.w(G,"lastcookies: " + cshort2);
			      		//{Bundle bxb = new Bundle(); bxb.putString("string", "lastcookies");bxb.putString("lastcookies", cshort2);
			      		//bxb.putString("long", "lasthttp");bxb.putLong("lasthttp", System.currentTimeMillis());
			      		//Message mx = new Message(); mx.setData(bxb);setrefHandler.sendMessageDelayed(mx,50);}
			      		
			      		/*
			      		Thread eb = new Thread(){public void run(){mReg = getSharedPreferences("Preferences",MODE_WORLD_WRITEABLE);mEdt = mReg.edit();
			      	mEdt.putLong("lasthttp",System.currentTimeMillis());
			      	mEdt.putString("lastcookies", s);
			      	mEdt.commit();
			      		}};eb.start();//*/
			      	}//}};tc.start();
			      	mHE.consumeContent();
			        }
			        
			      	
			        //
			        // Print Cookies
			        //if ( !mHttpCookie.isEmpty() ) { for (int i = 0; i < mHttpCookie.size(); i++) { Log.w(G,"safeHttpGet() Cookie: " + mHttpCookie.get(i).toString()); } }
			        
			        //
			        // Print Headers
		        	//Header[] h = mHttpResponse.getAllHeaders(); for( int i = 0; i < h.length; i++){ Log.w(G,"safeHttpGet() Header: " + h[i].getName() + ": " + h[i].getValue()); }
			        //mUrl = httpget.getURI().toString();
			        //Log.w(G,"safeHttpGet() " + mUrl);
			        
				}
			
			
	        responseCode = mHR.getStatusLine().toString();
			
		} catch (ClientProtocolException e) {
			//{Message mx = new Message(); Bundle bx = new Bundle();bx.putString("text","safeHttpGet() 1121 ClientProtocolException for " + who);bx.putInt("l",2);mx.setData(bx);logoly.sendMessageDelayed(mx,pRate);}
			//{Message mx = new Message(); Bundle bx = new Bundle();bx.putString("text","safeHttpGet() 1122 IO Exception Message " + e.getLocalizedMessage());bx.putInt("l",2);mx.setData(bx);logoly.sendMessageDelayed(mx,pRate);}
			e.printStackTrace();//{Message mx = new Message(); Bundle bx = new Bundle();bx.putString("text",e.printStackTrace());bx.putInt("l",2);mx.setData(bx);logoly.sendMessageDelayed(mx,100);}
			responseCode = " " + e.getLocalizedMessage() + " HTTP ERROR";//easyStatus(responseCode);
		} catch (NullPointerException e) {
			//{Message mx = new Message(); Bundle bx = new Bundle();bx.putString("text","safeHttpGet() 1126 NullPointer Exception for " + who);bx.putInt("l",2);mx.setData(bx);logoly.sendMessageDelayed(mx,pRate);}
			//{Message mx = new Message(); Bundle bx = new Bundle();bx.putString("text","safeHttpGet() 1127 IO Exception Message " + e.getLocalizedMessage());bx.putInt("l",2);mx.setData(bx);logoly.sendMessageDelayed(mx,pRate);}
			e.printStackTrace();//{Message mx = new Message(); Bundle bx = new Bundle();bx.putString("text",e.printStackTrace());bx.putInt("l",2);mx.setData(bx);logoly.sendMessageDelayed(mx,100);}
		} catch (IOException e) {
			//{Message mx = new Message(); Bundle bx = new Bundle();bx.putString("text","safeHttpGet() 1130 IO Exception for " + who);bx.putInt("l",2);mx.setData(bx);logoly.sendMessageDelayed(mx,pRate);}
			//if( e.getLocalizedMessage().contains("Host is unresolved") ){ SystemClock.sleep(1880); }
			responseCode = e.getLocalizedMessage();
			
			Editor mEdt = mReg.edit();
			mEdt.putLong("error", System.currentTimeMillis());mEdt.putString("errortype", responseCode);mEdt.commit();
			
			//{Message mx = new Message(); Bundle bx = new Bundle();bx.putString("text","safeHttpGet() 1132 IO Exception Message " + e.getLocalizedMessage());bx.putInt("l",2);mx.setData(bx);logoly.sendMessageDelayed(mx,pRate);}
			//StackTraceElement[] err = e.getStackTrace();
			//for(int i = 0; i < err.length; i++){
				//Log.w(G,"safeHttpGet() 1135 IO Exception Message " + i + " class(" + err[i].getClassName() + ") file(" + err[i].getFileName() + ") line(" + err[i].getLineNumber() + ") method(" + err[i].getMethodName() + ")");
			//}
			//easyStatus(responseCode);
		} catch (OutOfMemoryError e) {
			//{Message mx = new Message(); Bundle bx = new Bundle();bx.putString("text","safeHttpGet() 1121 OutOfMemoryError for " + who);bx.putInt("l",2);mx.setData(bx);logoly.sendMessageDelayed(mx,pRate);}
			//{Message mx = new Message(); Bundle bx = new Bundle();bx.putString("text","safeHttpGet() 1122 IO Memory Message " + e.getLocalizedMessage());bx.putInt("l",2);mx.setData(bx);logoly.sendMessageDelayed(mx,pRate);}
			e.printStackTrace();//{Message mx = new Message(); Bundle bx = new Bundle();bx.putString("text",e.printStackTrace());bx.putInt("l",2);mx.setData(bx);logoly.sendMessageDelayed(mx,100);}
			Editor mEdt = mReg.edit();
			long freememory = Runtime.getRuntime().freeMemory();
			responseCode = "OS Crunch, Out of RAM at " + (freememory/1024) + " Kb";
			mEdt.putLong("error", System.currentTimeMillis());mEdt.putString("errortype", responseCode);mEdt.commit();
			//easyStatus(responseCode);
		} catch (IllegalArgumentException e){
			//{Message mx = new Message(); Bundle bx = new Bundle();bx.putString("text","Argument Exception "+e.getLocalizedMessage()+" for " + who);bx.putInt("l",2);mx.setData(bx);logoly.sendMessageDelayed(mx,pRate);}
			responseCode = e.getLocalizedMessage();
		
		} catch (IllegalStateException e) {
			//{Message mx = new Message(); Bundle bx = new Bundle();bx.putString("text","safeHttpGet() 1139 IllegalState Exception for " + who);bx.putInt("l",2);mx.setData(bx);logoly.sendMessageDelayed(mx,pRate);}
			//{Message mx = new Message(); Bundle bx = new Bundle();bx.putString("text","safeHttpGet() 1140 IO Exception Message " + e.getLocalizedMessage());bx.putInt("l",2);mx.setData(bx);logoly.sendMessageDelayed(mx,pRate);}
			e.printStackTrace();//{Message mx = new Message(); Bundle bx = new Bundle();bx.putString("text",e.printStackTrace());bx.putInt("l",2);mx.setData(bx);logoly.sendMessageDelayed(mx,100);}
			//if( responseCode == "" ){
				//responseCode = "440"; //440 simulates a timeout condition and recreates the client.
			//}
		}//e.getLocalizedMessage()
		}};mt.start();}		
	};
    

	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
    }
    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		return super.onCreateOptionsMenu(menu);
	}


	@Override
	public boolean onCreatePanelMenu(int featureId, Menu menu) {
		// TODO Auto-generated method stub
		//menu.add(0, 401, 0, "View Article Link")
			//.setIcon(android.R.drawable.ic_menu_view);
        
		
		
		menu.add(0, 402, 0, "Email/Save").setIcon(android.R.drawable.ic_menu_send);
	
		if(false){
    		int groupNum = 20;
			SubMenu sync = menu.addSubMenu(Menu.NONE, groupNum, 20, "Interval"); //getItem().
			sync.setIcon(android.R.drawable.ic_menu_agenda);
			sync.add(groupNum, 0, 0, "Not Automatic");//value == 0
			sync.add(groupNum, 30, 2, "30 Minutes");
			sync.add(groupNum, 60, 2, "Hourly");
			sync.add(groupNum, 1, 2, "5 Hours");//value == 1
			sync.add(groupNum, 2, 3, "Daily");// value == 2
			SharedPreferences mReg = getSharedPreferences("Preferences", MODE_WORLD_READABLE);
			int interval = mReg.contains("interval") ? mReg.getInt("interval",1) : 1;
			sync.setGroupCheckable(groupNum, true, true);
			sync.setGroupEnabled(groupNum, true);
			
			MenuItem activeitem = null;
			activeitem = sync.findItem(interval);
			if( activeitem == null ){
				if( interval >= 10 ){
					sync.add(groupNum, interval, 1, "Every " + interval + " minutes");
				}else{
					interval = 1; // Must exist.
				}
				activeitem = sync.findItem(interval);
			}
			activeitem.setChecked(true);
		}
        
		//menu.add(0, 405, 0, "ScreenShot").setIcon(android.R.drawable.ic_menu_camera);
		
		menu.add(0, 403, 0, "List").setIcon(android.R.drawable.ic_menu_more);
        
        return super.onCreatePanelMenu(featureId, menu);
	}


	@Override
	public View onCreatePanelView(int featureId) {
		// TODO Auto-generated method stub
		return super.onCreatePanelView(featureId);
	}

	


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
//		mLog.w(G,"onOptionsItemSelected()");
		
		final AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		final String link = mLink; 
		TextView mTitle = (TextView) findViewById(R.id.browser_title);

		final int itemid = item.getItemId();
		final int groupid = item.getGroupId();
		if(groupid > 0){ item.setChecked(true); }
		final long mtitleid = mTitle.getId();
		//moomo = menu;
		//moomoMenu.sendEmptyMessageDelayed(50,10);
		Thread tx = new Thread(){public void run(){
		switch(groupid){
		case 20: // Interval
			SharedPreferences mReg = getSharedPreferences("Preferences",MODE_WORLD_WRITEABLE);
			Editor mEdt = mReg.edit(); mEdt.putInt("interval",itemid);mEdt.commit();
			//{Message mxm = new Message(); Bundle bxb = new Bundle();bxb.putString("int", "interval");bxb.putInt("interval",itemid);mxm.setData(bxb);setrefHandler.sendMessage(mxm);}
			if(itemid == 0){
				easyStatus("Not Automatic");
				Intent service = new Intent();service.setClass(mCtx, AutomaticService.class); stopService(service);
			}else{easyStatus("Interval Setting Saved");}
			break;
		default:
			break;
		}
		

    	
		switch(itemid){
		case 401:
			Intent d = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
			startActivity(d);
			break;
		case 402:
			{
				//{Message ml = new Message(); Bundle bl = new Bundle(); bl.putLong("id", mtitleid);  ml.setData(bl); click.sendMessage(ml);}
				//mTitle.performClick();
				//{Message ml = new Message(); Bundle bl = new Bundle(); bl.putString("text","\n\n\n"+date + "\n"+title+"\n" +link + "\n\n\nAndroid\n"); bl.putString("title", "FW: " + title); ml.setData(bl); wayForward.sendMessage(ml);}
				wayForward.sendEmptyMessage(2);
			
				
			}
			break;
		case 403:
			Intent d2 = new Intent(mCtx,Motion.class);
			d2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			//d2.addFlags(d2.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
			startActivity(d2);
			wayGo.sendEmptyMessage(2);
			break;
		case 405:
			{
				//snapshot.sendEmptyMessageDelayed(2,3000);
			}
			break;
		}
		
		}};tx.start();
		return super.onOptionsItemSelected(item);
	}

	
	private void easyStatus(final String m){
		{Message ml = new Message(); Bundle bl = new Bundle(); bl.putString("text",m);ml.setData(bl);easyStatusHandler.sendMessage(ml);}
	}

	private long essmooth = 0;
	private Handler easyStatusHandler = new Handler(){
		public void handleMessage(Message msg){
		int es = 10;
		if(essmooth > System.currentTimeMillis() - 1750){es = (int)((essmooth + 1750) - System.currentTimeMillis());}
		essmooth = System.currentTimeMillis()+es;
		int esx = es;Bundle bdl = msg.getData();
		
		//long freememory = Runtime.getRuntime().freeMemory();
		//Log.i(G,"easyStatus("+bdl.getString("text").replaceAll("\n", "   ")+") freememory("+freememory/1024+" Kb) has(" + getListView().hasFocus() +") shown("+getListView().isShown()+") enabled("+getListView().isEnabled()+")");
		//if( (mTitle.isShown() || mTitle.hasFocus()) && mTitle.isEnabled() ){}else{Log.e(G,"List isn't shown and nofocus, sensor watch close");wayGo.sendEmptyMessage(2);return;}
		/*if(!getListView().isShown() || !getListView().isEnabled()){
			return;
			//bdl.putString("text", "Interruption Handled");
			//wayGo.sendEmptyMessageDelayed(2,1000);
		}//*/
		Toast.makeText(mCtx, "\n"+bdl.getString("text")+"\n", bdl.getString("text").length() > 12?(bdl.getString("text").length() < 50?Toast.LENGTH_LONG:10000):Toast.LENGTH_SHORT).show();
		{Message ml = new Message(); Bundle bl = new Bundle(); bl.putString("text", bdl.getString("text"));ml.setData(bl);logoly.sendMessage(ml);}
		//{Message ml = new Message();/**/bdl.putString("status", bdl.getString("text"));bdl.putInt("esx", esx);ml.setData(bdl);updateStatus.sendMessageDelayed(ml,esx);}//easyStatus(msg.getData().getString("text"));
	}
	};

	private Handler logoly = new Handler(){public void handleMessage(Message msg){Bundle bx = msg.getData();int l = bx.getInt("l");String text = bx.getString("text");switch(l){case 2:Log.e(G,":"+text);break;case 3:Log.w(G,":"+text);break;default:Log.i(G,":"+text);break;}}};
	Handler click = new Handler(){public void handleMessage(Message msg){Bundle bdl = msg.getData();long id = bdl.getLong("id",0);View ct = findViewById((int)id);ct.performClick();}};
	

	Handler wayProceed = new Handler(){
		public void handleMessage(Message msg){
			ScrollView s = (ScrollView) findViewById(R.id.browser_scroll);
			Log.i(G,"Scroll at "+s.getScrollY());
			s.smoothScrollBy(0, s.getHeight()/2);
			
		}
	};
	Handler wayForward = new Handler(){
		public void handleMessage(Message msg){
			TextView mTitle = (TextView) findViewById(R.id.browser_title);
			TextView mDate = (TextView) findViewById(R.id.browser_date);
			final String title = mTitle.getText().toString(); 
			final String published = mDate.getText().toString();
			final String link = mLink;
			
			
			
			
			Intent jump = new Intent(Intent.ACTION_SEND);
			jump.putExtra(Intent.EXTRA_TEXT, "\n\n\n"+published + "\n"+title+"\n" +link + "\n\n\nAndroid\n" ); 
			jump.putExtra(Intent.EXTRA_SUBJECT, title );
			jump.setType("message/rfc822"); 
			startActivity(Intent.createChooser(jump, "Email"));

			wayGo.sendEmptyMessage(2);
			
		}
	};
	

	Handler wayGo = new Handler(){
		public void handleMessage(Message msg){
			finish();
		}
	};

	
	@Override
	protected void onPause() {
//		mLog.w(G,"onPause() ++++++++++++++++++++++++++++++++");
		//easyLoadData("<html></html>");
		//mHideData.sendEmptyMessage(2);
		// TODO Auto-generated method stub
		super.onPause();
	}

	
    
}

