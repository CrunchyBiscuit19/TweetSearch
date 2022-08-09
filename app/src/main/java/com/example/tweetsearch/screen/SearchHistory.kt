package com.example.tweetsearch.screen

import android.app.Application
import android.widget.Toast
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.tweetsearch.R
import com.example.tweetsearch.component.generic.HeaderBodyText
import com.example.tweetsearch.component.generic.LoadingCircle
import com.example.tweetsearch.data.history.History
import com.example.tweetsearch.data.history.HistoryViewModel
import com.example.tweetsearch.data.history.HistoryViewModelFactory
import com.example.tweetsearch.ui.theme.DEFAULT_PADDING
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun SearchHistoryScreen(modifier: Modifier, navController: NavController) {
    val historyViewModel: HistoryViewModel = viewModel(
        factory = HistoryViewModelFactory(LocalContext.current.applicationContext as Application)
    )

    LazyColumn(
        modifier
            .fillMaxWidth()
    ) {
        item {
            HistoryDisplay(modifier, historyViewModel, navController)
        }
    }
}

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun HistoryDisplay(
    modifier: Modifier,
    historyViewModel: HistoryViewModel,
    navController: NavController,
) {
    var query by rememberSaveable { mutableStateOf("") }
    val historyList =
        historyViewModel.historyList.collectAsStateWithLifecycle(initialValue = emptyList())
    historyViewModel.getHistory(query)

    val clipboardManager: ClipboardManager = LocalClipboardManager.current
    val context = LocalContext.current

    var deleteDialog: Pair<Boolean, History?> by rememberSaveable {
        mutableStateOf(
            Pair(
                false,
                null
            )
        )
    }

    TextField(
        value = query,
        onValueChange = { query = it },
        label = { Text(stringResource(R.string.enter_query)) },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedLabelColor = MaterialTheme.colors.onBackground,
            unfocusedLabelColor = MaterialTheme.colors.onBackground,
        ),
        modifier = modifier
            .padding(vertical = 2.dp)
            .fillMaxWidth()
    )

    if (historyList.value == null) {
        LoadingCircle()
    } else if (historyList.value!!.isEmpty()) {
        Text(stringResource(R.string.empty_history), modifier.padding(DEFAULT_PADDING))
    } else {
        historyList.value!!.forEach { history ->
            Row(
                modifier
                    .fillMaxWidth()
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onLongPress = {
                                deleteDialog = Pair(true, history)
                            },
                            onTap = {
                                navController.navigate(
                                    "${Screen.TweetInfo.name}/${
                                        URLEncoder.encode(
                                            history.fullPath,
                                            StandardCharsets.UTF_8.toString()
                                        )
                                    }"
                                )
                            }
                        )
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = history.fullPath,
                    contentDescription = "Thumbnail of ${history.fullPath}",
                    modifier = modifier
                        .fillMaxWidth()
                        .weight(1.5F),
                    contentScale = ContentScale.FillWidth,
                    error = painterResource(R.drawable.preview_error),
                    fallback = painterResource(R.drawable.preview_placeholder),
                )
                Column(
                    modifier
                        .padding(DEFAULT_PADDING)
                        .weight(9F)
                ) {
                    var shortPath = history.shortPath
                    var fullPath = history.fullPath
                    if (shortPath.length > 26) {
                        shortPath = "${shortPath.take(25)}..."
                    }
                    if (fullPath.length > 36) {
                        fullPath = "${fullPath.take(35)}..."
                    }
                    Text(shortPath)
                    Text(fullPath, style = MaterialTheme.typography.caption)
                }
                IconButton(onClick = {
                    clipboardManager.setText(AnnotatedString(history.fullPath))
                    Toast.makeText(
                        context,
                        "Copied full path of ${history.shortPath}",
                        Toast.LENGTH_SHORT
                    ).show()
                }) {
                    Icon(
                        Icons.Filled.ContentCopy,
                        contentDescription = stringResource(R.string.copy_path_button)
                    )
                }
            }
            Divider()
        }
    }

    if (deleteDialog.first) {
        DeleteConfirmationDialog(modifier, deleteDialog.second!!, historyViewModel) { result ->
            deleteDialog = Pair(result, deleteDialog.second)
        }
    }
}

@Composable
fun DeleteConfirmationDialog(
    modifier: Modifier,
    history: History,
    historyViewModel: HistoryViewModel,
    setExpanded: (Boolean) -> Unit
) {
    AlertDialog(
        backgroundColor = MaterialTheme.colors.background,
        onDismissRequest = { setExpanded(false) },
        title = {
            HeaderBodyText(
                modifier,
                stringResource(R.string.delete_confirmation_dialog_title)
            )
        },
        text = {
            Text("Are you sure you want to delete ${history.shortPath}?")
        },
        buttons = {
            Row(
                modifier = Modifier.padding(DEFAULT_PADDING),
                horizontalArrangement = Arrangement.Start
            ) {
                TextButton(
                    onClick = {
                        historyViewModel.deleteHistory(history)
                        setExpanded(false) })
                {
                    Text(stringResource(R.string.yes))
                }
                TextButton(
                    onClick = { setExpanded(false) })
                {
                    Text(stringResource(R.string.no))
                }
            }

        },
    )
}