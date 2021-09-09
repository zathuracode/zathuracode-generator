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

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zcode.eclipse.plugin.generator.utilities.EclipseGeneratorUtil;
import org.zcode.generator.model.IZathuraGenerator;
import org.zcode.generator.utilities.GeneratorUtil;
import org.zcode.generator.utilities.JalopyCodeFormatter;
import org.zcode.metadata.model.ManyToOneMember;
import org.zcode.metadata.model.Member;
import org.zcode.metadata.model.MetaData;
import org.zcode.metadata.model.MetaDataModel;
import org.zcode.metadata.model.OneToManyMember;
import org.zcode.metadata.model.OneToOneMember;
import org.zcode.metadata.model.SimpleMember;

/**
 * Zathuracode Generator www.zathuracode.org
 * 
 * @author Diego Armando Gomez (dgomez@vortexbird.com)
 * @version 1.0
 */
public class SkyJet implements IZathuraSkyJetTemplate, IZathuraGenerator {

	private static final Logger log = LoggerFactory.getLogger(SkyJet.class);

	private String paqueteVirgen;
	private VelocityEngine ve;
	private Properties properties;
	private String fullPathProject;

	private final static String mainFolder = "skyJet";

	private final static String templatesPath = GeneratorUtil.getGeneratorTemplatesPath() + GeneratorUtil.slash
			+ mainFolder + GeneratorUtil.slash;
	private final static String extPath = GeneratorUtil.getGeneratorExtPath() + GeneratorUtil.slash + mainFolder
			+ GeneratorUtil.slash;

	@Override
	public void toGenerate(MetaDataModel metaDataModel, String projectName, String folderProjectPath,
			Properties propiedades) throws Exception {

		Thread thread = Thread.currentThread();
		ClassLoader loader = thread.getContextClassLoader();
		thread.setContextClassLoader(EclipseGeneratorUtil.bundleClassLoader);
		log.info("Chaged ContextClassLoader:" + EclipseGeneratorUtil.bundleClassLoader);

		try {
			this.properties = propiedades;
			String nombrePaquete = propiedades.getProperty("jpaPckgName");
			Integer specificityLevel = (Integer) propiedades.get("specificityLevel");
			String domainName = nombrePaquete.substring(0, nombrePaquete.indexOf("."));
			this.fullPathProject = propiedades.getProperty("fullPathProject");

			log.info("===================== Begin SkyJet Zathuracode =====================");
			doTemplate(folderProjectPath, metaDataModel, nombrePaquete, projectName, specificityLevel, domainName);
			copyLibraries();
			log.info("=====================  End SkyJet Zathuracode  =====================");
		} catch (Exception e) {
			throw e;
		} finally {
			log.info("Chaged ContextClassLoader:" + loader);
			thread.setContextClassLoader(loader);
		}

	}

	/**
	 * Se usa para que copie los archivos completos que no son Velocity
	 */
	public void copyLibraries() {

		String log4j = extPath + GeneratorUtil.slash + "log4j" + GeneratorUtil.slash;
		String log4jDestination = properties.getProperty("mainResoruces");
		GeneratorUtil.copyFolder(log4j, log4jDestination);

	}

