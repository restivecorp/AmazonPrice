package ruboweb.amazon.properties;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * Clase para trabajar con ficheros de propiedades
 */
public final class Reader {
	private final static Logger log = Logger.getLogger(Reader.class);

	/**
	 * Constructor, por defecto, de la clase
	 */
	private Reader() {

	}

	/**
	 * Metodo que carga en memora el fichero de propiedades
	 * 
	 * @param ruta Direccion del fichero
	 * 
	 * @return Mapa con las clave-valor del fichero
	 */
	public static Map<String, String> cargarFicheroSS(String ruta) {
		Map<String, String> retValue = new HashMap<String, String>();

		Properties prop = new Properties();
		InputStream is = null;

		try {
			is = new FileInputStream(ruta);
			prop.load(is);
		} catch (IOException ioe) {
			Reader.log.error("ERROR en el rastreo: " + ioe.getMessage() + "\n");
		}

		Object obj = null;
		for (Enumeration<Object> e = prop.keys(); e.hasMoreElements();) {
			obj = e.nextElement();
			retValue.put(obj.toString(), prop.getProperty(obj.toString()));
		}

		return retValue;
	}

}
