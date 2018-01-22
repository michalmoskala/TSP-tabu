
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Objects;

class GeneticAlgo {
    private float matrix[][];
    private Comparator<ArrayList<Integer>> comparator = Comparator.comparingDouble(this::fitness);
    private ArrayList<ArrayList<Integer>> pop=new ArrayList<>();

    void printBestResult(){
        System.out.println(getFittest(pop));
    }


    private double fitness(ArrayList<Integer> permutation){
        return (1000000000.0 - pathLength(permutation));
    }

    private float pathLength(ArrayList<Integer> permutation)  {

        float value = 0;
        int prev = 0;
        for (int i = 1; i<permutation.size(); ++i) {
            value = value + matrix[prev][permutation.get(i)];
            prev = permutation.get(i);
        }
        value = value + matrix[prev][0];
        return value;
    }

    private double getRandTargetValue(double permutationEvalSum) {
        return ((permutationEvalSum * (Math.random())));
    }

    private float getFittest(ArrayList<ArrayList<Integer>> population)
    {
        float value = (float)1000000000.0;

        for (ArrayList<Integer> aPopulation : population) {
            float temp = pathLength(aPopulation);
            if (temp < value) {
                value = temp;
            }

        }
        return value;
    }


    GeneticAlgo(float[][] matrix, int popSize, int childrenAmount, float mutationProbability, float crossRate){

        mainLoop(matrix, popSize, childrenAmount, mutationProbability,crossRate);

    }


    private void mainLoop(float[][] matrix, int popSize, int childrenAmount, float mutationProbability, float crossRate) {

        this.matrix = matrix;


        ArrayList<ArrayList<Integer>> population = getStartingPop(popSize);



        int parentsPopSize = popSize / 2;
        ArrayList<ArrayList<Integer>> parentsPop;

        ArrayList<ArrayList<Integer>> childrenPop = new ArrayList<>();

        do {
            parentsPop = getParents(population,
                    popSize,
                    parentsPopSize);

            for (int i = 0; i < childrenAmount; ++i) {
                ArrayList<ArrayList<Integer>> twoChildren = getChildren(parentsPop,
                        mutationProbability, crossRate);
                childrenPop.add(twoChildren.get(0));
                childrenPop.add(twoChildren.get(1));

            }

            population = getNewPop(popSize, childrenPop);

            System.out.println(getFittest(population));
            pop = population;
            childrenPop = new ArrayList<>();
        } while (true);

    }

    private ArrayList<ArrayList<Integer>> getStartingPop(int popSize){
        ArrayList<Integer> nodes = new ArrayList<>();
        for(int i = 1; i<matrix.length; ++i)
            nodes.add(i);
        ArrayList<ArrayList<Integer>> population = new ArrayList<>();

        for(int i = 0; i<popSize; ++i){
            ArrayList<Integer> temp= new ArrayList<>(nodes);
            Collections.shuffle(temp);
            temp.add(0,0);
            population.add(temp);
        }
        return population;
    }

    private ArrayList<ArrayList<Integer>> getNewPop(
            int popSize,
            ArrayList<ArrayList<Integer>> childrenPop) {

        ArrayList<ArrayList<Integer>> newPop = new ArrayList<>();

        ArrayList<ArrayList<Integer>>  thisPop = new ArrayList<>();
        ArrayList<ArrayList<Integer>>  thisPopChildren = new ArrayList<>(childrenPop);

        thisPop.sort(comparator);
        thisPopChildren.sort(comparator);


        for (int i = 0; i<popSize; ++i){


            if (thisPopChildren.size()==0 && thisPop.size()!=0) {
                newPop.add(new ArrayList<>(thisPop.get(thisPop.size()-1)));
                thisPop.remove(thisPop.size()-1);
                continue;
            }


            if (thisPopChildren.size()!=0 && thisPop.size()==0) {
                newPop.add(new ArrayList<>(thisPopChildren.get(thisPopChildren.size()-1)));
                thisPopChildren.remove(thisPopChildren.size()-1);
                continue;
            }


            if (fitness(thisPopChildren.get(thisPopChildren.size() - 1))
                    < fitness(thisPop.get(thisPop.size() - 1))) {
                newPop.add(new ArrayList<>(thisPop.get(thisPop.size()-1)));
                thisPop.remove(thisPop.size()-1);
            } else {
                newPop.add(new ArrayList<>(thisPopChildren.get(thisPopChildren.size() - 1)));
                thisPopChildren.remove(thisPopChildren.size()-1);
            }
        }


        return newPop;
    }

