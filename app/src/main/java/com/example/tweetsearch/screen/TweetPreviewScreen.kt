package com.example.tweetsearch.screen

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.tweetsearch.R
import com.example.tweetsearch.component.generic.HeaderBodyText
import com.example.tweetsearch.ui.theme.DEFAULT_PADDING
import com.example.tweetsearch.ui.theme.buttonRoundCorners
import com.example.tweetsearch.ui.theme.defaultTextModifier
import com.example.tweetsearch.ui.theme.imageRoundCorners
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun TweetPreviewScreen(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    var previewScreenshotModel: String? by rememberSaveable { mutableStateOf(null) }

    LazyColumn(
        modifier
            .fillMaxWidth()
    ) {
        item {
            TweetPictureSelect(modifier) {
                previewScreenshotModel = it
            }
        }
        item {
            TweetPicturePreview(modifier, navController, previewScreenshotModel)
        }
    }
}

@Composable
fun TweetPictureSelect(
    modifier: Modifier = Modifier,
    onChangePreviewScreenshotModel: (String?) -> Unit,
) {
    val selectedFileLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            if (uri != null) {
                onChangePreviewScreenshotModel(uri.toString())
            }
        }
    var screenshotUrl by rememberSaveable { mutableStateOf("") }

    HeaderBodyText(
        defaultTextModifier,
        stringResource(R.string.select_tweet_screenshot_instruction)
    )
    OutlinedButton(
        onClick = {
            selectedFileLauncher.launch("image/*")
        },
        shape = buttonRoundCorners,
        border = BorderStroke(1.dp, MaterialTheme.colors.onBackground),
        modifier = defaultTextModifier,
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = MaterialTheme.colors.onPrimary,
        )
    ) {
        Text(
            stringResource(R.string.local_tweet_screenshot),
            style = MaterialTheme.typography.body1,
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
                .padding(DEFAULT_PADDING, 2.dp)
                .weight(0.70F),
        )
        OutlinedButton(
            onClick = { onChangePreviewScreenshotModel(screenshotUrl) },
            shape = buttonRoundCorners,
            border = BorderStroke(1.dp, MaterialTheme.colors.onBackground),
            modifier = defaultTextModifier
                .weight(0.30F),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = MaterialTheme.colors.onPrimary,
            )
        ) {
            Text(
                stringResource(R.string.submit_url),
                style = MaterialTheme.typography.body1
            )
        }
    }
}

@Composable
fun TweetPicturePreview(
    modifier: Modifier = Modifier,
    navController: NavController,
    previewScreenshotModel: String?,
) {
    var validScreenshot by rememberSaveable { mutableStateOf(false) }
    if (previewScreenshotModel == null) {
        validScreenshot = false
    }

    HeaderBodyText(defaultTextModifier, stringResource(R.string.preview_screenshot))
    AsyncImage(
        model = previewScreenshotModel,
        contentDescription = stringResource(R.string.preview_screenshot),
        modifier = modifier
            .fillMaxWidth()
            .padding(DEFAULT_PADDING)
            .clip(imageRoundCorners),
        contentScale = ContentScale.FillWidth,
        error = painterResource(R.drawable.preview_error),
        fallback = painterResource(R.drawable.preview_placeholder),
        onSuccess = { validScreenshot = true },
        onError = { validScreenshot = false },
    )
    OutlinedButton(
        onClick = {
            navController.navigate(
                "${Screen.TweetInfo.name}/${
                    URLEncoder.encode(
                        previewScreenshotModel,
                        StandardCharsets.UTF_8.toString()
                    )
                }"
            )
        },
        shape = buttonRoundCorners,
        border = BorderStroke(1.dp, MaterialTheme.colors.onBackground),
        modifier = defaultTextModifier,
        colors = ButtonDefaults.outlinedButtonColors(
            backgroundColor = MaterialTheme.colors.secondary,
            contentColor = MaterialTheme.colors.onSecondary,
        ),
        enabled = validScreenshot,
    ) {
        Text(
            stringResource(R.string.confirm_screenshot),
            style = MaterialTheme.typography.body1,
        )
    }
}