	@Override
	public void doTemplate(String hdLocation, MetaDataModel metaDataModel, String jpaPckgName, String projectName,
			Integer specificityLevel, String domainName) throws Exception {

		try {

			ve = new VelocityEngine();
			Properties propiedadesVelocity = new Properties();
			propiedadesVelocity.put("file.resource.loader.description", "Velocity File Resource Loader");
			propiedadesVelocity.put("file.resource.loader.class",
					"org.apache.velocity.runtime.resource.loader.FileResourceLoader");
			propiedadesVelocity.put("file.resource.loader.path", templatesPath);
			propiedadesVelocity.put("file.resource.loader.cache", "false");
			propiedadesVelocity.put("file.resource.loader.modificationCheckInterval", "2");
			ve.init(propiedadesVelocity);

			VelocityContext velocityContext = new VelocityContext();
			List<MetaData> listMetaData = metaDataModel.getTheMetaData();

			ISkyJetStringBuilderForId stringBuilderForId = new SkyJetStringBuilderForId(listMetaData);
			ISkyJetStringBuilder stringBuilder = new SkyJetStringBuilder(listMetaData,
					(SkyJetStringBuilderForId) stringBuilderForId);
			String packageOriginal = null;
			String virginPackage = null;
			String modelName = null;

			HashMap<String, String> primaryKeyByClass = new HashMap<String, String>();

			for (MetaData metaData : listMetaData) {
				primaryKeyByClass.put(metaData.getRealClassName().toLowerCase(),
						SkyJetUtilities.getInstance().camelCaseToUnderScore(metaData.getPrimaryKey().getName()));
			}

			try {
				packageOriginal = jpaPckgName;

				int lastIndexOf2 = packageOriginal.lastIndexOf(".") + 1;
				modelName = jpaPckgName.substring(lastIndexOf2);

				int virginLastIndexOf = packageOriginal.lastIndexOf(".");
				virginPackage = packageOriginal.substring(0, virginLastIndexOf);
			} catch (Exception e) {
				log.error(e.toString());
			}

			velocityContext.put("packageOriginal", packageOriginal);
			velocityContext.put("virginPackage", virginPackage);
			velocityContext.put("package", jpaPckgName);
			velocityContext.put("projectName", projectName);
			velocityContext.put("domainName", domainName);
			velocityContext.put("modelName", modelName);
			velocityContext.put("schema", EclipseGeneratorUtil.schema);

			// Variables para generar el persistence.xml
			velocityContext.put("connectionUrl", EclipseGeneratorUtil.connectionUrl);
			velocityContext.put("connectionDriverClass", EclipseGeneratorUtil.connectionDriverClass);
			velocityContext.put("connectionUsername", EclipseGeneratorUtil.connectionUsername);
			velocityContext.put("connectionPassword", EclipseGeneratorUtil.connectionPassword);

			this.paqueteVirgen = GeneratorUtil.replaceAll(virginPackage, ".", GeneratorUtil.slash);

			SkyJetUtilities.getInstance().buildFoldersJava(virginPackage, hdLocation, specificityLevel, packageOriginal,
					properties);
			SkyJetUtilities.getInstance().buildFoldersTest(virginPackage, hdLocation, specificityLevel, packageOriginal,
					properties);
			SkyJetUtilities.getInstance().biuldHashToGetIdValuesLengths(listMetaData);

			for (MetaData metaData : listMetaData) {

				log.info(metaData.getRealClassName());

				String var = metaData.getPrimaryKey().getName();
				velocityContext.put("var", var);
				log.info(metaData.getPrimaryKey().getName());

				velocityContext.put("databaseName",
						SkyJetUtilities.getInstance().camelCaseToUnderScore(metaData.getRealClassNameAsVariable()));
				velocityContext.put("primaryKeyByClass", primaryKeyByClass);
				velocityContext.put("composedKey", false);

				if (metaData.getPrimaryKey().isPrimiaryKeyAComposeKey()) {
					velocityContext.put("composedKey", true);
					velocityContext.put("finalParamForIdClass",
							stringBuilderForId.finalParamForIdClass(listMetaData, metaData));
				}

				if (metaData.isGetManyToOneProperties()) {
					velocityContext.put("getVariableForManyToOneProperties", stringBuilder
							.getVariableForManyToOneProperties(metaData.getManyToOneProperties(), listMetaData));
					velocityContext.put("getStringsForManyToOneProperties", stringBuilder
							.getStringsForManyToOneProperties(metaData.getManyToOneProperties(), listMetaData));
				}

				// generacion de nuevos dto
				velocityContext.put("variableDto", stringBuilder.getPropertiesDto(listMetaData, metaData));
				velocityContext.put("propertiesDto", SkyJetUtilities.getInstance().dtoProperties);
				velocityContext.put("memberDto", SkyJetUtilities.getInstance().nameMemberToDto);

				// generacion de la entidad
				List<SimpleMember> simpleMembers = new ArrayList<SimpleMember>();
				List<ManyToOneMember> manyToOneMembers = new ArrayList<ManyToOneMember>();
				List<OneToManyMember> oneToManyMembers = new ArrayList<OneToManyMember>();
				List<OneToOneMember> oneMembers = new ArrayList<OneToOneMember>();
				SimpleMember primaryKey = (SimpleMember) metaData.getPrimaryKey();

				String constructorStr = "";

				constructorStr = constructorStr + primaryKey.getType().getSimpleName() + " " + primaryKey.getShowName()
						+ ", ";

				for (Member member : metaData.getProperties()) {

					member.setDatabaseName(SkyJetUtilities.getInstance().camelCaseToUnderScore(member.getName()));

					if (member.isSimpleMember() && !member.getShowName().equals(primaryKey.getShowName())) {
						simpleMembers.add((SimpleMember) member);

						constructorStr = constructorStr + member.getType().getSimpleName() + " " + member.getShowName()
								+ ", ";
					}

					if (member.isOneToOneMember()) {
						oneMembers.add((OneToOneMember) member);

						constructorStr = constructorStr + member.getType().getSimpleName() + " " + member.getShowName()
								+ ", ";
					}

					if (member.isManyToOneMember()) {
						manyToOneMembers.add((ManyToOneMember) member);

						constructorStr = constructorStr + member.getType().getSimpleName() + " " + member.getShowName()
								+ ", ";
					}

					if (member.isOneToManyMember()) {
						oneToManyMembers.add((OneToManyMember) member);

						constructorStr = constructorStr + "List<" + member.getType().getSimpleName() + "> "
								+ member.getShowName() + ", ";
					}

				}

				constructorStr = constructorStr.substring(0, constructorStr.length() - 2);

				velocityContext.put("simpleMembers", simpleMembers);
				velocityContext.put("manyToOneMembers", manyToOneMembers);
				velocityContext.put("oneToManyMembers", oneToManyMembers);
				velocityContext.put("primaryKey", primaryKey);
				velocityContext.put("constructorStr", constructorStr);
				velocityContext.put("composeKeyAttributes",
						stringBuilderForId.attributesComposeKey(listMetaData, metaData));

				// generacion de atributos para mapear de Entidad a DTO
				// TODO Revisa la generacion de DTO
				velocityContext.put("dtoAttributes",
						stringBuilderForId.obtainDTOMembersAndSetEntityAttributes(listMetaData, metaData));
				velocityContext.put("dtoAttributes2",
						stringBuilder.obtainDTOMembersAndSetEntityAttributes2(listMetaData, metaData));

				// generacion de los atributos para mapear de DTO a Entidad
				velocityContext.put("entityAttributes",
						stringBuilderForId.obtainEntityMembersAndSetDTOAttributes(listMetaData, metaData));
				velocityContext.put("entityAttributes2",
						stringBuilder.obtainEntityMembersAndSetDTOAttributes2(listMetaData, metaData));

				velocityContext.put("getMappingsEntityToDTo",
						stringBuilder.getMappingsEntityToDTo(metaData.getManyToOneProperties(), listMetaData));
				velocityContext.put("getMappingsDTOToEntity",
						stringBuilder.getMappingsDTOToEntity(metaData.getManyToOneProperties(), listMetaData));

				// generacion de la nueva logica
				velocityContext.put("dtoConvert", stringBuilderForId.dtoConvert(listMetaData, metaData));
				velocityContext.put("dtoConvert2", stringBuilder.dtoConvert2(listMetaData, metaData));

				velocityContext.put("finalParamForIdVariablesAsList",
						stringBuilderForId.finalParamForIdVariablesAsList(listMetaData, metaData));
				velocityContext.put("finalParamForIdClassAsVariables",
						stringBuilderForId.finalParamForIdClassAsVariables(listMetaData, metaData));

				velocityContext.put("finalParamForViewVariablesInList",
						stringBuilder.finalParamForViewVariablesInList(listMetaData, metaData));
				velocityContext.put("isFinalParamForViewDatesInList",
						SkyJetUtilities.getInstance().isFinalParamForViewDatesInList());
				velocityContext.put("isFinalParamForIdForViewDatesInList",
						SkyJetUtilities.getInstance().isFinalParamForIdForViewDatesInList());

				velocityContext.put("finalParamForDtoUpdate",
						stringBuilder.finalParamForDtoUpdate(listMetaData, metaData));
				velocityContext.put("finalParamForIdForDtoForSetsVariablesInList",
						stringBuilderForId.finalParamForIdForDtoForSetsVariablesInList(listMetaData, metaData));
				velocityContext.put("finalParamForDtoForSetsVariablesInList",
						stringBuilder.finalParamForDtoForSetsVariablesInList(listMetaData, metaData));
				velocityContext.put("finalParamForIdForDtoForSetsVariablesInList",
						stringBuilderForId.finalParamForIdForDtoForSetsVariablesInList(listMetaData, metaData));
				velocityContext.put("finalParamForIdForViewVariablesInList",
						stringBuilderForId.finalParamForIdForViewVariablesInList(listMetaData, metaData));
				velocityContext.put("isFinalParamForIdForViewDatesInList",
						SkyJetUtilities.getInstance().isFinalParamForIdForViewDatesInList());
				velocityContext.put("finalParamForIdForViewClass",
						stringBuilderForId.finalParamForIdForViewClass(listMetaData, metaData));
				velocityContext.put("finalParamForViewForSetsVariablesInList",
						stringBuilder.finalParamForViewForSetsVariablesInList(listMetaData, metaData));
				velocityContext.put("finalParamForView", stringBuilder.finalParamForView(listMetaData, metaData));
				velocityContext.put("finalParamForIdClassAsVariablesAsString",
						stringBuilderForId.finalParamForIdClassAsVariablesAsString(listMetaData, metaData));
				velocityContext.put("finalParamForDtoUpdateOnlyVariables",
						stringBuilder.finalParamForDtoUpdateOnlyVariables(listMetaData, metaData));
				velocityContext.put("finalParamForIdForDtoInViewForSetsVariablesInList",
						stringBuilderForId.finalParamForIdForDtoInViewForSetsVariablesInList(listMetaData, metaData));
				velocityContext.put("finalParamForDtoInViewForSetsVariablesInList",
						stringBuilder.finalParamForDtoInViewForSetsVariablesInList(listMetaData, metaData));
				velocityContext.put("finalParamForIdForViewDatesInList", SkyJetUtilities.getInstance().datesId);
				velocityContext.put("finalParamForViewDatesInList", SkyJetUtilities.getInstance().dates);
				velocityContext.put("isFinalParamForIdClassAsVariablesForDates",
						SkyJetUtilities.getInstance().isFinalParamForIdClassAsVariablesForDates());
				velocityContext.put("finalParamForIdClassAsVariablesForDates",
						SkyJetUtilities.getInstance().datesIdJSP);
				velocityContext.put("finalParamVariablesAsList",
						stringBuilder.finalParamVariablesAsList(listMetaData, metaData));
				velocityContext.put("isFinalParamDatesAsList", SkyJetUtilities.getInstance().isFinalParamDatesAsList());
				velocityContext.put("finalParamDatesAsList", SkyJetUtilities.getInstance().datesJSP);
				velocityContext.put("finalParamForIdClassAsVariables2",
						stringBuilderForId.finalParamForIdClassAsVariables2(listMetaData, metaData));
				velocityContext.put("finalParamForVariablesDataTablesForIdAsList",
						stringBuilderForId.finalParamForVariablesDataTablesForIdAsList(listMetaData, metaData));
				velocityContext.put("finalParamVariablesAsList2",
						stringBuilder.finalParamVariablesAsList2(listMetaData, metaData));
				velocityContext.put("finalParamForVariablesDataTablesAsList",
						stringBuilder.finalParamForVariablesDataTablesAsList(listMetaData, metaData));
				velocityContext.put("finalParamForIdForViewForSetsVariablesInList",
						stringBuilderForId.finalParamForIdForViewForSetsVariablesInList(listMetaData, metaData));
				velocityContext.put("finalParamVariablesDatesAsList2",
						stringBuilder.finalParamVariablesDatesAsList2(listMetaData, metaData));

				// listas nuevas para el manejo de tablas maestro detalle AndresPuerta
				velocityContext.put("finalParamForGetIdForViewClass",
						stringBuilder.finalParamForGetIdForViewClass(listMetaData, metaData));
				velocityContext.put("finalParamForGetIdByDtoForViewClass",
						stringBuilder.finalParamForGetIdByDtoForViewClass(listMetaData, metaData));
				velocityContext.put("finalParamForIdForViewForSetsVariablesDtoInList",
						stringBuilderForId.finalParamForIdForViewForSetsVariablesDtoInList(listMetaData, metaData));
				velocityContext.put("finalParamForViewForSetsVariablesDtoInList",
						stringBuilder.finalParamForViewForSetsVariablesDtoInList(listMetaData, metaData));
				velocityContext.put("finalParamForGetManyToOneForViewClass",
						stringBuilder.finalParamForGetManyToOneForViewClass(listMetaData, metaData));

				velocityContext.put("dataModel", metaDataModel);
				velocityContext.put("metaData", metaData);

				String finalParam = stringBuilder.finalParam(listMetaData, metaData);
				velocityContext.put("finalParam", finalParam);
				metaData.setFinamParam(finalParam);

				String finalParamForId = stringBuilderForId.finalParamForId(listMetaData, metaData);
				velocityContext.put("finalParamForId", finalParamForId);
				metaData.setFinalParamForId(finalParamForId);

				String finalParamVariables = stringBuilder.finalParamVariables(listMetaData, metaData);
				velocityContext.put("finalParamVariables", finalParamVariables);
				metaData.setFinamParamVariables(finalParamVariables);

				String finalParamForIdVariables = stringBuilderForId.finalParamForIdVariables(listMetaData, metaData);
				velocityContext.put("finalParamForIdVariables", finalParamForIdVariables);
				metaData.setFinalParamForIdVariables(finalParamForIdVariables);

				// generacion datasource.xml
				velocityContext.put("connectionUrl", EclipseGeneratorUtil.connectionUrl);
				velocityContext.put("connectionDriverClass", EclipseGeneratorUtil.connectionDriverClass);
				velocityContext.put("connectionUsername", EclipseGeneratorUtil.connectionUsername);
				velocityContext.put("connectionPassword", EclipseGeneratorUtil.connectionPassword);

				doEntityGenerator(metaData, velocityContext, hdLocation, metaDataModel);
				doRepository(metaData, velocityContext, hdLocation);

				doService(metaData, velocityContext, hdLocation, metaDataModel, modelName);
				doDto(metaData, velocityContext, hdLocation, metaDataModel, modelName);
				doDTOMapper(metaData, velocityContext, hdLocation, metaDataModel);
				doRestControllers(metaData, velocityContext, hdLocation, metaDataModel);

				doServiceTest(metaData, velocityContext, hdLocation, metaDataModel, modelName);

			}

			if (EclipseGeneratorUtil.isMavenProject) {
				GeneratorUtil.doPomXml(velocityContext, ve);
			}

			doExceptions(velocityContext, hdLocation);
			doGenericService(velocityContext, hdLocation, metaDataModel, modelName);
			doDocker(velocityContext, hdLocation, metaDataModel, modelName);
			doJWTSecurity(velocityContext, hdLocation, metaDataModel, modelName);
			doUtilites(velocityContext, hdLocation, metaDataModel, modelName);
			doGeneralExceptionHandler(velocityContext, hdLocation, metaDataModel, modelName);
			doSpringBootRunner(velocityContext, hdLocation, metaDataModel, modelName);
			doSwaggerConfig(velocityContext, hdLocation, metaDataModel, modelName);
			doApplicationProperties(metaDataModel, velocityContext, hdLocation);
			doORMXML(metaDataModel, velocityContext, hdLocation);

			String restPath = paqueteVirgen + GeneratorUtil.slash + "controller";
			restPath = restPath.replace(GeneratorUtil.slash, ".");
			velocityContext.put("restPackage", restPath);

		} catch (Exception e) {
			log.error(e.toString());
			throw e;
		} finally {

		}

	}

