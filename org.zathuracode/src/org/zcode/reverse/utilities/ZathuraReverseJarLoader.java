package org.zcode.reverse.utilities;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Driver;
import java.sql.DriverManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zcode.eclipse.plugin.generator.ZathuraGeneratorActivator;



// TODO: Auto-generated Javadoc
/**
 * Zathura Generator.
 *
 * @author Diego Armando Gomez Mosquera (dgomez@vortexbird.com)
 * @author William Altuzarra Noriega Noriega (williamaltu@gmail.com)
 * @version 1.0
 */
public class ZathuraReverseJarLoader {
	
    private static final Logger log = LoggerFactory.getLogger(ZathuraReverseJarLoader.class);

	

	/**
	 * Gets the url class loader.
	 *
	 * @param jarURL the jar url
	 * @return the URL class loader
	 */
	private static URLClassLoader getURLClassLoader(URL jarURL) {
		return new URLClassLoader(new URL[] { jarURL });
	}

	/**
	 * Load jar2.
	 *
	 * @param jarLocation the jar location
	 * @throws Exception the exception
	 */
	/**
	 * Load jar system.
	 *
	 * @param jarLocation the jar location
	 * @throws Exception the exception
	 */
	@SuppressWarnings("deprecation")
	public static void loadJarSystem(String jarLocation,String driverClassName) throws Exception {

		// Para que funcione con el RPC JDP se debe poner Eclipse-BuddyPolicy:
		// appﬂ

		try {
			Method addURL = URLClassLoader.class.getDeclaredMethod("addURL", new Class[] { URL.class });
			addURL.setAccessible(true);// you're telling the JVM to override the
			// default visibility
			File[] files = getExternalJars(jarLocation);// some method returning
			// the
			// jars to add

			ClassLoader cl = ClassLoader.getSystemClassLoader();

			for (int i = 0; i < files.length; i++) {
				URL url = files[i].toURL();
				addURL.invoke(cl, new Object[] { url });
				
				URLClassLoader classLoader = new URLClassLoader(new URL[]{url}, cl);
				Driver driver = (Driver) Class.forName(driverClassName, true, classLoader).newInstance();
				DriverManager.registerDriver(driver);
				
				
				log.info("Loaded JRE:" + files[i].getName());
			}

			// at this point, the default class loader has all the jars you
			// indicated
			//Carga de jars en el Bundle del contenedor OSGI
			
			ClassLoader bundleClassLoader =  ZathuraGeneratorActivator.getDefault().getBundle().getClass().getClassLoader();
			
			for (int i = 0; i < files.length; i++) {
				URL url = files[i].toURL();
				addURL.invoke(bundleClassLoader, new Object[] { url });				
			
				
			
				URLClassLoader classLoader = new URLClassLoader(new URL[]{url}, bundleClassLoader);
				Driver driver = (Driver) Class.forName(driverClassName, true, classLoader).newInstance();
				DriverManager.registerDriver(driver);
				ZathuraGeneratorActivator.getDefault().getBundle().loadClass(driverClassName);
				
				
				log.info("Loaded Bundle:" + files[i].getName());
			}
			
			
			
		} catch (Exception e) {
			log.error(e.getMessage());
			throw e;
		}
	}

	/**
	 * Gets the external jars.
	 *
	 * @param jarLocation the jar location
	 * @return the external jars
	 */
	private static File[] getExternalJars(String jarLocation) {
		File[] files = new File[1];
		files[0] = new File(jarLocation);
		return files;
	}
}