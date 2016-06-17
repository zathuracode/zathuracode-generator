package org.zcode.metadata.model;

// TODO: Auto-generated Javadoc
/**
 * Powered by jpa2web Zathura Generator.
 *
 * @author Diego Armando Gomez Mosquera (dgomez@vortexbird.com)
 * @version 1.0
 */
public class OneToManyMember extends Member {

	/** The collection type. */
	private Class collectionType;
	
	/** The mapped by. */
	private String mappedBy;

	/**
	 * The Constructor.
	 *
	 * @param name the name
	 * @param showName the show name
	 * @param type the type
	 * @param collectionType the collection type
	 * @param order the order
	 */
	public OneToManyMember(String name, String showName, Class type, Class collectionType, int order) {
		super(name, showName, type, order);
		this.collectionType = collectionType;
	}

	/**
	 * Gets the collection type.
	 *
	 * @return the collection type
	 */
	public Class getCollectionType() {
		return collectionType;
	}

	/**
	 * Sets the collection type.
	 *
	 * @param collectionType the collection type
	 */
	public void setCollectionType(Class collectionType) {
		this.collectionType = collectionType;
	}

	/**
	 * Gets the mapped by.
	 *
	 * @return the mapped by
	 */
	public String getMappedBy() {
		return mappedBy;
	}

	/**
	 * Sets the mapped by.
	 *
	 * @param mappedBy the mapped by
	 */
	public void setMappedBy(String mappedBy) {
		this.mappedBy = mappedBy;
	}

}
