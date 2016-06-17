package org.zcode.generator.exceptions;

// TODO: Auto-generated Javadoc
/**
 * Zathura Generator.
 *
 * @author Diego Armando Gomez Mosquera (dgomez@vortexbird.com)
 * @version 1.0
 */
public class GeneratorNotFoundException extends Exception {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -8547472738527169792L;

	/**
	 * The Constructor.
	 */
	public GeneratorNotFoundException() {
		super("The generator name is not optional");
	}

	/**
	 * The Constructor.
	 *
	 * @param generatorName the generator name
	 */
	public GeneratorNotFoundException(String generatorName) {
		super("The generator " + generatorName + " no Found");
	}
}