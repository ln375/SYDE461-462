Public Class Main
    Private Sub ListBox1_SelectedIndexChanged(sender As Object, e As EventArgs) Handles lstFarmers.SelectedIndexChanged

    End Sub

    Private Sub lstFarmers_DoubleClick(sender As Object, e As EventArgs) Handles lstFarmers.DoubleClick
        My.Forms.FarmerEntry.Show()
    End Sub

    Private Sub Main_Load(ByVal sender As Object,
    ByVal e As System.EventArgs) Handles MyBase.Load
        lblDate.Text = "Date: " + System.DateTime.Now.ToShortDateString
    End Sub

    Private Sub btnAddFarmer_Click(sender As Object, e As EventArgs) Handles btnAddFarmer.Click

    End Sub
End Class
