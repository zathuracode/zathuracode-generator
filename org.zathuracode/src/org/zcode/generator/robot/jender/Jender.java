package org.zcode.generator.robot.jender;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.StringWriter;
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
import org.zcode.metadata.model.MetaData;
import org.zcode.metadata.model.MetaDataModel;

/**
 * Jender
 * Zathuracode Generator
 * www.zathuracode.org
 * @author Diego Armando Gomez (dgomez@vortexbird.com)
 * @version 1.0
 */
public class Jender implements IZathuraJenderTemplate,IZathuraGenerator{
	
	private static final Logger log = LoggerFactory.getLogger(Jender.class);
	//private static String pathTemplates;
	private Properties properties;
	private String virginPackageInHd;
	private VelocityEngine ve;
	private String webRootPath;
	
	private final static String mainFolder="jender";
	
	
	/// final static String pathTemplates;
	private final static String templatesPath=GeneratorUtil.getGeneratorTemplatesPath()+GeneratorUtil.slash+mainFolder+GeneratorUtil.slash;
	private final static String librariesPath=GeneratorUtil.getGeneratorLibrariesPath()+GeneratorUtil.slash;
	private final static String extPath	  =GeneratorUtil.getGeneratorExtPath()+         GeneratorUtil.slash+mainFolder+GeneratorUtil.slash;
	
	


	@Override
	public void toGenerate(MetaDataModel metaDataModel, String projectName,	String folderProjectPath, Properties propiedades) throws Exception{
	
		
		Thread thread = Thread.currentThread();
		ClassLoader loader = thread.getContextClassLoader();
		thread.setContextClassLoader(EclipseGeneratorUtil.bundleClassLoader);
		log.info("Chaged ContextClassLoader:"+EclipseGeneratorUtil.bundleClassLoader);
		
		try {
			String jpaPckgName = propiedades.getProperty("jpaPckgName");
			String domainName = jpaPckgName.substring(0, jpaPckgName.indexOf("."));
			Integer specificityLevel = (Integer) propiedades.get("specificityLevel");
			properties = propiedades;
			webRootPath=(propiedades.getProperty("webRootFolderPath"));
			
			log.info("===================== Begin Jender Zathuracode =====================");				
			doTemplate(folderProjectPath, metaDataModel, jpaPckgName, projectName, specificityLevel, domainName);
			copyLibraries();				
			log.info("=====================  End Jender Zathuracode  =====================");
		} catch (Exception e) {
			throw e;
		}finally{
			log.info("Chaged ContextClassLoader:"+loader);
			thread.setContextClassLoader(loader);
		}


	}
	
	public void copyLibraries() throws Exception{
		String pathIndexJsp = extPath+"index.jsp";
		String pathLogin = extPath+"login.xhtml";
		String pathWebXml= extPath+"WEB-INF"+GeneratorUtil.slash;
		String generatorExtZathuraJavaEEWebSpringPrimeHibernateCentricImages = extPath + GeneratorUtil.slash + "images"	+ GeneratorUtil.slash;
		
		String pathHibernate= librariesPath+"core-hibernate"+GeneratorUtil.slash;
		String pathJpaHibernate=librariesPath+"hibernate-jpa"+GeneratorUtil.slash;
		
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
		
		String pathCss = extPath + GeneratorUtil.slash + "css"+ GeneratorUtil.slash;
		String log4j = extPath+ GeneratorUtil.slash + "log4j"+ GeneratorUtil.slash;
		
		
		// Copy Css
		GeneratorUtil.createFolder(webRootPath + "css");
		GeneratorUtil.copyFolder(pathCss, webRootPath + "css" + GeneratorUtil.slash);		
		GeneratorUtil.copyFolder(pathWebXml,webRootPath+"WEB-INF"+GeneratorUtil.slash);		
		
		//create folder images and insert .png
		GeneratorUtil.createFolder(webRootPath + "images");
		GeneratorUtil.copyFolder(generatorExtZathuraJavaEEWebSpringPrimeHibernateCentricImages, webRootPath + "images" + GeneratorUtil.slash);
		
		
		// create login.xhtml
		GeneratorUtil.copy(pathLogin,webRootPath+"login.xhtml" );
		// create index.jsp
		GeneratorUtil.copy(pathIndexJsp,webRootPath+"index.jsp" );
		//Se valida si el proyecto no es maven, para empezar a copiar las librerias
		if(!EclipseGeneratorUtil.isMavenProject){
			//copy libraries
			log.info("Copy Libraries files Zathuracode");
			GeneratorUtil.copyFolder(pathHibernate, pathLib);
			GeneratorUtil.copyFolder(pathJpaHibernate, pathLib);
			GeneratorUtil.copyFolder(pathPrimeFaces, pathLib);
			GeneratorUtil.copyFolder(pathSpring, pathLib);
			GeneratorUtil.copyFolder(pathSpringSecurity, pathLib);
			GeneratorUtil.copyFolder(pathSL4J, pathLib);
			GeneratorUtil.copyFolder(pathJamon, pathLib);
			GeneratorUtil.copyFolder(pathMojarra, pathLib);
			GeneratorUtil.copyFolder(pathApacheCommons, pathLib);
			GeneratorUtil.copyFolder(pathAopAlliance, pathLib);
			GeneratorUtil.copyFolder(pathLog4j, pathLib);
			//GeneratorUtil.copyFolder(pathServlet, pathLib);
		}
		
		// copy to Log4j
		String folderProjectPath = properties.getProperty("folderProjectPath");
		GeneratorUtil.copyFolder(log4j, folderProjectPath + GeneratorUtil.slash);
		
	}

