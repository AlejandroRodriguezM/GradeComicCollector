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
		// Establecer parámetros para los campos de ComicGradeo
		ps.setString(1, datos.getTituloComic()); // título del cómic
		ps.setString(2, datos.getCodigoComic()); // código del cómic
		ps.setString(3, datos.getNumeroComic()); // número del cómic
		ps.setString(4, datos.getFechaGradeo()); // fecha de gradeo
		ps.setString(5, datos.getEditorComic()); // editor del cómic
		ps.setString(6, datos.getGradeoComic()); // gradeo del cómic
		ps.setString(7, datos.getKeyComentarios()); // key de comentarios
		ps.setString(8, Utilidades.corregirNombre(datos.getFirmaComic())); // firma del cómic
		ps.setString(9, datos.getValorComic()); // valor del cómic
		ps.setString(10, Utilidades.corregirNombre(datos.getArtistaComic())); // artista del cómic
		ps.setString(11, Utilidades.corregirNombre(datos.getGuionistaComic())); // guionista del cómic
		ps.setString(12, Utilidades.corregirNombre(datos.getVarianteComic())); // variante del cómic
		ps.setString(13, datos.getDireccionImagenComic()); // dirección de la imagen
		ps.setString(14, datos.getUrlReferenciaComic()); // URL de referencia

		if (includeID) {
			ps.setString(15, datos.getIdComic()); // ID del cómic (note: this should match the query if required)
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

	public static String datosConcatenados(ComicGradeo comic) {
		String connector = " WHERE ";

		// Construir la consulta SQL base
		StringBuilder sql = new StringBuilder(SelectManager.SENTENCIA_BUSQUEDA_COMPLETA);

		// Agregar condiciones para cada campo del cómic
		connector = agregarCondicion(sql, connector, "idComic", comic.getIdComic());
		connector = agregarCondicionLike(sql, connector, "tituloComic", comic.getTituloComic());
		connector = agregarCondicion(sql, connector, "codigoComic", comic.getCodigoComic());
		connector = agregarCondicion(sql, connector, "numeroComic", comic.getNumeroComic());
		connector = agregarCondicion(sql, connector, "fechaGradeo", comic.getFechaGradeo());
		connector = agregarCondicion(sql, connector, "editorComic", comic.getEditorComic());
		connector = agregarCondicion(sql, connector, "gradeoComic", comic.getGradeoComic());
		connector = agregarCondicion(sql, connector, "keyComentarios", comic.getKeyComentarios());
		connector = agregarCondicionLike(sql, connector, "artistaComic", comic.getArtistaComic());
		connector = agregarCondicionLike(sql, connector, "guionistaComic", comic.getGuionistaComic());
		connector = agregarCondicionLike(sql, connector, "varianteComic", comic.getVarianteComic());
		connector = agregarCondicion(sql, connector, "direccionImagenComic", comic.getDireccionImagenComic());
		connector = agregarCondicion(sql, connector, "urlReferenciaComic", comic.getUrlReferenciaComic());

		// Si no se ha agregado ninguna condición, devolver una cadena vacía
		if (connector.trim().equalsIgnoreCase("WHERE")) {
			return "";
		}

		// Devolver la consulta SQL con las condiciones agregadas
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
		sql.append("SELECT * FROM comicsGbbdd");

		switch (tipoBusqueda.toLowerCase()) {
		case "nomComic":
			sql.append(connector).append("nomComic LIKE '%" + busquedaGeneral + "%';");
			break;
		case "coleccionComic":
			sql.append(connector).append("coleccionComic LIKE '%" + busquedaGeneral + "%';");
			break;
		case "artistaComic":
			sql.append(connector).append("artistaComic LIKE '%" + busquedaGeneral + "%';");
			break;
		case "varianteComic":
			sql.append(connector).append("varianteComic LIKE '%" + busquedaGeneral + "%';");
			break;
		case "guionistaComic":
			sql.append(connector).append("guionistaComic LIKE '%" + busquedaGeneral + "%';");
			break;
		case "firmaComic":
			sql.append(connector).append("firmaComic LIKE '%" + busquedaGeneral + "%';");
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
			String editorComic = rs.getString("editorComic");
			String gradeoComic = rs.getString("gradeoComic");
			String keyComentarios = rs.getString("keyComentarios");
			String firmaComic = rs.getString("firmaComic");
			String valorComic = rs.getString("valorComic");
			String artistaComic = rs.getString("artistaComic");
			String guionistaComic = rs.getString("guionistaComic");
			String varianteComic = rs.getString("varianteComic");
			String direccionImagenComic = rs.getString("direccionImagenComic");
			String urlReferenciaComic = rs.getString("urlReferenciaComic");

			return new ComicGradeo.ComicGradeoBuilder(idComic, tituloComic).codigoComic(codigoComic)
					.numeroComic(numeroComic).fechaGradeo(fechaGradeo).editorComic(editorComic).gradeoComic(gradeoComic)
					.keyComentarios(keyComentarios).firmaComic(firmaComic).valorComic(valorComic)
					.artistaComic(artistaComic).guionistaComic(guionistaComic).varianteComic(varianteComic)
					.direccionImagenComic(direccionImagenComic).urlReferenciaComic(urlReferenciaComic).build();

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
