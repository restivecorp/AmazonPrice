package ruboweb.amazon.model;

public class ProdcutoModificado {

	private String codigo;
	private String url;
	private String descripcion;

	private String precioOld;
	private String precioNew;

	public ProdcutoModificado() {

	}

	public ProdcutoModificado(Producto p) {
		this.codigo = p.getCodigo();
		this.url = p.getUrl();
		this.descripcion = p.getDescripcion();
	}

	/**
	 * @return the codigo
	 */
	public String getCodigo() {
		return codigo;
	}

	/**
	 * @param codigo
	 *            the codigo to set
	 */
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url
	 *            the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the descripcion
	 */
	public String getDescripcion() {
		return descripcion;
	}

	/**
	 * @param descripcion
	 *            the descripcion to set
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	/**
	 * @return the precioOld
	 */
	public String getPrecioOld() {
		return precioOld;
	}

	/**
	 * @param precioOld
	 *            the precioOld to set
	 */
	public void setPrecioOld(String precioOld) {
		this.precioOld = precioOld;
	}

	/**
	 * @return the precioNew
	 */
	public String getPrecioNew() {
		return precioNew;
	}

	/**
	 * @param precioNew
	 *            the precioNew to set
	 */
	public void setPrecioNew(String precioNew) {
		this.precioNew = precioNew;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProdcutoModificado other = (ProdcutoModificado) obj;
		if (codigo == null) {
			if (other.codigo != null)
				return false;
		} else if (!codigo.equals(other.codigo))
			return false;
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ProdcutoModificado [codigo=" + codigo + ", url=" + url
				+ ", descripcion=" + descripcion + ", precioOld=" + precioOld
				+ ", precioNew=" + precioNew + "]";
	}

	public String toInfo() {
		return "\t" + this.codigo + ": " + this.descripcion + "\n\t\t Antes: "
				+ this.precioOld + "\n\t\t Ahora: " + this.precioNew + "\n";
	}
}
