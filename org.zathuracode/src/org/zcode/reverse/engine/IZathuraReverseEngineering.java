package org.zcode.reverse.engine;

import java.util.List;
import java.util.Properties;

// TODO: Auto-generated Javadoc
/**
 * Zathura Generator.
 *
 * @author Diego Armando Gomez Mosquera (dgomez@vortexbird.com)
 * @author William Altuzarra Noriega Noriega (williamaltu@gmail.com)
 * @version 1.0
 */
public interface IZathuraReverseEngineering {
	
	/**
	 * Make pojos jp a_ v1_0.
	 *
	 * @param connectionProperties the connection properties
	 * @param tables the tables
	 */
	public void makePojosJPA_V1_0(Properties connectionProperties, List<String> tables)throws Exception;
}
