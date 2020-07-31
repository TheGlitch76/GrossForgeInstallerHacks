package net.patchworkmc.grossforgeinstallerhacks;


import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.commons.io.IOUtils;

class ResourceRedirectClassLoader extends ClassLoader {
	// You probably have several questions, this should answer some of em.
	// The forge installer is fond of getResourceAsStream.
	// As you can probably guess, that's not good for our purposes here.
	// So we load the installer on a hacky classloader to redirect it to a filesystem!
	// The only way to get that to actually work is to defineClass the installer (and a few of my utilities) ourselves so that class.getClassLoader() is this loader.
	// (Glitch) Additionally, for whatever reason, loading the Forge installer off of our main classpath fails in a NPE on some classes.
	// Rather than figuring out why, I determined it was easier to use just load the Forge Installer jar instead.

	private FileSystem loadFs;

	public ResourceRedirectClassLoader(ClassLoader parent) {
		super(parent);
	}

	protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
		synchronized (getClassLoadingLock(name)) {
			if (name.startsWith("net.minecraftforge.installer") || name.startsWith("net.patchworkmc.grossforgeinstallerhacks.loaderhack")) {

				try {
					byte[] bytes;
					if (name.startsWith("net.minecraftforge.installer")) {
						bytes = Files.readAllBytes(loadFs.getPath(name.replace('.', '/') + ".class"));
					} else {
						InputStream in = super.getResourceAsStream(name.replace('.', '/') + ".class");
						bytes = IOUtils.toByteArray(in);
						in.close();
					}
					return defineClass(name, bytes, 0, bytes.length);
				} catch (IOException e) {
					throw new ClassNotFoundException("ioexception", e);
				}
			}
			return super.loadClass(name, resolve);
		}
	}

	public InputStream getResourceAsStream(String name) {
		InputStream stream;
		if ((stream = super.getResourceAsStream(name)) == null) {
			try {
				return Files.newInputStream(loadFs.getPath(name));
			} catch (IOException e) {
				return null;
			}
		}
		return stream;
	}

	public void setResourceJar(Path to) throws IOException {
		loadFs = FileSystems.newFileSystem(to, null);
	}

	public void closeFs() throws IOException {
		loadFs.close();
	}
}