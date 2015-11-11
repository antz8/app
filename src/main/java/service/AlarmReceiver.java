package service;

import java.util.Collection;

import org.jivesoftware.smack.ConnectionConfiguration;
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

import xmpp.XMPPLogic;

import com.example.p_talk.R;

import action.Chats_Action;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Builder;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver {

	 public XMPPConnection connection;
	 private Handler mHandler = new Handler();
	 
	 Intent i;
	 
	//Context context;
	    
	@Override
	public void onReceive(final Context context, Intent arg1) {
		// TODO Auto-generated method stub
	
//		Intent background = new Intent(context, BackgroundService.class);
//        context.startService(background);
		setConnection(XMPPLogic.getInstance().getConnection(), context);
    
	}
	

	 
	public static void createNotification(Context context, String msg,
			String txtMessage, String alert, String name, String idd
	) {
			Intent i = new Intent(context , Chats_Action.class);
			i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
			i.putExtra("name", name);
			i.putExtra("idd", idd);
			i.putExtra("recipient", idd);
			i.putExtra("message", txtMessage);

			PendingIntent pendingIntent = PendingIntent.getActivity(context,
					-1, i,
					PendingIntent.FLAG_UPDATE_CURRENT);

			Builder mBuilder = new Builder(
					context).setSmallIcon(R.drawable.icon)
					.setContentTitle("PTALK").setContentText(txtMessage)
					.setContentIntent(pendingIntent);

			//mBuilder.setDefaults(NotificationCompat.DEFAULT_SOUND);
			mBuilder.setAutoCancel(true);

			NotificationManager notificationManager = (NotificationManager) context
					.getSystemService(Context.NOTIFICATION_SERVICE);

			notificationManager.notify(1, mBuilder.build());
		}
	
	public static void setConnection(XMPPConnection connection,final Context context) {

		//this.connection = connection;
		if (connection != null) {
			PacketFilter filter = new MessageTypeFilter(Message.Type.chat);
			connection.addPacketListener(new PacketListener() {
				public void processPacket(Packet packet) {
					Message message = (Message) packet;
					if (message.getBody() != null) {
						
							String fromName = StringUtils
									.parseBareAddress(message.getFrom());
							createNotification(context,
									fromName, message.getBody()
											.toString(), "PTALK", "-",
									fromName);
							Log.i("XMPPClient", "Got text Service["
									+ message.getBody() + "] from ["
									+ fromName + "]");
						}
	
					}
	
				
			}, filter);
		
	}

}
}


