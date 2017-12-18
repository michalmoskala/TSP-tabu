public class TabuOperation implements Runnable {

    private Query query;

    private int[][] tabuList;
    private int[] path;
    private int[] bestPath;

    private int Size;
    private int lowestCost;
    private int tabuSize;
    private int multiplication;

    public TabuOperation(int multiplication, int tabuSize, int[][] tab) {
        setupCurrentSolution(tab, tabuSize);
        this.tabuSize = tabuSize;
        this.multiplication = multiplication;
    }

    public void getData() {

        System.out.println("Przerwano ze względu na interupcję kryterium stopu");

        Boolean next = false;

        for (int aSolution : bestPath) {
            if (next)
                System.out.print("->" + aSolution);
            else {
                next = true;
                System.out.print(aSolution);
            }
        }
        System.out.println("\n" + "Dlugosc drogi:" + lowestCost);


        Menu.t2.interrupt();
    }

    private void tabuStep(int city1, int city2) {
        for (int i = 0; i < tabuList.length; i++) {
            for (int j = 0; j < tabuList.length; j++) {
                if (tabuList[i][j] > 0)
                    tabuList[i][j]--;
            }
        }

        tabuList[city1][city2] = tabuList[city1][city2] + tabuSize;
        tabuList[city2][city1] = tabuList[city2][city1] + tabuSize;

    }

    private void setupCurrentSolution(int[][] tab, int tabuSize) {

        this.query = new Query(tab);
        this.tabuSize = tabuSize;
        Size = query.matrix.length;

        tabuList = new int[Size][Size];

        path = new int[Size + 1];
        path[0] = 0;
        path[Size] = 0;

        Boolean contains;
        int t = 0;
        int a;
        int shortpath;
        for (int i = 1; i < Size; i++) {

            shortpath = 10000;
            for (int j = 1; j < Size; j++) {
                a = query.getWeight(path[i - 1], j);
                if (a < shortpath && a > 0) {
                    contains = false;
                    for (int k = 1; k <= i; k++)
                        if (path[k] == j)
                            contains = true;


                    if (!contains) {
                        shortpath = a;
                        t = j;
                    }
                }
                path[i] = t;

            }
        }

        bestPath = new int[Size + 1];

        for (int i = 0; i < bestPath.length; i++) {
            bestPath[i] = path[i];
        }

        lowestCost = query.getObjectiveValue(bestPath);


    }

    public void run() {
        Long start = System.currentTimeMillis();

        int temp;

        System.out.println(query.getObjectiveValue(path));
        for (int i = 0; i < Size * multiplication; i++) {
            int from = 0;
            int to = 0;

            for (int j = 1; j < path.length - 1; j++) {
                for (int k = 2; k < path.length - 1; k++) {
                    if (j != k) {

                        temp = path[j];
                        path[j] = path[k];
                        path[k] = temp;
                        int currCost = query.getObjectiveValue(path);


                        if ((currCost < lowestCost) && tabuList[j][k] == 0) {
                            from = j;
                            to = k;

                            for (int z = 0; z < bestPath.length; z++) {
                                bestPath[z] = path[z];
                            }

                            lowestCost = currCost;


                        } else {

                            temp = path[j];
                            path[j] = path[k];
                            path[k] = temp;
                        }
                    }
                }
            }


            if (from != 0) {
                tabuStep(from, to);
            }
        }

        Long end = System.currentTimeMillis();

        Boolean next = false;

        for (int aSolution : bestPath) {
            if (next)
                System.out.print("->" + aSolution);
            else {
                next = true;
                System.out.print(aSolution);
            }
        }
        System.out.println("\n" + "Dlugosc drogi:" + lowestCost);

        System.out.println("\nCzas: " + (end - start) + "ms");
    }


}
