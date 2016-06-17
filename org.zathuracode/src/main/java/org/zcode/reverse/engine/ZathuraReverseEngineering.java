package org.zcode.reverse.engine;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.Properties;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DefaultLogger;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectHelper;
import org.apache.tools.ant.listener.Log4jListener;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zcode.eclipse.plugin.generator.utilities.EclipseGeneratorUtil;
import org.zcode.generator.utilities.GeneratorUtil;
import org.zcode.reverse.utilities.ZathuraReverseEngineeringUtil;
import org.zcode.reverse.utilities.ZathuraReverseJarLoader;


// TODO: Auto-generated Javadoc
/**
 * Zathura Generator.
 *
 * @author Diego Armando Gomez Mosquera (dgomez@vortexbird.com)
 * @author William Altuzarra Noriega Noriega (williamaltu@gmail.com)
 * @version 1.0
 */
public class ZathuraReverseEngineering implements IZathuraReverseEngineering {
	
	

	// MYSQL, SQLSERVER
	/** The Constant CATALOG. */
	public final static String CATALOG = "1";
	// ORACLE, POSTGRES,
	/** The Constant SCHEMA. */
	public final static String SCHEMA = "2";
	// DB2 AS400
	/** The Constant CATALOG_SCHEMA. */
	public final static String CATALOG_SCHEMA = "3";

	/** The log. */
    private static final Logger log = LoggerFactory.getLogger(ZathuraReverseEngineering.class);


	/** The Constant reverseTemplatesPath. */
	private final static String reverseTemplatesPath = ZathuraReverseEngineeringUtil.getReverseTemplates();

	/** The Constant tempFiles. */
	private final static String tempFiles = ZathuraReverseEngineeringUtil.getTempFilesPath();

	/** The connection driver class. */
	private String connectionDriverClass;
	
	/** The connection url. */
	private String connectionUrl;
	
	/** The connection username. */
	private String connectionUsername;

	/** The connection password. */
	private String connectionPassword;
	
	/** The company domain name. */
	private String companyDomainName;
	
	/** The company domain name for pojo location. */
	private String companyDomainNameForPojoLocation;
	
	/** The connection driver jar path. */
	private String connectionDriverJarPath;
	
	/** The destination directory. */
	private String destinationDirectory;
	
	/** The make it xml. */
	private Boolean makeItXml;

	/** The catalog. */
	private String catalog;
	
	/** The schema. */
	private String schema;
	
	/** The catalog and schema. */
	private String catalogAndSchema;

	/** The tables list. */
	private List<String> tablesList;
	
	/** The ve. */
	private VelocityEngine ve;

	/* (non-Javadoc)
	 * @see org.zathuracode.reverse.engine.IZathuraReverseEngineering#makePojosJPA_V1_0(java.util.Properties, java.util.List)
	 */
	public void makePojosJPA_V1_0(Properties connectionProperties, List<String> tables)throws Exception {

		log.info("Begin ZathuraReverseMappingTool (Pojo Making & compilation) with HibernateTools");

		connectionDriverClass = connectionProperties.getProperty("connectionDriverClass");
		connectionUrl = connectionProperties.getProperty("connectionUrl");

		connectionUsername = connectionProperties.getProperty("connectionUsername");
		connectionPassword = connectionProperties.getProperty("connectionPassword");
		companyDomainName = connectionProperties.getProperty("companyDomainName");
		companyDomainNameForPojoLocation = ZathuraReverseEngineeringUtil.fixDomain(companyDomainName);
		connectionDriverJarPath = connectionProperties.getProperty("connectionDriverJarPath");
		destinationDirectory = connectionProperties.getProperty("destinationDirectory");
		schema = connectionProperties.getProperty("schema");
		catalog = connectionProperties.getProperty("catalog");
		catalogAndSchema = connectionProperties.getProperty("catalogAndSchema");

		// Este parametro es para que genere hibernatexml y los pojos con
		// hibernate
		makeItXml = Boolean.parseBoolean(connectionProperties.getProperty("makeItXml"));

		this.tablesList = tables;

		// Llama los templates que generan los archivos de configuracion para
		// HibernateTools
		doTemplate();

		
		ZathuraReverseJarLoader.loadJarSystem(connectionDriverJarPath, connectionDriverClass);
		ZathuraReverseJarLoader.loadJarSystem(connectionDriverJarPath, connectionDriverClass);
		

		// LLama el proceso de ANT con los archivos generados
		callAntProcess();

		log.info("End ZathuraReverseMappingTool (Pojo Making & compilation)");
	}

