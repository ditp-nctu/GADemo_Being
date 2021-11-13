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
import java.util.stream.IntStream;
import java.util.stream.Stream;
import lombok.Getter;

/**
 *
 * @author Jonathan Chang, Chun-yien <ccy@musicapoetica.org>
 */
@Getter
public class BeingDemoGA extends GeneticAlgorithm<BeingPopulation, Being> {

  private float width;
  private float height;

  public BeingDemoGA(int populationSize, double mutationRate, double crossoverRate,
          int elitismCount, float width, float height) {

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

    return !IntStream.range(0, elitismCount)
            .map(i -> population.getFittest(i).getRing())
            .anyMatch(ring -> ring <= Being.min_ring);
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
    var deltaScore = (being.getRing() == 1) ? 0.0
            : (1.0 / (deltaScoreBase / (being.getRing() - 1) + 1.0));
    var ringScore = 1.0 * being.getRing() / Being.max_ring;
    var fitness = (deltaScore * 1.0
            + ringScore * 9.0) / 10;
    being.setFitness(fitness);
    if (debug) {
      System.out.printf("ring=%d, deltaScore=%.2f, ringScore=%.2f\n",
              being.getRing(),
              deltaScore, ringScore);
    }
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
      // Apply crossover to this individual?
      if (populationIndex < this.elitismCount || this.crossoverRate < Being.r.nextDouble()) {
        // Add individual to new population without applying crossover
        newPopulation.setIndividual(populationIndex, parent1);
      } else {
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
        // Add offspring to new population
        if (population.containsSameSize(offspring)) {
          newPopulation.setIndividual(populationIndex, parent1);
        } else {
          newPopulation.setIndividual(populationIndex, offspring);
          crossoverCounter++;
          calcFitness(offspring);
          System.out.printf("X %s\n", offspring.getInfo());
        }
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
      if (populationIndex < this.elitismCount || this.mutationRate < Being.r.nextDouble()) {
        newPopulation.setIndividual(populationIndex, being);
      } else {
        var newBeing = new Being(being);
        var mutation = Being.r.nextInt(Being.chromosomeLength / 10) + 1;
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
        if (population.containsSameSize(newBeing)) {
          newPopulation.setIndividual(populationIndex, being);
        } else {
          newBeing.setColor(Being.r.nextInt(256));
          newPopulation.setIndividual(populationIndex, newBeing);
          mutationCounter++;
          calcFitness(newBeing);
          System.out.printf("\n! %s", newBeing.getInfo());
        }
      }
    }
    System.out.print((mutationCounter > 0) ? "\n" : "");
    System.out.print(" mutationCounter=" + mutationCounter);
    // Return mutated population
    return newPopulation;
  }

  public double getElitismFitnessAverage(BeingPopulation population) {

    return IntStream.range(0, elitismCount)
            .mapToObj(population::getFittest)
            .mapToDouble(b -> b.getFitness() * 100)
            .average().getAsDouble();
  }
}
