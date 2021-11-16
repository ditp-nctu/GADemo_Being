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
package art.cctcc.c1642.being.server;

import art.cctcc.c1642.being.Being;
import art.cctcc.c1642.being.BeingDemoGA;
import art.cctcc.c1642.being.BeingPopulation;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

/**
 *
 * @author Jonathan Chang, Chun-yien <ccy@musicapoetica.org>
 */
public class BeingDemoGAServerThread implements Runnable {

  static final Map<String, BeingDemoGAServerThread> sessions = new HashMap<>();

  final String session_id;
  final BeingDemoGA ga;
  final float max_ring;

  int generation = 1;
  BeingPopulation population;

  public BeingDemoGAServerThread(String session_id,
          int populationSize, double mutationRate, double crossoverRate,
          float width, float height) {
    this.session_id = session_id;
    this.max_ring = 40 * width / 3840;
    ga = new BeingDemoGA(populationSize, mutationRate, crossoverRate,
            populationSize * 30 / 100, width, height);
    population = ga.initPopulation();
    ga.evalPopulation(population);
    new Thread(this).start();
  }

  public Response getResponse(String query, String msg) {

    return new Response(this, query, msg);
  }

  @Override
  public void run() {
    while (!ga.isTerminationConditionMet(population)) {
      System.out.printf("========== generation#%d ==========\n", generation++);
      population = ga.crossoverPopulation(population);
      population = ga.mutatePopulation(population);
      System.out.println();
      ga.evalPopulation(population);
      IntStream.range(0, ga.getElitismCount())
              .mapToObj(population::getFittest)
              .sorted(Comparator.comparing(Being::getSize))
              .map(Being::getInfo)
              .forEach(System.out::println);
      System.out.printf(" Elitism fitness average=%.2f (population: %d/%d)\n",
              ga.getElitismFitnessAverage(population),
              ga.getElitismCount(), ga.getPopulationSize());
    }
    sessions.remove(this.session_id);
  }

}
