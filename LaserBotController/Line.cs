using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace LaserBotController
{
	class Line
	{

		public Point p1;
		public Point p2;

		public Line(Point a, Point b)
		{
			p1 = a;
			p2 = b;
		}

		public double Intersect(double Y)
		{
			if (Y < p1.Y || Y > p2.Y) return -1;

			double A1 = p2.Y - p1.Y;
			double B1 = p1.X - p2.X;
			double C1 = A1 * p1.X + B1 * p1.Y;

			if (A1 == 0)
				return -1;
			else
				return (C1 - B1 * Y) / A1;
		}

		public Point Intersect(Line line)
		{
			double A1 = this.p2.Y - this.p1.Y;
			double B1 = this.p1.X - this.p2.X;
			double C1 = A1 * this.p1.X + B1 * this.p1.Y;

			double A2 = line.p2.Y - line.p1.Y;
			double B2 = line.p1.X - line.p2.X;
			double C2 = A1 * line.p1.X + B1 * line.p1.Y;

			double delta = A1 * B2 - A2 * B1;
			if (delta == 0)
				return null;
			else
				return new Point((B2 * C1 - B1 * C2) / delta, (A1 * C2 - A2 * C1) / delta);
		}

	}

	static class LineExtension
	{
		public static Line[] GenerateEdge(this List<Path> paths)
		{
			List<Line> edges = new List<Line>();
			Point p1, p2;
			foreach (Path path in paths)
			{
				List<Point> points = path.Points;
				for (int i = 0; i < points.Count - 1; i++)
				{
					if ((p1 = points[i]).Y < (p2 = points[i + 1]).Y)
					{
						edges.Add(new Line(p1, p2));
					}
					else
					{
						edges.Add(new Line(p2, p1));
					}
				}
			}
			return edges.ToArray();
		}
	}
}
