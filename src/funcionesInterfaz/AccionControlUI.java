package funcionesInterfaz;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Controladores.ImagenAmpliadaController;
import Controladores.VentanaAccionController;
import alarmas.AlarmaList;
import comicManagement.ComicGradeo;
import dbmanager.ComicManagerDAO;
import dbmanager.ListasComicsDAO;
import funcionesAuxiliares.Utilidades;
import funcionesManagment.AccionAniadir;
import funcionesManagment.AccionEliminar;
import funcionesManagment.AccionFuncionesComunes;
import funcionesManagment.AccionModificar;
import funcionesManagment.AccionReferencias;
import funcionesManagment.AccionSeleccionar;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;

public class AccionControlUI {

	private static AccionReferencias referenciaVentana = getReferenciaVentana();

	private static VentanaAccionController accionController = new VentanaAccionController();

	private static AccionAniadir accionAniadir = new AccionAniadir();

	private static AccionEliminar accionEliminar = new AccionEliminar();

	private static AccionModificar accionModificar = new AccionModificar();

	public static void autoRelleno() {

		referenciaVentana.getIdComicTratarTextField().textProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue.isEmpty()) {
				if (!rellenarCampos(newValue)) {
					limpiarAutorellenos(false);
					borrarDatosGraficos();
				}
			} else {
				limpiarAutorellenos(false);
				borrarDatosGraficos();
			}
		});
	}

	public static boolean rellenarCampos(String idComic) {
		ComicGradeo comicTempTemp = ComicGradeo.obtenerComic(idComic);
		if (comicTempTemp != null) {
			rellenarDatos(comicTempTemp);
			return true;
		}
		return false;
	}

	public static void mostrarOpcion(String opcion) {
		ocultarCampos();

		List<Node> elementosAMostrarYHabilitar = new ArrayList<>();

		switch (opcion.toLowerCase()) {
		case "eliminar":
			accionEliminar.mostrarElementosEliminar(elementosAMostrarYHabilitar);
			break;
		case "aniadir":
			accionAniadir.mostrarElementosAniadir(elementosAMostrarYHabilitar);
			break;
		case "modificar":
			accionModificar.mostrarElementosModificar(elementosAMostrarYHabilitar);
			break;
		default:
			accionController.closeWindow();
			return;
		}

		mostrarElementos(elementosAMostrarYHabilitar);
	}

	public static List<Node> modificarInterfazAccion(String opcion) {

		List<Node> elementosAMostrarYHabilitar = new ArrayList<>();

		switch (opcion.toLowerCase()) {
		case "modificar":
			elementosAMostrarYHabilitar.add(referenciaVentana.getBotonModificarComic());
			elementosAMostrarYHabilitar.add(referenciaVentana.getBotonEliminar());
			break;
		case "aniadir":
			elementosAMostrarYHabilitar.add(referenciaVentana.getBotonGuardarComic());
			elementosAMostrarYHabilitar.add(referenciaVentana.getBotonEliminarImportadoComic());
			elementosAMostrarYHabilitar.add(referenciaVentana.getBotonClonarComic());
			break;
		default:
			break;
		}

		return elementosAMostrarYHabilitar;
	}

	private static void mostrarElementos(List<Node> elementosAMostrarYHabilitar) {
		for (Node elemento : elementosAMostrarYHabilitar) {

			if (elemento != null) {
				elemento.setVisible(true);
				elemento.setDisable(false);
			}
		}

		if (!AccionFuncionesComunes.TIPO_ACCION.equals("modificar")) {
			autoRelleno();
		}

		if (!AccionFuncionesComunes.TIPO_ACCION.equals("aniadir")) {

			referenciaVentana.getNavegacionCerrar().setDisable(true);
			referenciaVentana.getNavegacionCerrar().setVisible(false);
		} else {
			referenciaVentana.getIdComicTratarTextField().setEditable(false);
			referenciaVentana.getIdComicTratarTextField().setOpacity(0.7);
		}
		if (AccionFuncionesComunes.TIPO_ACCION.equals("eliminar")) {
			referenciaVentana.getLabelIdMod().setLayoutX(5);
			referenciaVentana.getNumeroComicTextField().setVisible(false);
			referenciaVentana.getColeccionComicTextField().setVisible(false);
			referenciaVentana.getNombreComicTextField().setVisible(false);
			referenciaVentana.getEdicionComicTextField().setVisible(false);
			referenciaVentana.getLabelColeccion().setVisible(false);
			referenciaVentana.getLabelNombre().setVisible(false);

		}

		if (AccionFuncionesComunes.TIPO_ACCION.equals("aniadir")) {
			referenciaVentana.getBotonEliminarImportadoListaComic().setVisible(false);
			referenciaVentana.getBotonGuardarListaComics().setVisible(false);

			referenciaVentana.getBotonEliminarImportadoListaComic().setDisable(true);
			referenciaVentana.getBotonGuardarListaComics().setDisable(true);
		}
		if (AccionFuncionesComunes.TIPO_ACCION.equals("modificar")) {

			referenciaVentana.getBotonModificarComic().setVisible(false);
			referenciaVentana.getBotonModificarComic().setDisable(true);

			referenciaVentana.getBotonEliminar().setVisible(false);
			referenciaVentana.getBotonEliminar().setDisable(true);
		}

	}

	/**
	 * Oculta y deshabilita varios campos y elementos en la interfaz gráfica.
	 */
	public static void ocultarCampos() {

		List<Node> elementosTextfield = Arrays.asList(referenciaVentana.getGradeoComicTextField(),
				referenciaVentana.getDireccionImagenTextField(), referenciaVentana.getUrlReferenciaTextField(),
				referenciaVentana.getNombreEmpresaTextField(), referenciaVentana.getAnioComicTextField(),
				referenciaVentana.getCodigoComicTextField());

		List<Node> elementosLabel = Arrays.asList(referenciaVentana.getLabelGradeo(),
				referenciaVentana.getLabelPortada(), referenciaVentana.getLabelReferencia(),
				referenciaVentana.getLabelEmpresa(), referenciaVentana.getLabelCodigo(),
				referenciaVentana.getLabelAnio());

		List<Node> elementosBoton = Arrays.asList(referenciaVentana.getBotonSubidaPortada(),
				referenciaVentana.getBotonEliminar(), referenciaVentana.getBotonModificarComic(),
				referenciaVentana.getBotonBusquedaCodigo(), referenciaVentana.getBotonbbdd());

		Utilidades.cambiarVisibilidad(elementosTextfield, true);
		Utilidades.cambiarVisibilidad(elementosLabel, true);
		Utilidades.cambiarVisibilidad(elementosBoton, true);
	}

	/**
	 * Establece los atributos del cómic basándose en el objeto Comic proporcionado.
	 * 
	 * @param comicTempTemp El objeto Comic que contiene los datos a establecer.
	 */
	public void setAtributosDesdeTabla(ComicGradeo comicTemp) {

		referenciaVentana.getIdComicTratarTextField().setText(comicTemp.getIdComic());

		referenciaVentana.getNombreComicTextField().setText(comicTemp.getNomComic());

		referenciaVentana.getNumeroComicTextField().setText(comicTemp.getNumComic());

		referenciaVentana.getEdicionComicTextField().setText(comicTemp.getEdicionComic());

		referenciaVentana.getColeccionComicTextField().setText(comicTemp.getColeccionComic());

		referenciaVentana.getAnioComicTextField().setText(comicTemp.getAnioComic());

		referenciaVentana.getUrlReferenciaTextField().setText(comicTemp.getUrlReferenciaComic());

		referenciaVentana.getGradeoComicTextField().setText(comicTemp.getGradeoComic());

		referenciaVentana.getNombreEmpresaTextField().setText(comicTemp.getEmpresaComic());

		referenciaVentana.getGradeoComicTextField().setText(comicTemp.getGradeoComic());

		referenciaVentana.getCodigoComicTextField().setText(comicTemp.getCodComic());

		Utilidades.cargarImagenAsync(comicTemp.getDireccionImagenComic(), referenciaVentana.getImagenComic());
	}

	private static void rellenarDatos(ComicGradeo comicTemp) {

		referenciaVentana.getNumeroComicTextField().setText(comicTemp.getNumComic());
		referenciaVentana.getNombreComicTextField().setText(comicTemp.getNomComic());
		referenciaVentana.getNumeroComicTextField().setText(comicTemp.getNumComic());
		referenciaVentana.getEdicionComicTextField().setText(comicTemp.getEdicionComic());
		referenciaVentana.getColeccionComicTextField().setText(comicTemp.getColeccionComic());
		referenciaVentana.getGradeoComicTextField().setText(comicTemp.getGradeoComic());
		referenciaVentana.getDireccionImagenTextField().setText(comicTemp.getDireccionImagenComic());
		referenciaVentana.getUrlReferenciaTextField().setText(comicTemp.getUrlReferenciaComic());

		referenciaVentana.getProntInfoTextArea().clear();
		referenciaVentana.getProntInfoTextArea().setOpacity(1);

		Image imagenComic = Utilidades.devolverImagenComic(comicTemp.getDireccionImagenComic());
		referenciaVentana.getImagenComic().setImage(imagenComic);
	}

	public static void validarCamposClave(boolean esBorrado) {
		List<TextField> camposUi = Arrays.asList(referenciaVentana.getNombreComicTextField(),
				referenciaVentana.getEdicionComicTextField(), referenciaVentana.getColeccionComicTextField(),
				referenciaVentana.getGradeoComicTextField());

		for (TextField campoUi : camposUi) {

			if (campoUi != null) {
				String datoComic = campoUi.getText();

				if (esBorrado) {
					if (datoComic == null || datoComic.isEmpty() || datoComic.equalsIgnoreCase("vacio")) {
						campoUi.setStyle("");
					}
				} else {
					// Verificar si el campo está vacío, es nulo o tiene el valor "Vacio"
					if (datoComic == null || datoComic.isEmpty() || datoComic.equalsIgnoreCase("vacio")) {
						campoUi.setStyle("-fx-background-color: red;");
					} else {
						campoUi.setStyle("");
					}
				}
			}
		}
	}

	public boolean camposComicSonValidos() {
		List<Control> camposUi = Arrays.asList(referenciaVentana.getNombreComicTextField(),
				referenciaVentana.getEdicionComicTextField(), referenciaVentana.getColeccionComicTextField(),
				referenciaVentana.getGradeoComicTextField(), referenciaVentana.getNumeroComicTextField());

		for (Control campoUi : camposUi) {
			String datoComic = ((TextField) campoUi).getText();

			// Verificar si el campo está vacío, es nulo o tiene el valor "Vacio"
			if (datoComic == null || datoComic.isEmpty() || datoComic.equalsIgnoreCase("vacio")) {
				campoUi.setStyle("-fx-background-color: #FF0000;");
				return false; // Devolver false si al menos un campo no es válido
			} else {
				campoUi.setStyle("");
			}
		}

		return true; // Devolver true si todos los campos son válidos
	}

	public static boolean comprobarListaValidacion(ComicGradeo c) {
		String numComicStr = c.getNumComic();

		// Validar campos requeridos y "vacio"
		if (c.getNomComic() == null || c.getNomComic().isEmpty() || c.getNomComic().equalsIgnoreCase("vacio")
				|| numComicStr == null || numComicStr.isEmpty() || c.getEmpresaComic() == null
				|| c.getEmpresaComic().isEmpty() || c.getEmpresaComic().equalsIgnoreCase("vacio")
				|| c.getColeccionComic() == null || c.getColeccionComic().isEmpty()
				|| c.getColeccionComic().equalsIgnoreCase("vacio") || c.getGradeoComic() == null
				|| c.getGradeoComic().isEmpty() || c.getGradeoComic().equalsIgnoreCase("vacio")
				|| c.getUrlReferenciaComic() == null || c.getUrlReferenciaComic().isEmpty()
				|| c.getUrlReferenciaComic().equalsIgnoreCase("vacio") || c.getEdicionComic() == null
				|| c.getEdicionComic().isEmpty() || c.getEdicionComic().equalsIgnoreCase("vacio")
				|| c.getAnioComic() == null || c.getAnioComic().isEmpty() || c.getAnioComic().equalsIgnoreCase("vacio")
				|| c.getDireccionImagenComic() == null || c.getDireccionImagenComic().isEmpty()
				|| c.getDireccionImagenComic().equalsIgnoreCase("vacio")) {

			String mensajePront = "Revisa la lista, algunos campos están mal rellenados.";
			AlarmaList.mostrarMensajePront(mensajePront, false, referenciaVentana.getProntInfoTextArea());
			return false;
		}

		return true;
	}

	public static boolean isValidPrecio(String precioStr) {
		// Verificar si el precio es válido (no vacío, no solo un símbolo, no solo el
		// número 0)
		if (precioStr == null || precioStr.isEmpty()) {
			return false;
		}

		// Formatear el precio
		String formattedPrecio = parsePrecio(precioStr);

		System.out.println(formattedPrecio);

		// Verificar si el precio es "€0.0", "€0", "$0.0", "$0" o solo el símbolo de la
		// moneda
		if (formattedPrecio.equalsIgnoreCase("€0.0") || formattedPrecio.equalsIgnoreCase("€0")
				|| formattedPrecio.equalsIgnoreCase("$0.0") || formattedPrecio.equalsIgnoreCase("$0")
				|| formattedPrecio.equals("€") || formattedPrecio.equals("$")) {
			return false;
		}

		// Verificar si el precio consiste solo en un símbolo
		if (formattedPrecio.length() == 1) {
			return false;
		}

		// Verificar si el precio consiste solo en el número 0
		if (formattedPrecio.equals("0.0") || formattedPrecio.equals("0,0")) {
			return false;
		}

		return true;
	}

	public static String parsePrecio(String precioStr) {
		if (precioStr == null || precioStr.isEmpty()) {
			return "€0.0";
		}

		// Crear un formateador decimal que maneje símbolos y formatos específicos
		DecimalFormatSymbols symbols = new DecimalFormatSymbols();
		symbols.setDecimalSeparator('.');
		symbols.setGroupingSeparator(',');
		DecimalFormat format = new DecimalFormat("#,##0.00", symbols);
		format.setParseBigDecimal(true);

		// Encontrar el símbolo de moneda al inicio de la cadena
		char simbolo = precioStr.charAt(0);

		// Eliminar todos los caracteres que no son dígitos, punto o coma
		String cleanPrecioStr = precioStr.replaceAll("[^0-9.,]", "");

		// Insertar el símbolo de moneda al inicio del número limpio
		cleanPrecioStr = simbolo + cleanPrecioStr;

		// Devolver el valor parseado como double
		return cleanPrecioStr;
	}

	/**
	 * Borra los datos del cómic
	 */
	public static void limpiarAutorellenos(boolean esPrincipal) {

		if (esPrincipal) {
			referenciaVentana.getProntInfoTextArea().clear();
			referenciaVentana.getProntInfoTextArea().setText(null);
			referenciaVentana.getProntInfoTextArea().setOpacity(0);
			referenciaVentana.getTablaBBDD().getItems().clear();
			referenciaVentana.getTablaBBDD().setOpacity(0.6);
			referenciaVentana.getTablaBBDD().refresh();
			referenciaVentana.getImagenComic().setImage(null);
			referenciaVentana.getImagenComic().setOpacity(0);
			return;
		}

		referenciaVentana.getImagenComic().setImage(null);
		referenciaVentana.getImagenComic().setOpacity(0);
		referenciaVentana.getNombreComicTextField().setText("");
		referenciaVentana.getNumeroComicTextField().setText("");
		referenciaVentana.getAnioComicTextField().setText("");
		referenciaVentana.getNombreEmpresaTextField().setText("");
		referenciaVentana.getCodigoComicTextField().setText("");
		referenciaVentana.getEdicionComicTextField().setText("");
		referenciaVentana.getColeccionComicTextField().setText("");
		referenciaVentana.getGradeoComicTextField().setText("");

		referenciaVentana.getIdComicTratarTextField().setText("");

		referenciaVentana.getDireccionImagenTextField().setText("");
		referenciaVentana.getUrlReferenciaTextField().setText("");
		referenciaVentana.getNumeroComicTextField().setText("");

		if ("aniadir".equals(AccionFuncionesComunes.TIPO_ACCION)) {
			referenciaVentana.getIdComicTratarTextField().setDisable(false);
			referenciaVentana.getIdComicTratarTextField().setText("");
			referenciaVentana.getIdComicTratarTextField().setDisable(true);
		}else {
			referenciaVentana.getTablaBBDD().getItems().clear();
			referenciaVentana.getTablaBBDD().refresh();
		}

		if ("modificar".equals(AccionFuncionesComunes.TIPO_ACCION)) {
			referenciaVentana.getBotonModificarComic().setVisible(false);
			referenciaVentana.getBotonEliminar().setVisible(false);
		}

		referenciaVentana.getProntInfoTextArea().setText(null);
		referenciaVentana.getProntInfoTextArea().setOpacity(0);
		referenciaVentana.getProntInfoTextArea().setStyle("");
		validarCamposClave(true);
	}

	public static void borrarDatosGraficos() {
		referenciaVentana.getProntInfoTextArea().setText(null);
		referenciaVentana.getProntInfoTextArea().setOpacity(0);
		referenciaVentana.getProntInfoTextArea().setStyle("");
	}

	/**
	 * Asigna tooltips a varios elementos en la interfaz gráfica. Estos tooltips
	 * proporcionan información adicional cuando el usuario pasa el ratón sobre los
	 * elementos.
	 */
	public static void establecerTooltips() {
		Platform.runLater(() -> {
			Map<Node, String> tooltipsMap = new HashMap<>();

			tooltipsMap.put(referenciaVentana.getNombreComicCombobox(), "Nombre de los cómics / libros / mangas");
			tooltipsMap.put(referenciaVentana.getNumeroComicCombobox(), "Número del cómic / libro / manga");
			tooltipsMap.put(referenciaVentana.getNombreEdicionCombobox(),
					"Nombre de la variante del cómic / libro / manga");
			tooltipsMap.put(referenciaVentana.getBotonLimpiar(), "Limpia la pantalla y reinicia todos los valores");
			tooltipsMap.put(referenciaVentana.getBotonbbdd(), "Botón para acceder a la base de datos");
			tooltipsMap.put(referenciaVentana.getBotonSubidaPortada(), "Botón para subir una portada");
			tooltipsMap.put(referenciaVentana.getBotonEliminar(), "Botón para eliminar un cómic");
			tooltipsMap.put(referenciaVentana.getBotonParametroComic(),
					"Botón para buscar un cómic mediante una lista de parámetros");
			tooltipsMap.put(referenciaVentana.getBotonModificarComic(), "Botón para modificar un cómic");

			tooltipsMap.put(referenciaVentana.getNombreColeccionCombobox(),
					"Nombre de la firma del cómic / libro / manga");
			tooltipsMap.put(referenciaVentana.getNombreGradeoCombobox(),
					"Nombre del guionista del cómic / libro / manga");
			tooltipsMap.put(referenciaVentana.getBotonIntroducir(),
					"Realizar una acción de introducción del cómic / libro / manga");
			tooltipsMap.put(referenciaVentana.getBotonModificar(),
					"Realizar una acción de modificación del cómic / libro / manga");
			tooltipsMap.put(referenciaVentana.getBotonEliminar(),
					"Realizar una acción de eliminación del cómic / libro / manga");
			tooltipsMap.put(referenciaVentana.getBotonMostrarParametro(),
					"Buscar por parámetros según los datos rellenados");

			FuncionesTooltips.assignTooltips(tooltipsMap);
		});
	}

	public static void autocompletarListas() {
		FuncionesManejoFront.asignarAutocompletado(referenciaVentana.getNombreComicTextField(),
				ListasComicsDAO.listaNombre);
		FuncionesManejoFront.asignarAutocompletado(referenciaVentana.getEdicionComicTextField(),
				ListasComicsDAO.listaEdicion);
		FuncionesManejoFront.asignarAutocompletado(referenciaVentana.getColeccionComicTextField(),
				ListasComicsDAO.listaColeccion);
		FuncionesManejoFront.asignarAutocompletado(referenciaVentana.getGradeoComicTextField(),
				ListasComicsDAO.listaGradeo);
		FuncionesManejoFront.asignarAutocompletado(referenciaVentana.getNumeroComicTextField(),
				ListasComicsDAO.listaNumeroComic);
		FuncionesManejoFront.asignarAutocompletado(referenciaVentana.getNombreEmpresaTextField(),
				ListasComicsDAO.listaEmpresa);
	}

	public static void controlarEventosInterfaz() {

		referenciaVentana.getProntInfoTextArea().textProperty().addListener((observable, oldValue, newValue) -> {
			FuncionesTableView.ajustarAnchoVBox();
		});

		// Desactivar el enfoque en el VBox para evitar que reciba eventos de teclado
		referenciaVentana.getRootVBox().setFocusTraversable(false);

		// Agregar un filtro de eventos para capturar el enfoque en el TableView y
		// desactivar el enfoque en el VBox
		referenciaVentana.getTablaBBDD().addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
			referenciaVentana.getRootVBox().setFocusTraversable(false);
			referenciaVentana.getTablaBBDD().requestFocus();
		});

		referenciaVentana.getImagenComic().imageProperty().addListener((observable, oldImage, newImage) -> {
			if (newImage != null) {
				// Cambiar la apariencia del cursor y la opacidad cuando la imagen se ha cargado
				referenciaVentana.getImagenComic().setOnMouseEntered(e -> {
					if (referenciaVentana.getImagenComic() != null) {
						referenciaVentana.getImagenComic().setOpacity(0.7);
						referenciaVentana.getImagenComic().setCursor(Cursor.HAND);
					}
				});

				// Restaurar el cursor y la opacidad al salir del ImageView
				referenciaVentana.getImagenComic().setOnMouseExited(e -> {

					if (referenciaVentana.getImagenComic() != null) {

						referenciaVentana.getImagenComic().setOpacity(1.0); // Restaurar la opacidad
						referenciaVentana.getImagenComic().setCursor(Cursor.DEFAULT);
					}

				});
			} else {
				// Restaurar el cursor y la opacidad al salir del ImageView
				referenciaVentana.getImagenComic()
						.setOnMouseEntered(e -> referenciaVentana.getImagenComic().setCursor(Cursor.DEFAULT));
			}
		});

	}

	public static void controlarEventosInterfazAccion() {
		controlarEventosInterfaz();

		// Establecemos un evento para detectar cambios en el segundo TextField
		referenciaVentana.getIdComicTratarTextField().textProperty().addListener((observable, oldValue, newValue) -> {
			// Verificar que newValue no sea null antes de usarlo
			AccionSeleccionar.mostrarComic(newValue, false);

			if (newValue != null && !newValue.isEmpty()) {

				if ("modificar".equals(AccionFuncionesComunes.TIPO_ACCION)) {

					AccionSeleccionar.verBasedeDatos(true, true, null);
					ComicGradeo comic = ComicManagerDAO.comicDatos(newValue);

					referenciaVentana.getBotonEliminar().setVisible(true);
					referenciaVentana.getBotonModificarComic().setVisible(true);

					referenciaVentana.getBotonEliminar().setDisable(false);
					referenciaVentana.getBotonModificarComic().setDisable(false);

					referenciaVentana.getTablaBBDD().getSelectionModel().select(comic);
					referenciaVentana.getTablaBBDD().scrollTo(comic); // Esto hará scroll hasta el elemento seleccionado
				}

			}
		});

		List<Node> elementos = Arrays.asList(referenciaVentana.getBotonGuardarComic(),
				referenciaVentana.getBotonEliminarImportadoComic(),
				referenciaVentana.getBotonEliminarImportadoListaComic(), referenciaVentana.getBotonGuardarListaComics(),
				referenciaVentana.getBotonEliminarImportadoComic(), referenciaVentana.getBotonGuardarComic());

		ListasComicsDAO.comicsImportados.addListener((ListChangeListener<ComicGradeo>) change -> {
			while (change.next()) {

				if (!change.wasAdded() && ListasComicsDAO.comicsImportados.isEmpty()) {
					Utilidades.cambiarVisibilidad(elementos, true);
				}
			}
		});
	}

	public static void controlarEventosInterfazPrincipal(AccionReferencias referenciaVentana) {
		controlarEventosInterfaz();

		referenciaVentana.getTablaBBDD().getSelectionModel().selectedItemProperty()
				.addListener((obs, oldSelection, newSelection) -> {

					if (newSelection != null) {
						// Esto algo hace pero seguramente cambie de idea. Mejor no tocar
//						Comic idRow = referenciaVentana.getTablaBBDD().getSelectionModel().getSelectedItem();
					}
				});

		// Establecer un Listener para el tamaño del AnchorPane
		referenciaVentana.getRootAnchorPane().widthProperty().addListener((observable, oldValue, newValue) -> {

			FuncionesTableView.ajustarAnchoVBox();
			FuncionesTableView.seleccionarRaw();
			FuncionesTableView.modificarColumnas(true);
		});
	}

	public static ComicGradeo camposComic(List<String> camposComic, boolean esAccion) {
		ComicGradeo comicTemp = new ComicGradeo();

		// Asignar los valores a las variables correspondientes
		String nomComic = camposComic.get(0);
		String numComic = camposComic.get(1);
		String edicionComic = camposComic.get(2);
		String coleccionComic = camposComic.get(3);
		String gradeoComic = camposComic.get(4);

		String empresaComic = "";
		String codigoComic = "";
		String urlReferenciaComic = "";
		String direccionImagenComic = "";
		String idComicTratar = "";
		String anioComic = "";
		if (esAccion) {
			anioComic = camposComic.get(4);
			urlReferenciaComic = camposComic.get(5);
			empresaComic = camposComic.get(6);
			gradeoComic = camposComic.get(7);
			codigoComic = camposComic.get(8);
			direccionImagenComic = camposComic.get(9);
			idComicTratar = camposComic.get(11);
		}
		comicTemp.setNomComic(Utilidades.defaultIfNullOrEmpty(nomComic, ""));
		comicTemp.setNumComic(Utilidades.defaultIfNullOrEmpty(numComic, ""));
		comicTemp.setEdicionComic(Utilidades.defaultIfNullOrEmpty(edicionComic, ""));
		comicTemp.setColeccionComic(Utilidades.defaultIfNullOrEmpty(coleccionComic, ""));
		comicTemp.setGradeoComic(Utilidades.defaultIfNullOrEmpty(gradeoComic, ""));
		comicTemp.setEmpresaComic(Utilidades.defaultIfNullOrEmpty(empresaComic, ""));
		comicTemp.setAnioComic(Utilidades.defaultIfNullOrEmpty(anioComic, ""));
		comicTemp.setCodComic(Utilidades.defaultIfNullOrEmpty(codigoComic, ""));
		comicTemp.setUrlReferenciaComic(Utilidades.defaultIfNullOrEmpty(urlReferenciaComic, ""));
		comicTemp.setDireccionImagenComic(Utilidades.defaultIfNullOrEmpty(direccionImagenComic, ""));
		comicTemp.setIdComic(Utilidades.defaultIfNullOrEmpty(idComicTratar, ""));

		return comicTemp;
	}

	public static List<String> comprobarYDevolverLista(List<ComboBox<String>> comboBoxes,
			ObservableList<Control> observableList) {
		List<String> valores = new ArrayList<>();
		for (ComboBox<String> comboBox : comboBoxes) {
			valores.add(comboBox.getValue() != null ? comboBox.getValue() : "");
		}
		if (contieneNulo(comboBoxes)) {
			return Arrays.asList(observableList.stream()
					.map(control -> control instanceof TextInputControl ? ((TextInputControl) control).getText() : "")
					.toArray(String[]::new));
		} else {
			return valores;
		}
	}

	private static <T> boolean contieneNulo(List<T> lista) {

		if (lista == null) {
			return false;
		}

		for (T elemento : lista) {
			if (elemento == null) {
				return true;
			}
		}
		return false;
	}

	public static ComicGradeo comicModificado() {

		String id_comicTemp = referenciaVentana.getIdComicTratarTextField().getText();

		ComicGradeo comicTempTemp = ComicManagerDAO.comicDatos(id_comicTemp);

		List<String> controls = new ArrayList<>();

		for (Control control : AccionReferencias.getListaTextFields()) {
			controls.add(((TextField) control).getText()); // Add the Control object itself
		}

		// Añadir valores de los ComboBoxes de getListaComboboxes() a controls
		for (ComboBox<?> comboBox : AccionReferencias.getListaComboboxes()) {
			Object selectedItem = comboBox.getSelectionModel().getSelectedItem();
			controls.add(selectedItem != null ? selectedItem.toString() : "");
		}

		ComicGradeo datos = camposComic(controls, true);

		int numComic = Integer.parseInt(datos.getNumComic());

		ComicGradeo comicTempModificado = new ComicGradeo();
		comicTempModificado.setIdComic(comicTempTemp.getIdComic());
		comicTempModificado
				.setNomComic(Utilidades.defaultIfNullOrEmpty(datos.getNomComic(), comicTempTemp.getNomComic()));
		comicTempModificado.setNumComic(numComic != 0 ? datos.getNumComic() : comicTempTemp.getNumComic());
		comicTempModificado.setEdicionComic(
				Utilidades.defaultIfNullOrEmpty(datos.getEdicionComic(), comicTempTemp.getEdicionComic()));
		comicTempModificado.setColeccionComic(
				Utilidades.defaultIfNullOrEmpty(datos.getColeccionComic(), comicTempTemp.getColeccionComic()));
		comicTempModificado.setGradeoComic(
				Utilidades.defaultIfNullOrEmpty(datos.getGradeoComic(), comicTempTemp.getGradeoComic()));
		comicTempModificado.setUrlReferenciaComic(
				Utilidades.defaultIfNullOrEmpty(datos.getUrlReferenciaComic(), comicTempTemp.getUrlReferenciaComic()));
		comicTempModificado.setDireccionImagenComic(Utilidades.defaultIfNullOrEmpty(datos.getDireccionImagenComic(),
				comicTempTemp.getDireccionImagenComic()));
		comicTempModificado.setEmpresaComic(
				Utilidades.defaultIfNullOrEmpty(datos.getEmpresaComic(), comicTempTemp.getEmpresaComic()));

		// Si hay otros campos que deben ser ignorados, simplemente no los incluimos
		// A continuación, puedes agregar cualquier lógica adicional que necesites

		return comicTempModificado;

	}

	public static AccionReferencias getReferenciaVentana() {
		return referenciaVentana;
	}

	public static void setReferenciaVentana(AccionReferencias referenciaVentana) {
		AccionControlUI.referenciaVentana = referenciaVentana;
	}

}