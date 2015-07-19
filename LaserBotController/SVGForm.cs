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
				btnProcess_Click(this, null);
				foreach (Control cntrl in this.Controls)
				{
					if ("NeedOpen".Equals(cntrl.Tag))
					{
						cntrl.Enabled = true;
					}
				}
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
		Sample sample;
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
			//svgPath = @"D:\Projects\Arduino\LaserBot\SVG\drawing.svg";
			//loadFile();
			//btnProcess_Click(null, null);
		}

		private void btnSimulate_Click(object sender, EventArgs e)
		{
			render.StartSimulate();
		}

		private void btnGenerateGCode_Click(object sender, EventArgs e)
		{
			gcode = new GCode(parser.Paths, grbl, double.Parse(txtFeedRate.Text), 1000, lvOutput, previewImage);
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
				if (btnConnect.Text == "Connect")
				{
					grbl = new GRBL(cbComPort.SelectedItem.ToString(), lvSerial);
					string result = grbl.Connect();
					if (result != null)
					{
						MessageBox.Show("Error: " + result);
					}
					else
					{
						btnConnect.Text = "Disconnect";

						foreach (Control cntrl in this.Controls)
						{
							if ("NeedConnect".Equals(cntrl.Tag))
							{
								cntrl.Enabled = true;
							}
						}
					}
				}
				else
				{
					if (grbl != null)
					{
						grbl.Disconnect();
						btnConnect.Text = "Connect";

						foreach (Control cntrl in this.Controls)
						{
							if ("NeedConnect".Equals(cntrl.Tag))
							{
								cntrl.Enabled = false;
							}
						}
					}
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

		private void btnSample_Click(object sender, EventArgs e)
		{
			if (btnSample.Text == "Sample")
			{
				btnSample.Text = "Save SVG";
				sample = new Sample(grbl, previewImage);
				Application.AddMessageFilter(sample);
			}
			else
			{
				btnSample.Text = "Sample";
				Application.RemoveMessageFilter(sample);

				SaveFileDialog dialog = new SaveFileDialog();
				dialog.CheckPathExists = true;
				dialog.DereferenceLinks = true;
				dialog.RestoreDirectory = true;
				dialog.Filter = "SVG Files (*.svg)|*.svg";
				if (dialog.ShowDialog(this) == DialogResult.OK)
				{
					sample.SaveSVG(dialog.FileName);
				}

				sample = null;
			}
		}

	}
}
