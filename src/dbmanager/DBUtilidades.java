package dbmanager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import comicManagement.ComicGradeo;
import funcionesAuxiliares.Utilidades;
import funcionesManagment.AccionReferencias;

public class DBUtilidades {

	private static AccionReferencias referenciaVentana = getReferenciaVentana();

	public enum TipoBusqueda {
		COMPLETA, ELIMINAR
	}

	public static void setParameters(PreparedStatement ps, ComicGradeo datos, boolean includeID) throws SQLException {
	    ps.setString(1, datos.getNomComic());
	    ps.setString(2, datos.getCodComic());
	    ps.setString(3, datos.getNumComic());
	    ps.setString(4, datos.getAnioComic());
	    ps.setString(5, datos.getColeccionComic());
	    ps.setString(6, datos.getEdicionComic());
	    ps.setString(7, datos.getEmpresaComic());
	    ps.setString(8, datos.getGradeoComic());
	    ps.setString(9, datos.getUrlReferenciaComic());
	    ps.setString(10, datos.getDireccionImagenComic());

	    if (includeID) {
	        ps.setString(11, datos.getIdComic());
	    }
	}


//	############################################
//	###########SELECT FUNCTIONS#################
//	############################################

	public static String construirSentenciaSQL(TipoBusqueda tipoBusqueda) {

		switch (tipoBusqueda) {
		case COMPLETA:
			return SelectManager.SENTENCIA_COMPLETA;
		default:
			throw new IllegalArgumentException("Tipo de búsqueda no válido");
		}
	}

	public static String datosConcatenados(ComicGradeo Comic) {
	    String connector = " WHERE ";

	    StringBuilder sql = new StringBuilder(SelectManager.SENTENCIA_BUSQUEDA_COMPLETA);

	    connector = agregarCondicion(sql, connector, "idComic", Comic.getIdComic());
	    connector = agregarCondicion(sql, connector, "nomComic", Comic.getNomComic());
	    connector = agregarCondicion(sql, connector, "codComic", Comic.getCodComic());
	    connector = agregarCondicion(sql, connector, "numComic", Comic.getNumComic());
	    connector = agregarCondicion(sql, connector, "anioComic", Comic.getAnioComic());
	    connector = agregarCondicion(sql, connector, "coleccionComic", Comic.getColeccionComic());
	    connector = agregarCondicion(sql, connector, "edicionComic", Comic.getEdicionComic());
	    connector = agregarCondicion(sql, connector, "empresaComic", Comic.getEmpresaComic());
	    connector = agregarCondicionLike(sql, connector, "gradeoComic", Comic.getGradeoComic());

	    if (connector.trim().equalsIgnoreCase("WHERE")) {
	        return "";
	    }

	    return (connector.length() > 0) ? sql.toString() : "";
	}

	public static String agregarCondicion(StringBuilder sql, String connector, String columna, String valor) {
		if (!valor.isEmpty()) {
			sql.append(connector).append(columna).append(" = '").append(valor).append("'");
			return " AND ";
		}
		return connector;
	}

	public static String agregarCondicionLike(StringBuilder sql, String connector, String columna, String valor) {
		if (!valor.isEmpty()) {
			sql.append(connector).append(columna).append(" LIKE '%").append(valor).append("%'");
			return " AND ";
		}
		return connector;
	}

	public static String datosGenerales(String tipoBusqueda, String busquedaGeneral) {
	    String connector = " WHERE ";
	    StringBuilder sql = new StringBuilder();
	    sql.append("SELECT * FROM albumbbdd");

	    switch (tipoBusqueda.toLowerCase()) {
	    case "nomComic":
	        sql.append(connector).append("nomComic LIKE '%" + busquedaGeneral + "%';");
	        break;
	    case "coleccionComic":
	        sql.append(connector).append("coleccionComic LIKE '%" + busquedaGeneral + "%';");
	        break;
	    case "editorialComic":
	        sql.append(connector).append("empresaComic LIKE '%" + busquedaGeneral + "%';");
	        break;
	    case "codComic":
	        sql.append(connector).append("codComic LIKE '%" + busquedaGeneral + "%';");
	        break;
	    default:
	        break;
	    }

	    return sql.toString();
	}


	/**
	 * Funcion que permite hacer una busqueda general mediante 1 sola palabra, hace
	 * una busqueda en ciertos identificadores de la tabla.
	 *
	 * @param sentencia
	 * @return
	 * @throws SQLException
	 */
	public static List<ComicGradeo> verBusquedaGeneral(String busquedaGeneral) {
		String sql1 = datosGenerales("nomComic", busquedaGeneral);
		String sql2 = datosGenerales("coleccionComic", busquedaGeneral);
		String sql3 = datosGenerales("edicionComic", busquedaGeneral);
		try (Connection conn = ConectManager.conexion();
				PreparedStatement ps1 = conn.prepareStatement(sql1);
				PreparedStatement ps2 = conn.prepareStatement(sql2);
				PreparedStatement ps3 = conn.prepareStatement(sql3);

				ResultSet rs1 = ps1.executeQuery();
				ResultSet rs2 = ps2.executeQuery();
				ResultSet rs3 = ps3.executeQuery()) {

			ListasComicsDAO.listaComics.clear();

			agregarSiHayDatos(rs1);
			agregarSiHayDatos(rs2);
			agregarSiHayDatos(rs3);

			ListasComicsDAO.listaComics = ListasComicsDAO.listaArreglada(ListasComicsDAO.listaComics);
			return ListasComicsDAO.listaComics;

		} catch (SQLException ex) {
			Utilidades.manejarExcepcion(ex);
		}

		return Collections.emptyList();
	}

