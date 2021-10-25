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
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.studentshedule.R
import com.example.studentshedule.dao.IDAOLessons
import com.example.studentshedule.databinding.ActivityLessonListBinding
import com.example.studentshedule.model.CLesson
import com.example.studentshedule.util.CDatabase
import com.example.studentshedule.view.adapters.CRecyclerViewLessonListAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.joda.time.LocalDateTime
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter
import java.util.UUID
import kotlin.collections.ArrayList

class CActivityLessonList : AppCompatActivity() {
    private lateinit var binding: ActivityLessonListBinding
    private val lessons = ArrayList<CLesson>()

    private lateinit var adapter: CRecyclerViewLessonListAdapter
    private lateinit var daoLessons: IDAOLessons
    private val formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm")
    // Обработчик закрытия окон
    var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // There are no request codes
            val data: Intent? = result.data
            val activity = data?.getStringExtra("PARAM_ACTIVITY_NAME")
            when (activity) {
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

        // Обработчик клика на элемент списка открывает форму редактирования/просмотра выбранного элемента
        val listener = object : CRecyclerViewLessonListAdapter.IClickListener {
            override fun onItemClick(lesson: CLesson, index: Int) {
                //Toast.makeText(this@CActivityLessonList, lesson.subject, Toast.LENGTH_SHORT).show()

                val intent = Intent(this@CActivityLessonList, CActivityLesson::class.java)
                intent.putExtra(getString(R.string.PARAM_LESSON_ID), lesson.id.toString())

                resultLauncher.launch(intent)
            }

            override fun onItemDeleteClick(lesson : CLesson, index: Int) {
                lifecycleScope.launch {
                    daoLessons.delete(lesson)
                }

            }
        }


        adapter = CRecyclerViewLessonListAdapter(lessons, listener)
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
                    val lesson = CLesson(UUID.randomUUID(), "", LocalDateTime.now())
                    lifecycleScope.launch {
                        daoLessons.insert(lesson)

                        val intent = Intent(this@CActivityLessonList, CActivityLesson::class.java)
                        intent.putExtra(getString(R.string.PARAM_LESSON_ID), lesson.id.toString())
                        resultLauncher.launch(intent)
                    }


                    true
                }
                else -> false
            }
        }

        // получаем БД
        val db = CDatabase.getDatabase(this)
        daoLessons = db.daoLessons()
        lifecycleScope.launch {
            // данные сохранены в БД
            createInitialData(daoLessons)
        }

        // специальный объект, отвечает за отслеживание жизни текущей активности приложения
        lifecycleScope.launch {
            daoLessons.getAllFlow().collect { test_lessons ->
                // Update the UI.
                adapter.updateData(test_lessons)
            }
        }




    }

    // функция будет выполняться в фоновом потоке
    private suspend fun createInitialData(daoLessons : IDAOLessons) = withContext(Dispatchers.IO) // говорит в каком потомке будет выполняться
    {
        // если не пустая БД - то не выставляем
        if (daoLessons.getAll().isNotEmpty())
            return@withContext
        lessons.add(CLesson(UUID.fromString("5ab5d614-96c5-4623-9dd3-02003b9490ef"),"Математика", LocalDateTime.parse("2021-09-30 08:00", formatter)))
        lessons.add(CLesson(UUID.fromString("9504c234-72d4-4985-870b-6184db63cda0"),"Численные методы", LocalDateTime.parse("2021-09-30 09:45", formatter)))
        lessons.add(CLesson(UUID.fromString("182e4e39-dc6a-408f-97bf-1ea8147e02d2"),"Физ-ра", LocalDateTime.parse("2021-09-30 11:30", formatter)))

        lessons.forEach { lesson ->
            daoLessons.insert(lesson)
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