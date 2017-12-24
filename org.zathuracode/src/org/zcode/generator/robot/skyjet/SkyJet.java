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
import org.zcode.generator.robot.jender.JenderUtilities;
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
 * Zathuracode Generator
 * www.zathuracode.org
 * @author Diego Armando Gomez (dgomez@vortexbird.com)
 * @version 1.0
 */
public class SkyJet implements IZathuraSkyJetTemplate,IZathuraGenerator {

	private static final Logger log = LoggerFactory.getLogger(SkyJet.class);
	//private static String pathTemplates;
	private String paqueteVirgen;
	private VelocityEngine ve;
	private Properties properties;
	private String webRootPath;

	private final static String mainFolder="skyJet";


	/// final static String pathTemplates;
	private final static String templatesPath=GeneratorUtil.getGeneratorTemplatesPath()+GeneratorUtil.slash+mainFolder+GeneratorUtil.slash;
	private final static String librariesPath=GeneratorUtil.getGeneratorLibrariesPath()+GeneratorUtil.slash;
	private final static String extPath	  =GeneratorUtil.getGeneratorExtPath()+         GeneratorUtil.slash+mainFolder+GeneratorUtil.slash;




	@Override
	public void toGenerate(MetaDataModel metaDataModel, String projectName,
			String folderProjectPath, Properties propiedades)throws Exception {

		Thread thread = Thread.currentThread();
		ClassLoader loader = thread.getContextClassLoader();
		thread.setContextClassLoader(EclipseGeneratorUtil.bundleClassLoader);
		log.info("Chaged ContextClassLoader:"+EclipseGeneratorUtil.bundleClassLoader);

		try {
			webRootPath=(propiedades.getProperty("webRootFolderPath"));					
			properties=propiedades;
			String nombrePaquete= propiedades.getProperty("jpaPckgName");
			Integer specificityLevel = (Integer) propiedades.get("specificityLevel");
			String  domainName= nombrePaquete.substring(0, nombrePaquete.indexOf("."));

			log.info("===================== Begin SkyJet Zathuracode =====================");
			doTemplate(folderProjectPath, metaDataModel, nombrePaquete, projectName, specificityLevel, domainName);
			copyLibraries();
			log.info("=====================  End SkyJet Zathuracode  =====================");
		} catch (Exception e) {
			throw e;
		}finally{
			log.info("Chaged ContextClassLoader:"+loader);
			thread.setContextClassLoader(loader);
		}

	}


	public void copyLibraries(){		

		String pathWebXml= extPath+"WEB-INF"+GeneratorUtil.slash;
	
		String pathHibernate= librariesPath+"core-hibernate"+GeneratorUtil.slash;
		String pathJPA=librariesPath+"jpa-hibernate"+GeneratorUtil.slash;
		String pathHibernateJpa = librariesPath+"hibernate-jpa"+GeneratorUtil.slash;

		String pathPrimeFaces= librariesPath+"primeFaces"+GeneratorUtil.slash;

		String pathSpring= librariesPath+"spring"+GeneratorUtil.slash;
		String pathSpringSecurity= librariesPath+"spring-security"+GeneratorUtil.slash;


		String pathSL4J= librariesPath+"slf4j"+GeneratorUtil.slash;
		String pathJamon= librariesPath+"jamon"+GeneratorUtil.slash;
		String pathMojarra= librariesPath+"mojarra"+GeneratorUtil.slash;
		String pathApacheCommons= librariesPath+"apache-commons"+GeneratorUtil.slash;
		String pathAopAlliance= librariesPath+"aopalliance"+GeneratorUtil.slash;
		String pathLog4j=librariesPath+"log4j"+GeneratorUtil.slash;
		//String pathServlet=librariesPath+"servlet3.1.1"+GeneratorUtil.slash;

		String pathLib= properties.getProperty("libFolderPath");

		String log4j = extPath+ GeneratorUtil.slash + "log4j"+ GeneratorUtil.slash;

		if (EclipseGeneratorUtil.isFrontend) {
			
			String pathCss = extPath + GeneratorUtil.slash + "css"+ GeneratorUtil.slash;
			String pathJs = extPath + GeneratorUtil.slash + "js"+ GeneratorUtil.slash;
			String pathBootstrap = extPath + GeneratorUtil.slash + "bootstrap"+ GeneratorUtil.slash;
			String generatorExtZathuraJavaEEWebSpringPrimeJpaCentricImages = extPath + GeneratorUtil.slash + "images"	+ GeneratorUtil.slash;
			String pathIndexJsp = extPath+"index.jsp";
			String pathLogin = extPath+"login.xhtml";
			
			// Copy Css
			GeneratorUtil.createFolder(webRootPath + "css");
			GeneratorUtil.copyFolder(pathCss, webRootPath + "css" + GeneratorUtil.slash);		
			
			// Copy JS
			GeneratorUtil.createFolder(webRootPath + "js");
			GeneratorUtil.copyFolder(pathJs, webRootPath + "js" + GeneratorUtil.slash);
			
			// Copy BOOTSRAP
			GeneratorUtil.createFolder(webRootPath + "bootstrap");
			GeneratorUtil.copyFolder(pathBootstrap, webRootPath + "bootstrap" + GeneratorUtil.slash);

			//create folder images and insert .png
			GeneratorUtil.createFolder(webRootPath + "images");
			GeneratorUtil.copyFolder(generatorExtZathuraJavaEEWebSpringPrimeJpaCentricImages, webRootPath + "images" + GeneratorUtil.slash);

			// create login.xhtml
			GeneratorUtil.copy(pathLogin,webRootPath+"login.xhtml" );
			// create index.jsp
			GeneratorUtil.copy(pathIndexJsp,webRootPath+"index.jsp" );

		}
		
		GeneratorUtil.copyFolder(pathWebXml,webRootPath+"WEB-INF"+GeneratorUtil.slash);
		
		if (!EclipseGeneratorUtil.isMavenProject) {
			//copy libraries
			log.info("Copy Libraries files Zathuracode");
			GeneratorUtil.copyFolder(pathHibernate, pathLib);
			GeneratorUtil.copyFolder(pathJPA, pathLib);
			GeneratorUtil.copyFolder(pathPrimeFaces, pathLib);

			GeneratorUtil.copyFolder(pathSpring, pathLib);
			GeneratorUtil.copyFolder(pathSpringSecurity, pathLib);

			GeneratorUtil.copyFolder(pathHibernateJpa, pathLib);			
			GeneratorUtil.copyFolder(pathSL4J, pathLib);
			GeneratorUtil.copyFolder(pathJamon, pathLib);
			GeneratorUtil.copyFolder(pathMojarra, pathLib);
			GeneratorUtil.copyFolder(pathApacheCommons, pathLib);
			GeneratorUtil.copyFolder(pathAopAlliance, pathLib);
			GeneratorUtil.copyFolder(pathLog4j, pathLib);
			//GeneratorUtil.copyFolder(pathServlet, pathLib);
		}

		//copy log4j
		String folderProjectPath = properties.getProperty("folderProjectPath");
		GeneratorUtil.copyFolder(log4j, folderProjectPath + GeneratorUtil.slash);

	}

