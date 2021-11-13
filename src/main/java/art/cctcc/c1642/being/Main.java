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

import java.util.stream.IntStream;
import processing.core.PApplet;

/**
 *
 * @author Jonathan Chang, Chun-yien <ccy@cctcc.art>
 */
public class Main extends PApplet {

  int populationSize;

  BeingDemoGA ga;
  BeingPopulation population;
  // Keep track of current generation
  int generation = 1;
  int timer;
  int text_size;

  @Override
  public void settings() {

    fullScreen();
  }

  @Override
  public void setup() {

    rectMode(CENTER);
    stroke(255);
    noFill();
    frameRate(60);
    text_size = 50 * width / 3840;
    Being.max_ring = 40 * width / 3840;
    populationSize = 200 * (width * height) / (3840 * 2160);
    ga = new BeingDemoGA(populationSize, 0.75, 0.75, populationSize * 30 / 100, width, height);
    // Initialize population
    population = ga.initPopulation();
    // Evaluate population
    ga.evalPopulation(population);
    new Thread(() -> {
      while (true) {
        try {
//      System.out.println(timer + " vs. " + frameRate + " " + ga.isTerminationConditionMet(population));
          if (timer > frameRate && !ga.isTerminationConditionMet(population)) {
            timer = 0;

            // Increment the current generation
            System.out.printf("========== generation#%d ==========\n", generation++);
            // Apply crossover
            population = ga.crossoverPopulation(population);
            // Apply mutation
            population = ga.mutatePopulation(population);
            System.out.println();
            // Evaluate population
            ga.evalPopulation(population);
            IntStream.range(0, populationSize)
                    .peek(i -> {
                      if (i == ga.getElitismCount()) {
                        System.out.println("-".repeat(80));
                      }
                    })
                    .mapToObj(population::getFittest)
                    .map(Being::getInfo)
                    .forEach(System.out::println);
            System.out.printf(" population fitness=%.2f\n", ga.getElitismFitnessAverage(population));
          } else {
            Thread.sleep(100);
          }
        } catch (InterruptedException ex) {
          ex.printStackTrace();
        }
      }
    }).start();
  }

  float bg = 100;
  int inc = 1;

  @Override
  public void draw() {

    timer++;
    background(bg += inc);
    if (bg >= 255 || bg <= 0) {
      inc = -inc;
    }
    for (var i = 0; i < ga.getElitismCount(); i++) {
      Being b = population.getFittest(i);
      stroke(b.getColor());
      pushMatrix();
      translate(b.getX(), b.getY());
      var size = b.getSize();
      for (int j = 0; j < b.getRing() - 1; j++) {
        if (j % 2 == 0) {
          circle(0, 0, size);
        } else {
          rotate(random(0.99f, 1.01f) * PI / (float) b.getDelta()[j - 1] * (b.isClockwise() ? 1 : -1));
          rect(0, 0, size, size);
        }
        size -= b.getDelta()[j];
      }
      popMatrix();
      b.move();
      if ((b.getX() + 0.5 * b.getSize()) > width || (b.getX() - 0.5 * b.getSize()) < 0) {
        b.reverseDir("x");
      }
      if ((b.getY() + 0.5 * b.getSize()) > height || (b.getY() - 0.5 * b.getSize()) < 0) {
        b.reverseDir("y");
      }
    }
    fill(bg > 128 ? 0 : 255);
    textSize(text_size);
    text(String.valueOf(generation), 10, text_size);
    noFill();
  }

  @Override
  public void mouseClicked() {

    population = ga.initPopulation();
    ga.evalPopulation(population);
    generation = 1;
  }

  public static void main(String[] args) {
    System.setProperty("sun.java2d.uiScale", "1.0");
    PApplet.main(Main.class);
  }
}
