package org.zcode.generator.robot.wallj;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import javax.persistence.Basic;
import javax.persistence.Column;

import org.zcode.eclipse.plugin.generator.utilities.EclipseGeneratorUtil;
import org.zcode.generator.utilities.GeneratorUtil;
import org.zcode.metadata.model.Member;
import org.zcode.metadata.model.MetaData;
import org.zcode.metadata.model.MetaDataModel;
import org.zcode.metadata.model.SimpleMember;


/**
 * Zathuracode Generator
 * www.zathuracode.org
 * @author Diego Armando Gomez (dgomez@vortexbird.com)
 * @version 1.0
 */
public class WallJUtilities {

	/** The instance. */
	private static WallJUtilities instance = null;

	/**
	 * The Constructor.
	 */
	private WallJUtilities() {
	}

	/**
	 * Gets the instance.
	 *
	 * @return the instance
	 */
	public static WallJUtilities getInstance() {
		if (instance == null) {
			instance = new WallJUtilities();
		}

		return instance;
	}

	/** The length. */
	public Long length;
	
	/** The precision. */
	public Long precision;
	
	/** The scale. */
	public Long scale;
	
	/** The nullable. */
	public Boolean nullable;

	/** The ifcondition. */
	public String ifcondition = "if(";
	
	/** The ifcondition close. */
	public String ifconditionClose = "){";
	
	/** The throw exception null. */
	public String throwExceptionNull = "throw new ZMessManager().new EmptyFieldException(";
	
	/** The throw exception length. */
	public String throwExceptionLength = "throw new ZMessManager().new NotValidFormatException(";
	
	/** The throw exception close. */
	public String throwExceptionClose = ");}";

	/** The dates. */
	public List<String> dates;
	
	/** The dates jsp. */
	public List<String> datesJSP;
	
	/** The dates id. */
	public List<String> datesId;
	
	/** The dates id jsp. */
	public List<String> datesIdJSP;
	
	/** The many to one temp hash. */
	public HashMap<String, Member> manyToOneTempHash;
	
	/** Properties to data type and name member class  @author Mauricio */
	public HashMap<String, String> dtoProperties;
	/** name to member class to generate dto  @author Mauricio */
	public List<String> nameMemberToDto;
	
	// getTypeAndvariableForManyToOneProperties
	/**
	 * Gets the type andvariable for many to one properties.
	 *
	 * @param strClass the str class
	 * @param theMetaData the meta data
	 * @return the type andvariable for many to one properties
	 */
	public String[] getTypeAndvariableForManyToOneProperties(String strClass, List<MetaData> theMetaData) {
		String ret[] = new String[50];

		for (MetaData metaData : theMetaData) {
			if (metaData.getRealClassName().equalsIgnoreCase(strClass)) {

				manyToOneTempHash = metaData.getPrimaryKey().getHashMapIdsProperties();

				if (!metaData.getPrimaryKey().isPrimiaryKeyAComposeKey()) {
					Member member = metaData.getPrimaryKey();
					ret[0] = member.getRealClassName();
					ret[1] = member.getName() + "_" + metaData.getRealClassName();
					// ret[2] = member.getGetNameOfPrimaryName();
					// ret[3] = member.getRealClassName();
				} else {
					int contTmp = 0;
					Field[] field = metaData.getComposeKey().getDeclaredFields();
					for (int i = 0; i < field.length; i++) {

						Field field2 = field[i];

						String name = field2.getName();

						String realType = field2.getType().toString().substring((field2.getType().toString()).lastIndexOf(".") + 1,
								(field2.getType().toString()).length());

						ret[contTmp] = realType;
						contTmp++;
						ret[contTmp] = name + "_" + metaData.getRealClassName();
						contTmp++;
					}
				}
			}
		}

		boolean watch = false;

		for (int j = 0; j < ret.length; j++) {
			if (ret[j] != null) {
				if (!ret[j].equalsIgnoreCase(""))
					watch = true;
			}
		}

		if (watch) {
			return ret;
		} else {
			return null;
		}

	}

