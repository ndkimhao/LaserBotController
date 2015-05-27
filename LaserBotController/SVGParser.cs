﻿using System;
using System.Collections.Generic;
using System.Drawing;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Xml;

namespace LaserBotController
{
	class SVGParser
	{
		private string filePath;
		public string FilePath { get { return filePath; } }

		public List<Path> Paths { get; set; }

		public SVGParser(string filePath)
		{
			this.filePath = filePath;
			Paths = new List<Path>();
		}

		public void DoParse()
		{
			using (XmlReader reader = XmlReader.Create(new StreamReader(FilePath)))
			{
				int groupDepth = 0;
				while (reader.Read())
				{
					switch (reader.NodeType)
					{
						case XmlNodeType.Element:
							if (reader.Name == "path")
							{
								if (groupDepth == 0)
								{
									string pathData = reader.GetAttribute("d");

									string style = reader.GetAttribute("style");
									double fillSpace = -1;
									if (style != null)
									{
										int idx = style.IndexOf("fill:#");
										if (idx != -1)
										{
											float fillDensity = ColorTranslator.FromHtml(style.Substring(idx + 5, 7)).GetBrightness();
											fillSpace = Global.MinScanline + fillDensity * (Global.MaxScanline - Global.MinScanline);
										}
									}
									Paths.AddRange(parsePath(pathData, fillSpace));
								}
							}
							else if (reader.Name == "g")
							{
								groupDepth++;
							}
							break;
						case XmlNodeType.EndElement:
							if (reader.Name == "g")
							{
								groupDepth--;
							}
							break;

					}
				}
			}

			Paths.CheckBound();
			//Paths.ScalePath(Global.SVGScale);
			fillPath();
			//calcDistance();
			optimizePath();
			//calcDistance();

			//System.Windows.Forms.MessageBox.Show(Paths.Count.ToString());

		}

		private void calcDistance()
		{
			Point curPoint = new Point(0, 0);
			double d = 0;
			foreach (Path path in Paths)
			{
				foreach (Point point in path.Points)
				{
					d += point.Distance(curPoint);
					curPoint = point;
				}
			}
			System.Windows.Forms.MessageBox.Show(d.ToString());
		}

		private void optimizePath()
		{
			List<Path> newPaths = new List<Path>();
			Point curPoint = Global.SVGStartPoint.Copy();
			Path nearestPath = new Path();
			while (Paths.Count > 0)
			{
				double minDistance = double.MaxValue;
				double d;
				foreach (Path path in Paths)
				{
					if ((d = path.Distance(curPoint)) < minDistance)
					{
						minDistance = d;
						nearestPath = path;
					}
				}
				nearestPath.CheckReverse();
				curPoint = nearestPath.Points.Last();
				newPaths.Add(nearestPath);
				Paths.Remove(nearestPath);
			}
			Paths = newPaths;
		}

		private void fillPath()
		{
			for (int g = 0; g < Paths.Count; g++)
			{
				Path path = Paths[g];
				double fillSpace;
				if ((fillSpace = path.FillSpace) != -1)
				{
					Edge[] edges = path.ChildList.GenerateEdge();

					Edge tmp;
					for (int i = 0; i < edges.Length - 1; i++)
					{
						for (int j = 0; j < edges.Length - 1; j++)
						{
							if (edges[j].p1.Y > edges[j + 1].p1.Y)
							{
								tmp = edges[j];
								edges[j] = edges[j + 1];
								edges[j + 1] = tmp;
							}
						}
					}

					double scanlineEnd = 0;
					foreach (Edge edge in edges)
					{
						if (scanlineEnd < edge.p2.Y)
						{
							scanlineEnd = edge.p2.Y;
						}
					}

					for (double scanline = edges[0].p1.Y; scanline < scanlineEnd; scanline += fillSpace)
					{
						List<double> xList = new List<double>();
						double lastX = -1;
						foreach (Edge edge in edges)
						{
							double p = edge.Intersect(scanline);
							if (p != -1)
							{
								if (p == lastX)
								{
									if (xList.Count > 0)
									{
										xList.RemoveAt(xList.Count - 1);
									}
								}
								else
								{
									xList.Add(p);
									lastX = p;
								}
							}
						}
						if (xList.Count < 2 || xList.Count % 2 != 0)
						{
							continue;
						}

						List<Path> paths = new List<Path>();
						xList.Sort();
						for (int i = 0; i < xList.Count; i++)
						{
							Path pathLine = new Path();
							pathLine.Points.Add(new Point(xList[i], scanline));
							pathLine.Points.Add(new Point(xList[++i], scanline));
							paths.Add(pathLine);
						}
						Paths.AddRange(paths);
					}

				}
			}
		}

