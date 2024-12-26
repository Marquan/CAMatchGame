using MatchGameBackend.Data;
using Microsoft.EntityFrameworkCore;

var builder = WebApplication.CreateBuilder(args);

// Add services to the container.
builder.Services.AddControllers();
builder.Services.AddControllersWithViews();  // Add MVC support for rendering views

builder.Services.AddDbContext<ApplicationDbContext>(options =>
    options.UseMySql(
        builder.Configuration.GetConnectionString("DefaultConnection"),
        new MySqlServerVersion(new Version(8, 0, 38)) // Replace with your MySQL version
    )
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

app.UseHttpsRedirection();

app.UseAuthorization();

// Default route for MVC
app.MapControllerRoute(
    name: "default",
    pattern: "{controller=Home}/{action=Index}/{id?}");

// Map API routes 
app.MapControllers();

app.Run();
