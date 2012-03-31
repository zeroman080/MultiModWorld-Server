package de.davboecki.multimodworld.api;

import java.util.ArrayList;
import java.util.Collections;

import net.minecraft.server.BaseMod;
import net.minecraft.server.Entity;
import net.minecraft.server.ModLoader;
import net.minecraft.server.Packet;
import net.minecraft.server.World;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Player;

import de.davboecki.multimodworld.api.plugin.ExceptionType;
import de.davboecki.multimodworld.api.plugin.IModWorldHandlePlugin;
import de.davboecki.multimodworld.api.plugin.ModAddList;
import de.davboecki.multimodworld.api.plugin.PluginExceptionHandler;

public class ModChecker {
	
	private static IModWorldHandlePlugin staticplugin = null;
	private static boolean firsterror = true;
	
	public static void registerIModWorldHandlePlugin(IModWorldHandlePlugin plugin) {
		if(staticplugin == null) {
			staticplugin = plugin;
		} else if(firsterror) {
			CraftServer server = (CraftServer)Bukkit.getServer();
			server.getLogger().severe("[MultiModWorld] Tried to register another ModWorldHandlePlugin.");
			firsterror = false;
		}
	}
	
	public static IModWorldHandlePlugin getModWorldHandlePlugin() {
		if(firsterror && staticplugin == null) {
			CraftServer server = (CraftServer)Bukkit.getServer();
			server.getLogger().severe("[MultiModWorld] No ModWorldHandlePlugin registered.");
			firsterror = false;
		}
		return staticplugin;
	}

	private static ArrayList<BaseMod> ModList = new ArrayList<BaseMod>();
	private static ArrayList<ModAddList> AddedBlockList = new ArrayList<ModAddList>();

	public static boolean isIdAllowed(String WorldName, int id){
		if(getModWorldHandlePlugin() == null) return true;
		try {
			return getModWorldHandlePlugin().isIdAllowed(WorldName, id);
		} catch(Exception e) {
			PluginExceptionHandler.HandleException(e,ExceptionType.isIdAllowed);
			return true;
		}
	}
	
	public static boolean isCraftingAllowed(String WorldName, int id){
		if(getModWorldHandlePlugin() == null) return true;
		try {
			return getModWorldHandlePlugin().isCraftingAllowed(WorldName, id);
		} catch(Exception e) {
			PluginExceptionHandler.HandleException(e,ExceptionType.isCraftingAllowed);
			return true;
		}
	}

	public static boolean isEntityAllowed(String WorldName, net.minecraft.server.Entity entity){
		if(getModWorldHandlePlugin() == null) return true;
		try {
			return getModWorldHandlePlugin().isEntityAllowed(WorldName, entity);
		} catch(Exception e) {
			PluginExceptionHandler.HandleException(e,ExceptionType.isEntityAllowed);
			return true;
		}
	}
	
	public static boolean hasWorldSetting(String WorldName, String Setting){
		if(getModWorldHandlePlugin() == null) return true;
		try {
			return getModWorldHandlePlugin().hasWorldSetting(WorldName, Setting);
		} catch(Exception e) {
			PluginExceptionHandler.HandleException(e,ExceptionType.hasWorldSetting);
			return true;
		}
	}
	
	public static boolean SendPacket(Packet packet, Player player) {
		if(getModWorldHandlePlugin() == null) return true;
		try {
			return getModWorldHandlePlugin().PacketSend(packet, player);
		} catch(Exception e) {
			PluginExceptionHandler.HandleException(e,ExceptionType.SendPacket);
			return true;
		}
	}
	
	public static net.minecraft.server.Entity replaceEntity(String WorldName,net.minecraft.server.Entity entity){
		if(getModWorldHandlePlugin() == null) return entity;
		try {
			return getModWorldHandlePlugin().ReplaceEntity(WorldName, entity);
		} catch(Exception e) {
			PluginExceptionHandler.HandleException(e,ExceptionType.replaceEntity);
			return entity;
		}
	}
	
	
	public static World getModWorldbyTag(String Tag){
		if(getModWorldHandlePlugin() == null) return (World)ModLoader.getMinecraftServerInstance().worlds.get(0);
		for(int i = 0; i < ModLoader.getMinecraftServerInstance().worlds.size();i++){
			  World world = (World)ModLoader.getMinecraftServerInstance().worlds.get(i);
			  if(hasWorldSetting(world.getWorld().getName(), Tag)) {
				  return world;
			  }
		  }
		Bukkit.getServer().getLogger().severe("[MultiModWorld] No world with tag "+Tag+" found. Returning default world.");
		return (World)ModLoader.getMinecraftServerInstance().worlds.get(0);
	}
	
	public static void ModLoaded(BaseMod Mod){
		ModList.add(Mod);
	}
	
	public static void ModAddedBlockofEntity(BaseMod Mod, int[] Ids, Class<Entity>[] Entities){
		AddedBlockList.add(new ModAddList(Mod,Ids,Entities));
	}
	
	public static ArrayList<BaseMod> getModList(){
		return (ArrayList<BaseMod>)Collections.unmodifiableList(ModList);
	}
	
	public static ArrayList<ModAddList> getAddedBlockList(){
		return (ArrayList<ModAddList>)Collections.unmodifiableList(AddedBlockList);
	}
	
	public static String getVersion(){
		return "v1.2.0";
	}

}