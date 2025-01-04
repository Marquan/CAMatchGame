using System.Collections.Generic;
using MySql.Data.MySqlClient;
using MatchGameBackend.Models;

namespace MatchGameBackend.LeaderService
{
    public class LeaderDAO
    {
        private const string CONNECTION_STRING = "Server=localhost;Database=MemoryGame;User=root;Password=password;";

        public List<User> GetTopUsers()
        {
            List<User> users = new List<User>();

            try
            {
                using (MySqlConnection conn = new MySqlConnection(CONNECTION_STRING))
                {
                    conn.Open();
                    string query = "SELECT id, name, game_time FROM users ORDER BY game_time ASC";

                    using (MySqlCommand cmd = new MySqlCommand(query, conn))
                    using (MySqlDataReader reader = cmd.ExecuteReader())
                    {
                        while (reader.Read())
                        {
                            User user = new User
                            {
                                UserId = reader.GetInt32("id"),
                                Username = reader.GetString("name"),
                                Password = reader.GetString("password"),
                                GameTime = reader.GetInt32("game_time")
                            };
                            
                            users.Add(user);
                        }
                    }
                }
            }
            catch (Exception ex)
            {
                Console.WriteLine($"Error: {ex.Message}");
            }

            return users;
        }
    }
}