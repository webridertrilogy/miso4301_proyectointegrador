package clusterer;

/**
 * 
 * Define la tabla y el tipo de operacion que se realiza sobre dicha tabla
 * relacionada a una clase.
 *
 */
public class RelacionTabla {
	private String tabla;
	private String tipoRelacion; // CRUD: create, read, update, delete

	public RelacionTabla(String tabla, String tipoRelacion) {
		super();
		this.tabla = tabla;
		this.tipoRelacion = tipoRelacion;
	}

	public String getTabla() {
		return tabla;
	}

	public void setTabla(String tabla) {
		this.tabla = tabla;
	}

	public String getTipoRelacion() {
		return tipoRelacion;
	}

	public void setTipoRelacion(String tipoRelacion) {
		this.tipoRelacion = tipoRelacion;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((tabla == null) ? 0 : tabla.hashCode());
		result = prime * result + ((tipoRelacion == null) ? 0 : tipoRelacion.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RelacionTabla other = (RelacionTabla) obj;
		if (tabla == null) {
			if (other.tabla != null)
				return false;
		} else if (!tabla.equals(other.tabla))
			return false;
		if (tipoRelacion == null) {
			if (other.tipoRelacion != null)
				return false;
		} else if (!tipoRelacion.equals(other.tipoRelacion))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "[tabla=" + tabla + ", tipoRelacion=" + tipoRelacion + "]";
	}
	
	

}
