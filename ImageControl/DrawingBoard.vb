Imports System.Drawing.Imaging

<System.Runtime.InteropServices.ComVisible(False)> _
Friend Class DrawingBoard

    'Public Events
    Public Event SetScrollPositions()

    Public Event PreMouseDown(ByVal sender As Object, ByVal e As System.Windows.Forms.MouseEventArgs)
    Public Event PreMouseUp(ByVal sender As Object, ByVal e As System.Windows.Forms.MouseEventArgs)
    Public Event PreMouseWheel(ByVal sender As Object, ByVal e As System.Windows.Forms.MouseEventArgs)
    Public Event PostMouseWheel(ByVal sender As Object, ByVal e As System.Windows.Forms.MouseEventArgs)

    'Member Variables
    Private m_MouseButtons As System.Windows.Forms.MouseButtons = Windows.Forms.MouseButtons.Left
    Private m_OriginalImage As System.Drawing.Bitmap

    Private m_StartPoint As System.Drawing.Point
    Private m_Origin As New System.Drawing.Point(0, 0)

    Private g As Graphics
    Private SrcRect As System.Drawing.Rectangle
    Private DestRect As System.Drawing.Rectangle

    Private m_ZoomOnMouseWheel As Boolean = True
    Private m_ZoomFactor As Double = 1.0

    Private m_ApparentImageSize As New Size(0, 0)

    Private m_DrawWidth As Integer
    Private m_DrawHeight As Integer

    Private m_centerpoint As Point

    Private m_PanMode As Boolean = True
    Private m_StretchImageToFit As Boolean = False

    Private m_Select_Rect As Rectangle
    Private m_Select_Pen As New Pen(Color.Blue, 2) ' Pen used to indicate a selection on the image (zoom window)

    Private EndPoint As Point ' for pan and box-zoom


#Region "Public/Private Shadows"
    Public Shadows Property Image() As System.Drawing.Image
        Get
            Return m_OriginalImage
        End Get
        Set(ByVal Value As System.Drawing.Image)
            If Not m_OriginalImage Is Nothing Then
                m_OriginalImage.Dispose()
                m_Select_Rect = Nothing
                m_Origin = New Point(0, 0)
                m_ApparentImageSize = New Size(0, 0)
                m_ZoomFactor = 1
                GC.Collect()
            End If

            If Value Is Nothing Then
                m_OriginalImage = Nothing
                Me.Invalidate()
                Exit Property
            End If

            Dim r As New Rectangle(0, 0, Value.Width, Value.Height)
            m_OriginalImage = New Bitmap(Value)
            Dim bmpData As New BitmapData
            m_OriginalImage = DirectCast(m_OriginalImage.Clone(r, Imaging.PixelFormat.Format32bppPArgb), Bitmap)

            'Force a paint
            Me.Invalidate()
        End Set
    End Property

    Public Shadows Property initialimage() As System.Drawing.Image
        Get
            Return m_OriginalImage
        End Get
        Set(ByVal value As System.Drawing.Image)
            Me.Image = value
            Me.ZoomFactor = 1
        End Set
    End Property

    Public Shadows Property BackgroundImage() As System.Drawing.Image
        Get
            Return Nothing
        End Get
        Set(ByVal Value As System.Drawing.Image)
            Me.Image = Value
            Me.ZoomFactor = 1
        End Set
    End Property

#End Region

#Region "Protected Overrides"

    Protected Overrides Sub OnPaint(ByVal e As PaintEventArgs)
        e.Graphics.Clear(Me.BackColor)
        DrawImage(e.Graphics)
        MyBase.OnPaint(e)
    End Sub

    Protected Overrides Sub OnSizeChanged(ByVal e As EventArgs)
        DestRect = New System.Drawing.Rectangle(0, 0, ClientSize.Width, ClientSize.Height)
        ComputeDrawingArea()
        MyBase.OnSizeChanged(e)
    End Sub

#End Region

