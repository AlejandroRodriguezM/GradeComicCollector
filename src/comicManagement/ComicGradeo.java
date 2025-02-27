package comicManagement;

import java.util.Objects;
import dbmanager.ComicManagerDAO;
import dbmanager.ListasComicsDAO;
import funcionesAuxiliares.Utilidades;

/**
 * Esta clase representa un cómic que estará dentro de la base de datos.
 *
 * @autor Alejandro Rodriguez
 */
public class ComicGradeo implements Cloneable {

	protected String idComic;
	protected String tituloComic;
	protected String codigoComic;
	protected String numeroComic;
	protected String fechaGradeo;
	protected String editorComic;
	protected String gradeoComic;
	protected String keyComentarios;
	protected String firmaComic;
	protected String valorComic;
	protected String artistaComic;
	protected String guionistaComic;
	protected String varianteComic;
	protected String direccionImagenComic;
	protected String urlReferenciaComic;

	// Getters
	public String getIdComic() {
		return idComic;
	}

	public String getTituloComic() {
		return tituloComic;
	}

	public String getCodigoComic() {
		return codigoComic;
	}

	public String getNumeroComic() {
		return numeroComic;
	}

	public String getFechaGradeo() {
		return fechaGradeo;
	}

	public String getEditorComic() {
		return editorComic;
	}

	public String getGradeoComic() {
		return gradeoComic;
	}

	public String getKeyComentarios() {
		return keyComentarios;
	}

	public String getValorComic() {
		return valorComic;
	}

	public String getFirmaComic() {
		return firmaComic;
	}

	public String getArtistaComic() {
		return artistaComic;
	}

	public String getGuionistaComic() {
		return guionistaComic;
	}

	public String getVarianteComic() {
		return varianteComic;
	}

	public String getDireccionImagenComic() {
		return direccionImagenComic;
	}

	public String getUrlReferenciaComic() {
		return urlReferenciaComic;
	}

	// Setters
	public void setIdComic(String idComic) {
		this.idComic = idComic;
	}

	public void setTituloComic(String tituloComic) {
		this.tituloComic = tituloComic;
	}

	public void setCodigoComic(String codigoComic) {
		this.codigoComic = codigoComic;
	}

	public void setNumeroComic(String numeroComic) {
		this.numeroComic = numeroComic;
	}

	public void setFechaGradeo(String fechaGradeo) {
		this.fechaGradeo = fechaGradeo;
	}

	public void setEditorComic(String editorComic) {
		this.editorComic = editorComic;
	}

	public void setGradeoComic(String gradeoComic) {
		this.gradeoComic = gradeoComic;
	}

	public void setKeyComentarios(String keyComentarios) {
		this.keyComentarios = keyComentarios;
	}

	public void setFirmaComic(String firmaComic) {
		this.firmaComic = firmaComic;
	}

	public void setValorComic(String valorComic) {
		this.valorComic = valorComic;
	}

	public void setArtistaComic(String artistaComic) {
		this.artistaComic = artistaComic;
	}

	public void setGuionistaComic(String guionistaComic) {
		this.guionistaComic = guionistaComic;
	}

	public void setVarianteComic(String varianteComic) {
		this.varianteComic = varianteComic;
	}

	public void setDireccionImagenComic(String direccionImagenComic) {
		this.direccionImagenComic = direccionImagenComic;
	}

	public void setUrlReferenciaComic(String urlReferenciaComic) {
		this.urlReferenciaComic = urlReferenciaComic;
	}

	// Builder class
	public static class ComicGradeoBuilder {
		private String idComic;
		private String tituloComic;
		private String codigoComic;
		private String numeroComic;
		private String fechaGradeo;
		private String editorComic;
		private String gradeoComic;
		private String keyComentarios;
		private String firmaComic;
		private String valorComic;
		private String artistaComic;
		private String guionistaComic;
		private String varianteComic;
		private String direccionImagenComic;
		private String urlReferenciaComic;

		public ComicGradeoBuilder(String idComic, String tituloComic) {
			this.idComic = idComic;
			this.tituloComic = tituloComic;
		}

		public ComicGradeoBuilder codigoComic(String codigoComic) {
			this.codigoComic = codigoComic;
			return this;
		}

		public ComicGradeoBuilder numeroComic(String numeroComic) {
			this.numeroComic = numeroComic;
			return this;
		}

