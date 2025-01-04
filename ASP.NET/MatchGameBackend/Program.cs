using MatchGameBackend.Data;
using MatchGameBackend.LeaderService;
using MatchGameBackend.Models;
using Microsoft.EntityFrameworkCore;

var builder = WebApplication.CreateBuilder(args);

// Add services to the container.
builder.Services.AddControllers();
builder.Services.AddControllersWithViews();  // Add MVC support for rendering views
builder.Services.AddScoped<LeaderDAO>();

builder.Services.AddDbContext<MatchGameDbContext>(options =>
    options.UseMySql(
        builder.Configuration.GetConnectionString("DefaultConnection"),
        new MySqlServerVersion(new Version(8, 0, 38)) // Replace with your MySQL version
    ).UseLazyLoadingProxies()
);

//CORS policy to allow requests from any origin
builder.Services.AddCors(options =>
{
    options.AddDefaultPolicy(policy =>
    {
        policy.AllowAnyOrigin()   // Allow requests from any origin
              .AllowAnyHeader()   // Allow any headers (e.g., JSON, Authorization)
              .AllowAnyMethod();  // Allow all HTTP methods (GET, POST, etc.)
    });
});

var app = builder.Build();

InitializeDatabase(app);

app.UseHttpsRedirection();

app.UseAuthorization();

// Default route for MVC
app.MapControllerRoute(
    name: "default",
    pattern: "{controller=Home}/{action=Index}/{id?}");

// Map API routes 
app.MapControllers();

app.Run();

void InitializeDatabase(WebApplication app)
{
    using (var scope = app.Services.CreateScope())
    {
        var dbContext = scope.ServiceProvider.GetRequiredService<MatchGameDbContext>();

        dbContext.Database.EnsureDeleted();  
        dbContext.Database.EnsureCreated(); 

        SeedDatabase(dbContext);
    }
}

void SeedDatabase(MatchGameDbContext dbContext)
{
    // Add sample users
    dbContext.Users.Add(new User
    {
        Username = "freeuser",
        Password = "password123",
        IsPaidUser = false,
        GameTime = 120
    });
    dbContext.Users.Add(new User
    {
        Username = "paiduser",
        Password = "password123",
        IsPaidUser = true,
        GameTime = 90
    });

    dbContext.SaveChanges();
}
