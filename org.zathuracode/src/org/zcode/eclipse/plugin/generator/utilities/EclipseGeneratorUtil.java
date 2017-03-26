package org.zcode.eclipse.plugin.generator.utilities;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Driver;
import java.sql.DriverManager;
import java.util.List;
import java.util.Properties;

import org.eclipse.core.resources.IProject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zcode.eclipse.plugin.generator.ZathuraGeneratorActivator;
import org.zcode.eclipse.plugin.generator.gui.WizardMainZathura;
import org.zcode.generator.exceptions.GeneratorNotFoundException;
import org.zcode.generator.factory.ZathuraGeneratorFactory;
import org.zcode.generator.model.IZathuraGenerator;
import org.zcode.generator.utilities.GeneratorUtil;
import org.zcode.metadata.exceptions.MetaDataReaderNotFoundException;
import org.zcode.metadata.model.MetaDataModel;
import org.zcode.metadata.reader.IMetaDataReader;
import org.zcode.metadata.reader.MetaDataReaderFactory;
import org.zcode.reverse.engine.IZathuraReverseEngineering;
import org.zcode.reverse.engine.ZathuraReverseEngineering;
import org.zcode.reverse.utilities.ZathuraReverseEngineeringUtil;


// TODO: Auto-generated Javadoc
/**
 * Zathura Generator.
 *
 * @author Diego Armando Gomez Mosquera (dgomez@vortexbird.com)
 * @version 1.0
 */
public class EclipseGeneratorUtil {

	/** The log. */
	private static final Logger log = LoggerFactory.getLogger(EclipseGeneratorUtil.class);

	/** The project. */
	public static IProject project;
	
	public static ClassLoader bundleClassLoader =  ZathuraGeneratorActivator.getDefault().getBundle().getClass().getClassLoader();

	public static ClassLoader threadClassLoader =  Thread.currentThread().getContextClassLoader();
	
	public static String javaVersion;
	
	/** The project name. */
	public static String projectName;
	
	/** The java entity package. */
	public static String javaEntityPackage;

	/** The workspace folder path. */
	public static String workspaceFolderPath;
	
	/** The java class folder path. */
	public static String javaClassFolderPath;
	
	/** The java source folder path. */
	public static String javaSourceFolderPath;
	
	/** The web root folder path. */
	public static String webRootFolderPath;
	
	/** The lib folder path. */
	public static String libFolderPath;
	
	/** The full path project. */
	public static String fullPathProject;

	/** The zathura generator name. */
	public static String zathuraGeneratorName;
	
	/** The meta data reader. */
	public static int metaDataReader;

	/** The connection driver class. */
	public static String connectionDriverClass;
	
	/** The connection driver name. */
	public static String connectionDriverName;
	
	/** The connection url. */
	public static String connectionUrl;
	
	/** The connection username. */
	public static String connectionUsername;
	
	/** The connection password. */
	public static String connectionPassword;
	
	/** The connection driver jar path. */
	public static String connectionDriverJarPath;
	
	/** The connection driver template. */
	public static String connectionDriverTemplate;
	
	public static String connectionGroupId;
	
	public static String connectionArtifactId;
	
	public static String connectionVersion;
	

	/** The company domain name. */
	public static String companyDomainName;
	
	/** The destination directory. */
	public static String destinationDirectory;
	
	/** The catalog. */
	public static String catalog;
	
	/** The schema. */
	public static String schema;
	
	/** The catalog and schema. */
	public static String catalogAndSchema;
	
	/** The tables list. */
	public static List<String> tablesList;
	
	/** The make it xml. */
	public static Boolean makeItXml = false;
	
	/** The meta data model. */
	private static MetaDataModel metaDataModel = null;

	/** The copy d bdriver jars. */
	public static boolean copyDBdriverJars = true;
	
	/** The jar list. */
	public static String[] jarList;

	/** The wizard main. */
	public static WizardMainZathura wizardMain;
	
	public static boolean isMavenProject;
	
	public static File pomXmlFile;
	
	public static String groupIdMavenPoject;
	
	public static String artifactIdMavenProject;
	
	public static boolean isFrontend;

