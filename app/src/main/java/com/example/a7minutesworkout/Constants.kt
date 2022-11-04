package com.example.a7minutesworkout

import java.util.*
import kotlin.collections.ArrayList

object Constants {
    fun defaultExerciseList(): ArrayList<ExerciseViewModel>{
        val exerciseList = ArrayList<ExerciseViewModel>()

        val jumpingJacks = ExerciseViewModel(1,"Jumping Jacks",R.drawable.jumpingjacks2,false,false)
        val abdominalCrunch = ExerciseViewModel(2,"Abdominal Crunch",R.drawable.abdominalcrunch,false,false)
        val higKneeRunningInPlace = ExerciseViewModel(3,"High Knee Running Place",R.drawable.highknees,false,false)
        val lunge = ExerciseViewModel(4,"lunge",R.drawable.lunges,false,false)
        val plank = ExerciseViewModel(5,"plank",R.drawable.plank,false,false)
        val pushUp = ExerciseViewModel(6,"PushUp",R.drawable.pushup,false,false)
        val pushUpRotation = ExerciseViewModel(7,"Push Up Rotation",R.drawable.pushupandrotation,false,false)

        Collections.addAll(exerciseList,jumpingJacks,abdominalCrunch,higKneeRunningInPlace,lunge,plank,pushUp,pushUpRotation)
        return exerciseList

    }
}