package dbmanager;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import comicManagement.ComicGradeo;
import ficherosFunciones.FuncionesFicheros;
import funcionesAuxiliares.Utilidades;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Esta clase sirve para realizar diferentes operaciones que tengan que ver con
 * la base de datos.
 *
 * @author Alejandro Rodriguez
 */
public class ListasComicsDAO {

	/**
	 * Obtenemos el directorio de inicio del usuario
	 */
	private static final String USER_DIR = System.getProperty("user.home");

	/**
	 * Construimos la ruta al directorio "Documents"
	 */
	private static final String DOCUMENTS_PATH = USER_DIR + File.separator + "Documents";

	/**
	 * Construimos la ruta al directorio "libreria_comics" dentro de "Documents" y
	 * añadimos el nombre de la base de datos y la carpeta "portadas".
	 */
	private static final String SOURCE_PATH = DOCUMENTS_PATH + File.separator + "gradeo_comics" + File.separator
			+ Utilidades.nombreDB() + File.separator + "portadas" + File.separator;

	public static List<ComicGradeo> listaTemporalComics = new ArrayList<>();

	/**
	 * Lista de cómics.
	 */
	public static List<ComicGradeo> listaComics = new ArrayList<>();

	/**
	 * Lista de cómics con verificación.
	 */
	public static List<ComicGradeo> listaComicsCheck = new ArrayList<>();

	public static List<String> listaID = new ArrayList<>();

	/**
	 * Lista de nombres.
	 */
	public static List<String> listaNombre = new ArrayList<>();

	/**
	 * Lista de números de cómic.
	 */
	public static List<String> listaNumeroComic = new ArrayList<>();

	/**
	 * Lista de formatos.
	 */
	public static List<String> listaGradeo = new ArrayList<>();

	/**
	 * Lista de editoriales.
	 */
	public static List<String> listaEdicion = new ArrayList<>();

	/**
	 * Lista de procedencias.
	 */
	public static List<String> listaColeccion = new ArrayList<>();

	public static List<String> listaEmpresa = new ArrayList<>();

	/**
	 * Lista de nombres de cómics.
	 */
	public static List<String> nombreComicList = new ArrayList<>();

	/**
	 * Lista de números de cómic.
	 */
	public static List<String> numeroComicList = new ArrayList<>();

	public static List<String> nombrePrecioNormalList = new ArrayList<>();

	public static List<String> nombrePrecioFoilList = new ArrayList<>();

	/**
	 * Lista de nombres de procedencia.
	 */
	public static List<String> nombreColeccionList = new ArrayList<>();

	/**
	 * Lista de nombres de formato.
	 */
	public static List<String> nombreRarezaList = new ArrayList<>();

	/**
	 * Lista de nombres de editorial.
	 */
	public static List<String> nombreEditorialList = new ArrayList<>();

	/**
	 * Lista de nombres de dibujantes.
	 */
	public static List<String> listaImagenes = new ArrayList<>();

	public static List<String> listaReferencia = new ArrayList<>();

	/**
	 * Lista de cómics limpios.
	 */
	public static List<ComicGradeo> listaLimpia = new ArrayList<>();

	/**
	 * Lista de sugerencias de autocompletado de entrada limpia.
	 */
	public static List<String> listaLimpiaAutoCompletado = new ArrayList<>();

	public static ObservableList<ComicGradeo> comicsImportados = FXCollections.observableArrayList();
	/**
	 * Lista ordenada que contiene todas las listas anteriores.
	 */
	public static List<List<String>> listaOrdenada = Arrays.asList(nombreComicList, numeroComicList,
			nombreEditorialList, nombreColeccionList, nombreRarezaList, nombrePrecioNormalList, nombrePrecioFoilList);

	/**
	 * Lista de listas de elementos.
	 */
	public static List<List<String>> itemsList = Arrays.asList(listaNombre, listaNumeroComic, listaEdicion,
			listaColeccion, listaGradeo, nombrePrecioNormalList, nombrePrecioFoilList);

	public static void eliminarUltimaComicImportada() {
		ObservableList<ComicGradeo> comicsImportados = ListasComicsDAO.comicsImportados;
		if (!comicsImportados.isEmpty()) {
			comicsImportados.remove(comicsImportados.size() - 1);
		}
	}

