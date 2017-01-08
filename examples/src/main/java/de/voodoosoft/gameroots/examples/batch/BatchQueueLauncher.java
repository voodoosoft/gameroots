package de.voodoosoft.gameroots.examples.batch;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;



public class BatchQueueLauncher {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "Voodoosoft BatchQueue Sample";
		cfg.width = 1024;
		cfg.height = 768;
		new LwjglApplication(new BatchQueueSample(), cfg);
	}
}
