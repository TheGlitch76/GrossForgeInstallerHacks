package net.patchworkmc.grossforgeinstallerhacks.loaderhack;

import java.io.File;
import java.nio.file.Path;
import java.util.function.Predicate;

import net.minecraftforge.installer.actions.ProgressCallback;
import net.minecraftforge.installer.actions.ServerInstall;
import net.minecraftforge.installer.json.Install;

class PatchworkServerInstall extends ServerInstall {

	private final Path libraries;
	private final Path server;

	public PatchworkServerInstall(Install profile, ProgressCallback monitor, Path libraries, Path server) {
		super(profile, monitor);
		this.libraries = libraries;
		this.server = server;
	}

	@Override
	public boolean run(File target, Predicate<String> optionals) {
		return processors.process(libraries.toFile(), server.toFile());
	}
}