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
package art.cctcc.dlterm;

import static art.cctcc.c1642.being.Constants.*;
import ga.chapter2.GeneticAlgorithm;
import java.util.function.Predicate;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 *
 * @author Jonathan Chang, Chun-yien <ccy@musicapoetica.org>
 */
public class DLProjectGA extends GeneticAlgorithm<BeingPopulation, Latent> {

  final float screenWidth;
  final float screenHeight;
  public final Predicate<Latent> qualifier;
  private int chromosome_size;

  public DLProjectGA(int populationSize, double mutationRate, double crossoverRate,
          int elitismCount, float width, float height) {

    super(populationSize, mutationRate, crossoverRate, elitismCount);
    this.screenWidth = width;
    this.screenHeight = height;
    this.qualifier = being -> being.getRing() > DefaultMinRing / DefaultMaxSize; //TODO
  }

  public DLProjectGA(int populationSize, double mutationRate, double crossoverRate,
          int elitismCount) {

    super(populationSize, mutationRate, crossoverRate, elitismCount);
    this.screenWidth = BrowserScreenWidth;
    this.screenHeight = BrowserScreenHeight;
    this.qualifier = being -> being.getRing() > DefaultMinRing / DefaultMaxSize; //TODO
  }

  public DLProjectGA(int populationSize, double mutationRate, double crossoverRate,
          int elitismCount, int max_size) {

    super(populationSize, mutationRate, crossoverRate, elitismCount);
    this.screenWidth = BrowserScreenWidth;
    this.screenHeight = BrowserScreenHeight;
    this.qualifier = being -> being.getRing() > DefaultMinRing / DefaultMaxSize; //TODO
  }

  @Override
  public BeingPopulation initPopulation() {
    // Initialize population
    var population = new BeingPopulation(this.populationSize);

    for (int individualCount = 0; individualCount < this.populationSize; individualCount++) {
      var individual = Stream.generate(() -> new Latent(this.chromosome_size))
              .findAny()
              .get();
      var size = individual.getSize();
      individual.setX(r.nextInt((int) screenWidth - size) + size / 2);
      individual.setY(r.nextInt((int) screenHeight - size) + size / 2);
      individual.setColor(r.nextInt(256));
      individual.encodeGenes();
      population.setIndividual(individualCount, individual);
    }
    return population;
  }

  @Override
  public boolean isTerminationConditionMet(BeingPopulation population) {

    return getQualifiedCount(population) == elitismCount;
  }

  public long getQualifiedCount(BeingPopulation population) {

    return IntStream.range(0, elitismCount)
            .mapToObj(i -> population.getFittest(i))
            .filter(qualifier)
            .count();
  }

  public double getElitismFitnessAverage(BeingPopulation population) {

    return IntStream.range(0, elitismCount)
            .mapToObj(population::getFittest)
            .mapToDouble(b -> b.getFitness() * 100)
            .average().getAsDouble();
  }

  @Override
  public double calcFitness(Latent being) {

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
    var ringScore = 1.0 * being.getRing() / DefaultMaxRing;
    var fitness = being.getSize() > this.chromosome_size ? 0
            : (deltaScore * 1.0 + ringScore * 9.0) / 10;
    being.setFitness(fitness);
    return fitness;
  }

  @Override
  public BeingPopulation crossoverPopulation(BeingPopulation population) {

    var crossoverCounter = 0;
    var newPopulation = new BeingPopulation(this.populationSize);
    for (int populationIndex = 0; populationIndex < population.size(); populationIndex++) {
      var parent1 = population.getFittest(populationIndex);
      if (populationIndex < this.elitismCount || this.crossoverRate < r.nextDouble()) {
        newPopulation.setIndividual(populationIndex, parent1);
      } else {
        var offspring = new Latent(parent1);
        var parent2 = selectParent(population);
        var crossover = r.nextInt(DefaultChromosomeLength / 20) + 1;
        var crossoverCites = Stream.generate(() -> r.nextInt(parent1.getChromosomeLength()))
                .distinct()
                .limit(crossover)
                .toList();
        var usingFirst = true;
        for (var geneIndex = 0; geneIndex < parent1.getChromosomeLength(); geneIndex++) {
          if (crossoverCites.contains(geneIndex)) {
            usingFirst = !usingFirst;
          }
          offspring.setGene(geneIndex, (usingFirst ? parent1 : parent2).getGene(geneIndex));
        }
        offspring.decodeGenes();
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
    var newPopulation = new BeingPopulation(this.populationSize);
    for (int populationIndex = 0; populationIndex < population.size(); populationIndex++) {
      var being = population.getFittest(populationIndex);
      if (populationIndex < this.elitismCount || this.mutationRate < r.nextDouble()) {
        newPopulation.setIndividual(populationIndex, being);
      } else {
        var newBeing = new Latent(being);
        var mutation = r.nextInt(DefaultChromosomeLength / 10) + 1;
        var mutationCites = Stream.generate(() -> r.nextInt(being.getChromosomeLength()))
                .distinct()
                .limit(mutation)
                .toList();
        for (int geneIndex = 0; geneIndex < being.getChromosomeLength(); geneIndex++) {
          if (mutationCites.contains(geneIndex)) {
            int newGene = (being.getGene(geneIndex) == 1) ? 0 : 1;
            newBeing.setGene(geneIndex, newGene);
          } else {
            newBeing.setGene(geneIndex, being.getGene(geneIndex));
          }
        }
        newBeing.decodeGenes();
        if (population.containsSameSize(newBeing)) {
          newPopulation.setIndividual(populationIndex, being);
        } else {
          newBeing.setColor(r.nextInt(256));
          newPopulation.setIndividual(populationIndex, newBeing);
          mutationCounter++;
          calcFitness(newBeing);
          System.out.printf("\n! %s", newBeing.getInfo());
        }
      }
    }
    System.out.print((mutationCounter > 0) ? "\n" : "");
    System.out.print(" mutationCounter=" + mutationCounter);
    return newPopulation;
  }
}
