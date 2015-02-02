package com.iems5722.translateapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import android.view.View.OnLongClickListener;

public class HistoryDeleteListener implements OnLongClickListener {

	private static final String TAG = "HistoryDeleteListener";

	public interface HistoryDeleteDelegate {
		public void deleted(int positon);
	}

	public HistoryDeleteListener(HistoryDeleteDelegate delegate, int position){
		super();
		this.delegate = delegate;
		this.position = position;
	}

	private int position;
	private HistoryDeleteDelegate delegate;

	@Override
	public boolean onLongClick(View v) {
		new AlertDialog.Builder(v.getContext())
			.setTitle(R.string.msg_confirm_delete_title)
			.setMessage(R.string.msg_confirm_delete_msg)
			.setPositiveButton(R.string.btn_no,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Log.d(TAG, "cancel delete");
						dialog.dismiss();
					}
				})
			.setNegativeButton(R.string.btn_yes,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Log.i(TAG, "delete position: " + position);
						if (delegate != null) delegate.deleted(position);
						dialog.dismiss();
					}
				})
			.create().show();
		return true;
	}

}