	/**
	 * Do template.
	 */
	private void doTemplate() throws Exception{
		
		//TODO https://github.com/whitesource/whitesource-bamboo-agent/issues/9
		log.info("Execute init doTemplate");
		
		Thread thread = Thread.currentThread();
		ClassLoader loader = thread.getContextClassLoader();
		thread.setContextClassLoader(EclipseGeneratorUtil.bundleClassLoader);
		log.info("Chaged ContextClassLoader:"+EclipseGeneratorUtil.bundleClassLoader);
		try {
	
			ve = new VelocityEngine();
			
			// Limpio las propiedades
			
			ve.clearProperty("file.resource.loader.description");
			ve.clearProperty("file.resource.loader.class");
			ve.clearProperty("file.resource.loader.path");
			ve.clearProperty("file.resource.loader.cache");
			ve.clearProperty("file.resource.loader.modificationCheckInterval");
			
			Properties properties = new Properties();

			properties.setProperty("file.resource.loader.description", "Velocity File Resource Loader");															  		 
			properties.setProperty("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.FileResourceLoader");
			properties.setProperty("file.resource.loader.path", reverseTemplatesPath);
			properties.setProperty("file.resource.loader.cache", "false");
			properties.setProperty("file.resource.loader.modificationCheckInterval", "2");
			
			

			ve.init(properties);
			
			VelocityContext context = new VelocityContext();

			context.put("connectionDriverClass", connectionDriverClass);
			context.put("connectionUrl", connectionUrl);
			context.put("connectionUsername", connectionUsername);
			context.put("connectionPassword", connectionPassword);
			context.put("companyDomainName", companyDomainName);
			context.put("companyDomainNameForPojoLocation", companyDomainNameForPojoLocation);
			context.put("makeItXml", makeItXml);
			context.put("connectionDriverJarPath", connectionDriverJarPath);
			context.put("destinationDirectory", destinationDirectory);
			context.put("tablesList", tablesList);
			context.put("isTableList", ZathuraReverseEngineeringUtil.validationsList(tablesList));
			context.put("schema", schema);
			context.put("catalog", catalog);
			context.put("catalogAndSchema", catalogAndSchema);
			if(EclipseGeneratorUtil.javaVersion==null || EclipseGeneratorUtil.javaVersion.equals("")==true){
				throw new Exception("The JavaVersion not Found for this reason is not posible generate");
			}
			context.put("javaVersion",EclipseGeneratorUtil.javaVersion);
			

			doCfg(context);
			doBuild(context);
			doRevEng(context);
			doBuildCompile(context);
			
			log.info("Execute end doTemplate");
		} catch (Exception e) {
			log.info(e.getMessage());
			throw e;
		}finally{
			log.info("Chaged ContextClassLoader:"+loader);
			thread.setContextClassLoader(loader);
		}
	}

	/**
	 * Do cfg.
	 *
	 * @param context the context
	 */
	private void doCfg(VelocityContext context)throws Exception {
		log.info("Begin doCfg");
		try {
			Template cfg = null;
			StringWriter swCfg = new StringWriter();

			try {
				cfg = ve.getTemplate("hibernate.cfg.xml.vm");
			} catch (ResourceNotFoundException rnfe) {
				rnfe.printStackTrace();
				log.info("doCfg",rnfe);
			} catch (ParseErrorException pee) {
				// syntax error: problem parsing the template
				pee.printStackTrace();
				log.info("doCfg",pee);
			} catch (MethodInvocationException mie) {
				// something invoked in the template
				// threw an exception
				mie.printStackTrace();
				log.info("doCfg",mie);
			} catch (Exception e) {
				e.printStackTrace();
				log.info("doCfg",e);
			}

			cfg.merge(context, swCfg);

			FileWriter fstream = new FileWriter(tempFiles + "hibernate.cfg.xml");
			BufferedWriter out = new BufferedWriter(fstream);
			out.write(swCfg.toString());
			
			// Close the output stream
			out.close();

			log.info("End doCfg");

		} catch (Exception e) {
			log.error("Error doCfg", e);
			throw e;
		}
	}

	/**
	 * Do build.
	 *
	 * @param context the context
	 */
	private void doBuild(VelocityContext context)throws Exception {
		log.info("Begin doBuild");
		try {
			Template build = null;
			StringWriter swBuild = new StringWriter();

			try {
				build = ve.getTemplate("build.xml.vm");
			} catch (ResourceNotFoundException rnfe) {
				rnfe.printStackTrace();
				log.info("doBuild",rnfe);				
			} catch (ParseErrorException pee) {
				// syntax error: problem parsing the template
				pee.printStackTrace();
				log.info("doBuild",pee);
			} catch (MethodInvocationException mie) {
				// something invoked in the template
				// threw an exception
				mie.printStackTrace();
				log.info("doBuild",mie);
			} catch (Exception e) {
				e.printStackTrace();
				log.info("doBuild",e);
			}

			build.merge(context, swBuild);
			log.info(swBuild.toString());
			FileWriter fstream = new FileWriter(tempFiles + "build.xml");
			BufferedWriter out = new BufferedWriter(fstream);
			out.write(swBuild.toString());
			// Close the output stream
			out.close();

			log.info("End doBuild");

		} catch (Exception e) {
			log.error("Error doBuild", e);
			throw e;
		}
	}

