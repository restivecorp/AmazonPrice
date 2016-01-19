package ruboweb.amazon.service;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import ruboweb.amazon.db.DataBase;
import ruboweb.amazon.model.ProdcutoModificado;
import ruboweb.amazon.model.Producto;
import ruboweb.amazon.properties.Literales;
import ruboweb.amazon.properties.Propiedades;

public class ScraperService {
	private final static Logger log = Logger.getLogger(ScraperService.class);
	private Propiedades p = Propiedades.getInstancia();
	private DataBase db = DataBase.getInstancia();

	public ScraperService() {

	}

	public void rastrearProductos() {
		try {
			// 1- abrir la conexion
			this.db.abrirConexion();

			// 2- obtener las listas de deseos
			List<String> listas = this.obtenerDeseos();

			// 3- obtener los precios productos de cada lista
			List<Producto> productos = null;
			for (String s : listas) {
				ScraperService.log.info("Iniciando el rastreo de productos ("
						+ s + ")...");
				productos = this.obtenerPrecios(s);

				// 4- mostrar los productos de cada lista
				for (Producto p : productos) {
					ScraperService.log.info(p.toInfo());
				}

				ScraperService.log.info("Finalizado el rastreo de productos ("
						+ s + ")\n");

				// 5- actualizar los productos
				List<ProdcutoModificado> productosModificados = new ArrayList<>();
				ProdcutoModificado pm = null;
				for (Producto p : productos) {
					pm = this.actualizarProducto(p);

					if (pm != null) {
						productosModificados.add(pm);
					}
				}
				// 6- notificar los cambios de precio
				this.notificar(productosModificados, s);
			}
		} finally {
			// 7- cerrar la conexion
			this.db.cerrarConexion();
		}
	}

	private List<String> obtenerDeseos() {
		return this.db.getDeseos();
	}

	private List<Producto> obtenerPrecios(String lista) {
		List<Producto> productos = new ArrayList<Producto>();

		try {
			// Acceso a la lista
			Document doc = Jsoup
					.connect(lista)
					.userAgent("Mozilla")
					.timeout(
							Integer.parseInt(p.getTag(Literales.CFG_MX_TIMEOUT)))
					.get();

			// Tags donde se guardan los deseos
			List<Element> elementos = doc.getElementsByAttributeValueStarting(
					p.getTag(Literales.HTML_ID),
					p.getTag(Literales.HTML_NOMBRE));

			Producto prod = null;

			for (Element e : elementos) {
				prod = new Producto();

				prod.setCodigo(e.attr(Literales.HTML_ID).replaceAll(
						this.p.getTag(Literales.HTML_NOMBRE), ""));
				prod.setDescripcion(e.text());
				prod.setUrl(this.obtenerDominio(lista)
						+ e.attr(Literales.HTML_HREF));
				prod.setFecha(new Date(System.currentTimeMillis()));
				prod.setPrecio(this.parsearPrecio(doc
						.getElementById(
								this.p.getTag(Literales.HTML_PRECIO)
										+ prod.getCodigo()).text()));
				productos.add(prod);
			}

		} catch (IOException e) {
			ScraperService.log.error("ERROR en el rastreo: " + e.getMessage()
					+ "\n");
		}

		return productos;
	}

	private String parsearPrecio(String precio) {
		return precio.replaceAll("EUR ", "");
	}

	private String obtenerDominio(String lista) {
		String trozos[] = lista.split("/");

		return trozos[0] + "//" + trozos[2];
	}

	private ProdcutoModificado actualizarProducto(Producto p) {
		String precio = this.db.getPrecioProducto(p.getCodigo());
		boolean cambio = true;
		if (precio.equals("-1")) {
			cambio = true;
		}

		if (precio.equals(p.getPrecio())) {
			cambio = false;
		}

		if (cambio) {
			this.insertarCambioPrecio(p);
			ProdcutoModificado pm = new ProdcutoModificado(p);
			pm.setPrecioOld(precio);
			pm.setPrecioNew(p.getPrecio());
			return pm;
		}
		return null;

	}

	private void insertarCambioPrecio(Producto p) {
		this.db.insertarProducto(p);
	}

	private void notificar(List<ProdcutoModificado> productos, String lista) {
		lista = this.truncarLista(lista);

		boolean flag = false;

		if (productos != null && productos.size() > 0) {
			flag = true;
			System.out.println("\tProductos que han cambiado de precio:");

			for (ProdcutoModificado pm : productos) {
				System.out.println(pm.toInfo());
			}
		} else {
			flag = false;
			System.out.println("\tNo hay cambios de precio en los productos.");
		}

		try {
			String mensaje = "";
			if (flag) {
				mensaje = "Hay productos que han cambiado de precio en la lista: "
						+ lista;
			} else {
				mensaje = "No existen variaciones de precio en la lista: "
						+ lista;
			}

			this.sendPush(mensaje);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private String truncarLista(String nombre) {
		try {
			String trozos[] = nombre.split("/");
			return trozos[trozos.length - 1];
		} catch (Exception e) {
			return nombre;
		}
	}

	private void sendPush(String mensaje) throws Exception {

		Calendar hoy = GregorianCalendar.getInstance();
		hoy.add(Calendar.DATE, 2);

		String fecha = hoy.get(Calendar.YEAR) + "-"
				+ (hoy.get(Calendar.MONTH) + 1) + "-"
				+ hoy.get(Calendar.DAY_OF_MONTH);

		String url = "http://api.pushetta.com/api/pushes/"
				+ this.p.getTag(Literales.CANAL) + "/";
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// set reuqest header
		con.setRequestMethod("POST");
		con.setRequestProperty("Host", "api.pushetta.com");
		con.setRequestProperty("User-Agent", "Mozilla/5.0");
		con.setRequestProperty("Authorization", "Token " + this.p.getToken());
		con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
		con.setRequestProperty("Content-Type", "application/json");

		// API parameters
		String urlParameters = "{ \"body\" : \"" + mensaje
				+ "\", \"message_type\" : \"text/plain\", \"expire\" : \""
				+ fecha + "\"  }";

		// Send post request
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(urlParameters);
		wr.flush();
		wr.close();

		con.getResponseCode();
		con.getResponseMessage();
	}
}
