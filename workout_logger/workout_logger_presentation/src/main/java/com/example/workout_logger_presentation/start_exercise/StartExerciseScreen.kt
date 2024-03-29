package com.example.workout_logger_presentation.start_exercise

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.annotation.ExperimentalCoilApi
import com.example.workout_logger_presentation.components.ExerciseInfoDialog
import com.example.workout_logger_presentation.components.IconButton
import com.example.workout_logger_presentation.start_exercise.components.ExerciseCard
import com.example.workout_logger_presentation.start_workout.components.NotificationUtil
import com.example.workout_logger_presentation.start_exercise.components.Timer
import com.hbaez.core.util.UiEvent
import com.hbaez.core_ui.LocalSpacing
import java.time.Duration
import java.time.Month

@RequiresApi(Build.VERSION_CODES.S)
@OptIn(ExperimentalCoilApi::class)
@Composable
fun StartExerciseScreen(
    exerciseName: String,
    dayOfMonth: Int,
    month: Int,
    year: Int,
    onNavigateUp: () -> Unit,
    viewModel: StartExerciseViewModel = hiltViewModel()
){
    val spacing = LocalSpacing.current
    val context = LocalContext.current
    val state = viewModel.state
    val showExerciseInfoDialog = remember { mutableStateOf(false) }

    LaunchedEffect(Unit){
        viewModel.uiEvent.collect{ event ->
            when(event) {
                is UiEvent.NavigateUp -> onNavigateUp()
                else -> Unit
            }
        }
    }

    if(showExerciseInfoDialog.value){
        ExerciseInfoDialog(
            trackableExerciseState = state.exerciseInfo.first(),
            onDescrClick = {
                viewModel.onEvent(StartExerciseEvent.OnToggleExerciseDescription(state.exerciseInfo.first()))
            },
            onDismiss = { showExerciseInfoDialog.value = false })
    }

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    modifier = Modifier
                        .padding(spacing.spaceMedium),
                    text = "${Month.of(month).name} ${dayOfMonth}, $year".uppercase(),
                    style = MaterialTheme.typography.bodyMedium
                )
                IconButton(
                    borderColor = MaterialTheme.colorScheme.outline,
                    modifier = Modifier
                        .padding(top = spacing.spaceMedium, end = spacing.spaceMedium, bottom = spacing.spaceLarge),
                    onClick = {
                        viewModel.onEvent(StartExerciseEvent.OnSubmitWorkout(state.exerciseName, state.exerciseInfo.first().exercise.id!!, state.sets, state.reps, state.weight, state.rest, dayOfMonth, month, year))
                    },
                    icon = Icons.Default.Done
                )
            }
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(padding),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Row{
                    Text(
                        text = state.exerciseName,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                        style = MaterialTheme.typography.displaySmall,
                        modifier = Modifier.fillMaxWidth(.75f)
                    )
                    Spacer(modifier = Modifier.width(spacing.spaceSmall))
                    IconButton(
                        onClick = {
                            showExerciseInfoDialog.value = true
                        },
                        icon = Icons.Outlined.Info,
                        padding = 0.dp
                    )
                }
                Spacer(modifier = Modifier.height(spacing.spaceMedium))
                ExerciseCard(
                    timerStatus = state.timerStatus,
                    sets = state.sets,
                    isCompleted = state.isCompleted,
                    reps = state.reps,
                    weight = state.weight,
                    onRepsChange = { reps, index ->
                        viewModel.onEvent(StartExerciseEvent.OnRepsChange(reps = reps, index = index))
                    },
                    onWeightChange = { weight, index ->
                        viewModel.onEvent(StartExerciseEvent.OnWeightChange(weight = weight, index = index))
                    },
                    onCheckboxChange = { isChecked, index ->
                        if(isChecked && state.currRunningIndex != index && state.timerStatus == TimerStatus.RUNNING){ // non checked clicked while timer already running
                            // DO NOTHING
//                                viewModel.onEvent(StartWorkoutEvent.OnCheckboxChange(isChecked= false, timerStatus = TimerStatus.RUNNING, currRunningIndex = state.currRunningIndex, index = index, rowId = id, page = page))
                        }
                        if(isChecked && (state.timerStatus == TimerStatus.START || state.timerStatus == TimerStatus.FINISHED)){ // non checked clicked while timer not running
                            viewModel.onEvent(StartExerciseEvent.OnCheckboxChange(isChecked= true, timerStatus = TimerStatus.RUNNING, currRunningIndex = index, index = index, shouldUpdateTime = true))
                            val wakeupTime = StartExerciseViewModel.setAlarm(context = context, timeDuration = Duration.ofSeconds((state.rest.getOrElse(index) { state.rest.last() }).toLong()))
                            NotificationUtil.showTimerRunning(context, wakeupTime)
                            viewModel.onEvent(StartExerciseEvent.ChangeCheckboxColor(color = Color(255,153,51), index = index))
                        }
                        else if(!isChecked && state.currRunningIndex == index && state.timerStatus == TimerStatus.RUNNING){ // checked clicked while that row has timer running
                            viewModel.onEvent(StartExerciseEvent.OnCheckboxChange(isChecked= true, timerStatus = TimerStatus.FINISHED, currRunningIndex = -1, index = index, shouldUpdateTime = true))
                            StartExerciseViewModel.removeAlarm(context)
                            NotificationUtil.hideTimerNotification(context)
                            viewModel.onEvent(StartExerciseEvent.ChangeCheckboxColor(color = Color.DarkGray, index = index))
                        }
                        else if(!isChecked && state.currRunningIndex != index){ // checked clicked while that row does not have timer running
                            viewModel.onEvent(StartExerciseEvent.OnCheckboxChange(isChecked= false, timerStatus = state.timerStatus, currRunningIndex = state.currRunningIndex, index = index, shouldUpdateTime = false))
                        }
                    },
                    onRemoveSet = {
                        viewModel.onEvent(StartExerciseEvent.OnRemoveSet)
                    },
                    onAddSet = {
                        viewModel.onEvent(StartExerciseEvent.OnAddSet)
                    }
                )
            }
        },
        bottomBar = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ){
                Timer(
                    modifier = Modifier
                        .size(200.dp),
                    timerJump = state.timerJump,
                    handleColor = MaterialTheme.colorScheme.primary,
                    inactiveBarColor = MaterialTheme.colorScheme.secondary,
                    activeBarColor = MaterialTheme.colorScheme.inversePrimary
                )
            }
        }
    )
}