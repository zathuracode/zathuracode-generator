package org.zcode.metadata.engine;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zcode.metadata.model.GeneratedValueMember;
import org.zcode.metadata.model.ManyToManyMember;
import org.zcode.metadata.model.ManyToOneMember;
import org.zcode.metadata.model.Member;
import org.zcode.metadata.model.MetaData;
import org.zcode.metadata.model.MetaDataModel;
import org.zcode.metadata.model.OneToManyMember;
import org.zcode.metadata.model.OneToOneMember;
import org.zcode.metadata.model.SimpleMember;
import org.zcode.metadata.reader.IMetaDataReader;
import org.zcode.metadata.utilities.MetaDataUtil;


/**
 * Zathura Generator.
 *
 * @author Diego Armando Gomez Mosquera (dgomez@vortexbird.com)
 * @version 1.0
 */
public class JPAEntityLoaderEngine implements IMetaDataReader {

	/** The log. */
	private static final Logger log = LoggerFactory.getLogger(JPAEntityLoaderEngine.class);

	/* (non-Javadoc)
	 * @see org.zathuracode.metadata.reader.IMetaDataReader#loadMetaDataModel(java.lang.String, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public MetaDataModel loadMetaDataModel(String path, String pckgName) throws Exception{
		log.info("Loading JPA Entity Data Model");
		MetaDataModel metaDataModel = new MetaDataModel();
		metaDataModel.setTheMetaData(new ArrayList<MetaData>());

		try {
			log.info("Reading from:" + JPAEntityLoaderEngine.class.getClassLoader().getResource("").getFile());
			List<Class> classes = MetaDataUtil.findEntityInFolder(path, pckgName);
			for (Class clazz : classes) {
				log.info("--------------------------------------------------------------------------------------------------");
				log.info("Loading MetaData Entity class:" + clazz.getCanonicalName());
				MetaData metaData = loadMetaData(clazz);
				metaDataModel.getTheMetaData().add(metaData);
			}
			for (MetaData metaData : metaDataModel.getTheMetaData()) {
				if (metaData.getPrimaryKey().isPrimiaryKeyAComposeKey()) {
					metaData.setComposeKey(MetaDataUtil.findClassIdInFolder(path, pckgName, metaData.getPrimaryKey().getRealClassName()));
				}
			}
		} catch (ClassNotFoundException e) {
			throw e;
		} catch (IllegalAccessException e) {
			throw e;
		} catch (InvocationTargetException e) {
			throw e;
		} catch (NoSuchMethodException e) {
			throw e;
		} catch (InstantiationException e) {
			throw e;
		} catch (MalformedURLException e) {
			throw e;
		}
		return metaDataModel;
	}

	/**
	 * Load meta data.
	 *
	 * @param clazz the clazz
	 * @return the meta data
	 * @throws IllegalAccessException the illegal access exception
	 * @throws InvocationTargetException the invocation target exception
	 * @throws NoSuchMethodException the no such method exception
	 * @throws InstantiationException the instantiation exception
	 */
	private MetaData loadMetaData(Class clazz) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, InstantiationException, Exception {

		MetaData metaData = new MetaData();
		metaData.setMainClass(clazz);
		List<Member> theMembers = new ArrayList<Member>();
		metaData.setProperties(theMembers);
		metaData.setName(clazz.getName());

		// Con Bean Util se lee las descripcion de la clase
		Map<String, String> desc = BeanUtils.describe(clazz.newInstance());
		Set<String> members = desc.keySet();

		log.info("Properties Entity:" + members);

		try {
			for (String memberName : members) {// Begin Member
				if (!memberName.equals("class")) {// Begin if Member //Solo para
					// miembros las clases no se
					// tiene en cuenta
					loadMetaDataInMethod(clazz, metaData, theMembers, memberName);
					loadMetaDataInField(clazz, metaData, theMembers, memberName);
				}// End if Member
			}// End Member

			Collections.sort(metaData.getProperties());

			for (Member m : metaData.getProperties()) {
				log.debug("Entity Member:" + m.getName());
			}
		} catch (RuntimeException e) {
			throw e;
		}catch (Exception e) {
			throw e;
		}
		return metaData;
	}

