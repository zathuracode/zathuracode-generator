package org.zcode.generator.robot.jender;

import java.util.List;

import org.zcode.metadata.model.MetaData;


/**
 * Zathuracode Generator
 * www.zathuracode.org
 * @author Diego Armando Gomez (dgomez@vortexbird.com)
 * @version 1.0
 */
public interface IJenderStringBuilderForId {

	/**
	 * Final param for id class as variables as string.
	 *
	 * @param list the list
	 * @param metaData the meta data
	 * @return the string
	 */
	public String finalParamForIdClassAsVariablesAsString(List<MetaData> list, MetaData metaData);

	/**
	 * Final param for id class as variables.
	 *
	 * @param list the list
	 * @param metaData the meta data
	 * @return the list< string>
	 */
	public List<String> finalParamForIdClassAsVariables(List<MetaData> list, MetaData metaData);

	/**
	 * Final param for variables data tables for id as list.
	 *
	 * @param theMetaData the meta data
	 * @param metaData the meta data
	 * @return the list< string>
	 */
	public List<String> finalParamForVariablesDataTablesForIdAsList(List<MetaData> theMetaData, MetaData metaData);

	/**
	 * Final param for id variables as list.
	 *
	 * @param theMetaData the meta data
	 * @param metaData the meta data
	 * @return the list< string>
	 */
	public List<String> finalParamForIdVariablesAsList(List<MetaData> theMetaData, MetaData metaData);

	/**
	 * Final param for id for view variables in list.
	 *
	 * @param list the list
	 * @param metaData the meta data
	 * @return the list< string>
	 */
	public List<String> finalParamForIdForViewVariablesInList(List<MetaData> list, MetaData metaData);

	/**
	 * Final param for id for view for sets variables in list.
	 *
	 * @param theMetaData the meta data
	 * @param metaData the meta data
	 * @return the list< string>
	 */
	public List<String> finalParamForIdForViewForSetsVariablesInList(List<MetaData> theMetaData, MetaData metaData);

	/**
	 * Final param for id for dto for sets variables in list.
	 *
	 * @param theMetaData the meta data
	 * @param metaData the meta data
	 * @return the list< string>
	 */
	public List<String> finalParamForIdForDtoForSetsVariablesInList(List<MetaData> theMetaData, MetaData metaData);

	/**
	 * Final param for id for dto in view for sets variables in list.
	 *
	 * @param theMetaData the meta data
	 * @param metaData the meta data
	 * @return the list< string>
	 */
	public List<String> finalParamForIdForDtoInViewForSetsVariablesInList(List<MetaData> theMetaData, MetaData metaData);

	/**
	 * Final param for id class.
	 *
	 * @param list the list
	 * @param metaData the meta data
	 * @return the list< string>
	 */
	public List<String> finalParamForIdClass(List<MetaData> list, MetaData metaData);

	/**
	 * Final param for id class as variables2.
	 *
	 * @param list the list
	 * @param metaData the meta data
	 * @return the list< string>
	 */
	public List<String> finalParamForIdClassAsVariables2(List<MetaData> list, MetaData metaData);

	/**
	 * Final param for id for view class.
	 *
	 * @param list the list
	 * @param metaData the meta data
	 * @return the list< string>
	 */
	public List<String> finalParamForIdForViewClass(List<MetaData> list, MetaData metaData);

	/**
	 * Final param for id.
	 *
	 * @param list the list
	 * @param metaData the meta data
	 * @return the string
	 */
	public String finalParamForId(List<MetaData> list, MetaData metaData);

	/**
	 * Final param for id variables.
	 *
	 * @param list the list
	 * @param metaData the meta data
	 * @return the string
	 */
	public String finalParamForIdVariables(List<MetaData> list, MetaData metaData);

	/**
	 * Needed ids.
	 *
	 * @param list the list
	 */
	public void neededIds(List<MetaData> list);
	
		
	public List<String> dtoConvert(List<MetaData> theMetaData, MetaData metaData);
	
	public List<String> finalParamForIdForViewForSetsVariablesDtoInList(List<MetaData> theMetaData, MetaData metaData);
	

}