package com.kahin.espressodemo.ui.main.activity.compose.profile

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Chat
import androidx.compose.material.icons.outlined.Create
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.insets.statusBarsPadding
import com.kahin.espressodemo.R
import com.kahin.espressodemo.ui.main.activity.compose.conversation.ChatAppBar
import com.kahin.espressodemo.ui.main.activity.compose.conversation.FunctionalityNotAvailablePopup
import com.kahin.espressodemo.ui.main.activity.compose.ui.theme.EspressoDemoTheme

@Composable
fun  ProfileScreen(
    userId: String,
    onNavIconPressed: () -> Unit = {}
) {
    val viewModel: ProfileViewModel = viewModel()
    viewModel.setUserId(userId)

    var functionalityNotAvailablePopupShown by remember { mutableStateOf(false) }
    if (functionalityNotAvailablePopupShown) {
        FunctionalityNotAvailablePopup {
            functionalityNotAvailablePopupShown = false
        }
    }

    val scrollState = rememberScrollState()

    val userData by viewModel.userData.observeAsState()

    if (userData == null) {
        ProfileError()
    } else {
        Column(modifier = Modifier.fillMaxSize()) {
            ChatAppBar(
                // Use statusBarsPadding() to move the app bar content below the status bar
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding(),
                onNavIconPressed = onNavIconPressed,
                title = { },
                actions = {
                    CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                        // More icon
                        Icon(
                            imageVector = Icons.Outlined.MoreVert,
                            modifier = Modifier
                                .clickable { functionalityNotAvailablePopupShown = true }
                                .padding(horizontal = 12.dp, vertical = 16.dp)
                                .height(24.dp),
                            contentDescription = stringResource(id = R.string.more_options)
                        )
                    }
                }
            )
            BoxWithConstraints(modifier = Modifier.weight(1f)) {
                Surface {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(scrollState)
                    ) {
                        ProfileHeader(
                            scrollState = scrollState,
                            data = userData!!,
                            containerHeight = this@BoxWithConstraints.maxHeight
                        )
                        UserInfoFields(
                            userData = userData!!,
                            containerHeight = this@BoxWithConstraints.maxHeight
                        )
                    }
                }
                ProfileFab(
                    extended = scrollState.value == 0,
                    userIsMe = userData!!.isMe(),
                    modifier = Modifier.align(Alignment.BottomEnd),
                ) {
                    functionalityNotAvailablePopupShown = true
                }
            }
        }
    }
}

@Composable
private fun UserInfoFields(userData: ProfileScreenState, containerHeight: Dp) {
    Column {
        Spacer(modifier = Modifier.height(8.dp))

        NameAndPosition(userData = userData)

        ProfileProperty(label = stringResource(R.string.display_name), value = userData.displayName)

        ProfileProperty(label = stringResource(R.string.status), value = userData.status)

        ProfileProperty(label = stringResource(R.string.twitter), value = userData.twitter, isLink = true)

        userData.timeZone?.let {
            ProfileProperty(label = stringResource(R.string.timezone), value = userData.timeZone)
        }

        // Add a spacer that always shows part (320.dp) of the fields list regardless of the device,
        // in order to always leave some content at the top.
        Spacer(Modifier.height((containerHeight - 320.dp).coerceAtLeast(0.dp)))
    }
}

@Composable
private fun NameAndPosition(userData: ProfileScreenState) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Name(userData = userData, modifier = Modifier.baselineHeight(32.dp))
        Position(
            userData = userData,
            modifier = Modifier
                .padding(bottom = 20.dp)
                .baselineHeight(24.dp)
        )
    }
}

@Composable
private fun Name(userData: ProfileScreenState, modifier: Modifier = Modifier) {
    Text(
        text = userData.name,
        modifier = modifier,
        style = MaterialTheme.typography.h5
    )
}

@Composable
private fun Position(userData: ProfileScreenState, modifier: Modifier = Modifier) {
    CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
        Text(
            text = userData.position,
            modifier = modifier,
            style = MaterialTheme.typography.body1
        )
    }
}

