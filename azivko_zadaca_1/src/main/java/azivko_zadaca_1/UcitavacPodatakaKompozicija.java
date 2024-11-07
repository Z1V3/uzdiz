package azivko_zadaca_1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class UcitavacPodatakaKompozicija implements UcitavacPodataka {
    private List<String> greske = new ArrayList<>();
    ArrayList<VoziloKompozicija> listaVozilaKompozicija = new ArrayList<>();
    Integer oznakaKompozicijeProsliRed = 0;

    @Override
    public List<String> ucitaj(String imeDatoteke) {
        try (BufferedReader citac = new BufferedReader(new FileReader(imeDatoteke))) {
            procesirajSadrzajDatoteke(citac);
        } catch (IOException e) {
            greske.add("Neuspjelo citanje datoteke: " + imeDatoteke);
        }
        return greske;
    }

    private void procesirajSadrzajDatoteke(BufferedReader citac) throws IOException {
        String redak;
        int brojRetka = 0;
        while ((redak = citac.readLine()) != null) {
            brojRetka++;
            if (redak.contains("Oznaka;")) brojRetka = 0;
            if (preskociRedak(redak, brojRetka)) continue;

            String[] podaci = redak.split(";");
            if (!provjeriIspravnostPodataka(podaci, brojRetka, redak)) continue;

            if (brojRetka == 1) oznakaKompozicijeProsliRed = Integer.valueOf(podaci[0]);
            obradiIspravanRedak(podaci, brojRetka, redak);
        }
        
        dodajKompozicijuAkoJeIspravna();
    }

    private boolean preskociRedak(String redak, int brojRetka) {
        return redak.trim().isEmpty() || redak.startsWith("#") || redak.contains("Oznaka;") || redak.startsWith(";");
    }

    private boolean provjeriIspravnostPodataka(String[] podaci, int brojRetka, String redak) {
        boolean ispravan = true;
        if (podaci.length != 3) {
            dodajGresku(brojRetka, redak, "Mora postojati točno 3 polja u retku.");
            return false;
        }

        if (!Pattern.compile(RegExUtils.isNumberRegex).matcher(podaci[0]).matches()) {
            dodajGresku(brojRetka, redak, "Kolona 1 (Oznaka) nije valjani cijeli broj.");
            ispravan = false;
        }

        if (!ispravnaVozila(podaci[1])) {
            dodajGresku(brojRetka, redak, "Nema vozila s oznakom " + podaci[1] + " koje je aktivno.");
        }

        if (!ispravnaUloga(podaci[2])) {
            dodajGresku(brojRetka, redak, "Kolona 3 (Uloga) nije valjana vrijednost, valjani su P/V.");
        }

        return ispravan;
    }

    private void obradiIspravanRedak(String[] podaci, int brojRetka, String redak) {
        PrijevoznoSredstvo vozilo = ZeljeznickaMreza.getInstance().vratiVozilo(podaci[1]);
        Uloga ulogaIzDatoteke = odrediUlogu(podaci[2]);

        if (ulogaNeOdgovaraNamjeni(vozilo, ulogaIzDatoteke, brojRetka, redak)) return;

        VoziloKompozicija voziloKompozicija = new VoziloKompozicija(ulogaIzDatoteke, vozilo);
        Integer oznakaKompozicije = Integer.valueOf(podaci[0]);
        
        dodajVoziloUKompoziciju(oznakaKompozicije, voziloKompozicija);
    }

    private boolean ulogaNeOdgovaraNamjeni(PrijevoznoSredstvo vozilo, Uloga uloga, int brojRetka, String redak) {
        Uloga ocekivanaUloga = mapirajNamjenuUlogu(vozilo.getNamjenaVozila());
        if (uloga != ocekivanaUloga) {
            dodajGresku(brojRetka, redak, "Uloga ne odgovara namjeni vozila.");
            return true;
        }
        return false;
    }

    private void dodajVoziloUKompoziciju(Integer oznakaKompozicije, VoziloKompozicija voziloKompozicija) {
        if (!oznakaKompozicije.equals(oznakaKompozicijeProsliRed)) {
            dodajKompozicijuAkoJeIspravna();
            listaVozilaKompozicija.clear();
            oznakaKompozicijeProsliRed = oznakaKompozicije;
        }
        listaVozilaKompozicija.add(voziloKompozicija);
    }

    private void dodajKompozicijuAkoJeIspravna() {
        if (!listaVozilaKompozicija.isEmpty() && ispravnaKompozicija(listaVozilaKompozicija)) {
            ZeljeznickaKompozicija kompozicija = new ZeljeznickaKompozicija(
                oznakaKompozicijeProsliRed.toString(), 
                new ArrayList<>(listaVozilaKompozicija)
            );
            ZeljeznickaMreza.getInstance().dodajKompoziciju(oznakaKompozicijeProsliRed.toString(), kompozicija);
        }
    }

    private boolean ispravnaKompozicija(ArrayList<VoziloKompozicija> kompozicija) {
        if (kompozicija.isEmpty()) return false;

        VoziloKompozicija prvoVozilo = kompozicija.get(0);
        if (prvoVozilo.getUloga() != Uloga.P || prvoVozilo.getVozilo().getNamjenaVozila() != NamjenaVozila.PSVPVK) {
            return false;
        }

        return provjeriVozila(kompozicija);
    }

    private boolean provjeriVozila(ArrayList<VoziloKompozicija> kompozicija) {
        for (int i = 1; i < kompozicija.size(); i++) {
            VoziloKompozicija trenutno = kompozicija.get(i);
            VoziloKompozicija prethodno = kompozicija.get(i - 1);

            if (trenutno.getUloga() == Uloga.P && nijePravilnaNamjena(trenutno, prethodno)) {
                return false;
            }

            if (trenutno.getUloga() == Uloga.V && i == 0) {
                return false;
            }
        }
        return true;
    }

    private boolean nijePravilnaNamjena(VoziloKompozicija trenutno, VoziloKompozicija prethodno) {
        NamjenaVozila trenutnaNamjena = trenutno.getVozilo().getNamjenaVozila();
        return (trenutnaNamjena == NamjenaVozila.PSVP || trenutnaNamjena == NamjenaVozila.PSVPVK)
                && prethodno.getUloga() != Uloga.P;
    }

    private Uloga mapirajNamjenuUlogu(NamjenaVozila namjena) {
        switch (namjena) {
            case PSVPVK:
            case PSVP:
                return Uloga.P;
            case PSBP:
                return Uloga.V;
            default:
                return null;
        }
    }

    private Uloga odrediUlogu(String uloga) {
        switch(uloga) {
            case "P":
                return Uloga.P;
            case "V":
                return Uloga.V;
            default:
                return null;
        }
    }

    private boolean ispravnaVozila(String oznaka) {
        return ZeljeznickaMreza.getInstance().vratiListuVozila().containsKey(oznaka) &&
               ZeljeznickaMreza.getInstance().vratiVozilo(oznaka).getStatus() != StatusVozila.K;
    }

    private boolean ispravnaUloga(String uloga) {
        return "P".equals(uloga) || "V".equals(uloga);
    }

    private void dodajGresku(int brojRetka, String redak, String poruka) {
        greske.add("Greska u retku " + brojRetka + ": " + redak + " - " + poruka + ".");
    }
}