	@Override
	public void doTemplate(String hdLocation, MetaDataModel metaDataModel,
			String jpaPckgName, String projectName, Integer specificityLevel,
			String domainName) throws Exception{

		try {



			ve= new VelocityEngine();		
			Properties propiedadesVelocity= new Properties();
			propiedadesVelocity.put("file.resource.loader.description", "Velocity File Resource Loader");
			propiedadesVelocity.put("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.FileResourceLoader");
			propiedadesVelocity.put("file.resource.loader.path",templatesPath);
			propiedadesVelocity.put("file.resource.loader.cache", "false");
			propiedadesVelocity.put("file.resource.loader.modificationCheckInterval", "2");
			ve.init(propiedadesVelocity);

			VelocityContext velocityContext= new VelocityContext();
			List<MetaData> listMetaData= metaDataModel.getTheMetaData();

			ISkyJetStringBuilderForId stringBuilderForId = new SkyJetStringBuilderForId(listMetaData);
			ISkyJetStringBuilder stringBuilder = new SkyJetStringBuilder(listMetaData, (SkyJetStringBuilderForId) stringBuilderForId);
			String packageOriginal = null;
			String virginPackage = null;
			String modelName = null;

			HashMap<String, String> primaryKeyByClass = new HashMap<String, String>();
			
			for (MetaData metaData : listMetaData) {
				primaryKeyByClass.put(metaData.getRealClassName().toLowerCase(), SkyJetUtilities.getInstance().camelCaseToUnderScore(metaData.getPrimaryKey().getName()));
			}
			
			if (specificityLevel.intValue() == 2) {
				try {
					int lastIndexOf = jpaPckgName.lastIndexOf(".");
					packageOriginal = jpaPckgName.substring(0, lastIndexOf);

					int lastIndexOf2 = packageOriginal.lastIndexOf(".") + 1;
					modelName = packageOriginal.substring(lastIndexOf2);

					int virginLastIndexOf = packageOriginal.lastIndexOf(".");
					virginPackage = packageOriginal.substring(0, virginLastIndexOf);
				} catch (Exception e) {
					log.error(e.toString());
				}
			} else {
				try {
					packageOriginal = jpaPckgName;

					int lastIndexOf2 = packageOriginal.lastIndexOf(".") + 1;
					modelName = jpaPckgName.substring(lastIndexOf2);

					int virginLastIndexOf = packageOriginal.lastIndexOf(".");
					virginPackage = packageOriginal.substring(0, virginLastIndexOf);
				} catch (Exception e) {
					log.error(e.toString());
				}
			}

			velocityContext.put("packageOriginal", packageOriginal);
			velocityContext.put("virginPackage", virginPackage);
			velocityContext.put("package", jpaPckgName);
			velocityContext.put("projectName", projectName);
			velocityContext.put("domainName", domainName);
			velocityContext.put("modelName", modelName);
			velocityContext.put("schema", EclipseGeneratorUtil.schema);
			velocityContext.put("frontend", EclipseGeneratorUtil.isFrontend);

			//Variables para generar el persistence.xml
			velocityContext.put("connectionUrl", EclipseGeneratorUtil.connectionUrl);
			velocityContext.put("connectionDriverClass", EclipseGeneratorUtil.connectionDriverClass);
			velocityContext.put("connectionUsername", EclipseGeneratorUtil.connectionUsername);
			velocityContext.put("connectionPassword", EclipseGeneratorUtil.connectionPassword);

			this.paqueteVirgen = GeneratorUtil.replaceAll(virginPackage, ".", GeneratorUtil.slash);
			SkyJetUtilities.getInstance().buildFolders(virginPackage, hdLocation, specificityLevel, packageOriginal, properties);
			SkyJetUtilities.getInstance().biuldHashToGetIdValuesLengths(listMetaData);

			for (MetaData metaData : listMetaData) {

				log.info(metaData.getRealClassName());

				String var= metaData.getPrimaryKey().getName();
				velocityContext.put("var", var);
				log.info(metaData.getPrimaryKey().getName());
				
				velocityContext.put("databaseName", SkyJetUtilities.getInstance().camelCaseToUnderScore(metaData.getRealClassNameAsVariable()));
				velocityContext.put("primaryKeyByClass", primaryKeyByClass);
				velocityContext.put("composedKey", false);

				if (metaData.getPrimaryKey().isPrimiaryKeyAComposeKey()) {
					velocityContext.put("composedKey", true);
					velocityContext.put("finalParamForIdClass", stringBuilderForId.finalParamForIdClass(listMetaData, metaData));
				}

				if (metaData.isGetManyToOneProperties()) {
					velocityContext.put("getVariableForManyToOneProperties", stringBuilder.getVariableForManyToOneProperties(metaData.getManyToOneProperties(), listMetaData));
					velocityContext.put("getStringsForManyToOneProperties", stringBuilder.getStringsForManyToOneProperties(metaData.getManyToOneProperties(), listMetaData));
				}

				// generacion de nuevos dto
				velocityContext.put("variableDto", stringBuilder.getPropertiesDto(listMetaData, metaData));
				velocityContext.put("propertiesDto",SkyJetUtilities.getInstance().dtoProperties);
				velocityContext.put("memberDto",SkyJetUtilities.getInstance().nameMemberToDto);

				// generacion de la entidad	
				List<SimpleMember> simpleMembers = new ArrayList<SimpleMember>();
				List<ManyToOneMember> manyToOneMembers= new ArrayList<ManyToOneMember>();
				List<OneToManyMember> oneToManyMembers= new ArrayList<OneToManyMember>();
				List<OneToOneMember> oneMembers = new ArrayList<OneToOneMember>();
				SimpleMember primaryKey = (SimpleMember) metaData.getPrimaryKey();

				String constructorStr = "";

				constructorStr = constructorStr + primaryKey.getType().getSimpleName() + " "
						+ primaryKey.getShowName() + ", ";

				for (Member member : metaData.getProperties()) {
					
					member.setDatabaseName(SkyJetUtilities.getInstance().camelCaseToUnderScore(member.getName()));
					
					if (member.isSimpleMember() && !member.getShowName().equals(primaryKey.getShowName())) {
						simpleMembers.add((SimpleMember) member);

						constructorStr = constructorStr + member.getType().getSimpleName() + " "
								+ member.getShowName() + ", ";
					}
					
					if (member.isOneToOneMember()) {
						oneMembers.add((OneToOneMember) member);
						
						constructorStr = constructorStr + member.getType().getSimpleName() + " "
								+ member.getShowName() + ", ";
					}


					if (member.isManyToOneMember()) {
						manyToOneMembers.add((ManyToOneMember) member);
						
						constructorStr = constructorStr + member.getType().getSimpleName() + " "
								+ member.getShowName() + ", ";
					}

					if (member.isOneToManyMember()) {
						oneToManyMembers.add((OneToManyMember) member);

						constructorStr = constructorStr + "Set<" + member.getType().getSimpleName() + "> "
								+ member.getShowName() + ", ";
					}

				}

				constructorStr = constructorStr.substring(0, constructorStr.length() - 2);

				velocityContext.put("simpleMembers", simpleMembers);
				velocityContext.put("manyToOneMembers", manyToOneMembers);
				velocityContext.put("oneToManyMembers", oneToManyMembers);
				velocityContext.put("primaryKey", primaryKey);
				velocityContext.put("constructorStr", constructorStr);
				velocityContext.put("composeKeyAttributes", stringBuilderForId.attributesComposeKey(listMetaData,metaData));

				// generacion de atributos para mapear de Entidad a DTO
				velocityContext.put("dtoAttributes", stringBuilderForId.obtainDTOMembersAndSetEntityAttributes(listMetaData, metaData));
				velocityContext.put("dtoAttributes2", stringBuilder.obtainDTOMembersAndSetEntityAttributes2(listMetaData, metaData));

				// generacion de los atributos para mapear de DTO a Entidad 
				velocityContext.put("entityAttributes", stringBuilderForId.obtainEntityMembersAndSetDTOAttributes(listMetaData, metaData));
				velocityContext.put("entityAttributes2", stringBuilder.obtainEntityMembersAndSetDTOAttributes2(listMetaData, metaData));


				// generacion de la nueva logica 
				velocityContext.put("dtoConvert", stringBuilderForId.dtoConvert(listMetaData,metaData));
				velocityContext.put("dtoConvert2", stringBuilder.dtoConvert2(listMetaData, metaData));

				velocityContext.put("finalParamForIdVariablesAsList", stringBuilderForId.finalParamForIdVariablesAsList(listMetaData, metaData));
				velocityContext.put("finalParamForIdClassAsVariables", stringBuilderForId.finalParamForIdClassAsVariables(listMetaData, metaData));

				velocityContext.put("finalParamForViewVariablesInList", stringBuilder.finalParamForViewVariablesInList(listMetaData, metaData));
				velocityContext.put("isFinalParamForViewDatesInList", SkyJetUtilities.getInstance().isFinalParamForViewDatesInList());
				velocityContext.put("isFinalParamForIdForViewDatesInList", SkyJetUtilities.getInstance().isFinalParamForIdForViewDatesInList());

				velocityContext.put("finalParamForDtoUpdate", stringBuilder.finalParamForDtoUpdate(listMetaData, metaData));
				velocityContext.put("finalParamForIdForDtoForSetsVariablesInList", stringBuilderForId.finalParamForIdForDtoForSetsVariablesInList(listMetaData, metaData));
				velocityContext.put("finalParamForDtoForSetsVariablesInList", stringBuilder.finalParamForDtoForSetsVariablesInList(listMetaData, metaData));
				velocityContext.put("finalParamForIdForDtoForSetsVariablesInList", stringBuilderForId.finalParamForIdForDtoForSetsVariablesInList(listMetaData, metaData));
				velocityContext.put("finalParamForIdForViewVariablesInList", stringBuilderForId.finalParamForIdForViewVariablesInList(listMetaData, metaData));
				velocityContext.put("isFinalParamForIdForViewDatesInList", SkyJetUtilities.getInstance().isFinalParamForIdForViewDatesInList());
				velocityContext.put("finalParamForIdForViewClass", stringBuilderForId.finalParamForIdForViewClass(listMetaData, metaData));
				velocityContext.put("finalParamForViewForSetsVariablesInList", stringBuilder.finalParamForViewForSetsVariablesInList(listMetaData, metaData));
				velocityContext.put("finalParamForView", stringBuilder.finalParamForView(listMetaData, metaData));
				velocityContext.put("finalParamForIdClassAsVariablesAsString", stringBuilderForId.finalParamForIdClassAsVariablesAsString(listMetaData, metaData));
				velocityContext.put("finalParamForDtoUpdateOnlyVariables", stringBuilder.finalParamForDtoUpdateOnlyVariables(listMetaData, metaData));
				velocityContext.put("finalParamForIdForDtoInViewForSetsVariablesInList", stringBuilderForId.finalParamForIdForDtoInViewForSetsVariablesInList(listMetaData,metaData));
				velocityContext.put("finalParamForDtoInViewForSetsVariablesInList", stringBuilder.finalParamForDtoInViewForSetsVariablesInList(listMetaData, metaData));
				velocityContext.put("finalParamForIdForViewDatesInList", SkyJetUtilities.getInstance().datesId);
				velocityContext.put("finalParamForViewDatesInList", SkyJetUtilities.getInstance().dates);
				velocityContext.put("isFinalParamForIdClassAsVariablesForDates", SkyJetUtilities.getInstance().isFinalParamForIdClassAsVariablesForDates());
				velocityContext.put("finalParamForIdClassAsVariablesForDates", SkyJetUtilities.getInstance().datesIdJSP);
				velocityContext.put("finalParamVariablesAsList", stringBuilder.finalParamVariablesAsList(listMetaData, metaData));
				velocityContext.put("isFinalParamDatesAsList", SkyJetUtilities.getInstance().isFinalParamDatesAsList());
				velocityContext.put("finalParamDatesAsList", SkyJetUtilities.getInstance().datesJSP);
				velocityContext.put("finalParamForIdClassAsVariables2", stringBuilderForId.finalParamForIdClassAsVariables2(listMetaData, metaData));
				velocityContext.put("finalParamForVariablesDataTablesForIdAsList", stringBuilderForId.finalParamForVariablesDataTablesForIdAsList(listMetaData, metaData));
				velocityContext.put("finalParamVariablesAsList2", stringBuilder.finalParamVariablesAsList2(listMetaData, metaData));
				velocityContext.put("finalParamForVariablesDataTablesAsList", stringBuilder.finalParamForVariablesDataTablesAsList(listMetaData, metaData));
				velocityContext.put("finalParamForIdForViewForSetsVariablesInList", stringBuilderForId.finalParamForIdForViewForSetsVariablesInList(listMetaData, metaData));
				velocityContext.put("finalParamVariablesDatesAsList2", stringBuilder.finalParamVariablesDatesAsList2(listMetaData, metaData));

				//listas nuevas para el manejo de tablas maestro detalle AndresPuerta
				velocityContext.put("finalParamForGetIdForViewClass", stringBuilder.finalParamForGetIdForViewClass(listMetaData, metaData));
				velocityContext.put("finalParamForGetIdByDtoForViewClass", stringBuilder.finalParamForGetIdByDtoForViewClass(listMetaData, metaData));
				velocityContext.put("finalParamForIdForViewForSetsVariablesDtoInList", stringBuilderForId.finalParamForIdForViewForSetsVariablesDtoInList(listMetaData, metaData));
				velocityContext.put("finalParamForViewForSetsVariablesDtoInList", stringBuilder.finalParamForViewForSetsVariablesDtoInList(listMetaData, metaData));
				velocityContext.put("finalParamForGetManyToOneForViewClass", stringBuilder.finalParamForGetManyToOneForViewClass(listMetaData, metaData));

				velocityContext.put("dataModel", metaDataModel);
				velocityContext.put("metaData", metaData);

				String finalParam = stringBuilder.finalParam(listMetaData, metaData);
				velocityContext.put("finalParam", finalParam);
				metaData.setFinamParam(finalParam);

				String finalParamForId= stringBuilderForId.finalParamForId(listMetaData, metaData);
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
				doDaoSpringXMLHibernate(metaData, velocityContext, hdLocation);
				if (EclipseGeneratorUtil.isFrontend) {
					doBackingBeans(metaData, velocityContext, hdLocation, metaDataModel);
					doJsp(metaData, velocityContext, hdLocation, metaDataModel);
				}
				doLogicSpringXMLHibernate(metaData, velocityContext, hdLocation, metaDataModel, modelName);
				doDto(metaData, velocityContext, hdLocation, metaDataModel, modelName);
				doDTOMapper(metaData, velocityContext, hdLocation, metaDataModel);
				doRestControllers(metaData, velocityContext, hdLocation, metaDataModel);

			}

			if (EclipseGeneratorUtil.isMavenProject) {
				GeneratorUtil.doPomXml(velocityContext, ve);
			}

			doApiSpringHibernate(velocityContext, hdLocation);
			doExceptions(velocityContext, hdLocation);
			doUtilites(velocityContext, hdLocation, metaDataModel, modelName);
			if (EclipseGeneratorUtil.isFrontend) {
				doAuthenticationProvider(velocityContext, hdLocation, metaDataModel, modelName);
				doJspFacelets(velocityContext, hdLocation);
				doJspInitialMenu(metaDataModel, velocityContext, hdLocation);
			}
			doPersitenceXml(metaDataModel, velocityContext, hdLocation);						
			doBusinessDelegator(velocityContext, hdLocation, metaDataModel);
			doFacesConfig(metaDataModel, velocityContext, hdLocation);
			doSpringContextConfFiles(velocityContext, hdLocation, metaDataModel, modelName);

			String restPath = paqueteVirgen + GeneratorUtil.slash + "rest" + GeneratorUtil.slash + "controllers";
			restPath = restPath.replace(GeneratorUtil.slash, ".");
			velocityContext.put("restPackage", restPath);

			doMvcDispatcherServlet(metaDataModel, velocityContext, hdLocation);

		} catch (Exception e) {
			log.error(e.toString());
			throw e;
		}finally{

		}


	}

