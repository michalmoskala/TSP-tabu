import com.oracle.tools.packager.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

public class GeneticAlgorithm {
    int matrix[][];
    Comparator<ArrayList<Integer>> comparator = Comparator.comparingInt(this::permutation_evaluation_value);
    ArrayList<ArrayList<Integer>> pop=new ArrayList<>();

    public void printBestResult(){
        System.out.println(getFittest(pop));
    }


    private Integer getFittest(ArrayList<ArrayList<Integer>> population)
    {
        int value = Integer.MAX_VALUE;

        for(int i = 0; i<population.size(); ++i){
            int temp = permutation_value(population.get(i));
            if(temp<value) {
                value = temp;
            }

        }
        return value;
    }


    GeneticAlgorithm(int[][] matrix, int iterrations, int population_size, int children_pairs_size, float mutation_probability){

        long start = System.nanoTime();
        ArrayList<ArrayList<Integer>> population = solve(matrix, iterrations, population_size, children_pairs_size, mutation_probability);
        int value = Integer.MAX_VALUE;
        ArrayList<Integer> path = new ArrayList<>();

        long end = System.nanoTime();
        long time = (end - start) / 1000;

        System.out.println(value);

        //giveResults(path, value, (int)time/1000);
    }


    ArrayList<ArrayList<Integer>> solve(int[][] matrix, int iterations, int population_size, int children_pairs_size, float mutation_probability) {

        int number_of_cities = matrix.length;
        this.matrix = matrix;



        ArrayList<ArrayList<Integer>> population = create_starting_population(population_size);



        int parents_population_size = population_size / 2;
        ArrayList<ArrayList<Integer>> parents_population;

        ArrayList<ArrayList<Integer>> children_population = new ArrayList<>();

        for (int itteration = 0; itteration<iterations; ++itteration) {
            parents_population = find_parents_in_population(population,
                    population_size,
                    parents_population_size);

            for (int i = 0; i<children_pairs_size; ++i) {
                ArrayList<ArrayList<Integer>> children_pair = generate_children_pair(parents_population,
                        mutation_probability);
                children_population.add(children_pair.get(0));
                children_population.add(children_pair.get(1));

            }

            population = regenerate_population(population_size, children_population);

            System.out.println(getFittest(population));
            pop=population;
            children_population = new ArrayList<>();
        }
        return  population;

    }



    ArrayList<ArrayList<Integer>> create_starting_population(int population_size){
        ArrayList<Integer> nodes = new ArrayList<>();
        for(int i = 1; i<matrix.length; ++i)
            nodes.add(i);
        ArrayList<ArrayList<Integer>> population = new ArrayList<>();

        for(int i = 0; i<population_size; ++i){
            ArrayList<Integer> temp= new ArrayList<>(nodes);
            Collections.shuffle(temp);
            temp.add(0,0);
            population.add(temp);
        }
        return population;
    }

    ArrayList<ArrayList<Integer>> regenerate_population(
            int population_size,
            ArrayList<ArrayList<Integer>> population_children) {

        ArrayList<ArrayList<Integer>> new_population = new ArrayList<>();

        ArrayList<ArrayList<Integer>>  current_population = new ArrayList<>();
        ArrayList<ArrayList<Integer>>  current_population_children = new ArrayList<>(population_children);

        Collections.sort(current_population, comparator);
        Collections.sort(current_population_children, comparator);


        // Iteracja po całym docelowym rozmiarze populacji
        for (int i = 0; i<population_size; ++i){

            // Jeżeli pusty jest zbiór dzieci, ale zbiór rodziców ma jeszcze elementy
            // Dodajemy do nowej populacji alement zbioru rodziców
            if (current_population_children.size()==0 && current_population.size()!=0) {
                new_population.add(new ArrayList<>(current_population.get(current_population.size()-1)));
                current_population.remove(current_population.size()-1);
                continue;
            }

            // Jeżeli pusty jest zbiór rodziców, ale zbiór dzieci ma jescze elementy
            // Dodajemy do nowej populacji alement zbioru dzieci
            if (current_population_children.size()!=0 && current_population.size()==0) {
                new_population.add(new ArrayList<>(current_population_children.get(current_population_children.size()-1)));
                current_population_children.remove(current_population_children.size()-1);
                continue;
            }

            // Jeżeli obie populacje zawierają jeszcze elementy
            // Wybieramy ten, o korzystniejszej wartości funkcji przystosowania
            // Można sprawdzać po indeksach tablicy, bo wcześniej je sortowaliśmy
            if (permutation_evaluation_value(current_population_children.get(current_population_children.size() - 1))
                    < permutation_evaluation_value(current_population.get(current_population.size() - 1))) {
                new_population.add(new ArrayList<>(current_population.get(current_population.size()-1)));
                current_population.remove(current_population.size()-1);
            } else {
                new_population.add(new ArrayList<>(current_population_children.get(current_population_children.size() - 1)));
                current_population_children.remove(current_population_children.size()-1);
            }
        }

        // Zwracamy nową populację wygenerowaną z dzieci i rodziców
        return new_population;
    }

