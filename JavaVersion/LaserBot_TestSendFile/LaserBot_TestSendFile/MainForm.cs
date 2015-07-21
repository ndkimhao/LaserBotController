using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.IO;
using System.IO.Ports;
using System.Linq;
using System.Text;
using System.Threading;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace LaserBot_TestSendFile
{
    public partial class MainForm : Form
    {
        private SerialPort comPort;

        public MainForm()
        {
            InitializeComponent();
            Control.CheckForIllegalCrossThreadCalls = false;
        }

        private Thread t;

        private void btnSend_Click(object sender, EventArgs e)
        {
            btnSend.Enabled = false;
            t = new Thread(new ThreadStart(proccess));
            t.Start();
        }

        private void proccess()
        {
            comPort = new SerialPort("COM5", 115200);
            comPort.Open();
            using (BinaryReader reader = new BinaryReader(File.Open("picture.jpg", FileMode.Open)))
            {
                int length = (int)reader.BaseStream.Length;
                lblLength.Text = length.ToString();

                comPort.WriteLine("`BEGIN_TRANSFER_IMAGE");
                comPort.WriteLine("`" + length.ToString());
                comPort.WriteLine("`START_TRANSFER_IMAGE");
                Thread.Sleep(500);

                int pos;
                while ((pos = (int)reader.BaseStream.Position) < length)
                {
                    byte[] data = reader.ReadBytes(4096);
                    int l = data.Length;
                    comPort.Write(data, 0, l);
                    int percent = (int)(pos * 10000L / length);
                    lblPercent.Text = (percent * 1.0 / 100).ToString();
                    lblSent.Text = (pos + l).ToString();
                    pbStatus.Value = percent;
                }
                pbStatus.Value = 10000;
                //comPort.Write(reader.ReadBytes(length), 0, length);
            }
            comPort.Close();
            lblPercent.Text = "100";
            //MessageBox.Show("Finish !");
            btnSend.Enabled = true;
        }

        private void MainForm_Load(object sender, EventArgs e)
        {
            pbImage.ImageLocation = "picture.jpg";
        }

        private void MainForm_FormClosing(object sender, FormClosingEventArgs e)
        {
            try
            {
                t.Abort();
                comPort.Close();

            }
            catch (Exception)
            {
            }
        }
    }
}
