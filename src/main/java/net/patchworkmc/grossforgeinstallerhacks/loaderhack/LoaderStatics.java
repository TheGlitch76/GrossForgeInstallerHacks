package net.patchworkmc.grossforgeinstallerhacks.loaderhack;

import java.nio.file.Path;
import java.util.ArrayList;

import net.minecraftforge.installer.DownloadUtils;
import net.minecraftforge.installer.actions.ProgressCallback;
import net.minecraftforge.installer.json.Install;
import net.minecraftforge.installer.json.Util;
import net.minecraftforge.installer.json.Version.Library;

/**
 * @deprecated internal use only!
 */
public class LoaderStatics {

	public static void installClient(Path forgeLibs, Path vanilla) {
		installLibs(forgeLibs);
		new PatchworkClientInstall(Util.loadInstallProfile(), (m, p) -> {if(p == ProgressCallback.MessagePriority.NORMAL) System.out.println(m);}, forgeLibs, vanilla).run(null, i -> true);
	}

	public static void installServer(Path forgeLibs, Path vanilla) {
		installLibs(forgeLibs);
		new PatchworkClientInstall(Util.loadInstallProfile(), (m, p) -> {if(p == ProgressCallback.MessagePriority.NORMAL) System.out.println(m);}, forgeLibs, vanilla).run(null, i -> true);
	}

	private static void installLibs(Path forgeLibs) {
		Install profile = Util.loadInstallProfile();
		for(Library lib : profile.getLibraries()) {
			if(!lib.getName().getLocalPath(forgeLibs.toFile()).exists()) {
				DownloadUtils.downloadLibrary((m, p) -> {if(p == ProgressCallback.MessagePriority.NORMAL) System.out.println(m);}, profile.getMirror(), lib, forgeLibs.toFile(), t -> true, new ArrayList<>());
			}
		}
	}

}