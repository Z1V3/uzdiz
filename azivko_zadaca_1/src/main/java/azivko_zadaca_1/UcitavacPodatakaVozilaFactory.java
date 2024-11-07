package azivko_zadaca_1;

public class UcitavacPodatakaVozilaFactory implements UcitavacPodatakaFactory{
    @Override
    public UcitavacPodataka izradiUcitavacPodataka() {
        return new UcitavacPodatakaVozila();
    }
}
