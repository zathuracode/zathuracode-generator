package org.zcode.generator.utilities;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zcode.eclipse.plugin.generator.utilities.EclipseGeneratorUtil;


/**
 * Zathura Generator.
 *
 * @author Diego Armando Gomez Mosquera (dgomez@vortexbird.com)
 * @version 1.0
 */
public class GeneratorUtil {
	
	
	
	
	private final static String generatorTemplatesPath="templates";
	private final static String generatorExtPath="ext";

	/** The log. */
	private static final Logger log = LoggerFactory.getLogger(GeneratorUtil.class);

	/** The slash. */
	public static String slash = System.getProperty("file.separator");
	
	public static String pomFile = "pom.xml";

	/** The File name. */
	public static String FileName = "zathura-generator-factory-config.xml";

	/** The full path. */
	private static String fullPath = "";

	/** The xml config factory path. */
	private static String xmlConfigFactoryPath = "config" + slash + FileName;

	/** The xml config. */
	private static String xmlConfig = "config" + slash;

	
	/** The generator libraries zathura java ee web centric. */
	//private static String generatorLibrariesZathuraJavaEEWebCentric = "generatorLibraries" + GeneratorUtil.slash;

	/** The generator ext zathura java ee web centric. */
	private static String generatorExtZathuraJavaEEWebCentric = "generatorExt" + GeneratorUtil.slash + "zathura-JavaEE-Web-Centric" + GeneratorUtil.slash;

	// JavaEE-HibernateCore-Spring-WebCentric
	/** The spring centric templates. */
	private static String springCentricTemplates = "generatorTemplates" + GeneratorUtil.slash + "zathura-JavaEE-hibernateCore-Spring-Centric" + GeneratorUtil.slash;

	/** The generator libraries zathura java ee spring web centric. */
	private static String generatorLibrariesZathuraJavaEESpringWebCentric = "generatorLibraries" + GeneratorUtil.slash;

	/** The generator ext zathura java ee spring web centric. */
	private static String generatorExtZathuraJavaEESpringWebCentric = "generatorExt" + GeneratorUtil.slash + "zathura-JavaEE-hibernateCore-Spring-Centric"+ GeneratorUtil.slash;

	// JavaEE-HibernateCore-WebCentric
	/** The templates zathura java ee hibernate core web centric. */
	private static String templatesZathuraJavaEEHibernateCoreWebCentric = "generatorTemplates" + GeneratorUtil.slash
	+ "zathura-JavaEE-hibernateCore-Web-Centric" + GeneratorUtil.slash;

	/** The generator libraries zathura java ee hibernate core web centric. */
	private static String generatorLibrariesZathuraJavaEEHibernateCoreWebCentric = "generatorLibraries" + GeneratorUtil.slash;

