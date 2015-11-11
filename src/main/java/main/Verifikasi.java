package main;

import com.example.p_talk.R;

import activity.MenuTabActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Verifikasi extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.verifikasi);
	}

	public void onClick_Verifikasi (View v){
		
		Intent i = new Intent(Verifikasi.this, MenuTabActivity.class);
		startActivity(i);
	}
}
