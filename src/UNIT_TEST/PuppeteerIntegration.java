package UNIT_TEST;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ficherosFunciones.FuncionesFicheros;

public class PuppeteerIntegration {

	public static void main(String[] args) throws URISyntaxException {
//		String url = "https://www.cardmarket.com/en/Magic/Cards/Sliver-Overlord/Versions";
//		
//		getEnlacesFromPuppeteer(url);

		String palabra = "Sliver Overlord";

		List<String> tal = buscarEnGoogle("Sliver");
		int cont = 0;
		System.out.println(tal.size());
		for (String string : tal) {
			
			System.out.println(string);
			
			System.out.println(getCartaFromPuppeteer(string));
		}

	}

	public static String agregarMasAMayusculas(String cadena) {
		return cadena.toUpperCase();
	}
	
	public static List<String> buscarEnGoogle(String searchTerm) throws URISyntaxException {
		searchTerm = agregarMasAMayusculas(searchTerm).replace("(", "%28").replace(")", "%29").replace("#", "%23");

		try {
			String encodedSearchTerm = URLEncoder.encode(searchTerm, "UTF-8");
			String urlString = "https://www.google.com/search?q=cardmarke+" + encodedSearchTerm + "+versions";

			URI uri = new URI(urlString);
			URL url = uri.toURL();
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			con.setRequestProperty("User-Agent",
					"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/97.0.4692.99 Safari/537.36");

			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuilder content = new StringBuilder();
			while ((inputLine = in.readLine()) != null) {
				content.append(inputLine);
			}
			in.close();
			con.disconnect();

			String html = content.toString();
			int startIndex = html.indexOf("www.cardmarket.com/");
			List<String> urls = new ArrayList<>(); // Use ArrayList to dynamically store URLs

			while (startIndex != -1) {
				int endIndex = html.indexOf("\"", startIndex);
				if (endIndex != -1) {
					String urlFound = html.substring(startIndex, endIndex);

					if (urlFound.endsWith("/Versions")) { // Check if the URL ends with "/Versions"
						List<String> versionLinks = getEnlacesFromPuppeteer("https://" + urlFound);
						if (versionLinks.isEmpty()) {
							return null; // Return null if no versions links are found
						} else {
							urls.add(urlFound); // Add the URL to the list
						}
					} else {
						urls.add(urlFound); // Add the URL to the list
					}
					startIndex = html.indexOf("www.cardmarket.com/", endIndex);
				} else {
					break;
				}
			}

			if (urls.isEmpty()) {
				return null; // Return null if no URLs are found
			} else {
				return urls; // Return the list of URLs found
			}
		} catch (IOException e) {
			e.printStackTrace();
			return null; // Return null in case of exception
		}
	}

	public static List<String> getCartaFromPuppeteer(String url) {
		List<String> dataArrayList = new ArrayList<>();

		try {
			String scriptPath = FuncionesFicheros.rutaDestinoRecursos + File.separator + "scrap.js";
			String command = "node " + scriptPath + " " + url;

			int attempt = 0;
			int backoff = 2000; // Tiempo de espera inicial en milisegundos

			while (true) {
				attempt++;
				Process process = Runtime.getRuntime().exec(command);

				// Leer la salida del proceso
				BufferedReader processReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
				String outputLine;
				StringBuilder output = new StringBuilder();
				while ((outputLine = processReader.readLine()) != null) {
					output.append(outputLine).append("\n");
				}
				processReader.close();

				// Esperar a que termine el proceso
				int exitCode = process.waitFor();
				if (exitCode == 0) {
					// Proceso terminado exitosamente, obtener el resultado
					String dataString = output.toString().trim();
					if (!dataString.isEmpty()) {
						// Dividir los pares clave-valor y añadirlos al List<String>
						String[] keyValuePairs = dataString.split("\n");
						for (String pair : keyValuePairs) {
							dataArrayList.add(pair.trim());
						}
						return dataArrayList;
					} else {
						System.err.println("El resultado obtenido está vacío. Volviendo a intentar...");
						Thread.sleep(backoff); // Esperar antes de intentar nuevamente
						backoff += 10; // Aumentar el tiempo de espera (backoff exponencial)
					}

					if (attempt >= 5) {
						// Si se superan los intentos, devolver un List<String> vacío
						return new ArrayList<>();
					}
				} else {
					// Error al ejecutar el script
					System.err.println("Error al ejecutar el script de Puppeteer");
					break; // Salir del bucle si hay un error
				}
			}
		} catch (InterruptedException | IOException e) {
			e.printStackTrace();
		}
		return new ArrayList<>(); // Devolver un List<String> vacío en caso de excepción
	}

	public static List<String> getEnlacesFromPuppeteer(String url) {
		List<String> dataArrayList = new ArrayList<>();

		try {
			String scriptPath = FuncionesFicheros.rutaDestinoRecursos + File.separator + "scrap2.js";
			String command = "node " + scriptPath + " " + url;

			int attempt = 0;
			int backoff = 2000; // Tiempo de espera inicial en milisegundos

			while (true) {
				attempt++;
				Process process = Runtime.getRuntime().exec(command);

				// Leer la salida del proceso
				BufferedReader processReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
				String outputLine;
				StringBuilder output = new StringBuilder();
				while ((outputLine = processReader.readLine()) != null) {
					output.append(outputLine).append("\n");
				}
				processReader.close();

				// Esperar a que termine el proceso
				int exitCode = process.waitFor();
				if (exitCode == 0) {
					// Proceso terminado exitosamente, obtener el resultado
					String dataString = output.toString().trim();
					if (!dataString.isEmpty()) {
						System.out.println(dataString);
						dataArrayList.add(dataString);
						return dataArrayList;
					} else {
						System.err.println("El resultado obtenido está vacío. Volviendo a intentar...");
						Thread.sleep(backoff); // Esperar antes de intentar nuevamente
						backoff += 10; // Aumentar el tiempo de espera (backoff exponencial)
					}

					if (attempt >= 5) {
						// Si se superan los intentos, devolver un List<String> vacío
						return new ArrayList<>();
					}
				} else {
					// Error al ejecutar el script
					System.err.println("Error al ejecutar el script de Puppeteer");
					break; // Salir del bucle si hay un error
				}
			}
		} catch (InterruptedException | IOException e) {
			e.printStackTrace();
		}
		return new ArrayList<>(); // Devolver un List<String> vacío en caso de excepción
	}

}