    // Funkcja wybierająca rodziców spośród populacji
// Przy użyciu kryterium celu i funkcji ewaluacji wartości osobników
    ArrayList<ArrayList<Integer>> find_parents_in_population(ArrayList<ArrayList<Integer>> population, int population_size, int parents_population_size){
        // Populacja która będzie przechowywać wybranych rodziców
        ArrayList<ArrayList<Integer>> selected_parents = new ArrayList<>();
        // Suma całkowita wszystkich wartości funkcji przystosowania populacji
        // Uwaga, spora liczba może tu wyjść
        long permutations_evaluation_sum = 0;
        // Tablica przechowująca wartości funkcji przystosowania wszystkich permutacji
        ArrayList<Integer> permutation_evaluation_values = new ArrayList<>();
        // Wyliczenie wartości funkcji przystosowania dla wszystkich osobników populacji
        // Zwiększenie sumy całkowitej funkcji przystosowania
        for(int i = 0; i<population_size; ++i){
            permutation_evaluation_values.add(permutation_evaluation_value(population.get(i)));
            // System.out.print("\nPermutation evaluation value: "+permutation_evaluation_value(matrix, population.get(i)));
            permutations_evaluation_sum = permutations_evaluation_sum + permutation_evaluation_values.get(i);
        }
        // Zmienna przechowująca losowy współczynnik określający funkcję celu
        long random_target_value;

        // Iteracja wybierająca elementy do populacji rodziców
        for (int i = 0; i<parents_population_size; ++i) {
            // Określenie losowe funkcji celu dla wygenerowanych wartości
            random_target_value = generate_randomized_target_value(permutations_evaluation_sum);
            // Zmienne przechowujące aktualną i poprzednią wartość
            // Wykorzystywaną przez pętlę sprawdzającą populację początkową
            long current_value = 0;
            long previous_value = 0;
            // Pętla sprawdzająca wszystkie osobniki populacji
            // Wybiera te, które spełniają funkcję celu i umieszcza jes w tablicy rodziców
            for (int j = 0; j<population_size; ++j) {
                // Zwiększenie aktualnej wartośći o wartość funkcji przystosowania dla aktualnie sprawdzanego osobnika
                current_value = current_value + permutation_evaluation_values.get(i);

                // Jeżeli poprzednia i aktualna wartość jest mniejsza od wartości funkcji celu
                // Aktualnie sprawdzany element populacji nadaje się na rodzica
                // W przeciwnym wypadku należy zwiększyć wartość ostatniej funkcji
                // O wartość funkcji ewaluacji ostatniego osobnika
                if ((previous_value <= random_target_value) && (random_target_value <= current_value)) {
                    selected_parents.add(new ArrayList<>(population.get(i)));
                    break;
                } else {
                    previous_value = previous_value + permutation_evaluation_values.get(i);
                }
            }
        }




        // Zwracana wartość jest tablicą zawierającą osobniki spełniające
        // Kryteria do bycia rodzicem
        return selected_parents;
    }

    // Funkcja wybiera dwoje osobników z populacji rodziców
// Następnie generuje z nich parę osobników kolejnego pokolenia
    ArrayList<ArrayList<Integer>> generate_children_pair(ArrayList<ArrayList<Integer>> parents_population,
                                                         float mutation_probability){
        // Tablica przechowująca dwie permutacje, odpowiadające
        // Parze dzieci (osobników kolejnej populacji)
        ArrayList<ArrayList<Integer>> children_pair = new ArrayList<>();
        // Losowy wybór osobników z populacji pierwotnej
        // Będą oni rodzicami pary osobników nowej populacji
        ArrayList<ArrayList<Integer>>  parents_pair = generate_parents_pair(parents_population);
        // System.out.print("\nOjciec: "+parents_pair.get(0)+" matka: "+parents_pair.get(1));
        // Początkowo dzieci są klonami rodziców
        children_pair.add(new ArrayList<>(parents_pair.get(0)));
        children_pair.add(new ArrayList<>(parents_pair.get(0)));
        // Następnie następuje krzyżowanie osobników
        children_pair = cross_children_pair_pmx(children_pair);
        // I próba mutacji otrzymanych dzieci
        children_pair.set(0, attempt_child_mutation(children_pair.get(0), mutation_probability));
        children_pair.set(1, attempt_child_mutation(children_pair.get(1), mutation_probability));
        //System.out.print("\nSyn:    "+children_pair.get(0)+" córka: "+children_pair.get(1));

        return children_pair;
    }

    // Funkcja wyliczająca wartość funkcji przystosowania dla wybranego elementu populacji
    Integer permutation_evaluation_value(ArrayList<Integer> permutation){
        return (Integer.MAX_VALUE - permutation_value(permutation));
    }

