using System;
using System.Collections.Generic;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading;
using System.Threading.Tasks;

namespace LaserBotController
{
	class SVGRender
	{
		public ImageControl.ImageControl ImageControl { get; set; }
		public Bitmap Image { get; set; }
		public List<Path> Paths { get; set; }

		public Thread SimulateThread { get; set; }
		public int RefreshPerPixels { get; set; }
		public int SimulateDelay { get; set; }
		public int ResetDelay { get; set; }

		private int simulateDrawlinePixelCount;
		private bool isRunSimulate;
		private Bitmap originImage;

		public SVGRender(List<Path> paths, ImageControl.ImageControl imageControl)
		{
			ImageControl = imageControl;
			Paths = paths;
			ImageControl.Image = new Bitmap(Global.RenderLimit.X_i, Global.RenderLimit.Y_i);
			Image = (Bitmap)ImageControl.Image;

			ImageControl.MouseDown += ImageControl_Pause;
			ImageControl.MouseUp += ImageControl_Resume;
			imageControl.PreMouseWheel += ImageControl_Pause;
			imageControl.PostMouseWheel += ImageControl_Resume;

			RefreshPerPixels = 7;
			SimulateDelay = 2;
			ResetDelay = 5;
		}

		void ImageControl_Resume(object sender, System.Windows.Forms.MouseEventArgs e)
		{
			ResumeSimulate();
		}

		void ImageControl_Pause(object sender, System.Windows.Forms.MouseEventArgs e)
		{
			PauseSimulate();
		}

		public void Render()
		{
			int x, y, prevX = 0, prevY = 0;
			Point p;
			foreach (Path path in Paths)
			{
				for (int i = 0; i < path.Points.Count; i++)
				{
					p = path.Points[i];
					x = (int)Math.Round(p.X * Global.RenderScale);
					y = (int)Math.Round(p.Y * Global.RenderScale);
					if (i > 0)
					{
						Image.DrawLine(prevX, prevY, x, y);
					}
					prevX = x;
					prevY = y;
				}
			}
			originImage = (Bitmap)Image.Clone();
		}

		public void Refresh()
		{
			ImageControl.Refresh();
		}

		public void StartSimulate()
		{
			simulateDrawlinePixelCount = 0;
			isRunSimulate = true;
			SimulateThread = new Thread(doSimulate);
			SimulateThread.IsBackground = true;
			SimulateThread.Start();
		}

		public void PauseSimulate()
		{
			isRunSimulate = false;
		}

		public void ResumeSimulate()
		{
			isRunSimulate = true;
		}

		private void doSimulate()
		{
			int x, y, prevX = 0, prevY = 0;
			Point p;
			foreach (Path path in Paths)
			{
				while (!isRunSimulate) ;
				for (int i = 0; i < path.Points.Count; i++)
				{
					p = path.Points[i];
					x = (int)Math.Round(p.X * Global.RenderScale);
					y = (int)Math.Round(p.Y * Global.RenderScale);
					if (i > 0)
					{
						DrawLine(prevX, prevY, x, y, Global.SVGSimulateColor);
					}
					prevX = x;
					prevY = y;
				}
			}
			Thread.Sleep(ResetDelay * 1000);
			ImageControl.Image = originImage;
		}

		public void DrawLine(int x1, int y1, int x2, int y2, Color color)
		{
			// delta of exact value and rounded value of the dependant variable
			int d = 0;

			int dy = Math.Abs(y2 - y1);
			int dx = Math.Abs(x2 - x1);

			int dy2 = (dy << 1); // slope scaling factors to avoid floating
			int dx2 = (dx << 1); // point

			int ix = x1 < x2 ? 1 : -1; // increment direction
			int iy = y1 < y2 ? 1 : -1;

			if (dy <= dx)
			{
				for (; ; )
				{
					while (!isRunSimulate) ;
					try
					{
						Image.SetPixel(x1, y1, color);
					}
					catch
					{
						continue;
					}
					if (simulateDrawlinePixelCount >= RefreshPerPixels)
					{
						simulateDrawlinePixelCount = 0;
						ImageControl.Refresh();
						Thread.Sleep(SimulateDelay);
					}
					else
					{
						simulateDrawlinePixelCount++;
					}
					if (x1 == x2)
						break;
					x1 += ix;
					d += dy2;
					if (d > dx)
					{
						y1 += iy;
						d -= dx2;
					}
				}
			}
			else
			{
				for (; ; )
				{
					while (!isRunSimulate) ;
					try
					{
						Image.SetPixel(x1, y1, color);
					}
					catch
					{
						continue;
					}
					if (simulateDrawlinePixelCount >= RefreshPerPixels)
					{
						simulateDrawlinePixelCount = 0;
						ImageControl.Refresh();
						Thread.Sleep(SimulateDelay);
					}
					else
					{
						simulateDrawlinePixelCount++;
					}
					if (y1 == y2)
						break;
					y1 += iy;
					d += dx2;
					if (d > dy)
					{
						x1 += ix;
						d -= dy2;
					}
				}
			}
		}
	}

	static class SVGRenderExtension
	{
		public static void DrawLine(this Bitmap bmp, int x1, int y1, int x2, int y2)
		{
			// delta of exact value and rounded value of the dependant variable
			int d = 0;

			int dy = Math.Abs(y2 - y1);
			int dx = Math.Abs(x2 - x1);

			int dy2 = (dy << 1); // slope scaling factors to avoid floating
			int dx2 = (dx << 1); // point

			int ix = x1 < x2 ? 1 : -1; // increment direction
			int iy = y1 < y2 ? 1 : -1;

			if (dy <= dx)
			{
				for (; ; )
				{
					bmp.SetPixel(x1, y1, Global.SVGRenderColor);
					if (x1 == x2)
						break;
					x1 += ix;
					d += dy2;
					if (d > dx)
					{
						y1 += iy;
						d -= dx2;
					}
				}
			}
			else
			{
				for (; ; )
				{
					bmp.SetPixel(x1, y1, Global.SVGRenderColor);
					if (y1 == y2)
						break;
					y1 += iy;
					d += dx2;
					if (d > dy)
					{
						x1 += ix;
						d -= dy2;
					}
				}
			}
		}

		public static void DrawLine(this Bitmap bmp, int x1, int y1, int x2, int y2, Color color)
		{
			// delta of exact value and rounded value of the dependant variable
			int d = 0;

			int dy = Math.Abs(y2 - y1);
			int dx = Math.Abs(x2 - x1);

			int dy2 = (dy << 1); // slope scaling factors to avoid floating
			int dx2 = (dx << 1); // point

			int ix = x1 < x2 ? 1 : -1; // increment direction
			int iy = y1 < y2 ? 1 : -1;

			if (dy <= dx)
			{
				for (; ; )
				{
					bmp.SetPixel(x1, y1, color);
					if (x1 == x2)
						break;
					x1 += ix;
					d += dy2;
					if (d > dx)
					{
						y1 += iy;
						d -= dx2;
					}
				}
			}
			else
			{
				for (; ; )
				{
					bmp.SetPixel(x1, y1, color);
					if (y1 == y2)
						break;
					y1 += iy;
					d += dx2;
					if (d > dy)
					{
						x1 += ix;
						d -= dy2;
					}
				}
			}
		}
	}

}
