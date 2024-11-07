package azivko_zadaca_1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class UcitavacPodatakaVozila implements UcitavacPodataka {
	private List<String> errors = new ArrayList<>();

	@Override
	public List<String> ucitaj(String imeDatoteke) {
		try (BufferedReader reader = new BufferedReader(new FileReader(imeDatoteke))) {
			processFileContent(reader);
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
			if(line.contains("Oznaka;"))
				lineNumber = 0;
			if (shouldSkipLine(line, lineNumber))
				continue;

			String[] data = line.split(";");
			if (!isValidVehicleData(data, lineNumber, line))
				continue;
			processVehicleData(data, lineNumber, line);
		}
	}

	private boolean shouldSkipLine(String line, int lineNumber) {
		return line.trim().isEmpty() || line.startsWith("#") || line.contains("Oznaka;") || line.startsWith(";");
	}

	private void processVehicleData(String[] data, int lineNumber, String line) {
		PrijevoznoSredstvo vozilo = createVehicle(data, lineNumber, line);
		if (vozilo != null) {
			ZeljeznickaMreza.getInstance().dodajVozilo(data[0], vozilo);
		}
	}

	private void formatDataFields(String[] data) {
		data[14] = data[14].replace(",", ".");
		data[15] = data[15].replace(",", ".");
		data[8] = data[8].replace(",", ".");
	}

	private PrijevoznoSredstvo createVehicle(String[] data, int lineNumber, String line) {
		try {
			return new PrijevoznoSredstvo(data[0], determineVehiclePurpose(data[4], lineNumber, line),
					determineVehicleType(data[5], lineNumber, line), determineVehicleDrive(data[6], lineNumber, line),
					Double.valueOf(data[8]), Double.valueOf(data[7]), Integer.valueOf(data[3]), data[2],
					determineSpace(data[9], data[10], data[12], data[11], data[13], lineNumber, line),
					Double.valueOf(data[14]), Integer.valueOf(data[16]), determineStatus(data[17], lineNumber, line));
		} catch (Exception e) {
			errors.add("Error creating vehicle in line " + lineNumber + ": " + line);
			return null;
		}
	}

	private NamjenaVozila determineVehiclePurpose(String purpose, int lineNumber, String line) {
		switch (purpose) {
		case "PSVPVK":
			return NamjenaVozila.PSVPVK;
		case "PSVP":
			return NamjenaVozila.PSVP;
		case "PSBP":
			return NamjenaVozila.PSBP;
		default:
			addErrorToList(lineNumber, line, "Neispravna namjena vozila");
			return null;
		}
	}

	private VrstaPrijevoza determineVehicleType(String type, int lineNumber, String line) {
		switch (type) {
		case "N":
			return VrstaPrijevoza.N;
		case "P":
			return VrstaPrijevoza.P;
		case "TA":
			return VrstaPrijevoza.TA;
		case "TK":
			return VrstaPrijevoza.TK;
		case "TPS":
			return VrstaPrijevoza.TPS;
		case "TRS":
			return VrstaPrijevoza.TRS;
		case "TTS":
			return VrstaPrijevoza.TTS;
		default:
			addErrorToList(lineNumber, line, "Neispravna vrsta prijevoza");
			return null;
		}
	}

	private VrstaPogona determineVehicleDrive(String drive, int lineNumber, String line) {
		switch (drive) {
		case "N":
			return VrstaPogona.N;
		case "D":
			return VrstaPogona.D;
		case "B":
			return VrstaPogona.B;
		case "E":
			return VrstaPogona.E;
		case "S":
			return VrstaPogona.S;
		default:
			addErrorToList(lineNumber, line, "Neispravna vrsta pogona");
			return null;
		}
	}

	private ArrayList<Integer> determineSpace(String sit, String stand, String bed, String bicycle, String car,
			int lineNumber, String line) {
		ArrayList<Integer> space = new ArrayList<>();
		space.add(Integer.valueOf(sit));
		space.add(Integer.valueOf(stand));
		space.add(Integer.valueOf(bed));
		space.add(Integer.valueOf(bicycle));
		space.add(Integer.valueOf(car));

		return space;
	}

	private StatusVozila determineStatus(String status, int lineNumber, String line) {
		switch (status) {
		case "I":
			return StatusVozila.I;
		case "K":
			return StatusVozila.K;
		default:
			addErrorToList(lineNumber, line, "Neispravan status vozila");
			return null;
		}
	}

	private boolean isValidVehicleData(String[] data, int lineNumber, String line) {
		boolean valid = true;
		if (data.length != 18) {
			addErrorToList(lineNumber, line, "Nedostaju podaci");
			return false;
		}

		formatDataFields(data);

		if (!"PSVPVK".equals(data[4]) && !"PSVP".equals(data[4]) && !"PSBP".equals(data[4])) {
			addErrorToList(lineNumber, line,
					"Stupac 5 (Namjena vozila) Nema ispravnu vrijednost, moguce vrijednosti su PSVPVK/PSVP/PSBP");
			valid = false;
		}

		if (!"N".equals(data[5]) && !"P".equals(data[5]) && !"TA".equals(data[5]) && !"TK".equals(data[5])
				&& !"TPS".equals(data[5]) && !"TRS".equals(data[5]) && !"TTS".equals(data[5])) {
			addErrorToList(lineNumber, line,
					"Stupac 6 (Vrsta prijevoza) Nema ispravnu vrijednost, moguce vrijednosti su N/P/TA/TK/TPS/TRS/TTS");
			valid = false;
		}

		if (!"I".equals(data[17]) && !"K".equals(data[17])) {
			addErrorToList(lineNumber, line, "Stupac 18 (Status) Nema ispravnu vrijednost, moguce vrijednosti su I/K");
			valid = false;
		}

		if (!"N".equals(data[6]) && !"D".equals(data[6]) && !"B".equals(data[6]) && !"E".equals(data[6])
				&& !"S".equals(data[6])) {
			addErrorToList(lineNumber, line, "Stupac 7 (Vrsta pogona) Nema ispravnu vrijednost");
			valid = false;
		}

		if (!Pattern.compile(RegExUtils.isNumberRegex).matcher(data[3]).matches()) {
			addErrorToList(lineNumber, line, "Stupac 4 (Godina) nema ispravnu vrijednost");
			valid = false;
		} else if (Integer.valueOf(data[3]) < 1900 || Integer.valueOf(data[3]) > 2024) {
			addErrorToList(lineNumber, line, "Stupac 4 (Godina) nije u dozvoljenom rasponu vrijednosti (1900-2024)");
			valid = false;
		}

		if (!Pattern.compile(RegExUtils.isNumberRegex).matcher(data[7]).matches()) {
			addErrorToList(lineNumber, line, "Stupac 8 (Brzina) nema ispravnu vrijednost");
			valid = false;
		} else if (Integer.valueOf(data[7]) < 1 || Integer.valueOf(data[7]) > 200) {
			addErrorToList(lineNumber, line, "Stupac 8 (Brzina) nije u dozvoljenom rasponu vrijednosti (1-200)");
			valid = false;
		}

		if (!Pattern.compile(RegExUtils.isNumberRegex).matcher(data[9]).matches()) {
			addErrorToList(lineNumber, line, "Stupac 10 (Br. sjedecih mjesta) nema ispravnu vrijednost");
			valid = false;
		}

		if (!Pattern.compile(RegExUtils.isNumberRegex).matcher(data[10]).matches()) {
			addErrorToList(lineNumber, line, "Stupac 11 (Br. stajacih mjesta) nema ispravnu vrijednost");
			valid = false;
		}
		if (!Pattern.compile(RegExUtils.isNumberRegex).matcher(data[11]).matches()) {
			addErrorToList(lineNumber, line, "Stupac 12 (Br. bicikala) nema ispravnu vrijednost");
			valid = false;
		}
		if (!Pattern.compile(RegExUtils.isNumberRegex).matcher(data[12]).matches()) {
			addErrorToList(lineNumber, line, "Stupac 13 (Br. kreveta) nema ispravnu vrijednost");
			valid = false;
		}
		if (!Pattern.compile(RegExUtils.isNumberRegex).matcher(data[13]).matches()) {
			addErrorToList(lineNumber, line, "Stupac 14 (Br. automobila) nema ispravnu vrijednost");
			valid = false;
		}

		if (!Pattern.compile(RegExUtils.isDoubleWithNegRegex).matcher(data[8]).matches()) {
			addErrorToList(lineNumber, line, "Stupac 9 (Max snaga) nema ispravnu vrijednost");
			valid = false;
		} else if (Double.valueOf(data[8]) != -1 && !(Double.valueOf(data[8]) >= 0 && Double.valueOf(data[8]) <= 10)) {
			addErrorToList(lineNumber, line, "Stupac 9 (Max snaga) nije u dozvoljenom rasponu vrijednosti (-1/0/0-10)");
			valid = false;
		}

		if (!Pattern.compile(RegExUtils.isDoubleRegex).matcher(data[14]).matches()) {
			addErrorToList(lineNumber, line, "Stupac 15 (Nosivost) nema ispravnu vrijednost");
			valid = false;
		}

		if (!Pattern.compile(RegExUtils.isDoubleRegex).matcher(data[15]).matches()) {
			addErrorToList(lineNumber, line, "Stupac 16 (Povrsina) nema ispravnu vrijednost");
			valid = false;
		}

		if (!Pattern.compile(RegExUtils.isDoubleRegex).matcher(data[16]).matches()) {
			addErrorToList(lineNumber, line, "Stupac 17 (Zapremina) nema ispravnu vrijednost");
			valid = false;
		}

		return valid;
	}

	private void addErrorToList(int lineNumber, String line, String message) {
		errors.add(message + " u liniji " + lineNumber + "\n Linija: " + line);
	}
}