	@Override
	public void doRepository(MetaData metaData, VelocityContext context, String hdLocation) throws Exception {
		try {

			String path = hdLocation + paqueteVirgen + GeneratorUtil.slash + "repository" + GeneratorUtil.slash;
			log.info("Begin Interface EntityRepository");
			Template templateIDao = ve.getTemplate("EntityRepository.vm");
			StringWriter swIdao = new StringWriter();
			templateIDao.merge(context, swIdao);
			FileWriter fwIdao = new FileWriter(path + metaData.getRealClassName() + "Repository.java");
			BufferedWriter bwIdao = new BufferedWriter(fwIdao);
			bwIdao.write(swIdao.toString());
			bwIdao.close();
			fwIdao.close();
			log.info("End Interface EntityRepository");

			JalopyCodeFormatter.formatJavaCodeFile(path + metaData.getRealClassName() + "Repository.java");

		} catch (Exception e) {
			log.error(e.toString());
			throw e;
		}

	}

	@Override
	public void doService(MetaData metaData, VelocityContext context, String hdLocation, MetaDataModel dataModel,
			String modelName) throws Exception {
		try {
			String path = hdLocation + paqueteVirgen + GeneratorUtil.slash + "entity" + GeneratorUtil.slash + "service"
					+ GeneratorUtil.slash;

			log.info("Begin Interface Service");
			Template templateIlogic = ve.getTemplate("Service.vm");
			StringWriter swIlogic = new StringWriter();
			templateIlogic.merge(context, swIlogic);

			FileWriter fwIlogic = new FileWriter(path + metaData.getRealClassName() + "Service.java");
			BufferedWriter bwIlogic = new BufferedWriter(fwIlogic);
			bwIlogic.write(swIlogic.toString());
			bwIlogic.close();
			fwIlogic.close();
			log.info("End Interface Service");

			log.info("Begin Class ServiceImpl");
			Template templateLogic = ve.getTemplate("ServiceImpl.vm");
			StringWriter swLogic = new StringWriter();
			templateLogic.merge(context, swLogic);
			FileWriter fwLogic = new FileWriter(path + metaData.getRealClassName() + "ServiceImpl.java");
			BufferedWriter bwLogic = new BufferedWriter(fwLogic);
			bwLogic.write(swLogic.toString());
			bwLogic.close();
			fwLogic.close();
			log.info("End Class ServiceImpl");

			JalopyCodeFormatter.formatJavaCodeFile(path + metaData.getRealClassName() + "Service.java");
			JalopyCodeFormatter.formatJavaCodeFile(path + metaData.getRealClassName() + "ServiceImpl.java");

		} catch (Exception e) {
			log.error(e.toString());
			throw e;
		}
	}

