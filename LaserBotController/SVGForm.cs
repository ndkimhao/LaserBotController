using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.IO.Ports;
using System.Linq;
using System.Text;
using System.Threading;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace LaserBotController
{
	public partial class SVGForm : Form
	{
		private string svgPath;

		public SVGForm()
		{
			InitializeComponent();
			Control.CheckForIllegalCrossThreadCalls = false;
		}

		private void button1_Click(object sender, EventArgs e)
		{
			MessageBox.Show(Global.Path);
		}

		private void btnOpen_Click(object sender, EventArgs e)
		{
			OpenFileDialog dialog = new OpenFileDialog();
			dialog.CheckFileExists = true;
			dialog.CheckPathExists = true;
			dialog.DereferenceLinks = true;
			dialog.RestoreDirectory = true;
			dialog.Multiselect = false;
			dialog.Filter = "SVG Files (*.svg)|*.svg";
			if (dialog.ShowDialog(this) == DialogResult.OK)
			{
				svgPath = dialog.FileName;
			}
		}

		private void btnQuickLoad_Click(object sender, EventArgs e)
		{
			svgPath = @"D:\Projects\Arduino\LaserBot\SVG\drawing.svg";
			loadFile();
		}

		private void loadFile()
		{
		}

		SVGParser parser;
		SVGRender render;
		GCode gcode;
		GRBL grbl;
		private void btnProcess_Click(object sender, EventArgs e)
		{
			parser = new SVGParser(svgPath);
			parser.DoParse();
			render = new SVGRender(parser.Paths, previewImage);
			render.Render();
			render.Refresh();
		}

		private void SVGForm_Load(object sender, EventArgs e)
		{
			svgPath = @"D:\Projects\Arduino\LaserBot\SVG\drawing.svg";
			loadFile();
			btnProcess_Click(null, null);
		}

		private void btnSimulate_Click(object sender, EventArgs e)
		{
			render.StartSimulate();
		}

		private void btnGenerateGCode_Click(object sender, EventArgs e)
		{
			gcode = new GCode(parser.Paths, grbl, double.Parse(txtFeedRate.Text), double.Parse(txtLaserPower.Text), lvOutput, previewImage);
			gcode.Generate();
		}

		private void cbComPort_DropDown(object sender, EventArgs e)
		{
			int oldIndex = cbComPort.SelectedIndex;
			cbComPort.Items.Clear();
			cbComPort.Items.AddRange(SerialPort.GetPortNames());
			if (oldIndex != -1 && oldIndex < cbComPort.Items.Count)
			{
				cbComPort.SelectedIndex = oldIndex;
			}
			else if (cbComPort.Items.Count > 0)
			{
				cbComPort.SelectedIndex = 0;
			}
		}

		private void btnConnect_Click(object sender, EventArgs e)
		{
			if (cbComPort.SelectedIndex == -1)
			{
				MessageBox.Show("Please select com port !");
			}
			else
			{
				grbl = new GRBL(cbComPort.SelectedItem.ToString(), lvSerial);
				string result = grbl.Connect();
				if (result != null)
				{
					MessageBox.Show("Error: " + result);
				}
				else
				{
					btnConnect.Enabled = false;
				}
			}
		}

		private void btnHoming_Click(object sender, EventArgs e)
		{
			grbl.Homing();
		}

		private void btnSend_Click(object sender, EventArgs e)
		{
			gcode.Send();
		}

		private void btnUnlock_Click(object sender, EventArgs e)
		{
			grbl.Serial.WriteLine("$X");
		}

	}
}
