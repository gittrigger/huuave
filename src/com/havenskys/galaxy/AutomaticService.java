package com.havenskys.galaxy;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.URI;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

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
import org.w3c.dom.Text;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;

public class AutomaticService extends Service implements Runnable {

	private static String G = "Automatic"+August.title;
	private static String TAG = "Automatic"+August.title;
	private Handler mHandler;
	private NotificationManager mNM;
	private Context mCtx;
	private int pRate = 72;
	@Override
	public void onCreate() {
		super.onCreate();
		mCtx = this.getApplicationContext();
		//mLog.w(TAG,"onCreate() ++++++++++++++++++++++++++++++++++");
		
        mHandler = new Handler();
        
        Thread thr = new Thread(null, this, TAG + "_service_thread");
        thr.start();
	}

	
	public double[] getload(){
		
		try {
			Process mLoadProcess;
			InputStream mLoadStream;
			byte[] mLoadBytes;
			String[] ml = null;
			
			int mLoadReadSize;
			mLoadProcess = Runtime.getRuntime().exec("cat /proc/loadavg");
			mLoadProcess.waitFor();
			mLoadStream = mLoadProcess.getInputStream();
			mLoadBytes = new byte[100];
			mLoadReadSize = mLoadStream.read(mLoadBytes, 0, 99);
			
			if(ml == null){}else{
				ml = (mLoadBytes!=null)?new String(mLoadBytes).trim().replaceAll("\\s+", " ").split(" "):new String[]{"0","0","0","0"};
			if(ml.length >= 4){	
				Log.e(TAG,"Load Test " + ml[0]+","+ml[1]+","+ml[2]+","+ml[3]);
			}else{Log.e(TAG,"Load Test " + ml[0]+" length:"+ml.length);}
				//mLoadDouble = new Double(mLoadParts[0]);
			}
	
			
		} catch (InterruptedException e) {
			{Message mx = new Message(); Bundle bx = new Bundle();bx.putString("text","Load InterruptedException");bx.putInt("l",2);mx.setData(bx);logoly.sendMessageDelayed(mx,pRate);}
			e.printStackTrace();
		} catch (NumberFormatException e) {
			//Log.e(G,"Load NumberFormatException");
			//e.printStackTrace();
		} catch (IOException e) {
			{Message mx = new Message(); Bundle bx = new Bundle();bx.putString("text","Load IOException");bx.putInt("l",2);mx.setData(bx);logoly.sendMessageDelayed(mx,pRate);}
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return new double[]{0,0,0};
	}
	
	
	@Override
	public IBinder onBind(Intent arg0) {
		Log.w(TAG,"onBind() ++++++++++++++++++++++++++++++++++");
		return null;
	}

	public void run() {
		Log.w(TAG,"run() ++++++++++++++++++++++++++++++++++");
		{Message ml = new Message(); Bundle bl = new Bundle(); bl.putLong("ms", SystemClock.uptimeMillis()); ml.setData(bl); getlatest.sendMessage(ml);}
	}

	@Override
	public void onDestroy() {
		Log.w(TAG,"onDestroy() ++++++++++++++++++++++++++++++++++");
		SharedPreferences mReg = getSharedPreferences("Preferences", MODE_WORLD_WRITEABLE);
		Editor mEdt = mReg.edit();
		
		super.onDestroy();
	}


	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		Log.w(TAG,"onStart() ++++++++++++++++++++++++++++++++++");
	}

	
	
	private SharedPreferences mReg;
	private Editor mEdt;

	String location = "";String ltitle = "";boolean ga = false;
	Handler getlatest = new Handler(){ public void handleMessage(Message msg){
		Bundle bdl = msg.getData();
		
		long took = SystemClock.uptimeMillis() - bdl.getLong("ms");
		Log.w(TAG,"getlatest() ++++++++++++++++++++++++++++++++++ " +(took)+" ms");
		//if( took > 50 ){
			//ready(10 * 1000);
			//return;
		//}
		//mLog = new Custom(this);
		
		
		//getload();
		
		mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		//mLog.setNotificationManager(mNM);
		mReg = getSharedPreferences("Preferences", MODE_WORLD_WRITEABLE);
		mEdt = mReg.edit();
		//mLog.setSharedPreferences(mReg,mEdt);
  	  	
		mEdt.putInt("count_intent", mReg.getInt("count_service", 0)+1);mEdt.commit();
		
        //String mUuid = UUID.randomUUID().toString();
        //content://settings/system/notification_sound
        //for (Account account1 : accountsWithNewMail.keySet()) { if (account1.isVibrate()) vibrate = true; ringtone = account1.getRingtone(); }

        //BASEURL = "http://www.seashepherd.org/news-and-media/sea-shepherd-news/feed/rss.html";
        //BASEURL = "http://www.whitehouse.gov/blog/";
        // This will block until load is low or time limit exceeded
		
	
		
		if(workat == -10){
		Log.w(G,"getlatest() loading workorder");
			Cursor gx = null;
		gx = SqliteWrapper.query(mCtx, getContentResolver(), Uri.withAppendedPath(DataProvider.CONTENT_URI,"filter"), new String[]{"_id","location","status","title"}, "status > 1 GROUP BY location", null, "created asc");
		int fwos = 0;
		int wos = gx.getCount()+fwos;
		workorder = new String[wos];
		//int fwos = 1;int wos = (gx.getCount()>0?gx.getCount()+1:1);
		//
		//workorder[0] = "\n"+August.dest+"\n\n"+August.title;workat = 0;
		
		//if(August.dataprovider.contains("metrix")){
		//fwos = 3;
		//workorder[1] = "\nhttp://twitter.com/statuses/user_timeline/73936473.rss\n\nTwitter";workat = 1;
		//workorder[2] = "\nhttp://api.flickr.com/services/feeds/photos_public.gne?id=42471238@N02&lang=en-us&format=rss_200\n\nFlickr";workat = 2;
		//}
		
		String ltitle = "";String work = "";
		if(gx!=null){if(gx.moveToFirst()){
			
			for(int wi = 0; wi < gx.getCount(); wi++){gx.moveToPosition(wi);
			sourceid = gx.getInt(0);
			status = gx.getString(2)==null?0:gx.getInt(2);
			location = gx.getString(1)==null?"":gx.getString(1); 
			ltitle = gx.getString(3)==null?"":gx.getString(3);
			source = Uri.parse(DataProvider.CONTENT_URI+"filter/"+sourceid);
			
			Log.i(TAG,"Preparing work("+source+") location("+location+") status("+status+") title("+ltitle+")");
			workorder[wi] = source + "\n"+location+"\n"+status+"\n"+ltitle;
			workat = wi+fwos;
			work += Uri.encode(source + "\n"+location+"\n"+status+"\n"+ltitle)+"\n";
			}

			
			//for(int j = 0; j < mReg.getInt("workof",10); j++){
				//if(mReg.contains("bucket"+j+"_start") ){			
					//Log.i(G,"\tbucketline ["+j+"]");
				//}
			//}
			
			
			mEdt.putInt("workof",workorder.length);
			mEdt.putInt("workat",-2);
			mEdt.putString("work",work);
			mEdt.putLong("workready",System.currentTimeMillis());
			mEdt.commit();

			
			
		}gx.close();}
		}
		Log.w(G,"getlatest("+workat+"/"+workorder.length+")");

		Date d = new Date();
		//ga = false;

		//for(;workat < workorder.length; workat++){
			
			if(workat < 0 || workat >= workorder.length || workorder[workat] == null || workorder[workat].length() == 0){
			
				Log.i(TAG,"prepare complete " + workorder.length);
				
				//Intent service = new Intent();service.setClass(mCtx, com.havenskys.galaxy.AutomaticService.class); stopService(service);
				
				  
				AlarmManager mAlM = (AlarmManager) mCtx.getSystemService(mCtx.ALARM_SERVICE);
				Intent resetservice = new Intent();
				resetservice.setAction(August.recoveryintent);
				PendingIntent service4 = PendingIntent.getBroadcast(mCtx, 80, resetservice, Intent.FLAG_ACTIVITY_NEW_TASK | PendingIntent.FLAG_CANCEL_CURRENT);
				Date d4 = new Date();
				d4.setSeconds(0);d4.setMinutes(0);d4.setHours(d4.getHours()+1);
				mAlM.set(AlarmManager.RTC_WAKEUP, d4.getTime(), service4);
				Log.w(TAG,"Scheduling recovery at the top of the hour("+(d4.getHours())+") with("+d4.getTime()+") valence("+(d4.getTime()-System.currentTimeMillis())/1000/60+" m)");
			
				
					/*
	SharedPreferences mReg = getSharedPreferences("Preferences", MODE_WORLD_WRITEABLE);
					Editor mEdt = mReg.edit();
				
				if(!ga){
					if(mReg.getLong("bucket_start", 0) > 0 ){//mEdt
						mEdt.putLong("bucket_error", System.currentTimeMillis());mEdt.putString("errortype", "Started " + ((System.currentTimeMillis() - mReg.getLong("bucket_start", 0))/1000));mEdt.commit();
					}else if(mReg.getLong("bucket_start", 0) > mReg.getLong("bucket_saved", 0) ){
						mEdt.putLong("bucket_error", System.currentTimeMillis());mEdt.putString("errortype", "Download interrupted.");mEdt.commit();
					}else if(mReg.getLong("bucket_start", 0) > mReg.getLong("bucket_done", 0) ){
						mEdt.putLong("bucket_error", System.currentTimeMillis());mEdt.putString("errortype", "Work interrupted.");mEdt.commit();
					}
				}
				//*/

				int saved = 0; long sizec = 0; long newc = 0; int erc = 0;String ers = "";
				for(int i = 0; i < workorder.length; i++){
					if(mReg.getLong("bucket"+i+"_start",0) < mReg.getLong("bucket"+i+"_error",0)){
						erc++;ers += mReg.getString("bucket"+i+"_title","") + "(";
						ers += mReg.getString("bucket"+i+"_errortype","").trim() + ") ";
					}
					if(mReg.getLong("bucket"+i+"_start", 0) > mReg.getLong("bucket"+i+"_saved", 0) && mReg.getLong("bucket"+i+"_saved",0) < mReg.getLong("bucket"+i+"_done",0)){
						sizec += mReg.getLong("bucket"+i+"_size",0);
						newc += mReg.getLong("bucket"+i+"_new",0);
					}else{
						erc ++;
						ers += mReg.getString("bucket"+i+"_title","") + "(";
						ers += "track skip error) ";
					}
					
				}
				if(erc > 0){
					mEdt.putString("bucket_errortype",erc + " Errors: " + ers);
					//mEdt.putLong("bucket_error",System.currentTimeMillis());
				}
				mEdt.putLong("bucket_saved",System.currentTimeMillis()-1);
				mEdt.putLong("bucket_done",System.currentTimeMillis());
				mEdt.putLong("bucket_new",newc);
				mEdt.putLong("bucket_size",sizec);
				mEdt.commit();
				
				Intent service = new Intent();service.setClass(mCtx, com.havenskys.galaxy.AutomaticService.class); stopService(service);
				
				
				return;
			}	
		
			String[] shy = workorder[workat].split("\n");
			source = shy[0].length()>0?Uri.parse(shy[0]):null;
			location = shy[1];
			if( shy.length > 3 ){ltitle = shy[3];}else{ltitle = "";}
			if( shy.length > 2 && shy[2].matches("[0-9]+") ){status = Integer.parseInt(shy[2]);}else{status = 0;}
		String loc = "bucket"+workat;
		if(source!=null){
		Cursor gx2 = null;
		gx2 = SqliteWrapper.query(mCtx, getContentResolver(), Uri.withAppendedPath(DataProvider.CONTENT_URI,"filter"), new String[]{"_id","filtercode"}, "_id = "+source.getLastPathSegment(), null, null);
		if(gx2 != null){gx2.moveToFirst();
			String fc = Uri.decode(gx2.getString(1));
			if(fc != null){
			String fx[] = fc.split("\n");
			for(int f = 0; f < fx.length; f++){
				String fcc[] = fx[f].split(" ",2);if(fcc.length < 2){continue;}
				if(fcc[0].contentEquals("itemsplit")){mEdt.putString(loc+"_split",fcc[1].trim());continue;}
				mEdt.putString(loc+"_"+fcc[0].trim(),fcc[1].trim());
			}}
		}gx2.close();
		}
		if(mReg.getLong(loc+"_start",0) <= mReg.getLong(loc+"_done",0) ){
		
		mEdt.putString("bucket_title",ltitle);
		mEdt.putString(loc+"_title",ltitle);
		mEdt.putString(loc+"_dest",location);
		mEdt.putLong(loc+"_start",System.currentTimeMillis());
		mEdt.putLong("bucket_start",System.currentTimeMillis());
		
		mEdt.putInt("workat",workat);mEdt.commit();
		Log.w(TAG,"Starting work ["+workat+"]location("+location+")(" + workorder[workat].replaceAll("\n","<!-- -->") + ")");
		
		
		Message ml = new Message(); Bundle bl = new Bundle(); bl.putString("title", ltitle); bl.putString("dest", location); bl.putString("storloc", loc); ml.setData(bl); 
		ga = true;
		getlist.sendMessageDelayed(ml,3000);
		}else{
			Log.w(G,"Work already active["+workat+"]location("+location+")");
		}
		workat--;
		
		
		/*
		int interval = mReg.getInt("interval",1);
		long ls = mReg.getLong("bucket_saved",(long)0);
		
		//mLog.loadLimit(TAG + " getlatest() 107", syncLoad, 5 * 1000, 30 * 1000);
		if( interval == 0 ){
			Log.w(TAG,"Not Automatic");return;
		}else if( interval == 1 ){
			
			if( d.getHours() == 0 || d.getHours() == 5 || d.getHours() == 10 || d.getHours() == 15 || d.getHours() == 20 ){
				if( ls < System.currentTimeMillis() - 5 * 60 * 60 * 1000 ){
					getlist.sendMessage(ml);ga = true;
				}else{Log.w(TAG,"recent " + (System.currentTimeMillis() - ls)/60000 + " minutes");}
			}else{Log.w(TAG,"wrong hour " + d.getHours());}
		}else if( interval == 2 ){
			
			if( d.getHours() == 0 || ls < System.currentTimeMillis() - 24 * 60 * 60 * 1000 ){
				getlist.sendMessage(ml);ga = true;
			}else{Log.w(TAG,"recent " + (System.currentTimeMillis() - ls)/60000 + " minutes or hour("+d.getHours()+") != 0");}
		}else if( interval >= 10 ){
		
			if( d.getHours() == 0 || ls < System.currentTimeMillis() - interval * 60000 ){
				getlist.sendMessage(ml);ga = true;
			}else{Log.w(TAG,"recent interval("+interval+") > " + (System.currentTimeMillis() - ls)/60000 + " minutes or hour("+d.getHours()+") != 0");}
		}
		
		if(!ga && mReg.getLong("bucket_start",0) == 0){
			Log.w(TAG,"Running by manual request");
			getlist.sendMessage(ml);
			ga = true;
		}
		//*/
		//}
		
		
		
		//ready(10 * 60 * 1000);
	  
		
		
		/*/
	
		//*/
	}
	};
	
		
	//private void wayGo(){mCtx.finish();}
	private void ready(long mx){
		
		mHandler.postDelayed(this, mx);
		//Log.w(TAG,"ready("+mx+" ms)");
	}
	