#Region "Public Properties"

    Public Sub ZoomIn()
        ZoomImage(True)
    End Sub

    Public Sub ZoomOut()
        ZoomImage(False)
    End Sub

    Private Sub ZoomImage(ByVal ZoomIn As Boolean)
        ' Get center point
        m_centerpoint.X = m_Origin.X + SrcRect.Width / 2
        m_centerpoint.Y = m_Origin.Y + SrcRect.Height / 2

        'set new zoomfactor
        If ZoomIn Then
            ZoomFactor = Math.Round(ZoomFactor * 1.1, 2)
        Else
            ZoomFactor = Math.Round(ZoomFactor * 0.9, 2)
        End If

        'Reset the origin to maintain center point
        m_Origin.X = m_centerpoint.X - ClientSize.Width / m_ZoomFactor / 2
        m_Origin.Y = m_centerpoint.Y - ClientSize.Height / m_ZoomFactor / 2

        CheckBounds()
    End Sub

    Public Sub InvertColors()
        Try
            Cursor = Cursors.WaitCursor
            If m_OriginalImage Is Nothing Then Exit Sub
            ' This is the color matrix to invert the image colors.
            Dim cm As ColorMatrix = New ColorMatrix(New Single()() _
                                   {New Single() {-1, 0, 0, 0, 0}, _
                                    New Single() {0, -1, 0, 0, 0}, _
                                    New Single() {0, 0, -1, 0, 0}, _
                                    New Single() {0, 0, 0, 1, 0}, _
                                    New Single() {1, 1, 1, 1, 1}})

            Dim ImageAttributes As New ImageAttributes()
            ImageAttributes.SetColorMatrix(cm)

            Dim g As Graphics
            g = Graphics.FromImage(m_OriginalImage)

            Dim rc As New Rectangle(0, 0, m_OriginalImage.Width, m_OriginalImage.Height)
            g.DrawImage(m_OriginalImage, rc, 0, 0, m_OriginalImage.Width, m_OriginalImage.Height, GraphicsUnit.Pixel, ImageAttributes)

            Me.Invalidate()
        Catch ex As Exception
            Throw ex
        Finally
            Cursor = Cursors.Default
        End Try
    End Sub

    Public Property PanButton() As System.Windows.Forms.MouseButtons
        Get
            Return m_MouseButtons
        End Get
        Set(ByVal value As System.Windows.Forms.MouseButtons)
            m_MouseButtons = value
        End Set
    End Property

    Public Property ZoomOnMouseWheel() As Boolean
        Get
            Return m_ZoomOnMouseWheel
        End Get
        Set(ByVal value As Boolean)
            m_ZoomOnMouseWheel = value
        End Set
    End Property

    Public Property ZoomFactor() As Double
        Get
            Return m_ZoomFactor
        End Get
        Set(ByVal value As Double)
            m_ZoomFactor = value
            If m_ZoomFactor > 15 Then m_ZoomFactor = 15
            If m_ZoomFactor < 0.05 Then m_ZoomFactor = 0.05
            If Not m_OriginalImage Is Nothing Then
                m_ApparentImageSize.Height = m_OriginalImage.Height * m_ZoomFactor
                m_ApparentImageSize.Width = m_OriginalImage.Width * m_ZoomFactor
                ComputeDrawingArea()
                CheckBounds()
            End If
            Me.Invalidate()
        End Set
    End Property

    Public Property Origin() As System.Drawing.Point
        Get
            Return m_Origin
        End Get
        Set(ByVal value As System.Drawing.Point)
            m_Origin = value
            Me.Invalidate()
        End Set
    End Property

    Public ReadOnly Property ApparentImageSize() As System.Drawing.Size
        Get
            Return m_ApparentImageSize
        End Get
    End Property

    Public Property PanMode() As Boolean
        Get
            Return m_PanMode
        End Get
        Set(ByVal value As Boolean)
            m_PanMode = value
        End Set
    End Property

    Public Property StretchImageToFit() As Boolean
        Get
            Return m_StretchImageToFit
        End Get
        Set(ByVal value As Boolean)
            m_StretchImageToFit = value
            Me.Invalidate()
        End Set
    End Property

    Public Sub fittoscreen()
        Me.StretchImageToFit = False
        Me.Origin = New Point(0, 0)
        If m_OriginalImage Is Nothing Then Exit Sub
        ZoomFactor = Math.Min(ClientSize.Width / m_OriginalImage.Width, ClientSize.Height / m_OriginalImage.Height)
    End Sub