		public ComicGradeoBuilder fechaGradeo(String fechaGradeo) {
			this.fechaGradeo = fechaGradeo;
			return this;
		}

		public ComicGradeoBuilder editorComic(String editorComic) {
			this.editorComic = editorComic;
			return this;
		}

		public ComicGradeoBuilder gradeoComic(String gradeoComic) {
			this.gradeoComic = gradeoComic;
			return this;
		}

		public ComicGradeoBuilder keyComentarios(String keyComentarios) {
			this.keyComentarios = keyComentarios;
			return this;
		}

		public ComicGradeoBuilder firmaComic(String firmaComic) {
			this.firmaComic = firmaComic;
			return this;
		}

		public ComicGradeoBuilder valorComic(String valorComic) {
			this.valorComic = valorComic;
			return this;
		}

		public ComicGradeoBuilder artistaComic(String artistaComic) {
			this.artistaComic = artistaComic;
			return this;
		}

		public ComicGradeoBuilder guionistaComic(String guionistaComic) {
			this.guionistaComic = guionistaComic;
			return this;
		}

		public ComicGradeoBuilder varianteComic(String varianteComic) {
			this.varianteComic = varianteComic;
			return this;
		}

		public ComicGradeoBuilder direccionImagenComic(String direccionImagenComic) {
			this.direccionImagenComic = direccionImagenComic;
			return this;
		}

		public ComicGradeoBuilder urlReferenciaComic(String urlReferenciaComic) {
			this.urlReferenciaComic = urlReferenciaComic;
			return this;
		}

		public ComicGradeo build() {
			return new ComicGradeo(this);
		}
	}

	private ComicGradeo(ComicGradeoBuilder builder) {
		this.idComic = builder.idComic;
		this.tituloComic = builder.tituloComic;
		this.codigoComic = builder.codigoComic;
		this.numeroComic = builder.numeroComic;
		this.fechaGradeo = builder.fechaGradeo;
		this.editorComic = builder.editorComic;
		this.gradeoComic = builder.gradeoComic;
		this.keyComentarios = builder.keyComentarios;
		this.firmaComic = builder.firmaComic;
		this.valorComic = builder.valorComic;
		this.artistaComic = builder.artistaComic;
		this.guionistaComic = builder.guionistaComic;
		this.varianteComic = builder.varianteComic;
		this.direccionImagenComic = builder.direccionImagenComic;
		this.urlReferenciaComic = builder.urlReferenciaComic;
	}

	public ComicGradeo() {
		this.idComic = "";
		this.tituloComic = "";
		this.codigoComic = "";
		this.numeroComic = "";
		this.fechaGradeo = "";
		this.editorComic = "";
		this.gradeoComic = "";
		this.keyComentarios = "";
		this.firmaComic = "";
		this.valorComic = "";
		this.artistaComic = "";
		this.guionistaComic = "";
		this.varianteComic = "";
		this.direccionImagenComic = "";
		this.urlReferenciaComic = "";
	}

	public static ComicGradeo obtenerComic(String idComic) {
		boolean existeComic = ComicManagerDAO.comprobarIdentificadorComic(idComic);
		if (!existeComic) {
			existeComic = ListasComicsDAO.verificarIDExistente(idComic);
			if (existeComic) {
				return ListasComicsDAO.devolverComicLista(idComic);
			}
		} else {
			return ComicManagerDAO.comicDatos(idComic);
		}
		return null;
	}

	public boolean estaVacio() {
		return isNullOrEmpty(this.tituloComic) && this.numeroComic.equals("0") && isNullOrEmpty(this.gradeoComic)
				&& isNullOrEmpty(this.urlReferenciaComic) && isNullOrEmpty(this.direccionImagenComic)
				&& isNullOrEmpty(this.editorComic);
	}

	private boolean isNullOrEmpty(String str) {
		return str == null || str.isEmpty();
	}

	public static void limpiarCamposComic(ComicGradeo comic) {
		comic.setTituloComic(limpiarCampo(comic.getTituloComic()));
		comic.setGradeoComic(limpiarCampo(comic.getGradeoComic()));
		comic.setEditorComic(limpiarCampo(comic.getEditorComic()));
	}

