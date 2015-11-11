package alarm;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.jivesoftware.smack.XMPPConnection;
import org.json.JSONException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import xmpp.XMPPLogic;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AlarmManagerHelper extends BroadcastReceiver {

	public static final String ID = "id";
//	public static final String NAME = "name";
//	public static final String TIME_HOUR = "timeHour";
//	public static final String KET = "keterangan";
	public static final String TONE = "alarmTone";

	public static XMPPConnection connection = XMPPLogic.getInstance()
			.getConnection();

	public static AQuery aq;

	Context ctx;

	public String idd = "a";

	//String idcek = "0";
	String id = "0";
	String tanggal_mulai, tanggal_selesai, jam_mulai, jam_selesai, judul,
			keterangan;

	String tampilan = "yyyy-MM-dd HH:mm:ss";

	SimpleDateFormat format = new SimpleDateFormat(tampilan);

	Date currentDate;

	@Override
	public void onReceive(Context context, Intent intent) {
		aq = new AQuery(context);
		setAlarms(context);
		ctx = context;

	}

	public void setAlarms(Context context) {

		asyncGetReminder(context, XMPPLogic.getInstance().getConnection());
	}

	public void asyncGetReminder(Context context, XMPPConnection conn) {

		if (conn != null) {

			idd = conn.getUser().replace("@ptalk/Smack", "").toString();

		}

		currentDate = new Date(System.currentTimeMillis());

		String tanggal = format.format(currentDate);
		String jam = currentDate.getHours() + ":" + currentDate.getMinutes()
				+ ":00";

		String url = "http://" + XMPPLogic.getInstance().getHost()
				+ ":8112/ptalk/reminder/reminder&" + idd + "&"
				+ tanggal.substring(0, 10) + "&" + tanggal.substring(11, 16)
				+ ":00";

		aq.ajax(url, String.class, this, "OutputName");

	}

	public void OutputName(String url, String txt, AjaxStatus status)
			throws JSONException {
		Log.i("Jumlah", "j");
		if (txt != null) {
			// JSONArray listMahasiswa = txt.getJSONArray("");

			Document doc = Jsoup.parse(txt, "UTF-8");
			Elements itm = doc.getElementsByTag("row");
			// Log.i("Jumlah", itm.size());

			for (Element e : itm) {

				id = e.getElementsByTag("id").html();
				tanggal_mulai = e.getElementsByTag("tanggal_mulai").html();
				tanggal_selesai = e.getElementsByTag("tanggal_selesai").html();
				judul = e.getElementsByTag("judul").html();
				jam_mulai = e.getElementsByTag("jam_mulai").html();
				jam_selesai = e.getElementsByTag("jam_selesai").html();
				keterangan = e.getElementsByTag("keterangan").html();

			}

			Log.i("Jumlah", "j" + itm.size());

			if (itm.size() > 0) {
				if (id.equals(XMPPLogic.getInstance().getId())) {

				} else {
					PendingIntent pIntent = createPendingIntent(ctx);
					// setAlarm(context, calendar, pIntent)
					Calendar calendar = Calendar.getInstance();

					final int nowDay = Calendar.getInstance().get(
							Calendar.DAY_OF_WEEK);
					final int nowHour = Calendar.getInstance().get(
							Calendar.HOUR_OF_DAY);
					final int nowMinute = Calendar.getInstance().get(
							Calendar.MINUTE);
					setAlarm(ctx, calendar, pIntent);
					XMPPLogic.getInstance().setId(id);
				}
			}
		} else {
			Log.d("tes", "null");
		}
	}

	@SuppressLint("NewApi")
	private static void setAlarm(Context context, Calendar calendar,
			PendingIntent pIntent) {
		AlarmManager alarmManager = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
			alarmManager.setExact(AlarmManager.RTC_WAKEUP,
					calendar.getTimeInMillis(), pIntent);

		} else {
			alarmManager.set(AlarmManager.RTC_WAKEUP,
					calendar.getTimeInMillis(), pIntent);
		}
	}

	public PendingIntent createPendingIntent(Context context) {
		Intent intent = new Intent(context, AlarmService.class);
		// intent.putExtra(ID, model.id);
		 intent.putExtra("name", judul);
		 intent.putExtra("jam_mulai", jam_mulai);
		 intent.putExtra("jam_selesai", jam_selesai);
		 intent.putExtra("tanggal_mulai", tanggal_mulai);
		 intent.putExtra("tanggal_selesai", tanggal_selesai);
		 intent.putExtra("keterangan", keterangan);
		// intent.putExtra(TONE, model.alarmTone.toString());

		return PendingIntent.getService(context, -1, intent,
				PendingIntent.FLAG_UPDATE_CURRENT);
	}

}
