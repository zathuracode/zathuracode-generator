package org.zcode.generator.factory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zcode.generator.exceptions.GeneratorNotFoundException;
import org.zcode.generator.model.GeneratorModel;
import org.zcode.generator.model.IZathuraGenerator;
import org.zcode.generator.utilities.GeneratorUtil;


/**
 * Zathura Generator.
 *
 * @author Diego Armando Gomez Mosquera (dgomez@vortexbird.com)
 * @version 1.0
 */
public class ZathuraGeneratorFactory {

	/** Log4j. */
	private static final Logger log = LoggerFactory.getLogger(ZathuraGeneratorFactory.class);

	/** xml file path. */
	private static String xmlConfigFactoryPath = GeneratorUtil.getXmlConfigFactoryPath();

	/** Generator Model. */
	private static HashMap<String, GeneratorModel> theZathuraGenerators = null;

	/** The names of generators. */
	private static java.util.List<String> generatorNames = new ArrayList<String>();

	static {
		try {
			loadZathuraGenerators();
		} catch (FileNotFoundException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		} catch (XMLStreamException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		} catch (InstantiationException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * The Constructor.
	 */
	private ZathuraGeneratorFactory() {

	}

	/**
	 * Creates the zathura generator.
	 *
	 * @param generatorName the generator name
	 * @return the i zathura generator
	 * @throws GeneratorNotFoundException the generator not found exception
	 */
	public static IZathuraGenerator createZathuraGenerator(String generatorName) throws GeneratorNotFoundException {
		IZathuraGenerator zathuraGenerator;
		if (generatorName == null || generatorName.equals("") == true) {
			throw new GeneratorNotFoundException();
		}
		GeneratorModel generatorModel = theZathuraGenerators.get(generatorName);
		if (generatorModel == null) {
			throw new GeneratorNotFoundException(generatorName);
		}
		zathuraGenerator = generatorModel.getZathuraGenerator();
		return zathuraGenerator;
	}

	/**
	 * Load zathura generators.
	 *
	 * @throws FileNotFoundException the file not found exception
	 * @throws XMLStreamException the XML stream exception
	 * @throws InstantiationException the instantiation exception
	 * @throws IllegalAccessException the illegal access exception
	 * @throws ClassNotFoundException the class not found exception
	 */
	public static void loadZathuraGenerators() throws FileNotFoundException, XMLStreamException, InstantiationException, IllegalAccessException,
			ClassNotFoundException {

		log.info("Reading:" + GeneratorUtil.getXmlConfigFactoryPath());

		GeneratorModel generatorModel = null;
		boolean boolName = false;
		boolean descriptionName = false;
		boolean className = false;
		boolean guiName = false;
		boolean persistence = false;
		boolean zathuraVersion = false;

		theZathuraGenerators = new HashMap<String, GeneratorModel>();

		// Get the factory instace first.
		XMLInputFactory factory = XMLInputFactory.newInstance();
		factory.setProperty(XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES, Boolean.TRUE);
		factory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, Boolean.FALSE);
		factory.setProperty(XMLInputFactory.IS_NAMESPACE_AWARE, Boolean.TRUE);
		factory.setProperty(XMLInputFactory.IS_COALESCING, Boolean.TRUE);

		log.debug("FACTORY: " + factory);

		// create the XMLEventReader, pass the filename for any relative
		// XMLEventReader r = factory.createXMLEventReader(new
		// FileInputStream(xmlConfigFactoryPath));

		// xmlConfigFactoryPath="config/zathura-generator-factory-config.xml";
		XMLEventReader r = factory.createXMLEventReader(new FileInputStream(xmlConfigFactoryPath));

		// iterate as long as there are more events on the input stream
		while (r.hasNext()) {
			XMLEvent e = r.nextEvent();
			if (e.isStartElement()) {
				StartElement startElement = (StartElement) e;
				QName qname = startElement.getName();
				String localName = qname.getLocalPart();
				if (localName.equals("generator") == true) {
					generatorModel = new GeneratorModel();
				} else if (localName.equals("name") == true) {
					boolName = true;
					log.debug(localName);
				} else if (localName.equals("description") == true) {
					descriptionName = true;
					log.debug(localName);
				} else if (localName.equals("gui-name") == true) {
					guiName = true;
					log.debug(localName);
				} else if (localName.equals("class") == true) {
					className = true;
					log.debug(localName);
				} else if (localName.equals("persistence") == true) {
					persistence = true;
					log.debug(localName);
				} else if (localName.equals("zathuraVersion") == true) {
					zathuraVersion = true;
					log.debug(localName);
				}
			} else if (e.isCharacters()) {
				Characters characters = (Characters) e;
				String cadena = characters.getData().toString().trim();
				if (boolName == true) {
					generatorModel.setName(cadena);
					generatorNames.add(cadena);
					boolName = false;
					log.debug(cadena);
				} else if (descriptionName == true) {
					generatorModel.setDescription(cadena);
					descriptionName = false;
					log.debug(cadena);
				} else if (guiName == true) {
					generatorModel.setGuiName(cadena);
					guiName = false;
					log.debug(cadena);
				} else if (className == true) {
					generatorModel.setZathuraGenerator((IZathuraGenerator) Class.forName(cadena).newInstance());
					className = false;
					log.debug(cadena);
				} else if (persistence == true) {
					generatorModel.setPersistence(cadena);
					persistence = false;
					log.debug(cadena);
				} else if (zathuraVersion == true) {
					generatorModel.setZathuraVersion(cadena);
					zathuraVersion = false;
					log.debug(cadena);
				}
			} else if (e.isEndElement() == true) {
				EndElement endElement = (EndElement) e;
				QName qname = endElement.getName();
				String localName = qname.getLocalPart();
				if (localName.equals("generator") == true) {
					theZathuraGenerators.put(generatorModel.getName(), generatorModel);
				}
			}
		}
		log.debug("Generator length:" + theZathuraGenerators.size());
	}

	/**
	 * Gets the the zathura generators.
	 *
	 * @return the the zathura generators
	 */
	public static HashMap<String, GeneratorModel> getTheZathuraGenerators() {
		return theZathuraGenerators;
	}

	/**
	 * Gets the generator names.
	 *
	 * @return the generator names
	 */
	public static java.util.List<String> getGeneratorNames() {
		return generatorNames;
	}

	/**
	 * Gets the generator name for gui name.
	 *
	 * @param guiName the gui name
	 * @return the generator name for gui name
	 */
	public static String getGeneratorNameForGuiName(String guiName) {
		for (GeneratorModel generatorModel : theZathuraGenerators.values()) {
			if (generatorModel.getGuiName().equals(guiName) == true) {
				return generatorModel.getName();
			}
		}
		return "";
	}
	
	public static String pathConfigXML(){
		return xmlConfigFactoryPath;
	}

}
