package activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.example.p_talk.R;

import action.Chats_Group_Action;
import adapter.Groups_Adapter;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.filter.IQTypeFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.provider.PrivacyProvider;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smackx.GroupChatInvitation;
import org.jivesoftware.smackx.PrivateDataManager;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.packet.ChatStateExtension;
import org.jivesoftware.smackx.packet.LastActivity;
import org.jivesoftware.smackx.packet.OfflineMessageInfo;
import org.jivesoftware.smackx.packet.OfflineMessageRequest;
import org.jivesoftware.smackx.packet.SharedGroupsInfo;
import org.jivesoftware.smackx.provider.BytestreamsProvider;
import org.jivesoftware.smackx.provider.DataFormProvider;
import org.jivesoftware.smackx.provider.DelayInformationProvider;
import org.jivesoftware.smackx.provider.DiscoverInfoProvider;
import org.jivesoftware.smackx.provider.DiscoverItemsProvider;
import org.jivesoftware.smackx.provider.MUCAdminProvider;
import org.jivesoftware.smackx.provider.MUCOwnerProvider;
import org.jivesoftware.smackx.provider.MUCUserProvider;
import org.jivesoftware.smackx.provider.MessageEventProvider;
import org.jivesoftware.smackx.provider.MultipleAddressesProvider;
import org.jivesoftware.smackx.provider.RosterExchangeProvider;
import org.jivesoftware.smackx.provider.StreamInitiationProvider;
import org.jivesoftware.smackx.provider.VCardProvider;
import org.jivesoftware.smackx.provider.XHTMLExtensionProvider;
import org.jivesoftware.smackx.search.UserSearch;
import org.json.JSONException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import service.BackgroundService;

import xmpp.XMPPLogic;

@SuppressLint("NewApi") 
public class GroupsActivity extends Activity {

	private final static String discoNode = "http://jabber.org/protocol/muc#rooms";
	private final static String discoNamespace = "http://jabber.org/protocol/muc";
	private static Map joinedRooms = new WeakHashMap();

	HashMap<String, String> hash;
	GridView lv;
	ArrayList<HashMap<String, String>> array;

	String name_group[] = { "Alpro Dasar", "PBO", "PBO Lanjut", "Vispro",
			"Enterprise", "Enterprise Lanjut", "Alpro Lanjut", "XML", "APB",
			"PDBF", "Framework" };

	private XMPPConnection connection;

	private AQuery aq;

	private MultiUserChat mMultiUserChat;
	BackgroundService b = new BackgroundService();

	String naturalName, nameroom;

	@SuppressLint("NewApi") @Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.groups);
		
		configure(ProviderManager.getInstance());

		MenuTabActivity myTabs = (MenuTabActivity) this.getParent();
		myTabs.setTitle("My Groups");
		
//		getActionBar().setTitle("My Groups");
		
		connection = XMPPLogic.getInstance().getConnection();
		array = new ArrayList<HashMap<String, String>>();
		;

		lv = (GridView) findViewById(R.id.list_group);
		aq = new AQuery(this);

		// listGroups();
		// getGroups();
		//asyncGroup();
		// AddPacketListener();

		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				naturalName = array.get(position).get("name1");
				Log.i("Natural name", "Name :" + naturalName);
				nameroom = array.get(position).get("nameroom");

				Intent i = getIntent();// new
										// Intent(GroupsActivity.this,Chats_Group_Action.class);
				i.setClass(getApplicationContext(), Chats_Group_Action.class);
				i.putExtra("naturalname", naturalName);
				i.putExtra("nameroom", nameroom);
				startActivity(i);

			}

		});
	}

	
	
	public void listGroups() {

		for (int i = 0; i < name_group.length; i = i + 2) {

			hash = new HashMap<String, String>();

			hash.put("name1", name_group[i]);

			array.add(hash);

		}

		Groups_Adapter adapter = new Groups_Adapter(this.getBaseContext(),
				array);
		lv.setAdapter(adapter);

	}

	public void asyncGroup() {
		String url = "http://" + XMPPLogic.getInstance().getHost()
				+ ":8112/ptalk/ptalk/"
				+ connection.getUser().replace("/Smack", "") + "&a&a";
		// String url = "http://localhost:8112/akbar/json/jadwal/asal";
		aq.ajax(url, String.class, this, "Output");
	}

	public void Output(String url, String txt, AjaxStatus status)
			throws JSONException {
		if (txt != null) {
			// JSONArray listMahasiswa = txt.getJSONArray("");
			Document doc = Jsoup.parse(txt, "UTF-8");
			Elements itm = doc.getElementsByTag("row");

			for (Element e : itm) {

				Elements child = e.getAllElements();
				hash = new HashMap<String, String>();

				for (Element childitem : child) {
					// Log.i("Nama", ""+childitem.nodeName());
					if (childitem.nodeName().equals("name")) {
						hash.put("nameroom", childitem.text());

					}
					if (childitem.nodeName().equals("naturalname")) {
						hash.put("name1", childitem.text());

					}
				}

				array.add(hash);
			}

			Groups_Adapter adapter = new Groups_Adapter(this.getBaseContext(),
					array);
			lv.setAdapter(adapter);

		} else {
			Log.d("tes", "null");
		}
	}

