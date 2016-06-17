package org.zcode.eclipse.plugin.generator.utilities;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zcode.generator.utilities.GeneratorUtil;


// TODO: Auto-generated Javadoc
/**
 * The Class ConnectionsUtils.
 */
public class ConnectionsUtils {

	/**
	 * The Constructor.
	 */
	private ConnectionsUtils() {

	}

	/** The properties. */
	private static Properties properties = new Properties();

	/** Log4j. */
	private static final Logger log = LoggerFactory.getLogger(ConnectionsUtils.class);

	/** xml file path. */
	private static String xmlConfigConnections = GeneratorUtil.getXmlConfig() + "zathura-connections.properties";

	/** Generator Model. */
	private static HashMap<String, ConnectionModel> theZathuraConnections = null;
	

	static {
		try {
			loadZathuraConnections();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	

	/**
	 * Load zathura connections.
	 *
	 * @throws FileNotFoundException the file not found exception
	 * @throws IOException the IO exception
	 */
	private static void loadZathuraConnections() throws FileNotFoundException, IOException {
		log.info("Reading connection config:" + xmlConfigConnections);
		theZathuraConnections = new HashMap<String, ConnectionModel>();
		try {
			properties.load(new java.io.FileInputStream(xmlConfigConnections));
		} catch (Exception e) {
			properties.store(new java.io.FileOutputStream(xmlConfigConnections), "");
		}
		List<String> connectionNames = new ArrayList<String>();
		Enumeration<Object> theKeys = properties.keys();
		while (theKeys.hasMoreElements()) {
			Object object = (Object) theKeys.nextElement();
			if (object instanceof String) {
				String key = (String) object;
				if (key.contains("-name")) {
					connectionNames.add(properties.getProperty(key));
				}
			}
		}

		for (String nameConnection : connectionNames) {
			ConnectionModel connectionModel = new ConnectionModel();
			connectionModel.setDriverClassName(properties.getProperty(nameConnection + "-" + "driverClass"));
			connectionModel.setJarPath(properties.getProperty(nameConnection + "-" + "jarPath"));
			connectionModel.setName(properties.getProperty(nameConnection + "-" + "name"));
			connectionModel.setPassword(properties.getProperty(nameConnection + "-" + "password"));
			connectionModel.setUrl(properties.getProperty(nameConnection + "-" + "url"));
			connectionModel.setUser(properties.getProperty(nameConnection + "-" + "user"));
			connectionModel.setDriverTemplate(properties.getProperty(nameConnection + "-" + "driverTemplate"));
			
			//Carga la configuracion de Maven
			connectionModel.setConnectionArtifactId(properties.getProperty(nameConnection + "-" + "connectionArtifactId"));
			connectionModel.setConnectionGroupId(   properties.getProperty(nameConnection + "-" + "connectionGroupId"));
			connectionModel.setConnectionVersion(   properties.getProperty(nameConnection + "-" + "connectionVersion"));
			
			theZathuraConnections.put(nameConnection, connectionModel);
		}
	}

	/**
	 * Gets the the zathura connections.
	 *
	 * @return the the zathura connections
	 */
	public static HashMap<String, ConnectionModel> getTheZathuraConnections() {
		return theZathuraConnections;
	}

	/**
	 * Gets the the zathura connection model.
	 *
	 * @param name the name
	 * @return the the zathura connection model
	 */
	public static ConnectionModel getTheZathuraConnectionModel(String name) {
		return theZathuraConnections.get(name);
	}

	/**
	 * Gets the connection names.
	 *
	 * @return the connection names
	 */
	public static List<String> getConnectionNames() {

		List<String> connectionNames = new ArrayList<String>();
		Enumeration<Object> theKeys = properties.keys();
		while (theKeys.hasMoreElements()) {
			Object object = (Object) theKeys.nextElement();
			if (object instanceof String) {
				String key = (String) object;
				if (key.contains("-name")) {
					connectionNames.add(properties.getProperty(key));
				}
			}
		}
		return connectionNames;

	}

	/**
	 * Remove a ConnectionModel.
	 *
	 * @param connectionName the connection name
	 * @throws Exception the exception
	 */
	public static void removeConnectionModel(String connectionName) throws Exception {
		properties.remove(connectionName + "-name");
		properties.remove(connectionName + "-driverClass");
		properties.remove(connectionName + "-jarPath");
		properties.remove(connectionName + "-password");
		properties.remove(connectionName + "-url");
		properties.remove(connectionName + "-user");
		properties.remove(connectionName + "-driverTemplate");

		theZathuraConnections.remove(connectionName);

		store();
	}

	/**
	 * Save a connection Model.
	 *
	 * @param connectionModel the connection model
	 * @throws Exception the exception
	 */
	public static void saveConnectionModel(ConnectionModel connectionModel) throws Exception {
		if (connectionModel == null) {
			throw new Exception("Connection model null");
		}
		if (connectionModel.getDriverTemplate() == null || connectionModel.getDriverTemplate().trim().equals("") == true) {
			throw new Exception("Driver Template null");
		}
		if (connectionModel.getDriverClassName() == null || connectionModel.getDriverClassName().trim().equals("") == true) {
			throw new Exception("DriverClassName null");
		}
		if (connectionModel.getJarPath() == null || connectionModel.getJarPath().trim().equals("") == true) {
			throw new Exception("JarPath null");
		}
		if (connectionModel.getName() == null || connectionModel.getName().trim().equals("") == true) {
			throw new Exception("Name null");
		}
		if (connectionModel.getPassword() == null || connectionModel.getPassword().trim().equals("") == true) {
			throw new Exception("Password null");
		}
		if (connectionModel.getUrl() == null || connectionModel.getUrl().trim().equals("") == true) {
			throw new Exception("URL null");
		}
		if (connectionModel.getUser() == null || connectionModel.getUser().trim().equals("") == true) {
			throw new Exception("User null");
		}
		if (connectionModel.getConnectionArtifactId() == null || connectionModel.getConnectionArtifactId().trim().equals("") == true) {
			throw new Exception("Maven ArtifactId null");
		}
		
		if (connectionModel.getConnectionGroupId() == null || connectionModel.getConnectionGroupId().trim().equals("") == true) {
			throw new Exception("Maven GroupId null");
		}
		if (connectionModel.getConnectionVersion() == null || connectionModel.getConnectionVersion().trim().equals("") == true) {
			throw new Exception("Maven Version null");
		}

		properties.put(connectionModel.getName() + "-name", connectionModel.getName());
		properties.put(connectionModel.getName() + "-driverClass", connectionModel.getDriverClassName());
		properties.put(connectionModel.getName() + "-jarPath", connectionModel.getJarPath());
		properties.put(connectionModel.getName() + "-password", connectionModel.getPassword());
		properties.put(connectionModel.getName() + "-url", connectionModel.getUrl());
		properties.put(connectionModel.getName() + "-user", connectionModel.getUser());
		properties.put(connectionModel.getName() + "-driverTemplate", connectionModel.getDriverTemplate());
		
		//Maven
		properties.put(connectionModel.getName() + "-connectionArtifactId", connectionModel.getConnectionArtifactId());
		properties.put(connectionModel.getName() + "-connectionGroupId", connectionModel.getConnectionGroupId());
		properties.put(connectionModel.getName() + "-connectionVersion", connectionModel.getConnectionVersion());
		
		

		// Graba en el properties
		store();

		// Se adjunta a el hashMap
		theZathuraConnections.put(connectionModel.getName(), connectionModel);

	}

	/**
	 * Save properties information in the file.
	 *
	 * @throws FileNotFoundException the file not found exception
	 * @throws IOException the IO exception
	 */
	private static void store() throws FileNotFoundException, IOException {
		properties.store(new java.io.FileOutputStream(xmlConfigConnections), "");
	}

	/**
	 * Returns <tt>true</tt> if this map contains a mapping for the specified
	 * connectionName.
	 * 
	 * @param connectionName
	 *            The key whose presence in this map is to be tested
	 * @return <tt>true</tt> if this map contains a mapping for the specified
	 *         connectionName.
	 */
	public static Boolean connectionExist(String connectionName) {
		return theZathuraConnections.containsKey(connectionName);
	}

}
