package org.zcode.reverse.utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

/*
import net.sourceforge.squirrel_sql.fw.sql.ISQLAlias;
import net.sourceforge.squirrel_sql.fw.sql.ISQLConnection;
import net.sourceforge.squirrel_sql.fw.sql.ISQLDriver;
import net.sourceforge.squirrel_sql.fw.sql.ITableInfo;
import net.sourceforge.squirrel_sql.fw.sql.SQLDriver;
import net.sourceforge.squirrel_sql.fw.sql.SQLDriverManager;
*/
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// TODO: Auto-generated Javadoc
/**
 * Zathura Generator.
 * 
 * @author Diego Armando Gomez Mosquera (dgomez@vortexbird.com)
 * @author William Altuzarra Noriega Noriega (williamaltu@gmail.com)
 * @version 1.0
 */
public class ZathuraReverseEngineeringUtil {
	
	

	/** The full path. */
	private static String fullPath = "";

	/** The slash. */
	public static String slash = System.getProperty("file.separator");

	/** The rev eng file name. */
	public static String revEngFileName = "hibernate.reveng.xml";

	/** The database types file name. */
	public static String databaseTypesFileName = "database-types.xml";

	/** The build xml file name. */
	public static String buildXmlFileName = "build.xml";

	/** The build compile xml file name. */
	public static String buildCompileXmlFileName = "buildCompile.xml";

	/** The reverse templates. */
	private static String reverseTemplates = "reverseTemplates"
			+ ZathuraReverseEngineeringUtil.slash;

	/** The temp files. */
	private static String tempFiles = "tempFiles"
			+ ZathuraReverseEngineeringUtil.slash;

	/** The ant rev eng. */
	private static String antRevEng = "antBuild-revEng"
			+ ZathuraReverseEngineeringUtil.slash;

	/** The xml database types path. */
	private static String xmlDatabaseTypesPath = "config"
			+ ZathuraReverseEngineeringUtil.slash + databaseTypesFileName;

	/** The temp file build path. */
	private static String tempFileBuildPath = tempFiles + buildXmlFileName;

	/** The temp file build compile path. */
	private static String tempFileBuildCompilePath = tempFiles
			+ buildCompileXmlFileName;

//	
//	/** The sql connection. */
//	private static ISQLConnection sqlConnection = null;
//
//	/** The alias. */
//	private static ISQLAlias alias = null;
//
//	/** The sql driver. */
//	private static ISQLDriver sqlDriver = null;
//	// private static SQLDriverPropertyCollection driverProperties =null;
//	/** The sql driver manager. */
//	private static SQLDriverManager sqlDriverManager = null;
	
	
	private static Connection connection;

	/** The types. */
	String[] types = { "TABLE", "VIEW", "SYNONYM", "ALIAS" };

	/** Log4j. */
    private static final Logger log = LoggerFactory.getLogger(ZathuraReverseEngineeringUtil.class);


	/** Generator Model. */
	private static HashMap<String, DatabaseTypeModel> theZathuraDataBaseTypes = null;

	/** The names of generators. */
	private static java.util.List<String> theZathuraDataBaseNames = new ArrayList<String>();

	/**
	 * Gets the temp file build path.
	 * 
	 * @return the temp file build path
	 */
	public static String getTempFileBuildPath() {
		if (fullPath != null && fullPath.equals("") != true) {
			return fullPath + tempFileBuildPath;
		}
		return tempFileBuildPath;
	}

	/**
	 * Gets the temp file build compile path.
	 * 
	 * @return the temp file build compile path
	 */
	public static String getTempFileBuildCompilePath() {
		if (fullPath != null && fullPath.equals("") != true) {
			return fullPath + tempFileBuildCompilePath;
		}
		return tempFileBuildCompilePath;
	}