//	public List<HostedRoom> getHostRooms() {
//		if (connection == null)
//			return null;
//		Collection<HostedRoom> hostrooms = null;
//		List<HostedRoom> roominfos = new ArrayList<HostedRoom>();
//		try {
//			new ServiceDiscoveryManager(connection);
//			hostrooms = MultiUserChat.getHostedRooms(connection,
//					connection.getServiceName());
//			for (HostedRoom entry : hostrooms) {
//				roominfos.add(entry);
//				Log.i("room",
//						"Name: " + entry.getName() + " - ID:" + entry.getJid());
//			}
//			Log.i("room", "Service meeting number:" + roominfos.size());
//		} catch (XMPPException e) {
//			e.printStackTrace();
//		}
//
//		for (int i = 0; i < roominfos.size(); i++) {
//
//			hash = new HashMap<String, String>();
//			hash.put("nameroom", roominfos.get(i).get);
//
//			hash.put("name1", childitem.text());
//
//			array.add(hash);
//		}
//
//		Groups_Adapter adapter = new Groups_Adapter(this.getBaseContext(),
//				array);
//		lv.setAdapter(adapter);
//
//		return roominfos;
//
//	}

	public void AddPacketListener() {
		PacketFilter filter = new IQTypeFilter(IQ.Type.RESULT);

		this.connection.addPacketListener(new PacketListener() {
			public void processPacket(Packet paramPacket) {
				Log.i("form :", "Form :" + paramPacket.getFrom());
				Log.i("form :", "User :" + connection.getUser());

				if (paramPacket.getFrom().equals(
						connection.getUser().toString())) {
					String xml = paramPacket.toXML();
					String from[];

					System.out.println(xml);
					from = paramPacket.getFrom().split("/");
					Pattern pattern = Pattern.compile("<item jid=\"(.*?)/>");
					Matcher matcher = pattern.matcher(xml);
					String parts[];

					ArrayList<String> Roomlist = new ArrayList<String>();

					Roomlist.clear();
					while (matcher.find()) {

						parts = matcher.group(1).split("@");
						Roomlist.add(parts[0]);

					}
					return;
				}

			}
		}, filter);

	}

	private Boolean exit = false;

	@Override
	public void onBackPressed() {
		if (exit) {
			finish(); // finish activity
			connection.disconnect();
			XMPPLogic.getInstance().setConnection(null);
			// android.os.Process.killProcess(android.os.Process.myPid());
			// System.exit(1);
		} else {
			Toast.makeText(this, "Press Back again to Exit.",
					Toast.LENGTH_SHORT).show();
			exit = true;
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					exit = false;
				}
			}, 3 * 1000);

		}

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		connection = XMPPLogic.getInstance().getConnection();
		array.clear();
		asyncGroup();
		MenuTabActivity myTabs = (MenuTabActivity) this.getParent();
		myTabs.setTitle("My Groups");
		
	}

	public void configure(ProviderManager pm) {

		// Private Data Storage
		pm.addIQProvider("query", "jabber:iq:private",
				new PrivateDataManager.PrivateDataIQProvider());

		// Time
		try {
			pm.addIQProvider("query", "jabber:iq:time",
					Class.forName("org.jivesoftware.smackx.packet.Time"));
		} catch (ClassNotFoundException e) {
			Log.w("TestClient",
					"Can't load class for org.jivesoftware.smackx.packet.Time");
		}

		// Roster Exchange
		pm.addExtensionProvider("x", "jabber:x:roster",
				new RosterExchangeProvider());

		// Message Events
		pm.addExtensionProvider("x", "jabber:x:event",
				new MessageEventProvider());

		// Chat State
		pm.addExtensionProvider("active",
				"http://jabber.org/protocol/chatstates",
				new ChatStateExtension.Provider());
		pm.addExtensionProvider("composing",
				"http://jabber.org/protocol/chatstates",
				new ChatStateExtension.Provider());
		pm.addExtensionProvider("paused",
				"http://jabber.org/protocol/chatstates",
				new ChatStateExtension.Provider());
		pm.addExtensionProvider("inactive",
				"http://jabber.org/protocol/chatstates",
				new ChatStateExtension.Provider());
		pm.addExtensionProvider("gone",
				"http://jabber.org/protocol/chatstates",
				new ChatStateExtension.Provider());

		// XHTML
		pm.addExtensionProvider("html", "http://jabber.org/protocol/xhtml-im",
				new XHTMLExtensionProvider());

		// Group Chat Invitations
		pm.addExtensionProvider("x", "jabber:x:conference",
				new GroupChatInvitation.Provider());

		// Service Discovery # Items
		pm.addIQProvider("query", "http://jabber.org/protocol/disco#items",
				new DiscoverItemsProvider());

		// Service Discovery # Info
		pm.addIQProvider("query", "http://jabber.org/protocol/disco#info",
				new DiscoverInfoProvider());

		// Data Forms
		pm.addExtensionProvider("x", "jabber:x:data", new DataFormProvider());

		// MUC User
		pm.addExtensionProvider("x", "http://jabber.org/protocol/muc#user",
				new MUCUserProvider());

		// MUC Admin
		pm.addIQProvider("query", "http://jabber.org/protocol/muc#admin",
				new MUCAdminProvider());

		// MUC Owner
		pm.addIQProvider("query", "http://jabber.org/protocol/muc#owner",
				new MUCOwnerProvider());

		// Delayed Delivery
		pm.addExtensionProvider("x", "jabber:x:delay",
				new DelayInformationProvider());

		// Version
		try {
			pm.addIQProvider("query", "jabber:iq:version",
					Class.forName("org.jivesoftware.smackx.packet.Version"));
		} catch (ClassNotFoundException e) {
			// Not sure what's happening here.
		}

		// VCard
		pm.addIQProvider("vCard", "vcard-temp", new VCardProvider());

		// Offline Message Requests
		pm.addIQProvider("offline", "http://jabber.org/protocol/offline",
				new OfflineMessageRequest.Provider());

		// Offline Message Indicator
		pm.addExtensionProvider("offline",
				"http://jabber.org/protocol/offline",
				new OfflineMessageInfo.Provider());

		// Last Activity
		pm.addIQProvider("query", "jabber:iq:last", new LastActivity.Provider());

		// User Search
		pm.addIQProvider("query", "jabber:iq:search", new UserSearch.Provider());

		// SharedGroupsInfo
		pm.addIQProvider("sharedgroup",
				"http://www.jivesoftware.org/protocol/sharedgroup",
				new SharedGroupsInfo.Provider());

		// JEP-33: Extended Stanza Addressing
		pm.addExtensionProvider("addresses",
				"http://jabber.org/protocol/address",
				new MultipleAddressesProvider());

		// FileTransfer
		pm.addIQProvider("si", "http://jabber.org/protocol/si",
				new StreamInitiationProvider());

		pm.addIQProvider("query", "http://jabber.org/protocol/bytestreams",
				new BytestreamsProvider());

		// Privacy
		pm.addIQProvider("query", "jabber:iq:privacy", new PrivacyProvider());
		// pm.addIQProvider("command", "http://jabber.org/protocol/commands",
		// new AdHocCommandDataProvider());
		// pm.addExtensionProvider("malformed-action",
		// "http://jabber.org/protocol/commands", new
		// AdHocCommandDataProvider.MalformedActionError());
		// pm.addExtensionProvider("bad-locale",
		// "http://jabber.org/protocol/commands", new
		// AdHocCommandDataProvider.BadLocaleError());
		// pm.addExtensionProvider("bad-payload",
		// "http://jabber.org/protocol/commands", new
		// AdHocCommandDataProvider.BadPayloadError());
		// pm.addExtensionProvider("bad-sessionid",
		// "http://jabber.org/protocol/commands", new
		// AdHocCommandDataProvider.BadSessionIDError());
		// pm.addExtensionProvider("session-expired",
		// "http://jabber.org/protocol/commands", new
		// AdHocCommandDataProvider.SessionExpiredError());

	}

	

//	@Override
//	  public boolean onCreateOptionsMenu(Menu menu) {
//	    MenuInflater inflater = getMenuInflater();
//	    inflater.inflate(R.menu.menu, menu);
//	    return true;
//	  }
//
//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		switch (item.getItemId()) {
//		case android.R.id.home:
//			Intent intent = new Intent(this, MenuTabActivity.class);
//			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//			startActivity(intent);
//			return true;
//		case R.id.logout:
//			
//		default:
//			return super.onOptionsItemSelected(item);
//		}
//	}
}
