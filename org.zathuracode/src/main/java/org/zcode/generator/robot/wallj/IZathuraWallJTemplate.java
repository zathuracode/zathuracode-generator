package org.zcode.generator.robot.wallj;


import org.apache.velocity.VelocityContext;
import org.zcode.metadata.model.MetaData;
import org.zcode.metadata.model.MetaDataModel;



/**
 * Zathuracode Generator
 * www.zathuracode.org
 * @author Diego Armando Gomez (dgomez@vortexbird.com)
 * @version 1.0
 */
public interface IZathuraWallJTemplate {

	/**
	 * Do template.
	 *
	 * @param hdLocation the hd location
	 * @param metaDataModel the meta data model
	 * @param jpaPckgName the jpa pckg name
	 * @param projectName the project name
	 * @param specificityLevel the specificity level
	 * @param domainName the domain name
	 */
	public void doTemplate(String hdLocation, MetaDataModel metaDataModel, String jpaPckgName, String projectName, Integer specificityLevel, String domainName)throws Exception;;

	/**
	 * Do dao spring xml hibernate.
	 *
	 * @param metaData the meta data
	 * @param context the context
	 * @param hdLocation the hd location
	 */
	public void doDaoEjbJpa(MetaData metaData, VelocityContext context, String hdLocation)throws Exception;

	/**
	 * Do dao API EJB
	 *
	 * @param metaData the meta data
	 * @param context the context
	 * @param hdLocation the hd location
	 */
	public void doApiEjbJpa(VelocityContext context, String hdLocation)throws Exception;
	
	/**
	 * Do logic spring xml hibernate.
	 *
	 * @param metaData the meta data
	 * @param context the context
	 * @param hdLocation the hd location
	 * @param dataModel the data model
	 * @param modelName the model name
	 */
	public void doLogicEjbJpa(MetaData metaData, VelocityContext context, String hdLocation, MetaDataModel dataModel, String modelName)throws Exception;

	/**
	 * Do business delegator.
	 *
	 * @param context the context
	 * @param hdLocation the hd location
	 * @param dataModel the data model
	 */
	public void doBusinessDelegator(VelocityContext context, String hdLocation, MetaDataModel dataModel)throws Exception;

	/**
	 * Do jsp.
	 *
	 * @param metaData the meta data
	 * @param context the context
	 * @param hdLocation the hd location
	 * @param dataModel the data model
	 */
	public void doJsp(MetaData metaData, VelocityContext context, String hdLocation, MetaDataModel dataModel)throws Exception;

	/**
	 * Do jsp initial menu.
	 *
	 * @param dataModel the data model
	 * @param context the context
	 * @param hdLocation the hd location
	 */
	public void doJspInitialMenu(MetaDataModel dataModel, VelocityContext context, String hdLocation)throws Exception;

	/**
	 * Do faces config.
	 *
	 * @param dataModel the data model
	 * @param context the context
	 * @param hdLocation the hd location
	 */
	public void doFacesConfig(MetaDataModel dataModel, VelocityContext context, String hdLocation)throws Exception;

	/**
	 * Do dto.
	 *
	 * @param metaData the meta data
	 * @param context the context
	 * @param hdLocation the hd location
	 * @param dataModel the data model
	 * @param modelName the model name
	 */
	public void doDto(MetaData metaData, VelocityContext context, String hdLocation, MetaDataModel dataModel, String modelName)throws Exception;

	/**
	 * Do exceptions.
	 *
	 * @param context the context
	 * @param hdLocation the hd location
	 */
	public void doExceptions(VelocityContext context, String hdLocation)throws Exception;

	/**
	 * Do utilites.
	 *
	 * @param context the context
	 * @param hdLocation the hd location
	 * @param dataModel the data model
	 * @param modelName the model name
	 */
	public void doUtilites(VelocityContext context, String hdLocation, MetaDataModel dataModel, String modelName)throws Exception;

	/**
	 * Do jsp facelets.
	 *
	 * @param context the context
	 * @param hdLocation the hd location
	 */
	public void doJspFacelets(VelocityContext context, String hdLocation)throws Exception;
	
	/**
	 * do BackingBeans
	 * @param metaData
	 * @param context
	 * @param hdLocation
	 * @param dataModel
	 */
	public void doBackingBeans(MetaData metaData, VelocityContext context, String hdLocation, MetaDataModel dataModel)throws Exception;
	
	/**
	 * 
	 * @param dataModel
	 * @param context
	 * @param hdLocation
	 */
	public void doPersitenceXml(MetaDataModel dataModel, VelocityContext context, String hdLocation)throws Exception;
	
}
