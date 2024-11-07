package azivko_zadaca_1;

public class Zeljeznica {

	public static void main(String[] args) {
		if (args.length < 3) {
			System.out.println(
					"Struktura argumenta treba izgledati ovako: java -jar yourApp.jar --zs stationsFile.csv --zps linesFile.csv --zk vehiclesFile.csv");
			return;
		}

		String staniceDatoteka = null;
		String kompozicijeDatoteka = null;
		String vozilaDatoteka = null;
		for (int i = 0; i < args.length; i++) {
			switch (args[i]) {
			case "--zs":
				staniceDatoteka = args[++i];
				break;
			case "--zps":
				vozilaDatoteka = args[++i];
				break;
			case "--zk":
				kompozicijeDatoteka = args[++i];
				break;
			default:
				System.out.println("Unknown argument: " + args[i]);
				return;
			}
		}

		if (staniceDatoteka == null || kompozicijeDatoteka == null || vozilaDatoteka == null) {
			System.out.println("Missing required files. Please provide paths for stations, lines, and vehicles files.");
			return;
		}

		UpraviteljPodataka upraviteljPodataka = UpraviteljPodataka.getInstance();
		upraviteljPodataka.initializeSystem(staniceDatoteka, kompozicijeDatoteka, vozilaDatoteka);

		ProcesorKomanda.getInstance().pokreniInteraktivanNacinRada();
	}

}
