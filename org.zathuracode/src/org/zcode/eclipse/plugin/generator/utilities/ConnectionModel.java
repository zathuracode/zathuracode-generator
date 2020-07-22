package org.zcode.eclipse.plugin.generator.utilities;

// TODO: Auto-generated Javadoc
/**
 * The Class ConnectionModel.
 */
public class ConnectionModel {

	/** The driver template. */
	private String driverTemplate;
	
	/** The name. */
	private String name;
	
	/** The url. */
	private String url;
	
	/** The user. */
	private String user;
	
	/** The password. */
	private String password;
	
	/** The driver class name. */
	private String driverClassName;
	
	private  String connectionGroupId;
	
	private  String connectionArtifactId;
	
	private  String connectionVersion;
	
	
	

	public String getConnectionGroupId() {
		return connectionGroupId;
	}

	public void setConnectionGroupId(String connectionGroupId) {
		this.connectionGroupId = connectionGroupId;
	}

	public String getConnectionArtifactId() {
		return connectionArtifactId;
	}

	public void setConnectionArtifactId(String connectionArtifactId) {
		this.connectionArtifactId = connectionArtifactId;
	}

	public String getConnectionVersion() {
		return connectionVersion;
	}

	public void setConnectionVersion(String connectionVersion) {
		this.connectionVersion = connectionVersion;
	}

	/**
	 * The Constructor.
	 */
	public ConnectionModel() {

	}

	/**
	 * The Constructor.
	 *
	 * @param driverTemplate the driver template
	 * @param name the name
	 * @param url the url
	 * @param user the user
	 * @param password the password
	 * @param driverClassName the driver class name
	 * @param jarPath the jar path
	 */
	public ConnectionModel(String driverTemplate, String name, String url, String user, String password, String driverClassName) {
		super();
		this.driverTemplate = driverTemplate;
		this.name = name;
		this.url = url;
		this.user = user;
		this.password = password;
		this.driverClassName = driverClassName;
	}

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name.
	 *
	 * @param name the name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the url.
	 *
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * Sets the url.
	 *
	 * @param url the url
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * Gets the user.
	 *
	 * @return the user
	 */
	public String getUser() {
		return user;
	}

	/**
	 * Sets the user.
	 *
	 * @param user the user
	 */
	public void setUser(String user) {
		this.user = user;
	}

	/**
	 * Gets the password.
	 *
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Sets the password.
	 *
	 * @param password the password
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Gets the driver class name.
	 *
	 * @return the driver class name
	 */
	public String getDriverClassName() {
		return driverClassName;
	}

	/**
	 * Sets the driver class name.
	 *
	 * @param driverClassName the driver class name
	 */
	public void setDriverClassName(String driverClassName) {
		this.driverClassName = driverClassName;
	}

	/**
	 * Gets the driver template.
	 *
	 * @return the driver template
	 */
	public String getDriverTemplate() {
		return driverTemplate;
	}

	/**
	 * Sets the driver template.
	 *
	 * @param driverTemplate the driver template
	 */
	public void setDriverTemplate(String driverTemplate) {
		this.driverTemplate = driverTemplate;
	}

}
