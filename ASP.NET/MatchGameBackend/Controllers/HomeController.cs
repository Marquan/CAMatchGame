using Microsoft.AspNetCore.Mvc;

namespace MatchGameBackend.Controllers
{
    public class HomeController : Controller
    {
        public IActionResult Index()
        {
            return View();
        }
    }
}