	@Override
	public void doDaoSpringXMLHibernate(MetaData metaData,
			VelocityContext context, String hdLocation)throws Exception {
		try {

			String path =hdLocation + paqueteVirgen + GeneratorUtil.slash + "dataaccess" + GeneratorUtil.slash + "dao"+ GeneratorUtil.slash;
			log.info("Begin IDao");
			Template templateIDao = ve.getTemplate("IDAOSpringJpaPrime.vm");
			StringWriter swIdao = new StringWriter();
			templateIDao.merge(context, swIdao);
			FileWriter fwIdao = new FileWriter(path+ "I" +metaData.getRealClassName()+"DAO.java");
			BufferedWriter bwIdao = new BufferedWriter(fwIdao);
			bwIdao.write(swIdao.toString());
			bwIdao.close();
			fwIdao.close();
			log.info("End IDao");
			JalopyCodeFormatter.formatJavaCodeFile(path + "I" + metaData.getRealClassName() + "DAO.java");

			log.info("Begin Dao");
			Template templateDao = ve.getTemplate("DAOSpringJpaPrime.vm");
			StringWriter swDao = new StringWriter();
			templateDao.merge(context, swDao);
			FileWriter fwDao = new FileWriter(path+ metaData.getRealClassName()+"DAO.java");
			BufferedWriter bwDao = new BufferedWriter(fwDao);
			bwDao.write(swDao.toString());
			bwDao.close();
			fwDao.close();
			JalopyCodeFormatter.formatJavaCodeFile(path + metaData.getRealClassName() + "DAO.java");
			log.info("End Dao");

		} catch (Exception e) {
			log.error(e.toString());
			throw e;
		}

	}

