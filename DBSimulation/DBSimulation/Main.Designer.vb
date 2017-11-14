<Global.Microsoft.VisualBasic.CompilerServices.DesignerGenerated()> _
Partial Class Main
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
        Me.lstFarmers = New System.Windows.Forms.ListBox()
        Me.btnAddFarmer = New System.Windows.Forms.Button()
        Me.lblDate = New System.Windows.Forms.Label()
        Me.SuspendLayout()
        '
        'lstFarmers
        '
        Me.lstFarmers.FormattingEnabled = True
        Me.lstFarmers.Items.AddRange(New Object() {"Farmer1", "Farmer2", "Farmer3"})
        Me.lstFarmers.Location = New System.Drawing.Point(12, 78)
        Me.lstFarmers.Name = "lstFarmers"
        Me.lstFarmers.Size = New System.Drawing.Size(260, 173)
        Me.lstFarmers.TabIndex = 0
        '
        'btnAddFarmer
        '
        Me.btnAddFarmer.Location = New System.Drawing.Point(104, 49)
        Me.btnAddFarmer.Name = "btnAddFarmer"
        Me.btnAddFarmer.Size = New System.Drawing.Size(75, 23)
        Me.btnAddFarmer.TabIndex = 1
        Me.btnAddFarmer.Text = "Add Farmer"
        Me.btnAddFarmer.UseVisualStyleBackColor = True
        '
        'lblDate
        '
        Me.lblDate.AutoSize = True
        Me.lblDate.Location = New System.Drawing.Point(12, 9)
        Me.lblDate.Name = "lblDate"
        Me.lblDate.Size = New System.Drawing.Size(39, 13)
        Me.lblDate.TabIndex = 3
        Me.lblDate.Text = "Label1"
        '
        'Main
        '
        Me.AutoScaleDimensions = New System.Drawing.SizeF(6.0!, 13.0!)
        Me.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font
        Me.ClientSize = New System.Drawing.Size(284, 261)
        Me.Controls.Add(Me.lblDate)
        Me.Controls.Add(Me.btnAddFarmer)
        Me.Controls.Add(Me.lstFarmers)
        Me.Name = "Main"
        Me.Text = "Form1"
        Me.ResumeLayout(False)
        Me.PerformLayout()

    End Sub

    Friend WithEvents lstFarmers As ListBox
    Friend WithEvents btnAddFarmer As Button
    Friend WithEvents lblDate As Label
End Class
