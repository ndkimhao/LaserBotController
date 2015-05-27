<System.Runtime.InteropServices.ComVisible(False)> _
Public Class ImageControl

    Private m_ScrollVisible As Boolean = True

    Public Shadows Event MouseDown(ByVal sender As Object, ByVal e As System.Windows.Forms.MouseEventArgs)
    Public Shadows Event MouseUp(ByVal sender As Object, ByVal e As System.Windows.Forms.MouseEventArgs)
    Public Shadows Event PreMouseWheel(ByVal sender As Object, ByVal e As System.Windows.Forms.MouseEventArgs)
    Public Shadows Event PostMouseWheel(ByVal sender As Object, ByVal e As System.Windows.Forms.MouseEventArgs)

    Public Sub New()

        ' This call is required by the Windows Form Designer.
        InitializeComponent()

        ' Add any initialization after the InitializeComponent() call.
    End Sub

#Region "Public Properties"

    Public Property PanMode() As Boolean
        Get
            Return DrawingBoard1.PanMode
        End Get
        Set(ByVal value As Boolean)
            DrawingBoard1.PanMode = value
        End Set
    End Property

    Public Property PanButton() As System.Windows.Forms.MouseButtons
        Get
            Return DrawingBoard1.PanButton
        End Get
        Set(ByVal value As System.Windows.Forms.MouseButtons)
            DrawingBoard1.PanButton = value
        End Set
    End Property

    Public Property ZoomOnMouseWheel() As Boolean
        Get
            Return DrawingBoard1.ZoomOnMouseWheel
        End Get
        Set(ByVal value As Boolean)
            DrawingBoard1.ZoomOnMouseWheel = value
        End Set
    End Property

    Public Property ZoomFactor() As Double
        Get
            Return DrawingBoard1.ZoomFactor
        End Get
        Set(ByVal value As Double)
            DrawingBoard1.ZoomFactor = value
        End Set
    End Property

    Public Property Origin() As System.Drawing.Point
        Get
            Return DrawingBoard1.Origin
        End Get
        Set(ByVal value As System.Drawing.Point)
            DrawingBoard1.Origin = value
        End Set
    End Property

    Public Property StretchImageToFit() As Boolean
        Get
            Return Me.DrawingBoard1.StretchImageToFit
        End Get
        Set(ByVal value As Boolean)
            Me.DrawingBoard1.StretchImageToFit = value
        End Set
    End Property

    Public ReadOnly Property ApparentImageSize() As System.Drawing.Size
        Get
            Return DrawingBoard1.ApparentImageSize
        End Get
    End Property

    Public Sub fittoscreen()
        Me.DrawingBoard1.fittoscreen()
    End Sub

    Public Sub InvertColors()
        Me.DrawingBoard1.InvertColors()
    End Sub

    Public Sub ZoomIn()
        Me.DrawingBoard1.ZoomIn()
    End Sub

    Public Sub ZoomOut()
        Me.DrawingBoard1.ZoomOut()
    End Sub

    Public Property ScrollbarsVisible() As Boolean
        Get
            Return m_ScrollVisible
        End Get
        Set(ByVal value As Boolean)
            m_ScrollVisible = value
            Me.HScrollBar1.Visible = value
            Me.VScrollBar1.Visible = value
            If value = False Then
                Me.DrawingBoard1.Dock = DockStyle.Fill
            Else
                Me.DrawingBoard1.Dock = DockStyle.None
                Me.DrawingBoard1.Location = New Point(0, 0)
                Me.DrawingBoard1.Width = ClientSize.Width - VScrollBar1.Width
                Me.DrawingBoard1.Height = ClientSize.Height - HScrollBar1.Height

            End If
        End Set
    End Property

#End Region