	@Override
	public void doApiSpringHibernate(VelocityContext context, String hdLocation) throws Exception{

		try {

			String path=hdLocation + paqueteVirgen + GeneratorUtil.slash + "dataaccess" + GeneratorUtil.slash + "api"+ GeneratorUtil.slash;

			log.info("Begin api Spring+PrimeFaces+Hibernate");

			Template apiSpringPrimeHibernateTemplate = ve.getTemplate("Dao.vm");
			StringWriter stringWriter = new StringWriter();
			apiSpringPrimeHibernateTemplate.merge(context, stringWriter);
			FileWriter fileWriter = new FileWriter(path+"Dao.java");
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
			bufferedWriter.write(stringWriter.toString());
			bufferedWriter.close();
			fileWriter.close();

			apiSpringPrimeHibernateTemplate = ve.getTemplate("DaoException.vm");
			stringWriter = new StringWriter();
			apiSpringPrimeHibernateTemplate.merge(context, stringWriter);
			fileWriter = new FileWriter(path+"DaoException.java");
			bufferedWriter = new BufferedWriter(fileWriter);
			bufferedWriter.write(stringWriter.toString());
			bufferedWriter.close();
			fileWriter.close();

			apiSpringPrimeHibernateTemplate = ve.getTemplate("JpaDaoImpl.vm");
			stringWriter = new StringWriter();
			apiSpringPrimeHibernateTemplate.merge(context, stringWriter);
			fileWriter = new FileWriter(path+"JpaDaoImpl.java");
			bufferedWriter = new BufferedWriter(fileWriter);
			bufferedWriter.write(stringWriter.toString());
			bufferedWriter.close();
			fileWriter.close();

			apiSpringPrimeHibernateTemplate = ve.getTemplate("Paginator.vm");
			stringWriter = new StringWriter();
			apiSpringPrimeHibernateTemplate.merge(context, stringWriter);
			fileWriter = new FileWriter(path+"Paginator.java");
			bufferedWriter = new BufferedWriter(fileWriter);
			bufferedWriter.write(stringWriter.toString());
			bufferedWriter.close();
			fileWriter.close();

			log.info("End api Spring+PrimeFaces+Hibernate");

			JalopyCodeFormatter.formatJavaCodeFile(path + "Dao.java");
			JalopyCodeFormatter.formatJavaCodeFile(path + "DaoException.java");
			JalopyCodeFormatter.formatJavaCodeFile(path + "JpaDaoImpl.java");
			JalopyCodeFormatter.formatJavaCodeFile(path + "Paginator.java");

		} catch (Exception e) {
			log.error(e.toString());
			throw e;
		}

	}

