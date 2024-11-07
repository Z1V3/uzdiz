package azivko_zadaca_1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class UcitavacPodatakaKompozicija implements UcitavacPodataka {
	private List<String> errors = new ArrayList<>();
	ArrayList<VoziloKompozicija> listaVozilaKompozicija = new ArrayList<VoziloKompozicija>();
	Integer oznakaKompozicijeProsliRed = 0;

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
			if (!isValidCompositionData(data, lineNumber, line))
				continue;
			if (lineNumber == 1)
				oznakaKompozicijeProsliRed = Integer.valueOf(data[0]);
			processValidLine(data, lineNumber, line);
		}
	}

	private boolean shouldSkipLine(String line, int lineNumber) {
		return line.trim().isEmpty() || line.startsWith("#") || line.contains("Oznaka;") || line.startsWith(";");
	}

	private boolean isValidCompositionData(String[] data, int lineNumber, String line) {
		boolean valid = true;

		if (data.length != 3) {
			addErrorToList(lineNumber, line, "Not enough fields");
			return false;
		}

		if (!Pattern.compile(RegExUtils.isNumberRegex).matcher(data[0]).matches()) {
			addErrorToList(lineNumber, line, "Column 1 (Oznaka) is not a valid integer");
			valid = false;
		}

		if (!ZeljeznickaMreza.getInstance().vratiListuVozila().containsKey(data[1])
				|| ZeljeznickaMreza.getInstance().vratiVozilo(data[1]).getStatus() == StatusVozila.K) {
			addErrorToList(lineNumber, line, "There is no vehicle with mark " + data[1]
					+ " of column 1 in the list of vehicles that is also active");
		}
		
		if (!"P".equals(data[2]) && !"V".equals(data[2])) {
			addErrorToList(lineNumber, line, "Column 3 (Uloga) is not a valid value, valid values are P/V");
		}

		return valid;
	}

	private void processValidLine(String[] data, int lineNumber, String line) {
		if(ZeljeznickaMreza.getInstance().vratiVozilo(data[1]).getNamjenaVozila() == NamjenaVozila.PSVPVK) {
			
		}
		
	    VoziloKompozicija voziloKompozicija = new VoziloKompozicija(
	        determineRole(data[2]),
	        ZeljeznickaMreza.getInstance().vratiVozilo(data[1])
	    );

	    Integer oznakaKompozicije = Integer.valueOf(data[0]);
	    if (!oznakaKompozicije.equals(oznakaKompozicijeProsliRed)) {
	        if (!listaVozilaKompozicija.isEmpty() && isValidComposition(listaVozilaKompozicija)) {
	            ZeljeznickaKompozicija zeljeznickaKompozicija = new ZeljeznickaKompozicija(
	                oznakaKompozicijeProsliRed.toString(), 
	                new ArrayList<>(listaVozilaKompozicija)
	            );
	            ZeljeznickaMreza.getInstance().dodajKompoziciju(oznakaKompozicijeProsliRed.toString(), zeljeznickaKompozicija);
	        }

	        listaVozilaKompozicija.clear();
	        oznakaKompozicijeProsliRed = oznakaKompozicije;
	    }

	    listaVozilaKompozicija.add(voziloKompozicija);
	}

	private boolean isValidComposition(ArrayList<VoziloKompozicija> composition) {
	    if (composition.isEmpty() || composition.get(0).getUloga() != Uloga.P) {
	        return false;
	    }

	    for (int i = 1; i < composition.size(); i++) {
	        VoziloKompozicija current = composition.get(i);
	        VoziloKompozicija previous = composition.get(i - 1);

	        if (current.getUloga() == Uloga.P && previous.getUloga() != Uloga.P) {
	            return false;
	        }
	    }

	    return true;
	}
	
	private Uloga determineRole(String role) {
		switch(role) {
		case "P":
			return Uloga.P;
		case "V":
			return Uloga.V;
		default:
			return null;
		}
	}

	private void addErrorToList(int lineNumber, String line, String message) {
		errors.add("Error in line " + lineNumber + ": " + line + " - " + message + ".");
	}
}
