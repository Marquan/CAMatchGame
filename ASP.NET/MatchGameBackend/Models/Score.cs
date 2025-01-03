using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace MatchGameBackend.Models
{
    public class Score
    {
        public Score()
        {
            ScoreId = Guid.NewGuid().ToString();
        }

        [Key]
        public string ScoreId { get; set; }

        [Required]
        public int Points { get; set; }

        [ForeignKey("User")]
        public string UserId { get; set; }
        public virtual User User { get; set; }

    }
}