@Composable
private fun ProfileHeader(
    scrollState: ScrollState,
    data: ProfileScreenState,
    containerHeight: Dp
) {
    val offset = (scrollState.value / 2)
    val offsetDp = with(LocalDensity.current) { offset.toDp() }

    data.photo?.let {
        Image(
            modifier = Modifier
                .heightIn(max = containerHeight / 2)
                .fillMaxWidth()
                .padding(top = offsetDp),
            painter = painterResource(id = it),
            contentScale = ContentScale.Crop,
            contentDescription = null
        )
    }
}

@Composable
fun ProfileProperty(label: String, value: String, isLink: Boolean = false) {
    Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp)) {
        Divider()
        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
            Text(
                text = label,
                modifier = Modifier.baselineHeight(24.dp),
                style = MaterialTheme.typography.caption
            )
        }
        val style = if (isLink){
            MaterialTheme.typography.body1.copy(color = MaterialTheme.colors.primary)
        } else {
            MaterialTheme.typography.body1
        }
        Text(
            text = value,
            modifier = Modifier.baselineHeight(24.dp),
            style = style
        )
    }
}

@Composable
fun ProfileError() {
    Text(text = stringResource(id = R.string.profile_error))
}

@Composable
fun ProfileFab(
    extended: Boolean,
    userIsMe: Boolean,
    modifier: Modifier = Modifier,
    onFabClicked: () -> Unit = {}
) {
    key(userIsMe) { // Prevent multiple invocations to execute during composition
        FloatingActionButton(
            onClick = onFabClicked,
            modifier = modifier
                .padding(16.dp)
                .navigationBarsPadding()
                .height(48.dp)
                .widthIn(min = 48.dp),
            backgroundColor = MaterialTheme.colors.primary,
            contentColor = MaterialTheme.colors.onPrimary
        ) {
            AnimatingFabContent(
                icon = {
                    Icon(
                        imageVector = if (userIsMe) Icons.Outlined.Create else Icons.Outlined.Chat,
                        contentDescription = stringResource(
                            if (userIsMe) R.string.edit_profile else R.string.message
                        )
                    )
                },
                text = {
                    Text(
                        text = stringResource(
                            id = if (userIsMe) R.string.edit_profile else R.string.message
                        ),
                    )
                },
                extended = extended
            )
        }
    }
}


/**
 * Example colleague profile
 */
val colleagueProfile = ProfileScreenState(
    userId = "12345",
    photo = R.drawable.someone_else,
    name = "Taylor Brooks",
    status = "Away",
    displayName = "taylor",
    position = "Senior Android Dev at Openlane",
    twitter = "twitter.com/taylorbrookscodes",
    timeZone = "12:25 AM local time (Eastern Daylight Time)",
    commonChannels = "2"
)

/**
 * Example "me" profile.
 */
val meProfile = ProfileScreenState(
    userId = "me",
    photo = R.drawable.ali,
    name = "Ali Conors",
    status = "Online",
    displayName = "aliconors",
    position = "Senior Android Dev at Yearin\nGoogle Developer Expert",
    twitter = "twitter.com/aliconors",
    timeZone = "In your timezone",
    commonChannels = null
)

@Preview(widthDp = 640, heightDp = 360)
@Composable
fun ConvPreviewLandscapeMeDefault() {
    ProvideWindowInsets(consumeWindowInsets = false) {
        EspressoDemoTheme {
            ProfileScreen(meProfile.userId)
        }
    }
}

@Preview(widthDp = 360, heightDp = 480)
@Composable
fun ConvPreviewPortraitMeDefault() {
    ProvideWindowInsets(consumeWindowInsets = false) {
        EspressoDemoTheme {
            ProfileScreen(meProfile.userId)
        }
    }
}

@Preview(widthDp = 360, heightDp = 480)
@Composable
fun ConvPreviewPortraitOtherDefault() {
    ProvideWindowInsets(consumeWindowInsets = false) {
        EspressoDemoTheme {
            ProfileScreen(colleagueProfile.userId)
        }
    }
}

@Preview
@Composable
fun ProfileFabPreview() {
    ProvideWindowInsets(consumeWindowInsets = false) {
        EspressoDemoTheme {
            ProfileFab(extended = true, userIsMe = false)
        }
    }
}
