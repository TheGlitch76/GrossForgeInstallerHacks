package net.patchworkmc.grossforgeinstallerhacks;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Path;


public class GrossForgeInstallerHacks {
	private static final ResourceRedirectClassLoader OH_GOD_LOADER = new ResourceRedirectClassLoader(GrossForgeInstallerHacks.class.getClassLoader());
	private static final Method CLIENT_INSTALL;
	private static final Method SERVER_INSTALL;

	static {
		try {
			Class<?> onLoader = OH_GOD_LOADER.loadClass("net.patchworkmc.grossforgeinstallerhacks.loaderhack.LoaderStatics");
			CLIENT_INSTALL = onLoader.getMethod("installClient", Path.class, Path.class);
			SERVER_INSTALL = onLoader.getMethod("installServer", Path.class, Path.class);
		} catch (ClassNotFoundException | NoSuchMethodException | SecurityException e) {
			throw new RuntimeException(e);
		}
	}

	public static void installClient(Path forgeLibs, Path vanilla, Path installer) throws IOException {
		OH_GOD_LOADER.setResourceJar(installer);
		try {
			CLIENT_INSTALL.invoke(null, forgeLibs, vanilla);
		} catch (IllegalAccessException | InvocationTargetException e) {
			throw new RuntimeException(e);
		} finally {
			OH_GOD_LOADER.closeFs();
		}
	}

	public static void installServer(Path forgeLibs, Path vanilla, Path installer) throws IOException {
		OH_GOD_LOADER.setResourceJar(installer);
		try {
			SERVER_INSTALL.invoke(null, forgeLibs, vanilla);
		} catch (IllegalAccessException | InvocationTargetException e) {
			throw new RuntimeException(e);
		} finally {
			OH_GOD_LOADER.closeFs();
		}
	}
}