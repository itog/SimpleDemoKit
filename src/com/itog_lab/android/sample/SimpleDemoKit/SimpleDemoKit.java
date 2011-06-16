package com.itog_lab.android.sample.SimpleDemoKit;

import java.io.FileInputStream;
import java.io.FileOutputStream;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SimpleDemoKit extends OpenAccessoryBase implements OnClickListener {
	static final String TAG = "SimpleDemoKit";

	private TextView mInputLabel;
	private TextView mOutputLabel;
	private LinearLayout mInputContainer;
	private LinearLayout mOutputContainer;

	private InputController mInputController;
	private OutputController mOutputController;
	private ADKCommandSender mAdkSender;
	private ADKCommandReceiver adkReceiver;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		adkReceiver = new ADKCommandReceiver();
		mAdkSender = new ADKCommandSender();
		
		setContentView(R.layout.main);
		enableControls(false);
		
		if (isAccessoryOpen()) {
			showControls();
		} else {
			hideControls();
		}
	}

	protected void showControls() {
		setContentView(R.layout.main);
		mInputLabel = (TextView) findViewById(R.id.inputLabel);
		mOutputLabel = (TextView) findViewById(R.id.outputLabel);
		mInputContainer = (LinearLayout) findViewById(R.id.inputContainer);
		mOutputContainer = (LinearLayout) findViewById(R.id.outputContainer);
		mInputLabel.setOnClickListener(this);
		mOutputLabel.setOnClickListener(this);

		mInputController = new InputController(this);
		mInputController.accessoryAttached();
		adkReceiver.setInputController(mInputController);
		
		mOutputController = new OutputController(this, mAdkSender); //FIXME consider mAdkController life cycle
		mOutputController.accessoryAttached();

		showTabContents(true);
	}

	protected void hideControls() {
		setContentView(R.layout.no_device);

		mInputController = null;
		mOutputController = null;
		adkReceiver.removeInputController();
	}

	void showTabContents(Boolean showInput) {
		if (showInput) {
			mInputContainer.setVisibility(View.VISIBLE);
			mInputLabel.setBackgroundColor(Color.DKGRAY);
			mOutputContainer.setVisibility(View.GONE);
			mOutputLabel.setBackgroundColor(Color.BLACK);
		} else {
			mInputContainer.setVisibility(View.GONE);
			mInputLabel.setBackgroundColor(Color.BLACK);
			mOutputContainer.setVisibility(View.VISIBLE);
			mOutputLabel.setBackgroundColor(Color.DKGRAY);
		}
	}

	public void onClick(View v) {
		int vId = v.getId();
		switch (vId) {
		case R.id.inputLabel:
			showTabContents(true);
			break;
		case R.id.outputLabel:
			showTabContents(false);
			break;
		}
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getTitle().equals("Simulate")) {
			showControls();
		} else if (item.getTitle().equals("Quit")) {
			finish();
			System.exit(0);
		}
		return true;
	}

	protected void enableControls(boolean enable) {
		if (enable) {
			showControls();
		} else {
			hideControls();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add("Simulate");
		menu.add("Quit");
		return true;
	}

	@Override
	protected void onAccessoryOpen(FileInputStream is, FileOutputStream os) {
		adkReceiver.setInputStream(is);
		Thread thread = new Thread(null, adkReceiver, "DemoKit");
		thread.start();
		
		mAdkSender.setOutputStream(os);
		enableControls(true);		
	}
	
	@Override
	protected void onAccessoryClose() {
		enableControls(false);
		//TODO should reset input, output stream ?
		adkReceiver.stopRunningThread();
	}
}