	/** The generator ext zathura java ee hibernate core web centric. */
	private static String generatorExtZathuraJavaEEHibernateCoreWebCentric = "generatorExt" + GeneratorUtil.slash + "zathura-JavaEE-hibernateCore-Web-Centric"
	+ GeneratorUtil.slash;


	
	// Java JEE jpa+ prime
	private static String  primeCentricTemplates= "generatorTemplates"+ GeneratorUtil.slash + "zathura-JavaEE-Prime-Web-Centric" +GeneratorUtil.slash;
	private static String  generatorExtZathuraJavaEEPrimeCentric="generatorExt"+GeneratorUtil.slash+"zathura-JavaEE-Prime-Web-Centric"+GeneratorUtil.slash;
	private static String  generatorLibrariesZathuraJavaEEPrimefaces = "generatorLibraries"+ GeneratorUtil.slash;
	// JavaEE PrimeFaces+Hibernate  
	private static String  primeHibernateTemplates ="generatorTemplates"+GeneratorUtil.slash+"zathura-JavaEE-HibernateCore-Prime-Web-Centric"+GeneratorUtil.slash;
	private static String  generatorExtZathuraJavaEEPrimeHibernateCentric = "generatorExt"+GeneratorUtil.slash+"zathura-JavaEE-hibernateCore-Prime-Centric"+GeneratorUtil.slash;
	private static String  generatorLibrariesZathuraJavaEEPrimefacesHibernate = "generatorLibraries"+ GeneratorUtil.slash;
	// JavaEE Jpa + Spring
	private static String springJpaTemplates="generatorTemplates" + GeneratorUtil.slash + "zathura-JavaEE-jpaCore-Spring-Centric" + GeneratorUtil.slash;
	private static String generatorExtZathuraJavaEESpringJpa = "generatorExt" + GeneratorUtil.slash + "zathura-JavaEE-jpaCore-Spring-Centric"+ GeneratorUtil.slash;
	private static String generatorLibrariesZathuraJavaEESpringJpa="generatorLibraries" + GeneratorUtil.slash;
	// JavaEE Primefaces+Hibernate+Spring
	private static String SpringHibernatePrime = "generatorTemplates" + GeneratorUtil.slash +"zathura-JavaEE-HibernateCore-Prime-Spring-Centric"+GeneratorUtil.slash;
	private static String generatorExtZathuraJavaEEPrimeSpringHibernateCentric= "generatorExt" +GeneratorUtil.slash+"zathura-JavaEE-hibernateCore-Prime-Spring-Centric"+GeneratorUtil.slash;
	private static String generatorLibrariesZathuraJavaEEPrimeSpringHibernate="generatorLibraries"+ GeneratorUtil.slash;
	// JavaEE Primefaces+Jpa+Spring
	private static String SpringJpaPrime = "generatorTemplates" + GeneratorUtil.slash +"zathura-JavaEE-jpaCore-Prime-Spring-Centric"+GeneratorUtil.slash;
	private static String generatorExtZathuraJavaEEPrimeSpringJpaCentric= "generatorExt" +GeneratorUtil.slash+"zathura-JavaEE-jpaCore-Prime-Spring-Centric"+GeneratorUtil.slash;
	private static String generatorLibrariesZathuraJavaEEPrimeSpringJpa="generatorLibraries"+ GeneratorUtil.slash;
	
	
	/**
	 * Crea la estructura de caroetas del proyecto maven si no existe
	 */
	public static void generateMavenDirectoryStructure(String projectPath) {
		String MAIN_RESOURCES=	GeneratorUtil.slash+"src"+GeneratorUtil.slash+"main"+GeneratorUtil.slash+"resources"+GeneratorUtil.slash;
		String TEST_JAVA=		GeneratorUtil.slash+"src"+GeneratorUtil.slash+"test"+GeneratorUtil.slash+"java"+GeneratorUtil.slash;
		String TEST_RESOURCES=	GeneratorUtil.slash+"src"+GeneratorUtil.slash+"test"+GeneratorUtil.slash+"resources"+GeneratorUtil.slash;
		
		//String MAIN_META_INF=	GeneratorUtil.slash+"src"+GeneratorUtil.slash+"main"+GeneratorUtil.slash+"resources"+GeneratorUtil.slash+"META-INF"+GeneratorUtil.slash;
		log.info("Begin Validate Maven Directory Structure");
			projectPath=projectPath+GeneratorUtil.slash;
			createFolder(projectPath+MAIN_RESOURCES);
			createFolder(projectPath+TEST_JAVA);
			createFolder(projectPath+TEST_RESOURCES);
			//createFolder(projectPath+MAIN_META_INF);
		log.info("End Validate Maven Directory Structure");
	}
	
	/**
	 * Retorna el path de los templates de velocity
	 * @return
	 */
	public static String getGeneratorTemplatesPath() {
		if(fullPath!=null && fullPath.equals("")!= true){
			return fullPath +generatorTemplatesPath;
		}
		return generatorTemplatesPath;
	}

	/**
	 * retorna el path de los extras del proyecto
	 * @return
	 */
	public static String getGeneratorExtPath() {
		if(fullPath!=null && fullPath.equals("")!= true){
			return fullPath+generatorExtPath;
		}
		return generatorExtPath;
	}

	// Java JEE jpa+ prime
	public static String getPrimeCentricTemplates() {
		if(fullPath!=null && fullPath.equals("")!= true){
			return fullPath + primeCentricTemplates;
		}

		return primeCentricTemplates;
	}

	public static String getGeneratorExtZathuraJavaEEPrimeCentric() {
		if (fullPath != null && fullPath.equals("") != true) {
			return fullPath + generatorExtZathuraJavaEEPrimeCentric;
		}
		return generatorExtZathuraJavaEEPrimeCentric;
	}