	/**
	 * Agrega el primer conjunto de resultados a la lista de cómics si hay datos en
	 * el ResultSet.
	 * 
	 * @param rs El ResultSet que contiene los resultados de la consulta.
	 * @throws SQLException Si se produce un error al acceder a los datos del
	 *                      ResultSet.
	 */
	public static void agregarSiHayDatos(ResultSet rs) throws SQLException {
		if (rs.next()) {
			obtenerComicDesdeResultSet(rs);
		}
	}

	/**
	 * Filtra y devuelve una lista de cómics de la base de datos según los datos
	 * proporcionados.
	 *
	 * @param datos Objeto Comic con los datos para filtrar.
	 * @return Lista de cómics filtrados.
	 */
	public static List<ComicGradeo> filtroBBDD(ComicGradeo datos, String busquedaGeneral) {

		// Reiniciar la lista de cómics antes de realizar el filtrado
		ListasComicsDAO.listaComics.clear();

		// Crear la consulta SQL a partir de los datos proporcionados
		String sql = datosConcatenados(datos);

		// Verificar si la consulta SQL no está vacía
		if (!sql.isEmpty()) {
			try (Connection conn = ConectManager.conexion();
					PreparedStatement ps = conn.prepareStatement(sql);
					ResultSet rs = ps.executeQuery()) {

				// Llenar la lista de cómics con los resultados obtenidos
				while (rs.next()) {
					ListasComicsDAO.listaComics.add(obtenerComicDesdeResultSet(rs));
				}

				return ListasComicsDAO.listaComics;
			} catch (SQLException ex) {
				// Manejar la excepción según tus necesidades (en este caso, mostrar una alerta)
				Utilidades.manejarExcepcion(ex);
			}
		} else {
			getReferenciaVentana().getProntInfoTextArea().setOpacity(1);
			// Show error message in red when no search fields are specified
			getReferenciaVentana().getProntInfoTextArea().setStyle("-fx-text-fill: red;");
			getReferenciaVentana().getProntInfoTextArea()
					.setText("Error No existe Comic con los datos: " + busquedaGeneral + datos.toString());
		}

		if (sql.isEmpty() && busquedaGeneral.isEmpty()) {
			getReferenciaVentana().getProntInfoTextArea().setOpacity(1);
			// Show error message in red when no search fields are specified
			getReferenciaVentana().getProntInfoTextArea().setStyle("-fx-text-fill: red;");
			getReferenciaVentana().getProntInfoTextArea().setText("Todos los campos estan vacios");
		}

		return ListasComicsDAO.listaComics;
	}

	/**
	 * Devuelve una lista de valores de una columna específica de la base de datos.
	 *
	 * @param columna Nombre de la columna de la base de datos.
	 * @return Lista de valores de la columna.
	 * @throws SQLException Si ocurre un error en la consulta SQL.
	 */
	public static List<String> obtenerValoresColumna(String columna) {
		String sentenciaSQL = "SELECT " + columna + " FROM comicsGbbdd ORDER BY " + columna + " ASC;";
		
		ListasComicsDAO.listaComics.clear();
		return ListasComicsDAO.guardarDatosAutoCompletado(sentenciaSQL, columna);
	}

	/**
	 * Crea y devuelve un objeto Comic a partir de los datos del ResultSet.
	 * 
	 * @param rs El ResultSet que contiene los datos del cómic.
	 * @return Un objeto Comic con los datos obtenidos del ResultSet.
	 * @throws SQLException Si se produce un error al acceder a los datos del
	 *                      ResultSet.
	 */
	public static ComicGradeo obtenerComicDesdeResultSet(ResultSet rs) {
	    try {
	        String idComic = rs.getString("idComic");
	        String tituloComic = rs.getString("tituloComic");
	        String codigoComic = rs.getString("codigoComic");
	        String numeroComic = rs.getString("numeroComic");
	        String fechaGradeo = rs.getString("fechaGradeo");
	        String anioPublicacion = rs.getString("anioPublicacion");
	        String editorComic = rs.getString("editorComic");
	        String tipoVariante = rs.getString("tipoVariante");
	        String gradeoComic = rs.getString("gradeoComic");
	        String keyComentarios = rs.getString("keyComentarios");
	        String artistaComic = rs.getString("artistaComic");
	        String guionistaComic = rs.getString("guionistaComic");
	        String varianteComic = rs.getString("varianteComic");
	        String direccionImagenComic = rs.getString("direccionImagenComic");
	        String urlReferenciaComic = rs.getString("urlReferenciaComic");

	        return new ComicGradeo.ComicGradeoBuilder(idComic, tituloComic)
	                .codigoComic(codigoComic)
	                .numeroComic(numeroComic)
	                .fechaGradeo(fechaGradeo)
	                .anioPublicacion(anioPublicacion)
	                .editorComic(editorComic)
	                .tipoVariante(tipoVariante)
	                .gradeoComic(gradeoComic)
	                .keyComentarios(keyComentarios)
	                .artistaComic(artistaComic)
	                .guionistaComic(guionistaComic)
	                .varianteComic(varianteComic)
	                .direccionImagenComic(direccionImagenComic)
	                .urlReferenciaComic(urlReferenciaComic)
	                .build();
	    } catch (SQLException e) {
	        Utilidades.manejarExcepcion(e);
	        return null;
	    }
	}



	public static AccionReferencias getReferenciaVentana() {
		return referenciaVentana;
	}

	public static void setReferenciaVentana(AccionReferencias referenciaVentana) {
		DBUtilidades.referenciaVentana = referenciaVentana;
	}
}