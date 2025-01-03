package iss.nus.edu.sg.ca.matchgame

import android.view.LayoutInflater
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import iss.nus.edu.sg.ca.matchgame.FetchActivity
import iss.nus.edu.sg.ca.matchgame.LeaderboardAdapter
import iss.nus.edu.sg.ca.matchgame.LeaderboardItemAnimator
import iss.nus.edu.sg.ca.matchgame.R

class LeaderboardManager(private val activity: FetchActivity) {
    private val leaderboardAdapter = LeaderboardAdapter()
    private var leaderboardDialog: AlertDialog? = null

    fun setupListeners() {
        activity.binding.btnShowLeaderboard.setOnClickListener {
            showLeaderboard()
        }
    }

    fun observeViewModel() {
        activity.viewModel.leaderboard.observe(activity) { result ->
            when (result) {
                is Resource.Success -> {
                    leaderboardAdapter.submitList(result.data ?: emptyList())
                }
                is Resource.Error -> {
                    Toast.makeText(activity, result.message, Toast.LENGTH_SHORT).show()
                }
                is Resource.Loading -> {
                    // 可以添加加载动画
                }
            }
        }
    }

    private fun showLeaderboard() {
        val dialogView = LayoutInflater.from(activity)
            .inflate(R.layout.dialog_leaderboard, null)

        val recyclerView = dialogView.findViewById<RecyclerView>(R.id.rvLeaderboard)
        val progressBar = dialogView.findViewById<ProgressBar>(R.id.progressBar)
        val swipeRefresh = dialogView.findViewById<SwipeRefreshLayout>(R.id.swipeRefresh)
        val emptyView = dialogView.findViewById<View>(R.id.emptyView)

        recyclerView.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = leaderboardAdapter
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            itemAnimator = LeaderboardItemAnimator()
        }

        swipeRefresh.setOnRefreshListener {
            activity.viewModel.fetchLeaderboard()
        }

        leaderboardDialog = AlertDialog.Builder(activity)
            .setView(dialogView)
            .setTitle("排行榜")
            .setPositiveButton("关闭", null)
            .create()

        activity.viewModel.leaderboard.observe(activity) { result ->
            when (result) {
                is Resource.Success -> {
                    progressBar.visibility = View.GONE
                    swipeRefresh.isRefreshing = false
                    val scores = result.data ?: emptyList()
                    leaderboardAdapter.submitList(scores)
                    emptyView.visibility = if (scores.isEmpty()) View.VISIBLE else View.GONE
                    recyclerView.visibility = if (scores.isEmpty()) View.GONE else View.VISIBLE
                }
                is Resource.Error -> {
                    progressBar.visibility = View.GONE
                    swipeRefresh.isRefreshing = false
                    Toast.makeText(activity, result.message, Toast.LENGTH_SHORT).show()
                }
                is Resource.Loading -> {
                    if (!swipeRefresh.isRefreshing) {
                        progressBar.visibility = View.VISIBLE
                    }
                }
            }
        }

        activity.viewModel.fetchLeaderboard()
        leaderboardDialog?.show()
    }

    fun onDestroy() {
        leaderboardDialog?.dismiss()
    }
}