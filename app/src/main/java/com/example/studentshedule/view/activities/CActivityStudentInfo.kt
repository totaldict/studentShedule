package com.example.studentshedule.view.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.studentshedule.R
import com.example.studentshedule.databinding.ActivityLoginBinding
import com.example.studentshedule.databinding.ActivityStudentInfoBinding

class CActivityStudentInfo : AppCompatActivity() {

    private lateinit var binding: ActivityStudentInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStudentInfoBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // чтение данных из SharedPreferences
        val sharedPref = applicationContext.getSharedPreferences(getString(R.string.FILE_NAME_PREFERENCES), Context.MODE_PRIVATE)
//      val defaultValue = resources.getInteger(R.integer.saved_high_score_default_key)
        val login = sharedPref.getString(getString(R.string.PARAM_LOGIN), "")

        binding.textViewLoginShow.text = login

        binding.button2.setOnClickListener {
            val intent = Intent()
            intent.putExtra("PARAM_ACTIVITY_NAME", "CActivityStudentInfo")
            setResult(RESULT_OK, intent)
            finish()
        }
    }
}