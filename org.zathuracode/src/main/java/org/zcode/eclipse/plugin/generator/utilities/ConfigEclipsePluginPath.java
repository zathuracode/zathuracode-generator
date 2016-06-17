package org.zcode.eclipse.plugin.generator.utilities;

import java.io.IOException;
import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.zcode.eclipse.plugin.generator.ZathuraGeneratorActivator;
import org.zcode.generator.utilities.GeneratorUtil;
import org.zcode.reverse.utilities.ZathuraReverseEngineeringUtil;


// TODO: Auto-generated Javadoc
/**
 * Zathura Generator.
 *
 * @author Diego Armando Gomez Mosquera (dgomez@vortexbird.com)
 * @version 1.0
 */
public class ConfigEclipsePluginPath {

	/** The plugin path. */
	private String pluginPath = null;
	
	/** The me. */
	private static ConfigEclipsePluginPath me = null;

	/**
	 * The Constructor.
	 */
	private ConfigEclipsePluginPath() {
		configPath();
	}

	/**
	 * Config path.
	 */
	private void configPath() {
		URL bundleRootURL = ZathuraGeneratorActivator.getDefault().getBundle().getEntry("/");
		try {
			URL pluginUrl = FileLocator.resolve(bundleRootURL);
			pluginPath = pluginUrl.getPath();
			GeneratorUtil.setFullPath(pluginPath);
			ZathuraReverseEngineeringUtil.setFullPath(pluginPath);

		} catch (IOException e) {
			ZathuraGeneratorLog.logError(e);
		}
	}

	/**
	 * Gets the instance.
	 *
	 * @return the instance
	 */
	public static ConfigEclipsePluginPath getInstance() {
		if (me == null) {
			me = new ConfigEclipsePluginPath();
		}
		return me;

	}

}
