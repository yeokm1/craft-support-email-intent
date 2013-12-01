package com.yeokhengmeng.craftsupportemailintent;

import com.lamerman.FileDialog;
import com.lamerman.SelectionMode;
import com.yeokhengmeng.craftsupportemailintent.R;

import android.os.Bundle;
import android.os.Environment;
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
	
	EditText email;
	EditText subject;
	TextView filePathView;

	private final int FILE_DIALOG_RESULT = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		outputView = (TextView) findViewById(R.id.output_view);
		email = (EditText) findViewById(R.id.email_to);
		subject = (EditText) findViewById(R.id.subject);
		filePathView = (TextView) findViewById(R.id.file_path);
		filePathView.setMovementMethod(new ScrollingMovementMethod());


		outputView.setMovementMethod(new ScrollingMovementMethod());
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void basicData(View view){
		CraftSupportEmail gatherer = new CraftSupportEmail(this);
		String text = gatherer.getBasicDetails();
		outputView.setText(text);;
	}

	public void allData(View view){
		CraftSupportEmail gatherer = new CraftSupportEmail(this);
		String text = gatherer.getAllDetails();
		outputView.setText(text);
	}


	public void browse(View view){
		Intent intent = new Intent(getBaseContext(), FileDialog.class);
		intent.putExtra(FileDialog.START_PATH, Environment.getExternalStorageDirectory().getPath());

		//can user select directories or not
		intent.putExtra(FileDialog.CAN_SELECT_DIR, true);
		intent.putExtra(FileDialog.SELECTION_MODE, SelectionMode.MODE_OPEN);

		startActivityForResult(intent, FILE_DIALOG_RESULT);
	}


	public synchronized void onActivityResult(final int requestCode,
			int resultCode, final Intent data) {

		if (resultCode == Activity.RESULT_OK) {

			if (requestCode == FILE_DIALOG_RESULT) {
				String filePath = data.getStringExtra(FileDialog.RESULT_PATH);
				setFilePathView(filePath);
			}

			//		} else if (resultCode == Activity.RESULT_CANCELED) {
			//			showToast("no file selected");
		}

	}

	public void setFilePathView(String filePath){
		filePathView = (TextView) findViewById(R.id.file_path);
		filePathView.setText(filePath);
	}
	
	public void composeEmail(View view){
	CraftSupportEmail gatherer = new CraftSupportEmail(this);

	try{
		gatherer.addRecipientTo(email.getText().toString());
		gatherer.appendSubject(subject.getText().toString());
		
		gatherer.appendContent(outputView.getText().toString());

		String filePath = filePathView.getText().toString();

		if(filePath != null && (filePath.length() != 0)){
			gatherer.addAttachment(filePath);
		}
		Intent intent = gatherer.generateEmailIntent();
		gatherer.sendIntent(this, intent);
	} catch ( IllegalArgumentException e){
		Toast.makeText(this, "Invalid Email", Toast.LENGTH_SHORT).show();
	}

}

//	public void composeBasicEmail(View view){
//		CraftSupportEmail gatherer = new CraftSupportEmail(this);
//		gatherer.appendContent(gatherer.getAllDetails());
//
//		try{
//			gatherer.addRecipientTo(email.getText().toString());
//			gatherer.appendContent(gatherer.getBasicDetails());
//			gatherer.appendSubject(subject.getText().toString());
//
//			String filePath = filePathView.getText().toString();
//
//			if(filePath != null || filePath != ""){
//				gatherer.addAttachment(filePath);
//			}
//			Intent intent = gatherer.generateEmailIntent();
//			gatherer.sendIntent(this, intent);
//		} catch ( IllegalArgumentException e){
//			Toast.makeText(this, "Invalid Email", Toast.LENGTH_SHORT).show();
//		}
//
//	}
//
//	public void composeAllEmail(View view){
//		CraftSupportEmail gatherer = new CraftSupportEmail(this);
//
//
//		try{
//			gatherer.addRecipientTo(email.getText().toString());
//			gatherer.appendContent(gatherer.getAllDetails());
//			gatherer.appendSubject(subject.getText().toString());
//
//			String filePath = filePathView.getText().toString();
//			if(filePath != null || filePath != ""){
//				gatherer.addAttachment(filePath);
//			}
//			Intent intent = gatherer.generateEmailIntent();
//			gatherer.sendIntent(this, intent);
//		} catch ( IllegalArgumentException e){
//			Toast.makeText(this, "Invalid Email", Toast.LENGTH_SHORT).show();
//		}
//
//	}

}
