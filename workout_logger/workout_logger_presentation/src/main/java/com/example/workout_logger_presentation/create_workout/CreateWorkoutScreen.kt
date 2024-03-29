package com.example.workout_logger_presentation.create_workout

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import coil.annotation.ExperimentalCoilApi
import com.example.workout_logger_presentation.components.AddButton
import com.example.workout_logger_presentation.components.NameField
import com.hbaez.core_ui.LocalSpacing
import com.hbaez.core.R
import androidx.compose.material3.Text
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import com.example.workout_logger_presentation.components.ExerciseInfoDialog
import com.example.workout_logger_presentation.create_workout.components.ExerciseCard
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.hbaez.core.util.UiEvent
import com.hbaez.user_auth_presentation.common.composable.DialogCancelButton
import com.hbaez.user_auth_presentation.common.composable.DialogConfirmButton
import kotlinx.coroutines.launch

@OptIn(ExperimentalComposeUiApi::class, ExperimentalPagerApi::class)
@ExperimentalCoilApi
@Composable
fun CreateWorkoutScreen(
    snackBarHost: SnackbarHostState,
    createWorkout: Boolean,
    onNavigateUp: () -> Unit,
    onNavigateToSearchExercise: (page: Int) -> Unit,
    viewModel: CreateWorkoutViewModel = hiltViewModel()
) {
    val spacing = LocalSpacing.current
    val state = viewModel.state

    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current

    val coroutineScope = rememberCoroutineScope()

    val pagerState = rememberPagerState()
    val pageCount = remember { mutableStateOf(state.pageCount) }
    val showExerciseInfoDialog = remember { mutableStateOf(false) }
    val showExerciseInfoDialogSuperset = remember { mutableStateOf(false) }
    val showDeleteRoutineDialog = remember { mutableStateOf(false) }

    var menuExpanded = remember { mutableStateOf(false) }

    LaunchedEffect(Unit){
        viewModel.onEvent(CreateWorkoutEvent.CheckTrackedExercise)

        viewModel.uiEvent.collect{ event ->
            when(event) {
                is UiEvent.ShowSnackbar -> {
                    snackBarHost.showSnackbar(
                        message = event.message.asString(context)
                    )
                }
                is UiEvent.NavigateUp -> onNavigateUp()
                else -> Unit
            }
        }
    }
    LaunchedEffect(state.pageCount) {
        pageCount.value = state.pageCount
        if (pagerState.currentPage >= state.pageCount) {
            pagerState.scrollToPage(state.pageCount)
        }
    }
    LaunchedEffect(pagerState.isScrollInProgress){
        if(pagerState.targetPage > state.pageCount){
            pagerState.scrollToPage(state.pageCount)
        }
    }
    LaunchedEffect(key1 = pagerState.currentPage, key2 = state.trackableExercises.size){
        if(state.trackableExercises.getOrNull(pagerState.currentPage) != null){
            Log.println(Log.DEBUG, "CreateWorkoutScreen currentPage", pagerState.currentPage.toString())
            Log.println(Log.DEBUG, "CreateWorkoutScreen isSuperset", state.trackableExercises[pagerState.currentPage].isSuperset.toString())
            Log.println(Log.DEBUG, "CreateWorkoutScreen name", state.trackableExercises[pagerState.currentPage].name)
            if(state.trackableExercises[pagerState.currentPage].isSuperset){
                Log.println(Log.DEBUG, "CreateWorkoutScreen launchedEffect", "isSuperset true")
                viewModel.onEvent(CreateWorkoutEvent.GetExerciseInfo(pagerState.currentPage, true))
            } else {
                Log.println(Log.DEBUG, "CreateWorkoutScreen launchedEffect", "isSuperset false")
                viewModel.onEvent(CreateWorkoutEvent.GetExerciseInfo(pagerState.currentPage, false))
            }
        }
        if (!createWorkout){
            viewModel.onEvent(CreateWorkoutEvent.GetAllExerciseInfo)
        }
    }

    if(showExerciseInfoDialog.value){
        ExerciseInfoDialog(
            trackableExerciseState = if(showExerciseInfoDialogSuperset.value) state.exerciseInfo[1] else state.exerciseInfo[0],
            onDescrClick = {
                           viewModel.onEvent(CreateWorkoutEvent.OnToggleExerciseDescription(if(showExerciseInfoDialogSuperset.value) state.exerciseInfo[1] else state.exerciseInfo[0], if(showExerciseInfoDialogSuperset.value) 1 else 0))
                           },
            onDismiss = { showExerciseInfoDialog.value = false; showExerciseInfoDialogSuperset.value = false }
        )
    }

    if(showDeleteRoutineDialog.value){
        AlertDialog(
            title = { 
                Text(
                style = MaterialTheme.typography.displayMedium,
                text = stringResource(R.string.delete_routine)
                )
                    },
            dismissButton = { DialogCancelButton(R.string.cancel) { showDeleteRoutineDialog.value = false } },
            confirmButton = {
                DialogConfirmButton(R.string.delete) {
                    if(createWorkout){
                        onNavigateUp()
                    } else {
                        viewModel.onEvent(CreateWorkoutEvent.DeleteRoutine)
                    }
                }
            },
            onDismissRequest = { showDeleteRoutineDialog.value = false }
        )
    }

    Scaffold(
        topBar = {
            Column {
                Spacer(modifier = Modifier.height(spacing.spaceMedium))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    NameField(
                        modifier = Modifier
                            .fillMaxWidth(.8f)
                            .padding(spacing.spaceMedium),
//                            .padding(end = spacing.spaceExtraLarge)
                        text = state.workoutName,
                        hint = stringResource(id = R.string.routine_name),
                        onValueChange = {
                            viewModel.onEvent(CreateWorkoutEvent.OnWorkoutNameChange(it))
                        },
                        onFocusChanged = {
                            viewModel.onEvent(CreateWorkoutEvent.OnWorkoutNameFocusChange(it.isFocused))
                        },
                        keyboardController = keyboardController
                    )
                    Box {
                        Icon(
                            modifier = Modifier
                                .clickable {
                                    menuExpanded.value = true
                                },
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "Create/Edit Workout Screen Option button"
                        )
                        DropdownMenu(
                            expanded = menuExpanded.value,
                            onDismissRequest = { menuExpanded.value = false }
                        ) {
                            DropdownMenuItem(
                                text = {
                                    Text(text = stringResource(id = R.string.delete))
                                },
                                onClick = {
                                showDeleteRoutineDialog.value = true
                                menuExpanded.value = false
                            })
                        }
                    }
                }
                Spacer(modifier = Modifier.height(spacing.spaceSmall))
            }
        },
        content = { padding ->
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                verticalAlignment = Alignment.CenterVertically,
                count = 15,
                contentPadding = PaddingValues(spacing.spaceSmall)
            ) { page ->
                val trackableExercises = state.trackableExercises.filter { !it.isDeleted && it.position == page }
                ExerciseCard(
                    page = page,
                    onAddCard = {
                        onNavigateToSearchExercise(page)
                    },
                    onAddSet = {
                        viewModel.onEvent(CreateWorkoutEvent.AddSet(page))
                    },
                    onMakeSuperset = {
                        onNavigateToSearchExercise(page)
                    },
                    trackableExercises = trackableExercises,
                    onShowInfo = {
                        showExerciseInfoDialog.value = true
                    },
                    onShowInfoSuperset = {
                        showExerciseInfoDialog.value = true
                        showExerciseInfoDialogSuperset.value = true
                    },
                    onDeleteRow = { id ->
                        viewModel.onEvent(CreateWorkoutEvent.OnRemoveSetRow(id, page))
                    },
                    onDeletePage = {
                        viewModel.onEvent(CreateWorkoutEvent.OnRemovePage(page))
                    },
                    onRepsChange = { text, index, exerciseName ->
                        viewModel.onEvent(CreateWorkoutEvent.OnTrackableExerciseUiRepsChange(reps = text, page = page, index = index, exercise = exerciseName))
                    },
                    onRestChange = { text, index, exerciseName->
                        viewModel.onEvent(CreateWorkoutEvent.OnTrackableExerciseUiRestChange(rest = text, page = page, index = index, exercise = exerciseName))
                    },
                    onWeightChange = { text, index, exerciseName ->
                        viewModel.onEvent(CreateWorkoutEvent.OnTrackableExerciseUiWeightChange(weight = text, page = page, index = index, exercise = exerciseName))
                    }
                )
            }
        },
        bottomBar = {
            Column {
                Row(
                    Modifier
                        .padding(horizontal = spacing.spaceMedium)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowLeft,
                        contentDescription = null,
                        tint = if(pagerState.currentPage == 0) MaterialTheme.colorScheme.background else MaterialTheme.colorScheme.onBackground,
                        modifier = if(pagerState.currentPage == 0) { Modifier.size(spacing.spaceLarge)
                        } else {
                            Modifier
                                .clickable {
                                    if (pagerState.currentPage > 0) {
                                        coroutineScope.launch {
                                            pagerState.animateScrollToPage(
                                                pagerState.currentPage - 1,
                                                0f
                                            )
                                        }
                                    }
                                }
                                .size(spacing.spaceLarge)
                        }
                    )
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowRight,
                        contentDescription = null,
                        tint = if((pagerState.currentPage == state.pageCount) || (pagerState.currentPage == (pagerState.pageCount - 1))) MaterialTheme.colorScheme.background else MaterialTheme.colorScheme.onBackground,
                        modifier = if((pagerState.currentPage == state.pageCount) || (pagerState.currentPage == (pagerState.pageCount - 1))) { Modifier.size(spacing.spaceLarge)
                        } else {
                            Modifier
                                .clickable {
                                    if ((pagerState.currentPage + 1) < pagerState.pageCount) {
                                        coroutineScope.launch {
                                            pagerState.animateScrollToPage(
                                                pagerState.currentPage + 1,
                                                0f
                                            )
                                        }
                                    }
                                }
                                .size(spacing.spaceLarge)
                        }
                    )
                }
                Spacer(modifier = Modifier.height(spacing.spaceMedium))
                Row(
                    Modifier.padding(spacing.spaceSmall)
                ){
                    AddButton(
                        text = stringResource(id = if(createWorkout) R.string.submit else R.string.update),
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .padding(start = spacing.spaceExtraExtraLarge, end = spacing.spaceSmall),
                        onClick = {
                            if(createWorkout){
                                viewModel.onEvent(CreateWorkoutEvent.OnCreateWorkout(state.trackableExercises.toList(), state.workoutName, state.lastUsedId))
                            }
                            else {
                                viewModel.onEvent(CreateWorkoutEvent.OnUpdateWorkout(state.trackableExercises.toList(), state.workoutName, state.lastUsedId))
                            }
                        },
                        icon = Icons.Default.Done
                    )
                    Spacer(modifier = Modifier.width(spacing.spaceLarge))
                }
            }
        }
    )
}