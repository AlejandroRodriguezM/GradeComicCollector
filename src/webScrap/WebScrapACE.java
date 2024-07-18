package webScrap;

import java.io.File;
import java.util.List;

import comicManagement.ComicGradeo;
import ficherosFunciones.FuncionesFicheros;

public class WebScrapACE {

	public static ComicGradeo extraerDatosMTG(String codigoCarta) {
		String scriptPath = FuncionesFicheros.rutaDestinoRecursos + File.separator + "scrapACE.js";
		List<String> data = FuncionesScrapeoComunes.getCartaFromPuppeteer(codigoCarta, scriptPath);
		
		String nombre = "";
		String codigo = "";
		String numero = "";
		String anio = "";
		String edicion = "";
		String empresa = "";
		String gradeo = "";
		String referencia = "";
		

		for (String line : data) {
			if (line.startsWith("Nombre: ")) {
				nombre = line.substring("Nombre: ".length()).trim().replaceAll("\\(.*\\)", "").trim();
			}
			else if (line.startsWith("Codigo: ")) {
				codigo = line.substring("Codigo: ".length()).trim();
			}
			else if (line.startsWith("Numero: ")) {
				numero = line.substring("Numero: ".length()).trim();
			}
			else if (line.startsWith("Anio: ")) {
				anio = line.substring("Anio: ".length()).trim();
			}
			else if (line.startsWith("Edicion: ")) {
				edicion = line.substring("Edicion: ".length()).trim();
			}
			else if (line.startsWith("Empresa: ")) {
				empresa = line.substring("Empresa: ".length()).trim();
			}
			else if (line.startsWith("Gradeo: ")) {
				gradeo = line.substring("Gradeo: ".length()).trim();
			}
			else if (line.startsWith("Referencia: ")) {
				referencia = line.substring("Referencia: ".length()).trim();
			}
		}
        return new ComicGradeo.CartaGradeoBuilder("", nombre)
                .codCarta(codigo)
                .numCarta(numero)
                .anioCarta(anio)
                .coleccionCarta("")
                .edicionCarta(edicion)
                .empresaCarta(empresa)
                .gradeoCarta(gradeo)
                .urlReferenciaCarta(referencia)
                .direccionImagenCarta("")
                .build();
	}

	public static ComicGradeo devolverCartaBuscada(String urlCarta) {
		return extraerDatosMTG(urlCarta);
	}
}
