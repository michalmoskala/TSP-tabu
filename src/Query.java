public class Query {

    int[][] matrix;


    Query(int[][] matrix) {
        this.matrix = matrix;
    }


    int getWeight(int a, int b) {
        return matrix[a][b];
    }

    int getObjectiveValue(int path[]) {
        int cost = 0;

        for (int i = 0; i < path.length - 1; i++) {
            cost += matrix[path[i]][path[i + 1]];
        }

        return cost;
    }



}