using System;
using System.Collections.Generic;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace LaserBotController
{
	static class Global
	{
		public static string Path = Application.StartupPath + "\\";

		public const double InterpolatePrecision = 0.5;
		public const double InterpolatePrecisionPow2 = InterpolatePrecision * InterpolatePrecision;
		public const int InterpolateMaxSegments = 1000;

		public static Color SVGRenderColor = Color.Black;
		public static Color SVGSimulateColor = Color.Red;
		public static Color SVGSentColor = Color.Green;

		public const double UnitFactor = 90 / 25.4;

		public static readonly Point ZeroPoint = new Point(0, 0);
		//public static readonly Point MachineLimit = new Point(160, 200);
		public static readonly Point MachineLimit = new Point(100, 50);
		public static readonly Point SVGLimit = new Point(MachineLimit.X * UnitFactor, MachineLimit.Y * UnitFactor);
		public const double SampleStep = 1;

		public const double RenderScale = 2;
		public static readonly Point RenderLimit = new Point(SVGLimit.X * RenderScale + 1, SVGLimit.Y * RenderScale + 1);

		public const double MinScanline = 1;
		public const double MaxScanline = 4;
		public const double ScanlineStep = 0.25;

		public static readonly Color GCode_Comment_Color = Color.Green;
		public static readonly Color GCode_G0_Color = Color.Red;
		public static readonly Color GCode_M_G1_Color = Color.IndianRed;
		public static readonly Color GCode_G2_G3_Color = Color.RoyalBlue;
		public static readonly Color GCode_SentBackColor = Color.Yellow;

		public static readonly Color GRBL_SendColor = Color.Blue;
		public static readonly Color GRBL_ReceiveColor = Color.Green;
		public static readonly Color GRBL_ErrorColor = Color.Red;
		public static readonly Color GRBL_InfoColor = Color.LimeGreen;

		public const int GRBL_ReadTimeOut = 5000;
		public const int GRBL_HomingTimeOut = 15000;
		public const int GRBL_BufferLimit = 127;

		public const int Arc_MinSegments = 3;
		public static readonly double Arc_MinTotalAngle = (5.0).ToRadian();
		public static readonly double Arc_MaxRelativeAngle = (15.0).ToRadian();
		public static readonly double Arc_LenEpsilon = 2;
		public static readonly double Arc_AngleEpsilon = (15.0).ToRadian();

		public static double Pow2(double n)
		{
			return n * n;
		}
	}

	static class StaticExtensions
	{
		public static double ToRadian(this double val)
		{
			return (Math.PI / 180) * val;
		}
		public static double ToDegrees(this double val)
		{
			return (180 / Math.PI) * val;
		}
	}
}
