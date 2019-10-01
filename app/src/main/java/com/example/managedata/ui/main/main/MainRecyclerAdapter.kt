package com.example.managedata.ui.main.main

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.managedata.R
import com.example.managedata.data.Monster

class MainRecyclerAdapter(
    val context: Context,
    val monsters: List<Monster>,
    val itemListener: MonsterItemListener
) :
    RecyclerView.Adapter<MainRecyclerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.monster_grid_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = monsters.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val monster = monsters[position]
        with(holder) {
            nameText?.let {
                it.text = monster.monsterName
                it.contentDescription = monster.description
            }
            ratingBar?.rating = monster.scariness.toFloat()
            Glide.with(context)
                .load(monster.thumbnailUrl)
                .into(monsterImage!!)

            itemView.setOnClickListener {
                itemListener.onMonsterItemClick(monster)
            }
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameText: TextView? = itemView.findViewById(R.id.nameText)
        val monsterImage: ImageView? = itemView.findViewById(R.id.monsterImage)
        val ratingBar: RatingBar? = itemView.findViewById(R.id.ratingBar)
    }

    interface MonsterItemListener {
        fun onMonsterItemClick(monster: Monster)
    }
}