    // Funkcja obliczająca koszt ścieżki
    Integer permutation_value(ArrayList<Integer> permutation)  {

        int value = 0;
        int previous_node = 0;
        for (int i = 1; i<permutation.size(); ++i) {
            value = value + matrix[previous_node][permutation.get(i)];
            previous_node = permutation.get(i);
        }
        value = value + matrix[previous_node][0];
        return value;
    }

    // Funkcja generująca losową wartość celu
// Wykorzystywana przy wyborze rodziców do kolejnej populacji
    long generate_randomized_target_value(long permutation_evaluation_sum) {
        long target = (long)((permutation_evaluation_sum * (Math.random())));
        return target;
    }





    // Zwraca parę dzieci po krzyżowaniu
// Metodą Partially Matched Cross (PMX)
    ArrayList<ArrayList<Integer>> cross_children_pair_pmx(ArrayList<ArrayList<Integer>> children_pair) {

        // Nowa para dzieci
        ArrayList<ArrayList<Integer>> new_children_pair = new ArrayList<>(children_pair);
        // Obliczenie ilości elementów w permutacji
        int child_size = children_pair.get(0).size();
        // Wyznaczenie dwóch punktów krzyżowania
        int first_cross_point = 0;
        int second_cross_point = 0;
        // Pętla zapobiegająca wylosowaniu dwóch takich samych punktów krzyżowania
        while (first_cross_point>=second_cross_point) {
            first_cross_point = (int) ((Math.random()*10000)%(child_size-2)+1);
            second_cross_point = (int) ((Math.random()*10000)%(child_size-2)+1);
        }


        // Pierwsza pętla krzyżująca metodą PMX
        // Zamienia elementy dzieci w zakresie punktów krzyżowania
        for (int i = first_cross_point; i<second_cross_point; ++i) {
            Integer temp_child_element = new_children_pair.get(0).get(i);
            new_children_pair.get(0).set(i, new_children_pair.get(1).get(i));
            new_children_pair.get(1).set(i, temp_child_element);
        }

        // Druga pętla krzyżująca metodą PMX
        // Zamienia pozostałe elementy, aby uniknąć niespójnych permutacji
        for (int i = first_cross_point; i<second_cross_point; ++i) {
            for (int j = 1; j<first_cross_point; ++j) {
                if (new_children_pair.get(0).get(i) == new_children_pair.get(0).get(j)) {
                    new_children_pair.get(0).set(j, new_children_pair.get(1).get(i));
                }
                if (new_children_pair.get(1).get(i) == new_children_pair.get(1).get(j)) {
                    new_children_pair.get(1).set(j, new_children_pair.get(0).get(i));
                }
            }
        }

        return new_children_pair;
    }

    // Funkcja generuje losową parę rodziców z populacji
// Zapobiega wylosowaniu dwukrotnie tego samego rodzica
    ArrayList<ArrayList<Integer>> generate_parents_pair(ArrayList<ArrayList<Integer>> parents_population) {
        ArrayList<ArrayList<Integer>> parents_pair = new ArrayList<>();
        int father_index = 0;
        int mother_index = 0;
        while (father_index == mother_index) {
            father_index = (int)((Math.random()*10000)%parents_population.size());
            mother_index = (int)((Math.random()*10000)%parents_population.size());
        }
        parents_pair.add(parents_population.get(father_index));
        parents_pair.add(parents_population.get(mother_index));

        return parents_pair;
    }


    ArrayList<Integer> swap_elements_in_permutation(ArrayList<Integer> permutation, int first_element_index, int second_element_index) {

        ArrayList<Integer> new_population = new ArrayList<>(permutation);
        new_population.set(first_element_index, permutation.get(second_element_index));
        new_population.set(second_element_index, permutation.get(first_element_index));
        return new_population;
    }

    ArrayList<Integer> invert_elements_in_permutation(ArrayList<Integer> permutation, int first_element_index, int second_element_index) {

        for (int i=0;i<(second_element_index-first_element_index)/2;i++)
        //Collections.swap(permutation,first_element_index+i,second_element_index-i);
        permutation=swap_elements_in_permutation(permutation,first_element_index+i,second_element_index-i);

        return permutation;
    }

    // Metoda wykonuje mutację danego osobnika
// Zamieniając elementu, jeżeli spełniony zostanie
// Warunek określony przez prawdopodobieństwo
    ArrayList<Integer> attempt_child_mutation(ArrayList<Integer>  permutation, float mutation_probability){

        float random_float = (float)Math.random();

        if (random_float <= mutation_probability) {

            int first_element_index = 0;
            int second_element_index = 0;

            while (first_element_index == second_element_index) {
                first_element_index = (int) ((Math.random()*10000)%(permutation.size()-2)+1);
                second_element_index = (int) ((Math.random()*10000)%(permutation.size()-2)+1);
            }

            return invert_elements_in_permutation(permutation, first_element_index, second_element_index);
        } else {
            return permutation;
        }
    }
}
