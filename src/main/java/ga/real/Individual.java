package ga.real;

import java.util.Arrays;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.Setter;

/**
 * An "Individual" represents a single candidate solution. The core piece of
 * information about an individual is its "chromosome", which is an encoding of
 * a possible solution to the problem at hand. A chromosome can be a string, an
 * array, a list, etc -- in this class, the chromosome is an integer array.
 *
 * An individual position in the chromosome is called a gene, and these are the
 * atomic pieces of the solution that can be manipulated or mutated. When the
 * chromosome is a string, as in this case, each character or set of characters
 * can be a gene.
 *
 * An individual also has a "fitness" score; this is a number that represents
 * how good a solution to the problem this individual is. The meaning of the
 * fitness score will vary based on the problem at hand.
 *
 * @author bkanber
 *
 */
public class Individual {

  @Getter
  private double[] chromosome;

  @Getter @Setter
  private double fitness = -1;

  /**
   * Initializes individual with specific chromosome
   *
   * @param chromosome The chromosome to give individual
   */
  public Individual(double[] chromosome) {
    // Create individual chromosome
    this.chromosome = chromosome;
  }

  /**
   * Initializes random individual.
   *
   * This constructor assumes that the chromosome is made entirely of 0s and 1s,
   * which may not always be the case, so make sure to modify as necessary. This
   * constructor also assumes that a "random" chromosome means simply picking
   * random zeroes and ones, which also may not be the case (for instance, in a
   * traveling salesman problem, this would be an invalid solution).
   *
   * @param chromosomeLength The length of the individuals chromosome
   */
  public Individual(int chromosomeLength) {

    this(new double[chromosomeLength]);
  }

  /**
   * Gets individual's chromosome length
   *
   * @return The individual's chromosome length
   */
  public int getChromosomeLength() {
    return this.chromosome.length;
  }

  /**
   * Set gene at offset
   *
   * @param gene
   * @param offset
   */
  public void setGene(int offset, double gene) {
    this.chromosome[offset] = gene;
  }

  /**
   * Get gene at offset
   *
   * @param offset
   * @return gene
   */
  public double getGene(int offset) {
    return this.chromosome[offset];
  }

  /**
   * Display the chromosome as a string.
   *
   * @return string representation of the chromosome
   */
  @Override
  public String toString() {
    String output = Arrays.stream(this.chromosome)
            .mapToObj(gene -> String.format("%.5f", gene))
            .collect(Collectors.joining(", "));
    return output;
  }
}
