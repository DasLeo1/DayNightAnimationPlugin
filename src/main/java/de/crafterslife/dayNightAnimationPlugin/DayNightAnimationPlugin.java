package de.crafterslife.dayNightAnimationPlugin;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class DayNightAnimationPlugin extends JavaPlugin {

    @Override
    public void onEnable() {

        getLogger().info("DayNightAnimationPlugin has been enabled ...");

        Objects.requireNonNull(this.getCommand("day")).setExecutor(new DayCommand());
        Objects.requireNonNull(this.getCommand("night")).setExecutor(new NightCommand());
    }

    public class DayCommand implements CommandExecutor {
        @Override
        public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
            if (!(sender instanceof Player player)) {
                sender.sendMessage("§cNur Spieler können diesen Befehl verwenden.");
                return true;
            }

            if (!player.hasPermission("daynightanimation.day")) {
                player.sendMessage("§cDu hast keine Berechtigung, die Zeit auf Tag zu setzen.");
                return true;
            }

            World world = player.getWorld();
            animateTimeChange(world, 0);  // 0 = Tageszeit
            player.sendMessage("§6Die Zeit wird auf §eTag§6 gesetzt!");
            return true;
        }
    }

    public class NightCommand implements CommandExecutor {
        @Override
        public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
            if (!(sender instanceof Player player)) {
                sender.sendMessage("§cNur Spieler können diesen Befehl verwenden.");
                return true;
            }

            if (!player.hasPermission("daynightanimation.night")) {
                player.sendMessage("§cDu hast keine Berechtigung, die Zeit auf Nacht zu setzen.");
                return true;
            }

            World world = player.getWorld();
            animateTimeChange(world, 13000);  // 13000 = Nachtzeit
            player.sendMessage("§6Die Zeit wird auf §9Nacht§6 gesetzt!");
            return true;
        }
    }

    private void animateTimeChange(World world, long targetTime) {
        new BukkitRunnable() {
            long currentTime = world.getTime();
            final long step = (targetTime > currentTime) ? 100 : -100; // Schrittweite für die Animation
            final int animationLength = 20; // Anzahl der Schritte in der Animation
            int steps = 0;

            @Override
            public void run() {
                if ((step > 0 && currentTime >= targetTime) || (step < 0 && currentTime <= targetTime) || steps >= animationLength) {
                    world.setTime(targetTime); // Zielzeit setzen
                    cancel();
                    return;
                }

                currentTime += step; // Zeit schrittweise ändern
                world.setTime(currentTime);
                steps++;
            }
        }.runTaskTimer(this, 0L, 1L); // 1 Tick Pause zwischen jedem Schritt
    }
}