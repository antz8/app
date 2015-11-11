package database;

import java.sql.Date;
import java.util.Currency;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class DBAdapter {
	public static String KEY_ID = "_id";
	public static String KEY_USER = "user";
	public static String KEY_RECIPIENT = "recipient";
	public static String KEY_MESSAGE_USER = "msg_user";
	public static String KEY_MESSAGE_RECIPIENT = "msg_recipient";
	public static String KEY_MESSAGE_TIME = "time";
	
	public static String KEY_ID_LOGIN = "_id";
	public static String KEY_USERNAME = "username";
	public static String KEY_PASSWORD = "password";
	

	
	public static String TAG = "DBAdapter";
	
	public static String DBNAME = "ptalkt";
	
	public static String DBTABLECHAT = "chat";
	public static String DBTABLELOGIN = "login";
	public static int DBVERSION = 1;
	
	public static final String CREATETABLECHAT = "create table chat (_id integer primary key autoincrement, user text not null, recipient text not null,  msg_user text not null , msg_recipient text not null, time text not null);";
	public static final String CREATETABLELOGIN = "create table login (_id integer primary key autoincrement, username text not null, password text not null);";
	
	
	private final Context context ;
	public DbHelper theHelper;
	public SQLiteDatabase db;
	
	public DBAdapter(Context ct) {
		// TODO Auto-generated constructor stub
		this.context = ct;
		theHelper = new DbHelper(ct);
	}
	
	public DBAdapter open() throws SQLException{
		db =theHelper.getWritableDatabase();
		return this;
	}
	
	//---closes the database--
	   public void close(){

	       theHelper.close();

	   }
	    //---insert a contact into the database--
	   public long insertChat(String user,String recipient , String msg_user,String msg_recipient,String time){

	       ContentValues initialValues = new ContentValues();

	       initialValues.put(KEY_USER,user);

	       initialValues.put(KEY_RECIPIENT,recipient);
	       
	       initialValues.put(KEY_MESSAGE_USER,msg_user);
	       
	       initialValues.put(KEY_MESSAGE_RECIPIENT,msg_recipient);
	       
	       initialValues.put(KEY_MESSAGE_TIME,time);
	       
	       return db.insert(DBTABLECHAT,null,initialValues);

	   }
	   
	  

	   public boolean deleteChat(String recipient) {
	       
		   return db.delete(DBTABLECHAT,KEY_RECIPIENT+ "='"+ recipient+"'", null)> 0;

	   }
	    //---retrieves all the contacts--
     public Cursor getAllListChat(String user) {

    	 return db.rawQuery("SELECT distinct (recipient) FROM chat where user='"+user+"'", null);
	// return db.query(DBTABLECHAT,new String[] {KEY_IDTRANS,KEY_IDUSER,KEY_MASUK,KEY_KELUAR,KEY_TANGGAL,KEY_STATUS},null,null,null,null,null);
	 
	  }
     
     public Cursor getChat(String recipient )  {
 		
 		Cursor mCursor =
                 db.query(true, DBTABLECHAT, new String[] {KEY_ID,
                 KEY_USER, KEY_RECIPIENT,KEY_MESSAGE_USER,KEY_MESSAGE_RECIPIENT,KEY_MESSAGE_TIME}, KEY_RECIPIENT+ "='" + recipient +"'", null,
                 null, null, null, null);
//         if (mCursor != null) {
//             mCursor.moveToFirst();
//         }
         return mCursor;

 	   }
     
     public long insertLogin(String username,String password){

	       ContentValues initialValues = new ContentValues();

	       initialValues.put(KEY_USERNAME,username);

	       initialValues.put(KEY_PASSWORD,password);

	       return db.insert(DBTABLELOGIN,null,initialValues);

	   }
     
     public Cursor getAllListLogin() {
    	 Cursor mCursor =
    	  db.query(DBTABLELOGIN,new String[] {KEY_ID_LOGIN,KEY_USERNAME,KEY_PASSWORD},null,null,null,null,null);
	// return db.query(DBTABLECHAT,new String[] {KEY_IDTRANS,KEY_IDUSER,KEY_MASUK,KEY_KELUAR,KEY_TANGGAL,KEY_STATUS},null,null,null,null,null);
    	 if (mCursor != null){
    		 mCursor.moveToFirst();
    		 return mCursor;	
    	 }else{
    		 return null;	 
    	 }
    	 
//    	return null; 
//    	 
	  }
     
     public boolean deleteLogin(String username) {
	       
		   return db.delete(DBTABLELOGIN,KEY_USERNAME+ "='"+username+"'", null)> 0;

	   }
	   
//	    //---retrieves a particular contact--
	   
	
//	
//     public Cursor getHistory(String id) throws SQLException {
//		
//		Cursor mCursor =
//                db.query(true, DBTABLETRANS, new String[] {KEY_IDTRANS,KEY_IDUSER,
//                KEY_MASUK, KEY_KELUAR,KEY_TANGGAL,KEY_STATUS}, KEY_IDTRANS+ "='" +id+"'", null,
//                null, null, null, null);
//        if (mCursor != null) {
//            mCursor.moveToFirst();
//        }
//        return mCursor;
//
//	   }
	


   

	
	public static class DbHelper extends SQLiteOpenHelper{
		
		public DbHelper(Context ct){
			super(ct, DBNAME, null, DBVERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub
			try{
				db.execSQL(CREATETABLELOGIN);
				db.execSQL(CREATETABLECHAT);
				
				
			}catch(Exception e){
				e.printStackTrace();
				
			}
			
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			
			Log.w(TAG, "Upgrading database from version " + oldVersion + "to "
			                    + newVersion + ", which will destroy all old data");
			            db.execSQL("DROP TABLE IF EXISTS chat");
			            onCreate(db);
			            
			            Log.w(TAG, "Upgrading database from version " + oldVersion + "to "
			                    + newVersion + ", which will destroy all old data");
			            db.execSQL("DROP TABLE IF EXISTS login");
			            onCreate(db);
			            
		   

			
		}
		
	}
	
}