	public static boolean verificarIDExistente(String id) {
		// Verificar que el id no sea nulo ni esté vacío
		if (id == null || id.isEmpty()) {
			return false;
		}

		// Buscar en comicsImportados
		for (ComicGradeo comic : comicsImportados) {
			if (id.equalsIgnoreCase(comic.getIdComic())) {
				return true; // Si encuentra un comic con el mismo id, devuelve true
			}
		}

		return false; // Si no encuentra ningún comic con el mismo id, devuelve false
	}

	public static ComicGradeo devolverComicLista(String id) {
		for (ComicGradeo comic : comicsImportados) {

			if (comic.getIdComic().equalsIgnoreCase(id)) {
				return comic;
			}
		}
		return null;
	}

	/**
	 * Realiza llamadas para inicializar listas de autocompletado.
	 *
	 * @throws SQLException si ocurre un error al acceder a la base de datos.
	 */
	public static void listasAutoCompletado() {
		listaID = DBUtilidades.obtenerValoresColumna("idComic");
		listaNombre = DBUtilidades.obtenerValoresColumna("nomComic");
		listaNumeroComic = DBUtilidades.obtenerValoresColumna("numComic");
		listaEdicion = DBUtilidades.obtenerValoresColumna("edicionComic");
		listaColeccion = DBUtilidades.obtenerValoresColumna("coleccionComic");
		listaGradeo = DBUtilidades.obtenerValoresColumna("gradeoComic");
		listaReferencia = DBUtilidades.obtenerValoresColumna("urlReferenciaComic");
		listaEmpresa = DBUtilidades.obtenerValoresColumna("empresaComic");

		listaID = ordenarLista(listaID);

		// Ordenar listaNumeroComic como enteros
//		List<Integer> numerosComic = listaNumeroComic.stream().map(Integer::parseInt).collect(Collectors.toList());
//		Collections.sort(numerosComic);
//		listaNumeroComic = numerosComic.stream().map(String::valueOf).collect(Collectors.toList());

		itemsList = Arrays.asList(listaNombre, listaNumeroComic, listaEdicion, listaColeccion, listaGradeo,
				listaEmpresa);

	}

	public static void actualizarDatosAutoCompletado(String sentenciaSQL) {
		List<List<String>> listaOrdenada = new ArrayList<>(); // Cambia el tipo aquí
		try (Connection conn = ConectManager.conexion();
				PreparedStatement stmt = conn.prepareStatement(sentenciaSQL, ResultSet.TYPE_FORWARD_ONLY,
						ResultSet.CONCUR_READ_ONLY);
				ResultSet rs = stmt.executeQuery()) {

			while (rs.next()) {
				List<String> nombreComicSet = new ArrayList<>(); // Cambia el tipo aquí
				List<Integer> numeroComicSet = new ArrayList<>(); // Cambia el tipo aquí
				List<String> nombreEdicionSet = new ArrayList<>(); // Cambia el tipo aquí
				List<String> nombreColeccionSet = new ArrayList<>(); // Cambia el tipo aquí
				List<String> gradeoComicSet = new ArrayList<>(); // Cambia el tipo aquí

				do {
					String nomComic = rs.getString("nomComic").trim();
					nombreComicSet.add(nomComic);

					String numComic = rs.getString("numComic"); // Convertir a entero
					numeroComicSet.add(Integer.parseInt(numComic));

					String nomEdicion = rs.getString("edicionComic").trim();
					nombreEdicionSet.add(nomEdicion);

					String nomColeccion = rs.getString("coleccionComic").trim();
					nombreColeccionSet.add(nomColeccion);

					String gradeoComic = rs.getString("gradeoComic").trim();
					gradeoComicSet.add(gradeoComic);

				} while (rs.next());

				procesarDatosAutocompletado(nombreColeccionSet);
				procesarDatosAutocompletado(nombreEdicionSet);

				// Eliminar elementos repetidos
				nombreComicSet = listaArregladaAutoComplete(nombreComicSet);
				numeroComicSet = listaArregladaAutoComplete(numeroComicSet);
				nombreColeccionSet = listaArregladaAutoComplete(nombreColeccionSet);
				gradeoComicSet = listaArregladaAutoComplete(gradeoComicSet);
				nombreEdicionSet = listaArregladaAutoComplete(nombreEdicionSet);

				Collections.sort(numeroComicSet, Comparable::compareTo);

				listaOrdenada.add(nombreComicSet);
				listaOrdenada.add(numeroComicSet.stream().map(String::valueOf).toList());
				listaOrdenada.add(nombreEdicionSet);
				listaOrdenada.add(nombreColeccionSet);
				listaOrdenada.add(gradeoComicSet);

				ListasComicsDAO.listaOrdenada = listaOrdenada;
			}
		} catch (SQLException e) {
			Utilidades.manejarExcepcion(e);
		}
	}