    private ArrayList<ArrayList<Integer>> getParents(ArrayList<ArrayList<Integer>> population, int popSize, int parentsPopSize){
        ArrayList<ArrayList<Integer>> parents = new ArrayList<>();
        double permutationEvalSum = 0;
        ArrayList<Double> permutationEval = new ArrayList<>();
        for(int i = 0; i<popSize; ++i){
            permutationEval.add(fitness(population.get(i)));
            permutationEvalSum = permutationEvalSum + permutationEval.get(i);
        }
        double thisTarget;

        for (int i = 0; i<parentsPopSize; ++i) {
            thisTarget = getRandTargetValue(permutationEvalSum);

            double thisval = 0;
            double prevval = 0;

            for (int j = 0; j<popSize; ++j) {
                thisval = thisval + permutationEval.get(i);


                if ((prevval <= thisTarget) && (thisTarget <= thisval)) {
                    parents.add(new ArrayList<>(population.get(i)));
                    break;
                } else {
                    prevval = prevval + permutationEval.get(i);
                }
            }
        }


        return parents;
    }

    private ArrayList<ArrayList<Integer>> getChildren(ArrayList<ArrayList<Integer>> parentsPop,
                                                      float mutationProbability, float crossRate){
        ArrayList<ArrayList<Integer>> twoChildren = new ArrayList<>();
        ArrayList<ArrayList<Integer>>  twoParents = getTwoParents(parentsPop);
        twoChildren.add(new ArrayList<>(twoParents.get(0)));
        twoChildren.add(new ArrayList<>(twoParents.get(0)));
        twoChildren = pmxCrossing(twoChildren,crossRate);
        twoChildren.set(0, mutation(twoChildren.get(0), mutationProbability));
        twoChildren.set(1, mutation(twoChildren.get(1), mutationProbability));

        return twoChildren;
    }







    private ArrayList<ArrayList<Integer>> pmxCrossing(ArrayList<ArrayList<Integer>> twoChildren, float crossRate) {

        ArrayList<ArrayList<Integer>> newChildrenPair = new ArrayList<>(twoChildren);
        int childrenAmount = twoChildren.get(0).size();
        int firstCrossPoint = 0;
        int secondCrossPoint = 0;
        while (firstCrossPoint>=secondCrossPoint&&(secondCrossPoint-firstCrossPoint)<=crossRate*childrenAmount) {
            firstCrossPoint = (int) ((Math.random()*10000)%(childrenAmount-2)+1);
            secondCrossPoint = (int) ((Math.random()*10000)%(childrenAmount-2)+1);
        }


        for (int i = firstCrossPoint; i<secondCrossPoint; ++i) {
            Integer tempChildElement = newChildrenPair.get(0).get(i);
            newChildrenPair.get(0).set(i, newChildrenPair.get(1).get(i));
            newChildrenPair.get(1).set(i, tempChildElement);
        }

        for (int i = firstCrossPoint; i<secondCrossPoint; ++i) {
            for (int j = 1; j<firstCrossPoint; ++j) {
                if (Objects.equals(newChildrenPair.get(0).get(i), newChildrenPair.get(0).get(j))) {
                    newChildrenPair.get(0).set(j, newChildrenPair.get(1).get(i));
                }
                if (Objects.equals(newChildrenPair.get(1).get(i), newChildrenPair.get(1).get(j))) {
                    newChildrenPair.get(1).set(j, newChildrenPair.get(0).get(i));
                }
            }
        }

        return newChildrenPair;
    }

    private ArrayList<ArrayList<Integer>> getTwoParents(ArrayList<ArrayList<Integer>> parentsPop) {
        ArrayList<ArrayList<Integer>> twoParents = new ArrayList<>();
        int daddy = 0;
        int mom = 0;
        while (daddy == mom) {
            daddy = (int)((Math.random()*10000)%parentsPop.size());
            mom = (int)((Math.random()*10000)%parentsPop.size());
        }
        twoParents.add(parentsPop.get(daddy));
        twoParents.add(parentsPop.get(mom));

        return twoParents;
    }


    private ArrayList<Integer> swap(ArrayList<Integer> permutation, int first, int second) {

        ArrayList<Integer> newPop = new ArrayList<>(permutation);
        newPop.set(first, permutation.get(second));
        newPop.set(second, permutation.get(first));
        return newPop;
    }

    private ArrayList<Integer> invert(ArrayList<Integer> permutation, int first, int second) {

        for (int i=0;i<(second-first)/2;i++)
        permutation= swap(permutation,first+i,second-i);

        return permutation;
    }

    private ArrayList<Integer> mutation(ArrayList<Integer> solution, float mutationProbability){

        float randomFloat = (float)Math.random();

        if (randomFloat <= mutationProbability) {

            int first = 0;
            int second = 0;

            while (first == second) {
                first = (int) ((Math.random()*10000)%(solution.size()-2)+1);
                second = (int) ((Math.random()*10000)%(solution.size()-2)+1);
            }

            return invert(solution, first, second);
        } else {
            return solution;
        }
    }
}