	int bl2 = -1;File f8 = null;FileInputStream hx4 = null;byte[] bx5 = new byte[1];
	public int nxtIndex(String isplit){
	int bx8 = -2;String b7 = "";
	//if(bx5 == null || bx5.length < f8.length() ){bx5 = new byte[(int) f8.length()];}
	bx5 = new byte[102400];
	long bc6 = 3;int sb2 = bl2;int gg7 = 0;int u8 = 1;
	try{b7 = "";for(bx8 = bl2+1; bx8 <f8.length() ;bx8+=(bx5.length/2)){
	if(u8 > 10){Log.e(G,"["+u8+"] nxt("+bx8+") ("+b7.length()+") ("+isplit+") "+sb2+"-"+bx8+"");return -14;}
		Log.i(G,"["+u8+"] nxt("+bx8+") ("+b7.length()+") ("+isplit+")");
		bc6=hx4.skip(bx8);
	hx4.read(bx5,0,(int) (bc6+bx5.length<f8.length()?bx5.length:f8.length()-bc6));
	b7=new String(bx5);Log.i(G,"nxt b7("+b7.length()+")");
	u8++;if(b7.indexOf(isplit) > -1 ){bx8 += b7.indexOf(isplit);bl2 = bx8; break;} }}//mGet
    
	
	catch(IndexOutOfBoundsException e){Log.e(G,"nxt123 seek("+bx8+") over file size("+f8.length()+") " + e.getLocalizedMessage() );e.printStackTrace();return -1;}
    catch(IOException e){Log.e(G,"nxt123 seek("+bx8+") ioexception size("+f8.length()+") " + e.getLocalizedMessage() );e.printStackTrace(); return -1;}
    catch(OutOfMemoryError e){Log.e(G,"nxt123 seek("+bx8+") outofmemoryerror size("+f8.length()+") " + e.getLocalizedMessage() );e.printStackTrace(); bl2 = sb2; return -13;}
    
    return bx8;
	
	}
    
	Handler spliter = new Handler(){
		int l5 = 0;
		int sa1 = -2;int sat = -2;String isplit = "";
		public void handleMessage(Message msg){
		Bundle bdl = msg.getData();
		if(bdl.containsKey("isplit")){isplit = bdl.getString("isplit");}
		if(isplit.length() == 0){Log.e(G,"spliter isplit length empty turning off at "+sat+"/"+f8.length()+"");return;}
		
		sa1 = nxtIndex(isplit);
		sat = nxtIndex(isplit);
		
		Log.i(G,"spliter("+sa1+","+sat+","+bl2+","+isplit+")");
		
		l5++;if(l5 > 5){return;}
		spliter.sendEmptyMessageDelayed(-2,70);
	}};
	
