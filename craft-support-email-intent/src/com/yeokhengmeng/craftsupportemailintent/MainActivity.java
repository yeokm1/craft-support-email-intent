package com.yeokhengmeng.craftsupportemailintent;

import com.yeokhengmeng.craftsupportemailintent.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {


	TextView outputView;
	CraftSupportEmail gatherer;
	EditText email;
	EditText subject;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		outputView = (TextView) findViewById(R.id.output_view);
		email = (EditText) findViewById(R.id.email_to);
		subject = (EditText) findViewById(R.id.subject);


		gatherer = new CraftSupportEmail(this);

		outputView.setMovementMethod(new ScrollingMovementMethod());
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void basicData(View view){
		String text = gatherer.getBasicDetails();
		outputView.setText(text);;
	}

	public void allData(View view){
		String text = gatherer.getAllDetails();
		outputView.setText(text);
	}

	public void composeBasicEmail(View view){
		gatherer.appendContent(gatherer.getAllDetails());

		try{
			gatherer.addRecipientTo(email.getText().toString());
			gatherer.appendContent(gatherer.getBasicDetails());
			gatherer.appendSubject(subject.getText().toString());
			Intent intent = gatherer.generateEmailIntent();
			gatherer.sendIntent(this, intent);
		} catch ( IllegalArgumentException e){
			Toast.makeText(this, "Invalid Email", Toast.LENGTH_SHORT).show();
		}

	}

	public void composeAllEmail(View view){
		

		try{
			gatherer.addRecipientTo(email.getText().toString());
			gatherer.appendContent(gatherer.getAllDetails());
			gatherer.appendSubject(subject.getText().toString());
			Intent intent = gatherer.generateEmailIntent();
			gatherer.sendIntent(this, intent);
		} catch ( IllegalArgumentException e){
			Toast.makeText(this, "Invalid Email", Toast.LENGTH_SHORT).show();
		}
		
	}

}
