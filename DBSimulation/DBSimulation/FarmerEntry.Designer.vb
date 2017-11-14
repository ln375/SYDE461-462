<Global.Microsoft.VisualBasic.CompilerServices.DesignerGenerated()> _
Partial Class FarmerEntry
    Inherits System.Windows.Forms.Form

    'Form overrides dispose to clean up the component list.
    <System.Diagnostics.DebuggerNonUserCode()> _
    Protected Overrides Sub Dispose(ByVal disposing As Boolean)
        Try
            If disposing AndAlso components IsNot Nothing Then
                components.Dispose()
            End If
        Finally
            MyBase.Dispose(disposing)
        End Try
    End Sub

    'Required by the Windows Form Designer
    Private components As System.ComponentModel.IContainer

    'NOTE: The following procedure is required by the Windows Form Designer
    'It can be modified using the Windows Form Designer.  
    'Do not modify it using the code editor.
    <System.Diagnostics.DebuggerStepThrough()> _
    Private Sub InitializeComponent()
        Dim ChartArea1 As System.Windows.Forms.DataVisualization.Charting.ChartArea = New System.Windows.Forms.DataVisualization.Charting.ChartArea()
        Dim Legend1 As System.Windows.Forms.DataVisualization.Charting.Legend = New System.Windows.Forms.DataVisualization.Charting.Legend()
        Dim Series1 As System.Windows.Forms.DataVisualization.Charting.Series = New System.Windows.Forms.DataVisualization.Charting.Series()
        Me.lblFarmerName = New System.Windows.Forms.Label()
        Me.Chart1 = New System.Windows.Forms.DataVisualization.Charting.Chart()
        Me.lblTests = New System.Windows.Forms.Label()
        Me.btnSmell = New System.Windows.Forms.Button()
        Me.btnDensity = New System.Windows.Forms.Button()
        Me.btnAlcohol = New System.Windows.Forms.Button()
        Me.lblJugs = New System.Windows.Forms.Label()
        Me.btnJug = New System.Windows.Forms.Button()
        Me.lblVolume = New System.Windows.Forms.Label()
        CType(Me.Chart1, System.ComponentModel.ISupportInitialize).BeginInit()
        Me.SuspendLayout()
        '
        'lblFarmerName
        '
        Me.lblFarmerName.AutoSize = True
        Me.lblFarmerName.Location = New System.Drawing.Point(12, 9)
        Me.lblFarmerName.Name = "lblFarmerName"
        Me.lblFarmerName.Size = New System.Drawing.Size(70, 13)
        Me.lblFarmerName.TabIndex = 0
        Me.lblFarmerName.Text = "Farmer Name"
        '
        'Chart1
        '
        ChartArea1.Name = "ChartArea1"
        Me.Chart1.ChartAreas.Add(ChartArea1)
        Legend1.Name = "Legend1"
        Me.Chart1.Legends.Add(Legend1)
        Me.Chart1.Location = New System.Drawing.Point(15, 35)
        Me.Chart1.Name = "Chart1"
        Series1.ChartArea = "ChartArea1"
        Series1.Legend = "Legend1"
        Series1.Name = "Series1"
        Me.Chart1.Series.Add(Series1)
        Me.Chart1.Size = New System.Drawing.Size(311, 179)
        Me.Chart1.TabIndex = 1
        Me.Chart1.Text = "Chart1"
        '
        'lblTests
        '
        Me.lblTests.AutoSize = True
        Me.lblTests.Location = New System.Drawing.Point(12, 227)
        Me.lblTests.Name = "lblTests"
        Me.lblTests.Size = New System.Drawing.Size(33, 13)
        Me.lblTests.TabIndex = 2
        Me.lblTests.Text = "Tests"
        '
        'btnSmell
        '
        Me.btnSmell.Location = New System.Drawing.Point(33, 252)
        Me.btnSmell.Name = "btnSmell"
        Me.btnSmell.Size = New System.Drawing.Size(75, 48)
        Me.btnSmell.TabIndex = 3
        Me.btnSmell.Text = "Smell"
        Me.btnSmell.UseVisualStyleBackColor = True
        '
        'btnDensity
        '
        Me.btnDensity.Location = New System.Drawing.Point(130, 252)
        Me.btnDensity.Name = "btnDensity"
        Me.btnDensity.RightToLeft = System.Windows.Forms.RightToLeft.No
        Me.btnDensity.Size = New System.Drawing.Size(75, 48)
        Me.btnDensity.TabIndex = 4
        Me.btnDensity.Text = "Density"
        Me.btnDensity.UseVisualStyleBackColor = True
        '
        'btnAlcohol
        '
        Me.btnAlcohol.Location = New System.Drawing.Point(228, 252)
        Me.btnAlcohol.Name = "btnAlcohol"
        Me.btnAlcohol.Size = New System.Drawing.Size(75, 48)
        Me.btnAlcohol.TabIndex = 5
        Me.btnAlcohol.Text = "Alcohol"
        Me.btnAlcohol.UseVisualStyleBackColor = True
        '
        'lblJugs
        '
        Me.lblJugs.AutoSize = True
        Me.lblJugs.Location = New System.Drawing.Point(12, 318)
        Me.lblJugs.Name = "lblJugs"
        Me.lblJugs.Size = New System.Drawing.Size(29, 13)
        Me.lblJugs.TabIndex = 6
        Me.lblJugs.Text = "Jugs"
        '
        'btnJug
        '
        Me.btnJug.Location = New System.Drawing.Point(33, 344)
        Me.btnJug.Name = "btnJug"
        Me.btnJug.Size = New System.Drawing.Size(75, 27)
        Me.btnJug.TabIndex = 7
        Me.btnJug.Text = "Select"
        Me.btnJug.UseVisualStyleBackColor = True
        '
        'lblVolume
        '
        Me.lblVolume.AutoSize = True
        Me.lblVolume.Location = New System.Drawing.Point(156, 318)
        Me.lblVolume.Name = "lblVolume"
        Me.lblVolume.Size = New System.Drawing.Size(42, 13)
        Me.lblVolume.TabIndex = 8
        Me.lblVolume.Text = "Volume"
        '
        'FarmerEntry
        '
        Me.AutoScaleDimensions = New System.Drawing.SizeF(6.0!, 13.0!)
        Me.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font
        Me.ClientSize = New System.Drawing.Size(339, 398)
        Me.Controls.Add(Me.lblVolume)
        Me.Controls.Add(Me.btnJug)
        Me.Controls.Add(Me.lblJugs)
        Me.Controls.Add(Me.btnAlcohol)
        Me.Controls.Add(Me.btnDensity)
        Me.Controls.Add(Me.btnSmell)
        Me.Controls.Add(Me.lblTests)
        Me.Controls.Add(Me.Chart1)
        Me.Controls.Add(Me.lblFarmerName)
        Me.Name = "FarmerEntry"
        Me.Text = "FarmerEntry"
        CType(Me.Chart1, System.ComponentModel.ISupportInitialize).EndInit()
        Me.ResumeLayout(False)
        Me.PerformLayout()

    End Sub

    Friend WithEvents lblFarmerName As Label
    Friend WithEvents Chart1 As DataVisualization.Charting.Chart
    Friend WithEvents lblTests As Label
    Friend WithEvents btnSmell As Button
    Friend WithEvents btnDensity As Button
    Friend WithEvents btnAlcohol As Button
    Friend WithEvents lblJugs As Label
    Friend WithEvents btnJug As Button
    Friend WithEvents lblVolume As Label
End Class