	/**
	 * Load meta data in field.
	 *
	 * @param clazz the clazz
	 * @param metaData the meta data
	 * @param theMembers the members
	 * @param memberName the member name
	 */
	private void loadMetaDataInField(Class clazz, MetaData metaData, List<Member> theMembers, String memberName)throws Exception {

		for (Field field : clazz.getDeclaredFields()) {
			int order = -1;
			if (field.getName().equals(memberName) == true) {
				if (field.getAnnotations() != null && field.getAnnotations().length > 0) {
					if (field.getAnnotation(OneToMany.class) != null) {
						// es one-to-many
						log.info("one to many:" + memberName);

						ParameterizedType rettype = (ParameterizedType) field.getGenericType();
						log.debug(rettype.getActualTypeArguments()[0].toString());

						// Reomver esa propiedad
						MetaDataUtil.removeMember(theMembers, memberName);
						OneToManyMember oneToManyMember = new OneToManyMember(memberName, field.getName(), (Class) rettype.getActualTypeArguments()[0], field
								.getType(), order);
						oneToManyMember.setMappedBy(field.getAnnotation(OneToMany.class).mappedBy());
						theMembers.add(oneToManyMember);
					} else if (field.getAnnotation(OneToOne.class) != null) {
						// es one-to-one
						log.info("one to one:" + memberName);
						MetaDataUtil.removeMember(theMembers, memberName);
						theMembers.add(new OneToOneMember(memberName, field.getName(), field.getType(), order));
					} else if (field.getAnnotation(ManyToOne.class) != null) {
						// many to one
						log.info("many to one:" + memberName);
						MetaDataUtil.removeMember(theMembers, memberName);

						ManyToOneMember manyToOneMember = new ManyToOneMember(memberName, field.getName(), field.getType(), order);

						JoinColumn joinColumn = field.getAnnotation(JoinColumn.class);
						JoinColumns joinColumns = field.getAnnotation(JoinColumns.class);

						if (joinColumn != null) {

							Boolean nullable = joinColumn.nullable();
							nullable = nullable == null ? !field.getAnnotation(ManyToOne.class).optional() : nullable;

							HashMap<String, Boolean> hashMapNullableColumn = new HashMap<String, Boolean>();

							hashMapNullableColumn.put(memberName.toUpperCase(), nullable);

							manyToOneMember.setHashMapNullableColumn(hashMapNullableColumn);
						} else {
							if (joinColumns != null) {
								JoinColumn[] joinColumnFromColumns = joinColumns.value();
								HashMap<String, Boolean> hashMapNullableColumn = new HashMap<String, Boolean>();

								for (JoinColumn joinColumn2 : joinColumnFromColumns) {
									String name = joinColumn2.name();

									String neededName = new String();

									try {
										if (name.split("_").length > 1) {
											String[] tmp = name.split("_");
											for (int i = 0; i < tmp.length; i++) {
												neededName = neededName + tmp[i];
											}
										} else {
											neededName = name;
										}
									} catch (Exception e) {
										neededName = name;
									}

									neededName = neededName.toUpperCase();

									Boolean nullable = joinColumn2.nullable();

									hashMapNullableColumn.put(neededName, nullable);
								}
								manyToOneMember.setHashMapNullableColumn(hashMapNullableColumn);
							}
						}

						theMembers.add(manyToOneMember);
					} else if (field.getAnnotation(ManyToMany.class) != null) {
						// many to many
						log.info("many to many:" + memberName);
						ParameterizedType rettype = (ParameterizedType) field.getGenericType();
						log.debug(rettype.getActualTypeArguments()[0].toString());
						MetaDataUtil.removeMember(theMembers, memberName);
						theMembers.add(new ManyToManyMember(memberName, field.getName(), (Class) rettype.getActualTypeArguments()[0], order));
					} else {
						Member mb = null;
						log.info("regular:" + memberName);
						if (field.getAnnotation(GeneratedValue.class) != null) {
							mb = new GeneratedValueMember(memberName, field.getName(), field.getType(), order);
						} else {
							mb = new SimpleMember(memberName, field.getName(), field.getType(), order);

							if (field.getAnnotation(Column.class) != null) {
								Column column = field.getAnnotation(Column.class);
								Long lenght = new Long(column.length());
								Long precision = new Long(column.precision());
								Long scale = new Long(column.scale());

								Boolean nullable = new Boolean(column.nullable());
								nullable = nullable == null ? !field.getAnnotation(Basic.class).optional() : nullable;

								mb.setLength(lenght);
								mb.setPrecision(precision);
								mb.setScale(scale);
								mb.setNullable(nullable);
							}
						}
						MetaDataUtil.removeMember(theMembers, memberName);
						theMembers.add(mb);

						if (field.getAnnotation(Id.class) != null) {
							log.debug("@id");
							log.info("primary key:" + memberName);
							metaData.setPrimaryKey(mb);
						} else if (field.getAnnotation(EmbeddedId.class) != null) {
							log.debug("@EmbeddedId");
							log.info("primary key:" + memberName);
							metaData.setPrimaryKey(mb);
						}// TODO El @IdClass se debe mirar que pasa cuando
						
					}
				}
			}
		}
	}

