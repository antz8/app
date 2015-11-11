package alarm;

import com.example.p_talk.R;
import com.example.p_talk.R.raw;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class AlarmScreen extends Activity {
	
	public final String TAG = this.getClass().getSimpleName();

	private WakeLock mWakeLock;
	private MediaPlayer mPlayer;

	private static final int WAKELOCK_TIMEOUT = 60 * 1000;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//Setup layout
		this.setContentView(R.layout.activity_alarm_screen);

		String name = getIntent().getStringExtra("name");
		String jam_mulai = getIntent().getStringExtra("jam_mulai");
		String jam_selesai = getIntent().getStringExtra("jam_selesai");
		String tanggal_mulai = getIntent().getStringExtra("tanggal_mulai");
		String tanggal_selesai = getIntent().getStringExtra("tanggal_selesai");
		String ket = getIntent().getStringExtra("keterangan");
		//String tone = getIntent().getStringExtra(AlarmManagerHelper.TONE);
		
		TextView tvName = (TextView) findViewById(R.id.alarm_screen_title);
		tvName.setText(name);
		
		TextView tvTime = (TextView) findViewById(R.id.alarm_screen_time);
		tvTime.setText(jam_mulai);
		
		TextView tvKet = (TextView) findViewById(R.id.alarm_screen_name);
		tvKet.setText("Jadwal Pengerjaan :"+tanggal_mulai+"("+jam_mulai+") s/d "+tanggal_selesai+"("+jam_selesai+") \n Keterangan : "+ket);
		
		Button dismissButton = (Button) findViewById(R.id.alarm_screen_button);
		dismissButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				mPlayer.stop();
				finish();
			}
		});

		//Play alarm tone
		//mPlayer = new MediaPlayer();
		try {
			
					mPlayer = MediaPlayer.create(this, raw.alarm);
					//mPlayer.create(this, raw.alarm);
					//mPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
					mPlayer.setLooping(true);
					//mPlayer.prepare();
					mPlayer.setVolume(1, 1);
					mPlayer.start();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//Ensure wakelock release
		Runnable releaseWakelock = new Runnable() {

			@Override
			public void run() {
				getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
				getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
				getWindow().clearFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
				getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);

				if (mWakeLock != null && mWakeLock.isHeld()) {
					mWakeLock.release();
				}
			}
		};

		new Handler().postDelayed(releaseWakelock, WAKELOCK_TIMEOUT);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	protected void onResume() {
		super.onResume();

		// Set the window to keep screen on
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);

		// Acquire wakelock
		PowerManager pm = (PowerManager) getApplicationContext().getSystemService(Context.POWER_SERVICE);
		if (mWakeLock == null) {
			mWakeLock = pm.newWakeLock((PowerManager.FULL_WAKE_LOCK | PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP), TAG);
		}

		if (!mWakeLock.isHeld()) {
			mWakeLock.acquire();
			Log.i(TAG, "Wakelock aquired!!");
		}

	}

	@Override
	protected void onPause() {
		super.onPause();

		if (mWakeLock != null && mWakeLock.isHeld()) {
			mWakeLock.release();
		}
	}
	
	@Override
	public void onBackPressed() {
	
		mPlayer.stop();
		finish();
	
	}
	
	
}
