package com.sportzinteractive.demo.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.sportzinteractive.demo.databinding.CardViewDesignBinding
import com.sportzinteractive.demo.models.MatchDetails

class CustomAdapter :
    RecyclerView.Adapter<CustomAdapter.CustomAdapterViewHolder>() {

    inner class CustomAdapterViewHolder(val binding: CardViewDesignBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val differCallback = object : DiffUtil.ItemCallback<MatchDetails>() {
        override fun areItemsTheSame(
            oldItem: MatchDetails,
            newItem: MatchDetails
        ): Boolean {
            return oldItem.Name_Full == newItem.Name_Full
        }

        override fun areContentsTheSame(
            oldItem: MatchDetails,
            newItem: MatchDetails
        ): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomAdapterViewHolder {
        val binding =
            CardViewDesignBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CustomAdapterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CustomAdapterViewHolder, position: Int) {
        val matchDetails =  differ.currentList[position]
        holder.binding.apply {
            var appendingText = ""
            if (matchDetails.Iscaptain)
                appendingText = " (c)"
            if (matchDetails.Iskeeper)
                appendingText = " (wk)"
            textView.text = matchDetails.Name_Full.plus(appendingText)

            root.setOnClickListener {
                onItemClickListener?.let {
                    it(matchDetails)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickListener: ((MatchDetails) -> Unit)? = null

    fun setOnItemClickListener(listener: (MatchDetails) -> Unit) {
        onItemClickListener = listener
    }
}
