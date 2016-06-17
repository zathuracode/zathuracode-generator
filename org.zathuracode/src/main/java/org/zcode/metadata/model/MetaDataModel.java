package org.zcode.metadata.model;

import java.util.List;

// TODO: Auto-generated Javadoc
/**
 * Powered by jpa2web Zathura Generator.
 *
 * @author Diego Armando Gomez Mosquera (dgomez@vortexbird.com)
 * @version 1.0
 */
public class MetaDataModel {

	/** The meta data. */
	private List<MetaData> theMetaData;

	/**
	 * Gets the the meta data.
	 *
	 * @return the the meta data
	 */
	public List<MetaData> getTheMetaData() {
		return theMetaData;
	}

	/**
	 * Sets the the meta data.
	 *
	 * @param theMetaData the meta data
	 */
	public void setTheMetaData(List<MetaData> theMetaData) {
		this.theMetaData = theMetaData;
	}

}
