public class Menu {
    static TabuSearch tabuSearch;
    static Thread t2;

    public static void getData(){
        tabuSearch.getData();

    }

    public static void main() {
        final int multiplication=10000;
        final int tabuSize=100;
        final int timeLimit=100;
        final int randomSize=20;

        int [][]k=new int [][]{{0,3,5,48,48,8,8,5,5,3,3,0,3,5,8,8,5},
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

        int m[][]=random(randomSize);

        tabuSearch = new TabuSearch(multiplication,tabuSize,m);

        Timer timer=new Timer(timeLimit);
        Thread t=new Thread(timer);
        t.start();

        t2=new Thread(tabuSearch);

        t2.start();




    }
    private static int[][] random(int size) {
        int[][]matrix=new int[size][size];
        java.util.Random random = new java.util.Random();
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                if (row != col) {
                    int value = random.nextInt(100) + 1;
                    matrix[row][col] = value;
                    matrix[col][row] = value;
                }
            }
        }
        return matrix;
    }

}