	public static List<String> ordenarLista(List<String> listaStrings) {
		// Creamos una lista para almacenar los enteros
		List<Integer> listaEnteros = new ArrayList<>();

		// Convertimos los strings a enteros y los almacenamos en la lista de enteros
		for (String str : listaStrings) {
			listaEnteros.add(Integer.parseInt(str));
		}

		// Ordenamos la lista de enteros de menor a mayor
		Collections.sort(listaEnteros);

		// Creamos una nueva lista para almacenar los strings ordenados
		List<String> listaOrdenada = new ArrayList<>();

		// Convertimos los enteros ordenados a strings y los almacenamos en la lista
		// ordenada
		for (int num : listaEnteros) {
			listaOrdenada.add(String.valueOf(num));
		}

		// Devolvemos la lista ordenada de strings
		return listaOrdenada;
	}

	public static void procesarDatosAutocompletado(List<String> lista) {
		List<String> nombresProcesados = new ArrayList<>();
		for (String cadena : lista) {
			String[] nombres = cadena.split("-");
			for (String nombre : nombres) {
				nombre = nombre.trim();
				if (!nombre.isEmpty()) {
					nombresProcesados.add(nombre);
				}
			}
		}
		lista.clear(); // Limpiar la lista original
		lista.addAll(nombresProcesados); // Agregar los nombres procesados a la lista original
	}

	/**
	 * Limpia todas las listas utilizadas para autocompletado.
	 */
	public static void limpiarListas() {
		nombreComicList.clear();
		numeroComicList.clear();
		nombreColeccionList.clear();
		nombreRarezaList.clear();
		nombreEditorialList.clear();
		nombrePrecioNormalList.clear();
		nombrePrecioFoilList.clear();
	}

	/**
	 * Limpia todas las listas principales utilizadas en el contexto actual. Esto
	 * incluye listas de nombres, números de cómic, variantes, firmas, editoriales,
	 * guionistas, dibujantes, fechas, formatos, procedencias y cajas.
	 */
	public static void limpiarListasPrincipales() {
		listaNombre.clear();
		listaNumeroComic.clear();
		listaEdicion.clear();
		listaGradeo.clear();
		listaColeccion.clear();
	}

	/**
	 * Permite reiniciar la lista de cómics.
	 */
	public static void reiniciarListaComics() {
		listaComics.clear(); // Limpia la lista de cómics
	}

	public static void reiniciarListas() {
		listaComics.clear();
		listaComicsCheck.clear();
		listaID.clear();
		listaNombre.clear();
		listaNumeroComic.clear();
		listaGradeo.clear();
		listaEdicion.clear();
		listaColeccion.clear();
		nombreComicList.clear();
		numeroComicList.clear();
		nombrePrecioNormalList.clear();
		nombrePrecioFoilList.clear();
		nombreColeccionList.clear();
		nombreRarezaList.clear();
		nombreEditorialList.clear();
		listaReferencia.clear();
		listaImagenes.clear();
		listaLimpia.clear();
		listaLimpiaAutoCompletado.clear();
		comicsImportados.clear();
	}

	/**
	 * Comprueba si la lista de cómics contiene o no algún dato.
	 *
	 * @param listaComic Lista de cómics a comprobar.
	 * @return true si la lista está vacía, false si contiene elementos.
	 */
	public boolean checkList(List<ComicGradeo> listaComic) {
		if (listaComic.isEmpty()) {
			return true; // La lista está vacía
		}
		return false; // La lista contiene elementos
	}

	/**
	 * Ordena un HashMap en orden ascendente por valor (valor de tipo Integer).
	 *
	 * @param map El HashMap a ordenar.
	 * @return Una lista de entradas ordenadas por valor ascendente.
	 */
	public static List<Map.Entry<Integer, Integer>> sortByValueInt(Map<Integer, Integer> map) {
		List<Map.Entry<Integer, Integer>> list = new LinkedList<>(map.entrySet());
		list.sort(Map.Entry.comparingByValue());
		return list;
	}