#End Region

    Private Property Selected_Rectangle() As Rectangle
        Get
            Return m_Select_Rect
        End Get
        Set(ByVal Value As Rectangle)
            m_Select_Rect = Value
            Me.Invalidate()
        End Set
    End Property

    Private Sub Draw_Rectangle(ByVal e As System.Windows.Forms.MouseEventArgs)
        If (New Rectangle(0, 0, Me.Width, Me.Height)).Contains(PointToClient(Windows.Forms.Cursor.Position)) Then
            Dim Width As Integer = System.Math.Abs(m_StartPoint.X - e.X)
            Dim Height As Integer = System.Math.Abs(m_StartPoint.Y - e.Y)
            Dim UpperLeft As Point
            'need to determine the  upper left corner of the rectangel regardless of whether it's 
            'the start point or the end point, or other.
            UpperLeft = New Point(System.Math.Min(m_StartPoint.X, e.X), System.Math.Min(m_StartPoint.Y, e.Y))
            Selected_Rectangle = New Rectangle(UpperLeft.X, UpperLeft.Y, Width, Height)
        End If
    End Sub

    Private Sub DrawImage(ByRef g As Graphics)
        If m_OriginalImage Is Nothing Then Exit Sub

        g.PixelOffsetMode = Drawing2D.PixelOffsetMode.Half
        g.SmoothingMode = Drawing2D.SmoothingMode.None
        g.InterpolationMode = Drawing2D.InterpolationMode.NearestNeighbor

        If m_StretchImageToFit Then
            SrcRect = New System.Drawing.Rectangle(0, 0, m_OriginalImage.Width, m_OriginalImage.Height)
        Else
            SrcRect = New System.Drawing.Rectangle(m_Origin.X, m_Origin.Y, m_DrawWidth, m_DrawHeight)
        End If

        g.DrawImage(m_OriginalImage, DestRect, SrcRect, GraphicsUnit.Pixel)

        If Not PanMode Then
            g.DrawRectangle(m_Select_Pen, Selected_Rectangle)
        End If

        RaiseEvent SetScrollPositions()

    End Sub

    Private Sub ComputeDrawingArea()
        m_DrawHeight = Me.Height / m_ZoomFactor
        m_DrawWidth = Me.Width / m_ZoomFactor

    End Sub

    Private Sub ImageViewer_MouseDown(ByVal sender As Object, ByVal e As System.Windows.Forms.MouseEventArgs) Handles Me.MouseDown
        RaiseEvent PreMouseDown(sender, e)

        If m_OriginalImage Is Nothing Then Exit Sub
        EndPoint = Nothing
        Selected_Rectangle = Nothing

        If e.Button = MouseButtons.Middle Then
            PanMode = False
            PanButton = MouseButtons.Middle
        ElseIf e.Button = MouseButtons.Left Then
            PanMode = True
            PanButton = MouseButtons.Left
        Else
            Origin = New Point(0, 0)
            ZoomFactor = 1
        End If

        'Set the start point. This is used for panning and zooming so we always set it.
        m_StartPoint = New Point(e.X, e.Y)
        Me.Focus()
    End Sub

    Private Sub ImageViewer_MouseMove(ByVal sender As Object, ByVal e As System.Windows.Forms.MouseEventArgs) Handles Me.MouseMove
        If m_OriginalImage Is Nothing Then Exit Sub
        If e.Button = m_MouseButtons Then

            Dim DeltaX As Integer = m_StartPoint.X - e.X
            Dim DeltaY As Integer = m_StartPoint.Y - e.Y

            If PanMode Then
                'Set the origin of the new image

                m_Origin.X = m_Origin.X + (DeltaX / m_ZoomFactor)
                m_Origin.Y = m_Origin.Y + (DeltaY / m_ZoomFactor)

                CheckBounds()

                'reset the startpoints
                m_StartPoint.X = e.X
                m_StartPoint.Y = e.Y

                'Force a paint
                Me.Invalidate()
            Else
                Draw_Rectangle(e)
            End If
        End If
    End Sub

    Private Sub ImageViewer_MouseUp(ByVal sender As Object, ByVal e As System.Windows.Forms.MouseEventArgs) Handles Me.MouseUp
        RaiseEvent PreMouseUp(sender, e)
    End Sub

    Private Sub CheckBounds()
        If m_OriginalImage Is Nothing Then Exit Sub
        'Make sure we don't go out of bounds
        If m_Origin.X < 0 Then m_Origin.X = 0
        If m_Origin.Y < 0 Then m_Origin.Y = 0
        If m_Origin.X > m_OriginalImage.Width - (ClientSize.Width / m_ZoomFactor) Then
            m_Origin.X = m_OriginalImage.Width - (ClientSize.Width / m_ZoomFactor)
        End If
        If m_Origin.Y > m_OriginalImage.Height - (ClientSize.Height / m_ZoomFactor) Then
            m_Origin.Y = m_OriginalImage.Height - (ClientSize.Height / m_ZoomFactor)
        End If

        If m_Origin.X < 0 Then m_Origin.X = 0
        If m_Origin.Y < 0 Then m_Origin.Y = 0
    End Sub

    Private Sub DrawingBoard_MouseUp(ByVal sender As Object, ByVal e As System.Windows.Forms.MouseEventArgs) Handles Me.MouseUp
        If m_OriginalImage Is Nothing Then Exit Sub
        If Not PanMode Then
            EndPoint = New Point(e.X, e.Y)
            If Selected_Rectangle = Nothing Then Exit Sub
            ZoomSelection()
        End If
    End Sub

    Private Sub ZoomSelection()
        If m_OriginalImage Is Nothing Then Exit Sub
        Try

            Dim NewOrigin As New Point(CInt(Me.Origin.X + (Selected_Rectangle.X / ZoomFactor)), _
                                              Me.Origin.Y + (Selected_Rectangle.Y / ZoomFactor))

            Dim NewFactor As Double
            If Selected_Rectangle.Width > Selected_Rectangle.Height Then
                NewFactor = (ClientSize.Width / (Selected_Rectangle.Width / ZoomFactor))
            Else
                NewFactor = (ClientSize.Height / (Selected_Rectangle.Height / ZoomFactor))
            End If

            Me.Origin = NewOrigin
            Me.ZoomFactor = NewFactor

        Catch ex As InvalidOperationException
        Catch ex As Exception
            Throw ex
        End Try
        Selected_Rectangle = Nothing
    End Sub


    Private Sub ImageViewer_MouseWheel(ByVal sender As Object, ByVal e As System.Windows.Forms.MouseEventArgs) Handles Me.MouseWheel
        RaiseEvent PreMouseWheel(sender, e)
        If Not ZoomOnMouseWheel Then Exit Sub
        'set new zoomfactor
        If e.Delta > 0 Then
            ZoomImage(True)
        ElseIf e.Delta < 0 Then
            ZoomImage(False)
        End If
        RaiseEvent PostMouseWheel(sender, e)
    End Sub

    Public Sub RotateFlip(ByVal RotateFlipType As System.Drawing.RotateFlipType)
        If m_OriginalImage Is Nothing Then Exit Sub
        m_OriginalImage.RotateFlip(RotateFlipType)
        Me.Invalidate()
    End Sub

    Public Sub New()

        ' This call is required by the Windows Form Designer.
        InitializeComponent()

        ' Add any initialization after the InitializeComponent() call.
        Me.SetStyle(ControlStyles.AllPaintingInWmPaint, True)
        Me.SetStyle(ControlStyles.DoubleBuffer, True)
    End Sub

    Private Sub DrawingBoard_Resize(ByVal sender As Object, ByVal e As System.EventArgs) Handles Me.Resize
        Me.ComputeDrawingArea()
        If Me.StretchImageToFit Then Me.Invalidate()
    End Sub


End Class

