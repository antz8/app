package action;

import main.Login;

import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

import xmpp.XMPPLogic;

import com.example.p_talk.R;

import database.DBAdapter;

import activity.MenuTabActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.view.View.OnClickListener;

public class Profile extends Activity {

	private XMPPConnection connection;

	EditText pass, repass;

	String txtPass;

	Button change, logout;
	
	DBAdapter db;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.profile);
		connection = XMPPLogic.getInstance().getConnection();
		db = new DBAdapter(this);
		pass = (EditText) findViewById(R.id.newPasswor);
		repass = (EditText) findViewById(R.id.reNewPassword);

		change = (Button) findViewById(R.id.btnChangepassword);
		logout = (Button) findViewById(R.id.btnLogout);

		change.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (pass.getText().toString()
						.equals(repass.getText().toString())) {
					txtPass = pass.getText().toString();
					changePassword(txtPass);
					pass.setText("");
					repass.setText("");
				} else {
					Toast.makeText(getApplicationContext(), "Invalid password",
							Toast.LENGTH_SHORT).show();
				}
			}
		});

		logout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				 finish(); // finish activity
				 db.open();
				 db.deleteLogin(connection.getUser().replace("@ptalk/Smack", ""));
				 db.close();
				 connection.disconnect();
				 connection = null;
				 XMPPLogic.getInstance().setConnection(null);
				 Intent i = new Intent(Profile.this, Login.class);
				 startActivity(i);
				 
			}
		});
	}

	public boolean changePassword(String pwd) {
		if (connection == null)
			return false;
		try {
			connection.getAccountManager().changePassword(pwd);
			return true;
		} catch (XMPPException e) {
			return false;
		}
	}
	
	@Override
	public void onBackPressed() {
		finish();
		Intent i = getIntent();
		i.setClass(getApplicationContext(), MenuTabActivity.class);
		startActivity(i);
	}


}