	Handler serviceWork = new Handler(){
		public void handleMessage(Message msg){
		
			final Bundle bdl = msg.getData();
			Thread mt = new Thread(){
				public void run(){
			
			SharedPreferences mReg = getSharedPreferences("Preferences", MODE_WORLD_WRITEABLE);
			Editor mEdt = mReg.edit();
			
			//if(!mReg.contains("index01")){
				//Cursor dx = null;
				//dx = SqliteWrapper.query(mCtx, mCtx.getContentResolver(), Uri.withAppendedPath(DataProvider.CONTENT_URI,"moment"), new String[]{""}, selection, selectionArgs, sortOrder)
				
				
				
        		//mEdt.putLong("index01", System.currentTimeMillis());mEdt.commit();
				
			//}
				
			// downloading
			final String dest = bdl.getString("dest");
			final String loc = bdl.getString("storloc");
			final String titlr = bdl.getString("title");
			final String procg = bdl.getString("procg");
			
			if( mReg.getLong(loc+"_start", 0) < mReg.getLong(loc+"_error", 0) ){
				Log.e(TAG, "serviceWork() download error("+mReg.getString(loc+"_errortype", "")+") errored " + (System.currentTimeMillis() - mReg.getLong(loc+"_error", 0) ) +"ms ago Started " + (System.currentTimeMillis() - mReg.getLong(loc+"_start", 0) ) +"ms ");
				{Message ml = new Message(); Bundle bl = new Bundle(); bl.putLong("ms", SystemClock.uptimeMillis()); ml.setData(bl); getlatest.sendMessage(ml);}
				
				return;
			}
			
			if( mReg.getLong(loc+"_start", 0) > mReg.getLong(loc+"_saved", 0) ){
				Log.w(TAG, "serviceWork() download waiting " + (System.currentTimeMillis() - mReg.getLong(loc+"_start", 0) ) +"ms ");
				{Message ml = new Message(); Bundle bl = new Bundle(bdl); ml.setData(bl); serviceWork.sendMessageDelayed(ml,2700);}
				return;
			}
			
			
			if(bdl.getInt("retryax",0) > 5){
				Log.e(G,"###\n###\n###\n###\n###\n### RETRY AX ESTABLISHED ###\n###\n###\n###\n###");
				return;
			}
			
			String isplit = "";
			int foundnew = 0;
			if( mReg.getLong(loc+"_start", 0) < mReg.getLong(loc+"_saved", 0) ){
				Log.i(G,"sw() download ready("+(mReg.getLong(loc+"_saved", 0) -mReg.getLong(loc+"_start", 0) )+"ms)");
				Date d = new Date(mReg.getLong(loc+"_saved", 0));
				String ct = mReg.getString(loc, "");
				String lct = ct.toLowerCase();
				String[] ctl = null;
				
				if(!ct.contains("\n")){
					Log.i(G,"sw() f8("+ct+")");
					f8 = new File(ct);
					if(f8.exists()){
					
					try {
						hx4 = new FileInputStream(f8);
						
						byte[] bx = new byte[10240];
		                String o9 = "";
						
		                int wx = 3;
		                
		                
		                
		                for(wx = 0; wx < f8.length(); wx+=1024){
							if( hx4.read(bx, wx, 1024) > -1 ){
								o9 += new String(bx);
							}else{Log.e(G,"get split eof " + o9.length() + " of " + f8.length() + " bytes");break;}
							if(o9.indexOf(mReg.getString(loc+"_split", "<entry")) > -1){
							isplit = mReg.getString(loc+"_split", "<entry");
							break;	
							}else if(o9.indexOf(mReg.getString(loc+"_parse_item",August.parse_item)) > -1 ){
							isplit = mReg.getString(loc+"_parse_item",August.parse_item);
							break;
							}else if(o9.indexOf("<item") > -1){isplit = "<item";
							break;
							}else if(o9.indexOf("<html") > -1){isplit = "<html";
							break;
							}else if(o9.indexOf("<xml") > -1){isplit = "<xml";
							break;
							}else if(o9.indexOf("<body") > -1){isplit = "<body";
							break;
							}else{
							Log.e(G,"sw() noisplit error [continue]");
							}
						}
		                
		                if(isplit.length() == 0){
		                	Log.e(G,"isplit not happening what to do with ("+o9.length()+","+f8.length()+")");//("+Uri.encode(o9.substring(0,1024<o9.length()?1024:o9.length()-2))+")");
		                
		                }else{
		                	mEdt.putString(loc+"_split",isplit);
		                	mEdt.commit();
		                }
		                //String b7 = "";
		                //int bl2 = 0;
		                //int bx8 = 2;
		                int nxtspt = -2;
		                int nxtspt2 = -2;
		                int nxtspt3 = -2;
		                
		                //bl2 = (int) (bl2<f8.length()?bl2:f8.length()-1);
		                //{b7 = "";for(bx8 = bl2; bx8 <f8.length() ;bx8+=512){hx4.read(bx,bx8,512);b7+=new String(bx); if(b7.indexOf(isplit,isplit.length()) > -1 ){bx8 = bl2+b7.indexOf(isplit,isplit.length());bl2 = bx8; break;} }} 
		                //nxtspt = bx8;
		                
		                //run handler getting entries through nxtIndex
		               
		                for(bl2 = -1;bl2 < f8.length() && nxtspt < f8.length();){
		                	nxtspt = nxtIndex(isplit);
		                	nxtspt2 = nxtIndex(isplit);
		                	Log.i(G,"nxt("+nxtspt+","+nxtspt2+","+bl2+"<!----------------------------------------------------)"+f8.length());
		                }
		               // nxtspt3 = nxtIndex(isplit);
		               //int  nxtspt4 = nxtIndex(isplit);
		               // {Bundle bl = new Bundle(); bl.putString("isplit",isplit);Message ml = new Message(); ml.setData(bl); spliter.sendMessage(ml);}
		                
		                //try{b7 = "";for(bx8 = bl2+1; bx8 <f.length() ;bx8+=512){h.read(bx,bx8,bx8+512);b7+=new String(bx);if(b7.indexOf(isplit,isplit.length()) > -1 ){bx8 = bl2+b7.indexOf(isplit,isplit.length());bl2 = bx8; break;} }}
		                //catch(IndexOutOfBoundsException e){Log.e(G,"nxt123 seek("+bx8+") over file size("+f.length()+") " + e.getLocalizedMessage() );e.printStackTrace();}
		                //nxtspt3 = bx8;
		               
		                
		                //Log.i(G,"nxt123("+isplit+","+nxtspt+","+nxtspt2+","+nxtspt3+","+nxtspt4+")<!------------------------------- HAVEN");
		                Log.i(G,"nxt123("+(o9)+") ");
		                
		                byte[] bx3 = new byte[(int)f8.length()];
		                hx4.read(bx3,0,(int)f8.length());
						ct = new String(bx3);//beatimg
		                
						
						Log.i(G,"############## httpPage loaded("+f8.getAbsolutePath()+") " + ct.length() );
					}catch (OutOfMemoryError mme){
						Log.e(G,"OUT OF MEMORY 481");
						{Message ml = new Message(); Bundle bl = new Bundle(bdl);bdl.putInt("retryax",bl.getInt("retryax",1)); ml.setData(bl); serviceWork.sendMessageDelayed(ml,6000);}
						mme.printStackTrace();return;
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						{Message ml = new Message(); Bundle bl = new Bundle(bdl);bdl.putInt("retryax",bl.getInt("retryax",1)); ml.setData(bl); serviceWork.sendMessageDelayed(ml,6000);}

						e.printStackTrace();
						return;
	                } catch (IOException e) {
						// TODO Auto-generated catch block
						{Message ml = new Message(); Bundle bl = new Bundle(bdl);bdl.putInt("retryax",bl.getInt("retryax",1)); ml.setData(bl); serviceWork.sendMessageDelayed(ml,6000);}

	                	e.printStackTrace();
	                	return;
	                }
	                
					}
				}	
				
				
				{
				
				
				//ct = ct.replaceAll("<[eE][nN][tT][rR][yY]","<entry"); ct = ct.replaceAll("<[xX][mM][lL]","<xml");
				//ct = ct.replaceAll("<[hH][tT][mM][lL]","<html");ct=ct.replaceAll("<[bB][oO][dD][yY]","<body");ct=ct.replaceAll("<[iI][tT][eE][mM]","<item");
				
				
				//
				// CUSTOMIZED
				//
				//ct = ct.replaceAll("(%3C|<|&lt;)[sS][cC][rR][iI][pP][tT].*?(&gt;|>|%3E).*?(%3C|<|&lt;)(/|%2F)[sS][cC][rR][iI][pP][tT].*?(&gt;|>|%3E)","");
				//ct = ct.replaceAll("(%3C|<|&lt;)(!|%21)(-|%2D)(-|%2D).*?(-|%2D)(-|%2D)(&gt;|>|%3E)","");

				if( (ctl == null || ctl.length == 1) && ct.indexOf(isplit) > -1){ctl = ct.split(isplit);}
				if( (ctl == null || ctl.length == 1) && ct.indexOf(mReg.getString(loc+"_parse_item",August.parse_item)) > -1 ){ctl = ct.split(mReg.getString(loc+"_parse_item",August.parse_item));}
				if( (ctl == null || ctl.length == 1) && ct.indexOf("<entry") > -1){ctl = ct.split("<entry");mEdt.putString(loc+"_split","<entry");}
				if( (ctl == null || ctl.length == 1) && ct.indexOf("<body") > -1){ctl = ct.split("<body");mEdt.putString(loc+"_split","<body");}
				if(ctl == null){
					ctl = new String[]{ct};
				}
				// EC
				
				}
				
				mEdt.putLong(loc+"_size",ct.length());
				mEdt.commit();
				String desttitle = mReg.getString(loc+"_title", "");
				
				//{Message ml = new Message(); Bundle bl = new Bundle(); bl.putInt("id", headerText); bl.putString("text", "Downloaded " + (ct.length()>1024?ct.length()/1024+"Kb":ct.length()+"b")+" at " + (d.getHours() > 12?d.getHours()-12:d.getHours()) + ":" + (d.getMinutes()<10?"0":"") + d.getMinutes() + "\nContaining " + ctl.length); ml.setData(bl); setText.sendMessage(ml);}
				//ct = ct.replace("\r", "\n");
				
				if( ctl.length > 0){
				
					//mProgressDialog = ProgressDialog.show(mCtx, "Gratuitous Notification", "Loading", true);
					//{Message ml = new Message(); Bundle bl = new Bundle(); bl.putInt("max",ctl.length); bl.putString("titleOFF", "Notification"); bl.putBoolean("indeter", true); bl.putString("text", "Loading"); ml.setData(bl); mProgress.sendMessage(ml);}
					//{Message ml = new Message(); Bundle bl = new Bundle(); bl.putInt("max",ctl.length); ml.setData(bl); mProgressMax.sendMessage(ml);}
				Cursor cx=null;
				boolean found = true;


				ContentValues inc = new ContentValues();
				inc.put("location", dest);
				inc.put("prefilter", ct);
				inc.put("status",1);
				if(status < 10 && source!=null && source.getLastPathSegment().matches("[0-9]+")){
					//ContentValues inc = new ContentValues();
					//inc.put("location", dest);
					//inc.put("prefilter", ct);
					SqliteWrapper.update(mCtx,getContentResolver(), Uri.withAppendedPath(DataProvider.CONTENT_URI, "filter"), inc, "_id = "+source.getLastPathSegment(), null );		
					Log.i(TAG,"Updating " + source.toString());
				}else{
					source = SqliteWrapper.insert(mCtx, getContentResolver(), Uri.withAppendedPath(DataProvider.CONTENT_URI, "filter"), inc);
					Log.i(TAG,"Created " + source.toString());
				}
				
				Uri contentpath = Uri.withAppendedPath(DataProvider.CONTENT_URI,"moment");
				ContentValues cv = new ContentValues();
				String title, published, content, url, author;Uri geturi;
				String parse_link = mReg.getString(loc+"_parse_link", August.parse_link);
				String parse_title = mReg.getString(loc+"_parse_title", August.parse_title);
				String parse_author = mReg.getString(loc+"_parse_author", August.parse_author);
				String parse_author2 = mReg.getString(loc+"_parse_author2", August.parse_author2);
				String parse_content = mReg.getString(loc+"_parse_content", August.parse_content);
				String parse_summary = mReg.getString(loc+"_parse_summary", August.parse_summary);
				String parse_published = mReg.getString(loc+"_parse_published", August.parse_published);
				String parse_lastbuild = mReg.getString(loc+"_parse_lastbuild", August.parse_lastbuild);
				String momentd = "";int hush = 0;String momentdf = "";String plitt = mReg.getString(loc+"_split","");
				CharBuffer cbuf;CharsetDecoder decoder;CharsetEncoder encoder;ByteBuffer bbuf;
				long gm7 = 2;
				for(int b = 0; b < ctl.length; b++){if(ctl[b].length() == 0){continue;}
					ctl[b] = plitt + " " + ctl[b];
					
					momentdf = ctl[b];
					momentd = ctl[b].toLowerCase().replaceAll("(\n|\r)", " ");
					//Log.i(G,ctl[b]+"<------");
					
					//Log.i(TAG,"["+b+"]("+Uri.encode(momentd)+")" );
					if( (gm7+5000) < System.currentTimeMillis() ){
						gm7 = System.currentTimeMillis();mEdt.putLong(loc+"_working",gm7);mEdt.commit();
					}
					// && foundnew <= mReg.getInt("August_naturalLimit", August.naturalLimit)
					
				//
				// CUSTOMIZED
				//
				
				// Link
				
				if(parse_link.contains("\n")){
					if(parse_link.split("\n")[0].length() == 0){url = ctl[b];}
					else{url = ctl[b].replaceAll(".*?"+parse_link.split("\n")[0],"");}
					url = url.replaceAll(parse_link.split("\n")[1]+".*", "");
				}else if(ctl[b].indexOf("<"+parse_link+">") > -1){
					//ctl[b].substring(ctl[b].indexOf(">",ctl[b].indexOf("<"+August.parse_content))+1, ctl[b].indexOf("</"+August.parse_content+">"));
					//url = ctl[b].substring(ctl[b].indexOf("<"+parse_link+">")+(parse_link.length()+2), ctl[b].indexOf("</"+parse_link+">"));
					url = ctl[b].substring(ctl[b].indexOf('>', ctl[b].indexOf(parse_link))+1, ctl[b].indexOf("</"+parse_link+">"));
				}
				else{ 
				//	int p = ctl[b].indexOf("href=\"",ctl[b].indexOf("<"+parse_link))+6;
				//	url = ctl[b].substring(p,ctl[b].indexOf(">",p));
					url = ctl[b].replaceAll(".* [hH][rR][eE][fF].*?=.*?(\"|')","").replaceAll("(\"|'|>).*","");
				}
				if(url.indexOf('"') > 3){url = url.replaceAll("\".*", "");}
				if(url.indexOf("CDATA[") > -1){url = url.substring(url.indexOf("CDATA[")+6, url.lastIndexOf("]]"));}
				
				/*/
				// Author
				if(ctl[b].indexOf("<"+August.parse_author+">") > -1){
					author = ctl[b].substring(ctl[b].indexOf("<"+August.parse_author+">")+(August.parse_author.length()+2), ctl[b].indexOf("</"+August.parse_author+">"));
				}else if(ctl[b].indexOf("<"+August.parse_author2+">") > -1){
					author = ctl[b].substring(ctl[b].indexOf("<"+August.parse_author2+">")+(August.parse_author2.length()+2), ctl[b].indexOf("</"+August.parse_author2+">"));
				}else{author = "";}
				if(author.indexOf("CDATA[") > -1){author = author.substring(author.indexOf("CDATA[")+6, author.lastIndexOf("]]"));}
				
				//if(ctl[b].indexOf("<"+August.parse_published+">") > -1){ published = fixDate(ctl[b].substring(ctl[b].indexOf("<"+August.parse_published+">")+(August.parse_published.length()+2), ctl[b].indexOf("</"+August.parse_published+">")));}else{published = "unavail";}
				
				// Link
				if(ctl[b].indexOf("<"+August.parse_link+">") > -1){
				url = ctl[b].substring(ctl[b].indexOf("<"+August.parse_link+">")+(August.parse_link.length()+2), ctl[b].indexOf("</"+August.parse_link+">"));
				}else{int p = ctl[b].indexOf("href=\"",ctl[b].indexOf("<"+August.parse_link))+6; url = ctl[b].substring(p,ctl[b].indexOf(">",p));}
				if(url.indexOf('"') > 3){url = url.replaceAll("\".*", "");}
				if(url.indexOf("CDATA[") > -1){url = url.substring(url.indexOf("CDATA[")+6, url.lastIndexOf("]]"));}
				//*/
				
				

				/*
				// Title
				title = ctl[b].substring(ctl[b].indexOf('>', ctl[b].indexOf(August.parse_title))+1, ctl[b].indexOf("</"+August.parse_title+">"));
				title = Uri.decode(title).trim();
				title = title.replaceAll("&#039;","'");								
				title = title.replaceAll("&#39;","'");
				title = title.replaceAll("&#34;","'");title = title.replaceAll("&#x27;","'");
				title = title.replaceAll("&#8217;","'");title = title.replaceAll("&#187;",":");
				title = title.replaceAll("&quot;","\"").replaceAll("&apos;","'");
				title = title.replaceAll("\"", "'");
				if(August.dest.indexOf("twitter.com") > -1 && title.indexOf(':') > -1 && title.length() > title.indexOf(':')+3 ){title = title.substring(title.indexOf(':')+2);}
				if(title.indexOf("CDATA[") > -1){title = title.substring(title.indexOf("CDATA[")+6, title.lastIndexOf("]]"));}
//*/
				// Title
				title = momentd;
				//if(b == 0){
				//	if(title.contains("</title>") ){
				//		title = ctl[b].substring(title.indexOf('>', ctl[b].indexOf("title"))+1, ctl[b].indexOf("</"+"title"+">"));
				//		//title = title.replaceAll("</title>.*","");//[tT][iI][tT][lL][eE]>.*", "");
				//		//title = title.replaceAll(".*<title.*?>","");//<[tT][iI][tT][lL][eE]>", "");
				//	}else if(title.contains("</atom:link.*?>")){
				//		title = ctl[b].substring(title.indexOf('>', ctl[b].indexOf("atom:link"))+1, ctl[b].indexOf("</"+"atom:link"+">"));
				//		//title = title.replaceAll("<.*?link>.*","");
				//		//title = title.replaceAll(".*?<atom:link.*?>","");
				//	}else{
				//		Log.e(TAG,"document title missing\n" + ctl[b]);
				//	}
				//		
				//	
				//}else{
					if(parse_title.contains("\n")){
						if(parse_title.split("\n")[0].length() == 0){title = ctl[b];}
						else{title = ctl[b].replaceAll(".*?"+parse_title.split("\n")[0],"");}
						title = title.replaceAll(parse_title.split("\n")[1]+".*", "");
					}else if(ctl[b].indexOf("</"+parse_title+">") > -1){
						title = ctl[b].substring(title.indexOf('>', ctl[b].indexOf(parse_title))+1, ctl[b].indexOf("</"+parse_title+">"));
					}else if(title.contains(parse_title+">")){title = title.substring(title.indexOf(parse_title+">")+parse_title.length()+1);}
					
					
					// Seattle's The Stranger uses encoding for the title
					//title = title.replaceAll("&quot;", "\"");
				//}
				title = Uri.decode(title).trim();	
				title = title.replaceAll("&amp;","&").replaceAll("&nbsp;"," ");
				title = title.replaceAll("&quot;","\"").replaceAll("&apos;","'").replaceAll("&lt;","<").replaceAll("&gt;",">");
				
				try{
					String cset = mReg.getString(loc+"_charset","ISO-8859-1");
					Charset charset = Charset.forName(cset);
					decoder = charset.newDecoder();
					encoder = charset.newEncoder();
					bbuf = ByteBuffer.wrap(title.getBytes());
					CharBuffer c2;
					c2 = decoder.decode(bbuf);	
				
					title = c2.toString();
				
					
					if(!cset.matches(".*8859-1")){
						title = Uri.decode(title).trim();//tripple
						title = title.replaceAll("&amp;","&").replaceAll("&nbsp;"," ");
						title = title.replaceAll("&quot;","\"").replaceAll("&apos;","'").replaceAll("&lt;","<").replaceAll("&gt;",">");
								
						charset = Charset.forName("ISO-8859-1");
						ByteBuffer b3 = ByteBuffer.wrap(title.getBytes());
						CharBuffer c3; c3 = decoder.decode(b3);
						title = c3.toString();
					
					}
				
				
				} catch (CharacterCodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				title = Uri.decode(title).trim();//double
				title = title.replaceAll("&amp;","&").replaceAll("&nbsp;"," ");
				title = title.replaceAll("&quot;","\"").replaceAll("&apos;","'").replaceAll("&lt;","<").replaceAll("&gt;",">");//beatimg-log
				
				
				
				//title = title.replaceAll("&#039;","'");								
				//title = title.replaceAll("&#39;","'");title = title.replaceAll("&#38;","&");title = title.replaceAll("&#x27;","'");
				//title = title.replaceAll("&#34;","'");
				//title = title.replaceAll("&#124;",":");title = title.replaceAll("&#187;",":");
				//title = title.replaceAll("&#8217;","'");title = title.replaceAll("&#8216;","'");title = title.replaceAll("&#8212;","/");
				//title = title.replaceAll("&#[0-9]+;","");
				if(title.matches("&([a-zA-Z])+;")){Log.e(G,"##############)Found One In ("+Uri.encode(title)+")");}
				if(dest.indexOf("twitter.com") > -1 && title.indexOf(':') > -1 && title.length() > title.indexOf(':')+3 ){title = title.substring(title.indexOf(':')+2);}
				if(title.indexOf("CDATA[") > -1){title = title.substring(title.indexOf("CDATA[")+6, title.lastIndexOf("]]"));}
				
				title = title.replaceAll("<[a-zA-Z].*?>", "");
				
				if(b == 0 && !title.contains(desttitle)){
					mEdt.putString(loc+"_title",title);
					mEdt.putString("bucket_title",title);
					mEdt.putString("sourcetitle",title);mEdt.commit();
				}
				
				if(dest.indexOf("dealextreme.com") > -1 ){
					url = url.replaceAll("99999999","89653085");
				
				}
				
				

				// Published
				if(parse_published.contains("\n")){
					
						if(parse_published.split("\n")[0].length() == 0){published = ctl[b];}
					else{published = ctl[b].replaceAll(".*?"+parse_published.split("\n")[0],"");}
					published = published.replaceAll(parse_published.split("\n")[1]+".*", "");
				}else if(ctl[b].indexOf("<"+parse_published+">") > -1){	
					published = fixDate(ctl[b].substring(ctl[b].indexOf("<"+parse_published+">")+(parse_published.length()+2), ctl[b].indexOf("</"+parse_published+">")));
					//Log.e(G,"Using found published("+published+") <"+August.parse_published+">");
				}else if(ctl[b].indexOf("<"+parse_lastbuild+">") > -1){
					published = fixDate(ctl[b].substring(ctl[b].indexOf("<"+parse_lastbuild+">")+(parse_lastbuild.length()+2), ctl[b].indexOf("</"+parse_lastbuild+">")));
					//Log.e(G,"Using found published("+published+") <"+August.parse_lastbuild+">");
					//mEdt.putString("sourcetitle",title);mEdt.commit();{Message ml = new Message(); Bundle bl = new Bundle(); bl.putString("text",title); ml.setData(bl); mProgressTitle.sendMessage(ml);}
				}else if(ctl[b].indexOf("<dc:date>") > -1){
					published = fixDate(ctl[b].substring(ctl[b].indexOf("<dc:date>")+(new String("<dc:date>").length()), ctl[b].indexOf("</dc:date>")));
					//Log.e(G,"Using found published("+published+") <dc:date>");
					//mEdt.putString("sourcetitle",title);mEdt.commit();{Message ml = new Message(); Bundle bl = new Bundle(); bl.putString("text",title); ml.setData(bl); mProgressTitle.sendMessage(ml);}
				}else if(ctl[b].indexOf("<published>") > -1){
					published = fixDate(ctl[b].substring(ctl[b].indexOf("<published>")+(new String("<published>").length()), ctl[b].indexOf("</published>")));
					//Log.e(G,"Using found published("+published+") <published>");
					//mEdt.putString("sourcetitle",title);mEdt.commit();{Message ml = new Message(); Bundle bl = new Bundle(); bl.putString("text",title); ml.setData(bl); mProgressTitle.sendMessage(ml);}
				}else{published = datetime();}//Log.e(TAG,"Using current date. because unable to locate <"+parse_published+"> --- second thought, skipping these");String[] pp = ctl[b].split("\n");for(int p = 0; p < pp.length; p++){Log.i(TAG,"DEBUG: " + pp[p]);} }
				//Log.i(G,title+" published(" + published+")");
				
				//2010-10-04 if(b == 0){mEdt.putString("sourcetitle",title);mEdt.commit(); {Message ml = new Message(); Bundle bl = new Bundle(); bl.putString("text",title); ml.setData(bl); mProgressTitle.sendMessage(ml);}}
				
				
				
				
				// EC
					
					//DEBUG
					//Log.w(G,"thread("+this.getId()+") title("+title+") published("+published+") author(unavil) url("+url+") content("+content+")");
					
					found = false;
					if(mFoo > 5280){ SystemClock.sleep(2000);hush++;} fooTest.sendEmptyMessageDelayed((int)SystemClock.uptimeMillis(),3000);
					//
					// CUSTOMIZED
					//
					try {
					cx = SqliteWrapper.query(mCtx, mCtx.getContentResolver(), contentpath, new String[] {"_id"}, "title = \""+Uri.encode(title)+"\" AND url = \""+url+"\" AND strftime('%s',published) = strftime('%s','"+published+"')", null, null);
					} catch (SQLiteException e){Log.e(TAG,"SQL Error:" + e.getLocalizedMessage());}
					// EC
					
					if( cx != null){if( cx.moveToFirst() ){ if( cx.getInt(0) > 0){ found=true;}; } cx.close();}
					if(!found){
						
						
						// author
						if(parse_author.contains("\n")){
							if(parse_author.split("\n")[0].length() == 0){author = ctl[b];}
							else{author = ctl[b].replaceAll(".*?"+parse_author.split("\n")[0],"");}
							author = author.replaceAll(parse_author.split("\n")[1]+".*", "");
						}else if(ctl[b].indexOf("<"+parse_author+">") > -1){
							author = ctl[b].substring(ctl[b].indexOf("<"+parse_author+">")+(parse_author.length()+2), ctl[b].indexOf("</"+parse_author+">"));
						}else if(ctl[b].indexOf("<"+parse_author2+">") > -1){
							author = ctl[b].substring(ctl[b].indexOf("<"+parse_author2+">")+(parse_author2.length()+2), ctl[b].indexOf("</"+parse_author2+">"));
						}else{author = "";}
						if(author.indexOf("CDATA[") > -1){author = author.substring(author.indexOf("CDATA[")+6, author.lastIndexOf("]]"));}
						

						// Content
						//Log.w(G,"Parse content("+parse_content+")");
						if(parse_content.contains("\n")){
							if(parse_content.split("\n")[0].length() == 0){content = ctl[b];}
						else{content = ctl[b].replaceAll(".*?"+parse_content.split("\n")[0],"");}	
							content = content.replaceAll(parse_content.split("\n")[1]+".*", "");
						}else if(ctl[b].indexOf("<"+parse_content+"") > 0 && ctl[b].indexOf("</"+parse_content+"") > 0){content = ctl[b].substring(ctl[b].indexOf(">",ctl[b].indexOf("<"+parse_content))+1, ctl[b].indexOf("</"+parse_content+">"));
						}else if(ctl[b].indexOf("<"+parse_summary+">") > 0 && ctl[b].indexOf("</"+parse_summary+">") > 0){content = ctl[b].substring(ctl[b].indexOf("<"+parse_summary+">")+(parse_summary.length()+2), ctl[b].indexOf("</"+parse_summary+">")); 
						}else if(ctl[b].indexOf("<subtitle>") > 0 && ctl[b].indexOf("</subtitle>") > 0){content = ctl[b].substring(ctl[b].indexOf("<subtitle>")+(new String("subtitle").length()+2), ctl[b].indexOf("</subtitle>"));
						}else if(ctl[b].indexOf("<summary>") > 0 && ctl[b].indexOf("</summary>") > 0){content = ctl[b].substring(ctl[b].indexOf("<summary>")+(new String("summary").length()+2), ctl[b].indexOf("</summary>"));
						}else if(ctl[b].indexOf("<content") > 0 && ctl[b].indexOf("</content") > 0){content = ctl[b].substring(ctl[b].indexOf('>', ctl[b].indexOf("<content"))+1, ctl[b].indexOf("</content>"));
						}else if(ctl[b].indexOf("<subtitle") > 0 && ctl[b].indexOf("</subtitle") > 0){content = ctl[b].substring(ctl[b].indexOf('>', ctl[b].indexOf("<subtitle"))+1, ctl[b].indexOf("</subtitle>"));
						}else if(ctl[b].indexOf("<summary") > 0 && ctl[b].indexOf("</summary") > 0){content = ctl[b].substring(ctl[b].indexOf('>', ctl[b].indexOf("<summary"))+1, ctl[b].indexOf("</summary>"));
						}else{content="unavail";}
						//content = content.replaceAll("&#8230;","");// CC Insider
						
						
						if(August.dest.indexOf("dealextreme.com") > -1 ){content = content.replaceAll("99999999","89653085");}
						if(August.dest.indexOf("flickr.com") > -1 && content.indexOf("posted a photo:") > -1 ){content = content.replaceFirst(".*?posted a photo:", "");}
						
						

						/*
						// Content
						if(ctl[b].indexOf("<"+August.parse_content+"") > 0){content = ctl[b].substring(ctl[b].indexOf(">",ctl[b].indexOf("<"+August.parse_content))+1, ctl[b].indexOf("</"+August.parse_content+">"));
						}else if(ctl[b].indexOf("<"+August.parse_summary+">") > 0){content = ctl[b].substring(ctl[b].indexOf("<"+August.parse_summary+">")+(August.parse_summary.length()+2), ctl[b].indexOf("</"+August.parse_summary+">")); 
						}else if(ctl[b].indexOf("<summary>") > 0){content = ctl[b].substring(ctl[b].indexOf("<summary>")+(new String("summary").length()+2), ctl[b].indexOf("</summary>"));
						}else if(ctl[b].indexOf("<content") > 0){content = ctl[b].substring(ctl[b].indexOf('>', ctl[b].indexOf("<content"))+1, ctl[b].indexOf("</content>"));
						}else if(ctl[b].indexOf("<subtitle") > 0){content = ctl[b].substring(ctl[b].indexOf('>', ctl[b].indexOf("<subtitle"))+1, ctl[b].indexOf("</subtitle>"));
						}else if(ctl[b].indexOf("<summary") > 0){content = ctl[b].substring(ctl[b].indexOf('>', ctl[b].indexOf("<summary"))+1, ctl[b].indexOf("</summary>"));
						}else{content="unavail";}
						
						// Published
						if(ctl[b].indexOf("<"+August.parse_published+">") > -1){	
							published = fixDate(ctl[b].substring(ctl[b].indexOf("<"+August.parse_published+">")+(August.parse_published.length()+2), ctl[b].indexOf("</"+August.parse_published+">")));
						}else if(ctl[b].indexOf("<"+August.parse_lastbuild+">") > -1){
							published = fixDate(ctl[b].substring(ctl[b].indexOf("<"+August.parse_lastbuild+">")+(August.parse_lastbuild.length()+2), ctl[b].indexOf("</"+August.parse_lastbuild+">")));
						}else if(ctl[b].indexOf("<dc:date>") > -1){
							published = fixDate(ctl[b].substring(ctl[b].indexOf("<dc:date>")+(new String("<dc:date>").length()), ctl[b].indexOf("</dc:date>")));
						}else if(ctl[b].indexOf("<published>") > -1){
							published = fixDate(ctl[b].substring(ctl[b].indexOf("<published>")+(new String("<published>").length()), ctl[b].indexOf("</published>")));
						}else{published = datetime();Log.e(TAG,"Using current date. because unable to locate <"+August.parse_published+"> --- second thought, skipping these"); }
//						if(b == 0){mEdt.putString("sourcetitle",title);mEdt.commit();}
						//*/
						


						
						
						
						try {
						    // Convert a string to ISO-LATIN-1 bytes in a ByteBuffer
						    // The new ByteBuffer is ready to be read.
						    //ByteBuffer bbuf = encoder.encode(CharBuffer.wrap("a string"));
							
							Charset charset = null; 
							charset =Charset.forName("ISO-8859-1");
							decoder = charset.newDecoder();
							encoder = charset.newEncoder();    
							bbuf = ByteBuffer.wrap(content.getBytes());
						    // Convert ISO-LATIN-1 bytes in a ByteBuffer to a character ByteBuffer and then to a string.
						    // The new ByteBuffer is ready to be read.
						    cbuf = decoder.decode(bbuf);
						    content = cbuf.toString();
							
						    
						} catch (CharacterCodingException e) {
						Log.e(G,"CCE MAN " + e.getLocalizedMessage());
						}
						
						
						//content = content.replaceAll("&#[0-9]+;","");
						//content = content.replaceAll("&amp;","&").replaceAll("&lt;","<").replaceAll("&gt;",">");
						//content = content.replaceAll("&quot;","\"").replaceAll("&apos;","'");
						//content = content.replaceAll("&([a-z]|[a-z])+;", "");
						

						foundnew ++;
						cv.put("title", Uri.encode(title));
						cv.put("url", url);
						cv.put("published", published);
						
						cv.put("content", content);
						cv.put("status", 1);
						cv.put("prefilter", momentdf);
						cv.put("source",source.toString());
						//cv.put("author", author);
						geturi = SqliteWrapper.insert(mCtx, mCtx.getContentResolver(), contentpath, cv);
						
						//Log.w(TAG,"Inserted("+mFoo+") New " + geturi.toString() + " " + title.replaceAll("\n"," ") + " ("+content.replaceAll("\n"," ")+")");
						
						if(mFoo > 5380){ SystemClock.sleep(2000);hush++;} fooTest.sendEmptyMessageDelayed((int)SystemClock.uptimeMillis(),3000);
						
						//
						// CUSOMIZED
						//
						
						// EC
						if( (foundnew == 1 && b != 0) || b==1){setEntryNotification(TAG + " getlist", geturi, Uri.decode(title), mReg.getString("sourcetitle", August.title));}
					
					}else{geturi = Uri.withAppendedPath(contentpath, "#"+found);}
					
					//{Message ml = new Message(); Bundle bl = new Bundle(); bl.putInt("progress",b+1); ml.setData(bl); mProgressPlus.sendMessage(ml);}
				}
				//mProgressOut.sendEmptyMessage(2);
				//{Message ml = new Message(); Bundle bl = new Bundle(); bl.putInt("id", headerText); bl.putString("text", "Downloaded " + (ct.length()>1024?ct.length()/1024+"Kb":ct.length()+"b")+" at " + (d.getHours() > 12?d.getHours()-12:d.getHours()) + ":" + (d.getMinutes()<10?"0":"") + d.getMinutes() + "\nContaining " + ctl.length + ", " + foundnew + " New"); ml.setData(bl); setText.sendMessage(ml);}
				
				Log.w(TAG,"Hushed " + hush + " times");
				
				}
			}
			
			
			mEdt.putLong(loc+"_new",foundnew);
			mEdt.putLong(loc+"_done",System.currentTimeMillis());
			mEdt.commit();
	
			
			{Message ml = new Message(); Bundle bl = new Bundle(); bl.putLong("ms", SystemClock.uptimeMillis()); ml.setData(bl); getlatest.sendMessage(ml);}
			
			
			}};mt.start();
			
		}
	};
	
	long mFoo = -2;
	
	Handler fooTest = new Handler(){public void handleMessage(Message msg){mFoo = SystemClock.uptimeMillis() - (long)msg.what;}};
	

	Uri source = null;int sourceid = -1;int status = -1;String[] workorder;int workat = -10;
	Handler getlist = new Handler(){
    	private long hover = 0;
    	public void handleMessage(Message msg){
    		
    		if(hover > SystemClock.uptimeMillis()){
    			Log.e(TAG,"################## Started 5700>" + (hover - SystemClock.uptimeMillis()) + "ms ago.");return;
    		}
    		hover = SystemClock.uptimeMillis() + 5700;
    		
    		final Bundle bdl = msg.getData();
    		
    		final String dest = bdl.getString("dest");
			final String loc = bdl.getString("storloc");
			final String titlr = bdl.getString("title");
			final String procg = bdl.getString("procg");
			
    		
    		SharedPreferences mReg = getSharedPreferences("Preferences", MODE_WORLD_WRITEABLE);
			Editor mEdt = mReg.edit();
			mEdt.putLong("bucket_start", System.currentTimeMillis());
			
			mEdt.putLong(loc+"_start", System.currentTimeMillis());mEdt.commit();
    		//Log.i(TAG,"getlist");
    		
    		
    		
    		//"_start"
    		{Message ml = new Message(); Bundle bl = new Bundle(bdl); ml.setData(bl); mGet.sendMessage(ml);}
    		{Message ml = new Message(); Bundle bl = new Bundle(bdl); ml.setData(bl); serviceWork.sendMessageDelayed(ml,1000);}
			
			
    		
    		if(mReg.contains("notifier")){
				//NotificationManager mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
				//mNM.cancelAll();
			}
			
    		
    		
    		
    		
    		
    		//mNM.cancelAll();
    		
    		//Thread tx = new Thread(){
    			//public void run(){
    				//long st = System.currentTimeMillis();
    					
    					//SharedPreferences mReg = getSharedPreferences("Preferences", MODE_WORLD_WRITEABLE);
        					
        					
        					//Editor mEdt = mReg.edit();mEdt.remove("notifier");mEdt.commit();
        				
    					
    					//{Message ml = new Message(); Bundle bl = new Bundle(); bl.putInt("id", headerText); bl.putString("text", "Running"); ml.setData(bl); setText.sendMessage(ml);}
    				
    					/*/
    					try {
							long freememory = 0;
    						for(int i = 0; i < 300; i++){	
    							freememory = Runtime.getRuntime().freeMemory();
								//Log.i(TAG,"processing [" + i + "] freememory("+freememory+")");
								
								if(mReg.getLong("bucket_start", 0) > mReg.getLong("bucket_saved", 0) && mReg.getLong("bucket_start", 0) > mReg.getLong("bucket_error", 0)){
							//		{Message ml = new Message(); Bundle bl = new Bundle(); bl.putInt("id", headerText); bl.putString("text", "Running " + ((System.currentTimeMillis() - st)/1000) + " seconds."); ml.setData(bl); setText.sendMessageDelayed(ml,pRate);}
									Thread.sleep(750);
									continue;
								}else{
									
									
								}
								break;
							}
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}//*/
    				
						
						
					//}
    		//};
    		//tx.start();
    	}	
    };

public void setEntryNotification(String who, Uri geturi, String title, String summary){
		
	
	//summary = summary.replaceAll("&lt;", "<").replaceAll("&gt;", ">").replaceAll("&amp;", "&");
	if(summary.length() > 20 && summary.indexOf("CDATA[") == 3){summary = summary.substring(9, summary.length() - 3);}
		
		summary = summary.replaceAll("<.*?>", "").trim();
		
		
		//
		// CUSOMIZED
		//
		Notification notif = new Notification(August.notifyimage, title + " -- " + summary, System.currentTimeMillis()); // This text scrolls across the top.
		Intent intentJump2 = new Intent(mCtx,com.havenskys.galaxy.Lookup.class);
		// EC
		intentJump2.putExtra("uri", geturi.toString());
		intentJump2.putExtra("title", title);
		intentJump2.putExtra("moment", Integer.parseInt(geturi.getLastPathSegment()));
		PendingIntent pi2 = PendingIntent.getActivity(this, 0, intentJump2, Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS | Intent.FLAG_ACTIVITY_NO_HISTORY);
        //PendingIntent pi2 = PendingIntent.getActivity(mContext, 0, intentJump2, Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_FROM_BACKGROUND | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED | Intent.FLAG_ACTIVITY_MULTIPLE_TASK );
        
        //if( syncvib != 3 ){ // NOT OFF
        	//notif.defaults = Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE;
        //}else{
        notif.defaults = Notification.DEFAULT_LIGHTS;
        //}
        notif.ledARGB = Color.argb(255, 250, 10, 250);
		notif.setLatestEventInfo(mCtx, title, summary, pi2); // This Text appears after the slide is open
		
		//Date da = new Date();if(da.getHours() < 10){syncvib = 3;}
		
		SharedPreferences mReg = getSharedPreferences("Preferences", MODE_WORLD_WRITEABLE);
		Editor mEdt = mReg.edit();mEdt.putBoolean("notifier",true);mEdt.commit();
		mNM.notify(1, notif);
	}

    
    private DefaultHttpClient mHC;
	public Handler mGet = new Handler(){
		public void handleMessage(Message msg){Bundle bx = msg.getData();mget2(bx);}
		private void mget2(final Bundle bx){if(mHC == null){mHC = new DefaultHttpClient();}
		final String dest = bx.getString("dest");
		final String loc = bx.getString("storloc");
		final String titlr = bx.getString("title");final String procg = bx.getString("procg");
		final Bundle bdl = new Bundle(bx);
		if( dest == null || dest.length() == 0 ){
			Log.e(TAG,"Blocked empty get request: Destination titled " + titlr + " intended to " + loc);
			return;
		}
		
		Thread mt = new Thread(){
			
			public void run(){SharedPreferences mReg = getSharedPreferences("Preferences", MODE_WORLD_WRITEABLE);Editor mEdt = mReg.edit();
			/*if(mReg.contains(loc) && mReg.contains(loc+U_SAVED)){
				if( mReg.getLong(loc+U_SAVED, 10) > (System.currentTimeMillis()-bx.getLong("age",18000))){
					{Message mxm = new Message(); Bundle bxb = new Bundle(); bxb.putString("title",procg + " " + titlr);bxb.putString("subtitle",(int)(System.currentTimeMillis() - mReg.getLong(loc+U_SAVED, 33))/1000+" Second Cache " + titlr +" for "+loc+".\n"+dest ); mxm.setData(bxb);easyViewerHandler.sendMessageDelayed(mxm,10);}
					{Message mxx = new Message();mxx.setData(bx);taskDone.sendMessageDelayed(mxx, 30);}
					return;
				}
			}//*/
			
		
			final String dest = bdl.getString("dest");String who = bdl.getString("who");
			final String loc = bdl.getString("storloc");
			final String titlr = bdl.getString("title");String procg = bdl.getString("procg");
	
			
			Log.i(TAG,"Acquiring " + titlr +"\n"+dest);
			//{Message mxm = new Message(); Bundle bxb = new Bundle(); bxb.putString("title",procg);bxb.putString("subtitle", ); mxm.setData(bxb);easyViewerHandler.sendMessageDelayed(mxm,10);}
			
			
		final long sh = SystemClock.uptimeMillis();
		
		//mUrl = dest;
		
		//HttpGet httpget = new HttpGet(Uri.parse(dest).toString());
		//String mUrl = httpget.getURI().toString();
		
		//Log.w(G,"safeHttpGet() 1033 getURI("+httpget.getURI()+") for " + who);
		//if( httpget.getURI().toString() == "" ){
		if(dest.length() == 0){
			Log.e(TAG,"Blocked empty destination get.");
			return;
		}
		
		String responseCode = ""; //String mHP = "";
		//CookieStore c = mHC.getCookieStore();
		//mHC = new DefaultHttpClient();mHC.setCookieStore(c);
		CookieStore cs = (mHC != null) ? mHC.getCookieStore(): new DefaultHttpClient().getCookieStore();
		DefaultHttpClient mHC = new DefaultHttpClient();
		//SharedPreferences mReg = mCtx.getSharedPreferences("Preferences", MODE_WORLD_WRITEABLE);Editor mEdt = mReg.edit();
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
			//{Message mxm = new Message(); Bundle bxb = new Bundle(); bxb.putString("text","Downloading"); mxm.setData(bxb);easyStatusHandler.sendMessageDelayed(mxm,10);}
			
			
			
			//HttpResponse mHR = mHC.execute(httpget);
			
			//reply[2] = mReg.getString(loc+"url", mUrl);mUrl = reply[2];
			{
	//		if( mHR != null ){
		     //   Log.w(TAG,"safeHttpGet() 436 " + mHR.getStatusLine() + " " + " for " + who);
				//{Message mxm = new Message(); Bundle bxb = new Bundle(); bxb.putString("text","Server says "+mHR.getStatusLine().getStatusCode() + " "+mHR.getStatusLine().getReasonPhrase()); mxm.setData(bxb);easyStatusHandler.sendMessageDelayed(mxm,10);}
				//easyStatus(mHR.getStatusLine().getStatusCode() + " " + mHR.getStatusLine().getReasonPhrase());
				
		        //Log.w(TAG,"safeHttpGet() 440 response.getEntity() for " + who);
		        //HttpEntity mHE = mHR.getEntity();
	
		        {
		        //if (mHE != null) {
			        //byte[] bytes = ;
		        	//Log.w(TAG,"Downloaded into RAM " + mHE.getContentLength());
		        	Log.w(TAG,"safeHttpGet() 445 byte[] to EntityUtils.toByteArray(mHE) expect 448");
		        	//String mhpb = EntityUtils.toString(mHE);
		        	
		        	Log.i(G,"######## destination (" + dest + ") ");
		    		String mhpb = getPage(loc,dest,"mGet() 931");
		    		
		        	
		        	
		        	//mhpb = Uri.encode(mhpb);
		        	Log.w(TAG,"safeHttpGet() 448 mhpb("+mhpb.length()+"bytes raw) to String for " + who);
		        	/*
		        	{//%C2

		        	//InputStream is = mHE.getContent();
					//byte[] bh;
					
				//	for(byte[] hb = null; is.read(hb) > -1; ){
						//Log.w(TAG,"input stream read " + hb.length);
					//}
		        		//,"[A-Za-z0-9\n .?!]"
		        		
		        		
		        		//Log.i(TAG,"####################     Content Type ("+mHE.getContentType()+")");
		        		

		        		//if(mHE.isChunked()){Log.w(TAG,"CHUNCKED CONTENT");}
		        		mhpb = mhpb.replaceAll("\t"," ").replaceAll(" ( )+"," ").replaceAll("\r","\n").replaceAll("\n( )+","\n").replaceAll("\n(\n)+","\n");
		        		mhpb = Uri.encode(mhpb,"[A-Za-z0-9 .!\"'<>]");
		        		
		        		String bn[];
		        		bn = mhpb.split("%0A");
		        		
		        		//if(mhpb.matches(".*%(0[0-9BCEF]|1[0-9A-Z]|[8-9A-F]|7F).*")){
			        		//Uri.encode(mhpb)
		        			{	
		        			//mhpb = "";
			        		for(int u=0;u <bn.length;u++){
		        			//bn[u] = Uri.encode(bn[u]);
			        		String g[] = bn[u].replaceAll("%20"," ").replaceAll("%1","\n%1").replaceAll("%7F","\n%7F").replaceAll("%0","\n%0").replaceAll("%8","\n%8").replaceAll("%9","\n%9").replaceAll("%A","\n%A").replaceAll("%B","\n%B").replaceAll("%C","\n%C").replaceAll("%D","\n%D").replaceAll("%E","\n%E").replaceAll("%F","\n%F").split("\n");
			        		bn[u] = bn[u].replaceAll("%[8-9A-F][0-9A-F]","");	
			        			for(int gi=0;gi<g.length;gi++){
		        				if(g[gi].matches("%(1|0[0-8B-CEF]|[8-9A-F]|7F).*")){
	        					Log.w(TAG,"["+u+"]["+gi+"]"+g[gi]+"[]");
	        					//if(gi > 0){Log.w(TAG,"["+u+"]["+(gi-1)+"]"+g[gi-1]+"[]");}
	        					
	        					//if(gi < g.length-1){Log.w(TAG,"["+u+"]["+(gi+1)+"]"+g[gi+1]+"[]");}
	        					//g[gi] = g[gi].replaceAll("%[8-9A-F][0-9A-F]","");
		        				}else{
	        				//Log.i(TAG,"["+u+"]["+gi+"]"+g[gi]+"[]");
		        				}
		        			
		        				//mhpb += g[gi];
		        				
		        			}
			        		//mhpb += "\n";
			        		}
			        		}
		        		
		        			//mhpb = mhpb.trim();
		        		
		        			//mhpb = mhpb.replaceAll("%C2%A0", "\n");
		        			mhpb = mhpb.replaceAll("%09","%20").replaceAll("%20(%20)+","%20").replaceAll("%0D","%0A").replaceAll("%0A(%20)+","%0A").replaceAll("%0A(%0A)+","%0A").replaceAll("%[8-9A-F][0-9A-F]","");

		        		
		        		
		        		
		        		
		        			//mhpb = Uri.decode(Uri.encode(mhpb).replaceAll("%(8|9|[A-F])([0-9]|[A-F])","\n"));
		        			
		        	//String[] x = mhpb.split("\n");
		        	//for(int u = 0; u < x.length; u++){
		        	//if(Uri.encode(x[u]).matches(".*%([8-9]|[A-F])([0-9]|[A-Z]).*")){
		        		//Log.w(TAG,"["+u+"]" + x[u] );
		        		//Log.w(TAG,"["+u+"]"+Uri.encode(x[u]));
		        	//}
		        	//}
		        	
		        	
		        	
		        	Log.i(TAG,"###############   post acquired ");// + mhpb.length() + " bytes encoded");
		        	
		        	
		        
        			bn = mhpb.split("%0A");
	        		//mhpb = "";
	        		for(int u=0;u <bn.length;u++){

		        		//mhpb += Uri.decode(bn[u])+"\n";
	        		//*
		        		String g[] = bn[u].replaceAll("%20"," ").replaceAll("%1","\n%1").replaceAll("%7F","\n%7F").replaceAll("%0","\n%0").replaceAll("%8","\n%8").replaceAll("%9","\n%9").replaceAll("%A","\n%A").replaceAll("%B","\n%B").replaceAll("%C","\n%C").replaceAll("%D","\n%D").replaceAll("%E","\n%E").replaceAll("%F","\n%F").split("\n");
        			for(int gi=0;gi<g.length;gi++){
    				if(g[gi].matches("%(1|0[0-8B-CEF]|[8-9A-F]|7F).*")){
    					if(gi > 0){Log.w(TAG,"["+u+"]["+(gi-1)+"]"+g[gi-1]+"[]");}
    					Log.e(TAG,"["+u+"]["+gi+"]"+g[gi]+"[]");
    					if(gi < g.length-1){Log.w(TAG,"["+u+"]["+(gi+1)+"]"+g[gi+1]+"[]");}
    				}else{
    				//Log.i(TAG,"["+u+"]["+gi+"]"+g[gi]+"[]");
    				}
        			//mhpb += Uri.decode(g[gi]);
        			}
	        		
	        		}
	        		}//*/
		        	
		        	//mhpb = Uri.decode(mhpb);
					//mhpb = mhpb.trim();
		        	
		        	
		        	
		        	
		        	
		        	
		        	
					Log.i(TAG,"###############   pre storage " + mhpb.length() );
					String statusline = "OK";//mHR.getStatusLine().getReasonPhrase()
		        	//{Message ml = new Message(); Bundle bl = new Bundle(); bl.putString("title", titlr); bl.putString("murl", mUrl); bl.putLong("startdl", sh); bl.putString("statusline", statusline);bl.putString("dest", dest); bl.putString("storloc", loc); bl.putString("mhpb", mhpb); bl.putString("pageconnectknow", bdl.getString("pageconnectknow")); ml.setData(bl); storePage.sendMessage(ml);}
		        	
		        	
		        	
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
			      		
			      		mReg = getSharedPreferences("Preferences",MODE_WORLD_WRITEABLE);mEdt = mReg.edit();
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
			      	
			      	
			      	
			      	
			      	
			      	
			      	
			      	
			      	
			      	
			      	//mHE.consumeContent();
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
			
			
	        responseCode = "200 OK";//mHR.getStatusLine().toString();
			
		//} catch (ClientProtocolException e) {
			//{Message mx = new Message(); Bundle bx = new Bundle();bx.putString("text","safeHttpGet() 1121 ClientProtocolException for " + who);bx.putInt("l",2);mx.setData(bx);logoly.sendMessageDelayed(mx,pRate);}
			//{Message mx = new Message(); Bundle bx = new Bundle();bx.putString("text","safeHttpGet() 1122 IO Exception Message " + e.getLocalizedMessage());bx.putInt("l",2);mx.setData(bx);logoly.sendMessageDelayed(mx,pRate);}
			
			//e.printStackTrace();//{Message mx = new Message(); Bundle bx = new Bundle();bx.putString("text",e.printStackTrace());bx.putInt("l",2);mx.setData(bx);logoly.sendMessageDelayed(mx,100);}
			//responseCode = " " + e.getLocalizedMessage() + " HTTP ERROR";//easyStatus(responseCode);
		} catch (NullPointerException e) {
			{Message mx = new Message(); Bundle bx = new Bundle();bx.putString("text","safeHttpGet() 1126 NullPointer Exception for " + who);bx.putInt("l",2);mx.setData(bx);logoly.sendMessageDelayed(mx,pRate);}
			{Message mx = new Message(); Bundle bx = new Bundle();bx.putString("text","safeHttpGet() 1127 IO Exception Message " + e.getLocalizedMessage());bx.putInt("l",2);mx.setData(bx);logoly.sendMessageDelayed(mx,pRate);}
			e.printStackTrace();//{Message mx = new Message(); Bundle bx = new Bundle();bx.putString("text",e.printStackTrace());bx.putInt("l",2);mx.setData(bx);logoly.sendMessageDelayed(mx,100);}
		//} catch (IOException e) {
			//{Message mx = new Message(); Bundle bx = new Bundle();bx.putString("text","safeHttpGet() 1130 IO Exception for " + who);bx.putInt("l",2);mx.setData(bx);logoly.sendMessageDelayed(mx,pRate);}
			////if( e.getLocalizedMessage().contains("Host is unresolved") ){ SystemClock.sleep(1880); }
			////mEdt.putLong("bucket_error", System.currentTimeMillis());mEdt.putString("errortype", e.getLocalizedMessage());mEdt.commit();
			//mEdt.putLong(loc+"_error", System.currentTimeMillis());mEdt.putString("errortype", e.getLocalizedMessage());mEdt.commit();
			//{Message mx = new Message(); Bundle bx = new Bundle();bx.putString("text","safeHttpGet() 1132 IO Exception Message " + e.getLocalizedMessage());bx.putInt("l",2);mx.setData(bx);logoly.sendMessageDelayed(mx,pRate);}
			//StackTraceElement[] err = e.getStackTrace();
			////for(int i = 0; i < err.length; i++){
			//	//Log.w(G,"safeHttpGet() 1135 IO Exception Message " + i + " class(" + err[i].getClassName() + ") file(" + err[i].getFileName() + ") line(" + err[i].getLineNumber() + ") method(" + err[i].getMethodName() + ")");
			////}
			//responseCode = e.getLocalizedMessage();//easyStatus(responseCode);
		} catch (OutOfMemoryError e) {
			{Message mx = new Message(); Bundle bx = new Bundle();bx.putString("text","safeHttpGet() 1121 OutOfMemoryError for " + who);bx.putInt("l",2);mx.setData(bx);logoly.sendMessageDelayed(mx,pRate);}
			{Message mx = new Message(); Bundle bx = new Bundle();bx.putString("text","safeHttpGet() 1122 IO Memory Message " + e.getLocalizedMessage());bx.putInt("l",2);mx.setData(bx);logoly.sendMessageDelayed(mx,pRate);}
			e.printStackTrace();//{Message mx = new Message(); Bundle bx = new Bundle();bx.putString("text",e.printStackTrace());bx.putInt("l",2);mx.setData(bx);logoly.sendMessageDelayed(mx,100);}
			mEdt.putLong(loc+"_error", System.currentTimeMillis());mEdt.putString("errortype", "Out of Memory");mEdt.commit();
			responseCode = " " + e.getLocalizedMessage() + " MEMORY ERROR";//easyStatus(responseCode);
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
		}};mt.start();
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
	
	
	int DOWNLOAD_LIMIT = 400 * 1024;
	public void e(String t, String x){Log.e(t,x);}
	public void i(String t, String x){Log.i(t,x);}
	public void w(String t, String x){Log.w(t,x);}
	public String getPage(String loc, String gourl, String who){
		
		w(TAG,"getPage() get ConnectivityManager");
    	ConnectivityManager cnnm = (ConnectivityManager) mCtx.getSystemService(mCtx.CONNECTIVITY_SERVICE);
    	w(TAG,"getPage() get NetworkInfo");
    	NetworkInfo ninfo = cnnm.getActiveNetworkInfo();
    	w(TAG,"getPage() got NetworkInfo state("+ninfo.getState().ordinal()+") name("+ninfo.getState().name()+")");
    	//android.os.Process.getElapsedCpuTime()
    	
		String httpPage = "";
		//String gourl = baseurl;
		Socket socket = null;
		SSLSocket sslsocket = null;
		BufferedReader br = null;
		BufferedWriter bw = null;
		int loopcnt = -1;
		try {
			while(gourl.length() > 0 ){
				
				//mPreferencesEditor.putLong("lastfeedactive", System.currentTimeMillis()).commit();
				loopcnt ++;
				if( loopcnt > 8 ){
					e(TAG,"getPage() Looped 8 times, really?! this many forwards?");
					break;
				}
				boolean secure = gourl.contains("https:") ? true : false;
				String hostname = gourl.replaceFirst(".*://", "").replaceFirst("/.*", "");
				int port = secure ? 443 : 80;
				if( hostname.contains(":") ){
					String[] p = hostname.split(":");
					hostname = p[0];
					port = Integer.parseInt(p[1]);
				}
				
				String docpath = gourl.replaceFirst(".*://", "").replaceFirst(".*?/", "/");
				
				int dlis = docpath.replaceAll("[?].*","").lastIndexOf('/');
				File df = new File(getfilepath(), hostname + docpath.substring(0,dlis>-1?dlis:docpath.length()-1));
				df.mkdirs();
				File f = new File(getfilepath(), hostname + docpath.replaceAll("[?].*","") );
				if(f.isDirectory() || !f.getAbsolutePath().matches(".*[.]([0-9a-zA-z][0-9a-zA-z]|[0-9a-zA-z][0-9a-zA-z][0-9a-zA-z])")  ){
					
					
					Log.i(G,"----------> File is Directory mode <---------------");
					String h7_type = mReg.getString(loc+"_type","text/html").replaceAll(".*/","");
					f = new File(f,"index."+h7_type);
					File pf2 = f.getParentFile();
					pf2.mkdirs();
				}
				
				if(!f.exists()){f.createNewFile();}


ContentValues ins = new ContentValues();
        ins.put("hostname",hostname);
        ins.put("docpath",docpath);
        ins.put("url",gourl);
        ins.put("port",port);
Uri source = SqliteWrapper.insert(mCtx, getContentResolver(), Uri.withAppendedPath(DataProvider.CONTENT_URI, "retrospect"), ins);
int sourceid = source.getLastPathSegment().matches("[0-9]+")?Integer.parseInt(source.getLastPathSegment()):-1;
Log.i(TAG,"Created "+sourceid+" " + source.toString());
String sourceheader = ""; String sourcebody = "";

	
				w(TAG,"getPage() hostname("+hostname+") path("+docpath+") gourl("+gourl+") file("+f.exists()+","+f.getAbsolutePath()+")");
				/*
				if(f.exists()){
					if(f.lastModified() +30*60*1000 >System.currentTimeMillis()){
						mEdt.putLong(loc+"_saved",System.currentTimeMillis());mEdt.putLong(loc+"_connect",System.currentTimeMillis());mEdt.putLong(loc+"_connected",System.currentTimeMillis());mEdt.putLong(loc+"_responded",System.currentTimeMillis());
						mEdt.putString(loc,f.getAbsolutePath());mEdt.commit();
						return f.getAbsolutePath();
					}
				}
				//*/
				gourl = "";

				mEdt.putString(loc+"_hostname",hostname);
				mEdt.putInt(loc+"_forward",loopcnt);
				mEdt.commit();
				
				String ipaddr = "";
				long rrr = System.currentTimeMillis();long rrs = 2;
				if( !secure ){
					sslsocket = null;
					w(TAG,"getPage() Connecting to hostname("+hostname+") port("+port+")");
					socket = new Socket(hostname,port);
					
					//socket = new SecureSocket();
					//SecureSocket s = null;
					
					if( socket.isConnected() ){
						i(TAG,"getPage() Connecting to hostname("+hostname+") CONNECTED");
						if(rrs == 2){rrs = System.currentTimeMillis();Log.w(G,"Took " + (rrs - rrr) + "ms ready to connect.");mEdt.putLong(loc+"_respondedms",(rrs-rrr));mEdt.putLong(loc+"_connect",System.currentTimeMillis());mEdt.commit();}
						InetAddress iix = socket.getInetAddress();
						//SocketAddress b8 = socket.getRemoteSocketAddress();
						if(iix != null){mEdt.putString(loc+"_ipaddress",iix.getHostAddress());}
						//if(b8 != null){}
						mEdt.putInt(loc+"_ipport",socket.getPort());mEdt.commit();
					
					}else{
						int loopcnt2 = 0;
						while( !socket.isConnected() ){
							e(TAG,"getPage() Not connected to hostname("+hostname+")");
							loopcnt2++;
							if( loopcnt2 > 10 ){
								e(TAG,"getPage() Not connected to hostname("+hostname+") TIMEOUT REACHED");
								break;
							}
							SystemClock.sleep(300);
						}
					}
					
					//w(TAG,"getPage() Creating Writable to hostname("+hostname+") port("+port+")");
					bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
					//w(TAG,"getPage() Creating Readable to hostname("+hostname+") port("+port+")");
					br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				}else{
					socket = null;
					w(TAG,"getPage() Connecting Securely to hostname("+hostname+") port("+port+")");
					
					SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory.getDefault();
					sslsocket = (SSLSocket) factory.createSocket(hostname,443);
					SSLSession session = sslsocket.getSession();
					X509Certificate cert;
					try { cert = (X509Certificate) session.getPeerCertificates()[0]; }
					catch(SSLPeerUnverifiedException e){
						e(TAG,"getPage() Connecting to hostname("+hostname+") port(443) failed CERTIFICATE UNVERIFIED");
						break;
					}
					
					if( sslsocket.isConnected() ){
						i(TAG,"getPage() Connecting to hostname("+hostname+") CONNECTED");
						if(rrs == 2){rrs = System.currentTimeMillis();Log.w(G,"Took " + (rrs - rrr) + "ms ready to connect.");mEdt.putLong(loc+"_respondedms",(rrs-rrr));mEdt.putLong(loc+"_connect",System.currentTimeMillis());mEdt.commit();}
						InetAddress iix = sslsocket.getInetAddress();
						if(iix != null){mEdt.putString(loc+"_ipaddress",iix.getHostAddress());}
						mEdt.putInt(loc+"_ipport",sslsocket.getPort());mEdt.commit();
					}else{
						int loopcnt2 = 0;
						while( !sslsocket.isConnected() ){
							e(TAG,"getPage() Not connected to hostname("+hostname+")");
							loopcnt2++;
							if( loopcnt2 > 20 ){
								e(TAG,"getPage() Not connected to hostname("+hostname+") TIMEOUT REACHED");
								break;
							}
							SystemClock.sleep(300);
						}
					}
											
					//w(TAG,"getPage() Creating Writable to hostname("+hostname+") port("+port+")");
					bw = new BufferedWriter(new OutputStreamWriter(sslsocket.getOutputStream()));
					//w(TAG,"getPage() Creating Readable to hostname("+hostname+") port("+port+")");
					br = new BufferedReader(new InputStreamReader(sslsocket.getInputStream()));
				}
				
				rrs = 2;//
				mEdt.putLong(loc+"_connected",System.currentTimeMillis());
				mEdt.putLong("lastfeedactive", System.currentTimeMillis());mEdt.commit();
				w(TAG,"getPage() Requesting document hostname("+hostname+") port("+port+")");
				bw.write("GET " + docpath + " HTTP/1.0\r\n");
				bw.write("Host: " + hostname + "\r\n");
				bw.write("User-Agent: Android Doc Chomps Soft\r\n");
				bw.write("Range: bytes=0-"+(1024 * DOWNLOAD_LIMIT)+"\r\n");
				//bw.write("TE: deflate\r\n");
				bw.write("\r\n");
				bw.flush();
				//http://www.w3.org/Protocols/rfc2616/rfc2616-sec5.html#sec5
				Log.i(G,"Request delivered");
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
					Log.i(G,"####################Ready for Reply#############");
					int linecnt = 0;
					for(line = br.readLine(); line != null; line = br.readLine()){
						if( line.length() == 0 ){if(rrs == 2){rrs = System.currentTimeMillis();Log.w(G,"Took " + (rrs - rrr) + "ms ready to respond.");mEdt.putLong(loc+"_respondedms",(rrs-rrr));mEdt.putLong(loc+"_responded",System.currentTimeMillis());mEdt.commit();}
						
							w(TAG,"getPage() End of header Reached");
							break;
						}
						linecnt++;
						i(TAG,"getPage() received("+line+")");
sourceheader += line;
						if( line.regionMatches(true, 0, "Location:", 0, 9) ){
							gourl = line.replaceFirst(".*?:", "").trim();
							i(TAG,"getPage() ###############>>>>>>>>>>>>>> FOUND FORWARD URL("+gourl+") ");
						}else
						if(line.regionMatches(true,0,"Content-Length:", 0, 15) && line.replaceAll(".*?:","").trim().matches("[0-9]+")){
							int css = Integer.parseInt(line.replaceAll(".*?:","").trim());
							mEdt.putLong(loc+"_length",css);
							Log.i(G,"getPage() "+loc+"_length("+css+")");
						}else
						if(line.regionMatches(true,0,"Content-Type:", 0, 13) ){
							String ct = line.replaceAll(".*?:","").replaceAll(";.*","").trim();
							mEdt.putString(loc+"_type",ct);
							Log.i(G,"getPage() "+loc+"_type("+ct+")");
							if(line.toLowerCase().matches(".*;.*?charset.*?=.*")){
								String ch = line.replaceAll(".*;.*?[cC][hH][aA][rR][sS][eE][tT].*?=","").trim();
								mEdt.putString(loc+"_charset",ch.toUpperCase());
								Log.i(G,"getPage() "+loc+"_charset("+ch+")");
							}
						}
					
					}
					if( gourl.length() > 0 ){ continue; }
					if( line == null ){
						w(TAG,"getPage() End of read");
					}
					if( linecnt > 0 ){
						mEdt.putLong("lastfeedactive", System.currentTimeMillis());
						mEdt.putLong("lowmemory", 0);
					}
					mEdt.commit();
					
					int httpPageSize = 0;
					if( line != null ){
						//int zerocnt = 0;
						char[] u7 = new char[102400];
						

						FileOutputStream fs = null;
						try {
							fs = new FileOutputStream(f);
							//b3.compress(CompressFormat.PNG,0,fs);
							//fs.write(array);
							httpPage = f.getAbsolutePath();
							mEdt.putString(loc,httpPage);mEdt.commit();
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						
						} catch (IOException e1) {
			                    Log.e(TAG, "ComputerStart() 1534 fs.close() failed");
			                    e1.printStackTrace();
			            }

						long pr = -1;
						for(int cs = br.read(u7); cs > -1; cs = br.read(u7)){
							pr = SystemClock.uptimeMillis();
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
							i(TAG,"getPage() host("+hostname+") line("+line+")");
							//fs.write(line.getBytes());
							line = line.trim();
							if(fs != null && line.length() > 0){
							fs.write(line.getBytes());}
							httpPageSize += cs;
sourcebody += line;

							i(G,"getPage() being sent " + cs + " bytes secure("+secure+") encoded("+line.length()+") " + (SystemClock.uptimeMillis() - pr) + "ms");
							
							//httpPage += Uri.decode(line);
							if( httpPageSize > 1024 * DOWNLOAD_LIMIT ){
								w(TAG,"getPage() downloaded "+DOWNLOAD_LIMIT+"K from the site, moving on.");
								break;
							}
						}
					
						i(TAG,"getPage() download concluded");
						
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
					w(TAG,"getPage() Downloaded("+httpPageSize+" bytes)");

					mEdt.putLong("bucket_saved", System.currentTimeMillis());
					mEdt.putLong(loc+"_saved", System.currentTimeMillis());
					mEdt.commit();
					
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
					e(TAG,"getPage() IOException while reading from web server " + msg);
					e1.printStackTrace();
				}
				
				if( !secure ){
					socket.close();
				}else{
					sslsocket.close();
				}
			}
		} catch (UnknownHostException e1) {
			e(TAG,"getPage() unknownHostException");
			e1.printStackTrace();
		} catch (IOException e1) {
			e(TAG,"getPage() IOException");
			e1.printStackTrace();
		}
		
		return httpPage;
	}
	
	
	
	
	
	
	
	private Handler logoly = new Handler(){public void handleMessage(Message msg){Bundle bx = msg.getData();int l = bx.getInt("l");String text = bx.getString("text");switch(l){case 2:Log.e(TAG,":"+text);break;case 3:Log.w(TAG,":"+text);break;default:Log.i(TAG,":"+text);break;}}};
	
	public String datetime(){
		String g = "";
		Date d = new Date();
		g = (d.getYear()+1900)+"-"+((d.getMonth() < 9)?"0":"")+((d.getMonth()+1))+"-"+((d.getDate() < 10)?"0":"")+d.getDate()+"T"+((d.getHours() < 10)?"0":"")+d.getHours()+":"+((d.getMinutes() < 10)?"0":"")+d.getMinutes()+":00";
		{Message mx = new Message(); Bundle bx = new Bundle();bx.putString("text","generated date "+g);bx.putInt("l",1);mx.setData(bx);logoly.sendMessageDelayed(mx,pRate);}
		return g;
	}
	
	private String fixDate(String updated) {
		//day, month dd, yyyy hh:mm tt
		//m/d/year hh:mm tt
		//2010-07-15T19:07:30+05:00
		if(updated.indexOf("CDATA[") > -1){updated = updated.substring(updated.indexOf("CDATA[")+6, updated.lastIndexOf("]]"));}
		String[] dateparts = updated.split(" ");
		if(dateparts.length == 1){dateparts = updated.replaceAll("T", " ").split(" ");}
		//Log.i(TAG,"fixDate ("+updated+") parts("+dateparts.length+") length("+updated.length()+")");
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
			/*if(dateparts[2].toLowerCase().contains("pm") && h < 12){
				h+=12;
			}if(dateparts[2].toLowerCase().contains("am") && h == 12){
				h-=12;
			}//*/
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
			
			if(dateparts[2].length() == 4 && !dateparts[0].contains(",") && dateparts[0].matches("[0-9]+")){
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
	
	
	
	
	//private long storeTime = 0;
	Handler storePage = new Handler(){
		public void handleMessage(Message msg){
			//if(storeTime > System.currentTimeMillis()){ easyStatus("Refresh occured within 8 seconds, try later.");  return; }//Bundle bl = msg.getData();Message ml = new Message(); ml.setData(bl);storePage.sendMessageDelayed(ml,1750);return;} 
			//storeTime = System.currentTimeMillis()+7750;
			Log.i(TAG,"storePage");
			final Bundle bdl = msg.getData();
	//		s01(bdl);
	
		
	//public void s01(final Bundle bdl){
		
		Thread tx = new Thread(){
				public void run(){
					String mhpb = bdl.getString("mhpb");
					String loc = bdl.getString("storloc");
					Log.i(TAG,"storePage thread " + mhpb.length() + " bytes to " + loc + " ");
					//byte[] mx = new byte[mhpb.length+1 * 2];
					//String mHP = "";
					//for(int i = 0; i+6 < mhpb.length; i++){mHP += mhpb[i]+mhpb[++i]+mhpb[++i]+mhpb[++i]+mhpb[++i]+mhpb[++i];}
					/*
					int errorcnt = 0;
					for(errorcnt = 0; errorcnt < 5; errorcnt++){
					try {
					mHP = new String(mhpb);
					break;
					} catch (OutOfMemoryError e){
						
						Log.e(G,"OutOfMemory Error Received while Storing Page");
						//{Message ml = new Message(); Bundle bl = new Bundle(bdl); ml.setData(bl); storePage.sendMessageDelayed(ml, 3500);}return;
					}
					}	
					if( errorcnt == 5 || mHP == null || mHP.length() == 0){
						easyStatus("Wild errors " + errorcnt);
						return;
					}//*/
						
					
					SharedPreferences mReg = getSharedPreferences("Preferences",MODE_WORLD_WRITEABLE);Editor mEdt = mReg.edit();
					//mEdt.putString(loc+"url", murl);
					
					Log.w(TAG,"storePage 86 storing content");
					//mx = null;
					mEdt.putString(loc, mhpb);
					mEdt.putLong("bucket_saved", System.currentTimeMillis());
					mEdt.putLong(loc+"_saved", System.currentTimeMillis());
					mEdt.commit();
					Log.w(TAG,"safeHttpGet() 368 saved");
				
					String titlr = bdl.getString("title");
			        String murl = bdl.getString("murl");	
					long sh = bdl.getLong("startdl");
			        String dest = bdl.getString("dest");
					String statusline = bdl.getString("statusline");
			        String pageconnectknow = bdl.getString("pageconnectknow");
					
					//final String murl = mUrl;final String mhp = mHP;
			        	//Thread eb = new Thread(){public void run(){
			        	if( pageconnectknow == null || mhpb.contains(pageconnectknow) ){
			        	
			        		//easyStatus("Acquired "+titlr+" in "+(SystemClock.uptimeMillis()-sh)/1000+" secs.\n"+dest);
			        	//{Message mxm = new Message(); Bundle bxb = new Bundle(); bxb.putString("subtitle","Acquired "+titlr+" in "+(SystemClock.uptimeMillis()-sh)/1000+" secs.\n"+dest ); mxm.setData(bxb);easyViewerHandler.sendMessageDelayed(mxm,10);}
						//}};eb.start();
			      		//mReg = getSharedPreferences("Preferences",MODE_WORLD_WRITEABLE);mEdt = mReg.edit();
			      		//mEdt.putString(loc, mHP);mEdt.commit();
			        	//{Message mx = new Message();Bundle bxb = new Bundle();// bxb.putString("string", loc+",");bxb.putString(loc, mHP);
			      		//bxb.putString("long", "lasthttp");bxb.putLong("lasthttp", System.currentTimeMillis());
			      		// mx.setData(bxb);setrefHandler.sendMessageDelayed(mx,50);}//*/
			        	}else if(pageconnectknow != null){//easyStatus("Invalid Download");
			        	{Message ml = new Message(); Bundle bl = new Bundle(); bl.putInt("l",2);bl.putString("text",titlr+" downloaded didn't pass acknowledgement("+bdl.getString("pageconnectknow")+"). Lov'n cookies and the rest.");ml.setData(bl);logoly.sendMessageDelayed(ml,75); }
			        	String ct = "";
			        	{Message ml = new Message(); Bundle bxb = new Bundle(); bxb.putString("remove", "connect");  ml.setData(bxb);setrefHandler.sendMessageDelayed(ml, 50);}
						//String[] h = mHP.split("\n");for(int b = 0; b < h.length; b++){{Message mx = new Message(); Bundle bx = new Bundle();bx.putString("text","pageknow("+bdl.getString("pageconnectknow")+") "+h[b]);mx.setData(bx);logoly.sendMessageDelayed(mx,pRate);}
						//if(h[b].contains("content1=") ){ct += h[b].substring(h[b].indexOf("content1=")+9, h[b].indexOf(";HS;", h[b].indexOf("content1=")));}}
						//ct = ct.replaceAll("%0a", "\n").trim();
						Log.e(TAG,"know("+bdl.getString("pageconnectknow")+")");//Log.w(G,"know("+bdl.getString("pageconnectknow")+")"+h[b]);
			        	}
			        {Message ml = new Message(); Bundle bx = new Bundle();bx.putString("text","Downloaded status(" + statusline + ") loc("+loc+") mUrl("+murl+") " + mhpb.length() + " bytes.");ml.setData(bx);logoly.sendMessageDelayed(ml,pRate);}
			    
					
				}
			};
			tx.start();
		//}	
		}
	};
	
	
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
	
}
