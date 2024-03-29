package com.hbaez.user_auth_presentation.components

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.hbaez.core_ui.LocalSpacing

@Composable
fun BasicButton(@StringRes text: Int, modifier: Modifier, action: () -> Unit) {
    val spacing = LocalSpacing.current
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(100f))
            .clickable { action() }
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.onPrimary,
                shape = RoundedCornerShape(100f)
            )
            .padding(spacing.spaceMedium),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
//        colors =
//        ButtonDefaults.run {
//            buttonColors(
//                backgroundColor = MaterialTheme.colors.primary,
//                contentColor = MaterialTheme.colors.onPrimary
//            )
//        }
    ) {
        Text(
            text = stringResource(text),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}

@Composable
fun Button(@StringRes text: Int, modifier: Modifier, action: () -> Unit) {
    /**
     * TODO: Add the ability to change the color of the outline
     * TODO: Add the ability to add an icon
     */
    val spacing = LocalSpacing.current
    Row(
        modifier = modifier
            .background(MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(15.dp))
            .clickable { action() }
            .padding(spacing.spaceSmall)
            .fillMaxWidth(1f)
            .clip(RoundedCornerShape(15.dp)),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(text),
            style = MaterialTheme.typography.labelLarge,
//            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}


@Composable
fun FlatButton(@StringRes text: Int, modifier: Modifier, action: () -> Unit) {
    /**
     * TODO: Add the ability to change the color of the outline
     * TODO: Add the ability to add an icon
     */
    val spacing = LocalSpacing.current
    Row(
        modifier = modifier
            .background(Color.Transparent)
            .clickable { action() }
//            .border(1.dp, MaterialTheme.colors.onPrimary, RoundedCornerShape(15.dp))
            .padding(spacing.spaceSmall)
            .fillMaxWidth(1f),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(text),
            style = MaterialTheme.typography.labelLarge,
//            color = Color.White
        )
    }
}

@Composable
fun OutlineButton(@StringRes text: Int, modifier: Modifier, action: () -> Unit) {
    /**
     * TODO: Add the ability to change the color of the outline
     * TODO: Add the ability to add an icon
     */
    val spacing = LocalSpacing.current
    Row(
        modifier = modifier
            .background(Color.Transparent)
            .clickable { action() }
            .border(1.dp, MaterialTheme.colorScheme.onPrimary, RoundedCornerShape(15.dp))
            .padding(spacing.spaceSmall)
            .fillMaxWidth(1f),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(text),
            style = MaterialTheme.typography.labelLarge,
//            color = Color.White
        )
    }
}