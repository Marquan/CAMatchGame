using Microsoft.AspNetCore.Mvc;
using MatchGameBackend.Data;
using MatchGameBackend.Models;
using System.Linq;
using MatchGameBackend.DTO;
using Microsoft.EntityFrameworkCore;
namespace MatchGameBackend.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class UsersController : ControllerBase
    {
        private readonly MatchGameDbContext _context;

        public UsersController(MatchGameDbContext context)
        {
            _context = context;
        }

        // POST: api/Users/Login
        [HttpPost("Login")]
        public IActionResult Login([FromBody] User loginRequest)
        {
            {
                Console.WriteLine($"Received Username: {loginRequest.Username}, Password: {loginRequest.Password}");

                if (loginRequest == null || string.IsNullOrEmpty(loginRequest.Username) || string.IsNullOrEmpty(loginRequest.Password))
                {
                    return BadRequest(new { success = false, message = "Invalid username or password." });
                }

                var user = _context.Users
                    .FirstOrDefault(u => u.Username.ToLower().Trim() == loginRequest.Username.ToLower().Trim()
                                      && u.Password.ToLower().Trim() == loginRequest.Password.ToLower().Trim());

                if (user == null)
                {
                    return Unauthorized(new { success = false, message = "Invalid credentials." });
                }

                return Ok(new
                {
                    status = true,
                    message = "Login successful"
                });
            }
        }

        [HttpGet("GetUser")]
        public IActionResult GetUser(string username)
        {
            var user = _context.Users.FirstOrDefault(u => u.Username == username);
            if (user == null) return NotFound();
            return Ok(user);
        }

        [HttpPut("update-timer")]
        public async Task<IActionResult> UpdateTimer([FromBody] UpdateTimerRequest request)
        {
            var user = await _context.Users.FindAsync(request.UserId);
            if (user == null)
            {
                return NotFound(new { Message = "User not found" });
            }

            user.GameTime = request.GameTime;
            await _context.SaveChangesAsync();

            return Ok(new { Message = "Game time updated successfully", GameTime = user.GameTime });
        }
    }
}