		private List<Path> parsePath(string pathData, double fillSpace)
		{
			List<Path> paths = new List<Path>();
			string[] data = pathData.Split(' ', ',', '\t');

			char modeTmp, mode = '\u0255'; // 0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15 = M,m,L,l,H,h,V,v,C,c,S,s,A,a,Z,z
			double tmpx, tmpy;
			double xc1, xc2, yc1, yc2, px, py, rx, ry, xrot;
			bool bigarc, sweep;
			bool isRel;
			Point cntrlpt = new Point(0.0, 0.0); // special point for s commands
			Point relpt = new Point(0.0, 0.0); // for relative commands
			Point startpt = new Point(0.0, 0.0); // for z commands
			List<Point> pathpoints = null;

			for (int i = 0; i < data.Length; i++)
			{
				if (mode == 'M') { mode = 'L'; }  // only one M/m command at a time
				else if (mode == 'm') { mode = 'l'; }
				switch (modeTmp = data[i][0])
				{
					case 'M':
					case 'm':
					case 'L':
					case 'l':
					case 'C':
					case 'c':
					case 'A':
					case 'a':
						mode = modeTmp;
						i++;
						break;
					case 'S':
					case 's':
						if (mode != 'C' && mode != 'c' && mode != 'S' && mode != 's')
						{
							cntrlpt = relpt;
						}
						mode = modeTmp;
						i++;
						break;
					case 'Z':
					case 'z':
						mode = modeTmp;
						break;
					case 'H':
					case 'h':
					case 'V':
					case 'v':
					case 'Q':
					case 'q':
					case 'T':
					case 't':
						throw new Exception(data[i][0] + " not supported");
				}

				switch (mode)
				{
					case 'M':
					case 'm':
						if (pathpoints != null && pathpoints.Count > 0)
						{
							paths.Add(new Path(pathpoints));
						}
						pathpoints = new List<Point>();

						isRel = mode == 'm';
						// this is followed by 2 numbers
						tmpx = (isRel ? relpt.X : 0) + double.Parse(data[i]);
						tmpy = (isRel ? relpt.Y : 0) + double.Parse(data[++i]);

						relpt.Change(tmpx, tmpy);
						startpt.Change(tmpx, tmpy);
						pathpoints.Add(new Point(tmpx, tmpy));
						break;

					case 'L':
					case 'l':
						isRel = mode == 'l';
						// this is followed by 2 numbers
						tmpx = (isRel ? relpt.X : 0) + double.Parse(data[i]);
						tmpy = (isRel ? relpt.Y : 0) + double.Parse(data[++i]);

						relpt.Change(tmpx, tmpy);
						pathpoints.Add(new Point(tmpx, tmpy));
						break;

					case 'C':
					case 'c':
						isRel = mode == 'c';
						// this is followed by 6 numbers
						tmpx = relpt.X;
						tmpy = relpt.Y;
						xc1 = (isRel ? tmpx : 0) + double.Parse(data[i]);
						yc1 = (isRel ? tmpy : 0) + double.Parse(data[++i]);
						xc2 = (isRel ? tmpx : 0) + double.Parse(data[++i]);
						yc2 = (isRel ? tmpy : 0) + double.Parse(data[++i]);
						px = (isRel ? tmpx : 0) + double.Parse(data[++i]);
						py = (isRel ? tmpy : 0) + double.Parse(data[++i]);

						pathpoints.AddRange(interpolateCurve(relpt.Copy(), new Point(xc1, yc1), new Point(xc2, yc2), new Point(px, py)));
						relpt.Change(px, py);
						cntrlpt.Change(tmpx + tmpx - xc2, tmpy + tmpy - yc2);
						break;

					case 'S':
					case 's':
						isRel = mode == 's';
						// this is followed by 4 numbers
						tmpx = relpt.X;
						tmpy = relpt.Y;
						xc2 = (isRel ? tmpx : 0) + double.Parse(data[i]);
						yc2 = (isRel ? tmpy : 0) + double.Parse(data[++i]);
						px = (isRel ? tmpx : 0) + double.Parse(data[++i]);
						py = (isRel ? tmpy : 0) + double.Parse(data[++i]);

						pathpoints.AddRange(interpolateCurve(relpt.Copy(), cntrlpt.Copy(), new Point(xc2, yc2), new Point(px, py)));
						relpt.Change(px, py);
						cntrlpt.Change(tmpx + tmpx - xc2, tmpy + tmpy - yc2);
						break;

					case 'A':
					case 'a':
						isRel = mode == 'a';
						// this is followed by 7 numbers
						tmpx = relpt.X;
						tmpy = relpt.Y;
						rx = double.Parse(data[i]);
						ry = double.Parse(data[++i]);
						xrot = double.Parse(data[++i]);
						bigarc = int.Parse(data[++i]) > 0;
						sweep = int.Parse(data[++i]) > 0;
						px = (isRel ? tmpx : 0) + double.Parse(data[++i]);
						py = (isRel ? tmpy : 0) + double.Parse(data[++i]);

						pathpoints.AddRange(interpolateArc(relpt.Copy(), rx, ry, xrot, bigarc, sweep, new Point(px, py)));
						relpt.Change(px, py);
						break;

					case 'Z':
					case 'z':
						tmpx = startpt.X;
						tmpy = startpt.Y;
						pathpoints.Add(new Point(tmpx, tmpy));
						relpt.Change(tmpx, tmpy);
						break;
				}
			}
			if (pathpoints != null && pathpoints.Count > 0)
			{
				paths.Add(new Path(pathpoints));
			}
			if (paths.Count > 0 && fillSpace != -1)
			{
				paths[0].FillSpace = fillSpace;
				paths[0].ChildList = paths;
			}
			return paths;
		}

