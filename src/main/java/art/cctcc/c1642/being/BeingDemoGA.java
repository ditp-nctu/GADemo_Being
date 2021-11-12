/*
 * Copyright 2021 Jonathan Chang, Chun-yien <ccy@musicapoetica.org>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package art.cctcc.c1642.being;

import ga.chapter2.GeneticAlgorithm;
import java.util.Arrays;
import java.util.stream.Stream;

/**
 *
 * @author Jonathan Chang, Chun-yien <ccy@musicapoetica.org>
 */
public class BeingDemoGA extends GeneticAlgorithm<BeingPopulation, Being> {

  private float width;
  private float height;

  public BeingDemoGA(int populationSize, double mutationRate, double crossoverRate, int elitismCount,
          float width, float height) {

    super(populationSize, mutationRate, crossoverRate, elitismCount);
    this.width = width;
    this.height = height;
  }

  @Override
  public BeingPopulation initPopulation() {
    // Initialize population
    var population = new BeingPopulation(this.populationSize, width, height);
    return population;
  }

  @Override
  public boolean isTerminationConditionMet(BeingPopulation population) {

    return false;
  }
  public static boolean debug;

  @Override
  public double calcFitness(Being being) {

    var deltaScoreBase = 0.0;
    var currentSize = being.getSize() - being.getDelta()[0];
    for (int i = 1; i < being.getRing() - 1; i++) {
      deltaScoreBase += (i % 2 == 0)
              ? being.getDelta()[i - 1]
              : Math.abs(being.getDelta()[i - 1] - currentSize * (Math.sqrt(2.0) - 1));
      currentSize -= being.getDelta()[i];
    }
    var deltaScore = being.getRing() <= 2 ? 0.0 : 1.0 / (deltaScoreBase / (being.getRing() - 1) + 1.0);
//    var sizeScore = individual.getSize() - Being.min_size > 0 ? 1.0 : 0.0;
//    var sizeScore = Math.abs(being.getSize() - (Being.min_size + Being.max_size) / 2.0) / Being.max_size;
    var ringScore = 1.0 * being.getRing() / Being.max_ring;
//    var fitness = Math.pow(deltaScore + sizeScore + ringScore, 1.0 / 3);
//    var fitness = (deltaScore * 1.0 + ringScore * 2.5 + sizeScore * 1.0) / 5.0;
    var fitness = (deltaScore * 1.0 + ringScore * 2.5) / 3.5;
    being.setFitness(fitness);
    if (debug)
//      System.out.printf("ring=%d, deltaScore=%.2f, ringScore=%.2f, sizeScore=%.2f\n",
//              being.getRing(),
//              deltaScore, ringScore, sizeScore);
      System.out.printf("ring=%d, deltaScore=%.2f, ringScore=%.2f\n",
              being.getRing(),
              deltaScore, ringScore);
    return fitness;
  }

  @Override
  public BeingPopulation crossoverPopulation(BeingPopulation population) {

    var crossoverCounter = 0;
    // Create new population
    var newPopulation = new BeingPopulation(this.populationSize);

    // Loop over current population by fitness
    for (int populationIndex = 0; populationIndex < population.size(); populationIndex++) {
      var parent1 = population.getFittest(populationIndex);
//      System.out.printf("(%.2f)", parent1.getFitness());
      // Apply crossover to this individual?
      if (this.crossoverRate < Being.r.nextDouble() || populationIndex < this.elitismCount)
        // Add individual to new population without applying crossover
        newPopulation.setIndividual(populationIndex, parent1);
      else {
        // Initialize offspring
        var offspring = new Being(parent1);

        // Find second parent
        var parent2 = selectParent(population);

        // Loop over genome
        var crossover = Being.r.nextInt(Being.chromosomeLength / 20) + 1;
        var crossoverCites = Stream.generate(() -> Being.r.nextInt(parent1.getChromosomeLength()))
                .distinct()
                .limit(crossover)
                .toList();
        var usingFirst = true;
        for (var geneIndex = 0; geneIndex < parent1.getChromosomeLength(); geneIndex++) {
          // 3 crossover cites at most with 10% swapping rate
          if (crossoverCites.contains(geneIndex)) {
            usingFirst = !usingFirst;
          }
          offspring.setGene(geneIndex, (usingFirst ? parent1 : parent2).getGene(geneIndex));
        }
        offspring.decodeGenes();
        calcFitness(offspring);
        // Add offspring to new population
        newPopulation.setIndividual(populationIndex, offspring);
        crossoverCounter++;
        System.out.printf("X Ring=%d, delta=%s\n", offspring.getRing(), Arrays.toString(offspring.getDelta()));
      }
    }
    System.out.print(" crossoverCounter=" + crossoverCounter);
    return newPopulation;
  }

  @Override
  public BeingPopulation mutatePopulation(BeingPopulation population) {

    var mutationCounter = 0;
    // Initialize new population
    var newPopulation = new BeingPopulation(this.populationSize);

    // Loop over current population by fitness
    for (int populationIndex = 0; populationIndex < population.size(); populationIndex++) {
      var being = population.getFittest(populationIndex);
      // Skip mutation if this is an elite individual
      if (this.mutationRate < Being.r.nextDouble() || populationIndex < this.elitismCount)
        newPopulation.setIndividual(populationIndex, being);
      else {
        var newBeing = new Being(being);
        var mutation = Being.r.nextInt(Being.chromosomeLength / 20) + 1;
        var mutationCites = Stream.generate(() -> Being.r.nextInt(being.getChromosomeLength()))
                .distinct()
                .limit(mutation)
                .toList();
        // Loop over individual's genes
        for (int geneIndex = 0; geneIndex < being.getChromosomeLength(); geneIndex++) {
          // Does this gene need mutation?
          if (mutationCites.contains(geneIndex)) {
            // Get new gene
            int newGene = (being.getGene(geneIndex) == 1) ? 0 : 1;
            // Mutate gene
            newBeing.setGene(geneIndex, newGene);
          } else {
            newBeing.setGene(geneIndex, being.getGene(geneIndex));
          }
        }
        newBeing.decodeGenes();
        newBeing.setColor(Being.r.nextInt(256));
        newPopulation.setIndividual(populationIndex, newBeing);
        mutationCounter++;
        System.out.printf("\n! Ring=%d, delta=%s", newBeing.getRing(), Arrays.toString(newBeing.getDelta()));
      }

    }
    if (mutationCounter > 0) System.out.println();
    System.out.print(" mutationCounter=" + mutationCounter);
    // Return mutated population
    return newPopulation;

  }

}