	/**
	 * Reset.
	 */
	public static void reset() {
		project = null;
		projectName = null;

		javaEntityPackage = null;

		workspaceFolderPath = null;
		javaClassFolderPath = null;
		javaSourceFolderPath = null;
		webRootFolderPath = null;
		libFolderPath = null;
		fullPathProject = null;

		zathuraGeneratorName = null;
		metaDataReader = 0;

		connectionDriverClass = null;
		connectionUrl = null;
		catalog = null;
		connectionUsername = null;
		connectionPassword = null;
		companyDomainName = null;
		connectionDriverJarPath = null;
		destinationDirectory = null;
		schema = null;
		catalogAndSchema = null;
		tablesList = null;
		makeItXml = false;
		
		isMavenProject = false;
		pomXmlFile = null;
		groupIdMavenPoject = null;
		artifactIdMavenProject = null;
	}

	/**
	 * Se usa para generar cuando termina el wizard.
	 *
	 * @throws MetaDataReaderNotFoundException the meta data reader not found exception
	 * @throws GeneratorNotFoundException the generator not found exception
	 */
	public static void generate() throws MetaDataReaderNotFoundException, GeneratorNotFoundException,Exception {

		EclipseGeneratorUtil.metaDataReader = MetaDataReaderFactory.JPAEntityLoaderEngine;
		String jpaPath = EclipseGeneratorUtil.javaClassFolderPath;
		String jpaPckgName = EclipseGeneratorUtil.javaEntityPackage;
		String projectName = EclipseGeneratorUtil.projectName;
		String folderProjectPath = EclipseGeneratorUtil.javaSourceFolderPath;
		String webRootFolderPath = EclipseGeneratorUtil.webRootFolderPath;
		String libFolderPath = EclipseGeneratorUtil.libFolderPath;
		File pomFile = EclipseGeneratorUtil.pomXmlFile;

		// Para que no corte los nombres de los paquetes
		int specificityLevel = 1;

		if (metaDataModel == null) {
			IMetaDataReader entityLoader = null;
			entityLoader = MetaDataReaderFactory.createMetaDataReader(MetaDataReaderFactory.JPAEntityLoaderEngine);
			metaDataModel = entityLoader.loadMetaDataModel(jpaPath, jpaPckgName);
		}

		// Variables para el properties
		Properties properties = new Properties();
		properties.put("jpaPath", jpaPath);
		properties.put("jpaPckgName", jpaPckgName);
		properties.put("specificityLevel", new Integer(specificityLevel));
		properties.put("webRootFolderPath", webRootFolderPath);
		properties.put("libFolderPath", libFolderPath);
		properties.put("folderProjectPath", folderProjectPath);
		properties.put("isMavenProject", isMavenProject);
		properties.put("pomFile", pomFile);
		
		IZathuraGenerator zathuraGenerator = ZathuraGeneratorFactory.createZathuraGenerator(EclipseGeneratorUtil.zathuraGeneratorName);
		zathuraGenerator.toGenerate(metaDataModel, projectName, folderProjectPath, properties);
	}

	/**
	 * Validar package.
	 *
	 * @param packageName the package name
	 * @throws Exception the exception
	 */
	public static void validarPackage(String packageName) throws Exception {
		if (packageName.startsWith(".") || packageName.endsWith(".")) {
			throw new Exception("A package name cannot start or end with a dot");
		}
	}

	/**
	 * Generate jpa reverse engineering.
	 */
	public static void generateJPAReverseEngineering()throws Exception{

		try {
			
		
		Properties connectionProperties = new Properties();

		destinationDirectory = workspaceFolderPath + destinationDirectory + File.separatorChar;

		connectionProperties.put("connectionDriverClass", connectionDriverClass);
		connectionProperties.put("connectionUrl", connectionUrl);

		connectionProperties.put("connectionUsername", connectionUsername);
		connectionProperties.put("connectionPassword", connectionPassword);
		connectionProperties.put("companyDomainName", companyDomainName);

		connectionProperties.put("connectionDriverJarPath", connectionDriverJarPath);
		connectionProperties.put("destinationDirectory", destinationDirectory);
		connectionProperties.put("makeItXml", makeItXml == true ? "True" : false);
		connectionProperties.put("catalogAndSchema", catalogAndSchema == null ? "" : catalogAndSchema);
		connectionProperties.put("schema", schema == null ? "" : schema);
		connectionProperties.put("catalog", catalog == null ? "" : catalog);

		log.info("Delete folder in "+destinationDirectory);
		// Borrar carpeta de temporales
		GeneratorUtil.deleteFiles(destinationDirectory);
		log.info("Create folder in "+destinationDirectory);
		// Crea carpeta de temporales
		GeneratorUtil.createFolder(destinationDirectory);

		IZathuraReverseEngineering mappingTool = new ZathuraReverseEngineering();
		mappingTool.makePojosJPA_V1_0(connectionProperties, tablesList);
		
		} catch (Exception e) {
			log.error("generateJPAReverseEngineering",e);
			throw e;
		}

	}

