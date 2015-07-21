using System;
using System.Collections.Generic;
using System.Drawing;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace LaserBotController
{
	enum Dir : int
	{
		Up = 1, Down = 2, Left = 4, Right = 8
	}
	class Sample : IMessageFilter
	{

		public GRBL Grbl { get; set; }
		public List<Point> PointList { get; set; }
		public Point CurrentPoint { get; set; }
		public ImageControl.ImageControl ImageControl { get; set; }
		public Bitmap preview { get; set; }

		public Sample(GRBL grbl, ImageControl.ImageControl imageControl)
		{
			CurrentPoint = Global.MachineStartPoint;
			PointList = new List<Point>();
			Grbl = grbl;
			ImageControl = imageControl;
			ImageControl.Image = new Bitmap(Global.RenderLimit.X_i, Global.RenderLimit.Y_i);
			preview = (Bitmap)ImageControl.Image;
		}

		public void SaveSVG(string path)
		{
			StringBuilder data = new StringBuilder();
			data.Append("M ");
			foreach (Point p in PointList)
			{
				data.AppendFormat("{0:0.####} {1:0.####} ", p.X * Global.UnitFactor, p.Y * Global.UnitFactor);
			}
			string content = Properties.Resources.SampleSVG;
			content = content.Replace("{width}", Global.SVGLimit.X.ToString("F2"));
			content = content.Replace("{height}", Global.SVGLimit.Y.ToString("F2"));
			content = content.Replace("{d}", data.ToString());
			File.WriteAllText(path, content);
		}

		public void KeyDown(object sender, KeyEventArgs e)
		{
			int keyState = -1;
			switch (e.KeyCode)
			{
				case Keys.Up:
					keyState = (int)Dir.Up;
					break;
				case Keys.Down:
					keyState = (int)Dir.Down;
					break;
				case Keys.Left:
					keyState = (int)Dir.Left;
					break;
				case Keys.Right:
					keyState = (int)Dir.Right;
					break;
			}
			if (keyState != -1) Move((Dir)keyState, Global.SampleStep);
		}

		enum WindowsMessages
		{
			WM_LBUTTONDOWN = 0x0201,
			WM_LBUTTONUP = 0x0202,
			WM_MOUSEMOVE = 0x0200,
			WM_MOUSEWHEEL = 0x020A,
			WM_RBUTTONDOWN = 0x0204,
			WM_RBUTTONUP = 0x0205,
			WM_MOUSELEAVE = 0x02A3,
			WM_KEYDOWN = 0x0100,
			WM_KEYUP = 0x0101
		}

		public bool PreFilterMessage(ref Message m)
		{
			if (m.Msg == ((int)WindowsMessages.WM_KEYDOWN))
			{
				KeyDown(this, new KeyEventArgs((Keys)m.WParam));
			}
			return false;
		}

		public bool Move(Dir dir, double step)
		{
			Point newPoint = CurrentPoint.Copy();
			if ((dir & Dir.Up) != 0)
			{
				newPoint.Y -= step;
			}
			if ((dir & Dir.Down) != 0)
			{
				newPoint.Y += step;
			}
			if ((dir & Dir.Left) != 0)
			{
				newPoint.X -= step;
			}
			if ((dir & Dir.Right) != 0)
			{
				newPoint.X += step;
			}
			if (newPoint.Equals(CurrentPoint))
			{
				return false;
			}
			if (newPoint.X < Global.ZeroPoint.X || newPoint.Y < Global.ZeroPoint.Y ||
				newPoint.X > Global.MachineLimit.X || newPoint.Y > Global.MachineLimit.Y)
			{
				return false;
			}
			bool result = "ok".Equals(Grbl.SendAndWait(string.Format("G0 X{0} Y{1}",
				newPoint.X - Global.MachineStartPoint.X, newPoint.Y - Global.MachineStartPoint.Y)));
			if (result)
			{
				preview.DrawLine((int)Math.Round(CurrentPoint.X * Global.UnitFactor * Global.RenderScale),
					(int)Math.Round(CurrentPoint.Y * Global.UnitFactor * Global.RenderScale),
					(int)Math.Round(newPoint.X * Global.UnitFactor * Global.RenderScale),
					(int)Math.Round(newPoint.Y * Global.UnitFactor * Global.RenderScale));
				ImageControl.Refresh();
				CurrentPoint = newPoint;
				PointList.Add(CurrentPoint);
			}
			return result;
		}

	}
}
