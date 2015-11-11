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


}
