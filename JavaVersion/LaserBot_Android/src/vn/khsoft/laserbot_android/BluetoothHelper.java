package vn.khsoft.laserbot_android;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

public class BluetoothHelper {
	private BluetoothAdapter mBluetoothAdapter;
	private BluetoothSocket mBluetoothSocket;
	private BluetoothDevice mBluetoothDevice;
	public OutputStream mOutputStream;
	public boolean isError = false;

	public static BluetoothHelper bluetoothInstance = null;

	public static BluetoothHelper getInstance() {
		return (bluetoothInstance == null || bluetoothInstance.isError) ? new BluetoothHelper()
				: bluetoothInstance;
	}

	private BluetoothHelper(String name) {
		bluetoothInstance = this;
		try {
			mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
			if (mBluetoothAdapter == null) {
				MainActivity.self
						.messageBox("Phone does not support bluetooth");
				isError = true;
				return;
			}
			if (!mBluetoothAdapter.isEnabled()) {
				MainActivity.self.messageBox("Bluetooth is not activated");
				isError = true;
				return;
			}

			boolean deviceFound = false;
			Set<BluetoothDevice> paired = mBluetoothAdapter.getBondedDevices();
			if (paired.size() > 0) {
				for (BluetoothDevice d : paired) {
					if (d.getName().equals(name)) {
						mBluetoothDevice = d;
						deviceFound = true;
						break;
					}
				}
			}

			if (!deviceFound) {
				MainActivity.self.messageBox("There is not bot paired");
				isError = true;
				return;
			}

		} catch (Exception e) {
			MainActivity.self.messageBox("Error creating bluetooth: \n"
					+ e.getMessage());
			isError = true;
			return;
		}

	}

	private BluetoothHelper() {
		this("Laser Bot - KH");
	}

	public boolean connect(boolean isReceive) {
		if (isError)
			return false;
		try {
			UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
			mBluetoothSocket = mBluetoothDevice
					.createRfcommSocketToServiceRecord(uuid);
			mBluetoothSocket.connect();
			mOutputStream = mBluetoothSocket.getOutputStream();
			MainActivity.self.makeToast(mBluetoothAdapter.getName()
					+ " connect successfully");
			return true;

		} catch (Exception e) {
			MainActivity.self.messageBox("Error while conecting: \n"
					+ e.getMessage());
			isError = true;
			return false;
		}
	}

	public static void disconnect() {
		try {
			bluetoothInstance.mOutputStream.close();
		} catch (Exception e) {
		}
		try {
			bluetoothInstance.mBluetoothSocket.close();
		} catch (Exception e) {
		}
		bluetoothInstance = null;
	}

	public void sendMessage(String msg) {
		try {
			mOutputStream.write(("`" + msg + "\n").getBytes());
		} catch (IOException e) {
			MainActivity.self.messageBox("Error while send meta-data: \n"
					+ e.getMessage());
		}
	}

}
