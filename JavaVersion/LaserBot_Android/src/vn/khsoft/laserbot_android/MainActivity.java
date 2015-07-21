package vn.khsoft.laserbot_android;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends Activity {

	public static MainActivity self;

	private BluetoothHelper mBluetooth;
	private Button btnConnect;
	private Button btnDisconnect;
	private Button btnSend;
	private ImageView ivPreview;
	private boolean isConnected = false;
	private Uri fileUri;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		self = this;
		btnConnect = (Button) findViewById(R.id.btnConnect);
		btnDisconnect = (Button) findViewById(R.id.btnDisconnect);
		btnSend = (Button) findViewById(R.id.btnSend);
		ivPreview = (ImageView) findViewById(R.id.ivPreview);
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnConnect:
			mBluetooth = BluetoothHelper.getInstance();
			mBluetooth.connect(false);
			if (mBluetooth != null && !mBluetooth.isError) {
				setConnectState(true);
				showFileChooser();
			}
			break;
		case R.id.ivPreview:
			showFileChooser();
			break;
		case R.id.btnDisconnect:
			BluetoothHelper.disconnect();
			setConnectState(false);
			break;
		case R.id.btnSend:
			sendData();
			break;
		}
	}

	public static final int SFILE_BUFFER = 4096;
	private Handler handler = new Handler();

	private void sendData() {
		final ProgressDialog progress = new ProgressDialog(this);
		progress.setMessage("Sending image...");
		progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		progress.setIndeterminate(false);
		progress.setCancelable(false);
		progress.setProgress(0);
		progress.show();

		Thread thread = new Thread() {
			@Override
			public void run() {
				try {
					OutputStream outputStream = mBluetooth.mOutputStream;
					InputStream inputStream = self.getContentResolver()
							.openInputStream(fileUri);
					int size = inputStream.available(), remaining;
					progress.setMax(size);
					mBluetooth.sendMessage("BEGIN_TRANSFER_IMAGE");
					mBluetooth.sendMessage(String.valueOf(size));
					mBluetooth.sendMessage("START_TRANSFER_IMAGE");
					Thread.sleep(1000);
					byte[] buffer = new byte[SFILE_BUFFER];
					while ((remaining = inputStream.available()) != 0) {
						progress.setProgress(size - remaining);
						int recv = inputStream.read(buffer);
						outputStream.write(buffer, 0, recv);
						final int sent = size - remaining;
						handler.post(new Runnable() {
							public void run() {
								progress.setProgress(sent);
							}
						});
						Thread.sleep(10);
					}
					inputStream.close();
				} catch (Exception e) {
					final Exception fe = e;
					handler.post(new Runnable() {
						public void run() {
							MainActivity.self
									.messageBox("Error while send file-content: \n"
											+ fe.getMessage());
						}
					});
				}
				progress.dismiss();
			}
		};
		thread.setPriority(Thread.MAX_PRIORITY);
		thread.start();
	}

	private void setConnectState(boolean state) {
		btnConnect.setEnabled(!state);
		btnSend.setEnabled(state);
		btnDisconnect.setEnabled(state);
		ivPreview.setEnabled(state);
		isConnected = state;
		if (!state) {
			ivPreview.setImageResource(R.drawable.ic_launcher);
			fileUri = null;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void messageBox(String title, String content) {
		final AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
		dlgAlert.setTitle(title);
		dlgAlert.setMessage(content);
		dlgAlert.setPositiveButton("OK", null);
		dlgAlert.setCancelable(true);
		dlgAlert.create().show();
	}

	public void messageBox(String content) {
		messageBox("Message", content);
	}

	public void makeToast(String message) {
		Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
	}

	// FILE SELECT
	private static final int FILE_SELECT_CODE = 0;

	private void showFileChooser() {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType("image/jpeg");
		intent.addCategory(Intent.CATEGORY_OPENABLE);

		try {
			startActivityForResult(
					Intent.createChooser(intent, "Select a File to Upload"),
					FILE_SELECT_CODE);
		} catch (android.content.ActivityNotFoundException ex) {
			makeToast("Please install a File Manager.");
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case FILE_SELECT_CODE:
			if (resultCode == RESULT_OK) {
				try {
					ivPreview
							.setImageBitmap(resizeBitmapForImageView(MediaStore.Images.Media
									.getBitmap(this.getContentResolver(),
											fileUri = data.getData())));
				} catch (IOException e) {
				}
			}
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	public Bitmap resizeBitmapForImageView(Bitmap bitmap) {
		Bitmap resizedBitmap = null;
		int originalWidth = bitmap.getWidth();
		int originalHeight = bitmap.getHeight();
		int newWidth = -1;
		int newHeight = -1;
		float multFactor = -1.0F;
		if (originalHeight > originalWidth) {
			newHeight = 4096;
			multFactor = (float) originalWidth / (float) originalHeight;
			newWidth = (int) (newHeight * multFactor);
		} else if (originalWidth > originalHeight) {
			newWidth = 4096;
			multFactor = (float) originalHeight / (float) originalWidth;
			newHeight = (int) (newWidth * multFactor);
		} else if (originalHeight == originalWidth) {
			newHeight = 4096;
			newWidth = 4096;
		}
		resizedBitmap = Bitmap.createScaledBitmap(bitmap, newWidth, newHeight,
				false);
		return resizedBitmap;
	}

}
