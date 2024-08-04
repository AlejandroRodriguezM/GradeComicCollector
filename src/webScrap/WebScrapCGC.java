package webScrap;

import java.io.File;
import java.util.List;

import comicManagement.ComicGradeo;
import ficherosFunciones.FuncionesFicheros;
import funcionesAuxiliares.Utilidades;

public class WebScrapCGC {

	public static ComicGradeo extraerDatosMTG(String codigoCarta) {

		if (codigoCarta.contains("cgccomics")) {
			String numero = Utilidades.extractNumberFromUrl(codigoCarta);
			codigoCarta = "https://comicbookrealm.com/cgc-analyzer/certificate/id/" + numero;
		}
		if (!codigoCarta.contains("comicbookrealm.com")) {
			codigoCarta = "https://comicbookrealm.com/cgc-analyzer/certificate/id/" + codigoCarta;
		}

		String scriptPath = FuncionesFicheros.rutaDestinoRecursos + File.separator + "scrapCGC.js";
		List<String> data = FuncionesScrapeoComunes.getCartaFromPuppeteer(codigoCarta, scriptPath);
		String certNumber = "";
		String titulo = "";
		String numero = "";
		String editor = "";
		String fechaG = "";
		String grade = "";
		String referencia = "";
		String dibujante = "";
		String variante = "";
		String key = "";
		String firma = "";
		String guionista = "";
		String imageUrl = "";

		for (String line : data) {
			if (line.startsWith("Titulo: ")) {
				titulo = line.substring("Titulo: ".length()).trim();
			} else if (line.startsWith("Certificado: ")) {
				certNumber = line.substring("Certificado: ".length()).trim();
			} else if (line.startsWith("Numero: ")) {
				numero = line.substring("Numero: ".length()).trim();
			} else if (line.startsWith("Editor: ")) {
				editor = line.substring("Editor: ".length()).trim().replace(".", " ");
			} else if (line.startsWith("FechaG: ")) {
				fechaG = line.substring("FechaG: ".length()).trim();
			} else if (line.startsWith("Grade: ")) {
				grade = line.substring("Grade: ".length()).trim();
			} else if (line.startsWith("Firma: ")) {
				firma = Utilidades.corregirNombre(line.substring("Firma: ".length()).trim());
			} else if (line.startsWith("Referencia: ")) {
				referencia = line.substring("Referencia: ".length()).trim();
			} else if (line.startsWith("Dibujante: ")) {
				dibujante = Utilidades.corregirNombre(line.substring("Dibujante: ".length()).trim());
			} else if (line.startsWith("Variante: ")) {
				variante = Utilidades.corregirNombre(line.substring("Variante: ".length()).trim());
			} else if (line.startsWith("Guionista: ")) {
				guionista = Utilidades.corregirNombre(line.substring("Guionista: ".length()).trim());
			} else if (line.startsWith("Imagen: ")) {
				imageUrl = line.substring("Imagen: ".length()).trim();
			} else if (line.startsWith("KeyC: ")) {
				key = line.substring("KeyC: ".length()).trim();
			}
		}

		return new ComicGradeo.ComicGradeoBuilder("", titulo).codigoComic(certNumber).numeroComic(numero)
				.fechaGradeo(fechaG).editorComic(editor).gradeoComic(grade).keyComentarios(key).firmaComic(firma)
				.valorComic("").artistaComic(dibujante).guionistaComic(guionista).varianteComic(variante)
				.direccionImagenComic(imageUrl).urlReferenciaComic(referencia).build();
	}

	public static ComicGradeo devolverCartaBuscada(String urlCarta) {
		return extraerDatosMTG(urlCarta);
	}
}
