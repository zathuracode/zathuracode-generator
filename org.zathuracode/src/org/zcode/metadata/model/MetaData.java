package org.zcode.metadata.model;

import java.util.ArrayList;
import java.util.List;

// TODO: Auto-generated Javadoc
/**
 * Powered by jpa2web Zathura Generator.
 *
 * @author Diego Armando Gomez Mosquera (dgomez@vortexbird.com)
 * @version 1.0
 */
public class MetaData {

	/** The name. */
	private String name;
	
	/** The main class. */
	private Class<Object> mainClass;
	
	/** The primary key. */
	private Member primaryKey;

	/** The properties. */
	private List<Member> properties;
	
	/** The compose key. */
	private Class composeKey;

	/** The finam param. */
	private String finamParam;
	
	/** The finam param variables. */
	private String finamParamVariables;
	
	/** The final param for id. */
	private String finalParamForId;
	
	/** The final param for id variables. */
	private String finalParamForIdVariables;

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public String getId() {
		return getMainClass().getName().replaceAll("\\.", "_");
	}

	/**
	 * Gets the real class name.
	 *
	 * @return the real class name
	 */
	public String getRealClassName() {
		String[] tmp = (name.replace(".", "%")).split("%");
		String realName = tmp[tmp.length - 1];
		return realName;
	}

	/**
	 * Gets the real class name as variable.
	 *
	 * @return the real class name as variable
	 */
	public String getRealClassNameAsVariable() {
		String[] tmp = (name.replace(".", "%")).split("%");
		String realName = tmp[tmp.length - 1];
		return (realName.substring(0, 1).toLowerCase()) + realName.substring(1, realName.length());
	}

	/**
	 * Gets the properties.
	 *
	 * @return the properties
	 */
	public List<Member> getProperties() {
		return properties;
	}

	/**
	 * Sets the properties.
	 *
	 * @param properties the properties
	 */
	public void setProperties(List<Member> properties) {
		this.properties = properties;
	}

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name.
	 *
	 * @param name the name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the main class.
	 *
	 * @return the main class
	 */
	public Class<Object> getMainClass() {
		return mainClass;
	}

	/**
	 * Sets the main class.
	 *
	 * @param mainClass the main class
	 */
	public void setMainClass(Class<Object> mainClass) {
		this.mainClass = mainClass;
	}

	/**
	 * Gets the primary key.
	 *
	 * @return the primary key
	 */
	public Member getPrimaryKey() {
		return primaryKey;
	}

	/**
	 * Gets the compose key.
	 *
	 * @return the compose key
	 */
	public Class getComposeKey() {
		return composeKey;
	}

	/**
	 * Sets the compose key.
	 *
	 * @param composeKey the compose key
	 */
	public void setComposeKey(Class composeKey) {
		this.composeKey = composeKey;
	}

	/**
	 * Sets the primary key.
	 *
	 * @param primaryKey the primary key
	 */
	public void setPrimaryKey(Member primaryKey) {
		this.primaryKey = primaryKey;
	}

	/**
	 * Gets the finam param.
	 *
	 * @return the finam param
	 */
	public String getFinamParam() {
		return finamParam;
	}

	/**
	 * Sets the finam param.
	 *
	 * @param finamParam the finam param
	 */
	public void setFinamParam(String finamParam) {
		this.finamParam = finamParam;
	}

	/**
	 * Gets the finam param variables.
	 *
	 * @return the finam param variables
	 */
	public String getFinamParamVariables() {
		return finamParamVariables;
	}

	/**
	 * Sets the finam param variables.
	 *
	 * @param finamParamVariables the finam param variables
	 */
	public void setFinamParamVariables(String finamParamVariables) {
		this.finamParamVariables = finamParamVariables;
	}

	/**
	 * Gets the final param for id.
	 *
	 * @return the final param for id
	 */
	public String getFinalParamForId() {
		return finalParamForId;
	}

	/**
	 * Sets the final param for id.
	 *
	 * @param finalParamForId the final param for id
	 */
	public void setFinalParamForId(String finalParamForId) {
		this.finalParamForId = finalParamForId;
	}

	/**
	 * Gets the final param for id variables.
	 *
	 * @return the final param for id variables
	 */
	public String getFinalParamForIdVariables() {
		return finalParamForIdVariables;
	}

