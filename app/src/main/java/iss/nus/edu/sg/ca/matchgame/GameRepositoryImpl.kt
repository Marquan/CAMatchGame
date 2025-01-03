package iss.nus.edu.sg.ca.matchgame

class GameRepositoryImpl @Inject constructor(
    private val api: GameApi
) : GameRepository {

    override suspend fun getTopScores(): List<Score> {
        val response = api.getTopScores()
        if (response.isSuccessful) {
            return response.body() ?: emptyList()
        } else {
            throw Exception("获取排行榜失败: ${response.message()}")
        }
    }

    override suspend fun saveScore(username: String, completionTime: Long): Score {
        val response = api.saveScore(SaveScoreRequest(username, completionTime))
        if (response.isSuccessful) {
            return response.body() ?: throw Exception("保存分数失败")
        } else {
            throw Exception("保存分数失败: ${response.message()}")
        }
    }
}