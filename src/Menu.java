import java.util.Scanner;


public class Menu {
    static TabuOperation tabuOperation;
    static Thread t2;

    public static void getData(){
        tabuOperation.getData();

    }

    public static void main() {


        Scanner sc=new Scanner(System.in);

        System.out.println("Podaj wielokrotnosc iteracji (np 10000)");
        int num=sc.nextInt();
        final int multiplication=num;

        System.out.println("Podaj kadencje(np 100)");
        num=sc.nextInt();
        final int tabuSize=num;

        System.out.println("Podaj czasowe kryterium stopu (np 10s)");
        num=sc.nextInt();
        final int timeLimit=num;


        System.out.println("Podaj tryb dzialania\n1-macierz hardkodowana\n2-macierz losowa\n3-wczytanie z pliku");
        num=sc.nextInt();
        final int mode=num;
        int [][]k;
        if (mode==1)
        k=new int [][]{{0,3,5,48,48,8,8,5,5,3,3,0,3,5,8,8,5},
                {3,0,3,48,48,8,8,5,5,0,0,3,0,3,8,8,5},
                {5,3,0,72,72,48,48,24,24,3,3,5,3,0,48,48,24},
                {48,48,74,0,0,6,6,12,12,48,48,48,48,74,6,6,12},
                {48,48,74,0,0,6,6,12,12,48,48,48,48,74,6,6,12},
                {8,8,50,6,6,0,0,8,8,8,8,8,8,50,0,0,8},
                {8,8,50,6,6,0,0,8,8,8,8,8,8,50,0,0,8},
                {5,5,26,12,12,8,8,0,0,5,5,5,5,26,8,8,0},
                {5,5,26,12,12,8,8,0,0,5,5,5,5,26,8,8,0},
                {3,0,3,48,48,8,8,5,5,0,0,3,0,3,8,8,5},
                {3,0,3,48,48,8,8,5,5,0,0,3,0,3,8,8,5},
                {0,3,5,48,48,8,8,5,5,3,3,0,3,5,8,8,5},
                {3,0,3,48,48,8,8,5,5,0,0,3,0,3,8,8,5},
                {5,3,0,72,72,48,48,24,24,3,3,5,3,0,48,48,24},
                {8,8,50,6,6,0,0,8,8,8,8,8,8,50,0,0,8},
                {8,8,50,6,6,0,0,8,8,8,8,8,8,50,0,0,8},
                {5,5,26,12,12,8,8,0,0,5,5,5,5,26,8,8,0}};

        else if (mode==2) {
            System.out.println("Podaj rozmiar");
            num=sc.nextInt();
            final int randomSize=num;
            k = random(randomSize);

        }
        else
        k=FromXMLfile.getAllUserNames("c:\\pea/pcb1173.xml");

//        k= FromFile.main("data.txt");

        tabuOperation = new TabuOperation(multiplication,tabuSize,k);

        Timer timer=new Timer(timeLimit);
        Thread t=new Thread(timer);
        t.start();

        t2=new Thread(tabuOperation);

        t2.start();

    }
    private static int[][] random(int size) {
        int[][]matrix=new int[size][size];

        java.util.Random random = new java.util.Random();

        for (int row = 0; row < size; row++) {

            for (int col = 0; col < size; col++) {

                if (row != col) {

                    int value = random.nextInt(80) + 1;
                    matrix[row][col] = value;
                    matrix[col][row] = value;
                }
                else
                {
                    matrix[row][col] = 0;
                    matrix[col][row] = 0;
                }
            }
        }
        return matrix;
    }

}
