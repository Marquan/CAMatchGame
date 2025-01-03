import okhttp3.Response

interface GameApi {
    @GET("api/scores/top")
    suspend fun getTopScores(): Response<List<Score>>

    @POST("api/scores")
    suspend fun saveScore(@Body request: SaveScoreRequest): Response<Score>
}

data class SaveScoreRequest(
    @SerializedName("username") val username: String,
    @SerializedName("completionTime") val completionTime: Long
)