	@Override
	public void doTemplate(String hdLocation, MetaDataModel metaDataModel,
			String jpaPckgName, String projectName, Integer specificityLevel,
			String domainName)throws Exception {		

		try {
	
			
			
			ve = new VelocityEngine();
			Properties propiedades = new Properties();
			propiedades.setProperty("file.resource.loader.description", "Velocity File Resource Loader");
			propiedades.setProperty("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.FileResourceLoader");
			propiedades.setProperty("file.resource.loader.path", templatesPath);
			propiedades.setProperty("file.resource.loader.cache", "false");
			propiedades.setProperty("file.resource.loader.modificationCheckInterval", "2");
			ve.init(propiedades);
	
	
			VelocityContext context = new VelocityContext();
			MetaDataModel dataModel = metaDataModel;
			List<MetaData> list = dataModel.getTheMetaData();
	
			IJenderStringBuilderForId stringBuilderForId = new JenderStringBuilderForId(list);
			IJenderStringBuilder stringBuilder = new JenderStringBuilder(list, (JenderStringBuilderForId) stringBuilderForId);
			String packageOriginal = null;
			String virginPackage = null;
			String modelName = null;
	
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
	
			String projectNameClass = (projectName.substring(0, 1)).toUpperCase() + projectName.substring(1, projectName.length());
			context.put("packageOriginal", packageOriginal);
			context.put("virginPackage", virginPackage);
			context.put("package", jpaPckgName);
			context.put("projectName", projectName);
			context.put("modelName", modelName);
			context.put("projectNameClass", projectNameClass);
			context.put("domainName", domainName);
			
			this.virginPackageInHd = GeneratorUtil.replaceAll(virginPackage, ".", GeneratorUtil.slash);
			JenderUtilities.getInstance().buildFolders(virginPackage, hdLocation, specificityLevel, packageOriginal, properties);
			JenderUtilities.getInstance().biuldHashToGetIdValuesLengths(list);
	
			for (MetaData metaData : list) {
	
				List<String> imports = JenderUtilities.getInstance().getRelatedClasses(metaData, dataModel);
	
				if (imports != null) {
					if (imports.size() > 0 && !imports.isEmpty()) {
						context.put("isImports", true);
						context.put("imports", imports);
					} else {
						context.put("isImports", false);
					}
				} else {
					context.put("isImports", false);
				}
	
				// generacion de nuevos dto
				context.put("variableDto", stringBuilder.getPropertiesDto(list, metaData));
				context.put("propertiesDto",JenderUtilities.getInstance().dtoProperties);
				context.put("memberDto",JenderUtilities.getInstance().nameMemberToDto);
				
				// generacion de la entidad
				context.put("properties", metaData.getProperties());
//				context.put("properties", metaData.getProperties().get(0).getType());
				
				// generacion de la nueva logica 
				context.put("dtoConvert", stringBuilderForId.dtoConvert(list,metaData));
				context.put("dtoConvert2", stringBuilder.dtoConvert2(list, metaData));
				
				// generacion de atributos para mapear de Entidad a DTO
				context.put("dtoAttributes", stringBuilderForId.obtainDTOMembersAndSetEntityAttributes(list, metaData));
				context.put("dtoAttributes2", stringBuilder.obtainDTOMembersAndSetEntityAttributes2(list, metaData));
				
				// generacion de los atributos para mapear de DTO a Entidad 
				context.put("entityAttributes", stringBuilderForId.obtainEntityMembersAndSetDTOAttributes(list, metaData));
				context.put("entityAttributes2", stringBuilder.obtainEntityMembersAndSetDTOAttributes2(list, metaData));
				
				context.put("finalParamForView", stringBuilder.finalParamForView(list, metaData));
				context.put("finalParamForDtoUpdate", stringBuilder.finalParamForDtoUpdate(list, metaData));
				context.put("finalParamForDtoUpdateOnlyVariables", stringBuilder.finalParamForDtoUpdateOnlyVariables(list, metaData));
				context.put("finalParamForViewVariablesInList", stringBuilder.finalParamForViewVariablesInList(list, metaData));
				context.put("isFinalParamForViewDatesInList", JenderUtilities.getInstance().isFinalParamForViewDatesInList());
				context.put("finalParamForViewDatesInList", JenderUtilities.getInstance().dates);
				context.put("finalParamForIdForViewVariablesInList", stringBuilderForId.finalParamForIdForViewVariablesInList(list, metaData));
				context.put("isFinalParamForIdForViewDatesInList", JenderUtilities.getInstance().isFinalParamForIdForViewDatesInList());
				context.put("finalParamForIdForViewDatesInList", JenderUtilities.getInstance().datesId);
				context.put("finalParamForIdForViewClass", stringBuilderForId.finalParamForIdForViewClass(list, metaData));
				context.put("finalParamForIdClassAsVariablesAsString", stringBuilderForId.finalParamForIdClassAsVariablesAsString(list, metaData));
				context.put("finalParamForViewForSetsVariablesInList", stringBuilder.finalParamForViewForSetsVariablesInList(list, metaData));
				context.put("finalParamForIdForDtoForSetsVariablesInList", stringBuilderForId.finalParamForIdForDtoForSetsVariablesInList(list, metaData));
				context.put("finalParamForDtoForSetsVariablesInList", stringBuilder.finalParamForDtoForSetsVariablesInList(list, metaData));
				context.put("finalParamForIdForDtoInViewForSetsVariablesInList", stringBuilderForId.finalParamForIdForDtoInViewForSetsVariablesInList(list,	metaData));
				context.put("finalParamForDtoInViewForSetsVariablesInList", stringBuilder.finalParamForDtoInViewForSetsVariablesInList(list, metaData));
				context.put("finalParamForIdForViewForSetsVariablesInList", stringBuilderForId.finalParamForIdForViewForSetsVariablesInList(list, metaData));
				context.put("finalParamForIdVariablesAsList", stringBuilderForId.finalParamForIdVariablesAsList(list, metaData));
				
				//listas nuevas para el manejo de tablas maestro detalle AndresPuerta
				context.put("finalParamForGetIdForViewClass", stringBuilder.finalParamForGetIdForViewClass(list, metaData));				
				context.put("finalParamForGetIdByDtoForViewClass", stringBuilder.finalParamForGetIdByDtoForViewClass(list, metaData));
				context.put("finalParamForIdForViewForSetsVariablesDtoInList", stringBuilderForId.finalParamForIdForViewForSetsVariablesDtoInList(list, metaData));
				context.put("finalParamForViewForSetsVariablesDtoInList", stringBuilder.finalParamForViewForSetsVariablesDtoInList(list, metaData));
				context.put("finalParamForGetManyToOneForViewClass", stringBuilder.finalParamForGetManyToOneForViewClass(list, metaData));
				
	
				String finalParam = stringBuilder.finalParam(list, metaData);
				context.put("finalParam", finalParam);
				metaData.setFinamParam(finalParam);
	
				String finalParamVariables = stringBuilder.finalParamVariables(list, metaData);
				context.put("finalParamVariables", finalParamVariables);
				metaData.setFinamParamVariables(finalParamVariables);
	
				String finalParamForId = stringBuilderForId.finalParamForId(list, metaData);
				context.put("finalParamForId", stringBuilderForId.finalParamForId(list, metaData));
				metaData.setFinalParamForId(finalParamForId);
	
				String finalParamForIdVariables = stringBuilderForId.finalParamForIdVariables(list, metaData);
				context.put("finalParamForIdVariables", stringBuilderForId.finalParamForIdVariables(list, metaData));
				metaData.setFinalParamForIdVariables(finalParamForIdVariables);
	
				context.put("finalParamVariablesAsList", stringBuilder.finalParamVariablesAsList(list, metaData));
				context.put("finalParamVariablesAsList2", stringBuilder.finalParamVariablesAsList2(list, metaData));
				context.put("finalParamVariablesDatesAsList2", stringBuilder.finalParamVariablesDatesAsList2(list, metaData));
				context.put("isFinalParamDatesAsList", JenderUtilities.getInstance().isFinalParamDatesAsList());
				context.put("finalParamDatesAsList", JenderUtilities.getInstance().datesJSP);
	
				context.put("finalParamForIdClassAsVariables", stringBuilderForId.finalParamForIdClassAsVariables(list, metaData));
				context.put("finalParamForIdClassAsVariables2", stringBuilderForId.finalParamForIdClassAsVariables2(list, metaData));
				context.put("isFinalParamForIdClassAsVariablesForDates", JenderUtilities.getInstance().isFinalParamForIdClassAsVariablesForDates());
				context.put("finalParamForIdClassAsVariablesForDates", JenderUtilities.getInstance().datesIdJSP);
	
				context.put("finalParamForVariablesDataTablesAsList", stringBuilder.finalParamForVariablesDataTablesAsList(list, metaData));
				context.put("finalParamForVariablesDataTablesForIdAsList", stringBuilderForId.finalParamForVariablesDataTablesForIdAsList(list, metaData));
	
				if (metaData.isGetManyToOneProperties()) {
					context.put("getVariableForManyToOneProperties", stringBuilder.getVariableForManyToOneProperties(metaData.getManyToOneProperties(), list));
					context.put("getStringsForManyToOneProperties", stringBuilder.getStringsForManyToOneProperties(metaData.getManyToOneProperties(), list));
				}
	
				context.put("composedKey", false);
	
				if (metaData.getPrimaryKey().isPrimiaryKeyAComposeKey()) {
					context.put("composedKey", true);
					context.put("finalParamForIdClass", stringBuilderForId.finalParamForIdClass(list, metaData));
				}
				context.put("metaData", metaData);
				context.put("dataModel", dataModel);
				
				
				
				// generacion datasource.xml
				context.put("connectionUrl", EclipseGeneratorUtil.connectionUrl);
				context.put("connectionDriverClass", EclipseGeneratorUtil.connectionDriverClass);
				context.put("connectionUsername", EclipseGeneratorUtil.connectionUsername);
				context.put("connectionPassword", EclipseGeneratorUtil.connectionPassword);
	
				doEntityGenerator(metaData, context, hdLocation, dataModel);
				doDaoSpringHibernate(metaData, context, hdLocation);
				doBackingBeans(metaData, context, hdLocation, dataModel);
				doJsp(metaData, context, hdLocation, dataModel);
				doLogicSpringXMLHibernate(metaData, context, hdLocation, dataModel, modelName);
				doDto(metaData, context, hdLocation, dataModel, modelName);
				doRestControllers(metaData, context, hdLocation, dataModel);
				doDTOMapper(metaData, context, hdLocation, dataModel);
				
			}
	
			if (EclipseGeneratorUtil.isMavenProject) {
				GeneratorUtil.doPomXml(context, ve);
			}
			
			doApiSpringHibernate(context, hdLocation);
			doUtilites(context, hdLocation, dataModel, modelName);
			doAuthenticationProvider(context, hdLocation, dataModel, modelName);
			doExceptions(context, hdLocation);
			doBusinessDelegator(context, hdLocation, dataModel);
			doJspInitialMenu(dataModel, context, hdLocation);
			doFacesConfig(dataModel, context, hdLocation);
			doJspFacelets(context, hdLocation);
			doSpringContextConfFiles(context, hdLocation, dataModel, modelName);			
		
		} catch (Exception e) {
			log.error(e.toString());
			throw e;
		}finally{
			
		}
	}