	/**
	 * Do rev eng.
	 *
	 * @param context the context
	 */
	private void doRevEng(VelocityContext context)throws Exception {
		log.info("Begin doRevEng");
		try {
			Template revEng = null;
			StringWriter swRevEng = new StringWriter();

			try {
				revEng = ve.getTemplate("hibernate.reveng.xml.vm");
			} catch (ResourceNotFoundException rnfe) {
				rnfe.printStackTrace();
				log.info("doRevEng",rnfe);
			} catch (ParseErrorException pee) {
				// syntax error: problem parsing the template
				pee.printStackTrace();
				log.info("doRevEng",pee);
			} catch (MethodInvocationException mie) {
				// something invoked in the template
				// threw an exception
				mie.printStackTrace();
				log.info("doRevEng",mie);
			} catch (Exception e) {
				e.printStackTrace();
				log.info("doRevEng",e);
			}

			revEng.merge(context, swRevEng);

			FileWriter fstream = new FileWriter(tempFiles + "hibernate.reveng.xml");
			BufferedWriter out = new BufferedWriter(fstream);
			out.write(swRevEng.toString());
			// Close the output stream
			out.close();

			log.info("End doRevEng");

		} catch (Exception e) {
			log.error("Error doRevEng", e);
			throw e;
		}
	}

	

	/**
	 * Do build compile.
	 *
	 * @param context the context
	 */
	private void doBuildCompile(VelocityContext context)throws Exception {
		log.info("Begin doBuildCompile");
		try {
			Template build = null;
			StringWriter swBuild = new StringWriter();

			try {
				build = ve.getTemplate("buildCompile.xml.vm");
			} catch (ResourceNotFoundException rnfe) {
				rnfe.printStackTrace();
				log.error("doBuildCompile",rnfe);
			} catch (ParseErrorException pee) {
				// syntax error: problem parsing the template
				pee.printStackTrace();
				log.error("doBuildCompile",pee);
			} catch (MethodInvocationException mie) {
				// something invoked in the template
				// threw an exception
				mie.printStackTrace();
				log.error("doBuildCompile",mie);
			} catch (Exception e) {
				e.printStackTrace();
				log.error("doBuildCompile",e);
			}

			build.merge(context, swBuild);
			log.info(swBuild.toString());
			FileWriter fstream = new FileWriter(tempFiles + "buildCompile.xml");
			BufferedWriter out = new BufferedWriter(fstream);
			out.write(swBuild.toString());
			// Close the output stream
			out.close();

			log.info("End doBuildCompile");

		} catch (Exception e) {
			log.error("Error doBuildCompile",e);
			throw e;
		}
	}
	
	
	
	/**
	 * Call ant process.
	 */
	public static void callAntProcess() throws Exception{
		log.info("Begin Ant");
		
		Thread thread = Thread.currentThread();
		ClassLoader loader = thread.getContextClassLoader();
		thread.setContextClassLoader(EclipseGeneratorUtil.bundleClassLoader);
		log.info("Chaged ContextClassLoader:"+EclipseGeneratorUtil.bundleClassLoader);
		
		try {
			File buildFile = new File(ZathuraReverseEngineeringUtil.getTempFileBuildPath());
			Project p = new Project();
			p.setUserProperty("ant.file", buildFile.getAbsolutePath());	
			org.apache.tools.ant.listener.Log4jListener log4jListener=new Log4jListener();
			p.addBuildListener(log4jListener);

			try {
				p.fireBuildStarted();
				p.init();
				ProjectHelper helper = ProjectHelper.getProjectHelper();
				p.addReference("ant.projectHelper", helper);
				helper.parse(p, buildFile);
				p.executeTarget(p.getDefaultTarget());
				p.fireBuildFinished(null);
			} catch (BuildException e) {
				p.fireBuildFinished(e);
				log.error("callAntProcess Helper 1 Build",e);
				throw e;
			}

			// Compile Pojos
			File buildFile2 = new File(ZathuraReverseEngineeringUtil.getTempFileBuildCompilePath());
			Project p2 = new Project();
			p2.setUserProperty("ant.file", buildFile2.getAbsolutePath());
			log4jListener=new Log4jListener();	
			p2.addBuildListener(log4jListener);
			

			try {
				p2.fireBuildStarted();
				p2.init();
				ProjectHelper helper2 = ProjectHelper.getProjectHelper();
				p2.addReference("ant.projectHelper", helper2);
				helper2.parse(p2, buildFile2);
				p2.executeTarget(p2.getDefaultTarget());
				p2.fireBuildFinished(null);
			} catch (BuildException e) {
				p2.fireBuildFinished(e);
				log.error("callAntProcess Helper 2 Compile Pojos",e);
				throw e;
			}
		} catch (Exception e) {
			throw e;
		}finally{
			log.info("Chaged ContextClassLoader:"+loader);
			thread.setContextClassLoader(loader);
		}
		log.info("End Ant");
	}

}