	@Override
	public void doServiceTest(MetaData metaData, VelocityContext context, String hdLocation, MetaDataModel dataModel,
			String modelName) throws Exception {
		try {
			String path = properties.getProperty("testJava") + paqueteVirgen + GeneratorUtil.slash + "service"
					+ GeneratorUtil.slash;

			log.info("Begin Interface Service");
			Template templateIlogic = ve.getTemplate("ServiceTest.vm");
			StringWriter swIlogic = new StringWriter();
			templateIlogic.merge(context, swIlogic);

			FileWriter fwIlogic = new FileWriter(path + metaData.getRealClassName() + "ServiceTest.java");
			BufferedWriter bwIlogic = new BufferedWriter(fwIlogic);
			bwIlogic.write(swIlogic.toString());
			bwIlogic.close();
			fwIlogic.close();
			log.info("End Interface Service");

			JalopyCodeFormatter.formatJavaCodeFile(path + metaData.getRealClassName() + "ServiceTest.java");

		} catch (Exception e) {
			log.error(e.toString());
			throw e;
		}
	}

	@Override
	public void doDto(MetaData metaData, VelocityContext context, String hdLocation, MetaDataModel dataModel,
			String modelName) throws Exception {
		try {

			Template dtoTemplate = ve.getTemplate("Dto.vm");
			StringWriter swDto = new StringWriter();
			dtoTemplate.merge(context, swDto);

			String path = hdLocation + paqueteVirgen + GeneratorUtil.slash + "dto" + GeneratorUtil.slash;
			FileWriter fwDto = new FileWriter(path + metaData.getRealClassName() + "DTO.java");
			BufferedWriter bwDto = new BufferedWriter(fwDto);
			bwDto.write(swDto.toString());
			bwDto.close();
			swDto.close();
			fwDto.close();
			JalopyCodeFormatter.formatJavaCodeFile(path + metaData.getRealClassName() + "DTO.java");

		} catch (Exception e) {
			log.error(e.toString());
			throw e;
		}

	}

