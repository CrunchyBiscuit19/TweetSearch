package com.example.tweetsearch.screen

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.tweetsearch.R
import com.example.tweetsearch.component.generic.BodyText
import com.example.tweetsearch.component.generic.HeaderBodyText
import com.example.tweetsearch.component.generic.HyperlinkText
import com.example.tweetsearch.ui.theme.DEFAULT_PADDING
import com.example.tweetsearch.ui.theme.Typography
import com.example.tweetsearch.ui.theme.defaultTextModifier

@Composable
fun InstructionScreen(modifier: Modifier) {
    LazyColumn(
        modifier
            .fillMaxWidth()
    ) {
        item {
            SearchingTweetsSection(modifier)
        }
        item {
            SourceCodeSection()
        }
    }
}

@Composable
fun SearchingTweetsSection(modifier: Modifier) {
    val searchingSteps = listOf(
        stringResource(R.string.search_step_download_copy),
        stringResource(R.string.search_step_open_search),
        stringResource(R.string.search_step_submit),
        stringResource(R.string.search_step_wait)
    )
    val searchingTips = listOf(
        stringResource(R.string.search_tip_accuracy),
        stringResource(R.string.search_tip_information),
        stringResource(R.string.search_tip_limitation)
    )

    HeaderBodyText(
        defaultTextModifier,
        stringResource(R.string.searching_tweets_header)
    )
    searchingSteps.forEachIndexed { index, step ->
        Row {
            val number = index + 1
            BodyText(modifier.padding(DEFAULT_PADDING), "$number.")
            BodyText(defaultTextModifier, step)
        }
    }
    searchingTips.forEach { tip ->
        Row {
            BodyText(modifier.padding(DEFAULT_PADDING), "-")
            BodyText(defaultTextModifier, tip)
        }
    }

}

@Composable
fun SourceCodeSection() {
    val sourceCodeUrl = stringResource(R.string.tweetsearch_repo_url)
    val sourceCodeRepo = stringResource(R.string.source_code_repo)
    val sourceCodeStep = "Source code for the app can be found at $sourceCodeRepo."

    HeaderBodyText(
        defaultTextModifier,
        stringResource(R.string.source_code_header)
    )
    HyperlinkText(
        defaultTextModifier,
        fullText = sourceCodeStep,
        fullTextColor = MaterialTheme.colors.onBackground,
        linkText = listOf(sourceCodeRepo),
        linkTextColor = MaterialTheme.colors.secondary,
        hyperlinks = listOf(sourceCodeUrl),
        fontSize = Typography.body1.fontSize
    )
}