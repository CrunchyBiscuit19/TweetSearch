package com.example.tweetsearch.screen

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.icu.text.CompactDecimalFormat
import android.icu.util.ULocale
import android.net.Uri
import android.webkit.URLUtil
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Comment
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Repeat
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.tweetsearch.R
import com.example.tweetsearch.component.generic.CardHeader
import com.example.tweetsearch.component.generic.ErrorBodyText
import com.example.tweetsearch.component.generic.ExpandableCard
import com.example.tweetsearch.data.rotation.RotationAngles
import com.example.tweetsearch.data.search.SearchDataHelper
import com.example.tweetsearch.data.search.TweetMetricUI
import com.example.tweetsearch.data.search.TwitterApiViewModel
import com.example.tweetsearch.data.search.TwitterApiViewModelFactory
import com.example.tweetsearch.ui.theme.DEFAULT_PADDING
import com.example.tweetsearch.ui.theme.DEFAULT_TEXT_MODIFIER
import com.example.tweetsearch.ui.theme.IMAGE_ROUND_CORNERS
import com.example.tweetsearch.ui.theme.Shapes
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import kotlinx.coroutines.launch
import me.xdrop.fuzzywuzzy.FuzzySearch
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Collections.emptyList

const val TWEET_DATETIME_FORMAT = "dd MMM yyyy HH:mm"

@Composable
fun TweetAnalysisScreen(modifier: Modifier = Modifier, screenshotModel: String?) {
    LazyColumn(
        modifier
            .fillMaxWidth()
    ) {
        item {
            ScreenshotImage(modifier, screenshotModel)
        }
        if (screenshotModel != null) {
            item {
                ImageOCR(modifier, screenshotModel)
            }
        } else {
            item {
                ErrorBodyText(DEFAULT_TEXT_MODIFIER, stringResource(R.string.image_not_found_error))
            }
        }
    }
}

@Composable
fun ScreenshotImage(modifier: Modifier = Modifier, screenshotModel: String?) {
    var screenshotImageFold by rememberSaveable { mutableStateOf(false) }
    var screenshotImageArrow by rememberSaveable { mutableStateOf(RotationAngles.DOWN) }
    screenshotImageArrow = when (screenshotImageFold) {
        true -> RotationAngles.UP
        false -> RotationAngles.DOWN
    }

    ExpandableCard(
        modifier = DEFAULT_TEXT_MODIFIER,
        shape = Shapes.medium,
        backgroundColor = MaterialTheme.colors.background,
        elevation = 4.dp,
        folded = screenshotImageFold,
        onClick = { screenshotImageFold = !screenshotImageFold },
        header = {
            CardHeader(
                modifier,
                stringResource(R.string.screenshot_selected_card_title),
                screenshotImageArrow
            )
        },
    ) {
        AsyncImage(
            model = screenshotModel,
            contentDescription = stringResource(R.string.confirmed_screenshot_model),
            modifier = modifier
                .fillMaxWidth()
                .padding(DEFAULT_PADDING)
                .clip(IMAGE_ROUND_CORNERS),
            contentScale = ContentScale.FillWidth,
            error = painterResource(R.drawable.preview_error),
            fallback = painterResource(R.drawable.preview_placeholder),
        )
    }
}

