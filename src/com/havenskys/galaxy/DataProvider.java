package com.havenskys.galaxy;

import java.util.Date;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

public class DataProvider extends ContentProvider {
	private static String mainuri = August.dataprovider;
    private static final int DB_VERSION = 5;
	
    
    
    
	private static String TAG = "DataProvider";

	private SQLiteDatabase mDb;
    private DatabaseHelper mDbHelper;
    
    private static String DATABASE_NAME = "moment.db";
    
    private static final int ALL_MESSAGES = 1;
    private static final int SPECIFIC_MESSAGE = 2;
    

    private static final int ALL_MESSAGESFILTER = 3;
    private static final int SPECIFIC_MESSAGEFILTER = 4;
    

    private static final int ALL_MESSAGESPERSPECTIVE = 5;
    private static final int SPECIFIC_MESSAGEPERSPECTIVE = 6;

    private static final int ALL_MESSAGESRETROSPECT = 7;
    private static final int SPECIFIC_MESSAGERETROSPECT = 8;
    
    private static final UriMatcher URI_MATCHER;
    static{
        URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
        URI_MATCHER.addURI(mainuri, "moment", ALL_MESSAGES);
        URI_MATCHER.addURI(mainuri, "moment/#", SPECIFIC_MESSAGE);
        URI_MATCHER.addURI(mainuri, "perspective", ALL_MESSAGESPERSPECTIVE);
        URI_MATCHER.addURI(mainuri, "perspective/#", SPECIFIC_MESSAGEPERSPECTIVE);
        URI_MATCHER.addURI(mainuri, "retrospect", ALL_MESSAGESPERSPECTIVE);
        URI_MATCHER.addURI(mainuri, "retrospect/#", SPECIFIC_MESSAGEPERSPECTIVE);
        URI_MATCHER.addURI(mainuri, "filter", ALL_MESSAGESFILTER);
        URI_MATCHER.addURI(mainuri, "filter/#", SPECIFIC_MESSAGEFILTER);
    
    }

    public static final Uri CONTENT_URI = Uri.parse( "content://"+mainuri+"/");
	
    //url text unique not null, urltext text, farkpicurl text, farkpictext text, commenturl text, commenttext text, description text
    
 

    // Database creation/version management helper.
    // Create it statically because we don't need to have customized instances.
    private static class DatabaseHelper extends SQLiteOpenHelper {

    	private static String TAG = "DataProviderDB";
    
