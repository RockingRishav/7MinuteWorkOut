package com.example.a7minutesworkout

import android.app.Dialog
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.a7minutesworkout.databinding.ActivityExerciseBinding
import com.example.a7minutesworkout.databinding.DialogCustomBackConfirmationBinding

import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

class ExerciseActivity : AppCompatActivity(), TextToSpeech.OnInitListener {
    private var countDownTimer: CountDownTimer? = null
    private var restProgress = 0
    private var restProgressExercise =0
    private var exerciseTimer: CountDownTimer?= null
    private var binding  : ActivityExerciseBinding? =null

    private var exerciseList : ArrayList<ExerciseViewModel>?= null
    private var currentExercisePosition = -1
    private var tts: TextToSpeech? = null
    private var mediaPlayer : MediaPlayer? = null

    private var exerciseAdapter : ExerciseStatusAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =ActivityExerciseBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.toolbarExercise)
        if(supportActionBar!= null){
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
         exerciseList = Constants.defaultExerciseList()

        tts = TextToSpeech(this,this)
        binding?.toolbarExercise?.setNavigationOnClickListener {
            customBackNavigation()
        }

            setUpRestView()
        setUpExerciseStatusRecyclerView()
        binding?.skipBtn?.setOnClickListener{
            exerciseTimer?.onFinish()
        }

    }
    private fun customBackNavigation(){
        val customDialog = Dialog(this)
        val dialogBinding = DialogCustomBackConfirmationBinding.inflate(layoutInflater)
        customDialog.setContentView(dialogBinding.root)
        customDialog.setCanceledOnTouchOutside(false)
        dialogBinding.btnYes.setOnClickListener{
            this@ExerciseActivity.finish()
            customDialog.dismiss()
        }
        dialogBinding.btnNo.setOnClickListener{
            customDialog.dismiss()
        }
        customDialog.show()
    }

    private fun setUpExerciseStatusRecyclerView(){
        binding?.rvExerciseStatus?.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false )

        exerciseAdapter = ExerciseStatusAdapter(exerciseList!!)

        binding?.rvExerciseStatus?.adapter = exerciseAdapter
    }
    private fun setUpRestView(){

        try{
            val soundUri = Uri.parse("android.resource://com.example.a7minutesworkout/" + R.raw.press_start)
            mediaPlayer =  MediaPlayer.create(applicationContext,soundUri)
            mediaPlayer?.isLooping= false
            mediaPlayer?.start()
        }
        catch (e:Exception){
            e.printStackTrace()
        }
        binding?.progressBar?.visibility = View.VISIBLE
        binding?.tvTitle?.visibility = View.VISIBLE
        binding?.tvExercise?.visibility = View.INVISIBLE
        binding?.ivImage?.visibility = View.INVISIBLE
        binding?.flFramelayoutExerciseactual?.visibility = View.INVISIBLE
        binding?.skipBtn?.visibility = View.INVISIBLE
        binding?.flFramelayoutExercise?.visibility = View.VISIBLE
        binding?.upcomingExercise?.visibility = View.VISIBLE
        binding?.upcomingExerciseName?.visibility = View.VISIBLE
        currentExercisePosition++
        binding?.upcomingExerciseName?.text= exerciseList!![currentExercisePosition].getName()

        if(countDownTimer != null){
            countDownTimer?.cancel()
            restProgress=0
        }
        speakOut("Get ready for the next Exercise")
        startTimer()
    }
    private fun setUpExerciseTimer(){
        binding?.progressBar?.visibility = View.INVISIBLE
        binding?.tvTitle?.visibility = View.INVISIBLE
        binding?.tvExercise?.visibility = View.VISIBLE
        binding?.ivImage?.visibility = View.VISIBLE
        binding?.flFramelayoutExerciseactual?.visibility = View.VISIBLE
        binding?.skipBtn?.visibility = View.VISIBLE
        binding?.flFramelayoutExercise?.visibility = View.INVISIBLE
        binding?.upcomingExercise?.visibility = View.INVISIBLE
        binding?.upcomingExerciseName?.visibility = View.INVISIBLE
        if(exerciseTimer != null){
            exerciseTimer?.cancel()
            restProgressExercise=0
        }
        speakOut(exerciseList!![currentExercisePosition].getName())
        binding?.ivImage?.setImageResource(exerciseList!![currentExercisePosition].getImage())
        binding?.tvExercise?.text = exerciseList!![currentExercisePosition].getName()
        startTimerExercise()
    }
    private fun startTimer(){
        binding?.progressBar?.progress = restProgress
          countDownTimer = object : CountDownTimer(10000,1000) {
              override fun onTick(millisUntilFinished: Long){
                  restProgress++
                  binding?.progressBar?.progress = 10-restProgress
                  binding?.tvTimer?.text = (10-restProgress).toString()
              }

              override fun onFinish() {
//                  currentExercisePosition++
//                  Toast.makeText(this@ExerciseActivity, "Here we now start the Exercise",Toast.LENGTH_SHORT).show()
                  exerciseList!![currentExercisePosition].setIsSelected(true)
                  exerciseAdapter!!.notifyDataSetChanged()
                  setUpExerciseTimer()
              }
          }.start()
        
    }
    private fun startTimerExercise(){
        binding?.progressBar?.progress = restProgressExercise
        exerciseTimer = object : CountDownTimer(30000,1000) {
            override fun onTick(millisUntilFinished: Long){
                restProgressExercise++
                binding?.progressBarExercise?.progress = 30-restProgressExercise
                binding?.tvTimerExercise?.text = (30-restProgressExercise).toString()
            }

            override fun onFinish() {
//                Toast.makeText(this@ExerciseActivity, "Exercise Ended",Toast.LENGTH_SHORT).show()
                exerciseList!![currentExercisePosition].setIsSelected(false)
                exerciseList!![currentExercisePosition].setIsCompleted(true)
                exerciseAdapter!!.notifyDataSetChanged()
            if(currentExercisePosition < exerciseList?.size!!-1){
                setUpRestView()
            }
                else{
                Toast.makeText(this@ExerciseActivity, "Congratulation You made it to End",Toast.LENGTH_SHORT).show()
//                val dao = (application as WorkOut).db.historyDao()
//                addDateToDatabase(dao)
                val intent = Intent(this@ExerciseActivity,MainActivity::class.java)
                startActivity(intent)
                finish()

            }
            }
        }.start()

    }