	/**
	 * Función que guarda los datos para autocompletado en una lista.
	 * 
	 * @param sentenciaSQL La sentencia SQL para obtener los datos.
	 * @param columna      El nombre de la columna que contiene los datos para
	 *                     autocompletado.
	 * @return Una lista de cadenas con los datos para autocompletado.
	 * @throws SQLException Si ocurre algún error al ejecutar la consulta SQL.
	 */
	public static List<String> guardarDatosAutoCompletado(String sentenciaSQL, String columna) {
		List<String> listaAutoCompletado = new ArrayList<>();
		try (Connection conn = ConectManager.conexion();
				PreparedStatement stmt = conn.prepareStatement(sentenciaSQL);
				ResultSet rs = stmt.executeQuery()) {

			while (rs.next()) {
				String datosAutocompletado = rs.getString(columna);
				if (columna.equals("nomComic")) {
					listaAutoCompletado.add(datosAutocompletado.trim());
				} else if (columna.equals("direccionImagenComic")) {
					listaAutoCompletado.add(SOURCE_PATH + Utilidades.obtenerUltimoSegmentoRuta(datosAutocompletado));
				} else {
					String[] nombres = datosAutocompletado.split("-");
					for (String nombre : nombres) {
						nombre = nombre.trim();
						if (!nombre.isEmpty()) {
							listaAutoCompletado.add(nombre);
						}
					}
				}
			}

			if (columna.contains("precio")) {
				return listaAutoCompletado;
			}

			return listaArregladaAutoComplete(listaAutoCompletado);

		} catch (SQLException e) {
			Utilidades.manejarExcepcion(e); // Manejar la excepción adecuadamente, por ejemplo, registrándola o
											// notificándola
			// En caso de excepción, podrías decidir retornar una lista parcial o marcar que
			// la operación fue interrumpida
			return listaAutoCompletado; // Retorna lo que se haya procesado hasta el momento
		}
	}

	/**
	 * Funcion que devuelve una lista en la que solamente se guardan aquellos datos
	 * que no se repiten
	 *
	 * @param listaComics
	 * @return
	 */
	public static List<ComicGradeo> listaArreglada(List<ComicGradeo> listaComics) {

		// Forma número 1 (Uso de Maps).
		Map<String, ComicGradeo> mapComics = new HashMap<>(listaComics.size());

		// Aquí está la magia
		for (ComicGradeo c : listaComics) {
			mapComics.put(c.getIdComic(), c);
		}

		// Agrego cada elemento del map a una nueva lista y muestro cada elemento.

		for (Entry<String, ComicGradeo> c : mapComics.entrySet()) {

			listaLimpia.add(c.getValue());

		}
		return listaLimpia;
	}

	/**
	 * Funcion que devuelve una lista en la que solamente se guardan aquellos datos
	 * que no se repiten
	 *
	 * @param listaComics
	 * @return
	 */
	public static <T extends Comparable<? super T>> List<T> listaArregladaAutoComplete(List<T> listaComics) {
		Set<T> uniqueSet = new HashSet<>();
		List<T> result = new ArrayList<>();

		for (T item : listaComics) {
			if (uniqueSet.add(item)) {
				result.add(item);
			}
		}

		// Ordenar la lista resultante de forma ascendente
		Collections.sort(result);

		return result;
	}

	/**
	 * Busca un cómic por su ID en una lista de cómics.
	 *
	 * @param comics  La lista de cómics en la que se realizará la búsqueda.
	 * @param idComic La ID del cómic que se está buscando.
	 * @return El cómic encontrado por la ID, o null si no se encuentra ninguno.
	 */
	public static ComicGradeo buscarComicPorID(List<ComicGradeo> comics, String idComic) {
		for (ComicGradeo c : comics) {
			if (c.getIdComic().equals(idComic)) {
				return c; // Devuelve el cómic si encuentra la coincidencia por ID
			}
		}
		return null; // Retorna null si no se encuentra ningún cómic con la ID especificada
	}