    	private Context mContext;
        //public DatabaseHelper(Context context, String name, CursorFactory factory, int version) {
			//super(context, name, factory, version);
			// TODO Auto-generated constructor stub
		//}
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DB_VERSION);
            mContext = context;
            //mLog = new Custom(mContext, TAG + " DatabaseHelper() 52");
            Log.i(TAG, "DatabaseHelper() 53");
        
            
        }

        SQLiteDatabase db = null;
		@Override
        public void onCreate(SQLiteDatabase db2){
			db = db2;
			Log.i(TAG,"DatabaseHelper onCreate() ++++++++++++++++++++++++");
        	try{
        		String SQL = "";
        		SQL += "create table IF NOT EXISTS moment (";
        		SQL += "_id integer primary key autoincrement ";
        		//SQL += mContext.getString(R.string.contactStore_columns);
        		SQL += ", url text";
        		SQL += ", title text, published DATE, content text, author text";
        		SQL += ", created DATE";
        		SQL += ", updated DATE";
        		SQL += ", status INTEGER default 1"; // < 0 deleted(value * -1), 1 active(created), ++ per update 
        		SQL += ");\n";
        		db.execSQL( SQL );
        		db.execSQL("create index IF NOT EXISTS moment_title on moment(title);");
        		db.execSQL("create index IF NOT EXISTS moment_url on moment(url);");
        		db.execSQL("create index IF NOT EXISTS moment_published_title_url on moment(published,title,url);");
        		db.execSQL("create index IF NOT EXISTS moment_published on moment(published);");
        		
        		//db.execSQL("create index moment_created on moment(created);");
        		
        		//db.execSQL("insert into moment (_id,title) values (null,\"Created Database\"");
        		
        		
            } catch (SQLException e) {
            	e.printStackTrace();
            }
            
		}
		public String datetime(){
    		String g = "";
    		Date d = new Date();
    		g = (d.getYear()+1900)+"-"+((d.getMonth() < 9)?"0":"")+((d.getMonth()+1))+"-"+((d.getDate() < 10)?"0":"")+d.getDate()+" "+((d.getHours() < 10)?"0":"")+d.getHours()+":"+((d.getMinutes() < 10)?"0":"")+d.getMinutes()+":00";
    		//Log.i(TAG,"generated date "+g);
    		return g;
    	}
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        	Log.i(TAG,"DatabaseHelper onUpgrade() ++++++++++++++++++++++++");
            db.execSQL("DROP TABLE IF EXISTS " + "moment");
            onCreate(db);
        }

    }
    
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		//mLog.w(TAG,"delete() uri("+uri+") lastsegment("+uri.getLastPathSegment()+")");
		int rowCount = mDb.delete(uri.getLastPathSegment(), selection, selectionArgs);

        // Notify any listeners and return the deleted row count.
        getContext().getContentResolver().notifyChange(uri, null);
        return rowCount;

	}

	@Override
	public String getType(Uri uri) {
		
        switch (URI_MATCHER.match(uri)){
        case ALL_MESSAGES:
        	//Log.w(TAG,"gettype() uri("+uri+") ALL MESSAGES");
            return "vnd.android.cursor.dir/moment"; // List of items.
        case SPECIFIC_MESSAGE:
        	//Log.w(TAG,"gettype() uri("+uri+") Specific Message");
            return "vnd.android.cursor.item/moment";     // Specific item.
        case ALL_MESSAGESPERSPECTIVE:
        	//Log.w(TAG,"gettype() uri("+uri+") ALL MESSAGES");
            return "vnd.android.cursor.dir/perspective"; // List of items.
        case SPECIFIC_MESSAGEPERSPECTIVE:
        	//Log.w(TAG,"gettype() uri("+uri+") Specific Message");
            return "vnd.android.cursor.item/perspective";     // Specific item.
        case ALL_MESSAGESRETROSPECT:
        	//Log.w(TAG,"gettype() uri("+uri+") ALL MESSAGES");
            return "vnd.android.cursor.dir/retrospect"; // List of items.
        case SPECIFIC_MESSAGERETROSPECT:
        	//Log.w(TAG,"gettype() uri("+uri+") Specific Message");
            return "vnd.android.cursor.item/retrospect";     // Specific item.
        case ALL_MESSAGESFILTER:
        	//Log.w(TAG,"gettype() uri("+uri+") ALL MESSAGES");
            return "vnd.android.cursor.dir/filter"; // List of items.
        case SPECIFIC_MESSAGEFILTER:
        	//Log.w(TAG,"gettype() uri("+uri+") Specific Message");
            return "vnd.android.cursor.item/filter";     // Specific item.
        case -1:
        	Log.e(TAG,"getType(-1) uri("+uri+")");
            return "vnd.android.cursor.item/moment";
        default:
        	Log.e(TAG,"getType() uri("+uri+")");
            return "vnd.android.cursor.item/moment";
        }
	}

	public String datetime(){
		String g = "";
		Date d = new Date();
		g = (d.getYear()+1900)+"-"+((d.getMonth() < 9)?"0":"")+((d.getMonth()+1))+"-"+((d.getDate() < 10)?"0":"")+d.getDate()+" "+((d.getHours() < 10)?"0":"")+d.getHours()+":"+((d.getMinutes() < 10)?"0":"")+d.getMinutes()+":00";
		//Log.i(TAG,"generated date "+g);
		return g;
	}
	@Override
	public Uri insert(Uri uri, ContentValues values)  {
		if( mDb == null ){
			return null;
		}
		long rowId = -1;
		//Log.w(TAG,"insert() uri("+uri+") lastsegment("+uri.getLastPathSegment()+")");
		String tablename = uri.getLastPathSegment();values.put("created", datetime());values.put("updated", datetime());
       rowId = mDb.insertOrThrow(tablename, "rawcontent", values);
       Uri newUri = Uri.withAppendedPath(CONTENT_URI, tablename+"/"+rowId);
       //mLog.w(TAG,"insert()  newUri(" + newUri.toString() + ")");
       
       
       // Notify any listeners and return the URI of the new row.
       getContext().getContentResolver().notifyChange(CONTENT_URI, null);
       
       /*/
       if( rowId > 100 ){
    	   int del = (int) (rowId - 100);
    	   mDb.execSQL("update " + DATABASE_TABLE_NAME + " set "+ CONTENT +" = \"\" where _id < "+del+" ");
       }
       //*/
       
       return newUri;

	}

	@Override
	public boolean onCreate() {

		//mLog = new Custom(this.getContext(), TAG + " onCreate() 130");
		mDbHelper = new DatabaseHelper(getContext());

		//final Context con = getContext();
        try{
        	mDb = mDbHelper.getWritableDatabase();
        	
        	
        	if(mDb!=null){
                Log.w(TAG,"db updates");
                mDb.execSQL("create index IF NOT EXISTS moment_title on moment(title);");
        		mDb.execSQL("create index IF NOT EXISTS moment_url on moment(url);");
        		mDb.execSQL("create index IF NOT EXISTS moment_published_title_url on moment(published,title,url);");
        		mDb.execSQL("create index IF NOT EXISTS moment_published on moment(published);");
        		mDb.execSQL("create index IF NOT EXISTS moment_updated on moment(updated);");
        		mDb.execSQL("create index IF NOT EXISTS moment_created on moment(created);");
        		mDb.execSQL("create index IF NOT EXISTS moment_status on moment(status);");
            	
        		
        		Cursor d = mDb.query("moment", new String[]{"*"}, "", null, null, null, "_id LIMIT 1");
        		if(d==null){
        			Log.e(TAG,"Get one row failed");
        		}else{
        			String[] c = d.getColumnNames();
        			Log.i(TAG,"Database Upgrade Test");
        			if(d.getColumnIndex("images") == -1){
        				mDb.execSQL("alter table moment add column images TEXT");
        			}
        			if(d.getColumnIndex("prefilter") == -1){
        				mDb.execSQL("alter table moment add column prefilter TEXT");
        			}
        			if(d.getColumnIndex("source") == -1){
        				mDb.execSQL("alter table moment add column source TEXT");
        			}
        			if(d.getColumnIndex("textcontent") == -1){
        				mDb.execSQL("alter table moment add column textcontent TEXT");
        			}
        			if(d.getColumnIndex("urls") == -1){
        				mDb.execSQL("alter table moment add column urls TEXT");
        			}
        			if(d.getColumnIndex("js") == -1){
        				mDb.execSQL("alter table moment add column js TEXT");
        			}
        		}
        		
        		mDb.execSQL("create table IF NOT EXISTS filter (_id integer primary key autoincrement,title text,filtercode text,location text,prefilter text, updated date, created date, status integer default 1 ) ");
        		mDb.execSQL("create index IF NOT EXISTS filter_updated on filter(updated)");
        		mDb.execSQL("create index IF NOT EXISTS filter_status on filter(status)");
        		mDb.execSQL("create index IF NOT EXISTS filter_location on filter(location)");
        		mDb.execSQL("create index IF NOT EXISTS filter_loccre on filter(location,created)");
        		mDb.execSQL("create table IF NOT EXISTS perspective (_id integer primary key autoincrement,title text, location text, detail text, cux float, cuy float, cuz float, aux float, auy float, auz float, updated date, created date, status integer default 1 ) ");
        		mDb.execSQL("create index IF NOT EXISTS perspective_updated on perspective(updated)");
        		mDb.execSQL("create table IF NOT EXISTS retrospect (_id integer primary key autoincrement, filterid integer, title text, url text, hostname text, docpath text, port integer, body text, header text, updated date, created date, status integer default 1 ) ");
        		mDb.execSQL("create index IF NOT EXISTS retrospect_updated on retrospect(updated)");
        		mDb.execSQL("create index IF NOT EXISTS retrospect_hostname on retrospect(hostname)");
        		mDb.execSQL("create index IF NOT EXISTS retrospect_docpath on retrospect(docpath)");


}
        	
        	
        	
            //mDb = mDbHelper.openDatabase(getContext(), DATABASE_NAME, null, DB_VERSION);
            //mLogger.info("RssContentProvider.onCreate(): Opened a database");
        } catch (Exception ex) {
        	Log.e(TAG,"Failed to connected to Database, exception");
        	ex.printStackTrace();
              return false;
        }
        if(mDb == null){
        	Log.e(TAG,"Failed to connected to Database, mDb null");
            return false;
        } else {
        	
            return true;
        }

	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		
		if( mDb == null ){
			return null;
		}
		//Log.i(TAG,"query() uri("+uri+") type("+getType(uri)+") lastsegment("+uri.getLastPathSegment()+") fragment("+uri.getFragment()+") selection("+selection+")");
		
		SQLiteQueryBuilder qBuilder = new SQLiteQueryBuilder();
		
		String tablename = uri.getLastPathSegment();
		String recordid = "";
		if( getType(uri).contentEquals("vnd.android.cursor.item/moment") ){
			recordid = uri.getLastPathSegment();
			tablename = "moment";
		}else if( getType(uri).contentEquals("vnd.android.cursor.item/filter") ){
			recordid = uri.getLastPathSegment();
			tablename = "filter";
		}else if( getType(uri).contentEquals("vnd.android.cursor.item/perspective") ){
			recordid = uri.getLastPathSegment();
			tablename = "perspective";
		}
		
		// Set the table we're querying.
        qBuilder.setTables(tablename);

        // If the query ends in a specific record number, we're
        // being asked for a specific record, so set the
        // WHERE clause in our query.
        if(recordid.length() > 0){
        	qBuilder.appendWhere("_id=" + recordid);
		}else if(uri.getFragment() != null && !uri.getFragment().contains("-1")){//(URI_MATCHER.match(uri)) == SPECIFIC_MESSAGE){
    		qBuilder.appendWhere("_id=" + uri.getFragment()); // + uri.getPathLeafId());
    	}
        // Set sort order. If none specified, use default.
        if(TextUtils.isEmpty(sortOrder)){
            //sortOrder = Custom.DEFAULT_SORT_ORDER;
        }

        String groupby = null; String having = null;
        if(selection.toUpperCase().contains("GROUP BY")){
        	String us = selection.toUpperCase();
        	
        	groupby = selection.substring(us.indexOf("GROUP BY")+8,us.indexOf("HAVING")>-1?us.indexOf("HAVING")-1:us.length());
        	if(us.contains("HAVING")){
        		having = selection.substring(us.indexOf("HAVING")+6);
        	}
        
        	selection = selection.substring(0, us.indexOf("GROUP BY")-1);
        }
        // Make the query.
        Cursor c = qBuilder.query(mDb,
                projection,
                selection,//.replaceAll("\\\"", "\\\\\""),
                selectionArgs,
                groupby,
                having,
                sortOrder);
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;

	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		
		Log.i(TAG,"update() uri("+uri+") lastsegment("+uri.getLastPathSegment()+") selection("+selection+")");
		String tablename = uri.getLastPathSegment();values.put("updated", datetime());
		// NOTE Argument checking code omitted. Check your parameters!
        int updateCount = mDb.update(tablename, values, selection, selectionArgs);

        // Notify any listeners and return the updated row count.
        //getContext().getContentResolver().notifyUpdate(uri, null);
        getContext().getContentResolver().notifyChange(uri, null);
		return updateCount;
	}
	
	
	
}
