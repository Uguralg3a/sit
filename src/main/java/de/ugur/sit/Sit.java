package de.ugur.sit;


import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.spigotmc.event.entity.EntityDismountEvent;

import java.util.ArrayList;

public final class Sit extends JavaPlugin implements Listener {

    private static ArrayList<Player> sitting = new ArrayList<>();

    @Override
    public void onEnable() {
        getCommand("sit").setExecutor(this);
        getServer().getPluginManager().registerEvents(this, this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @EventHandler
    public void onDismount(EntityDismountEvent event) {
        if(!(event.getEntity() instanceof Player))
            return;

            Player player = (Player) event.getEntity();

            if (!(event.getDismounted() instanceof ArmorStand))
                return;

            sitting.remove(player);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player)){
            sender.sendMessage("Nur Spieler können diesen Befehl nutzen!");
            return true;
        }

        Player player = (Player) sender;

        if (!player.isOnGround()) {
            player.sendMessage("Du kannst dich nicht in der Luft hinsetzen!");
            return true;
        }

        if(sitting.contains(player)) {
            player.sendMessage("Du bist bereits am fliegen!");
            return true;
        }

        sitting.add(player);

        Location location = player.getLocation();
        World world = location.getWorld();
        ArmorStand chair = (ArmorStand) world.spawnEntity(location, EntityType.ARMOR_STAND);

        chair.setGravity(false);
        chair.setVisible(false);
        chair.setInvulnerable(false);
        chair.setPassenger(player);
        return true;
    }
}