@Composable
fun LoadingCircle(modifier: Modifier = Modifier) {
    Row(
        modifier
            .fillMaxWidth()
            .height(150.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun ImageOCR(modifier: Modifier = Modifier, screenshotModel: String) {
    var fieldsDetectedFold by rememberSaveable { mutableStateOf(false) }
    var fieldsDetectedArrow by rememberSaveable { mutableStateOf(RotationAngles.DOWN) }
    fieldsDetectedArrow = when (fieldsDetectedFold) {
        true -> RotationAngles.UP
        false -> RotationAngles.DOWN
    }

    var tweetMatchesFold by rememberSaveable { mutableStateOf(false) }
    var tweetMatchesArrow by rememberSaveable { mutableStateOf(RotationAngles.DOWN) }
    tweetMatchesArrow = when (tweetMatchesFold) {
        true -> RotationAngles.UP
        false -> RotationAngles.DOWN
    }

    val textRecognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
    var screenshot: InputImage
    var detectedText: String? by rememberSaveable { mutableStateOf(null) }
    var detectionError by rememberSaveable { mutableStateOf(false) }

    if (URLUtil.isNetworkUrl(screenshotModel)) {
        val imageLoader = ImageLoader(LocalContext.current)
        val imageRequest = ImageRequest.Builder(LocalContext.current)
            .data(screenshotModel)
            .target { result ->
                // Must convert HARDWARE config to ARGB_8888 via copy method
                val bitmap =
                    (result as BitmapDrawable).bitmap.copy(Bitmap.Config.ARGB_8888, false)
                screenshot = InputImage.fromBitmap(bitmap, 0)
                textRecognizer.process(screenshot)
                    .addOnSuccessListener { visionText ->
                        detectedText = visionText.text
                    }
                    .addOnFailureListener {
                        detectionError = true
                    }
            }
            .build()
        imageLoader.enqueue(imageRequest)
    } else {
        screenshot = InputImage.fromFilePath(LocalContext.current, Uri.parse(screenshotModel))
        textRecognizer.process(screenshot)
            .addOnSuccessListener { visionText ->
                detectedText = visionText.text
            }
            .addOnFailureListener {
                detectionError = true
            }
    }

    ExpandableCard(
        modifier = DEFAULT_TEXT_MODIFIER,
        shape = Shapes.medium,
        backgroundColor = MaterialTheme.colors.background,
        elevation = 4.dp,
        folded = fieldsDetectedFold,
        onClick = { fieldsDetectedFold = !fieldsDetectedFold },
        header = {
            CardHeader(
                modifier,
                stringResource(R.string.fields_detection_card_title),
                fieldsDetectedArrow
            )
        },
    ) {
        if (detectedText == null) {
            LoadingCircle()
        } else if (detectedText != "") {
            TweetDetails(modifier, detectedText!!)
        } else if (detectedText == "") {
            ErrorBodyText(DEFAULT_TEXT_MODIFIER, stringResource(R.string.no_text_detected_error))
        } else if (detectionError) {
            ErrorBodyText(
                DEFAULT_TEXT_MODIFIER,
                stringResource(R.string.ocr_error)
            )
        }
    }

    ExpandableCard(
        modifier = DEFAULT_TEXT_MODIFIER,
        shape = Shapes.medium,
        backgroundColor = MaterialTheme.colors.background,
        elevation = 4.dp,
        folded = tweetMatchesFold,
        onClick = { tweetMatchesFold = !tweetMatchesFold },
        header = {
            CardHeader(
                modifier,
                stringResource(R.string.tweet_matches_card_title),
                tweetMatchesArrow
            )
        },
    ) {
        if (detectedText == null) {
            LoadingCircle()
        } else if (detectedText != "") {
            TweetMatches(modifier, detectedText!!)
        } else if (detectedText == "") {
            ErrorBodyText(DEFAULT_TEXT_MODIFIER, stringResource(R.string.no_text_detected_error))
        } else if (detectionError) {
            ErrorBodyText(
                DEFAULT_TEXT_MODIFIER,
                stringResource(R.string.ocr_error)
            )
        }
    }
}

@SuppressLint("MutableCollectionMutableState")
@Composable
fun TweetDetails(modifier: Modifier = Modifier, detectedText: String) {
    val searchDataBuilder = SearchDataHelper(detectedText)
    var allHandles by rememberSaveable { mutableStateOf(emptyList<String>()) }
    var allDates by rememberSaveable { mutableStateOf(emptyList<LocalDate>()) }

    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        coroutineScope.launch {
            allHandles = searchDataBuilder.processHandles()
            allDates = searchDataBuilder.processDates()
        }
    }

    val fieldsMap = mapOf(
        stringResource(R.string.handles_field) to allHandles,
        stringResource(R.string.dates_field) to allDates.map { date ->
            date.toString()
        }
    )

    fieldsMap.forEach {
        val (field, value) = it
        Divider(modifier.padding(horizontal = DEFAULT_PADDING))
        Row(
            modifier.padding(DEFAULT_PADDING),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                field,
                modifier.padding(DEFAULT_PADDING),
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier.weight(1f))
            Text(
                if (value.isNotEmpty()) {
                    value.joinToString("\n")
                } else {
                    "-"
                },
                modifier.padding(DEFAULT_PADDING),
                textAlign = TextAlign.Right
            )
        }
    }
}

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun TweetMatches(modifier: Modifier = Modifier, detectedText: String) {
    val twitterApiViewModel: TwitterApiViewModel = viewModel(
        factory = TwitterApiViewModelFactory(detectedText)
    )
    twitterApiViewModel.searchTweet()

    val processedContent = twitterApiViewModel.processedContent.collectAsStateWithLifecycle()
    val tweetMatches = twitterApiViewModel.tweetMatches.collectAsStateWithLifecycle()
    val tweetApiError = twitterApiViewModel.tweetApiError.collectAsStateWithLifecycle()

    if (tweetApiError.value) {
        ErrorBodyText(DEFAULT_TEXT_MODIFIER, stringResource(R.string.network_error))
    } else if (tweetMatches.value == null) {
        LoadingCircle()
    } else if (tweetMatches.value!!.size == 0) {
        ErrorBodyText(DEFAULT_TEXT_MODIFIER, stringResource(R.string.no_matching_tweets_error))
    } else {
        tweetMatches.value!!.forEach { (tweet, author) ->
            Column(
                modifier
                    .padding(DEFAULT_PADDING)
                    .clickable {
                        //TODO open link in twitter app
                    }) {
                Divider()
                Text(
                    "${FuzzySearch.tokenSetRatio(processedContent.value, tweet.text)}% Accuracy",
                    fontStyle = FontStyle.Italic
                )
                Row(
                    modifier
                        .fillMaxWidth()
                ) {
                    AsyncImage(
                        model = author.profileImageUrl,
                        modifier = modifier
                            .padding(vertical = DEFAULT_PADDING)
                            .clip(CircleShape)
                            .fillMaxWidth()
                            .weight(1.5F),
                        contentDescription = stringResource(R.string.tweet_author_profile_picture),
                        contentScale = ContentScale.FillWidth,
                        error = painterResource(R.drawable.preview_error)
                    )
                    Column(
                        modifier
                            .padding(DEFAULT_PADDING)
                            .weight(9F)
                    ) {
                        Text(author.name, fontWeight = FontWeight.Bold)
                        Text(
                            "@${author.screenName}",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.subtitle2
                        )
                    }
                }
                Text(tweet.text)
                val dateTimeFormat = DateTimeFormatter.ofPattern(TWEET_DATETIME_FORMAT)
                val tweetCreationDateTime = tweet.createdAt!!.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()
                Text(
                    dateTimeFormat.format(tweetCreationDateTime),
                    modifier.padding(vertical = DEFAULT_PADDING),
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.subtitle2
                )
                Row(
                    modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    val compactDecimalFormat = CompactDecimalFormat.getInstance(
                        ULocale.getDefault(),
                        CompactDecimalFormat.CompactStyle.SHORT
                    )
                    val publicMetrics = tweet.publicMetrics!!
                    val tweetMetrics = listOf(
                        TweetMetricUI(
                            publicMetrics.likeCount,
                            Icons.Outlined.FavoriteBorder,
                            stringResource(R.string.twitter_like_description)
                        ),
                        TweetMetricUI(
                            publicMetrics.replyCount,
                            Icons.Outlined.Comment,
                            stringResource(R.string.twitter_reply_description)
                        ),
                        TweetMetricUI(
                            publicMetrics.retweetCount,
                            Icons.Outlined.Repeat,
                            stringResource(R.string.twitter_retweet_description)
                        ),
                    )
                    tweetMetrics.forEach {
                        Row(modifier.padding(horizontal = 2.dp)) {
                            Text(compactDecimalFormat.format(it.number))
                            Icon(
                                it.icon,
                                contentDescription = it.description,
                                modifier.padding(horizontal = 1.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}