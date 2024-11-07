package azivko_zadaca_1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class UcitavacPodatakaStanica implements UcitavacPodataka {
    private List<String> pogreske = new ArrayList<>();
    private int ukupnaDuljinaPruge = 0;
    private String oznakaProslePruge = "";

    @Override
    public List<String> ucitaj(String imeDatoteke) {
        try (BufferedReader reader = new BufferedReader(new FileReader(imeDatoteke))) {
            obradiSadrzajDatoteke(reader);
            obradiPosljednjuDuljinuPruge();
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
            if (redak.contains("Stanica;")) brojRetka = 0;
            if (trebaPreskocitiRedak(redak)) continue;

            String[] podaci = redak.split(";");
            if (!jeValidniPodaciStanica(podaci, brojRetka, redak)) continue;
            obradiValidniRedak(podaci);
        }
    }

    private boolean trebaPreskocitiRedak(String redak) {
        return redak.trim().isEmpty() || redak.startsWith("#") || redak.contains("Stanica;") || redak.startsWith(";");
    }

    private boolean jeValidniPodaciStanica(String[] podaci, int brojRetka, String redak) {
        if (podaci.length != 14) {
            dodajPogreskuURedak(brojRetka, redak, "Redak mora imati točno 14 polja");
            return false;
        }

        return validirajPolja(podaci, brojRetka, redak);
    }

    private boolean validirajPolja(String[] podaci, int brojRetka, String redak) {
        boolean validno = true;

        podaci[10] = podaci[10].replace(",", ".");
        podaci[11] = podaci[11].replace(",", ".");

        validno &= validirajOznakuPruge(podaci[1], brojRetka, redak);
        validno &= validirajVrstuStanice(podaci[2], brojRetka, redak);
        validno &= validirajStatusStanice(podaci[3], brojRetka, redak);
        validno &= validirajPutniciRoba(podaci[4], podaci[5], brojRetka, redak);
        validno &= validirajKategorijuPruge(podaci[6], brojRetka, redak);
        validno &= validirajVrstuPruge(podaci[8], brojRetka, redak);
        validno &= validirajStatusPruge(podaci[12], brojRetka, redak);
        validno &= validirajBrojPerona(podaci[7], brojRetka, redak);
        validno &= validirajBrojKolosijeka(podaci[9], brojRetka, redak);
        validno &= validirajDuljinu(podaci[13], brojRetka, redak);
        validno &= validirajDO(podaci[10], 10, 50, "Stupac 11", brojRetka, redak);
        validno &= validirajDO(podaci[11], 2, 10, "Stupac 12", brojRetka, redak);

        return validno;
    }

    private boolean validirajOznakuPruge(String vrijednost, int brojRetka, String redak) {
        if (!Pattern.compile(RegExUtils.LRM_REGEX).matcher(vrijednost).matches()) {
            dodajPogreskuURedak(brojRetka, redak, "Stupac 2 (Oznaka pruge) nije ispravan");
            return false;
        }
        return true;
    }

    private boolean validirajVrstuStanice(String vrijednost, int brojRetka, String redak) {
        if (!"staj.".contains(vrijednost) && !"kol.".contains(vrijednost)) {
            dodajPogreskuURedak(brojRetka, redak, "Stupac 3 (Vrsta stanice) nije ispravan");
            return false;
        }
        return true;
    }

    private boolean validirajStatusStanice(String vrijednost, int brojRetka, String redak) {
        if (!"O".equals(vrijednost) && !"Z".equals(vrijednost)) {
            dodajPogreskuURedak(brojRetka, redak, "Stupac 4 (Status stanice) nije ispravan");
            return false;
        }
        return true;
    }

    private boolean validirajPutniciRoba(String vrijednost1, String vrijednost2, int brojRetka, String redak) {
        if (!"DA".equals(vrijednost1) && !"NE".equals(vrijednost1)) {
            dodajPogreskuURedak(brojRetka, redak, "Stupac 5 (Putnici ul/iz) nije ispravan");
            return false;
        }
        if (!"DA".equals(vrijednost2) && !"NE".equals(vrijednost2)) {
            dodajPogreskuURedak(brojRetka, redak, "Stupac 6 (Roba ul/iz) nije ispravan");
            return false;
        }
        return true;
    }

    private boolean validirajKategorijuPruge(String vrijednost, int brojRetka, String redak) {
        if (!"M".equals(vrijednost) && !"L".equals(vrijednost) && !"R".equals(vrijednost)) {
            dodajPogreskuURedak(brojRetka, redak, "Stupac 7 (Kategorija pruge) nije ispravan");
            return false;
        }
        return true;
    }

    private boolean validirajVrstuPruge(String vrijednost, int brojRetka, String redak) {
        if (!"K".equals(vrijednost) && !"E".equals(vrijednost)) {
            dodajPogreskuURedak(brojRetka, redak, "Stupac 9 (Vrsta pruge) nije ispravan");
            return false;
        }
        return true;
    }

    private boolean validirajStatusPruge(String vrijednost, int brojRetka, String redak) {
        if (!"I".equals(vrijednost) && !"K".equals(vrijednost) && !"Z".equals(vrijednost)) {
            dodajPogreskuURedak(brojRetka, redak, "Stupac 13 (Status pruge) nije ispravan");
            return false;
        }
        return true;
    }

    private boolean validirajBrojPerona(String vrijednost, int brojRetka, String redak) {
        if (!Pattern.compile(RegExUtils.isNumberRegex).matcher(vrijednost).matches()) {
            dodajPogreskuURedak(brojRetka, redak, "Stupac 8 (Broj perona) nije ispravan");
            return false;
        } else if (Integer.valueOf(vrijednost) < 1 || Integer.valueOf(vrijednost) > 99) {
            dodajPogreskuURedak(brojRetka, redak, "Stupac 8 (Broj perona) nije u dozvoljenom rasponu (1-99)");
            return false;
        }
        return true;
    }

    private boolean validirajBrojKolosijeka(String vrijednost, int brojRetka, String redak) {
        if (!Pattern.compile(RegExUtils.isNumberRegex).matcher(vrijednost).matches()) {
            dodajPogreskuURedak(brojRetka, redak, "Stupac 10 (Broj kolosijeka) nije ispravan");
            return false;
        } else if (Integer.valueOf(vrijednost) > 2 || Integer.valueOf(vrijednost) < 1) {
            dodajPogreskuURedak(brojRetka, redak, "Stupac 10 (Broj kolosijeka) nije u dozvoljenom rasponu (1-2)");
            return false;
        }
        return true;
    }

    private boolean validirajDuljinu(String vrijednost, int brojRetka, String redak) {
        if (!Pattern.compile(RegExUtils.isNumberRegex).matcher(vrijednost).matches()) {
            dodajPogreskuURedak(brojRetka, redak, "Stupac 14 (Duljina) nije ispravan");
            return false;
        } else if (Integer.valueOf(vrijednost) < 0 || Integer.valueOf(vrijednost) > 999) {
            dodajPogreskuURedak(brojRetka, redak, "Stupac 14 (Duljina) nije u dozvoljenom rasponu (1-999)");
            return false;
        }
        return true;
    }

    private boolean validirajDO(String vrijednost, double min, double max, String nazivStupca, int brojRetka, String redak) {
        if (!Pattern.compile(RegExUtils.isDoubleRegex).matcher(vrijednost).matches()) {
            dodajPogreskuURedak(brojRetka, redak, nazivStupca + " nije ispravan broj");
            return false;
        } else if (Double.valueOf(vrijednost) < min || Double.valueOf(vrijednost) > max) {
            dodajPogreskuURedak(brojRetka, redak, nazivStupca + " nije u dozvoljenom rasponu (" + min + "-" + max + ")");
            return false;
        }
        return true;
    }

    private void obradiValidniRedak(String[] podaci) {
        azurirajDuljinuPruge(podaci);
        obradiPodatkeStanice(podaci);
    }

    private void azurirajDuljinuPruge(String[] podaci) {
        if (podaci[1].contains(oznakaProslePruge)) {
            ukupnaDuljinaPruge += Integer.valueOf(podaci[13]);
        } else {
            if (!oznakaProslePruge.isEmpty()) {
                ZeljeznickaMreza.getInstance().vratiPrugu(oznakaProslePruge).setDuzina(ukupnaDuljinaPruge);
            }
            ukupnaDuljinaPruge = Integer.valueOf(podaci[13]);
        }
        oznakaProslePruge = podaci[1];
    }

    private void obradiPodatkeStanice(String[] podaci) {
        ZeljeznickaPruga pruga = kreirajPrugu(podaci);
        ZeljeznickaMreza.getInstance().dodajPrugu(podaci[1], pruga);

        ZeljeznickaStanica stanica = kreirajStanicu(podaci);
        ZeljeznickaPruga vracenaPruga = ZeljeznickaMreza.getInstance().vratiPrugu(podaci[1]);
        vracenaPruga.dodajStanicu(stanica);
    }

    private ZeljeznickaPruga kreirajPrugu(String[] podaci) {
        return new ZeljeznickaPruga(podaci[1], odrediKategorijuPruge(podaci[6]), odrediNacinPrijevoza(podaci[8]),
                Integer.valueOf(podaci[9]), Double.parseDouble(podaci[10]), Double.parseDouble(podaci[11]),
                odrediStatusPruge(podaci[12]));
    }

    private ZeljeznickaStanica kreirajStanicu(String[] podaci) {
        return new ZeljeznickaStanica(podaci[0], odrediVrstuStanice(podaci[2]),
                odrediAktivnostiStanice(podaci[4], podaci[5]), Integer.valueOf(podaci[7]), "O".equals(podaci[3]),
                ukupnaDuljinaPruge);
    }

    private KategorijaPruge odrediKategorijuPruge(String kategorija) {
        if ("M".equals(kategorija)) return KategorijaPruge.MEDUANRODNA;
        if ("R".equals(kategorija)) return KategorijaPruge.REGIONALNA;
        if ("L".equals(kategorija)) return KategorijaPruge.LOKALNA;
        return null;
    }

    private NacinPrijevoza odrediNacinPrijevoza(String nacin) {
        if ("K".equals(nacin)) return NacinPrijevoza.K;
        if ("E".equals(nacin)) return NacinPrijevoza.E;
        return null;
    }

    private StatusPruge odrediStatusPruge(String status) {
        if ("I".equals(status)) return StatusPruge.ISPRAVNA;
        if ("K".equals(status)) return StatusPruge.KVAR;
        if ("Z".equals(status)) return StatusPruge.ZATVORENA;
        return null;
    }

    private VrstaStanice odrediVrstuStanice(String vrsta) {
        if ("kol.".equals(vrsta)) return VrstaStanice.KOL;
        if ("staj.".equals(vrsta)) return VrstaStanice.STAJ;
        return null;
    }

    private AktivnostiStanice odrediAktivnostiStanice(String uip, String uir) {
        if ("DA".equals(uip) && "DA".equals(uir)) return AktivnostiStanice.OBA;
        if ("DA".equals(uip)) return AktivnostiStanice.UIP;
        if ("DA".equals(uir)) return AktivnostiStanice.UIR;
        return null;
    }

    private void obradiPosljednjuDuljinuPruge() {
        if (!oznakaProslePruge.isEmpty()) {
            ZeljeznickaMreza.getInstance().vratiPrugu(oznakaProslePruge).setDuzina(ukupnaDuljinaPruge);
        }
    }

    private void dodajPogreskuURedak(int brojRetka, String redak, String poruka) {
        pogreske.add("Pogreška u retku " + brojRetka + ": " + redak + " - " + poruka + ".");
    }
}
