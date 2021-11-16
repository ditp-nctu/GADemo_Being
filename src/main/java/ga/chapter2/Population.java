package ga.chapter2;

import static art.cctcc.c1642.being.Constants.*;
import java.util.Arrays;
import java.util.Comparator;

/**
 * A population is an abstraction of a collection of individuals.The population
 * class is generally used to perform group-level operations on its individuals,
 * such as finding the strongest individuals, collecting stats on the population
 * as a whole, and selecting individuals to mutate or crossover.
 *
 * @author bkanber
 * @param <I>
 *
 */
abstract public class Population<I extends Individual> {

  protected I population[];
  private double populationFitness = -1;

  /**
   * Get individuals from the population
   *
   * @return individuals Is in population
   */
  public I[] getIndividuals() {
    return this.population;
  }

  /**
   * Find an individual in the population by its fitness
   *
   * This method lets you select an individual in order of its fitness. This can
   * be used to find the single strongest individual (eg, if you're testing for
   * a solution), but it can also be used to find weak individuals (if you're
   * looking to cull the population) or some of the strongest individuals (if
   * you're using "elitism").
   *
   * @param offset The offset of the individual you want, sorted by fitness. 0
   * is the strongest, population.length - 1 is the weakest.
   * @return individual I at offset
   */
  synchronized public I getFittest(int offset) {
    // Order population by fitness
    Arrays.sort(this.population, Comparator.comparing(Individual::getFitness, Comparator.reverseOrder()));
    // Return the fittest individual
    return this.population[offset];
  }

  /**
   * Set population's group fitness
   *
   * @param fitness The population's total fitness
   */
  public void setPopulationFitness(double fitness) {
    this.populationFitness = fitness;
  }

  /**
   * Get population's group fitness
   *
   * @return populationFitness The population's total fitness
   */
  public double getPopulationFitness() {
    return this.populationFitness;
  }

  /**
   * Get population's size
   *
   * @return size The population's size
   */
  public int size() {
    return this.population.length;
  }

  /**
   * Set individual at offset
   *
   * @param individual
   * @param offset
   * @return individual
   */
  public I setIndividual(int offset, I individual) {
    return population[offset] = individual;
  }

  /**
   * Get individual at offset
   *
   * @param offset
   * @return individual
   */
  public I getIndividual(int offset) {
    return population[offset];
  }

  /**
   * Shuffles the population in-place
   *
   */
  public void shuffle() {
    for (int i = population.length - 1; i > 0; i--) {
      int index = r.nextInt(i + 1);
      I a = population[index];
      population[index] = population[i];
      population[i] = a;
    }
  }
}
