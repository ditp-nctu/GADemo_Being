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

import ga.chapter2.Population;
import java.util.Objects;
import java.util.stream.IntStream;

/**
 *
 * @author Jonathan Chang, Chun-yien <ccy@musicapoetica.org>
 */
public class BeingPopulation extends Population<Latent> {

  public BeingPopulation(int populationSize) {

    this.population = new Latent[populationSize];
  }

  public boolean containsSameSize(Latent being) {

    return containsSameSize(being, this.population.length);
  }

  public boolean containsSameSize(Latent being, int elitismCount) {

    return IntStream.range(0, elitismCount)
            .mapToObj(this::getFittest)
            .filter(Objects::nonNull)
            .map(Latent::getSize)
            .anyMatch(size -> size == being.getSize());
  }
}
