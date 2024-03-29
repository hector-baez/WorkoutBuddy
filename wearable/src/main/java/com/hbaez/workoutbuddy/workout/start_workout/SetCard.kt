package com.hbaez.workoutbuddy.workout.start_workout

import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowDownward
import androidx.compose.material.icons.rounded.ArrowUpward
import androidx.compose.material.icons.rounded.CheckCircleOutline
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.hbaez.core_ui.LocalSpacing
import com.hbaez.core.R
import com.hbaez.workoutbuddy.components.WearButton
import com.hbaez.workoutbuddy.components.WearText

@Composable
fun SetCard(
    exerciseName: String,
    currSet: Int,
    totalSets: Int,
    currReps: String,
    currWeight: String,
    onRepIncrease: () -> Unit,
    onRepDecrease: () -> Unit,
    onWeightIncrease: () -> Unit,
    onWeightDecrease: () -> Unit,
    onRest: () -> Unit
){
    val spacing = LocalSpacing.current
    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onBackground,
            disabledContainerColor = MaterialTheme.colorScheme.background,
            disabledContentColor = MaterialTheme.colorScheme.onBackground
        ),
        modifier = Modifier
            .clip(
                RoundedCornerShape(50.dp)
            )
            .border(2.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(50.dp))
            .padding(spacing.spaceSmall)
            .fillMaxWidth(.9f)
            .height(155.dp)
    ) {
        val scroll = rememberScrollState(0)
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            WearText(
                modifier = Modifier
                    .padding(horizontal = spacing.spaceSmall)
                    .horizontalScroll(scroll),
                color = MaterialTheme.colorScheme.onBackground,
                text = exerciseName,
                style = MaterialTheme.typography.labelMedium,
                maxLines = 1,
//                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(spacing.spaceSmall))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
            ){
                WearButton(
                    text = "",
                    onClick = { onRepDecrease() },
                    icon = Icons.Rounded.ArrowDownward,
                    borderColor = MaterialTheme.colorScheme.background,
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f)
                        .alignByBaseline()
                )
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f)
                        .alignByBaseline()
                ) {
                    WearText(
                        text = currReps,
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    WearText(
                        text = stringResource(id = R.string.reps),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )

                }
                WearButton(
                    text = "",
                    onClick = { onRepIncrease() },
                    icon = Icons.Rounded.ArrowUpward,
                    borderColor = MaterialTheme.colorScheme.background,
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f)
                        .alignByBaseline()
                )
            }
            Spacer(modifier = Modifier.height(spacing.spaceSmall))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
            ){
                WearButton(
                    text = "",
                    onClick = { onWeightDecrease() },
                    icon = Icons.Rounded.ArrowDownward,
                    borderColor = MaterialTheme.colorScheme.background,
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f)
                        .alignByBaseline()
                )
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f)
                        .alignByBaseline()
                ) {
                    WearText(
                        text = currWeight,
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    WearText(
                        text = stringResource(id = R.string.lbs),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )

                }
                WearButton(
                    text = "",
                    onClick = { onWeightIncrease() },
                    icon = Icons.Rounded.ArrowUpward,
                    borderColor = MaterialTheme.colorScheme.background,
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f)
                        .alignByBaseline()
                )
            }
            Spacer(modifier = Modifier.height(spacing.spaceSmall))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(35.dp)
            ){
                WearText(
                    text = "${currSet+1}/${totalSets}",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
                WearButton(
                    text = "",
                    onClick = { onRest() },
                    icon = Icons.Rounded.CheckCircleOutline,
                    borderColor = MaterialTheme.colorScheme.background,
                    modifier = Modifier
                        .fillMaxHeight()
                )
            }
        }
    }
}