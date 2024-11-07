package azivko_zadaca_1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Pattern;

public class ProcesorKomanda {
	private static ProcesorKomanda instance;

	private ProcesorKomanda() {
	}

	public static ProcesorKomanda getInstance() {
		if (instance == null) {
			instance = new ProcesorKomanda();
		}
		return instance;
	}

	public void pokreniInteraktivanNacinRada() {
		System.out.println("\n---Interaktivan nacin rada---\n");
		try (Scanner skener = new Scanner(System.in)) {
			while (true) {
				System.out.print("Komanda: ");
				String komanda = skener.nextLine().toUpperCase();
				if (komanda.equals("Q")) {
					break;
				}
				procesirajKomandu(komanda);
			}
		}
	}

	public void procesirajKomandu(String komanda) {
		if (komanda.startsWith("IP")) {
			obradiIP();
		} else if (komanda.startsWith("ISP")) {
			obradiISP(komanda);
		} else if (komanda.startsWith("ISI2S")) {
			obradiISI2S(komanda);
		} else if (komanda.startsWith("IK")) {
			obradiIK(komanda);
		} else if (komanda.startsWith("IV")) {
			obradiIV();
		} else {
			System.out.println("Nepoznata komanda");
		}
	}

	private void obradiIP() {
		System.out.println("Oznaka pruge - Pocetna stanica - Zavrsna stanica - Ukupan broj kilometara");
		for (Map.Entry<String, ZeljeznickaPruga> entry : ZeljeznickaMreza.getInstance().vratiListuPruga().entrySet()) {
			String oznakaPruge = entry.getKey();
			ZeljeznickaPruga pruga = entry.getValue();
			System.out.println(oznakaPruge + " - " + pruga.getPopisStanica().getFirst().getStanicaNaziv() + " - "
					+ pruga.getPopisStanica().getLast().getStanicaNaziv() + " - " + pruga.getDuzina());
		}
	}

	private void obradiISP(String komanda) {
		if (!Pattern.compile(RegExUtils.ISP_REGEX).matcher(komanda).matches()) {
			System.out.println("Format komande nije ispravan");
			return;
		}

		String[] komandaSplit = komanda.split(" ");
		String oznakaPruge = komandaSplit[1];
		String opcija = komandaSplit[2];

		ZeljeznickaPruga pruga = ZeljeznickaMreza.getInstance().vratiPrugu(oznakaPruge);
		if (pruga == null) {
			System.out.println("Pruga s oznakom " + oznakaPruge + " ne postoji");
			return;
		}

		if (opcija.equals("N")) {
			ispisiStaniceNormalanPoredak(pruga);
		} else if (opcija.equals("O")) {
			ispisiStaniceObrnutiPoredak(pruga);
		} else {
			System.out.println("Nepoznata opcija za ISP komandu");
		}
	}

	private void ispisiStaniceNormalanPoredak(ZeljeznickaPruga pruga) {
		System.out.println("Naziv stanice - Vrsta - Broj km od pocetne");
		for (ZeljeznickaStanica stanica : pruga.getPopisStanica()) {
			System.out.println(stanica.getStanicaNaziv() + " " + stanica.getVrstaStanice() + " "
					+ stanica.getUdaljenostOdPocetne());
		}
	}

	private void ispisiStaniceObrnutiPoredak(ZeljeznickaPruga pruga) {
		System.out.println("Naziv stanice - Vrsta - Broj km od zadnje (u obrnutom poretku)");

		List<ZeljeznickaStanica> staniceReverse = new ArrayList<>(pruga.getPopisStanica());
		Collections.reverse(staniceReverse);

		int cumulativeDistance = 0;
		for (int i = 0; i < staniceReverse.size(); i++) {
			ZeljeznickaStanica stanica = staniceReverse.get(i);
			System.out.println(stanica.getStanicaNaziv() + " " + stanica.getVrstaStanice() + " " + cumulativeDistance);
			if (i < staniceReverse.size() - 1) {
				cumulativeDistance += Math
						.abs(staniceReverse.get(i + 1).getUdaljenostOdPocetne() - stanica.getUdaljenostOdPocetne());
			}
		}
	}

	private void obradiISI2S(String komanda) {

	}

	private void obradiIK(String komanda) {
		for (Map.Entry<String, ZeljeznickaKompozicija> kompozicija : ZeljeznickaMreza.getInstance()
				.vratiListuKompozicija().entrySet()) {
			for (VoziloKompozicija voziloKompozicija : kompozicija.getValue().getListaVozila()) {
				PrijevoznoSredstvo vozilo = voziloKompozicija.getVozilo();
				System.out.println(vozilo.getOznaka() + " " + voziloKompozicija.getUloga().name() + " " + vozilo.getGodinaProizvodnje() + " " + vozilo.getNamjenaVozila().name() + " " + vozilo.getVrstaPogona().name() + " " + vozilo.getMaxBrzina());
			}
		}
	}

	// OZNAKA ULOGA OPIS GODINA NAMJENA VRSTA POGONA MAX BRZINA

	private void obradiIV() {
		int brojVozila = 0;
		for (PrijevoznoSredstvo sredstvo : ZeljeznickaMreza.getInstance().vratiListuVozila().values()) {
			System.out.println(sredstvo);
			brojVozila++;
		}
		System.out.println("\n" + brojVozila);
	}
}