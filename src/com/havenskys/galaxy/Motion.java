package com.havenskys.galaxy;
// Don't Panic


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URI;
import java.net.UnknownHostException;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.List;

import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

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
import android.app.AlarmManager;
import android.app.ListActivity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnCancelListener;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SubMenu;
import android.view.TouchDelegate;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.OnHierarchyChangeListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AbsListView.RecyclerListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageView.ScaleType;

public class Motion extends ListActivity {
    
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		//Log.w(G,"Destroy");
		//finish();
		super.onDestroy();
	}





	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		//Log.w(G,"Stop");
		//finish();
		//wayGo.sendEmptyMessage(3);
		super.onStop();
	}


	/** Called when the activity is first created. */
    private static String G = "Motion";
    //private SharedPreferences mReg;
	//private Editor mEdt;
	private Context mCtx;
	int pRate = 72;
	
	int coloroff = Color.argb(205, 40, 40, 98);
	int coloron = Color.argb(205, 80, 80, 138);
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.motion);
        wHeight = getWindowManager().getDefaultDisplay().getHeight();
    	wWidth = getWindowManager().getDefaultDisplay().getWidth();
    	Log.w(G,"onCreate " + wWidth + "x" + wHeight);
    	
        mCtx = this;
        /*
        Intent tx = getIntent();if(tx != null){Bundle bx = tx.getExtras();
        if(bx != null){
        if(bx.getBoolean("code413", false)){wayGo.sendEmptyMessage(2);return;}
        }}//*/
     

        uniq = (int)SystemClock.uptimeMillis();
        
        loadapp.sendEmptyMessage(2);
	}
    
	
	
	RelativeLayout headz;
	LocationListener mLocationListener;LocationManager mLocator;
	Handler loadapp = new Handler(){
		@Override
		public void handleMessage(Message msg){
			RelativeLayout ba = (RelativeLayout) findViewById(R.id.base);
	        ba.setBackgroundColor(colorbase);
			Log.w(G,"la() loadlist()");
	        {Message ml = new Message(); Bundle bl = new Bundle(); ml.setData(bl); loadlist.sendMessage(ml);}
	        Log.w(G,"la() sensorservice()");
	        {SensorService.sendEmptyMessageDelayed(2,pRate);}
	        Log.w(G,"la() serviceread()");
	        {serviceRead.sendEmptyMessageDelayed(-2,10);}
	        //{Message ml = new Message(); Bundle bl = new Bundle(); bl.putString("title", August.title); bl.putString("dest", August.dest); bl.putString("storloc", "bucket"); ml.setData(bl); getlist.sendMessage(ml);}
	        Log.w(G,"la() servicestart()");
	        {serviceStart.sendEmptyMessageDelayed(2,pRate*4);}
	        Log.w(G,"la() notification()");
	        {handleNotification.sendEmptyMessageDelayed(2,pRate*2);}
		/*
			Thread tg = new Thread(){public void run(){
			// this is the "not spit your coffee" version
			int dest = 90;
			
			
			int[] ar = new int[dest];int gto = 2; int at = 2;int o1 = 2; int i =2;
			for(int e1 = 0; e1 < ar.length; e1++){ar[e1] = e1+1;}
			
			for(i = 0;i < ar.length; i++){int head = ar.length; int gr = 0;
			gto = dest-i;
			for(o1 = ar.length/2; o1>-1;){
			at = ar[o1];
			Log.w(G,"search " + at + " from " + o1);
			if(at == gto){Log.w(G,"\ngot " + at);break;}
			
			if(o1 == 0){Log.e(G,"\nsearch not found");break;}
			if(at > gto){ head = o1; if(o1 == 1){o1 = 0;continue;} o1 -= (head-gr)/2; }else{ gr = o1; o1+= (head-o1)/2; }
			
			
			}
			
			
			}
			}};tg.start();//*/
		
			
			mLocator = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		    mLocationListener = new LocationListener() {
		      public void onLocationChanged(Location location) {
		        if (location != null) {
		          Log.w(G,"GPS " + location.getAltitude()+","+
		          location.getLatitude()+","+
		          location.getLongitude()+","+
		          location.getTime()+","+
		          location.getAccuracy()+","+
		          location.getSpeed()+","+
		          location.getProvider()+"");
		        }
		      }

		      public void onProviderDisabled(String provider) {
		    	  Log.w(G,"GPS PROVIDER GONE " + provider);
		      }

		      public void onProviderEnabled(String provider) {
		    	  Log.w(G,"GPS PROVIDER HERE " + provider);
		    	  Criteria criteria = new Criteria();
				    criteria.setAccuracy(Criteria.ACCURACY_COARSE);  // Faster, no GPS fix.
				    //criteria.setAccuracy(Criteria.ACCURACY_FINE);  // More accurate, GPS fix.
				    // You can specify the time and distance between location updates.
				    // Both are useful for reducing power requirements.
				    mLocator.requestLocationUpdates(mLocator.getBestProvider(criteria, true),
				        60 * 1000, 1, mLocationListener,
				        getMainLooper());
		      }

		      public void onStatusChanged(String provider, int status, Bundle extras) {
		    	  Log.w(G,"GPS PROVIDER STATUS " + provider + " " + status);
		    	  
		      }
		    };
			
		    
		    
		    //You can also get the phone's last known location using the LocationManager. This is faster than setting up a LocationListener and waiting for a fix.
		    // Start with fine location.
		    Location l = mLocator.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		    if (l == null) {
		      // Fall back to coarse location.
		      l = mLocator.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		     if(l!=null){ 
		      Log.w(G,"Last known location " + ((System.currentTimeMillis()/1000) - l.getTime()) + " seconds ago" );
		    }
		    }
			
		}
	};
	
	Handler mS2 = new Handler(){
	
	public void handleMessage(Message msg){
		Bundle bdl = msg.getData();
		int id = -2;
		if(msg.what > 0){
			id = msg.what;
		}
		LinearLayout wrl = (LinearLayout) findViewById(id);
		int x = wrl.getScrollX();
		Log.i(G,"wrl("+x+","+wrl.getWidth()+")");
		
		if(x > 40){
			wrl.scrollTo(0,0);
			return;
		}//else if(x < wrl.getWidth()*-1){
			
		//}
		mS2.sendEmptyMessageDelayed(id,15);
		wrl.scrollBy(17, 0);
		
		
	}};
	
	Handler serviceRead = new Handler(){
		long pgo = 2; long gof = 2; long gos = 1; long goc = 1; long goa = 1; long god = 1;long workready = -3;
		public void handleMessage(Message msg){
	
			SharedPreferences mReg = getSharedPreferences("Preferences", MODE_WORLD_WRITEABLE);
            Editor mEdt = mReg.edit();
            long st = mReg.getLong("bucket_start",0);
            long go = (SystemClock.uptimeMillis() -  mReg.getLong("go", msg.getWhen()) );
    		int detailtext = msg.what>0?msg.what:headerText;
    		if(detailtext <= 0 && headerText > 0){detailtext = headerText;}//fix was here :)
    		if(detailtext <= 0){
				Log.e(G,"detailtext <= 0("+detailtext+") ("+headerText+")");
				serviceRead.sendEmptyMessageDelayed(-5,1500);return;
    		}
    		
    		//r(
			//  clicker
			//	t
			//:	r3(
			//		wrl(
			//			r3(i:t:i)
			//:			zr["_v"](zl2(zr2(i6:g6[responsems"_v3"]:g7[hostname"_v4"]:g8[ipaddress:ipport"_v6"]:g9:title["_v2"]:source:dest)))
			//:			r3(i:t:i)
			//)
			//:		pear8(i:t:i)
			//:		pear8(i:t:i)
			//:		i5[beatimg]
			//:		ex[HPAGE]()
			//:		g5[responsems]
			//))
    		
    		
    		
    		
    		//long gs = System.currentTimeMillis() - msg.getWhen();
    		if(go > gos){gos = go;}
        	if(go < gof){gof = go;}
        	goa += go;goc++;god = go - pgo;pgo = go;
        	
        	int workat = mReg.getInt("workat",-1);
            int workof = mReg.getInt("workof",-1);
            long fsize = -2;String ffn = mReg.getString("bucket"+workat,"");
			if(ffn.length() > 0 && !ffn.contains("\n")){File f = new File(ffn);fsize = f.length();}
			long tsize = mReg.getLong("bucket"+workat+"_length",-1);
			st = mReg.getLong("bucket"+workat+"_start",st);
			String desttitle = mReg.getString("bucket+"+workat+"_title",mReg.getString("August_title", August.title));
			TextView t = null; RelativeLayout r = null;
			{    
			t = (TextView) findViewById(headerText);
			//t.
            if(t == null){//Log.w(G,"header Text View exists");
			Log.w(G,"essential textview error("+headerText+")");
			serviceRead.sendEmptyMessageDelayed(detailtext,1500);
			return;
			}	
			//r.addView
			r = (RelativeLayout) t.getParent();
			if(r == null){
			Log.w(G,"essential layout error");
			serviceRead.sendEmptyMessageDelayed(detailtext,1500);
			return;
			}
			}
			//r(
			//  clicker
			//	t
			//:	r3(
			//		wrl(
			//			r3(i:t:i)
			//:			zr["_v"](zl2(zr2(i6:g6[responsems"_v3"]:g7[hostname"_v4"]:g8[ipaddress:ipport"_v6"]:g9[errortype]:title["_v2"]:source:dest)))
			//:			r3(i:t:i)
			//)
			//:		pear8(i:t:i)
			//:		pear8(i:t:i)
			//:		i5[beatimg]
			//:		ex[HPAGE]()
			//:		g5[responsems]
			//))
			
			
			
				//*/ //
				//Log.w(G,"header service view("+r.getChildCount()+")");
				if(r.getChildCount() == 2){
				
				// r(t:r3(wrl:pear8(i:t:i):pear8(i:t:i):i5[beatimg]:ex[HPAGE]():g5[responsems]))
				RelativeLayout r3 = new RelativeLayout(mCtx);
				r3.setId(uniq++);
				RelativeLayout.LayoutParams r3l = new RelativeLayout.LayoutParams(-1,-2);
				//r3l.setMargins(140,0,0,00);
				r3l.addRule(RelativeLayout.BELOW,t.getId());
				r3.setPadding(0,0,0,20);
				//r3.setMinimumHeight(120);
				r3.setOnTouchListener(new OnTouchListener(){public boolean onTouch(View v, MotionEvent m){Log.w(G,"r3 touched " + m.getAction());return true;}});
				r3.setLayoutParams(r3l);
				r.addView(r3);
				Log.i(G,"Creating service list");
				// wrl
				LinearLayout wrl = new LinearLayout(mCtx);
				wrl.setId(uniq++);
				RelativeLayout.LayoutParams wrll = new RelativeLayout.LayoutParams(-1,-2);
				wrll.setMargins(140,0,0,0);wrl.setPadding(0,0,5,0);
				//wrl.setPadding(140,90,0,40);
				wrl.setLayoutParams(wrll);
				wrl.setOrientation(LinearLayout.VERTICAL);
				r3.setVisibility(View.VISIBLE);
				r3.addView(wrl);
				wrl.scrollTo(wWidth*-1, 0);
				mS2.sendEmptyMessageDelayed(wrl.getId(),350);
				
				int apple2 = -2;int apple3 = -3;

				{// HBottom
					// pear8
					RelativeLayout pear8 = new RelativeLayout(mCtx);
					pear8.setId(uniq++);apple3 = pear8.getId();
					RelativeLayout.LayoutParams pear8l = new RelativeLayout.LayoutParams(130,-2);
					pear8l.setMargins(0,0,0,0);pear8l.addRule(RelativeLayout.ALIGN_BOTTOM, wrl.getId());
					pear8.setLayoutParams(pear8l);
					r3.addView(pear8);
					
					// wi2
					ImageView wi2 = new ImageView(mCtx);wi2.setId(uniq++);wi2.setScaleType(ScaleType.MATRIX);wi2.setImageResource(R.drawable.serverleftc);
					RelativeLayout.LayoutParams wil2 = new RelativeLayout.LayoutParams(-2,-2);
					//wil2.addRule(RelativeLayout.BELOW,g5.getId());
					wil2.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
					wi2.setLayoutParams(wil2);
					//if(oi != works.length-1){wi2.setVisibility(View.INVISIBLE);}
					pear8.addView(wi2,0);
				
					// wi2s
					TextView wi2s = new TextView(mCtx);wi2s.setBackgroundColor(Color.BLACK);
					RelativeLayout.LayoutParams wi2sl = new RelativeLayout.LayoutParams(-1,26);
					wi2sl.setMargins(0,0,25,0);
					wi2s.setLayoutParams(wi2sl);
					pear8.addView(wi2s,0);
					
					// wi3
					ImageView wi3 = new ImageView(mCtx);wi3.setId(uniq++);wi3.setScaleType(ScaleType.MATRIX);wi3.setImageResource(R.drawable.serverrightc);
					RelativeLayout.LayoutParams wil3 = new RelativeLayout.LayoutParams(-2,-2);
					wil3.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
					wi3.setLayoutParams(wil3);
					//if(oi != works.length-1){wi3.setVisibility(View.INVISIBLE);}
					pear8.addView(wi3,0);
				}// HBottom
				
				
				{// HTop

					// pear8
					RelativeLayout pear8 = new RelativeLayout(mCtx);
					pear8.setId(uniq++);apple2 = pear8.getId();
					RelativeLayout.LayoutParams pear8l = new RelativeLayout.LayoutParams(130,-2);
					pear8l.setMargins(0,0,0,0);pear8l.addRule(RelativeLayout.ALIGN_TOP, wrl.getId());
					pear8.setLayoutParams(pear8l);
					r3.addView(pear8);
					
					// wi2
					ImageView wi2 = new ImageView(mCtx);wi2.setId(uniq++);wi2.setScaleType(ScaleType.MATRIX);wi2.setImageResource(R.drawable.serverleftb);
					RelativeLayout.LayoutParams wil2 = new RelativeLayout.LayoutParams(-2,-2);
					//wil2.addRule(RelativeLayout.BELOW,g5.getId());
					wil2.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
					wi2.setLayoutParams(wil2);
					//if(oi != works.length-1){wi2.setVisibility(View.INVISIBLE);}
					pear8.addView(wi2,0);
				
					// wi2s
					TextView wi2s = new TextView(mCtx);wi2s.setBackgroundColor(Color.BLACK);
					RelativeLayout.LayoutParams wi2sl = new RelativeLayout.LayoutParams(-1,26);
					wi2sl.setMargins(0,0,25,0);
					wi2s.setLayoutParams(wi2sl);
					pear8.addView(wi2s,0);
				
					// wi3
					ImageView wi3 = new ImageView(mCtx);wi3.setId(uniq++);wi3.setScaleType(ScaleType.MATRIX);wi3.setImageResource(R.drawable.serverrightb);
					RelativeLayout.LayoutParams wil3 = new RelativeLayout.LayoutParams(-2,-2);
					wil3.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
					wi3.setLayoutParams(wil3);
					//if(oi != works.length-1){wi3.setVisibility(View.INVISIBLE);}
					pear8.addView(wi3,0);
				}// HTop
				
				
				{// HContainer
					// g5
					TextView g5 = new TextView(mCtx);//g5.setBackgroundColor(Color.BLACK);
					RelativeLayout.LayoutParams g5l = new RelativeLayout.LayoutParams(130,-2);
					
					g5l.addRule(RelativeLayout.ALIGN_BOTTOM,wrl.getId());
					g5l.setMargins(0,0,3,1);g5.setGravity(Gravity.RIGHT);
					g5.setLayoutParams(g5l);
					//
					Log.i(G,"sr() g5(++)");
					g5.setText("++");g5.setId(uniq++);
					g5.setTextColor(textcolor);g5.setTextSize((float)28.0);
					
					// i5
					ImageView i5 = new ImageView(mCtx);
					i5.setId(uniq++);
					i5.setScaleType(ScaleType.FIT_XY);i5.setImageResource(R.drawable.clear100);//beatimg
					//i5.setMinimumHeight(85);
					RelativeLayout.LayoutParams i5l = new RelativeLayout.LayoutParams(130,-2);
					//i5.setPadding(0,);
					i5l.setMargins(0,0,38,0);//beatimg
					i5l.addRule(RelativeLayout.ALIGN_BOTTOM,g5.getId());i5l.addRule(RelativeLayout.ALIGN_TOP,wrl.getId());//i5l.addRule(RelativeLayout.ALIGN_RIGHT,g5.getId());
					i5.setLayoutParams(i5l);
					r3.addView(i5);//,r3.getChildCount()-2);//conver-1
					
					// ex
					RelativeLayout ex = new RelativeLayout(mCtx);
					ex.setId(uniq++);
					RelativeLayout.LayoutParams exl = new RelativeLayout.LayoutParams(130,-2);
					exl.setMargins(0,1,0,1);exl.addRule(RelativeLayout.BELOW,apple2);exl.addRule(RelativeLayout.ABOVE,apple3);
					ex.setLayoutParams(exl);
					ex.setBackgroundColor(Color.argb(75,0,0,0));
					r3.addView(ex);//beatimg
					
					TextView ext3= new TextView(mCtx);
					ext3.setId(uniq++);
					ext3.setLayoutParams(new RelativeLayout.LayoutParams(-1,35));
					ext3.setText("");
					ex.addView(ext3);
					
					r3.addView(g5);
				}// Hontainer
				
				/*
				{
				ImageView wi = new ImageView(mCtx);wi.setId(uniq++);wi.setScaleType(ScaleType.FIT_XY);wi.setImageResource(R.drawable.servertop);
				RelativeLayout.LayoutParams wil = new RelativeLayout.LayoutParams(-1,5);
				//wil.setMargins(140,85,0,0);
				wil.addRule(RelativeLayout.ABOVE,wrl.getId());
				wi.setLayoutParams(wil);
				r3.addView(wi);
				}
				{
				ImageView wi2 = new ImageView(mCtx);wi2.setId(uniq++);wi2.setScaleType(ScaleType.FIT_XY);wi2.setImageResource(R.drawable.serverbottom);
				RelativeLayout.LayoutParams wil2 = new RelativeLayout.LayoutParams(-1,4);
				//wil2.setMargins(140, 0, 0, 36);
				wil2.addRule(RelativeLayout.BELOW,wrl.getId());
				wi2.setLayoutParams(wil2);
				r3.addView(wi2);
				}//*/
				
				}// create 
			
				
				
				
				
				
				
				
				
				
				
				
			RelativeLayout r3 = null;
			LinearLayout wrl = null;//zr(zl2(zr2(i6:g6[responsems]:g7[hostname]:g8[ipaddress:ipport]:title:source:dest)))
			//r(
			//  clicker
			//	t
			//:	r3(
			//		wrl(
			//			r3(i:t:i)
			//:			zr["_v"](zl2(zr2(i6:g6[responsems"_v3"]:g7[hostname"_v4"]:g8[ipaddress:ipport"_v6"]:g9[errortype]:title["_v2"]:source:dest)))
			//:			r3(i:t:i)
			//)
			//:		pear8(i:t:i)
			//:		pear8(i:t:i)
			//:		i5[beatimg]
			//:		ex[HPAGE]()
			//:		g5[responsems]
			//))
			//"_v" beatimg	
			//Log.i(G,"r("+r.getChildCount()+")508");//3
			try{r3 = (RelativeLayout) r.getChildAt(2);}catch(ClassCastException eec){r3 = null;}
			//RelativeLayout rm3 = (RelativeLayout) r3.getChildAt(2);
			ImageView i5 = (ImageView) r3.getChildAt(3);//headerGen
			TextView g5 = (TextView) r3.getChildAt(5);
			//
			if(go < 0 ){ Log.i(G,"sr() g5("+go+")"); }
			g5.setText("ms["+go+"]");
			//
			//Log.w(G,"sr() beatimg("+i5.getId()+","+go+")");
			{Bundle bl = new Bundle(); bl.putInt("id",i5.getId()); bl.putLong("ms", go); Message ml = new Message(); ml.setData(bl); beatimg.sendMessage(ml);}

			//Log.w(G,"sr() ready("+workready+","+(System.currentTimeMillis() - mReg.getLong("workready",0))+")");
			if(mReg.getLong("workready",2) > workready && mReg.getLong("workready",-1) != -1){//long rrx = (System.currentTimeMillis()-msg.getWhen());EXPIRED msg.getWhen() at this point
				Log.w(G,"sr("+(mReg.contains("workready"))+") 531 work changed");
				
				
			
			workready = System.currentTimeMillis();//mReg.getLong("workready",2);
			
			wrl = (LinearLayout) r3.getChildAt(0);
			
			String work = mReg.getString("work","");
			String[] works = work.split("\n");
			boolean[] workb = new boolean[works.length];
			int oi = -3;
			for(oi = 0; oi < works.length; oi++){workb[oi]=true;}
			

			String dest = "";String sititle = "";String source = "";String status = "";
					
				
			for(int x = 0; x < wrl.getChildCount(); x++){//zr(zl2(zr2(i6:g6[responsems]:g7[hostname]:g8[ipaddress:ipport]:title:source:dest)))
				//r(
				//  clicker
				//	t
				//:	r3(
				//		wrl(
				//			r3(i:t:i)
				//:			zr["_v"](zl2(zr2(i6:g6[responsems"_v3"]:g7[hostname"_v4"]:g8[ipaddress:ipport"_v6"]:title["_v2"]:source:dest)))
				//:			r3(i:t:i)
				//)
				//:		pear8(i:t:i)
				//:		pear8(i:t:i)
				//:		i5[beatimg]
				//:		ex[HPAGE]()
				//:		g5[responsems]
				//))
				RelativeLayout zr = (RelativeLayout) wrl.getChildAt(x);
				//Log.i(G, "zrchild ["+x+"]zr "+zr.getChildCount());
				if(zr.getChildCount() > 1){continue;}//Log.e(G," butter ribs error "); 0=4 [1-(n-1)]=1 n=3
				LinearLayout zl2 = (LinearLayout) zr.getChildAt(0);
				RelativeLayout zr2 = (RelativeLayout) zl2.getChildAt(0);
				//RelativeLayout zr4 = (RelativeLayout) zr2.getChildAt(4);
				String sndest = "";// WARNING if zrdest is incorrect it will cause rapid duplication, muahahahah
				TextView zrdest = (TextView) zr2.getChildAt(7);sndest = zrdest.getText().toString();
				//TextView sss = (TextView) zzx.getChildAt(5);
				//if(sss.getText().toString().contentEquals(source)){found = true;}
				//for(int oi = 0; oi < workof; oi++){
				for(oi = 0; oi < works.length; oi++){
					String[] ww = Uri.decode(works[oi]).split("\n");
					dest = ww[1];sititle = ww[3];source = ww[0];status = ww[2];
					//Log.i(G, "("+dest+")==("+sndest+")");
					if(dest.contentEquals(sndest) ){
					if(mReg.getInt("bucket"+oi+"_v",-3) != zr.getId() ){Log.e(G,"Disabled Updating workindex("+(oi+1)+") from("+mReg.getInt("bucket"+oi+"_v",-3)+") to("+zr.getId()+")");
					
					
					//mEdt.putInt("bucket"+oi+"_v",zr.getId());mEdt.commit();
					}
					
					//Log.i(G,"yes known");
					workb[oi] = false;
					//found = false;
					break;
					}
				}
				
			}
			
			
				
				//int oi = -3;
				for(oi = 0; oi < works.length; oi++){	
					if(!workb[oi]){
						//Log.w(G,"sr() Already create work order ("+works[oi]+")");
						continue;
					}
					Log.i(G,"sr() Creating work order("+oi+","+works[oi]+")");
					
					String[] ww = Uri.decode(works[oi]).split("\n");
					dest = ww[1];sititle = ww[3];source = ww[0];status = ww[2];
					
					
					//if(mReg.getLong("bucket"+oi+"_start", 0) < mReg.getLong("bucket"+oi+"_vwhen", 0) ){
						//continue; //already created _vwhen(long) and _v(int)
					//}
					/*
					try {
					int ix = mReg.getInt("bucket"+oi+"_v",-3);//zr(zl2(zr2(i6:g6[responsems]:g7[hostname]:g8[ipaddress:ipport]:title:source:dest)))
					RelativeLayout rx = (RelativeLayout) findViewById(ix);
					if(rx != null){
						LinearLayout zl2 = (LinearLayout) rx.getChildAt(0);
						RelativeLayout zr2 = (RelativeLayout) zl2.getChildAt(0);
						//RelativeLayout zr4 = (RelativeLayout) zr2.getChildAt(4);
						TextView sndest = (TextView) zr2.getChildAt(4);
						Log.i(G, "("+dest+")==("+sndest+")");
						if(dest.contentEquals(sndest)){
							Log.i(G, "yes known");
							
							continue;
						}else{
							//New
						}
					}//*/
					
					
					
					//}catch(ClassCastException e){e.printStackTrace();Log.e(G,"############  FIXXXX");
					//}
					
					/*
					boolean found = false;
				{	
					
				}
				if(found){continue;}
				//*/
				
				
				
				{// Top wrl's zr3 Relativelayout image<-text->image
					
					RelativeLayout zr3 = new RelativeLayout(mCtx);
					zr3.setId(uniq++);
					LinearLayout.LayoutParams zrl3 = new LinearLayout.LayoutParams(-1,-2);
					if(oi != 0 && oi != works.length-1){
					zrl3.setMargins(0,2,0,0);
					
					}else if(oi==works.length-1 && oi>0){zrl3.setMargins(0,2,0,0);}
					zr3.setLayoutParams(zrl3);
					wrl.addView(zr3);
				
					
				ImageView wi2 = new ImageView(mCtx);wi2.setId(uniq++);wi2.setScaleType(ScaleType.MATRIX);wi2.setImageResource(R.drawable.serverleftb);
				RelativeLayout.LayoutParams wil2 = new RelativeLayout.LayoutParams(-2,-2);
				wil2.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
				wi2.setLayoutParams(wil2);
				//if(oi > 0){wi2.setVisibility(View.INVISIBLE);}
				zr3.addView(wi2);
				
				TextView wi2s = new TextView(mCtx);wi2s.setBackgroundColor(Color.BLACK);
				RelativeLayout.LayoutParams wi2sl = new RelativeLayout.LayoutParams(-1,26);
				wi2sl.setMargins(0,0,25,0);
				wi2s.setLayoutParams(wi2sl);
				zr3.addView(wi2s);

				ImageView wi4 = new ImageView(mCtx);wi4.setId(uniq++);wi4.setScaleType(ScaleType.MATRIX);wi4.setImageResource(R.drawable.serverrightb);
				RelativeLayout.LayoutParams wil4 = new RelativeLayout.LayoutParams(-2,-2);
				wil4.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
				//if(oi > 0){wi4.setVisibility(View.INVISIBLE);}
				wi4.setLayoutParams(wil4);
				zr3.addView(wi4);

				

				ImageView wi3 = new ImageView(mCtx);wi3.setId(uniq++);wi3.setScaleType(ScaleType.MATRIX);wi3.setImageResource(R.drawable.serverbar);
				RelativeLayout.LayoutParams wil3 = new RelativeLayout.LayoutParams(-2,-2);
				wil3.addRule(RelativeLayout.CENTER_IN_PARENT);
				wi3.setLayoutParams(wil3);
				zr3.addView(wi3);
				
				
			
				
				}//Top
				
				
				
				int sid = 2;
				int tid = 2;
				//r(
				//  clicker
				//	t
				//:	r3(
				//		wrl(
				//			r3(i:t:i)
				//:			zr["_v"](zl2(zr2(i6:g6[responsems"_v3"]:g7[hostname"_v4"]:g8[ipaddress:ipport"_v6"]:title["_v2"]:source:dest)))
				//:			r3(i:t:i)
				//)
				//:		pear8(i:t:i)
				//:		pear8(i:t:i)
				//:		i5[beatimg]
				//:		ex[HPAGE]()
				//:		g5[responsems]
				//))
				RelativeLayout zr = null;

				
				zr = new RelativeLayout(mCtx);
				zr.setId(uniq++);
				LinearLayout.LayoutParams zrl = new LinearLayout.LayoutParams(-1,-2);
				zrl.setMargins(0,0,0,0);
				zr.setLayoutParams(zrl);
				wrl.addView(zr);
				mEdt.putInt("bucket"+oi+"_v",zr.getId());
				mEdt.commit();
				//}			
				
				
				
				
				
				{// bottom Per Item
				
					RelativeLayout zr3 = new RelativeLayout(mCtx);
					zr3.setId(uniq++);
					LinearLayout.LayoutParams zrl3 = new LinearLayout.LayoutParams(-1,-2);
					zrl3.setMargins(0,0,0,0);//beatimg
					zr3.setLayoutParams(zrl3);
					wrl.addView(zr3);
					
					
					
					{
					ImageView wi2 = new ImageView(mCtx);wi2.setId(uniq++);wi2.setScaleType(ScaleType.MATRIX);wi2.setImageResource(R.drawable.serverleftc);
					RelativeLayout.LayoutParams wil2 = new RelativeLayout.LayoutParams(-2,-2);
					wil2.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
					wi2.setLayoutParams(wil2);
					//if(oi != works.length-1){wi2.setVisibility(View.INVISIBLE);}
					zr3.addView(wi2,0);
					}
				
					{
						ImageView wi2 = new ImageView(mCtx);wi2.setId(uniq++);wi2.setScaleType(ScaleType.MATRIX);wi2.setImageResource(R.drawable.serverrightc);
						RelativeLayout.LayoutParams wil2 = new RelativeLayout.LayoutParams(-2,-2);
						wil2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
						wi2.setLayoutParams(wil2);
						//if(oi != works.length-1){wi2.setVisibility(View.INVISIBLE);}
						zr3.addView(wi2,0);
					}
					{
						TextView wi2s = new TextView(mCtx);wi2s.setBackgroundColor(Color.BLACK);
						RelativeLayout.LayoutParams wi2sl = new RelativeLayout.LayoutParams(-1,26);
						wi2sl.setMargins(25,0,25,0);//corner
						wi2s.setLayoutParams(wi2sl);
						zr3.addView(wi2s);
					}
					
				}// bottom Per Item
					
				
				
				{// Where Per site created
						
						// LinearLayout for iplist online has one entr
						LinearLayout zl2 = new LinearLayout(mCtx);
						zl2.setId(uniq++);
						RelativeLayout.LayoutParams zl2l = new RelativeLayout.LayoutParams(-1,-2);
						zl2.setBackgroundColor(Color.argb(105,0,0,0));
						zl2.setOrientation(LinearLayout.VERTICAL);
						zl2l.setMargins(0,1,0,1);//zl2l.addRule(RelativeLayout.BELOW,tid);
						zl2.setLayoutParams(zl2l);
						zr.addView(zl2);
						//mEdt.putInt("bucket"+oi+"_v5",zl2.getId());
						//mEdt.commit();
						//*/
						
					
					
					
						// zl2's RelativeLayout zr2<-   zr(zl2(zr2(i6:g6[responsems]:g7[hostname]:g8[ipaddress:ipport]:title:source:dest)))
						//r(
						//  clicker
						//	t
						//:	r3(
						//		wrl(
						//			r3(i:t:i)
						//:			zr["_v"](zl2(zr2(i6:g6[responsems"_v3"]:g7[hostname"_v4"]:g8[ipaddress:ipport"_v6"]:title["_v2"]:source:dest)))
						//:			r3(i:t:i)
						//)
						//:		pear8(i:t:i)
						//:		pear8(i:t:i)
						//:		i5[beatimg]
						//:		ex[HPAGE]()
						//:		g5[responsems]
						//))
						
						RelativeLayout zr2 = new RelativeLayout(mCtx);
						zr2.setId(uniq++);
						LinearLayout.LayoutParams zr2l = new LinearLayout.LayoutParams(-1,-2);
						if(oi !=0 && oi != works.length-1){
						zr2l.setMargins(0, 1, 0, 0);
						} else if(oi == works.length-1){zr2l.setMargins(0,1,0,1);}
						zr2.setBackgroundColor(Color.argb(105,20,60,80));
						zr2.setLayoutParams(zr2l);//zr2.setMinimumHeight(120);
						
						zl2.addView(zr2);//"_v"
						zr2.setFocusable(true);zr2.setClickable(true);//headerGen
						//zr2.setOnClickListener(new OnClickListener(){public void onClick(View v){Bundle bl = new Bundle(); bl.putString("type","ipaddress");Message ml = new Message(); ml.setData(bl);}});
						zr2.setOnFocusChangeListener(new OnFocusChangeListener(){public void onFocusChange(View v, boolean h){RelativeLayout zr2 = (RelativeLayout) v; if(h){}else{zr2.setBackgroundColor(Color.argb(100,80,80,40));}}});
						zr2.setOnTouchListener(new OnTouchListener(){public boolean onTouch(View v, MotionEvent m2){Log.i(G,"touch event action("+m2.getAction()+") ");RelativeLayout zr2 = (RelativeLayout) v; if(m2.getAction() == m2.ACTION_DOWN){zr2.setBackgroundColor(Color.argb(105,20,80,60));}else if(m2.getAction() == m2.ACTION_UP){zr2.setBackgroundColor(Color.argb(105,0,80,40));}  return false;}});
						
						
						
						
						{// zr2's beatimg responsems hostname ipaddress:ipport
						//zr(zl2(zr2(i6:g6[responsems]:g7[hostname]:g8[ipaddress:ipport]:title:source:dest)))
							//r(
							//  clicker
							//	t
							//:	r3(
							//		wrl(
							//			r3(i:t:i)
							//:			zr["_v"](zl2(zr2(i6:g6[responsems"_v3"]:g7[hostname"_v4"]:g8[ipaddress:ipport"_v6"]:g9:title["_v2"]:source:dest)))
							//:			r3(i:t:i)
							//)
							//:		pear8(i:t:i)
							//:		pear8(i:t:i)
							//:		i5[beatimg]
							//:		ex[HPAGE]()
							//:		g5[responsems]
							//))
						// ImageView
						ImageView i6 = new ImageView(mCtx);
						i6.setId(uniq++);
						i6.setScaleType(ScaleType.FIT_XY);i6.setImageResource(R.drawable.clear100);//beatimg
						RelativeLayout.LayoutParams i6l = new RelativeLayout.LayoutParams(-1,-1);//i6l.addRule(RelativeLayout.RIGHT_OF,g6.getId());
						i6l.setMargins(3,0,3,0);//beatimg
						i6l.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM,-1);//i6l.addRule(RelativeLayout.ALIGN_TOP,g6.getId());
						i6.setLayoutParams(i6l);
						zr2.addView(i6);
						
						// TextView
						TextView g6 = new TextView(mCtx);//g6.setBackgroundColor(Color.BLACK);
						RelativeLayout.LayoutParams g6l = new RelativeLayout.LayoutParams(-1,-2);
						//g6l.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM,-1);
						//g6l
						g6l.setMargins(1,0,10,0);g6l.addRule(RelativeLayout.ALIGN_BOTTOM,i6.getId());g6.setGravity(Gravity.RIGHT);
						g6.setPadding(5,65,5,0);
						g6.setLayoutParams(g6l);
						String g6t = ""+mReg.getLong("bucket"+oi+"_respondedms",-2);
						//go
						Log.i(G,"sr() g6("+g6t+")");
						g6.setText(g6t);g6.setId(uniq++);
						g6.setTextColor(textcolor);g6.setTextSize((float)28.0);
						zr2.addView(g6);
						//mEdt.putInt("bucket"+oi+"_v3",g6.getId());
						
						// TextView 
						TextView g7 = new TextView(mCtx);
						//g7.setBackgroundColor(colorm3);
						RelativeLayout.LayoutParams g7l = new RelativeLayout.LayoutParams(-1,-2);
						//g7l.addRule(RelativeLayout.RIGHT_OF,g6.getId());
						g7l.addRule(RelativeLayout.ALIGN_BOTTOM,i6.getId());
						g7l.setMargins(15,-2,16,0);g7.setLayoutParams(g7l);g7.setLines(1);g7.setId(uniq++);
						String g7t = mReg.getString("bucket"+oi+"_hostname","no hostname butter error");
						//
						Log.i(G,"sr() g7("+g7t+")");
						g7.setText(g7t);
						g7.setTextColor(textcolor);g7.setTextSize((float)14.0);
						zr2.addView(g7);
						//mEdt.putInt("bucket"+oi+"_v4",g7.getId());
						
						// TextView
						TextView g8 = new TextView(mCtx);
						//g7.setBackgroundColor(colorm3);
						RelativeLayout.LayoutParams g8l = new RelativeLayout.LayoutParams(-1,-2);
						//g7l.addRule(RelativeLayout.RIGHT_OF,g6.getId());
						g8l.addRule(RelativeLayout.ABOVE,g7.getId());
						g8l.setMargins(15,-2,16,0);g8.setLayoutParams(g8l);g8.setLines(1);g8.setId(uniq++);
						String g8t = mReg.getString("bucket"+oi+"_ipaddress","ipaddr");
						//
						Log.i(G,"sr() g8("+g8t+")");
						g8.setText(g8t);
						g8.setTextColor(textcolor);g8.setTextSize((float)12.0);
						zr2.addView(g8);
						//mEdt.putInt("bucket"+oi+"_v6",g8.getId());
						mEdt.commit();
						
						// TextView
						TextView g9 = new TextView(mCtx);
						RelativeLayout.LayoutParams g9l = new RelativeLayout.LayoutParams(-1,-2);
						g9l.addRule(RelativeLayout.ABOVE,g8.getId());
						g9l.setMargins(15,-2,16,0);g9.setLayoutParams(g9l);g9.setLines(1);g9.setId(uniq++);
						g9.setTextColor(textcolor);g9.setTextSize((float)10.0);
						zr2.addView(g9);
						// zr2's beatimg responsems hostname ipaddress:ipport titletext source dest
						
						{
							TextView sn = new TextView(mCtx);
							sn.setId(uniq++);tid = sn.getId();mEdt.putInt("bucket"+oi+"_v2",sn.getId());
							RelativeLayout.LayoutParams snl = new RelativeLayout.LayoutParams(-1,-2);
							snl.setMargins(10,0,10,0);
							snl.addRule(RelativeLayout.ALIGN_PARENT_TOP);
							sn.setLayoutParams(snl);
							//sn.setPadding(0,0,0,60);
							//
							Log.i(G, "sr() zr2[5]("+sititle+")");
							sn.setText(sititle);//sn.setMinimumHeight(30);
							//sn.setPadding(15,0,15,0);
							//sn.setLines(1);
							sn.setTextColor(textcolor);sn.setTextSize((float)18.0);
							
							zr2.addView(sn);
							}
							
							{
							TextView sn = new TextView(mCtx);
							sn.setId(uniq++);sid = sn.getId();
							RelativeLayout.LayoutParams snl = new RelativeLayout.LayoutParams(-1,-2);
							
							snl.addRule(RelativeLayout.BELOW,tid);
							snl.setMargins(10,0,10,0);
							sn.setTextSize((float)12.0);
							sn.setLayoutParams(snl);
							//sn.setVisibility(View.GONE);
							sn.setLines(1);
							//
							Log.i(G, "sr() zr2[5]("+source+")");
							sn.setText(source);
							sn.setTextColor(textcolor);//sn.setTextSize((float)16.0);
							zr2.addView(sn);
							}
							{
							TextView sn = new TextView(mCtx);
							sn.setId(uniq++);
							RelativeLayout.LayoutParams snl = new RelativeLayout.LayoutParams(-1,-2);
							snl.addRule(RelativeLayout.BELOW,sid);//snl.addRule(RelativeLayout.BELOW,sid);
							snl.setMargins(10,0,10,0);
							sn.setTextSize((float)12.0);
							sn.setLayoutParams(snl);
							//sn.setVisibility(View.GONE);
							sn.setLines(1);
							//
							Log.i(G, "sr() zr2[6]("+dest+")");
							sn.setText(dest);
							sn.setTextColor(textcolor);sn.setTextSize((float)16.0);
							zr2.addView(sn);
							}
						
						
							//zr(zl2(zr2(i6:g6[responsems]:g7[hostname]:g8[ipaddress:ipport]:title:source:dest)))
						
							//Updated data content "_v" zr(zl2(zr2(i6:g6[responsems]:g7[hostname]:g8[ipaddress:ipport]:title:source:dest)))
							
						/*/ //
								RelativeLayout zr4 = new RelativeLayout(mCtx);
								RelativeLayout.LayoutParams z4l = new RelativeLayout.LayoutParams(-1,-2);
								z4l.setMargins(0,0,0,0);
								z4l.addRule(RelativeLayout.ABOVE,g6.getId());z4l.addRule(RelativeLayout.ALIGN_PARENT_TOP);
								zr4.setLayoutParams(z4l);
								zr2.addView(zr4);
								//zr4.setMinimumHeight(120);	
								zr4.setClickable(true);zr4.setFocusable(true);
								//{Bundle bl = new Bundle(); bl.setInt("id",zr4.getId()); Message ml = new Message(); ml.setData(bl); filterText.sendMessage(ml);}
								//zr4.setOnClickListener();
								zr4.setOnFocusChangeListener(new OnFocusChangeListener(){public void onFocusChange(View v, boolean h){RelativeLayout zr2 = (RelativeLayout) v; if(h){}else{zr2.setBackgroundColor(Color.argb(100,80,80,40));}}});
								zr4.setOnTouchListener(new OnTouchListener(){public boolean onTouch(View v, MotionEvent m2){Log.i(G,"touch event action("+m2.getAction()+") ");RelativeLayout zr2 = (RelativeLayout) v; if(m2.getAction() == m2.ACTION_DOWN){zr2.setBackgroundColor(Color.argb(105,20,80,60));}else if(m2.getAction() == m2.ACTION_UP){zr2.setBackgroundColor(Color.argb(105,0,80,40));}  return false;}});	
								zr4.setBackgroundColor(Color.argb(55, 0, 0, 0));
								zr4.setPadding(15,1,15,1);
							//*/ //	
								
						
								mEdt.putLong("bucket"+oi+"_vwhen",System.currentTimeMillis());
								mEdt.commit();
								Log.w(G, "sr() speedExam("+oi+","+i6.getId()+")");
								{Bundle bl = new Bundle();bl.putInt("workindex",oi);bl.putInt("beatid",i6.getId());Message ml = new Message(); ml.setData(bl); speedExam.sendMessageDelayed(ml,750);}
										
								
						}
						
						

						
						
						//mReg.getInt("bucket"+workat_v3,-1); speed testing "_v"

					
						
						
						
						
					
					
					}
					
					
				
					
				
				
				
				
				
				
				
				
				
				
				}// for ; oi
			
				serviceRead.sendEmptyMessageDelayed(detailtext,750);
				return;
			
			
		}//work
				
				
				
				//r(
				//  clicker
				//	t
				//:	r3(
				//		wrl(
				//			r3(i:t:i)
				//:			zr["_v"](zl2(zr2(i6:g6[responsems"_v3"]:g7[hostname"_v4"]:g8[ipaddress:ipport"_v6"]:title["_v2"]:source:dest)))
				//:			r3(i:t:i)
				//)
				//:		pear8(i:t:i)
				//:		pear8(i:t:i)
				//:		i5[beatimg]
				//:		ex[HPAGE]()
				//:		g5[responsems]
				//))
				
				
				
			
			//}
			
			
			
			
			//}
        	//Log.w(G,"service read performance("+headerText+") " + go + " "+mReg.getLong("go", 0) + " " + msg.getWhen() + " " + SystemClock.uptimeMillis() + " ms past schedule "+goa+"[" + goc + "] " + god);
        	
        	
        	//RelativeLayout hl = (RelativeLayout) findViewById(getListView().get);
			//hl.setBackgroundColor(Color.MAGENTA);
            //TextView ht = (TextView) hl.getChildAt(1);headerText = ht.getId();
	
		{
			if( st > 0 && st < mReg.getLong("bucket_error", 0) ){
				mEdt.putLong("bucket_start",0);mEdt.commit();
            	//
				Log.e(G,"sr("+headerText+") error("+mReg.getString("errortype", "")+")");
				Log.i(G,"sr("+headerText+") error("+mReg.getString("errortype", "")+")");
				{Message ml = new Message(); Bundle bl = new Bundle(); bl.putInt("id", headerText); bl.putString("text", mReg.getString("errortype", "")); ml.setData(bl); setText.sendMessage(ml);}
            	{Bundle bl = new Bundle(); bl.putInt("id", headerText); bl.putInt("color",colorm2);Message ml = new Message(); ml.setData(bl);tvColorBg.sendMessageDelayed(ml,1000);}
            	Log.w(G,"sr() serviceRead("+detailtext+")");
            	serviceRead.sendEmptyMessageDelayed(detailtext,750);
            	return;
            }
            //if(go > -1){
            	
            //if(){
        	//{Bundle bl = new Bundle(); bl.putInt("id", headz.getId()); bl.putInt("color",Color.argb(205,120,100,80+(int)(god < 120?god:140)));Message ml = new Message(); ml.setData(bl);rlColorBg.sendMessageDelayed(ml,100);}
        		
            //}
            
			String ttl = "("+(workat+1)+"/"+(workof)+") "+mReg.getString("bucket_title","");
			if(  fsize > -2 && mReg.getLong("bucket"+workat+"_saved", 0) < mReg.getLong("bucket"+workat+"_start", 0) && mReg.getLong("bucket"+workat+"_responded", 0) > mReg.getLong("bucket"+workat+"_start", 0) ){
			//if(fsize > -2 && mReg.getLong("bucket"+workat+"_saved", 0) < mReg.getLong("bucket"+workat+"_start", 0)){
			
				ttl += "\n("+(fsize)+"/"+tsize+" "+((int)((float)fsize*100/(float)tsize*100)/100)+"%)";
			
			}
			
			
			if(mReg.getLong("bucket"+workat+"_start", 0) < mReg.getLong("bucket"+workat+"_vwhen", 0) ){

				detailtext =  mReg.getInt("bucket"+workat+"_v2", headerText);
				//Log.i(G,"############### Using console button ################ " + workat + " ("+detailtext+")("+headerText+")");
				
				
			}
			long nx6 = System.currentTimeMillis() - st;
			if(nx6 > 600000){
				Log.e(G,"Started("+nx6+"ms)");
				serviceinit.sendEmptyMessageDelayed(2,300);
				mEdt.putLong("bucket"+workat+"_start",0);mEdt.commit();
				serviceRead.sendEmptyMessageDelayed(detailtext,3750);
				return;
			}
			if(st == 0 || st < mReg.getLong("workready", 0) ){
				Log.w(G,"sr() starting");
				{Message ml = new Message(); Bundle bl = new Bundle(); bl.putInt("id", detailtext); bl.putInt("id2",headerText); bl.putString("text", "Starting"); ml.setData(bl); setText.sendMessageDelayed(ml,pRate);}
				serviceRead.sendEmptyMessageDelayed(detailtext,750);
				return;
			//}else if( mReg.getLong("bucket"+workat+"_connect", 0) < st ){
			//	Log.w(G,"sr() connecting");
			//	{Message ml = new Message(); Bundle bl = new Bundle(); bl.putInt("id", detailtext); bl.putInt("id2",headerText); bl.putString("text", ttl+"\nConnecting " + ((System.currentTimeMillis() - st)/1000) + " "); ml.setData(bl); setText.sendMessageDelayed(ml,pRate);}
			//	serviceRead.sendEmptyMessageDelayed(detailtext,750);
			//	return;
			}else if( mReg.getLong("bucket"+workat+"_connected", 0) < st ){
				Log.w(G,"sr() connection");
				{Message ml = new Message(); Bundle bl = new Bundle(); bl.putInt("id", detailtext); bl.putInt("id2",headerText); bl.putString("text", ttl+"\nConnection " + ((System.currentTimeMillis() - st)/1000) + " "); ml.setData(bl); setText.sendMessageDelayed(ml,pRate);}
				serviceRead.sendEmptyMessageDelayed(detailtext,750);
				return;
			}else if( mReg.getLong("bucket"+workat+"_responded", 0) < mReg.getLong("bucket"+workat+"_done", st) ){
				//Log.w(G,"sr() processing");//mReg.getLong("bucket"+workat+"_responded", 0) < mReg.getLong("bucket"+workat+"_done", st)
				long nx5 = System.currentTimeMillis() - mReg.getLong("bucket"+workat+"_responded", st);
				if(nx5 > 180000){
					Log.e(G,"processing("+nx5+"ms)");
					serviceinit.sendEmptyMessageDelayed(2,300);
					mEdt.putLong("bucket"+workat+"_start",0);mEdt.commit();
					serviceRead.sendEmptyMessageDelayed(detailtext,3750);
					return;
				}
				{Message ml = new Message(); Bundle bl = new Bundle(); bl.putInt("id", detailtext); bl.putInt("id2",headerText); bl.putString("text", ttl+"\nServer processing request " + (nx5/1000) + ""); ml.setData(bl); setText.sendMessageDelayed(ml,pRate);}
				serviceRead.sendEmptyMessageDelayed(detailtext,750);
				return;
			
			}else if( mReg.getLong("bucket"+workat+"_saved", 0) < mReg.getLong("bucket"+workat+"_done", st) ){
				Log.w(G,"sr() downloading");
				//bx8 = bl2+b7.indexOf(isplit,isplit.length());bl2 = bx8; break;} }}
				//fsize
				long rinx = (System.currentTimeMillis() - mReg.getLong("bucket"+workat+"_responded", st));
				if(mReg.getLong("bucket"+workat+"_dat",-2) != fsize){//fsize == atfilesize
					mEdt.putLong("bucket"+workat+"_datd",System.currentTimeMillis());mEdt.putLong("bucket"+workat+"_dat",fsize);mEdt.commit();
					//beatimg pageimg
				}else{
					
				}
				long rindx = (System.currentTimeMillis() - mReg.getLong("bucket"+workat+"_datd", 0));
				if(rinx > 30000 && rindx > 30000){
					Log.e(G,"downloading("+rinx+","+rindx+") file("+fsize+"/"+tsize+")");
					serviceinit.sendEmptyMessageDelayed(2,300);
					mEdt.putLong("bucket"+workat+"_start",0);mEdt.commit();
					serviceRead.sendEmptyMessageDelayed(detailtext,3750);
					return;
				}
				
				{Message ml = new Message(); Bundle bl = new Bundle(); bl.putInt("id", detailtext); bl.putInt("id2",headerText); bl.putString("text", ttl+"\nDownloading " + (rinx/1000) + " "); ml.setData(bl); setText.sendMessageDelayed(ml,pRate);}
				serviceRead.sendEmptyMessageDelayed(detailtext,3750);
				return;
			}else if( mReg.getLong("bucket"+workat+"_saved", 0) > mReg.getLong("bucket"+workat+"_working", 0) && mReg.getLong("bucket"+workat+"_saved", 0) > mReg.getLong("bucket"+workat+"_start", 0) ){
				Log.w(G,"sr() shuffling");
				long nx5 = (System.currentTimeMillis() - mReg.getLong("bucket"+workat+"_saved", 0));
				if(nx5 > 30000){
					Log.e(G,"shuffling("+nx5+"ms)");
					serviceinit.sendEmptyMessageDelayed(2,300);
					mEdt.putLong("bucket"+workat+"_start",0);mEdt.commit();
					serviceRead.sendEmptyMessageDelayed(detailtext,3750);
				}else{
					serviceRead.sendEmptyMessageDelayed(detailtext,750);
					{Message ml = new Message(); Bundle bl = new Bundle();bl.putLong("go",SystemClock.uptimeMillis()+pRate); bl.putInt("id", detailtext); bl.putInt("id2",headerText); bl.putString("text", ttl+"\nShuffling " + ((System.currentTimeMillis() - mReg.getLong("bucket"+workat+"_saved", 0))/1000) + " "); ml.setData(bl); setText.sendMessageDelayed(ml,pRate);}
				}
				return;
			}else if( mReg.getLong("bucket"+workat+"_working", 0) > mReg.getLong("bucket"+workat+"_done", 0) ) {
				Log.w(G,"sr() working");
				long nx5 = mReg.getLong("bucket"+workat+"_working", 0) - mReg.getLong("bucket"+workat+"_start", 0);
				if(nx5 > 30000){
					Log.e(G,"working("+nx5+"ms)");
					serviceinit.sendEmptyMessageDelayed(2,30);
					mEdt.putLong("bucket"+workat+"_start",0);mEdt.commit();
				}else{
				{Message ml = new Message(); Bundle bl = new Bundle();bl.putLong("go",SystemClock.uptimeMillis()+pRate); bl.putInt("id", detailtext); bl.putInt("id2",headerText); bl.putString("text", ttl+"\nWorking " + ((System.currentTimeMillis() - mReg.getLong("bucket"+workat+"_saved", 0))/1000) + " "); ml.setData(bl); setText.sendMessageDelayed(ml,pRate);}
				}
				serviceRead.sendEmptyMessageDelayed(detailtext,750);
				return;
			}
	
			
			long foundnew = mReg.getLong("bucket"+workat+"_new", 0);
			long size = mReg.getLong("bucket"+workat+"_size", 0);
	
			Log.i(G, "sr() service workat("+workat+") " + foundnew + " " + size + " " + desttitle);
			
			if(size == 1){	
				String ct = mReg.getString("bucket"+workat+"", "");
				String sh = ct.replaceAll("\n","").replaceAll("\r","").replaceAll("<script.*?</script>",""); 
				sh = sh.replaceAll("&nbsp;"," ").replaceAll("<(br|tr|th|input|table)>","\n").replaceAll("<.*?>","");
				sh = sh.replaceAll("\n\n+","\n");sh = new String("\n\n"+sh).replaceFirst("\n+","");
				easyStatus(sh);
				Date d = new Date(mReg.getLong("bucket"+workat+"_saved", 0));
				{Message ml = new Message(); Bundle bl = new Bundle(); bl.putInt("id", footerText); bl.putString("text", "\n"+mReg.getString("sourcetitle",desttitle)+"\nDownloaded " + (ct.length()>1024?ct.length()/1024+" Kb":ct.length()+"b")+" at " + (d.getHours() > 12?d.getHours()-12:d.getHours()) + ":" + (d.getMinutes()<10?"0":"") + d.getMinutes() + "\n\n"+sh+"\n"); ml.setData(bl); setText.sendMessage(ml);}
				{Message ml = new Message(); Bundle bl = new Bundle(); bl.putInt("id", detailtext); bl.putInt("id2",headerText); bl.putString("text", mReg.getString("sourcetitle",desttitle)+"\nDownloaded " + (ct.length()>1024?ct.length()/1024+" Kb":ct.length()+"b")+" at " + (d.getHours() > 12?d.getHours()-12:d.getHours()) + ":" + (d.getMinutes()<10?"0":"") + d.getMinutes() ); ml.setData(bl); setText.sendMessage(ml);}
				Log.w(G,"sr() 1102("+detailtext+")");
				serviceRead.sendEmptyMessageDelayed(detailtext,3750);
				return;
			}
			if(size > 1){
				String ct = mReg.getString("bucket"+workat+"", "");
				Date d = new Date(mReg.getLong("bucket"+workat+"_saved", 0));Date d3 = new Date(); long si = d3.getTime() - d.getTime();
				{Message ml = new Message(); Bundle bl = new Bundle(); bl.putInt("id", detailtext); bl.putInt("id2",headerText); bl.putString("text", mReg.getString("sourcetitle",desttitle)+"\nDownloaded "+ foundnew +" in " + (size>1024?size/1024+" Kb":size+"b")+" at " + (d.getHours() > 12?d.getHours()-12:d.getHours()) + ":" + (d.getMinutes()<10?"0":"") + d.getMinutes() + "("+(si<180000?(si/1000)+"s":(si<90*360000?(si/60000)+"m":(si/3600000)+"h"))+")"); ml.setData(bl); setText.sendMessage(ml);}
				//mProgressDialog = ProgressDialog.show(mCtx, "Gratuitous Notification", "Loading", true);
				//2010-10-04 {Message ml = new Message(); Bundle bl = new Bundle(); bl.putInt("max",ctl.length); bl.putString("title", mReg.getString("sourcetitle","Colliding data")); bl.putBoolean("indeter", true); bl.putString("text", "Processing " + (ctl.length-1)+"\n"+August.naturalLimit+" New Record Limit"); ml.setData(bl); mProgress.sendMessage(ml);}
				//
				Log.w(G,"sr() 1112("+detailtext+")");
				serviceRead.sendEmptyMessageDelayed(detailtext,3750);
				return;
			}
			//
			Log.w(G,"sr() 1117("+detailtext+")");
			serviceRead.sendEmptyMessageDelayed(detailtext,750);
			{Bundle bl = new Bundle(); bl.putInt("id", detailtext); bl.putInt("color",colorm2);Message ml = new Message(); ml.setData(bl);tvColorBg.sendMessageDelayed(ml,1000);}
		}
			/*
			
			Date d = new Date(mReg.getLong("bucket_saved", 0));
			String ct = mReg.getString("bucket", "");
		
			
			if( mReg.getLong("bucket_saved", 0) < mReg.getLong("bucket_done", 0) ){
				String todayCountSQL = mReg.getString("August_todayCountSQL", August.todayCountSQL);
				if(getListView().isShown() || getListView().hasFocus()){}else{Log.e(G,"List isn't shown and nofocus, sensor watch close");wayGo.sendEmptyMessage(2);return;}
				//2010-10-04 mProgressOut.sendEmptyMessage(2);
				Cursor cx = null;
				Uri contentpath = Uri.withAppendedPath(DataProvider.CONTENT_URI,"moment");
				cx = SqliteWrapper.query(mCtx, mCtx.getContentResolver(), contentpath, new String[] {"count(*)"}, todayCountSQL, null, null);
     			int today = 0;
				if( cx != null){if( cx.moveToFirst() ){ if( cx.getInt(0) > 0){today=cx.getInt(0);}; } cx.close();}
				
				cx = SqliteWrapper.query(mCtx, mCtx.getContentResolver(), contentpath, new String[] {"count(*)"}, "", null, null);
     			int onfile = 0;
				if( cx != null){if( cx.moveToFirst() ){ if( cx.getInt(0) > 0){onfile=cx.getInt(0);}; } cx.close();}
				try{
					String handleCleanSQL = mReg.getString("August_handleCleanSQL", August.handleCleanSQL);
					int hand = SqliteWrapper.delete(mCtx, mCtx.getContentResolver(), contentpath, handleCleanSQL, null);//new String[] {"count(*)"});
     			if(hand > 0){easyStatus("Handled removal of " + hand + " past records.");}
				} catch (SQLiteException e){easyStatus("Smart People\nSQL Error\n" + e.getLocalizedMessage());}
				
				
				
				{Message ml = new Message(); Bundle bl = new Bundle(); bl.putInt("id", headerText); bl.putString("text", (desttitle.length()>0?desttitle+"\n":"")+"Success " + (ct.length()>1024?ct.length()/1024+" Kb":ct.length()+"b")+" at " + (d.getHours() > 12?d.getHours()-12:d.getHours()) + ":" + (d.getMinutes()<10?"0":"") + d.getMinutes() + "\nSource Mass " + (size-1) + "\n" + (foundnew > 0?foundnew + " New ":"")+today+" Dated Today\n"+onfile+" Stored"); ml.setData(bl); setText.sendMessage(ml);}
				
				
				{Bundle bl = new Bundle(); bl.putInt("view", mPagev[0]); bl.putInt("page", 0);Message ml = new Message(); ml.setData(bl); pageimg.sendMessageDelayed(ml,1000);}
			
				
			}
				
			//*/
			
		}
	};
	
	Handler speedExam = new Handler(){
		long lt_speedExam = 2;boolean runani = true;
		public void handleMessage(Message msg){
		final Bundle bdl = msg.getData();
		final int wi = bdl.getInt("workindex");
        final boolean runani = bdl.getBoolean("runani",false);
		final int beatid = bdl.getInt("beatid");
        //Log.i(G,"se("+wi+","+runani+")");
        
        
		
		SharedPreferences mReg = getSharedPreferences("Preferences", MODE_WORLD_WRITEABLE);
        Editor mEdt = mReg.edit();
		
        final long go = (SystemClock.uptimeMillis() -  mReg.getLong("go", msg.getWhen()) );

		final String dest = mReg.getString("bucket"+wi+"_dest","");

		//r(
		//  clicker
		//	t
		//:	r3(
		//		wrl(
		//			r3(i:t:i)
		//:			zr["_v"](zl2(zr2(i6:g6[responsems"_v3"]:g7[hostname"_v4"]:g8[ipaddress:ipport"_v6"]:g9:title["_v2"]:source:dest)))
		//:			r3(i:t:i)
		//)
		//:		pear8(i:t:i)
		//:		pear8(i:t:i)
		//:		i5[beatimg]
		//:		ex[HPAGE]()
		//:		g5[responsems]
		//))
		RelativeLayout zr = (RelativeLayout) findViewById(mReg.getInt("bucket"+wi+"_v",-2));
		if(zr == null){
			Log.e(G,"se("+wi+","+mReg.getInt("bucket"+wi+"_v",-2)+") essential zr missing");
			return;
		}
		LinearLayout zl2 = (LinearLayout) zr.getChildAt(0);
		if(zl2 == null){
			Log.e(G,"se("+wi+","+mReg.getInt("bucket"+wi+"_v",-2)+") essential zl2 missing");
			return;
		}

		RelativeLayout zr2 = (RelativeLayout) zl2.getChildAt(0);
		if(zr2 == null){
			Log.e(G,"se("+wi+","+mReg.getInt("bucket"+wi+"_v",-2)+") essential zr2 missing");
			return;
		}

		TextView t = (TextView) zr2.getChildAt(1);
		if(t == null){Log.e(G,"se("+wi+","+mReg.getInt("bucket"+wi+"_v",-2)+","+zr.getId()+"("+zr.getChildCount()+"),"+zr2.getId()+"("+zr2.getChildCount()+")) 3");return;}
		TextView th = (TextView) zr2.getChildAt(2);
		TextView vipaddr = (TextView) zr2.getChildAt(3);//findViewById(g8id);
		
		final int g6id = t.getId();//mReg.getInt("bucket"+wi+"_v3",-1);
		//final int g7id = th.getId();mReg.getInt("bucket"+wi+"_v4",-1);
		//final int iplistid = mReg.getInt("bucket"+wi+"_v5",-1);
		//final int g8id = vipaddr.getId();//mReg.getInt("bucket"+wi+"_v6",-1);
		
		final long lt = bdl.getLong("lastrun",0);
		final long ms = bdl.getLong("ms",0);
		
		String att = t.getText().toString();
		long ms2 = -2;if(att.matches("[0-9]+")){ms2 = Long.parseLong(att);}
		
		if(ms2!=ms){
			//
			Log.w(G,"se() beatimg("+beatid+","+ms2+")");
			{Bundle bl = new Bundle(); bl.putInt("id",beatid); bl.putLong("ms",ms2); Message ml = new Message(); ml.setData(bl); beatimg.sendMessage(ml);}
			
		}
		
		//if(g6id == -1){
		//	
		//Log.e(G,"###################################\n#############\nspeedExam no workindex("+wi+") target");
		//return;
		//}

		
		
			
		if(runani){
		//runani = false;
		
		{Bundle bl = new Bundle(); bl.putInt("color",ms2==ms?Color.argb(255,200,200,20):Color.argb(255,180,80,10)); bl.putInt("id",g6id);Message ml = new Message(); ml.setData(bl); setColor.sendMessageDelayed(ml,75);}
		}else{
		//runani = true;
		{Bundle bl = new Bundle(); bl.putInt("color",textcolor); bl.putInt("id",g6id);Message ml = new Message(); ml.setData(bl); setColor.sendMessageDelayed(ml,75);}	
		}
		
		long ltn = lt;
		if(lt + 60000/2 < System.currentTimeMillis()){
			ltn = System.currentTimeMillis();
			ms2 = -4;
			{Bundle bl = new Bundle(); bl.putInt("color",Color.argb(255,200,20,20)); bl.putInt("id",g6id);Message ml = new Message(); ml.setData(bl); setColor.sendMessageDelayed(ml,75);}
			
			//{Bundle bl = new Bundle(); bl.putInt("color",Color.argb(255,200,40,20)); bl.putInt("id",g6id);Message ml = new Message(); ml.setData(bl); setColor.sendMessageDelayed(ml,175);}
			//mEdt.putLong("bucket"+wi+"_responsems",);
			//mEdt.commit();
		}
		//
		
		long lts = ((lt + 60000/2) - System.currentTimeMillis());
			
		{Bundle bl = new Bundle();bl.putInt("workindex",wi);bl.putInt("beatid",beatid);bl.putLong("lastrun",ltn); bl.putLong("ms",ms2);bl.putBoolean("runani",runani?false:true);Message ml = new Message();ml.setData(bl);speedExam.sendMessageDelayed(ml,750);}//lts > 3000?(lts < 15000?lts:15000):3000
	
		if(lt + 60000/2  > System.currentTimeMillis()){
			//Log.i(G,"se() speedExam("+wi+","+beatid+","+ltn+","+lts+"ms from starting,"+ms2+","+(runani?false:true)+")");//beatimg
			return;	
		}
		
		
		Log.i(G,"############# Running Speed Exam Test");
		
		//LinearLayout l4 = (LinearLayout) findViewById(iplistid);
		String g7t = mReg.getString("bucket"+wi+"_hostname","no hostname");
		//
		Log.i(G,"se() g7("+g7t+")");
		th.setText(g7t);
		int port = mReg.getInt("bucket"+wi+"_ipport",-2);
		String g8t = mReg.getString("bucket"+wi+"_ipaddress","noaddr") + (port>-2?":"+port:"") ;
		//
		Log.i(G,"se() g8("+g8t+")");
		vipaddr.setText(g8t);
		
		
		
		Thread se = new Thread(){
		
		public void run(){
			SharedPreferences mReg = getSharedPreferences("Preferences", MODE_WORLD_WRITEABLE);
	        Editor mEdt = mReg.edit();
			
			
			
			String gourl = dest;
			String loc = "bucket"+wi;
			
			
			Socket socket = null;
			SSLSocket sslsocket = null;
			BufferedReader br = null;
			BufferedWriter bw = null;
			int loopcnt = 0;
			long rxx = -2;
			try {
				
				
				
				
				
				while(gourl.length() > 0 ){
					
					//mPreferencesEditor.putLong("lastfeedactive", System.currentTimeMillis()).commit();
					loopcnt ++;
					
					if( loopcnt > 8 ){
						Log.e(G,"se() Looped 8 times, really?! this many forwards?");
						break;
					}
					if( gourl.matches("javascript:.*")){Log.e(G,"js6");break;}
					boolean secure = gourl.contains("https:") ? true : false;
					String hostname = gourl.replaceFirst(".*://", "").replaceFirst("/.*", "");
					int port = secure ? 443 : 80;
					if( hostname.contains(":") ){
						String[] p = hostname.split(":");
						if(p[1].matches("[0-9]+")){
						hostname = p[0];
						port = Integer.parseInt(p[1]);
						}}
					gourl = (secure?"https":"http")+"://"+hostname+(port!=80?":"+port:"")+"/robots.txt";
					
					String docpath = gourl.replaceFirst(".*://", "").replaceFirst(".*?/", "/");
					//String docpath = "/robots.txt";
					String pageloc = "";
					String contenttype = "text/html";
					Log.w(G,"speedExam("+wi+") g6id("+g6id+") destination("+dest+")");
			        
					mEdt.putString(loc+"_hostname",hostname);
					mEdt.commit();


ContentValues ins = new ContentValues();
        ins.put("hostname",hostname);
        ins.put("docpath",docpath);
        ins.put("url",gourl);
        ins.put("port",port);
Uri source = SqliteWrapper.insert(mCtx, getContentResolver(), Uri.withAppendedPath(DataProvider.CONTENT_URI, "retrospect"), ins);
int sourceid = source.getLastPathSegment().matches("[0-9]+")?Integer.parseInt(source.getLastPathSegment()):-1;
Log.i(G,"Created "+sourceid+" " + source.toString());
String sourceheader = ""; String sourcebody = "";



					
					int dlis = docpath.replaceAll("[?].*","").lastIndexOf('/');// Responsive programing (?style?)
					File df = new File(getfilepath(), hostname + docpath.substring(0,dlis>-1?dlis:docpath.length()-1));
					df.mkdirs();
					File f = new File(getfilepath(), hostname + docpath.replaceAll("[?].*",""));
					if(f.isDirectory()){
						String h7_type = contenttype.replaceAll(".*/","");
						f = new File(f,"index."+h7_type);
					}
					if(!f.exists()){f.createNewFile();}

					
					FileOutputStream fs = null;
					try {
						fs = new FileOutputStream(f);
						pageloc = f.getAbsolutePath();
						//b3.compress(CompressFormat.PNG,0,fs);
						//fs.write(array);
						//httpPage = f.getAbsolutePath();
						//mEdt.putString(loc,httpPage);mEdt.commit();
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					
					} catch (IOException e1) {
		                    Log.e(G, "ComputerStart() 1534 fs.close() failed");
		                    e1.printStackTrace();
		            }
					
					
					
					
					
					
					
					
					
					
					Log.i(G,"se() hostname("+hostname+") path("+docpath+") gourl("+gourl+")");
					gourl = "";
					
					long rrr = System.currentTimeMillis();long rrs = 2;
					
					if( !secure ){
						sslsocket = null;
						Log.w(G,"se() Connecting to hostname("+hostname+") port("+port+")");
						socket = new Socket(hostname,port);
						
						//socket = new SecureSocket();
						//SecureSocket s = null;
						
						if( socket.isConnected() ){
							if(rrs == 2){rrs = System.currentTimeMillis();Log.w(G,"Took " + (rrs - rrr) + "ms ready to respond.");rxx = (rrs - rrr);}
							Log.i(G,"se() Connecting to hostname("+hostname+") CONNECTED");
							InetAddress iix = socket.getInetAddress();
							if(iix != null){mEdt.putString("bucket"+wi+"_ipaddress",iix.getHostAddress());}
							mEdt.putInt("bucket"+wi+"_ipport",socket.getPort());mEdt.commit();
						}else{
							int loopcnt2 = 0;
							while( !socket.isConnected() ){
								Log.e(G,"se() Not connected to hostname("+hostname+")");
								loopcnt2++;
								if( loopcnt2 > 10 ){
									Log.e(G,"se() Not connected to hostname("+hostname+") TIMEOUT REACHED");
									break;
								}
								SystemClock.sleep(300);
							}
						}
						
						//Log.w(G,"se() Creating Writable to hostname("+hostname+") port("+port+")");
						bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
						//Log.w(G,"se() Creating Readable to hostname("+hostname+") port("+port+")");
						br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					}else{
						socket = null;
						Log.w(G,"se() Connecting Securely to hostname("+hostname+") port("+port+")");
						
						SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory.getDefault();
						sslsocket = (SSLSocket) factory.createSocket(hostname,443);
						SSLSession session = sslsocket.getSession();
						X509Certificate cert;
						try { cert = (X509Certificate) session.getPeerCertificates()[0]; }
						catch(SSLPeerUnverifiedException e){
							Log.e(G,"se() Connecting to hostname("+hostname+") port(443) failed CERTIFICATE UNVERIFIED");
							break;
						}
						
						if( sslsocket.isConnected() ){
							if(rrs == 2){rrs = System.currentTimeMillis();Log.w(G,"Took " + (rrs - rrr) + "ms ready to respond.");rxx = (rrs - rrr);}
							Log.i(G,"se() Connecting to hostname("+hostname+") CONNECTED");
							InetAddress iix = sslsocket.getInetAddress();
							if(iix != null){
							mEdt.putString("bucket"+wi+"_ipaddress",iix.getHostAddress());}
							mEdt.putInt("bucket"+wi+"_ipport",sslsocket.getPort());mEdt.commit();
						}else{
							int loopcnt2 = 0;
							while( !sslsocket.isConnected() ){
								Log.e(G,"se() Not connected to hostname("+hostname+")");
								loopcnt2++;
								if( loopcnt2 > 20 ){
									Log.e(G,"se() Not connected to hostname("+hostname+") TIMEOUT REACHED");
									break;
								}
								SystemClock.sleep(300);
							}
						}					
						
						//Log.w(G,"se() Creating Writable to hostname("+hostname+") port("+port+")");
						bw = new BufferedWriter(new OutputStreamWriter(sslsocket.getOutputStream()));
						//Log.w(G,"se() Creating Readable to hostname("+hostname+") port("+port+")");
						br = new BufferedReader(new InputStreamReader(sslsocket.getInputStream()));
					}
					
					
					
					
					if(gourl.matches("[hH][tT][tT][pP].*")){
					mEdt.putString(loc+"_proto",secure?"https":"http");
						//mEdt.putLong(loc+"_connected",System.currentTimeMillis());
					//mEdt.putLong("lastfeedactive", System.currentTimeMillis());mEdt.commit();
						Log.w(G,"se() Requesting document hostname("+hostname+") port("+port+")");
					bw.write("GET "+docpath+" HTTP/1.0\r\n");
					bw.write("Host: " + hostname + "\r\n");
					bw.write("User-Agent: Android\r\n");
					//bw.write("Range: bytes=0-"+(1024 * 1)+"\r\n");
					//bw.write("TE: deflate\r\n");
					bw.write("\r\n");
					bw.flush();
					//http://www.w3.org/Protocols/rfc2616/rfc2616-sec5.html#sec5
					Log.i(G,"Request delivered ("+hostname+") in http to /robots.txt");
					}else{
						mEdt.putString(loc+"_proto",secure?"protos":"proto");
						
					
					Log.i(G,"Request delivered ("+hostname+")");
					}
					
					mEdt.commit();
					String status = "";
					String line = "";
					try {
						/*
						if( !secure ){
							if( br.ready() ){
								w(TAG,"getPage() Ready to be read");
							}else{
								int loopcnt2 = 0;long sv = SystemClock.uptimeMillis();
								while( !br.ready() ){
									e(TAG,"getPage() NOT Ready to be read");
									loopcnt2++;
									if( SystemClock.uptimeMillis() - sv > 30000 ){
										e(TAG,"getPage() NOT Ready to be read TIMEOUT REACHED WAITING");
										//line = br.readLine();
										//e(TAG,"getPage() NOT Ready to be read TIMEOUT REACHED WAITING line("+line+")");
										break;
									}
									SystemClock.sleep(300);
								}
							}
						}else{
							// br.ready() doesn't work from the sslsocket source
						}//*/
						//Log.i(G,"####################Ready for Reply#############");
						//rrr = System.currentTimeMillis();rrs = 2;
						int linecnt = 0;
						
						//
						Log.i(G,"se() g6("+g6id+","+rxx+")");
						{Bundle bl = new Bundle(); bl.putString("text",""+rxx); bl.putInt("id",g6id);Message ml = new Message(); ml.setData(bl); setText.sendMessage(ml);}
						
						
						
						if(gourl.matches("[hH][tT][tT][pP].*")){
						for(line = br.readLine(); line != null; line = br.readLine()){if(rrs == 2){rrs = System.currentTimeMillis();Log.w(G,"Took " + (rrs - rrr) + "ms ready to respond.");rxx = (rrs - rrr);}
							if( line.length() == 0 ){
								Log.w(G,"se() End of header Reached");
								break;
							}
							linecnt++;
							Log.i(G,"se() received("+line+")");
sourceheader += line;
							if( line.regionMatches(true, 0, "Location:", 0, 9) ){
								gourl = line.replaceFirst(".*?:", "").trim();
								Log.i(G,"se() ###############>>>>>>>>>>>>>> FOUND FORWARD URL("+gourl+") ");
							}else
							if(line.regionMatches(true,0,"Content-Length:", 0, 15) && line.replaceAll(".*?:","").trim().matches("[0-9]+")){
								int css = Integer.parseInt(line.replaceAll(".*?:","").trim());
								//mEdt.putLong(loc+"_length",css);
								Log.i(G,"se() "+loc+"_length("+css+")");
							}else
							if(line.regionMatches(true,0,"Content-Type:", 0, 13) ){
								String ct = line.replaceAll(".*?:","").replaceAll(";.*","").trim();
								contenttype = ct;
								Log.i(G,"se() "+loc+"_type("+ct+")");
								if(line.toLowerCase().matches(".*;.*?charset.*?=.*")){
									String ch = line.replaceAll(".*;.*?[cC][hH][aA][rR][sS][eE][tT].*?=","").trim();
									//mEdt.putString(loc+"_charset",ch);
									Log.i(G,"se() "+loc+"_charset("+ch+")");
								}
							}
						
						}
						if( gourl.length() > 0 ){ continue; }
						if( line == null ){
							//w(G,"se() End of read");
						}
						if( linecnt > 0 ){
							//mEdt.putLong("lastfeedactive", System.currentTimeMillis());
							//mEdt.putLong("lowmemory", 0);
						}
						//mEdt.commit();
						
						
						}else{
							Log.w(G,"Listening for first line of content");
							line = "";
						}
						
						
						//
						Log.i(G,"se() g6("+g6id+","+rxx+")");
						{Bundle bl = new Bundle(); bl.putString("text",""+rxx); bl.putInt("id",g6id);Message ml = new Message(); ml.setData(bl); setText.sendMessage(ml);}
						
						
						int httpPageSize = 0;
						if( line != null ){
							//int zerocnt = 0;
							char[] u7 = new char[10240];
							

							long pr = -1;
							for(int cs = br.read(u7); cs > -1; cs = br.read(u7,0,10240)){
								pr = SystemClock.uptimeMillis();if(rrs == 2){rrs = System.currentTimeMillis();Log.w(G,"Took " + (rrs - rrr) + "ms ready to respond.");rxx = (rrs - rrr);}
								//if( line.length() == 0 ){
									//zerocnt++;
									//if( zerocnt > 50 ){
										//e(TAG,"getPage() host("+hostname+") 50 empty lines received, moving on.");
										//break;
									//}
									//continue;
								//}
								//zerocnt = 0;
								
								/* didn't work
								try{
									
									Charset charset = Charset.forName("US-ASCII");
									CharsetDecoder decoder = charset.newDecoder();
									CharsetEncoder encoder = charset.newEncoder();
									ByteBuffer bbuf = ByteBuffer.wrap(line.getBytes());
								    CharBuffer c2;
									c2 = decoder.decode(bbuf);	
								    line = c2.toString();
								} catch (CharacterCodingException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}//*/
								
								
								
								
								line = Uri.encode(new String(u7));
								/*
								String g[] = line.replaceAll("%20"," ").replaceAll("%1","\n%1").replaceAll("%7F","\n%7F").replaceAll("%0","\n%0").replaceAll("%8","\n%8").replaceAll("%9","\n%9").replaceAll("%A","\n%A").replaceAll("%B","\n%B").replaceAll("%C","\n%C").replaceAll("%D","\n%D").replaceAll("%E","\n%E").replaceAll("%F","\n%F").split("\n");
				        		for(int gi=0;gi<g.length;gi++){
			        				if(g[gi].matches("%(0[1-8BCEF]|[8-9A-F1][0-9A-F]|7F).*")){
		        					Log.w(TAG,"["+gi+"]"+g[gi].substring(0,3)+"[]");
			        				}}//*/
								line = line.replaceAll("%([8-9A-F1][0-9A-F]|7F|0[1-8BCEF])","");
								line = Uri.decode(line);
								line = line.replaceAll("\t+"," ").replaceAll("\n+","\n").replaceAll(" +"," ").replaceAll(" +<","<").replaceAll("> +",">");
								line = line.replaceAll("\n+<","<").replaceAll(">\n+",">");
								//line = line.replaceAll(">",">\n").replaceAll("<","\n<");
								//line = line.replaceAll("\n+</","</");
								//linecnt++;
								//i(TAG,"getPage() host("+hostname+") line("+line+")");
								//fs.write(line.getBytes());
								line = line.trim();
								if(line.length() > 0){
								fs.write(line.getBytes());}
								httpPageSize += cs;
sourcebody += line;								

								Log.i(G,"se() being sent " + cs + " bytes secure("+secure+") encoded("+line.length()+") " + (SystemClock.uptimeMillis() - pr) + "ms");
								
								//httpPage += Uri.decode(line);
								//if( httpPageSize > 1024 * DOWNLOAD_LIMIT ){
									//w(G,"getPage() downloaded "+DOWNLOAD_LIMIT+"K from the site, moving on.");
									//break;
								//}
							}
						
							Log.i(G,"se() download concluded");
							
							fs.flush();
			                fs.close();
						
			                //FileInputStream h = new FileInputStream(f);
			                //byte[] bx = new byte[(int)f.length()];
			                //h.read(bx);
			                //httpPage = new String(bx);
			                
			                Log.i(G,"############## httpPage download " + httpPageSize );
ContentValues uns = new ContentValues();
uns.put("body",sourcebody);
uns.put("header",sourceheader);
//uns.put("title",);
SqliteWrapper.update(mCtx, getContentResolver(), Uri.withAppendedPath(DataProvider.CONTENT_URI, "retrospect"), uns, "_id = " + sourceid, null);
			                
						}
						
						Log.w(G,"se() Downloaded("+httpPageSize+" bytes)");
						File nf = new File(f.getAbsolutePath().substring(0,f.getAbsolutePath().lastIndexOf('.'))+contenttype.replaceAll(".*?/",""));
						if(f.renameTo(nf)){
							Log.i(G,"Downloaded and renamed to " + nf.getAbsolutePath());
							if(f.exists()){Log.i(G,"exists 1");}else{Log.i(G,"-exists 1");}
							f = nf;
							if(f.exists()){Log.i(G,"exists 2");}else{Log.i(G,"-exists 2");}
						}
						
						//mEdt.putLong("bucket_saved", System.currentTimeMillis());
						//mEdt.putLong(loc+"_saved", System.currentTimeMillis());
						//mEdt.commit();
						
						/*/
						
						if( br.ready() ){
							
						}
						while(br.ready()){
							line = br.readLine();
							if( line == null ){
								w(TAG,"getPage() End of read Reached");
								break;
							} else if( line.length() == 0 ){
								w(TAG,"getPage() End of header Reached");
								break;
							}
							i(TAG,"getPage() feed("+longname+") received("+line+")");
							if( line.regionMatches(true, 0, "Location:", 0, 9) ){
								gourl = line.replaceFirst(".*?:", "").trim();
								w(TAG,"getPage() feed("+longname+") FOUND FORWARD URL("+gourl+") ");
							}
						}
						
						
						
						mPreferencesEditor.putLong("lastfeedactive", System.currentTimeMillis()).commit();
						while(br.ready()){
							line = br.readLine();
							if( line == null ){
								w(TAG,"getPage() End of read Reached");
								break;
							} else if( line.length() == 0 ){
								w(TAG,"getPage() End of header Reached");
								break;
							}
						}
						//*/
					}catch (IOException e1) {
						String msg = null;
						msg = e1.getLocalizedMessage() != null ? e1.getLocalizedMessage() : e1.getMessage();
						if( msg == null ){
							msg = e1.getCause().getLocalizedMessage();
							if( msg == null ){ msg = ""; }
						}
						Log.e(G,"se() e1(" + msg + ") ("+hostname+")");//IOException while reading from web server " + msg);
						mEdt.putString("errortype",msg);
						mEdt.commit();
						rxx = -10;
						e1.printStackTrace();
					}
					
					if( !secure ){
						socket.close();
					}else{
						sslsocket.close();
					}
				}
			} catch (UnknownHostException e1) {
				Log.e(G,"se() unknownHostException");
				mEdt.putString("errortype","Unknown host " + e1.getLocalizedMessage());
				mEdt.commit();
				e1.printStackTrace();
				rxx = -11;
			} catch (IOException e1) {
				Log.e(G,"se() IOException");
				mEdt.putString("errortype","serious face " + e1.getLocalizedMessage());
				mEdt.commit();
				e1.printStackTrace();
				rxx = -12;
			}
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			//if(rxx > -1){	
			//
			Log.i(G,"se() g6("+g6id+","+g6id+")");
			{Bundle bl = new Bundle(); bl.putString("text",""+rxx); bl.putInt("id",g6id);Message ml = new Message(); ml.setData(bl); setText.sendMessage(ml);}
			//}
		
		
			
			
		}};
		
		
		
		se.start();
		//Log.e(G, "se() shutdown at 1714");
	
		
		
		
}};
			
	Handler serviceinit = new Handler(){public void handleMessage(Message msg){
		Intent service = new Intent();service.setClass(mCtx, com.havenskys.galaxy.AutomaticService.class);
		if(msg.what == 1 || msg.what == 2){//stop 
		if(stopService(service)){
    	Log.e(G,"Service Stop");
    	}//else{Log.e(G,"Service stop paper error");
		//serviceinit.sendEmptyMessageDelayed(msg.what,2700);return;
    	//}
    	}
		
		if(msg.what == 2){//start after stop
    	 
			//if(stopService(service)){
			Log.e(G,"Service Start");
			startService(service);
			//}else{Log.e(G,"Service start paper error");startService(service);}
			//if(stopService(service)){
			//Log.e(G,"Service ");
			//startService(service);
			//}else{Log.e(G,"Service start paper error");startService(service);}
		}
			
	}};
    	//Intent service2 = new Intent();service2.setClass(mCtx, com.havenskys.galaxy.AutomaticService.class);
	Handler getlist = new Handler(){
    	private long hover = 0;
    	@Override
		public void handleMessage(Message msg){
    		if(hover > SystemClock.uptimeMillis()){
    			RelativeLayout hl = (RelativeLayout) getListView().getChildAt(1);
    			hl.setBackgroundColor(Color.MAGENTA);hover = SystemClock.uptimeMillis() + 5000;
    			{Bundle bl = new Bundle(); bl.putInt("id", hl.getId()); bl.putInt("color",colorm3);Message ml = new Message(); ml.setData(bl);rlColorBg.sendMessage(ml);}
    			{Bundle bl = new Bundle(); bl.putInt("id", hl.getId()); bl.putInt("color",colorm2);Message ml = new Message(); ml.setData(bl);rlColorBg.sendMessageDelayed(ml,300);}
    			{Bundle bl = new Bundle(); bl.putInt("id", hl.getId()); bl.putInt("color",colorm1);Message ml = new Message(); ml.setData(bl);rlColorBg.sendMessageDelayed(ml,350);}
    			{Bundle bl = new Bundle(); bl.putInt("id", hl.getId()); bl.putInt("color",colorm2);Message ml = new Message(); ml.setData(bl);rlColorBg.sendMessageDelayed(ml,400);}return;
    			//easyStatus("Want a tap-tap game here? Email Me.");return;
    		
    		}
    		hover = SystemClock.uptimeMillis() + 5000;
    		
    		

    		final Bundle bdl = msg.getData();
    		SharedPreferences mReg = getSharedPreferences("Preferences", MODE_WORLD_WRITEABLE);
            Editor mEdt = mReg.edit();
    		if(mReg.getLong("bucket_start", 0) > mReg.getLong("bucket_done", 0)){
    			
    			
    			{Message ml = new Message(); Bundle bl = new Bundle(); bl.putInt("id", headerText); bl.putString("text", "Restart " + ((System.currentTimeMillis() - mReg.getLong("bucket_start", 0))/1000) + " seconds."); ml.setData(bl); setText.sendMessageDelayed(ml,pRate);}
    			mEdt.putLong("bucket_done", 0);
    			mEdt.putLong("bucket_error", 0);	
            	mEdt.putLong("bucket_start", 0);
            	mEdt.commit();
            	
    			serviceinit.sendEmptyMessageDelayed(1,30);
    			
    			RelativeLayout hl = (RelativeLayout) getListView().getChildAt(1);
    			if(hl == null){Log.e(G,"hl empty ("+getListView().getChildCount()+","+getListView().getCount()+")");}else{
    			hl.setBackgroundColor(Color.MAGENTA);
    		}   			hover = SystemClock.uptimeMillis() + 500;
    			{Bundle bl = new Bundle(); bl.putInt("id", hl.getId()); bl.putInt("color",colorm3);Message ml = new Message(); ml.setData(bl);rlColorBg.sendMessage(ml);}
    			//{Bundle bl = new Bundle(); bl.putInt("id", hl.getId()); bl.putInt("color",colorm2);Message ml = new Message(); ml.setData(bl);rlColorBg.sendMessageDelayed(ml,100);}
    			//{Bundle bl = new Bundle(); bl.putInt("id", hl.getId()); bl.putInt("color",colorm3);Message ml = new Message(); ml.setData(bl);rlColorBg.sendMessageDelayed(ml,120);}
    			//{Bundle bl = new Bundle(); bl.putInt("id", hl.getId()); bl.putInt("color",colorm2);Message ml = new Message(); ml.setData(bl);rlColorBg.sendMessageDelayed(ml,130);}
    			//{Bundle bl = new Bundle(); bl.putInt("id", hl.getId()); bl.putInt("color",colorm3);Message ml = new Message(); ml.setData(bl);rlColorBg.sendMessageDelayed(ml,140);}
    			{Bundle bl = new Bundle(); bl.putInt("id", hl.getId()); bl.putInt("color",colorm2);Message ml = new Message(); ml.setData(bl);rlColorBg.sendMessageDelayed(ml,150);}
    			
    			{Bundle bl = new Bundle(bdl);Message ml = new Message(); ml.setData(bl);getlist.sendMessageDelayed(ml,3000);}
    			
    			//{mEdt.putLong("bucket_error",System.currentTimeMillis());mEdt.putString("errortype","Request Stop");mEdt.commit();}
    			return;
    		}
    		{Message ml = new Message(); Bundle bl = new Bundle();bl.putString("text","getlist");ml.setData(bl);logoly.sendMessage(ml);}
    		
    		
    		if( !mReg.contains("adds") ){
    			{	
    			ContentValues inc = new ContentValues();
				inc.put("location", August.dest);
				inc.put("title",August.title);
				//inc.put("prefilter", ct);
				inc.put("status",10);
				Uri source = SqliteWrapper.insert(mCtx, getContentResolver(), Uri.withAppendedPath(DataProvider.CONTENT_URI, "filter"), inc);
				Log.i(G,"Created " + source.toString());
    			}
    			
    			if(August.dest.contains("metrix")){	
    			ContentValues inc = new ContentValues();
				inc.put("location", "http://twitter.com/statuses/user_timeline/73936473.rss");
				inc.put("title","Twitter / Metrix Create:Space");
				//inc.put("prefilter", ct);
				inc.put("status",10);
				Uri source = SqliteWrapper.insert(mCtx, getContentResolver(), Uri.withAppendedPath(DataProvider.CONTENT_URI, "filter"), inc);
				Log.i(G,"Created " + source.toString());
    			}
				
    			if(August.dest.contains("metrix")){	
    			ContentValues inc = new ContentValues();
				inc.put("location", "http://api.flickr.com/services/feeds/photos_public.gne?id=42471238@N02&lang=en-us&format=rss_200");
				inc.put("title","Uploads from Metrix Create:Space");
				//inc.put("prefilter", ct);
				inc.put("status",10);
				Uri source = SqliteWrapper.insert(mCtx, getContentResolver(), Uri.withAppendedPath(DataProvider.CONTENT_URI, "filter"), inc);
				Log.i(G,"Created " + source.toString());
    			}
    			
				mEdt.putLong("adds",System.currentTimeMillis());
    			mEdt.commit();
    		}
    		
    		
    		
    		//Thread tx = new Thread(){
    			//@Override
				//public void run(){
    				
    				//long lvhi = getListView().getItemIdAtPosition(0);
    				//LinearLayout lvh = (LinearLayout) findViewById((int)lvhi);//.getItemAtPosition(1);
    				//if(headerText > 0){
    				//TextView tvh = (TextView) findViewById(headerText);
    				//if(tvh != null){
    	            	mEdt.putLong("bucket_error", 0);	
    	            	mEdt.putLong("bucket_start", 0);mEdt.commit();
    					//long lt = mReg.getLong("bucket_saved", 0);
    					//tvh.setText("Running");
    					
    					//String sourcetitle = mReg.getString("sourcetitle","");
    					//{Message ml = new Message(); Bundle bl = new Bundle(bdl); ml.setData(bl); mGet.sendMessage(ml);}
    					//{Message ml = new Message(); Bundle bl = new Bundle(); bl.putInt("id", headerText); bl.putString("text", "Running"); ml.setData(bl); setText.sendMessage(ml);}
    				
    	            	
    	            	
    	            	
    	            	serviceinit.sendEmptyMessageDelayed(2,30);
    	            	
    	            	/*
    					try {
    						long freememory = 0;
    						for(int i = 0; i < 300 && getListView().isShown(); i++){	
    							if(getListView().isShown() || getListView().hasFocus()){}else{Log.e(G,"List isn't shown and nofocus, sensor watch close");wayGo.sendEmptyMessage(2);break;}
								//if(getListView().isShown()){}else{Log.e(G,"List isn't shown, process watch close");wayGo.sendEmptyMessage(2);break;}
								freememory = Runtime.getRuntime().freeMemory();
								{Message ml = new Message(); Bundle bl = new Bundle(); bl.putString("text","processing " + i + " freememory("+freememory/1024+" Kb) has(" + getListView().hasFocus() +") shown("+getListView().isShown()+") enabled("+getListView().isEnabled()+")");ml.setData(bl);logoly.sendMessage(ml);}
								
								if(mReg.getLong("bucket_start", 0) == 0 || (mReg.getLong("bucket_start", 0) > mReg.getLong("bucket_saved", 0) && mReg.getLong("bucket_start", 0) > mReg.getLong("bucket_error", 0)) ){
									{Message ml = new Message(); Bundle bl = new Bundle(); bl.putInt("id", headerText); bl.putString("text", "Running " + ((System.currentTimeMillis() - mReg.getLong("bucket_start", 0))/1000) + " seconds."); ml.setData(bl); setText.sendMessageDelayed(ml,pRate);}
									Thread.sleep(750);
									continue;
								}else{
									
									
								}
								break;
							}
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						//*/
						
						
    			//}
    		//};
    		//tx.start();
    	}	
    };
	
    
    
    
    
    
    
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.w(G,"onP");
		if(getListView().isShown() || getListView().hasFocus()){}else{Log.e(G,"List isn't shown and nofocus, sensor watch close");wayGo.sendEmptyMessage(2);return;}  
	}

	int wHeight = -2; int wWidth = -2;
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.w(G,"Resume");lastposition = 0;
		wHeight = getWindowManager().getDefaultDisplay().getHeight();
		wWidth = getWindowManager().getDefaultDisplay().getWidth();
		Log.w(G,"Resume " + wWidth + "x" + wHeight);
		
		if(mPagev != null){
			pagesGen.sendEmptyMessageDelayed(-2,500);
			//if(mPagev.length > mPagen){
			//{Bundle bl = new Bundle(); bl.putInt("view", mPagev[mPagen]); bl.putInt("page", mPagen);Message ml = new Message(); ml.setData(bl); pageimg.sendMessageDelayed(ml,1000);}
		}//}
		
	}
	

	Handler wayGo = new Handler(){
		public void handleMessage(Message msg){
			wayGo();
		}
	};

	private void wayGo(){finish();}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
    	
		menu.add(0, 404, 0, "Update").setIcon(android.R.drawable.ic_menu_today);
		//menu.add(0, 405, 0, "ScreenShot").setIcon(android.R.drawable.ic_menu_camera);
		
		
		SharedPreferences mReg = getSharedPreferences("Preferences", MODE_WORLD_WRITEABLE);
    	/*if(mReg.contains("wave")){
    		menu.add(0, 406, 0, "Wave Away").setIcon(android.R.drawable.ic_menu_add);
    	}else{
    		menu.add(0, 406, 0, "Wave Open").setIcon(android.R.drawable.ic_menu_add);
    	}*/
    	
    	//menu.add(0, 413, 3, "Quit");
    	
    	{
    		int groupNum = 20;
			SubMenu sync = menu.addSubMenu(Menu.NONE, groupNum, 20, "How often"); //getItem().
			sync.setIcon(android.R.drawable.ic_menu_agenda);
			sync.add(groupNum, 0, 0, "Not Automatic");//value == 0
			sync.add(groupNum, 30, 2, "30 Minutes");
			sync.add(groupNum, 60, 2, "Hourly");
			sync.add(groupNum, 1, 2, "5 Hours");//value == 1
			sync.add(groupNum, 2, 3, "Daily");// value == 2
			
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
    	
    	return super.onCreateOptionsMenu(menu);
	}

    
    @Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		final int itemid = item.getItemId();
		final int groupid = item.getGroupId();
		if(groupid > 0){item.setChecked(true);}
		SharedPreferences mReg = getSharedPreferences("Preferences", MODE_WORLD_READABLE);
		if(itemid == 406){ if(!mReg.contains("wave")){ item.setTitle("Wave Away"); }else{item.setTitle("Wave Open");} }
		
		//moomo = menu;
		//moomoMenu.sendEmptyMessageDelayed(50,10);
		Thread tx = new Thread(){public void run(){
		switch(groupid){
		case 0:
			if(itemid == 404){
				{Message ml = new Message(); Bundle bl = new Bundle(); bl.putLong("position", 1);  ml.setData(bl); click.sendMessage(ml);}
			}
			if(itemid == 405){
				takescreen.sendEmptyMessageDelayed(2,3000);
			}
			if(itemid == 406){
				SharedPreferences mReg = getSharedPreferences("Preferences", MODE_WORLD_WRITEABLE);
				Editor mEdt = mReg.edit();
				if(!mReg.contains("wave")){
					mEdt.putBoolean("wave", true);mEdt.commit();
					{SensorService.sendEmptyMessageDelayed(2,pRate);}
					//easyStatus("Wave Ready");
				}else{
					mEdt.remove("wave");mEdt.commit();
					//mEdt.putBoolean("wave", false);
				}
				
			}
			if(itemid == 413){
			
				Intent d2 = new Intent(mCtx,Motion.class);
				d2.putExtra("code413", true);
				d2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				//d2.addFlags(d2.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
				startActivity(d2);
				
				finish();
				//wayGo.sendEmptyMessage(2);
			}
			break;
		case 20: // Interval
			
			{Message mxm = new Message(); Bundle bxb = new Bundle();bxb.putString("int", "interval");bxb.putInt("interval",itemid);mxm.setData(bxb);setrefHandler.sendMessage(mxm);}
			if(itemid == 0){
				easyStatus("Not Automatic");
				Intent service = new Intent();service.setClass(mCtx, com.havenskys.galaxy.AutomaticService.class); stopService(service);
			}else{easyStatus("Interval Setting Saved");Intent service = new Intent();service.setClass(mCtx, com.havenskys.galaxy.AutomaticService.class); startService(service);}
			break;
		default:
			break;
		}
		}};tx.start();
		return super.onOptionsItemSelected(item);
	}
    
    
	protected void onListItemClick(ListView l, View v, int position, long id) {
		{Message ml = new Message(); Bundle bl = new Bundle(); bl.putLong("id", id); bl.putLong("position", position); ml.setData(bl); click.sendMessage(ml);}
		super.onListItemClick(l, v, position, id);
    }
	
	
	Handler clickLong = new Handler(){
		public void handleMessage(Message msg){
			Bundle bdl = msg.getData();
			//long id = bdl.getLong("id",0);
			//long position = bdl.getLong("position",0);
			//19.prospect
			try{
			RelativeLayout rl = (RelativeLayout) getListView().getSelectedView();
			if(rl != null){
				rl.performLongClick();
			}
			}catch(ClassCastException e){}
			
		}
	};
		
	Handler click = new Handler(){
		public void handleMessage(Message msg){
			Bundle bdl = msg.getData();
			long id = bdl.getLong("id",0);
			long position = bdl.getLong("position",0);
			
			{Message mx = new Message(); Bundle bx = new Bundle();bx.putString("text","List Item Clicked position("+position+") id("+id+") count("+getListView().getCount()+")");bx.putInt("l",2);mx.setData(bx);logoly.sendMessageDelayed(mx,pRate);}
			
			if(position == 0 && mPagev!=null ){
				if(mPagen < 0){mPagen = 0;}
				if(mPagev.length > mPagen){
		        	ImageView sx = (ImageView) findViewById(mPagev[mPagen]);
		        	if(sx != null){sx.requestFocusFromTouch();}
		        }
			}else if(position == 1){
				//header
				SharedPreferences mReg = getSharedPreferences("Preferences", MODE_WORLD_READABLE);
				String title = mReg.getString("August_title", August.title);
				String dest = mReg.getString("August_dest", August.dest);
				{Message ml = new Message(); Bundle bl = new Bundle(); bl.putString("title", title); bl.putString("dest", dest); bl.putString("storloc", "bucket"); ml.setData(bl); getlist.sendMessage(ml);}
			
				
			
			}else if(position == getListView().getCount()-1){
				//footer
				pushlist.sendEmptyMessage(-2);
				
			}else if(position > getListView().getHeaderViewsCount() -1 ){
				//moment
				//Cursor cx = SqliteWrapper.query(mCtx, mCtx.getContentResolver(), contentpath, new String[] {"url"}, "_id = " + id, null, null);
	 			//if( cx != null){if( cx.moveToFirst() ){ if( cx.getInt(0) > 0){found=true;}; } cx.close();}
				if(position > 0){	
					id = getListView().getItemIdAtPosition((int)position);
				}
				Intent lookup = new Intent(mCtx,com.havenskys.galaxy.Lookup.class);
				lookup.putExtra("id", id);
				lookup.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
				startActivity(lookup);
				
				if(mPagev != null){
					if(mPagev.length > mPagen){
					{Bundle bl = new Bundle(); bl.putInt("view", mPagev[mPagen]); bl.putInt("page", mPagen);Message ml = new Message(); ml.setData(bl); pageimg.sendMessageDelayed(ml,1000);}
				}}
			}

		}
	};
	
	
	int lastposition = 0;
	
	Handler SensorService = new Handler(){
    	boolean running = false;
		public void handleMessage(Message msg){
    		final Bundle bdl = msg.getData();
    		
    		SharedPreferences mReg = getSharedPreferences("Preferences", MODE_WORLD_READABLE);
    		if( !mReg.contains("waveopt") ){ Editor mEdt = mReg.edit();  mEdt.putBoolean("wave", true); mEdt.putBoolean("waveopt", true); mEdt.commit();     }else{
    		if( !mReg.contains("wave") ){ return; }}
    		if(running){return;}
    		
    		running = true;
    		Thread tx = new Thread(){
    			boolean mStable = true;
    			int position = 0;float[] lastvalues;
				long smooth = 34;//long smoothtext = 32;//String cn = "";
				
    			public void run(){
    				
    				SensorEventListener or = new SensorEventListener(){


    					SharedPreferences mReg = getSharedPreferences("Preferences", MODE_WORLD_READABLE);

    					public void onAccuracyChanged(Sensor arg0, int arg1) {
    						// TODO Auto-generated method stub
    						
    					}

    					//float mStable0 = 1;
    					public void onSensorChanged(SensorEvent event) {
    						// TODO Auto-generated method stub
    						
    						if(smooth > SystemClock.uptimeMillis() || !getListView().hasFocus() ){return;}
    						
    						smooth = SystemClock.uptimeMillis() + 130;//bdl.getInt("sensorspeed",250);
    						float[] values = event.values;
    						float valence = 0;
							
    						if(lastvalues == null){
    							Log.w(G,"Loading Initial Sensor Values");
    							lastvalues = values;
    							for(int b = 0; b < values.length; b++){lastvalues[b] = 0;}
    						}
    						if(getListView().isShown() || getListView().hasFocus()){}else{Log.e(G,"List isn't shown and nofocus, sensor watch close");wayGo.sendEmptyMessage(2);}
    						
    						if( lastvalues != null && values.length == lastvalues.length ){
    							//if(){
    							if(!mReg.contains("wave")){return;}
    								position = getListView().getSelectedItemPosition();
    								if(position == -1 || position == getListView().getCount()-1){return;}
    								boolean flowon = false;
    								for(int b = 0; b < values.length; b++){
    									//float o = lastvalues[b];
    									valence = (lastvalues[b]>values[b]?lastvalues[b]-values[b]:values[b]-lastvalues[b]);
    									lastvalues[b] = values[b];
    									
    									
    									/*
    									if(b == 0){
    										float valence1 = values[b]-o;
    										//float valence0 = values[0]-lastvalues[0];
    										float valence2 = values[b+1]-lastvalues[b+1]; 
    										float valence3 = values[b+2]-lastvalues[b+2]; 
    										if(valence1 > .34 && valence2 > valence1 && valence2 > valence3 && position > 0){
    											//try{
    											//View ll = getListView().focusSearch(View.FOCUS_UP);
    											//if(ll != null){ll.requestFocusFromTouch();}
    											//}catch()
    											getListView().setSelection(position-1);getListView().setSelectionFromTop(position-1, 132);
    											//smooth = SystemClock.uptimeMillis() + 1250;
    											flowon = true;//lastvalues = null;
    											continue;
    										}else if(valence1 < -.34 && valence2 < valence1 && valence2 < valence3 ){
    											//View ll = getListView().focusSearch(View.FOCUS_DOWN);
    											getListView().setSelection(position+1);getListView().setSelectionFromTop(position+1, 132);
    											//if(ll != null){ll.requestFocusFromTouch();}
    											//smooth = SystemClock.uptimeMillis() + 1250;
    											flowon = true;//lastvalues = null;
    											continue;
    											//getListView().setSelection(position-1);if(ll.getTop() < 0){getListView().setSelectionFromTop(position-1, 0);}else{getListView().setSelectionFromTop(position-1,134);}
    										}
    									}//*/
    									if(valence <= 3.4 || !getListView().isShown() || flowon ){continue;}
    									
    									//Log.i(G,"Sensor Orientation ["+b+"] "+lastvalues[b]+" to "+values[b]+" position("+position+") last("+lastposition+") valence " + valence);
    									
    										//Log.w(G,"Wave Select has(" + getListView().hasFocus() +") shown("+getListView().isShown()+") position("+position+") last("+lastposition+")");
    										
    										if(position != lastposition){
    											//smooth = SystemClock.uptimeMillis() + bdl.getInt("sensorspeed",1750);
        										
    											//getListView().clearFocus();
    											
    											{Message ml = new Message(); Bundle bl = new Bundle(); bl.putLong("position", position); ml.setData(bl); clickLong.sendMessage(ml);}
    											lastposition = position;
    										}
    										
    										
    										
    										

    										
    										break;
    									
    								}
    								//if(position != lastposition){
    									//lastvalues = values;
    								//}
    							//}	
    						}
    						//int sensorid = event.sensor.getType();
    						//if(sensorid == SensorManager.SENSOR_ORIENTATION){if(mStable){mStable0 = values[0];mStable = false;}					
    							
    						
    					    
    						//{Message ml = new Message(); Bundle bl = new Bundle(); bl.putInt("id", R.id.apps); bl.putInt("x", (int)((mStable0>values[0]?mStable0-values[0]:values[0]-mStable0)*71)); bl.putInt("y", (int)(values[1]*33)); ml.setData(bl); move.sendMessage(ml);}
    						
    						//	{Message ml = new Message();Bundle bl = new Bundle(); bl.putFloat("u1", values[0]); bl.putFloat("u2", values[1]); bl.putFloat("u3", values[2]); ml.setData(bl);setOrientation.sendMessageDelayed(ml,pRate);}
    							//{Message ml = new Message();Bundle bl = new Bundle(); bl.putFloat("u", values[0]); ml.setData(bl);setLabelOrientation.sendMessageDelayed(ml,pRate);}
    					}
    						
    						//if(smoothtext > SystemClock.uptimeMillis()){return;}
    						//smoothtext = SystemClock.uptimeMillis() + 1750;
    						//String cn = "("+event.sensor.getName()+"+"+values.length+")"+(int)values[0]+(values.length>0?"\n"+(int)values[1]:"")+""+(values.length>1?"\n"+(int)values[2]:"");//+""+(values.length>2?"\n"+(int)values[3]:"")+"";
    						//{Message ml = new Message(); Bundle bl = new Bundle(); bl.putInt("id", bdl.getInt("morsv"));bl.putString("text", cn);bl.putInt("color", Color.argb(200, 250, 250, 255));ml.setData(bl);setText.sendMessageDelayed(ml,pRate);}
    						//{Message ml = new Message(); Bundle bl = new Bundle(); bl.putInt("id", bdl.getInt("morsv"));bl.putString("text", cn);bl.putInt("color", Color.argb(240, 250, 250, 255));ml.setData(bl);setText.sendMessageDelayed(ml,pRate<100?pRate*4:(int)(pRate+(pRate/4)));}
    						
    						
    						
    					};
    					{Message ml = new Message(); Bundle bl = new Bundle(); bl.putString("text", "Getting Sensor Provider"); ml.setData(bl); logoly.sendMessage(ml);}
    		        	
    					SensorManager sm = null;try{ sm = (SensorManager) mCtx.getSystemService(SENSOR_SERVICE);}finally{}
    		        	
    					{Message ml = new Message(); Bundle bl = new Bundle(); bl.putString("text", "Registering Sensor Service"); ml.setData(bl); logoly.sendMessage(ml);}
    		        	sm.registerListener(or, sm.getDefaultSensor(SensorManager.SENSOR_ORIENTATION) , SensorManager.SENSOR_DELAY_GAME );
    		        	{Message ml = new Message(); Bundle bl = new Bundle(); bl.putString("text", "Wave Sensor Service"); ml.setData(bl); logoly.sendMessage(ml);}
    		        	//easyStatus("Wave Ready");
    					/*
    		        	try {
    						 
    						for(;;Thread.sleep(1750)){if(!mReg.contains("wave")){easyStatus("Wave Off");break;}if(getListView().isShown() || getListView().hasFocus()){}else{Log.e(G,"List isn't shown and nofocus, sensor watch close");wayGo.sendEmptyMessage(2);break;}}
    					}catch(InterruptedException e){Thread.interrupted();}
    						//*/
    				
    				
    			}
    		};
    		tx.start();
    	}	
    };

    
	Handler h1 = new Handler(){
    	public void handleMessage(Message msg){
    		Bundle bdl = msg.getData();
    		Thread tx = new Thread(){
    			public void run(){
    				
    			}
    		};
    		tx.start();
    	}	
    };
    
    Handler takescreen = new Handler(){
    	public void handleMessage(Message msg){
    		RelativeLayout ba = (RelativeLayout) findViewById(R.id.base);
			ba.setDrawingCacheEnabled(true);
			ba.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
			Bitmap m = ba.getDrawingCache();
			String filename = System.currentTimeMillis()+"_"+August.title.replaceAll(" ", "") + ".png";
			easyStatus("Snapshot Saved");
			
			FileOutputStream fs = null;
			
			File filea = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "wave");
			File file = new File(filea.getAbsolutePath(), filename);
			
			try {
				fs = new FileOutputStream(file);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			//Drawable db = Drawable.createFromPath(file.getAbsolutePath()+"/"+filename);
			
			m.compress(CompressFormat.PNG, 0, fs);
			
			try {
                fs.flush();
            } catch (IOException e1) {
                    Log.e(G, "ComputerStart() 1528 fs.flush() failed");
                    e1.printStackTrace();
            }
            try {
                    fs.close();
            } catch (IOException e1) {
                    Log.e(G, "ComputerStart() 1534 fs.close() failed");
                    e1.printStackTrace();
            }
		}  
	};
    
	Handler beatimg = new Handler(){
	long oof = 2;
		long[] idc = new long[1000];
		long[] idmax = new long[1000];
		int cid = -2;
		public void handleMessage(Message msg){
		Bundle bdl = msg.getData();

		int id = bdl.getInt("id",-2);
		long pulse = bdl.getLong("ms",-2);
		long max = bdl.getLong("max",-2);
		//long max = bdl.getLong("max",26);
		if(id == -2 || oof +20000 > System.currentTimeMillis()){Log.e(G,"skip beat");return;}
		
		ImageView i5 = null;try {i5 = (ImageView) findViewById(id);}catch(ClassCastException ec){Log.e(G,"beatimg("+id+")");ec.printStackTrace();}
		
		if(i5==null){Log.e(G,"skip ibeat");return;}
		RelativeLayout r = (RelativeLayout) i5.getParent();//or i6
		TextView g9 = null;
		LinearLayout wrl = null;
		if(r.getChildCount() > 6){
		
		//wrl = (LinearLayout) r.getChildAt(0);
		g9 = (TextView) r.getChildAt(4); 
		
		
		}else {
			RelativeLayout r3 = (RelativeLayout) r.getChildAt(2);
			//Log.i(G,"beatimg(r3 "+r3.getChildCount()+")");
			//RelativeLayout r32 = (RelativeLayout) r.getChildAt(2);
			
			//wrl = (LinearLayout) r3.getChildAt(0);
			//RelativeLayout rz = (RelativeLayout) wrl.getChildAt(1);
			//LinearLayout zl2 = (LinearLayout) rz.getChildAt(0);
			//RelativeLayout rz2 = (RelativeLayout) zl2.getChildAt(0);
			g9 = (TextView) findViewById(headerText); 
				
		}

		
		//r(
		//  clicker
		//	t
		//:	r3(
		//		wrl(
		//			r3(i:t:i)
		//:			zr["_v"](zl2(zr2(i6:g6[responsems"_v3"]:g7[hostname"_v4"]:g8[ipaddress:ipport"_v6"]:g9[errortype]:title["_v2"]:source:dest)))
		//:			r3(i:t:i)
		//)
		//:		pear8(i:t:i)
		//:		pear8(i:t:i)
		//:		i5[beatimg]
		//:		ex[HPAGE]()
		//:		g5[responsems]
		//))
		int width = bdl.getInt("width",i5.getWidth());int height = bdl.getInt("height",i5.getHeight());
		//Log.i(G,"beatimg("+id+","+pulse+") "+i5.getWidth()+"x"+i5.getHeight()+"");
		//if(max == -2){max = height;}
		//if(pulse > max && height < 300){max = 300;}
		
		if(height < 10){height = 30;}
		if(width < 10){width = 75;}
		
		for(cid = 0; cid < idc.length;cid++ ){
			//Log.i(G,"cid " + idc[cid]);
			if(idc[cid] == 0){break;}
			if(idc[cid] == id){break;}
		}
		if(idc[cid] == 0){idc[cid] = id;}
		max = max>pulse?max:pulse;
		max = idmax[cid]>max?idmax[cid]:max;
		if(max < height){max = height;}
		idmax[cid] = max;
		
		
		Bitmap b3 = null;
		try{
		b3 = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		}catch(OutOfMemoryError eom){Log.e(G,"Out of Memory during gen of beatimg.");oof = System.currentTimeMillis();return;}
		Paint p2 = new Paint();//p2.setColor(coloroff);//p2.setStrokeWidth(3);
		p2.setDither(true);
        Canvas c = new Canvas(b3);
        c.drawBitmap(b3, 0, 0, p2);
        
        if(max < pulse){max = pulse;}
        
		Paint cp2 = new Paint();cp2.setColor(Color.argb(250,20,80,111));cp2.setStrokeWidth((float)3.0);
        float sa = 1;  float ea = 1; float eb = (float)height - ((( (float)pulse/(float)max) )*(float)height);
        float sb = height;eb = height-eb;
        if(pulse < 0){cp2.setColor(Color.argb(250,111,111,60));
        
        if(g9 != null ){
        SharedPreferences mReg = getSharedPreferences("Preferences", MODE_WORLD_WRITEABLE);
        
        g9.setText(mReg.getString("errortype","gold error"));
        }
        
        
		}
        c.drawLine(sa,sb,ea,eb,cp2);
        
		cp2.setStrokeWidth((float)1.0);
		sa=3;sb=height;ea=3;cp2.setColor(Color.argb(240,90,10,30));eb = (float)height;
		c.drawLine(sa,sb,ea,(float)1.0,cp2);
        

        Drawable d5 = i5.getDrawable();
        d5.setBounds(4,0,width,height);d5.setAlpha(252);
        d5.draw(c);
		
        i5.setImageBitmap(b3);
        
        
	}};
	
	
	
	
	
	
	
	Handler pageimg = new Handler(){
		public void handleMessage(Message msg){
			Bundle bdl = msg.getData();
			
			int viewid = bdl.getInt("view");
			int page = bdl.getInt("page");
			
			
			
			Log.w(G, "pageimg request " + viewid + " page " + page);
			
			ImageView iv = null; try{iv = (ImageView) findViewById(viewid);
			}catch( ClassCastException ec){Log.e(G,"pageimg cast exc");return;}
			//ShapeDrawable xr = new ShapeDrawable();
			
			
			//int HEIGHT = 140;//d.getMinimumHeight();
			//int WIDTH = 70;//d.getMinimumWidth();
			//int STRIDE = WIDTH + 20;
			//Log.w(G,"height " + HEIGHT + " width " + WIDTH + " stride " + STRIDE);
			
			Bitmap b3 = null;
			try{
			b3 = Bitmap.createBitmap(70, 120, Bitmap.Config.ARGB_8888);
			}catch(OutOfMemoryError eom){Log.e(G,"Out of Memory during gen of pageimg.");return;}
			//Drawable db = Drawable.createFromPath(filename);
			
			
			/*/
			int[] colors = new int[STRIDE * HEIGHT];
	        for (int y = 0; y < HEIGHT; y++) {
	            for (int x = 0; x < WIDTH; x++) {
	                int r = x * 255 / (WIDTH - 1);
	                int g = y * 255 / (HEIGHT - 1);
	                int b = 255 - Math.min(r, g);
	                int a = Math.max(r, g);
	                colors[y * STRIDE + x] = (a << 24) | (r << 16) | (g << 8) | b;
	            }
	        }
			b3.setPixels(colors, 0, STRIDE, 0, 0, WIDTH, HEIGHT);
			//*/
			
			
            Paint p2 = new Paint();//p2.setColor(coloroff);//p2.setStrokeWidth(3);
			p2.setDither(true);
            
			
			Canvas c = new Canvas(b3);
			//c.setBitmap(b3);
			//c.translate(10, 10);
			//c.drawColor(Color.LTGRAY);


			Drawable d = mCtx.getResources().getDrawable(R.drawable.place);
			d.setBounds(0,0,70,120);
			d.setAlpha(120);
			d.draw(c);
			
			c.drawBitmap(b3, 0, 0, p2);
            //c.drawBitmap(colors, 0, STRIDE, 0, 0, WIDTH, HEIGHT, false, p2);
			

			Paint p = new Paint(); p.setColor(colorm1); p.setStrokeWidth((float)2);
			Paint ps = new Paint(); ps.setColor(colorm2); p.setStrokeWidth((float)2);
			Paint pr = new Paint(); pr.setColor(colorm3); pr.setStrokeWidth((float)2);
			Paint pg = new Paint(); pg.setColor(Color.argb(255, 0, 90, 170)); pg.setStrokeWidth(0);
			Paint pgl = new Paint(); pgl.setColor(Color.argb(255, 120, 20, 120)); pgl.setStrokeWidth((float)2);
			int offx = 0;
			//int offy = 10;
			
			Uri contentpath = Uri.withAppendedPath(DataProvider.CONTENT_URI, "moment");
	        Cursor cx = SqliteWrapper.query(mCtx, mCtx.getContentResolver(), contentpath, new String[] {"_id","status"}, August.loadlistSQL, null, August.loadlistSort+" limit "+(page*pagesize)+","+pagesize);
 			int sn = 2;int xc = 2;
			if( cx != null){if( cx.moveToFirst() ){
			
				//for(int ci = cx.getCount()-1; ci >= 0 ; ci--){
				for(int ci = 0; ci < cx.getCount(); ci++){
					
					cx.moveToPosition(ci);
					sn=cx.getInt(1);
					
					xc = (ci*2)+offx;
					sn -= 100;
					if(sn == 0){//1 seen
						//c.drawLine((float)10.0, (float)xc, (float)30.0, (float)xc, p);
						c.drawLine((float)1.0, (float)xc, (float)69.0, (float)xc, p);
					}else if(sn == 1){//2
						//c.drawLine((float)10.0, (float)xc, (float)20.0, (float)xc, p);
						//c.drawLine((float)50.0, (float)xc, (float)60.0, (float)xc, p);
						c.drawLine((float)1.0, (float)xc, (float)69.0, (float)xc, ps);
					}else if(sn >= 2){//3
						//c.drawLine((float)10.0, (float)xc, (float)20.0, (float)xc, pr);
						//c.drawLine((float)30.0, (float)xc, (float)40.0, (float)xc, pr);
						//c.drawLine((float)50.0, (float)xc, (float)60.0, (float)xc, pr);
						c.drawLine((float)1.0, (float)xc, (float)69.0, (float)xc, pr);
					}else if(sn < 0){//unseen
						c.drawLine((float)1.0, (float)xc, (float)69.0, (float)xc, pg);
					}else{
						c.drawLine((float)1.0, (float)xc, (float)69.0, (float)xc, pgl);
					}
				}
			
			 } cx.close();}
			
            
			
            /*
            c.drawLine((float)15.0, (float)4.0, (float)60.0, (float)4.0, p);
			c.drawLine((float)15.0, (float)6.0, (float)60.0, (float)6.0, p);
			
			//three
			c.drawLine((float)15.0, (float)8.0, (float)25.0, (float)8.0, p);
			c.drawLine((float)30.0, (float)8.0, (float)45.0, (float)8.0, p);
			c.drawLine((float)50.0, (float)8.0, (float)60.0, (float)8.0, p);
			
			c.drawLine((float)15.0, (float)10, (float)25.0, (float)10.0, p);
			c.drawLine((float)50.0, (float)10, (float)60.0, (float)10.0, p);
			
			for(int i = 12; i <= 60; i+=2){
				c.drawLine((float)15.0, (float)i, (float)60.0, (float)i, p);
			}
			//*/
			//c.translate(0, 60);
			
            //c.save();
			
			//compress png
			//ByteArrayOutputStream os = new ByteArrayOutputStream();
			//b3.compress(Bitmap.CompressFormat.PNG, 0, os);
            //byte[] array = os.toByteArray();
            //Bitmap bn = BitmapFactory.decodeByteArray(array, 0, array.length);
			
			iv.setImageBitmap(b3);
			//*/
		}
	};
	
	
    
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
    
    private Cursor lCursor;
	private int headerText = 0;
	private int footerText = 0;
	static public int pagesize = 60;
	public int pagestart = 0;
	int[] mPagev;int mPagen;int[] mBcolor;
	Handler pushlist = new Handler(){
		int pc = pagesize;int pcs = 0;
		public void handleMessage(Message msg){
			int topp = getListView().getFirstVisiblePosition();
			//Log.w(G,"topat " + topp);
			
			
			//RelativeLayout tv = (RelativeLayout) getListView().
	        //if(tv == null){return;}
	        int lpage = mPagen;
			int gopage = msg.what;
	        Bundle bdl = msg.getData();
	        if(bdl.containsKey("long2") || gopage == -3){pc = 0;mPagen = 0;pcs=0;}
			if(gopage == -1){pc -= pagesize;pc--; if(pc < 1){pc = pagesize; pcs--; if(pcs < 0){pcs = 0; pc = 0;}}}
	        if(bdl.containsKey("long") || gopage == -2){pc += pagesize + 1;mPagen = pc/pagesize;}
			//pcs = pc - pagesize;if(pcs < 0){pcs = 0;}
			
			if(gopage >= 0){
				{Bundle bl = new Bundle(); bl.putInt("view", mPagev[gopage]); bl.putInt("page", gopage);Message ml = new Message(); ml.setData(bl); pageimg.sendMessageDelayed(ml,100);}
				//easyStatus(""+gopage);
				
				mPagen = gopage;pagestart = gopage * pagesize; pc = pagesize;pcs = pagestart;
				//mPagen = mTotal/pagesize;mPagen++;
			}
			if(mPagev != null && lpage < mPagev.length && pcs >1){ImageView li = (ImageView) findViewById(mPagev[lpage]);if(li!=null){li.requestFocusFromTouch();}}
			
			String[] columns = new String[] {"max(_id) as _id","title","strftime('"+August.listdate+"',max(published)) as published","max(status) as status","images"};
			lCursor = SqliteWrapper.query(mCtx, getContentResolver(), Uri.withAppendedPath(DataProvider.CONTENT_URI,"moment"), 
	        		columns,
	        		August.loadlistSQL, // Future configurable time to expire seen and unread
	        		null, 
	        		August.loadlistSort + " limit "+(pcs<=0?"":pcs+",")+""+pc);		
			//if(pc == 0){{Message ml = new Message(); Bundle bl = new Bundle(); bl.putInt("id", headerText); bl.putString("text", ""); ml.setData(bl); setText.sendMessageDelayed(ml,pRate);}}
			//if(pc == 0){pc = 1;}else if(pc == 1){pc = 3;}else{pc+=pagesize;}
			startManagingCursor(lCursor);
			String[] from = new String[]{ "title","published","status", "_id","images"};
			int[] to = new int[]{R.id.listrow_title, R.id.listrow_published, R.id.listrow_opened, R.id.listrow_momenti,R.id.listrow_images};
	        SimpleCursorAdapter entries = new SimpleCursorAdapter(mCtx, R.layout.listrow, lCursor, from, to);
	        
	        mBcolor = new int[lCursor.getCount()*2];
	        //getListView().setStackFromBottom(true);
	        
	        //int tm = getListView().getScrollY();
	        setListAdapter(entries);
	        if(gopage <= -2){
	        	getListView().setSelection(topp);
	        }
	        
	        //getListView().scrollTo(0, tm);
	        //getListView().setSelectionFromTop(topp, 10);
			//getListView().setSelection(getListView().getChildCount()-1);
			//getListView().getSelectedView().requestFocusFromTouch();
		
	       
		}
	};
	//SharedPreferences mReg;
	
	
	
	OnTouchListener listtouch = new OnTouchListener(){public boolean onTouch(View v, MotionEvent ev) {if(ev.getAction() == MotionEvent.ACTION_DOWN){RelativeLayout rf = (RelativeLayout) v;rf.setBackgroundColor(coloron);}else if(ev.getAction() == MotionEvent.ACTION_UP){RelativeLayout rf = (RelativeLayout) v;rf.setBackgroundColor(coloroff);}return false;}};
	OnFocusChangeListener listfocus = new OnFocusChangeListener(){public void onFocusChange(View v, boolean has){if(has){v.setBackgroundColor(coloron);}else{v.setBackgroundColor(coloroff);}}};
	ScrollView si;
	RelativeLayout pagesr2;
	Handler siscroll = new Handler(){
		public void handleMessage(Message msg){
			int foo = msg.what;//si.getScrollX()+
			Log.w(G,"smooth " + foo);
			si.smoothScrollBy(foo, 0);
		}
	};	
	
	Handler footerGen = new Handler(){
		public void handleMessage(Message msg){
			SharedPreferences mReg = getSharedPreferences("Preferences", MODE_WORLD_WRITEABLE);
			Bundle bdl = msg.getData();
	
			 {
			        // FOOTER
			        
			        
			        
			        RelativeLayout l1 = (RelativeLayout) findViewById(msg.what);
			        
			        //l1.setBackgroundColor(coloroff);//Color.argb(180, 0, 180, 95));
			        l1.setGravity(Gravity.BOTTOM);//android:listSelector="#FF303060"
			        l1.setPadding(0, 0, 0, 0);
			        l1.setOnClickListener(new OnClickListener(){public void onClick(View v){pushlist.sendEmptyMessage(-2);}});
			        l1.setId((int)SystemClock.uptimeMillis());
			        
			        {Bundle bl = new Bundle(); bl.putInt("id", l1.getId()); bl.putInt("color",colorm3);Message ml = new Message(); ml.setData(bl);rlColorBg.sendMessage(ml);}
	    			{Bundle bl = new Bundle(); bl.putInt("id", l1.getId()); bl.putInt("color",colorm2);Message ml = new Message(); ml.setData(bl);rlColorBg.sendMessageDelayed(ml,1000);}
	    			{Bundle bl = new Bundle(); bl.putInt("id", l1.getId()); bl.putInt("color",colorm3);Message ml = new Message(); ml.setData(bl);rlColorBg.sendMessageDelayed(ml,1200);}
	    			{Bundle bl = new Bundle(); bl.putInt("id", l1.getId()); bl.putInt("color",colorm2);Message ml = new Message(); ml.setData(bl);rlColorBg.sendMessageDelayed(ml,1300);}
	    			{Bundle bl = new Bundle(); bl.putInt("id", l1.getId()); bl.putInt("color",colorm3);Message ml = new Message(); ml.setData(bl);rlColorBg.sendMessageDelayed(ml,1400);}
	    			{Bundle bl = new Bundle(); bl.putInt("id", l1.getId()); bl.putInt("color",colorm2);Message ml = new Message(); ml.setData(bl);rlColorBg.sendMessageDelayed(ml,1500);}
	    			
			        l1.setOnLongClickListener(new OnLongClickListener(){public boolean onLongClick(View v){Message ml = new Message(); Bundle bl = new Bundle();if(getListView().getCount() <= getListView().getHeaderViewsCount() + getListView().getFooterViewsCount()){wayGo.sendEmptyMessage(2);bl.putBoolean("long2",true);}else if(getListView().getCount() - (getListView().getHeaderViewsCount() + getListView().getFooterViewsCount()) > 3){bl.putBoolean("long2",true);}else{bl.putBoolean("long2",true);}ml.setData(bl);pushlist.sendMessage(ml);return false;}});
			        
			        //l1.setFocusable(true);
			        //l1.setOnTouchListener(new OnTouchListener(){public boolean onTouch(View v, MotionEvent ev) {if(ev.getAction() == MotionEvent.ACTION_DOWN){RelativeLayout rf = (RelativeLayout) v;rf.setBackgroundColor(coloron);}else if(ev.getAction() == MotionEvent.ACTION_UP){RelativeLayout rf = (RelativeLayout) v;rf.setBackgroundColor(coloroff);}else if(ev.getAction() == MotionEvent.ACTION_OUTSIDE || ev.getAction() == MotionEvent.ACTION_CANCEL){RelativeLayout rf = (RelativeLayout) v;rf.setBackgroundColor(colorred);}return false;}});
			        
			        TextView t1 = new TextView(mCtx);
			        RelativeLayout.LayoutParams t1r = new RelativeLayout.LayoutParams(-1,-2);
			        t1.setLayoutParams(t1r);
			        t1.setId((int)SystemClock.uptimeMillis());
			        t1.setTextSize((float)12);
			        t1.setGravity(Gravity.CENTER_VERTICAL);
			        t1.setTextColor(textcolor);
			        t1.setGravity(Gravity.CENTER);
			        t1.setPadding(7, 13, 7, 13);
			        t1.setText("");
			        /*ImageView i1 = new ImageView(mCtx);
			        i1.setLayoutParams(new RelativeLayout.LayoutParams(-2,-2));
			        i1.setScaleType(ScaleType.MATRIX);i1.setImageResource(R.drawable.flatpearl);
			        l1.addView(i1);//*/
			        /*//*/
			        
			        
			        l1.addView(t1);
			        { // FLAT PEARL
			        	
			        	ImageView i3 = new ImageView(mCtx);
					        RelativeLayout.LayoutParams rp = new RelativeLayout.LayoutParams(-2,-1);
					        LinearLayout bi1 = new LinearLayout(mCtx);
					        bi1.setGravity(Gravity.BOTTOM);
					        bi1.setLayoutParams(rp);
					        i3.setLayoutParams(new LinearLayout.LayoutParams(-2,-2));
					        i3.setPadding(0, 0, 0, 0);
					        i3.setScaleType(ScaleType.MATRIX);i3.setImageResource(R.drawable.flatpearl3);
					        bi1.addView(i3,0);
					        l1.addView(bi1,0);					
					}
			        footerText = t1.getId();
			        
			        // ENDLESS SCROLLINGISH
			        loadmore.sendEmptyMessageDelayed((int)SystemClock.uptimeMillis(),50);
			        
			 }
			
		}
	};
	
	Handler headerGen = new Handler(){
		public void handleMessage(Message msg){
			SharedPreferences mReg = getSharedPreferences("Preferences", MODE_WORLD_WRITEABLE);
			Bundle bdl = msg.getData();
			
			
			//r(
			//  clicker
			//	t
			//:	r3(
			//		wrl(
			//			r3(i:t:i)
			//:			zr["_v"](zl2(zr2(i6:g6[responsems"_v3"]:g7[hostname"_v4"]:g8[ipaddress:ipport"_v6"]:title["_v2"]:source:dest)))
			//:			r3(i:t:i)
			//)
			//:		pear8(i:t:i)
			//:		pear8(i:t:i)
			//:		i5[beatimg]
			//:		ex[HPAGE]()
			//:		g5[responsems]
			//))
			
			
			 // HEADER

	        
	        //RelativeLayout l2 = new RelativeLayout(mCtx); 
	        //LinearLayout.LayoutParams b2 = new LinearLayout.LayoutParams(-1, -1);
	        //b2.weight = 1;
	        //l2.setLayoutParams(b2);
	        /*/
	        l2.setId((int)SystemClock.uptimeMillis());
	        recentid = l2.getId();
	        l2.setBackgroundColor(coloroff);//Color.argb(190, 0, 180, 95));
	        //l2.setBackgroundColor(Color.argb(200, 80, 80, 80));
	        l2.setPadding(0, 0, 0, 0);
	        l2.setHapticFeedbackEnabled(true);
	        l2.setClickable(true);l2.setFocusable(true);
	        l2.setOnTouchListener(new OnTouchListener(){public boolean onTouch(View v, MotionEvent ev) {if(ev.getAction() == MotionEvent.ACTION_DOWN){RelativeLayout rf = (RelativeLayout) v;rf.setBackgroundColor(coloron);}else if(ev.getAction() == MotionEvent.ACTION_UP){RelativeLayout rf = (RelativeLayout) v;rf.setBackgroundColor(coloroff);}else if(ev.getAction() == MotionEvent.ACTION_OUTSIDE || ev.getAction() == MotionEvent.ACTION_CANCEL){RelativeLayout rf = (RelativeLayout) v;rf.setBackgroundColor(colorred);}return false;}});
	        l2.setOnFocusChangeListener(new OnFocusChangeListener(){public void onFocusChange(View v, boolean has){RelativeLayout lv = (RelativeLayout)v;if(has){lv.setBackgroundColor(coloron);}else{lv.setBackgroundColor(coloroff);}}});
	        //*/
	        //rl2.setNextFocusUpId(l2.getId());
	        //rl2.setOnFocusChangeListener(new OnFocusChangeListener(){public void onFocusChange(View v, boolean has){LinearLayout lv = (LinearLayout)v;if(has){lv.requestFocus(LinearLayout.FOCUS_UP);}else{}}});
	        

        
        
        { // Recent
        	
	        RelativeLayout rl2 = (RelativeLayout) findViewById(msg.what);
	        if(rl2 == null){Log.e(G,"header missing "+msg.what);return;}
	        TextView t2 = new TextView(mCtx);
	        t2.setLayoutParams(new RelativeLayout.LayoutParams(-2,-2));
	        t2.setTextColor(textcolor);
	        t2.setId(uniq++);
	        t2.setMinimumHeight(59);
	        t2.setPadding(13, 7, 13, 7);
	        headerText = t2.getId();
	        //SharedPreferences mReg = getSharedPreferences("Preferences", MODE_WORLD_WRITEABLE);
			
	        // Recent
	        String sourcetitle = mReg.getString("sourcetitle","error889");
	        Uri contentpath = Uri.withAppendedPath(DataProvider.CONTENT_URI, "moment");
	        Cursor cx = SqliteWrapper.query(mCtx, mCtx.getContentResolver(), contentpath, new String[] {"count(*)"}, August.todayCountSQL, null, null);
 			int today = 0;
			if( cx != null){if( cx.moveToFirst() ){ if( cx.getInt(0) > 0){today=cx.getInt(0);}; } cx.close();}
			String recent = "";//
 			Date d = new Date(mReg.getLong("bucket_saved", 0));
	        recent = (d.getYear()+1900)+"/"+d.getMonth()+"/"+d.getDate()+" "+(d.getHours() > 12?d.getHours()-12:d.getHours()) + ":" + (d.getMinutes()<10?"0":"") + d.getMinutes();
	        t2.setTextSize((float)22);
	        
	        sourcetitle = Uri.decode(sourcetitle).replaceAll("<[a-zA-Z].*?>","");
	        if(sourcetitle.length() > 0){t2.setText(sourcetitle+" \n"+recent+"\n"+mTotal);}
	        
	        // Flatpearl
	        ImageView i3 = new ImageView(mCtx);
	        i3.setLayoutParams(new RelativeLayout.LayoutParams(-2,-2));
	        i3.setScaleType(ScaleType.MATRIX);i3.setImageResource(R.drawable.flatpearl);
	        
	        rl2.addView(i3);
	        rl2.addView(t2);
	        
        }
			
		}
	};
	
	boolean movingtouch = false;
	Handler pagesGen = new Handler(){
		public void handleMessage(Message msg){
			SharedPreferences mReg = getSharedPreferences("Preferences", MODE_WORLD_WRITEABLE);
			Bundle bdl = msg.getData();
			{ // Pages
	        	
				
		        
		        
		        //pagesr2.setBackgroundColor(Color.argb(255, 40, 40, 120));
		        
		        //pagesr2.addView(t2);

		        // SQL
		        //Uri contentpath = Uri.withAppendedPath(DataProvider.CONTENT_URI, "moment");
		        //Cursor cx = SqliteWrapper.query(mCtx, mCtx.getContentResolver(), contentpath, new String[] {"count(*)"}, "status > -10", null, null);
	 			//int today = 0;
				//if( cx != null){if( cx.moveToFirst() ){ if( cx.getInt(0) > 0){today=cx.getInt(0);}; } cx.close();}
		        //t2.setText(mTotal + " ");
		        
		        // Flatpearl3
		        /*/
		        ImageView i4 = new ImageView(mCtx);
		        i4.setLayoutParams(bli);
		        i4.setScaleType(ScaleType.MATRIX);i4.setImageResource(R.drawable.flatpearl);
		        pagesr2.addView(i4);
		        //*/
		        
		       
		        
		        
		        
		        boolean hiu = true;
		        {
		        	
		        	
		        	LinearLayout bi1 = null;
		        	int im = mTotal/pagesize;im++;
			        if(mPagev != null && mPagev.length > 0){
			        	int[] xw = mPagev; 
			        	mPagev = new int[im];
			        	for(int i = 0; i < xw.length; i++){mPagev[i] = xw[i];}
			        	bi1 = (LinearLayout) si.getChildAt(0);
			        	ScrollView.LayoutParams sp = new ScrollView.LayoutParams((71*im)+240,-1);
			        	bi1.setLayoutParams(sp);
			        }else{
			        	hiu = false;
			        	mPagev = new int[im];
			        
			        	RelativeLayout.LayoutParams bli = new RelativeLayout.LayoutParams(-2,-1);
				        //bli.addRule(RelativeLayout.ALIGN_BOTTOM,pagesr2.getId());
				        
				        TextView t2 = new TextView(mCtx);
				        t2.setLayoutParams(bli);
				        t2.setTextColor(textcolor);
				        t2.setId(uniq++);
				        t2.setPadding(13, 7, 13, 7);
				        t2.setTextSize((float)22);

			        	RelativeLayout.LayoutParams rp = new RelativeLayout.LayoutParams(-2,-1);
			        	//rp.setMargins(0,10,0,10);
			        	si = new ScrollView(mCtx);
			        	si.setId((int)SystemClock.uptimeMillis());
			        	si.setLayoutParams(rp);
			        	si.setScrollContainer(true);
			        	si.setHorizontalScrollBarEnabled(true);
			        	//si.setHorizontalFadingEdgeEnabled(true);
			        	si.setScrollBarStyle(si.SCROLLBARS_INSIDE_INSET);
			        	//si.setSmoothScrollingEnabled(true);
			        	//si.setEnabled(true);
			        	//si.setFocusable(true);
			        	//si.setClickable(true);
			       
			        	 ScrollView.LayoutParams sp = new ScrollView.LayoutParams((71*im)+240,-1);
				        	bi1 = new LinearLayout(mCtx);
					        //bi1.setBackgroundColor(Color.argb(150,40,40,150));
					        bi1.setLayoutParams(sp);	
					        si.addView(bi1);
					        
					        {ImageView i2 = new ImageView(mCtx);
					        LinearLayout.LayoutParams lm = new LinearLayout.LayoutParams(-2,-2);
					        lm.setMargins(90+(im==1?0:(im==2?40:80))*-1,10,0,10);
					        i2.setLayoutParams(lm);
					        i2.setScaleType(ScaleType.MATRIX);i2.setImageResource(R.drawable.horbar);
					        bi1.addView(i2);}
					        
					        {ImageView i2 = new ImageView(mCtx);
					        LinearLayout.LayoutParams lm = new LinearLayout.LayoutParams(-2,-2);
					        lm.setMargins(0,10,0,10);
					        i2.setLayoutParams(lm);
					        i2.setScaleType(ScaleType.MATRIX);i2.setImageResource(R.drawable.holbar);
					        bi1.addView(i2);}
					        
					        //bi1.setPadding(10,10,10,10);
					        
					        bi1.setFocusable(true);
					        //bi1.setClickable(true);
					        bi1.setDescendantFocusability(bi1.FOCUS_AFTER_DESCENDANTS);
					        //TextView edgetop = (TextView) findViewById(R.id.edgetop);
					        bi1.setOnFocusChangeListener(new OnFocusChangeListener(){public void onFocusChange(View v, boolean has){ Log.w(G,"bi1 focus " + has); LinearLayout lb = (LinearLayout) v; if(has){lb.setBackgroundColor(coloron);}else{lb.setBackgroundColor(colorm2);}  }});
					        bi1.setOrientation(LinearLayout.HORIZONTAL);
			        	
			        }
		        	
			       
			        
			        int xbb = 1;
			        
			        LinearLayout.LayoutParams lx2 = new LinearLayout.LayoutParams(70,140);lx2.setMargins((120+(im==1?0:(im==2?60:100))*-1),10,0,10);
			        LinearLayout.LayoutParams lx = new LinearLayout.LayoutParams(70,140);lx.setMargins(0,10,0,10);
			        
			        for(int ix = 0; ix < im; ix++){
			        	if(mPagev[ix] > 0){
			        		{Bundle bl = new Bundle(); bl.putInt("view", mPagev[ix]); bl.putInt("page", ix);Message ml = new Message(); ml.setData(bl); pageimg.sendMessageDelayed(ml,75);}
			        		continue;
			        	}
			        	ImageView iv = new ImageView(mCtx);
			        	//if(ix == (im-1)){
			        		//iv.setLayoutParams(lx2);
			        	//}else{
			        		iv.setLayoutParams(lx);
			        	//}
			        	iv.setImageResource(R.drawable.place);
			        	iv.setScaleType(ScaleType.FIT_XY);
			        	//iv.setPadding(5,5,5,5);
			        	iv.setBackgroundColor(Color.BLACK);
			        	iv.setClickable(true);
			        	iv.setFocusable(true);iv.setFocusableInTouchMode(true);
			        	iv.setId((int)SystemClock.uptimeMillis());
			        	iv.setPadding(0,10,0,10);
			        	//iv.setNextFocusDownId(bi1.getId());iv.setNextFocusUpId(bi1.getId());
			        	final int myix = ix;
			        	iv.setOnFocusChangeListener(new OnFocusChangeListener(){
			        		long laston = -1;
			        		public void onFocusChange(View v, boolean has){ 
			        			ImageView iv = (ImageView) v; 
			        			//Log.w(G,"pfocus " + myix + " " + mPagen + " " + has);
			        			if(has){  
			        				//Log.w(G,"pfocus " + myix + " " + mPagen);
			        				
			        				if(laston == -1){iv.requestFocusFromTouch();}
			        				
			        				laston = SystemClock.uptimeMillis();
			        				if( myix == mPagen ){
			        					iv.setBackgroundColor(colorpageon);
			        				}else{
			        					iv.setBackgroundColor(coloron);
			        				}
			        				//Log.w(G,iv.getLeft()+" boundaries "+iv.getRight() + " from " + si.getWidth() + " x " + si.getScrollX() + " within " + (si.getScrollX()+si.getWidth())); 
			        		
			        				
				        			if(iv.getRight() > (si.getScrollX()+si.getWidth()+iv.getWidth()) ){
				        				siscroll.sendEmptyMessage(iv.getWidth());//iv.getRight()-si.getScrollX());
				        				//iv.requestFocusFromTouch();
				        				//iv.requestFocus(ImageView.FOCUS_RIGHT);
				        			}else if(iv.getLeft() < si.getScrollX() ){ 
				        				siscroll.sendEmptyMessage(-2*iv.getWidth());//iv.getLeft()-si.getScrollX());
				        				//iv.requestFocusFromTouch();
				        				//iv.requestFocus(ImageView.FOCUS_LEFT);
				        			}
				        			//if(laston < 10){iv.requestFocusFromTouch();laston++;}
			        			
			        			}else{ 
			        				//Log.w(G,"p losing focus");
			        				
			        				//iv.clearFocus();
			        				//if(laston < 10){
			        					//iv.requestFocusFromTouch();
			        					//iv.setBackgroundColor(colorred);
			        				//}else{
			        					//iv.setBackgroundColor(coloroff); 
			        					if( myix == mPagen ){
				        					iv.setBackgroundColor(colorpagein);
				        				}else{
				        					iv.setBackgroundColor(colorpageoff);
				        				}
			        					//}
			        			}
			        		
			        			iv.invalidate();
			        		
			        			
			        		}});
			        	
			        	
			        	iv.setOnTouchListener(new OnTouchListener(){
			        		float hx = 2;float mx = 2;int leri = 2;int d;float hs = 2;int ld = 2;
			        		public boolean onTouch(View v, MotionEvent event) {
			        			 //Log.w(G,"Action " + event.getAction() + " " + event.getPressure() + " h("+event.getHistorySize()+") " + movingtouch + " dt("+event.getDownTime()+") " + SystemClock.uptimeMillis() + " " + (SystemClock.uptimeMillis() - event.getDownTime())); 
			        			 
			        			// if(event.getAction() == MotionEvent.ACTION_MOVE && event.getHistorySize() > 1 && (SystemClock.uptimeMillis() - event.getDownTime()) > 200){//&& ((event.getHistorySize() > 1 && (movingtouch || event.getPressure() > 0.2 )) || event.getHistorySize() > 2 ) && event.getHistorySize() < 6){
			        				 //movingtouch = true;
			        				 //float x = event.getX();
			        				 //float lx = event.getHistoricalX(1);
			        				 //int d = (int) ((lx-x)*2);
			        				 
		        				 if(event.getAction() == MotionEvent.ACTION_MOVE){
		        					
		        					 
		        					 
		        						mx = event.getX();
		        						
		        						if(event.getHistorySize() > 0){hx = event.getHistoricalX(1);}
		        						//if(mx < hx){hs = hx; hx = mx; mx = hs;}
		        						
		        						if(mx < hx){ leri = 1;}//left
		        						else if(mx > hx){ leri = 2;}//right
		        						
		        						
		        						d = (int) ((hx-mx)*2);
		        				 		//if(d > 20 || d < -20){d = (int) ((hx-mx)*2);}
		        				 	

				        				 //Log.w(G,"Move "+(leri==2?"right":"left")+" "+event.getHistorySize()+" " + mx + " -> " + hx + " scroll " + d );
				        				 hx = mx;
				        				 if( (d > 30 && ld <0) || (d < -30 && ld >0) ){Log.e(G,"-x-");return true;}
		        						 ld = d;
		        						 if(d > 8 || d < -8){movingtouch = true;}
			        				 //v.requestFocusFromTouch();
			        				 siscroll.sendEmptyMessage(d);return true;
			        				 //float bx = -1; if(event.getHistorySize() > 2){bx = event.getHistoricalX(2);}
			        				 //Log.w(G,"Move "+event.getHistorySize()+" " + x + " -> " + lx + " " + bx);
			        			 }else if(event.getAction() == MotionEvent.ACTION_UP){  
			        				 if(movingtouch){movingtouch = false;}else{Log.w(G,"up");ImageView iv = (ImageView) v; iv.performClick();}return true;
			        			 }else if(event.getAction() == MotionEvent.ACTION_DOWN){hx = event.getX();Log.w(G,"down");
			        				 ImageView iv = (ImageView) v; iv.setBackgroundColor(colorm1);getListView().scrollTo(0, 0);getListView().setScrollContainer(false);
			        				 movingtouch = false;
			        			 }
			        				 return false;}});
			        			 
			        		iv.setId((int)SystemClock.uptimeMillis() + ix);
			        		iv.setClickable(true);iv.setFocusable(true);
			        		final int ixx = ix;
			        		iv.setOnClickListener(new OnClickListener(){public void onClick(View v){ ImageView iv = (ImageView) v; iv.setBackgroundColor(colorm3); iv.requestFocusFromTouch(); pushlist.sendEmptyMessage(ixx); }});
			        		bi1.addView(iv,1);
			        	mPagev[ix] = iv.getId();
			        	{Bundle bl = new Bundle(); bl.putInt("view", iv.getId()); bl.putInt("page", ix);Message ml = new Message(); ml.setData(bl); pageimg.sendMessageDelayed(ml,75 * ix);}
			        	if(ix > 100){im = ix+1;break;}
			        }
			     
		        }
		        
		     

		        if(!hiu){
		        
		        pagesr2.addView(si);
		       // if(mPagev.length > im-1){
		        	//ImageView sx = (ImageView) findViewById(mPagev[im-1]);
		        	//if(sx != null){sx.performClick();}
		        //}
		        //pagesr2.setTouchDelegate(new TouchDelegate());
		        //pagesr2.setTouchDelegate(si.getId());
		        //pagesr2.setBackgroundColor(Color.argb(240,75,35,10));
		        //pagesr2.setBackgroundColor(colorm1);
		        //if(mTotal >= pagesize){pagesr2.setVisibility(View.VISIBLE);}
		        //else if(mTotal < pagesize){pagesr2.setVisibility(View.GONE);}
		        
		        //si.smoothScrollTo(30,0);    
		        //siscroll.sendEmptyMessage(110);
		        
		        {
			        //*/
			        
			        	ImageView i2 = new ImageView(mCtx);
				        i2.setLayoutParams(new RelativeLayout.LayoutParams(-2,-2));
				        //i2.setPadding(getWindowManager().getDefaultDisplay().getWidth()-48, 0, 0, 0);
				        i2.setScaleType(ScaleType.MATRIX);i2.setImageResource(R.drawable.flatpearl);
				        pagesr2.addView(i2);	
			        	
			        	ImageView i3 = new ImageView(mCtx);
				        RelativeLayout.LayoutParams rp = new RelativeLayout.LayoutParams(-2,-1);
				        LinearLayout bi1 = new LinearLayout(mCtx);
				        bi1.setGravity(Gravity.BOTTOM);
				        bi1.setLayoutParams(rp);
				        i3.setLayoutParams(new LinearLayout.LayoutParams(-2,-2));
				        bi1.setOrientation(LinearLayout.HORIZONTAL);
				        i3.setPadding(0, 0, 0, 0);
				        i3.setScaleType(ScaleType.MATRIX);i3.setImageResource(R.drawable.flatpearl3);
				        bi1.addView(i3);
				        pagesr2.addView(bi1);//*/
			        }
		        }
		        
	        }
		}
	};
	
	
	int mTotal = -2;
	Handler loadlist = new Handler(){
		public void handleMessage(Message msg){
			SharedPreferences mReg = getSharedPreferences("Preferences", MODE_WORLD_WRITEABLE);
			Bundle bdl = msg.getData();
			
			
			{	
				Uri contentpath = Uri.withAppendedPath(DataProvider.CONTENT_URI, "moment");
		        Cursor cx = SqliteWrapper.query(mCtx, mCtx.getContentResolver(), contentpath, new String[] {"max(status)"}, August.loadlistSQL, null, null);
				if( cx != null){if( cx.moveToFirst() ){ mTotal=cx.getCount(); } cx.close();}
			}//*/
			
			String[] columns = new String[] {"max(_id) as _id","title","strftime('"+August.listdate+"',max(published)) as published","max(status) as status","images"};
			String[] from = new String[]{ "title","published","status", "_id", "images"};
			int[] to = new int[]{R.id.listrow_title, R.id.listrow_published, R.id.listrow_opened, R.id.listrow_momenti, R.id.listrow_images};
	        
			lCursor = SqliteWrapper.query(mCtx, getContentResolver(), Uri.withAppendedPath(DataProvider.CONTENT_URI,"moment"), 
	        		columns,
	        		August.loadlistSQL, // Future configurable time to expire seen and unread
	        		null, 
	        		August.loadlistSort + " limit 0,"+pagesize );// + startrow + "," + numrows
			
			startManagingCursor(lCursor);
	        SimpleCursorAdapter entries = new SimpleCursorAdapter(mCtx, R.layout.listrow, lCursor, from, to);
	        //mTotal = lCursor.getCount();
	        mPagen = mTotal/pagesize;mPagen++;
	        
	        lCursor.setNotificationUri(getContentResolver(), Uri.withAppendedPath(DataProvider.CONTENT_URI,"moment"));
	        mBcolor = new int[lCursor.getCount()*2];
	        
	       {   
		       RelativeLayout l1 = new RelativeLayout(mCtx); 
		       l1.setId((int)SystemClock.uptimeMillis());
		       l1.setLayoutParams(new ListView.LayoutParams(-1, 140));
		       //l1.setMinimumHeight(65);
		       l1.setId((int)SystemClock.uptimeMillis());
		       l1.setBackgroundColor(colorm3);
		       getListView().addFooterView(l1, null, true);
		       footerGen.sendEmptyMessageDelayed(l1.getId(),75);

	    	   pagesr2 = new RelativeLayout(mCtx); 
		       //pagesr2.setOrientation(LinearLayout.HORIZONTAL);
		       ListView.LayoutParams lli = new ListView.LayoutParams(-1, 160);
		       pagesr2.setLayoutParams(lli);
		       pagesr2.setId(l1.getId()+1); 
		       //if(mTotal < pagesize){pagesr2.setVisibility(View.GONE);}
		       //pagesr2.setFocusable(true);
		       pagesr2.setBackgroundColor(colorm1);
		       getListView().addHeaderView(pagesr2, null, false);
		       
		       //pagesr2.setDescendantFocusability(RelativeLayout.FOCUS_AFTER_DESCENDANTS);
		       //pagesr2.setOnFocusChangeListener(new OnFocusChangeListener(){public void onFocusChange(View v, boolean has){Log.w(G,"page focus " + has);RelativeLayout lv = (RelativeLayout)v;if(has){lv.setBackgroundColor(colorm2);}else{lv.setBackgroundColor(colorm1);}}});
		       
	    	   
		       //Button edgetop = (Button) findViewById(R.id.edgetop);
		       //edgetop.setOnFocusChangeListener(new OnFocusChangeListener(){public void onFocusChange(View v, boolean has){Log.w(G,"button focus " + has);Button bv = (Button) v; if(has){}}});

	    	   headz = new RelativeLayout(mCtx); 
		       //rl2.setOrientation(LinearLayout.HORIZONTAL);
		       headz.setLayoutParams(new ListView.LayoutParams(-1, -2));
		       headz.setMinimumHeight(85);
		       headz.setId((int)(pagesr2.getId()+1));
		       headz.setBackgroundColor(colorm2);
		       //rl2.setFocusable(true);
		       //rl2.setNextFocusUpId(pagesr2.getId());
		       //rl2.setOnFocusChangeListener(new OnFocusChangeListener(){public void onFocusChange(View v, boolean has){Log.w(G,"head focus " + has);RelativeLayout lv = (RelativeLayout)v;if(has){lv.setBackgroundColor(colorm2);}else{lv.setBackgroundColor(colorm1);}}});
		       getListView().addHeaderView(headz, null, true);
		       if(mTotal < pagesize){getListView().setSelectionFromTop(getListView().getHeaderViewsCount(), 10);}
		       pagesGen.sendEmptyMessageDelayed(pagesr2.getId(),50); 
		       headerGen.sendEmptyMessageDelayed(headz.getId(),50);
	       }


	        
	        
	        
	        
	        
	        /*/
	        { // Edit
		        RelativeLayout l2 = new RelativeLayout(mCtx); 
		        RelativeLayout.LayoutParams b2 = new RelativeLayout.LayoutParams(80, -1);
		        //b2.weight = 1;
		        b2.setMargins(1,0,0,0);
		        b2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,-1);
		        //b2.addRule(RelativeLayout.RIGHT_OF,headerText);
		        //b2.addRule(RelativeLayout.ALIGN_TOP,headerText);
		        b2.addRule(RelativeLayout.ALIGN_BOTTOM,headerText);
		        l2.setLayoutParams(b2);
		        l2.setId((int)SystemClock.uptimeMillis());
		        l2.setBackgroundColor(coloroff);//Color.argb(190, 0, 180, 95));
		        //l2.setBackgroundColor(Color.argb(200, 80, 80, 80));
		        l2.setPadding(0, 0, 0, 0);
		        l2.setMinimumWidth(80);
		        l2.setClickable(true);//l2.setFocusable(true);
		        l2.setOnTouchListener(new OnTouchListener(){public boolean onTouch(View v, MotionEvent ev) {if(ev.getAction() == MotionEvent.ACTION_DOWN){RelativeLayout rf = (RelativeLayout) v;rf.setBackgroundColor(coloron);}else if(ev.getAction() == MotionEvent.ACTION_UP){RelativeLayout rf = (RelativeLayout) v;rf.setBackgroundColor(colorred);}else if(ev.getAction() == MotionEvent.ACTION_OUTSIDE || ev.getAction() == MotionEvent.ACTION_CANCEL){RelativeLayout rf = (RelativeLayout) v;rf.setBackgroundColor(colorred);}return false;}});
		        l2.setOnFocusChangeListener(new OnFocusChangeListener(){public void onFocusChange(View v, boolean has){RelativeLayout lv = (RelativeLayout)v;if(has){lv.setBackgroundColor(coloron);}else{lv.setBackgroundColor(colorred);}}});
		        
		        //getListView().setNextFocusUpId(l2.getId());
		        TextView t2 = new TextView(mCtx);
		        t2.setLayoutParams(new RelativeLayout.LayoutParams(-2,-2));
		        t2.setGravity(Gravity.BOTTOM);
		        t2.setTextColor(textcolor);
		        //t2.setId((int)SystemClock.uptimeMillis());
		        t2.setPadding(13, 7, 13, 7);
		        t2.setText("ABC");
		        
		        // Flatpearl
		        ImageView i3 = new ImageView(mCtx);
		        RelativeLayout.LayoutParams bli = new RelativeLayout.LayoutParams(-2,-2);
		        bli.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);bli.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM,-1);
		        i3.setLayoutParams(bli);
		        i3.setScaleType(ScaleType.MATRIX);i3.setImageResource(R.drawable.flatpearlb);
		        
		        
		        l2.addView(i3);
		        l2.addView(t2);
		        rl2.addView(l2);
	        }//*/
	        
	        
	        
	        //"strftime('%m-%d-%Y',published) = strftime('%m-%d-%Y','now')"
	        
			
			//cx = SqliteWrapper.query(mCtx, mCtx.getContentResolver(), contentpath, new String[] {"strftime('%m/%d %H:%M',created) as created"}, "status > 0", null, "created desc limit 1");
 			
 			
 			//if( cx != null){if( cx.moveToFirst() ){ recent=cx.getString(0); } cx.close();}
			
			
			
	        
	        
	        //if( mReg.getLong("bucket_saved", 0) != 0 ){	
		        //easyStatus(today + " Dated Today"+"\nRefreshed " + recent );
			//}
			
			
	        
	        //t2.setFocusable(true);
	        /*t2.setOnFocusChangeListener(new OnFocusChangeListener(){ public void onFocusChange(View v, boolean has){
	        	if(has){
	        		{Message ml = new Message(); Bundle bl = new Bundle(); bl.putInt("id", v.getId()); bl.putInt("color", Color.argb(200, 200, 50, 200)); ml.setData(bl); setColor.sendMessage(ml);}
	        	}else{
	        		{Message ml = new Message(); Bundle bl = new Bundle(); bl.putInt("id", v.getId()); bl.putInt("color", Color.argb(200, 20, 250, 200)); ml.setData(bl); setColor.sendMessage(ml);}
	        	}
	        	//tv = (TextView) v;
	        	//if(has){tv.setTextColor(Color.argb(255, 200, 50, 200));}else{tv.setTextColor(Color.argb(255, 50, 200, 50));}
	        }});//*/
	        //t2.setOnClickListener(new OnClickListener(){public void onClick(View v){    }});
	        
	        /*ImageView i3b = new ImageView(mCtx);
	        i3b.setLayoutParams(new RelativeLayout.LayoutParams(-2,-1));
	        //i3.setPadding(0, 0, 0, 0);
	        i3b.setScaleType(ScaleType.CENTER_CROP);i3b.setImageResource(R.drawable.glarre);
	        l2.addView(i3b);*/
	        
	        
	        /*ImageView i4 = new ImageView(mCtx);
	        i4.setLayoutParams(new RelativeLayout.LayoutParams(-2,-2));
	        i4.setPadding(getWindowManager().getDefaultDisplay().getWidth()-48, 0, 0, 0);
	        i4.setScaleType(ScaleType.MATRIX);i4.setImageResource(R.drawable.flatpearl);
	        l2.addView(i4);//*/
	        
	        

	        //final int coloroff = Color.argb(255, 50, 30, 50);
	    	//final int coloron = Color.argb(255, 100, 30, 100);
	        
	        
	        
	        
	        
	        // LISTVIEW ACTIONS
	    	//getListView().setRecyclerListener(new RecyclerListener(){public void onMovedToScrapHeap(View view) {Log.w(G,"Recycle " + view.getId());}});
	        
//	        getListView().setBackgroundColor(coloroff);
	        //RelativeLayout.LayoutParams rx = new RelativeLayout.LayoutParams(getWindowManager().getDefaultDisplay().getWidth(),2);
	        //getListView().setLayoutParams(rx);
	        getListView().setSelector(R.drawable.blueselector);
	        //getListView().setBackgroundColor(Color.BLACK);
	        
	        getListView().setOnScrollListener(new OnScrollListener(){

				public void onScroll(AbsListView view, int firstVisibleItem,
						int visibleItemCount, int totalItemCount) {
						//Log.w(G," on scroll " + firstVisibleItem + " - " + getListView().getLastVisiblePosition());
				
						groupFlowProcess();
					//int firstVisibleItem = getListView().getFirstVisiblePosition();
					
					/*
					for(int ix = firstVisibleItem-2; ix < getListView().getLastVisiblePosition(); ix++ ){
						//View v = getListView().getChildAt(ix);
						//if(v != null){
							//v.setId((int)SystemClock.uptimeMillis());
							//flowHandling(v);
						//}
						
						
						
						RelativeLayout rl;try {rl = (RelativeLayout) getListView().getChildAt(ix);}catch(ClassCastException cce){continue;}
						if(rl == null){continue;}
						if(rl.getChildCount() >= 5){

//							rl.setOnTouchListener(new OnTouchListener(){public boolean onTouch(View v, MotionEvent ev) {if(ev.getAction() == MotionEvent.ACTION_DOWN){RelativeLayout rf = (RelativeLayout) v;rf.setBackgroundColor(coloron);}else if(ev.getAction() == MotionEvent.ACTION_UP){RelativeLayout rf = (RelativeLayout) v;rf.setBackgroundColor(coloroff);}return false;}});
//							rl.setOnFocusChangeListener(new OnFocusChangeListener(){public void onFocusChange(View v, boolean has){if(has){v.setBackgroundColor(coloron);}else{v.setBackgroundColor(coloroff);}}});
							//rl.setOnTouchListener(listtouch);
							//rl.setOnFocusChangeListener(listfocus);
							TextView ti = (TextView) rl.getChildAt(1);
							ti.setTextColor(textcolor);
							TextView pi = (TextView) rl.getChildAt(4);
							pi.setTextColor(textcolor);
							TextView moi = (TextView) rl.getChildAt(5);
							TextView ot = (TextView) rl.getChildAt(2);
							ImageView it = (ImageView) rl.getChildAt(3);
							RelativeLayout sb = (RelativeLayout) rl.getChildAt(0);
							ImageView ib = (ImageView) sb.getChildAt(0);
							//*
							ImageView ib2 = (ImageView) sb.getChildAt(2);
							ImageView ib3 = (ImageView) sb.getChildAt(3);
							ImageView ib4 = (ImageView) sb.getChildAt(4);
							ImageView ib5 = (ImageView) sb.getChildAt(5);
							if(ix == 2){
								ib2.setVisibility(View.INVISIBLE);
								ib3.setVisibility(View.INVISIBLE);
							}else{
								ib2.setVisibility(View.VISIBLE);
								ib3.setVisibility(View.VISIBLE);
							}
							if(ix == getListView().getCount() - getListView().getFooterViewsCount() - 2){
								ib4.setVisibility(View.INVISIBLE);
								ib5.setVisibility(View.INVISIBLE);
							}else{
								ib4.setVisibility(View.VISIBLE);
								ib5.setVisibility(View.VISIBLE);
							}
							/
							long moment = Long.parseLong((moi.length()>0?moi.getText().toString():"2"));
							int s = Integer.parseInt(ot.getText().toString());
							s-=100;
							if(s >= 0){
								//Toast.makeText(mCtx, "Status " + s, 2750).show();
								if(s == 0){sb.setBackgroundColor(colorm1);it.setImageResource(R.drawable.wavemoment1);}else if(s == 1){sb.setBackgroundColor(colorm2);it.setImageResource(R.drawable.wavemoment2);}else if(s >= 2){sb.setBackgroundColor(colorm3);it.setImageResource(R.drawable.wavemoment3);}
							}else{
								it.setImageResource(R.drawable.wavemoment);sb.setBackgroundColor(coloroff);
							}
							
							if(s >= 1){
								ib.setId((int)SystemClock.uptimeMillis());
							//	{Message ml = new Message(); Bundle bl = new Bundle(); bl.putLong("moment", moment); bl.putInt("view", ib.getId()); ml.setData(bl); setImage.sendMessageDelayed(ml,720);}
								SharedPreferences mReg = getSharedPreferences("Preferences", MODE_WORLD_WRITEABLE);
								//Log.i(G,"hierarchy added moment " + moment);
								if(mReg.contains("image_"+moment+"_1")){
									String filename = mReg.getString("image_"+moment+"_1","");
									try{
										
										
										Drawable db = Drawable.createFromPath(filename);
										ib.setImageDrawable(db);
										ib.setVisibility(View.VISIBLE);
										//ib.setAlpha(160);
									}catch(OutOfMemoryError me){
										ib.setVisibility(View.INVISIBLE);
										continue;
									}	
									
									//TextView ti = (TextView) rl.getChildAt(1);
									//ti.setText(filename);ti.setTextScaleX((float)0.3);ti.setWidth(200);
									
									
								}else{ib.setImageResource(R.drawable.ic_menu_forward);ib.setVisibility(View.VISIBLE);}
								
							}else{ib.setImageResource(R.drawable.ic_menu_forward);ib.setVisibility(View.VISIBLE);}
						
						}
						
						
						
					}//*/
				}

				public void onScrollStateChanged(AbsListView view,
						int scrollState) {
						
						//Log.w(G,"scroll state " + scrollState + " " + getListView().getFirstVisiblePosition() + "-" + getListView().getLastVisiblePosition());
					
						if(scrollState == 0){
							groupFlowProcess();
						}
				
				
				}});
	    	
	        getListView().setOnHierarchyChangeListener(new OnHierarchyChangeListener(){

				public void onChildViewAdded(View m, View v) {
					
					
					
					//int ix = getListView().indexOfChild(v);
					//v.setId((int)SystemClock.uptimeMillis());
					//Log.w(G,"Hierarchy flow #"+ix+" " + v.getId());
					flowHandling(v);
					
					/*		
					//}
				
					if(ix < getListView().getHeaderViewsCount() || ix >= getListView().getCount() - getListView().getFooterViewsCount() ){return;}
					RelativeLayout rl;try {rl = (RelativeLayout) v;}catch(ClassCastException cce){return;}if(rl == null){return;}
					
					
					
					
					//Log.w(G," view added " + rl.getChildCount());
					//if(rl.getChildCount() >= 4){}else{
						//Log.i(G,"hierarchy added");
				//	}
				
					//int firstVisibleItem = getListView().getFirstVisiblePosition();
					//int visibleItemCount = getListView().getLastVisiblePosition() - getListView().getFirstVisiblePosition();
					//for(int ix = firstVisibleItem; ix < (firstVisibleItem + visibleItemCount); ix++ ){
						//RelativeLayout rl = (RelativeLayout) getListView().getChildAt(ix);
						
						if(rl.getChildCount() >= 5){
							rl.setId((int)SystemClock.uptimeMillis());
							
							//rl.setOnTouchListener(listtouch);
							//rl.setOnFocusChangeListener(listfocus);

							TextView ti = (TextView) rl.getChildAt(1);
							ti.setTextColor(textcolor);
							TextView pi = (TextView) rl.getChildAt(4);
							pi.setTextColor(textcolor);
							TextView moi = (TextView) rl.getChildAt(5);
							TextView ot = (TextView) rl.getChildAt(2);
							ImageView it = (ImageView) rl.getChildAt(3);
							
							try{
							RelativeLayout sb = (RelativeLayout) rl.getChildAt(0);
							ImageView ib = (ImageView) sb.getChildAt(0);
							/
							ImageView ib2 = (ImageView) sb.getChildAt(2);
							ImageView ib3 = (ImageView) sb.getChildAt(3);
							ImageView ib4 = (ImageView) sb.getChildAt(4);
							ImageView ib5 = (ImageView) sb.getChildAt(5);
							if(ix == 2){
								ib2.setVisibility(View.INVISIBLE);
								ib3.setVisibility(View.INVISIBLE);
							}else{
								ib2.setVisibility(View.VISIBLE);
								ib3.setVisibility(View.VISIBLE);
							}
							if(ix == getListView().getCount() - getListView().getFooterViewsCount() - 3){
								ib4.setVisibility(View.INVISIBLE);
								ib5.setVisibility(View.INVISIBLE);
							}else{
								ib4.setVisibility(View.VISIBLE);
								ib5.setVisibility(View.VISIBLE);
							}//
							
							
							long moment = Long.parseLong((moi.length()>0?moi.getText().toString():"2"));
							int s = Integer.parseInt(ot.getText().toString());
							s-=100;
							if(s >= 0){
								//Toast.makeText(mCtx, "Status " + s, 2750).show();
								if(s == 0){sb.setBackgroundColor(colorm1);it.setImageResource(R.drawable.wavemoment1);}else if(s == 1){sb.setBackgroundColor(colorm2);it.setImageResource(R.drawable.wavemoment2);}else if(s >= 2){sb.setBackgroundColor(colorm3);it.setImageResource(R.drawable.wavemoment3);}
							}else{
								it.setImageResource(R.drawable.wavemoment);sb.setBackgroundColor(coloroff);
							}
							
							if(s >= 1){
								ib.setId((int)SystemClock.uptimeMillis());
							//	{Message ml = new Message(); Bundle bl = new Bundle(); bl.putLong("moment", moment); bl.putInt("view", ib.getId()); ml.setData(bl); setImage.sendMessageDelayed(ml,720);}
								SharedPreferences mReg = getSharedPreferences("Preferences", MODE_WORLD_WRITEABLE);
								//Log.i(G,"hierarchy added moment " + moment);
								if(mReg.contains("image_"+moment+"_1")){
									String filename = mReg.getString("image_"+moment+"_1","");
									try{
										Drawable db = Drawable.createFromPath(filename);
										ib.setImageDrawable(db);
										ib.setVisibility(View.VISIBLE);
										//ib.setAlpha(160);
									}catch(OutOfMemoryError me){
										ib.setVisibility(View.INVISIBLE);
										return;
									}	
									
									//TextView ti = (TextView) rl.getChildAt(1);
									//ti.setText(filename);ti.setTextScaleX((float)0.3);ti.setWidth(200);
									
									
								}else{ib.setImageResource(R.drawable.ic_menu_forward);ib.setVisibility(View.VISIBLE);}
								
							}else{ib.setImageResource(R.drawable.ic_menu_forward);ib.setVisibility(View.VISIBLE);}
						
							}catch(ClassCastException e){Log.e(G,"Error " + e.getLocalizedMessage()); }
							
						}//*/
				
				
				
				}

				public void onChildViewRemoved(View parent, View child) {
					//Log.i(G,"hierarchy removed");
				}});
	        
	        //getListView().setOnTouchListener(new OnTouchListener(){public boolean onTouch(View pv, MotionEvent ev) { View v = getListView().getSelectedView(); if(ev.getAction() == MotionEvent.ACTION_DOWN){RelativeLayout rf = (RelativeLayout) v;rf.setBackgroundColor(coloron);}else if(ev.getAction() == MotionEvent.ACTION_UP){RelativeLayout rf = (RelativeLayout) v;rf.setBackgroundColor(coloroff);}return false;}});
	        
	        
	        getListView().setOnItemLongClickListener(new OnItemLongClickListener(){     
	        
	        	public boolean onItemLongClick(AdapterView<?> av, View v, int idi, long idl){
	        	
	        		int cl = getListView().getCount();
	        		Log.w(G,"long idi("+idi+") cl("+cl+") ");
	        		//if(idi == 0){return false;}
	        		//if(idi == cl - getListView().getFooterViewsCount() ){
	        		
	        			//Message ml = new Message(); Bundle bl = new Bundle();if(cl <= 5){wayGo.sendEmptyMessage(2);bl.putBoolean("long2",true);}else if(cl == 3){bl.putBoolean("long2",true);}else{bl.putBoolean("long2",true);}ml.setData(bl);pushlist.sendMessage(ml);
	        		 //return false;
	        		//}
	        		
	        	
	        		if(idi >= getListView().getHeaderViewsCount() && idi < cl - getListView().getFooterViewsCount() ){
	        		
	        			RelativeLayout rl;try {rl = (RelativeLayout) v;}catch(ClassCastException cce){return false;}
	        		//RelativeLayout rl = (RelativeLayout) v;
			//if(rl.getChildCount() >= 4){
				/*TextView moi = (TextView) rl.getChildAt(5);
				TextView ot = (TextView) rl.getChildAt(2);
				ImageView it = (ImageView) rl.getChildAt(3);
				ImageView ib = (ImageView) rl.getChildAt(0);
				*/
	        			TextView ti = (TextView) rl.getChildAt(1);
			
						Intent space = null;
						try {
							space = new Intent(mCtx,Class.forName("com.havenskys.galaxy.Space"));
						} catch (ClassNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						space.putExtra("title", ti.getText().toString());
						space.putExtra("moment", idl);
						space.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_NEW_TASK);
						space.putExtra("wish", "Handle content");
						startActivity(space);             
						
	        		}else if(idi == cl - getListView().getFooterViewsCount() ){
	        			Log.w(G,"long press up list");
	        			pushlist.sendEmptyMessageDelayed(-1,50);
	        			return true;
	        		}
	        		
	        		
	        		return false;
	        	
	        	}
	        });
	        
	        //*
	        getListView().setOnFocusChangeListener(new OnFocusChangeListener(){public void onFocusChange(View v, boolean h){lastposition = 0;  RelativeLayout b = null; try{b= (RelativeLayout) v;}catch(ClassCastException ec){return;}if(b==null){return;}int x = getListView().getPositionForView(b);Log.w(G,"focus x "+x); if(mBcolor[x] > 0){
			if(h){b.setBackgroundColor(mBcolor[x]+1000);}else{b.setBackgroundColor(mBcolor[x]);}
			}}});
	        getListView().setOnItemSelectedListener(new OnItemSelectedListener(){

				public void onItemSelected(AdapterView<?> av, View v, int x, long d) {
					// TODO Auto-generated method stub
					lastposition = 0;Log.w(G,"selected " + x + " " + d);
					if(x < mBcolor.length){if( mBcolor[x] != 0){Log.w(G,"colored " + x + " with " + mBcolor[x]);
						RelativeLayout b = (RelativeLayout) v;b.setBackgroundColor(mBcolor[x]+1000);
					}}
				}

				public void onNothingSelected(AdapterView<?> arg0) {
					// TODO Auto-generated method stub
					lastposition = 0;
				}});//*/
			
			
	        
	        
	        
	        
	        
	        // CREATE LIST
	        setListAdapter(entries);
	        //getListView().setSelectionAfterHeaderView();
	        
	        
	        
	        
	        
			
		}
	};


	void groupFlowProcess(){
		
		int firstVisibleItem = getListView().getFirstVisiblePosition();
		int lastVisibleItem = getListView().getLastVisiblePosition();
		View v = null;
		//int visibleItemCount = getListView().getLastVisiblePosition() - getListView().getFirstVisiblePosition();
		for(int ix = firstVisibleItem-2; ix < lastVisibleItem; ix++ ){
			
			//if(ix < getListView().getHeaderViewsCount() || ix >= getListView().getCount() - getListView().getFooterViewsCount() ){continue;}
			/*
			View v = getListView().getChildAt(ix);
			if(v != null){
				v.setId((int)SystemClock.uptimeMillis());
				//Log.w(G,"Scroll Flow #"+ix + " " + v.getId());
				flowHandling(v);
			}
			
			//*/
			//RelativeLayout rl = (RelativeLayout) getListView().getChildAt(ix);
			
			//RelativeLayout rl;try {rl = (RelativeLayout) getListView().getChildAt(ix);}catch(ClassCastException cce){continue;}
			//if(rl == null){continue;}
			View iv = getListView().getChildAt(ix);
			if(iv!=null){flowHandling(iv);}
			
			/*
			if(rl.getChildCount() >= 5){
				int idx = getListView().getPositionForView(rl);
				Log.w(G,"index/position " + ix + "/"+idx);
				rl.setId((int)SystemClock.uptimeMillis());
//				rl.setOnTouchListener(new OnTouchListener(){public boolean onTouch(View v, MotionEvent ev) {if(ev.getAction() == MotionEvent.ACTION_DOWN){RelativeLayout rf = (RelativeLayout) v;rf.setBackgroundColor(coloron);}else if(ev.getAction() == MotionEvent.ACTION_UP){RelativeLayout rf = (RelativeLayout) v;rf.setBackgroundColor(coloroff);}return false;}});
//				rl.setOnFocusChangeListener(new OnFocusChangeListener(){public void onFocusChange(View v, boolean has){if(has){v.setBackgroundColor(coloron);}else{v.setBackgroundColor(coloroff);}}});
				//rl.setOnTouchListener(listtouch);
				//rl.setOnFocusChangeListener(listfocus);
				TextView ti = (TextView) rl.getChildAt(1);
				ti.setTextColor(textcolor);
				TextView pi = (TextView) rl.getChildAt(4);
				pi.setTextColor(textcolor);
				TextView moi = (TextView) rl.getChildAt(5);
				TextView ot = (TextView) rl.getChildAt(2);
				ImageView it = (ImageView) rl.getChildAt(3);
				RelativeLayout sb = (RelativeLayout) rl.getChildAt(0);
				ImageView ib = (ImageView) sb.getChildAt(0);
				
				ImageView ib2 = (ImageView) sb.getChildAt(2);
				ImageView ib3 = (ImageView) sb.getChildAt(3);
				ImageView ib4 = (ImageView) sb.getChildAt(4);
				ImageView ib5 = (ImageView) sb.getChildAt(5);
				if(idx == 2){
					ib2.setVisibility(View.INVISIBLE);
					ib3.setVisibility(View.INVISIBLE);
				}else{
					ib2.setVisibility(View.VISIBLE);
					ib3.setVisibility(View.VISIBLE);
				}
				if(idx == getListView().getCount() - getListView().getFooterViewsCount() - 2){
					ib4.setVisibility(View.INVISIBLE);
					ib5.setVisibility(View.INVISIBLE);
				}else{
					ib4.setVisibility(View.VISIBLE);
					ib5.setVisibility(View.VISIBLE);
				}
				
				long moment = Long.parseLong((moi.length()>0?moi.getText().toString():"2"));
				int s = Integer.parseInt(ot.getText().toString());
				s-=100;
				if(s >= 0){
					//Toast.makeText(mCtx, "Status " + s, 2750).show();
					if(s == 0){sb.setBackgroundColor(colorm1);it.setImageResource(R.drawable.wavemoment1);}else if(s == 1){sb.setBackgroundColor(colorm2);it.setImageResource(R.drawable.wavemoment2);}else if(s >= 2){sb.setBackgroundColor(colorm3);it.setImageResource(R.drawable.wavemoment3);}
				}else{
					it.setImageResource(R.drawable.wavemoment);sb.setBackgroundColor(coloroff);
				}
				
				if(s >= 1){
					ib.setId((int)SystemClock.uptimeMillis());
				//	{Message ml = new Message(); Bundle bl = new Bundle(); bl.putLong("moment", moment); bl.putInt("view", ib.getId()); ml.setData(bl); setImage.sendMessageDelayed(ml,720);}
					SharedPreferences mReg = getSharedPreferences("Preferences", MODE_WORLD_WRITEABLE);
					//Log.i(G,"hierarchy added moment " + moment);
					if(mReg.contains("image_"+moment+"_1")){
						String filename = mReg.getString("image_"+moment+"_1","");
						try{
							
							
							Drawable db = Drawable.createFromPath(filename);
							ib.setImageDrawable(db);
							ib.setVisibility(View.VISIBLE);
							//ib.setAlpha(160);
						}catch(OutOfMemoryError me){
							ib.setVisibility(View.INVISIBLE);
							continue;
						}	
						
						//TextView ti = (TextView) rl.getChildAt(1);
						//ti.setText(filename);ti.setTextScaleX((float)0.3);ti.setWidth(200);
						
						
					}else{ib.setImageResource(R.drawable.ic_menu_forward);ib.setVisibility(View.VISIBLE);}
					
				}else{ib.setImageResource(R.drawable.ic_menu_forward);ib.setVisibility(View.VISIBLE);}
			
			}
		//*/		
		}
		
		
		
	}
	
	
	
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
	
	
	int uniq = 2;
	void flowHandling(View v){
	
		
		final int idx = getListView().getPositionForView(v);
		int ix = getListView().indexOfChild(v);
		//Log.i(G,"Flow Handling "+idx+"#"+ix+" " + v.getId());
		
		if(idx < getListView().getHeaderViewsCount() || idx >= getListView().getCount() - getListView().getFooterViewsCount() ){return;}
		
		RelativeLayout rl;try {rl = (RelativeLayout) v;}catch(ClassCastException cce){return;}if(rl == null){return;}
		
		rl.postInvalidateDelayed(138);
		
		//Log.w(G," view added " + rl.getChildCount());
		//if(rl.getChildCount() >= 4){}else{
			//Log.i(G,"hierarchy added");
	//	}
	
		//int firstVisibleItem = getListView().getFirstVisiblePosition();
		//int visibleItemCount = getListView().getLastVisiblePosition() - getListView().getFirstVisiblePosition();
		//for(int ix = firstVisibleItem; ix < (firstVisibleItem + visibleItemCount); ix++ ){
			//RelativeLayout rl = (RelativeLayout) getListView().getChildAt(ix);
			
			if(rl.getChildCount() >= 5){
				
				
				//rl.setOnTouchListener(listtouch);
				//rl.setOnFocusChangeListener(listfocus);

				TextView ti = (TextView) rl.getChildAt(1);
				ti.setTextColor(textcolor);
				ti.setText(Uri.decode(ti.getText().toString().replaceAll("&#[0-9]+;","")));
				TextView pi = (TextView) rl.getChildAt(4);
				pi.setTextColor(textcolor);
				TextView moi = (TextView) rl.getChildAt(5);
				TextView ims = (TextView) rl.getChildAt(6);
				TextView ot = (TextView) rl.getChildAt(2);
				ImageView it = (ImageView) rl.getChildAt(3);
				
				try{
				RelativeLayout sb = (RelativeLayout) rl.getChildAt(0);
				LinearLayout ibl = (LinearLayout) sb.getChildAt(0);
				ImageView ib = (ImageView) sb.getChildAt(3);
				ImageView ib1 = (ImageView) sb.getChildAt(1);
				ImageView ib2 = (ImageView) sb.getChildAt(2);
				ImageView ib3 = (ImageView) sb.getChildAt(4);
				ImageView ib4 = (ImageView) sb.getChildAt(5);
				ImageView ib5 = (ImageView) sb.getChildAt(6);
				ImageView ib6 = (ImageView) sb.getChildAt(7);
				if(idx == 2){
					ib3.setVisibility(View.INVISIBLE);
					ib4.setVisibility(View.INVISIBLE);
				}else{
					ib3.setVisibility(View.VISIBLE);
					ib4.setVisibility(View.VISIBLE);
				}
				if(idx == getListView().getCount() - getListView().getFooterViewsCount() - 1){
					ib5.setVisibility(View.INVISIBLE);
					ib6.setVisibility(View.INVISIBLE);
				}else{
					ib5.setVisibility(View.VISIBLE);
					ib6.setVisibility(View.VISIBLE);
				}
				ibl.setId(uniq++);
				 //RelativeLayout.LayoutParams gl2 = new RelativeLayout.LayoutParams(55,55); 
				 	//RelativeLayout.LayoutParams gl = (RelativeLayout.LayoutParams) ibl.getLayoutParams(); 
				 	//gl2.setMargins(gl.leftMargin, gl.topMargin, gl.rightMargin, gl.bottomMargin); 
				 	//gl2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,-1); ibl.setLayoutParams(gl2);
				
				rl.setId(uniq++);
				final int rli = rl.getId();
				final int iib = ibl.getId();
				final int wmw = sb.getWidth() - 1;
				long moment = Long.parseLong((moi.length()>0?moi.getText().toString():"2"));
				String ots = ot.getText().toString();if(ots.length() == 0){ots = "0";}
				int s = Integer.parseInt(ots);
				int bcolor = 2;int bimg = 2;
				s-=100;
				if(s >= 0){
					//Toast.makeText(mCtx, "Status " + s, 2750).show(); 
					if(s == 0){
						bcolor = colorm1;bimg = R.drawable.wavemoment1;
					}else if(s == 1){
						bcolor = colorm2;bimg = R.drawable.wavemoment2;
					}else if(s >= 2){
						bcolor = colorm3;bimg = R.drawable.wavemoment3;
					}
				}else{
					bcolor = coloroff;bimg = R.drawable.wavemoment;
				}
				
				sb.setBackgroundColor(bcolor);
				it.setImageResource(bimg);
				
				//ib2.setId((int)SystemClock.uptimeMillis());
				//
				//ibl.setPadding(ibl.getPaddingLeft()-ib2.getWidth()-3, ibl.getPaddingTop(), ibl.getPaddingRight(), ibl.getPaddingBottom());
				//final int colorw = bcolor;Log.w(G,"Setting c " + bcolor);
				if(mBcolor == null || mBcolor.length <= idx){
					int[] xw = mBcolor; 
		        	mBcolor = new int[(getListView().getCount() *2)];
		        	for(int i = 0; i < xw.length; i++){mBcolor[i] = xw[i];}
					
				}
					
					mBcolor[idx] = bcolor;
				//rl.setFocusable(true);
				//rl.setOnFocusChangeListener(new OnFocusChangeListener(){RelativeLayout rv = null;public void onFocusChange(View v, boolean h){rv = (RelativeLayout)v;if(rv==null){return;}if(h){rv.setBackgroundColor(colorw);}else{rv.setBackgroundColor(colorw+10000);}}});
				ibl.setOnTouchListener(new OnTouchListener(){public boolean onTouch(View v, MotionEvent e){return true;}});
				//RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) ib.getLayoutParams();rlp.setMargins(rlp.leftMargin,rlp.topMargin,39,rlp.bottomMargin);ib.setLayoutParams(rlp);
				ib.setOnTouchListener(new OnTouchListener(){boolean motiontouch = false; boolean tap = false; long hover = 2;RelativeLayout.LayoutParams rlp = null;
				
				public boolean onTouch(View v, MotionEvent m){
				
				if(m.getAction() == MotionEvent.ACTION_MOVE && m.getHistorySize() > 0){
					
				float x = m.getX();
				float lx = m.getHistoricalX(1);
				int d = (int) ((lx-x)*2);
				tap = false;
				if(d > 8 || d < -8){motiontouch=true;}
				
				//final int wmw = getWindowManager().getDefaultDisplay().getWidth();setId(ib2.getId()+(int)3)
				 

				 ImageView iv = (ImageView) v;
				 
				  rlp = (RelativeLayout.LayoutParams) iv.getLayoutParams();
				 
				 if(rlp.rightMargin < -1 || rlp.rightMargin+d > wmw ){}else{
					 int os = rlp.rightMargin+d<-1?-1:rlp.rightMargin+d>wmw-iv.getWidth()?wmw-iv.getWidth():rlp.rightMargin+d;//iv.setId(iv.getId()+3); 
					 rlp.setMargins(rlp.leftMargin,rlp.topMargin,os,rlp.bottomMargin);iv.setLayoutParams(rlp); 
					 LinearLayout lv = (LinearLayout) findViewById(iib);if(lv == null){return true;}
					 RelativeLayout.LayoutParams gl = (RelativeLayout.LayoutParams) lv.getLayoutParams();
					 RelativeLayout.LayoutParams gl2 = new RelativeLayout.LayoutParams((int)(os*1.25),55); 
					 gl2.setMargins(gl.leftMargin, gl.topMargin, gl.rightMargin, gl.bottomMargin);
					 gl2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,-1); lv.setLayoutParams(gl2);  }
				 	return false;
				 
				 }else if(m.getAction() == MotionEvent.ACTION_UP && motiontouch){
					 motiontouch=false;ImageView iv = (ImageView) v;RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) iv.getLayoutParams();rlp.setMargins(rlp.leftMargin,rlp.topMargin,39,rlp.bottomMargin);iv.setLayoutParams(rlp);
					 LinearLayout lv = (LinearLayout) findViewById(iib);if(lv == null){return true;}
					 RelativeLayout.LayoutParams gl2 = new RelativeLayout.LayoutParams(55,55); 
				 	RelativeLayout.LayoutParams gl = (RelativeLayout.LayoutParams) lv.getLayoutParams(); 
				 	gl2.setMargins(gl.leftMargin, gl.topMargin, gl.rightMargin, gl.bottomMargin); 
				 	gl2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,-1); lv.setLayoutParams(gl2);return true;}
				

				 //Log.w(G,"Selecting " + idx + " " + rli);
				
				 else if(m.getAction() == MotionEvent.ACTION_DOWN && tap){tap=false;motiontouch=false;return false;}
				 else if(m.getAction() == MotionEvent.ACTION_DOWN){tap=true;
				 //getListView().setSelection(idx);
				 RelativeLayout rx = (RelativeLayout) getListView().getChildAt(idx);
				 if(rx !=null){Log.w(G,"down d"+idx);rx.requestFocusFromTouch();}
				 //getListView().requestFocusFromTouch();
				 //rlFocusOn.sendEmptyMessageDelayed(rli,10);
				 motiontouch = false;
				 return true;}
				 if(m.getAction() == MotionEvent.ACTION_UP){return false;}
				 
				 return true;}});
				//iv.setPadding(13, 0, iv.getPaddingRight()+d, 0);
				
				
				
				if(s >= 1){
					ib.setId((int)SystemClock.uptimeMillis());
				//	{Message ml = new Message(); Bundle bl = new Bundle(); bl.putLong("moment", moment); bl.putInt("view", ib.getId()); ml.setData(bl); setImage.sendMessageDelayed(ml,720);}
					//SharedPreferences mReg = getSharedPreferences("Preferences", MODE_WORLD_WRITEABLE);
					//Log.i(G,"hierarchy added moment " + moment);
					//if(mReg.contains("image_"+moment+"_1")){
					if(ims.length() > 1){
						//String filename = mReg.getString("image_"+moment+"_1","");
						String filelist = ims.getText().toString();
						String[] fs = filelist.split("\n");
						String filename = fs[0].replaceAll(".*?://", "").replaceAll("(:)", "_");
						try{
							String ni = getfilepath().getAbsolutePath() + "/compressed/"+filename.substring(0,filename.lastIndexOf('.'))+".jpg";
							Log.i(G,"display image " + ni);
							Drawable db = Drawable.createFromPath(ni);
							ib.setImageDrawable(db);
							ib.setVisibility(View.VISIBLE);
							ib.setAlpha(255);
						}catch(OutOfMemoryError me){
							ib.setVisibility(View.INVISIBLE);
							return;
						}	
						
						//TextView ti = (TextView) rl.getChildAt(1);
						//ti.setText(filename);ti.setTextScaleX((float)0.3);ti.setWidth(200);
						
						
					}else{ib.setImageResource(R.drawable.b);ib.setVisibility(View.VISIBLE);}
					
				}else{ib.setImageResource(R.drawable.b);ib.setVisibility(View.VISIBLE);}
			
				}catch(ClassCastException e){Log.e(G,"Error " + e.getLocalizedMessage()); }
				
			}
	}
	
	
	int mShowing = -2;boolean featureAutoadd = false;
	Handler loadmore = new Handler(){
		int track = -2;
		public void handleMessage(Message msg){
			long foo = SystemClock.uptimeMillis() - (long)msg.what;
			if( foo > 3105 || (foo > 5000 && !pagesr2.isShown()) && foo < 30000){
				Log.w(G,"defer foo " + foo + " ms");
				loadmore.sendEmptyMessageDelayed((int)SystemClock.uptimeMillis(), 3000);return;
			}
			{	
				Uri contentpath = Uri.withAppendedPath(DataProvider.CONTENT_URI, "moment");
		        Cursor cx = SqliteWrapper.query(mCtx, mCtx.getContentResolver(), contentpath, new String[] {"max(status)"}, August.loadlistSQL, null, null);
				if( cx != null){if( cx.moveToFirst() ){ mTotal=cx.getCount(); } cx.close();}
			
				if(mTotal != track){track = mTotal;pagesGen.sendEmptyMessageDelayed(-2,500);}
			}
			//TextView ft = (TextView) findViewById(footerText);if(ft.isShown())
			
			mShowing = getListView().getCount() - getListView().getHeaderViewsCount() + getListView().getFooterViewsCount();
			
			if( featureAutoadd && getListView().getLastVisiblePosition() >= getListView().getCount() - (pagesize/3) && mShowing < mTotal && mShowing != getListView().getLastVisiblePosition()){
				Log.w(G,"#"+mShowing + " at "+getListView().getLastVisiblePosition() + " " + mShowing + "/"+mTotal + "    " + getListView().getChildCount() + "~"+getListView().getCount());
				pushlist.sendEmptyMessage(-2);
			}
			loadmore.sendEmptyMessageDelayed((int)SystemClock.uptimeMillis(), 3000);
			
		}
	};
	
		
	Handler serviceStart = new Handler(){
		public void handleMessage(Message msg){
			Thread tx = new Thread(){
				public void run(){
					SharedPreferences mReg = getSharedPreferences("Preferences", MODE_WORLD_WRITEABLE);
					int interval = mReg.getInt("interval", 1);
					if(interval != 0){//Log.w(G,"start service");
						String intervalText = "";
						if(interval == 1){ intervalText = "5 Hour Interval"; }
						else if(interval == 2){ intervalText = "Daily Interval"; }
						else if(interval >= 10){ intervalText = interval + " Minute Interval"; }
						if(!mReg.contains("interval")){
						
							//easyStatus("This is a custom Independent data viewer.\nlive the revolution");
						{Message ml = new Message(); Bundle bl = new Bundle(); bl.putString("title", August.title); bl.putString("dest", August.dest); bl.putString("storloc", "bucket"); ml.setData(bl); getlist.sendMessage(ml);}
						//easyStatus("Automatic\n" + intervalText );
						Editor mEdt = mReg.edit(); mEdt.putInt("interval", 1);mEdt.commit();}
						
						
						AlarmManager mAlM = (AlarmManager) mCtx.getSystemService(mCtx.ALARM_SERVICE);
						Intent resetservice = new Intent();
						resetservice.setAction(August.recoveryintent);
						PendingIntent service4 = PendingIntent.getBroadcast(mCtx, 80, resetservice, Intent.FLAG_ACTIVITY_NEW_TASK | PendingIntent.FLAG_CANCEL_CURRENT);
						Date d4 = new Date();
						d4.setSeconds(0);d4.setMinutes(0);d4.setHours(d4.getHours()+1);Log.w(G,"Scheduling recovery at the top of the hour("+(d4.getHours())+") with("+d4.getTime()+") valence("+(d4.getTime()-System.currentTimeMillis())/1000/60+" m)");
						mAlM.set(AlarmManager.RTC_WAKEUP, d4.getTime(), service4);
						
						
						
						//Intent service = new Intent(); 
						//service.setClass(mCtx, AutomaticService.class);
				    	//stopService(service);
				    	//startService(service);   	
					}else{ Log.w(G,"stop service");Intent service = new Intent();service.setClass(mCtx, com.havenskys.galaxy.AutomaticService.class); stopService(service); }
				}
			};
			tx.start();
		
		}
	};	

	
    
    private ProgressDialog mProgressDialog;
    private Handler mProgress = new Handler(){
		public void handleMessage(Message mx){Bundle bx = mx.getData();
		String n = bx.getString("text");
		String nx = bx.getString("title");	
		boolean indeter = bx.getBoolean("indeter");
		if(getListView().isShown() || getListView().hasFocus()){}else{Log.e(G,"List isn't shown and nofocus, sensor watch close");wayGo.sendEmptyMessage(2);return;}
		mProgressDialog = ProgressDialog.show(mCtx, nx, n, indeter);
		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgressDialog.setIcon(August.notifyimage);
		Log.w(G,"progress max " + mProgressDialog.getMax());
		//mProgressDialog.setMax(100);
		//if( bx.containsKey("max") ){ mProgressDialog.setMax((bx.getInt("max")!=null?bx.getInt("max"):100)); }
		
		mProgressDialog.setCanceledOnTouchOutside(true);	
		mProgressDialog.setOnCancelListener(new OnCancelListener(){

				public void onCancel(DialogInterface dv) {
					// TODO Auto-generated method stub
					//easyStatus("Loading in Background");
					
					//Log.w(G,"Cancelled Progress Dialog");
				}});
			
			}
    };

    private Handler mProgressTitle = new Handler(){public void handleMessage(Message msg){Bundle bdl = msg.getData(); if(mProgressDialog == null){ easyStatus(bdl.getString("text")); return; }else{ mProgressDialog.setTitle(bdl.getString("text")); }}};
    private Handler mProgressMessage = new Handler(){long smooth = 0;public void handleMessage(Message msg){Bundle bdl = msg.getData(); if(mProgressDialog == null){ easyStatus(bdl.getString("text")); return; }else{ if(smooth > SystemClock.uptimeMillis()){ {Message ml = new Message(); Bundle bl = new Bundle(); bl.putString("text", bdl.getString("text")); ml.setData(bl); mProgressMessage.sendMessageDelayed(ml,750);} return;} smooth = SystemClock.uptimeMillis() + 1750; mProgressDialog.setMessage(bdl.getString("text"));}}};
    private Handler mProgressMax = new Handler(){public void handleMessage(Message msg){if(mProgressDialog != null){Bundle bl = msg.getData();int max = bl.getInt("max"); Log.i(G,"setting max to "+max); mProgressDialog.setMax(max);}}};
    private Handler mProgressPlus = new Handler(){public void handleMessage(Message msg){if(mProgressDialog != null){Bundle bl = msg.getData();mProgressDialog.setProgress(bl.getInt("progress"));}}};
	private Handler mProgressOut = new Handler(){public void handleMessage(Message msg){if(getListView().isShown() || getListView().hasFocus()){}else{Log.e(G,"List isn't shown and nofocus, sensor watch close");wayGo.sendEmptyMessage(2);return;}   if(mProgressDialog != null && mProgressDialog.isShowing() && mProgressDialog.getWindow() != null){   try{mProgressDialog.dismiss();}catch(IllegalArgumentException e){e.printStackTrace();}}}};

    
    /*
    private DefaultHttpClient mHC;
	public Handler mGet = new Handler(){
		public void handleMessage(Message msg){Bundle bx = msg.getData();mget2(bx);}
		private void mget2(final Bundle bx){if(mHC == null){mHC = new DefaultHttpClient();}
		final String dest = bx.getString("dest");
		final String loc = bx.getString("storloc");
		final String titlr = bx.getString("title");final String procg = bx.getString("procg");
	
		if( dest == null || dest.length() == 0 ){
			Log.e(G,"Blocked empty get request: Destination titled " + titlr + " intended to " + loc);
			return;
		}
		final Bundle bdl = new Bundle(bx);
		Thread mt = new Thread(){
			
			public void run(){//SharedPreferences mReg = getSharedPreferences("Preferences", MODE_WORLD_WRITEABLE);
			//*if(mReg.contains(loc) && mReg.contains(loc+U_SAVED)){
			//	if( mReg.getLong(loc+U_SAVED, 10) > (System.currentTimeMillis()-bx.getLong("age",18000))){
			//		{Message mxm = new Message(); Bundle bxb = new Bundle(); bxb.putString("title",procg + " " + titlr);bxb.putString("subtitle",(int)(System.currentTimeMillis() - mReg.getLong(loc+U_SAVED, 33))/1000+" Second Cache " + titlr +" for "+loc+".\n"+dest ); mxm.setData(bxb);easyViewerHandler.sendMessageDelayed(mxm,10);}
			//		{Message mxx = new Message();mxx.setData(bx);taskDone.sendMessageDelayed(mxx, 30);}
			//		return;
			//	}
			//}//*
			
	
		//Thread tx = new Thread(){public void run(){
			final String dest = bdl.getString("dest");String who = bdl.getString("who");
			final String loc = bdl.getString("storloc");
			final String titlr = bdl.getString("title");String procg = bdl.getString("procg");
	
			//HAVEN
			//easyStatus("Acquiring " + titlr +"\n"+dest);
			//{Message mxm = new Message(); Bundle bxb = new Bundle(); bxb.putString("title",procg);bxb.putString("subtitle", ); mxm.setData(bxb);easyViewerHandler.sendMessageDelayed(mxm,10);}
			
			
		final long sh = SystemClock.uptimeMillis();
		HttpGet httpget = new HttpGet(dest);
		String mUrl = httpget.getURI().toString();
		
		//Log.w(G,"safeHttpGet() 1033 getURI("+httpget.getURI()+") for " + who);
		if( httpget.getURI().toString() == "" ){
			{Message mx = new Message(); Bundle bx = new Bundle();bx.putString("subtitle","Blocked empty destination get.");bx.putString("title",procg+" "+titlr);mx.setData(bx);easyViewerHandler.sendMessageDelayed(mx,pRate);}
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
					
					{Message mxm = new Message(); Bundle bxb = new Bundle();bxb.putString("string", loc+"url");bxb.putString(loc+"url",url );mxm.setData(bxb);setrefHandler.sendMessageDelayed(mxm,10);}
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
							{Message mxm = new Message(); Bundle bxb = new Bundle();bxb.putString("string", loc+"url");bxb.putString(loc+"url",url );mxm.setData(bxb);setrefHandler.sendMessageDelayed(mxm,10);}
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
			
			//HAVEN
			//{Message mxm = new Message(); Bundle bxb = new Bundle(); bxb.putString("text","Downloading into RAM\n"+(freememory/1024)+" Kb free"); mxm.setData(bxb);easyStatusHandler.sendMessageDelayed(mxm,10);}
			
			HttpResponse mHR = mHC.execute(httpget);
			//reply[2] = mReg.getString(loc+"url", mUrl);mUrl = reply[2];
			if(getListView().isShown() || getListView().hasFocus()){}else{Log.e(G,"List isn't shown and nofocus, sensor watch close");wayGo.sendEmptyMessage(2);}
			if( mHR != null ){
		        Log.w(G,"safeHttpGet() 436 " + mHR.getStatusLine() + " " + " for " + who);
				//{Message mxm = new Message(); Bundle bxb = new Bundle(); bxb.putString("text","Server says "+mHR.getStatusLine().getStatusCode() + " "+mHR.getStatusLine().getReasonPhrase()); mxm.setData(bxb);easyStatusHandler.sendMessageDelayed(mxm,10);}
				if(mHR.getStatusLine().getStatusCode() == 200){}else{
					easyStatus(mHR.getStatusLine().getStatusCode() + " " + mHR.getStatusLine().getReasonPhrase());
				}
		        Log.w(G,"safeHttpGet() 440 response.getEntity() for " + who);
		        HttpEntity mHE = mHR.getEntity();
	
		        if (mHE != null) {
			        //byte[] bytes = ;
		        	Log.w(G,"safeHttpGet() 445 byte[] to EntityUtils.toByteArray(mHE) expect 448");
		        	freememory = Runtime.getRuntime().freeMemory();
		        	
		        	String mhpb = EntityUtils.toString(mHE);
		        	//easyStatus("Downloaded into RAM\n"+ (mhpb.length()>1024?(mhpb.length()/1024)+" Kb":(mhpb.length())+" b" ));
		        	Log.w(G,"safeHttpGet() 448 mhpb("+mhpb.length()+") to String for " + who);
		        	{Message ml = new Message(); Bundle bl = new Bundle(); bl.putString("title", titlr); bl.putString("murl", mUrl); bl.putLong("startdl", sh); bl.putString("statusline", mHR.getStatusLine().getReasonPhrase());bl.putString("dest", dest); bl.putString("storloc", loc); bl.putString("mhpb", mhpb); bl.putString("pageconnectknow", bdl.getString("pageconnectknow")); ml.setData(bl); storePage.sendMessage(ml);}
		        	
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
			      		
			      		//*
			      		//Thread eb = new Thread(){public void run(){mReg = getSharedPreferences("Preferences",MODE_WORLD_WRITEABLE);mEdt = mReg.edit();
			      	//mEdt.putLong("lasthttp",System.currentTimeMillis());
			      	//mEdt.putString("lastcookies", s);
			      	//mEdt.commit();
			      		//}};eb.start();//*
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
			{Message mx = new Message(); Bundle bx = new Bundle();bx.putString("text","safeHttpGet() 1121 ClientProtocolException for " + who);bx.putInt("l",2);mx.setData(bx);logoly.sendMessageDelayed(mx,pRate);}
			{Message mx = new Message(); Bundle bx = new Bundle();bx.putString("text","safeHttpGet() 1122 IO Exception Message " + e.getLocalizedMessage());bx.putInt("l",2);mx.setData(bx);logoly.sendMessageDelayed(mx,pRate);}
			e.printStackTrace();//{Message mx = new Message(); Bundle bx = new Bundle();bx.putString("text",e.printStackTrace());bx.putInt("l",2);mx.setData(bx);logoly.sendMessageDelayed(mx,100);}
			responseCode = " " + e.getLocalizedMessage() + " HTTP ERROR";easyStatus(responseCode);
		} catch (NullPointerException e) {
			{Message mx = new Message(); Bundle bx = new Bundle();bx.putString("text","safeHttpGet() 1126 NullPointer Exception for " + who);bx.putInt("l",2);mx.setData(bx);logoly.sendMessageDelayed(mx,pRate);}
			{Message mx = new Message(); Bundle bx = new Bundle();bx.putString("text","safeHttpGet() 1127 IO Exception Message " + e.getLocalizedMessage());bx.putInt("l",2);mx.setData(bx);logoly.sendMessageDelayed(mx,pRate);}
			e.printStackTrace();//{Message mx = new Message(); Bundle bx = new Bundle();bx.putString("text",e.printStackTrace());bx.putInt("l",2);mx.setData(bx);logoly.sendMessageDelayed(mx,100);}
		} catch (IOException e) {
			{Message mx = new Message(); Bundle bx = new Bundle();bx.putString("text","safeHttpGet() 1130 IO Exception for " + who);bx.putInt("l",2);mx.setData(bx);logoly.sendMessageDelayed(mx,pRate);}
			//if( e.getLocalizedMessage().contains("Host is unresolved") ){ SystemClock.sleep(1880); }
			responseCode = e.getLocalizedMessage();
			
			Editor mEdt = mReg.edit();
			mEdt.putLong("bucket_error", System.currentTimeMillis());mEdt.putString("errortype", responseCode);mEdt.commit();
			
			{Message mx = new Message(); Bundle bx = new Bundle();bx.putString("text","safeHttpGet() 1132 IO Exception Message " + e.getLocalizedMessage());bx.putInt("l",2);mx.setData(bx);logoly.sendMessageDelayed(mx,pRate);}
			//StackTraceElement[] err = e.getStackTrace();
			//for(int i = 0; i < err.length; i++){
				//Log.w(G,"safeHttpGet() 1135 IO Exception Message " + i + " class(" + err[i].getClassName() + ") file(" + err[i].getFileName() + ") line(" + err[i].getLineNumber() + ") method(" + err[i].getMethodName() + ")");
			//}
			easyStatus(responseCode);
		} catch (OutOfMemoryError e) {
			{Message mx = new Message(); Bundle bx = new Bundle();bx.putString("text","safeHttpGet() 1121 OutOfMemoryError for " + who);bx.putInt("l",2);mx.setData(bx);logoly.sendMessageDelayed(mx,pRate);}
			{Message mx = new Message(); Bundle bx = new Bundle();bx.putString("text","safeHttpGet() 1122 IO Memory Message " + e.getLocalizedMessage());bx.putInt("l",2);mx.setData(bx);logoly.sendMessageDelayed(mx,pRate);}
			e.printStackTrace();//{Message mx = new Message(); Bundle bx = new Bundle();bx.putString("text",e.printStackTrace());bx.putInt("l",2);mx.setData(bx);logoly.sendMessageDelayed(mx,100);}
			Editor mEdt = mReg.edit();
			long freememory = Runtime.getRuntime().freeMemory();
			responseCode = "OS Crunch, Out of RAM at " + (freememory/1024) + " Kb";
			mEdt.putLong("bucket_error", System.currentTimeMillis());mEdt.putString("errortype", responseCode);mEdt.commit();
			easyStatus(responseCode);
		} catch (IllegalArgumentException e){
			{Message mx = new Message(); Bundle bx = new Bundle();bx.putString("text","Argument Exception "+e.getLocalizedMessage()+" for " + who);bx.putInt("l",2);mx.setData(bx);logoly.sendMessageDelayed(mx,pRate);}
			responseCode = e.getLocalizedMessage();
		
		} catch (IllegalStateException e) {
			{Message mx = new Message(); Bundle bx = new Bundle();bx.putString("text","safeHttpGet() 1139 IllegalState Exception for " + who);bx.putInt("l",2);mx.setData(bx);logoly.sendMessageDelayed(mx,pRate);}
			{Message mx = new Message(); Bundle bx = new Bundle();bx.putString("text","safeHttpGet() 1140 IO Exception Message " + e.getLocalizedMessage());bx.putInt("l",2);mx.setData(bx);logoly.sendMessageDelayed(mx,pRate);}
			e.printStackTrace();//{Message mx = new Message(); Bundle bx = new Bundle();bx.putString("text",e.printStackTrace());bx.putInt("l",2);mx.setData(bx);logoly.sendMessageDelayed(mx,100);}
			//if( responseCode == "" ){
				//responseCode = "440"; //440 simulates a timeout condition and recreates the client.
			//}
		}//e.getLocalizedMessage()
		}};mt.start();}		
	};
	//*/
	
	
	public String datetime(){
		String g = "";
		Date d = new Date();
		g = (d.getYear()+1900)+"-"+((d.getMonth() < 9)?"0":"")+((d.getMonth()+1))+"-"+((d.getDate() < 10)?"0":"")+d.getDate()+"T"+((d.getHours() < 10)?"0":"")+d.getHours()+":"+((d.getMinutes() < 10)?"0":"")+d.getMinutes()+":00";
		{Message mx = new Message(); Bundle bx = new Bundle();bx.putString("text","generated date "+g);bx.putInt("l",1);mx.setData(bx);logoly.sendMessageDelayed(mx,pRate);}
		return g;
	}
	/*
	private String fixDate(String updated) {
		//day, month dd, yyyy hh:mm tt
		//m/d/year hh:mm tt
		//2010-07-15T19:07:30+05:00
		if(updated.indexOf("CDATA[") > -1){updated = updated.substring(updated.indexOf("CDATA[")+6, updated.lastIndexOf("]]"));}
		String[] dateparts = updated.split(" ");
		if(dateparts.length == 1){dateparts = updated.replaceAll("T", " ").split(" ");}
		//Log.i(G,"fixDate ("+updated+") parts("+dateparts.length+") length("+updated.length()+")");
		if(updated.length() > 35){return datetime();}
		//if( dateparts[0].contains(",") ){ dateparts = updated.replaceFirst("T", " ").replaceFirst("..., ", "").split(" "); }
		
		
		if(dateparts[0].contains("/") && dateparts[0].contains(":")){
			
			int year = Integer.parseInt(dateparts[0].substring(dateparts[0].lastIndexOf("/")+1, dateparts[0].lastIndexOf("/")+5));
			int mon = Integer.parseInt(dateparts[0].substring(0, dateparts[0].indexOf("/")));
			int day = Integer.parseInt(dateparts[0].substring(dateparts[0].indexOf("/")+1, dateparts[0].lastIndexOf("/")));
			if( mon < 10 ){
				updated = year + "-0" + mon + "-";
			}else{
				updated = year + "-" + mon + "-";
			}
			if( day < 10 ){updated += "0"+ day + " ";}else{updated += day + " ";}
			int h = 0;int m = 0;
			h = Integer.parseInt(dateparts[0].substring(dateparts[0].indexOf(":")-2, dateparts[0].lastIndexOf(":")));
			m = Integer.parseInt(dateparts[0].substring(dateparts[0].indexOf(":")+1));
			if(dateparts[1].toLowerCase().contains("pm") && h < 12){
				h+=12;
			}if(dateparts[1].toLowerCase().contains("am") && h == 12){
				h-=12;
			}
			if( h < 10 ){updated += "0"+ h + ":";}else{updated += h + ":";}
			if( m < 10 ){updated += "0"+ m;}else{updated += m;}
			
			{Message mx = new Message(); Bundle bx = new Bundle();bx.putString("text","Updated date to SQLite Format("+updated+") #3");bx.putInt("l",1);mx.setData(bx);logoly.sendMessageDelayed(mx,pRate);}
			
		}
		//2010-07-15T19:07:30+05:00
		if(dateparts[0].contains("-")
				&& dateparts[1].contains(":")){
			String[] dp = dateparts[0].replaceAll("-0", "-").split("-");
			int year = Integer.parseInt(dp[0]);
			int mon = Integer.parseInt(dp[1]);
			int day = Integer.parseInt(dp[2]);
			if( mon < 10 ){
				updated = year + "-0" + mon + "-";
			}else{
				updated = year + "-" + mon + "-";
			}
			if( day < 10 ){updated += "0"+ day + " ";}else{updated += day + " ";}
			int h = 0;int m = 0;
			String[] t = dateparts[1].replaceAll(":0", ":").split(":");
			h = Integer.parseInt(t[0]);
			m = Integer.parseInt(t[1]);
			//*if(dateparts[2].toLowerCase().contains("pm") && h < 12){
			//	h+=12;
			//}if(dateparts[2].toLowerCase().contains("am") && h == 12){
			//	h-=12;
			//}//*
			if( h < 10 ){updated += "0"+ h + ":";}else{updated += h + ":";}
			if( m < 10 ){updated += "0"+ m;}else{updated += m;}
			
			{Message mx = new Message(); Bundle bx = new Bundle();bx.putString("text","Updated date to SQLite Format("+updated+") #2");bx.putInt("l",1);mx.setData(bx);logoly.sendMessageDelayed(mx,pRate);}
		}
		if(dateparts[0].contains("/")
				&& dateparts[1].contains(":")){
			String[] dp = dateparts[0].split("/");
			int year = Integer.parseInt(dp[2]);
			int mon = Integer.parseInt(dp[0]);
			int day = Integer.parseInt(dp[1]);
			if( mon < 10 ){
				updated = year + "-0" + mon + "-";
			}else{
				updated = year + "-" + mon + "-";
			}
			if( day < 10 ){updated += "0"+ day + " ";}else{updated += day + " ";}
			int h = 0;int m = 0;
			String[] t = dateparts[1].split(":");
			h = Integer.parseInt(t[0]);
			m = Integer.parseInt(t[1]);
			if(dateparts[2].toLowerCase().contains("pm") && h < 12){
				h+=12;
			}if(dateparts[2].toLowerCase().contains("am") && h == 12){
				h-=12;
			}
			if( h < 10 ){updated += "0"+ h + ":";}else{updated += h + ":";}
			if( m < 10 ){updated += "0"+ m;}else{updated += m;}
			
			{Message mx = new Message(); Bundle bx = new Bundle();bx.putString("text","Updated date to SQLite Format("+updated+") #2");bx.putInt("l",1);mx.setData(bx);logoly.sendMessageDelayed(mx,pRate);}
		}
		if( dateparts.length > 5 || (dateparts.length == 5 && dateparts[3].contains(":")) ){
			// Month
			String[] month = new String("xxx Jan Feb Mar Apr May Jun Jul Aug Sep Oct Nov Dec xxx").split(" ");
			int mon = 0;
			for(;mon < month.length; mon++){
				if( month[mon].equalsIgnoreCase(dateparts[2]) ){ break; } 
				if(dateparts[1].startsWith(month[mon])){
					break;
				}
			}
			if( mon == 13 ){
				{Message mx = new Message(); Bundle bx = new Bundle();bx.putString("text","Unable to determine month in fixDate("+updated+")");bx.putInt("l",2);mx.setData(bx);logoly.sendMessageDelayed(mx,pRate);}
				return updated;
			}
			
			// Year
			Date d = new Date();
			int year = d.getYear()+1900;
			if(dateparts[2].length() == 4){
				year = Integer.parseInt(dateparts[2]);
			}else if(dateparts[3].length() == 4){
				year = Integer.parseInt(dateparts[3]);
			}else if(dateparts[4].length() == 4){
				year = Integer.parseInt(dateparts[4]);
			}else{
				{Message mx = new Message(); Bundle bx = new Bundle();bx.putString("text","Unable to determine year in fixDate("+updated+") 2("+dateparts[2]+") 3("+dateparts[3]+")");bx.putInt("l",2);mx.setData(bx);logoly.sendMessageDelayed(mx,pRate);}
			}
			
			// Day
			int day = -1;
			
			if(dateparts[2].length() == 4 && !dateparts[0].contains(",")){
				day = Integer.parseInt(dateparts[0]);
			}else if(dateparts[3].length() == 4){// && dateparts[1].length() > 0){// && dateparts[2].contains(",")){
				//dateparts[1] = dateparts[1].replaceAll(",", "");
				day = Integer.parseInt(dateparts[1]);
			}
			
			// Date == updated
			updated = year + "-";
			updated += (mon < 10?"0"+mon:mon) + "-";
			//if( mon < 10 ){
				//updated = year + "-0" + mon + "-";
			//}else{
				//updated = year + "-" + mon + "-";
			//}
			updated += (day < 10?"0"+day:day) + " ";
			//if( day < 10 ){updated += "0"+ day + " ";}else{updated += day + " ";}
			
			// Hour Minute
			
			int h = 0;int m = 0;
			
			if(dateparts[3].contains(":")){
				String[] t = dateparts[3].split(":");
				h = Integer.parseInt(t[0]);
				m = Integer.parseInt(t[1]);
			}else if(dateparts[4].contains(":")){
				String[] t = dateparts[4].split(":");
				h = Integer.parseInt(t[0]);
				m = Integer.parseInt(t[1]);
				if(dateparts[5].toLowerCase().contains("pm") && h < 12){
					h+=12;
				}if(dateparts[5].toLowerCase().contains("am") && h == 12){
					h-=12;
				}
			}
			
			
			
			//Time
			updated += (h < 10?"0"+h:h)+":";
			updated += (m < 10?"0"+m:m);
			//if( h < 10 ){updated += "0"+ h + ":";}else{updated += h + ":";}
			//if( m < 10 ){updated += "0"+ m;}else{updated += m;}
			
			//{Message mx = new Message(); Bundle bx = new Bundle();bx.putString("text","Updated date to SQLite Format("+updated+")");bx.putInt("l",1);mx.setData(bx);logoly.sendMessageDelayed(mx,pRate);}
		}
		
		return updated;
	}
	
	
	
	/*
	//private long storeTime = 0;
	Handler storePage = new Handler(){
		public void handleMessage(Message msg){
			//if(storeTime > System.currentTimeMillis()){ easyStatus("Refresh occured within 8 seconds, try later.");  return; }//Bundle bl = msg.getData();Message ml = new Message(); ml.setData(bl);storePage.sendMessageDelayed(ml,1750);return;} 
			//storeTime = System.currentTimeMillis()+7750;
			
			final Bundle bdl = msg.getData();
	//		s01(bdl);
	
		
	//public void s01(final Bundle bdl){
		
		Thread tx = new Thread(){
				public void run(){
					String mhpb = bdl.getString("mhpb");
					String loc = bdl.getString("storloc");
					//Log.w(G,"storePage 81 converting content " + mhpb.length() + " bytes");
					//byte[] mx = new byte[mhpb.length+1 * 2];
					//String mHP = "";
					//for(int i = 0; i+6 < mhpb.length; i++){mHP += mhpb[i]+mhpb[++i]+mhpb[++i]+mhpb[++i]+mhpb[++i]+mhpb[++i];}
					//*
					//int errorcnt = 0;
					//for(errorcnt = 0; errorcnt < 5; errorcnt++){
					//try {
					//mHP = new String(mhpb);
					//break;
					//} catch (OutOfMemoryError e){
					//	
					//	Log.e(G,"OutOfMemory Error Received while Storing Page");
					//	//{Message ml = new Message(); Bundle bl = new Bundle(bdl); ml.setData(bl); storePage.sendMessageDelayed(ml, 3500);}return;
					//}
					//}	
					///if( errorcnt == 5 || mHP == null || mHP.length() == 0){
					//	easyStatus("Wild errors " + errorcnt);
					//	return;
					//}//*
						
					
					SharedPreferences mReg = getSharedPreferences("Preferences",MODE_WORLD_WRITEABLE);Editor mEdt = mReg.edit();
					//mEdt.putString(loc+"url", murl);
					
					Log.w(G,"storePage 86 storing content");
					//mx = null;
					mEdt.putString(loc, mhpb);
					mEdt.putLong(loc+"_saved", System.currentTimeMillis());
					mEdt.commit();
					Log.w(G,"safeHttpGet() 368 saved");
				
					String titlr = bdl.getString("title");
			        String murl = bdl.getString("murl");	
					long sh = bdl.getLong("startdl");
			        String dest = bdl.getString("dest");
					String statusline = bdl.getString("statusline");
			        String pageconnectknow = bdl.getString("pageconnectknow");
					
					//final String murl = mUrl;final String mhp = mHP;
			        	//Thread eb = new Thread(){public void run(){
			        	if( pageconnectknow == null || mhpb.contains(pageconnectknow) ){
			        	
			        		//HAVEN
			        		//easyStatus("Downloaded "+(mhpb.length()>1024?(mhpb.length()/1024)+" Kb":(mhpb.length())+" b" )+" in "+(SystemClock.uptimeMillis()-sh)/1000+" secs.");
			        	
			        		
			        		
			        		
			        		//{Message mxm = new Message(); Bundle bxb = new Bundle(); bxb.putString("subtitle","Acquired "+titlr+" in "+(SystemClock.uptimeMillis()-sh)/1000+" secs.\n"+dest ); mxm.setData(bxb);easyViewerHandler.sendMessageDelayed(mxm,10);}
						//}};eb.start();
			      		//mReg = getSharedPreferences("Preferences",MODE_WORLD_WRITEABLE);mEdt = mReg.edit();
			      		//mEdt.putString(loc, mHP);mEdt.commit();
			        	//{Message mx = new Message();Bundle bxb = new Bundle();// bxb.putString("string", loc+",");bxb.putString(loc, mHP);
			      		//bxb.putString("long", "lasthttp");bxb.putLong("lasthttp", System.currentTimeMillis());
			      		// mx.setData(bxb);setrefHandler.sendMessageDelayed(mx,50);}//*
			        	}else if(pageconnectknow != null){easyStatus("Invalid Download");
			        	{Message ml = new Message(); Bundle bl = new Bundle(); bl.putInt("l",2);bl.putString("text",titlr+" downloaded didn't pass acknowledgement("+bdl.getString("pageconnectknow")+"). Lov'n cookies and the rest.");ml.setData(bl);logoly.sendMessageDelayed(ml,75); }
			        	String ct = "";
			        	{Message ml = new Message(); Bundle bxb = new Bundle(); bxb.putString("remove", "connect");  ml.setData(bxb);setrefHandler.sendMessageDelayed(ml, 50);}
						//String[] h = mHP.split("\n");for(int b = 0; b < h.length; b++){{Message mx = new Message(); Bundle bx = new Bundle();bx.putString("text","pageknow("+bdl.getString("pageconnectknow")+") "+h[b]);mx.setData(bx);logoly.sendMessageDelayed(mx,pRate);}
						//if(h[b].contains("content1=") ){ct += h[b].substring(h[b].indexOf("content1=")+9, h[b].indexOf(";HS;", h[b].indexOf("content1=")));}}
						//ct = ct.replaceAll("%0a", "\n").trim();
						Log.e(G,"know("+bdl.getString("pageconnectknow")+")");//Log.w(G,"know("+bdl.getString("pageconnectknow")+")"+h[b]);
			        	}
			        {Message ml = new Message(); Bundle bx = new Bundle();bx.putString("text","Downloaded status(" + statusline + ") loc("+loc+") mUrl("+murl+") " + mhpb.length() + " bytes.");ml.setData(bx);logoly.sendMessageDelayed(ml,pRate);}
			    
					
				}
			};
			tx.start();
		//}	
		}
	};
	//*/
	private Handler logoly = new Handler(){public void handleMessage(Message msg){Bundle bx = msg.getData();int l = bx.getInt("l");String text = bx.getString("text");switch(l){case 2:Log.e(G,":"+text);break;case 3:Log.w(G,":"+text);break;default:Log.i(G,":"+text);break;}}};
	private Handler setrefHandler = new Handler(){
		Bundle bx;public void handleMessage(Message mx){
		final Bundle bx = mx.getData();
			Thread tx = new Thread(){public void run(){
		
				SharedPreferences mReg = getSharedPreferences("Preferences",MODE_WORLD_WRITEABLE);
			Editor mEdt = mReg.edit();
	String[] ht = new String[]{"int","long","float","remove","string"};
	for(int b = 0; b < ht.length; b++){
		String t = ht[b];
		if(!bx.containsKey(t)){continue;}
	String[] nm = (bx.getString(t)+",0").split(",");
	for(int h = 0; h < nm.length; h++){
		String k = nm[h];if(k==null){continue;}if(k.length()<=1){continue;}else if(k.contentEquals("null")){continue;}
		if(!bx.containsKey(k) && !t.contentEquals("remove") ){
			{Message mx = new Message(); Bundle bx = new Bundle();bx.putString("text","error reading incoming preference (doesn't exist in bundle) "+k);bx.putInt("l", 2);mx.setData(bx);logoly.sendMessageDelayed(mx,pRate);}
			continue;}String len = "";
		if(t.contentEquals("float")){mEdt.putFloat(k, bx.getFloat(k));len += bx.getFloat(k)+"";}
		else if(t.contentEquals("int")){mEdt.putInt(k, bx.getInt(k));len += bx.getInt(k)+"";}
		else if(t.contentEquals("long")){mEdt.putLong(k, bx.getLong(k));len += bx.getLong(k)+"";}
		else if(t.contentEquals("string")){mEdt.putString(k, bx.getString(k));len += bx.getString(k)+"";}
		else if(t.contentEquals("remove")){mEdt.remove(k);}
		//{Message mx = new Message(); Bundle bx = new Bundle();bx.putString("text","setpref "+k+" "+t + " "+(len.length() <100?len:len.length()+"b"));mx.setData(bx);logoly.sendMessageDelayed(mx,100);}
	}mEdt.commit();	
	}
	
	}};tx.start();
	   	}
	};

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
		if( (getListView().isShown() || getListView().hasFocus()) && getListView().isEnabled() ){}else{Log.e(G,"List isn't shown and nofocus, sensor watch close");wayGo.sendEmptyMessage(2);return;}
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

	private void easyViewer(final String m, final String s){
		{Message ml = new Message(); Bundle bl = new Bundle(); bl.putString("title", m);bl.putString("sub", s);ml.setData(bl);easyViewerHandler.sendMessageDelayed(ml,75);}
		
	}

	private long evsmooth = 0;
	private Handler easyViewerHandler = new Handler(){
		public void handleMessage(final Message msg){//runnable,runable,run code here
			//int ev1 = 50;
			if(evsmooth > System.currentTimeMillis()){Bundle bl = msg.getData();Message ml = new Message(); ml.setData(bl);easyViewerHandler.sendMessageDelayed(ml,750);return;} 
			evsmooth = System.currentTimeMillis()+1750;
			//final int ev = ev1;
			
		//	Thread evt = new Thread(){public void run(){
		
		final Bundle bdl = msg.getData();//easyViewer(bx.getString("title"),bx.getString("subtitle"));
		
		
		String m = bdl.getString("title");String s = bdl.containsKey("subtitle")?bdl.getString("subtitle"):bdl.getString("sub");
		
		Toast.makeText(mCtx, m+"\n"+s, Toast.LENGTH_SHORT).show();
		
		//{Message mx = new Message();Bundle bx = new Bundle();bx.putString("title", m);bx.putString("sub", s);mx.setData(bx);updateViewer.sendMessageDelayed(mx,175);}
		//{Message mx = new Message(); Bundle bx = new Bundle();bx.putString("text",m);mx.setData(bx);mTitleHandler.sendMessageDelayed(mx, 75);}
		//{Message mx = new Message(); Bundle bx = new Bundle();bx.putString("text",s);mx.setData(bx);mSubtitleHandler.sendMessageDelayed(mx, 75);}
		
		//}};evt.start();
		}
	};
	private Handler rlColorBg = new Handler(){public void handleMessage(Message msg){Bundle bl = msg.getData();int id = bl.getInt("id",0);if(id == 0){return;}RelativeLayout v = (RelativeLayout) findViewById(id);if(v != null){int col = bl.getInt("color",Color.CYAN);v.setBackgroundColor(col);}}};
	private Handler tvColorBg = new Handler(){public void handleMessage(Message msg){Bundle bl = msg.getData();int id = bl.getInt("id",0);if(id == 0){return;}TextView v = (TextView) findViewById(id);if(v != null){int col = bl.getInt("color",Color.CYAN);v.setBackgroundColor(col);}}};
	
	//private Handler rlColorBg = new Handler(){public void handleMessage(Message msg){Bundle bl = msg.getData();int id = bl.getInt("id",0);if(id == 0){return;}try{RelativeLayout v = (RelativeLayout) findViewById(id);if(v != null){int col = bl.getInt("color",Color.CYAN);v.setBackgroundColor(col);}}catch(ClassCastException e){e.printStackTrace();return;}}};
	private Handler colorFilterIN = new Handler(){public void handleMessage(Message msg){Bundle bl = msg.getData();int id = bl.getInt("id",0);if(id == 0){return;}try{ImageView v = (ImageView) findViewById(id);if(v != null){int col = bl.getInt("color",Color.CYAN);int alpha = bl.getInt("alpha",200);v.setColorFilter(col, PorterDuff.Mode.SRC_IN);v.setAlpha(alpha);}}catch(ClassCastException e){e.printStackTrace();return;}}};
	// mPearl.setColorFilter(Color.MAGENTA, PorterDuff.Mode.SRC_IN);mPearl.setAlpha(200);
	private Handler pointAt = new Handler(){public void handleMessage(Message msg){Bundle bl = msg.getData();int id = bl.getInt("id");View v = findViewById(id);int x = bl.getInt("x",10); int y = bl.getInt("y",10); v.setPadding(x-bl.getInt("w")/2, y-bl.getInt("h")/2, 0, 0);}};
	private Handler setColor = new Handler(){public void handleMessage(Message msg){Bundle bl = msg.getData();int id = bl.getInt("id");try{TextView v = (TextView) findViewById(id);if(v != null && bl.containsKey("color")){int co = bl.getInt("color",Color.BLACK);v.setTextColor(co);}}catch(ClassCastException e){Log.e(G,"Wrong target for text color");}}};
	private Handler setText = new Handler(){public void handleMessage(Message msg){Bundle bl = msg.getData();int id = bl.getInt("id");String t = bl.getString("text");try{TextView v = (TextView) findViewById(id);if(t!=null&&v!=null){if(bl.containsKey("color")){int co = bl.getInt("color",Color.BLACK);v.setTextColor(co);}v.setText(t); if(bl.containsKey("id2")){int id2 = bl.getInt("id2");if(id2 != id){TextView v2 = (TextView) findViewById(id2);if(v2 != null){v2.setText(t);}}}             }}catch(ClassCastException e){Log.e(G,"Wrong target for text " + t);}/*int x = bl.getInt("x",10); int y = bl.getInt("y",10); int size = bl.getInt("size",10);v.setPadding(x-size/2, y-size/2, 0, 0);/**/}};
	private Handler textUpdate = new Handler(){
	public void handleMessage(Message msg){
		TextView t = (TextView) findViewById(msg.getData().getInt("id"));t.setText(msg.getData().getString("text"));}};
	private Handler setFocusOn = new Handler(){public void handleMessage(Message msg){Bundle bl = msg.getData();int id = bl.getInt("id");if(bl.containsKey("parentpos")){LinearLayout lv = (LinearLayout) findViewById(bl.getInt("parentpos"));if(lv != null){int pc = lv.getChildCount();int pos = bl.getInt("pos");while(pos > pc){pos-=pc;} View v = lv.getChildAt(pos>0?pos-1:0);if(v!=null){v.requestFocusFromTouch();}}}else{View v = findViewById(id);v.requestFocusFromTouch();}}};
	private Handler rlFocusOn = new Handler(){public void handleMessage(Message msg){RelativeLayout v = (RelativeLayout) findViewById(msg.what);if(v == null){return;}Log.w(G,"Focusing " + msg.what);v.requestFocusFromTouch();}};
	private Handler rlistFocusOn = new Handler(){public void handleMessage(Message msg){RelativeLayout v = (RelativeLayout) findViewById(msg.what);v.requestFocusFromTouch();}};
	
	private Handler setFocusOff = new Handler(){public void handleMessage(Message msg){int id = msg.getData().getInt("id");View v = findViewById(id);v.clearFocus();}};
	private Handler setHidden = new Handler(){public void handleMessage(Message msg){int id = msg.getData().getInt("id");View v = findViewById(id);v.setVisibility(View.INVISIBLE);}};
	private Handler setGone = new Handler(){
	public void handleMessage(Message msg){	int id = msg.getData().getInt("id");
	View v = findViewById(id);if(v == null){return;}v.setVisibility(View.GONE);}
	};
	private Handler setVisible = new Handler(){public void handleMessage(Message msg){int id = msg.getData().getInt("id");View v = findViewById(id);v.setVisibility(View.VISIBLE);}};
    
	
	
    
    
}
