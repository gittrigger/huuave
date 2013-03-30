package com.havenskys.galaxy;

public class August {


	public static String title = "Galaxy";
    public static String dest = "http://twitter.com/statuses/user_timeline/73936473.rss";
    public static String dataprovider = "com.havenskys.galaxy";
    public static Class lookupClass = com.havenskys.galaxy.Lookup.class;
    	
	//Visual
	public static int notifyimage = R.drawable.yellowt;
    public static String listdate = "%Y/%m/%d %H:%M";
	public static int listMost = 1000;
	public static int naturalLimit = 100;//New records inserted per download/processing event.
	public static String handleCleanSQL = "strftime('%J',created) < strftime('%J','now')-7 AND (published is null || strftime('%J',published) < strftime('%J','now')-7) ";
	public static String todayCountSQL = "strftime('%m-%d-%Y',published) = strftime('%m-%d-%Y','now') AND status > 0";
	public static String loadlistSQL = "status > 0";//"julianday(published) > julianday(datetime('NOW'))-32 AND status > 0";
	public static String loadlistSort = "published desc";
    
    //Private Intent Broadcast
    public static String recoveryintent = dataprovider+".SERVICE_RECOVER3";
	
	//Type, <item> or <entry> typically
	public static String parse_item = "<item>";
    
	//Content
	public static String parse_content = "content:encoded";// type=\"html\" xml:lang=\"en\" xml:base=\"http://blog.ted.com/\"";
	public static String parse_summary = "description";// if parse_content failed
	
	//Link
	public static String parse_link = "link";
	
	//Author
	public static String parse_author = "dc:creator";
	public static String parse_author2 = "author";// if parse_author failed
	
	//Title
	public static String parse_title = "title";
	
	//Published Date
	public static String parse_published = "pubDate";
	public static String parse_lastbuild = "lastBuildDate";// if parse_published failed

	
	
}
