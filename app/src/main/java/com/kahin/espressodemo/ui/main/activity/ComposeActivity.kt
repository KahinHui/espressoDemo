package com.kahin.espressodemo.ui.main.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kahin.espressodemo.R

class ComposeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Greeting("Compose")
        }
    }
}

@Composable
fun Greeting(name: String) {
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_background),
            contentDescription = null,
            modifier = Modifier
                .height(180.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(4.dp)),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "hello $name~ fsdf sdf sdfsfsdfs dkadla s dk sad a,nlda  dasda jfjlskafjlslfjkls",
            style = typography.h6,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis)
        Text(text = "hello $name~", style = typography.body2)
        Text(text = "hello $name~", style = typography.body2)
    }
}

@Preview
@Composable
fun PreviewGreeting() {
    Greeting("Compose")
}