	@Override
	public void doLogicSpringXMLHibernate(MetaData metaData,
			VelocityContext context, String hdLocation,
			MetaDataModel dataModel, String modelName)throws Exception {
		try {
			String path=hdLocation + paqueteVirgen + GeneratorUtil.slash + modelName + GeneratorUtil.slash +"control" + GeneratorUtil.slash;

			log.info("Begin ILogic");
			Template templateIlogic = ve.getTemplate("ILogicSpringJpaPrimeFaces.vm");
			StringWriter swIlogic = new StringWriter();
			templateIlogic.merge(context, swIlogic);

			FileWriter fwIlogic = new FileWriter(path+"I"+ metaData.getRealClassName()+"Logic.java");
			BufferedWriter bwIlogic = new BufferedWriter(fwIlogic);
			bwIlogic.write(swIlogic.toString());
			bwIlogic.close();
			fwIlogic.close();
			log.info("End ILogic");

			log.info("Begin Logic");
			Template templateLogic = ve.getTemplate("LogicSpringJpaPrimeFaces.vm");
			StringWriter swLogic= new StringWriter();
			templateLogic.merge(context, swLogic);
			FileWriter fwLogic = new FileWriter(path+ metaData.getRealClassName() + "Logic.java");
			BufferedWriter bwLogic = new BufferedWriter(fwLogic);
			bwLogic.write(swLogic.toString());
			bwLogic.close();
			fwLogic.close();
			log.info("End Logic");

			JalopyCodeFormatter.formatJavaCodeFile(path + "I" + metaData.getRealClassName() + "Logic.java");
			JalopyCodeFormatter.formatJavaCodeFile(path + metaData.getRealClassName() + "Logic.java");




		} catch (Exception e) {
			log.error(e.toString());
			throw e;
		}


	}

	@Override
	public void doBusinessDelegator(VelocityContext context, String hdLocation,
			MetaDataModel dataModel)throws Exception {
		try {

			String path = hdLocation + paqueteVirgen + GeneratorUtil.slash + "presentation"+ GeneratorUtil.slash + "businessDelegate" +GeneratorUtil.slash;
			log.info("Begin IBusinessDelegate");
			Template templateIbusinessDelegate= ve.getTemplate("IBusinessDelegatorView.vm");
			StringWriter swIbusinessDelegate = new StringWriter();
			templateIbusinessDelegate.merge(context, swIbusinessDelegate);
			FileWriter fwIbusinessDelegate = new FileWriter(path+"IBusinessDelegatorView.java");
			BufferedWriter bwIbusinessDelegate = new BufferedWriter(fwIbusinessDelegate);
			bwIbusinessDelegate.write(swIbusinessDelegate.toString());
			bwIbusinessDelegate.close();
			fwIbusinessDelegate.close();
			log.info("End IBusinessDelegate");

			log.info("Begin BusinessDelegate");
			Template templateBusinessDelegate = ve.getTemplate("BusinessDelegatorView.vm");
			StringWriter swBusinessDelegate = new StringWriter();
			templateBusinessDelegate.merge(context, swBusinessDelegate);
			FileWriter fwBusinessDelegate = new FileWriter(path+"BusinessDelegatorView.java");
			BufferedWriter bwBusinessDelegate = new BufferedWriter(fwBusinessDelegate);
			bwBusinessDelegate.write(swBusinessDelegate.toString());
			bwBusinessDelegate.close();
			fwBusinessDelegate.close();
			log.info("End BusinessDelegate");

			JalopyCodeFormatter.formatJavaCodeFile(path + "IBusinessDelegatorView.java");
			JalopyCodeFormatter.formatJavaCodeFile(path + "BusinessDelegatorView.java");

		} catch (Exception e) {
			log.error(e.toString());
			throw e;

		}

	}

	@Override
	public void doJsp(MetaData metaData, VelocityContext context,
			String hdLocation, MetaDataModel dataModel)throws Exception {
		try {

			String path = properties.getProperty("webRootFolderPath") + "XHTML" + GeneratorUtil.slash;

			log.info("Begin XHTML");
			Template templateXhtml = ve.getTemplate("XHTMLSpringJpaPrime.vm");
			StringWriter swXhtml = new StringWriter();
			templateXhtml.merge(context, swXhtml);

			FileWriter fwXhtml = new FileWriter(path+metaData.getRealClassNameAsVariable()+".xhtml");
			BufferedWriter bwXhtml = new BufferedWriter(fwXhtml);
			bwXhtml.write(swXhtml.toString());
			bwXhtml.close();
			fwXhtml.close();
			log.info("End XHTML");

			log.info("Begin DataTable");
			Template templateDataTable = ve.getTemplate("XHTMLdataTablesJpaPrime.vm");
			StringWriter swDataTable = new StringWriter();
			templateDataTable.merge(context, swDataTable);
			FileWriter fwDataTable = new FileWriter(path+metaData.getRealClassNameAsVariable()+"ListDataTable.xhtml");
			BufferedWriter bwDataTable = new BufferedWriter(fwDataTable);
			bwDataTable.write(swDataTable.toString());
			bwDataTable.close();
			fwDataTable.close();
			log.info("End DataTable");

//			log.info("Begin DataTableEditable");
//			Template templateDataTableEditable = ve.getTemplate("XHTMLdataTableEditableJpaPrime.vm");
//			StringWriter swDataTableEditable = new StringWriter();
//			templateDataTableEditable.merge(context, swDataTableEditable);
//			FileWriter fwDataTableEditable = new FileWriter(path+ metaData.getRealClassNameAsVariable()+"ListDataTableEditable.xhtml");
//			BufferedWriter bwDataTableEditable = new BufferedWriter(fwDataTableEditable);
//			bwDataTableEditable.write(swDataTableEditable.toString());
//			bwDataTableEditable.close();
//			fwDataTableEditable.close();
//			log.info("End DataTableEditable");

			SkyJetUtilities.getInstance().datesJSP = null;
			SkyJetUtilities.getInstance().datesIdJSP = null;


		} catch (Exception e) {
			log.error(e.toString());
			throw e;
		}

	}

