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

import ga.chapter2.Population;
import lombok.Getter;

/**
 *
 * @author Jonathan Chang, Chun-yien <ccy@musicapoetica.org>
 */
@Getter
public class BeingPopulation extends Population<Being> {

  public BeingPopulation(int populationSize) {

    this.population = new Being[populationSize];
  }

  public BeingPopulation(int populationSize, float width, float height) {

    this(populationSize);

    for (int individualCount = 0; individualCount < populationSize; individualCount++) {
      // Create an individual, initializing its chromosome to the given
      // length
      var individual = new Being();
      var size = individual.getSize();
      individual.setX((int) (Math.random() * (width - size * 2) + size));
      individual.setY((int) (Math.random() * (height - size * 2) + size));
      individual.setColor((int) (Math.random() * 256));
      individual.encodeGenes();
      // Add individual to population
      this.population[individualCount] = individual;
    }
  }

  @Override
  public Being[] getIndividuals() {

    return this.population;
  }

}
