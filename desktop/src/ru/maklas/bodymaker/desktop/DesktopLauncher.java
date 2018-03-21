package ru.maklas.bodymaker.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import ru.maklas.bodymaker.MBodyMaker;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		final float scale = 0.67f;
		config.width  = (int) (1280 * scale);
		config.height = (int) (720 * scale);
		new LwjglApplication(new MBodyMaker(), config);
	}
}
