package com.kahin.espressodemo.ui.main.activity.compose.conversation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kahin.espressodemo.R
import com.kahin.espressodemo.ui.main.activity.compose.ui.theme.EspressoDemoTheme
import com.kahin.espressodemo.ui.main.activity.compose.ui.theme.elevatedSurface

@Composable
fun ChatAppBar(
    modifier: Modifier = Modifier,
    onNavIconPressed: () -> Unit = { },
    title: @Composable RowScope.() -> Unit,
    actions: @Composable RowScope.() -> Unit = {}
) {
    // This bar is translucent but elevation overlays are not applied to translucent colors.
    // Instead we manually calculate the elevated surface color from the opaque color,
    // then apply our alpha.
    //
    // We set the background on the Column rather than the TopAppBar,
    // so that the background is drawn behind any padding set on the app bar (i.e. status bar).
    val backgroundColor = MaterialTheme.colors.elevatedSurface(3.dp)
    Column(
        Modifier.background(backgroundColor.copy(0.95f))
    ) {
        TopAppBar(
            modifier = modifier,
            backgroundColor = Color.Transparent,
            elevation = 0.dp, // No shadow needed
            contentColor = MaterialTheme.colors.onSurface,
            actions = actions,
            title = { Row { title() } },
            navigationIcon = {
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_background),
                    contentDescription = stringResource(id = R.string.back),
                    modifier = Modifier
                        .clickable(onClick = onNavIconPressed)
                        .padding(horizontal = 16.dp)
                )
            }
        )
        Divider()
    }
}

@Preview
@Composable
fun JetchatAppBarPreview() {
    EspressoDemoTheme {
        ChatAppBar(title = { Text("Preview!") })
    }
}

@Preview
@Composable
fun JetchatAppBarPreviewDark() {
    EspressoDemoTheme(darkTheme = true) {
        ChatAppBar(title = { Text("Preview!") })
    }
}