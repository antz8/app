package main;


import java.util.Collection;
import java.util.Currency;
import java.util.Date;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Presence;

import service.AlarmReceiver;
import xmpp.XMPPLogic;

import com.example.p_talk.R;

import database.DBAdapter;

import activity.MenuTabActivity;
import alarm.AlarmManagerHelper;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class Main extends Activity {
	
	protected boolean _active = true;
    protected int _splashTime = 4000;
    
    Intent newIntent;

    private Context context;
    DBAdapter db;
    
    Date tes =  new Date();
    
    XMPPConnection connection;
    int i = 0;
    String username;
    String pass;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_screen);
		db = new DBAdapter(this);
		
		db.open();
		
		Cursor c = db.getAllListLogin();
		
		
		if (c != null){
			i = c.getCount();
			Log.i("Jumlah Lo", "Nih" + c.getCount());
			if (i > 0){
				username = c.getString(c.getColumnIndex("username")).toString();
				pass = c.getString(c.getColumnIndex("password")).toString();
			}	
		}
		
		db.close();
		
		
		Date currentDate = new Date(System.currentTimeMillis());
		currentDate.toString();
		tes.parse(currentDate.toString());
		Log.d("Date", "Date"+tes);
		
		this.context = this;
        Intent alarm = new Intent(this.context, AlarmReceiver.class);
        boolean alarmRunning = (PendingIntent.getBroadcast(this.context, 0, alarm, PendingIntent.FLAG_NO_CREATE) != null);
        if(alarmRunning == false) {
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this.context, 0, alarm, 0);
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), 1000, pendingIntent);
        }
        
       
       
        // thread for displaying the SplashScreen
        Thread splashTread = new Thread() {
            @Override
            public void run() {
                try {
                    int waited = 0;
                    while(_active && (waited < _splashTime)) {
                        sleep(100);
                        if(_active) {
                            waited += 100;
                        }
                    }
                } catch(InterruptedException e) {
                    // do nothing
                } finally {
                    finish();
                    if (i > 0){
                    	 
                    	setConnection();
                    	if (connection != null){
    						XMPPLogic.getInstance().setConnection(connection);
    						}
                    	newIntent=new Intent(Main.this,MenuTabActivity.class);
                        startActivityForResult(newIntent,0);
                    }else{
                    	newIntent=new Intent(Main.this,Login.class);
                        startActivityForResult(newIntent,0);
                    }
                    
                }
            }
        };
        splashTread.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            _active = false;
        }
        return true;
	}
    
    public void setConnection(){
		
    	
    	Log.i("XMPPClient", "onCreate called");
    		 
    		//String host = "www.apa.apa";
//    	   String host = "192.168.89.1";
//    		
//           String port = "5222";
//           String service = "ptalk";
    	
//           username = user.getText().toString();
//           pass = pass.getText().toString();
      	
      	 // Create a connection
          ConnectionConfiguration connConfig =
                  new ConnectionConfiguration(XMPPLogic.getInstance().getHost(), Integer.parseInt(XMPPLogic.getInstance().getPort()), XMPPLogic.getInstance().getService());
          
          //connConfig.setReconnectionAllowed(true);  
         // connConfig.setSendPresence(true); 
          
          connection = new XMPPConnection(connConfig);

          try {
              connection.connect();  
              Log.i("XMPPClient", "[SettingsDialog] Connected to " + connection.getHost());
          } catch (XMPPException ex) {
              Log.e("XMPPClient", "[SettingsDialog] Failed to connect to " + connection.getHost());
              Log.e("XMPPClient", ex.toString());
             
          }
          try {
        	  
             	 connection.login(username, pass);
              
            	  Log.i("XMPPClient", "Logged in as " + connection.getUser());
                  
                  // Set the status to available
                  Presence presence = new Presence(Presence.Type.available);
                  connection.sendPacket(presence); 
                  Roster roster = connection.getRoster();
                  roster.addRosterListener(new RosterListener() {
                      // Ignored events public void entriesAdded(Collection<String> addresses) {}
                      public void entriesDeleted(Collection<String> addresses) {}
                      public void entriesUpdated(Collection<String> addresses) {}
                      public void presenceChanged(Presence presence) {
                   	   
                   	   Log.i("Friend","Presence changed: " + presence.getFrom() + " " + presence);
//                   	   Log.i("Friend","Presence changed: " + presence);
                      }
                      
        			@Override
        			public void entriesAdded(Collection<String> arg0) {
        				// TODO Auto-generated method stub
        			}      
                  });
                  
                  
//                  Intent i = getIntent();//new Intent(Login.this,MenuTabActivity.class);
//                  i.setClass(getApplicationContext(), MenuTabActivity.class);
//                  startActivity(i);	  
//                  user.setText("");
//                  pass.setText("");
             
                  
                  Intent alarm2 = new Intent(this.context, AlarmManagerHelper.class);
                  boolean alarmRunning2 = (PendingIntent.getBroadcast(this.context, 0, alarm2, PendingIntent.FLAG_NO_CREATE) != null);
                  if(alarmRunning2 == false) {
                      PendingIntent pendingIntent = PendingIntent.getBroadcast(this.context, 0, alarm2, 0);
                      AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                      alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), 3000, pendingIntent);
                  }


          } catch (XMPPException ex) {
//        	  err.setText("Kesalahan Kombinasi Username dan Password");
//        	  err.setVisibility(View.VISIBLE);
              Log.e("XMPPClient", "[SettingsDialog] Failed to log in as " + username);
              Log.e("XMPPClient", ex.toString());
             
          }
          
    	
    	}
	

}
