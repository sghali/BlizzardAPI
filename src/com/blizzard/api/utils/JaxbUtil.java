package com.blizzard.api.utils;

import java.io.File;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
//import org.apache.log4j.Logger;




/**
 * This class is used for Unmarshalling XML to Java Object
 * @author sghali
 * 
 */
public class JaxbUtil {
	//private static final Logger logger = Logger.getLogger(JaxbUtil.class);

	/**
	 * Method to convert XMl to Java entity
	 * 
	 * @param xmlInput
	 *            - Input xml to be converted to Java
	 * @return - Java object
	 */
	public static Object unMarshal(String xmlInput, Class<?> classs) {

		try {
			File file = new File(xmlInput);
			JAXBContext jaxbContext = JAXBContext.newInstance(classs);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			Object returnJavaObject = jaxbUnmarshaller.unmarshal(file);
			return returnJavaObject;
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return null;
	}

	
	/**
	 * Method to convert Java entity to XML
	 * 
	 * @param javaObject
	 *            - Object to unmarshall
	 * @return String - XML String
	 */
	public static String marshal(Object javaObject) {
		return null;
	}

}
