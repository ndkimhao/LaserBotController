using System;
using System.Collections.Generic;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace LaserBotController
{
	class GCode
	{

		public List<Path> Paths { get; set; }
		public List<string> GCodes { get; set; }
		public List<string> GCodesToSend { get; set; }
		public List<Edge> GCodesPaths { get; set; }
		public ListView ListViewGCode { get; set; }
		public ImageControl.ImageControl ImageControl { get; set; }
		public double FeedRate { get; set; }
		public double LaserPower { get; set; }
		public Thread SendThread { get; set; }
		public GRBL Grbl { get; set; }

		public GCode(List<Path> paths, GRBL grbl, double feedRate, double laserPower, ListView listView, ImageControl.ImageControl imageControl)
		{
			Paths = paths;
			GCodes = new List<string>();
			GCodesToSend = new List<string>();
			GCodesPaths = new List<Edge>();
			ListViewGCode = listView;
			ImageControl = imageControl;
			Grbl = grbl;
			FeedRate = feedRate;
			LaserPower = laserPower;
		}

		public void Generate()
		{
			GCodes.Add(string.Format("M05 G21 G90 S{0:0.####}", LaserPower));
			GCodesToSend.Add(string.Format("M5G21G90S{0:0.####}", LaserPower));
			GCodesPaths.Add(null);

			GCodes.Add(string.Format("G01 F{0:0.####}", FeedRate));
			GCodesToSend.Add(string.Format("G01F{0:0.####}", FeedRate));
			GCodesPaths.Add(null);

			Point p;
			double x, y;
			double limit_y = Global.MachineLimit.Y;
			for (int i = 0; i < Paths.Count; i++)
			{
				Path path = Paths[i];
				for (int j = 0; j < path.Points.Count; j++)
				{
					p = path.Points[j];
					x = p.X / Global.UnitFactor;
					y = limit_y - p.Y / Global.UnitFactor;
					if (j == 0)
					{
						GCodes.Add(string.Empty);
						GCodesToSend.Add(null);
						GCodesPaths.Add(null);

						GCodes.Add(string.Format("(path id {0})", i));
						GCodesToSend.Add(null);
						GCodesPaths.Add(null);

						GCodes.Add(string.Format("G00 X{0:0.####} Y{1:0.####}", x, y));
						GCodesToSend.Add(string.Format("G0X{0:0.####}Y{1:0.####}", x, y));
						GCodesPaths.Add(null);
					}
					else
					{
						if (j == 1)
						{
							GCodes.Add("M03");
							GCodesToSend.Add("M3");
							GCodesPaths.Add(null);
						}

						GCodes.Add(string.Format("G01 X{0:0.####} Y{1:0.####}", x, y));
						GCodesToSend.Add(string.Format("G1X{0:0.####}Y{1:0.####}", x, y));
						GCodesPaths.Add(new Edge(path.Points[j - 1], p));

						if (j == (path.Points.Count - 1))
						{
							GCodes.Add("M05");
							GCodesToSend.Add("M5");
							GCodesPaths.Add(null);
						}
					}
				}
			}
			GCodes.Add(string.Format("G00 X{0:0.####} Y{1:0.####}", Global.MachineLimit.X, Global.MachineLimit.Y));
			GCodesToSend.Add(string.Format("G0X{0:0.####}Y{1:0.####}", Global.MachineLimit.X, Global.MachineLimit.Y));
			GCodesPaths.Add(null);
			OutputListView();
		}

		public void OutputListView()
		{

			ListViewGCode.Items.Clear();
			ListViewGCode.BeginUpdate();
			foreach (string line in GCodes)
			{
				ListViewItem item = new ListViewItem();
				item.Text = line;
				if (line.StartsWith("("))
				{
					item.ForeColor = Global.GCode_CommentColor;
				}
				else if (line.StartsWith("G00"))
				{
					item.ForeColor = Global.GCode_G00Color;
				}
				else if (line.StartsWith("G01") || line.StartsWith("M"))
				{
					item.ForeColor = Global.GCode_G01Color;
				}
				ListViewGCode.Items.Add(item);
			}
			ListViewGCode.EndUpdate();
		}

		public void Send()
		{
			SendThread = new Thread(doSend);
			SendThread.IsBackground = true;
			SendThread.Priority = ThreadPriority.Highest;
			SendThread.Start();
		}

		private void doSend()
		{
			Grbl.ReceiveQueue.Clear();
			Grbl.SendQueue.Clear();
			int result;
			Dictionary<int, int> data = new Dictionary<int, int>();
			for (int i = 0; i < GCodesToSend.Count; )
			{
				string line = GCodesToSend[i];
				if (line == null)
				{
					i++;
					continue;
				}
				if ((result = Grbl.TrySend(line, GCodes[i])) != -1)
				{
					data.Add(i, result);
					i++;
					continue;
				}
				foreach (KeyValuePair<int, int> entry in data)
				{
					highlightGCode(entry.Key, entry.Value);
				}
				data.Clear();
			}
			foreach (KeyValuePair<int, int> entry in data)
			{
				//highlightGCode(entry.Key, entry.Value);
				new Thread(() => highlightGCode(entry.Key, entry.Value)).Start();
			}
		}

		private void highlightGCode(int index, int count)
		{
			ListViewItem item = ListViewGCode.Items[index];
			item.Text += "  (" + (count > 1 ? count.ToString() : ("*** " + count + " ***")) + ")";
			item.BackColor = Global.GCode_SentBackColor;
			item.EnsureVisible();

			Edge edge = GCodesPaths[index];
			lock (ImageControl)
			{
				if (edge != null)
				{
					Point p1 = edge.p1;
					Point p2 = edge.p2;
					((Bitmap)ImageControl.Image).DrawLine(
						x1: (int)Math.Round(p1.X * Global.RenderScale),
						y1: (int)Math.Round(p1.Y * Global.RenderScale),
						x2: (int)Math.Round(p2.X * Global.RenderScale),
						y2: (int)Math.Round(p2.Y * Global.RenderScale),
						color: Global.SVGSentColor);
					ImageControl.Refresh();
				}
			}
		}

	}
}
