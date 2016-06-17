package org.zcode.generator.model;

// TODO: Auto-generated Javadoc
/**
 * Zathura Generator.
 *
 * @author Diego Armando Gomez Mosquera (dgomez@vortexbird.com)
 * @version 1.0
 */
public class GeneratorModel {

	/** The name. */
	private String name;
	
	/** The gui name. */
	private String guiName;
	
	/** The description. */
	private String description;
	
	/** The persistence. */
	private String persistence;
	
	/** The Zathura version belong. */
	private String zathuraVersion;
	
	/** The zathura generator. */
	private IZathuraGenerator zathuraGenerator;

	/**
	 * Gets the gui name.
	 *
	 * @return the gui name
	 */
	public String getGuiName() {
		return guiName;
	}

	/**
	 * Sets the gui name.
	 *
	 * @param guiName the gui name
	 */
	public void setGuiName(String guiName) {
		this.guiName = guiName;
	}

	/**
	 * Gets the persistence.
	 *
	 * @return the persistence
	 */
	public String getPersistence() {
		return persistence;
	}

	/**
	 * Sets the persistence.
	 *
	 * @param persistence the persistence
	 */
	public void setPersistence(String persistence) {
		this.persistence = persistence;
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
	 * Gets the description.
	 *
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the description.
	 *
	 * @param description the description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Gets the zathura generator.
	 *
	 * @return the zathura generator
	 */
	public IZathuraGenerator getZathuraGenerator() {
		return zathuraGenerator;
	}

	/**
	 * Sets the zathura generator.
	 *
	 * @param zathuraGenerator the zathura generator
	 */
	public void setZathuraGenerator(IZathuraGenerator zathuraGenerator) {
		this.zathuraGenerator = zathuraGenerator;
	}

	/**
	 * Gets the zathura version.
	 *
	 * @return the zathura version
	 */
	public String getZathuraVersion() {
		return zathuraVersion;
	}

	/**
	 * Sets the zathura version.
	 *
	 * @param zathuraVersion the zathura version
	 */
	public void setZathuraVersion(String zathuraVersion) {
		this.zathuraVersion = zathuraVersion;
	}

}