	public static void setGeneratorExtZathuraJavaEEPrimeCentric(
			String generatorExtZathuraJavaEEPrimeCentric) {
		GeneratorUtil.generatorExtZathuraJavaEEPrimeCentric = generatorExtZathuraJavaEEPrimeCentric;
	}

	public static String getGeneratorLibrariesZathuraJavaEEPrimefaces() {
		if (fullPath != null && fullPath.equals("") != true) {
			return fullPath + generatorLibrariesZathuraJavaEEPrimefaces;
		}
		return generatorLibrariesZathuraJavaEEPrimefaces;
	}


// Java EE Hibernate + Prime	
		
	public static String getPrimeHibernateTemplates() {
		if(fullPath!=null && fullPath.equals("")!= true){
			return fullPath + primeHibernateTemplates ;
		}
		return primeHibernateTemplates;
	}

	public static String getGeneratorExtZathuraJavaEEPrimeHibernateCentric() {
		if (fullPath != null && fullPath.equals("") != true) {
			return fullPath + generatorExtZathuraJavaEEPrimeHibernateCentric;
		}
		return generatorExtZathuraJavaEEPrimeHibernateCentric;
	}
	
	public static String getGeneratorLibrariesZathuraJavaEEPrimefacesHibernate() {
		if (fullPath != null && fullPath.equals("") != true) {
			return fullPath + generatorLibrariesZathuraJavaEEPrimefacesHibernate;
		}
		return generatorLibrariesZathuraJavaEEPrimefacesHibernate;
	}
	
// Java EE Hibernate + PrimeFaces +Spring
	
	public static String getSpringHibernatePrime() {
		if(fullPath!=null && fullPath.equals("")!= true){
			return fullPath + SpringHibernatePrime ;
		}
		return SpringHibernatePrime;
	}
	
	public static String getGeneratorExtZathuraJavaEEPrimeSpringHibernateCentric() {
		if (fullPath != null && fullPath.equals("") != true) {
			return fullPath + generatorExtZathuraJavaEEPrimeSpringHibernateCentric;
		}
		
		return generatorExtZathuraJavaEEPrimeSpringHibernateCentric;
	}
	
	public static String getGeneratorLibrariesZathuraJavaEEPrimeSpringHibernate() {
		if (fullPath != null && fullPath.equals("") != true) {
			return fullPath + generatorLibrariesZathuraJavaEEPrimeSpringHibernate;
		}
		return generatorLibrariesZathuraJavaEEPrimeSpringHibernate;
	}

//	Java EE Jpa + PrimeFaces +Spring	
	
	public static String getSpringJpaPrime() {
		
		if(fullPath!=null && fullPath.equals("")!= true){
			return fullPath + SpringJpaPrime ;
		}
		return SpringJpaPrime;
	}

	public static String getGeneratorExtZathuraJavaEEPrimeSpringJpaCentric() {
		
		if (fullPath != null && fullPath.equals("") != true) {
			return fullPath + generatorExtZathuraJavaEEPrimeSpringJpaCentric;
		}
		return generatorExtZathuraJavaEEPrimeSpringJpaCentric;
	}
	
	public static String getGeneratorLibrariesZathuraJavaEEPrimeSpringJpa() {
		
		if (fullPath != null && fullPath.equals("") != true) {
			return fullPath + generatorLibrariesZathuraJavaEEPrimeSpringJpa;
		}
		return generatorLibrariesZathuraJavaEEPrimeSpringJpa;
	}
//END
	

	/**
	 * Gets the full path.
	 *
	 * @return the full path
	 */
	public static String getFullPath() {
		return fullPath;
	}


	/**
	 * Sets the full path.
	 *
	 * @param fullPath the full path
	 */
	public static void setFullPath(String fullPath) {
		// System.err.println(fullPath);
		if (fullPath != null && fullPath.startsWith("/") && System.getProperty("os.name").toUpperCase().contains("WINDOWS") == true) {
			fullPath = fullPath.substring(1, fullPath.length());
		}

		if (fullPath != null) {
			if (slash.equals("/")) {
				fullPath = replaceAll(fullPath, "\\", slash);
				if (fullPath.endsWith("\\") == true) {
					fullPath = fullPath.substring(0, fullPath.length() - 1);
					fullPath = fullPath + slash;
				}

			} else if (slash.equals("\\")) {
				fullPath = replaceAll(fullPath, "/", slash);
				if (fullPath.endsWith("/") == true) {
					fullPath = fullPath.substring(0, fullPath.length() - 1);
					fullPath = fullPath + slash;
				}
			}
		}
		// System.err.println(fullPath);

		GeneratorUtil.fullPath = fullPath;
	}

