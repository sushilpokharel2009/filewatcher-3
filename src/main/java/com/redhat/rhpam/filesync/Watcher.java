package com.redhat.rhpam.filesync;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Watcher {

	private static final Logger logger = LogManager.getLogger(Watcher.class);
	
	private Path target;
	private WatchService watchService;
	
	public Watcher(Path target) {
		this.target = target;
	}
	
	public void register() throws IOException {
		watchService = FileSystems.getDefault().newWatchService();
		WatchKey watchKey = this.target.register(watchService, 
				StandardWatchEventKinds.ENTRY_MODIFY, 
				StandardWatchEventKinds.ENTRY_CREATE, 
				StandardWatchEventKinds.ENTRY_DELETE);
		processKey(watchKey);
	}
	
	private void processKey(WatchKey watchKey) {
		for( WatchEvent<?> event : watchKey.pollEvents()) {
			Path path = (Path) event.context();
			if(path != null) {
				logger.info("File {} received event [{}] {} times.", path.toString(), event.kind(), event.count());
			} else {
				logger.warn("Event received with null path [{}]", event.kind());
			}
		}
	}
	
	public void watch() throws InterruptedException {
		WatchKey watchKey;
		logger.info("Watcher started watching on {}", target.toString());
		while((watchKey = watchService.take()) != null) {
			processKey(watchKey);
			watchKey.reset();
		}
		logger.info("Watcher finished watching on {}", target.toString());
	}
	
}