	@Override
	public void doExceptions(VelocityContext context, String hdLocation) throws Exception {
		try {

			String path = hdLocation + paqueteVirgen + GeneratorUtil.slash + "exception" + GeneratorUtil.slash;
			
			log.info("Begin Exception");
			
			Template zMessException = ve.getTemplate("ZMessManager.vm");
			StringWriter swZMessException = new StringWriter();
			zMessException.merge(context, swZMessException);
			
			FileWriter fwZMessException = new FileWriter(path + "ZMessManager.java");
			BufferedWriter bwZMessException = new BufferedWriter(fwZMessException);
			bwZMessException.write(swZMessException.toString());
			bwZMessException.close();
			fwZMessException.close();
			
			Template vbException = ve.getTemplate("VortexbirdException.vm");
			StringWriter swVbException = new StringWriter();
			vbException.merge(context, swVbException);
			
			FileWriter fwVbException = new FileWriter(path + "VortexbirdException.java");
			BufferedWriter bwVbException = new BufferedWriter(fwVbException);
			bwVbException.write(swVbException.toString());
			bwVbException.close();
			fwVbException.close();
			
			Template systemException = ve.getTemplate("SystemException.vm");
			StringWriter swSystemException = new StringWriter();
			systemException.merge(context, swSystemException);
			
			FileWriter fwSystemException = new FileWriter(path + "SystemException.java");
			BufferedWriter bwSystemException = new BufferedWriter(fwSystemException);
			bwSystemException.write(swSystemException.toString());
			bwSystemException.close();
			fwSystemException.close();
			
			Template configException = ve.getTemplate("ConfigException.vm");
			StringWriter swConfigException = new StringWriter();
			configException.merge(context, swConfigException);
			
			FileWriter fwConfigException = new FileWriter(path + "ConfigException.java");
			BufferedWriter bwConfigException = new BufferedWriter(fwConfigException);
			bwConfigException.write(swConfigException.toString());
			bwConfigException.close();
			fwConfigException.close();
			
			Template userException = ve.getTemplate("UserException.vm");
			StringWriter swUserException = new StringWriter();
			userException.merge(context, swUserException);
			
			FileWriter fwUserException = new FileWriter(path + "UserException.java");
			BufferedWriter bwUserException = new BufferedWriter(fwUserException);
			bwUserException.write(swUserException.toString());
			bwUserException.close();
			fwUserException.close();
			
			log.info("End Exception");
			
			JalopyCodeFormatter.formatJavaCodeFile(path + "ZMessManager.java");
			JalopyCodeFormatter.formatJavaCodeFile(path + "VortexbirdException.java");
			JalopyCodeFormatter.formatJavaCodeFile(path + "SystemException.java");
			JalopyCodeFormatter.formatJavaCodeFile(path + "ConfigException.java");
			JalopyCodeFormatter.formatJavaCodeFile(path + "UserException.java");

		} catch (Exception e) {
			log.error(e.toString());
			throw e;
		}
		
	}
	
