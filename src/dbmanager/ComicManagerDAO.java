package dbmanager;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import comicManagement.ComicGradeo;

public class ComicManagerDAO {

	// #############################################
	// ############# FUNCIONES INSERT ##############
	// #############################################

	/**
	 * Inserta los datos de un cómic en la base de datos.
	 *
	 * @param comicDatos los datos del cómic a insertar
	 * @throws IOException  si ocurre un error al manejar el archivo de imagen
	 * @throws SQLException si ocurre un error al ejecutar la consulta SQL
	 */
	public static void insertarDatos(ComicGradeo datos, boolean esImportar) {
		InsertManager.insertarDatos(datos, esImportar);
	}

	// #############################################
	// ############# FUNCIONES DELETE ##############
	// #############################################

	/**
	 * Borra el contenido de la base de datos.
	 *
	 * @return
	 * @throws SQLException
	 */
	public static CompletableFuture<Boolean> deleteTable() {
		return DeleteManager.deleteAndRestartDatabaseAsync();
	}

	public static void borrarComic(String idComic) {
		DeleteManager.borrarComic(idComic);
	}

	// #############################################
	// ############ FUNCIONES UPDATE ###############
	// #############################################

	/**
	 * Realiza acciones específicas en la base de datos para un comic según la
	 * operación indicada.
	 *
	 * @param id        El ID del comic a modificar.
	 * @param operacion La operación a realizar: "Vender", "En venta" o "Eliminar".
	 * @throws SQLException Si ocurre un error en la consulta SQL.
	 */
	public static void actualizarComicBBDD(ComicGradeo comic, String operacion) throws SQLException {
		UpdateManager.actualizarComicBBDD(comic, operacion);
	}

	// #############################################
	// ############ FUNCIONES SELECT ###############
	// #############################################

	public static int countRows() {
		return SelectManager.countRows();
	}

	public static ComicGradeo comicDatos(String idComic) {
		return SelectManager.comicDatos(idComic);
	}

	public static boolean comprobarIdentificadorComic(String idComic) {
		return SelectManager.comprobarIdentificadorComic(idComic);
	}

	/**
	 * Obtiene la dirección de la portada de un cómic.
	 *
	 * @param idComic ID del cómic
	 * @return Dirección de la portada del cómic
	 * @throws SQLException Si ocurre algún error de SQL
	 */
	public static String obtenerDireccionPortada(String idComic) {
		return SelectManager.obtenerDireccionPortada(idComic);
	}

	/**
	 * Función que busca en el ArrayList el o los cómics que tengan coincidencia con
	 * los datos introducidos en el TextField.
	 *
	 * @param comic           el cómic con los parámetros de búsqueda
	 * @param busquedaGeneral el texto de búsqueda general
	 * @return una lista de cómics que coinciden con los criterios de búsqueda
	 * @throws SQLException si ocurre un error al acceder a la base de datos
	 */
	public static List<ComicGradeo> busquedaParametro(ComicGradeo comic, String busquedaGeneral) {
		return SelectManager.busquedaParametro(comic, busquedaGeneral);
	}

	public static boolean hayDatosEnLibreria(String sentenciaSQL) {
		return SelectManager.hayDatosEnLibreria(sentenciaSQL);
	}

	/**
	 * Devuelve una lista con todos los comics de la base de datos que se encuentran
	 * "En posesion"
	 *
	 * @return una lista de cómics que coinciden con los criterios de búsqueda
	 * @throws SQLException
	 */
	public static List<ComicGradeo> libreriaSeleccionado(String datoSeleccionado) {
		return SelectManager.libreriaSeleccionado(datoSeleccionado);
	}

	/**
	 * Método que muestra los cómics de la librería según la sentencia SQL
	 * proporcionada.
	 * 
	 * @param sentenciaSQL La sentencia SQL para obtener los cómics de la librería.
	 * @return Una lista de objetos Comic que representan los cómics de la librería.
	 * @throws SQLException Si ocurre algún error al ejecutar la consulta SQL.
	 */
	public static List<ComicGradeo> verLibreria(String sentenciaSQL) {
		return SelectManager.verLibreria(sentenciaSQL,false);
	}

}
