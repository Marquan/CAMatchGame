using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace MatchGameBackend.Models
{
    public class Game
    {
        public Game()
        {
            GameId = Guid.NewGuid().ToString();
        }
        [Key]
        public string GameId { get; set; }

        [Required]
        public int Duration { get; set; }

        [ForeignKey("User")]
        public string UserId { get; set; }
        public virtual User User { get; set; }

    }





}
