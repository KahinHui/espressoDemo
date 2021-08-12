package com.kahin.espressodemo.ui.main.activity.compose

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kahin.espressodemo.R
import com.kahin.espressodemo.ui.main.activity.compose.ui.theme.EspressoDemoTheme
import com.kahin.espressodemo.ui.main.activity.compose.ui.theme.User
import kotlinx.coroutines.launch
import kotlin.math.max

@Composable
fun LogInScreen(
    onNavigationEvent: MainActions,
    scaffoldState: ScaffoldState = rememberScaffoldState()
) {
    val context = LocalContext.current

    val viewModel: LogInViewModel = viewModel()
    val result: User by viewModel.userData.observeAsState(User("", ""))

    viewModel.logOut()

    BodyContent(
        context,
        result,
        iconClick = {},
        toHome = onNavigationEvent.toHome,
        logIn = { name, pwd -> viewModel.logIn(User(name, pwd)) },
        signUp = {},
        scaffoldState = scaffoldState
    )
}

val topics = listOf(
    "Arts & Crafts", "Beauty", "Books", "Business", "Comics", "Culinary",
    "Design", "Fashion", "Film", "History", "Maths", "Music", "People", "Philosophy",
    "Religion", "Social sciences", "Technology", "TV", "Writing"
)

@Composable
fun BodyContent(
    context: Context,
    result: User,
    iconClick: () -> Unit,
    toHome: () -> Unit,
    logIn: (String, String) -> Unit,
    signUp: () -> Unit,
    scaffoldState: ScaffoldState
) {
    val errorMsg = stringResource(id = R.string.user_or_pwd_incorrect)
    val snackbarActionLabel = stringResource(id = R.string.dismiss)
    val scope = rememberCoroutineScope()

    result.isSuccess?.let {
        if (!it) {
            // `LaunchedEffect` will cancel and re-launch if
            // `scaffoldState.snackbarHostState` changes
//            LaunchedEffect(scaffoldState.snackbarHostState) {
            scope.launch {
                scaffoldState.snackbarHostState.showSnackbar(
                    message = errorMsg,
                    actionLabel = snackbarActionLabel
                )
            }
        }
    }

    // A surface container using the 'background' color from the theme
    Surface(
        color = MaterialTheme.colors.surface,
        elevation = 2.dp
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(text = "LayoutsCodelab")
                    },
                    actions = {
                        IconButton(onClick = iconClick) {
                            Icon(Icons.Filled.Favorite, contentDescription = null)
                        }
                    }
                )
            }
        ) { innerPadding ->
            if (result.isLogIn) {
                toHome()
            } else {
                ConstraintLayout {
                    // Create references for the composables to constrain
                    val (row, button1, button2, text, userTextField, pwdTextField, searchTextField) = createRefs()

                    Row(
                        // Assign reference "row" to the Row composable
                        // and constrain it to the top of the ConstraintLayout
                        modifier = Modifier
                            .padding(innerPadding)
                            .constrainAs(row) {
                                top.linkTo(parent.top, margin = 16.dp)
                            }
                            .padding(16.dp)
                            .height(150.dp)
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState())
                    ) {
                        StaggeredGrid {
                            topics.forEach {
                                Chip(modifier = Modifier.padding(8.dp), text = it)
                            }
                        }
                    }

                    var userText by rememberSaveable { mutableStateOf("") }
                    var pwdText by remember { mutableStateOf("") }

                    TextField(
                        modifier = Modifier
                            .constrainAs(userTextField) {
                                top.linkTo(row.bottom, margin = 16.dp)
                                linkTo(parent.start, parent.end)
                            },
                        value = userText,
                        onValueChange = { userText = it },
                        label = { Text(text = "User") },
                        singleLine = true
                    )

                    OutlinedTextField(
                        modifier = Modifier
                            .constrainAs(pwdTextField) {
                                top.linkTo(userTextField.bottom)
                                linkTo(parent.start, parent.end)
                            },
                        value = pwdText,
                        onValueChange = { pwdText = it },
                        label = { Text(text = "Password") },
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                    )

                    Button(
                        onClick = {
                            logIn(userText, pwdText)
                        },
                        // Assign reference "button" to the Button composable
                        // and constrain it to the bottom of the Row composable
                        modifier = Modifier.constrainAs(button1) {
                            top.linkTo(pwdTextField.bottom, margin = 16.dp)
                            linkTo(parent.start, parent.end)
                        }
                    ) {
                        Text(text = stringResource(id = R.string.log_in))
                    }

                    Text(
                        text = "Or",
                        modifier = Modifier.constrainAs(text) {
                            top.linkTo(button1.bottom, margin = 16.dp)
                            centerAround(button1.end)
                        }
                    )

                    val barrier = createEndBarrier(button1, text)
                    Button(
                        onClick = signUp,
                        modifier = Modifier.constrainAs(button2) {
                            top.linkTo(text.bottom, margin = 16.dp)
                            start.linkTo(barrier)
                        }
                    ) {
                        Text(text = stringResource(id = R.string.sign_up))
                    }

                    var searchText by remember { mutableStateOf("") }
                    TextField(
                        modifier = Modifier.constrainAs(searchTextField) {
                            top.linkTo(button2.bottom, margin = 16.dp)
                            linkTo(parent.start, parent.end)
                        },
                        value = searchText,
                        onValueChange = { searchText = it },
                        label = { Text(text = "Search") },
                        shape = RoundedCornerShape(16.dp),
                        leadingIcon = @Composable {
                            Image(
                                imageVector = Icons.Filled.Search,
                                contentDescription = null,
                                modifier = Modifier.clickable {
                                    Toast.makeText(context, "search $searchText", Toast.LENGTH_LONG)
                                        .show()
                                }
                            )
                        },
                        trailingIcon = @Composable {
                            Image(
                                imageVector = Icons.Filled.Clear,
                                contentDescription = null,
                                modifier = Modifier.clickable {
                                    searchText = ""
                                }
                            )
                        },
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                        keyboardActions = KeyboardActions(onSearch = {
                            Toast.makeText(context, "search $searchText", Toast.LENGTH_LONG).show()
                        }),
                        singleLine = true,
                        colors = TextFieldDefaults.textFieldColors(
                            focusedIndicatorColor = MaterialTheme.colors.primary,
                            unfocusedIndicatorColor = Color.Transparent,
                            errorIndicatorColor = Color.Red,
                            disabledIndicatorColor = Color.Gray
                        ),
                        isError = false,
                        placeholder = @Composable { Text(text = "Text something") }
                    )
                }
            }
        }
        
        Box(modifier = Modifier.fillMaxSize()) {
            ErrorSnackbar(
                snackbarHostState = scaffoldState.snackbarHostState,
                onDismiss = { scaffoldState.snackbarHostState.currentSnackbarData?.dismiss() },
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }
}

