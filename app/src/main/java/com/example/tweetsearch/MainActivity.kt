package com.example.tweetsearch

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.tweetsearch.generic.AppToolbar
import com.example.tweetsearch.ui.theme.TweetSearchTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TweetSearchTheme {
                TweetSearchApp()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TweetSearchApp() {
    TweetPreviewPage(
        Modifier
    )
}

@Composable
fun TweetPreviewPage(modifier: Modifier = Modifier) {
    val scaffoldState = rememberScaffoldState()
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            AppToolbar(Modifier, scaffoldState = scaffoldState)
        },
        drawerContent = { },
        drawerBackgroundColor = MaterialTheme.colors.background,
        content = {
            TweetPictureSelect(modifier)
        },
    )
}

@Composable
fun TweetPictureSelect(modifier: Modifier = Modifier) {
    var previewScreenshotModel: String? by remember { mutableStateOf(null) }
    val selectedFileLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri ->
            previewScreenshotModel = uri.toString()
        }
    var screenshotUrl by remember { mutableStateOf("") }

    val buttonRoundCorners = RoundedCornerShape(20)
    val defaultModifier = modifier
        .fillMaxWidth()
        .padding(8.dp)

    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState()),
    ) {
        Text(
            stringResource(R.string.select_tweet_screenshot_instruction),
            style = MaterialTheme.typography.subtitle1,
            modifier = defaultModifier,
        )
        Button(
            onClick = {
                selectedFileLauncher.launch("image/*")
            },
            shape = buttonRoundCorners,
            modifier = defaultModifier
                .border(1.dp, MaterialTheme.colors.onBackground, buttonRoundCorners),
        ) {
            Text(
                stringResource(R.string.local_tweet_screenshot),
                style = MaterialTheme.typography.body1
            )
        }
        Row(
            modifier = modifier
        )
        {
            OutlinedTextField(
                value = screenshotUrl,
                onValueChange = { screenshotUrl = it },
                label = { Text(stringResource(R.string.url_tweet_screenshot)) },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    unfocusedBorderColor = MaterialTheme.colors.onBackground,
                    unfocusedLabelColor = MaterialTheme.colors.onBackground,
                ),
                modifier = modifier
                    .fillMaxWidth()
                    .padding(8.dp, 2.dp)
                    .weight(0.74F),
            )
            Button(
                onClick = { previewScreenshotModel = screenshotUrl },
                shape = buttonRoundCorners,
                modifier = defaultModifier
                    .border(1.dp, MaterialTheme.colors.onBackground, buttonRoundCorners)
                    .weight(0.25F),
            ) {
                Text(
                    stringResource(R.string.submit_url),
                    style = MaterialTheme.typography.body1
                )
            }
        }
        TweetPicturePreview(modifier, previewScreenshotModel)
    }

}

@Composable
fun TweetPicturePreview(
    modifier: Modifier = Modifier,
    previewScreenshotModel: String?,
) {
    val imageRoundCorners = RoundedCornerShape(5)
    val defaultModifier = modifier
        .fillMaxWidth()
        .padding(8.dp)

    Text(
        stringResource(R.string.preview_screenshot),
        style = MaterialTheme.typography.subtitle1,
        modifier = defaultModifier,
    )
    AsyncImage(
        model = previewScreenshotModel,
        contentDescription = stringResource(R.string.preview_screenshot),
        modifier = defaultModifier
            .clip(imageRoundCorners)
            .border(1.dp, MaterialTheme.colors.onBackground),
        contentScale = ContentScale.FillWidth,
        error = painterResource(R.drawable.preview_error),
        fallback = painterResource(R.drawable.preview_placeholder),
    )
}