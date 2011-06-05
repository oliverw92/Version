package uk.co.oliwali.Version;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;

public class Permission {
	
	private Version plugin;
	private static PermissionPlugin handler = PermissionPlugin.OP;
	private static PermissionHandler permissionPlugin;
	
	public Permission(Version instance) {
		plugin = instance;
        Plugin permissions = plugin.getServer().getPluginManager().getPlugin("Permissions");
        
        if (permissions != null) {
        	permissionPlugin = ((Permissions)permissions).getHandler();
        	handler = PermissionPlugin.PERMISSIONS;
        	Util.info("Using Permissions for user permissions");
        }
        else {
        	Util.info("No permission handler detected, only ops can use commands");
        }
	}
	
	private static boolean hasPermission(CommandSender sender, String node) {
		if (!(sender instanceof Player))
			return true;
		Player player = (Player)sender;
		switch (handler) {
			case PERMISSIONS:
				return permissionPlugin.has(player, node);
			case OP:
				return player.isOp();
		}
		return false;
	}
	
	public static boolean version(CommandSender player) {
		return hasPermission(player, "version.view");
	}
	
	public static String getPrefix(Player player) {
		String prefix = "&f";
		switch (handler) {
			case PERMISSIONS:
				String permPrefix = permissionPlugin.getGroupPrefix(player.getWorld().getName(), getGroup(player));
				if (permPrefix != null)
					prefix = permPrefix;
				break;
			case OP:
				if (player.isOp())
					prefix = "&c";
				break;
		}
		if (prefix.length() == 0)
			return "&f";
		return prefix;
	}
	
	public static String getSuffix(Player player) {
		String prefix = "";
		switch (handler) {
			case PERMISSIONS:
				prefix = permissionPlugin.getGroupPrefix(player.getWorld().getName(), getGroup(player));
				break;
		}
		return prefix;
	}
	
	public static String getGroup(Player player) {
		switch (handler) {
			case PERMISSIONS:
				return permissionPlugin.getGroup(player.getWorld().getName(), player.getName());
			case OP:
				if (player.isOp())
					return "op";
		}
		return "regular";
	}
	
	private enum PermissionPlugin {
		PERMISSIONS,
		OP
	}

}
