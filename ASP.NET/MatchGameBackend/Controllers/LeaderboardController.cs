using MatchGameBackend.LeaderService;
using Microsoft.AspNetCore.Mvc;

namespace MatchGameBackend.Controllers
{
    [Route("api/[controller]")]
    public class LeaderboardController : Controller
    {
        private readonly LeaderDAO _leaderboardService;

        public LeaderboardController(LeaderDAO leaderboardService)
        {
            _leaderboardService = leaderboardService;
        }

        [HttpGet]
        public IActionResult Index()
        {
            var leaderboard = _leaderboardService.GetTopUsers();
            return View(leaderboard);
        }
    }
}