	/**
	 * Adds the variables values to list depending on data type for id.
	 *
	 * @param finalParam2 the final param2
	 * @param field the field
	 * @param variableName the variable name
	 * @param clazz the clazz
	 * @return the list< string>
	 */
	public List<String> addVariablesValuesToListDependingOnDataTypeForID(List<String> finalParam2, Field field, String variableName, String methodAccesorName, Class clazz) {

		String realClassName = field.getType().getSimpleName();

		String variableNameFormethod = variableName.substring(0, 1).toUpperCase() + variableName.substring(1);

		buildStringToCheckLengths(field, clazz, variableName);

		if (realClassName.equalsIgnoreCase("String")) {
			if (!length.equals("0"))
				finalParam2.add(ifcondition + methodAccesorName + "!=null && " + "Utilities.checkWordAndCheckWithlength(" + methodAccesorName + "," + length + ")==false"
						+ ifconditionClose + throwExceptionLength + "\"" + variableName + "\"" + throwExceptionClose);

		}

		if (realClassName.equalsIgnoreCase("Integer")) {
			if (!precision.equals("0"))
				finalParam2.add(ifcondition + methodAccesorName + "!=null && " + "Utilities.checkNumberAndCheckWithPrecisionAndScale(\"\"+" + methodAccesorName + ","
						+ precision + "," + 0 + ")==false" + ifconditionClose + throwExceptionLength + "\"" + variableName + "\"" + throwExceptionClose);
		}

		if (realClassName.equalsIgnoreCase("Double")) {
			if (!precision.equals("0"))
				finalParam2.add(ifcondition + methodAccesorName + "!=null && " + "Utilities.checkNumberAndCheckWithPrecisionAndScale(\"\"+" + methodAccesorName + ","
						+ precision + "," + scale + ")==false" + ifconditionClose + throwExceptionLength + "\"" + variableName + "\"" + throwExceptionClose);
		}

		if (realClassName.equalsIgnoreCase("Long")) {
			if (!precision.equals("0"))
				finalParam2.add(ifcondition + methodAccesorName + "!=null && " + "Utilities.checkNumberAndCheckWithPrecisionAndScale(\"\"+" + methodAccesorName + ","
						+ precision + "," + 0 + ")==false" + ifconditionClose + throwExceptionLength + "\"" + variableName + "\"" + throwExceptionClose);
		}

		if (realClassName.equalsIgnoreCase("Float")) {
			if (!precision.equals("0"))
				finalParam2.add(ifcondition + methodAccesorName + "!=null && " + "Utilities.checkNumberAndCheckWithPrecisionAndScale(\"\"+" + methodAccesorName + ","
						+ precision + "," + scale + ")==false" + ifconditionClose + throwExceptionLength + "\"" + variableName + "\"" + throwExceptionClose);
		}

		return finalParam2;
	}

	/**
	 * Builds the string to check lengths.
	 *
	 * @param field the field
	 * @param clazz the clazz
	 * @param realClassName the real class name
	 */
	public void buildStringToCheckLengths(Field field, Class clazz, String realClassName) {

		if (field.getAnnotations() != null && field.getAnnotations().length > 0) {
			if (field.getAnnotation(Column.class) != null) {
				Column column = field.getAnnotation(Column.class);
				length = new Long(column.length());
				precision = new Long(column.precision());
				scale = new Long(column.scale());
				nullable = new Boolean(column.nullable());

			} else {
				if (field.getAnnotation(Basic.class) != null) {
					nullable = new Boolean(field.getAnnotation(Basic.class).optional());
				}
			}
		} else {
			for (Method method : clazz.getDeclaredMethods()) {

				if (method.getAnnotation(Column.class) != null) {
					String property = new String();

					if (method.getName().startsWith("get")) {
						property = method.getName().substring(3);

						if (realClassName.equalsIgnoreCase(property)) {
							Column column = method.getAnnotation(Column.class);
							length = new Long(column.length());
							precision = new Long(column.precision());
							scale = new Long(column.scale());
							nullable = new Boolean(column.nullable());

							break;
						}
					} else {
						if (method.getName().startsWith("is")) {
							property = method.getName().substring(3);

							if (realClassName.equalsIgnoreCase(property)) {
								Column column = method.getAnnotation(Column.class);
								length = new Long(column.length());
								precision = new Long(column.precision());
								scale = new Long(column.scale());
								nullable = new Boolean(column.nullable());

								break;
							}
						}
					}
				} else {
					if (method.getAnnotation(Basic.class) != null) {
						nullable = new Boolean(method.getAnnotation(Basic.class).optional());
					}
				}
			}
		}

	}