	/**
	 * Gets the xml database types path.
	 * 
	 * @return the xml database types path
	 */
	public static String getXmlDatabaseTypesPath() {
		if (fullPath != null && fullPath.equals("") != true) {
			return fullPath + xmlDatabaseTypesPath;
		}
		return xmlDatabaseTypesPath;
	}

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
	 * @param fullPath
	 *            the full path
	 */
	public static void setFullPath(String fullPath) {

		if (fullPath != null
				&& fullPath.startsWith("/")
				&& System.getProperty("os.name").toUpperCase()
						.contains("WINDOWS") == true) {
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
		ZathuraReverseEngineeringUtil.fullPath = fullPath;
	}

	/**
	 * Gets the reverse templates.
	 * 
	 * @return the reverse templates
	 */
	public static String getReverseTemplates() {
		if (fullPath != null && fullPath.equals("") != true) {
			return fullPath + reverseTemplates;
		}
		return reverseTemplates;
	}

	/**
	 * Gets the temp files path.
	 * 
	 * @return the temp files path
	 */
	public static String getTempFilesPath() {
		if (fullPath != null && fullPath.equals("") != true) {
			return fullPath + tempFiles;
		}
		return tempFiles;
	}

	/**
	 * Gets the ant build rev eng path.
	 * 
	 * @return the ant build rev eng path
	 */
	public static String getAntBuildRevEngPath() {
		if (fullPath != null && fullPath.equals("") != true) {
			return fullPath + antRevEng;
		}
		return reverseTemplates;
	}

	/**
	 * Fix domain.
	 * 
	 * @param domainName
	 *            the domain name
	 * @return the string
	 */
	public static String fixDomain(String domainName) {
		String retFixDomian = null;
		if (domainName == null) {
			return "";
		}
		if (domainName.length() > 0) {
			retFixDomian = replaceAll(domainName, ".",
					ZathuraReverseEngineeringUtil.slash);
		}
		return retFixDomian;
	}

	/**
	 * Replace all.
	 * 
	 * @param cadena
	 *            the cadena
	 * @param old
	 *            the old
	 * @param snew
	 *            the snew
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
	 * Project path in console.
	 * 
	 * @return the string
	 */
	public static String projectPathInConsole() {
		URL url = ZathuraReverseEngineeringUtil.class
				.getResource("ReverseUtil.class");
		String classPath = url.getPath().substring(1);
		int tmp = classPath.indexOf("zathura-ReverseMappingTool") + 26;

		String inconmpletePath = classPath.substring(0, tmp);
		String cPath = "" + classPath.charAt(tmp);
		String projectPath = null;

		if (cPath.equals("2")) {
			projectPath = inconmpletePath + "2"
					+ ZathuraReverseEngineeringUtil.slash;
		} else {
			projectPath = inconmpletePath + ZathuraReverseEngineeringUtil.slash;
		}
		return projectPath;
	}

	/**
	 * Validations list.
	 * 
	 * @param list
	 *            the list
	 * @return true, if validations list
	 */
	public static boolean validationsList(List list) {
		if (list != null) {
			if (!list.isEmpty() && list.size() > 0) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	/**
	 * Load zathura database types.
	 * 
	 * @return the hash map< string, database type model>
	 * @throws FileNotFoundException
	 *             the file not found exception
	 * @throws XMLStreamException
	 *             the XML stream exception
	 * @throws InstantiationException
	 *             the instantiation exception
	 * @throws IllegalAccessException
	 *             the illegal access exception
	 * @throws ClassNotFoundException
	 *             the class not found exception
	 */
	public static HashMap<String, DatabaseTypeModel> loadZathuraDatabaseTypes()
			throws FileNotFoundException, XMLStreamException,
			InstantiationException, IllegalAccessException,
			ClassNotFoundException {

		log.info("Reading DatabaseTypes:"+ ZathuraReverseEngineeringUtil.xmlDatabaseTypesPath);

		DatabaseTypeModel databaseTypeModel = null;
		boolean boolName = false;
		boolean boolUrl = false;
		boolean boolDriverClassName = false;
		boolean boolGroupId=false;
		boolean boolArtifactId=false;
		boolean boolVersion=false;
		

		theZathuraDataBaseTypes = new HashMap<String, DatabaseTypeModel>();

		// Get the factory instace first.
		XMLInputFactory factory = XMLInputFactory.newInstance();
		factory.setProperty(XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES,
				Boolean.TRUE);
		factory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES,
				Boolean.FALSE);
		factory.setProperty(XMLInputFactory.IS_NAMESPACE_AWARE, Boolean.TRUE);
		factory.setProperty(XMLInputFactory.IS_COALESCING, Boolean.TRUE);

		log.debug("FACTORY: " + factory);

		XMLEventReader r = factory.createXMLEventReader(new FileInputStream(getXmlDatabaseTypesPath()));

		// iterate as long as there are more events on the input stream
		while (r.hasNext()) {
			XMLEvent e = r.nextEvent();
			if (e.isStartElement()) {
				StartElement startElement = (StartElement) e;
				QName qname = startElement.getName();
				String localName = qname.getLocalPart();
				if (localName.equals("database") == true) {
					databaseTypeModel = new DatabaseTypeModel();
				} else if (localName.equals("name") == true) {
					boolName = true;
					log.info(localName);
				} else if (localName.equals("url") == true) {
					boolUrl = true;
					log.info(localName);
				} else if (localName.equals("driverClassName") == true) {
					boolDriverClassName = true;
					log.info(localName);
				}else if (localName.equals("groupId") == true) {
					boolGroupId = true;
					log.info(localName);
				}else if (localName.equals("artifactId") == true) {
					boolArtifactId = true;
					log.info(localName);
				}else if (localName.equals("version") == true) {
					boolVersion = true;
					log.info(localName);
				}
				

			} else if (e.isCharacters()) {
				Characters characters = (Characters) e;
				String cadena = characters.getData().toString().trim();
				if (boolName == true) {
					databaseTypeModel.setName(cadena);
					theZathuraDataBaseNames.add(cadena);
					boolName = false;
					log.info(cadena);
				} else if (boolUrl == true) {
					databaseTypeModel.setUrl(cadena);
					boolUrl = false;
					log.info(cadena);
				} else if (boolDriverClassName == true) {
					databaseTypeModel.setDriverClassName(cadena);
					boolDriverClassName = false;
					log.info(cadena);
				}else if (boolGroupId == true) {
					databaseTypeModel.setGroupId(cadena);
					boolGroupId = false;
					log.info(cadena);
				}else if (boolArtifactId == true) {
					databaseTypeModel.setArtifactId(cadena);
					boolArtifactId = false;
					log.info(cadena);
				}else if (boolVersion == true) {
					databaseTypeModel.setVersion(cadena);
					boolVersion = false;
					log.info(cadena);
				}
			} else if (e.isEndElement() == true) {
				EndElement endElement = (EndElement) e;
				QName qname = endElement.getName();
				String localName = qname.getLocalPart();
				if (localName.equals("database") == true) {
					theZathuraDataBaseTypes.put(databaseTypeModel.getName(),databaseTypeModel);
				}
			}

		}
		log.debug("DataBaseTypes length:" + theZathuraDataBaseTypes.size());
		return theZathuraDataBaseTypes;
	}

	/**
	 * Gets the database type model.
	 * 
	 * @param name
	 *            the name
	 * @return the database type model
	 */
	public static DatabaseTypeModel getDatabaseTypeModel(String name) {
		if (theZathuraDataBaseTypes == null) {
			try {
				theZathuraDataBaseTypes = loadZathuraDatabaseTypes();
			} catch (FileNotFoundException e) {

				e.printStackTrace();
			} catch (XMLStreamException e) {

				e.printStackTrace();
			} catch (InstantiationException e) {

				e.printStackTrace();
			} catch (IllegalAccessException e) {

				e.printStackTrace();
			} catch (ClassNotFoundException e) {

				e.printStackTrace();
			}
		}
		return theZathuraDataBaseTypes.get(name);
	}

	

	/**
	 * Validar package.
	 * 
	 * @param packageName
	 *            the package name
	 * @throws Exception
	 *             the exception
	 */
	public static void validarPackage(String packageName) throws Exception {
		if (packageName.startsWith(".") || packageName.endsWith(".")) {
			throw new Exception("A package name cannot start or end with a dot");
		}
	}

	/**
	 * Delete files.
	 * 
	 * @param path
	 *            the path
	 */
	public static void deleteFiles(String path) {
		File file = new File(path);
		deleteFiles(file);
	}

	/**
	 * Delete files.
	 * 
	 * @param file
	 *            the file
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
	 * Creates the folder.
	 * 
	 * @param path
	 *            the path
	 * @return the file
	 */
	public static File createFolder(String path) {
		
		File file = new File(path);
		
		
        
        //set application user permissions to 455
        file.setExecutable(false);
        file.setReadable(true);
        file.setWritable(true);
         
        //change permission to 777 for all the users
        //no option for group and others
        file.setExecutable(false, false);
        file.setReadable(true, false);
        file.setWritable(true, false);
        
        /*
        //using PosixFilePermission to set file permissions 777
        Set<PosixFilePermission> perms = new HashSet<PosixFilePermission>();
        //add owners permission
        perms.add(PosixFilePermission.OWNER_READ);
        perms.add(PosixFilePermission.OWNER_WRITE);
        perms.add(PosixFilePermission.OWNER_EXECUTE);
        //add group permissions
        perms.add(PosixFilePermission.GROUP_READ);
        perms.add(PosixFilePermission.GROUP_WRITE);
        perms.add(PosixFilePermission.GROUP_EXECUTE);
        //add others permissions
        perms.add(PosixFilePermission.OTHERS_READ);
        perms.add(PosixFilePermission.OTHERS_WRITE);
        perms.add(PosixFilePermission.OTHERS_EXECUTE);
         
        Files.setPosixFilePermissions(Paths.get(path), perms);
		*/
        file.mkdirs();
		return file;
	}

	/**
	 * Borrar los archivos de la carpeta tempFiles.
	 * 
	 * @param path
	 *            the path
	 */
	public static void resetTempFiles(String path) {
		// Borrar carpeta de temporales
		deleteFiles(path);
		// Crea carpeta de temporales
		createFolder(path);
	}

	/**
	 * Test driver.
	 * 
	 * @param url
	 *            the url
	 * @param driverClassName
	 *            the driver class name
	 * @param user
	 *            the user
	 * @param password
	 *            the password
	 * @throws ClassNotFoundException
	 *             the class not found exception
	 * @throws SQLException
	 *             the SQL exception
	 * @throws Exception
	 *             the exception
	 */
	public static void testDriver(String url, String driverClassName,String user, String password) throws ClassNotFoundException,SQLException, Exception {

		connection = getConnection(url, driverClassName, user, password);

	}

	/**
	 * Gets the connection.
	 * 
	 * @param url
	 *            the url
	 * @param driverClassName
	 *            the driver class name
	 * @param user
	 *            the user
	 * @param password
	 *            the password
	 * @return the connection
	 * @throws ClassNotFoundException
	 *             the class not found exception
	 * @throws SQLException
	 *             the SQL exception
	 * @throws Exception
	 *             the exception
	 */

	public static Connection getConnection(String url, String driverClassName,String user, String password) throws ClassNotFoundException,SQLException, Exception {

		connection=DriverManager.getConnection(url, user, password);
		
		return connection;

	}

	/*
	 * public static Connection getConnection(String url, String
	 * driverClassName, String user, String password) throws
	 * ClassNotFoundException, SQLException, Exception {
	 * 
	 * sqlDriverManager = new SQLDriverManager(); sqlDriver = new SQLDriver();
	 * alias = new AmaziliaSQLAlias();
	 * 
	 * sqlDriver.setDriverClassName(driverClassName); sqlDriver.setUrl(url);
	 * alias.setUrl(url); alias.setUserName(user); alias.setPassword(password);
	 * 
	 * sqlConnection = sqlDriverManager.getConnection(sqlDriver, alias, user,
	 * password); return sqlConnection.getConnection();
	 * 
	 * }
	 */
	/**
	 * Close all.
	 */
	public static void closeAll() {

		try {
			if (connection != null) {
				connection.close();
			}
		} catch (SQLException e) {
			// Ignore
		}

	}

	/**
	 * Gets the catalogs.
	 * 
	 * @return the catalogs
	 * @throws SQLException
	 *             the SQL exception
	 */
	public static List<String> getCatalogs() throws SQLException {
		
		List<String> listCatalogs=new ArrayList<String>();
		
		if(connection.getMetaData()==null){
			throw new SQLException("Data Base MetaData is not Supported");
		}
		
		listCatalogs=DatabaseUtilities.getCatalogs(connection);
		/**
		ResultSet resultSet=connection.getMetaData().getCatalogs();
		while (resultSet.next()) {
			listCatalogs.add(resultSet.getString(1));
			log.info("Catalog:"+resultSet.getString(1));
		}
		**/
		
		return listCatalogs;
	}

	/**
	 * Gets the schemas.
	 * 
	 * @return the schemas
	 * @throws SQLException
	 *             the SQL exception
	 * @throws Exception
	 *             the exception
	 */
	public static List<String> getSchemas() throws SQLException, Exception {
		List<String> listSchemas=new ArrayList<String>();
		
		if(connection.getMetaData()==null){
			throw new SQLException("Data Base MetaData is not Supported");
		}
		listSchemas=DatabaseUtilities.getSchemas(connection);
		return listSchemas;
	}

	// public synchronized ITableInfo[] getTables(String catalog, String
	// schemaPattern, String tableNamePattern,String[] types, ProgressCallBack
	// progressCallBack)
	/**
	 * Gets the tables.
	 * 
	 * @param catalog
	 *            the catalog
	 * @param schemaPattern
	 *            the schema pattern
	 * @param tableNamePattern
	 *            the table name pattern
	 * @return the tables
	 * @throws SQLException
	 *             the SQL exception
	 */
	public static List<String> getTables(String catalog, String schemaPattern,String tableNamePattern) throws SQLException {
		List<String> listTables=DatabaseUtilities.getTables(connection, catalog, schemaPattern, tableNamePattern);
		return listTables;
	}


}
