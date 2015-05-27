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
		public ListView ListViewGCode { get; set; }
		public ImageControl.ImageControl ImageControl { get; set; }
		public double FeedRate { get; set; }
		public Thread SendThread { get; set; }
		public GRBL Grbl;

		public GCode(List<Path> paths, GRBL grbl, double feedRate, ListView listView, ImageControl.ImageControl imageControl)
		{
			Paths = paths;
			GCodes = new List<string>();
			GCodesToSend = new List<string>();
			ListViewGCode = listView;
			ImageControl = imageControl;
			Grbl = grbl;
			FeedRate = feedRate;
		}

		public void Generate()
		{
			GCodes.Add("M05 G21 G90 S1000");
			GCodesToSend.Add("M5G21G90S750");

			GCodes.Add(string.Format("G01 F{0:0.0000}", FeedRate));
			GCodesToSend.Add(string.Format("G01F{0:0.0000}", FeedRate));

			Point p;
			double x, y;
			double start_X = Global.MachineStartPoint.X, start_Y = Global.MachineStartPoint.Y;
			for (int i = 0; i < Paths.Count; i++)
			{
				Path path = Paths[i];
				for (int j = 0; j < path.Points.Count; j++)
				{
					p = path.Points[j];
					x = (p.X / Global.UnitFactor) - start_X;
					y = (p.Y / Global.UnitFactor) - start_Y;
					if (j == 0)
					{
						GCodes.Add(string.Empty);
						GCodesToSend.Add(null);

						GCodes.Add(string.Format("(path id {0})", i));
						GCodesToSend.Add(null);

						GCodes.Add(string.Format("G00 X{0:0.0000} Y{1:0.0000}", x, y));
						GCodesToSend.Add(string.Format("G0X{0:0.0000}Y{1:0.0000}", x, y));
					}
					else
					{
						if (j == 1)
						{
							GCodes.Add("M03");
							GCodesToSend.Add("M3");
						}
						GCodes.Add(string.Format("G01 X{0:0.0000} Y{1:0.0000}", x, y));
						GCodesToSend.Add(string.Format("G1X{0:0.0000}Y{1:0.0000}", x, y));
						if (j == (path.Points.Count - 1))
						{
							GCodes.Add("M05");
							GCodesToSend.Add("M5");
						}
					}
				}
			}
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
			SendThread.Start();
		}

		private void doSend()
		{
			Grbl.ReceiveQueue.Clear();
			Grbl.SendQueue.Clear();
			for (int i = 0; i < GCodesToSend.Count; )
			{
				string line = GCodesToSend[i];
				if (line == null)
				{
					i++;
					continue;
				}
				if (Grbl.TrySend(line, GCodes[i]))
				{
					highlightGCode(i);
					i++;
				}
			}
		}

		private void highlightGCode(int index)
		{
			ListViewItem item = ListViewGCode.Items[index];
			item.BackColor = Global.GCode_SentBackColor;
			item.EnsureVisible();
		}

	}
}
