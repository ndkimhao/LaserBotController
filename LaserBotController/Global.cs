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

		public const double InterpolatePrecision = 2;
		public const double InterpolatePrecisionPow2 = InterpolatePrecision * InterpolatePrecision;
		public const int InterpolateMaxSegments = 1000;

		public static Color SVGRenderColor = Color.Black;
		public static Color SVGSimulateColor = Color.Red;
		public static Color SVGSentColor = Color.Green;

		public const double UnitFactor = 90 / 25.4;

		public static readonly Point ZeroPoint = new Point(0, 0);
		public static readonly Point MachineLimit = new Point(35, 35);
		public static readonly Point MachineStartPoint = new Point(35, 35);
		public static readonly Point SVGStartPoint = new Point(MachineStartPoint.X * UnitFactor, MachineStartPoint.Y * UnitFactor);
		public static readonly Point SVGLimit = new Point(MachineLimit.X * UnitFactor, MachineLimit.Y * UnitFactor);
		public const double SampleStep = 1;

		public const int RenderScale = 4;
		public static readonly Point RenderLimit = new Point(SVGLimit.X * RenderScale + 1, SVGLimit.Y * RenderScale + 1);

		public const double MinScanline = 0.5;
		public const double MaxScanline = 4;
		public const double ScanlineStep = 0.25;

		public static readonly Color GCode_CommentColor = Color.Green;
		public static readonly Color GCode_G00Color = Color.Red;
		public static readonly Color GCode_G01Color = Color.IndianRed;
		public static readonly Color GCode_SentBackColor = Color.Yellow;

		public static readonly Color GRBL_SendColor = Color.Blue;
		public static readonly Color GRBL_ReceiveColor = Color.Green;
		public static readonly Color GRBL_ErrorColor = Color.Red;
		public static readonly Color GRBL_InfoColor = Color.LimeGreen;

		public const int GRBL_ReadTimeOut = 5000;
		public const int GRBL_HomingTimeOut = 15000;
		public const int GRBL_BufferLimit = 127;

		public static double Pow2(double n)
		{
			return n * n;
		}
	}
}