	@Override
	public void doJspInitialMenu(MetaDataModel dataModel,
			VelocityContext context, String hdLocation)throws Exception {
		try {
			String path = properties.getProperty("webRootFolderPath") + "XHTML" + GeneratorUtil.slash;

			log.info("Begin InitialMaenu");
			Template templateInitialMenu = ve.getTemplate("XHTMLinitialMenu.vm");
			StringWriter swInitialMenu = new StringWriter();
			templateInitialMenu.merge(context, swInitialMenu);
			FileWriter fwInitialMenu = new FileWriter(path+"initialMenu.xhtml");
			BufferedWriter bwInitialMenu = new BufferedWriter(fwInitialMenu);
			bwInitialMenu.write(swInitialMenu.toString());
			bwInitialMenu.close();
			fwInitialMenu.close();
			log.info("Begin InitialMaenu");

		} catch (Exception e) {
			log.error(e.toString());
			throw e;
		}

	}

	@Override
	public void doFacesConfig(MetaDataModel dataModel, VelocityContext context,
			String hdLocation) throws Exception{
		try {

			String path = properties.getProperty("webRootFolderPath")+"WEB-INF"+ GeneratorUtil.slash;
			log.info("Begin FacesConfig");
			Template templateFacesConfig = ve.getTemplate("faces-configSpringPrimeJpa.xml.vm");
			StringWriter swFacesConfig = new StringWriter();
			templateFacesConfig.merge(context, swFacesConfig);

			FileWriter fwFacesConfig = new FileWriter(path+"faces-config.xml");
			BufferedWriter bwFacesConfig = new BufferedWriter(fwFacesConfig);
			bwFacesConfig.write(swFacesConfig.toString());
			bwFacesConfig.close();
			fwFacesConfig.close();
			log.info("Begin FacesConfig");

		} catch (Exception e) {
			log.error(e.toString());
			throw e;
		}

	}

	@Override
	public void doDto(MetaData metaData, VelocityContext context,
			String hdLocation, MetaDataModel dataModel, String modelName)throws Exception {
		try {

			Template dtoTemplate = ve.getTemplate("DtoPrimeSpringJpa.vm");
			StringWriter swDto = new StringWriter();
			dtoTemplate.merge(context, swDto);

			String path=hdLocation + paqueteVirgen + GeneratorUtil.slash + modelName + GeneratorUtil.slash + "dto"+ GeneratorUtil.slash;
			FileWriter fwDto = new FileWriter(path+metaData.getRealClassName()+"DTO.java");
			BufferedWriter bwDto = new BufferedWriter(fwDto);
			bwDto.write(swDto.toString());
			bwDto.close();
			swDto.close();
			fwDto.close();
			JalopyCodeFormatter.formatJavaCodeFile(path+metaData.getRealClassName()+"DTO.java");

		} catch (Exception e) {
			log.error(e.toString());
			throw e;
		}

	}

	@Override
	public void doExceptions(VelocityContext context, String hdLocation)throws Exception {
		try {

			String path=hdLocation + paqueteVirgen + GeneratorUtil.slash + "exceptions" + GeneratorUtil.slash;
			log.info("Begin Exception");
			Template templateException = ve.getTemplate("ZMessManager.vm");
			StringWriter swException = new StringWriter();
			templateException.merge(context, swException);
			FileWriter fwException = new FileWriter(path+ "ZMessManager.java");
			BufferedWriter bwException = new BufferedWriter(fwException);
			bwException.write(swException.toString());
			bwException.close();
			fwException.close();
			log.info("End Exception");
			JalopyCodeFormatter.formatJavaCodeFile(path+ "ZMessManager.java");

		} catch (Exception e) {
			log.error(e.toString());
			throw e;
		}

	}

	@Override
	public void doUtilites(VelocityContext context, String hdLocation,
			MetaDataModel dataModel, String modelName)throws Exception {

		try {

			String path=hdLocation + paqueteVirgen + GeneratorUtil.slash + "utilities" + GeneratorUtil.slash;
			log.info("Begin Utilities");
			Template templateUtilities = ve.getTemplate("Utilities.vm");
			StringWriter swUtilities= new StringWriter();
			templateUtilities.merge(context, swUtilities);
			FileWriter fwUtilities = new FileWriter(path+"Utilities.java");
			BufferedWriter bwUtilities = new BufferedWriter(fwUtilities);
			bwUtilities.write(swUtilities.toString());
			bwUtilities.close();
			fwUtilities.close();
			log.info("Begin Utilities");

			log.info("Begin FacesUtils ");
			Template templateFacesUtils = ve.getTemplate("FacesUtils.vm");
			StringWriter swFacesUtils = new StringWriter();
			templateFacesUtils.merge(context, swFacesUtils);
			FileWriter fwFacesUtils = new FileWriter(path+"FacesUtils.java");
			BufferedWriter bwFacesUtils = new BufferedWriter(fwFacesUtils);
			bwFacesUtils.write(swFacesUtils.toString());
			bwFacesUtils.close();
			fwFacesUtils.close();

		} catch (Exception e) {
			log.error(e.toString());
			throw e;
		}

	}

