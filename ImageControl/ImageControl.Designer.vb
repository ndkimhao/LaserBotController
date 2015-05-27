<Global.Microsoft.VisualBasic.CompilerServices.DesignerGenerated()> _
Partial Class ImageControl
    Inherits System.Windows.Forms.UserControl

    'UserControl overrides dispose to clean up the component list.
    <System.Diagnostics.DebuggerNonUserCode()> _
    Protected Overrides Sub Dispose(ByVal disposing As Boolean)
        If disposing AndAlso components IsNot Nothing Then
            components.Dispose()
        End If
        MyBase.Dispose(disposing)
    End Sub

    'Required by the Windows Form Designer
    Private components As System.ComponentModel.IContainer

    'NOTE: The following procedure is required by the Windows Form Designer
    'It can be modified using the Windows Form Designer.  
    'Do not modify it using the code editor.
    <System.Diagnostics.DebuggerStepThrough()> _
    Private Sub InitializeComponent()
        Me.VScrollBar1 = New System.Windows.Forms.VScrollBar
        Me.HScrollBar1 = New System.Windows.Forms.HScrollBar
        Me.DrawingBoard1 = New Global.ImageControl.DrawingBoard
        Me.SuspendLayout()
        '
        'VScrollBar1
        '
        Me.VScrollBar1.Anchor = CType(((System.Windows.Forms.AnchorStyles.Top Or System.Windows.Forms.AnchorStyles.Bottom) _
                    Or System.Windows.Forms.AnchorStyles.Right), System.Windows.Forms.AnchorStyles)
        Me.VScrollBar1.Enabled = False
        Me.VScrollBar1.LargeChange = 20
        Me.VScrollBar1.Location = New System.Drawing.Point(199, 0)
        Me.VScrollBar1.Name = "VScrollBar1"
        Me.VScrollBar1.Size = New System.Drawing.Size(17, 137)
        Me.VScrollBar1.TabIndex = 1
        '
        'HScrollBar1
        '
        Me.HScrollBar1.Anchor = CType(((System.Windows.Forms.AnchorStyles.Bottom Or System.Windows.Forms.AnchorStyles.Left) _
                    Or System.Windows.Forms.AnchorStyles.Right), System.Windows.Forms.AnchorStyles)
        Me.HScrollBar1.Enabled = False
        Me.HScrollBar1.LargeChange = 20
        Me.HScrollBar1.Location = New System.Drawing.Point(0, 137)
        Me.HScrollBar1.Name = "HScrollBar1"
        Me.HScrollBar1.Size = New System.Drawing.Size(200, 17)
        Me.HScrollBar1.TabIndex = 2
        '
        'DrawingBoard1
        '
        Me.DrawingBoard1.Anchor = CType((((System.Windows.Forms.AnchorStyles.Top Or System.Windows.Forms.AnchorStyles.Bottom) _
                    Or System.Windows.Forms.AnchorStyles.Left) _
                    Or System.Windows.Forms.AnchorStyles.Right), System.Windows.Forms.AnchorStyles)
        Me.DrawingBoard1.BorderStyle = System.Windows.Forms.BorderStyle.Fixed3D
        Me.DrawingBoard1.Image = Nothing
        Me.DrawingBoard1.initialimage = Nothing
        Me.DrawingBoard1.Location = New System.Drawing.Point(0, -1)
        Me.DrawingBoard1.Name = "DrawingBoard1"
        Me.DrawingBoard1.Origin = New System.Drawing.Point(0, 0)
        Me.DrawingBoard1.PanButton = System.Windows.Forms.MouseButtons.Left
        Me.DrawingBoard1.Size = New System.Drawing.Size(200, 138)
        Me.DrawingBoard1.TabIndex = 0
        Me.DrawingBoard1.ZoomFactor = 1
        Me.DrawingBoard1.ZoomOnMouseWheel = True
        '
        'ImageControl
        '
        Me.AutoScaleDimensions = New System.Drawing.SizeF(6.0!, 13.0!)
        Me.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font
        Me.Controls.Add(Me.HScrollBar1)
        Me.Controls.Add(Me.VScrollBar1)
        Me.Controls.Add(Me.DrawingBoard1)
        Me.Name = "ImageControl"
        Me.Size = New System.Drawing.Size(217, 155)
        Me.ResumeLayout(False)

    End Sub
    Friend WithEvents DrawingBoard1 As Global.ImageControl.DrawingBoard
    Friend WithEvents VScrollBar1 As System.Windows.Forms.VScrollBar
    Friend WithEvents HScrollBar1 As System.Windows.Forms.HScrollBar

End Class
