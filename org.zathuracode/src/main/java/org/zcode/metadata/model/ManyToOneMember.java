package org.zcode.metadata.model;

import java.util.HashMap;

// TODO: Auto-generated Javadoc
/**
 * Powered by jpa2web Zathura Generator.
 *
 * @author Diego Armando Gomez Mosquera (dgomez@vortexbird.com)
 * @version 1.0
 */
public class ManyToOneMember extends Member {

	/** The hash map nullable column. */
	private HashMap<String, Boolean> hashMapNullableColumn;

	/**
	 * The Constructor.
	 *
	 * @param name the name
	 * @param showName the show name
	 * @param type the type
	 * @param order the order
	 */
	public ManyToOneMember(String name, String showName, Class type, int order) {
		super(name, showName, type, order);

	}

	/**
	 * Gets the hash map nullable column.
	 *
	 * @return the hash map nullable column
	 */
	public HashMap<String, Boolean> getHashMapNullableColumn() {
		return hashMapNullableColumn;
	}

	/**
	 * Sets the hash map nullable column.
	 *
	 * @param hashMapNullableColumn the hash map nullable column
	 */
	public void setHashMapNullableColumn(HashMap<String, Boolean> hashMapNullableColumn) {
		this.hashMapNullableColumn = hashMapNullableColumn;
	}

}
