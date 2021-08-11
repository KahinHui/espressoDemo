package com.kahin.espressodemo.ui.main.activity.compose

import android.content.res.Configuration
import android.util.Log
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavHostController
import com.kahin.espressodemo.R
import com.kahin.espressodemo.ui.main.activity.compose.ui.theme.EspressoDemoTheme
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    onNavigationEvent: MainActions,
    openDrawer: () -> Unit
) {
    HomeContent(
        openDrawer = openDrawer,
        logOut = {
            onNavigationEvent.logOut()
        }
    )
}

@Composable
fun HomeContent(
    openDrawer: () -> Unit,
    logOut: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "LayoutsCodelab")
                },
                navigationIcon = {
                    IconButton(onClick = { coroutineScope.launch { openDrawer() } }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_launcher_background),
                            contentDescription = stringResource(id = R.string.activity)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {}) {
                        Icon(Icons.Filled.Favorite, contentDescription = null)
                    }
                }
            )
        }
    ) { innerPadding ->
        MyOwnColumn(modifier = Modifier.padding(8.dp)) {
            Greeting("Compose")
            BodyContent(Modifier.padding(innerPadding))
            LazyList()
        }
    }
}

@Composable
fun Greeting(name: String) {
    Column {
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

        var isExpanded by remember { mutableStateOf(false) }
        val surfaceColor: Color by animateColorAsState(
            if (isExpanded) MaterialTheme.colors.primary else MaterialTheme.colors.surface
        )

        Surface(
            color = surfaceColor,
            modifier = Modifier
                .animateContentSize()
                .padding(1.dp)
        ) {
            Text(
                text = "hello $name~ fsdf sdf sdfsfsdfs dkadla s dk sad a,nlda  dasda jfjlska" +
                        "fjlslfjkls djs djlskj lsajldjsa;l dj;lajd;l aj;ld ja;ljd;laj;dja;l ;alj",
                style = typography.h6,
                maxLines = if (isExpanded) Int.MAX_VALUE else 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.clickable { isExpanded = !isExpanded },
            )
        }
        Text(text = "hello $name~", style = typography.body2, fontWeight = FontWeight.Bold)
        Text(
            buildAnnotatedString {
                withStyle(style = SpanStyle(color = MaterialTheme.colors.primary)) {
                    append("hello")
                }
                append(" $name~")
            }
        )
    }
}

@Composable
fun BodyContent(modifier: Modifier = Modifier) {
    Column(modifier = modifier.padding(8.dp)) {
        SelectionContainer {
            Text(text = "Hi there! This text is selectable")
        }
        Text(text = "Thanks for going through the Layouts codelab")

        val annotatedText = buildAnnotatedString {
            withStyle(style = SpanStyle(color = MaterialTheme.colors.onBackground)) {
                append("hello~ Click ")
            }

            // We attach this *URL* annotation to the following content
            // until `pop()` is called
            pushStringAnnotation(
                tag = "URL",
                annotation = "Https://bing.com"
            )

            withStyle(
                style = SpanStyle(
                    color = Color.Blue,
                    fontWeight = FontWeight.Bold
                )
            ) {
                append("here! ")
            }

            pop()

            withStyle(style = SpanStyle(color = MaterialTheme.colors.onBackground)) {
                append("Ciao!")
            }
        }

        ClickableText(
            text = annotatedText,
            onClick = { offset ->
                // We check if there is an *URL* annotation attached to the text
                // at the clicked position
                annotatedText.getStringAnnotations(tag = "URL", start = offset, end = offset)
                    .firstOrNull()?.let {
                        Log.d("ClickableText", "clicked: ${it.item}")
                    }
            })
    }
}

@Composable
fun SimpleList() {
    val scrollState = rememberScrollState()

    Column(Modifier.verticalScroll(scrollState)) {
        repeat(100) {
            Text(text = "Item #$it")
        }
    }
}

@Composable
fun LazyList() {
    val listSize = 10
    val scrollState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    ConstraintLayout(constraintSet =  decoupledConstraints()) {
        Row(
            modifier = Modifier
                .layoutId("row")
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Button(
                onClick = {
                    coroutineScope.launch {
                        scrollState.animateScrollToItem(0)
                    }
                },
                modifier = Modifier.weight(1f)
            ) {
                Text(text = "Scroll to the top")
            }
            Spacer(modifier = Modifier.width(16.dp))
            Button(
                onClick = {
                    coroutineScope.launch {
                        scrollState.animateScrollToItem(listSize - 1)
                    }
                },
                modifier = Modifier.weight(1f)
            ) {
                Text(text = "Scroll to the end")
            }
        }
        LazyColumn(
            state = scrollState,
            modifier = Modifier
                .layoutId("lazyList")
                .height(290.dp)
        ) {
            items(listSize) {
                PhotographerCard(it)
            }

        }
    }
}

private fun decoupledConstraints(): ConstraintSet {
    return ConstraintSet {
        val row = createRefFor("row")
        val lazyList = createRefFor("lazyList")

        constrain(row) {
            top.linkTo(parent.top)
        }
        constrain(lazyList) {
            linkTo(row.bottom, parent.bottom)
            height = Dimension.preferredWrapContent
        }
    }
}

@Composable
fun PhotographerCard(time: Int, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .padding(8.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(MaterialTheme.colors.surface)
            .clickable { }
            .padding(16.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            modifier = Modifier.size(50.dp),
            shape = CircleShape,
            color = MaterialTheme.colors.onSurface.copy(alpha = 0.2f)
        ) {
           /* Image(
                painter = rememberImagePainter(
                data = */Icons.Filled.Face
                /*),
                contentDescription = "Profile Img",
                modifier = Modifier.size(50.dp)*/
//            )
        }
        Column(
            modifier = Modifier
                .padding(start = 8.dp)
                .align(Alignment.CenterVertically)
        ) {
            Text(text = "Alfred Sisley", fontWeight = FontWeight.Bold)
            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                Text(text = "$time minutes ago", style = typography.body2)
            }
        }
    }
}

@Composable
fun MyOwnColumn(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
)  {
    Layout(
        modifier = modifier,
        content = content
    ) { measurables, constraints ->
        val placeables = measurables.map { measurable ->
            measurable.measure(constraints)
        }

        var yPosition =  0

        layout(constraints.maxWidth, constraints.maxHeight) {
            placeables.forEach { placeable ->
                placeable.placeRelative(x = 0, y = yPosition)

                yPosition += placeable.height
            }
        }
    }
}

@Preview(name = "Light Mode")
@Preview(
    name = "Dark Mode",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@Composable
fun Preview() {
    EspressoDemoTheme() {
//    Greeting("Compose")
//    PhotographerCard()
        HomeScreen(
            MainActions(NavHostController(LocalContext.current))
        ) {}
    }
}
