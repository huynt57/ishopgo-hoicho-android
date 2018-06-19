package ishopgo.com.exhibition.ui.main.home.post.question

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.SpinnerAdapter
import android.widget.TextView
import ishopgo.com.exhibition.R
import ishopgo.com.exhibition.model.question.QuestionCategory

class SpQuestionAdapter(private var categoryList: List<QuestionCategory>) : BaseAdapter(), SpinnerAdapter {

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.item_spinner_category, parent, false)
        val textView = view.findViewById(R.id.tv_category) as TextView
        textView.text = categoryList[position].name
        return view
    }

    override fun getItem(position: Int): Any {
        return categoryList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return categoryList.size
    }
}