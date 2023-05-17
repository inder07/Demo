package com.sportzinteractive.demo.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sportzinteractive.demo.models.MatchDetails
import com.sportzinteractive.demo.R

class CustomAdapter(private val mList: List<MatchDetails>) :
    RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_view_design, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val matchDetails = mList[position]
        var appendingText = ""
        if (matchDetails.Iscaptain)
            appendingText = " (c)"
        if (matchDetails.Iskeeper)
            appendingText = " (wk)"
        holder.textView.text = matchDetails.Name_Full.plus(appendingText)

        holder.itemView.setOnClickListener {
            onItemClickListener?.let {
                it(matchDetails)
            }
        }

    }

    override fun getItemCount(): Int {
        return mList.size
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val textView: TextView = itemView.findViewById(R.id.textView)
    }

    private var onItemClickListener: ((MatchDetails) -> Unit)? = null

    fun setOnItemClickListener(listener: (MatchDetails) -> Unit) {
        onItemClickListener = listener
    }
}