	/**
	 * Sets the final param for id variables.
	 *
	 * @param finalParamForIdVariables the final param for id variables
	 */
	public void setFinalParamForIdVariables(String finalParamForIdVariables) {
		this.finalParamForIdVariables = finalParamForIdVariables;
	}

	/**
	 * Checks for compose key.
	 *
	 * @return true, if checks for compose key
	 */
	public boolean hasComposeKey() {
		if (composeKey != null)
			return true;
		else
			return false;
	}

	/**
	 * Gets the real class name for compose key.
	 *
	 * @return the real class name for compose key
	 */
	public String getRealClassNameForComposeKey() {
		String realClassName = new String();
		if (hasComposeKey()) {
			realClassName = getRealClassName(composeKey.getName());
		}

		return realClassName;
	}

	/**
	 * Gets the simple properties.
	 *
	 * @return the simple properties
	 */
	public List<Member> getSimpleProperties() {
		List<Member> ret = new ArrayList<Member>();
		for (Member m : properties)
			if (m instanceof SimpleMember)
				ret.add(m);
		return ret;
	}

	/**
	 * Checks if is get simple properties.
	 *
	 * @return true, if checks if is get simple properties
	 */
	public boolean isGetSimpleProperties() {
		if (getSimpleProperties() != null) {
			if (!getSimpleProperties().isEmpty() && getSimpleProperties().size() > 0) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	/**
	 * Gets the many to one properties.
	 *
	 * @return the many to one properties
	 */
	public List<Member> getManyToOneProperties() {
		List<Member> ret = new ArrayList<Member>();
		for (Member m : properties)
			if (m instanceof ManyToOneMember)
				ret.add(m);
		return ret;
	}

	/**
	 * Checks if is get many to one properties.
	 *
	 * @return true, if checks if is get many to one properties
	 */
	public boolean isGetManyToOneProperties() {
		if (getManyToOneProperties() != null) {
			if (!getManyToOneProperties().isEmpty() && getManyToOneProperties().size() > 0) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	/**
	 * Gets the one to many properties.
	 *
	 * @return the one to many properties
	 */
	public List<Member> getOneToManyProperties() {
		List<Member> ret = new ArrayList<Member>();
		for (Member m : properties)
			if (m instanceof OneToManyMember)
				ret.add(m);
		return ret;
	}

	/**
	 * Checks if is get one to many properties.
	 *
	 * @return true, if checks if is get one to many properties
	 */
	public boolean isGetOneToManyProperties() {
		if (getOneToManyProperties() != null) {
			if (!getOneToManyProperties().isEmpty() && getOneToManyProperties().size() > 0) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	/**
	 * Gets the one to one properties.
	 *
	 * @return the one to one properties
	 */
	public List<Member> getOneToOneProperties() {
		List<Member> ret = new ArrayList<Member>();
		for (Member m : properties)
			if (m instanceof OneToOneMember)
				ret.add(m);
		return ret;
	}

	/**
	 * Checks if is get one to one properties.
	 *
	 * @return true, if checks if is get one to one properties
	 */
	public boolean isGetOneToOneProperties() {
		if (getOneToOneProperties() != null) {
			if (!getOneToOneProperties().isEmpty() && getOneToOneProperties().size() > 0) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	/**
	 * Gets the many to many properties.
	 *
	 * @return the many to many properties
	 */
	public List<Member> getManyToManyProperties() {
		List<Member> ret = new ArrayList<Member>();
		for (Member m : properties)
			if (m instanceof ManyToManyMember)
				ret.add(m);
		return ret;
	}

	/**
	 * Checks if is get many to many properties.
	 *
	 * @return true, if checks if is get many to many properties
	 */
	public boolean isGetManyToManyProperties() {
		if (getOneToOneProperties() != null) {
			if (!getOneToOneProperties().isEmpty() && getOneToOneProperties().size() > 0) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	/**
	 * Primary key properties.
	 *
	 * @return the string
	 */
	public String primaryKeyProperties() {

		String finalParam = new String();
		if (hasComposeKey()) {
			String realClassName = getRealClassName(composeKey.getName());
			String variableName = primaryKey.getName();

			finalParam = realClassName + " " + variableName;
		}
		return finalParam;
	}

	/**
	 * Gets the real class name.
	 *
	 * @param name the name
	 * @return the real class name
	 */
	public String getRealClassName(String name) {
		String[] tmp = (name.replace(".", "%")).split("%");
		String realName = tmp[tmp.length - 1];
		return realName;
	}

}
