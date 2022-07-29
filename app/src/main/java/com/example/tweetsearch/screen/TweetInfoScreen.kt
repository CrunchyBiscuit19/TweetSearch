package com.example.tweetsearch.screen

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.webkit.URLUtil
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.tweetsearch.R
import com.example.tweetsearch.component.generic.*
import com.example.tweetsearch.data.rotation.RotationAngles
import com.example.tweetsearch.ui.theme.Shapes
import com.example.tweetsearch.ui.theme.defaultModifier
import com.example.tweetsearch.ui.theme.imageRoundCorners
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions

@Composable
fun TweetInfoScreen(modifier: Modifier = Modifier, screenshotModel: String?) {
    TweetInfo(modifier, screenshotModel)
}

@Composable
fun TweetInfo(modifier: Modifier = Modifier, screenshotModel: String?) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState()),
    ) {
        HeaderBodyText(defaultModifier, stringResource(R.string.screenshot_selected))
        AsyncImage(
            model = screenshotModel,
            contentDescription = stringResource(R.string.confirmed_screenshot_model),
            modifier = defaultModifier
                .clip(imageRoundCorners)
                .border(1.dp, MaterialTheme.colors.onBackground),
            contentScale = ContentScale.FillWidth,
            error = painterResource(R.drawable.preview_error),
            fallback = painterResource(R.drawable.preview_placeholder),
        )
        if (screenshotModel != null) {
            TweetOCR(modifier, screenshotModel)
        } else {
            ErrorBodyText(defaultModifier, stringResource(R.string.image_not_found_error))
        }
    }
}

@Composable
fun TweetOCR(modifier: Modifier = Modifier, screenshotModel: String) {
    var detectionResultsFold by rememberSaveable { mutableStateOf(false) }
    var arrowRotationAngle by rememberSaveable { mutableStateOf(RotationAngles.DOWN.angle) }
    arrowRotationAngle = when (detectionResultsFold) {
        true -> RotationAngles.UP.angle
        false -> RotationAngles.DOWN.angle
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
                val bitmap = (result as BitmapDrawable).bitmap.copy(Bitmap.Config.ARGB_8888, false)
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
        modifier = defaultModifier,
        shape = Shapes.medium,
        backgroundColor = MaterialTheme.colors.background,
        elevation = 4.dp,
        folded = detectionResultsFold,
        onClick = { detectionResultsFold = !detectionResultsFold },
        header = {
            CardHeader(
                modifier,
                stringResource(R.string.text_detection_card_title),
                arrowRotationAngle
            )
        },
        content = {

            if (detectedText == null) {
                Row(modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                    CircularProgressIndicator(modifier.padding(32.dp))
                }
            } else if (detectedText != "") {
                BodyText(defaultModifier, detectedText!!)
            } else if (detectedText == "") {
                ErrorBodyText(defaultModifier, stringResource(R.string.no_text_detected_error))
            } else if (detectionError) {
                ErrorBodyText(defaultModifier, stringResource(R.string.cannot_detect_text_error))
            }
        }
    )
}