	/**
	 * Generate jpa reverse engineering tmp.
	 */
	public static void generateJPAReverseEngineeringTMP()throws Exception {

		Properties connectionProperties = new Properties();

		destinationDirectory = ZathuraReverseEngineeringUtil.getTempFilesPath();

		connectionProperties.put("connectionDriverClass", connectionDriverClass);
		connectionProperties.put("connectionUrl", connectionUrl);
		connectionProperties.put("connectionUsername", connectionUsername);
		connectionProperties.put("connectionPassword", connectionPassword);
		connectionProperties.put("companyDomainName", companyDomainName);
		connectionProperties.put("connectionDriverJarPath", connectionDriverJarPath);
		connectionProperties.put("destinationDirectory", destinationDirectory);
		// Genera JPA para poder usar el EntityLoader
		connectionProperties.put("makeItXml", false);
		connectionProperties.put("catalogAndSchema", catalogAndSchema == null ? "" : catalogAndSchema);
		connectionProperties.put("schema", schema == null ? "" : schema);
		connectionProperties.put("catalog", catalog == null ? "" : catalog);

		ZathuraReverseEngineeringUtil.resetTempFiles(destinationDirectory);

		IZathuraReverseEngineering mappingTool = new ZathuraReverseEngineering();
		mappingTool.makePojosJPA_V1_0(connectionProperties, tablesList);

		try {
			EclipseGeneratorUtil.javaClassFolderPath = destinationDirectory;
			IMetaDataReader entityLoader = null;
			entityLoader = MetaDataReaderFactory.createMetaDataReader(MetaDataReaderFactory.JPAEntityLoaderEngine);
			metaDataModel = entityLoader.loadMetaDataModel(destinationDirectory, companyDomainName);

		} catch (MetaDataReaderNotFoundException e) {
			log.error("generateJPAReverseEngineeringTMP",e);
			throw e;
		} finally {
			ZathuraReverseEngineeringUtil.resetTempFiles(destinationDirectory);
		}
	}

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
			log.error("loadJarSystem",e);
			throw e;
		}
	}
	
	/**
	 * Carga una lista de Jars en el JRE y El bundle del Plugin
	 * @param jarLocation
	 * @throws Exception
	 */
	public static void loadJarSystem(String []jarLocation,String driverClassName) throws Exception {
		for (String path : jarLocation) {
			loadJarSystem(path,driverClassName);
		}
	}

	
	
	/**
	 * Carga una clase en el Bundle del plugin de eclipse
	 * @param className
	 * @throws Exception
	 */
	public static void loadDriverJarBundle(String className)throws Exception{
		ZathuraGeneratorActivator.getDefault().getBundle().loadClass(className);
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

	/**
	 * Replace all.
	 *
	 * @param cadena the cadena
	 * @param old the old
	 * @param snew the snew
	 * @return the string
	 */
	public static String replaceAll(String cadena, String old, String snew) {
		StringBuffer replace = new StringBuffer();
		String aux;

		for (int i = 0; i < cadena.length(); i++) {
			if ((i + old.length()) < cadena.length()) {
				aux = cadena.substring(i, i + old.length());
				if (aux.equals(old)) {
					replace.append(snew);
					i += old.length() - 1;
				} else {
					replace.append(cadena.substring(i, i + 1));
				}
			} else
				replace.append(cadena.substring(i, i + 1));
		}
		return replace.toString();
	}

	/**
	 * The Constructor.
	 */
	private EclipseGeneratorUtil() {

	}

	/**
	 * Copy driver jars.
	 */
	public static void copyDriverJars() {
		if (copyDBdriverJars == true && jarList != null && jarList.length > 0) {
			for (String path : jarList) {
				String jarName = jarName(path);
				GeneratorUtil.copy(path, libFolderPath + jarName);
			}
		}
	}

	/**
	 * Jar name.
	 *
	 * @param path the path
	 * @return the string
	 */
	private static String jarName(String path) {
		int lastIndex = path.lastIndexOf(File.separatorChar);
		String nameJar = path.substring(lastIndex + 1, path.length());
		return nameJar;
	}

}
