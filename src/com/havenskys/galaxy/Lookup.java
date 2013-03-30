package com.havenskys.galaxy;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.util.List;
import java.util.Date;

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
import android.app.SearchManager;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
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
import android.webkit.WebView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ImageView.ScaleType;
import android.widget.VideoView;

public class Lookup extends Activity {

	int wHeight = 2;int wWidth = 2;
	@Override
	protected void onResume() {
	super.onResume();
		
	wHeight = getWindowManager().getDefaultDisplay().getHeight();
	wWidth = getWindowManager().getDefaultDisplay().getWidth();
	Log.w(TAG,"Resume " + wWidth + "x" + wHeight);
	}

	private static String TAG = "Lookup";
	private static String G = "Browser";
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
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//mLog = new Custom(this);
		mCtx = this;

		wHeight = getWindowManager().getDefaultDisplay().getHeight();
		wWidth = getWindowManager().getDefaultDisplay().getWidth();
		setContentView(R.layout.browser);
		Log.i(G,"a " + wWidth + "x" + wHeight);
		{s01.sendEmptyMessage(2);}
	}
	
	Handler s01 = new Handler(){
		public void handleMessage(Message msg){
		//Bundle bdl = msg.getData();
		
			
		mContent = (LinearLayout) findViewById(R.id.browser_viewer);
		//mContent.setBackgroundColor(Color.MAGENTA);
		mR = (LinearLayout) findViewById(R.id.browser);
		mR.setPadding(0,0,0,0);
		mR.setDrawingCacheEnabled(true);
		mR.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
		
		//mR.setLayoutParams(new FrameLayout.LayoutParams(-1,-1));
		mR.setBackgroundColor(Color.argb(255, 0, 0, 0));
		TextView mTitle = (TextView) findViewById(R.id.browser_title);
		
		//mContent.setVisibility(View.INVISIBLE);
		//mHideData.sendEmptyMessageDelayed(1, 10);
		
		//mReg = getSharedPreferences("Preferences", MODE_WORLD_WRITEABLE);
        //mEdt = mReg.edit();
        //String url = mReg.getString("url","");
        //mEdt.putLong("id", id); mEdt.commit();


		Bundle mIntentExtras = getIntent().getExtras();
		long id = mIntentExtras != null && mIntentExtras.containsKey("id") ? mIntentExtras.getLong("id") : 0;
		Uri geturi = mIntentExtras != null && mIntentExtras.containsKey("uri") ? Uri.parse(mIntentExtras.getString("uri")) : null;
		
		
		//mLinearLayout = (LinearLayout) findViewById(R.id.browser);
		
		
		//mDate = (TextView) findViewById(R.id.browser_date);
	
		{Message ml = new Message(); Bundle bl = new Bundle(); bl.putString("text", "Loading"); bl.putInt("id", R.id.browser_title); ml.setData(bl); setText.sendMessage(ml);}
		{Message ml = new Message(); Bundle bl = new Bundle(); bl.putString("text", ""); bl.putInt("id", R.id.browser_date); ml.setData(bl); setText.sendMessage(ml);}
		
		Uri contenturi = Uri.withAppendedPath(DataProvider.CONTENT_URI, "moment");
		
		if(geturi == null || id > 0){
			//mEdt.putLong("id", id);mEdt.commit();
			//{Message ml = new Message(); Bundle bl = new Bundle(); bl.putLong("id", id); ml.setData(bl); loadRecord.sendMessage(ml); }
			
			geturi = Uri.withAppendedPath(contenturi, ""+id);
		}else if(geturi != null && id == 0){
			id = geturi.getLastPathSegment().matches("[0-9]+")?Integer.parseInt(geturi.getLastPathSegment()):-1;
		}
	
		Log.w(TAG,"loadRecord id("+id+") uri("+geturi+")");
		{Message ml = new Message(); Bundle bl = new Bundle(); bl.putString("uri", geturi.toString()); bl.putLong("moment", id); ml.setData(bl); loadRecord.sendMessage(ml); }
		Log.w(TAG,"handle notification");
		handleNotification.sendEmptyMessage(2);
		}
	};

	Handler baseSQL = new Handler(){
		public void handleMessage(Message msg){
		Bundle bdl = msg.getData();
		

		SharedPreferences mReg = getSharedPreferences("Preferences", MODE_WORLD_READABLE);
		Editor mEdt = mReg.edit();
		if(!mReg.contains("sql2")){
		
			
			//Cursor x = SqliteWrapper.query(mCtx, getContentResolver(), Uri.parse(DataProvider.CONTENT_URI), new String[]{"count(*)"}, "", null}, null);
			
		//	mEdt.putLong("sql2", System.currentTimeMillis());mEdt.commit();
			
		}
		}
	};
	
	Handler softFilter = new Handler(){
		public void handleMessage(Message msg){
			Bundle bdl = msg.getData();
			int line = bdl.getInt("line");
			long moment = bdl.getLong("moment");
			int id = bdl.getInt("id");
			int row = bdl.getInt("row");
			RelativeLayout r = (RelativeLayout) findViewById(id);
			TextView t = (TextView) r.getChildAt(0);
			ScrollView s = (ScrollView) r.getChildAt(1);
			RelativeLayout vr = (RelativeLayout) r.getChildAt(2);
			ImageView tol = (ImageView) r.getChildAt(3);
			ImageView tor = (ImageView) r.getChildAt(4);
			ImageView bor = (ImageView) r.getChildAt(5);
			ImageView bol = (ImageView) r.getChildAt(6);
			ImageView c = (ImageView) r.getChildAt(7);
			TextView dt = (TextView) r.getChildAt(8);
			TextView dx = (TextView) r.getChildAt(9);
			
			//String rtext = shl[line];
			//String ntext = "";//int nxttxt = line;
			
			/*
			//ntext = "";for(int nxttxt=line+1;nxttxt<shl.length;nxttxt++){if(shl[nxttxt].matches("<.*")){Log.w(G,"softFilter ("+Uri.encode(shl[nxttxt],"([A-Z]|[a-z]|[0-9]| ")+")");continue;}else if(shl[nxttxt].length() > 0){ntext = shl[nxttxt];break;}}
			boolean bb = true;
			int cf = line;
			if(!shl[line].startsWith("<") && cf > 1 && !shl[line].matches(".*?[.?!] .*[.?!].*") ){
				for(cf = line-1;cf>0;cf--){ if(bb){Log.w(G,"softFilter context before("+shl[cf]+")");} if(shl[cf].length() == 0 || shl[cf].startsWith("<") || shl[cf].matches(".*?[.?!] .*[.?!].*")){cf++;break;}else if(shl[cf].toLowerCase().matches("(<(form|script|a |img |div|p|table|tr|/form|/script|/a|/div|/p|/table|/tr|hr).*>)")  ){break;} }//|.*?[.?!] .*?[.?!](|.*)  //|| (shl[cf].matches(".*?[.?!] .*?[.?!](|.*)") && shl[cf].length() > 50 )
				if(cf == 0){cf = 1;}
			}
			int cl = line;
			if(!shl[line].startsWith("<") && cl > 0 && cl < shl.length-2 && !shl[line].matches(".*?[.?!] .*[.?!].*")){
				for(cl = line+1;cl<shl.length&&cl>0;cl++){if(bb){Log.w(G,"softFilter context after("+shl[cl]+")");} if(shl[cl].length() == 0 || shl[cl].startsWith("<") || shl[cl].matches(".*?[.?!] .*[.?!].*")){cl--;break;}else if(shl[cl].toLowerCase().matches("(<(a |form |script|img |div|p|table|tr|hr|/form|/script|/a|/div|/p|/table|/tr|hr).*>)")  ){cl--;break;} }//|| (shl[cl].matches(".*?[.?!] .*?[.?!](|.*)") && shl[cl].length() > 50 )
			}
			
			//if(cl < shl.length && shl[cl].length() == 0){cl--;}
			//if(cf >= 0 && shl[cf].length() == 0){cf++;}
			String ecode = "([A-Z]|[a-z]|[0-9]| ";
			String cx = "";
			for(int i = cf; i <= cl && i < shl.length; i++){
				cx += Uri.encode(shl[i],ecode)+"\n";
				if(bb){Log.i(G,"softFilter context["+i+"]("+shl[i]+")");}
			}
			
			//Log.i(G,"softFilter filter("+cx+")");
			if(cf < line && cf > 0){Log.i(G,"repeat " + line + "["+cf+"-"+cl+"] context of " + cf);r.setBackgroundColor(Color.argb(205,80,80,70));r.setVisibility(View.GONE);}//return;}
			cx = cx.trim();	
			Log.i(G,"######################softFilter("+line+"["+cf+"-"+cl+"],"+id+","+cx+")");// ("+Uri.encode(rtext,"([A-Z]|[a-z]|[0-9]| )")+") ("+Uri.encode(ntext,"([A-Z]|[a-z]|[0-9]| )")+")");
			//*/
			t.setText(Uri.decode(shl[line]));
			if(line == 0){
			t.setMinimumHeight(120);
			}else if(line == mContent.getChildCount()-1){
			t.setMinimumHeight(120);
			}else{
			t.setMinimumHeight(75);
			}
			
			String cx = shl[line];
			
			String lc = Uri.decode(cx).toLowerCase();
			Log.w(G,"softFilter() =====("+lc+")====");
			if( lc.matches("<img .*?src.*?=.*?(\"|').*?(\"|').*>") ){
				Log.w(G,"type category image (" + lc + ")");
				
				String alt = ""; String url = "";
				String nw = Uri.decode(cx);
				
				if(lc.matches("<.*? alt.*?=.*?(\"|').*?(\"|').*?>")){	
					alt = nw.replaceAll("<.*? [aA][lL][tT].*?=.*?(\"|')","");alt = alt.replaceAll("(\"|').*>","").trim();
					Log.w(G,"########### ("+alt+") (" + nw + ") ");
				}
				url = nw.replaceAll("<.*? [sS][rR][cC].*?=.*?(\"|')",""); 
				url = url.replaceAll("(\"|').*>","").trim();
				//url = Uri.encode(url);
				
				Log.i(TAG,"Found image ("+alt+") (" + url + ")  tag "+mTag);
				//url.replaceAll("%([8-9]|[A-Z])([0-9]|[A-Z])","");
				//url = Uri.decode(url);
				dt.setText(Uri.encode(url));
				//addtag_detail = bt.length()>0?bt:"Image2";
				//addtag_click = android.R.drawable.ic_menu_gallery;
				//addtag_type = addtag_detail.length() > 0?addtag_detail:"Image2";
				//bcolor = Color.argb(200,65,90,80);
				allimages += url + "\n";
				t.setLayoutParams(new RelativeLayout.LayoutParams(-1,(int)(wWidth/2)));
				t.setText((alt.length()>0?alt+"\n":url));
				{Bundle bl = new Bundle();bl.putInt("id",r.getId());bl.putString("url",url);bl.putLong("moment",moment);Message ml = new Message();ml.setData(bl);imgPath.sendMessageDelayed(ml,2700);}
				
				ContentValues cv = new ContentValues();
				rimages = allimages;
				cv.put("images", allimages.trim());
				SqliteWrapper.update(mCtx, getContentResolver(), Uri.withAppendedPath(DataProvider.CONTENT_URI,"moment"), cv, "_id = " + moment, null);
				
				
				//Bundle bl = new Bundle();bl.putInt("id",r.getId());bl.putString("url",url);bl.putLong("moment",moment);Message ml = new Message(); ml.setData(bl);imgPath.sendMessage(ml);
				
				//tor.setVisibility(View.GONE);
				//tol.setVisibility(View.GONE);
				//bor.setVisibility(View.GONE);
				//bol.setVisibility(View.GONE);
				if(line > 0 && shl[line-1].toLowerCase().matches("<a .*>")){
				
					//RelativeLayout r0 = (RelativeLayout) mContent.getChildAt(row-1);
					//ImageView tol0 = (ImageView) r0.getChildAt(3);
					//ImageView tor0 = (ImageView) r0.getChildAt(4);
					//ImageView bor0 = (ImageView) r0.getChildAt(5);//confirmed
					//ImageView bol0 = (ImageView) r0.getChildAt(6);
					//tol0.setVisibility(tol0.getVisibility()==View.VISIBLE?View.GONE:tol0.getVisibility());
					//tor0.setVisibility(tor0.getVisibility()==View.VISIBLE?View.GONE:tor0.getVisibility());
					//bol0.setVisibility(View.VISIBLE);//bol0.getVisibility()==View.VISIBLE?View.GONE:bol.getVisibility());
					//bor0.setVisibility(View.VISIBLE);//bor0.getVisibility()==View.VISIBLE?View.GONE:bor.getVisibility());
				}
				
				c.setImageResource(android.R.drawable.ic_menu_gallery);
				c.setVisibility(View.VISIBLE);
				
			}else if(lc.matches("<a .*?href.*?=.*?(\"|').*?(\"|').*>")){
				Log.w(G,"type category link ("+lc+")");
			
				String nw = Uri.decode(cx);
				String url = "";String alt = "";
			
				if(lc.matches("<.*? title.*?=.*?(\"|').*?(\"|').*>")){
					alt = nw.replaceAll("<.*? [tT][iI][tT][lL][eE].*?=.*?(\"|')","");alt = alt.replaceAll("(\"|').*>", "").trim();
				}
				url = nw.replaceAll("<.*? [hH][rR][eE][fF].*?=.*?(\"|')","");url = url.replaceAll("(\"|').*>", "").trim();
				Log.i(G,"Found Link ("+alt+") ("+url+")");
				t.setMinimumHeight(70);
				dt.setText(Uri.encode(url));
				
				if(!shl[line+1].startsWith("<")){
					t.setText((shl[line+1].length()>0?shl[line+1]:url));//(url.length()>60?url.substring(0,60)+"...":url));
					//t.setLines(4);t.setEllipsize(TextUtils.TruncateAt.END);
					RelativeLayout r2 = (RelativeLayout) mContent.getChildAt(row+1);
					r2.setBackgroundColor(Color.argb(205,80,90,85));
					r2.setVisibility(r2.getVisibility()==View.VISIBLE?View.GONE:r2.getVisibility());
					//r2.setBackgroundColor(Color.argb(125,30,90,60));
					r.setBackgroundColor(Color.argb(215,30,90,60));
					
				}else{
					t.setText((alt.length()>0?alt:url));
					r.setBackgroundColor(Color.argb(205,30,90,40));
					
				}

				if(shl[line+1].toLowerCase().matches("<img.*>")){
					//RelativeLayout r2 = (RelativeLayout) mContent.getChildAt(row+1);
					//ImageView tol2 = (ImageView) r2.getChildAt(3);
					//ImageView tor2 = (ImageView) r2.getChildAt(4);
					//tol2.setVisibility(tol2.getVisibility()==View.VISIBLE?View.GONE:tol2.getVisibility());
					//tor2.setVisibility(tor2.getVisibility()==View.VISIBLE?View.GONE:tor2.getVisibility());

					bol.setVisibility(bol.getVisibility()==View.VISIBLE?View.GONE:bol.getVisibility());
					bor.setVisibility(bor.getVisibility()==View.VISIBLE?View.GONE:bor.getVisibility());
										
				}
				c.setImageResource(R.drawable.ic_menu_forward);
				c.setVisibility(View.VISIBLE);
			
			}else if(lc.matches("<(iframe|object|embed) .*?(data|src).*?=.*?(\"|').*?(\"|').*>")){
			
				String nw = Uri.decode(cx);
				String url = "";String alt = "";
			
				if(lc.matches("<.*? title.*?=.*?(\"|').*?(\"|').*>")){
					alt = nw.replaceAll("<.*? [tT][iI][tT][lL][eE].*?=.*?(\"|')","");alt = alt.replaceAll("(\"|').*>", "").trim();
				}
				url = nw.replaceAll("<.*? [sS][rR][cC].*?=.*?(\"|')","");url = url.replaceAll("(\"|').*>", "").trim();
				Log.i(G,"Found iframe ("+alt+") ("+url+")");
				
				dt.setText(Uri.encode(url));
				
				t.setText((alt.length()>0?alt+"\n":url) );
				//VideoView bv = new VideoView(mCtx);
				//bv.setVideoPath(url);
				WebView bv = new WebView(mCtx);
				RelativeLayout.LayoutParams rlx = new RelativeLayout.LayoutParams(-1,(int)(wWidth+90));
				rlx.setMargins(0,30,0,60);
				bv.setMinimumHeight((int)(wWidth/2));
				bv.setLayoutParams(rlx);
				bv.loadUrl(url);
				bv.getSettings().setJavaScriptEnabled(true);
				
				bv.setBackgroundColor(Color.argb(105,0,0,0));
				vr.setVisibility(View.VISIBLE);
				vr.addView(bv);
				r.setBackgroundColor(Color.argb(205,30,30,60));
				r.setVisibility(View.GONE);
				
				
				
			}else if(lc.matches("</(a|span|div|blockquote|em).*>") || lc.matches("<(span |b|hr|div|li|ul|blockquote|hr|em|div|object).*>") ){
				r.setBackgroundColor(Color.argb(205,90,90,90));
				r.setVisibility(r.getVisibility()==View.VISIBLE?View.GONE:r.getVisibility());
				//Log.i(G,"Learn from what you don't know.");
				
				
			}else if(lc.length() >0){
				
				if(row < mContent.getChildCount()-1){
					//RelativeLayout r2 = (RelativeLayout) mContent.getChildAt(row+1);
					//ImageView tol2 = (ImageView) r2.getChildAt(3);
					//ImageView tor2 = (ImageView) r2.getChildAt(4);
					//tol2.setVisibility(tol2.getVisibility()==View.VISIBLE?View.GONE:tol2.getVisibility());
					//tor2.setVisibility(tor2.getVisibility()==View.VISIBLE?View.GONE:tor2.getVisibility());
				}else{
					Log.e(G,"end of document?"+lc);
				}
				dt.setText(cx);
				
				
				//tol.setVisibility(tol.getVisibility()==View.VISIBLE?View.GONE:tol.getVisibility());
				//tor.setVisibility(View.GONE);
				bol.setVisibility(bol.getVisibility()==View.VISIBLE?View.GONE:bol.getVisibility());
				bor.setVisibility(bor.getVisibility()==View.VISIBLE?View.GONE:bor.getVisibility());
				
				//if(row > 1){
				//RelativeLayout r0 = (RelativeLayout) mContent.getChildAt(row-1);
				//LinearLayout.LayoutParams r0l = new LinearLayout.LayoutParams(-1,-2);
				//r0l.setMargins(0,2,0,1);
				//r0.setLayoutParams(r0l);
				//ImageView bor0 = (ImageView) r0.getChildAt(5);//confirmed
				//ImageView bol0 = (ImageView) r0.getChildAt(6);
				//bor0.setVisibility(bor0.getVisibility()==View.VISIBLE?View.GONE:bor0.getVisibility());
				//bol0.setVisibility(bol0.getVisibility()==View.VISIBLE?View.GONE:bol0.getVisibility());
				//}
				
			}
			
			
			
			
			if(row > 0){	
				
				RelativeLayout prr = null;
				int pr = row -1;for(pr = row-1;pr>0;pr--){prr = (RelativeLayout) mContent.getChildAt(pr); if(prr.getVisibility() == View.VISIBLE){break;} 
				if(r.getVisibility() == View.GONE){
					ImageView bol2 = (ImageView) prr.getChildAt(6);
					ImageView bor2 = (ImageView) prr.getChildAt(5);
					tol.setVisibility(bol2.getVisibility());//==View.VISIBLE?View.GONE:tol2.getVisibility());
					tor.setVisibility(bor2.getVisibility());
				
				}
				}
				
				{
				RelativeLayout r0 = (RelativeLayout) mContent.getChildAt(pr);
				if(r0.getVisibility() == View.VISIBLE && r.getVisibility() == View.VISIBLE){
				ImageView bol2 = (ImageView) r0.getChildAt(6);
				ImageView bor2 = (ImageView) r0.getChildAt(5);
				tol.setVisibility(bol2.getVisibility());//==View.VISIBLE?View.GONE:tol2.getVisibility());
				tor.setVisibility(bor2.getVisibility());
				
				}
			
				}
				
				
			}
			Log.i(G,"softFilter row("+row+"/"+mContent.getChildCount()+")");
			if(row == mContent.getChildCount()-1 && row > 0){	
				
				//RelativeLayout r2 = (RelativeLayout) mContent.getChildAt(row+1);
				//ImageView tol2 = (ImageView) r2.getChildAt(3);
				//ImageView tor2 = (ImageView) r2.getChildAt(4);
				if(r.getVisibility() == View.GONE){
				
				RelativeLayout prr = null;
				int pr = row -1;for(pr = row-1;pr>0;pr--){prr = (RelativeLayout) mContent.getChildAt(pr); if(prr.getVisibility() == View.VISIBLE){break;} }
				RelativeLayout r0 = (RelativeLayout) mContent.getChildAt(pr);
				
				if(r0.getVisibility() == View.VISIBLE){
				ImageView bol2 = (ImageView) r0.getChildAt(6);
				ImageView bor2 = (ImageView) r0.getChildAt(5);
				bol2.setVisibility(View.VISIBLE);
				bor2.setVisibility(View.GONE);
				}
			
				}else{
				bol.setVisibility(View.VISIBLE);
				bor.setVisibility(View.GONE);
					
					
				}
				
			}
		}
	};
	
	
	Handler runFilter = new Handler(){
	public void handleMessage(Message msg){
		Bundle bdl = msg.getData();
		int id = bdl.getInt("id");
		int tid = bdl.getInt("tid");
		RelativeLayout vr = (RelativeLayout) findViewById(id);
		TextView t = (TextView) findViewById(tid);
		RelativeLayout r = (RelativeLayout) t.getParent();
		ScrollView stp = (ScrollView) r.getChildAt(1);
		ImageView iv = (ImageView) r.getChildAt(7);
		
		String nw = t.getText().toString();
		Log.i(G,"runFilter " + r.getChildCount() + " ("+nw+")");
		
		String[] n2 = nw.split("\n",3);
		if(n2.length==3){nw = n2[2];}else if(n2.length==2){nw = n2[1];}
		SharedPreferences mReg = getSharedPreferences("Preferences", MODE_WORLD_READABLE);
	
		String bsli = "com.havenskys.galaxy.VIEW;HS;"+R.drawable.ic_menu_forward+";HS;Read;HS;extra:HS:extrauri:HS:%uri%;HS;extralong:HS:id:HS:%id%;HS;type:HS:vnd.android.cursor.item/moment";
		bsli += "\nandroid.intent.action.SEND;HS;"+android.R.drawable.ic_menu_send+";HS;Send;HS;extra:HS:"+Intent.EXTRA_TEXT+":HS:%0a%0a%0a%text%%0a%0a%published%%0a%title%%0a%link%;HS;extra:HS:"+Intent.EXTRA_SUBJECT+":HS:%title%;HS;extrauri:HS:"+Intent.EXTRA_STREAM+":HS:%file1%;HS;type:HS:message/rfc822";
		bsli += "\ncom.twidroid.SendTweet;HS;"+R.drawable.twitter+";HS;;HS;extra:HS:com.twidroid.extra.MESSAGE:HS:%0a%0a%stext%%0a%text%%0a%0a%title%%0a%link%;HS;type:HS:application/twitter";
		bsli += "\ncom.google.android.photostream.FLICKR_PHOTO;HS;"+R.drawable.flickr+";HS;;HS;extra:HS:com.google.android.photostream.photo:HS:%flickr%";				
		bsli += "\n"+Intent.ACTION_VIEW+";HS;"+android.R.drawable.ic_menu_mapmode+";HS;Map;HS;uri:HS:geo:0,0?q="+nw.trim().replaceAll(" ","+")+";HS;";
		bsli += "\n"+Intent.ACTION_WEB_SEARCH+";HS;"+android.R.drawable.ic_search_category_default+";HS;Search;HS;extra:HS:"+SearchManager.QUERY+":HS:%text%";//;HS;type:HS:text/plain";//;HS;title:HS:%text%";
		bsli += "\n"+Intent.ACTION_SEND+";HS;"+android.R.drawable.ic_menu_share+";HS;This;HS;extra:HS:"+Intent.EXTRA_SUBJECT+":HS:%title%;HS;extra:HS:"+Intent.EXTRA_TEXT+":HS:%text%;HS;type:HS:text/plain;HS;title:HS:%text%";
		bsli += "\n"+Intent.ACTION_SEND+";HS;"+android.R.drawable.ic_menu_share+";HS;Share;HS;extra:HS:;HS;extra:HS:"+Intent.EXTRA_SUBJECT+":HS:%title%"+Intent.EXTRA_TEXT+":HS:%0a%0a%0a%0a%published%%0a%title%%0a%link%%0a%0a%content%%0a;HS;type:HS:text/plain;HS;title:HS:%text%";
		bsli += "\n"+Intent.ACTION_EDIT+";HS;"+android.R.drawable.ic_menu_today+";HS;Schedule;HS;extrabeginms:HS:beginTime:HS:%text%;HS;extraendms:HS:endTime:HS:%text%;HS;extra:HS:title:HS:%title%;HS;extra:HS:description:HS:%0a%0a%title%%0a%text%%0a%link%%0a%0a;HS;type:HS:vnd.android.cursor.item/event";
		bsli += "\ncom.havenskys.galaxy.DATA;HS;"+android.R.drawable.ic_menu_manage+";HS;Moment;HS;extra:HS:uri:HS:%uri%";
		bsli += "\nINSERT;HS;"+android.R.drawable.ic_menu_save+";HS;Acquire;HS;extra:HS:location:HS:%text%;HS;extraint:HS:status:HS:2;HS;extra:HS:title:HS:%stext%;HS;extra:HS:filtercode:HS:itemsplit <html%0aparse_content <body%0aparse_title <title;HS;uri:HS:"+Uri.withAppendedPath(DataProvider.CONTENT_URI, "filter")+";HS;service:HS:com.havenskys.galaxy.AutomaticService:HS:restart";//
		bsli += "\ncom.havenskys.galaxy.DATA;HS;"+android.R.drawable.ic_menu_manage+";HS;Source;HS;extra:HS:uri:HS:%source%";
		
		
		
		//
		
		//"geo:0,0?q="+nw.replaceAll("  "," ").trim().replaceAll(" ","+")
		String ilist = mReg.getString("intent_list",bsli);
		
		RelativeLayout.LayoutParams ivr = new RelativeLayout.LayoutParams(90,90);
		ivr.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		ivr.setMargins(0,60,4,0);
		iv.setLayoutParams(ivr);
		
		int height = 75;
		LinearLayout lg = new LinearLayout(mCtx);
		lg.setOrientation(LinearLayout.VERTICAL);
		lg.setId((int)SystemClock.uptimeMillis());
		
		String[] il = ilist.split("\n");
		int rowcnt = 0;int colcnt = 0;
		LinearLayout lgrow = null;
		for(int ic = 0; ic < il.length; ic++){
			rowcnt ++;//(iv.getVisibility()==View.VISIBLE&&colcnt<3?3:0)
			if(( ((colcnt<3?1:0)+rowcnt)*65) >= wWidth){	
				rowcnt = 1;
			}
			if(rowcnt == 1){colcnt++;
				lgrow = new LinearLayout(mCtx);
				lgrow.setLayoutParams(new LinearLayout.LayoutParams(-1,height));
				lgrow.setPadding(25,0,0,0);
				lgrow.setOrientation(LinearLayout.HORIZONTAL);
				//lgrow.setMinimumHeight(height);
				lgrow.setId((int)SystemClock.uptimeMillis());
				lg.addView(lgrow);
			}
				
			
			String[] ir = il[ic].split(";HS;");
			final String action = ir[0];
			String clicker = ir[1];
			String text = ir[2];
			final String ill = il[ic];
			
			int rsid = -2;
			try{
			rsid = Integer.parseInt(ir[1]);}catch(NumberFormatException re){rsid = -1;}
			
			RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(45,45);
			
			ImageView ibx = new ImageView(mCtx);
			ibx.setId((int)SystemClock.uptimeMillis());
			ibx.setLayoutParams(lp);
			ibx.setAdjustViewBounds(true);
			ibx.setScaleType(ScaleType.CENTER_INSIDE
					);
			if(rsid > 0){
				try{
				ibx.setImageResource(rsid);
				}catch(OutOfMemoryError o7){o7.printStackTrace();rsid = -10;}
			}
			if(rsid <= 0){
				// set drawable with filepath
				Drawable db = Drawable.createFromPath(clicker);
				ibx.setImageDrawable(db);
			}
			LinearLayout.LayoutParams lmx = new LinearLayout.LayoutParams(65,75);
			//lmx.weight = 1;
			//lmx.setMargins(3, 1, 3, 2);
			RelativeLayout.LayoutParams rmx = new RelativeLayout.LayoutParams(-1,-2);
			rmx.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
			RelativeLayout rm = new RelativeLayout(mCtx);rm.setLayoutParams(lmx);
			rm.setBackgroundResource(R.drawable.tricli);
			rm.setPadding(3,5,3,3);
			rm.setClickable(true);//rm.setBackgroundColor(coloroff	);
			rm.setFocusable(true);final String tl = nw;final int rid = r.getId();
			rm.setOnClickListener(new OnClickListener(){public void onClick(View v){RelativeLayout rf = (RelativeLayout) v;rf.setBackgroundResource(R.drawable.tricligo);Bundle bl = new Bundle(); bl.putString("action",action); bl.putString("line",ill); bl.putString("text",tl); bl.putInt("id",rid); Message ml = new Message(); ml.setData(bl); codeTrigger.sendMessageDelayed(ml,10);}});
			rm.setOnFocusChangeListener(new OnFocusChangeListener(){public void onFocusChange(View v, boolean hasfocus){RelativeLayout rf = (RelativeLayout) v;if(hasfocus){rf.setBackgroundResource(R.drawable.triclion	);}else{rf.setBackgroundResource(R.drawable.tricli);}}});
			rm.setOnTouchListener(new OnTouchListener(){public boolean onTouch(View v, MotionEvent ev) {if(ev.getAction() == MotionEvent.ACTION_DOWN){RelativeLayout rf = (RelativeLayout) v;if(rf.hasFocus()){}else{rf.requestFocusFromTouch();rf.setBackgroundResource(R.drawable.triclion);return true;}}else if(ev.getAction() == MotionEvent.ACTION_UP){RelativeLayout rf = (RelativeLayout) v;rf.setBackgroundResource(R.drawable.tricliup);}else if(ev.getAction() == MotionEvent.ACTION_OUTSIDE || ev.getAction() == MotionEvent.ACTION_CANCEL){RelativeLayout rf = (RelativeLayout) v;rf.setBackgroundResource(R.drawable.tricli);}return false;}});
			TextView t1 = new TextView(mCtx);t1.setLayoutParams(rmx);
			t1.setGravity(Gravity.CENTER);
			t1.setPadding(3, 1, 3, 0);
			t1.setText(text);t1.setTextSize((float)12.0);
			t1.setTextColor(textcolor);
			//t1.setLines(2);
			RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(-1,-2);
			LinearLayout l2 = new LinearLayout(mCtx);
			l2.setLayoutParams(rlp);
			l2.setOrientation(LinearLayout.HORIZONTAL);
			l2.setGravity(Gravity.CENTER);
			
			
			
			l2.addView(ibx);
			rm.addView(l2);
			
			rm.addView(t1);
			lgrow.addView(rm);
			
			
			
		}

		//LinearLayout.LayoutParams lcr = new LinearLayout.LayoutParams(-1,(2+colcnt*(height+20))+colcnt+90);//(colcnt*height)+colcnt);
		//lcr.setMargins(0,1,0,0);
		//r.setLayoutParams(lcr);
		
		vr.addView(lg);
		//vr.postInvalidateDelayed(500);r.postInvalidateDelayed(1500);
		
		RelativeLayout.LayoutParams vll = new RelativeLayout.LayoutParams(-1,(2+colcnt*(height+1))+colcnt+90);
		vll.setMargins(0,30,0,60);
		lg.setLayoutParams(vll);
		
		//RelativeLayout.LayoutParams r3l = new RelativeLayout.LayoutParams(-1,(2+colcnt*(height+20))+colcnt+90);
		//r3l.addRule(RelativeLayout.ALIGN_TOP,t.getId());r3l.addRule(RelativeLayout.ALIGN_BOTTOM,stp.getId());r3l.addRule(RelativeLayout.ALIGN_RIGHT,t.getId());
		//r3l.setMargins(0,30,0,30);
		
		//vr.setLayoutParams(r3l);
		RelativeLayout.LayoutParams r3l2 = new RelativeLayout.LayoutParams(-1,(2+colcnt*(height+2))+colcnt+90);
		t.setLayoutParams(r3l2);

		
		
	}		
	};
	
	
	long moment;String rlink;String uri;String rpublished = "";String rtext= ""; String rtitle = "";String rsource = "";String rimages = "";String dtext = "";String rtextcontent = ""; String rurls = ""; String rjs = "";String rcontent = ""; int rstatus = -2; String rauthor = "";
	private Handler codeTrigger = new Handler(){
		public void handleMessage(Message msg){
		
			Bundle bdl = msg.getData(); String il = bdl.getString("line");String action = bdl.getString("action");String[] ir = il.split(";HS;");
		
			String text = bdl.containsKey("text")?bdl.getString("text"):"";
			int id = bdl.getInt("id");
			RelativeLayout r = (RelativeLayout) findViewById(id);
			
			TextView t = (TextView) r.getChildAt(0);
			RelativeLayout vr = (RelativeLayout) r.getChildAt(2);
			vr.setVisibility(View.GONE);
			TextView dt = (TextView) r.getChildAt(8);
			TextView dx = (TextView) r.getChildAt(9);
			

			if(text.length() > 0){rtext = text;}
			else if(t!=null && t.length() > 0){rtext = t.getText().toString();}
			else{rtext = rtitle + "\n" + rcontent;}
			dtext = Uri.decode(dt.getText().toString());
			
			
		if(action.toUpperCase().contentEquals("INSERT")){
			Log.i(G,"codr action INSERT");
			//bsli += "\nINSERT;HS;"+android.R.drawable.ic_menu_save+";HS;Acquire;HS;extra:HS:location:HS:%text%;HS;extraint:HS:status:HS:2;HS;extra:HS:title:HS:%stext%;HS;extra:HS:uri:HS:"+Uri.withAppendedPath(DataProvider.CONTENT_URI, "filter")+"";
		

			ContentValues inc = new ContentValues();
			Uri geturi = null;
			for(int in = 3; in < ir.length; in++){
				String[] pt = ir[in].split(":HS:",3);
				if(pt.length > 2){
					pt[2] = pt[2].replaceAll("%title%", rtitle).replaceAll("%source%", rsource).replaceAll("%uri%", uri).replaceAll("%content%",rcontent).replaceAll("%stext%",rtext).replaceAll("%text%",dtext).replaceAll("%link%",rlink).replaceAll("%published%", rpublished).replaceAll("%0a","\n");
				}
				if(pt[0].contentEquals("uri")){
					Log.i(G,"coder uri " + pt[1]);
					geturi = Uri.parse(pt[1]);
				} else if(pt[0].contains("extraint") && pt[2].matches("[0-9]+")){
					inc.put(pt[1],Integer.parseInt(pt[2]));
				}else if(pt[0].contentEquals("service")){
					String a3 = pt[1];
					String a3a = pt[2];
					if(a3a.contentEquals("restart")){
						Intent sr = new Intent();
						try {
						sr.setClass(mCtx, Class.forName(a3));
						}catch(ClassNotFoundException e){e.printStackTrace();}
						//sr.setClassName(a3.substring(0,a3.lastIndexOf('.')), a3.substring(a3.lastIndexOf('.')+1)+".class");//:HS:restart
						if(stopService(sr)){
						
							startService(sr);
							
						}else{Log.e(G,"start service() failed retrying");startService(sr);}
						
						
					
					}
						
				} else if(pt[0].contains("extra")){
					inc.put(pt[1],pt[2]);
				}
			}
		
			Uri source = SqliteWrapper.insert(mCtx, getContentResolver(), geturi, inc);//Uri.withAppendedPath(DataProvider.CONTENT_URI, "filter"), inc);
			Log.i(TAG,"Created " + source.toString());
    		
			
		}else{
		
		Intent coder = null; coder = new Intent(action);
		
		String codertitle = "";
		Log.i(G,"codr action " + action + ": " + il + " ("+text+") " + r.getChildCount() );
		
		
		
		boolean overridetype = false;
		for(int in = 3; in < ir.length; in++){
			String[] pt = ir[in].split(":HS:",3);
			if(pt[0].contentEquals("uri")){
				Log.i(G,"coder uri " + pt[1]);
				coder = new Intent(action,Uri.parse(pt[1]));
			}else if(pt[0].contentEquals("service")){
				String a3 = pt[1];
				String a3a = pt[2];
				if(a3a.contentEquals("restart")){
					Intent sr = new Intent(a3);
					
					if(stopService(sr)){
					
						startService(sr);
						
					}else{Log.e(G,"start service() failed retrying");startService(sr);}
					
					
				
				}
					
			} else if(pt[0].contains("extra")){
				SharedPreferences mReg = getSharedPreferences("Preferences", MODE_WORLD_READABLE);
				
				if(pt[2].contentEquals("%flickr%")){
					
					int s = rtext.indexOf("http://www.flickr.com/photos/");
					if(s > 0){
						String flickr = rtext.replaceAll(".*a href=\"http://www.flickr.com/photos/", "http://www.flickr.com/photos/");
						flickr = flickr.replaceAll("\".*", "");
						Log.w(G,"Flickr " + flickr);
				//		com.google.android.photostream.Flickr.Photo photo = null;
						
						Toast.makeText(mCtx,"Flickr URL found but not displaying yet",2300).show();return;
						//coder.putExtra(pt[1], flickr);
					}else{Toast.makeText(mCtx,"No Flickr URL Found",2300).show();return;}
				
				}else if(pt[0].matches("extra(end|begin)ms")){
					Log.i(G,"Determining time from ("+pt[0]+")");
					String gbt = rtext.toLowerCase().trim();
					gbt = gbt.replaceAll(","," "); 
					gbt = gbt.replaceAll(" +"," ");
					
					gbt = gbt.replaceAll("(sun|mon|tue|wed|thu|sat|fri)([a-z])+","\n").trim();
					String gts[] = gbt.split("\n");
					String gbt1 = gts[0].trim();
					String gbt2 = gts.length > 1?gts[1].trim():"";
					
					//Log.i(TAG,"found date ("+gbt1+" - "+gbt2+") "+addtag_whentitle.replaceAll("\n","%0a")+" " + addtag_contact.replaceAll("\n","%0a") + " " + bt.replaceAll("\n","%0a") + " "+ nw.replaceAll("\n","%0a") + "  tag " + mTag);
					Date xp = new Date();
					Date p = calDate(gbt1,xp.getTime());
					Date p2 = calDate(gbt2,p.getTime() + (10 * 60 * 1000) );
					
					if(pt[0].contains("end")){
					coder.putExtra(pt[1],p2.getTime());
						
					}else{
						coder.putExtra(pt[1],p.getTime());
						
					}
				
				
				}else if(pt[0].contentEquals("extradatatype")){
					//Uri uri2 = ContentUris.withAppendedId(getIntent().getData(), moment);
					//Log.w(G,"extradatatype sending to " + uri+"");
					//coder.setDataAndType(Uri.parse(uri), "text/*");
				
				}else if(pt[0].contentEquals("extrauri")){
					
					//Log.w(G,"Would have included file " + mReg.getString("image_"+moment+"_"+1,""));
					//if(!mReg.contains("image_"+moment+"_1")){
						//continue;
					//}
					/*	
					if(pt[1].contentEquals(Intent.EXTRA_STREAM)){
					String typ = mReg.getString("image_"+moment+"_1","");
					typ = typ.substring(typ.lastIndexOf('.')+1).toLowerCase();
					coder.setType("image/"+typ);
					overridetype = true;
					}/*/
					//for(int inb = 1; inb <=30; inb++){
						//if(mReg.contains("image_"+moment+"_"+inb)){
					
					String[] fs = rimages.split("\n");File file1 = null;File file1c = null;
					if(dtext.matches(".*?://.*")){
						file1 = new File(getfilepath()+"/"+dtext.replaceAll(".*?://", "").replaceAll("\\?.*","").replaceAll("(:)", "_"));
						Log.w(G,"test file1 from dtext " + file1.getAbsolutePath());
					}else{
						file1 = new File(getfilepath()+"/"+rtext.replaceAll(".*?://", "").replaceAll("\\?.*","").replaceAll("(:)", "_"));
						Log.w(G,"test file1 from rtext " + file1.getAbsolutePath());
					}
					if(!file1.exists()){
					for(int fi = 0; fi < fs.length; fi++){
					String filename = getfilepath() + "/" + fs[0].replaceAll(".*?://", "").replaceAll("\\?.*","").replaceAll("(:)", "_");
					String ni = getfilepath().getAbsolutePath() + "/compressed/"+filename.substring(0,filename.lastIndexOf('.')>0?filename.lastIndexOf('.'):filename.length()-1)+".jpg";
					file1 = new File(filename);
					file1c = new File(ni);
					if(file1.exists()){break;}Log.w(G,"File missing " + filename);
					
					
					}
					}
					if(rimages.length() == 0){			
						Bundle bl = new Bundle(); bl.putString("filename","i"+moment+".png");Message ml = new Message(); ml.setData(bl);snapshot.sendMessage(ml);		
						file1 = new File(getfilepath(),"screenshot/i"+moment+".png");
						File dr = new File(getfilepath(),"screenshot");dr.mkdirs();
						try {
							file1.createNewFile();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						file1c = new File(getfilepath(),"screenshot/i"+moment+".png");
					}
					if(pt[2].contentEquals("%file1%") && file1!=null && file1.exists()){		
						Log.i(G,"Including Attachement " + file1.getAbsolutePath());
						coder.putExtra(pt[1],Uri.parse("file://"+file1.getAbsolutePath()));
					}else if(!pt[2].contentEquals("%file1%")){	
						coder.putExtra(pt[1],Uri.parse(pt[2].replaceAll("%uri%", uri).replaceAll("%text%",dtext)));
					}
					
					//}else{break;}
					//}
				
				} else if(pt[0].contentEquals("extralong")){
					if(pt[2].contentEquals("%id%")){
					coder.putExtra(pt[1], moment);
					}
				} else {
					//if(rtitle == null || uri == null || rlink == null || published == null || moment == null){Log.w(G," " + rtitle + "\n"+rlink+"\n"+published);}
					//if(pt.length < 3){
					Log.e(G,"command 0(" + (pt.length>0?pt[0]:"") + ") 1" + (pt.length>1?pt[1]:"") + ") 2(" + (pt.length>2?pt[2]:"")+")"  );
					coder.putExtra(pt[1], pt[2].replaceAll("%title%", rtitle).replaceAll("%source%", rsource).replaceAll("%uri%", uri).replaceAll("%content%",rcontent).replaceAll("%stext%",rtext).replaceAll("%text%",dtext).replaceAll("%link%",rlink).replaceAll("%published%", rpublished).replaceAll("%0a","\n"));
				}
			}else if( pt[0].contentEquals("title") ){
				codertitle = pt[1].replaceAll("%title%", rtitle).replaceAll("%uri%", uri).replaceAll("%content%",rcontent).replaceAll("%stext%",rtext).replaceAll("%text%",dtext).replaceAll("%link%",rlink).replaceAll("%published%", rpublished).replaceAll("%0a","\n");
			}else if(pt[0].contains("type") && !overridetype){ 
				coder.setType(pt[1]);overridetype = true;
			}
		
		}
		
		
		

		coder.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS | Intent.FLAG_ACTIVITY_NO_HISTORY);
		if(overridetype){
		startActivity(Intent.createChooser(coder, (codertitle.length()>0?codertitle:coder.getType())));
		}else{
		try{
			startActivity(coder);
		}catch (ActivityNotFoundException ea){
			Log.e(G,"Activity not found");easyStatus("Activity("+action+") Not Found");
		}
		}
		
		}
		
		
		
		
		}
	};
	
	
	
	Handler setClickOff = new Handler(){public void handleMessage(Message msg){Bundle bdl = msg.getData(); int ix = bdl.getInt("id"); Log.w(G,"clickOff "+ix);for(int i = 0;i<clickOff.length;i++){ if(clickOff[i] == 0){ clickOff[i] = ix;break;} }}};
	
	int[] clickOff;
	Handler fullFilter = new Handler(){
		
		public void handleMessage(Message msg){
			Bundle bdl = msg.getData();
			int id = bdl.getInt("id");
			int prog = bdl.getInt("prog",1);
			int tid = bdl.getInt("tid");
			
			RelativeLayout vr = (RelativeLayout) findViewById(id);
			TextView t = (TextView) findViewById(tid);
			RelativeLayout r = (RelativeLayout) t.getParent();
			if(r == null){Log.e(G,"fill Filter no r");return;}
			ScrollView s = (ScrollView) r.getChildAt(1);
			if(s == null){Log.e(G,"fill Filter no s " + r.getChildCount() );return;}
			
			boolean clickoff = false;
			int g = 0;
			for(g = 0; g < clickOff.length; g++){if(clickOff[g] == vr.getId()){clickoff = true;break;} }
			if(prog == 1 && !clickoff){
			vr.setOnClickListener(new OnClickListener(){public void onClick(View v){RelativeLayout vr = (RelativeLayout)v; vr.setBackgroundColor(Color.argb(180, 120, 0, 30));{Bundle bl = new Bundle(); bl.putInt("id",vr.getId()); Message ml = new Message();ml.setData(bl);setClickOff.sendMessage(ml);}}});
			}
			
			if(prog < 16 && prog > 0){
				RelativeLayout.LayoutParams vrl = new RelativeLayout.LayoutParams((wWidth/15)*prog,-1); 
				vrl.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);vrl.addRule(RelativeLayout.ALIGN_TOP,t.getId());vrl.addRule(RelativeLayout.ALIGN_BOTTOM,s.getId());
				
				vr.setLayoutParams(vrl);
				{Bundle bl = new Bundle(); bl.putInt("prog",prog+(clickoff?-1:1)); bl.putInt("id", vr.getId()); bl.putInt("tid", t.getId()); Message ml = new Message(); ml.setData(bl); fullFilter.sendMessageDelayed(ml,70); }
				Log.w(G,"fill a " + prog);
				return;
			}else if(clickoff){
				clickOff[g] = 0;
				Log.w(G,"Click off done");
				vr.setVisibility(View.GONE);
				t.setVisibility(View.VISIBLE);
			}else{
				Log.w(G,"fill b");
				RelativeLayout.LayoutParams vrl = new RelativeLayout.LayoutParams(-1,-1); 
				vrl.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);vrl.addRule(RelativeLayout.ALIGN_TOP,t.getId());vrl.addRule(RelativeLayout.ALIGN_BOTTOM,s.getId());
				vr.setLayoutParams(vrl);
				vr.setBackgroundColor(Color.argb(105,205,102,34));
				t.setVisibility(View.VISIBLE);
			
				
				{Bundle bl = new Bundle(); bl.putInt("id", vr.getId()); bl.putInt("tid", t.getId()); Message ml = new Message(); ml.setData(bl); runFilter.sendMessageDelayed(ml,70); }
				
				
			}	
		
		}
	};
	
	
	OnClickListener clickFilter = new OnClickListener(){
		
		public void onClick(View v){
			TextView t = (TextView) v;
			RelativeLayout r = (RelativeLayout) t.getParent(); 
			RelativeLayout r3 = (RelativeLayout) r.getChildAt(2);
			ScrollView s = (ScrollView) r.getChildAt(1);
			Log.i(G,"clickFilter " + r3.getChildCount());
			int cr = r3.getChildCount();
			t.setVisibility(View.INVISIBLE);
			
			if(cr == 0){
				RelativeLayout vr = new RelativeLayout(mCtx);
				vr.setId((int)SystemClock.uptimeMillis());
				
				RelativeLayout.LayoutParams vrl = new RelativeLayout.LayoutParams(wWidth/15,-1); 
				vrl.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);vrl.addRule(RelativeLayout.ALIGN_TOP,t.getId());vrl.addRule(RelativeLayout.ALIGN_BOTTOM,s.getId());
				vr.setLayoutParams(vrl);
				
				vr.setBackgroundColor(Color.argb(105, 160, 190, 190));
				r3.addView(vr);
				{Bundle bl = new Bundle(); bl.putInt("id", vr.getId()); bl.putInt("tid", t.getId()); Message ml = new Message(); ml.setData(bl); fullFilter.sendMessage(ml); }
				Log.w(G,"click a");
			}else{
				RelativeLayout vr = (RelativeLayout) r3.getChildAt(0);
				RelativeLayout.LayoutParams vrl = new RelativeLayout.LayoutParams(wWidth/15,-1); 
				vrl.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);vrl.addRule(RelativeLayout.ALIGN_TOP,t.getId());vrl.addRule(RelativeLayout.ALIGN_BOTTOM,s.getId());
				vr.setLayoutParams(vrl);
				vr.setVisibility(View.VISIBLE);
				{Bundle bl = new Bundle(); bl.putInt("id", vr.getId()); bl.putInt("tid", t.getId()); Message ml = new Message(); ml.setData(bl); fullFilter.sendMessage(ml); }
				Log.w(G,"click b");
			}
			
			
		}
	};
	
	
	
	
	
	
	
	
	private Handler imgPath = new Handler(){
	
	public void handleMessage(Message msg){
	Bundle bdl = msg.getData();
	int id = bdl.getInt("id");
	long moment = bdl.getLong("moment");
	String url = bdl.getString("url");
	final String filename = url.replaceAll(".*?://", "").replaceAll("\\?.*","").replaceAll("(:)", "_");
	File file = null;
	file = getfilepath();
	
	String ni = "compressed/"+filename.substring(0,filename.lastIndexOf('.'))+".jpg";
	//String ni = filename.replaceAll("\\.","_");
    //ni += ".png";
    Log.i(G,"Using png " + ni);
	
	RelativeLayout r = (RelativeLayout) findViewById(id);
	r.setBackgroundColor(Color.MAGENTA);
	ImageView iv = (ImageView) r.getChildAt(7);
	Log.i(G,"imgPath("+url+") id("+iv.getId()+")");
	
	File fi2 = new File(file.getAbsolutePath(),ni);									
	File fi = new File(file.getAbsolutePath(),filename);
	if( !fi.exists() || !fi2.exists() ){
		iv.setVisibility(View.VISIBLE);
		iv.setImageResource(R.drawable.iget_1);
		{Message ml = new Message(); Bundle bl = new Bundle(); bl.putInt("id",iv.getId()); bl.putString("dest", url); bl.putString("storloc",filename); bl.putLong("moment",moment); ml.setData(bl); mGet.sendMessageDelayed(ml,75);}
	}
	
	
	r.setOnClickListener(new OnClickListener(){public void onClick(View v){
		
		File file = null;
		file = getfilepath();									
		File fi = new File(file.getAbsolutePath(),filename);
		if(fi.exists()){
		String f = fi.getAbsolutePath();
		String tp = f.substring(f.lastIndexOf('.')+1).toLowerCase();
		Intent d = new Intent(Intent.ACTION_VIEW);d.setDataAndType(Uri.fromFile(new File(f)),"image/"+(tp.length() <=4 && tp.length() > 2?tp:"png"));
		startActivity(d);
		}
		}});
	
	{Bundle bl = new Bundle(); bl.putString("filename", ni); bl.putInt("click",r.getId());bl.putInt("image",iv.getId());Message ml = new Message(); ml.setData(bl); watchImage.sendMessageDelayed(ml,1000);}
	
	
	}
	};
	
	
	
	
	private Handler watchImage = new Handler(){
		
		public void handleMessage(Message msg){
		
			Bundle bdl = msg.getData();
			int image = bdl.getInt("image");
			int click = bdl.getInt("click");
			int inst = bdl.getInt("instate",1);
			String filename = bdl.getString("filename");
			File file = null;
			file = getfilepath();
			File fi = new File(file.getAbsolutePath(),filename);
			Log.w(G,"watchImage [" + inst + "] " + fi.getAbsolutePath() );
			ImageView iv = (ImageView) findViewById(image);
			if(iv == null){Log.e(G,"image missing "+ image);return;}
			RelativeLayout r = (RelativeLayout) findViewById(click);
			
			if(inst > 30){
				iv.setImageResource(R.drawable.iget_error);
				return;
			}
				
		
		if(!fi.exists()){
			Log.w(G,"get 2 [" + inst + "] "+(inst - (int)(inst/2)*2));
			if( inst - (int)(inst/2)*2 == 1){
			//if( (float)((float)(inst/2)-(int)(inst/2)) > (float)0.0 ){
				iv.setImageResource(R.drawable.iget_2o);
			}else{
				iv.setImageResource(R.drawable.iget_2);
			}
			{Bundle bl = new Bundle(); bl.putString("filename", filename); bl.putInt("click", click); bl.putInt("image",image); bl.putInt("instate",inst+1);Message ml = new Message(); ml.setData(bl); watchImage.sendMessageDelayed(ml,1000);}
			return;
		}

		try{
		Log.w(G,"create drawable from path " + fi.getAbsolutePath());
		Drawable fid = Drawable.createFromPath(fi.getAbsolutePath());
		if(fid == null){Log.e(G,"drawable missing "+ fi.getAbsolutePath());
			iv.setImageResource(R.drawable.iget_3);
			{Bundle bl = new Bundle(); bl.putString("filename", filename); bl.putInt("click", click); bl.putInt("image",image);bl.putInt("instate",inst+1);Message ml = new Message(); ml.setData(bl); watchImage.sendMessageDelayed(ml,3000);}
			return;
		}
		
		//if(fid==null){return;}
		iv.setImageDrawable(fid);

		RelativeLayout r3 = (RelativeLayout) r.getChildAt(2);
		Drawable xi = Drawable.createFromPath(fi.getAbsolutePath());
		r3.setBackgroundDrawable(xi);
		}catch(OutOfMemoryError eom){iv.setImageResource(R.drawable.iget_error);return;}
		
		r.setMinimumHeight(120);//(int)(wWidth/1.7)
		TextView t1 = (TextView) r.getChildAt(0);
		ScrollView s1 = (ScrollView) r.getChildAt(1);
		//ImageView i1 = (ImageView) r.getChildAt(3);
		//ImageView i2 = (ImageView) r.getChildAt(4);
		//ImageView i3 = (ImageView) r.getChildAt(5);
		//ImageView i4 = (ImageView) r.getChildAt(6);
		//i1.setVisibility(View.GONE);//TL
		//i2.setVisibility(View.GONE);//TR
		//i3.setVisibility(View.GONE);//BR
		//i4.setVisibility(View.GONE);//BL
		
		//ivr.addRule(RelativeLayout.ALIGN_BOTTOM,s1.getId());
		iv.setVisibility(View.VISIBLE);
		RelativeLayout.LayoutParams ivr = new RelativeLayout.LayoutParams(90,90);
		
		ivr.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		ivr.setMargins(0,60,4,0);//i7
		//iv.setAdjustViewBounds(true);
		iv.setLayoutParams(ivr);
		//iv.setPadding(8,8,8,8);
		iv.setScaleType(ScaleType.CENTER_CROP);
		iv.setBackgroundColor(Color.argb(155,10,10,10));
		final int rid = r.getId();
		iv.setOnClickListener(new OnClickListener(){public void onClick(View v){RelativeLayout r = (RelativeLayout) findViewById(rid);r.performClick();}});
		//iv.postInvalidateDelayed(2000);
		
		//RelativeLayout.LayoutParams ivr2 = new RelativeLayout.LayoutParams(-1,-1);
		//t1.setLayoutParams(ivr2);
		
		t1.setBackgroundColor(Color.argb(180, 10, 10, 10));
	}
	};

	
	
	int mTag = 2;int mTog = 2;	String allimages = "";String[] shl;String allc = "";String allurl = "";String alljs = "";
	private Handler loadRecord = new Handler(){
		public void handleMessage(Message msg){
			Bundle bdl = msg.getData();
			uri = bdl.getString("uri");
			Uri geturi = Uri.parse(uri);
			moment = bdl.getLong("moment");
	//if( id == mLookup ){ return; }
	
			{Message ml = new Message(); Bundle bl = new Bundle(); bl.putString("text", "Loading"); bl.putInt("id", R.id.browser_title); ml.setData(bl); setText.sendMessage(ml);}
			{Message ml = new Message(); Bundle bl = new Bundle(); bl.putString("text", ""); bl.putInt("id", R.id.browser_date); ml.setData(bl); setText.sendMessage(ml);}
			
	//mTitle.setText("");
	//mDate.setText("");
	//easyLoadData("<html>Loading</html>");
	//easyLoadData("<html><body bgcolor=#000000 text=#e0e0e0 link=#0066cc vlink=#cc6600><h3><center>Loading</center></h1></body></html>");
	//mContent.loadData("<html><body bgcolor=#000000 text=#e0e0e0 link=#0066cc vlink=#cc6600><h3><center>Loading</center></h1></body></html>", "text/html", "UTF-8");
	//mContent.setVisibility(View.INVISIBLE);
	
			
	//
	// CUSOMIZED		
	//		
	Cursor lCursor = SqliteWrapper.query(mCtx, mCtx.getContentResolver(), geturi, //Uri.withAppendedPath(DataProvider.CONTENT_URI,"moment"), 
    		//new String[] { "_id", "address", "body", "strftime(\"%Y-%m-%d %H:%M:%S\", date, \"unixepoch\", \"localtime\") as date" },
    		//strftime("%Y-%m-%d %H:%M:%S"
    		new String[] {"_id", "title", "url", "strftime('%Y/%m/%d %H:%M',published) as published", "author", "content", "status","images","source","textcontent","urls","js"  },
			//new String[] { "_id", "address", "body", "date" },
    		"status > -10",
    		null, 
    		null);
	// EC
	
	if( lCursor == null ){Log.e(TAG," +++++ ");return;}
	startManagingCursor(lCursor);
	if( !lCursor.moveToFirst() ){Log.e(TAG," +++++000 ");return;}
	if( lCursor.getColumnCount() < 7 ){Log.e(TAG," 333");return;}
	
	//String title = null;
	//String link = null;
	//String published = null;
	//String author = null;
	String nextlink = "";
		
		
			
	//String content = null;
	//mLookup = link;
	/// <<<<<<<<<<<<<<<<<  LOOK HERE
	rtitle = lCursor.getString(1) != null ? Uri.decode(lCursor.getString(1)) : "";
	rlink = lCursor.getString(2) != null ? lCursor.getString(2) : "";
	rpublished = lCursor.getString(3) != null ? lCursor.getString(3) : "";
	rauthor = lCursor.getString(4) != null ? lCursor.getString(4) : "";
	rcontent = lCursor.getString(5) != null ? lCursor.getString(5) : "";
	int statusid = lCursor.getInt(0);
	rstatus = lCursor.getInt(6);
	rimages = lCursor.getString(7)!=null?lCursor.getString(7):"";
	rsource = lCursor.getString(8);
	rtextcontent = lCursor.getString(9)!=null?lCursor.getString(9):"";
	rurls = lCursor.getString(10)!=null?lCursor.getString(10):"";
	rjs = lCursor.getString(11)!=null?lCursor.getString(11):"";
	
	Charset charset = Charset.forName("ISO-8859-1");
	CharsetDecoder decoder = charset.newDecoder();
	CharsetEncoder encoder = charset.newEncoder();

	try {
	    // Convert a string to ISO-LATIN-1 bytes in a ByteBuffer
	    // The new ByteBuffer is ready to be read.
	    //ByteBuffer bbuf = encoder.encode(CharBuffer.wrap("a string"));

		ByteBuffer bbuf = ByteBuffer.wrap(rcontent.getBytes());
	    // Convert ISO-LATIN-1 bytes in a ByteBuffer to a character ByteBuffer and then to a string.
	    // The new ByteBuffer is ready to be read.
	    CharBuffer cbuf = decoder.decode(bbuf);
	    rcontent = cbuf.toString();
	} catch (CharacterCodingException e) {
	}

	
	
	
	//if(content.length() > 20 && content.indexOf("CDATA[") == 3){
		//content = content.substring(9, content.length() - 3);}
	//Log.w(TAG,"Found uri("+geturi+") title("+title+") content("+content+")");


	rcontent = rcontent.replaceAll("&lt;", "<").replaceAll("&gt;", ">").replaceAll("&amp;", "&").replaceAll("&quot;", "\"").replaceAll("&apos;", "'");
	rcontent = rcontent.replaceAll("&nbsp;", " ").replaceAll("&ndash;", "-").replaceAll("&amp;", "&");
	
	String ndata = "";
	String cdata = "";
	while(rcontent.indexOf("CDATA[") >= 0){

		Log.w(TAG,"CDATA " + rcontent+"\n\n");
					
		cdata = rcontent.substring(rcontent.indexOf("CDATA[",0)+6, rcontent.indexOf("]]>",0+6+1)>=0?rcontent.indexOf("]]>",0+6+1):rcontent.length()-1);
		ndata += cdata;
		rcontent = rcontent.substring(rcontent.indexOf("]]>",0+6+1)>=0?rcontent.indexOf("]]>",0+6+1):rcontent.length()-1, rcontent.length() - 1);
	

		
		if(rcontent.indexOf("CDATA[") < 0){
		break;
		}
	}
	
	//if(content.indexOf("CDATA[") >= 0){
	//cdata = content.substring(content.indexOf("CDATA[",0)+6, content.indexOf("]]>",0+6+1)>=0?content.indexOf("]]>",0+6+1):content.length()-1);
	//ndata = content.substring(0,content.indexOf("<![CDATA["));
	//if(ndata.length() < 5){ndata = cdata;	
	//ndata += cdata;	
	//}		
	if(ndata.length() > 0){/*Log.w(TAG,"NDATA " + ndata+"\n\n");*/ rcontent = ndata;}
				
				
	if(rcontent.contentEquals("unavail") ){rcontent = "";}
	//content = content.replaceAll("br>", "BR>");
	//if(content.indexOf("&ndash;") >0 ){content = content.replaceAll("&ndash;", "-");}
	//if(content.indexOf("&nbsp;") >0 ){content = content.replaceAll("&nbsp;", " ");}
	
	if( rauthor.length() == 0 ){
		rauthor = "Author Not Stated";
	}
				
	LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1,-2);lp.setMargins(0, 0, 0, 0);
	//LinearLayout.LayoutParams il = new LinearLayout.LayoutParams(-1,-2);il.setMargins(0, 1, 0, 0);
				//(lp
	//LinearLayout bvert = (LinearLayout) findViewById(R.id.browser_vertical);
	//RelativeLayout bbody = (RelativeLayout) findViewById(R.id.browser);
	
	/*{
		TextView b1 = new TextView(mCtx);
		LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(-1,-2);
		lp2.setMargins(0, 0, 0, 0);
		b1.setLayoutParams(lp2);
		b1.setId((int)SystemClock.uptimeMillis());
		//b1.setMaxLines(2);
		//b1.setMarqueeRepeatLimit(2);
		b1.setPadding(13,7,13,7);
		//b1.setEllipsize(TextUtils.TruncateAt.MARQUEE);
		b1.setText(title + "\n" + published);
		b1.setBackgroundColor(Color.argb(180,0,180,95));// android:background="#A0005020"
		b1.setTextSize(21);
		//b1.setTextScaleX((float)0.8);
		b1.setFocusable(true);
		b1.setClickable(true);
		//b1.setPadding(1, 0, 1, 0);
		b1.setOnFocusChangeListener(new OnFocusChangeListener(){public void onFocusChange(View v, boolean has){if(has){v.setBackgroundColor(Color.argb(180,0,180,50));}else{v.setBackgroundColor(Color.argb(140,0,0,0));}}});
		b1.setOnClickListener(new OnClickListener(){public void onClick(View v){ wayForward.sendEmptyMessage(2); }});
		mContent.addView(b1, 0);
		//b1.requestFocus();
		//b1.requestFocusFromTouch();
		{Message ml = new Message(); Bundle bl = new Bundle(); bl.putInt("id", b1.getId());  ml.setData(bl); setFocusOn.sendMessageDelayed(ml,250);}
		
	}*/
	
	/*/{
	//
		ImageView i3 = new ImageView(mCtx);
        RelativeLayout.LayoutParams rp = new RelativeLayout.LayoutParams(-2,-2);
		rp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        i3.setLayoutParams(rp);
        //i3.setPadding(0, 0, 0, 0);
        i3.setScaleType(ScaleType.MATRIX);i3.setImageResource(R.drawable.flatpearl2);
        bbody.addView(i3);
		
	}//*/
					
	/*/
	RelativeLayout r1 = new RelativeLayout(mCtx);
	RelativeLayout.LayoutParams rpa = new RelativeLayout.LayoutParams(-2,120);
	rpa.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
	r1.setLayoutParams(rpa);
	r1.setId((int)SystemClock.uptimeMillis());
	final int fid = r1.getId();
	
	r1.setOnFocusChangeListener(new OnFocusChangeListener(){public void onFocusChange(View v, boolean has){if(has){v.setBackgroundColor(Color.argb(180,0,0,48));}else{baralpha = 150;fadeBar.sendEmptyMessageDelayed(v.getId(), 150);}}});
	//r1.setOnFocusChangeListener(linkfocus);
	r1.setFocusable(true);
	r1.setClickable(true);
	//r1.setDescendantFocusability(r1.FOCUS_BLOCK_DESCENDANTS);
	
	r1.setGravity(Gravity.BOTTOM);
	r1.setBackgroundColor(coloroff);// android:background="#A0005020"
	
	fadeBar.sendEmptyMessageDelayed(r1.getId(), 1750);
	
	//r1.setOnTouchListener(new OnTouchListener(){public boolean onTouch(View v, MotionEvent event) {if(event.getAction() == MotionEvent.ACTION_CANCEL){wayProceed.sendEmptyMessage(2);return false;}return false;}});
	r1.setOnClickListener(new OnClickListener(){public void onClick(View v){wayProceed.sendEmptyMessage(2);}});//Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mLink));startActivity(myIntent);
	//ImageView ob = (ImageView) findViewById(R.id.browser_overback);
	
	r1.setOnLongClickListener(new OnLongClickListener(){public boolean onLongClick(View v){Intent d2 = new Intent(mCtx,Motion.class);
	d2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	//d2.addFlags(d2.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
	startActivity(d2);
	wayGo.sendEmptyMessage(2);return false;}});
	
	ImageView i3 = new ImageView(mCtx);
	i3.setScaleType(ScaleType.MATRIX);i3.setImageResource(R.drawable.flatpearlb);
	
	RelativeLayout.LayoutParams rp = new RelativeLayout.LayoutParams(-2,-2);
    //rp.setMargins(0, r1.getHeight()-i3.getHeight(), 0, 0);
    rp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);//rpa.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
    //rp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, -1);
    i3.setLayoutParams(rp);
    //i3.setPadding(0, 0, 0, 0);
    
    r1.addView(i3);
    //i3.setLayoutParams(rp);
    bbody.addView(r1);
	//*/
	
	
	
	
	
	/*TextView bb1 = new TextView(mCtx);
	bb1.setLayoutParams(new RelativeLayout.LayoutParams(-1,-2));
	bb1.setId((int)SystemClock.uptimeMillis());
	bb1.setText(" ");
	//bb1.setBackgroundColor(Color.argb(120, 0, 0, 0));


	bb1.setPadding(20, 9, 20, 9);
	
	bb1.setTextSize(16);
	
	bb1.setGravity(Gravity.CENTER);
	r1.addView(bb1);*/
				
						
				
    //
    //{
	//	TextView tb = new TextView(mCtx);
	//	LinearLayout.LayoutParams lpb = new LinearLayout.LayoutParams(-2,-2);
	//	lpb.setMargins(0, 1, 0, 0);
	//	tb.setLayoutParams(lpb);	
	//	tb.setText(" ");
	//	tb.setPadding(9, 9, 9, 9);
	//	tb.setTextSize(16);
	//	tb.setFocusable(true);tb.setOnFocusChangeListener(new OnFocusChangeListener(){public void onFocusChange(View v, boolean h){if(h){Message ml = new Message(); Bundle bl = new Bundle(); bl.putInt("id", fid);ml.setData(bl);setFocusOnR.sendMessage(ml);}}});
	//	tb.setMinHeight(48);
	//	mContent.addView(tb);
	//}
	//
	//
				
				
	
	if(August.dataprovider.contains("seattleland") || August.dataprovider.contains("dealextreme") || August.dest.contains("fark") || rcontent.length() < rtitle.length()/2){
			rcontent = rtitle +"\n"+rcontent;
	}
	{Message ml = new Message(); Bundle bl = new Bundle(); bl.putString("text", rtitle); bl.putInt("id", R.id.browser_title); ml.setData(bl); setText.sendMessage(ml);}
	{Message ml = new Message(); Bundle bl = new Bundle(); bl.putString("text", rpublished); bl.putInt("id", R.id.browser_date); ml.setData(bl); setText.sendMessage(ml);}
	//mTitle.setText(title);
	//mDate.setText(published);

	//mContent.getSettings().supportMultipleWindows();
	
	//link + "#contenttopoff",<div style=\"font-size:24px;\"><b>"+title+"</b></div>
	
	//
	// CUSTOMIZED
	//
	//String html = "<html><style>body {font-size: 24px;}</style><body bgcolor=#000000 text=#b0F0E0 link=#00cc66 vlink=#0066cc><div style=\"font-size:16px;\">"+title+"\n"+published + "</div><hr noshade><a name=contenttop></a><div style=\"text-align:justify;\">"+content + "</div><hr noshade>\n<a href=\""+August.dest+"\" style=\"font-size:16px;\">"+ August.title + "</a><br>\n<a href=\""+link+"\" style=\"font-size:16px;\">"+ title + "</a><br>\n<br>\n<br>\n</body></html>";//, "text/html", "UTF-8", link);
	// EC
	//mContent.getSettings().setSupportZoom(true);
	//content += "<a href=\""+link+"\"\">\n"+ title + "</a>"; 
	mLink = rlink;

	int uniq = (int)SystemClock.uptimeMillis();
	
	String sh = rtitle + "\n" + rcontent.replaceAll("[\n\r\t]+"," ");//.replaceAll("<script.*?</script>",""); 
	/*
	sh = sh.replaceAll("&quot;","\"");
	sh = sh.replaceAll("&nbsp;"," ");
	sh = sh.replaceAll("&apos;","'");sh = sh.replaceAll("&#x27;","'");sh = sh.replaceAll("&#8217;","'");sh = sh.replaceAll("&#8230;","/");
	sh = sh.replaceAll("&#039;","'");sh = sh.replaceAll("&#39;","'");
	sh = sh.replaceAll("&#034;","\"");sh = sh.replaceAll("&#8216;","");sh = sh.replaceAll("&#215;","\"");sh = sh.replaceAll("&#8212;",";");
	sh = sh.replaceAll("&raquo;","");sh = sh.replaceAll("&#8220;","'");sh = sh.replaceAll("&#8221;","'");sh = sh.replaceAll("&#8211;","--");
	sh = sh.replaceAll("&rsquo;","'");sh = sh.replaceAll("&ndash;"," ");sh = sh.replaceAll("&mdash;"," ");
	sh = sh.replaceAll("&rdquo;","");sh = sh.replaceAll("&ldquo;","");sh = sh.replaceAll("&amp;","&");
	//sh = sh.replaceAll("(:| +: +)"," : ");
	sh = sh.replaceAll("  +"," ");
	//sh = sh.replaceAll("&'","'");sh = sh.replaceAll("&\"","\"");
	//sh = sh.replaceAll("<span.*?>","").replaceAll("</span.*?>","");
	//sh = sh.replaceAll("\\.:", ". :");sh = sh.replaceAll(":\n+", ": ");sh = sh.replaceAll("\n+:", " :");sh = sh.replaceAll("\n +:", " :");sh = sh.replaceAll(":  +", ": ");
	sh = sh.replaceAll("<([bB][rR]|[pP]).*?>","\n");sh = sh.replaceAll("<(/|)[bB]( .*?|)>","\n");sh = sh.replaceAll("(\n|):(\n|)",":");
//*/
	sh = sh.replaceAll("<!--.*?-->","");
	sh = sh.replaceAll("<([bB][rR]|(/|)[pP]|(/|)[sS][tT][rR][oO][nN][gG]).*?>","\n");
	sh = sh.replaceAll("<","\n<");sh = sh.replaceAll(">",">\n");//sh = sh.replaceAll("\n:"," :");sh = sh.replaceAll(":\n",": ");
	//sh = sh.replaceAll("\\)", "\n").replaceAll("\\(", "\n");
	sh = sh.replaceAll("&#[0-9]+;"," ");
	sh = sh.replaceAll("(\n+|\n +| +\n)", "\n");sh = sh.replaceAll("\n +","\n");
	sh = sh.replaceAll("  +"," ");//sh = sh.replaceAll("\\. ", ".\n");
	//sh = sh.trim().replaceAll("\\.S\\.\n",".S. ").replaceAll(" Mt.\n", " Mt. ");
	//sh = sh.replaceAll("(\n\n+| +\n+|\n+ +)","\n");
	//int k2=2;for(int k=sh.indexOf("\\. ");k>-1&&k<sh.length();k=sh.indexOf("\\. ", k2))       {k2=k;if(sh.substring(k-3, k).matches("[a-z][a-z]\\. ") && k+1 < sh.length()){sh = sh.substring(0, k) + " " + sh.substring(k+1);}}
	//sh.replaceAll("[a-z][a-z]+\\. ", ".\n")
	//sh = new String("\n\n"+sh).replaceFirst("\n+","");
	//*/
	shl = sh.split("\n"); 
	////"All Content =======================\n";
	//for(int i = 0; i < shl.length; i++){allc+=shl[i]+"\n";}
	
	//Log.i(TAG,allc);
	
	//SharedPreferences mReg = getSharedPreferences("Preferences", MODE_WORLD_READABLE);
	SharedPreferences mReg = getSharedPreferences("Preferences", MODE_WORLD_WRITEABLE);
	Editor mEdt = mReg.edit();
	String txttype = "html"; // otherwise ^< is not text
	
	//mContent.setBackgroundColor(Color.argb(100,0,0,48));
	mContent.setGravity(Gravity.CENTER);
	String urlroot = mLink; if(urlroot.indexOf('/',10) > -1){urlroot = urlroot.substring(0, urlroot.indexOf('/',10));}
	String urlpath = mLink; if(urlpath.indexOf('/',10) > -1){urlpath = urlpath.substring(0, urlpath.lastIndexOf('/')) + '/';}
	int imgcnt = 0;int nxttxt = 1;int lsttxt = 1;int clmtxt = 1;String lc = "";int spn = 2; int lt = 2;int nxttg = 1;String details = "yes";String addtag_detail = "";String addtag_type = "";String addtag_where = "";String addtag_when = "";String addtag_contact = "";String addtag_whenall = "";String addtag_contactall = ""; String addtag_whentitle = "";String addtag_linktitle = "";int addtag_click = R.drawable.black;String nt = "";
	int data_only = 2;boolean unfilteredtext = true; boolean columntitle = false;
	String nnw = "";
	String ecode = "([A-Z]|[a-z]|[0-9]| |[.?!,/:'\"-_`~@#$%^&*;><])";
	//shl = Uri.encode(rcontent);
	for(int i = 0; i < shl.length; i++){
		
		//shl[i] = shl[i].replaceAll("\r","");
		Log.i(G,"prefilter ["+i+"]("+Uri.encode(shl[i],ecode)+")");
		shl[i] = shl[i].trim();
		if(shl[i].length() == 0	){continue;}
		if(shl[i].toLowerCase().matches("<(span |/span|/blockquote|/ul|/li|/b|/em|/a|/div|/object|/iframe|/embed).*>")){continue;}
		nnw += shl[i]+"\n";
	
		//if(shl[i].startsWith("<")){
		//
		//}else{
			
		//}
			
	}
	//.replaceAll("<[sS][cC][rR][iI][pP][tT].*?/[sS][cC][rR][iI][pP][tT]>","")
	shl = nnw.split("\n");
	
	
	
	
	
	
	
	
	String nnw2 = "";
	for(int line = 0; line < shl.length; line++){
	
	boolean bb = true;
	int cf = line;
	if(!shl[line].startsWith("<") && cf > 1 && !shl[line].matches(".*?[.?!] .*[.?!].*") ){
		for(cf = line-1;cf>0;cf--){ if(bb){Log.w(G,"prefilter context before("+shl[cf]+")");} if(shl[cf].length() == 0 || shl[cf].startsWith("<") || shl[cf].matches(".*?[.?!] .*[.?!].*")){cf++;break;}else if(shl[cf].toLowerCase().matches("(<(form|script|a |img |div|p|table|tr|/form|/script|/a|/div|b|/p|/table|/tr|hr).*>)")  ){break;} }//|.*?[.?!] .*?[.?!](|.*)  //|| (shl[cf].matches(".*?[.?!] .*?[.?!](|.*)") && shl[cf].length() > 50 )
		if(cf == 0){cf = 1;}
	}
	int cl = line;
	if(!shl[line].startsWith("<") && cl > 0 && cl < shl.length-2 && !shl[line].matches(".*?[.?!] .*[.?!].*")){
		for(cl = line+1;cl<shl.length&&cl>0;cl++){if(bb){Log.w(G,"prefilter context after("+shl[cl]+")");} if(shl[cl].length() == 0 || shl[cl].startsWith("<") || shl[cl].matches(".*?[.?!] .*[.?!].*")){cl--;break;}else if(shl[cl].toLowerCase().matches("(<(a |form |script|img |div|p|table|tr|hr|/form|/script|/a|/div|/p|/table|/tr|hr).*>)")  ){cl--;break;} }//|| (shl[cl].matches(".*?[.?!] .*?[.?!](|.*)") && shl[cl].length() > 50 )
	}
	
	//if(cl < shl.length && shl[cl].length() == 0){cl--;}
	//if(cf >= 0 && shl[cf].length() == 0){cf++;}
	//String ecode = "([A-Z]|[a-z]|[0-9]| ";
	String cx = "";
	for(int i = cf; i <= cl && i < shl.length; i++){
		//cx += Uri.encode(shl[i],ecode)+" ";
		cx += shl[i] + " ";
		if(bb){Log.i(G,"prefilter context["+i+"]("+shl[i]+")");}
	}
	
	//Log.i(G,"softFilter filter("+cx+")");
	if(cf < line && cf > 0){Log.i(G,"repeat " + line + "["+cf+"-"+cl+"] context of " + cf);continue;}//r.setBackgroundColor(Color.argb(205,80,80,70));r.setVisibility(View.GONE);}//return;}
	nnw2 += cx.trim() + "\n";	
	Log.i(G,"######################prefilter("+line+"["+cf+"-"+cl+"],"+cx+")");// ("+Uri.encode(rtext,"([A-Z]|[a-z]|[0-9]| )")+") ("+Uri.encode(ntext,"([A-Z]|[a-z]|[0-9]| )")+")");
	allc+=Uri.decode(cx)+"\n";
	
		}
	
	shl = nnw2.split("\n");
	
	
	
	
	
	clickOff = new int[shl.length+10];
	for(int i5=0;i5<shl.length+10;i5++){clickOff[i5]=0;}
	mBcolor = new int[shl.length+10];
	mScroll = new int[shl.length+10];
	
	
	//if(rtextcontent.length() == 0){}
	rtextcontent = allc;
	
	ContentValues cv = new ContentValues();
	cv.put("status", rstatus<100?100:rstatus+1);
	cv.put("textcontent",allc);
	SqliteWrapper.update(mCtx, getContentResolver(), Uri.withAppendedPath(DataProvider.CONTENT_URI,"moment"), cv, "_id = " + statusid, null);
	
	
	

	// Details
	//Log.i(TAG,"status up " + status);
	LinearLayout.LayoutParams lp3 = new LinearLayout.LayoutParams(-1,-2);
	//
	//
	//                                                       ////////////////////////////
	
	lp3.setMargins(0, 0, 0, 0);
	
	Log.w(TAG,"determinging details " + details.replaceAll("\n","%0a"));
	
	if(details.length() > 0){

	LinearLayout.LayoutParams lcr = new LinearLayout.LayoutParams(-1,-2);
	lcr.setMargins(0,0,0,0);
	RelativeLayout detailscr = new RelativeLayout(mCtx);
	detailscr.setLayoutParams(lcr);
	//int ci = mContent.getChildCount();Log.w(TAG,"row " + ci);
	detailscr.setId(uniq++);
	//cr.setMinimumHeight(90);
	
	TextView detailst = new TextView(mCtx);
	RelativeLayout.LayoutParams t1l = new RelativeLayout.LayoutParams(-1,-2);
	detailst.setLayoutParams(t1l);
	// Details
	detailst.setId(uniq++);
	detailst.setPadding(3, 7, 3, 7);
	detailst.setMinimumHeight(55);
	LinearLayout detailep = new LinearLayout(mCtx);
	//detailep.setLayoutParams(new RelativeLayout.LayoutParams(1000,55));// set to 55 on purpose
	detailep.setPadding(13,0,0,0);
	
	detailep.setOrientation(LinearLayout.HORIZONTAL);
	detailep.setId(uniq++);
	detailep.setScrollContainer(true);
	
	ScrollView dsp = new ScrollView(mCtx);//UNUSED
	RelativeLayout.LayoutParams dsl = new RelativeLayout.LayoutParams(-2,-2);
	dsl.addRule(RelativeLayout.ALIGN_BOTTOM, detailst.getId());
	dsp.setLayoutParams(dsl);
	//dsp.setLayoutParams(new RelativeLayout.LayoutParams(-2,-2));	
	dsp.setId(uniq++);
	dsp.setHorizontalScrollBarEnabled(true);
	dsp.setScrollContainer(true);
	dsp.addView(detailep);//DETAILS
	dsp.setVisibility(View.GONE);
	detailscr.addView(detailst);
    
	
	//
    //
	// Setting Details Text
    detailst.setGravity(Gravity.CENTER);
	detailst.setText("Details");detailst.setTextColor(textcolor);detailst.setTextSize((float)12);
    detailst.setVisibility(View.INVISIBLE);
	//
    detailscr.addView(dsp);
    //

    
    
    RelativeLayout.LayoutParams r3l = new RelativeLayout.LayoutParams(-1,-1);
	r3l.addRule(RelativeLayout.ALIGN_TOP,detailst.getId());r3l.addRule(RelativeLayout.ALIGN_BOTTOM,detailst.getId());r3l.addRule(RelativeLayout.ALIGN_RIGHT,detailst.getId());
	RelativeLayout r3 = new RelativeLayout(mCtx);
	r3.setId(uniq++);
	r3.setLayoutParams(r3l);
	detailscr.addView(r3);
    
    
    
    
    //
    //
    {
	{ImageView i1 = new ImageView(mCtx);
    i1.setLayoutParams(new RelativeLayout.LayoutParams(-2,-2));
    i1.setScaleType(ScaleType.MATRIX);i1.setImageResource(R.drawable.flatpearl);
    //if(ci == 0){i1.setVisibility(View.GONE);}
    detailscr.addView(i1);}	
	
	{ImageView i2 = new ImageView(mCtx);
    RelativeLayout.LayoutParams bli = new RelativeLayout.LayoutParams(-2,-2);
    bli.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
    i2.setLayoutParams(bli);
    i2.setScaleType(ScaleType.MATRIX);i2.setImageResource(R.drawable.flatpearl2);
    //if(ci == 0){i2.setVisibility(View.GONE);}
    detailscr.addView(i2);}
    }
	
	{ImageView i3 = new ImageView(mCtx);
    RelativeLayout.LayoutParams bli = new RelativeLayout.LayoutParams(-2,-1);
    bli.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);bli.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);bli.addRule(RelativeLayout.ALIGN_BOTTOM,r3.getId());
    i3.setLayoutParams(bli);
    i3.setScaleType(ScaleType.FIT_END);i3.setImageResource(R.drawable.flatpearlb);
    //if(ci == cn.length-1){i3.setVisibility(View.GONE);}
    detailscr.addView(i3);}
	
    {ImageView i4 = new ImageView(mCtx);
    RelativeLayout.LayoutParams bli = new RelativeLayout.LayoutParams(-2,-1);
    bli.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);bli.addRule(RelativeLayout.ALIGN_PARENT_LEFT);bli.addRule(RelativeLayout.ALIGN_BOTTOM,r3.getId());
    i4.setLayoutParams(bli);
    i4.setScaleType(ScaleType.FIT_END);i4.setImageResource(R.drawable.flatpearl3);
    //if(ci == cn.length-1){i3.setVisibility(View.GONE);}
    detailscr.addView(i4);}
    
    //detailscr.setBackgroundColor(colorm2);
    detailscr.setBackgroundResource(R.drawable.details);
    detailscr.setFocusable(true);detailscr.setClickable(true);
    detailscr.setOnFocusChangeListener(textfocus);
    detailscr.setOnClickListener(new OnClickListener(){public void onClick(View v){

    	
    
    RelativeLayout r = null;
    for(int i = 0; i< mContent.getChildCount();i++){
	    		
		try {
		r = (RelativeLayout) mContent.getChildAt(i);
		
		if(r != null && r.getVisibility() == View.GONE){
		r.setVisibility(View.VISIBLE);
		}
		}catch(ClassCastException ec){}
	}
    
    r = (RelativeLayout)v;
    r.setVisibility(View.GONE);	
    
    /*
    RelativeLayout r1 = (RelativeLayout) mContent.getChildAt(0);
    if(r1 != null){ImageView i1 = (ImageView) r1.getChildAt(4);
	i1.setVisibility(View.VISIBLE);
	ImageView i2 = (ImageView) r1.getChildAt(5);
	i2.setVisibility(View.VISIBLE);
    }//*/
    
    }});

    RelativeLayout r1 = (RelativeLayout) mContent.getChildAt(0);
    if(r1 != null){ImageView i1 = (ImageView) r1.getChildAt(5);
	i1.setVisibility(View.GONE);
	ImageView i2 = (ImageView) r1.getChildAt(6);
	i2.setVisibility(View.GONE);
	ImageView i4 = (ImageView) r1.getChildAt(3);
	i4.setVisibility(View.VISIBLE);
    }
    {ImageView i1 = (ImageView) detailscr.getChildAt(3);
	i1.setVisibility(View.GONE);
	ImageView i2 = (ImageView) detailscr.getChildAt(4);
	i2.setVisibility(View.GONE);
    }


	//RelativeLayout r1 = (RelativeLayout) mContent.getChildAt(0);
	//if(r1 != null){ImageView i1 = (ImageView) r1.getChildAt(4);
	//i1.setVisibility(View.GONE);
	//ImageView i2 = (ImageView) r1.getChildAt(5);
	//i2.setVisibility(View.GONE);
	//ImageView i3 = (ImageView) r1.getChildAt(3);
	//i3.setVisibility(View.GONE);
	//TextView bt1 = (TextView) r1.getChildAt(1);
	//
	//bt1.setMinimumHeight(55);
	//}//*/	
			       
					
    mContent.addView(detailscr);
			        	
	}
	
	
	
	
	
	for(int i = 0; i < shl.length; i++){//softFilter
	
		
		
		
		
		
		
		
		
		
		//.replaceAll("<(br|tr|th|input|table|hr|p)>","\n").replaceAll("<.*?>","")
		//
		//if(shl[i].trim().length() > 0){
							
							
		//if(shl[i].startsWith(" ")){shl[i] = shl[i].replaceFirst(" +", "");}
		//if(shl[i].endsWith(" ")){shl[i] = shl[i].replaceFirst(" +$", "");}
		/*
		if(shl[i].startsWith("<br")){continue;}
		else if(shl[i].startsWith("</")){continue;}
		else if(shl[i].startsWith("<div")){continue;}
		else if(shl[i].startsWith("<td")){continue;}
		else if(shl[i].startsWith("<tr")){continue;}
		else if(shl[i].startsWith("<th")){continue;}
		else if(shl[i].startsWith("<input")){continue;}
		else if(shl[i].startsWith("<object")){continue;}
		else if(shl[i].contentEquals("div#article")){continue;}//smashing magazine
		else if(shl[i].contentEquals("div.story")){continue;}//smashing magazine
		else if(shl[i].contentEquals("<code>")){continue;}//smashing magazine
		else if(shl[i].startsWith("<table")){continue;}
		else if(shl[i].startsWith("<span")){continue;}
		else if(shl[i].startsWith("<small")){continue;}
		else if(shl[i].startsWith("<mprid")){continue;}
		else if(shl[i].startsWith("<strong")){continue;}
		else if(shl[i].startsWith("<noscript")){continue;}
		else if(shl[i].startsWith("<b")){continue;}
		else if(shl[i].startsWith("<em")){continue;}
		else if(shl[i].startsWith("<blockquote")){continue;}
		else if(shl[i].startsWith("<h")){continue;}
		else if(shl[i].startsWith("<p")){continue;}
		else if(shl[i].startsWith("<ul")){continue;}
		else if(shl[i].startsWith("<li")){continue;}
		else if(shl[i].contentEquals(";")){continue;}
		else if(shl[i].contentEquals(":")){continue;}
		else if(shl[i].contentEquals("|")){continue;}
		else if(shl[i].contentEquals("[")){continue;}
		else if(shl[i].contentEquals("]")){continue;}
		else if(shl[i].contentEquals("<![CDATA[")){continue;}
		else if(shl[i].contentEquals("]]>")){continue;}//*/
	
		
		for(nxttxt =i+1;nxttxt<shl.length;nxttxt++){if(shl[nxttxt].startsWith("<")){continue;}break;}
		
		
		
		
//      //////////////////
		//						
		// BUILD (cbuildr)
		RelativeLayout cbuildr = null;
		cbuildr = new RelativeLayout(mCtx);	
		//
		TextView t1 = new TextView(mCtx);
		//TextView dt1 = new TextView(mCtx);
		TextView dt = new TextView(mCtx);
		RelativeLayout.LayoutParams dtl = new RelativeLayout.LayoutParams(-1,-2);
		dt.setLayoutParams(dtl);
		dt.setVisibility(View.GONE);
		dt.setId(uniq++);
		
		TextView dx = new TextView(mCtx);
		RelativeLayout.LayoutParams dxl = new RelativeLayout.LayoutParams(-1,-2);
		dx.setLayoutParams(dxl);
		dx.setVisibility(View.GONE);
		dx.setId(uniq++);
		
		//
		LinearLayout ephoz = new LinearLayout(mCtx);
		//
		//
		//                                                                            //////////////////
		ephoz.setLayoutParams(new ScrollView.LayoutParams(-1,-2));
		//ep.setMinimumHeight(90);
		ephoz.setPadding(13,3,0,3);
		//ephoz.setBackgroundColor(Color.argb(200,20,80,80));
		//ep.setScrollContainer(true);
		ephoz.setOrientation(LinearLayout.HORIZONTAL);
		ephoz.setId(uniq++);
		//Title t1
		//t1.setGravity(Gravity.BOTTOM);
		//on
		t1.setId(uniq++);
		
										
		
		//if( i > 0 && shl[i-1].toLowerCase().matches("<b>|<b")){}
		
		//                                                                            //////////////////
		//
		//
		RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(-1,-1);
		//
		LinearLayout.LayoutParams lcr = new LinearLayout.LayoutParams(-1,-2);
		//
		//
		//                                                                            //////////////////
		// THICKNESS OF SPACE BETWEEN
		

	    int spl = mContent.getChildCount();
		if(spl == 1){spl = 0;}
		
		if( spl == 0 ){lcr.setMargins(0,0,0,0);}else{
		lcr.setMargins(0,1,0,0);}
		
		cbuildr.setPadding(0,0,0,0);
		cbuildr.setLayoutParams(lcr);
		//int ci = mContent.getChildCount();Log.w(TAG,"row " + ci);
		cbuildr.setId(uniq++);
		//cbuildr.setMinimumHeight(90);
		columntitle = false;
		addtag_click = August.notifyimage;
		int bcolor = colorm1;
		String nw = shl[i];
		addtag_type = "";
		nt = "";
		addtag_detail = "";
		//bcolor = colorm1;
		
		addtag_linktitle = "";
		unfilteredtext = false;
		
		
		
		

		
		
		
		
		
		
		
		
		RelativeLayout.LayoutParams rlp3 = new RelativeLayout.LayoutParams(-1,-2);//HAVEN text 
		//rlp3.addRule(RelativeLayout.ABOVE,stoolp.getId());
		t1.setMinimumHeight(70);
		t1.setLayoutParams(rlp3);
		cbuildr.addView(t1);
		
//		
		ScrollView stoolp = null;
		stoolp = new ScrollView(mCtx);
		RelativeLayout.LayoutParams xps = new RelativeLayout.LayoutParams(-1,-2); 
		xps.setMargins(0,0,0,0);
		xps.addRule(RelativeLayout.ALIGN_BOTTOM,t1.getId());
		//xps.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		stoolp.setLayoutParams(xps);
		stoolp.setId(uniq++);

		
		
		stoolp.setVisibility(View.GONE);
		cbuildr.addView(stoolp);
		
		RelativeLayout r3 = new RelativeLayout(mCtx);
		RelativeLayout.LayoutParams r3l = new RelativeLayout.LayoutParams(-1,-1);
		r3l.addRule(RelativeLayout.ALIGN_TOP,t1.getId());r3l.addRule(RelativeLayout.ALIGN_BOTTOM,t1.getId());//,stoolp.getId());//r3l.addRule(RelativeLayout.ALIGN_RIGHT,t1.getId());
		r3.setLayoutParams(r3l);//r3l.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		r3.setId(uniq++);
		//r3.setBackgroundColor(Color.argb(105,90,90,30));
		cbuildr.addView(r3);
		
		{ImageView i1 = new ImageView(mCtx);
		i1.setId(uniq++);
		i1.setLayoutParams(new RelativeLayout.LayoutParams(-2,-2));
	    i1.setScaleType(ScaleType.MATRIX);i1.setImageResource(R.drawable.flatpearl);
	    
	    cbuildr.addView(i1);}
		
		{ImageView i2 = new ImageView(mCtx);
		i2.setId(uniq++);
		RelativeLayout.LayoutParams bli = new RelativeLayout.LayoutParams(-2,-2);
	    bli.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,-1);bli.addRule(RelativeLayout.ALIGN_PARENT_TOP,-1);
	    i2.setLayoutParams(bli);
	    i2.setScaleType(ScaleType.MATRIX);i2.setImageResource(R.drawable.flatpearl2);
	    
	    cbuildr.addView(i2);}
	    
		{ImageView i3 = new ImageView(mCtx);
		i3.setId(uniq++);
		RelativeLayout.LayoutParams bli = new RelativeLayout.LayoutParams(-2,-2);
	    bli.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);bli.addRule(RelativeLayout.ALIGN_BOTTOM,t1.getId());
	    i3.setLayoutParams(bli);
	    i3.setScaleType(ScaleType.MATRIX);i3.setImageResource(R.drawable.flatpearlb);
	    
	    cbuildr.addView(i3);}
		
	    {ImageView i4 = new ImageView(mCtx);
	    i4.setId(uniq++);
	    RelativeLayout.LayoutParams bli = new RelativeLayout.LayoutParams(-2,-2);
	    bli.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);bli.addRule(RelativeLayout.ALIGN_PARENT_LEFT);bli.addRule(RelativeLayout.ALIGN_BOTTOM,t1.getId());
	    i4.setLayoutParams(bli);
	    i4.setScaleType(ScaleType.MATRIX);i4.setImageResource(R.drawable.flatpearl3);
	    
	    cbuildr.addView(i4);}
		
	    {ImageView i7 = new ImageView(mCtx);
		//LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(85,85);
		i7.setScaleType(ScaleType.CENTER_INSIDE);
		i7.setImageResource(addtag_click);
		i7.setAdjustViewBounds(true);
		RelativeLayout.LayoutParams r7 = new RelativeLayout.LayoutParams(55,55);
		//r7.setMargins(17,15,0,0);
		//i7.setPadding(0,0,-5,-5);
		r7.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);//r7.addRule(RelativeLayout.ALIGN_BOTTOM,stoolp.getId());
		i7.setLayoutParams(r7);
		i7.setId(uniq++);
		cbuildr.addView(i7);
	    
	    }
	    
	    cbuildr.addView(dx);
	    cbuildr.addView(dt);
	    
	    

		//if(t1.length() == 1 && unfilteredtext){cbuildr.setVisibility(View.GONE);}else if(unfilteredtext && !columntitle){cbuildr.setVisibility(View.VISIBLE);}
		//if(mTag == t1.getId()){addtag_whentitle = nw;}
		
		t1.setTextSize(18);
		t1.setTextColor(textcolor);
		
		//t1.setLinksClickable(true);
		t1.setFocusable(true);
		//t1.setAutoLinkMask(Linkify.ALL);
		//t1.setOnFocusChangeListener(textfocus);
		t1.setPadding(17, 7, 17, 7);
		//t1.setMinimumHeight(90);
		//cr.setVisibility(View.GONE);
		//t1.setMinimumHeight(90);
		
		if(mTag == t1.getId()){
			//t1.setMinimumHeight(110);
			
			
		Log.i(TAG,"=========== TAG " + nw.replaceAll("\n","%0a") );
		cbuildr.setVisibility(View.VISIBLE);
		
		//
		//
		//
		}else //
		if(mTag > 2 && addtag_type.length() > 0){}
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		

		
		{
		    RelativeLayout.LayoutParams xps2 = new RelativeLayout.LayoutParams(-1,-2); 
			xps2.setMargins(0,0,0,0);//;
			stoolp.setVisibility(View.GONE);
			xps2.addRule(RelativeLayout.ALIGN_BOTTOM,t1.getId());
			stoolp.setLayoutParams(xps2);
			
		}	
		stoolp.setPadding(0,0,0,0);
		stoolp.setHorizontalScrollBarEnabled(true);Log.i(TAG,"Scroll Container " + spl + " " + nw.replaceAll("\n","%0a"));
		stoolp.setScrollContainer(true);
		stoolp.setScrollBarStyle(ScrollView.SCROLLBARS_INSIDE_INSET);
		
		mBcolor[spl] = bcolor; 
		cbuildr.setBackgroundColor(bcolor);
		
		stoolp.setFocusable(false);
		t1.setFocusable(false);
		stoolp.setVisibility(View.VISIBLE);
		stoolp.addView(ephoz);
		

		
		boolean match3 = false;
		boolean match4 = false;
		if(spl > 0){
	
		RelativeLayout r1 = null; try{r1 = (RelativeLayout) mContent.getChildAt(mContent.getChildCount()-1);}catch(ClassCastException ec){}
	    if(r1 != null){
		
		//ImageView i1 = (ImageView) r1.getChildAt(3);
		//ImageView i2 = (ImageView) r1.getChildAt(4);
		ImageView i3 = (ImageView) r1.getChildAt(5);if(i3!=null && i3.getVisibility() == View.VISIBLE){match3 = true;}
		ImageView i4 = (ImageView) r1.getChildAt(6);if(i4!=null && i4.getVisibility() == View.VISIBLE){match4 = true;}
	    }
		
		
		}
		
		{
		ImageView i1 = (ImageView) cbuildr.getChildAt(3);
		ImageView i2 = (ImageView) cbuildr.getChildAt(4);
		ImageView i4 = (ImageView) cbuildr.getChildAt(5);
		ImageView i3 = (ImageView) cbuildr.getChildAt(6);
		ImageView iclicker = (ImageView) cbuildr.getChildAt(7);
		if(!match3 ){i1.setVisibility(View.GONE);}
		if( !match4|| unfilteredtext || mTag == t1.getId()){i2.setVisibility(View.GONE);}
		if(mTag == t1.getId() || columntitle){i3.setVisibility(View.GONE);}
	    if(mTag == t1.getId() || columntitle ){i4.setVisibility(View.GONE);}
	    if(columntitle){t1.setMinimumHeight(55);}
	    if(addtag_click == August.notifyimage){iclicker.setVisibility(View.GONE);}else{iclicker.setImageResource(addtag_click);}
	    if(spl == 0){i4.setVisibility(View.GONE);i3.setVisibility(View.GONE);}
		}
		
		
		
		

	    
	    t1.setClickable(true);
	    t1.setFocusable(true);
	    final int bcolorf = bcolor;
	    t1.setOnFocusChangeListener(new OnFocusChangeListener(){public void onFocusChange(View v, boolean has){
	    //RelativeLayout r = (RelativeLayout) v;
	    TextView t = (TextView) v;//r.getChildAt(0);
	    RelativeLayout r = (RelativeLayout) t.getParent();
	    if(has){
	    	//r.setBackgroundResource(R.drawable.blueselector);
	    	r.setBackgroundColor(coloron);
	    }else{
	    	//r.setBackgroundResource(R.drawable.seetu);
	    	r.setBackgroundColor(bcolorf);
	    }
	    }});
	    //cbuildr.setFocusable(true);
	    t1.setClickable(true);
	    t1.setOnClickListener(clickFilter);
	    cbuildr.setGravity(Gravity.BOTTOM);
		
	    if(spl == 0){
	    	mContent.addView(cbuildr,0);
	    }else{
	    	mContent.addView(cbuildr);
	    }
	    
		{Bundle bl = new Bundle();bl.putInt("line",i);bl.putInt("row",spl);bl.putInt("id",cbuildr.getId());bl.putInt("mtag",mTag);bl.putLong("moment",moment);Message ml = new Message(); ml.setData(bl);softFilter.sendMessage(ml);}
		
		
	
		
			
	}//for
			
	
	
	
	
	
	
	
	
	
	
	
	
	
		

	
	
	if(mContent.getChildCount() -1 >=0){
		int lastone = -1;
		for(int c = mContent.getChildCount()-1; c >=1; c--){
		
			RelativeLayout r1 = null; try{r1 = (RelativeLayout) mContent.getChildAt(c);}catch(ClassCastException ec){}
	    if(r1 != null){
	    	if(r1.getVisibility() == View.GONE && c < mContent.getChildCount()-1 && c != lastone){continue;}
	    	
	    //ImageView i1 = (ImageView) r1.getChildAt(3);
		//ImageView i2 = (ImageView) r1.getChildAt(4);
		ImageView i3 = (ImageView) r1.getChildAt(5);if(i3!=null && i3.getVisibility() == View.VISIBLE){i3.setVisibility(View.GONE);}//BR
		ImageView i4 = (ImageView) r1.getChildAt(6);if(i4!=null && i4.getVisibility() == View.GONE){i4.setVisibility(View.VISIBLE);}//BL
		//if(c == lastone){break;}
		if(r1.getVisibility() == View.GONE){continue;}
		if(r1.getVisibility() == View.VISIBLE && c < mContent.getChildCount()-3){//c+=2;lastone = c-1;contunue;}
			RelativeLayout r2 = null; try{r2 = (RelativeLayout) mContent.getChildAt(c+1);}catch(ClassCastException ec){}
		    if(r2 != null){
		    	ImageView i1 = (ImageView) r2.getChildAt(3);if(i1!=null && i1.getVisibility() == View.GONE){i1.setVisibility(View.VISIBLE);}//TR
				ImageView i2 = (ImageView) r2.getChildAt(4);if(i2!=null && i2.getVisibility() == View.GONE){i2.setVisibility(View.VISIBLE);}//TL
		    }
		    
		}
		if(r1.getVisibility() == View.VISIBLE){break;}
	    }
		}
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	RelativeLayout mxt = (RelativeLayout) mContent.getChildAt(0);
    mxt.setVisibility(View.VISIBLE);
	mxt.setBackgroundResource(R.drawable.detailbackflow);
    TextView mxtt = (TextView) mxt.getChildAt(0);
	mxtt.setTextSize((float)24);
		
		
		
	/*
	if(imgcnt == 0){			
		Bundle bl = new Bundle(); bl.putString("filename","i"+moment+".png");Message ml = new Message(); ml.setData(bl);snapshot.sendMessageDelayed(ml,2700-1700);		
		allimages = "screenshot/i"+moment+".png";
		rimages = allimages;
		ContentValues cv2 = new ContentValues();
		cv2.put("images", allimages.trim());
		SqliteWrapper.update(mCtx, getContentResolver(), Uri.withAppendedPath(DataProvider.CONTENT_URI,"moment"), cv2, "_id = " + moment, null);
	}//*/		
		
		
		
		
		
		}
		
	
	};
	
	
	
	
	
	
	
	
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		//lc = shl[i].toLowerCase();
		//if(shl[i].relaceAll("<(br|tr|th|input|table|hr)>")){continue;}
		//if(shl[i].contains("<p>"){continue;}
		
		
		
		
		
		
		/*
		
		
		
		
		//                                                                           //////////////////
		// HTML Filter : Text Filter
		// ^
		// <a link
		if(lc.startsWith("<a") && i<-20){
		// <a link
		//	
		//                                                                            //////////////////	
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			//data_only = 2;	
			
			
			
		//shl[i] = shl[i].replaceAll(" : ",":");	
		lc = shl[i].toLowerCase();
		String href = shl[i].substring(lc.indexOf("=",lc.indexOf("href"))+1);
		if(href.startsWith(" ")){href = href.replaceFirst(" +", "");}
		href = href.replaceAll("\"", "");href = href.replaceAll(" .*", "").replaceAll("/>.*","").replaceAll(">.*", "").replaceAll("'", "");
		//Log.i(TAG,"HREF " + href + "<--");
							
		if(!href.startsWith("http") && !href.matches("://")){
		if(href.startsWith("/")){
		href = urlroot + href;
		}else{
		href = urlpath + href;
		}}
							
		//mTextadd = 1;
		if(href.compareToIgnoreCase(mLink) == 0){continue;}
			
		String gname = "";
		if(lc.indexOf("title") > -1){
		gname = shl[i].substring(lc.indexOf("=",lc.indexOf("title"))+1);
		if(gname.startsWith(" ")){gname = gname.replaceFirst(" +", "");}
		gname = gname.replaceAll("\"", "");gname = gname.replaceAll(" .*", "").replaceAll("/>.*","").replaceAll(">.*", "").replaceAll("'", "");
		//spn = lc.indexOf("\"",lc.indexOf("title"));
		//lt = lc.indexOf("\"",spn);
		//gname = shl[i].substring(spn,lt);
		}
							
							
	
		//if( shl.length > i+1 && shl[i+1].startsWith("<img") ){
		//nextlink = href; 
		//}else{
	
		nextlink = "";
		Button t1 = new Button(mCtx);
		t1.setLayoutParams(lp);
		// button
		t1.setId(uniq++);
		nt = gname;//href;//:shl[++i];
			
								
		//for(nxttg =nxttxt;nxttg<shl.length;nxttg++){if(shl[nxttg].toLowerCase().startsWith("</a")){break;}}
		//if(nxttg > nxttxt && nxttxt < shl.length){nt = shl[nxttxt];i=nxttxt;}
			
		if(nt.contains("Click here")){
		nt = nt.replaceAll("Click here", "Proceed here");
		}
		
		t1.setEllipsize(TextUtils.TruncateAt.MARQUEE);
		t1.setText(nt);
		//t1.setBackgroundColor(Color.argb(100, 0, 180, 50));// android:background="#A0005020"
		final String llink = href;
		t1.setTextSize(18);
		t1.setOnLongClickListener(new OnLongClickListener(){public boolean onLongClick(View v) {easyStatus(llink);  return true;}});
		t1.setOnClickListener(new OnClickListener(){public void onClick(View v){Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(llink));startActivity(myIntent);}});
		//t1.setOnFocusChangeListener(linkfocus);
		t1.setGravity(Gravity.CENTER);
		//t1.setFocusable(true);
		t1.setPadding(9,9,9,9);
		mContent.addView(t1);//Button
		
		continue;
								
									//}
		
		//                                                                            //////////////////
		//
		// <img
		} else//*
		if(lc.startsWith("<img")){
		// <img
		//	
		//                                                                            //////////////////	
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			data_only = 2;
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
		//shl[i] = shl[i].replaceAll(" : ",":");	
		lc = shl[i].toLowerCase();
						
		if(lc.matches("width=\"[0-9]\"") || lc.matches("height=\"[0-9]\"") ){continue;}
		String img = shl[i].substring(lc.indexOf("=",lc.indexOf("src"))+1);
		if(August.dest.contains("google")){	
		if(img.indexOf("6.jpg") > 10 ){img = img.replaceAll("6.jpg","2.jpg");}
		}
		Log.i(TAG,"image " + img + " ("+shl[i]+")");
		
		if(img.startsWith(" ")){img = img.replaceFirst(" +", "");}
		img = img.replaceAll("\"", "");img = img.replaceAll(" .*", "").replaceAll("/>.*", "").replaceAll(">.*", "").replaceAll("'", "");
		if(img.length() == 0){continue;}
								
								
		//if(img.startsWith("'")){img = img.replaceFirst("'", "");}
		//.replaceAll("\".*", "").replaceAll("'.*", "");
		
		
		
		imgcnt++;
		
		int browser_image_height = -2;
		//
		//if(August.dest.contains("google")){
		//	browser_image_height = 200;
		//}else
		//if(August.dest.contains("dealextreme")){
		//	browser_image_height = 200;
		//}
		ImageView t1 = new ImageView(mCtx);
		//t1.setBackgroundColor(Color.argb(230, 20, 180, 130));
		
		//if(nextlink.length() > 0){
		RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(-1,-2);
		//rlp.addRule(RelativeLayout.CENTER_IN_PARENT,-1);
		t1.setLayoutParams(rlp);
		///}else{
		//LinearLayout.LayoutParams il2 = new LinearLayout.LayoutParams(-2,-2);//il.setMargins(0, 1, 0, 0);
		//t1.setLayoutParams(il2);
		//}//
		//image
		t1.setId(uniq++);
		//t1.setMinimumHeight(120);
		//t1.setPadding(0, 1, 0, 0);
		t1.setAdjustViewBounds(true);
		t1.setScaleType(ScaleType.FIT_CENTER);
		// FIT_CENTER -
		// FIT_START 
		// FIT_XY
		// FIT_END
		// CENTER_INSIDE -
		
		//if(imgcnt == 0 && i == 0){
		//t1.setMinimumHeight(mContent.getWidth()-20);
		//}
						
		if(nextlink.length() > 0){Log.w(TAG,"ImageLink " + nextlink);final String nextlink2 = nextlink.replaceAll("&amp;", "&");
		t1.setOnFocusChangeListener(imagefocus);
		t1.setOnTouchListener(new OnTouchListener(){public boolean onTouch(View v, MotionEvent ev) {if(ev.getAction() == MotionEvent.ACTION_DOWN){ImageView rf = (ImageView) v;rf.setBackgroundColor(coloron);}else if(ev.getAction() == MotionEvent.ACTION_UP){ImageView rf = (ImageView) v;rf.setBackgroundColor(coloroff);}else if(ev.getAction() == MotionEvent.ACTION_OUTSIDE || ev.getAction() == MotionEvent.ACTION_CANCEL){ImageView rf = (ImageView) v;rf.setBackgroundColor(coloroff);}return false;}});
		t1.setFocusable(true);
		//t1.setBackgroundColor(coloroff);
		t1.setClickable(true);	
		t1.setOnClickListener(new OnClickListener(){public void onClick(View v){Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(nextlink2));startActivity(myIntent);}});
		}//Image
							
		//t1.setMinimumHeight(65);
		
		//t1.setMaxHeight(200);
								
								
		LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(-2,-2);
		RelativeLayout imager2 = new RelativeLayout(mCtx);
		imager2.setLayoutParams(lp2);
		imager2.setId(uniq++);
		imager2.addView(t1);
				
				
		final String imgloc = "img"+img.replaceAll("/","_").replaceAll(":","_");
		final String imgurl = img;
		final int imgid = t1.getId();
		final int rid = imager2.getId();
								
								
								
		if(t1.isClickable() || nextlink.length() > 0){
				
		//r2.setOrientation(LinearLayout.HORIZONTAL);
		//r2.setGravity(Gravity.CENTER);
		
		final String nextlink2 = nextlink.replaceAll("&amp;", "&");
		
		RelativeLayout.LayoutParams rlp2 = new RelativeLayout.LayoutParams(-2,-2);
		
		ImageView pi = new ImageView(mCtx);
		rlp2.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM,-1);
		rlp2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,-1);
		//rlp2.addRule();
		pi.setLayoutParams(rlp2);
		//pi.setGravity(Gravity.BOTTOM);
		//pi.setAdjustViewBounds(true);
		//pi.setPadding(10, 10, 10, 10);
		pi.setScaleType(ScaleType.MATRIX);
		pi.setImageResource(android.R.drawable.stat_notify_more);
				
		imager2.addView(pi);
		}
								
								
		final String filename = mReg.getString(imgloc,"");
		t1.setOnLongClickListener(new OnLongClickListener(){public boolean onLongClick(View v) {easyStatus(imgloc + "\n" + imgurl);  return true;}});
		t1.setOnClickListener(new OnClickListener(){public void onClick(View v){
			
		File file = null;
		file = getfilepath();									
		File fi = new File(file.getAbsolutePath(),filename);
		if(fi.exists()){
		String f = fi.getAbsolutePath();
		String tp = f.substring(f.lastIndexOf('.')+1).toLowerCase();
		Intent d = new Intent(Intent.ACTION_VIEW);d.setDataAndType(Uri.fromFile(new File(f)),"image/"+(tp.length() <=4 && tp.length() > 2?tp:"png"));
		startActivity(d);
		}
		}});
								
								
								
								
		imager2.setVisibility(View.GONE);						
		mContent.addView(imager2);
		/*						
		//SharedPreferences mReg = getSharedPreferences("Preferences", MODE_WORLD_WRITEABLE);
		
		//Log.w(TAG,"Image " + imgloc + " << " + imgurl);
		if( !mReg.contains(imgloc) ){
		try{
		t1.setImageResource(R.drawable.iget);
		t1.setAlpha(255);
		}catch(OutOfMemoryError me){
		}
		}else{
		//String filename = mReg.getString(imgloc,"");
		File file = null;
		file = getfilepath();									
		File fi = new File(file.getAbsolutePath(),filename);
		if(fi.exists()){
		{Message ml = new Message(); Bundle bl = new Bundle(); bl.putInt("id",imgid); bl.putInt("up",rid); bl.putString("storloc",imgloc); ml.setData(bl); setImage.sendMessageDelayed(ml,50);}
		//Drawable db = Drawable.createFromPath(file.getAbsolutePath()+"/"+filename);
		}else{mEdt.remove(imgloc);mEdt.commit();t1.setAlpha(100);t1.setImageResource(R.drawable.iget_error);}
		}
				
		//t1.setBackgroundColor(Color.argb(100, 100, 190, 100));// android:background="#A0005020"
		//t1.setTextSize(32);
				
		nextlink = "";
		//Log.i(TAG,"Image " + img + "<-- loc("+imgloc+") url("+imgurl+")");
		
		//SharedPreferences mReg = getSharedPreferences("Preferences", MODE_WORLD_READABLE);
		//if( mReg.contains(imgloc) ){
		//{Message ml = new Message(); Bundle bl = new Bundle(); bl.putInt("id",imgid); bl.putString("storloc",imgloc); ml.setData(bl); setImage.sendMessage(ml);}
		//}else{
		
		//t1.setOnClickListener(new OnClickListener(){public void onClick(View v){SharedPreferences mReg = getSharedPreferences("Preferences", MODE_WORLD_READABLE);Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mReg.getString(imgloc, "")));startActivity(myIntent);}});
		//t1 IMAGE
		
		
		if( !mReg.contains(imgloc) && imgcnt < 30){
		final int imgcnt2 = ++imgcnt;
		
		Thread it = new Thread(){
		public void run(){
		SharedPreferences mReg = getSharedPreferences("Preferences", MODE_WORLD_READABLE);
		
		
		{Message ml = new Message(); Bundle bl = new Bundle(); bl.putInt("id",imgid); bl.putString("dest", imgurl); bl.putString("storloc",imgloc); bl.putLong("moment",moment); ml.setData(bl); mGet.sendMessageDelayed(ml,(imgcnt2*1750));}
		try{for(;;Thread.sleep(750)){if(mReg.contains(imgloc)){break;} {Message ml = new Message(); Bundle bl = new Bundle(); bl.putInt("id",imgid); ml.setData(bl); flipAlpha.sendMessage(ml);}  }
		}catch(InterruptedException e){}
	
		{Message ml = new Message(); Bundle bl = new Bundle(); bl.putInt("id",imgid); bl.putInt("up", rid); bl.putString("storloc",imgloc); ml.setData(bl); setImage.sendMessageDelayed(ml,750);}
		//String x = mReg.getString(imgloc);
			
		}
		};
		it.start();
		}
		//*/						
		
		
		/*
		//}else if(lc.matches("(<br.*|<p.*|<b>)")&& txttype.contentEquals("html")){continue;	
		
		//continue;
		
		//                                                                            //////////////////
		// txttype=html
		// < 
		}else//* 								
		if(lc.startsWith("<") && txttype.contentEquals("html") && !lc.matches("<a.*>") ){
		// <	
		//	
		//                                                                            //////////////////	
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			data_only = 2;	
		Log.w(TAG,"unfiltered html " + shl[i]);
		continue;
		
		
		
		//                                                                            //////////////////
		//
		// foo: bar
		}else//*
		if( lc.length() < 50 && (lc.endsWith(":") || (i+1 < shl.length && nxttxt < shl.length && shl[nxttxt].matches(":.*"))      )){
		//
		//	
		//                                                                            //////////////////	
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
		//Log.w(TAG,"re ==" + shl[i] + "==  ==" + shl[nxttxt]+"==");
		//if(shl[nxttxt].length() > 1){
		//shl[nxttxt] = shl[i] + " " + shl[nxttxt];i=nxttxt;
		//}else{
		for(nxttxt = nxttxt;nxttxt<shl.length;nxttxt++){
		if(shl[nxttxt].startsWith("<") && !shl[nxttxt].matches("<(a|img).*")){continue;}
		//shl[i] += shl[nxttxt];break;
		}
		//i = nxttxt;
		//if(nxttxt < shl.length){shl[nxttxt] = shl[i] + " " + shl[nxttxt];i=nxttxt;}
		//}
		if( i < shl.length ){
		Log.i(TAG,"Column["+i+"] " + shl[i]);
		clmtxt = i;
		}else{
			continue;
		}
		
		
		//                                                                            //////////////////
		//
		//
		}else//*
		if(lc.length() >= 1){
		//	
		//	
		//                                                                            //////////////////	
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
		Log.i(TAG,"Text["+i+"] " + shl[i]);
		lsttxt = i;
		
		}//*/
		
		
		
		//
		//             v
		// HTML Filter : Gravity : Text Filter : Metrix
		//                                                                            //////////////////
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		//lc = shl[i].toLowerCase();
		
		//for(nxttxt =i+1;nxttxt<shl.length;nxttxt++){if(shl[nxttxt].startsWith("<")){continue;}break;}
		//Log.i(TAG,"Content ["+i+"/"+shl.length+"]========================" + shl[i] +"============"+ (nxttxt == shl.length?"":" n("+shl[nxttxt]+")"));
		//if(lc.length() == 0){continue;}
		//for(nxttxt =i+1;nxttxt<shl.length;nxttxt++){if(shl[nxttxt].startsWith("<")){continue;}break;}
		
		
	    
	    /*
		
		
		
		String[] by = null; 
		if(nw.toLowerCase().matches(".*?([0-9]pm|[0-9]am|<img |mailto:| jan| feb| mar| apr| may| jun| jul| aug| sep| oct| nov| dec).*")){by = nw.split(": ",2);}else{by = nw.split(":",2);}
		String bt = by[0].trim();
		if(by.length > 1 && !by[1].startsWith("//")){}else{bt="";by = new String[]{"",nw};}String bv = by[1].trim();if(bv.length() > 0){nw = bv;}else{bv = "";}
		Log.w(TAG,"Filtering line " + bt.replaceAll("\n","%0a") + "("+bv.replaceAll("\n","%0a")+")");lc = nw.toLowerCase();
		if(bt.length() == 0 && bv.length() == 0){continue;}
		//*
		
		//                                                                            //////////////////
		//
		// Address
		if(lc.length() < 200 && !lc.matches(".*?([0-9]pm|[0-9]am| jan| feb| mar| apr| may| jun| jul| aug| sep| oct| nov| dec).*") && (lc.matches("[0-9][0-9][0-9][0-9][0-9](-[0-9][0-9][0-9][0-9]|-[0-9][0-9][0-9][0-9][0-9]|)") || lc.matches("(.*? |.*? .*? |.*? .*? .*? |)[a-z][a-z]") || lc.matches("(.*? |.*? .*? |.*? .*? .*? |)[a-z][a-z] [0-9][0-9][0-9][0-9][0-9](-[0-9][0-9][0-9][0-9]|-[0-9][0-9][0-9][0-9][0-9]|)") || lc.matches("(.*? |.*? .*? |.*? .*? .*? |.*? .*? .*? .*? )(ave|st|rd|way).*? (\\&|and|x|\\+) .*? (.*? |.*? .*? |.*? .*? .*? |.*? .*? .*? .*? )(ave|st|rd|way).*?") || ( lc.matches("[0-9]+( |-).*?[a-z]+") || lc.matches("[0-9]+( |-).*?(.*?|.*? .*?|.*? .*? .*?)( |)([ensw]|[ensw][ensw])") || lc.matches("[0-9]+( |-)(.*?|.*? .*?|.*? .*? .*?)( |)(ave|st|way|dr|rd).*?( |).*?( |)") ) )){
		// Where 2
		//	                                                       ////////////////////////////
		//                                                                            //////////////////	
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
		//TextView tx5 = (TextView) findViewById(mTag);
		//String lo = "Where";
		
			
			
		//	
		//	
		//	
		//TextView tx5 = (TextView) findViewById(mTag);
		//
		//String lo = "Where";if(tx5!=null){lo = tx5.getText().toString();}
		//
		//
		//
		
		//if(mTog != 2){if(tx5!=null){RelativeLayout r7 = (RelativeLayout) tx5.getParent();if(r7!=null){r7.setVisibility(View.GONE);}}mTag = mTog;mTog = 2;}
		
			
			
		if(addtag_where.length() == 0){addtag_where = findValue(mContent.getChildCount()-1,true);}
		
		
		//RelativeLayout rx5 = (RelativeLayout) mContent.getChildAt(mContent.getChildCount()-1);
		//TextView tx5 = null;
		//if(rx5!=null){tx5 = (TextView) rx5.getChildAt(1);
		//if(tx5!=null){bt = tx5.getText().toString();rx5.setVisibility(View.GONE);}}
		//if(mTog != 2){if(tx5!=null){RelativeLayout r7 = (RelativeLayout) tx5.getParent();if(r7!=null){r7.setVisibility(View.GONE);}}mTag = mTog;mTog = 2;}
		
		cbuildr.setVisibility(View.GONE);
		
		
		if( !lc.matches(".* [a-z][a-z](| [0-9][0-9][0-9][0-9][0-9]| [0-9][0-9][0-9][0-9][0-9]-[0-9][0-9][0-9][0-9]| [0-9][0-9][0-9][0-9][0-9]-[0-9][0-9][0-9][0-9][0-9])") && nxttxt < shl.length && (shl[nxttxt].matches(".* [0-9][0-9][0-9][0-9][0-9].*") || shl[nxttxt].toLowerCase().matches("(.*? |.*? .*? |.*? .*? .*? |)[a-z][a-z](| [0-9][0-9][0-9][0-9][0-9])(|-[0-9][0-9][0-9][0-9]|-[0-9][0-9][0-9][0-9][0-9])")) ){Log.w(G,nxttxt+"-3");nw += " "+shl[nxttxt];i=nxttxt;for(nxttxt =i+1;nxttxt<shl.length;nxttxt++){if(shl[nxttxt].startsWith("<")){continue;}break;}}
		else if( nxttxt < shl.length && !lc.matches(".*?(, | )[a-z][a-z](| .*)") && shl[nxttxt].matches(".*( |,)[a-z][a-z](| .*)") ){Log.w(G,nxttxt+"-2");nw += " "+shl[nxttxt];i=nxttxt;for(nxttxt =i+1;nxttxt<shl.length;nxttxt++){if(shl[nxttxt].startsWith("<")){continue;}break;}}
		addtag_detail = bt.length()>0?bt:"Where4";
		addtag_click = android.R.drawable.ic_menu_mapmode;
		addtag_type = addtag_where.length()>0?addtag_where:"Where4";
		t1.setClickable(true);
		nw = nw.replaceAll("\n.*","");
		bcolor = Color.argb(205,80,30,30);nt = "geo:0,0?q="+nw.replaceAll("  "," ").trim().replaceAll(" ","+");final String fnt = nt;
		if(addtag_whenall.length() == 0){addtag_whenall =  (addtag_whentitle.length()>0?addtag_whentitle+"\n":"");}// note the over engineered view for the future.
		addtag_whenall += (bt.length()>0?bt + "\n":"") + nw + "\n";
		
		nw = (addtag_where.length()>0?addtag_where+"\n":"") + nw;
		final String flo = bt;
		t1.setOnClickListener(new OnClickListener(){public void onClick(View v){Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(fnt));startActivity(myIntent);Log.w(TAG,"Action View " + fnt);}});
		Log.i(TAG,"found address " + nw.replaceAll("\n","%0a") + "  tag " + mTag);	
		t1.setOnLongClickListener(new OnLongClickListener(){public boolean onLongClick(View v){Toast.makeText(mCtx,flo,3800).show();return false;}});
		
		
		//*                                                                            //////////////////
		//
		// Happy November 19th
		}else if(lc.length() < 80 && lc.matches(".*?(nov|dec|jan|feb|mar|apr|may|jun|jul|aug|sep|oct).*? [0-9]+(,| +).*?[0-9][0-9][0-9][0-9]+(|[a-z]+)(.|)")){
		// Agenda 3
		//
		//                                                                            //////////////////	
		// && lc.matches("(wed|thu|fri|sat|sun|mon|tue).*")	
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
		if(nxttxt < shl.length && shl[nxttxt].length() < 30 && !shl[nxttxt].toLowerCase().trim().matches(".*[0-9]+( +|)(pm|am)")){	
			shl[i] += " " + shl[nxttxt].trim();
		    //i = nxttxt;	
		}
			
			
			
			
		cbuildr.setVisibility(View.GONE);	
			
		//	
		String lc1 = lc.replaceAll(" +(nov|dec|jan|feb|mar|apr|may|jun|jul|aug|sep|oct).*? ","");
		//
		
		bt = shl[i].substring(0,lc1.length());
		nw = shl[i].substring(lc1.length());
		
		addtag_detail = "When3";
		addtag_click = android.R.drawable.ic_menu_agenda;
		addtag_type = bt.length()>0?bt:"When3";
		bcolor = Color.argb(205,50,50,30);
		//bcolor = colorm2;
		if(addtag_whenall.length() == 0){addtag_whenall =  (addtag_whentitle.length()>0?addtag_whentitle+"\n":"");}// note the over engineered view for the future.
		addtag_whenall += (bt.length()>0?bt + "\n":"") + nw + "\n";
		nw = (addtag_when.length()>0?addtag_when + "\n":"") + (bt.length()>0?bt + "\n":"") + nw;
		
		Log.i(TAG,"found agenda date ====" + bt + "======" + nw	+"======");
		
		//                                                                            //////////////////
		//
		//  Nov 7, 2010 2pm
		}else //*
		if(lc.length() < 50 && lc.matches(".*?(nov|dec|jan|feb|mar|apr|may|jun|jul|aug|sep|oct).*?([0-9]+.*?[0-9][0-9][0-9][0-9].*|[0-9]+)") ){// || lc.matches(".*[0-9]+( +|)(pm|am).*")  ){
		//	Agenda 4
		//	
		//                                                                            //////////////////	
		
			
		cbuildr.setVisibility(View.GONE);
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
		//nw = nw.replaceAll(" : ",":");
		lc = nw.toLowerCase();
			
		//if(addtag_when.length() == 0){
		//
		//RelativeLayout cr6 = (RelativeLayout) mContent.getChildAt(mContent.getChildCount()-1) ;
		//if(cr6!=null){
		//TextView ct6 = (TextView) cr6.getChildAt(1);
		//if(ct6!=null){
		//	addtag_when = ct6.getText().toString().replaceAll(".*?:","").trim();
		//
		//	
		//}}/
		//}//
		//if(addtag_when.length() == 0){addtag_when = findValue(mContent.getChildCount()-1,true);}
		//if(bt.length() == 0){
		//TextView tx5 = (TextView) findViewById(mTag);
		//if(tx5!=null){a = tx5.getText().toString();}
		//if(mTog != 2){if(tx5!=null){RelativeLayout r7 = (RelativeLayout) tx5.getParent();if(r7!=null){r7.setVisibility(View.GONE);}}mTag = mTog;mTog = 2;}
		if(nxttxt < shl.length && shl[nxttxt].trim().toUpperCase().matches("([0-9]+|[0-9]+:[0-9]+).*?(PM|AM)") || shl[nxttxt].toLowerCase().matches(".*(nov|dec|jan|feb|mar|apr|may|jun|jul|aug|sep|oct).*?[0-9]+.*?")){ i = nxttxt;nw += " "+shl[nxttxt].trim(); for(nxttxt =i+1;nxttxt<shl.length;nxttxt++){if(shl[nxttxt].startsWith("<")){continue;}break;}}
		//}
		//(addtag_whentitle.length()>0?addtag_whentitle + "\n":"") + 
		nw = (bt.length()>0?bt + "\n":"") + nw;
		
		addtag_detail = bt;
		addtag_click = android.R.drawable.ic_menu_today;
		addtag_type = addtag_whentitle.length()>0?addtag_whentitle:"When4";
		//t1.setClickable(true);
		bcolor = Color.argb(245,10,70,20);//nt = "cal:0,0?q="+nw.replaceAll("  "," ").trim().replaceAll(" ","+");final String fnt = nt;
		if(addtag_whenall.length() == 0){addtag_whenall =  (addtag_whentitle.length()>0?addtag_whentitle+"\n":"");}
		addtag_whenall += (bt.length()>0?bt + "\n":"") + nw + "\n";
		String gbt = nw.toLowerCase().trim();
		gbt = gbt.replaceAll(","," "); 
		gbt = gbt.replaceAll(" +"," ");
		
		gbt = gbt.replaceAll("(sun|mon|tue|wed|thu|sat|fri)([a-z])+","\n").trim();
		String gts[] = gbt.split("\n");
		String gbt1 = gts[0].trim();
		String gbt2 = gts.length > 1?gts[1].trim():"";
		
		Date xp = new Date();
		Date p = calDate(gbt1,xp.getTime());
		Date p2 = calDate(gbt2,p.getTime() + (10 * 60 * 1000) );
		
		
		Log.i(TAG,"found date ("+gbt1+" - "+gbt2+") ("+( (p2.getTime()-p.getTime()) / 1000/60)+") "+addtag_whentitle.replaceAll("\n","%0a")+" " + addtag_contact.replaceAll("\n","%0a") + " " + bt.replaceAll("\n","%0a") + " "+ nw.replaceAll("\n","%0a") + "  tag " + mTag);
		
		//
		//if(gbt.matches("")){}
		//String ndh = year+"-"+(month <10?"0":"")+month+"-"+(day<10?"0":"")+day+" "+hour+":"+(minute<10?"0":"")+minute;
		//Cursor gu = SqliteWrapper.query(mCtx, mCtx.getContentResolver(), geturi, //Uri.withAppendedPath(DataProvider.CONTENT_URI,"moment"), 
	    //		//new String[] { "_id", "address", "body", "strftime(\"%Y-%m-%d %H:%M:%S\", date, \"unixepoch\", \"localtime\") as date" },
	    //		//strftime("%Y-%m-%d %H:%M:%S"
	    //		new String[] { "strftime('%s','"+ndh+"')" },
		//		//new String[] { "_id", "address", "body", "date" },
	    //		"status > -10",
	    //		null, 
	    //		null);//
		// EC
		
		//if( gu == null ){Log.e(TAG,"gu null getting date in seconds ->" + bt.replaceAll("\n","%0a") + "<-");return;}
		//startManagingCursor(gu);
		//if( !gu.moveToFirst() ){Log.e(TAG,"gu unavilable  getting date in seconds ->" + bt.replaceAll("\n","%0a") + "<-");}//return;}
		//if( gu.getColumnCount() < 1 ){Log.e(TAG,"gu empty  getting date in seconds ->" + bt.replaceAll("\n","%0a") + "<-");}//return;}
		//else{
		final long es = p.getTime();
		Log.i(G," p " + p.getTime() + "  " + "   " + es);
		final long ee = p2.getTime();final String addtag_whentitlef = addtag_whentitle;final String addtag_wheref = addtag_where;
		final String addtag_typef = addtag_type; final String allcf = allc;
		t1.setOnClickListener(new OnClickListener(){public void onClick(View v){
		
			Intent intent = new Intent(Intent.ACTION_EDIT);
			intent.setType("vnd.android.cursor.item/event");
			intent.putExtra("title", addtag_whentitlef);
			intent.putExtra("where", addtag_wheref);
			intent.putExtra("description", allcf);
			intent.putExtra("beginTime", es);
			intent.putExtra("endTime", ee);
			startActivity(intent);
		}	});
		
		
		//Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(fnt));startActivity(myIntent);
		//Log.w(TAG,"Action View " + nt);
		
		
		
		
		//																		      //////////////////
		//
		// IMAGE
		}else if(lc.matches("<img .*?src.*?=.*?>") ){
		//
		//	
		//                                                                            //////////////////	
		
		
			
			
			
			
			
			
			
		
		//cbuildr.setBackgroundResource(R.drawable.iget);
		//send url and cbuildr's id to handler.
		
		
		//                                                                            //////////////////
		//
		// EMAIL
		}else if(lc.matches("(.*?|)<a.*?mailto.*?:.*?@.*?>") || lc.matches("[a-z0-9_\\.-=+,]+@[a-z0-9_\\.-=+,]+") ){
		//
		//	
		//                                                                            //////////////////	
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
		//String lts = shl[lsttxt];
		if(lc.matches("<a.*?mailto.*?:.*>") ){
		nw = nw.replaceAll(".*?mailto.*?:",""); nw = nw.replaceAll("(\"|'| |).*?>","").trim();
		}
									
		if(addtag_contact.length() == 0){addtag_contact = findValue(mContent.getChildCount()-1,true);}
		cbuildr.setVisibility(View.GONE);
		//if(addtag_contact == -2 && lts.toLowerCase().matches("([0-9a-z]+ [0-9a-z]+|[0-9a-z]+ [a-z] [0-9a-z]+)$")){
		//addtag_contact = lsttxt;
		//}
									
		if(addtag_contact.length() > 0){nw = addtag_contact + " " + nw;}
		Log.i(TAG,"Found email "+ addtag_contact+" " + nw + "  tag "+mTag);
		//Log.i(TAG,"found contact "+addtag_contact+" email " + nw + "     " + addtag_detail+" 	" + addtag_contact + " taggable " + shl[lsttxt] + " column " + shl[clmtxt]);
		nw = shl[i].replaceAll(".*mailto.*?:","").replaceAll("\".*","").replaceAll(">.*","").trim();
		nw = (addtag_contact.length()>0?addtag_contact+"\n":"") + nw;
		if(addtag_contactall.length() == 0){addtag_contactall =  (addtag_contact.length()>0?addtag_contact+"\n":"");}
		addtag_contactall += (bt.length()>0?bt:"Email")+": "+addtag_contact+" <"+nw+">\n";
		addtag_detail = bt.length()>0?bt:"";
		addtag_click = android.R.drawable.ic_menu_send;
		addtag_type = addtag_detail.length() > 0?addtag_detail:"Email2";
		//bcolor = colorm2;
		bcolor = Color.argb(205,40,80,80);
		
		
		//                                                                            //////////////////
		//
		// PHONE NUMBER
		}else if(lc.length() < 50 && lc.matches(".*?[0-9][0-9][0-9].*?[0-9][0-9][0-9][0-9](|x.*|e.*|#.*)"))	{
		//
		//	
		//                                                                            //////////////////
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
		if(addtag_contact.length() == 0){addtag_contact = findValue(mContent.getChildCount()-1,true);}
		cbuildr.setVisibility(View.GONE);
		//if(lc.matches(".*?(([0-9]+|[a-z]+) ([0-9]+|[a-z]+)|([0-9]+|[a-z]+) [a-z](.|) ([0-9]+|[a-z]+))$")){
		//nw = addtag_contact + "\n" + nw;
		//}//else if(addtag_contact.length() > 0){addtag_contact = "";}
		//String[] by = nw.split(":",2);
		//String bt = by[0].trim();
		//String bv = by[1].trim();if(bv.length() > 0){nw = bv;}
		//if(addtag_contact.length() > 0){nw = addtag_contact + " " + nw;}
		String num = nw.toLowerCase().replaceAll("(-|[a-z]| )","");
		nw = (addtag_contact.length()>0?addtag_contact+"\n":"") + nw;
		addtag_detail = "Call2";
		addtag_click = android.R.drawable.ic_menu_call;
		addtag_type = bt.length() > 0?bt:"Call2";
		//t1.setClickable(true);
		
		bcolor = Color.argb(205,50,80,80);
		//bcolor = colorm2;
		
		Log.i(TAG,"found telephone "+(addtag_contact.length()>0?addtag_contact:"")+" "+(bt.length()>0?bt:"phone number")+" " + addtag_detail + " " + nw);
		
		final String ftn = "tel:"+num;//
		t1.setOnClickListener(new OnClickListener(){public void onClick(View v){Intent myIntent = new Intent(Intent.ACTION_DIAL, Uri.parse(ftn));startActivity(myIntent);Log.w(TAG,"Action Dial " + ftn);}});
		
		
		//}else if(lc.matches("(\\$[0-9]|\\$[0-9]\.[0-9]+)") ){
		//bcolor = Color.argb(205,40,148,60);
		//addtag_type = bt.length()>0?bt:"Money";
		
		
		
		
		//                                                                            //////////////////
		//
		// URL
		}else if( lc.matches(".*?@.*?(www|http|[a-z]|[0-9]).*?\\.([a-z][a-z]|[a-z][a-z][a-z])(/.*|)") && lc.indexOf(" ") < 0 ){
		// URL
		//	
		//                                                                            //////////////////
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
		//nw = lc;
		addtag_detail = "Email3";
		addtag_click = android.R.drawable.ic_menu_send;
		addtag_type = bt.length()>0?bt:"Email3";
		bcolor = Color.argb(205,45,80,80);
		bcolor = colorm3;
		cbuildr.setVisibility(View.GONE);
		nw = (addtag_contact.length()>0?addtag_contact+"\n":"") + nw;
		if(addtag_contactall.length() == 0){addtag_contactall =  (addtag_contact.length()>0?addtag_contact+"\n":"");}
		addtag_contactall += (bt.length()>0?bt:"Email")+": "+addtag_contact+" <"+nw+">\n";
		Log.i(TAG,"Send " + bt + " " + nw);
		nw = (addtag_contact.length()>0?addtag_contact+"\n":"") + nw;
		//                                                                            //////////////////
		//
		// 
		}else if( lc.matches(".*?(www|http|[a-z]|[0-9]).*?\\.([a-z][a-z]|[a-z][a-z][a-z]|[a-z][a-z][a-z][a-z])(/.*|)") && lc.indexOf(" ") < 0 ){
		// URL 2
		//	
		//                                                                            //////////////////	
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
		//nw = lc;
		addtag_click = R.drawable.ic_menu_forward;
		addtag_type = "URL3";//bt.length()>0?bt:"URL";
		addtag_detail = "URL3";
		bcolor = Color.argb(205,48,80,80);
		//bcolor = colorm3;
		Log.i(TAG,"URL " + bt + " " + nw);
		cbuildr.setVisibility(View.GONE);
		nw = (addtag_contact.length()>0?addtag_contact+"\n":"") + nw;
		//                                                                            //////////////////
		//
		//
		}else if(lc.matches("<a.*?href(.*?)=(.*?)(\"|').*?(\"|'| )(.*?)>")){
		// URL 3
		//	
		//                                                                            //////////////////	
			
			
			
			
			
			
			
			
			
			
			
			
			
			
		if(nw.matches(".*? [tT][iI][tT][lL][eE](.*?)=.*")){	
		
			bt = nw.replaceAll(".*? [tT][iI][tT][lL][eE](.*?)=(.*?)(\"|')","");bt = bt.replaceAll("(\"|'|>)(.*|)","").trim();
		
		Log.w(G,"########### ("+bt+") (" + nw + ") ");
		}
		cbuildr.setVisibility(View.GONE);
		
		//nw = nw.replaceAll(" : ",":");	
		nw = nw.replaceAll(".*?[hH][rR][eE][fF](.*?)=(.*?)(\"|'| |)","");nw = nw.replaceAll("(\"|'|>| )(.*)","");
		
		lc = nw.toLowerCase();
		//if(addtag_linktitle.length() == 0){
		for(nxttxt=i+1;nxttxt < shl.length;nxttxt++){if(shl[nxttxt].matches("</[aA].*?>")){break;}if(!shl[nxttxt].matches("<")){addtag_linktitle += shl[nxttxt] + "\n";}}
		
		addtag_linktitle = addtag_linktitle.trim();//shl[nxttxt];
		//if(addtag_linktitle.matches("(<img.*>|.*? [a-z][a-z] [0-9]+.*?)")){}else{i = nxttxt;}
		//}
		//if(addtag_linktitle.length() == 0){addtag_linktitle = getValue(mContent.getChildCount()-1);}
		//if(addtag_linktitle.length() == 0 || addtag_linktitle.length() > 100){if(lc.matches("<.*?title(.*?)=(.*?)(\"|'| |).*?(\"|'| )(.*?)>")){addtag_linktitle = shl[i].replaceAll(" : ",":").replaceAll(".*?[tT][iI][tT][lL][eE](.*?)=(.*?)(\"|'| )","");addtag_linktitle = addtag_linktitle.replaceAll("(\"|'| )(.*?)>","");}addtag_detail = addtag_linktitle.length()>0?addtag_linktitle:"URL4";}

		
		//if(addtag_linktitle.matches("<img.*>") ){addtag_linktitle = alt.length()>0?alt:"get link";}
		//nw = (addtag_linktitle.length()>0?addtag_linktitle+"\n":"")+nw;
		if(addtag_linktitle.matches("<[iI][mM][gG].*>") && bt.length() > 0){addtag_linktitle = bt;}
		addtag_detail = bt.length()>0?bt:"URL4";
		addtag_click = R.drawable.ic_menu_forward;
		addtag_type = "URL4";
		bcolor = Color.argb(250,10,80,50);
		Log.i(TAG,"Link ("+addtag_linktitle+") (" + bt + ")############ " + nw.replaceAll("\n","%0a") + " " + "#######");
		
		//                                                                            //////////////////
		//
		// UnFiltered Text
		}else{
		// Unfiltered Text
		//	
		//                                                                            //////////////////
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
		unfilteredtext = true;	
		Log.i(TAG,"Unfiltered text " + lc.replaceAll("\n","%0a"));
		
		}
								
								
								
		lc = nw.toLowerCase();						
								
		//                                                                            //////////////////
		////
		////
		////bcolor = coloroff;
		//// column 
		//if(nxttxt < shl.length){Log.w(TAG,"detail/column check " + shl[nxttxt].replaceAll("\n","%0a"));}
		//if( (lc.endsWith(":") || (nxttxt < shl.length && shl[nxttxt].startsWith(":"))  ) && lc.length() < 100){
		//cbuildr.setVisibility(View.GONE);
		////mTextadd = t1.getId();
		//addtag_detail = "Detail2";
		////addtag_type = nw.replaceAll("( +|):.*",": ");
		//bcolor = Color.argb(205,0,30,30);
		//addtag_type = nw.replaceAll(".*?:( +|)","");
		////addtag_click
		////details += " " + addtag_detail;
		////details += " " + nw.substring(0,nw.indexOf(':')>0?nw.indexOf(':'):nw.length()).trim();
		//Log.i(TAG,"addtog_detail " + addtag_detail.replaceAll("\n","%0a"));
		//}else{ 
		//	
		//
		//}
		//
		//                                                                            //////////////////
		
		
		
		
		
		
		
		
		
		
		
		
		//                                                                            //////////////////
		// Recognized by filter
		//
		//if(addtag_type.length() == 0){
		//
		//	
		//                                                                            //////////////////	
			
			
			
		//                                                                            //////////////////	
		//	MTAG
		//	
		if( bt.length() ==0 && (lc.length() < 300 && mTag == 2) && !lc.startsWith("<")  && i != clmtxt){
		//
		//	
		//                                                                            //////////////////	
		
			
			
			
		//Log.i(TAG,"===============Clearing textadd and contact");
		//addtag_contact = "";
		//mTextadd = 1;
		
		//if(nw.length() < 300 || mTag == 2 && addtag_type.length() == 0){Log.i(TAG,"Setting mTag " + t1.getId() + " mTog " + mTag);
		//if(mTag == 2){t1.requestFocusFromTouch();}
		mTog = mTag;
		mTag = t1.getId();
		Log.i(TAG,"MTAG " + mTag);bcolor = coloroff;
		//}
		//data_only = 2;
		
		
		//                                                                            //////////////////
		// Add Tag Detail
		//
		}else if(bt.length() < 50 && !bt.contains(".") && bt.length() > 0 && !lc.startsWith("<") ){
		//
		//	
		//                                                                            //////////////////	
			
			//data_only = 2;
			
			
			
			
			
			
			
			
			
			
			
			
			
			if(unfilteredtext){
				data_only = t1.getId();
			}else{
			data_only = 2;
			}
			
			
		Log.i(TAG,"############Column DATA (" + bt.replaceAll("\n","%0a") + "): (" + nw.replaceAll("\n","%0a")+")");
		addtag_detail = bt;
		if(bcolor == colorm1){
		bcolor = Color.argb(250,10,70,80);
		}
		//cbuildr.setVisibility(View.GONE);
		
		
		//                                                                            //////////////////
		//
		//
		}else if(bt.length() < 0){
		//	
		//	
		//                                                                            //////////////////	
			
			
			
			//data_only = 2;
			
			
		Log.i(TAG,"Column Data "+bt.replaceAll("\n","%0a")+": " + bv.replaceAll("\n","%0a"));	
			
			
			
		if(bcolor == colorm1){	
		bcolor = Color.argb(250,80,80,20);
		}
		cbuildr.setVisibility(View.GONE);
	
		
		//                                                                            //////////////////
		//
		//
		}else{
		//
		//	
		//                                                                            //////////////////
			
			
		Log.i(TAG,"Data Only " + bv.replaceAll("\n","%0a"));	
		if(data_only != 2 && unfilteredtext){
			TextView ddo = (TextView) findViewById(data_only);
			ddo.append(" " + bv);
			continue;
		}else if(unfilteredtext){
			data_only = t1.getId();
		//}else{
		//data_only = 2;
		}
		if(bcolor == colorm1){
		bcolor = Color.argb(250,50,50,50);
		}
			
			
			
		}
		
		
		//}
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		if(lc.matches("(</|<br|<b>).*")){Log.w(TAG,"tag " + lc.replaceAll("\n","%0a"));continue;}
		
		//t1.setBackgroundColor(Color.argb(20, 100, 190, 100));// android:background="#A0005020"
		//t1.setShadowLayer((float)2.2, 2, 2, Color.argb(247,0,0,0));
		//t1.setTextScaleX((float)0.8);
		if(nw.contentEquals(bt+":")){bt = "";nw=nw.replaceFirst(":$","");columntitle = true;bcolor = Color.argb(180,10,50,90);}
		if(bt.length() > 0){columntitle = true;}
		t1.setText( (bt.length()>0?bt+"\n":addtag_linktitle.length()>0?addtag_linktitle+"\n":"") + nw);
		
		
		
			
			//*/
		
	    
	    //*
		
		
		
		
		
		//
		//	
		//	
			
			
			
			
			
			
			
			//*/
		
	    
	    /*
		
			
			

		
			
			
			
			
		if(addtag_type.length() > 0){	
		
		ScrollView stoolpin =null;TextView vb = null; RelativeLayout bu1 = null; TextView t1in = null;LinearLayout epu = null;
		//
		vb =null;try{vb= (TextView) findViewById(mTag);}catch(ClassCastException ec){Log.e(TAG,"vb");}
		//
		if(vb != null){
		//
		bu1 = (RelativeLayout) vb.getParent();
		
		//
		if(bu1 != null){
		//bu1.setBackgroundColor(Color.argb(180,20,20,60));
			//
		t1in = (TextView) bu1.getChildAt(0);
		//
		stoolpin = (ScrollView) bu1.getChildAt(1);
		r3 = (RelativeLayout) bu1.getChildAt(2);
		//
		if(stoolpin != null){
		stoolpin.setPadding(0,0,0,0);
		//spu.setBackgroundColor(Color.argb(180,30,50,10));
		mScroll[spl] = stoolpin.getId();
		epu = (LinearLayout) stoolpin.getChildAt(0);
		ScrollView.LayoutParams epus = new ScrollView.LayoutParams(56+40,-2);
		epus.setMargins(0,0,0,0);
		epu.setMinimumHeight(20);
		epu.setPadding(13,3,0,3);
		epu.setLayoutParams(epus);
		epu.setGravity(Gravity.BOTTOM);
		stoolpin.setVisibility(View.VISIBLE);
		RelativeLayout.LayoutParams xps2 = new RelativeLayout.LayoutParams(-1,-2); 
		xps2.setMargins(0,0,0,0);//;
		stoolp.setVisibility(View.INVISIBLE);
		xps2.addRule(RelativeLayout.BELOW,t1in.getId());
		stoolp.setLayoutParams(xps2);
		//epu.setBackgroundColor(Color.argb(180,70,80,10));
		}else{Log.e(TAG,"spu null");}
		}else{Log.e(TAG,"bu1 null");}
		}else{Log.e(TAG,"vb null");}
		
		//if(){
		//
		
		//
		//
		if( t1in != null && epu != null){
		//RelativeLayout.LayoutParams rtl = new RelativeLayout.LayoutParams()
		//t1in.setPadding(17,13,17,13);
		//t1in.setBackgroundColor(Color.argb(205,40,2,40));
		
		//
		//
		//
		//cr.setVisibility(View.GONE);
		//
		//
		//
		details += " " + addtag_type;
		//
		//
		//
		//lp2.setMargins(1,10,0,10);
		}//
		
		
		
		
		
		
		
		if(epu == null ){Log.w(TAG,"epu null");}
		
		
		if(epu != null && epu.getChildCount() == 0){
		//t1.setMinimumHeight(38);
		RelativeLayout.LayoutParams sl = new RelativeLayout.LayoutParams(-1,-2);
		//sl.addRule(RelativeLayout.BELOW,spu.getId());
		//t1in.setLayoutParams(sl);
		epu.setVisibility(View.VISIBLE);
		{ImageView i2 = new ImageView(mCtx);
	    LinearLayout.LayoutParams lm = new LinearLayout.LayoutParams(20,50);
	    lm.setMargins(0,1,0,1);
	    i2.setLayoutParams(lm);
	    i2.setScaleType(ScaleType.FIT_XY);i2.setImageResource(R.drawable.horbar);
	    epu.addView(i2,0);}
	    
	    {ImageView i2 = new ImageView(mCtx);
	    LinearLayout.LayoutParams lm = new LinearLayout.LayoutParams(20,50);
	    lm.setMargins(0,1,0,1);
	    i2.setLayoutParams(lm);
	    i2.setScaleType(ScaleType.FIT_XY);i2.setImageResource(R.drawable.holbar);
	    epu.addView(i2);}
		}
											
											
		
		
		if(epu != null){
		LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(55,50);
		if(epu != null && epu.getChildCount() > 2){
		lp2.setMargins(1,1,0,1);
		}else{lp2.setMargins(0,1,0,1);}
		RelativeLayout rt2p = new RelativeLayout(mCtx);
		rt2p.setLayoutParams(lp2);
		rt2p.setBackgroundColor(Color.argb(205,0,0,10));
		
		RelativeLayout.LayoutParams rp2 = new RelativeLayout.LayoutParams(55,50);
		TextView t2 = new TextView(mCtx);
		t2.setPadding(13,3,13,1);
		t2.setGravity(Gravity.BOTTOM);
		t2.setLayoutParams(rp2);
		t2.setTextColor(textcolor);
		t2.setText(addtag_type);t2.setTextSize((float)12);
		t2.setVisibility(View.GONE);
		//epu.addView(t2,epu.getChildCount()-1);
		rt2p.addView(t2);
	
	
		
		
		ImageView i2 = new ImageView(mCtx);
		//LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(85,85);
		i2.setScaleType(ScaleType.CENTER_INSIDE);
		i2.setImageResource(addtag_click);
		i2.setAdjustViewBounds(true);
		i2.setLayoutParams(rp2);
		i2.setPadding(0,0,0,0);
		i2.setOnFocusChangeListener(new OnFocusChangeListener(){public void onFocusChange(View v, boolean h){ImageView iv = (ImageView) v; if(h){iv.setBackgroundColor(coloron);RelativeLayout rx = (RelativeLayout) iv.getParent(); LinearLayout lx = (LinearLayout) rx.getParent(); ScrollView sx = (ScrollView) lx.getParent(); Log.w(TAG,"focus " + rx.getLeft() +" < " + wWidth + " scroll at " + sx.getScrollX());if(rx.getLeft()+sx.getScrollX() > wWidth){{Bundle bl = new Bundle(); bl.putInt("id",spl);bl.putInt("x",56);Message ml = new Message(); ml.setData(bl); siscroll.sendMessage(ml);}}else if(rx.getLeft() < 0){{Bundle bl = new Bundle(); bl.putInt("id",spl);bl.putInt("x",-56);Message ml = new Message(); ml.setData(bl); siscroll.sendMessage(ml);}}}else{iv.setBackgroundColor(Color.argb(205,0,0,0));}}});
		i2.setClickable(true);
		rt2p.addView(i2,0);
		final String fnt = nt;final String fnw = bt + "\n"+ nw;
		//i2.setOnLongClickListener(new OnLongClickListener(){public boolean onLongClick(View v){Log.w(TAG,"Action View " + fnt);Toast.makeText(mCtx,fnt,3800).show();Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(fnt));startActivity(Intent.createChooser(myIntent,"Chooser"));return false;}});
		i2.setFocusable(true);//i2.setFocusableInTouchMode(true);
		i2.setOnTouchListener(new OnTouchListener(){
		boolean movingtouch = false;float lx = 2;
		float hx = 2;float mx = 2;int leri = 2;int d=1;float hs = 2;int ld = 2;
		public boolean onTouch(View v, MotionEvent event) {
		//Log.w(TAG,"Action " + event.getAction() + " " + event.getPressure() + " h("+event.getHistorySize()+") " + movingtouch + " dt("+event.getDownTime()+") " + SystemClock.uptimeMillis() + " " + (SystemClock.uptimeMillis() - event.getDownTime())); 
			 
		 if(event.getAction() == 2 ){// && (SystemClock.uptimeMillis() - event.getDownTime()) > 200){//&& ((event.getHistorySize() > 1 && (movingtouch || event.getPressure() > 0.2 )) || event.getHistorySize() > 2 ) && event.getHistorySize() < 6){
		 
		 //float x = event.getX();if(lx == 2){lx = x;}
		 //float lx = event.getHistoricalX(1);
		 //int d = (int) ((lx-x)*2);
		 //int d = (int)((x-lx)*2);lx = x;
		 //Log.w(TAG,"Move "+event.getHistorySize()+" " + x + " -> " + lx + " scroll " + d );
		 //v.requestFocusFromTouch();
		 
		 mx = event.getX();
			
		if(event.getHistorySize() > 0){hx = event.getHistoricalX(1);}
		//if(mx < hx){hs = hx; hx = mx; mx = hs;}
		
		if(mx < hx){ leri = 1; d = (int) ((hx-mx)*2); }//left
		else if(mx > hx){ leri = 2; d = (int) ((hx-mx)*2);}//right
		
		
		//if(d > 20 || d < -20){d = (int) ((hx-mx)*2);}
 	

		 //Log.w(G,"Move "+(leri==2?"right":"left")+" "+event.getHistorySize()+" " + mx + " -> " + hx + " scroll " + d );
		 hx = mx;
		 if( (d > 60 && ld <0) || (d < -60 && ld >0) ){Log.e(TAG,"-x-");return true;}
		 ld = d;
		 
		 if(d > 8 || d < -8){movingtouch = true;}
		 
		 if(movingtouch ){
		 {Bundle bl = new Bundle(); bl.putInt("id",spl);bl.putInt("x",d);Message ml = new Message(); ml.setData(bl); siscroll.sendMessage(ml);}
		 }
		 return true;
		 //float bx = -1; if(event.getHistorySize() > 2){bx = event.getHistoricalX(2);}
		 //Log.w(G,"Move "+event.getHistorySize()+" " + x + " -> " + lx + " " + bx);
		 }else if(event.getAction() == MotionEvent.ACTION_UP){  
			 ImageView iv = (ImageView) v; iv.clearFocus();//ImageView iv = (ImageView) v;ImageView iv = (ImageView)v;
		 if(movingtouch){movingtouch = false;return true;}else{ iv.performClick();}iv.setBackgroundColor(coloroff);return true;
		 }else if(event.getAction() == MotionEvent.ACTION_DOWN){
		 ImageView iv = (ImageView) v; iv.requestFocusFromTouch();// iv.setBackgroundColor(Color.MAGENTA);
		 movingtouch = false;
		 }
		 return false;}});
	
		i2.setOnClickListener(new OnClickListener(){public void onClick(View v){Toast.makeText(mCtx,fnw,3800).show();}});
	
		epu.addView(rt2p,epu.getChildCount()-1);
		epu.setLayoutParams(new ScrollView.LayoutParams(epu.getChildCount()*56+40,-2));
		
	
	
		}
		
		
		
		}
		
		
		
		
		
		//*/
				
		
								
		
		
		//if(addtag_type.length() > 0){
		//}else{
		//xps2.addRule(RelativeLayout.BELOW,t1.getId());
		//}

	    
		//xps.addRule(RelativeLayout.ALIGN_RIGHT,t1.getId());
		//stoolp.setBackgroundColor(Color.argb(250,0,0,0));
		//mScroll[spl] = spu.getId();
		
	    //	TextView t1 = new TextView(mCtx);
	//      RelativeLayout.LayoutParams ctr = new RelativeLayout.LayoutParams(-1,-1);t1.setPadding(13,7,13,7);
	//      ctr.addRule(RelativeLayout.ALIGN_BOTTOM,ep.getId());
	    //  t1.setId(uniq++);
	   // t1.setLayoutParams(ctr);
	    //t1.setMinimumHeight(90);
	    //t1.setGravity(Gravity.BOTTOM);
	    	
		
		
		//t1.setGravity(Gravity.BOTTOM);
		//rlp3.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		//cbuildr<t1 stoolp(ScrollView)<ephoz(LinearLayout)> flatpearl(TL) flatpearl2(TR) flatpearlb(BR) flatpearl3(BL) addtag_click>

		//ep.setVisibility(View.GONE);
		
			
		
		//RelativeLayout pvl = (RelativeLayout) getListView().getViewFromPosition(getListView().getCount()-1);
		
		
	    
	    
	    
		/*{ImageView i4 = new ImageView(mCtx);
	    RelativeLayout.LayoutParams rp = new RelativeLayout.LayoutParams(-2,-1);
	    LinearLayout bi1 = new LinearLayout(mCtx);
	    bi1.setGravity(Gravity.BOTTOM);
	    bi1.setLayoutParams(rp);
	    i4.setLayoutParams(new LinearLayout.LayoutParams(-2,-2));
	    bi1.setOrientation(LinearLayout.HORIZONTAL);
	    i4.setPadding(0, 0, 0, 0);
	    i4.setScaleType(ScaleType.MATRIX);i4.setImageResource(R.drawable.flatpearl3);
	    bi1.addView(i4);
	    //if(ci == cn.length-1){i4.setVisibility(View.GONE);}
	    cr.addView(bi1);}//*/
		
	    
	    // && mContent.getChildCount() > 1){// mTag != t1.getId()){
			
		
    
	
	
	
	
	
	
	

	//*{	
	//Button bb1 = new Button(mCtx);
	//bb1.setLayoutParams(lp3);
	//bb1.setId((int)SystemClock.uptimeMillis());
	//bb1.setText( mReg.getString("sourcetitle",August.title) );
	//bb1.setBackgroundColor(Color.argb(120, 0, 0, 0));// android:background="#A0005020"
	//bb1.setTextSize(21);
	//bb1.setOnClickListener(new OnClickListener(){public void onClick(View v){Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(August.dest));startActivity(myIntent);}});
	//mContent.addView(bb1);
	//}//*/
	
	
	//*
	//TextView bs = new TextView(mCtx);
	//bs.setLayoutParams(lp3);
	//bs.setMinimumHeight(121);
	//mContent.addView(bs);
	//*/
	//mContent
				
	//easyLoadData(link,html);
	//mContent.loadData(html, "text/html", "utf-8");
	//mContent.reload();
				
	
	
	
		
	//mBrowser.addJavascriptInterface(new AndroidBridge(), "android");
	
	
	
	
	
	
	
	


	
	int nxthour = -2;int nxtminute = 10;
	private Date calDate(String gbt, long millis) {
		Date p = new Date(millis);
		
		
		//Date p = new Date();
		int hour = 10; int minute = 10; int day = p.getDate(); int month = p.getMonth()+1; int year = p.getYear()+1900;
		p.setSeconds(0);p.setMinutes(((int)p.getMinutes()/10) * 10);
		if(gbt.matches(".*(nov|dec|jan|feb|mar|apr|may|jun|jul|aug|sep|oct).*")){
		String g[] = gbt.split(" ");Log.w(G,"calDate (" + gbt + ")");
		for(int u = 0; u < g.length; u++){
			if(g[u].length() == 4 && g[u].matches("[0-9][0-9][0-9][0-9]") ){year = Integer.parseInt(g[u]);p.setYear(year-1900);year = p.getYear()+1900;Log.w(G,"year " + year);
			if(g[u-1].length() == 2 || g[u-1].length() == 1 &&g[u-1].matches("[0-9]+")){
				
				day = Integer.parseInt(g[u-1]);
				Log.w(G,"day "+ day);
				p.setDate(day);
			}
			}
		}
		Log.w(G,"ap ======= " + gbt);
		if(gbt.matches(".*? ([0-9]+|[0-9]+:[0-9]+|)(pm|am|)( |-|)([0-9]+|[0-9]+:[0-9]+)(pm|am)")){
			String ghy = gbt.replaceAll(".*? ([0-9][0-9][0-9][0-9]|[0-9]+) ","").trim();
			//String pm = "am";if(gbt.contains("pm")){pm = "pm";}
			//ghy = ghy.replaceAll("(am|pm)","");
			ghy = ghy.replaceAll("-"," ");
			Log.w(G,"time (" + ghy+")");
			String ghys[] = ghy.split(" ");
			if(ghys.length > 0){
				boolean pm = false; if(ghys[0].contains("pm") || (ghys.length > 1 && ghys[0].matches("([0-9]+|[0-9]+:[0-9]+)") && ghys[1].contains("pm")) ){pm = true;}
				ghys[0] = ghys[0].replaceAll("(pm|am)","").trim();
				//Log.w(G,"hour/minute (" + ghys[0]+")" + pm);
				if(ghys[0].contains(":")){
					String p5[] = ghys[0].split(":");
					if(p5.length > 0 && p5[0].matches("[0-9]+")){hour = Integer.parseInt(p5[0]);Log.w(G,"hour "+hour);}
					if(p5.length > 1 && p5[1].matches("[0-9]+")){minute = Integer.parseInt(p5[1]);Log.w(G,"minute " + minute);}
				}else if(ghys[0].matches("[0-9]+")){
					hour = Integer.parseInt(ghys[0]);Log.w(G,"hour " + hour);
					minute = 0;
				}
				if(pm){hour+=12;}
				p.setHours(hour);
				p.setMinutes(minute);
				//Log.i(G,"Set hour("+hour+") minute("+minute+")");
			}
			if(ghys.length > 1){
				//Log.w(G,"cal time to");
				boolean pm = false;
				if(ghys[1].contains("pm")){pm = true;}
				ghys[1] = ghys[1].replaceAll("(pm|am)","");
				if(ghys[1].contains("[0-9]+:[0-9]+")){
				String g8[] = ghys[1].split(":");
				nxthour = Integer.parseInt(g8[0]);Log.w(G,"nxthour ("+nxthour+")"+pm);
				nxtminute = Integer.parseInt(g8[1]);Log.w(G,"nxtminute ("+nxtminute+")");
				}else if(ghys[1].matches("[0-9]+")){
				nxthour = Integer.parseInt(ghys[1]);Log.w(G,"nxthour ("+nxthour + ")"+pm);
				nxtminute = 0;
				}
				
				if(pm){nxthour+=12;}
				//Log.i(G,"Set nxt hour("+nxthour+") minute("+nxtminute+")");
			}
		}
		if( gbt.contains("dec") ){month = 12;}
		if( gbt.contains("nov") ){month = 11;}
		if( gbt.contains("oct") ){month = 10;}
		if( gbt.contains("sep") ){month = 9;}
		if( gbt.contains("aug") ){month = 8;}
		if( gbt.contains("jul") ){month = 7;}
		if( gbt.contains("jun") ){month = 6;}
		if( gbt.contains("may") ){month = 5;}
		if( gbt.contains("apr") ){month = 4;}
		if( gbt.contains("mar") ){month = 3;}
		if( gbt.contains("feb") ){month = 2;}
		if( gbt.contains("jan") ){month = 1;}
		
		p.setMonth(month-1);Log.w(G,"month "+month);
		}else if(nxthour >-1){
		p.setHours(nxthour);p.setMinutes(nxtminute);
		}
			
		return p;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	private String findValue(int mcontentindex,boolean hide2){
	
		RelativeLayout cr6 = (RelativeLayout) mContent.getChildAt(mcontentindex) ;
		if(cr6!=null){
		TextView ct6 = (TextView) cr6.getChildAt(0);
		if(ct6!=null){
			if(hide2){cr6.setVisibility(View.GONE);}
			return ct6.getText().toString().replaceAll(".*?(:|\n)","").trim();
		
		//Log.i(TAG,"found addtag_when in When4 " + addtag_when );			
		}}
		
		return "";
		
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	 Handler handleNotification = new Handler(){
	    	public void handleMessage(Message msg){
	    		
	    		Bundle bdl = msg.getData();
	    		Thread tx = new Thread(){
	    			public void run(){
	    				SharedPreferences mReg = getSharedPreferences("Preferences", MODE_WORLD_WRITEABLE);
	    				if(mReg.contains("notifier")){
	    					NotificationManager mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
	    					mNM.cancelAll();
	    					Editor mEdt = mReg.edit();mEdt.remove("notifier");mEdt.commit();
	    				}
	    			}
	    		};
	    		tx.start();
	    	}	
	    };
    Handler snapshot = new Handler(){
    	public void handleMessage(Message msg){
    		Bundle bdl = msg.getData();
    		String req = bdl.containsKey("filename")?bdl.getString("filename"):"";//i.e.3rg.png will showup as File(getfilepath(),"screenshot/3rg.png")
    		LinearLayout ba = (LinearLayout) findViewById(R.id.browser);
			ba.setDrawingCacheEnabled(true);
			ba.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
			Bitmap m = ba.getDrawingCache();
			Date u = new Date();int year = u.getYear() + 1900; int mon = u.getMonth()+1; int day = u.getDate();
			String ti = (u.getHours()>9?"":"0")+u.getHours()+""+(u.getMinutes()>9?"":"0")+u.getMinutes();
			String nt = Uri.encode(rtitle,"([A-Z]|[a-z]|[0-9]|)");nt = nt.replaceAll("%([0-9]|[A-Z])([0-9]|[A-Z])","");
			String filename = nt + ""+year+""+mon+""+day+""+ti+".png";
			if(req.length() > 0 ){filename = req;}
			
			FileOutputStream fs = null;
			
			File filea = new File(getfilepath(),"screenshot"); 
			filea.mkdirs();
			if(filename == null || m == null || filea == null){return;}
			Log.w(TAG,"External Storage Status("+filea.getAbsolutePath()+") file("+filename+") size("+m.getWidth()+","+m.getHeight()+")");
			
			//if(Environment.getExternalStorageState().contentEquals(Environment.MEDIA_MOUNTED)){
				//filea = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "wave");
			//}else{
				//filea = new File(Environment.getDownloadCacheDirectory().getAbsolutePath());
			//}
			File file = new File(filea.getAbsolutePath(), filename);
			//file.createNewFile();
			Bitmap b3 = null;
			int h = m.getHeight();int w = m.getWidth();
			if(h > w){
				if(h < 480){h = 480;}else if(h < 800){h = 800;}else if(h < 854){h = 854;}
			}else{
				if(h < 320){h = 320;}else if(h < 480){h = 480;}
			}
			try{b3 = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
			}catch(OutOfMemoryError eom){Log.e(G,"Out of Memory during image compression.");return;}
            
			Canvas c = new Canvas(b3);
			Paint p2 = new Paint();p2.setDither(true);
			c.drawBitmap(b3, 0, 0, p2);
			c.drawBitmap(m,0,10,p2);
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            b3.compress(Bitmap.CompressFormat.PNG, 0, os);
			byte[] array = os.toByteArray();
			
			try {
				fs = new FileOutputStream(file);
				//b3.compress(CompressFormat.PNG,0,fs);
				fs.write(array);
				fs.flush();
                fs.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			
			} catch (IOException e1) {
                    Log.e(TAG, "ComputerStart() 1534 fs.close() failed");
                    e1.printStackTrace();
            }
		
            if(file.exists() && req.length() == 0){
            	//easyStatus("Snapshot Saved " + filename);
            	Intent et = new Intent(Intent.ACTION_SEND);
            	et.setType("message/rfc822");
            	et.putExtra(Intent.EXTRA_STREAM,Uri.parse("file://"+file.getAbsolutePath()));
            	et.putExtra(Intent.EXTRA_SUBJECT,"");
            	et.putExtra(Intent.EXTRA_TEXT,"\n\n\n\n"+rpublished+"\n"+rtitle+"\n"+rlink+"\n\n");
            	startActivity(Intent.createChooser(et,"Email"));
            	
            	//%file1%
            	
            }
    	
    	}  
	};

	
	
	/*
	private void easyLoadData(String html){
		Message msg = new Message();
		Bundle bdl = new Bundle();
		bdl.putString("html", html);
		msg.setData(bdl);
		mLoadData.sendMessage(msg);
	}
	private void easyLoadData(String url, String html){
		Message msg = new Message();
		Bundle bdl = new Bundle();
		bdl.putString("url", url);
		bdl.putString("html", html);
		msg.setData(bdl);
		mLoadData.sendMessage(msg);
	}
	private Handler mLoadData = new Handler(){
		public void handleMessage(Message msg){
			Bundle bdl = msg.getData();
			String html = bdl.containsKey("html") ? bdl.getString("html") : "";
			String url = bdl.containsKey("url") ? bdl.getString("url") : "";
			mContent.getSettings().setJavaScriptEnabled(true);
			if( url.length() > 0 ){
				Log.w(TAG,"Loaded page with baseurl " + url);
				mContent.loadDataWithBaseURL(url, html, "text/html", "UTF-8", url);
			}else{
				Log.w(TAG,"Loaded page");
				mContent.loadData(html, "text/html", "UTF-8");
			}
			mLookatData.sendEmptyMessageDelayed(2,75);
		}
	};//*/
	
	
	
	
	private Handler setFocusOn = new Handler(){public void handleMessage(Message msg){
		Bundle bl = msg.getData();int id = bl.getInt("id");
		TextView v = (TextView) findViewById(id); if(v!=null){ v.requestFocusFromTouch();}
	}};
	private Handler setFocusOnR = new Handler(){public void handleMessage(Message msg){
		Bundle bl = msg.getData();int id = bl.getInt("id");
		RelativeLayout v = (RelativeLayout) findViewById(id); if(v!=null){ v.requestFocusFromTouch();}
	}};
	private Handler mLookatData = new Handler(){
		public void handleMessage(Message msg){
			mContent.setVisibility(View.VISIBLE);
			mContent.requestFocus();
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
    	/*int mTextadd = 2;*/
		
	
	
	
	
	
    
    
    
    
    
    int mBcolor[];
    ScrollView sp;int mScroll[];
    Handler siscroll = new Handler(){
		public void handleMessage(Message msg){
			//int foo = msg.what;//si.getScrollX()+
			
			//if(sp == null && mScroll != null && mScroll.length > sp){}
			//if(msg.what == -1){sp = null;}
			
			Bundle bdl = msg.getData();
			int spid = bdl.getInt("id");
			int foo = bdl.getInt("x");
			//Log.w(G,foo);
			ScrollView sp = (ScrollView) findViewById(mScroll[spid]);
			if(sp!=null){sp.smoothScrollBy(foo, 0);Log.w(TAG,foo+"");}else{Log.e(TAG,"scroll " + spid);}
		
		}
	};
    
	File getfilepath(){
    	File file = null;
    	
		if(Environment.getExternalStorageState().contentEquals(Environment.MEDIA_MOUNTED)){
			file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "wave");
		}else{
			file = new File(getCacheDir().getAbsolutePath(), "wave");//Environment.getDownloadCacheDirectory().getAbsolutePath()
		}
		//Log.w(TAG,"filepath " + file.getAbsolutePath());
		return file;
    }
    	
    OnFocusChangeListener imagefocus = new OnFocusChangeListener(){public void onFocusChange(View v, boolean has){if(has){v.setBackgroundColor(coloron);}else{v.setBackgroundColor(coloroff);}}};
    OnFocusChangeListener linkfocus = new OnFocusChangeListener(){public void onFocusChange(View v, boolean has){if(has){v.setBackgroundColor(coloron);}else{v.setBackgroundColor(coloroff);}}};
    OnFocusChangeListener textfocus = new OnFocusChangeListener(){public void onFocusChange(View v, boolean has){if(has){v.setBackgroundColor(coloron);}else{v.setBackgroundColor(coloroff);}}};
    private Handler setText = new Handler(){public void handleMessage(Message msg){Bundle bl = msg.getData();int id = bl.getInt("id");String t = bl.getString("text");try{TextView v = (TextView) findViewById(id);if(t!=null){if(bl.containsKey("color")){int co = bl.getInt("color",Color.BLACK);v.setTextColor(co);}v.setText(Uri.decode(t));}}catch(ClassCastException e){Log.e(TAG,"Wrong target for text " + t);}/*int x = bl.getInt("x",10); int y = bl.getInt("y",10); int size = bl.getInt("size",10);v.setPadding(x-size/2, y-size/2, 0, 0);/**/}};
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
    			Log.w(TAG,"External Storage Status("+Environment.getExternalStorageState()+")  cache("+Environment.getDownloadCacheDirectory().getAbsolutePath()+") ("+Environment.getDataDirectory().getAbsolutePath()+")");
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
    		//}catch (IOException e){Log.e(TAG,"img "+e.getLocalizedMessage());}
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
			Log.e(TAG,"Blocked empty get request: Destination titled " + titlr + " intended to " + loc);
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
		Log.i(G,"HttpGet("+Uri.encode(dest)+")");
		HttpGet httpget = null;
		try{
		httpget = new HttpGet(dest);
		}catch(IllegalArgumentException ee){Log.e(G,"Error (" + Uri.encode(dest) + ") " + ee.getLocalizedMessage());
		SharedPreferences mReg = mCtx.getSharedPreferences("Preferences", MODE_WORLD_WRITEABLE);
		
		Editor mEdt = mReg.edit();
		mEdt.putLong("error", System.currentTimeMillis());mEdt.putString("errortype", ee.getLocalizedMessage());mEdt.commit();
		
		
			return;}
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
		        Log.w(TAG,"safeHttpGet() 436 " + mHR.getStatusLine() + " " + " for " + who);
				//{Message mxm = new Message(); Bundle bxb = new Bundle(); bxb.putString("text","Server says "+mHR.getStatusLine().getStatusCode() + " "+mHR.getStatusLine().getReasonPhrase()); mxm.setData(bxb);easyStatusHandler.sendMessageDelayed(mxm,10);}
				//easyStatus();
				
		        Log.w(TAG,mHR.getStatusLine().getStatusCode() + " " + mHR.getStatusLine().getReasonPhrase()+" safeHttpGet() 440 response.getEntity() for " + who);
		        HttpEntity mHE = mHR.getEntity();
	
		        if (mHE != null) {
			        //byte[] bytes = ;
		        	Log.w(TAG,"safeHttpGet() 445 byte[] to EntityUtils.toByteArray(mHE) expect 448");
		        	//freememory = Runtime.getRuntime().freeMemory();
		        	
		        	byte[] mhpb = EntityUtils.toByteArray(mHE);
		        	//String mhpb = EntityUtils.toString(mHE);
		        	//easyStatus("Downloaded into RAM\n"+ (mhpb.length()>1024?(mhpb.length()/1024)+" Kb":(mhpb.length())+" b" ));
		        	Log.w(TAG,"safeHttpGet() 448 mhpb("+mhpb.length+") to String for " + who);
		        	
		        	
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
                        
                        file.mkdirs();
                        path = file.getAbsolutePath();
                        //mEdt.putString(loc, "");mEdt.commit();
                        
		        		//String filename = this.timeStampFormat.format(new Date());
                        //SharedPreferences mReg = getSharedPreferences("Preferences", MODE_WORLD_WRITEABLE);
                        
                        String imgname = "";
                        Log.w(G,"image storage " + loc);
                        if(loc.contains("/") || loc.contains(".")){
                        imgname = loc.replaceAll(".*?://","").replaceAll("\\?.*","").replaceAll(":","");
                        }else{
                        imgname = SystemClock.uptimeMillis() + (mHE.getContentType().getValue().contains("jp")?".jpg":".png");
                        
                        mEdt.putString(loc, imgname);
                        }
                        //File f1 = new File(file,imgname);"/sdcard/wave/"+
                        //FileOutputStream fios = mCtx.openFileOutput(imgname,MODE_WORLD_WRITEABLE);
                        
                        
		        		//ContentValues values = new ContentValues();
                        //values.put(MediaColumns.TITLE, loc);
                        //values.put(ImageColumns.DESCRIPTION, "Wave");
                        //Uri uri = mCtx.getContentResolver().insert(Media.EXTERNAL_CONTENT_URI, values);
                        //Log.w(TAG,"Preparing to utter " + loc + " at uri " + uri.toString());
                        //Log.i(TAG, "ComputerStart() 1524 writting picture to disk name("+imgname+") type("+mHE.getContentType()+") encoding("+mHE.getContentEncoding()+")");
                        //Bitmap p = null;
                        //p.compress(CompressFormat.PNG, 100, fios);
                        
                        
                        FileOutputStream fios = null;
                        File file1 = new File(file, imgname);
                        File pd2 = new File(file1.getPath().substring(0,file1.getPath().lastIndexOf("/")));pd2.mkdirs();
                        
                        
                        Log.i(G,"Writing file to ("+path+") (" + imgname+") ("+file1.getName()+") ("+pd2.getPath()+")");
                        file1.createNewFile();
                        try {
                        fios = new FileOutputStream(file1);
                        fios.write(mhpb);
                        fios.flush();
                        fios.close();
                        } catch (FileNotFoundException ef){
                        	Log.e(G,"File not found " + ef.getLocalizedMessage());
                        }
                        Drawable bh = Drawable.createFromPath(file1.getAbsolutePath());
                        
                        Log.i(G,"wrote file " + file1.getAbsolutePath() + " " + bh.getIntrinsicWidth()+"x"+bh.getIntrinsicHeight() + " " + file1.exists());
                        
                        
                        // compress
                        int w = bh.getIntrinsicWidth();
                        int h = bh.getIntrinsicHeight();
                        if( w > 600 ){
                        	float s = (float) 600.0/(float)w;if(s <= 0){s = (float) 0.5;}
                        	w = (int)(w*s);
                        	h = (int)(h*s);
                        }else{
                        	//float s = (float) w/(float)600.0;if(s <= 0){s = (float) 1;}
                        	//w = (int)(w*s);
                        	//h = (int)(h*s);
                        }
                        Bitmap b3 = null;
            			
            			Bitmap b4 = BitmapFactory.decodeFile(file1.getAbsolutePath());
            			if(b4 !=null){
            				Log.w(G,"decoded file " + w + "x" + h);
            				b3 = Bitmap.createScaledBitmap(b4, w, h, true);
            			}else{
            				try{b3 = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
                			}catch(OutOfMemoryError eom){Log.e(G,"Out of Memory during image compression.");return;}
                            
            			}
            				
            			//Drawable d = mCtx.getResources().getDrawable(R.drawable.place);
            			//Drawable d = Drawable.createFromPath(path+"/"+imgname);
            			//d.setBounds(0,0,(bh.getIntrinsicWidth()/2), (bh.getIntrinsicHeight()/2));
            			//d.setAlpha(245);
            			//d.draw(c);

            			Canvas c = new Canvas(b3);
            			Paint p2 = new Paint();p2.setDither(true);
            			c.drawBitmap(b3, 0, 0, p2);

                        ByteArrayOutputStream os = new ByteArrayOutputStream();
                        b3.compress(Bitmap.CompressFormat.JPEG, 70, os);
            			byte[] array = os.toByteArray();
                        //Bitmap p = null;p.compress(CompressFormat.PNG, 100, fios);
                        
                        //Bitmap bn4 = BitmapFactory.decodeByteArray(array, 0, array.length);

                        String ni = "compressed/"+imgname.substring(0,imgname.lastIndexOf('.'))+".jpg";
                        File pd3 = new File(file.getAbsolutePath() + "/"+ni.substring(0,ni.lastIndexOf("/")));pd3.mkdirs();
                        
                        Log.i(G,"Writing compressed to " +path +"/" + ni);
                        File file2 = new File(file, ni);
                        file2.createNewFile();
                        FileOutputStream fios2 = new FileOutputStream(file2);
                        fios2.write(array);
                        fios2.flush();
                        fios2.close();

                        Log.i(G,"Wrote compressed to " +file2.getAbsolutePath() + " " + file2.length() + " " + file2.exists());
                        
                        
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

		        	}catch(OutOfMemoryError ex){Log.e(TAG,"Out of Memory");
	                }catch(Exception ex){
	                        Log.e(TAG,"Exception " + ex.getLocalizedMessage());
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
			        //if ( !mHttpCookie.isEmpty() ) { for (int i = 0; i < mHttpCookie.size(); i++) { Log.w(TAG,"safeHttpGet() Cookie: " + mHttpCookie.get(i).toString()); } }
			        
			        //
			        // Print Headers
		        	//Header[] h = mHttpResponse.getAllHeaders(); for( int i = 0; i < h.length; i++){ Log.w(TAG,"safeHttpGet() Header: " + h[i].getName() + ": " + h[i].getValue()); }
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
        
		menu.add(0, 405, 0, "ScreenShot").setIcon(android.R.drawable.ic_menu_camera);
		
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
//		mLog.w(TAG,"onOptionsItemSelected()");
		
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
				snapshot.sendEmptyMessageDelayed(2,3000);
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
		//if( (mTitle.isShown() || mTitle.hasFocus()) && mTitle.isEnabled() ){}else{Log.e(TAG,"List isn't shown and nofocus, sensor watch close");wayGo.sendEmptyMessage(2);return;}
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

	private Handler logoly = new Handler(){public void handleMessage(Message msg){Bundle bx = msg.getData();int l = bx.getInt("l");String text = bx.getString("text");switch(l){case 2:Log.e(TAG,":"+text);break;case 3:Log.w(TAG,":"+text);break;default:Log.i(TAG,":"+text);break;}}};
	Handler click = new Handler(){public void handleMessage(Message msg){Bundle bdl = msg.getData();long id = bdl.getLong("id",0);View ct = findViewById((int)id);ct.performClick();}};
	

	Handler wayProceed = new Handler(){
		public void handleMessage(Message msg){
			ScrollView s = (ScrollView) findViewById(R.id.browser_scroll);
			Log.i(TAG,"Scroll at "+s.getScrollY());
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
			
			File file1 = null; File file1c = null;
			String[] fs = rimages.split("\n");
			for(int fi = 0; fi < fs.length; fi++){
				String filename = getfilepath() + "/" + fs[0].replaceAll(".*?://", "").replaceAll("\\?.*","").replaceAll("(:)", "_");
				String ni = getfilepath().getAbsolutePath() + "/compressed/"+filename.substring(0,filename.lastIndexOf('.')>0?filename.lastIndexOf('.'):filename.length()-1)+".jpg";
				file1 = new File(filename);
				file1c = new File(ni);
				if(file1.exists()){break;}Log.w(G,"File missing " + filename);
			}
			if(file1!=null && file1.exists()){
				jump.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://"+file1.getAbsolutePath()));
			}
			jump.setType("message/rfc822"); 
			startActivity(Intent.createChooser(jump, "Email"));

			//wayGo.sendEmptyMessage(2);
			
		}
	};
	

	Handler wayGo = new Handler(){
		public void handleMessage(Message msg){
			finish();
		}
	};

	
	@Override
	protected void onPause() {
//		mLog.w(TAG,"onPause() ++++++++++++++++++++++++++++++++");
		//easyLoadData("<html></html>");
		//mHideData.sendEmptyMessage(2);
		// TODO Auto-generated method stub
		super.onPause();
	}

	
    
}

