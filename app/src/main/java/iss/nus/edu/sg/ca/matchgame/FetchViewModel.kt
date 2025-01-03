package iss.nus.edu.sg.ca.matchgame

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Job

@HiltViewModel
class FetchViewModel @Inject constructor(
    private val repository: ImageRepository,
    private val gameRepository: GameRepository
) : ViewModel() {

    private val _images = MutableLiveData<Resource<List<String>>>()
    val images: LiveData<Resource<List<String>>> = _images

    private val _downloadProgress = MutableLiveData<Int>()
    val downloadProgress: LiveData<Int> = _downloadProgress

    private var fetchJob: Job? = null

    private val _leaderboard = MutableLiveData<Resource<List<LeaderboardAdapter.Score>>>()
    val leaderboard: LiveData<Resource<List<LeaderboardAdapter.Score>>> = _leaderboard

    fun fetchImages(url: String) {
        fetchJob?.cancel()
        fetchJob = viewModelScope.launch {
            _images.value = Resource.Loading(true)
            try {
                repository.fetchImages(url) { progress ->
                    _downloadProgress.postValue(progress)
                }.collect { imageUrls ->
                    _images.value = Resource.Success(imageUrls)
                }
            } catch (e: Exception) {
                _images.value = Resource.Error(e.message ?: "获取图片失败")
            } finally {
                _images.value = Resource.Loading(false)
            }
        }
    }

    fun fetchLeaderboard() {
        viewModelScope.launch {
            _leaderboard.value = Resource.Loading(true)
            try {
                val scores = gameRepository.getTopScores()
                _leaderboard.value = Resource.Success(scores.map { score ->
                    LeaderboardAdapter.Score(
                        username = score.username,
                        completionTime = score.completionTime,
                        formattedTime = formatTime(score.completionTime)
                    )
                })
            } catch (e: Exception) {
                _leaderboard.value = Resource.Error(e.message ?: "获取排行榜失败")
            } finally {
                _leaderboard.value = Resource.Loading(false)
            }
        }
    }

    private fun formatTime(timeInMillis: Long): String {
        val seconds = timeInMillis / 1000
        val minutes = seconds / 60
        val remainingSeconds = seconds % 60
        return String.format("%02d:%02d", minutes, remainingSeconds)
    }
}