	/**
	 * Busca un cómic por su ID en una lista de cómics.
	 *
	 * @param comics  La lista de cómics en la que se realizará la búsqueda.
	 * @param idComic La ID del cómic que se está buscando.
	 * @return El cómic encontrado por la ID, o null si no se encuentra ninguno.
	 */
	public static boolean verificarComicPorID(List<ComicGradeo> comics, String idComic) {
		for (ComicGradeo c : comics) {
			if (c.getIdComic().equals(idComic)) {
				return true; // Devuelve el cómic si encuentra la coincidencia por ID
			}
		}
		return false; // Retorna null si no se encuentra ningún cómic con la ID especificada
	}

	public static boolean comprobarLista() {
		if (listaID.isEmpty()) {
			return false;
		}
		return true;
	}

	public static void mostrarTamanioListas() {
		System.out.println("Tamaño de listaComics: " + listaComics.size());
		System.out.println("Tamaño de listaComicsCheck: " + listaComicsCheck.size());
		System.out.println("Tamaño de listaID: " + listaID.size());
		System.out.println("Tamaño de listaNombre: " + listaNombre.size());
		System.out.println("Tamaño de listaNumeroComic: " + listaNumeroComic.size());
		System.out.println("Tamaño de listaRareza: " + listaGradeo.size());
		System.out.println("Tamaño de listaEditorial: " + listaEdicion.size());
		System.out.println("Tamaño de listaColeccion: " + listaColeccion.size());
		System.out.println("Tamaño de nombreComicList: " + nombreComicList.size());
		System.out.println("Tamaño de numeroComicList: " + numeroComicList.size());
		System.out.println("Tamaño de nombrePrecioListNormal: " + nombrePrecioNormalList.size());
		System.out.println("Tamaño de nombrePrecioListFoil: " + nombrePrecioFoilList.size());
		System.out.println("Tamaño de nombreColeccionList: " + nombreColeccionList.size());
		System.out.println("Tamaño de nombreRarezaList: " + nombreRarezaList.size());
		System.out.println("Tamaño de nombreEditorialList: " + nombreEditorialList.size());
		System.out.println("Tamaño de listaImagenes: " + listaImagenes.size());
		System.out.println("Tamaño de listaLimpia: " + listaLimpia.size());
		System.out.println("Tamaño de listaLimpiaAutoCompletado: " + listaLimpiaAutoCompletado.size());
		System.out.println("Tamaño de comicsImportados: " + comicsImportados.size());
	}

