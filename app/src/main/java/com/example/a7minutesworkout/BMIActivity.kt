package com.example.a7minutesworkout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.a7minutesworkout.databinding.ActivityBmiBinding
import java.math.BigDecimal
import java.math.RoundingMode

class BMIActivity : AppCompatActivity() {

    companion object{
        private const val METRIC_UNITS_VIEW = "METRIC_UNIT_VIEW"
        private const val US_UNITS_VIEW = "US_UNIT_VIEW"
    }
    private var currentVisibleView: String = METRIC_UNITS_VIEW
    private var binding: ActivityBmiBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBmiBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        setSupportActionBar(binding?.toolbarBmiActivity)
        if(supportActionBar!=null){
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.title = "CALCULATE BMI "
        }
        binding?.toolbarBmiActivity?.setNavigationOnClickListener {
            onBackPressed()
        }
        makeVisibleMetricUnitsView()
        binding?.rgUnits?.setOnCheckedChangeListener { _, checkedId: Int ->
            if(checkedId == R.id.rbMetricUnits){
                makeVisibleMetricUnitsView()
            }
            else{
                makeVisibleUsUnitsView()
            }
        }
        
       binding?.btnCalculateUnits?.setOnClickListener {
            calculateUnits()
        }
    }
    private fun makeVisibleMetricUnitsView(){
            currentVisibleView = METRIC_UNITS_VIEW
        binding?.etMetricUnitWeight?.hint = "Weight (in Kgs)"
        binding?.etMetricUnitWeight?.text!!.clear()
        binding?.etMetricUnitHeight?.visibility = View.VISIBLE
        binding?.etUSUnitHeight?.text!!.clear()
        binding?.etUSUnitHeightInches?.text!!.clear()
        binding?.USUnitsHeight?.visibility = View.GONE
        binding?.displayBMIResult?.visibility = View.INVISIBLE
    }
    private fun makeVisibleUsUnitsView(){
        currentVisibleView = US_UNITS_VIEW
        binding?.etMetricUnitWeight?.hint = "Weight (in Pounds)"
        binding?.etMetricUnitWeight?.text!!.clear()
        binding?.etMetricUnitHeight?.visibility = View.GONE
        binding?.etMetricUnitHeight?.text!!.clear()
        binding?.USUnitsHeight?.visibility = View.VISIBLE
        binding?.displayBMIResult?.visibility = View.INVISIBLE

    }
    private fun displayBMIResult(bmi: Float){

        val bmiType: String
        val bmiDescription: String

        if(bmi.compareTo(16f)<=0){
            bmiType = "Severely UnderWeight"
            bmiDescription = "Oops! You really need to take better care of yourself! Take good Diet."
        }
        else if(bmi.compareTo(18.5f)<=0){
            bmiType = "UnderWeight"
            bmiDescription = "You have to take yourself a little more.Eat little more"
        }
        else if(bmi.compareTo(25f)<=0){
            bmiType = "Normal"
            bmiDescription = "You are in a good shape.Maintain this posture of yourself"

        }
        else{
            bmiType = "Overweight"
            bmiDescription = "Loose adequate amount of fat to get fit."
        }

        binding?.displayBMIResult?.visibility = View.VISIBLE
        binding?.tvBMIValue?.text  = BigDecimal(bmi.toDouble()).setScale(2,RoundingMode.HALF_EVEN).toString()
        binding?.tvBMIType?.text  = bmiType
        binding?.tvBMIDescription?.text = bmiDescription



    }
    private fun validateMetricUnits(): Boolean{
        var isValid = true
        if(binding?.etMetricUnitWeight?.text.toString().isEmpty()){
            isValid = false
        }
        else if(binding?.etMetricUnitHeight?.text.toString().isEmpty()){
            isValid= false
        }
        return isValid
    }
    private fun validateUSUnits(): Boolean{
        var isValid = true
        when {
            binding?.etMetricUnitWeight?.text.toString().isEmpty() -> {
                isValid = false
            }
            binding?.etUSUnitHeight?.text.toString().isEmpty() -> {
                isValid = false
            }
            binding?.etUSUnitHeightInches?.text.toString().isEmpty() ->{
                isValid = false
            }
        }
        return isValid
    }

    private fun calculateUnits(){
        val weight :Float = binding?.etMetricUnitWeight?.text.toString().toFloat()
        if(currentVisibleView == METRIC_UNITS_VIEW){
            if(validateMetricUnits()){
                val height : Float = binding?.etMetricUnitHeight?.text.toString().toFloat() /100


                val bmi = weight/(height*height)
                displayBMIResult(bmi)
            }
            else{ Toast.makeText(this@BMIActivity,"Please Enter Valid Values.", Toast.LENGTH_SHORT).show()
            }
        }
        else{
            if(validateUSUnits()){
               val heightFeet: String= binding?.etUSUnitHeight?.text.toString()
               val heightInches: String= binding?.etUSUnitHeightInches?.text.toString()
               val heightInInches = heightInches.toFloat() + heightFeet.toFloat() *12
                val bmi = 703 *(weight/(heightInInches*heightInInches))
                displayBMIResult(bmi)
            }
            else{
                Toast.makeText(this@BMIActivity,"Please Enter valid values.",Toast.LENGTH_SHORT).show()
            }
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}