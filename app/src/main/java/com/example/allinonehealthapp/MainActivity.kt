package com.example.allinonehealthapp

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.navigation.compose.*
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.material3.*
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.lifecycle.viewmodel.compose.viewModel


import com.example.allinonehealthapp.ui.theme.AllInOneHealthAppTheme
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AllInOneHealthAppTheme {
                MainScreen()
            }
        }
    }
}

data class Message(val author: String, val body: String)


@Composable()
fun Message_card(msg: Message) {
    Row(modifier = Modifier.padding(all = 8.dp)){
        Image(
            painter = painterResource(R.drawable.profile_picture),
            contentDescription = "Contact profile picture",
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .border(1.5.dp, MaterialTheme.colorScheme.primary, CircleShape),
        )
        Spacer(modifier = Modifier.width(8.dp))

        var isExpanded by remember { mutableStateOf(false)}
        val surfaceColor by animateColorAsState(
            if (isExpanded) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
        )
        Column(modifier = Modifier.clickable{ isExpanded = !isExpanded}) {
            Text(
                text = msg.author,
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.titleSmall
            )
            Spacer(modifier = Modifier.height(4.dp))
            Surface(
                shape = MaterialTheme.shapes.medium, shadowElevation = 1.dp,
                color = surfaceColor,
                modifier = Modifier.animateContentSize().padding(1.dp)
            ){
                Text(
                    text = msg.body,
                    modifier = Modifier.padding(all = 4.dp),
                    maxLines = if (isExpanded) Int.MAX_VALUE else 1,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Preview()
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
    name = "Dark Mode"
)
@Composable
fun PreviewMessageCard() {
    AllInOneHealthAppTheme {
        Surface {
            Message_card(
                msg = Message("Lexi", "Take a look at Jetpack Compose, it's great!")
            )
        }
    }
}

@Composable
fun Conversation(messages: List <Message>){
    LazyColumn {
        items(messages){ message ->
            Message_card(message)
        }
    }
}

@Preview()
@Composable
fun PreviewConversation(){
    AllInOneHealthAppTheme{
        Conversation(SampleData.conversationSample)
    }
}

data class NavItem(
    val route: String,
    val label: String,
    val filledIcon: Int,
    val outlinedIcon: Int
)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(){

    val pages = listOf(
        NavItem(Routes.Home, "Home", R.drawable.home_24px_filled, R.drawable.home_24px),
        NavItem(Routes.Diet, "Diet", R.drawable.diet_24px_filled, R.drawable.diet_24px),
        NavItem(Routes.Exercise, "Exercise", R.drawable.exercise_24px_filled, R.drawable.exercise_24px),
        NavItem(Routes.Profile, "Profile", R.drawable.profile_24px_filled, R.drawable.profile_24px)
    )
    val pagerState = rememberPagerState(pageCount = {pages.size})
    val scope = rememberCoroutineScope()

    val haptic = LocalHapticFeedback.current

    LaunchedEffect(pagerState.currentPage) {
        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(pages[pagerState.currentPage].label) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                )
            )

        },
        bottomBar = {
            NavigationBar {
                pages.forEachIndexed { index, item ->
                    val isSelected = pagerState.currentPage == index
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = { scope.launch { pagerState.animateScrollToPage(index) } },
                        label = { Text(item.label) },
                        icon = {
                            Icon(
                                painter = painterResource(if (isSelected) item.filledIcon else item.outlinedIcon),
                                contentDescription = item.label,
                                tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    )
                }
            }
        }
    ){ innerPadding ->
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            // Optional: prevent swiping on specific pages if needed
            userScrollEnabled = true
        ) { pageIndex ->
            // 5. Render the screen based on the current page index
            when (pages[pageIndex].route) {
                Routes.Home -> Conversation(SampleData.conversationSample)
                Routes.Diet-> DietScreen(viewModel = viewModel())
                Routes.Exercise -> ExerciseScreen()
                Routes.Profile -> ProfileScreen()
            }
        }
    }
}