#Region "Public/Private Shadows"
    Public Shadows Property Image() As System.Drawing.Image
        Get
            Return DrawingBoard1.Image
        End Get
        Set(ByVal Value As System.Drawing.Image)
            DrawingBoard1.Image = Value
            If Value Is Nothing Then
                HScrollBar1.Enabled = False
                VScrollBar1.Enabled = False
                Exit Property
            End If
        End Set
    End Property

    Public Shadows Property initialimage() As System.Drawing.Image
        Get
            Return DrawingBoard1.initialimage
        End Get
        Set(ByVal value As System.Drawing.Image)
            DrawingBoard1.initialimage = value
            If value Is Nothing Then
                HScrollBar1.Enabled = False
                VScrollBar1.Enabled = False
                Exit Property
            End If
        End Set
    End Property

    Public Shadows Property BackgroundImage() As System.Drawing.Image
        Get
            Return DrawingBoard1.BackgroundImage
        End Get
        Set(ByVal Value As System.Drawing.Image)
            DrawingBoard1.BackgroundImage = Value
            If Value Is Nothing Then
                HScrollBar1.Enabled = False
                VScrollBar1.Enabled = False
                Exit Property
            End If
        End Set
    End Property

#End Region

    Public Sub RotateFlip(ByVal RotateFlipType As System.Drawing.RotateFlipType)
        DrawingBoard1.RotateFlip(RotateFlipType)
    End Sub

    Private Sub DrawingBoard1_SetScrollPositions() Handles DrawingBoard1.SetScrollPositions
        While True
            Try
                Dim DrawingWidth As Integer = DrawingBoard1.Image.Width
                Dim DrawingHeight As Integer = DrawingBoard1.Image.Height
                Dim OriginX As Integer = DrawingBoard1.Origin.X
                Dim OriginY As Integer = DrawingBoard1.Origin.Y
                Dim FactoredCtrlWidth As Integer = DrawingBoard1.Width / DrawingBoard1.ZoomFactor
                Dim FactoredCtrlHeight As Integer = DrawingBoard1.Height / DrawingBoard1.ZoomFactor
                HScrollBar1.Maximum = Me.DrawingBoard1.Image.Width
                VScrollBar1.Maximum = Me.DrawingBoard1.Image.Height

                If FactoredCtrlWidth >= DrawingBoard1.Image.Width Or StretchImageToFit Then
                    HScrollBar1.Enabled = False
                    HScrollBar1.Value = 0
                Else
                    HScrollBar1.LargeChange = FactoredCtrlWidth
                    HScrollBar1.Enabled = True
                    HScrollBar1.Value = OriginX
                End If

                If FactoredCtrlHeight >= DrawingBoard1.Image.Height Or StretchImageToFit Then
                    VScrollBar1.Enabled = False
                    VScrollBar1.Value = 0
                Else
                    VScrollBar1.Enabled = True
                    VScrollBar1.LargeChange = FactoredCtrlHeight
                    VScrollBar1.Value = OriginY
                End If

                Exit While
            Catch ex As Exception

            End Try
        End While
    End Sub

    Private Sub DrawingBoard1_MouseDown(ByVal sender As Object, ByVal e As System.Windows.Forms.MouseEventArgs) Handles DrawingBoard1.PreMouseDown
        RaiseEvent MouseDown(sender, e)
    End Sub
    Private Sub DrawingBoard1_MouseUp(ByVal sender As Object, ByVal e As System.Windows.Forms.MouseEventArgs) Handles DrawingBoard1.PreMouseUp
        RaiseEvent MouseUp(sender, e)
    End Sub
    Private Sub DrawingBoard1_PreMouseWheel(ByVal sender As Object, ByVal e As System.Windows.Forms.MouseEventArgs) Handles DrawingBoard1.PreMouseWheel
        RaiseEvent PreMouseWheel(sender, e)
    End Sub
    Private Sub DrawingBoard1_PostMouseWheel(ByVal sender As Object, ByVal e As System.Windows.Forms.MouseEventArgs) Handles DrawingBoard1.PostMouseWheel
        RaiseEvent PostMouseWheel(sender, e)
    End Sub

    Private Sub ScrollBar_ValueChanged(ByVal sender As Object, ByVal e As System.EventArgs) Handles HScrollBar1.ValueChanged, VScrollBar1.ValueChanged
        Me.DrawingBoard1.Origin = New Point(HScrollBar1.Value, VScrollBar1.Value)
    End Sub
End Class