		private List<Point> interpolateCurve(Point p1, Point pc1, Point pc2, Point p2)
		{
			List<Point> pts = new List<Point>();

			pts.Insert(0, p1);
			pts.Insert(1, p2);
			double maxDist = p1.Distance(p2);
			double interval = 1.0;
			double win, win2;
			double iin, iin2;
			int segments = 1;
			double tmpX, tmpY;

			while (maxDist > Global.InterpolatePrecision && segments < 1000)
			{
				interval = interval / 2.0;
				segments = segments * 2;

				for (int i = 1; i < segments; i += 2)
				{
					win = 1 - interval * i;
					iin = interval * i;
					win2 = win * win;
					iin2 = iin * iin;

					tmpX = win2 * win * p1.X +
						3 * win2 * iin * pc1.X +
						3 * win * iin2 * pc2.X +
						iin2 * iin * p2.X;
					tmpY = win2 * win * p1.Y +
						3 * win2 * iin * pc1.Y +
						3 * win * iin2 * pc2.Y +
						iin2 * iin * p2.Y;

					pts.Insert(i, new Point(tmpX, tmpY));
				}
				if (segments > 3)
				{
					maxDist = 0.0;
					for (int i = 0; i < pts.Count - 2; i++)
					{
						// this is the deviation from a straight line between 3 points
						tmpX = Global.Pow2(pts[i].X - pts[i + 1].X) +
							 Global.Pow2(pts[i].Y - pts[i + 1].Y) -
							(Global.Pow2(pts[i].X - pts[i + 2].X) +
							 Global.Pow2(pts[i].Y - pts[i + 2].Y)) / 4.0;
						if (tmpX > maxDist)
						{
							maxDist = tmpX;
						}
					}
					maxDist = Math.Sqrt(maxDist);
				}
			}

			return pts;
		}