	/**
	 * Load meta data in method.
	 *
	 * @param clazz the clazz
	 * @param metaData the meta data
	 * @param theMembers the members
	 * @param memberName the member name
	 */
	private void loadMetaDataInMethod(Class clazz, MetaData metaData, List<Member> theMembers, String memberName)throws Exception {
		try {
			
			log.info("Read in Method:" + memberName);
			for (Method method : clazz.getDeclaredMethods()) {// Begin for Method
				int order = -1;
				String property = "";

				// Lee los metodos get o is
				if (method.getAnnotation(Transient.class) == null && method.getAnnotation(Override.class) == null) {
					if (method.getName().startsWith("is")) {
						property = method.getName().substring(3);
						property = method.getName().substring(2, 3).toLowerCase() + property;
					} else if (method.getName().startsWith("get")) {
						property = method.getName().substring(4);
						property = method.getName().substring(3, 4).toLowerCase() + property;
					}
				}

				// Empieza con los miembros

				if (memberName.equalsIgnoreCase(property)) {// Beging
					// memberName.equals(property)

					log.debug("working with:" + memberName);

					String showName = property;

					if (method.getAnnotation(OneToMany.class) != null) {
						// es one-to-many
						log.info("one to many:" + memberName);
						ParameterizedType rettype = (ParameterizedType) method.getGenericReturnType();
						log.debug(rettype.getActualTypeArguments()[0].toString());

						OneToManyMember oneToManyMember = new OneToManyMember(memberName, showName, (Class) rettype.getActualTypeArguments()[0], method
								.getReturnType(), order);
						oneToManyMember.setMappedBy(method.getAnnotation(OneToMany.class).mappedBy());

						theMembers.add(oneToManyMember);

					} else if (method.getAnnotation(OneToOne.class) != null) {
						// es one-to-many
						log.info("one to one:" + memberName);
						theMembers.add(new OneToOneMember(memberName, showName, method.getReturnType(), order));
					} else if (method.getAnnotation(ManyToOne.class) != null) {
						// many to one
						log.info("many to one:" + memberName);

						ManyToOneMember manyToOneMember = new ManyToOneMember(memberName, showName, method.getReturnType(), order);

						JoinColumn joinColumn = method.getAnnotation(JoinColumn.class);
						JoinColumns joinColumns = method.getAnnotation(JoinColumns.class);

						if (joinColumn != null) {

							Boolean nullable = joinColumn.nullable();
							nullable = nullable == null ? !method.getAnnotation(ManyToOne.class).optional() : nullable;

							HashMap<String, Boolean> hashMapNullableColumn = new HashMap<String, Boolean>();

							hashMapNullableColumn.put(property.toUpperCase(), nullable);

							manyToOneMember.setHashMapNullableColumn(hashMapNullableColumn);
						} else {
							if (joinColumns != null) {
								JoinColumn[] joinColumnFromColumns = joinColumns.value();
								HashMap<String, Boolean> hashMapNullableColumn = new HashMap<String, Boolean>();

								int cont = 0;
								for (JoinColumn joinColumn2 : joinColumnFromColumns) {

									String name = joinColumn2.name();

									String neededName = new String();

									try {
										if (name.split("_").length > 1) {
											String[] tmp = name.split("_");
											for (int i = 0; i < tmp.length; i++) {
												neededName = neededName + tmp[i];
											}
										} else {
											neededName = name;
										}
									} catch (Exception e) {
										neededName = name;
									}

									neededName = neededName.toUpperCase();

									Boolean nullable = joinColumn2.nullable();

									if (hashMapNullableColumn.get(neededName) == null) {
										hashMapNullableColumn.put(neededName, nullable);
									} else {
										hashMapNullableColumn.put(neededName + cont, nullable);
									}

									cont++;
								}
								manyToOneMember.setHashMapNullableColumn(hashMapNullableColumn);
							}
						}

						theMembers.add(manyToOneMember);
					} else if (method.getAnnotation(ManyToMany.class) != null) {
						// many to many
						log.info("many to many:" + memberName);
						ParameterizedType rettype = (ParameterizedType) method.getGenericReturnType();
						log.debug(rettype.getActualTypeArguments()[0].toString());
						theMembers.add(new ManyToManyMember(memberName, showName, (Class) rettype.getActualTypeArguments()[0], order));
					} else {

						Member mb = null;
						log.info("regular:" + memberName);
						if (method.getAnnotation(GeneratedValue.class) != null) {
							mb = new GeneratedValueMember(memberName, showName, method.getReturnType(), order);
						} else {
							mb = new SimpleMember(memberName, showName, method.getReturnType(), order);
							if (method.getAnnotation(Column.class) != null) {
								Column column = method.getAnnotation(Column.class);
								Long lenght = new Long(column.length());
								Long precision = new Long(column.precision());
								Long scale = new Long(column.scale());
								Boolean nullable = new Boolean(column.nullable());
								nullable = nullable == null ? !method.getAnnotation(Basic.class).optional() : nullable;

								mb.setLength(lenght);
								mb.setPrecision(precision);
								mb.setScale(scale);
								mb.setNullable(nullable);
							}

						}
						theMembers.add(mb);

						if (method.getAnnotation(Id.class) != null) {
							log.debug("@id");
							log.info("primary key:" + memberName);
							metaData.setPrimaryKey(mb);
						} else if (method.getAnnotation(EmbeddedId.class) != null) {

							log.debug("@EmbeddedId");
							log.info("primary key:" + memberName);
							metaData.setPrimaryKey(mb);
						}// TODO El @IdClass se debe mirar que pasa cuando sea
						// @IdClass
					}
					break;// rompe el ciclo si ya agrego el miembro a la lista es
					// para que sea mas rapido
				}// End memberName.equals(property)
			}// End for Method
			
		}catch (RuntimeException e) {
			throw e;
		}catch (Exception e) {
			throw e;
		}
		
	}
}
