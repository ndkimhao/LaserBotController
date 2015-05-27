using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace LaserBotController
{
	class Path
	{
		public List<Point> Points { get; set; }
		public Point MinPoint { get; set; }
		public Point MaxPoint { get; set; }
		public double FillSpace { get; set; }
		public List<Path> ChildList { get; set; }

		private bool needReverse;

		public Path()
		{
			FillSpace = -1;
			Points = new List<Point>();
		}

		public Path(List<Point> points)
		{
			FillSpace = -1;
			Points = points;
		}

		public double Distance(Point curPoint)
		{
			double firstPointDistance = Points.First().Distance(curPoint);
			double lastPointDistance = Points.Last().Distance(curPoint);
			if (lastPointDistance < firstPointDistance)
			{
				needReverse = true;
				return lastPointDistance;
			}
			else
			{
				needReverse = false;
				return firstPointDistance;
			}
		}

		public void CheckReverse()
		{
			if (needReverse)
			{
				Points.Reverse();
			}
		}

		public void Add(Point p)
		{
			Points.Add(p);
		}

		public void Scale(double factor)
		{
			foreach (Point point in Points)
			{
				point.X *= factor;
				point.Y *= factor;
			}
		}

		public void MatrixTransform(MatrixTransformData matrix)
		{
			foreach (Point point in Points)
			{
				double X = point.X;
				double Y = point.Y;
				point.Change(matrix.A * X + matrix.C * Y + matrix.E, matrix.B * X + matrix.D * Y + matrix.F);
			}
		}
	}

	static class PathExtension
	{
		public static void ScalePath(this List<Path> paths, double factor)
		{
			foreach (Path path in paths)
			{
				path.Scale(factor);
			}
		}

		public static void MatrixTransform(this List<Path> paths, MatrixTransformData matrix)
		{
			foreach (Path path in paths)
			{
				path.MatrixTransform(matrix);
			}
		}

		public static void CheckBound(this List<Path> paths)
		{
			foreach (Path path in paths)
			{
				foreach (Point point in path.Points)
				{
					point.CheckBound(Global.SVGLimit);
				}
			}
		}
	}
}
