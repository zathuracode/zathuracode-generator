package org.zcode.reverse.utilities;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


// TODO: Auto-generated Javadoc
/**
 * The Class DatabaseUtilities.
 *
 * @author Diego
 */
public class DatabaseUtilities {
	
    private static final Logger log = LoggerFactory.getLogger(DatabaseUtilities.class);

	/**
	 * Consulta los catalogos de una base de datos.
	 *
	 * @param connection the connection
	 * @return the catalogs
	 * @throws SQLException the SQL exception
	 */
	public static List<String> getCatalogs(Connection connection) throws SQLException {
		DatabaseMetaData databaseMetaData = null;
		ResultSet resultSet = null;
		List<String> catalogsList = null;

		try {
			databaseMetaData = connection.getMetaData();
			resultSet = databaseMetaData.getCatalogs();
			catalogsList = new ArrayList<String>();
			while (resultSet.next()) {
				catalogsList.add(resultSet.getString(1));
				log.info("Catalog:"+resultSet.getString(1));
			}
			return catalogsList;
		} catch (SQLException e) {
			throw e;
		} finally {
			if (resultSet != null) {
				resultSet.close();
			}
		}
	}

	/**
	 * Consulta los schemas de una base de datos.
	 *
	 * @param connection the connection
	 * @return the schemas
	 * @throws SQLException the SQL exception
	 * @throws Exception the exception
	 */
	public static List<String> getSchemas(Connection connection) throws SQLException, Exception {
		DatabaseMetaData databaseMetaData = null;
		ResultSet resultSet = null;
		Map<String, List<String>> schemasMap = null;
		List<String> schemasList = null;
		try {
			databaseMetaData = connection.getMetaData();
			resultSet = databaseMetaData.getSchemas();
			schemasMap = new HashMap<String, List<String>>();

			while (resultSet.next()) {
				String schema = resultSet.getString(1);
				String catalog = null;
				if (resultSet.getMetaData().getColumnCount() > 1) {
					catalog = resultSet.getString(2);
					
				}

				schemasList = (List<String>) schemasMap.get(catalog);
				if (schemasList == null) {
					schemasList = new ArrayList<String>();
					schemasMap.put(catalog, schemasList);
				}
				schemasList.add(schema);
			}

			Collection<List<String>> collections = schemasMap.values();
			schemasList = new ArrayList<String>();
			for (List<String> list : collections) {
				for (String schemasName : list) {
					schemasList.add(schemasName);
					log.info("Schema:"+schemasName);
				}
			}

			return schemasList;
		} finally {
			if (resultSet != null) {
				resultSet.close();
			}
		}
	}

	/**
	 * Consulta las tablas de una base de datos por catalogo o schema.
	 *
	 * @param connection the connection
	 * @param catalog the catalog
	 * @param schema the schema
	 * @param tablePattern the table pattern
	 * @return the tables
	 * @throws SQLException the SQL exception
	 */
	public static List<String> getTables(Connection connection, String catalog, String schema, String tablePattern) throws SQLException {
		DatabaseMetaData databaseMetaData = null;
		ResultSet resultSet = null;
		List<String> tableList = null;
		try {
			if (tablePattern == null || tablePattern.equals("") == true) {
				tablePattern = "%";
			}
			databaseMetaData = connection.getMetaData();
			resultSet = databaseMetaData.getTables(catalog, schema, tablePattern, new String[] { "TABLE", "VIEW", "SYNONYM", "ALIAS" });
			tableList = new ArrayList<String>();
			while (resultSet.next()) {
				tableList.add(resultSet.getString(3));
				log.info("Table:"+resultSet.getString(3));
			}
			return tableList;
		} finally {
			if (resultSet != null)
				resultSet.close();
		}
	}

	/**
	 * Consulta las ForeignKey de una tabla.
	 *
	 * @param connection the connection
	 * @param catalog the catalog
	 * @param schema the schema
	 * @param table the table
	 * @return the foreign key columns
	 * @throws SQLException the SQL exception
	 */
	public static Set<String> getForeignKeyColumns(Connection connection, String catalog, String schema, String table) throws SQLException {
		DatabaseMetaData databaseMetaData = connection.getMetaData();
		ResultSet resultSet = null;
		HashSet<String> columns = null;
		try {
			resultSet = databaseMetaData.getImportedKeys(catalog, schema, table);
			columns = new HashSet<String>();
			while (resultSet.next()) {
				columns.add(resultSet.getString(8));
			}
			return columns;
		} finally {
			if (resultSet != null)
				resultSet.close();
		}
	}
}
