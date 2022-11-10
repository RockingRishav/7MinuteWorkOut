package com.example.a7minutesworkout

import android.app.Dialog
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.a7minutesworkout.databinding.DialogCustomBackConfirmationBinding
import com.example.a7minutesworkout.databinding.FragmentExerciseBinding
import java.util.*

/**
 * A simple [Fragment] subclass.
 * Use the [ExerciseFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ExerciseFragment : Fragment() , TextToSpeech.OnInitListener{

    private  var _binding: FragmentExerciseBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView
    private var countDownTimer: CountDownTimer? = null
    private var restProgress = 0
    private var restProgressExercise =0
    private var exerciseTimer: CountDownTimer?= null

    private var exerciseList : ArrayList<ExerciseViewModel>?= null
    private var currentExercisePosition = -1
    private var tts: TextToSpeech? = null
    private var mediaPlayer : MediaPlayer? = null

    private var exerciseAdapter : ExerciseStatusAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentExerciseBinding.inflate(inflater, container, false)
//        (activity as AppCompatActivity?)!!.setSupportActionBar(binding.toolbarExercise)
//        if((activity as AppCompatActivity?)!!.supportActionBar != null){
//            (activity as AppCompatActivity?)!!.supportActionBar?.setDisplayHomeAsUpEnabled(true)
//        }
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        binding.toolbarExercise.setNavigationOnClickListener {
//            activity?.onBackPressed()
//        }
        recyclerView = binding.rvExerciseStatus
        exerciseList = Constants.defaultExerciseList()

        tts = TextToSpeech(this.requireContext(),this)
        setUpRestView()
        setUpExerciseStatusRecyclerView()
        binding.skipBtn.setOnClickListener{
            exerciseTimer?.onFinish()
        }
//        binding.toolbarExercise.setNavigationOnClickListener {
//            activity?.onBackPressed()
//        }
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
    override fun onDestroyView() {
        super.onDestroyView()
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
        _binding = null
    }
    private fun setUpExerciseStatusRecyclerView(){
        binding.rvExerciseStatus.layoutManager = LinearLayoutManager(this.requireContext(), LinearLayoutManager.HORIZONTAL, false )

        exerciseAdapter = ExerciseStatusAdapter(exerciseList!!)

        binding.rvExerciseStatus.adapter = exerciseAdapter
    }
    private fun setUpRestView(){

        try{
            val soundUri = Uri.parse("android.resource://com.example.a7minutesworkout/" + R.raw.press_start)
            mediaPlayer =  MediaPlayer.create(activity?.applicationContext,soundUri)
            mediaPlayer?.isLooping= false
            mediaPlayer?.start()
        }
        catch (e: Exception){
            e.printStackTrace()
        }
        binding.progressBar.visibility = View.VISIBLE
        binding.tvTitle.visibility = View.VISIBLE
        binding.tvExercise.visibility = View.INVISIBLE
        binding.ivImage.visibility = View.INVISIBLE
        binding.flFramelayoutExerciseactual.visibility = View.INVISIBLE
        binding.skipBtn.visibility = View.INVISIBLE
        binding.flFramelayoutExercise.visibility = View.VISIBLE
        binding.upcomingExercise.visibility = View.VISIBLE
        binding.upcomingExerciseName.visibility = View.VISIBLE
        currentExercisePosition++
        binding.upcomingExerciseName.text= exerciseList!![currentExercisePosition].getName()

        if(countDownTimer != null){
            countDownTimer?.cancel()
            restProgress=0
        }
        speakOut("Get ready for the next Exercise")
        startTimer()
    }
    private fun speakOut(text: String){
        tts!!.speak(text, TextToSpeech.QUEUE_FLUSH,null,"")
    }
    private fun startTimer(){
        binding.progressBar.progress = restProgress
        countDownTimer = object : CountDownTimer(10000,1000) {
            override fun onTick(millisUntilFinished: Long){
                restProgress++
                binding.progressBar.progress = 10-restProgress
                binding.tvTimer.text = (10-restProgress).toString()
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
    private fun setUpExerciseTimer(){
        binding.progressBar.visibility = View.INVISIBLE
        binding.tvTitle.visibility = View.INVISIBLE
        binding.tvExercise.visibility = View.VISIBLE
        binding.ivImage.visibility = View.VISIBLE
        binding.flFramelayoutExerciseactual.visibility = View.VISIBLE
        binding.skipBtn.visibility = View.VISIBLE
        binding.flFramelayoutExercise.visibility = View.INVISIBLE
        binding.upcomingExercise.visibility = View.INVISIBLE
        binding.upcomingExerciseName.visibility = View.INVISIBLE
        if(exerciseTimer != null){
            exerciseTimer?.cancel()
            restProgressExercise=0
        }
        speakOut(exerciseList!![currentExercisePosition].getName())
        binding.ivImage.setImageResource(exerciseList!![currentExercisePosition].getImage())
        binding.tvExercise.text = exerciseList!![currentExercisePosition].getName()
        startTimerExercise()
    }
    private fun startTimerExercise(){
        binding.progressBar.progress = restProgressExercise
        exerciseTimer = object : CountDownTimer(30000,1000) {
            override fun onTick(millisUntilFinished: Long){
                restProgressExercise++
                binding.progressBarExercise.progress = 30-restProgressExercise
                binding.tvTimerExercise.text = (30-restProgressExercise).toString()
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
                    Toast.makeText(context, "Congratulation You made it to End",
                        Toast.LENGTH_SHORT).show()
//                val dao = (application as WorkOut).db.historyDao()
//                addDateToDatabase(dao)
//                    val intent = Intent(this@ExerciseFragment,MainActivity::class.java)
//                    startActivity(intent)
//                    finish()

                }
            }
        }.start()

    }

}