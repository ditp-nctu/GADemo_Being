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

import static art.cctcc.c1642.being.Constants.*;
import java.awt.Color;
import java.util.Comparator;
import java.util.stream.IntStream;
import processing.core.PApplet;

/**
 *
 * @author Jonathan Chang, Chun-yien <ccy@cctcc.art>
 */
public class Main extends PApplet {

  BeingDemoGA ga;
  BeingPopulation population;
  int generation;
  float text_size;
  float bg = 100;
  int inc = 1;
  boolean interrupted;

  Runnable gaThread = () -> {

    while (true) {
      if (!ga.isTerminationConditionMet(population) && !interrupted) {
        if (generation == 0) {
          try {
            Thread.sleep(3000);
          } catch (InterruptedException ex) {
          }
        }
        System.out.printf("========== generation#%d ==========\n", ++generation);
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
      } else if (interrupted) {
        population = ga.initPopulation();
        ga.evalPopulation(population);
        generation = 0;
        interrupted = false;
      } else {
        try {
          Thread.sleep(500);
        } catch (InterruptedException ex) {
        }
      }
    }
  };

  @Override
  public void settings() {

    fullScreen();
  }

  @Override
  public void setup() {

    rectMode(CENTER);
    strokeWeight(1);
    float ratio = height > width ? 1.0f * width / UHDScreenHeight : 1.0f * height / UHDScreenWidth;
    text_size = 72 * ratio;
    DefaultMaxRing = (int) (40 * ratio);
    DefaultChromosomeLength = 8 + 8 * (DefaultMaxRing - 1);
    int elitismCount = DefaultPopulationSize * 30 / 100;
    ga = new BeingDemoGA(DefaultPopulationSize,
            DefaultMutationRate, DefaultCrossoverRate,
            elitismCount, width, height);
    population = ga.initPopulation();
    ga.evalPopulation(population);
    new Thread(gaThread).start();
  }

  @Override
  public void draw() {

    background(bg += inc);
    if (bg >= 255 || bg <= 0) {
      inc = -inc;
    }
    noFill();
    for (var i = 0; i < ga.getElitismCount(); i++) {
      Being b = population.getFittest(i);
      if (ga.beingQualifier.test(b)) {
        stroke(b.getColor());
      } else {
        stroke(new Color(0, b.getColor(), 0).getRGB());
      }
      pushMatrix();
      translate(b.getX(), b.getY());
      var size = b.getSize();
      for (int j = 0; j < b.getRing(); j++) {
        if (j % 2 == 0) {
          circle(0, 0, size);
        } else {
          rotate(random(0.99f, 1.01f) * PI / (1.0f + b.getDelta()[j - 1]) * (b.isClockwise() ? 1 : -1));
          rect(0, 0, size, size);
        }
        if (j < b.getDelta().length) {
          size -= b.getDelta()[j];
        }
      }
      popMatrix();
      b.move();
      if ((b.getX() + 0.5 * b.getSize()) > width || (b.getX() - 0.5 * b.getSize()) < 0) {
        b.reverseDir("x");
      } else if ((b.getY() + 0.5 * b.getSize()) > height || (b.getY() - 0.5 * b.getSize()) < 0) {
        b.reverseDir("y");
      }
    }
    fill(bg > 128 ? 0 : 255);
    textSize(text_size);
    text(String.format("g=%d, t=%d/%d, a=%.2f",
            generation, ga.getQualifiedCount(population), ga.getElitismCount(), ga.getElitismFitnessAverage(population)),
            10, text_size);
  }

  @Override
  public void mouseClicked() {

    interrupted = true;
  }

  public static void main(String[] args) {

    System.setProperty("sun.java2d.uiScale", "1.0");
    PApplet.main(Main.class);
  }
}
