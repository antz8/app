package service;

import java.util.Collection;
import java.util.HashMap;

import main.Main;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.MessageTypeFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.util.StringUtils;

import xmpp.Connect;
import xmpp.XMPPLogic;

import com.example.p_talk.R;

import database.DBAdapter;

import action.Chats_Action;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;

import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Builder;
import android.util.Log;
import android.view.ViewDebug.FlagToString;
import android.webkit.WebView.FindListener;
import android.widget.TextView;

public class BackgroundService extends Service {

	private boolean isRunning;
	private Context context;
	private Thread backgroundThread;

	public XMPPConnection connection;

	PacketFilter filter = new MessageTypeFilter(Message.Type.chat);

	 Chats_Action c = new Chats_Action() ;
	// DBAdapter db = new DBAdapter(this);

	int x = 0;
	String nim = "";
	Intent i;

	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
	
		AlarmReceiver.setConnection(XMPPLogic.getInstance().getConnection(), context);
		
		return super.onStartCommand(intent, flags, startId);
	}

//	@Override
//	public void onCreate() {
//		this.context = this;
//		this.isRunning = false;
//
//		connection = XMPPLogic.getInstance().getConnection();
//		// if (connection != null) {
//		// // Add a packet listener to get messages sent to us
//		//
//		// connection.addPacketListener(new PacketListener() {
//		// public void processPacket(Packet packet) {
//		// Message message = (Message) packet;
//		// if (message.getBody() != null) {
//		// String fromName = StringUtils.parseBareAddress(message.getFrom());
//		//
//		// createNotification( getApplicationContext(), fromName,
//		// message.getBody().toString(),"PTALK" ,"-",fromName);
//		// Log.i("XMPPClient", "Got text [" + message.getBody() + "] from [" +
//		// fromName + "]");
//		// }
//		// }
//		// }, filter);
//		// }
//		this.backgroundThread = new Thread(myTask);
//	}
//
//	private Runnable myTask = new Runnable() {
//		@Override
//		public void run() {
//			
//			//setConnection(connection);
//
//			if (connection != null) {
//				connection.addPacketListener(new PacketListener() {
//					public void processPacket(Packet packet) {
//						Message message = (Message) packet;
//						if (message.getBody() != null) {
////							c.setCekNim("2");
//							x = x + 1;
//						//	nim = Chats_Action.getInstance().getCekNim();
//							//Log.i("NIM :", "NIMMMM :" +c.getCekNim() );
//							
//							if (x == 1) {
//								String fromName = StringUtils
//										.parseBareAddress(message.getFrom());
//								createNotification(getApplicationContext(),
//										fromName, message.getBody()
//												.toString(), "PTALK", "-",
//										fromName);
////								if (!nim.equals(fromName)) {
////									createNotification(getApplicationContext(),
////											fromName, message.getBody()
////													.toString(), "PTALK", "-",
////											fromName);
////								} else if (nim == null) {
////									createNotification(getApplicationContext(),
////											fromName, message.getBody()
////													.toString(), "PTALK", "-",
////											fromName);
////								} else {
//	//	
////								}
//		
//								Log.i("XMPPClient", "Got text Service["
//										+ message.getBody() + "] from ["
//										+ fromName + "]");
//							}
//		
//						}
//		
//					}
//				}, filter);
//			}
//			stopSelf();
//
//		}
//	};
//
//	@Override
//	public void onDestroy() {
//		this.isRunning = false;
//	}



//	public void createNotification(Context context, String msg,
//			String txtMessage, String alert, String name, String nim) {
//
//		Log.i("X", "X" + x);
//	
//			// i = ca.getIntent();
//			//c.setConnection(null);
//			//c.onBackPressed();
//		if (x==1){
//			i = new Intent(context , Chats_Action.class);
//			// i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//			// i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//			i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
//			i.putExtra("name", name);
//			i.putExtra("nim", nim);
//			i.putExtra("recipient", nim);
//			i.putExtra("message", txtMessage);
//
//			PendingIntent pendingIntent = PendingIntent.getActivity(context,
//					PendingIntent.FLAG_UPDATE_CURRENT, i,
//					PendingIntent.FLAG_UPDATE_CURRENT);
//
//			NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
//					getApplicationContext()).setSmallIcon(R.drawable.icon)
//					.setContentTitle("PTALK").setContentText(txtMessage)
//					.setContentIntent(pendingIntent);
//
//			mBuilder.setDefaults(NotificationCompat.DEFAULT_SOUND);
//			mBuilder.setAutoCancel(true);
//
//			NotificationManager notificationManager = (NotificationManager) context
//					.getSystemService(Context.NOTIFICATION_SERVICE);
//
//			notificationManager.notify(1, mBuilder.build());
//		}
//		
//	}

//	public void setConnection(XMPPConnection connection) {
//
//		this.connection = connection;
//		if (connection != null) {
//			connection.addPacketListener(new PacketListener() {
//				public void processPacket(Packet packet) {
//					Message message = (Message) packet;
//					if (message.getBody() != null) {
//						c.setCekNim("2");
//						x = x + 1;
//					//	nim = Chats_Action.getInstance().getCekNim();
//						Log.i("NIM :", "NIMMMM :" +c.getCekNim() );
//						
//						if (x == 1) {
//							String fromName = StringUtils
//									.parseBareAddress(message.getFrom());
//							createNotification(getApplicationContext(),
//									fromName, message.getBody()
//											.toString(), "PTALK", "-",
//									fromName);
////							if (!nim.equals(fromName)) {
////								createNotification(getApplicationContext(),
////										fromName, message.getBody()
////												.toString(), "PTALK", "-",
////										fromName);
////							} else if (nim == null) {
////								createNotification(getApplicationContext(),
////										fromName, message.getBody()
////												.toString(), "PTALK", "-",
////										fromName);
////							} else {
////	
////							}
//	
//							Log.i("XMPPClient", "Got text Service["
//									+ message.getBody() + "] from ["
//									+ fromName + "]");
//						}
//	
//					}
//	
//				}
//			}, filter);
//		}
//	}
	
//	if (connection != null) {
//		// Add a packet listener to get messages sent to us
//		PacketFilter filter = new MessageTypeFilter(Message.Type.chat);
//		connection.addPacketListener(new PacketListener() {
//			public void processPacket(Packet packet) {
//				Message message = (Message) packet;
//				if (message.getBody() != null) {
//					c.setCekNim("2");
//					x = x + 1;
//				//	nim = Chats_Action.getInstance().getCekNim();
//					Log.i("NIM :", "NIMMMM :" +c.getCekNim() );
//					
//					if (x == 1) {
//						String fromName = StringUtils
//								.parseBareAddress(message.getFrom());
//						if (!nim.equals(fromName)) {
//							createNotification(getApplicationContext(),
//									fromName, message.getBody()
//											.toString(), "PTALK", "-",
//									fromName);
//						} else if (nim == null) {
//							createNotification(getApplicationContext(),
//									fromName, message.getBody()
//											.toString(), "PTALK", "-",
//									fromName);
//						} else {
//
//						}
//
//						Log.i("XMPPClient", "Got text Service["
//								+ message.getBody() + "] from ["
//								+ fromName + "]");
//					}
//
//				}
//
//			}
//		}, filter);
//
//	}
}