	@Override
	public void doJspFacelets(VelocityContext context, String hdLocation)throws Exception {
		try {
			log.info("Begin Header");
			String pathFacelets = properties.getProperty("webRootFolderPath") + "WEB-INF" + GeneratorUtil.slash;
			Template templateHeader = ve.getTemplate("JSPheader.vm");
			StringWriter swHeader = new StringWriter();
			templateHeader.merge(context, swHeader);
			FileWriter fwHeader = new FileWriter(pathFacelets+"topbar.xhtml");
			BufferedWriter bwHeader = new BufferedWriter(fwHeader);
			bwHeader.write(swHeader.toString());
			bwHeader.close();
			fwHeader.close();
			log.info("End Header");

			log.info("Begin Footer");
			Template templateFooter = ve.getTemplate("JSPfooter.vm");
			StringWriter swFooter = new StringWriter();
			templateFooter.merge(context, swFooter);
			FileWriter fwFooter = new FileWriter(pathFacelets+"footer.xhtml");
			BufferedWriter bwFooter = new BufferedWriter(fwFooter);
			bwFooter.write(swFooter.toString());
			bwFooter.close();
			fwFooter.close();
			log.info("End Footer");


			log.info("Begin menu");
			Template templateCommonsColumns = ve.getTemplate("menu.vm");
			StringWriter swCommonColumns = new StringWriter();
			templateCommonsColumns.merge(context, swCommonColumns);
			FileWriter fwCommonColumns = new FileWriter(pathFacelets+"menu.xhtml");
			BufferedWriter bwCommonColumns = new BufferedWriter(fwCommonColumns);
			bwCommonColumns.write(swCommonColumns.toString());
			bwCommonColumns.close();
			swCommonColumns.close();
			log.info("End menu");

			log.info("Begin template");
			Template templateCommonLayout = ve.getTemplate("template.vm");
			StringWriter swCommonLayout = new StringWriter();
			templateCommonLayout.merge(context, swCommonLayout);
			FileWriter fwCommonLayout = new FileWriter(pathFacelets+"template.xhtml");
			BufferedWriter bwCommonLayout = new BufferedWriter(fwCommonLayout);
			bwCommonLayout.write(swCommonLayout.toString());
			bwCommonLayout.close();
			fwCommonLayout.close();
			log.info("End template");

		} catch (Exception e) {
			log.error(e.toString());
			throw e;
		}

	}

	@Override
	public void doSpringContextConfFiles(VelocityContext context,
			String hdLocation, MetaDataModel dataModel, String modelName)throws Exception {
		try {
			log.info("Begin applicationContext.xml");
			Template templateApplicationContext = ve.getTemplate("applicationContext.xml.vm");
			StringWriter swApplicationContext = new StringWriter();
			templateApplicationContext.merge(context, swApplicationContext);
			FileWriter fwApplicationContext = new FileWriter(hdLocation + "applicationContext.xml");
			BufferedWriter bwApplicationContext = new BufferedWriter(fwApplicationContext);
			bwApplicationContext.write(swApplicationContext.toString());
			bwApplicationContext.close();
			fwApplicationContext.close();
			log.info("End applicationContext.xml");

			log.info("Begin aopContext.xml");
			Template templateAopContext = ve.getTemplate("aopContext.xml.vm");
			StringWriter swAopContext = new StringWriter();
			templateAopContext.merge(context, swAopContext);
			FileWriter fwAopContext = new FileWriter(hdLocation+"aopContext.xml");
			BufferedWriter bwAopContext = new BufferedWriter(fwAopContext);
			bwAopContext.write(swAopContext.toString());
			bwAopContext.close();
			fwAopContext.close();
			log.info("End aopContext.xml");

			log.info("Begin securityContext.xml");
			Template secContext= ve.getTemplate("securityContext.xml.vm");
			StringWriter swSecContext = new StringWriter();
			secContext.merge(context, swSecContext);
			FileWriter fwSecContext = new FileWriter(hdLocation+"securityContext.xml");
			BufferedWriter bwSecContext = new BufferedWriter(fwSecContext);
			bwSecContext.write(swSecContext.toString());
			bwSecContext.close();
			fwSecContext.close();
			log.info("End securityContext.xml");


//			log.info("Begin dataSourceContext.xml");
//			Template dataSourceContext= ve.getTemplate("dataSourceContext.xml.vm");
//			StringWriter swDataSourceContext = new StringWriter();
//			dataSourceContext.merge(context, swDataSourceContext);
//			FileWriter fwDataSourceContext = new FileWriter(hdLocation+"dataSourceContext.xml");
//			BufferedWriter bwDataSourceContext = new BufferedWriter(fwDataSourceContext);
//			bwDataSourceContext.write(swDataSourceContext.toString());
//			bwDataSourceContext.close();
//			fwDataSourceContext.close();
//			log.info("End dataSourceContext.xml");

		} catch (Exception e) {
			log.error(e.toString());
			throw e;
		}

	}

	@Override
	public void doPersitenceXml(MetaDataModel dataModel,
			VelocityContext context, String hdLocation)throws Exception {

		try {

			log.info("Begin persistnece.xml");
			Template templatePersistence = ve.getTemplate("persistence.xml.vm");
			StringWriter swPersistence = new StringWriter();
			templatePersistence.merge(context, swPersistence);
			FileWriter fwPersistence = new FileWriter(hdLocation + "META-INF" + GeneratorUtil.slash + "persistence.xml");
			BufferedWriter bwPersistence = new BufferedWriter(fwPersistence);
			bwPersistence.write(swPersistence.toString());
			bwPersistence.close();
			fwPersistence.close();
			log.info("Begin persistnece.xml");


		} catch (Exception e) {
			log.error(e.toString());
			throw e;
		}

	}

	@Override
	public void doBackingBeans(MetaData metaData, VelocityContext context,
			String hdLocation, MetaDataModel dataModel)throws Exception {
		try {
			String path =hdLocation + paqueteVirgen + GeneratorUtil.slash + "presentation" + GeneratorUtil.slash + "backingBeans" + GeneratorUtil.slash;
			log.info("Begin BackEndBean ");
			Template templateBackEndBean= ve.getTemplate("BackingBeansSpringJpaPrime.vm");
			StringWriter swBackEndBean = new StringWriter();
			templateBackEndBean.merge(context, swBackEndBean);
			FileWriter fwBackEndBean = new FileWriter(path+ metaData.getRealClassName() + "View.java");
			BufferedWriter bwBackEndBean = new BufferedWriter(fwBackEndBean);
			bwBackEndBean.write(swBackEndBean.toString());
			bwBackEndBean.close();
			fwBackEndBean.close();
			JalopyCodeFormatter.formatJavaCodeFile(path + metaData.getRealClassName() + "View.java");
			SkyJetUtilities.getInstance().dates = null;
			SkyJetUtilities.getInstance().datesId = null;

		} catch (Exception e) {
			log.error(e.toString());
			throw e;
		}

	}

