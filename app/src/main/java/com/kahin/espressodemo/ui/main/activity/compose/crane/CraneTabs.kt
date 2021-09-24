package com.kahin.espressodemo.ui.main.activity.compose.crane

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.os.ConfigurationCompat
import com.kahin.espressodemo.R

enum class CraneScreen {
    Eat, Sleep
}

@Composable
fun CraneTabBar(
    modifier: Modifier = Modifier,
    onMenuClicked: () -> Unit,
    children: @Composable (Modifier) -> Unit
) {
    Row(modifier) {
        // Separate Row as the children shouldn't have the padding
        Row(Modifier.padding(top = 8.dp)) {
            Image(
                modifier = Modifier
                    .size(60.dp)
                    .padding(top = 8.dp)
                    .clickable(onClick = onMenuClicked),
                painter = painterResource(id = R.drawable.ic_launcher_background),
                contentDescription = stringResource(id = R.string.cd_menu)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Image(
                modifier = Modifier
                    .size(60.dp)
                    .padding(top = 8.dp),
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = null
            )
        }
        children(
            Modifier
                .weight(1f)
                .align(Alignment.CenterVertically)
        )
    }
}

@Composable
fun CraneTabs(
    modifier: Modifier = Modifier,
    titles: List<String>,
    tabSelected: CraneScreen,
    onTabSelected: (CraneScreen) -> Unit
) {
    TabRow(
        selectedTabIndex = tabSelected.ordinal,
        modifier,
        contentColor = MaterialTheme.colors.onSurface,
        indicator = { },
        divider = { }
    ) {
        titles.forEachIndexed { index, title ->
            val selected = index == tabSelected.ordinal

            var textModifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)
            if (selected) {
                textModifier = Modifier
                    .border(BorderStroke(2.dp, Color.White), RoundedCornerShape(16.dp))
                    .then(textModifier)
            }

            Tab(
                selected = selected,
                onClick = { onTabSelected(CraneScreen.values()[index]) }
            ) {
                Text(
                    modifier = textModifier,
                    text = title.uppercase(
                        ConfigurationCompat.getLocales(LocalConfiguration.current)[0]
                    )
                )
            }
        }
    }
}

@Preview
@Composable
fun previewTab() {
    CraneTabBar(onMenuClicked = {}) {
            tabBarModifier ->
        CraneTabs(
            tabBarModifier,
            CraneScreen.values().map { it.name },
            tabSelected = CraneScreen.Eat,
            onTabSelected = {}
        )
    }
}