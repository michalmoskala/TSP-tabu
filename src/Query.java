public class Query {

    float[][] matrix;


    Query(float[][] matrix) {
        this.matrix = matrix;
    }


    float getWeight(int a, int b) {
        return matrix[a][b];
    }

    float getObjectiveValue(int path[]) {
        float cost = 0;

        for (int i = 0; i < path.length - 1; i++) {
            cost += matrix[path[i]][path[i + 1]];
        }

        return cost;
    }



}