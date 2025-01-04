using System.ComponentModel.DataAnnotations;

namespace MatchGameBackend.Models
{
    public class User
    {
        [Key]
        public int UserId { get; set; }

        [Required]
        [MaxLength(25)]
        public string Username { get; set; }

        [Required]
        [MaxLength(25)]
        public string Password { get; set; }

        [Required]
        public bool IsPaidUser { get; set; }

        //New field to store elapsed time for game fragment

        public int GameTime { get; set; }

        //Navigation
        public virtual ICollection<Game> Games { get; set; } = new List<Game>();

    }
}
