/*
 * Copyright 2022 Jonathan Chang, Chun-yien <ccy@musicapoetica.org>.
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
import ga.real.GeneticAlgorithm;
import java.util.stream.DoubleStream;
import java.util.stream.Stream;

/**
 *
 * @author Jonathan Chang, Chun-yien <ccy@musicapoetica.org>
 */
public class DLTermGA extends GeneticAlgorithm<LatentPopulation, Latent> {

    private final int chromosomeLength;

    public DLTermGA(int populationSize, double mutationRate, double crossoverRate,
            int chromosomeLength, int elitismCount) {

        super(populationSize, mutationRate, crossoverRate, elitismCount);
        this.chromosomeLength = chromosomeLength;
    }

    @Override
    public LatentPopulation initPopulation() {
        // Initialize population
        var population = new LatentPopulation(this.populationSize);

        for (int individualCount = 0; individualCount < this.populationSize; individualCount++) {
            var individual = Stream.generate(() -> new Latent(this.chromosomeLength))
                    .findAny()
                    .get();

            var latent_code = DoubleStream.generate(r::nextGaussian)
                    .limit(chromosomeLength)
                    .toArray();
            individual.encodeGenes(latent_code);
            population.setIndividual(individualCount, individual);
        }
        return population;
    }

    @Override
    public boolean isTerminationConditionMet(LatentPopulation population) {

        return false;
    }

    @Override
    public LatentPopulation crossoverPopulation(LatentPopulation population) {

        var crossoverCounter = 0;
        var newPopulation = new LatentPopulation(this.populationSize);
        for (int populationIndex = 0; populationIndex < this.populationSize; populationIndex++) {
            var parent1 = population.getFittest(populationIndex);
            if (populationIndex < this.elitismCount || this.crossoverRate < r.nextDouble()) {
                newPopulation.setIndividual(populationIndex, parent1);
            } else {
                var offspring = new Latent(chromosomeLength);
                var parent2 = selectParent(population);
                var crossover = r.nextInt(chromosomeLength / 20) + 1;
                var crossoverCites = Stream.generate(() -> r.nextInt(chromosomeLength))
                        .distinct()
                        .limit(crossover)
                        .toList();
                var usingFirst = true;
                for (var geneIndex = 0; geneIndex < chromosomeLength; geneIndex++) {
                    if (crossoverCites.contains(geneIndex)) {
                        usingFirst = !usingFirst;
                    }
                    offspring.setGene(geneIndex, (usingFirst ? parent1 : parent2).getGene(geneIndex));
                }
                newPopulation.setIndividual(populationIndex, offspring);
                crossoverCounter++;
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
            var latent = population.getFittest(populationIndex);
            if (populationIndex < this.elitismCount || this.mutationRate < r.nextDouble()) {
                newPopulation.setIndividual(populationIndex, latent);
            } else {
                var newLatent = new Latent(chromosomeLength);
                var mutation = r.nextInt(chromosomeLength / 10) + 1;
                var mutationCites = Stream.generate(() -> r.nextInt(chromosomeLength))
                        .distinct()
                        .limit(mutation)
                        .toList();
                for (int geneIndex = 0; geneIndex < chromosomeLength; geneIndex++) {
                    if (mutationCites.contains(geneIndex)) {
                        double newGene = latent.getGene(geneIndex) + r.nextGaussian();
                        newLatent.setGene(geneIndex, newGene);
                    } else {
                        newLatent.setGene(geneIndex, latent.getGene(geneIndex));
                    }
                }
                newPopulation.setIndividual(populationIndex, newLatent);
                mutationCounter++;
            }
        }
        System.out.print((mutationCounter > 0) ? "\n" : "");
        System.out.print(" mutationCounter=" + mutationCounter);
        return newPopulation;
    }

    /**
     * In DLTerm fitness is given by input.
     *
     * @param individual
     * @return
     */
    @Override
    public double calcFitness(Latent individual) {

        return individual.getFitness();
    }
}