	@Override
	public void doAuthenticationProvider(VelocityContext context, String hdLocation,MetaDataModel dataModel, String modelName) throws Exception{

		try {

			String path =hdLocation + paqueteVirgen + GeneratorUtil.slash + "security" + GeneratorUtil.slash;

			log.info("Begin ZathuraCodeAuthenticationProvider");
			Template templateBakcEndBean= ve.getTemplate("ZathuraCodeAuthenticationProvider.vm");
			StringWriter swBackEndBean = new StringWriter();
			templateBakcEndBean.merge(context, swBackEndBean);

			FileWriter fwBackEndBean = new FileWriter(path+ "ZathuraCodeAuthenticationProvider.java");
			BufferedWriter bwBackEndBean = new BufferedWriter(fwBackEndBean);
			bwBackEndBean.write(swBackEndBean.toString());
			bwBackEndBean.close();
			fwBackEndBean.close();
			log.info("Begin BackEndBean");
			JalopyCodeFormatter.formatJavaCodeFile(path+ "ZathuraCodeAuthenticationProvider.java");
			SkyJetUtilities.getInstance().dates = null;
			SkyJetUtilities.getInstance().datesId = null;


			//ManageBean for LoginView
			path =hdLocation + paqueteVirgen + GeneratorUtil.slash + "presentation" + GeneratorUtil.slash + "backingBeans" + GeneratorUtil.slash;

			log.info("Begin LoginView");
			templateBakcEndBean= ve.getTemplate("LoginView.vm");
			swBackEndBean = new StringWriter();
			templateBakcEndBean.merge(context, swBackEndBean);

			fwBackEndBean = new FileWriter(path+ "LoginView.java");
			bwBackEndBean = new BufferedWriter(fwBackEndBean);
			bwBackEndBean.write(swBackEndBean.toString());
			bwBackEndBean.close();
			fwBackEndBean.close();
			log.info("Begin BackEndBean");
			JalopyCodeFormatter.formatJavaCodeFile(path+ "LoginView.java");
			SkyJetUtilities.getInstance().dates = null;
			SkyJetUtilities.getInstance().datesId = null;


		} catch (Exception e) {
			log.error(e.toString());
			throw e;
		}
	}

	@Override
	public void doDTOMapper(MetaData metaData, VelocityContext context,String hdLocation, MetaDataModel dataModel) throws Exception{
		try {

			String path = hdLocation + paqueteVirgen + GeneratorUtil.slash + "dto" + GeneratorUtil.slash + "mapper" + GeneratorUtil.slash;

			log.info("Begin I DTO Mapper");
			Template templateIMapperDTO = ve.getTemplate("IMapperDTOHbm.vm");
			StringWriter swIMapperDTO = new StringWriter();
			templateIMapperDTO.merge(context, swIMapperDTO);

			FileWriter fwIMapperDTO = new FileWriter(path + "I" + metaData.getRealClassName() + "Mapper.java");
			BufferedWriter bwIMapperDTO = new BufferedWriter(fwIMapperDTO);
			bwIMapperDTO.write(swIMapperDTO.toString());
			bwIMapperDTO.close();
			fwIMapperDTO.close();

			log.info("Begin DTO Mapper");

			Template templateMapperDTO = ve.getTemplate("MapperDTOHbm.vm");
			StringWriter swMapperDTO = new StringWriter();
			templateMapperDTO.merge(context, swMapperDTO);

			FileWriter fwMapperDTO = new FileWriter(path + metaData.getRealClassName() + "Mapper.java");
			BufferedWriter bwMapperDTO = new BufferedWriter(fwMapperDTO);
			bwMapperDTO.write(swMapperDTO.toString());
			bwMapperDTO.close();
			fwMapperDTO.close();

			JalopyCodeFormatter.formatJavaCodeFile(path + "I" + metaData.getRealClassName() + "Mapper.java");
			JalopyCodeFormatter.formatJavaCodeFile(path + metaData.getRealClassName() + "Mapper.java");

		} catch (Exception e) {
			log.error(e.toString());
			throw e;
		}

	}

	@Override
	public void doRestControllers(MetaData metaData, VelocityContext context,String hdLocation, MetaDataModel dataModel) throws Exception{
		try {

			String path = hdLocation + paqueteVirgen + GeneratorUtil.slash + "rest" + GeneratorUtil.slash + "controllers" + GeneratorUtil.slash;

			log.info("Begin RestControllers");
			Template templateBakcEndBean= ve.getTemplate("RestController.vm");
			StringWriter swBackEndBean = new StringWriter();
			templateBakcEndBean.merge(context, swBackEndBean);

			FileWriter fwBackEndBean = new FileWriter(path+ metaData.getRealClassName() + "RestController.java");
			BufferedWriter bwBackEndBean = new BufferedWriter(fwBackEndBean);
			bwBackEndBean.write(swBackEndBean.toString());
			bwBackEndBean.close();
			fwBackEndBean.close();
			log.info("Begin RestControllers 2");
			JalopyCodeFormatter.formatJavaCodeFile(path + metaData.getRealClassName() + "RestController.java");
			JenderUtilities.getInstance().dates = null;
			JenderUtilities.getInstance().datesId = null;

		} catch (Exception e) {
			log.error(e.toString());
			throw e;
		}

	}

	@Override
	public void doMvcDispatcherServlet(MetaDataModel dataModel,
			VelocityContext context, String hdLocation) throws Exception{
		try {
			String path=properties.getProperty("webRootFolderPath") + "WEB-INF" + GeneratorUtil.slash;
			log.info("Begin Initial MVC Dispatcher");
			Template templateInitialMenu = ve.getTemplate("mvcDispatcherServletSkyJet.vm");
			StringWriter swInitialMenu = new StringWriter();
			templateInitialMenu.merge(context, swInitialMenu);
			FileWriter fwInitialMenu = new FileWriter(path+"mvc-dispatcher-servlet.xml");
			BufferedWriter bwInitialMenu = new BufferedWriter(fwInitialMenu);
			bwInitialMenu.write(swInitialMenu.toString());
			bwInitialMenu.close();
			fwInitialMenu.close();
			log.info("End Initial  XHTML");
			
			log.info("Begin Initial web.xml");
			Template templateWeb = ve.getTemplate("web.vm");
			StringWriter swWeb = new StringWriter();
			templateWeb.merge(context, swWeb);
			FileWriter fwWeb = new FileWriter(path+"web.xml");
			BufferedWriter bwWeb = new BufferedWriter(fwWeb);
			bwWeb.write(swWeb.toString());
			bwWeb.close();
			bwWeb.close();
			log.info("End web.xml");
			
		} catch (Exception e) {
			log.error(e.toString());
			throw e;
		}

	}

	@Override
	public void doEntityGenerator(MetaData metaData, VelocityContext velocityContext, String hdLocation,
			MetaDataModel metaDataModel) throws Exception {

		try{
			String path = hdLocation + metaData.getMainClass().toString().substring(6, metaData.getMainClass().toString().lastIndexOf(".")) + GeneratorUtil.slash;

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
}
