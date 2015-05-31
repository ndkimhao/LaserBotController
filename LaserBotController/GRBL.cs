using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.IO.Ports;
using System.Linq;
using System.Text;
using System.Threading;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace LaserBotController
{
	class GRBL
	{

		public SerialPort Serial { get; set; }
		public string PortName { get; set; }
		public ListView ListViewCommand;

		public Queue<string> ReceiveQueue;
		public Queue<string> SendQueue;

		private Queue<ListViewItem> LogQueue;
		private Thread LogThread;

		public GRBL(string portName, ListView listView)
		{
			PortName = portName;
			ListViewCommand = listView;
			ReceiveQueue = new Queue<string>();
			SendQueue = new Queue<string>();

			LogQueue = new Queue<ListViewItem>();
			LogThread = new Thread(doLog);
			LogThread.IsBackground = true;
			LogThread.Start();
		}

		private void doLog()
		{
			while (true)
			{
				if (LogQueue.Count > 0)
				{
					while (LogQueue.Count > 0)
					{
						ListViewCommand.Items.Add(LogQueue.Dequeue());
					}
					ListViewCommand.Items[ListViewCommand.Items.Count - 1].EnsureVisible();
				}
			}
		}

		public int TrySend(string rawData, string displayData)
		{
			while (ReceiveQueue.Count > 0)
			{
				if (ReceiveQueue.Dequeue() == "ok")
				{
					SendQueue.Dequeue();
				}
			}
			if ((SendQueue.StringLength() + rawData.Length) < Global.GRBL_BufferLimit)
			{
				rawData += "\n";
				try
				{
					Serial.Write(rawData);
				}
				catch (Exception)
				{
					throw;
				}
				logSend(displayData);
				SendQueue.Enqueue(rawData);
				return SendQueue.Count;
			}
			else
			{
				return -1;
			}
		}

		public string Connect()
		{
			Serial = new SerialPort(PortName, 115200);
			Serial.DataReceived += Serial_DataReceived;
			Serial.ReadTimeout = Global.GRBL_ReadTimeOut;
			Serial.NewLine = "\n";
			try
			{
				Serial.Open();
			}
			catch (Exception ex)
			{
				return ex.Message;
			}
			if (Serial.IsOpen)
			{
				sendByte(24);
				return null;
			}
			else
			{
				return "Strange error";
			}
		}

		private string serialBuffer = "";
		private string serialReceive = null;
		private void Serial_DataReceived(object sender, SerialDataReceivedEventArgs e)
		{
			while (Serial.BytesToRead > 0)
			{
				byte[] buffer = new byte[Serial.BytesToRead];
				int bytesRead = Serial.Read(buffer, 0, buffer.Length);
				if (bytesRead <= 0) return;
				serialBuffer += Encoding.UTF8.GetString(buffer, 0, bytesRead);
				string[] lines = serialBuffer.Split('\n');
				for (int i = 0; i < (lines.Length - 1); i++)
				{
					string line = lines[i].Trim('\r', '\n');
					if (line.Length > 0)
					{
						logReceive(line);
						ReceiveQueue.Enqueue(line);
						if (serialReceive == null)
						{
							serialReceive = line;
						}
					}
				}
				serialBuffer = lines[lines.Length - 1];
			}
		}

		public bool Homing()
		{
			try
			{
				serialReceive = null;
				Serial.WriteLine("$H");
				logSend("$H - homing");
				Stopwatch sw = new Stopwatch();
				sw.Start();
				while (sw.ElapsedMilliseconds < Global.GRBL_HomingTimeOut)
				{
					if (serialReceive != null)
					{
						if (serialReceive == "ok")
						{
							logInfo("Homing success");
							sendAndWait("G92 X0 Y0");
							return true;
						}
						return false;
					}
					Application.DoEvents();
				}
				logInfo("Homing failed");
			}
			catch (Exception ex)
			{
				logProgramError(ex.Message);
			}
			return false;
		}

		private string sendAndWait(string data)
		{
			try
			{
				logSend(data);
				Serial.WriteLine(data);
				Stopwatch sw = new Stopwatch();
				sw.Start();
				while (sw.ElapsedMilliseconds < Global.GRBL_ReadTimeOut)
				{
					if (serialReceive != null)
					{
						return serialReceive;
					}
					Application.DoEvents();
				}
				logProgramError("Receive timeout");
				return null;
			}
			catch (Exception ex)
			{
				logProgramError(ex.Message);
			}
			return null;
		}

		private void sendByte(byte data)
		{
			if (data == 24) logSend("Ctrl-X: SOFT RESET");
			Serial.Write(new byte[] { data }, 0, 1);
		}

		private void logSend(string data)
		{
			ListViewItem item = new ListViewItem();
			item.Text = data;
			item.ForeColor = Global.GRBL_SendColor;
			LogQueue.Enqueue(item);
		}

		private void logReceive(string data)
		{
			if (data.StartsWith("ALARM:") || data.StartsWith("error:"))
			{
				logError(data);
			}
			else
			{
				ListViewItem item = new ListViewItem();
				item.Text = data;
				item.ForeColor = Global.GRBL_ReceiveColor;
				LogQueue.Enqueue(item);
			}
		}

		private void logError(string data)
		{
			ListViewItem item = new ListViewItem();
			item.Text = data;
			item.ForeColor = Global.GRBL_ErrorColor;
			LogQueue.Enqueue(item);
		}

		private void logProgramError(string data)
		{
			ListViewItem item = new ListViewItem();
			item.Text = "Program error: " + data;
			item.ForeColor = Global.GRBL_ErrorColor;
			LogQueue.Enqueue(item);
		}

		private void logInfo(string data)
		{
			ListViewItem item = new ListViewItem();
			item.Text = "Info: " + data;
			item.ForeColor = Global.GRBL_InfoColor;
			LogQueue.Enqueue(item);
		}

	}

	public static class GRBLExtension
	{
		public static int StringLength(this Queue<string> q)
		{
			int length = 0;
			foreach (string str in q)
			{
				length += str.Length;
			}
			return length;
		}
	}

}
