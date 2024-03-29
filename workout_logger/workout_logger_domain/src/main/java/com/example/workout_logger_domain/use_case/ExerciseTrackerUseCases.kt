package com.example.workout_logger_domain.use_case


data class ExerciseTrackerUseCases(
    val getExerciseForName: GetExerciseForName,
    val getUniqueExerciseForName: GetUniqueExerciseForName,
    val getWorkouts: GetWorkouts,
    val getWorkoutsByName: GetWorkoutsByName,
    val addCompletedWorkout: AddCompletedWorkout,
    val getWorkoutsForDate: GetWorkoutsForDate,
    val addExercise: AddExercise,
    val updateExercise: UpdateExercise
)
