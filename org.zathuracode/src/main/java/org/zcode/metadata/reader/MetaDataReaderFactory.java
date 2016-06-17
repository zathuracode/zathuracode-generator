package org.zcode.metadata.reader;

import org.zcode.metadata.exceptions.MetaDataReaderNotFoundException;

// TODO: Auto-generated Javadoc
/**
 * Zathura Generator.
 *
 * @author Diego Armando Gomez Mosquera (dgomez@vortexbird.com)
 * @version 1.0
 */
public class MetaDataReaderFactory {

	/** The Constant JPAEntityLoaderEngine. */
	public final static int JPAEntityLoaderEngine = 1;

	/**
	 * The Constructor.
	 */
	private MetaDataReaderFactory() {
	}

	/**
	 * Creates the meta data reader.
	 *
	 * @param metaDataReader the meta data reader
	 * @return the i meta data reader
	 * @throws MetaDataReaderNotFoundException the meta data reader not found exception
	 */
	public static IMetaDataReader createMetaDataReader(int metaDataReader) throws MetaDataReaderNotFoundException {

		switch (metaDataReader) {
		case 1: {
			return new org.zcode.metadata.engine.JPAEntityLoaderEngine();
		}
		default: {
			throw new MetaDataReaderNotFoundException();
		}
		}
	}

}
