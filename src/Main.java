
public class Main {

    public static void main(String[] args) {

        int[][] k=FromXMLfile.getAllUserNames("c:\\pea/d493.xml");

        new GeneticAlgorithm(k,10000,1000,500,(float)0.2);




       // Menu menu=new Menu();
        //menu.main();
    }

}