	@Override
	public void doUtilites(VelocityContext context, String hdLocation, MetaDataModel dataModel, String modelName)
			throws Exception {

		try {

			String path = hdLocation + paqueteVirgen + GeneratorUtil.slash + "utility" + GeneratorUtil.slash;
			log.info("Begin Utilities");
			Template templateUtilities = ve.getTemplate("Utilities.vm");
			StringWriter swUtilities = new StringWriter();
			templateUtilities.merge(context, swUtilities);
			FileWriter fwUtilities = new FileWriter(path + "Utilities.java");
			BufferedWriter bwUtilities = new BufferedWriter(fwUtilities);
			bwUtilities.write(swUtilities.toString());
			bwUtilities.close();
			fwUtilities.close();
			log.info("Begin Utilities");

		} catch (Exception e) {
			log.error(e.toString());
			throw e;
		}

	}

	@Override
	public void doApplicationProperties(MetaDataModel dataModel, VelocityContext context, String hdLocation)
			throws Exception {

		try {
			log.info("Begin application.properties.vm");
			
			String path = properties.getProperty("mainResoruces");
			
			Template templateApplication = ve.getTemplate("application.properties.vm");
			StringWriter swApplication = new StringWriter();
			templateApplication.merge(context, swApplication);
			FileWriter fwApplication = new FileWriter(path + GeneratorUtil.slash + "application.properties");
			BufferedWriter bwApplication = new BufferedWriter(fwApplication);
			bwApplication.write(swApplication.toString());
			bwApplication.close();
			fwApplication.close();
			
			Template templateApplicationDev = ve.getTemplate("application-dev.properties.vm");
			StringWriter swApplicationDev = new StringWriter();
			templateApplicationDev.merge(context, swApplicationDev);
			FileWriter fwApplicationDev = new FileWriter(path + GeneratorUtil.slash + "application-dev.properties");
			BufferedWriter bwApplicationDev = new BufferedWriter(fwApplicationDev);
			bwApplicationDev.write(swApplicationDev.toString());
			bwApplicationDev.close();
			fwApplicationDev.close();
			
			log.info("End application.properties.vm");

		} catch (Exception e) {
			log.error(e.toString());
			throw e;
		}

	}

	@Override
	public void doORMXML(MetaDataModel dataModel, VelocityContext context, String hdLocation) throws Exception {

		try {

			log.info("Begin orm.xml.vm");
			String path = properties.getProperty("mainResoruces");
			Template templatePersistence = ve.getTemplate("orm.xml.vm");
			StringWriter swPersistence = new StringWriter();
			templatePersistence.merge(context, swPersistence);
			FileWriter fwPersistence = new FileWriter(
					path + GeneratorUtil.slash + "META-INF" + GeneratorUtil.slash + "orm.xml");
			BufferedWriter bwPersistence = new BufferedWriter(fwPersistence);
			bwPersistence.write(swPersistence.toString());
			bwPersistence.close();
			fwPersistence.close();
			log.info("End orm.xml.vm");

		} catch (Exception e) {
			log.error(e.toString());
			throw e;
		}

	}

	@Override
	public void doDTOMapper(MetaData metaData, VelocityContext context, String hdLocation, MetaDataModel dataModel)
			throws Exception {
		try {

			String path = hdLocation + paqueteVirgen + GeneratorUtil.slash + "mapper" + GeneratorUtil.slash;

			log.info("Begin Interface DTO Mapper");
			Template templateIMapperDTO = ve.getTemplate("DTOMapper.vm");
			StringWriter swIMapperDTO = new StringWriter();
			templateIMapperDTO.merge(context, swIMapperDTO);

			FileWriter fwIMapperDTO = new FileWriter(path + metaData.getRealClassName() + "Mapper.java");
			BufferedWriter bwIMapperDTO = new BufferedWriter(fwIMapperDTO);
			bwIMapperDTO.write(swIMapperDTO.toString());
			bwIMapperDTO.close();
			fwIMapperDTO.close();

			JalopyCodeFormatter.formatJavaCodeFile(path + metaData.getRealClassName() + "Mapper.java");

		} catch (Exception e) {
			log.error(e.toString());
			throw e;
		}

	}

	@Override
	public void doGeneralExceptionHandler(VelocityContext context, String hdLocation, MetaDataModel dataModel,
			String modelName) throws Exception {

		try {

			String path = hdLocation + paqueteVirgen + GeneratorUtil.slash + "entity" + GeneratorUtil.slash
					+ "controller" + GeneratorUtil.slash;

			log.info("Begin GeneralExceptionHandler");
			Template templateUtilities = ve.getTemplate("GeneralExceptionHandler.vm");
			StringWriter swUtilities = new StringWriter();
			templateUtilities.merge(context, swUtilities);
			FileWriter fwUtilities = new FileWriter(path + "GeneralExceptionHandler.java");
			BufferedWriter bwUtilities = new BufferedWriter(fwUtilities);
			bwUtilities.write(swUtilities.toString());
			bwUtilities.close();
			fwUtilities.close();
			log.info("Begin GeneralExceptionHandler");

		} catch (Exception e) {
			log.error(e.toString());
			throw e;
		}

	}

