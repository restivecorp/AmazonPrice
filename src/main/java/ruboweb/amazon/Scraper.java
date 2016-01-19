/**
 * 
 */
package ruboweb.amazon;

import org.apache.log4j.xml.DOMConfigurator;

import ruboweb.amazon.properties.Literales;
import ruboweb.amazon.service.ScraperService;

/**
 * Clase para recuperar la informacion de las listas de deseos de amazon
 * 
 */
public class Scraper implements Literales {

	/**
	 * Constructor de la clase
	 */
	public Scraper() {
		System.out.println("Comienza el rastreo de productos...");
		new ScraperService().rastrearProductos();
		System.out.println("Fin del rastreo.");
	}

	/**
	 * Lanzador de la aplicacion
	 */
	public static void main(String[] args) {
		DOMConfigurator.configure("log/log4j-config.xml");
		new Scraper();
	}

}