	/**
	 * Gets the xml config factory path.
	 *
	 * @return the xml config factory path
	 */
	public static String getXmlConfigFactoryPath() {
		if (fullPath != null && fullPath.equals("") != true) {
			return fullPath + xmlConfigFactoryPath;
		}
		return xmlConfigFactoryPath;
	}

	/**
	 * Gets the xml config.
	 *
	 * @return the xml config
	 */
	public static String getXmlConfig() {
		if (fullPath != null && fullPath.equals("") != true) {
			return fullPath + xmlConfig + slash;
		}
		return xmlConfigFactoryPath;
	}

	

	/**
	 * Gets the generator ext zathura java ee web centric.
	 *
	 * @return the generator ext zathura java ee web centric
	 */
	public static String getGeneratorExtZathuraJavaEEWebCentric() {
		if (fullPath != null && fullPath.equals("") != true) {
			return fullPath + generatorExtZathuraJavaEEWebCentric;
		}
		return generatorExtZathuraJavaEEWebCentric;
	}

	

	// JavaEESpringWebCentric
	/**
	 * Gets the generator libraries zathura java ee spring web centric.
	 *
	 * @return the generator libraries zathura java ee spring web centric
	 */
	public static String getGeneratorLibrariesZathuraJavaEESpringWebCentric() {
		if (fullPath != null && fullPath.equals("") != true) {
			return fullPath + generatorLibrariesZathuraJavaEESpringWebCentric;
		}
		return generatorLibrariesZathuraJavaEESpringWebCentric;
	}

	/**
	 * Gets the generator ext zathura java ee spring web centric.
	 *
	 * @return the generator ext zathura java ee spring web centric
	 */
	public static String getGeneratorExtZathuraJavaEESpringWebCentric() {
		if (fullPath != null && fullPath.equals("") != true) {
			return fullPath + generatorExtZathuraJavaEESpringWebCentric;
		}
		return generatorExtZathuraJavaEESpringWebCentric;
	}

	/**
	 * Gets the spring web centric templates.
	 *
	 * @return the spring web centric templates
	 */
	public static String getSpringWebCentricTemplates() {
		if (fullPath != null && fullPath.equals("") != true) {
			return fullPath + springCentricTemplates;
		}
		return springCentricTemplates;
	}







	//Spring + Jpa


	public static String getSpringJpaTemplates() {
		if(fullPath != null && fullPath.equals("")!= true){
			return fullPath + springJpaTemplates;
		}

		return springJpaTemplates;
	}


	public static String getGeneratorExtZathuraJavaEESpringJpa() {
		if(fullPath != null && fullPath.equals("")!=true){
			return fullPath + generatorExtZathuraJavaEESpringJpa; 
		}

		return generatorExtZathuraJavaEESpringJpa;
	}


	public static String getGeneratorLibrariesZathuraJavaEESpringJpa() {

		if(fullPath!=null && fullPath.equals("")!=true){
			return fullPath + generatorLibrariesZathuraJavaEESpringJpa;
		}
		return generatorLibrariesZathuraJavaEESpringJpa;
	}




	// JavaEEWebCentric
	/**
	 * Gets the templates zathura java ee hibernate core web centric.
	 *
	 * @return the templates zathura java ee hibernate core web centric
	 */
	public static String getTemplatesZathuraJavaEEHibernateCoreWebCentric() {
		if (fullPath != null && fullPath.equals("") != true) {
			return fullPath + templatesZathuraJavaEEHibernateCoreWebCentric;
		}
		return templatesZathuraJavaEEHibernateCoreWebCentric;
	}

