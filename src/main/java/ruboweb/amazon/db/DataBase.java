package ruboweb.amazon.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import ruboweb.amazon.model.Producto;
import ruboweb.amazon.properties.Literales;

public class DataBase implements Literales {
	private Connection conexion;

	/**
	 * Instancia del objeto
	 */
	private static DataBase instancia;

	private DataBase() {

	}

	/**
	 * Devuelve la unica instancia posible del objeto
	 * 
	 * @return Instancia unica del objeto
	 */
	public static DataBase getInstancia() {
		if (DataBase.instancia == null) {
			DataBase.instancia = new DataBase();
		}
		return DataBase.instancia;
	}

	public List<String> getDeseos() {
		List<String> retValue = new ArrayList<String>();

		PreparedStatement ps;
		try {

			ps = this.conexion.prepareStatement(Literales.QUERY_DESEOS);

			ps.execute();
			final ResultSet rs = ps.getResultSet();

			while (rs.next()) {
				retValue.add(rs.getString(1));

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return retValue;
	}

	public String getPrecioProducto(String codigo) {
		String retValue = "-1";

		PreparedStatement ps;
		try {
			ps = this.conexion
					.prepareStatement(Literales.QUERY_PRECIO_POR_CODIGO);
			ps.setString(1, codigo);
			ps.setString(2, codigo);
			ps.execute();

			final ResultSet rs = ps.getResultSet();
			while (rs.next()) {
				retValue = rs.getString(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return retValue;
	}

	public void insertarProducto(Producto p) {
		PreparedStatement ps;
		try {
			ps = this.conexion
					.prepareStatement(Literales.QUERY_INSERT_PRODUCTO);
			ps.setString(1, p.getCodigo());
			ps.setString(2, p.getDescripcion());
			ps.setString(3, p.getUrl());
			ps.setDate(4, new java.sql.Date(p.getFecha().getTime()));
			ps.setString(5, p.getPrecio());

			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Crea la conexion
	 */
	public void abrirConexion() {
		try {
			Class.forName("org.hsqldb.jdbcDriver");
			this.conexion = DriverManager
					.getConnection("jdbc:hsqldb:file:db/apricedb");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Cierra la conexion
	 */
	public void cerrarConexion() {
		try {
			if (this.conexion != null) {
				Statement st = this.conexion.createStatement();
				st.execute("shutdown");
				this.conexion.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			this.conexion = null;
		}
	}
}
