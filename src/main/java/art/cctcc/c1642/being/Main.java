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

import processing.core.PApplet;

/**
 *
 * @author Jonathan Chang, Chun-yien <ccy@cctcc.art>
 */
public class Main extends PApplet {

  int beingNo;

  BeingDemoGA ga;
  BeingPopulation population;
  // Keep track of current generation
  int generation = 1;
  int timer = 0;
  int sec = 2;

  @Override
  public void settings() {
    fullScreen();
  }

  @Override
  public void setup() {
    rectMode(CENTER);
    stroke(255);
    noFill();
    frameRate(120);
    beingNo = 100 * width / 3840;
    ga = new BeingDemoGA(beingNo, 0.001, 0.95, beingNo / 10, width, height);
    // Initialize population
    population = ga.initPopulation();
    // Evaluate population
    ga.evalPopulation(population);
  }

  @Override
  public void mouseClicked() {

    population = ga.initPopulation();
    ga.evalPopulation(population);
  }

  @Override
  public void draw() {

    background(128);
    for (var i = 0; i < beingNo; i++) {
      Being b = population.getIndividuals()[i];
      stroke(b.getColor());
      pushMatrix();
      translate(b.getX(), b.getY());
      var size = b.getSize();

      for (int j = 0; j < b.getRing() - 1; j++) {
        if (j % 2 == 0) {
          circle(0, 0, size);
        } else {
          rotate(PI / (float) b.getDelta()[j - 1] * (b.isClockwise() ? 1 : -1));
          rect(0, 0, size, size);
        }
        size -= b.getDelta()[j];
      }
      popMatrix();
      b.move();
      if (b.getX() + b.getSize() / 2 > width || b.getX() - b.getSize() / 2 < 0
              || b.getY() + b.getSize() / 2 > height || b.getY() - b.getSize() / 2 < 0) {
        b.reverseDir();
      }
    }
    if (timer++ > frameRate * sec) {
      timer = 0;
      // Print fittest individual from population
//      System.out.println("Best solution: " + population.getFittest(0));
      System.out.println("Generation = " + generation);
      // Apply crossover
      population = ga.crossoverPopulation(population);
      // Apply mutation
      population = ga.mutatePopulation(population);
      // Evaluate population
      ga.evalPopulation(population);
      // Increment the current generation
      generation++;
    }
  }

  public static void main(String[] args) {
    System.setProperty("sun.java2d.uiScale", "1.0");
    PApplet.main(Main.class);
  }
}
