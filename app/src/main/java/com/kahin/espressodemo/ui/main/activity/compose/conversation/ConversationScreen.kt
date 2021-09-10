package com.kahin.espressodemo.ui.main.activity.compose.conversation

import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.LastBaseline
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.navigationBarsWithImePadding
import com.google.accompanist.insets.rememberInsetsPaddingValues
import com.google.accompanist.insets.statusBarsPadding
import com.kahin.espressodemo.R
import com.kahin.espressodemo.ui.main.activity.compose.MainActions
import com.kahin.espressodemo.ui.main.activity.compose.ui.theme.EspressoDemoTheme
import com.kahin.espressodemo.ui.main.activity.compose.ui.theme.White200
import com.kahin.espressodemo.ui.main.activity.compose.ui.theme.elevatedSurface
import kotlinx.coroutines.launch

@Composable
fun ConversationScreen(
    onNavigationEvent: MainActions,
    scaffoldState: ScaffoldState = rememberScaffoldState()
) {

    ConversationContent(uiState = ConversationUiState(
        initialMessages = listOf(
            Message(
                "me",
                "Check it out!",
                "8:07 PM"
            ),
        ),
        channelName = "#composers",
        channelMembers = 42
    ),
        navigateToProfile = { })
}

@Composable
fun ConversationContent(
    uiState: ConversationUiState,
    navigateToProfile: (String) -> Unit = {},
    modifier: Modifier = Modifier,
    onNavIconPressed: () -> Unit = {}
) {
    val authorMe = stringResource(id = R.string.author_me)
    val timeNow = stringResource(id = R.string.now)

    val scrollState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    Surface(modifier = modifier) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(Modifier.fillMaxSize()) {
                Messages(
                    messages = uiState.messages,
                    navigateToProfile = navigateToProfile,
                    modifier = Modifier.weight(1f),
                    scrollState = scrollState
                )
                UserInput(
                    onMessageSent = { content ->
                        uiState.addMessage(
                            Message(authorMe, content, timeNow)
                        )
                    },
                    resetScroll = {
                        scope.launch {
                            scrollState.scrollToItem(0)
                        }
                    },
                    // Use navigationBarsWithImePadding(), to move the input panel above both the
                    // navigation bar, and on-screen keyboard (IME)
                    modifier = Modifier.navigationBarsWithImePadding()
                )
            }
            // Channel name bar floats above the messages
            ChannelNameBar(
                channelName = uiState.channelName,
                channelMembers = uiState.channelMembers,
                onNavIconPressed = onNavIconPressed,
                // Use statusBarsPadding() to move the app bar content below the status bar
                modifier = Modifier.statusBarsPadding()
            )
        }
    }
}

@Composable
fun ChannelNameBar(
    channelName: String,
    channelMembers: Int,
    modifier: Modifier = Modifier,
    onNavIconPressed: () -> Unit = { }
) {
    var functionNotAvailablePopupShown by remember { mutableStateOf(false) }
    if (functionNotAvailablePopupShown) {
        FunctionalityNotAvailablePopup {
            functionNotAvailablePopupShown = false
        }
    }
    ChatAppBar(
        modifier = modifier,
        onNavIconPressed = onNavIconPressed,
        title = {
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Channel name
                Text(
                    text = channelName,
                    style = MaterialTheme.typography.subtitle1
                )
                // Number of members
                CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                    Text(
                        text = stringResource(id = R.string.members, channelMembers),
                        style = MaterialTheme.typography.caption
                    )
                }
            }
        },
        actions = {
            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                // Search icon
                Icon(
                    imageVector = Icons.Outlined.Search,
                    modifier = Modifier
                        .clickable { functionNotAvailablePopupShown = true }
                        .padding(horizontal = 12.dp, vertical = 16.dp)
                        .height(24.dp),
                    contentDescription = stringResource(id = R.string.search)
                )
                // Info icon
                Icon(
                    imageVector = Icons.Outlined.Info,
                    modifier = Modifier
                        .clickable { functionNotAvailablePopupShown = true }
                        .padding(horizontal = 12.dp, vertical = 16.dp)
                        .height(24.dp),
                    contentDescription = stringResource(id = R.string.info))
            }
        }
    )
}