	/**
	 * Adds the variables values to list depending on data type.
	 *
	 * @param finalParam2 the final param2
	 * @param realClassName the real class name
	 * @param variableName the variable name
	 * @param precision the precision
	 * @param scale the scale
	 * @param length the length
	 * @return the list< string>
	 */
	public List<String> addVariablesValuesToListDependingOnDataType(List<String> finalParam2, String realClassName, String variableName, String methodAccesorName, String precision,
			String scale, String length) {

		if (realClassName.equalsIgnoreCase("String")) {
			if (!length.equals("0"))
				finalParam2.add(ifcondition + methodAccesorName + "!=null && " + "Utilities.checkWordAndCheckWithlength(" + methodAccesorName + "," + length + ")==false"
						+ ifconditionClose + throwExceptionLength + "\"" + variableName + "\"" + throwExceptionClose);

		}

		if (realClassName.equalsIgnoreCase("Integer")) {
			if (!precision.equals("0"))
				finalParam2.add(ifcondition + methodAccesorName + "!=null && " + "Utilities.checkNumberAndCheckWithPrecisionAndScale(\"\"+" + methodAccesorName + ","
						+ precision + "," + 0 + ")==false" + ifconditionClose + throwExceptionLength + "\"" + variableName + "\"" + throwExceptionClose);
		}

		if (realClassName.equalsIgnoreCase("Double")) {
			if (!precision.equals("0"))
				finalParam2.add(ifcondition + methodAccesorName + "!=null && " + "Utilities.checkNumberAndCheckWithPrecisionAndScale(\"\"+" + methodAccesorName + ","
						+ (new Integer(precision) - new Integer(scale)) + "," + scale + ")==false" + ifconditionClose + throwExceptionLength + "\""
						+ variableName + "\"" + throwExceptionClose);
		}

		if (realClassName.equalsIgnoreCase("Long")) {
			if (!precision.equals("0"))
				finalParam2.add(ifcondition + methodAccesorName + "!=null && " + "Utilities.checkNumberAndCheckWithPrecisionAndScale(\"\"+" + methodAccesorName + ","
						+ precision + "," + 0 + ")==false" + ifconditionClose + throwExceptionLength + "\"" + variableName + "\"" + throwExceptionClose);
		}

		if (realClassName.equalsIgnoreCase("Float")) {
			if (!precision.equals("0"))
				finalParam2.add(ifcondition + methodAccesorName + "!=null && " + "Utilities.checkNumberAndCheckWithPrecisionAndScale(\"\"+" + methodAccesorName + ","
						+ (new Integer(precision) - new Integer(scale)) + "," + scale + ")==false" + ifconditionClose + throwExceptionLength + "\""
						+ variableName + "\"" + throwExceptionClose);
		}

		return finalParam2;
	}

	/**
	 * Gets the related classes.
	 *
	 * @param metaData the meta data
	 * @param dataModel the data model
	 * @return the related classes
	 */
	public List<String> getRelatedClasses(MetaData metaData, MetaDataModel dataModel) {
		List<String> imports = null;
		Member member = null;
		imports = new ArrayList<String>();
		if (metaData.getPrimaryKey().isPrimiaryKeyAComposeKey()) {
			imports.add(metaData.getPrimaryKey().getType().getName());
		}
		for (Object object : metaData.getProperties()) {

			if (!(object instanceof SimpleMember)) {

				member = (Member) object;
				imports.add(member.getType().getName());

				getRelatedClasses(dataModel, member, imports);
			} else {
				if (object instanceof SimpleMember) {
					if (((SimpleMember) object).getType().getName().equalsIgnoreCase("java.util.Date")) {
						imports.add(((SimpleMember) object).getType().getName());
					}
				}
			}
		}

		return imports;
	}

