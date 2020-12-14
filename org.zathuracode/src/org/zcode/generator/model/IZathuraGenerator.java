package org.zcode.generator.model;

import java.util.Properties;

import org.zcode.metadata.model.MetaDataModel;


// TODO: Auto-generated Javadoc
/**
 * Zathura Generator.
 *
 * @author Diego Armando Gomez Mosquera (dgomez@vortexbird.com)
 * @version 1.0
 */
public interface IZathuraGenerator {
	
	/**
	 * To generate.
	 *
	 * @param metaDataModel the meta data model
	 * @param projectName the project name
	 * @param folderProjectPath the folder project path
	 * @param propiedades the propiedades
	 */
	public void toGenerate(MetaDataModel metaDataModel, String projectName, String folderProjectPath, Properties propiedades)throws Exception;

}
