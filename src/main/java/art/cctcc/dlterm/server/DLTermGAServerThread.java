
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

import static art.cctcc.c1642.being.Constants.r;
import art.cctcc.dlterm.DLTermGA;
import art.cctcc.dlterm.Latent;
import art.cctcc.dlterm.LatentPopulation;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import java.util.Objects;
import java.util.stream.DoubleStream;
import lombok.Getter;

/**
 *
 * @author Jonathan Chang, Chun-yien <ccy@musicapoetica.org>
 */
@Getter
public class DLTermGAServerThread {

  private final String session_id;
  private final DLTermGA ga;

  private int generation;
  private LatentPopulation population;
  private boolean terminated;

  public DLTermGAServerThread(String session_id, int populationSize,
          double mutationRate, double crossoverRate,
          int latent_size, int elitismCount) {

    this.session_id = session_id;
    ga = new DLTermGA(populationSize, mutationRate, crossoverRate,
            latent_size, elitismCount);
    generation = 0;
    population = ga.initPopulation();
    ga.evalPopulation(population);
  }

  public Response getResponse(String query, String msg, JsonObject evals) {

    if (!this.terminated && Objects.nonNull(evals)) {
      this.terminated = this.run(evals);
    }
    return new Response(this, query, msg);
  }

  public boolean run(JsonObject evals) {

    System.out.printf("========== generation#%d ==========\n", ++generation);
    for (int i = 0; i < this.population.size(); i++) {
      var latent = this.population.getIndividual(i);
      var eval = evals.getDouble(latent.getId().toString());
      latent.setFitness(eval);
    }
    ga.evalPopulation(population);
//    for (int i = 0; i < this.population.size(); i++) {
//      var individual = population.getFittest(i);
//      if (i < ga.getElitismCount())
//        individual.setElite(true);
//      else {
//        individual.setElite(false);
//        var latent_code = DoubleStream.generate(r::nextGaussian)
//                .limit(individual.getChromosomeLength())
//                .toArray();
//        individual.encodeGenes(latent_code);
//      }
//    }
    //    this.population = ga.crossoverPopulation(this.population);
    this.population = ga.mutatePopulation(this.population);
    return ga.isTerminationConditionMet(population);
  }
}