@Composable
fun StaggeredGrid(
    modifier: Modifier = Modifier,
    rows: Int = 3,
    content: @Composable () -> Unit
) {
    Layout(
        modifier = modifier,
        content = content
    ) { measurables, constraints ->
        // Keep track of the width of each row
        val rowWidths = IntArray(rows) { 0 }

        // Keep track of the max height of each row
        val rowHeights = IntArray(rows) { 0 }

        // Don't constrain child views further, measure them with given constraints
        // List of measured children
        val placeables = measurables.mapIndexed { index, measurable ->
            // Measure each child
            val placeable = measurable.measure(constraints)

            // Track the width and max height of each row
            val row = index % rows
            rowWidths[row] += placeable.width
            rowHeights[row] = max(rowHeights[row], placeable.height)

            placeable
        }

        // Grid's width is the widest row
        val width = rowWidths.maxOrNull()
            ?.coerceIn(constraints.minWidth.rangeTo(constraints.maxWidth)) ?: constraints.minWidth

        // Grid's height is the sum of the tallest element of each row
        // coerced to the height constraints
        val height = rowHeights.sumOf { it }
            .coerceIn(constraints.minHeight.rangeTo(constraints.maxHeight))

        // Y of each row, based on the height accumulation of previous rows
        val rowY = IntArray(rows) { 0 }
        for (i in 1 until rows) {
            rowY[i] = rowY[i - 1] + rowHeights[i - 1]
        }

        // Set the size of the parent layout
        layout(width, height) {
            // x cord we have placed up to, per row
            val rowX = IntArray(rows) { 0 }

            placeables.forEachIndexed { index, placeable ->
                val row = index % rows
                placeable.placeRelative(
                    x = rowX[row],
                    y = rowY[row]
                )
                rowX[row] += placeable.width
            }
        }
    }
}

@Composable
fun Chip(modifier: Modifier = Modifier, text: String) {
    Card(
        modifier = modifier,
        border = BorderStroke(color = MaterialTheme.colors.secondaryVariant, width = Dp.Hairline),
        shape = RoundedCornerShape(8.dp),
        backgroundColor = MaterialTheme.colors.surface
    ) {
        Row(
            modifier = Modifier.padding(start = 8.dp, top = 4.dp, end = 8.dp, bottom = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(16.dp, 16.dp)
                    .background(color = MaterialTheme.colors.secondary)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = text)
        }
    }
}

@Composable
fun ErrorSnackbar(
    snackbarHostState: SnackbarHostState,
    modifier: Modifier,
    onDismiss: () -> Unit = {}
) {
    SnackbarHost(
        hostState = snackbarHostState,
        snackbar = { data ->
            Snackbar(
                modifier = Modifier.padding(16.dp),
                content = {
                    Text(
                        text = data.message,
                        style = MaterialTheme.typography.body2
                    )
                },
                action = {
                    data.actionLabel?.let {
                        TextButton(onClick = onDismiss) {
                            Text(
                                text = stringResource(id = R.string.dismiss),
                                color = MaterialTheme.colors.primaryVariant
                            )
                        }

                    }
                }
            )
        },
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(Alignment.Bottom)
    )
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    EspressoDemoTheme {
        BodyContent(
            LocalContext.current,
            User("", ""),
            {},
            {},
            { _, _ -> },
            {},
            rememberScaffoldState()
        )
    }
}