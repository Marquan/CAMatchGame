package iss.nus.edu.sg.ca.matchgame

data class Score(
    val id: String,
    val username: String,
    val completionTime: Long,
    val timestamp: Long = System.currentTimeMillis()
)