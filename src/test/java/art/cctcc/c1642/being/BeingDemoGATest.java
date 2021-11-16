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
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Jonathan Chang, Chun-yien <ccy@musicapoetica.org>
 */
public class BeingDemoGATest {

  public BeingDemoGATest() {
  }

  @BeforeAll
  public static void setUpClass() {
  }

  @AfterAll
  public static void tearDownClass() {
  }

  @BeforeEach
  public void setUp() {
  }

  @AfterEach
  public void tearDown() {
  }

  /**
   * Test of initPopulation method, of class BeingDemoGA.
   */
  @Test
  @Disabled
  public void testInitPopulation() {
    System.out.println("initPopulation");
    BeingDemoGA instance = null;
    BeingPopulation expResult = null;
    BeingPopulation result = instance.initPopulation();
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of calcFitness method, of class BeingDemoGA.
   */
  @Test
  public void testCalcFitness() {
    System.out.println("calcFitness");
    Being individual = new Being();
    int ring = 2;
    int[] delta = {225, 12, 8, 9, 3, 7, 8, 3, 8, 2, 2, 7, 6, 2, 7, 5, 11, 1, 3, 5, 16, 4, 12, 21};
    //{194, 3, 5, 5, 6, 2, 6, 6, 4, 5, 3, 1, 5, 2, 5, 1, 6, 5, 2, 9, 8, 4, 10, 9};
    //{247, 9, 9, 4, 8, 7, 8, 5, 6, 5, 8, 6, 6, 8, 6, 8, 1, 2, 7, 1, 9, 7, 13, 3};
    individual.setDelta(delta);
    assertEquals(delta, individual.getDelta());
    individual.setSize(min_size / 2 + delta[0] + delta[1]);
    individual.refreshRing();
    assertEquals(ring, individual.getRing());
    BeingDemoGA instance = new BeingDemoGA(1, 0, 0, 0, UHDScreenWidth, UHDScreenHeight);
    double result = instance.calcFitness(individual);
    System.out.println("result = " + result);
  }

  /**
   * Test of mutatePopulation method, of class BeingDemoGA.
   */
  @Test
  @Disabled
  public void testMutatePopulation() {
    System.out.println("mutatePopulation");
    BeingPopulation population = null;
    BeingDemoGA instance = null;
    BeingPopulation expResult = null;
    BeingPopulation result = instance.mutatePopulation(population);
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of crossoverPopulation method, of class BeingDemoGA.
   */
  @Test
  @Disabled
  public void testCrossoverPopulation() {
    System.out.println("crossoverPopulation");
    BeingPopulation population = null;
    BeingDemoGA instance = null;
    BeingPopulation expResult = null;
    BeingPopulation result = instance.crossoverPopulation(population);
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  @Test
  @Disabled
  public void testSize() {
    System.out.println("size");
    for (int i = min_size; i < max_size; i++) {
      var sizeScore = (Math.cos(i / 3.14) + 1) / 2;
      System.out.printf("i = %3d", i);
      System.out.println(", sizeScore = " + "*".repeat((int) (sizeScore * 10)));
    }
  }
}