	/**
	 * Gets the generator libraries zathura java ee hibernate core web centric.
	 *
	 * @return the generator libraries zathura java ee hibernate core web centric
	 */
	public static String getGeneratorLibrariesZathuraJavaEEHibernateCoreWebCentric() {
		if (fullPath != null && fullPath.equals("") != true) {
			return fullPath + generatorLibrariesZathuraJavaEEHibernateCoreWebCentric;
		}
		return generatorLibrariesZathuraJavaEEHibernateCoreWebCentric;
	}

	/**
	 * Gets the generator ext zathura java ee hibernate core web centric.
	 *
	 * @return the generator ext zathura java ee hibernate core web centric
	 */
	public static String getGeneratorExtZathuraJavaEEHibernateCoreWebCentric() {
		if (fullPath != null && fullPath.equals("") != true) {
			return fullPath + generatorExtZathuraJavaEEHibernateCoreWebCentric;
		}
		return generatorExtZathuraJavaEEHibernateCoreWebCentric;
	}

	

	/**
	 * Creates the folder.
	 *
	 * @param path the path
	 * @return the file
	 */
	public static File createFolder(String path) {
		File aFile = new File(path);
		aFile.mkdirs();
		return aFile;
	}

	/**
	 * Copy.
	 *
	 * @param source the source
	 * @param Target the Target
	 */
	public static void copy(String source, String Target) {
		FileInputStream fIn = null;
		FileOutputStream fOut = null;
		byte[] b;
		int l = 0;
		try {
			fIn = new FileInputStream(source);
			fOut = new FileOutputStream(Target);
			b = new byte[1024];
			while ((l = fIn.read(b)) > 0) {
				fOut.write(b, 0, l);
			}
			fOut.close();
			fIn.close();
		} catch (FileNotFoundException fnfe) {
			log.error(fnfe.toString());
		} catch (IOException ioe) {
			log.error(ioe.toString());
		}
	}

	/**
	 * Delete files.
	 *
	 * @param path the path
	 */
	public static void deleteFiles(String path) {
		File file = new File(path);
		deleteFiles(file);
	}

	/**
	 * Delete files.
	 *
	 * @param file the file
	 */
	public static void deleteFiles(File file) {
		File fileAux = null;
		File listFiles[] = null;
		int iPos = -1;

		listFiles = file.listFiles();
		for (iPos = 0; iPos < listFiles.length; iPos++) {
			fileAux = listFiles[iPos];
			if (fileAux.isDirectory())
				deleteFiles(listFiles[iPos]);
			listFiles[iPos].delete();
		}
		if (file.listFiles().length == 0)
			file.delete();
	}

	/**
	 * Delete file.
	 *
	 * @param path the path
	 */
	public static void deleteFile(String path) {
		File file = new File(path);
		deleteFile(file);
	}

	/**
	 * Delete file.
	 *
	 * @param file the file
	 */
	public static void deleteFile(File file) {
		file.delete();
	}

