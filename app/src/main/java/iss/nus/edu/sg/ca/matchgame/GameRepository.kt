package iss.nus.edu.sg.ca.matchgame

interface GameRepository {
    suspend fun getTopScores(): List<Score>
    suspend fun saveScore(username: String, completionTime: Long): Score
}