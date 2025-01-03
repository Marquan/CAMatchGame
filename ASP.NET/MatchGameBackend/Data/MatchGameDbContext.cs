using Microsoft.EntityFrameworkCore;
using MatchGameBackend.Models;
using Microsoft.AspNetCore.Mvc;
using MatchGameBackend.Data;

namespace MatchGameBackend.Data
{
    public class MatchGameDbContext : DbContext
    {
        public MatchGameDbContext(DbContextOptions<MatchGameDbContext> options) : base(options) { }
        public DbSet<User> Users { get; set; }
        public DbSet<Game> Games { get; set; }
        public DbSet<Score> Scores { get; set; }
   
        protected override void OnConfiguring(DbContextOptionsBuilder optionsBuilder)
        {
            if (!optionsBuilder.IsConfigured)
            {
                optionsBuilder.UseMySql(
                    "server=localhost;user=root;password=password;database=memorygame;", // Matches your MySQL database name
                    new MySqlServerVersion(new Version(8, 0, 38))
                );

                optionsBuilder.UseLazyLoadingProxies();
            }
        }

        protected override void OnModelCreating(ModelBuilder modelBuilder)
        {
            base.OnModelCreating(modelBuilder);

            // Value conversion for IsPaidUser (bool to 0/1)
            modelBuilder.Entity<User>(entity =>
            {
                entity.Property(e => e.IsPaidUser)
                      .HasConversion<int>(); // Converts bool to 0/1 in the database
            });
        }
    }
}