	@Override
	public void doRestControllers(MetaData metaData, VelocityContext context, String hdLocation,
			MetaDataModel dataModel) throws Exception {
		try {

			String path = hdLocation + paqueteVirgen + GeneratorUtil.slash + "entity" + GeneratorUtil.slash
					+ "controller" + GeneratorUtil.slash;

			log.info("Begin RestControllers");
			Template templateBakcEndBean = ve.getTemplate("RestController.vm");
			StringWriter swBackEndBean = new StringWriter();
			templateBakcEndBean.merge(context, swBackEndBean);

			FileWriter fwBackEndBean = new FileWriter(path + metaData.getRealClassName() + "RestController.java");
			BufferedWriter bwBackEndBean = new BufferedWriter(fwBackEndBean);
			bwBackEndBean.write(swBackEndBean.toString());
			bwBackEndBean.close();
			fwBackEndBean.close();
			log.info("Begin RestControllers 2");
			JalopyCodeFormatter.formatJavaCodeFile(path + metaData.getRealClassName() + "RestController.java");
			// JenderUtilities.getInstance().dates = null;
			// JenderUtilities.getInstance().datesId = null;

		} catch (Exception e) {
			log.error(e.toString());
			throw e;
		}

	}

	@Override
	public void doEntityGenerator(MetaData metaData, VelocityContext velocityContext, String hdLocation,
			MetaDataModel metaDataModel) throws Exception {

		try {
			String path = hdLocation + metaData.getMainClass().toString().substring(6,
					metaData.getMainClass().toString().lastIndexOf(".")) + GeneratorUtil.slash;

			path = path.replace(".", GeneratorUtil.slash);

			log.info("Begin Entity Generator");

			Template templateEntity = ve.getTemplate("EntityGenerator.vm");
			StringWriter swEntity = new StringWriter();
			templateEntity.merge(velocityContext, swEntity);

			FileWriter fwEntity = new FileWriter(path + metaData.getRealClassName() + ".java");
			BufferedWriter bwEntity = new BufferedWriter(fwEntity);
			bwEntity.write(swEntity.toString());
			bwEntity.close();
			fwEntity.close();

			if (metaData.getComposeKey() != null) {
				Template templateEntityComposeKey = ve.getTemplate("EntityIdGenerator.vm");
				StringWriter swEntityComposeKey = new StringWriter();
				templateEntityComposeKey.merge(velocityContext, swEntityComposeKey);

				FileWriter fwEntityComposeKey = new FileWriter(path + metaData.getRealClassName() + "Id" + ".java");
				BufferedWriter bwEntityComposeKey = new BufferedWriter(fwEntityComposeKey);
				bwEntityComposeKey.write(swEntityComposeKey.toString());
				bwEntityComposeKey.close();
				fwEntityComposeKey.close();

				JalopyCodeFormatter.formatJavaCodeFile(path + metaData.getRealClassName() + "Id" + ".java");
			}

			JalopyCodeFormatter.formatJavaCodeFile(path + metaData.getRealClassName() + ".java");
		} catch (Exception e) {
			log.error(e.toString());
			throw e;
		}
	}

	@Override
	public void doSpringBootRunner(VelocityContext context, String hdLocation, MetaDataModel dataModel,
			String modelName) throws Exception {

		try {
			String path = hdLocation + paqueteVirgen + GeneratorUtil.slash;

			log.info("Begin SpringBootRunner");
			Template templateIlogic = ve.getTemplate("SpringBootRunner.vm");
			StringWriter swIlogic = new StringWriter();
			templateIlogic.merge(context, swIlogic);

			FileWriter fwIlogic = new FileWriter(path + "SpringBootRunner.java");
			BufferedWriter bwIlogic = new BufferedWriter(fwIlogic);
			bwIlogic.write(swIlogic.toString());
			bwIlogic.close();
			fwIlogic.close();
			log.info("End SpringBootRunner");

			JalopyCodeFormatter.formatJavaCodeFile(path + "SpringBootRunner.java");
		} catch (Exception e) {
			log.error(e.toString());
			throw e;
		}

	}

	@Override
	public void doSwaggerConfig(VelocityContext context, String hdLocation, MetaDataModel dataModel, String modelName)
			throws Exception {

		try {
			String path = hdLocation + paqueteVirgen + GeneratorUtil.slash;

			log.info("Begin SwaggerConfig");
			Template templateIlogic = ve.getTemplate("SwaggerConfig.vm");
			StringWriter swIlogic = new StringWriter();
			templateIlogic.merge(context, swIlogic);

			FileWriter fwIlogic = new FileWriter(path + "SwaggerConfig.java");
			BufferedWriter bwIlogic = new BufferedWriter(fwIlogic);
			bwIlogic.write(swIlogic.toString());
			bwIlogic.close();
			fwIlogic.close();
			log.info("End SwaggerConfig");

			JalopyCodeFormatter.formatJavaCodeFile(path + "SwaggerConfig.java");
		} catch (Exception e) {
			log.error(e.toString());
			throw e;
		}

	}

