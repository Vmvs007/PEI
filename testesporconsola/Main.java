public class Main {

    public static void main(String[] args) {

        XMLManagement meuGestor = new XMLManagement("D:/PEI/PEI/customer.xml","D:/PEI/PEI/customerXSD.xsd");
        meuGestor.read(true);
        meuGestor.validate(true);
    }
}
