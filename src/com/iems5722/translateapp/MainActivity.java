package com.iems5722.translateapp;

import java.util.Locale;
import java.util.Map;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

public class MainActivity extends Activity {

	private static final String TAG = "MainActivity";

	private EditText txtIn;
	private TextView txtOut;
	private Map<String, String> dict;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// get references to layout objects
		txtIn = (EditText) findViewById(R.id.txt_input);
		txtOut = (TextView) findViewById(R.id.txt_output);

		// initialize the dictionary once
		dict = new WordDictionary().getDictionary();

		// add click listener to button to call translateText()
		((Button) findViewById(R.id.btn_submit))
			.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// hide the keyboard
					InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(txtIn.getWindowToken(), 0);
					// translate
					translateText();
				}
			});

		// translate the text when user click done on the keyboard
		txtIn.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) ||
					(actionId == EditorInfo.IME_ACTION_DONE)) {
					translateText();
	            }
				return false;
			}
	    });
	}

	/**
	 * translate look up
	 */
	private void translateText() {
		// get user input
		String input = txtIn.getText().toString();
		Log.i(TAG, "input: " + input);

		if (input == null || input.length() < 1) {
			toastMissingText();
			return;
		}

		// try get word out of dictionary
		input = input.trim().toLowerCase(Locale.ENGLISH);
		if (dict.containsKey(input)) {
			txtOut.setText(dict.get(input));
			Log.i(TAG, "output: " + txtOut);
			return;
		}

		// show some feedback to user: translated text, error message, dialog etc
		Log.w(TAG, "not find in dict");
		txtOut.setText(null);

		new AlertDialog.Builder(this)
		.setTitle(R.string.msg_error)
		.setMessage(R.string.msg_not_in_dict)
		.setPositiveButton(R.string.btn_ok,
			new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog,
						int which) {
					dialog.dismiss();
				}
			})
		.create().show();
	}

	private void toastMissingText(){
		Toast.makeText(this, getText(R.string.msg_without_input), Toast.LENGTH_SHORT).show();
	}

	/**
	 * Share the translated text to other applications
	 */
	private void openShare() {
		String out = txtOut.getText().toString();
		if (out == null || out.length() < 1) {
			toastMissingText();
			return;
		}
		Intent i = new Intent();
		i.setAction(Intent.ACTION_SEND);
		i.putExtra(Intent.EXTRA_TEXT, out);
		i.setType("text/plain");
		startActivity(Intent.createChooser(i, getText(R.string.title_share_to)));
	}

	/*
	 * show share icon in action bar
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	/*
	 * respond to action buttons
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {
			case R.id.action_share:
				openShare();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
}