	@Override
	public void doGenericService(VelocityContext context, String hdLocation, MetaDataModel dataModel, String modelName)
			throws Exception {
		try {
			String path = hdLocation + paqueteVirgen + GeneratorUtil.slash + "entity" + GeneratorUtil.slash + "service"
					+ GeneratorUtil.slash;

			log.info("Begin Interface GenericService");
			Template templateIlogic = ve.getTemplate("GenericService.vm");
			StringWriter swIlogic = new StringWriter();
			templateIlogic.merge(context, swIlogic);

			FileWriter fwIlogic = new FileWriter(path + "GenericService.java");
			BufferedWriter bwIlogic = new BufferedWriter(fwIlogic);
			bwIlogic.write(swIlogic.toString());
			bwIlogic.close();
			fwIlogic.close();
			log.info("End Interface GenericService");

		} catch (Exception e) {
			log.error(e.toString());
			throw e;
		}
	}

	@Override
	public void doDocker(VelocityContext context, String hdLocation, MetaDataModel dataModel, String modelName)
			throws Exception {
		try {
			String path = this.fullPathProject + GeneratorUtil.slash;

			log.info("Begin Dockerfile");
			Template templateIlogic = ve.getTemplate("Dockerfile.vm");
			StringWriter swIlogic = new StringWriter();
			templateIlogic.merge(context, swIlogic);

			FileWriter fwIlogic = new FileWriter(path + "Dockerfile");
			BufferedWriter bwIlogic = new BufferedWriter(fwIlogic);
			bwIlogic.write(swIlogic.toString());
			bwIlogic.close();
			fwIlogic.close();
			log.info("Begin Dockerfile");

		} catch (Exception e) {
			log.error(e.toString());
			throw e;
		}

	}

	@Override
	public void doJWTSecurity(VelocityContext context, String hdLocation, MetaDataModel dataModel, String modelName)
			throws Exception {
		try {
			String path = hdLocation + paqueteVirgen + GeneratorUtil.slash + "security" + GeneratorUtil.slash;
			String pathService = hdLocation + paqueteVirgen + GeneratorUtil.slash + "entity" + GeneratorUtil.slash
					+ "service" + GeneratorUtil.slash;
			String pathDomain = hdLocation + paqueteVirgen + GeneratorUtil.slash + "domain" + GeneratorUtil.slash;

			log.info("Begin Constants");
			Template templateIlogic = ve.getTemplate("Constants.vm");
			StringWriter swIlogic = new StringWriter();
			templateIlogic.merge(context, swIlogic);

			FileWriter fwIlogic = new FileWriter(path + "Constants.java");
			BufferedWriter bwIlogic = new BufferedWriter(fwIlogic);
			bwIlogic.write(swIlogic.toString());
			bwIlogic.close();
			fwIlogic.close();
			log.info("End Constants");

			log.info("Begin JWTAuthenticationFilter");
			templateIlogic = ve.getTemplate("JWTAuthenticationFilter.vm");
			swIlogic = new StringWriter();
			templateIlogic.merge(context, swIlogic);

			fwIlogic = new FileWriter(path + "JWTAuthenticationFilter.java");
			bwIlogic = new BufferedWriter(fwIlogic);
			bwIlogic.write(swIlogic.toString());
			bwIlogic.close();
			fwIlogic.close();
			log.info("End JWTAuthenticationFilter");

			log.info("Begin JWTAuthorizationFilter");
			templateIlogic = ve.getTemplate("JWTAuthorizationFilter.vm");
			swIlogic = new StringWriter();
			templateIlogic.merge(context, swIlogic);

			fwIlogic = new FileWriter(path + "JWTAuthorizationFilter.java");
			bwIlogic = new BufferedWriter(fwIlogic);
			bwIlogic.write(swIlogic.toString());
			bwIlogic.close();
			fwIlogic.close();
			log.info("End JWTAuthorizationFilter");

			log.info("Begin WebSecurity");
			templateIlogic = ve.getTemplate("WebSecurity.vm");
			swIlogic = new StringWriter();
			templateIlogic.merge(context, swIlogic);

			fwIlogic = new FileWriter(path + "WebSecurity.java");
			bwIlogic = new BufferedWriter(fwIlogic);
			bwIlogic.write(swIlogic.toString());
			bwIlogic.close();
			fwIlogic.close();
			log.info("End WebSecurity");

			log.info("Begin UserApplicationDetailsServiceImpl");
			templateIlogic = ve.getTemplate("UserApplicationDetailsServiceImpl.vm");
			swIlogic = new StringWriter();
			templateIlogic.merge(context, swIlogic);

			fwIlogic = new FileWriter(pathService + "UserApplicationDetailsServiceImpl.java");
			bwIlogic = new BufferedWriter(fwIlogic);
			bwIlogic.write(swIlogic.toString());
			bwIlogic.close();
			fwIlogic.close();
			log.info("End UserApplicationDetailsServiceImpl");

			log.info("Begin UserApplication");
			templateIlogic = ve.getTemplate("UserApplication.vm");
			swIlogic = new StringWriter();
			templateIlogic.merge(context, swIlogic);

			fwIlogic = new FileWriter(pathDomain + "UserApplication.java");
			bwIlogic = new BufferedWriter(fwIlogic);
			bwIlogic.write(swIlogic.toString());
			bwIlogic.close();
			fwIlogic.close();
			log.info("End UserApplication");

		} catch (Exception e) {
			log.error(e.toString());
			throw e;
		}
	}

}
