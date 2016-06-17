package org.zcode.metadata.reader;

import org.zcode.metadata.model.MetaDataModel;

// TODO: Auto-generated Javadoc
/**
 * Zathura Generator.
 *
 * @author Diego Armando Gomez Mosquera (dgomez@vortexbird.com)
 * @version 1.0
 */
public interface IMetaDataReader {
	
	/**
	 * Load meta data model.
	 *
	 * @param path the path
	 * @param pckgName the pckg name
	 * @return the meta data model
	 */
	public MetaDataModel loadMetaDataModel(String path, String pckgName)throws Exception;
}
