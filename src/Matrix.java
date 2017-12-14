public class Matrix {

    private int[][] matrix;

    private int edgeCount;

    public Matrix(int size) {
        edgeCount = size;
        matrix = new int[size][size];
        generateMatrix(size);
    }


    Matrix(int[][] matrix) {
        edgeCount = matrix.length;
        this.matrix = matrix;
    }

    int getEdgeCount() {
        return edgeCount;
    }



    int getWeight(int from, int to) {
        return matrix[from][to];
    }

    int calculateDistance(int solution[]) {
        int cost = 0;
        for (int i = 0; i < solution.length - 1; i++) {
            cost += matrix[solution[i]][solution[i + 1]];
        }
        return cost;
    }

    private void generateMatrix(int size) {
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
    }

}