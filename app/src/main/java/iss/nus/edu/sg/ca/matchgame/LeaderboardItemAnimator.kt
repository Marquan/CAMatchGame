package iss.nus.edu.sg.ca.matchgame

import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView

class LeaderboardItemAnimator : DefaultItemAnimator() {
    override fun animateAdd(holder: RecyclerView.ViewHolder): Boolean {
        holder.itemView.alpha = 0f
        holder.itemView.translationY = holder.itemView.height.toFloat()

        holder.itemView.animate()
            .alpha(1f)
            .translationY(0f)
            .setDuration(300)
            .setInterpolator(DecelerateInterpolator())
            .start()

        return true
    }

    override fun animateRemove(holder: RecyclerView.ViewHolder): Boolean {
        holder.itemView.animate()
            .alpha(0f)
            .translationY(holder.itemView.height.toFloat())
            .setDuration(300)
            .setInterpolator(AccelerateInterpolator())
            .start()

        return true
    }
}