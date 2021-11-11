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
import java.util.Random;

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
  public double calcFitness(Being individual) {

    var score_base = 0;
    var currentSize = individual.getSize() - individual.getDelta()[0];
    for (int i = 1; i < individual.getRing() - 1; i++) {
      score_base += (i % 2 == 0)
              ? individual.getDelta()[i - 1]
              : Math.abs(individual.getDelta()[i - 1] - currentSize * (Math.sqrt(2.0) - 1));
      currentSize -= individual.getDelta()[i];
    }
    var deltaScore = individual.getRing() <= 1 ? 0.0 : 1.0 / (score_base / (individual.getRing() - 1) + 1.0);
//    var sizeScore = individual.getSize() - Being.min_size > 0 ? 1.0 : 0.0;
    var sizeScore = Math.max(0.0, individual.getSize() - Being.min_size) / Being.max_size;
    var ringScore = 1.0 * individual.getRing() / Being.max_ring;
//    var fitness = Math.pow(deltaScore + sizeScore + ringScore, 1.0 / 3);
    var fitness = (Math.sqrt(deltaScore * ringScore * 100) + sizeScore * 100) / 2;
//    if (individual.getRing() < 5)
//      System.out.printf("deltaScore=%f, size=%d, sizeScore=%f, ringScore=%f, fitness=%f\n",
//              deltaScore, individual.getSize(), sizeScore, ringScore, fitness);
    individual.setFitness(fitness);
    if (debug) {
      System.out.println("score_base = " + score_base);
      System.out.println("deltaScore = " + deltaScore);
      System.out.println("sizeScore = " + sizeScore);
      System.out.println("ringScore = " + ringScore);
    }
    return fitness;
  }
  Random r = new Random();

  @Override
  public BeingPopulation mutatePopulation(BeingPopulation population) {

    var mutationCounter = 0;
    // Initialize new population
    var newPopulation = new BeingPopulation(this.populationSize);

    // Loop over current population by fitness
    for (int populationIndex = 0; populationIndex < population.size(); populationIndex++) {
      var individual = population.getFittest(populationIndex);
      // Skip mutation if this is an elite individual
      if (populationIndex > this.elitismCount) {
        var newIndividual = new Being(individual.getX(), individual.getY(),
                individual.getDx(), individual.getDy(),
                individual.getColor(), individual.isClockwise());
        var mutationCites = r.nextInt(3) + 1;
        // Loop over individual's genes
        for (int geneIndex = 0; geneIndex < individual.getChromosomeLength(); geneIndex++) {

          // Does this gene need mutation?
          if (this.mutationRate > Math.random() && mutationCites-- > 0) {
            // Get new gene
            int newGene = (individual.getGene(geneIndex) == 1) ? 0 : 1;
            // Mutate gene
            newIndividual.setGene(geneIndex, newGene);
          } else {
            newIndividual.setGene(geneIndex, individual.getGene(geneIndex));
          }
        }
        newIndividual.decodeGenes();
        if (calcFitness(newIndividual) > individual.getFitness()) {
          newIndividual.setColor((int) (Math.random() * 256));
          newPopulation.setIndividual(populationIndex, newIndividual);
          mutationCounter++;
          System.out.printf("! Ring=%d, delta=%s\n", newIndividual.getRing(), Arrays.toString(newIndividual.getDelta()));
          continue;
        }
      }
      newPopulation.setIndividual(populationIndex, individual);
    }
    System.out.println("mutationCounter = " + mutationCounter);
    // Return mutated population
    return newPopulation;

  }

  @Override
  public BeingPopulation crossoverPopulation(BeingPopulation population) {

    var crossoverCounter = 0;
    // Create new population
    var newPopulation = new BeingPopulation(this.populationSize);

    // Loop over current population by fitness
    for (int populationIndex = 0; populationIndex < population.size(); populationIndex++) {
      var parent1 = population.getFittest(populationIndex);

      // Apply crossover to this individual?
      if (this.crossoverRate > Math.random() && populationIndex >= this.elitismCount) {
        // Initialize offspring
        var offspring = new Being(parent1.getX(), parent1.getY(),
                parent1.getDx(), parent1.getDy(),
                parent1.getColor(), parent1.isClockwise());

        // Find second parent
        var parent2 = selectParent(population);

        // Loop over genome
        var usingFirst = true;
        var crossoverCites = r.nextInt(3) + 1;
        for (var geneIndex = 0; geneIndex < parent1.getChromosomeLength(); geneIndex++) {
          // 3 crossover cites at most with 10% swapping rate
          if (0.1 > Math.random() && crossoverCites-- > 0) {
            usingFirst = !usingFirst;
          }
          offspring.setGene(geneIndex, (usingFirst ? parent1 : parent2).getGene(geneIndex));
        }
        offspring.decodeGenes();
        if (calcFitness(offspring) > parent1.getFitness()) {
          // Add offspring to new population
          newPopulation.setIndividual(populationIndex, offspring);
          crossoverCounter++;
          System.out.printf("X Ring=%d, delta=%s\n", offspring.getRing(), Arrays.toString(offspring.getDelta()));
          continue;
        }
      }
      // Add individual to new population without applying crossover
      newPopulation.setIndividual(populationIndex, parent1);
    }
    System.out.println("crossoverCounter = " + crossoverCounter);
    return newPopulation;
  }

}
