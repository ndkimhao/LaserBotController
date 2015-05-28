using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace LaserBotController
{
	class Edge
	{

		public Point p1;
		public Point p2;

		public Edge(Point a, Point b)
		{
			p1 = a;
			p2 = b;
		}

		public double Intersect(double lineY)
		{
			if (lineY < p1.Y || lineY > p2.Y) return -1;

			double A1 = p2.Y - p1.Y;
			double B1 = p1.X - p2.X;
			double C1 = A1 * p1.X + B1 * p1.Y;

			if (A1 == 0)
				return -1;
			else
				return (C1 - B1 * lineY) / A1;
		}
	}

	static class EdgeExtension
	{
		public static Edge[] GenerateEdge(this List<Path> paths)
		{
			List<Edge> edges = new List<Edge>();
			Point p1, p2;
			foreach (Path path in paths)
			{
				List<Point> points = path.Points;
				for (int i = 0; i < points.Count - 1; i++)
				{
					if ((p1 = points[i]).Y < (p2 = points[i + 1]).Y)
					{
						edges.Add(new Edge(p1, p2));
					}
					else
					{
						edges.Add(new Edge(p2, p1));
					}
				}
			}
			return edges.ToArray();
		}
	}
}
