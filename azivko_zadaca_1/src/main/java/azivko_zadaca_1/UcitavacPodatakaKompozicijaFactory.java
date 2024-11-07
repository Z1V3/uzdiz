package azivko_zadaca_1;

public class UcitavacPodatakaKompozicijaFactory implements UcitavacPodatakaFactory{
    @Override
    public UcitavacPodataka izradiUcitavacPodataka() {
        return new UcitavacPodatakaKompozicija();
    }
}
