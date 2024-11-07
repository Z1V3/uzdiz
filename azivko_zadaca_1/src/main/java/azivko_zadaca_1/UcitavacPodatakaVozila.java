package azivko_zadaca_1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class UcitavacPodatakaVozila implements UcitavacPodataka {
    private List<String> pogreske = new ArrayList<>();

    @Override
    public List<String> ucitaj(String imeDatoteke) {
        try (BufferedReader reader = new BufferedReader(new FileReader(imeDatoteke))) {
            obradiSadrzajDatoteke(reader);
        } catch (IOException e) {
            pogreske.add("Neuspješno čitanje datoteke: " + imeDatoteke);
        }
        return pogreske;
    }

    private void obradiSadrzajDatoteke(BufferedReader reader) throws IOException {
        String redak;
        int brojRetka = 0;
        while ((redak = reader.readLine()) != null) {
            brojRetka++;
            if (redak.contains("Oznaka;"))
                brojRetka = 0;
            if (trebaPreskocitiRedak(redak, brojRetka))
                continue;

            String[] podaci = redak.split(";");
            if (!jeIspravanPodatakVozila(podaci, brojRetka, redak))
                continue;
            obradiPodatkeVozila(podaci, brojRetka, redak);
        }
    }

    private boolean trebaPreskocitiRedak(String redak, int brojRetka) {
        return redak.trim().isEmpty() || redak.startsWith("#") || redak.contains("Oznaka;") || redak.startsWith(";");
    }

    private void obradiPodatkeVozila(String[] podaci, int brojRetka, String redak) {
        PrijevoznoSredstvo vozilo = kreirajVozilo(podaci, brojRetka, redak);
        if (vozilo != null) {
            ZeljeznickaMreza.getInstance().dodajVozilo(podaci[0], vozilo);
        }
    }

    private void formatirajPoljaPodataka(String[] podaci) {
        podaci[14] = podaci[14].replace(",", ".");
        podaci[15] = podaci[15].replace(",", ".");
        podaci[8] = podaci[8].replace(",", ".");
    }

    private PrijevoznoSredstvo kreirajVozilo(String[] podaci, int brojRetka, String redak) {
        try {
            return new PrijevoznoSredstvo(podaci[0], odrediNamjenuVozila(podaci[4], brojRetka, redak),
                    odrediVrstuVozila(podaci[5], brojRetka, redak), odrediVrstuPogona(podaci[6], brojRetka, redak),
                    Double.valueOf(podaci[8]), Double.valueOf(podaci[7]), Integer.valueOf(podaci[3]), podaci[2],
                    odrediProstor(podaci[9], podaci[10], podaci[12], podaci[11], podaci[13], brojRetka, redak),
                    Double.valueOf(podaci[14]), Integer.valueOf(podaci[16]), odrediStatus(podaci[17], brojRetka, redak));
        } catch (Exception e) {
            pogreske.add("Pogreška pri kreiranju vozila u retku " + brojRetka + ": " + redak);
            return null;
        }
    }

    private NamjenaVozila odrediNamjenuVozila(String namjena, int brojRetka, String redak) {
        switch (namjena) {
            case "PSVPVK":
                return NamjenaVozila.PSVPVK;
            case "PSVP":
                return NamjenaVozila.PSVP;
            case "PSBP":
                return NamjenaVozila.PSBP;
            default:
                dodajPogreskuURedak(brojRetka, redak, "Neispravna namjena vozila");
                return null;
        }
    }

    private VrstaPrijevoza odrediVrstuVozila(String vrsta, int brojRetka, String redak) {
        switch (vrsta) {
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
                dodajPogreskuURedak(brojRetka, redak, "Neispravna vrsta prijevoza");
                return null;
        }
    }

    private VrstaPogona odrediVrstuPogona(String pogon, int brojRetka, String redak) {
        switch (pogon) {
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
                dodajPogreskuURedak(brojRetka, redak, "Neispravna vrsta pogona");
                return null;
        }
    }

    private ArrayList<Integer> odrediProstor(String sjedece, String stajace, String krevete, String bicikle, String automobili,
                                              int brojRetka, String redak) {
        ArrayList<Integer> prostor = new ArrayList<>();
        prostor.add(Integer.valueOf(sjedece));
        prostor.add(Integer.valueOf(stajace));
        prostor.add(Integer.valueOf(krevete));
        prostor.add(Integer.valueOf(bicikle));
        prostor.add(Integer.valueOf(automobili));

        return prostor;
    }

    private StatusVozila odrediStatus(String status, int brojRetka, String redak) {
        switch (status) {
            case "I":
                return StatusVozila.I;
            case "K":
                return StatusVozila.K;
            default:
                dodajPogreskuURedak(brojRetka, redak, "Neispravan status vozila");
                return null;
        }
    }
    
    public boolean jeIspravanPodatakVozila(String[] podaci, int brojRetka, String redak) {
        boolean ispravno = true;

        if (!jeIspravnaDuljinaPodataka(podaci, brojRetka, redak)) ispravno = false;
        formatirajPoljaPodataka(podaci);
        if (!jeIspravnaNamjenaVozila(podaci, brojRetka, redak)) ispravno = false;
        if (!jeIspravnaVrstaPrijevoza(podaci, brojRetka, redak)) ispravno = false;
        if (!jeIspravanStatus(podaci, brojRetka, redak)) ispravno = false;
        if (!jeIspravnaVrstaPogona(podaci, brojRetka, redak)) ispravno = false;
        if (!jeIspravnaGodina(podaci, brojRetka, redak)) ispravno = false;
        if (!jeIspravnaBrzina(podaci, brojRetka, redak)) ispravno = false;
        if (!jesuLiSjedistaIVrijednostiIspravni(podaci, brojRetka, redak)) ispravno = false;
        if (!jeIspravnaSnaga(podaci, brojRetka, redak)) ispravno = false;
        if (!jesuLiTezinaPovrsinaZapreminaIspravni(podaci, brojRetka, redak)) ispravno = false;

        return ispravno;
    }
    
    private boolean jeIspravnaDuljinaPodataka(String[] podaci, int brojRetka, String redak) {
        if (podaci.length != 18) {
            dodajPogreskuURedak(brojRetka, redak, "Linija mora imati točno 18 polja/stupaca, ni više ni manje");
            return false;
        }
        return true;
    }

    private boolean jeIspravnaNamjenaVozila(String[] podaci, int brojRetka, String redak) {
        String namjena = podaci[4];
        if (!"PSVPVK".equals(namjena) && !"PSVP".equals(namjena) && !"PSBP".equals(namjena)) {
            dodajPogreskuURedak(brojRetka, redak, "Stupac 5 (Namjena vozila) nema ispravnu vrijednost, moguće vrijednosti su PSVPVK/PSVP/PSBP");
            return false;
        }
        return true;
    }

    private boolean jeIspravnaVrstaPrijevoza(String[] podaci, int brojRetka, String redak) {
        String vrstaPrijevoza = podaci[5];
        if (!"N".equals(vrstaPrijevoza) && !"P".equals(vrstaPrijevoza) && !"TA".equals(vrstaPrijevoza)
                && !"TK".equals(vrstaPrijevoza) && !"TPS".equals(vrstaPrijevoza) && !"TRS".equals(vrstaPrijevoza)
                && !"TTS".equals(vrstaPrijevoza)) {
            dodajPogreskuURedak(brojRetka, redak, "Stupac 6 (Vrsta prijevoza) nema ispravnu vrijednost, moguće vrijednosti su N/P/TA/TK/TPS/TRS/TTS");
            return false;
        }
        return true;
    }

    private boolean jeIspravanStatus(String[] podaci, int brojRetka, String redak) {
        String status = podaci[17];
        if (!"I".equals(status) && !"K".equals(status)) {
            dodajPogreskuURedak(brojRetka, redak, "Stupac 18 (Status) nema ispravnu vrijednost, moguće vrijednosti su I/K");
            return false;
        }
        return true;
    }

    private boolean jeIspravnaVrstaPogona(String[] podaci, int brojRetka, String redak) {
        String vrstaPogona = podaci[6];
        if (!"N".equals(vrstaPogona) && !"D".equals(vrstaPogona) && !"B".equals(vrstaPogona)
                && !"E".equals(vrstaPogona) && !"S".equals(vrstaPogona)) {
            dodajPogreskuURedak(brojRetka, redak, "Stupac 7 (Vrsta pogona) nema ispravnu vrijednost");
            return false;
        }
        return true;
    }

    private boolean jeIspravnaGodina(String[] podaci, int brojRetka, String redak) {
        if (!Pattern.compile(RegExUtils.isNumberRegex).matcher(podaci[3]).matches()) {
            dodajPogreskuURedak(brojRetka, redak, "Stupac 4 (Godina) nema ispravnu vrijednost");
            return false;
        }
        int godina = Integer.valueOf(podaci[3]);
        if (godina < 1900 || godina > 2024) {
            dodajPogreskuURedak(brojRetka, redak, "Stupac 4 (Godina) nije u dozvoljenom rasponu vrijednosti (1900-2024)");
            return false;
        }
        return true;
    }

    private boolean jeIspravnaBrzina(String[] podaci, int brojRetka, String redak) {
        if (!Pattern.compile(RegExUtils.isNumberRegex).matcher(podaci[7]).matches()) {
            dodajPogreskuURedak(brojRetka, redak, "Stupac 8 (Brzina) nema ispravnu vrijednost");
            return false;
        }
        int brzina = Integer.valueOf(podaci[7]);
        if (brzina < 1 || brzina > 200) {
            dodajPogreskuURedak(brojRetka, redak, "Stupac 8 (Brzina) nije u dozvoljenom rasponu vrijednosti (1-200)");
            return false;
        }
        return true;
    }

    private boolean jesuLiSjedistaIVrijednostiIspravni(String[] podaci, int brojRetka, String redak) {
        String[] stupciZaProvjeru = {podaci[9], podaci[10], podaci[11], podaci[12], podaci[13]};
        String[] naziviStupaca = {"Br. sjedecih mjesta", "Br. stajacih mjesta", "Br. bicikala", "Br. kreveta", "Br. automobila"};

        for (int i = 0; i < stupciZaProvjeru.length; i++) {
            if (!Pattern.compile(RegExUtils.isNumberRegex).matcher(stupciZaProvjeru[i]).matches()) {
                dodajPogreskuURedak(brojRetka, redak, "Stupac " + (10 + i) + " (" + naziviStupaca[i] + ") nema ispravnu vrijednost");
                return false;
            }
        }
        return true;
    }

    private boolean jeIspravnaSnaga(String[] podaci, int brojRetka, String redak) {
        if (!Pattern.compile(RegExUtils.isDoubleWithNegRegex).matcher(podaci[8]).matches()) {
            dodajPogreskuURedak(brojRetka, redak, "Stupac 9 (Max snaga) nema ispravnu vrijednost");
            return false;
        }
        double snaga = Double.valueOf(podaci[8]);
        if (snaga != -1 && !(snaga >= 0 && snaga <= 10)) {
            dodajPogreskuURedak(brojRetka, redak, "Stupac 9 (Max snaga) nije u dozvoljenom rasponu vrijednosti (-1/0/0-10)");
            return false;
        }
        return true;
    }

    private boolean jesuLiTezinaPovrsinaZapreminaIspravni(String[] podaci, int brojRetka, String redak) {
        String[] stupci = {podaci[14], podaci[15], podaci[16]};
        String[] naziviStupaca = {"Nosivost", "Povrsina", "Zapremina"};

        for (int i = 0; i < stupci.length; i++) {
            if (!Pattern.compile(RegExUtils.isDoubleRegex).matcher(stupci[i]).matches()) {
                dodajPogreskuURedak(brojRetka, redak, "Stupac " + (15 + i) + " (" + naziviStupaca[i] + ") nema ispravnu vrijednost");
                return false;
            }
        }
        return true;
    }

    private void dodajPogreskuURedak(int brojRetka, String redak, String poruka) {
        pogreske.add(poruka + " u retku " + brojRetka + "\n Redak: " + redak);
    }
}
