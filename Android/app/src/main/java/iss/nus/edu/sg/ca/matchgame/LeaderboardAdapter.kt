package iss.nus.edu.sg.ca.matchgame

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import iss.nus.edu.sg.ca.matchgame.databinding.LeaderboardBinding

class LeaderboardAdapter : RecyclerView.Adapter<LeaderboardAdapter.LeaderboardViewHolder>() {
    private val scores = mutableListOf<Score>()

    data class Score(
        val username: String,
        val completionTime: Long,
        val formattedTime: String
    )

    inner class LeaderboardViewHolder(private val binding: LeaderboardBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(score: Score, position: Int) {
            binding.tvRank.text = "${position + 1}"
            binding.tvUsername.text = score.username
            binding.tvTime.text = score.formattedTime

            // 设置排名颜色
            binding.tvRank.setTextColor(
                when (position) {
                    0 -> Color.parseColor("#FFD700") // 金色
                    1 -> Color.parseColor("#C0C0C0") // 银色
                    2 -> Color.parseColor("#CD7F32") // 铜色
                    else -> Color.BLACK
                }
            )
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeaderboardViewHolder {
        val binding = ItemLeaderboardBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return LeaderboardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LeaderboardViewHolder, position: Int) {
        holder.bind(scores[position], position)
    }

    override fun getItemCount() = scores.size

    fun submitList(newScores: List<Score>) {
        scores.clear()
        scores.addAll(newScores)
        notifyDataSetChanged()
    }
}