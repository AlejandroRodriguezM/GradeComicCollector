package dbmanager;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import alarmas.AlarmaList;
import comicManagement.ComicGradeo;
import ficherosFunciones.FuncionesFicheros;
import funcionesAuxiliares.Utilidades;
import funcionesAuxiliares.Ventanas;
import javafx.scene.control.Label;

public class DatabaseManagerDAO {

	public static AtomicInteger contadorCambios = new AtomicInteger(0);

	private static final String DB_FOLDER = System.getProperty("user.home") + File.separator + "AppData"
			+ File.separator + "Roaming" + File.separator + "gradeoComics" + File.separator;

	/**
	 * Permite introducir un nuevo cómic en la base de datos.
	 *
	 * @param sentenciaSQL la sentencia SQL para insertar el cómic
	 * @param datos        los datos del cómic a insertar
	 * @throws IOException  si ocurre un error al manejar el archivo de imagen
	 * @throws SQLException si ocurre un error al ejecutar la consulta SQL
	 */
	public static void subirComic(ComicGradeo datos, boolean esImportar) {
		try (Connection conn = ConectManager.conexion();
				PreparedStatement statement = conn.prepareStatement(InsertManager.INSERT_SENTENCIA)) {

			DBUtilidades.setParameters(statement, datos, false);

			if (esImportar) {
				statement.addBatch();
			}
			statement.executeUpdate();
		} catch (SQLException ex) {
			Utilidades.manejarExcepcion(ex);
		}
	}

	public static void createTable(String nombreDatabase) {
		String url = "jdbc:sqlite:" + DB_FOLDER + nombreDatabase;

		System.out.println(url);

		try (Connection connection = DriverManager.getConnection(url)) {
			try (Statement statement = connection.createStatement()) {
				String dropTableSQL = "DROP TABLE IF EXISTS comicGbbdd";
				statement.executeUpdate(dropTableSQL);
			}

			try (Statement statement = connection.createStatement()) {
				String createTableSQL = "CREATE TABLE IF NOT EXISTS comicsGbbdd ("
						+ "idComic INTEGER PRIMARY KEY AUTOINCREMENT, " + "tituloComic TEXT NOT NULL, "
						+ "codigoComic TEXT NOT NULL, " + "numeroComic TEXT NOT NULL, " + "fechaGradeo TEXT NOT NULL, "
						+ "editorComic TEXT NOT NULL, " + "gradeoComic TEXT NOT NULL, "
						+ "keyComentarios TEXT NOT NULL, " + "firmaComic TEXT NOT NULL, " + "valorComic TEXT NOT NULL, "
						+ "artistaComic TEXT NOT NULL, " + "guionistaComic TEXT NOT NULL, "
						+ "varianteComic TEXT NOT NULL, " + "direccionImagenComic TEXT NOT NULL, "
						+ "urlReferenciaComic TEXT NOT NULL" + ");";

				statement.executeUpdate(createTableSQL);
			}
		} catch (SQLException e) {
			Utilidades.manejarExcepcion(e);
		}
	}

	/**
	 * Funcion que reconstruye una base de datos.
	 */
	public static boolean reconstruirBBDD(String nombreDatabase, Connection connection) {
		Ventanas nav = new Ventanas();

		if (nav.alertaTablaError()) {
			try {
				connection.close();
				createTable(nombreDatabase);
				return true;
			} catch (SQLException e) {
				e.printStackTrace();
			}

		} else {
			String excepcion = "Debes de reconstruir la base de datos. Si no, no podras entrar";
			nav.alertaException(excepcion);
		}
		return false;
	}

	/**
	 * Método que verifica si las tablas de la base de datos existen y si tienen las
	 * columnas esperadas. Si las tablas y columnas existen, devuelve true. Si no
	 * existen, reconstruye la base de datos y devuelve false.
	 * 
	 * @return true si las tablas y columnas existen, false si no existen y se
	 *         reconstruyó la base de datos.
	 */
	public static boolean checkTablesAndColumns(String nombreDatabase) {
		try (Connection connection = ConectManager.conexion()) {
			DatabaseMetaData metaData = connection.getMetaData();

			ResultSet tables = metaData.getTables(nombreDatabase, null, "comicsGbbdd", null);
			if (tables.next()) {
				// La tabla existe, ahora verifiquemos las columnas
				ResultSet columns = metaData.getColumns(nombreDatabase, null, "comicsGbbdd", null);
				Set<String> expectedColumns = Set.of("idComic", // ID del cómic
						"tituloComic", // Título del cómic
						"codigoComic", // Código del cómic
						"numeroComic", // Número del cómic
						"fechaGradeo", // Fecha de grado
						"editorComic", // Editor del cómic
						"gradeoComic", // Grado del cómic
						"keyComentarios", // Comentarios
						"firmaComic", // firma del comic
						"valorComic", // valor del comic
						"artistaComic", // Artista del cómic
						"guionistaComic", // Guionista del cómic
						"varianteComic", // Variante del cómic
						"direccionImagenComic", // Dirección de la imagen
						"urlReferenciaComic" // URL de referencia
				);

				Set<String> actualColumns = new HashSet<>();

				while (columns.next()) {
					String columnName = columns.getString("COLUMN_NAME");
					actualColumns.add(columnName);
				}

				if (actualColumns.containsAll(expectedColumns) && actualColumns.size() == expectedColumns.size()) {
					return true;
				} else {
					if (reconstruirBBDD(nombreDatabase, connection)) {
						return true;
					}
				}
			} else {
				if (reconstruirBBDD(nombreDatabase, connection)) {
					return true;
				}
			}
		} catch (SQLException e) {
			Utilidades.manejarExcepcion(e);
			return false;
		}
		return false;
	}

	public static void actualizarNombresEnLote(String columna, Map<Integer, String> actualizaciones) {
		if (actualizaciones.isEmpty()) {
			return; // No hay actualizaciones pendientes
		}

		String consultaUpdate = "UPDATE comicGbbdd SET " + columna + " = ? WHERE idComic = ?";
		String url = "jdbc:sqlite:" + DB_FOLDER + FuncionesFicheros.datosEnvioFichero();

		try (Connection connection = DriverManager.getConnection(url);
				PreparedStatement pstmt = connection.prepareStatement(consultaUpdate)) {

			for (Map.Entry<Integer, String> entry : actualizaciones.entrySet()) {
				int id = entry.getKey();
				String nombreCorregido = entry.getValue();

				pstmt.setString(1, nombreCorregido);
				pstmt.setInt(2, id);
				pstmt.addBatch(); // Agregar la actualización al lote
			}

			pstmt.executeBatch(); // Ejecutar todas las actualizaciones en lote

		} catch (SQLException e) {
			e.printStackTrace();
			Utilidades.manejarExcepcion(e);
		}
	}





	/**
	 * Funcion crea el fichero SQL segun el sistema operativo en el que te
	 * encuentres.
	 *
	 * @param fichero
	 */
	public static void makeSQL(Label prontInfo) {

		String frase = "Fichero sql lite";

		String formato = "*.db";

		File fichero = Utilidades.tratarFichero(frase, formato, true);

		// Verificar si se obtuvo un objeto FileChooser válido
		if (fichero != null) {

			Utilidades.backupDB(fichero); // Llamada a funcion

			String cadena = "Fichero SQL lite creado correctamente";
			AlarmaList.iniciarAnimacionAvanzado(prontInfo, cadena);

		}
	}

}