	/**
	 * public static List<String> copyFolder(String source,String target){
	 * List<String> filesLib =new ArrayList<String>(); try { File dir = new
	 * File(source); String[] fileNames = dir.list(); for (int i = 0; i <
	 * fileNames.length; i++) { String s=fileNames[i]; copy(source+s,target+s);
	 * filesLib.add(s); } return filesLib; } catch (Exception e) { //
	 * log4j System.out.println("Error copy all files of folder:"+e.toString());
	 * } return null; }
	 *
	 * @param source the source
	 * @param target the target
	 */
	public static void copyFolder(String source, String target) {
		try {
			File dir = new File(source);
			File[] listFiles = dir.listFiles();
			File fileSource = null;
			for (int i = 0; i < listFiles.length; i++) {
				fileSource = listFiles[i];
				if (fileSource.isDirectory()) {
					log.debug(fileSource.getName());
					log.debug("Source:" + fileSource.getAbsolutePath());
					log.debug("Target:" + target + fileSource.getName());
					createFolder(target + fileSource.getName());
					copyFolder(fileSource.getAbsolutePath() + slash, target + fileSource.getName() + slash);
				} else {
					copy(fileSource.getAbsolutePath(), target + fileSource.getName());
					log.debug(fileSource.toString());
				}
			}

		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}

	/**
	 * File fileAux = null; File listFiles[] = null; int iPos = -1;
	 * 
	 * listFiles = file.listFiles(); for(iPos = 0; iPos < listFiles.length;
	 * iPos++){ fileAux = listFiles[iPos]; if(fileAux.isDirectory())
	 * deleteFiles(listFiles[iPos]); listFiles[iPos].delete(); }
	 * if(file.listFiles().length == 0) file.delete();
	 *
	 * @param path the path
	 * @param pck the pck
	 * @return the string
	 */

	public static String createFolderOfPackage(String path, String pck) {
		try {
			path = path + replaceAll(pck, ".", File.separator);
			path = path + File.separator;
			File myDirectory = new File(path);
			myDirectory.mkdirs();
			return path;
		} catch (Exception e) {
			log.error("Fallo creacion de carpetas para los paquetes:" + e.toString());
		}
		return null;
	}

	/**
	 * Validate directory.
	 *
	 * @param packageName the package name
	 * @param location the location
	 * @return true, if validate directory
	 * @throws IOException the IO exception
	 */
	public static boolean validateDirectory(String packageName, String location) throws IOException {

		if (location == null || location.equals("") == true)
			return false;

		File dirComm = new File(location);

		if (dirComm.exists()) {
			String dirsToCreate[] = packageName.split("_");

			for (int i = 0; i < dirsToCreate.length; i++) {
				location = location + slash + dirsToCreate[i];
				dirComm = new File(location);

				if (!dirComm.exists()) {
					dirComm.mkdir();
				}
			}
		}
		return true;
	}

	/**
	 * Replace all.
	 *
	 * @param cadena the cadena
	 * @param old the old
	 * @param snew the snew
	 * @return the string
	 * 
	 * Esta utilidad permite remplazar en una cadena un valor por otro
	 * Ej: String cadena= "com.mauricio.demogenerator";
	 * 	   String salida=cadena.replaceAll(cadena,".","/");
	 * 		salida = com/mauricio/demogenerator
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
	
	
	public static void doPomXml(VelocityContext context,VelocityEngine ve)throws Exception{
		log.info("Begin doPomXml");
		
		Template pomTemplate = null;
		StringWriter swPom = new StringWriter();
		
		try {
			pomTemplate = ve.getTemplate("pom.xml.vm");
		} catch (ResourceNotFoundException rnfe) {
			// couldn't find the template
			rnfe.printStackTrace();
			log.info("doPomXml",rnfe);
		} catch (ParseErrorException pee) {
			// syntax error: problem parsing the template
			pee.printStackTrace();
			log.info("doPomXml",pee);
		} catch (MethodInvocationException mie) {
			// something invoked in the template
			// threw an exception
			mie.printStackTrace();
			log.info("doPomXml",mie);
		} catch (Exception e) {
			e.printStackTrace();
			log.info("doPomXml",e);
			throw e;
		}
		
		try {

			
			String pomLocation = EclipseGeneratorUtil.fullPathProject + GeneratorUtil.slash + GeneratorUtil.pomFile;
			
			String groupId = EclipseGeneratorUtil.companyDomainName;						
			String artifactId = EclipseGeneratorUtil.projectName;
			String name = EclipseGeneratorUtil.projectName;
			String description = "Spring Boot Project generated by Zathuracode";
									
			context.put("groupId", groupId);
			context.put("artifactId", artifactId);
			context.put("name", name);
			context.put("description", description);
			
			String groupIdConnector = 		EclipseGeneratorUtil.connectionGroupId;
			String artifactIdConnector = 	EclipseGeneratorUtil.connectionArtifactId;
			String versionConnector = 		EclipseGeneratorUtil.connectionVersion;
			
			context.put("groupIdConnector", groupIdConnector.equals("")!=true?groupIdConnector:"configure yourself");
			context.put("artifactIdConnector", artifactIdConnector.equals("")!=true?artifactIdConnector:"configure yourself");
			context.put("versionConnector", versionConnector.equals("")!=true?versionConnector:"configure yourself");
			
			pomTemplate.merge(context, swPom);			
			
			FileWriter fstream = new FileWriter(pomLocation);
			BufferedWriter out = new BufferedWriter(fstream);
			out.write(swPom.toString());
			// Close the output stream
			out.close();
			
			JalopyCodeFormatter.formatJavaCodeFile(pomLocation);

			log.info("End doPomXml");
		} catch (Exception e) {
			log.info("Error: " + e.getMessage());
			throw e;
		}
		
	}

		
}
