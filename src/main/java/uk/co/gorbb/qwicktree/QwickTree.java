package uk.co.gorbb.qwicktree;

import java.io.IOException;
import java.util.HashMap;

import org.bukkit.plugin.java.JavaPlugin;
import org.mcstats.Metrics;

import uk.co.gorbb.qwicktree.config.Config;
import uk.co.gorbb.qwicktree.tree.info.TreeType;
import uk.co.gorbb.qwicktree.util.DisabledList;
import uk.co.gorbb.qwicktree.util.HouseIgnore;
import uk.co.gorbb.qwicktree.util.Logging;

public class QwickTree extends JavaPlugin {
	private static QwickTree instance;
	
	public static QwickTree get() {
		return instance;
	}
	
	private HashMap<TreeType, Integer> chopCount;
	
	public QwickTree() {
		instance = this;
		
		chopCount = new HashMap<TreeType, Integer>();
	}
	
	@Override
	public void onEnable() {
		Config.get().update();
		Config.get().load();
		
		getServer().getPluginManager().registerEvents(new QTListener(), this);
		getCommand("qt").setExecutor(new QTCommand());
		
		//Load data
		HouseIgnore.get().load();
		DisabledList.get().load();
		
		//Check for loaded logging plugins
		Logging.checkPlugins();
		
		doMetrics();
	}
	
	@Override
	public void onDisable() {
		//Save data
		HouseIgnore.get().save();
		DisabledList.get().save();
		
		getServer().getScheduler().cancelTasks(this);
	}
	
	public void addTreeChop(TreeType type) {
		int count = 1;
		
		if (chopCount.containsKey(type))
			count += chopCount.get(type);
		
		chopCount.put(type, count);
	}
	
	public HashMap<TreeType, Integer> getChopCount() {
		return new HashMap<TreeType, Integer>(chopCount);
	}
	
	private void doMetrics() {
		try {
			Metrics metrics = new Metrics(this);
			metrics.start();
		}
		catch (IOException e) {
			
		}
	}
}