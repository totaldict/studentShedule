package com.example.studentshedule.view.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.studentshedule.R
import com.example.studentshedule.databinding.ActivityLessonListBinding
import com.example.studentshedule.model.CLesson
import com.example.studentshedule.view.adapters.CRecyclerViewLessonListAdapter
import org.joda.time.LocalDateTime
import org.joda.time.format.DateTimeFormat

class CActivityLessonList : AppCompatActivity() {
    private lateinit var binding: ActivityLessonListBinding
    private val lessons = ArrayList<CLesson>()
    // Обработчик закрытия окон
    var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // There are no request codes
            val data: Intent? = result.data
            val activity = data?.getStringExtra("PARAM_ACTIVITY_NAME")
            when (activity) {
                "CActivityLesson" -> {
                    val index = data.getIntExtra("PARAM_LESSON_INDEX", -1)
                    val subject = data.getStringExtra("PARAM_LESSON_SUBJECT")?:""
                    val sDateTime = data.getStringExtra("PARAM_LESSON_DATE")

                    val lesson =lessons[index]
                    lesson.subject = subject
                    lesson.dateTime = LocalDateTime.parse(sDateTime)

                    //актуализаци изображения, чтобы перерисовалось!
                    binding.rvLessonList.adapter?.notifyItemChanged(index)

                }
                "CActivityStudentInfo" -> {
                    val x = data?.getIntExtra("PARAM_123", 0)
                }
            }

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLessonListBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //val a = intent.getStringExtra("LOGIN")
        //binding.textView.text = a

        val formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm")

        lessons.add(CLesson("Математика", LocalDateTime.parse("2021-09-30 08:00", formatter)))
        lessons.add(CLesson("Численные методы", LocalDateTime.parse("2021-09-30 09:45", formatter)))
        lessons.add(CLesson("Физ-ра", LocalDateTime.parse("2021-09-30 11:30", formatter)))

        // Обработчик клика на элемент списка открывает форму редактирования/просмотра выбранного элемента
        val listener = object : CRecyclerViewLessonListAdapter.IClickListener {
            override fun onItemClick(lesson: CLesson, index: Int) {
                //Toast.makeText(this@CActivityLessonList, lesson.subject, Toast.LENGTH_SHORT).show()

                val intent = Intent(this@CActivityLessonList, CActivityLesson::class.java)
                intent.putExtra("PARAM_LESSON_SUBJECT", lesson.subject)
                intent.putExtra("PARAM_LESSON_DATE", lesson.dateTime.toString())
                intent.putExtra("PARAM_LESSON_INDEX", index)
                resultLauncher.launch(intent)
            }

            override fun onItemDeleteClick(lesson : CLesson, index: Int) {
                lessons.removeAt((index))
                binding.rvLessonList.adapter?.notifyItemRemoved(index)
            }
        }


        val adapter = CRecyclerViewLessonListAdapter(lessons, listener)
        binding.rvLessonList.adapter = adapter

        binding.rvLessonList.layoutManager = LinearLayoutManager(this) // выводит в виде списка

//        binding.fabAddLesson.setOnClickListener {
//            val lesson = CLesson("", LocalDateTime.now())
//            lessons.add(lesson)
//
//            val intent = Intent(this@CActivityLessonList, CActivityLesson::class.java)
//            intent.putExtra("PARAM_LESSON_SUBJECT", lesson.subject)
//            intent.putExtra("PARAM_LESSON_DATE", lesson.dateTime.toString())
//            intent.putExtra("PARAM_LESSON_INDEX", lessons.size-1)
//            resultLauncher.launch(intent)
//        }
        // нижнее меню
        binding.bottomNavigationLessonList.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.miExit -> {
                    finishAffinity()
                    true
                }
                R.id.miAddLesson -> {
                    val lesson = CLesson("", LocalDateTime.now())
                    lessons.add(lesson)

                    val intent = Intent(this@CActivityLessonList, CActivityLesson::class.java)
                    intent.putExtra("PARAM_LESSON_SUBJECT", lesson.subject)
                    intent.putExtra("PARAM_LESSON_DATE", lesson.dateTime.toString())
                    intent.putExtra("PARAM_LESSON_INDEX", lessons.size-1)
                    resultLauncher.launch(intent)
                    true
                }
                else -> false
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.activity_lesson_list_menu, menu)

        //var c = getColor(R.color.design_default_color_primary)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.miAccountDetails -> {
                val intent = Intent(this, CActivityStudentInfo::class.java)
                resultLauncher.launch(intent)
                true
            }
            R.id.miExit -> {
                //затираем данные о пользователе
                val sharedPref = applicationContext.getSharedPreferences(getString(R.string.FILE_NAME_PREFERENCES), Context.MODE_PRIVATE)
                with (sharedPref.edit()) {
                    putString(getString(R.string.PARAM_LOGIN), "")
                    apply()
                }

                finishAffinity()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}