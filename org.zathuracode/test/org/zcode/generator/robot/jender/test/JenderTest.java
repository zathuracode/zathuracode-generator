package org.zcode.generator.robot.jender.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Properties;

import javax.xml.stream.XMLStreamException;

import org.zcode.eclipse.plugin.generator.utilities.EclipseGeneratorUtil;
import org.zcode.generator.exceptions.GeneratorNotFoundException;
import org.zcode.generator.factory.ZathuraGeneratorFactory;
import org.zcode.generator.model.IZathuraGenerator;
import org.zcode.metadata.model.MetaDataModel;
import org.zcode.metadata.reader.IMetaDataReader;
import org.zcode.metadata.reader.MetaDataReaderFactory;


public class JenderTest {
	
	private static MetaDataModel metaDataModel = null;
	
	static String fullPathProject="/Users/dgomez/runtime-EclipseApplication/demo-banco-jender-web";
	
	//La ruta donde estan los .class de las clases con anotaciones JPA
	static String jpaPath = fullPathProject+"/src/main/java/";
	
	
	static String jpaPckgName = "com.vobi.banco.jender.modelo";
	static String projectName = "demo-banco-jender-web";
	static String folderProjectPath = fullPathProject+"/src/main/java/";
	
	static String webRootFolderPath = fullPathProject="/src/main/webapp/";
	static File pomFile =new File(fullPathProject+"/pom.xml");
	
	public static void main(String[] args) {
		try {
			
			
			
			//Cargo los generadores
			ZathuraGeneratorFactory.loadZathuraGenerators();			
			IZathuraGenerator zathuraGenerator=ZathuraGeneratorFactory.createZathuraGenerator("Jender");
			EclipseGeneratorUtil.javaVersion="1.8";
			EclipseGeneratorUtil.metaDataReader = MetaDataReaderFactory.JPAEntityLoaderEngine;
			EclipseGeneratorUtil.fullPathProject=fullPathProject;
			EclipseGeneratorUtil.javaClassFolderPath=jpaPath;
			EclipseGeneratorUtil.javaEntityPackage=jpaPckgName;
			EclipseGeneratorUtil.projectName=projectName;
			EclipseGeneratorUtil.javaSourceFolderPath=folderProjectPath;
			EclipseGeneratorUtil.webRootFolderPath=webRootFolderPath;
			EclipseGeneratorUtil.libFolderPath="";
			EclipseGeneratorUtil.companyDomainName=jpaPckgName;
			
			//Maven POM JDBC Connector
			EclipseGeneratorUtil.connectionGroupId="org.postgresql";
			EclipseGeneratorUtil.connectionArtifactId="postgresql";
			EclipseGeneratorUtil.connectionVersion="9.3-1102-jdbc41";
			
			EclipseGeneratorUtil.isMavenProject=true;
			
			//Para generacion de los Entity
			
			EclipseGeneratorUtil.workspaceFolderPath="/Users/dgomez/Workspaces/runtime-EclipseApplication";
			EclipseGeneratorUtil.destinationDirectory="/demoBancoWeb/src/main/java";
			EclipseGeneratorUtil.connectionDriverJarPath="/Users/dgomez/Software/java/jdbc/postgresql-9.4.1211.jre6.jar";
			EclipseGeneratorUtil.connectionDriverClass="org.postgresql.Driver";
			EclipseGeneratorUtil.connectionUrl="jdbc:postgresql://127.0.0.1:5432/banco";
			EclipseGeneratorUtil.connectionUsername="postgres";
			EclipseGeneratorUtil.connectionPassword="admin";
			EclipseGeneratorUtil.companyDomainName=jpaPckgName;
			//Sirve para saber si es Hibernate core o JPA en true es HibernateCore
			EclipseGeneratorUtil.makeItXml=true;
			EclipseGeneratorUtil.schema="public";
			EclipseGeneratorUtil.catalogAndSchema="2";
			EclipseGeneratorUtil.catalog=null;
			EclipseGeneratorUtil.tablesList=new ArrayList<String>();
	

			// Genera los entity originales
			EclipseGeneratorUtil.generateJPAReverseEngineering();

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
			properties.put("libFolderPath", "");
			properties.put("folderProjectPath", folderProjectPath);
			properties.put("isMavenProject", true);
			properties.put("pomFile", pomFile);
			
			
			
			
			zathuraGenerator.toGenerate(metaDataModel, projectName, folderProjectPath, properties);
			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XMLStreamException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (GeneratorNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
