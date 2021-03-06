package net.patchworkmc.grossforgeinstallerhacks.loaderhack;

import java.io.File;
import java.nio.file.Path;
import java.util.function.Predicate;

import net.minecraftforge.installer.actions.ClientInstall;
import net.minecraftforge.installer.actions.ProgressCallback;
import net.minecraftforge.installer.json.Install;

class PatchworkClientInstall extends ClientInstall {

	private final Path libraries;
	private final Path client;

	public PatchworkClientInstall(Install profile, ProgressCallback monitor, Path libraries, Path client) {
		super(profile, monitor);
		this.libraries = libraries;
		this.client = client;
	}

	@Override
	public boolean run(File target, Predicate<String> optionals) {
		return processors.process(libraries.toFile(), client.toFile());
	}
}