package xmpp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

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

import action.Chats_Action;
import android.app.Activity;
import android.os.Handler;
import android.util.Log;
import android.widget.ListView;

public class Connect{
	
	private XMPPConnection connection;
	
	public void setConnection(){
		Chats_Action c = new Chats_Action();
	
	Log.i("XMPPClient", "onCreate called");
		 
		//String host = "10.91.4.8";
		String host = "192.168.1.122";
		
       String port = "5222";
       String service = "ptalk";
       String username = "6301122194@ptalk";
       String password = "nggakpake";
  	
  	 // Create a connection
      ConnectionConfiguration connConfig =
              new ConnectionConfiguration(host, Integer.parseInt(port), service);
      connection = new XMPPConnection(connConfig);

      try {
          connection.connect();  
          Log.i("XMPPClient", "[SettingsDialog] Connected to " + connection.getHost());
      } catch (XMPPException ex) {
          Log.e("XMPPClient", "[SettingsDialog] Failed to connect to " + connection.getHost());
          Log.e("XMPPClient", ex.toString());
         
      }
      try {
          connection.login(username, password);
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
//           	   Log.i("Friend","Presence changed: " + presence);
              }
              
			@Override
			public void entriesAdded(Collection<String> arg0) {
				// TODO Auto-generated method stub
				
			}
              
          });
                  
          
          
      } catch (XMPPException ex) {
          Log.e("XMPPClient", "[SettingsDialog] Failed to log in as " + username);
          Log.e("XMPPClient", ex.toString());
         
      }
      
	
	}

	public XMPPConnection getConnection() {
		return connection;
	}

	public void setConnection(XMPPConnection connection) {
		this.connection = connection;
	}
	
	
	 
	
}
