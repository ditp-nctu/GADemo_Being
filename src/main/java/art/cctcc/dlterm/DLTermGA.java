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
import java.util.Random;
import java.util.function.Predicate;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 *
 * @author Jonathan Chang, Chun-yien <ccy@musicapoetica.org>
 */
public class DLTermGA extends GeneticAlgorithm<LatentPopulation, Latent> {

  public final Predicate<Latent> qualifier;
  private final int latent_size;

  public DLTermGA(int populationSize, double mutationRate, double crossoverRate,
          int latent_size, int elitismCount) {

    super(populationSize, mutationRate, crossoverRate, elitismCount);
    this.latent_size = latent_size;
    this.qualifier = latent -> false; //TODO
  }

  @Override
  public LatentPopulation initPopulation() {
    // Initialize population
    var population = new LatentPopulation(this.populationSize);

    var rand = new Random();
    for (int individualCount = 0; individualCount < this.populationSize; individualCount++) {
      var individual = Stream.generate(() -> new Latent(this.latent_size))
              .findAny()
              .get();

      var latent_code = DoubleStream.generate(rand::nextGaussian)
              .limit(individual.getLatentLength())
              .toArray();
      individual.encodeGenes(latent_code);
      population.setIndividual(individualCount, individual);
    }
    return population;
  }

  @Override
  public boolean isTerminationConditionMet(LatentPopulation population) {

    return getQualifiedCount(population) == elitismCount;
  }

  public long getQualifiedCount(LatentPopulation population) {

    return IntStream.range(0, elitismCount)
            .mapToObj(i -> population.getFittest(i))
            .filter(qualifier)
            .count();
  }

  public double getElitismFitnessAverage(LatentPopulation population) {

    return IntStream.range(0, elitismCount)
            .mapToObj(population::getFittest)
            .mapToDouble(b -> b.getFitness() * 100)
            .average().getAsDouble();
  }

  @Override
  public LatentPopulation crossoverPopulation(LatentPopulation population) {

    var crossoverCounter = 0;
    var newPopulation = new LatentPopulation(this.populationSize);
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
        newPopulation.setIndividual(populationIndex, offspring);
        crossoverCounter++;
//        calcFitness(offspring);
        System.out.printf("X %s\n", offspring.getInfo());
      }
    }
    System.out.print(" crossoverCounter=" + crossoverCounter);
    return newPopulation;
  }

  @Override
  public LatentPopulation mutatePopulation(LatentPopulation population) {

    var mutationCounter = 0;
    var newPopulation = new LatentPopulation(this.populationSize);
    for (int populationIndex = 0; populationIndex < population.size(); populationIndex++) {
      var being = population.getFittest(populationIndex);
      if (populationIndex < this.elitismCount || this.mutationRate < r.nextDouble()) {
        newPopulation.setIndividual(populationIndex, being);
      } else {
        var newLatent = new Latent(being);
        var mutation = r.nextInt(DefaultChromosomeLength / 10) + 1;
        var mutationCites = Stream.generate(() -> r.nextInt(being.getChromosomeLength()))
                .distinct()
                .limit(mutation)
                .toList();
        for (int geneIndex = 0; geneIndex < being.getChromosomeLength(); geneIndex++) {
          if (mutationCites.contains(geneIndex)) {
            int newGene = (being.getGene(geneIndex) == 1) ? 0 : 1;
            newLatent.setGene(geneIndex, newGene);
          } else {
            newLatent.setGene(geneIndex, being.getGene(geneIndex));
          }
        }
        newLatent.decodeGenes();
        newPopulation.setIndividual(populationIndex, newLatent);
        mutationCounter++;
//        calcFitness(newLatent);
        System.out.printf("\n! %s", newLatent.getInfo());
      }
    }
    System.out.print((mutationCounter > 0) ? "\n" : "");
    System.out.print(" mutationCounter=" + mutationCounter);
    return newPopulation;
  }

  @Override
  public double calcFitness(Latent individual) {

    return individual.getFitness();
  }
}
