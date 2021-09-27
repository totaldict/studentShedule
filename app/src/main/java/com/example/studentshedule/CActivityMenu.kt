package com.example.studentshedule

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import com.example.studentshedule.databinding.ActivityLoginBinding
import com.example.studentshedule.databinding.ActivityMenuBinding

class CActivityMenu : AppCompatActivity() {
    private lateinit var binding: ActivityMenuBinding

    var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // There are no request codes
            val data: Intent? = result.data
            val x = data?.getIntExtra("PARAM_123", 0)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val a = intent.getStringExtra("LOGIN")
        binding.textView.text = a

        binding.button.setOnClickListener {
            val intent = Intent(this, CActivityStudentInfo::class.java)
            resultLauncher.launch(intent)
        }


    }
}