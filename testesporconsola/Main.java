public class Main {

    public static void main(String[] args) {

        XMLManagement meuGestor = new XMLManagement("D:/PEI/PEI/currencyRate.xml","D:/PEI/PEI/currencyRateXSD.xsd");
        meuGestor.read(true);
        System.out.print(meuGestor.validate(true));
    }
}
