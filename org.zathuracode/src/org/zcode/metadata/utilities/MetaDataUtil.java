package org.zcode.metadata.utilities;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zcode.metadata.model.Member;


// TODO: Auto-generated Javadoc
/**
 * Zathura Generator.
 *
 * @author Diego Armando Gomez Mosquera (dgomez@vortexbird.com)
 * @version 1.0
 */
public class MetaDataUtil {

	/** The log. */
	private static Logger log = LoggerFactory.getLogger(MetaDataUtil.class);

	/**
	 * Removes the member.
	 *
	 * @param theMembers the members
	 * @param memberName the member name
	 */
	public static void removeMember(List<Member> theMembers, String memberName) {
		for (Member member : theMembers) {
			if (member.getName().equals(memberName)) {
				theMembers.remove(member);
				break;
			}
		}
	}

	/**
	 * Find entity in package.
	 *
	 * @param pckgName the pckg name
	 * @return the list< class>
	 * @throws ClassNotFoundException the class not found exception
	 */
	@SuppressWarnings("unchecked")
	public static List<Class> findEntityInPackage(final String pckgName) throws ClassNotFoundException {
		List<Class> ret = new ArrayList<Class>();
		String name = new String(pckgName);
		if (!name.startsWith("/")) {
			name = "/" + name;
		}

		name = name.replace('.', '/');

		final URL url = MetaDataUtil.class.getResource(name);
		log.info("Pack URL:" + url);
		final File directory = new File(url.getFile());

		if (directory.exists()) {
			final String[] files = directory.list();
			for (int i = 0; i < files.length; i++) {
				if (files[i].endsWith(".class")) {
					Class clazz = Class.forName(pckgName + "." + files[i].substring(0, files[i].length() - 6));
					if (clazz.getAnnotation(Entity.class) != null) {
						ret.add(clazz);
					}
				}
			}
		}
		return ret;
	}

	/**
	 * Find entity in folder.
	 *
	 * @param pathName the path name
	 * @param pckgName the pckg name
	 * @return the list< class>
	 * @throws ClassNotFoundException the class not found exception
	 * @throws MalformedURLException the malformed url exception
	 */
	@SuppressWarnings("unchecked")
	public static List<Class> findEntityInFolder(final String pathName, final String pckgName) throws ClassNotFoundException, MalformedURLException {
		List<Class> ret = new ArrayList<Class>();

		String realPath = pathName + pckgName.replace('.', File.separatorChar);
		final File directory = new File(realPath);
		log.info("Folder path:" + directory);
		if (directory.exists()) {
			final String[] files = directory.list();
			for (int i = 0; i < files.length; i++) {
				if (files[i].endsWith(".class")) {
					Class clazz = loadClassInFolder(pathName, pckgName + "." + files[i].substring(0, files[i].length() - 6));
					// Class
					// clazz=Class.forName(pckgName+"."+files[i].substring(0,files[i].length()-6));

					if (clazz.getAnnotation(Entity.class) != null) {
						ret.add(clazz);
					}
				}
			}
		}
		return ret;
	}

	/**
	 * Find class id in folder.
	 *
	 * @param pathName the path name
	 * @param pckgName the pckg name
	 * @param className the class name
	 * @return the class
	 * @throws ClassNotFoundException the class not found exception
	 * @throws MalformedURLException the malformed url exception
	 */
	@SuppressWarnings("unchecked")
	public static Class findClassIdInFolder(final String pathName, final String pckgName, final String className) throws ClassNotFoundException,
			MalformedURLException {
		Class ret = null;

		String realPath = pathName + pckgName.replace('.', File.separatorChar) + File.separatorChar + className + ".class";
		final File directory = new File(realPath);
		log.info("Folder path:" + directory);
		if (directory.exists()) {
			Class clazz = loadClassInFolder(pathName, pckgName + "." + className);
			// Class
			// clazz=Class.forName(pckgName+"."+files[i].substring(0,files[i].length()-6));
			ret = clazz;
		}
		return ret;
	}

	/**
	 * Load class in jar.
	 *
	 * @param pathName the path name
	 * @param clazzName the clazz name
	 * @return the class
	 */
	@SuppressWarnings( { "deprecation", "unchecked" })
	public static Class loadClassInJar(String pathName, String clazzName) {
		try {
			// Convert File to a URL
			File file = new File(pathName);
			URL url = file.toURL();
			URL[] urls = new URL[] { url };

			// Create a new class loader with the directory
			ClassLoader cl1 = MetaDataUtil.class.getClassLoader();
			ClassLoader cl = new URLClassLoader(urls, cl1);

			// The clazzName is with package example
			// co.edu.usbcali.zathura.modelo.Cliente
			Class clazz = cl.loadClass(clazzName);
			return clazz;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Load class in folder.
	 *
	 * @param pathName the path name
	 * @param clazzName the clazz name
	 * @return the class
	 */
	@SuppressWarnings( { "unchecked", "deprecation" })
	public static Class loadClassInFolder(String pathName, String clazzName) {
		try {
			// Convert File to a URL
			File file = new File(pathName);
			URL url = file.toURL();
			URL[] urls = new URL[] { url };

			// Create a new class loader with the directory
			ClassLoader cl1 = MetaDataUtil.class.getClassLoader();
			ClassLoader cl = new URLClassLoader(urls, cl1);

			Class clazz = cl.loadClass(clazzName);
			return clazz;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

}
