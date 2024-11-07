package azivko_zadaca_1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class UcitavacPodatakaStanica implements UcitavacPodataka {
	private List<String> errors = new ArrayList<>();
	private int ukupnaDuzinaPruge = 0;
	private String oznakaProslePruge = "";

	@Override
	public List<String> ucitaj(String imeDatoteke) {
		try (BufferedReader reader = new BufferedReader(new FileReader(imeDatoteke))) {
			processFileContent(reader);
			handleLastTrackLength();
		} catch (IOException e) {
			errors.add("Failed to read file: " + imeDatoteke);
		}
		return errors;
	}

	private void processFileContent(BufferedReader reader) throws IOException {
		String line;
		int lineNumber = 0;
		while ((line = reader.readLine()) != null) {
			lineNumber++;
			if(line.contains("Stanica;"))
				lineNumber = 0;
			if (shouldSkipLine(line, lineNumber))
				continue;

			String[] data = line.split(";");
			if (!isValidStationData(data, lineNumber, line))
				continue;
			processValidLine(data);
		}
	}

	private boolean shouldSkipLine(String line, int lineNumber) {
		return line.trim().isEmpty() || line.startsWith("#") || line.contains("Stanica;") || line.startsWith(";");
	}

	private boolean isValidStationData(String[] data, int lineNumber, String line) {
		boolean valid = true;
		if (data.length != 14) {
			addErrorToList(lineNumber, line, "Not enough fields");
			return false;
		}

		data[10] = data[10].replace(",", ".");
		data[11] = data[11].replace(",", ".");

		if (!Pattern.compile(RegExUtils.LRM_REGEX).matcher(data[1]).matches()) {
			addErrorToList(lineNumber, line, "Column 2 (Oznaka pruge) is not a valid value");
			valid = false;
		}

		if (!"staj.".contains(data[2]) && !"kol.".contains(data[2])) {
			addErrorToList(lineNumber, line, "Column 3 (Vrsta stanice) is not a valid value");
			valid = false;
		}

		if (!"O".equals(data[3]) && !"Z".equals(data[3])) {
			addErrorToList(lineNumber, line, "Column 4 (Status stanice) is not a valid value");
			valid = false;
		}

		if (!"DA".equals(data[4]) && !"NE".equals(data[4])) {
			addErrorToList(lineNumber, line, "Column 5 (Putnici ul/iz) is not a valid value");
			valid = false;
		}

		if (!"DA".equals(data[5]) && !"NE".equals(data[5])) {
			addErrorToList(lineNumber, line, "Column 6 (Roba ul/iz) is not a valid value");
			valid = false;
		}

		if (!"M".equals(data[6]) && !"L".equals(data[6]) && !"R".equals(data[6])) {
			addErrorToList(lineNumber, line, "Column 7 (Kategorija pruge) is not a valid value");
			valid = false;
		}

		if (!"K".equals(data[8]) && !"E".equals(data[8])) {
			addErrorToList(lineNumber, line, "Column 9 (Vrsta pruge) is not a valid value");
			valid = false;
		}

		if (!"I".equals(data[12]) && !"K".equals(data[12]) && !"Z".equals(data[12])) {
			addErrorToList(lineNumber, line, "Column 13 (Status pruge) is not a valid value");
			valid = false;
		}

		if (!Pattern.compile(RegExUtils.isNumberRegex).matcher(data[7]).matches()) {
			addErrorToList(lineNumber, line, "Column 8 (Broj perona) is not a valid integer");
			valid = false;
		} else if (Integer.valueOf(data[7]) < 1 || Integer.valueOf(data[7]) > 99) {
			addErrorToList(lineNumber, line, "Column 8 (Broj perona) is not in the valid range (1-99)");
			valid = false;
		}

		if (!Pattern.compile(RegExUtils.isNumberRegex).matcher(data[9]).matches()) {
			addErrorToList(lineNumber, line, "Column 10 (Broj kolosijeka) is not a valid integer");
			valid = false;
		} else if (Integer.valueOf(data[9]) > 2 || Integer.valueOf(data[9]) < 1) {
			addErrorToList(lineNumber, line, "Column 10 (Broj kolosijeka) is not in the valid range (1-2)");
			valid = false;
		}

		if (!Pattern.compile(RegExUtils.isNumberRegex).matcher(data[13]).matches()) {
			addErrorToList(lineNumber, line, "Column 14 (Duzina) is not a valid integer");
			valid = false;
		} else if (Integer.valueOf(data[13]) < 0 || Integer.valueOf(data[13]) > 999) {
			addErrorToList(lineNumber, line, "Column 14 (Duzina) is not in the valid range (1-999)");
			valid = false;
		}

		if (!Pattern.compile(RegExUtils.isDoubleRegex).matcher(data[10]).matches()) {
			addErrorToList(lineNumber, line, "Column 11 (DO po osovini) is not a valid double");
			valid = false;
		} else if (Double.valueOf(data[10]) < 10 || Double.valueOf(data[10]) > 50) {
			addErrorToList(lineNumber, line, "Column 11 (DO po osovini) is not in the valid range (1-50)");
			valid = false;
		}

		if (!Pattern.compile(RegExUtils.isDoubleRegex).matcher(data[11]).matches()) {
			addErrorToList(lineNumber, line, "Column 12 (DO po duznom metru) is not a valid double");
			valid = false;
		} else if (Double.valueOf(data[11]) < 2 || Double.valueOf(data[11]) > 10) {
			addErrorToList(lineNumber, line, "Column 12 (DO po duznom metru) is not in the valid range (2-10)");
			valid = false;
		}
		return valid;
	}

	private void processValidLine(String[] data) {
		updateTrackLength(data);
		processStationData(data);
	}

	private void updateTrackLength(String[] data) {
		if (data[1].contains(oznakaProslePruge)) {
			ukupnaDuzinaPruge += Integer.valueOf(data[13]);
		} else {
			if (!oznakaProslePruge.isEmpty()) {
				ZeljeznickaMreza.getInstance().vratiPrugu(oznakaProslePruge).setDuzina(ukupnaDuzinaPruge);
			}
			ukupnaDuzinaPruge = Integer.valueOf(data[13]);
		}
		oznakaProslePruge = data[1];
	}

	private void processStationData(String[] data) {
		ZeljeznickaPruga pruga = createTrack(data);
		ZeljeznickaMreza.getInstance().dodajPrugu(data[1], pruga);

		ZeljeznickaStanica stanica = createStation(data);
		ZeljeznickaPruga vracenaPruga = ZeljeznickaMreza.getInstance().vratiPrugu(data[1]);
		vracenaPruga.dodajStanicu(stanica);
	}

	private ZeljeznickaPruga createTrack(String[] data) {
		return new ZeljeznickaPruga(data[1], determineTrackCategory(data[6]), determineTransportMethod(data[8]),
				Integer.valueOf(data[9]), Double.parseDouble(data[10]), Double.parseDouble(data[11]),
				determineTrackStatus(data[12]));
	}

	private ZeljeznickaStanica createStation(String[] data) {
		return new ZeljeznickaStanica(data[0], determineStationType(data[2]),
				determineStationActivities(data[4], data[5]), Integer.valueOf(data[7]), "O".equals(data[3]),
				ukupnaDuzinaPruge);
	}

	private KategorijaPruge determineTrackCategory(String category) {
		if ("M".equals(category))
			return KategorijaPruge.MEDUANRODNA;
		if ("R".equals(category))
			return KategorijaPruge.REGIONALNA;
		if ("L".equals(category))
			return KategorijaPruge.LOKALNA;
		return null;
	}

	private NacinPrijevoza determineTransportMethod(String method) {
		if ("K".equals(method))
			return NacinPrijevoza.K;
		if ("E".equals(method))
			return NacinPrijevoza.E;
		return null;
	}

	private StatusPruge determineTrackStatus(String status) {
		if ("I".equals(status))
			return StatusPruge.ISPRAVNA;
		if ("K".equals(status))
			return StatusPruge.KVAR;
		if ("Z".equals(status))
			return StatusPruge.ZATVORENA;
		return null;
	}

	private VrstaStanice determineStationType(String type) {
		if ("kol.".equals(type))
			return VrstaStanice.KOL;
		if ("staj.".equals(type))
			return VrstaStanice.STAJ;
		return null;
	}

	private AktivnostiStanice determineStationActivities(String uip, String uir) {
		if ("DA".equals(uip) && "DA".equals(uir))
			return AktivnostiStanice.OBA;
		if ("DA".equals(uip))
			return AktivnostiStanice.UIP;
		if ("DA".equals(uir))
			return AktivnostiStanice.UIR;
		return null;
	}

	private void handleLastTrackLength() {
		if (!oznakaProslePruge.isEmpty()) {
			ZeljeznickaMreza.getInstance().vratiPrugu(oznakaProslePruge).setDuzina(ukupnaDuzinaPruge);
		}
	}

	private void addErrorToList(int lineNumber, String line, String message) {
		errors.add("Error in line " + lineNumber + ": " + line + " - " + message + ".");
	}

}