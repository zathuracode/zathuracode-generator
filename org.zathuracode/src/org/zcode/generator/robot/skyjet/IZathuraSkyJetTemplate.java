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

	public void doTemplate(String hdLocation, MetaDataModel metaDataModel, String jpaPckgName, String projectName, Integer specificityLevel, String domainName)throws Exception;

	public void doRepository(MetaData metaData, VelocityContext context, String hdLocation)throws Exception;
	
	public void doGenericService( VelocityContext context, String hdLocation, MetaDataModel dataModel, String modelName)throws Exception;

	public void doService(MetaData metaData, VelocityContext context, String hdLocation, MetaDataModel dataModel, String modelName)throws Exception;
	
	public void doSpringBootRunner( VelocityContext context, String hdLocation, MetaDataModel dataModel, String modelName)throws Exception;
	
	public void doSwaggerConfig( VelocityContext context, String hdLocation, MetaDataModel dataModel, String modelName)throws Exception;

	public void doDto(MetaData metaData, VelocityContext context, String hdLocation, MetaDataModel dataModel, String modelName)throws Exception;

	public void doExceptions(VelocityContext context, String hdLocation)throws Exception;
	
	public void doUtilites(VelocityContext context, String hdLocation, MetaDataModel dataModel, String modelName)throws Exception;
	
	public void doGeneralExceptionHandler(VelocityContext context, String hdLocation,MetaDataModel dataModel, String modelName)throws Exception;
		
	public void doApplicationProperties(MetaDataModel dataModel, VelocityContext context, String hdLocation)throws Exception;
	
	void doDTOMapper(MetaData metaData, VelocityContext context, String hdLocation, MetaDataModel dataModel)throws Exception;

	void doRestControllers(MetaData metaData, VelocityContext context, String hdLocation, MetaDataModel dataModel)throws Exception;

	void doEntityGenerator(MetaData metaData, VelocityContext velocityContext, String hdLocation,MetaDataModel metaDataModel) throws Exception;
	
	public void doServiceTest(MetaData metaData,VelocityContext context, String hdLocation,MetaDataModel dataModel, String modelName)throws Exception;
	
	public void doDocker( VelocityContext context, String hdLocation, MetaDataModel dataModel, String modelName)throws Exception;

}