const val ConversationTestTag = "ConversationTestTag"

@Composable
fun Messages(
    messages: List<Message>,
    navigateToProfile: (String) -> Unit,
    scrollState: LazyListState,
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()
    Box(modifier) {
        val authorME = stringResource(id = R.string.author_me)
        LazyColumn(
            reverseLayout = true,
            state = scrollState,
            // Add content padding so that the content can be scrolled (y-axis)
            // below the status bar + app bar
            // TODO: Get height from somewhere
            contentPadding = rememberInsetsPaddingValues(
                insets = LocalWindowInsets.current.statusBars,
                additionalTop = 90.dp
            ),
            modifier = Modifier
                .testTag(ConversationTestTag)
                .fillMaxSize()
        ) {
            for (index in messages.indices) {
                val prevAuthor = messages.getOrNull(index - 1)?.author
                val nextAuthor = messages.getOrNull(index + 1)?.author
                val content = messages[index]
                val isFirstMessageByAuthor = prevAuthor != content.author
                val isLastMessageByAuthor = nextAuthor != content.author

                // Hardcode day dividers for simplicity
                if (index == messages.size - 1) {
                    item {
                        DayHeader(dayString = "30 Aug")
                    }
                } else if (index == 2) {
                    item {
                        DayHeader(dayString = "Today")
                    }
                }

                item {
                    Message(
                        onAuthorClick = { name -> navigateToProfile(name) },
                        msg = content,
                        isUserMe = content.author == authorME,
                        isFirstMessageByAuthor = isFirstMessageByAuthor,
                        isLastMessageByAuthor = isLastMessageByAuthor
                    )
                }
            }
        }
        // Jump to bottom button shows up when user scrolls past a threshold.
        // Convert to pixels:
        val jumpThreshold = with(LocalDensity.current) {
            JumpToBottomThreshold.toPx()

        }

        // Show the button if the first visible item is not the first one or if the offset is
        // greater than the threshold.
        val jumpTOBottomButtonEnabled by remember {
            derivedStateOf {
                scrollState.firstVisibleItemIndex != 0 ||
                        scrollState.firstVisibleItemScrollOffset > jumpThreshold

            }
        }

        JumpToBottom(
            // Only show if the scroller is not at the bottom
            enabled = jumpTOBottomButtonEnabled,
            onClicked = {
                scope.launch {
                    scrollState.animateScrollToItem(0)
                }
            },
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
fun Message(
    onAuthorClick: (String) -> Unit,
    msg: Message,
    isUserMe: Boolean,
    isFirstMessageByAuthor: Boolean,
    isLastMessageByAuthor: Boolean
) {
    val borderColor = if (isUserMe) {
        MaterialTheme.colors.primary
    } else {
        MaterialTheme.colors.secondary
    }

    val spaceBetweenAuthors = if (isLastMessageByAuthor) Modifier.padding(top = 8.dp) else Modifier
    Row(modifier = spaceBetweenAuthors) {
        if (isLastMessageByAuthor) {
            // Avatar
            Image(
                modifier = Modifier
                    .clickable(onClick = { onAuthorClick(msg.author) })
                    .padding(horizontal = 16.dp)
                    .size(42.dp)
                    .border(1.5.dp, borderColor, CircleShape)
                    .border(3.dp, MaterialTheme.colors.surface, CircleShape)
                    .clip(CircleShape)
                    .align(Alignment.Top),
                painter = painterResource(id = msg.authorImage),
                contentScale = ContentScale.Crop,
                contentDescription = null,
            )
        } else {
            // Space under avatar
            Spacer(modifier = Modifier.width(74.dp))
        }
        AuthorAndTextMessage(
            msg = msg,
            isFirstMessageByAuthor = isFirstMessageByAuthor,
            isLastMessageByAuthor = isLastMessageByAuthor,
            authorClick = onAuthorClick,
            modifier = Modifier
                .padding(end = 16.dp)
                .weight(1f)
        )
    }
}

@Composable
fun AuthorAndTextMessage(
    msg: Message,
    isFirstMessageByAuthor: Boolean,
    isLastMessageByAuthor: Boolean,
    authorClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        if (isLastMessageByAuthor) {
            AuthorNameTimestamp(msg = msg)
        }
        ChatItemBubble(msg, isFirstMessageByAuthor, authorClicked = authorClick)
        if (isFirstMessageByAuthor) {
            // Last bubble before next author
            Spacer(modifier = Modifier.height(8.dp))
        } else {
            // Between bubbles
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}

@Composable
private fun AuthorNameTimestamp(msg: Message) {
    Row(modifier = Modifier.semantics(mergeDescendants = true) {}) {
        Text(
            text = msg.author,
            style = MaterialTheme.typography.subtitle1,
            modifier = Modifier
                .alignBy(LastBaseline)
                .paddingFrom(LastBaseline, after = 8.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
            Text(
                text = msg.timestamp,
                style = MaterialTheme.typography.caption,
                modifier = Modifier.alignBy(LastBaseline)
            )
        }
    }
}

private val ChatBubbleShape = RoundedCornerShape(0.dp, 8.dp, 8.dp, 0.dp)
private val LastChatBubbleShape = RoundedCornerShape(0.dp, 8.dp, 8.dp, 8.dp)

@Composable
fun DayHeader(dayString: String) {
    Row(
        modifier = Modifier
            .padding(vertical = 8.dp, horizontal = 16.dp)
            .height(16.dp)
    ) {
        DayHeaderLine()
        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
            Text(
                text = dayString,
                modifier = Modifier.padding(horizontal = 16.dp),
                style = MaterialTheme.typography.overline
            )
        }
        DayHeaderLine()
    }
}

@Composable
private fun RowScope.DayHeaderLine() {
    Divider(
        modifier = Modifier
            .weight(1f)
            .align(Alignment.CenterVertically),
        color = MaterialTheme.colors.onSurface.copy(alpha = 0.12f)
    )
}

@Composable
fun ChatItemBubble(
    message: Message,
    lastMessageByAuthor: Boolean,
    authorClicked: (String) -> Unit
) {
    val backgroundBubbleColor =
        if (MaterialTheme.colors.isLight) {
            White200
        } else {
            MaterialTheme.colors.elevatedSurface(2.dp)
        }

    val bubbleShape = if (lastMessageByAuthor) LastChatBubbleShape else ChatBubbleShape
    Column {
        Surface(color = backgroundBubbleColor, shape = bubbleShape) {
            ClickableMessage(
                message = message,
                authorClicked = authorClicked
            )
        }

        message.image?.let {
            Spacer(modifier = Modifier.height(4.dp))
            Surface(color = backgroundBubbleColor, shape = bubbleShape) {
                Image(
                    painter = painterResource(id = it),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(160.dp),
                    contentDescription = stringResource(id = R.string.attached_image)
                )
            }
        }
    }
}

@Composable
fun ClickableMessage(message: Message, authorClicked: (String) -> Unit) {
    val urlHandler = LocalUriHandler.current

    val styledMessage = messageFormatter(text = message.content)

    ClickableText(
        text = styledMessage,
        style = MaterialTheme.typography.body1.copy(color = LocalContentColor.current),
        modifier = Modifier.padding(8.dp),
        onClick = {
            styledMessage
                .getStringAnnotations(start = it, end = it)
                .firstOrNull()
                ?.let { annotation ->
                    when (annotation.tag) {
                        SymbolAnnotationType.LINK.name -> urlHandler.openUri(annotation.item)
                        SymbolAnnotationType.PERSON.name -> authorClicked(annotation.item)
                        else -> Unit
                    }
                }
        }
    )
}

@Preview
@Composable
fun ConversationPreview() {
    EspressoDemoTheme() {
        ConversationContent(
            uiState = ConversationUiState(
                initialMessages = listOf(
                    Message(
                        "me",
                        "Check it out!",
                        "8:07 PM"
                    ),
                ),
                channelName = "#composers",
                channelMembers = 42
            ),
            navigateToProfile = { }
        )
    }
}

@Preview
@Composable
fun channelBarPrev() {
    EspressoDemoTheme {
        ChannelNameBar(channelName = "composers", channelMembers = 52)
    }
}

@Preview
@Composable
fun DayHeaderPrev() {
    DayHeader("Aug 6")
}

private val JumpToBottomThreshold = 56.dp

private fun ScrollState.atBottom(): Boolean = value == 0