	public static String limpiarCampo(String campo) {
		if (campo != null) {
			campo = campo.replaceAll("^\\s*[,\\s-]+", ""); // Al principio
			campo = campo.replaceAll("[,\\s-]+\\s*$", ""); // Al final
			campo = campo.replaceAll(",\\s*,", ","); // Comas repetidas
			campo = campo.replaceAll(",\\s*", " - "); // Reemplazar ", " por " - "
			campo = campo.replace("'", " "); // Reemplazar comillas simples por espacios
		} else {
			return "";
		}
		return campo;
	}

	public static boolean validarComic(ComicGradeo comic) {
		if (comic.getTituloComic() != null && !comic.getTituloComic().isEmpty()) {
			return true;
		}
		if (!comic.getNumeroComic().equals("0")) {
			return true;
		}
		if (comic.getGradeoComic() != null && !comic.getGradeoComic().isEmpty()) {
			return true;
		}
		if (comic.getEditorComic() != null && !comic.getEditorComic().isEmpty()) {
			return true;
		}
		return false;
	}

	@Override
	public String toString() {
		StringBuilder contenidoComic = new StringBuilder();

		Utilidades.appendIfNotEmpty(contenidoComic, "ID", idComic);
		Utilidades.appendIfNotEmpty(contenidoComic, "Título", tituloComic);
		Utilidades.appendIfNotEmpty(contenidoComic, "Código", codigoComic);
		Utilidades.appendIfNotEmpty(contenidoComic, "Número", numeroComic);
		Utilidades.appendIfNotEmpty(contenidoComic, "Fecha de Gradeo", fechaGradeo);
		Utilidades.appendIfNotEmpty(contenidoComic, "Editor", editorComic);
		Utilidades.appendIfNotEmpty(contenidoComic, "Gradeo", gradeoComic);
		Utilidades.appendIfNotEmpty(contenidoComic, "Comentarios Clave", keyComentarios);
		Utilidades.appendIfNotEmpty(contenidoComic, "Firmado por", firmaComic);
		Utilidades.appendIfNotEmpty(contenidoComic, "Valor del gradeo", valorComic);
		Utilidades.appendIfNotEmpty(contenidoComic, "Artista", artistaComic);
		Utilidades.appendIfNotEmpty(contenidoComic, "Guionista", guionistaComic);
		Utilidades.appendIfNotEmpty(contenidoComic, "Variante", varianteComic);
		Utilidades.appendIfNotEmpty(contenidoComic, "Dirección de Imagen", direccionImagenComic);
		Utilidades.appendIfNotEmpty(contenidoComic, "URL de Referencia", urlReferenciaComic);

		return contenidoComic.toString();
	}

	// Método para sustituir comillas dobles, comillas simples y punto y coma por
	// una cadena vacía
	public void sustituirCaracteres(ComicGradeo comic) {
		comic.setIdComic(comic.getIdComic().replaceAll("[\"';]", ""));
		comic.setTituloComic(comic.getTituloComic().replaceAll("[\"';]", ""));
		comic.setCodigoComic(comic.getCodigoComic().replaceAll("[\"';]", ""));
		comic.setNumeroComic(comic.getNumeroComic().replaceAll("[\"';]", ""));
		comic.setFechaGradeo(comic.getFechaGradeo().replaceAll("[\"';]", ""));
		comic.setEditorComic(comic.getEditorComic().replaceAll("[\"';]", ""));
		comic.setKeyComentarios(comic.getKeyComentarios().replaceAll("[\"';]", ""));
		comic.setFirmaComic(comic.getFirmaComic().replaceAll("[\"';]", ""));
		comic.setValorComic(comic.getValorComic().replaceAll("[\"';]", ""));
		comic.setArtistaComic(comic.getArtistaComic().replaceAll("[\"';]", ""));
		comic.setGuionistaComic(comic.getGuionistaComic().replaceAll("[\"';]", ""));
		comic.setVarianteComic(comic.getVarianteComic().replaceAll("[\"';]", ""));
		comic.setDireccionImagenComic(comic.getDireccionImagenComic().replaceAll("[\"';]", ""));
		comic.setUrlReferenciaComic(comic.getUrlReferenciaComic().replaceAll("[\"';]", ""));
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		ComicGradeo comic = (ComicGradeo) o;
		return Objects.equals(codigoComic, comic.codigoComic) && Objects.equals(tituloComic, comic.tituloComic)
				&& Objects.equals(numeroComic, comic.numeroComic) && Objects.equals(editorComic, comic.editorComic);
	}

	@Override
	public int hashCode() {
		return Objects.hash(codigoComic, tituloComic, numeroComic, editorComic);
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
}
