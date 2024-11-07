package azivko_zadaca_1;

import java.util.ArrayList;

public class ZeljeznickaKompozicija {
    private String oznaka;
    private ArrayList<VoziloKompozicija> listaVozila;

    private ZeljeznickaKompozicija(String oznaka, ArrayList<VoziloKompozicija> listaVozila) {
        this.oznaka = oznaka;
        this.listaVozila = listaVozila;
    }

    public String getOznaka() {
        return oznaka;
    }

    public ArrayList<VoziloKompozicija> getListaVozila() {
        return listaVozila;
    }

    public static class KompozicijaBuilder {
        private String oznaka;
        private ArrayList<VoziloKompozicija> listaVozila = new ArrayList<>();
        private boolean hasLeadLocomotive = false;

        public KompozicijaBuilder(String oznaka) {
            this.oznaka = oznaka;
        }

        public KompozicijaBuilder addVozilo(VoziloKompozicija voziloKompozicija) {
            if (listaVozila.isEmpty()) {
                if (voziloKompozicija.getUloga() != Uloga.P ||
                    voziloKompozicija.getVozilo().getNamjenaVozila() != NamjenaVozila.PSVPVK) {
                    throw new IllegalArgumentException("Prva lokomotiva mora biti sa pogonom tipa PSVPVK.");
                }
                hasLeadLocomotive = true;
            }

            if (voziloKompozicija.getUloga() == Uloga.P &&
                voziloKompozicija.getVozilo().getNamjenaVozila() == NamjenaVozila.PSVPVK) {
                if (!hasLeadLocomotive) {
                    throw new IllegalArgumentException("Dodatne lokomotive moraju pratiti prvu PSVPVK ili druge lokomotive.");
                }
            }

            listaVozila.add(voziloKompozicija);
            return this;
        }

        public ZeljeznickaKompozicija build() {
            if (listaVozila.isEmpty()) {
                throw new IllegalStateException("Ne moze se kreirati prazna kompozicija.");
            }
            return new ZeljeznickaKompozicija(oznaka, listaVozila);
        }
    }
}