	@Override
	public void doBusinessDelegator(VelocityContext context, String hdLocation,
			MetaDataModel dataModel) throws Exception{
		try {
			String path = hdLocation + virginPackageInHd + GeneratorUtil.slash + "presentation"+ GeneratorUtil.slash + "businessDelegate" +GeneratorUtil.slash;
			
			log.info("Begin IBusinesDelegate ");
			Template templateIBusinessDelegate = ve.getTemplate("IBusinessDelegatorView.vm");
			StringWriter swIBusinessDelegate = new StringWriter();
			templateIBusinessDelegate.merge(context, swIBusinessDelegate);
			
			FileWriter fwIBusinessDelegate = new FileWriter(path+"IBusinessDelegatorView.java");
			BufferedWriter bwIBusinessDelegate = new  BufferedWriter(fwIBusinessDelegate);
			bwIBusinessDelegate.write(swIBusinessDelegate.toString());
			bwIBusinessDelegate.close();
			fwIBusinessDelegate.close();
			log.info("End IBusinesDelegate ");
			
			log.info("Begin BusinesDelegate ");
			Template templateBusinessDelegate = ve.getTemplate("BusinessDelegatorView.vm");
			StringWriter swBusinessDelegate = new StringWriter();
			templateBusinessDelegate.merge(context, swBusinessDelegate);
			FileWriter fwBusinessDelegate = new FileWriter(path + "BusinessDelegatorView.java");
			BufferedWriter bwBusinessDelegate = new BufferedWriter(fwBusinessDelegate);
			bwBusinessDelegate.write(swBusinessDelegate.toString());
			bwBusinessDelegate.close();
			fwBusinessDelegate.close();
			
			JalopyCodeFormatter.formatJavaCodeFile(path + "IBusinessDelegatorView.java");
			JalopyCodeFormatter.formatJavaCodeFile(path + "BusinessDelegatorView.java");
			
		} catch (Exception e) {
			log.error(e.toString());
			throw e;
		}

	}

