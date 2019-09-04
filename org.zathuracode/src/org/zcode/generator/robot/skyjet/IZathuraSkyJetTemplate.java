/*
 * Copyright 2002-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.zcode.generator.robot.skyjet;

import org.apache.velocity.VelocityContext;
import org.zcode.metadata.model.MetaData;
import org.zcode.metadata.model.MetaDataModel;
/**
 * @author Mauricio cárdenas Pérez
 */

public interface IZathuraSkyJetTemplate {

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
	public void doTemplate(String hdLocation, MetaDataModel metaDataModel, String jpaPckgName, String projectName, Integer specificityLevel, String domainName)throws Exception;

	/**
	 * Do dao spring xml hibernate.
	 *
	 * @param metaData the meta data
	 * @param context the context
	 * @param hdLocation the hd location
	 */
	public void doRepository(MetaData metaData, VelocityContext context, String hdLocation)throws Exception;

	/**
	 * Do dao spring xml hibernate.
	 *
	 * @param metaData the meta data
	 * @param context the context
	 * @param hdLocation the hd location
	 */
	//public void doRepositoryAPI(VelocityContext context, String hdLocation)throws Exception;

	/**
	 * Do logic spring xml hibernate.
	 *
	 * @param metaData the meta data
	 * @param context the context
	 * @param hdLocation the hd location
	 * @param dataModel the data model
	 * @param modelName the model name
	 */
	public void doService(MetaData metaData, VelocityContext context, String hdLocation, MetaDataModel dataModel, String modelName)throws Exception;

	/**
	 * Do business delegator.
	 *
	 * @param context the context
	 * @param hdLocation the hd location
	 * @param dataModel the data model
	 */
	//public void doBusinessDelegator(VelocityContext context, String hdLocation, MetaDataModel dataModel)throws Exception;

	/**
	 * Do jsp initial menu.
	 *
	 * @param dataModel the data model
	 * @param context the context
	 * @param hdLocation the hd location
	 */
	//public void doJspInitialMenu(MetaDataModel dataModel, VelocityContext context, String hdLocation)throws Exception;


	/**
	 * Do faces config.
	 *
	 * @param dataModel the data model
	 * @param context the context
	 * @param hdLocation the hd location
	 */
	//TODO Eliminar
	//public void doFacesConfig(MetaDataModel dataModel, VelocityContext context, String hdLocation)throws Exception;
	
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
	//public void doJspFacelets(VelocityContext context, String hdLocation)throws Exception;
	
	
	/**
	 * Do spring context conf files.
	 *
	 * @param context the context
	 * @param hdLocation the hd location
	 * @param dataModel the data model
	 * @param modelName the model name
	 */
	//public void doSpringContextConfFiles(VelocityContext context, String hdLocation, MetaDataModel dataModel, String modelName)throws Exception;

	//public void doSpringSecurityConfFiles(VelocityContext context,String hdLocation, MetaDataModel dataModel, String modelName)throws Exception ;
	
	public void doApplicationProperties(MetaDataModel dataModel, VelocityContext context, String hdLocation)throws Exception;
	
	//public void doBackingBeans(MetaData metaData, VelocityContext context, String hdLocation, MetaDataModel dataModel)throws Exception;
	
	/**
	 * Do Spring Security
	 * @param metaData
	 * @param context
	 * @param hdLocation
	 * @param dataModel
	 * @throws Exception
	 */
	//public void doAuthenticationProvider(VelocityContext context, String hdLocation,MetaDataModel dataModel, String modelName) throws Exception;

	void doDTOMapper(MetaData metaData, VelocityContext context, String hdLocation, MetaDataModel dataModel)throws Exception;

	void doRestControllers(MetaData metaData, VelocityContext context, String hdLocation, MetaDataModel dataModel)throws Exception;

	//void doMvcDispatcherServlet(MetaDataModel dataModel, VelocityContext context, String hdLocation) throws Exception;

	void doEntityGenerator(MetaData metaData, VelocityContext velocityContext, String hdLocation,MetaDataModel metaDataModel) throws Exception;
	
	
	
	

}
