using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace LaserBotController
{
	class Arc
	{
		public Point Start { get; set; }
		public Point End { get; set; }
		public Point Center { get; set; }
		public bool CCW { get; set; }
		public double Radius
		{
			get
			{
				return Start.Distance(Center);
			}
		}
	}

}