	/**
	 * Genera un archivo de estadísticas basado en los datos de la base de datos de
	 * cómics. Los datos se organizan en diferentes categorías y se cuentan para su
	 * análisis. Luego, se genera un archivo de texto con las estadísticas y se abre
	 * con el programa asociado.
	 */
	public static void generar_fichero_estadisticas() {
		// Crear HashMaps para almacenar los datos de cada campo sin repetición y sus
		// conteos
		Map<String, Integer> nomComicEstadistica = new HashMap<>();
		Map<String, Integer> nivelGradeoEstadistica = new HashMap<>();
		Map<String, Integer> nomEditorialEstadistica = new HashMap<>();
		Map<String, Double> precioComicEstadistica = new HashMap<>();
		Map<String, Integer> coleccionComicEstadistica = new HashMap<>();
		Map<String, Integer> rarezaComicEstadistica = new HashMap<>();
		Map<String, Integer> normasComicEstadistica = new HashMap<>();

		final String CONSULTA_SQL = "SELECT * FROM albumbbdd";

		try (Connection conn = ConectManager.conexion();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(CONSULTA_SQL)) {

			// Procesar los datos y generar la estadística
			while (rs.next()) {
				// Obtener los datos de cada campo
				String nomComic = rs.getString("nomComic");
				String numComic = rs.getString("numComic");
				String nivelGradeo = rs.getString("gradeoComic");
				String nomEditorial = rs.getString("editorialComic");
				String coleccion = rs.getString("coleccionComic");
				String precioComicStr = rs.getString("precioComic");
				String rarezaComic = rs.getString("rarezaComic");
				String esFoilComic = rs.getString("esFoilComic");
				String estadoComic = rs.getString("estadoComic");
				String normasComic = rs.getString("normasComic");

				// Actualizar los HashMaps para cada campo
				nomComicEstadistica.put(nomComic, nomComicEstadistica.getOrDefault(nomComic, 0) + 1);
				nivelGradeoEstadistica.put(nivelGradeo, nivelGradeoEstadistica.getOrDefault(nivelGradeo, 0) + 1);
				nomEditorialEstadistica.put(nomEditorial, nomEditorialEstadistica.getOrDefault(nomEditorial, 0) + 1);
				coleccionComicEstadistica.put(coleccion, coleccionComicEstadistica.getOrDefault(coleccion, 0) + 1);
				rarezaComicEstadistica.put(rarezaComic, rarezaComicEstadistica.getOrDefault(rarezaComic, 0) + 1);
				normasComicEstadistica.put(normasComic, normasComicEstadistica.getOrDefault(normasComic, 0) + 1);

				// Convertir precioComic de String a Double y añadir a la estadística de precios
				try {
					double precioComic = Double.parseDouble(precioComicStr);
					if (precioComic > 0) {
						precioComicEstadistica.put(nomComic, precioComic);
					}
				} catch (NumberFormatException e) {
					// Manejar excepción si no se puede convertir a double
					System.err.println("No se pudo convertir el precio de la comic a número: " + precioComicStr);
				}
			}

		} catch (SQLException e) {
			Utilidades.manejarExcepcion(e);
		}

		// Generar la cadena de estadística
		StringBuilder estadisticaStr = new StringBuilder();
		String lineaDecorativa = "\n--------------------------------------------------------\n";
		String fechaActual = Utilidades.obtenerFechaActual();
		String datosFichero = FuncionesFicheros.datosEnvioFichero();
		// Encabezado
		estadisticaStr.append(
				"Estadisticas de comics de la base de datos: " + datosFichero + ", a fecha de: " + fechaActual + "\n");

		// Generar estadísticas para cada tipo de dato
		generarEstadistica(estadisticaStr, "Estadística de nombres de comics:\n", nomComicEstadistica);
		generarEstadistica(estadisticaStr, "Estadística de niveles de gradeo:\n", nivelGradeoEstadistica);
		generarEstadistica(estadisticaStr, "Estadística de editoriales:\n", nomEditorialEstadistica);
		generarEstadistica(estadisticaStr, "Estadística de colecciones:\n", coleccionComicEstadistica);
		generarEstadistica(estadisticaStr, "Estadística de rarezas:\n", rarezaComicEstadistica);
		generarEstadistica(estadisticaStr, "Estadística de normas:\n", normasComicEstadistica);

		// Agregar estadística de precios
		estadisticaStr.append(lineaDecorativa);
		estadisticaStr.append("\nEstadística de precios de comics:\n");
		estadisticaStr.append(lineaDecorativa);
		for (Map.Entry<String, Double> entry : precioComicEstadistica.entrySet()) {
			estadisticaStr.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
		}

		estadisticaStr.append(lineaDecorativa);

		// Crear el archivo de estadística y escribir los datos en él
		String nombreArchivo = "estadistica_" + fechaActual + ".txt";
		String rutaCompleta = obtenerRutaArchivo(nombreArchivo);

		try (PrintWriter writer = new PrintWriter(new FileWriter(rutaCompleta))) {
			writer.print(estadisticaStr);

			// Abrir el archivo con el programa asociado en el sistema
			Utilidades.abrirArchivo(rutaCompleta);

		} catch (IOException e) {
			Utilidades.manejarExcepcion(e);
		}
	}

	// Método para generar estadística de un HashMap específico
	private static void generarEstadistica(StringBuilder sb, String titulo, Map<String, Integer> estadistica) {
		sb.append("\n--------------------------------------------------------\n");
		sb.append(titulo);
		sb.append("\n--------------------------------------------------------\n");

		List<Map.Entry<String, Integer>> listaOrdenada = sortByValue(estadistica);
		for (Map.Entry<String, Integer> entry : listaOrdenada) {
			sb.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
		}
	}

	// Método para obtener la ruta completa del archivo de estadísticas
	private static String obtenerRutaArchivo(String nombreArchivo) {
		String userHome = System.getProperty("user.home");
		String ubicacion = userHome + File.separator + "AppData" + File.separator + "Roaming";
		String carpetaLibreria = ubicacion + File.separator + "album";
		return carpetaLibreria + File.separator + nombreArchivo;
	}

	// Método para ordenar un Map por los valores (conteos en este caso)
	private static List<Map.Entry<String, Integer>> sortByValue(Map<String, Integer> estadistica) {
		List<Map.Entry<String, Integer>> lista = new LinkedList<>(estadistica.entrySet());
		lista.sort(Map.Entry.comparingByValue());
		Collections.reverse(lista); // Para orden descendente
		return lista;
	}

}