package ruboweb.amazon.properties;

/**
 * Interface para agrupar todos los lieterales de la aplicacion
 * 
 */
public interface Literales {
	// Configuracion
	public String CFG_MX_TIMEOUT = "max_timeout";

	// HTML TAGS
	public String HTML_ID = "id";
	public String HTML_HREF = "href";
	public String HTML_NOMBRE = "nombre";
	public String HTML_PRECIO = "precio";
	
	// Queries
	public String QUERY_DESEOS = "select * from public.deseo";
	public String QUERY_PRECIO_POR_CODIGO = "select p.precio from producto p where p.fecha=(select max(fecha) from producto where codigo = ?) and codigo = ?";
	public String QUERY_INSERT_PRODUCTO = "INSERT INTO PRODUCTO VALUES(?, ?, ?, ?, ?)";

	public String CANAL = "canal";
	public String TOKEN = "token";
	
}
