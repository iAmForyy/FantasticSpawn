package com.iamforyydev.utils;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class FileConfig {
	
	private static FileConfig instance;
	
	public static FileConfig getInstance() {
		if(instance == null) {
			instance = new FileConfig();
		}
		return instance;
	}
	
	private final Plugin PLUGIN = JavaPlugin.getProvidingPlugin(FileConfig.class);
	private Map<String, FileConfiguration> configs = new HashMap<String, FileConfiguration>();
	  
		public boolean isFileLoaded(String paramString){
			return configs.containsKey(paramString);
		}
	  
		public void load(String paramString){
			java.io.File localFile = new java.io.File(PLUGIN.getDataFolder(), paramString);
			if (!localFile.exists()) {
				try{
					PLUGIN.saveResource(paramString, false);
				}catch (Exception localException){
					localException.printStackTrace();
				}
			}
			if (!isFileLoaded(paramString)) {
				configs.put(paramString, YamlConfiguration.loadConfiguration(localFile));
			}
		}
	  
		public FileConfiguration get(String paramString){
			if (isFileLoaded(paramString)) {
				return (FileConfiguration)configs.get(paramString);
			}
			return null;
		}
	  
		public boolean update(String paramString1, String paramString2, Object paramObject){
			if ((isFileLoaded(paramString1)) && 
					(!((FileConfiguration)configs.get(paramString1)).contains(paramString2))){
				((FileConfiguration)configs.get(paramString1)).set(paramString2, paramObject);
				return true;
			}
			return false;
		}
	  
		public void set(String paramString1, String paramString2, Object paramObject){
			if (isFileLoaded(paramString1)) {
				((FileConfiguration)configs.get(paramString1)).set(paramString2, paramObject);
			}
		}
		public void addComment(String paramString1, String paramString2, String[] paramArrayOfString){
			if (isFileLoaded(paramString1)){
				String[] arrayOfString;
				int j = (arrayOfString = paramArrayOfString).length;
				for (int i = 0; i < j; i++){
					String str = arrayOfString[i];
					if (!((FileConfiguration)configs.get(paramString1)).contains(paramString2)) {
						((FileConfiguration)configs.get(paramString1)).set("_COMMENT_" + paramArrayOfString.length, " " + str);
					}
				}
			}
		}
	  
		public void remove(String paramString1, String paramString2){
			if (isFileLoaded(paramString1)) {
				((FileConfiguration)configs.get(paramString1)).set(paramString2, null);
			}
		}
	  
		public boolean contains(String paramString1, String paramString2){
			if (isFileLoaded(paramString1)) {
				return ((FileConfiguration)configs.get(paramString1)).contains(paramString2);
			}
			return false;
		}
	  
		public void reload(String paramString){
			java.io.File localFile = new java.io.File(PLUGIN.getDataFolder(), paramString);
			if (isFileLoaded(paramString)) {
				try{
					((FileConfiguration)configs.get(paramString)).load(localFile);
				}catch (Exception localException){
					localException.printStackTrace();
				}
			}
		}
	  
		public void save(String paramString){
			java.io.File localFile = new java.io.File(PLUGIN.getDataFolder(), paramString);
			if (isFileLoaded(paramString)) {
				try{
					((FileConfiguration)configs.get(paramString)).save(localFile);
				}catch (Exception localException){
					localException.printStackTrace();
				}
			}
		}
}
