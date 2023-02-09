package beansplusplus;

import beansplusplus.beansgameplugin.*;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.InputStream;
import java.util.List;

// plugin entrypoint - implements GameCreator so that it can be used to create games
public class TemplatePlugin extends JavaPlugin implements GameCreator {

  // this is the basic set up for a game plugin.
  public void onEnable() {
    BeansGamePlugin beansGamePlugin = (BeansGamePlugin) getServer().getPluginManager().getPlugin("BeansGamePlugin");
    beansGamePlugin.registerGame(this); // this method returns the game state if it's required outside of a running game
  }

  @Override
  public Game createGame(GameConfiguration config, GameState state) {
    // TODO: Method that creates an instance of the game of your choice. Must implement 'Game'.
    return new TemplateGame(this, config, state);
  }

  @Override
  public boolean isValidSetup(CommandSender sender, GameConfiguration config) {
    // TODO This should return false and show a message to the command sender if the current config/setup is not satisfactory
    // just make this method return true if there is no validation required.
    if (Bukkit.getOnlinePlayers().size() < 2) {
      sender.sendMessage(ChatColor.DARK_RED + "Required 2 players to start");
      return false;
    }
    return true;
  }

  @Override
  public InputStream config() {
    // points to a yml of the config
    return getResource("config.yml");
  }

  @Override
  public List<String> rulePages() {
    // TODO: rule book. Can feature ChatColors if needed
    return List.of(
        "Rules on page 1",
        ChatColor.BLUE + "Rules on page 2"
    );
  }

  @Override
  public String name() {
    // TODO change the name here
    return "Template game";
  }
}
