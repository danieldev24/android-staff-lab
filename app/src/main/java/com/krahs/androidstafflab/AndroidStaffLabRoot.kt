package com.krahs.androidstafflab

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.krahs.androidstafflab.navigation.AppNavigation
import com.krahs.androidstafflab.ui.theme.AndroidStaffLabTheme

@Composable
fun AndroidStaffLabRoot(modifier: Modifier = Modifier) {
    AppNavigation(modifier = modifier)
}

@Preview(showBackground = true)
@Composable
private fun AndroidStaffLabRootPreview() {
    AndroidStaffLabTheme {
        AndroidStaffLabRoot()
    }
}
