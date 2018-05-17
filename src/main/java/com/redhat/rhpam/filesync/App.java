package com.redhat.rhpam.filesync;

import java.io.IOException;
import java.nio.file.Paths;

/**
 * Hello world!
 *
 */
public class App {
	
	public static void main(String[] args) throws InterruptedException, IOException {
		String pathToWatch = System.getenv("PATH_TO_WATCH");
		if(pathToWatch != null && !pathToWatch.isEmpty()) {
			Watcher watcher = new Watcher(Paths.get(pathToWatch));
			watcher.register();
			watcher.watch();
		}
	}
	
}
