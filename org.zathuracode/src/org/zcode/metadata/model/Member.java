package org.zcode.metadata.model;

import java.util.HashMap;

// TODO: Auto-generated Javadoc
/**
 * Powered by jpa2web Zathura Generator.
 *
 * @author Diego Armando Gomez Mosquera (dgomez@vortexbird.com)
 * @version 1.0
 */
public class Member implements Comparable {

	/** The name. */
	private String name;
	
	/** The type. */
	private Class type;
	
	/** The order. */
	private int order = -1;
	
	/** The show name. */
	private String showName;

	/** The precision. */
	private Long precision;
	
	/** The scale. */
	private Long scale;
	
	/** The nullable. */
	Boolean nullable;
	
	/** The length. */
	private Long length;

	/** The hash map ids properties. */
	private HashMap<String, Member> hashMapIdsProperties = new HashMap<String, Member>();

	/**
	 * The Constructor.
	 */
	public Member() {

	}

	/**
	 * Gets the real class name.
	 *
	 * @return the real class name
	 */

	public String getRealClassName() {
		String typeComplete = type.getName();
		String[] tmp = (typeComplete.replace(".", "%")).split("%");
		String realName = tmp[tmp.length - 1];
		return realName;
	}

	/**
	 * Gets the real class variable name.
	 *
	 * @return the real class variable name
	 */
	public String getRealClassVariableName() {
		String typeComplete = type.getName();
		String[] tmp = (typeComplete.replace(".", "%")).split("%");
		String realName = tmp[tmp.length - 1];
		return (realName.substring(0, 1)).toLowerCase() + realName.substring(1, realName.length());
	}

	/**
	 * Gets the get name of primary name.
	 *
	 * @return the gets the name of primary name
	 */
	public String getGetNameOfPrimaryName() {
		String build = name.substring(0, 1).toUpperCase();
		String build2 = name.substring(1, name.length());
		return build + build2;
	}
	
	public String getMethodGetterName(){
		String methodGetterName = "get" + (name.substring(0, 1)).toUpperCase() + name.substring(1) + "()";
		return methodGetterName;
	}
	
	public String getMethodSetterName(){
		String methodGetterName = "set" + (name.substring(0, 1)).toUpperCase() + name.substring(1);
		return methodGetterName;
	}

	/**
	 * The Constructor.
	 *
	 * @param name the name
	 * @param showName the show name
	 * @param type the type
	 * @param order the order
	 */
	public Member(String name, String showName, Class type, int order) {
		super();
		this.name = name;
		this.type = type;
		this.order = order;
		this.showName = showName;
	}

	/**
	 * Checks if is primiary key a compose key.
	 *
	 * @return true, if checks if is primiary key a compose key
	 */
	public boolean isPrimiaryKeyAComposeKey() {
		boolean ret = false;

		String firstLetters = type.getName();
		String[] tmp = (firstLetters.replace(".", "%")).split("%");

		if (tmp != null) {
			if (tmp.length > 1) {
				// Is possible are java.lang.*, or java.util.* or java.math

				
				if ((tmp[0].equalsIgnoreCase("java") && tmp[1].equalsIgnoreCase("lang"))
						|| (tmp[0].equalsIgnoreCase("java") && tmp[1].equalsIgnoreCase("util"))
						|| (tmp[0].equalsIgnoreCase("java") && tmp[1].equalsIgnoreCase("math"))
						|| (tmp[0].equalsIgnoreCase("java") && tmp[1].equalsIgnoreCase("sql"))
					) {

					ret = false;
				} else {
					ret = true;
				}
			} else {
				ret = false;
			}
		}

		return ret;
	}

	/**
	 * Checks if is simple member.
	 *
	 * @return true, if checks if is simple member
	 */
	public boolean isSimpleMember() {
		boolean ret = false;

		try {
			if (this instanceof SimpleMember)
				ret = true;
			else
				ret = false;
		} catch (Exception e) {
			ret = false;
		}

		return ret;
	}

	/**
	 * Gets the order.
	 *
	 * @return the order
	 */
	public int getOrder() {
		return order;
	}

	/**
	 * Sets the order.
	 *
	 * @param order the order
	 */
	public void setOrder(int order) {
		this.order = order;
	}

	/**
	 * Gets the type.
	 *
	 * @return the type
	 */
	public Class getType() {
		return type;
	}

	/**
	 * Sets the type.
	 *
	 * @param type the type
	 */
	public void setType(Class type) {
		this.type = type;
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

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Object o) {

		Member m = (Member) o;
		if ((order == -1) && (m.getOrder() == -1)) {
			return getName().compareTo(m.getName());
		} else if (m.getOrder() == -1) {
			return -1;
		} else if (getOrder() == -1) {
			return 1;
		} else {
			return new Integer(order).compareTo(new Integer(m.getOrder()));
		}

	}

	/**
	 * Gets the show name.
	 *
	 * @return the show name
	 */
	public String getShowName() {
		return showName;
	}

	/**
	 * Sets the show name.
	 *
	 * @param showName the show name
	 */
	public void setShowName(String showName) {
		this.showName = showName;
	}

	/**
	 * Gets the precision.
	 *
	 * @return the precision
	 */
	public Long getPrecision() {
		return precision;
	}

	/**
	 * Sets the precision.
	 *
	 * @param precision the precision
	 */
	public void setPrecision(Long precision) {
		this.precision = precision;
	}

	/**
	 * Gets the scale.
	 *
	 * @return the scale
	 */
	public Long getScale() {
		return scale;
	}

	/**
	 * Sets the scale.
	 *
	 * @param scale the scale
	 */
	public void setScale(Long scale) {
		this.scale = scale;
	}

	/**
	 * Gets the nullable.
	 *
	 * @return the nullable
	 */
	public Boolean getNullable() {
		return nullable;
	}

	/**
	 * Sets the nullable.
	 *
	 * @param nullable the nullable
	 */
	public void setNullable(Boolean nullable) {
		this.nullable = nullable;
	}

	/**
	 * Gets the length.
	 *
	 * @return the length
	 */
	public Long getLength() {
		return length;
	}

	/**
	 * Sets the length.
	 *
	 * @param length the length
	 */
	public void setLength(Long length) {
		this.length = length;
	}

	/**
	 * Gets the hash map ids properties.
	 *
	 * @return the hash map ids properties
	 */
	public HashMap<String, Member> getHashMapIdsProperties() {
		return hashMapIdsProperties;
	}

	/**
	 * Sets the hash map ids properties.
	 *
	 * @param hashMapIdsProperties the hash map ids properties
	 */
	public void setHashMapIdsProperties(HashMap<String, Member> hashMapIdsProperties) {
		this.hashMapIdsProperties = hashMapIdsProperties;
	}
	
	/**
	 * Checks if is many to one member.
	 *
	 * @return true, if checks if is many to one member.
	 */
	public boolean isManyToOneMember() {
		boolean ret = false;

		try {
			if (this instanceof ManyToOneMember)
				ret = true;
			else
				ret = false;
		} catch (Exception e) {
			ret = false;
		}

		return ret;
	}
	
	/**
	 * Checks if is one to many member.
	 *
	 * @return true, if checks if is one to many member.
	 */
	public boolean isOneToManyMember() {
		boolean ret = false;

		try {
			if (this instanceof OneToManyMember)
				ret = true;
			else
				ret = false;
		} catch (Exception e) {
			ret = false;
		}

		return ret;
	}
	
}
