
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
package art.cctcc.dlterm.server;

import art.cctcc.dlterm.Latent;
import art.cctcc.dlterm.DLProjectGA;
import art.cctcc.dlterm.LatentPopulation;
import java.util.Comparator;
import java.util.stream.IntStream;
import lombok.Getter;

/**
 *
 * @author Jonathan Chang, Chun-yien <ccy@musicapoetica.org>
 */
@Getter
public class DLTermGAServerThread {

  private final String session_id;
  private final DLProjectGA ga;

  private int generation;
  private LatentPopulation population;
  private boolean terminated;
  private long qualifiedCount;

  public DLTermGAServerThread(String session_id, int populationSize,
          double mutationRate, double crossoverRate, int latent_size) {

    this.session_id = session_id;
    var elitismCount = populationSize * 30 / 100;
    ga = new DLProjectGA(populationSize, mutationRate, crossoverRate, latent_size, elitismCount);
    generation = -1;
    population = ga.initPopulation();
    ga.evalPopulation(population);
  }

  public Response getResponse(String query, String msg) {

    if (!this.terminated) {
      this.terminated = this.run();
    }
    return new Response(this, query, msg);
  }

  public boolean run() {

    System.out.printf("========== generation#%d ==========\n", ++generation);
    population = ga.crossoverPopulation(population);
    population = ga.mutatePopulation(population);
    System.out.println();
    ga.evalPopulation(population);
    IntStream.range(0, ga.getElitismCount())
            .mapToObj(population::getFittest)
//            .sorted(Comparator.comparing(Latent::getSize))
            .map(Latent::getInfo)
            .forEach(System.out::println);
    this.qualifiedCount = ga.getQualifiedCount(population);
    System.out.printf(" Elitism population: %d/%d/%d)\n",
            this.qualifiedCount, ga.getElitismCount(), ga.getPopulationSize());
    return ga.isTerminationConditionMet(population);
  }
}
