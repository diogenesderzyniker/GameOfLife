package fun.sim;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

	public static volatile boolean go = false;
	public static volatile int sleep = 1000;
	final static ExecutorService exService = Executors.newFixedThreadPool(3);
	
	public static void main(String[] args) {
		// workaround for awt bug
		System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");
		
		new StartScreenGUI();
		
//		WorldGUI.init();
//		WorldGUI.paintGUI();
	}

	public static void start() {
		exService.execute(new Runnable() {
			@Override
			public void run() {
				while (go) {
					try {
						Thread.sleep(sleep);
						WorldGUI.lifeWorld.tick();
						WorldGUI.repaint();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		});
	}
}