	/**
	 * Gets the related classes.
	 *
	 * @param dataModel the data model
	 * @param member the member
	 * @param imports the imports
	 */
	public void getRelatedClasses(MetaDataModel dataModel, Member member, List<String> imports) {

		for (MetaData metaDataInList : dataModel.getTheMetaData()) {
			if (metaDataInList.getRealClassName().equalsIgnoreCase(member.getRealClassName())) {
				if (metaDataInList.getPrimaryKey().isPrimiaryKeyAComposeKey()) {
					imports.add(metaDataInList.getPrimaryKey().getType().getName());
				}
			}
		}

	}

	/**
	 * Checks if is composed key.
	 *
	 * @param type the type
	 * @return true, if checks if is composed key
	 */
	public boolean isComposedKey(Class type) {
		boolean ret = false;

		String firstLetters = type.getName();
		String[] tmp = (firstLetters.replace(".", "%")).split("%");

		if (tmp != null) {
			if ((tmp[0].equalsIgnoreCase("java") && tmp[1].equalsIgnoreCase("lang")) || (tmp[0].equalsIgnoreCase("java") && tmp[1].equalsIgnoreCase("util"))) {
				ret = false;
			} else {
				ret = true;
			}
		}

		return ret;
	}

	/**
	 * Checks if is final param for view dates in list.
	 *
	 * @return true, if checks if is final param for view dates in list
	 */
	public boolean isFinalParamForViewDatesInList() {
		if (WallJUtilities.getInstance().dates != null) {
			if (!WallJUtilities.getInstance().dates.isEmpty() && WallJUtilities.getInstance().dates.size() > 0) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	/**
	 * Checks if is final param for id for view dates in list.
	 *
	 * @return true, if checks if is final param for id for view dates in list
	 */
	public boolean isFinalParamForIdForViewDatesInList() {
		if (WallJUtilities.getInstance().datesId != null) {
			if (!WallJUtilities.getInstance().datesId.isEmpty() && WallJUtilities.getInstance().datesId.size() > 0) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	/**
	 * Checks if is final param for id class as variables for dates.
	 *
	 * @return the object
	 */
	public Object isFinalParamForIdClassAsVariablesForDates() {
		if (WallJUtilities.getInstance().datesIdJSP != null) {
			if (!WallJUtilities.getInstance().datesIdJSP.isEmpty() && WallJUtilities.getInstance().datesIdJSP.size() > 0) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	/**
	 * Checks if is final param dates as list.
	 *
	 * @return the object
	 */
	public Object isFinalParamDatesAsList() {
		if (WallJUtilities.getInstance().datesJSP != null) {
			if (!WallJUtilities.getInstance().datesJSP.isEmpty() && WallJUtilities.getInstance().datesJSP.size() > 0) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	/**
	 * Biuld hash to get id values lengths.
	 *
	 * @param list the list
	 */
	public void biuldHashToGetIdValuesLengths(List<MetaData> list) {

		for (MetaData metaData : list) {

			HashMap<String, Member> hashMapIdsProperties = new HashMap<String, Member>();

			if (metaData.getPrimaryKey().isPrimiaryKeyAComposeKey()) {

				Class clazz = metaData.getComposeKey();
				Field[] field = metaData.getComposeKey().getDeclaredFields();

				for (Field field2 : field) {
					String realClassName = field2.getName();

					// length;
					// precision;
					// scale;
					// nullable;

					WallJUtilities.getInstance().buildStringToCheckLengths(field2, clazz, realClassName);

					Member member = new SimpleMember(field2.getName(), field2.getName(), field2.getType(), -1);
					member.setLength(WallJUtilities.getInstance().length);
					member.setPrecision(WallJUtilities.getInstance().precision);
					member.setScale(WallJUtilities.getInstance().scale);
					member.setNullable(WallJUtilities.getInstance().nullable);

					hashMapIdsProperties.put(field2.getName(), member);

					metaData.getPrimaryKey().setHashMapIdsProperties(hashMapIdsProperties);
				}
			} else {

				Member member = new SimpleMember(metaData.getPrimaryKey().getName(), metaData.getPrimaryKey().getName(), metaData.getPrimaryKey().getType(), -1);

				member.setLength(metaData.getPrimaryKey().getLength());
				member.setPrecision(metaData.getPrimaryKey().getPrecision());
				member.setScale(metaData.getPrimaryKey().getScale());
				member.setNullable(metaData.getPrimaryKey().getNullable());

				hashMapIdsProperties.put(metaData.getPrimaryKey().getName(), member);

				metaData.getPrimaryKey().setHashMapIdsProperties(hashMapIdsProperties);

			}
		}
	}

	/**
	 * Builds the folders.
	 *
	 * @param packageName the package name
	 * @param hardDiskLocation the hard disk location
	 * @param specificityLevel the specificity level
	 * @param packageOriginal the package original
	 * @param properties the properties
	 */
	public void buildFolders(String packageName, String hardDiskLocation, Integer specificityLevel, String packageOriginal, Properties properties) {

		// / se construye paquete
		String pckge = packageName.replace('.', '_') + "_";
		String modelPckg = packageOriginal.replace('.', '_') + "_";

		String dataAcces = pckge + "dataaccess_";
		String model = modelPckg;
		String presentation = pckge + "presentation_";
		String dao = dataAcces + "dao_";
		String api = dataAcces + "api_";

		List<String> folderBuilder = new ArrayList<String>();

		folderBuilder.add(pckge);

		folderBuilder.add(pckge + "exceptions");

		folderBuilder.add(pckge + "utilities");
		
		folderBuilder.add(pckge + "dto_mapper");
		
		folderBuilder.add(pckge + "rest_controllers");
		
		folderBuilder.add(dao);
		
		folderBuilder.add(api);

		//folderBuilder.add(dataAcces + "daoFactory");
		//folderBuilder.add(dataAcces + "sessionFactory");

		folderBuilder.add(model + "control");

		if (specificityLevel.intValue() == 2) {
			folderBuilder.add(model + "pojos");
		}

		folderBuilder.add(model + "dto");
		
		if (EclipseGeneratorUtil.isFrontend) {

			folderBuilder.add(presentation + "backingBeans");
		
		}
		
		folderBuilder.add(presentation + "businessDelegate");

		folderBuilder.add(properties.getProperty("webRootFolderPath"));

		for (String string : folderBuilder) {
			try {
				GeneratorUtil.validateDirectory(string, hardDiskLocation);
			} catch (IOException e) {
				// TODO Poner log4j por si lanza error
				e.printStackTrace();
			}
		}

		try {
			
			GeneratorUtil.validateDirectory("WEB-INF", properties.getProperty("webRootFolderPath"));
			//GeneratorUtil.validateDirectory("JSPX", properties.getProperty("webRootFolderPath"));
			if (EclipseGeneratorUtil.isFrontend) {
				GeneratorUtil.validateDirectory("XHTML", properties.getProperty("webRootFolderPath"));
				GeneratorUtil.validateDirectory("facelets", properties.getProperty("webRootFolderPath") + GeneratorUtil.slash + "WEB-INF");
			}
			
			
			// WEB-INF
			GeneratorUtil.validateDirectory("META-INF", hardDiskLocation);
		} catch (IOException e) {
			// TODO Poner log4j por si lanza error
			e.printStackTrace();
		}

	}

	/**
	 * Gets the get name of primary name.
	 *
	 * @param name the name
	 * @return the gets the name of primary name
	 */
	public String getGetNameOfPrimaryName(String name) {
		String build = name.substring(0, 1).toUpperCase();
		String build2 = name.substring(1);
		return build + build2;
	}

	/**
	 * Gets the real class name.
	 *
	 * @param type the type
	 * @return the real class name
	 */
	public String getRealClassName(String type) {
		String typeComplete = type;
		String[] tmp = (typeComplete.replace(".", "%")).split("%");
		String realName = tmp[tmp.length - 1];
		return realName;
	}
	
	public String camelCaseToUnderScore(String name) {

		String replaceString=name.replaceAll("([A-Z]+)","\\_$1").toLowerCase(); 

		return replaceString;
	}

}
