package org.zcode.generator.robot.wallj;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.zcode.metadata.model.ManyToOneMember;
import org.zcode.metadata.model.Member;
import org.zcode.metadata.model.MetaData;


/**
 * Zathuracode Generator
 * www.zathuracode.org
 * @author Diego Armando Gomez (dgomez@vortexbird.com)
 * @version 1.0
 */
public class WallJStringBuilderForId implements IWallJStringBuilderForId {

	/** The hash map ids. */
	public HashMap<String, String> hashMapIds;

	/**
	 * The Constructor.
	 *
	 * @param list the list
	 */
	public WallJStringBuilderForId(List<MetaData> list) {
		hashMapIds = new HashMap<String, String>();
		neededIds(list);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.zathuracode.generator.jeewebcentric.utils.
	 * IStringBuilderForId
	 * #finalParamForIdClassAsVariablesAsString(java.util.List,
	 * org.zathuracode.metadata.model.MetaData)
	 */
	public String finalParamForIdClassAsVariablesAsString(List<MetaData> list, MetaData metaData) {
		String finalParam = new String();

		if (metaData.getPrimaryKey().isPrimiaryKeyAComposeKey()) {
			Field[] field = metaData.getComposeKey().getDeclaredFields();
			for (Field field2 : field) {
				String name = field2.getName();
				String nameWithCapitalOnFirst = WallJUtilities.getInstance().getGetNameOfPrimaryName(name);
				String realType = field2.getType().toString().substring((field2.getType().toString()).lastIndexOf(".") + 1,
						(field2.getType().toString()).length());

				finalParam = finalParam + "FacesUtils.check" + realType + "(txt" + nameWithCapitalOnFirst + "), ";

			}
		} else {
			finalParam = "FacesUtils.check" + metaData.getPrimaryKey().getRealClassName() + "(txt" + metaData.getPrimaryKey().getGetNameOfPrimaryName() + "), ";
		}

		String finalCharacter = "" + finalParam.charAt(finalParam.length() - 2);

		finalParam = finalCharacter.equals(",") ? finalParam.substring(0, finalParam.length() - 2) : finalParam;

		return finalParam;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.zathuracode.generator.jeewebcentric.utils.
	 * IStringBuilderForId#finalParamForIdClassAsVariables(java.util.List,
	 * org.zathuracode.metadata.model.MetaData)
	 */
	public List<String> finalParamForIdClassAsVariables(List<MetaData> list, MetaData metaData) {
		List<String> finalParam = new ArrayList<String>();

		WallJUtilities.getInstance().datesIdJSP = new ArrayList<String>();

		if (metaData.getPrimaryKey().isPrimiaryKeyAComposeKey()) {
			Field[] field = metaData.getComposeKey().getDeclaredFields();
			for (Field field2 : field) {
				String name = field2.getName();
				String methodAccesorName = "entity.getId().get" + (name.substring(0, 1)).toUpperCase() + name.substring(1) + "()";
				String realType = WallJUtilities.getInstance().getRealClassName(field2.getType().getName());

				if (realType.equalsIgnoreCase("date")) {
					WallJUtilities.getInstance().datesIdJSP.add(methodAccesorName);
				} else {
					
					String stringAsVariable = WallJUtilities.getInstance().ifcondition + methodAccesorName + "==null" + WallJUtilities.getInstance().ifconditionClose
												+ WallJUtilities.getInstance().throwExceptionNull + "\"" + name + "\"" + WallJUtilities.getInstance().throwExceptionClose;
					
					finalParam.add(stringAsVariable);
				}
			}
		} else {
			
			String name = metaData.getPrimaryKey().getName();			
			String methodAccesorName = "entity.get" + name + "()";
			
			String stringAsVariable = WallJUtilities.getInstance().ifcondition + methodAccesorName + "==null" + WallJUtilities.getInstance().ifconditionClose
										+ WallJUtilities.getInstance().throwExceptionNull + "\"" + name + "\"" + WallJUtilities.getInstance().throwExceptionClose;
			
			finalParam.add(stringAsVariable);
		}

		return finalParam;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.zathuracode.generator.jeewebcentric.utils.
	 * IStringBuilderForId
	 * #finalParamForVariablesDataTablesForIdAsList(java.util.List,
	 * org.zathuracode.metadata.model.MetaData)
	 */
	public List<String> finalParamForVariablesDataTablesForIdAsList(List<MetaData> theMetaData, MetaData metaData) {

		List<String> finalParam2 = new ArrayList<String>();

		if (metaData.getPrimaryKey().isPrimiaryKeyAComposeKey()) {
			Field[] field = metaData.getComposeKey().getDeclaredFields();
			for (Field field2 : field) {

				String name = field2.getName();

				finalParam2.add(metaData.getPrimaryKey().getName() + "." + name);
			}
		} else {
			finalParam2.add(metaData.getPrimaryKey().getName());
		}

		return finalParam2;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.zathuracode.generator.jeewebcentric.utils.
	 * IStringBuilderForId#finalParamForIdVariablesAsList(java.util.List,
	 * org.zathuracode.metadata.model.MetaData)
	 */
	public List<String> finalParamForIdVariablesAsList(List<MetaData> theMetaData, MetaData metaData) {
		List<String> finalParam2 = new ArrayList<String>();
		String finalParam = new String();

		if (metaData.getPrimaryKey().isPrimiaryKeyAComposeKey()) {
			Field[] field = metaData.getComposeKey().getDeclaredFields();
			for (Field field2 : field) {

				String name = field2.getName();
				String methodAccesorName = "entity.getId().get" + (name.substring(0, 1)).toUpperCase() + name.substring(1) + "()";

				finalParam = finalParam + name + ", ";
				finalParam2.add(WallJUtilities.getInstance().ifcondition + methodAccesorName + "==null" + WallJUtilities.getInstance().ifconditionClose
						+ WallJUtilities.getInstance().throwExceptionNull + "\"" + name + "\"" + WallJUtilities.getInstance().throwExceptionClose);				

				finalParam2 = WallJUtilities.getInstance().addVariablesValuesToListDependingOnDataTypeForID(finalParam2, field2, name, methodAccesorName, metaData.getComposeKey());

			}
		}

		if (metaData.isGetSimpleProperties()) {
			for (Member member : metaData.getSimpleProperties()) {
				if (member.isPrimiaryKeyAComposeKey() == false) {

					String name = member.getName();
					String methodAccesorName = "entity." + member.getMethodGetterName();
					
					try {
						if (member.getNullable() == false) {
							finalParam = finalParam + name + ", ";
							finalParam2.add(WallJUtilities.getInstance().ifcondition + methodAccesorName + "==null" + WallJUtilities.getInstance().ifconditionClose
									+ WallJUtilities.getInstance().throwExceptionNull + "\"" + name + "\"" + WallJUtilities.getInstance().throwExceptionClose);
						}

						finalParam2 = WallJUtilities.getInstance().addVariablesValuesToListDependingOnDataType(finalParam2, member.getRealClassName(),
								name, methodAccesorName, member.getPrecision().toString(), member.getScale().toString(), member.getLength().toString());
					} catch (Exception e) {
						// TODO: handle exception
					}

				}
			}
		}

		if (metaData.isGetManyToOneProperties()) {
			for (Member member : metaData.getManyToOneProperties()) {

				WallJUtilities.getInstance().manyToOneTempHash = new HashMap<String, Member>();

				String params[] = WallJUtilities.getInstance().getTypeAndvariableForManyToOneProperties(member.getType().getSimpleName(), theMetaData);

				if (params != null) {
					int cont = 0;
					for (int i = 0; i < params.length; i++) {

						String tmpFinalParam = params[cont];
						if (tmpFinalParam != null) {
							if (cont > params.length)
								cont = params.length;
							else
								cont++;

							String tmp = params[cont];

							tmpFinalParam = finalParam + tmp + ", ";
							
							

							if (!finalParam.contains(tmpFinalParam)) {

								// ManyToOneMember manyToOneMember =
								// (ManyToOneMember) member;

								String variableNames = (tmp.split("_"))[0];
								//String className = (tmp.split("_"))[1];
								
								// Boolean nullable = null;
								//
								// try {
								// nullable = manyToOneMember
								// .getHashMapNullableColumn()
								// .get(variableNames.toUpperCase());
								// } catch (Exception e) {
								// // TODO: handle exception
								// }
								//
								// if (nullable == null) {
								// try {
								// nullable = manyToOneMember
								// .getHashMapNullableColumn()
								// .get(className.toUpperCase());
								// } catch (Exception e) {
								// // TODO: handle exception
								// }
								// }

								Member primarySimple = WallJUtilities.getInstance().manyToOneTempHash.get(variableNames);

								String methodAccesorName = "entity.get" + member.getRealClassName() + "()." + primarySimple.getMethodGetterName();
								
								// try {
								// if (nullable == false) {
								finalParam2.add(WallJUtilities.getInstance().ifcondition + methodAccesorName + "==null" + WallJUtilities.getInstance().ifconditionClose
										+ WallJUtilities.getInstance().throwExceptionNull + "\"" + tmp + "\"" + WallJUtilities.getInstance().throwExceptionClose);
								// }
								// } catch (Exception e) {
								// // System.out.println(e.getMessage());
								// }

								try {

									finalParam2 = WallJUtilities.getInstance().addVariablesValuesToListDependingOnDataType(finalParam2,
											primarySimple.getRealClassName(), tmp, methodAccesorName, primarySimple.getPrecision().toString(),
											primarySimple.getScale().toString(), primarySimple.getLength().toString());
								} catch (Exception e) {
									// TODO: handle exception
								}

							} else {
								ManyToOneMember manyToOneMember = (ManyToOneMember) member;

								String variableNames = (tmp.split("_"))[0] + i;
								String variableNames2 = (tmp.split("_"))[0];
								String className = (tmp.split("_"))[1] + i;

								Boolean nullable = null;

								try {
									nullable = manyToOneMember.getHashMapNullableColumn().get(variableNames.toUpperCase());
								} catch (Exception e) {
									// TODO: handle exception
								}

								if (nullable == null) {
									try {
										nullable = manyToOneMember.getHashMapNullableColumn().get(className.toUpperCase());
									} catch (Exception e) {
										// TODO: handle exception
									}
								}

								Member primarySimple = WallJUtilities.getInstance().manyToOneTempHash.get(variableNames2);

								String methodAccesorName = "entity.get" + member.getRealClassName() + "()." + primarySimple.getMethodGetterName();
								
								try {
									if (nullable == false) {
										finalParam2.add(WallJUtilities.getInstance().ifcondition + methodAccesorName + "==null" + WallJUtilities.getInstance().ifconditionClose
												+ WallJUtilities.getInstance().throwExceptionNull + "\"" + tmp + "\"" + WallJUtilities.getInstance().throwExceptionClose);
									}
								} catch (Exception e) {
									// System.out.println(e.getMessage());
								}

								try {

									finalParam2 = WallJUtilities.getInstance().addVariablesValuesToListDependingOnDataType(finalParam2,
											primarySimple.getRealClassName(), tmp, methodAccesorName, primarySimple.getPrecision().toString(),
											primarySimple.getScale().toString(), primarySimple.getLength().toString());
								} catch (Exception e) {
									// TODO: handle exception
								}
							}

							if (cont > params.length)
								cont = params.length;
							else
								cont++;
						}
					}
				}
			}
		}

		return finalParam2;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.zathuracode.generator.jeewebcentric.utils.
	 * IStringBuilderForId#finalParamForIdForViewVariablesInList(java.util.List,
	 * org.zathuracode.metadata.model.MetaData)
	 */
	public List<String> finalParamForIdForViewVariablesInList(List<MetaData> list, MetaData metaData) {

		// if (metaData.getRealClassName().equalsIgnoreCase("DiaNoLaboral")) {
		// String tmp = "";
		// System.out.println(tmp);
		// }

		List<String> finalParam2 = new ArrayList<String>();
		String finalParam = new String();
		WallJUtilities.getInstance().datesId = new ArrayList<String>();

		if (metaData.getPrimaryKey().isPrimiaryKeyAComposeKey()) {
			Field[] field = metaData.getComposeKey().getDeclaredFields();
			for (Field field2 : field) {
				String name = field2.getName();
				String type = WallJUtilities.getInstance().getRealClassName(field2.getType().getName());

				String tmp1 = (name.substring(0, 1)).toUpperCase() + name.substring(1);

				if (type.equalsIgnoreCase("date")) {
					if (!WallJUtilities.getInstance().dates.contains(tmp1))
						WallJUtilities.getInstance().datesId.add(tmp1);
				} else {
					finalParam2.add(tmp1);
				}

			}
		} else {
			finalParam = metaData.getPrimaryKey().getName();
			String type = metaData.getPrimaryKey().getRealClassName();
			String tmp1 = (finalParam.substring(0, 1)).toUpperCase() + finalParam.substring(1);
			if (type.equalsIgnoreCase("date")) {
				if (!WallJUtilities.getInstance().dates.contains(tmp1))
					WallJUtilities.getInstance().datesId.add(tmp1);
			} else {
				finalParam2.add(tmp1);
			}

		}

		return finalParam2;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.zathuracode.generator.jeewebcentric.utils.
	 * IStringBuilderForId
	 * #finalParamForIdForViewForSetsVariablesInList(java.util.List,
	 * org.zathuracode.metadata.model.MetaData)
	 */
	public List<String> finalParamForIdForViewForSetsVariablesInList(List<MetaData> theMetaData, MetaData metaData) {

		List<String> finalParam2 = new ArrayList<String>();
		String finalParam = new String();

		if (metaData.getPrimaryKey().isPrimiaryKeyAComposeKey()) {
			Field[] field = metaData.getComposeKey().getDeclaredFields();
			for (Field field2 : field) {
				String name = field2.getName();
				finalParam = finalParam + name;
				// String idName = metaData.getPrimaryKey().getName();
				// txtMasCodigo.setValue(entity.getId().getMasCodigo());
				String tmp1 = "txt" + (name.substring(0, 1)).toUpperCase() + name.substring(1) + ".setValue(" + "entity.get"
						+ metaData.getPrimaryKey().getGetNameOfPrimaryName() + "()" + ".get" + (name.substring(0, 1)).toUpperCase() + name.substring(1) + "()"
						+ ");" + "txt" + (name.substring(0, 1)).toUpperCase() + name.substring(1) + ".setDisabled(true);";

				finalParam2.add(tmp1);
			}

		} else {
			// txtCliCedula.setValue(entity.getCliCedula());
			finalParam = metaData.getPrimaryKey().getName();
			String tmp1 = "txt" + (finalParam.substring(0, 1)).toUpperCase() + finalParam.substring(1) + ".setValue(" + "entity.get"
					+ (finalParam.substring(0, 1)).toUpperCase() + finalParam.substring(1) + "()" + ");" + "txt" + (finalParam.substring(0, 1)).toUpperCase()
					+ finalParam.substring(1) + ".setDisabled(true);";
			finalParam2.add(tmp1);
		}

		return finalParam2;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.zathuracode.generator.jeewebcentric.utils.
	 * IStringBuilderForId
	 * #finalParamForIdForDtoForSetsVariablesInList(java.util.List,
	 * org.zathuracode.metadata.model.MetaData)
	 */
	public List<String> finalParamForIdForDtoForSetsVariablesInList(List<MetaData> theMetaData, MetaData metaData) {

		List<String> finalParam2 = new ArrayList<String>();
		String finalParam = new String();

		// if(metaData.getRealClassName().equalsIgnoreCase("alarma")){
		// String tmp = "";
		// System.out.println(tmp);
		// }

		if (metaData.getPrimaryKey().isPrimiaryKeyAComposeKey()) {
			Field[] field = metaData.getComposeKey().getDeclaredFields();
			for (Field field2 : field) {
				String name = field2.getName();
				finalParam = finalParam + name;
				String type = field2.getType().getSimpleName();

				// String idName = metaData.getPrimaryKey().getName();
				// txtMasCodigo.setValue(entity.getId().getMasCodigo());
				if (!type.equalsIgnoreCase("date")) {
					String tmp1 = name + "=" + metaData.getRealClassNameAsVariable() + ".get" + metaData.getPrimaryKey().getGetNameOfPrimaryName() + "()"
							+ ".get" + (name.substring(0, 1)).toUpperCase() + name.substring(1) + "().toString()" + ";";

					finalParam2.add(tmp1);
				} else {
					String tmp1 = name + "=" + metaData.getRealClassNameAsVariable() + ".get" + metaData.getPrimaryKey().getGetNameOfPrimaryName() + "()"
							+ ".get" + (name.substring(0, 1)).toUpperCase() + name.substring(1) + "()" + ";";

					finalParam2.add(tmp1);
				}

			}

		} else {
			// txtCliCedula.setValue(entity.getCliCedula());
			finalParam = metaData.getPrimaryKey().getName();
			String type = metaData.getPrimaryKey().getRealClassName();
			if (!type.equalsIgnoreCase("date")) {
				String tmp1 = finalParam + "=" + metaData.getRealClassNameAsVariable() + ".get" + (finalParam.substring(0, 1)).toUpperCase()
						+ finalParam.substring(1) + "().toString()" + ";";
				finalParam2.add(tmp1);
			} else {
				String tmp1 = finalParam + "=" + metaData.getRealClassNameAsVariable() + ".get" + (finalParam.substring(0, 1)).toUpperCase()
						+ finalParam.substring(1) + "()" + ";";
				finalParam2.add(tmp1);
			}

		}

		return finalParam2;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.zathuracode.generator.jeewebcentric.utils.
	 * IStringBuilderForId
	 * #finalParamForIdForDtoInViewForSetsVariablesInList(java.util.List,
	 * org.zathuracode.metadata.model.MetaData)
	 */
	public List<String> finalParamForIdForDtoInViewForSetsVariablesInList(List<MetaData> theMetaData, MetaData metaData) {

		List<String> finalParam2 = new ArrayList<String>();
		String finalParam = new String();

		if (metaData.getPrimaryKey().isPrimiaryKeyAComposeKey()) {
			Field[] field = metaData.getComposeKey().getDeclaredFields();
			for (Field field2 : field) {
				String name = field2.getName();
				finalParam = finalParam + name;
				String type = field2.getType().getSimpleName();
				// String idName = metaData.getPrimaryKey().getName();
				// txtMasCodigo.setValue(entity.getId().getMasCodigo());
				if (!type.equalsIgnoreCase("date")) {
					String tmp1 = metaData.getRealClassNameAsVariable() + "DTO2.set" + name.substring(0, 1).toUpperCase() + name.substring(1) + "("
							+ metaData.getRealClassNameAsVariable() + "Tmp.get" + metaData.getPrimaryKey().getGetNameOfPrimaryName() + "()" + ".get"
							+ (name.substring(0, 1)).toUpperCase() + name.substring(1) + "().toString()" + ");";

					finalParam2.add(tmp1);
				} else {
					String tmp1 = metaData.getRealClassNameAsVariable() + "DTO2.set" + name.substring(0, 1).toUpperCase() + name.substring(1) + "("
							+ metaData.getRealClassNameAsVariable() + "Tmp.get" + metaData.getPrimaryKey().getGetNameOfPrimaryName() + "()" + ".get"
							+ (name.substring(0, 1)).toUpperCase() + name.substring(1) + "()" + ");";

					finalParam2.add(tmp1);
				}
			}

		} else {
			// txtCliCedula.setValue(entity.getCliCedula());
			String type = metaData.getPrimaryKey().getRealClassName();
			finalParam = metaData.getPrimaryKey().getName();
			if (!type.equalsIgnoreCase("date")) {
				String tmp1 = metaData.getRealClassNameAsVariable() + "DTO2.set" + finalParam.substring(0, 1).toUpperCase() + finalParam.substring(1) + "("
						+ metaData.getRealClassNameAsVariable() + "Tmp.get" + (finalParam.substring(0, 1)).toUpperCase() + finalParam.substring(1)
						+ "().toString())" + ";";
				finalParam2.add(tmp1);
			} else {
				String tmp1 = metaData.getRealClassNameAsVariable() + "DTO2.set" + finalParam.substring(0, 1).toUpperCase() + finalParam.substring(1) + "("
						+ metaData.getRealClassNameAsVariable() + "Tmp.get" + (finalParam.substring(0, 1)).toUpperCase() + finalParam.substring(1) + "())"
						+ ";";
				finalParam2.add(tmp1);
			}
		}

		return finalParam2;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.zathuracode.generator.jeewebcentric.utils.
	 * IStringBuilderForId#finalParamForIdClass(java.util.List,
	 * org.zathuracode.metadata.model.MetaData)
	 */
	public List<String> finalParamForIdClass(List<MetaData> list, MetaData metaData) {
		List<String> finalParam = new ArrayList<String>();

		if (metaData.getPrimaryKey().isPrimiaryKeyAComposeKey()) {
			String id = metaData.getPrimaryKey().getRealClassName() + " " + metaData.getPrimaryKey().getName() + "Class = " + "new "
					+ metaData.getPrimaryKey().getRealClassName() + "();";
			finalParam.add(id);
			Field[] field = metaData.getComposeKey().getDeclaredFields();
			for (Field field2 : field) {
				String name = field2.getName();

				String setToId = metaData.getPrimaryKey().getName() + "Class.set" + name.substring(0, 1).toUpperCase() + name.substring(1, name.length()) + "("
						+ name + ");";
				finalParam.add(setToId);
			}
		}

		return finalParam;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.zathuracode.generator.jeewebcentric.utils.
	 * IStringBuilderForId#finalParamForIdClassAsVariables2(java.util.List,
	 * org.zathuracode.metadata.model.MetaData)
	 */
	public List<String> finalParamForIdClassAsVariables2(List<MetaData> list, MetaData metaData) {
		List<String> finalParam = new ArrayList<String>();

		if (metaData.getPrimaryKey().isPrimiaryKeyAComposeKey()) {
			Field[] field = metaData.getComposeKey().getDeclaredFields();
			for (Field field2 : field) {
				String name = field2.getName();

				finalParam.add(name);
			}
		} else {
			finalParam.add(metaData.getPrimaryKey().getName());
		}

		return finalParam;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.zathuracode.generator.jeewebcentric.utils.
	 * IStringBuilderForId#finalParamForIdForViewClass(java.util.List,
	 * org.zathuracode.metadata.model.MetaData)
	 */
	public List<String> finalParamForIdForViewClass(List<MetaData> list, MetaData metaData) {
		List<String> finalParam = new ArrayList<String>();

		if (metaData.getPrimaryKey().isPrimiaryKeyAComposeKey()) {
			String id = metaData.getPrimaryKey().getRealClassName() + " " + metaData.getPrimaryKey().getName() + " = " + "new "
					+ metaData.getPrimaryKey().getRealClassName() + "();";
			
			finalParam.add(id);
			Field[] field = metaData.getComposeKey().getDeclaredFields();
			for (Field field2 : field) {
				String name = field2.getName();
				

				String nameFragment = name.substring(0, 1).toUpperCase() + name.substring(1);

				String realType = field2.getType().toString().substring((field2.getType().toString()).lastIndexOf(".") + 1,
						(field2.getType().toString()).length());

				String setToId = new String();

				if (realType.equalsIgnoreCase("date")) {
					setToId = metaData.getPrimaryKey().getName() + ".set" + nameFragment + "((txt" + nameFragment + ".getValue())==null||(txt" + nameFragment
							+ ".getValue()).equals(\"\")?null:" + " FacesUtils.check" + realType + "(txt" + nameFragment + ".getValue()));";
				} else {
					setToId = metaData.getPrimaryKey().getName() + ".set" + nameFragment + "((txt" + nameFragment + ".getValue())==null||(txt" + nameFragment
							+ ".getValue()).equals(\"\")?null: FacesUtils.check" + realType + "(txt" + nameFragment + "));";
				}
				
				// String setToId = metaData.getPrimaryKey().getName() + ".set"
				// + nameFragment + "(new " + realType + "((String) txt"
				// + nameFragment + ".getValue()));";

				finalParam.add(setToId);
			}
		} else {
			String setToId = new String();

			setToId = metaData.getPrimaryKey().getRealClassName() + " " + metaData.getPrimaryKey().getName() + " = FacesUtils.check"
					+ metaData.getPrimaryKey().getRealClassName() + "(" + "txt" + metaData.getPrimaryKey().getGetNameOfPrimaryName()
					+ ");";

			finalParam.add(setToId);
		}

		return finalParam;
	}

	
	
	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.zathuracode.generator.jeewebcentric.utils.
	 * IStringBuilderForId#finalParamForId(java.util.List,
	 * org.zathuracode.metadata.model.MetaData)
	 */
	public String finalParamForId(List<MetaData> list, MetaData metaData) {
		String finalParam = new String();

		if (metaData.getPrimaryKey().isPrimiaryKeyAComposeKey()) {
			Field[] field = metaData.getComposeKey().getDeclaredFields();
			for (Field field2 : field) {
				String name = field2.getName();

				String realType = field2.getType().toString().substring((field2.getType().toString()).lastIndexOf(".") + 1,
						(field2.getType().toString()).length());

				finalParam = finalParam + realType + " " + name + ", ";
			}

			String finalCharacter = "" + finalParam.charAt(finalParam.length() - 2);

			finalParam = finalCharacter.equals(",") ? finalParam.substring(0, finalParam.length() - 2) : finalParam;

			return finalParam;
		} else {
			finalParam = metaData.getPrimaryKey().getRealClassName() + " " + metaData.getPrimaryKey().getName();
		}

		return finalParam;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.zathuracode.generator.jeewebcentric.utils.
	 * IStringBuilderForId#finalParamForIdVariables(java.util.List,
	 * org.zathuracode.metadata.model.MetaData)
	 */
	public String finalParamForIdVariables(List<MetaData> list, MetaData metaData) {
		String finalParam = new String();

		if (metaData.getPrimaryKey().isPrimiaryKeyAComposeKey()) {
			Field[] field = metaData.getComposeKey().getDeclaredFields();
			for (Field field2 : field) {
				String name = field2.getName();

				finalParam = finalParam + name + ", ";
			}

			String finalCharacter = "" + finalParam.charAt(finalParam.length() - 2);

			finalParam = finalCharacter.equals(",") ? finalParam.substring(0, finalParam.length() - 2) : finalParam;

			return finalParam;
		} else {
			finalParam = metaData.getPrimaryKey().getName();
		}

		return finalParam;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.zathuracode.generator.jeewebcentric.utils.
	 * IStringBuilderForId#neededIds(java.util.List)
	 */
	public void neededIds(List<MetaData> list) {
		for (MetaData metaData : list) {
			if (metaData.getPrimaryKey().isPrimiaryKeyAComposeKey()) {

				Field[] field = metaData.getComposeKey().getDeclaredFields();

				for (int i = 0; i < field.length; i++) {
					Field field2 = field[i];
					String name = field2.getName();

					hashMapIds.put(name + "_" + metaData.getRealClassName(), metaData.getPrimaryKey().getName());
				}
			}
		}
		HashMap<String, String> map = hashMapIds;
	}
	
	/** 
	 *  Este metodo sirve para la creacion del metodo getData  y permite  hidratar la entity  consultada en 
	 *  de la base de datos a el objeto DTO
	 * */
	
	public List<String> dtoConvert(List<MetaData> theMetaData, MetaData metaData) {

		List<String> finalParam2 = new ArrayList<String>();
		String finalParam = new String();

		if (metaData.getPrimaryKey().isPrimiaryKeyAComposeKey()) {
			Field[] field = metaData.getComposeKey().getDeclaredFields();
			for (Field field2 : field) {
				String name = field2.getName();
				finalParam = finalParam + name;
				 String type = field2.getType().getSimpleName();
				// String idName = metaData.getPrimaryKey().getName();
				// txtMasCodigo.setValue(entity.getId().getMasCodigo());
				 
					String tmp1 = metaData.getRealClassNameAsVariable() + "DTO2.set" + name.substring(0, 1).toUpperCase() + name.substring(1) + "("
							+ metaData.getRealClassNameAsVariable() + "Tmp.get" + metaData.getPrimaryKey().getGetNameOfPrimaryName() + "()" + ".get"
							+ (name.substring(0, 1)).toUpperCase() + name.substring(1) + "()" + ");";

					finalParam2.add(tmp1);
				}
			

		} else {
			// txtCliCedula.setValue(entity.getCliCedula());
			String type = metaData.getPrimaryKey().getRealClassName();
			finalParam = metaData.getPrimaryKey().getName();
			
				String tmp1 = metaData.getRealClassNameAsVariable() + "DTO2.set" + finalParam.substring(0, 1).toUpperCase() + finalParam.substring(1) + "("
						+ metaData.getRealClassNameAsVariable() + "Tmp.get" + (finalParam.substring(0, 1)).toUpperCase() + finalParam.substring(1) + "())"
						+ ";";
				finalParam2.add(tmp1);
			
		}

		return finalParam2;

	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.zathuracode.generator.jeewebcentric.utils.
	 * IStringBuilderForId
	 * #finalParamForIdForViewForSetsVariablesDtoInList(java.util.List,
	 * org.zathuracode.metadata.model.MetaData)
	 */
	public List<String> finalParamForIdForViewForSetsVariablesDtoInList(List<MetaData> theMetaData, MetaData metaData) {

		List<String> finalParam2 = new ArrayList<String>();
		String finalParam = new String();

		if (metaData.getPrimaryKey().isPrimiaryKeyAComposeKey()) {
			Field[] field = metaData.getComposeKey().getDeclaredFields();
			for (Field field2 : field) {
				String name = field2.getName();
				finalParam = finalParam + name;

				String tmp1 = "txt" + (name.substring(0, 1)).toUpperCase()
						+ name.substring(1) + ".setValue(" + "selected"
						+ metaData.getRealClassName() + ".get"
						+ (name.substring(0, 1)).toUpperCase()
						+ name.substring(1) + "()" + ");" + "txt"
						+ (name.substring(0, 1)).toUpperCase()
						+ name.substring(1) + ".setDisabled(true);";

				finalParam2.add(tmp1);
			}

		} else {
			finalParam = metaData.getPrimaryKey().getName();
			String tmp1 = "txt" + (finalParam.substring(0, 1)).toUpperCase()
					+ finalParam.substring(1) + ".setValue(" + "selected"
					+ metaData.getRealClassName() + ".get"
					+ (finalParam.substring(0, 1)).toUpperCase()
					+ finalParam.substring(1) + "()" + ");" + "txt"
					+ (finalParam.substring(0, 1)).toUpperCase()
					+ finalParam.substring(1) + ".setDisabled(true);";
			finalParam2.add(tmp1);
		}

		return finalParam2;

	}
}
