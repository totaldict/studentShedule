package com.example.studentshedule.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.studentshedule.databinding.LessonListItemBinding
import com.example.studentshedule.model.CLesson
import org.joda.time.format.DateTimeFormat

class CRecyclerViewLessonListAdapter (
    private var lessons: List<CLesson>,
    private val listener: IClickListener?
    ) :
    RecyclerView.Adapter<CRecyclerViewLessonListAdapter.ViewHolder>() {

    val formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm")

    fun updateData(lessons : List<CLesson>)
    {
        // тут лучше проверку, отличается или нет. Если нет - то не делаем.
        this.lessons = lessons
        // уведомляем форму что данные изменились и надо их обновить на форме
        notifyDataSetChanged()
    }

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    inner class ViewHolder( val binding: LessonListItemBinding)
        : RecyclerView.ViewHolder(binding.root)
    {
            var lesson: CLesson? = null
        }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val binding = LessonListItemBinding
            .inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.binding.tvSubject.text = lessons[position].subject
        viewHolder.binding.tvDateTime.text = lessons[position].dateTime.toString(formatter)
        viewHolder.lesson = lessons[position]

        viewHolder.binding.llLesson.setOnClickListener {
            viewHolder.lesson?.let {
                listener?.onItemClick(it, lessons.indexOf(it))
            }
        }
        viewHolder.binding.buttonDeleteLesson.setOnClickListener {
            viewHolder.lesson?.let {
                listener?.onItemDeleteClick(it, lessons.indexOf(it))
            }
        }
    }

    interface IClickListener {
        fun onItemClick(lesson : CLesson, index: Int)
        fun onItemDeleteClick(lesson : CLesson, index: Int)
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = lessons.size

}