		private List<Point> interpolateArc(Point p1, double rx, double ry, double xrot, bool bigarc, bool sweep, Point p2)
		{
			List<Point> pts = new List<Point>();

			pts.Insert(0, p1);
			pts.Insert(1, p2);
			// if the ellipse is too small to draw
			if (Math.Abs(rx) <= Global.InterpolatePrecision || Math.Abs(ry) <= Global.InterpolatePrecision)
			{
				return pts;
			}

			// Now we begin the task of converting the stupid SVG arc format 
			// into something actually useful (method derived from SVG specification)

			// convert xrot to radians
			xrot = xrot * Math.PI / 180.0;

			// radius check
			double x1 = Math.Cos(xrot) * (p1.X - p2.X) / 2.0 + Math.Sin(xrot) * (p1.Y - p2.Y) / 2.0;
			double y1 = -Math.Sin(xrot) * (p1.X - p2.X) / 2.0 + Math.Cos(xrot) * (p1.Y - p2.Y) / 2.0;

			rx = Math.Abs(rx);
			ry = Math.Abs(ry);
			double rchk = x1 * x1 / rx / rx + y1 * y1 / ry / ry;
			if (rchk > 1.0)
			{
				rx = Math.Sqrt(rchk) * rx;
				ry = Math.Sqrt(rchk) * ry;
			}

			// find the center
			double sq = (rx * rx * ry * ry - rx * rx * y1 * y1 - ry * ry * x1 * x1) / (rx * rx * y1 * y1 + ry * ry * x1 * x1);
			if (sq < 0)
			{
				sq = 0;
			}
			sq = Math.Sqrt(sq);
			double cx1 = 0.0;
			double cy1 = 0.0;
			if (bigarc == sweep)
			{
				cx1 = -sq * rx * y1 / ry;
				cy1 = sq * ry * x1 / rx;
			}
			else
			{
				cx1 = sq * rx * y1 / ry;
				cy1 = -sq * ry * x1 / rx;
			}
			double cx = (p1.X + p2.X) / 2.0 + Math.Cos(xrot) * cx1 - Math.Sin(xrot) * cy1;
			double cy = (p1.Y + p2.Y) / 2.0 + Math.Sin(xrot) * cx1 + Math.Cos(xrot) * cy1;

			// find angle start and angle extent
			double theta = 0.0;
			double dTheta = 0.0;
			double ux = (x1 - cx1) / rx;
			double uy = (y1 - cy1) / ry;
			double vx = (-x1 - cx1) / rx;
			double vy = (-y1 - cy1) / ry;
			double thing = Math.Sqrt(ux * ux + uy * uy);
			double thing2 = thing * Math.Sqrt(vx * vx + vy * vy);
			if (thing == 0)
			{
				thing = 1e-7;
			}
			if (thing2 == 0)
			{
				thing2 = 1e-7;
			}
			if (uy < 0)
			{
				theta = -Math.Acos(ux / thing);
			}
			else
			{
				theta = Math.Acos(ux / thing);
			}

			if (ux * vy - uy * vx < 0)
			{
				dTheta = -Math.Acos((ux * vx + uy * vy) / thing2);
			}
			else
			{
				dTheta = Math.Acos((ux * vx + uy * vy) / thing2);
			}
			dTheta = dTheta % (2 * Math.PI);
			if (sweep && dTheta < 0)
			{
				dTheta += 2 * Math.PI;
			}
			if (!sweep && dTheta > 0)
			{
				dTheta -= 2 * Math.PI;
			}

			// Now we have converted from stupid SVG arcs to something useful.

			double maxDist = 100;
			double interval = dTheta;
			int segments = 1;
			double tmpX, tmpY;

			while (maxDist > Global.InterpolatePrecision && segments < 1000)
			{
				interval = interval / 2.0;
				segments = segments * 2;

				for (int i = 1; i < segments; i += 2)
				{
					tmpX = cx + rx * Math.Cos(theta + interval * i) * Math.Cos(xrot) - ry * Math.Sin(theta + interval * i) * Math.Sin(xrot);
					tmpY = cy + rx * Math.Cos(theta + interval * i) * Math.Sin(xrot) + ry * Math.Sin(theta + interval * i) * Math.Cos(xrot);
					pts.Insert(i, new Point(tmpX, tmpY));
				}

				if (segments > 3)
				{
					maxDist = 0.0;
					for (int i = 0; i < pts.Count - 2; i++)
					{
						// this is the deviation from a straight line between 3 points
						tmpX = Global.Pow2(pts[i].X - pts[i + 1].X) +
							Global.Pow2(pts[i].Y - pts[i + 1].Y) -
							(Global.Pow2(pts[i].X - pts[i + 2].X) +
							Global.Pow2(pts[i].Y - pts[i + 2].Y)) / 4.0;
						if (tmpX > maxDist)
						{
							maxDist = tmpX;
						}
					}
					maxDist = Math.Sqrt(maxDist);
				}
			}

			return pts;
		}

	}
}