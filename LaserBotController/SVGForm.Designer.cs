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
			this.button1 = new System.Windows.Forms.Button();
			this.btnOpen = new System.Windows.Forms.Button();
			this.btnQuickLoad = new System.Windows.Forms.Button();
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
			this.txtFeedRate = new System.Windows.Forms.TextBox();
			this.SuspendLayout();
			// 
			// button1
			// 
			this.button1.Location = new System.Drawing.Point(604, 12);
			this.button1.Name = "button1";
			this.button1.Size = new System.Drawing.Size(75, 23);
			this.button1.TabIndex = 0;
			this.button1.Text = "Test";
			this.button1.UseVisualStyleBackColor = true;
			this.button1.Click += new System.EventHandler(this.button1_Click);
			// 
			// btnOpen
			// 
			this.btnOpen.Location = new System.Drawing.Point(604, 57);
			this.btnOpen.Name = "btnOpen";
			this.btnOpen.Size = new System.Drawing.Size(75, 23);
			this.btnOpen.TabIndex = 2;
			this.btnOpen.Text = "Open File";
			this.btnOpen.UseVisualStyleBackColor = true;
			this.btnOpen.Click += new System.EventHandler(this.btnOpen_Click);
			// 
			// btnQuickLoad
			// 
			this.btnQuickLoad.Location = new System.Drawing.Point(604, 86);
			this.btnQuickLoad.Name = "btnQuickLoad";
			this.btnQuickLoad.Size = new System.Drawing.Size(75, 23);
			this.btnQuickLoad.TabIndex = 2;
			this.btnQuickLoad.Text = "Quick Load";
			this.btnQuickLoad.UseVisualStyleBackColor = true;
			this.btnQuickLoad.Click += new System.EventHandler(this.btnQuickLoad_Click);
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
			this.btnProcess.Location = new System.Drawing.Point(604, 115);
			this.btnProcess.Name = "btnProcess";
			this.btnProcess.Size = new System.Drawing.Size(75, 23);
			this.btnProcess.TabIndex = 3;
			this.btnProcess.Text = "Process";
			this.btnProcess.UseVisualStyleBackColor = true;
			this.btnProcess.Click += new System.EventHandler(this.btnProcess_Click);
			// 
			// btnSimulate
			// 
			this.btnSimulate.Location = new System.Drawing.Point(604, 144);
			this.btnSimulate.Name = "btnSimulate";
			this.btnSimulate.Size = new System.Drawing.Size(75, 23);
			this.btnSimulate.TabIndex = 5;
			this.btnSimulate.Text = "Simulate";
			this.btnSimulate.UseVisualStyleBackColor = true;
			this.btnSimulate.Click += new System.EventHandler(this.btnSimulate_Click);
			// 
			// cbComPort
			// 
			this.cbComPort.DropDownStyle = System.Windows.Forms.ComboBoxStyle.DropDownList;
			this.cbComPort.FormattingEnabled = true;
			this.cbComPort.Location = new System.Drawing.Point(604, 183);
			this.cbComPort.Name = "cbComPort";
			this.cbComPort.Size = new System.Drawing.Size(75, 21);
			this.cbComPort.TabIndex = 6;
			this.cbComPort.DropDown += new System.EventHandler(this.cbComPort_DropDown);
			// 
			// btnConnect
			// 
			this.btnConnect.Location = new System.Drawing.Point(604, 211);
			this.btnConnect.Name = "btnConnect";
			this.btnConnect.Size = new System.Drawing.Size(75, 23);
			this.btnConnect.TabIndex = 7;
			this.btnConnect.Text = "Connect";
			this.btnConnect.UseVisualStyleBackColor = true;
			this.btnConnect.Click += new System.EventHandler(this.btnConnect_Click);
			// 
			// btnGenerateGCode
			// 
			this.btnGenerateGCode.Location = new System.Drawing.Point(604, 282);
			this.btnGenerateGCode.Name = "btnGenerateGCode";
			this.btnGenerateGCode.Size = new System.Drawing.Size(75, 42);
			this.btnGenerateGCode.TabIndex = 5;
			this.btnGenerateGCode.Text = "Generate GCode";
			this.btnGenerateGCode.UseVisualStyleBackColor = true;
			this.btnGenerateGCode.Click += new System.EventHandler(this.btnGenerateGCode_Click);
			// 
			// btnHoming
			// 
			this.btnHoming.Location = new System.Drawing.Point(604, 349);
			this.btnHoming.Name = "btnHoming";
			this.btnHoming.Size = new System.Drawing.Size(75, 23);
			this.btnHoming.TabIndex = 9;
			this.btnHoming.Text = "Homing";
			this.btnHoming.UseVisualStyleBackColor = true;
			this.btnHoming.Click += new System.EventHandler(this.btnHoming_Click);
			// 
			// lvOutput
			// 
			this.lvOutput.Location = new System.Drawing.Point(710, 12);
			this.lvOutput.Name = "lvOutput";
			this.lvOutput.Size = new System.Drawing.Size(282, 569);
			this.lvOutput.TabIndex = 10;
			this.lvOutput.TileSize = new System.Drawing.Size(168, 15);
			this.lvOutput.UseCompatibleStateImageBehavior = false;
			this.lvOutput.View = System.Windows.Forms.View.Tile;
			// 
			// lvSerial
			// 
			this.lvSerial.Location = new System.Drawing.Point(998, 12);
			this.lvSerial.Name = "lvSerial";
			this.lvSerial.Size = new System.Drawing.Size(206, 569);
			this.lvSerial.TabIndex = 11;
			this.lvSerial.TileSize = new System.Drawing.Size(168, 15);
			this.lvSerial.UseCompatibleStateImageBehavior = false;
			this.lvSerial.View = System.Windows.Forms.View.Tile;
			// 
			// btnSend
			// 
			this.btnSend.Location = new System.Drawing.Point(604, 378);
			this.btnSend.Name = "btnSend";
			this.btnSend.Size = new System.Drawing.Size(75, 42);
			this.btnSend.TabIndex = 5;
			this.btnSend.Text = "Send GCode";
			this.btnSend.UseVisualStyleBackColor = true;
			this.btnSend.Click += new System.EventHandler(this.btnSend_Click);
			// 
			// txtFeedRate
			// 
			this.txtFeedRate.Location = new System.Drawing.Point(604, 256);
			this.txtFeedRate.Name = "txtFeedRate";
			this.txtFeedRate.Size = new System.Drawing.Size(75, 20);
			this.txtFeedRate.TabIndex = 12;
			this.txtFeedRate.Text = "500";
			// 
			// SVGForm
			// 
			this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
			this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
			this.ClientSize = new System.Drawing.Size(1216, 593);
			this.Controls.Add(this.txtFeedRate);
			this.Controls.Add(this.lvSerial);
			this.Controls.Add(this.lvOutput);
			this.Controls.Add(this.btnHoming);
			this.Controls.Add(this.btnConnect);
			this.Controls.Add(this.cbComPort);
			this.Controls.Add(this.btnSend);
			this.Controls.Add(this.btnGenerateGCode);
			this.Controls.Add(this.btnSimulate);
			this.Controls.Add(this.btnProcess);
			this.Controls.Add(this.btnQuickLoad);
			this.Controls.Add(this.btnOpen);
			this.Controls.Add(this.previewImage);
			this.Controls.Add(this.button1);
			this.Name = "SVGForm";
			this.StartPosition = System.Windows.Forms.FormStartPosition.CenterScreen;
			this.Text = "MainForm";
			this.Load += new System.EventHandler(this.SVGForm_Load);
			this.ResumeLayout(false);
			this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.Button button1;
        private ImageControl.ImageControl previewImage;
		private System.Windows.Forms.Button btnOpen;
		private System.Windows.Forms.Button btnQuickLoad;
		private System.Windows.Forms.Button btnProcess;
		private System.Windows.Forms.Button btnSimulate;
		private System.Windows.Forms.ComboBox cbComPort;
		private System.Windows.Forms.Button btnConnect;
		private System.Windows.Forms.Button btnGenerateGCode;
		private System.Windows.Forms.Button btnHoming;
		private System.Windows.Forms.ListView lvOutput;
		private System.Windows.Forms.ListView lvSerial;
		private System.Windows.Forms.Button btnSend;
		private System.Windows.Forms.TextBox txtFeedRate;
    }
}