	@Override
	public void doDaoSpringHibernate(MetaData metaData,
			VelocityContext context, String hdLocation) throws Exception{

		try {

			String path=hdLocation + virginPackageInHd + GeneratorUtil.slash + "dataaccess" + GeneratorUtil.slash + "dao"+ GeneratorUtil.slash;

			log.info("Begin Idao Spring+PrimeFaces+Hibernate");
			Template idaoSpringPrimeTemplate = ve.getTemplate("IDAOSpringHibernatePrime.vm");
			StringWriter swIdaoPrime = new StringWriter();
			idaoSpringPrimeTemplate.merge(context, swIdaoPrime);
			FileWriter fwIdao = new FileWriter(path+ "I" +metaData.getRealClassName()+"DAO.java");
			BufferedWriter bwIdao = new BufferedWriter(fwIdao);
			bwIdao.write(swIdaoPrime.toString());
			bwIdao.close();
			fwIdao.close();
			log.info("End Idao Spring+PrimeFaces+Hibernate");

			log.info("Begin Dao Spring+PrimeFaces+Hibernate");
			Template daoSpringPrime = ve.getTemplate("DAOSpringHibernatePrime.vm");
			StringWriter swDao = new StringWriter();
			daoSpringPrime.merge(context, swDao);
			FileWriter fwDao = new FileWriter(path+ metaData.getRealClassName()+"DAO.java");
			BufferedWriter bwDao = new BufferedWriter(fwDao);
			bwDao.write(swDao.toString());
			bwDao.close();
			fwDao.close();
			log.info("End Dao Spring+PrimeFaces+Hibernate");
			
			JalopyCodeFormatter.formatJavaCodeFile(path + "I" + metaData.getRealClassName() + "DAO.java");
			JalopyCodeFormatter.formatJavaCodeFile(path + metaData.getRealClassName() + "DAO.java");
			
		} catch (Exception e) {
			log.error(e.toString());
			throw e;
		}

	}
	
