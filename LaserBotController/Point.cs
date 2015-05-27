using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace LaserBotController
{
	class Point
	{
		public double X { get; set; }
		public double Y { get; set; }

		public int X_i { get { return (int)Math.Round(X); } }
		public int Y_i { get { return (int)Math.Round(Y); } }

		public Point(double X, double Y)
		{
			this.X = X;
			this.Y = Y;
		}

		public void Change(double X, double Y)
		{
			this.X = X;
			this.Y = Y;
		}

		public void CheckBound(Point limitPoint)
		{
			if (X < Global.ZeroPoint.X || Y < Global.ZeroPoint.Y || X > limitPoint.X || Y > limitPoint.Y)
			{
				throw new Exception(String.Format("Point ({0}, {1}) exceed limit !", X, Y));
			}
		}

		public Point Copy()
		{
			return new Point(X, Y);
		}

		public bool Equals(Point p)
		{
			return p != null && X == p.X && Y == p.Y;
		}

		public override string ToString()
		{
			return "{" + X + ", " + Y + "}";
		}

		public double Distance(Point p)
		{
			return Math.Sqrt(Global.Pow2(X - p.X) + Global.Pow2(Y - p.Y));
		}
	}

}
