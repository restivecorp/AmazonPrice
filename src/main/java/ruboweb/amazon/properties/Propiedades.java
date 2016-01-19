package ruboweb.amazon.properties;

import java.util.Map;

/**
 * Clase para trabajar con las propiedades de los ficheros
 */
public final class Propiedades {

	/**
	 * Instancia del objeto
	 */
	private static Propiedades instancia;

	private Map<String, String> tags;

	/**
	 * Constructor privado para evitar multiples instancias
	 */
	private Propiedades() {
		this.tags = Reader.cargarFicheroSS("tags.properties");
	}

	/**
	 * Devuelve la unica instancia posible del objeto
	 * 
	 * @return Instancia unica del objeto
	 */
	public static Propiedades getInstancia() {
		if (Propiedades.instancia == null) {
			Propiedades.instancia = new Propiedades();
		}
		return Propiedades.instancia;
	}

	/**
	 * Permite obtener el valor dad su clave
	 * 
	 * @param tag
	 *            Clave del mapa
	 * 
	 * @return Valor en el mapa
	 */
	public String getTag(String tag) {
		return this.tags.get(tag);
	}

	public String getToken() {
		return this.tags.get(Literales.TOKEN);
	}

}