	@Override
	public void doApiSpringHibernate(VelocityContext context, String hdLocation) throws Exception{

		try {

			String path=hdLocation + virginPackageInHd + GeneratorUtil.slash + "dataaccess" + GeneratorUtil.slash + "api"+ GeneratorUtil.slash;

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
			
			apiSpringPrimeHibernateTemplate = ve.getTemplate("HibernateDaoImpl.vm");
			stringWriter = new StringWriter();
			apiSpringPrimeHibernateTemplate.merge(context, stringWriter);
			fileWriter = new FileWriter(path+"HibernateDaoImpl.java");
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
			JalopyCodeFormatter.formatJavaCodeFile(path + "HibernateDaoImpl.java");
			JalopyCodeFormatter.formatJavaCodeFile(path + "Paginator.java");
			
		} catch (Exception e) {
			log.error(e.toString());
			throw e;
		}

	}

	@Override
	public void doDto(MetaData metaData, VelocityContext context,
			String hdLocation, MetaDataModel dataModel, String modelName) throws Exception{
		try {

			Template dtoTemplate = ve.getTemplate("DtoSpringHibernatePrime.vm");
			StringWriter swDto = new StringWriter();
			dtoTemplate.merge(context, swDto);

			String path=hdLocation + virginPackageInHd + GeneratorUtil.slash + modelName + GeneratorUtil.slash + "dto"+ GeneratorUtil.slash;
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
	public void doExceptions(VelocityContext context, String hdLocation) throws Exception{
		try {
			String path = hdLocation + virginPackageInHd + GeneratorUtil.slash + "exceptions" + GeneratorUtil.slash;
			log.info("Begin ZMessManager");
			Template templateZmessManager = ve.getTemplate("ZMessManager.vm");
			StringWriter swZmessManager = new StringWriter();
			templateZmessManager.merge(context, swZmessManager);
			
			FileWriter fwZmessManager = new FileWriter(path+ "ZMessManager.java");
			BufferedWriter bwZmessManager = new BufferedWriter(fwZmessManager);
			bwZmessManager.write(swZmessManager.toString());
			bwZmessManager.close();
			fwZmessManager.close();
			log.info("Begin ZMessManager");
			
		} catch (Exception e) {
			log.error(e.toString());
			throw e;
		}

	}

	@Override
	public void doFacesConfig(MetaDataModel dataModel, VelocityContext context,
			String hdLocation) throws Exception{
		try {
			
			String path =properties.getProperty("webRootFolderPath")+"WEB-INF"+ GeneratorUtil.slash;
			
			log.info("Begin FacesConfig");
			Template templateFacesConfig = ve.getTemplate("faces-configSpringPrimeHibernate.xml.vm");
			StringWriter swFacesConfig = new StringWriter();
			templateFacesConfig.merge(context, swFacesConfig);
			FileWriter fwFacesConfig = new FileWriter(path+"faces-config.xml");
			BufferedWriter bwFacesConfig = new BufferedWriter(fwFacesConfig);
			bwFacesConfig.write(swFacesConfig.toString());
			bwFacesConfig.close();
			fwFacesConfig.close();
			log.info("End FacesConfig");
			
		} catch (Exception e) {
			log.error(e.toString());
			throw e;
		}

	}

	@Override
	public void doJsp(MetaData metaData, VelocityContext context,
			String hdLocation, MetaDataModel dataModel) throws Exception{
		try {
			
			String path=properties.getProperty("webRootFolderPath") + "XHTML" + GeneratorUtil.slash;
			log.info("Begin Crud XHTML");
			Template templateXhtml = ve.getTemplate("XHTMLSpringHibernatePrime.vm");
			StringWriter swXhtml= new StringWriter();
			templateXhtml.merge(context, swXhtml);
			FileWriter fwXhtml = new FileWriter(path+metaData.getRealClassNameAsVariable()+".xhtml");
			BufferedWriter bwXhtml = new BufferedWriter(fwXhtml);
			bwXhtml.write(swXhtml.toString());
			bwXhtml.close();
			fwXhtml.close();
			log.info("End Crud XHTML");
			
			log.info("Begin DataTable XHTML");
			Template templateDataTable = ve.getTemplate("XHTMLdataTablesHibernatePrime.vm");
			StringWriter swDataTable = new StringWriter();
			templateDataTable.merge(context, swDataTable);
			FileWriter fwDataTable = new FileWriter(path+metaData.getRealClassNameAsVariable()+"ListDataTable.xhtml");
			BufferedWriter bwDataTable = new BufferedWriter(fwDataTable);
			bwDataTable.write(swDataTable.toString());
			bwDataTable.close();
			fwDataTable.close();
			JenderUtilities.getInstance().datesJSP = null;
			JenderUtilities.getInstance().datesIdJSP = null;
			log.info("End DataTable XHTML");
			
			log.info("Begin DataTableEditable XHTML");
			Template templateDataTableEditable = ve.getTemplate("XHTMLdataTableEditablePrime.vm");
			StringWriter swDataTableEditable = new StringWriter();
			templateDataTableEditable.merge(context, swDataTableEditable);
			FileWriter fwDataTableEditable = new FileWriter(path+ metaData.getRealClassNameAsVariable()+"ListDataTableEditable.xhtml");
			BufferedWriter bwDataTableEditable = new BufferedWriter(fwDataTableEditable);
			bwDataTableEditable.write(swDataTableEditable.toString());
			bwDataTableEditable.close();
			fwDataTableEditable.close();
			log.info("Begin DataTableEditable XHTML");
			
			JenderUtilities.getInstance().datesJSP = null;
			JenderUtilities.getInstance().datesIdJSP = null;
			
		} catch (Exception e) {
			log.error(e.toString());
			throw e;
		}

	}

	@Override
	public void doJspFacelets(VelocityContext context, String hdLocation) throws Exception{
		try {
			log.info("Begin Header");
			String pathFacelets = properties.getProperty("webRootFolderPath") + "WEB-INF" + GeneratorUtil.slash + "facelets" + GeneratorUtil.slash;
			Template templateHeader = ve.getTemplate("JSPheader.vm");
			StringWriter swHeader = new StringWriter();
			templateHeader.merge(context, swHeader);
			FileWriter fwHeader = new FileWriter(pathFacelets+"header.jspx");
			BufferedWriter bwHeader = new BufferedWriter(fwHeader);
			bwHeader.write(swHeader.toString());
			bwHeader.close();
			fwHeader.close();
			log.info("End Header");
			
			log.info("Begin Footer");
			Template templateFooter = ve.getTemplate("JSPfooter.vm");
			StringWriter swFooter = new StringWriter();
			templateFooter.merge(context, swFooter);
			FileWriter fwFooter = new FileWriter(pathFacelets+"footer.jspx");
			BufferedWriter bwFooter = new BufferedWriter(fwFooter);
			bwFooter.write(swFooter.toString());
			bwFooter.close();
			fwFooter.close();
			log.info("End Footer");
			

			log.info("Begin menu");
			Template templateCommonsColumns = ve.getTemplate("menu.vm");
			StringWriter swCommonColumns = new StringWriter();
			templateCommonsColumns.merge(context, swCommonColumns);
			FileWriter fwCommonColumns = new FileWriter(pathFacelets+"menu.jspx");
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
	public void doJspInitialMenu(MetaDataModel dataModel,
			VelocityContext context, String hdLocation) throws Exception{
		try {
			String path=properties.getProperty("webRootFolderPath") + "XHTML" + GeneratorUtil.slash;
			log.info("Begin Initial  XHTML");
			Template templateInitialMenu = ve.getTemplate("XHTMLinitialMenu.vm");
			StringWriter swInitialMenu = new StringWriter();
			templateInitialMenu.merge(context, swInitialMenu);
			FileWriter fwInitialMenu = new FileWriter(path+"initialMenu.xhtml");
			BufferedWriter bwInitialMenu = new BufferedWriter(fwInitialMenu);
			bwInitialMenu.write(swInitialMenu.toString());
			bwInitialMenu.close();
			fwInitialMenu.close();
			log.info("End Initial  XHTML");
			
		} catch (Exception e) {
			log.error(e.toString());
			throw e;
		}

	}

	@Override
	public void doLogicSpringXMLHibernate(MetaData metaData,
			VelocityContext context, String hdLocation,
			MetaDataModel dataModel, String modelName) throws Exception{
		try {
			String path=hdLocation + virginPackageInHd + GeneratorUtil.slash + modelName + GeneratorUtil.slash +"control" + GeneratorUtil.slash;
			
			log.info("Begin Ilogic PrimeFaces+Spring+Hibernate");
			Template iLogicPrimeFaces = ve.getTemplate("ILogicSpringHibernatePrimeFaces.vm");
			StringWriter swIlogic = new StringWriter();
			iLogicPrimeFaces.merge(context, swIlogic);
			FileWriter fwIlogic = new FileWriter(path+"I"+ metaData.getRealClassName()+"Logic.java");
			BufferedWriter bwIlogic = new BufferedWriter(fwIlogic);
			bwIlogic.write(swIlogic.toString());
			bwIlogic.close();
			fwIlogic.close();
			log.info("End Ilogic PrimeFaces+Spring+Hibernate");
			
			log.info("Begin Logic PrimeFaces+Spring+Hibernate");
			Template LogicTemplate = ve.getTemplate("LogicSpringHibernatePrimeFaces.vm");
			StringWriter swLogic = new StringWriter();
			LogicTemplate.merge(context, swLogic);
			
			FileWriter fwLogic = new FileWriter(path+ metaData.getRealClassName() + "Logic.java");
			BufferedWriter bwLogic = new BufferedWriter(fwLogic);
			bwLogic.write(swLogic.toString());
			bwLogic.close();
			fwLogic.close();
			log.info("End Logic PrimeFaces+Spring+Hibernate");
			
			JalopyCodeFormatter.formatJavaCodeFile(path+"I" + metaData.getRealClassName() + "Logic.java");
			JalopyCodeFormatter.formatJavaCodeFile(path+metaData.getRealClassName() + "Logic.java");
			
		} catch (Exception e) {
			log.error(e.toString());
			throw e;
		}

	}

	@Override
	public void doSpringContextConfFiles(VelocityContext context,
			String hdLocation, MetaDataModel dataModel, String modelName) throws Exception{
		try {
			
			log.info("Begin ApplicationContext.xml");
			Template applicationContext = ve.getTemplate("applicationContext.xml.vm");
			StringWriter swApplicationContext = new StringWriter();
			applicationContext.merge(context, swApplicationContext);
			FileWriter fwApplicationContext = new  FileWriter(hdLocation+"applicationContext.xml");
			BufferedWriter bwApplicationContext = new BufferedWriter(fwApplicationContext);
			bwApplicationContext.write(swApplicationContext.toString());
			bwApplicationContext.close();
			fwApplicationContext.close();
			log.info("End ApplicationContext.xml");
			
			log.info("Begin aopContext.xml");
			Template aopContext= ve.getTemplate("aopContext.xml.vm");
			StringWriter swAopContext = new StringWriter();
			aopContext.merge(context, swAopContext);
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
			
			
			log.info("Begin dataSourceContext.xml");
			Template dataSourceContext= ve.getTemplate("dataSourceContext.xml.vm");
			StringWriter swDataSourceContext = new StringWriter();
			dataSourceContext.merge(context, swDataSourceContext);
			FileWriter fwDataSourceContext = new FileWriter(hdLocation+"dataSourceContext.xml");
			BufferedWriter bwDataSourceContext = new BufferedWriter(fwDataSourceContext);
			bwDataSourceContext.write(swDataSourceContext.toString());
			bwDataSourceContext.close();
			fwDataSourceContext.close();
			log.info("End dataSourceContext.xml");
			
		} catch (Exception e) {
			log.error(e.toString());
			throw e;
		}

	}



	@Override
	public void doUtilites(VelocityContext context, String hdLocation,MetaDataModel dataModel, String modelName) throws Exception{
		try {
			String path =hdLocation+virginPackageInHd+GeneratorUtil.slash+"utilities"+GeneratorUtil.slash;
			
			log.info("Begin Utilities Spring+Hibernate+PrimeFaces");
			Template utilitiesTemplate = ve.getTemplate("Utilities.vm");
			StringWriter swUtilities = new StringWriter();
			utilitiesTemplate.merge(context, swUtilities);
			FileWriter fwUtilities = new FileWriter(path+"Utilities.java");
			BufferedWriter bwUtilities = new BufferedWriter(fwUtilities);
			bwUtilities.write(swUtilities.toString());
			bwUtilities.close();
			fwUtilities.close();
			log.info("End Utilities Spring+Hibernate+PrimeFaces");
			
			log.info("Begin FacesUtils Spring+Hibernate+PrimeFaces");
			Template templateFacesUtils = ve.getTemplate("FacesUtils.vm");
			StringWriter swFacesUtils = new StringWriter();
			templateFacesUtils.merge(context, swFacesUtils);
			FileWriter fwFacesUtils = new FileWriter(path+"FacesUtils.java");
			BufferedWriter bfFacesUtils = new BufferedWriter(fwFacesUtils);
			bfFacesUtils.write(swFacesUtils.toString());
			bfFacesUtils.close();
			fwFacesUtils.close();
			log.info("End FacesUtils Spring+Hibernate+PrimeFaces");
			
			
		} catch (Exception e) {
			log.error(e.toString());
			throw e;
		}

	}


	@Override
	public void doBackingBeans(MetaData metaData, VelocityContext context,String hdLocation, MetaDataModel dataModel) throws Exception{
		try {
			
			String path =hdLocation + virginPackageInHd + GeneratorUtil.slash + "presentation" + GeneratorUtil.slash + "backingBeans" + GeneratorUtil.slash;
			
			log.info("Begin BackEndBean");
			Template templateBakcEndBean= ve.getTemplate("BackingBeansSpringHibernatePrime.vm");
			StringWriter swBackEndBean = new StringWriter();
			templateBakcEndBean.merge(context, swBackEndBean);
			
			FileWriter fwBackEndBean = new FileWriter(path+ metaData.getRealClassName() + "View.java");
			BufferedWriter bwBackEndBean = new BufferedWriter(fwBackEndBean);
			bwBackEndBean.write(swBackEndBean.toString());
			bwBackEndBean.close();
			fwBackEndBean.close();
			log.info("Begin BackEndBean");
			JalopyCodeFormatter.formatJavaCodeFile(path + metaData.getRealClassName() + "View.java");
			JenderUtilities.getInstance().dates = null;
			JenderUtilities.getInstance().datesId = null;
			
			
		} catch (Exception e) {
			log.error(e.toString());
			throw e;
		}

	}

	@Override
	public void doAuthenticationProvider(VelocityContext context, String hdLocation,MetaDataModel dataModel, String modelName) throws Exception{
		
		try {
			
			String path =hdLocation + virginPackageInHd + GeneratorUtil.slash + "security" + GeneratorUtil.slash;
			
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
			JenderUtilities.getInstance().dates = null;
			JenderUtilities.getInstance().datesId = null;
			
			
			//ManageBean for LoginView
			path =hdLocation + virginPackageInHd + GeneratorUtil.slash + "presentation" + GeneratorUtil.slash + "backingBeans" + GeneratorUtil.slash;
			
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
			JenderUtilities.getInstance().dates = null;
			JenderUtilities.getInstance().datesId = null;
			
			
		} catch (Exception e) {
			log.error(e.toString());
			throw e;
		}
	}		
	
	@Override
	public void doRestControllers(MetaData metaData, VelocityContext context,String hdLocation, MetaDataModel dataModel) throws Exception{
		try {
			
			String path = hdLocation + virginPackageInHd + GeneratorUtil.slash + "rest" + GeneratorUtil.slash + "controllers" + GeneratorUtil.slash;
			
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
	public void doDTOMapper(MetaData metaData, VelocityContext context,String hdLocation, MetaDataModel dataModel) throws Exception{
		try {
			
			String path = hdLocation + virginPackageInHd + GeneratorUtil.slash + "dto" + GeneratorUtil.slash + "mapper" + GeneratorUtil.slash;
			
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
	public void doEntityGenerator(MetaData metaData, VelocityContext context,String hdLocation, MetaDataModel dataModel) throws Exception{
		try {
			
			String path = hdLocation + virginPackageInHd + GeneratorUtil.slash + "test" + GeneratorUtil.slash;
			
			log.info("Begin Entity Generator");
			
			Template templateEntity = ve.getTemplate("EntityGenerator.vm");
			StringWriter swEntity = new StringWriter();
			templateEntity.merge(context, swEntity);
			
			FileWriter fwEntity = new FileWriter(path + "T" + metaData.getRealClassName() + ".java");
			BufferedWriter bwEntity = new BufferedWriter(fwEntity);
			bwEntity.write(swEntity.toString());
			bwEntity.close();
			fwEntity.close();
			
			if (metaData.getComposeKey() != null) {
				Template templateEntityComposeKey = ve.getTemplate("EntityIdGenerator.vm");
				StringWriter swEntityComposeKey = new StringWriter();
				templateEntityComposeKey.merge(context, swEntityComposeKey);
				
				FileWriter fwEntityComposeKey = new FileWriter(path + "T" + metaData.getRealClassName() + "Id" + ".java");
				BufferedWriter bwEntityComposeKey = new BufferedWriter(fwEntityComposeKey);
				bwEntityComposeKey.write(swEntityComposeKey.toString());
				bwEntityComposeKey.close();
				fwEntityComposeKey.close();
				
				JalopyCodeFormatter.formatJavaCodeFile(path + "T" + metaData.getRealClassName() + "Id" + ".java");
			}
			
			
			JalopyCodeFormatter.formatJavaCodeFile(path + "T" + metaData.getRealClassName() + ".java");
			
					
		} catch (Exception e) {
			log.error(e.toString());
			throw e;
		}

	}
		
}
