public class TabuSearch {

    private final Matrix matrix;


    int [][] tabuList;
    int[] currSolution;
    int numberOfIterations;
    int problemSize;

    private int[] bestSolution;
    private int bestCost;

    public void tabuMove(int city1, int city2, int[][]tabuList){ //tabus the swap operation
        tabuList[city1][city2]+= 150;
        tabuList[city2][city1]+= 150;

    }

    public void decrementTabu(int [][]tabuList){
        for(int i = 0; i<tabuList.length; i++){
            for(int j = 0; j<tabuList.length; j++){
                tabuList[i][j]-=tabuList[i][j]<=0?0:1;
            }
        }
    }

    public TabuSearch(Matrix matrix) {
        this.matrix = matrix;
        problemSize = matrix.getEdgeCount();
        numberOfIterations = problemSize * 100000;

        tabuList = new int[problemSize][problemSize];
        setupCurrentSolution();
        setupBestSolution();


    }

    private void setupBestSolution() {
        bestSolution = new int[problemSize + 1];
        System.arraycopy(currSolution, 0, bestSolution, 0, bestSolution.length);
        bestCost = matrix.calculateDistance(bestSolution);
    }

    private void setupCurrentSolution() {
        currSolution = new int[problemSize + 1];
//        for (int i = 1; i <= problemSize; i++)
//            currSolution[i] = i;
        currSolution[0]=0;
        currSolution[problemSize] = 0;

        Boolean contains;
        int t=0;
        int a;
        int shortpath;
        for(int i=1;i<problemSize;i++){

            shortpath=10000;
            for(int j=1;j<problemSize;j++) {
                a = matrix.getWeight(currSolution[i - 1], j);
                if (a < shortpath && a>0)
                {
                    contains=false;
                    for(int k=1;k<=i;k++)
                        if (currSolution[k]==j)
                            contains=true;


                    if (!contains) {
                        shortpath = a;
                        t = j;
                    }
                }
                currSolution[i]=t;

            }
        }


    }


    private void printSolution(int[] solution) {
        for (int i = 0; i < solution.length; i++) {
            System.out.print(solution[i] + " ");
        }
        System.out.println();
    }

    public void invoke() {

        System.out.println(matrix.calculateDistance(currSolution));
        for (int i = 0; i < numberOfIterations; i++) {
            int city1 = 0;
            int city2 = 0;

            for (int j = 1; j < currSolution.length - 1; j++) {
                for (int k = 2; k < currSolution.length - 1; k++) {
                    if (j != k) {
                        swap(j, k);
                        int currCost = matrix.calculateDistance(currSolution);
                        if ((currCost < bestCost) && tabuList[j][k] == 0) {
                            city1 = j;
                            city2 = k;
                            System.arraycopy(currSolution, 0, bestSolution, 0, bestSolution.length);
                            bestCost = currCost;

                        }
                        else
                            swap(j,k);
                    }
                }
            }


            if (city1 != 0) {
                decrementTabu(tabuList);
                tabuMove(city1, city2,tabuList);
            }
        }

        System.out.println("Search done! \nBest Solution cost found = " + bestCost + "\nBest Solution :");
        printSolution(bestSolution);
    }

    private void swap(int i, int k) {
        int temp = currSolution[i];
        currSolution[i] = currSolution[k];
        currSolution[k] = temp;
    }
}