//    private fun addDateToDatabase(historyDao: HistoryDao){
//        val c = Calendar.getInstance()
//        val dateTime = c.time
//        val sdf = SimpleDateFormat("dd MMM yyyy HH:mm:ss", Locale.getDefault())
//        val data = sdf.format(dateTime)
//        lifecycleScope.launch{
//              historyDao.insert(HistoryEntity(""))
//        }
//    }
    override fun onBackPressed() {
        customBackNavigation()
    }
    override fun onDestroy() {
        super.onDestroy()
        if(countDownTimer != null){
            countDownTimer?.cancel()
            restProgress=0
        }

        if(exerciseTimer != null){
            exerciseTimer?.cancel()
            restProgressExercise=0
        }

        if(tts != null){
            tts!!.stop()
            tts!!.shutdown()
        }
        if(mediaPlayer!= null){
            mediaPlayer!!.stop()
        }
        binding=null
    }

    override fun onInit(status: Int){
        if(status == TextToSpeech.SUCCESS){
            val result = tts?.setLanguage(Locale.US)

            if(result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED){
                Log.e("TTS","The Language specified is not supported")
            }
        }
        else{
            Log.e("TTS","Initialization Failed")
        }
    }
    private fun speakOut(text: String){
        tts!!.speak(text, TextToSpeech.QUEUE_FLUSH,null,"")
    }
}