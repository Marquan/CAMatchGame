using Microsoft.EntityFrameworkCore;
using MatchGameBackend.Models;
using Microsoft.AspNetCore.Mvc;
using MatchGameBackend.Data;

namespace MatchGameBackend.Data
{
    public class ApplicationDbContext : DbContext
    {
        public ApplicationDbContext(DbContextOptions<ApplicationDbContext> options)
            : base(options)
        {
        }

        public DbSet<User> Users { get; set; } // User table
    }
}


