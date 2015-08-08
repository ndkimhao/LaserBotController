namespace LaserBotController
{
    partial class SVGForm
    {
        /// <summary>
        /// Required designer variable.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        /// Clean up any resources being used.
        /// </summary>
        /// <param name="disposing">true if managed resources should be disposed; otherwise, false.</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Windows Form Designer generated code

        /// <summary>
        /// Required method for Designer support - do not modify
        /// the contents of this method with the code editor.
        /// </summary>
        private void InitializeComponent()
        {
			this.btnOpen = new System.Windows.Forms.Button();
			this.previewImage = new ImageControl.ImageControl();
			this.btnProcess = new System.Windows.Forms.Button();
			this.btnSimulate = new System.Windows.Forms.Button();
			this.cbComPort = new System.Windows.Forms.ComboBox();
			this.btnConnect = new System.Windows.Forms.Button();
			this.btnGenerateGCode = new System.Windows.Forms.Button();
			this.btnHoming = new System.Windows.Forms.Button();
			this.lvOutput = new System.Windows.Forms.ListView();
			this.lvSerial = new System.Windows.Forms.ListView();
			this.btnSend = new System.Windows.Forms.Button();
			this.btnUnlock = new System.Windows.Forms.Button();
			this.txtFeedRate = new System.Windows.Forms.TextBox();
			this.label1 = new System.Windows.Forms.Label();
			this.btnSample = new System.Windows.Forms.Button();
			this.txtLaserPower = new System.Windows.Forms.TextBox();
			this.label2 = new System.Windows.Forms.Label();
			this.SuspendLayout();
			// 
			// btnOpen
			// 
			this.btnOpen.Location = new System.Drawing.Point(604, 12);
			this.btnOpen.Name = "btnOpen";
			this.btnOpen.Size = new System.Drawing.Size(75, 23);
			this.btnOpen.TabIndex = 2;
			this.btnOpen.Text = "Open File";
			this.btnOpen.UseVisualStyleBackColor = true;
			this.btnOpen.Click += new System.EventHandler(this.btnOpen_Click);
			// 
			// previewImage
			// 
			this.previewImage.Image = null;
			this.previewImage.initialimage = null;
			this.previewImage.Location = new System.Drawing.Point(12, 12);
			this.previewImage.Name = "previewImage";
			this.previewImage.Origin = new System.Drawing.Point(0, 0);
			this.previewImage.PanButton = System.Windows.Forms.MouseButtons.Left;
			this.previewImage.PanMode = true;
			this.previewImage.ScrollbarsVisible = true;
			this.previewImage.Size = new System.Drawing.Size(577, 548);
			this.previewImage.StretchImageToFit = false;
			this.previewImage.TabIndex = 1;
			this.previewImage.ZoomFactor = 1D;
			this.previewImage.ZoomOnMouseWheel = true;
			// 
			// btnProcess
			// 
			this.btnProcess.Enabled = false;
			this.btnProcess.Location = new System.Drawing.Point(604, 41);
			this.btnProcess.Name = "btnProcess";
			this.btnProcess.Size = new System.Drawing.Size(75, 23);
			this.btnProcess.TabIndex = 3;
			this.btnProcess.Tag = "NeedOpen";
			this.btnProcess.Text = "Process";
			this.btnProcess.UseVisualStyleBackColor = true;
			this.btnProcess.Click += new System.EventHandler(this.btnProcess_Click);
			// 
			// btnSimulate
			// 
			this.btnSimulate.Enabled = false;
			this.btnSimulate.Location = new System.Drawing.Point(604, 70);
			this.btnSimulate.Name = "btnSimulate";
			this.btnSimulate.Size = new System.Drawing.Size(75, 23);
			this.btnSimulate.TabIndex = 5;
			this.btnSimulate.Tag = "NeedOpen";
			this.btnSimulate.Text = "Simulate";
			this.btnSimulate.UseVisualStyleBackColor = true;
			this.btnSimulate.Click += new System.EventHandler(this.btnSimulate_Click);
			// 
			// cbComPort
			// 
			this.cbComPort.DropDownStyle = System.Windows.Forms.ComboBoxStyle.DropDownList;
			this.cbComPort.FormattingEnabled = true;
			this.cbComPort.Location = new System.Drawing.Point(604, 115);
			this.cbComPort.Name = "cbComPort";
			this.cbComPort.Size = new System.Drawing.Size(75, 21);
			this.cbComPort.TabIndex = 6;
			this.cbComPort.Tag = "NeedOpen";
			this.cbComPort.DropDown += new System.EventHandler(this.cbComPort_DropDown);
			// 
			// btnConnect
			// 
			this.btnConnect.Location = new System.Drawing.Point(604, 143);
			this.btnConnect.Name = "btnConnect";
			this.btnConnect.Size = new System.Drawing.Size(75, 23);
			this.btnConnect.TabIndex = 7;
			this.btnConnect.Tag = "NeedOpen";
			this.btnConnect.Text = "Connect";
			this.btnConnect.UseVisualStyleBackColor = true;
			this.btnConnect.Click += new System.EventHandler(this.btnConnect_Click);
			// 
			// btnGenerateGCode
			// 
			this.btnGenerateGCode.Enabled = false;
			this.btnGenerateGCode.Location = new System.Drawing.Point(601, 327);
			this.btnGenerateGCode.Name = "btnGenerateGCode";
			this.btnGenerateGCode.Size = new System.Drawing.Size(75, 42);
			this.btnGenerateGCode.TabIndex = 5;
			this.btnGenerateGCode.Tag = "NeedConnect";
			this.btnGenerateGCode.Text = "Generate GCode";
			this.btnGenerateGCode.UseVisualStyleBackColor = true;
			this.btnGenerateGCode.Click += new System.EventHandler(this.btnGenerateGCode_Click);
			// 
			// btnHoming
			// 
			this.btnHoming.Enabled = false;
			this.btnHoming.Location = new System.Drawing.Point(604, 389);
			this.btnHoming.Name = "btnHoming";
			this.btnHoming.Size = new System.Drawing.Size(75, 23);
			this.btnHoming.TabIndex = 9;
			this.btnHoming.Tag = "NeedConnect";
			this.btnHoming.Text = "Homing";
			this.btnHoming.UseVisualStyleBackColor = true;
			this.btnHoming.Click += new System.EventHandler(this.btnHoming_Click);
			// 
			// lvOutput
			// 
			this.lvOutput.Location = new System.Drawing.Point(710, 12);
			this.lvOutput.Name = "lvOutput";
			this.lvOutput.Size = new System.Drawing.Size(282, 551);
			this.lvOutput.TabIndex = 10;
			this.lvOutput.TileSize = new System.Drawing.Size(250, 15);
			this.lvOutput.UseCompatibleStateImageBehavior = false;
			this.lvOutput.View = System.Windows.Forms.View.Tile;
			// 
			// lvSerial
			// 
			this.lvSerial.Location = new System.Drawing.Point(998, 12);
			this.lvSerial.Name = "lvSerial";
			this.lvSerial.Size = new System.Drawing.Size(206, 551);
			this.lvSerial.TabIndex = 11;
			this.lvSerial.TileSize = new System.Drawing.Size(168, 15);
			this.lvSerial.UseCompatibleStateImageBehavior = false;
			this.lvSerial.View = System.Windows.Forms.View.Tile;
			// 
			// btnSend
			// 
			this.btnSend.Enabled = false;
			this.btnSend.Location = new System.Drawing.Point(604, 462);
			this.btnSend.Name = "btnSend";
			this.btnSend.Size = new System.Drawing.Size(75, 42);
			this.btnSend.TabIndex = 5;
			this.btnSend.Tag = "NeedConnect";
			this.btnSend.Text = "Send GCode";
			this.btnSend.UseVisualStyleBackColor = true;
			this.btnSend.Click += new System.EventHandler(this.btnSend_Click);
			// 
			// btnUnlock
			// 
			this.btnUnlock.Enabled = false;
			this.btnUnlock.Location = new System.Drawing.Point(604, 418);
			this.btnUnlock.Name = "btnUnlock";
			this.btnUnlock.Size = new System.Drawing.Size(75, 23);
			this.btnUnlock.TabIndex = 13;
			this.btnUnlock.Tag = "NeedConnect";
			this.btnUnlock.Text = "Unlock";
			this.btnUnlock.UseVisualStyleBackColor = true;
			this.btnUnlock.Click += new System.EventHandler(this.btnUnlock_Click);
			// 
			// txtFeedRate
			// 
			this.txtFeedRate.Enabled = false;
			this.txtFeedRate.Location = new System.Drawing.Point(601, 255);
			this.txtFeedRate.Name = "txtFeedRate";
			this.txtFeedRate.Size = new System.Drawing.Size(75, 20);
			this.txtFeedRate.TabIndex = 12;
			this.txtFeedRate.Tag = "NeedConnect";
			this.txtFeedRate.Text = "1500";
			// 
			// label1
			// 
			this.label1.AutoSize = true;
			this.label1.Location = new System.Drawing.Point(601, 236);
			this.label1.Name = "label1";
			this.label1.Size = new System.Drawing.Size(55, 13);
			this.label1.TabIndex = 14;
			this.label1.Text = "Feed rate:";
			// 
			// btnSample
			// 
			this.btnSample.Enabled = false;
			this.btnSample.Location = new System.Drawing.Point(604, 193);
			this.btnSample.Name = "btnSample";
			this.btnSample.Size = new System.Drawing.Size(75, 23);
			this.btnSample.TabIndex = 5;
			this.btnSample.Tag = "NeedConnect";
			this.btnSample.Text = "Sample";
			this.btnSample.UseVisualStyleBackColor = true;
			this.btnSample.Click += new System.EventHandler(this.btnSample_Click);
			// 
			// txtLaserPower
			// 
			this.txtLaserPower.Enabled = false;
			this.txtLaserPower.Location = new System.Drawing.Point(601, 301);
			this.txtLaserPower.Name = "txtLaserPower";
			this.txtLaserPower.Size = new System.Drawing.Size(75, 20);
			this.txtLaserPower.TabIndex = 12;
			this.txtLaserPower.Tag = "NeedConnect";
			this.txtLaserPower.Text = "1000";
			// 
			// label2
			// 
			this.label2.AutoSize = true;
			this.label2.Location = new System.Drawing.Point(601, 282);
			this.label2.Name = "label2";
			this.label2.Size = new System.Drawing.Size(68, 13);
			this.label2.TabIndex = 14;
			this.label2.Text = "Laser power:";
			// 
			// SVGForm
			// 
			this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
			this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
			this.ClientSize = new System.Drawing.Size(1216, 575);
			this.Controls.Add(this.label2);
			this.Controls.Add(this.label1);
			this.Controls.Add(this.btnUnlock);
			this.Controls.Add(this.txtLaserPower);
			this.Controls.Add(this.txtFeedRate);
			this.Controls.Add(this.lvSerial);
			this.Controls.Add(this.lvOutput);
			this.Controls.Add(this.btnHoming);
			this.Controls.Add(this.btnConnect);
			this.Controls.Add(this.cbComPort);
			this.Controls.Add(this.btnSend);
			this.Controls.Add(this.btnSample);
			this.Controls.Add(this.btnGenerateGCode);
			this.Controls.Add(this.btnSimulate);
			this.Controls.Add(this.btnProcess);
			this.Controls.Add(this.btnOpen);
			this.Controls.Add(this.previewImage);
			this.Name = "SVGForm";
			this.StartPosition = System.Windows.Forms.FormStartPosition.CenterScreen;
			this.Text = "MainForm";
			this.Load += new System.EventHandler(this.SVGForm_Load);
			this.ResumeLayout(false);
			this.PerformLayout();

        }

        #endregion

		private ImageControl.ImageControl previewImage;
		private System.Windows.Forms.Button btnOpen;
		private System.Windows.Forms.Button btnProcess;
		private System.Windows.Forms.Button btnSimulate;
		private System.Windows.Forms.ComboBox cbComPort;
		private System.Windows.Forms.Button btnConnect;
		private System.Windows.Forms.Button btnGenerateGCode;
		private System.Windows.Forms.Button btnHoming;
		private System.Windows.Forms.ListView lvOutput;
		private System.Windows.Forms.ListView lvSerial;
		private System.Windows.Forms.Button btnSend;
		private System.Windows.Forms.Button btnUnlock;
		private System.Windows.Forms.TextBox txtFeedRate;
		private System.Windows.Forms.Label label1;
		private System.Windows.Forms.Button btnSample;
		private System.Windows.Forms.TextBox txtLaserPower;
		private System.Windows.Forms.Label label2;
    }
}