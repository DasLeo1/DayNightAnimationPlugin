package de.crafterslife.dayNightAnimationPlugin;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class DayNightAnimationPlugin extends JavaPlugin {

    @Override
    public void onEnable() {

        getLogger().info("Starting DayNightAnimationPlugin...");

        this.getCommand("day").setExecutor(new DayCommand());
        this.getCommand("night").setExecutor(new NightCommand());
    }

    public class DayCommand implements CommandExecutor {
        @Override
        public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
            World world = Bukkit.getWorlds().get(0);  // Hauptwelt
            animateTimeChange(world, 0);  // 0 = Tageszeit
            sender.sendMessage("§6Die Zeit wird auf §eTag§6 gesetzt!");
            return true;
        }
    }

    public class NightCommand implements CommandExecutor {
        @Override
        public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
            World world = Bukkit.getWorlds().get(0);  // Hauptwelt
            animateTimeChange(world, 13000);  // 13000 = Nachtzeit
            sender.sendMessage("§6Die Zeit wird auf §9Nacht§6 gesetzt!");
            return true;
        }
    }

    private void animateTimeChange(World world, long targetTime) {
        new BukkitRunnable() {
            long currentTime = world.getTime();
            long step = (targetTime > currentTime) ? 100 : -100; // Schrittweite für die Animation
            int animationLength = 20; // Anzahl der Schritte in der Animation
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
