package beansplusplus;

import beansplusplus.beansgameplugin.Game;
import beansplusplus.beansgameplugin.GameConfiguration;
import beansplusplus.beansgameplugin.GameState;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import org.bukkit.scoreboard.Team;

// Template game that extends a Game and listens for events
public class TemplateGame implements Game, Listener {
  private TemplatePlugin plugin;
  private GameState state;

  private int someNum;

  public TemplateGame(TemplatePlugin plugin, GameConfiguration config, GameState state) {
    // You will need to keep track of the plugin to setup tasks and event listeners
    this.plugin = plugin;

    // you should keep track of the game state to apply logic whenever the game is paused or to end the game
    this.state = state;

    // get a value from the configuration
    this.someNum = config.getValue("some_number");
  }

  @Override
  public void start() {
    // TODO: start the game. This does some basic setup for the game and registers listeners
    for (Player player : Bukkit.getOnlinePlayers()) {
      player.setHealth(20);
      player.setLevel(0);
      player.setFoodLevel(20);
      player.getInventory().clear();
      player.setGameMode(GameMode.SURVIVAL);
    }

    World world = Bukkit.getWorld("world");
    world.setTime(1000);

    // You will almost certainly need to register a listener. This is how you do it:
    Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
    Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, this::repeatingTask, 20, 20);
    refreshScoreboard();
  }


  @Override
  public void cleanUp() {
    // TODO: whatever needs to be done to stop the game from running.
    // Do not call this method within your game.
    // Use state.gameStop() to end the game and this will be called as a result.
    HandlerList.unregisterAll(this);
    Bukkit.getServer().getScheduler().cancelTasks(plugin);
  }

  private void repeatingTask() {
    // Any game that uses timers or repeating tasks I suggest to use this setup to make the game pause.
    if (state.isPaused()) return;

    // TODO: Implement logic
  }

  // This is a basic method that allows players to always be up to date with the latest scoreboard.
  // Call this method whenever a scoreboard update is required.
  private void refreshScoreboard() {
    Scoreboard scoreboard = getScoreboard();

    for (Player p : Bukkit.getOnlinePlayers()) {
      p.setScoreboard(scoreboard);
    }
  }

  public Scoreboard getScoreboard() {
    // TODO: most games will require a scoreboard of somesort. This is an example of how to make team prefixes
    Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();

    Objective obj = scoreboard.registerNewObjective("scoreboard", "scoreboard", "Scoreboard display name");
    obj.setDisplaySlot(DisplaySlot.PLAYER_LIST);

    Team team1 = scoreboard.registerNewTeam(ChatColor.RED + "Team 1");
    Team team2 = scoreboard.registerNewTeam(ChatColor.BLUE + "Team 2");

    team1.setPrefix(ChatColor.RED + "[Team 1] ");
    team2.setPrefix(ChatColor.BLUE + "[Team 2] ");

    return scoreboard;
  }

  @EventHandler
  public void onPlayerDeath(PlayerDeathEvent e) {
    state.stopGame(); // this causes the game to end. It will also call the cleanUp() method.
  }

  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent e) {
    refreshScoreboard();
  }

  @EventHandler
  public void onPlayerQuit(PlayerQuitEvent e) {
    refreshScoreboard();
  }
}