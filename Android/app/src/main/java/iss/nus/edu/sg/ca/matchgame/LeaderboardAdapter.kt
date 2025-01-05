package iss.nus.edu.sg.ca.matchgame

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import iss.nus.edu.sg.ca.matchgame.data.models.LeaderboardItem

class LeaderboardAdapter(private val leaderboardList: List<LeaderboardItem>) : RecyclerView.Adapter<LeaderboardAdapter.LeaderboardViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeaderboardViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_leaderboard, parent, false)
        return LeaderboardViewHolder(view)
    }

    override fun onBindViewHolder(holder: LeaderboardViewHolder, position: Int) {
        val item = leaderboardList[position]
        holder.usernameTextView.text = item.username
        holder.gameTimeTextView.text = item.gameTime.toString()
        holder.rankTextView.text = (position + 1).toString()
    }

    override fun getItemCount() = leaderboardList.size

    class LeaderboardViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val usernameTextView: TextView = view.findViewById(R.id.username)
        val gameTimeTextView: TextView = view.findViewById(R.id.game_time)
        val rankTextView: TextView = view.findViewById(R.id.rank)
    }
}