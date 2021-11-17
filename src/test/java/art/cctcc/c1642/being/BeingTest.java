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
import java.util.Arrays;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Jonathan Chang, Chun-yien <ccy@musicapoetica.org>
 */
public class BeingTest {

  Being instance;

  public BeingTest() {

    instance = new Being(DefaultMaxSize);
    int[] delta = {225, 12, 8, 9, 3, 7, 8, 3, 8, 2, 2, 7, 6, 2, 7, 5, 11, 1, 3, 5, 16, 4, 12, 21};
    //{194, 3, 5, 5, 6, 2, 6, 6, 4, 5, 3, 1, 5, 2, 5, 1, 6, 5, 2, 9, 8, 4, 10, 9};
    //{247, 9, 9, 4, 8, 7, 8, 5, 6, 5, 8, 6, 6, 8, 6, 8, 1, 2, 7, 1, 9, 7, 13, 3};
    instance.setDelta(delta);
    instance.setSize(226);
    instance.refreshRing();
    instance.encodeGenes();
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
   * Test of getSizeGene method, of class Being.
   */
  @Test
  public void testGetSizeGene() {
    System.out.println("getSizeGene");

    String expResult = "11100010";
    String result = instance.getSizeGene();
    assertEquals(expResult, result);
  }

  /**
   * Test of getDeltaGene method, of class Being.
   */
  @Test
  public void testGetDeltaGene() {
    System.out.println("getDeltaGene");

    String expResult = "111000010000110000001000000010010000001100000111000010000000001100001000000000100000001000000111000001100000001000000111000001010000101100000001000000110000010100010000000001000000110000010101";
    String result = instance.getDeltaGene();
    assertEquals(expResult, result);
  }

  /**
   * Test of encodeGenes method, of class Being.
   */
  @Test
  public void testEncodeGenes() {
    System.out.println("encodeGenes");

    Being instance2 = new Being(DefaultMaxSize);
    instance2.setSize(instance.getSize());
    instance2.setDelta(instance.getDelta());
    instance2.encodeGenes();
    assertEquals(instance.getSizeGene(), instance2.getSizeGene());
    assertEquals(instance.getDeltaGene(), instance2.getDeltaGene());
    for (int i = 0; i < instance.getChromosomeLength(); i++) {
      assertEquals(instance.getGene(i), instance2.getGene(i));
    }
  }

  /**
   * Test of decodeGenes method, of class Being.
   */
  @Test
  public void testDecodeGenes() {
    System.out.println("decodeGenes");

    Being instance = new Being(DefaultMaxSize);
    var size = instance.getSize();
    var delta = instance.getDelta();
    instance.encodeGenes();
    instance.setSize(size + 1);
    instance.setDelta(new int[DefaultMaxRing - 1]);
    assertNotEquals(size, instance.getSize());
    assertFalse(Arrays.equals(delta, instance.getDelta()));
    instance.decodeGenes();
    assertEquals(size, instance.getSize());
    assertTrue(Arrays.equals(delta, instance.getDelta()));
  }

  /**
   * Test of move method, of class Being.
   */
  @Test @Disabled
  public void testMove() {
    System.out.println("move");
    Being instance = new Being(DefaultMaxSize);
    instance.move();
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of reverseDir method, of class Being.
   */
  @Test @Disabled
  public void testReverseDir() {
    System.out.println("reverseDir");
    Being instance = new Being(DefaultMaxSize);
//    instance.reverseDir();
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of getSize method, of class Being.
   */
  @Test @Disabled
  public void testGetSize() {
    System.out.println("getSize");
    Being instance = new Being(DefaultMaxSize);
    int expResult = 0;
    int result = instance.getSize();
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of getX method, of class Being.
   */
  @Test @Disabled
  public void testGetX() {
    System.out.println("getX");
    Being instance = new Being(DefaultMaxSize);
    int expResult = 0;
    int result = instance.getX();
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of getY method, of class Being.
   */
  @Test @Disabled
  public void testGetY() {
    System.out.println("getY");
    Being instance = new Being(DefaultMaxSize);
    int expResult = 0;
    int result = instance.getY();
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of getColor method, of class Being.
   */
  @Test @Disabled
  public void testGetColor() {
    System.out.println("getColor");
    Being instance = new Being(DefaultMaxSize);
    int expResult = 0;
    int result = instance.getColor();
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of getDx method, of class Being.
   */
  @Test @Disabled
  public void testGetDx() {
    System.out.println("getDx");
    Being instance = new Being(DefaultMaxSize);
    int expResult = 0;
    int result = instance.getDx();
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of getDy method, of class Being.
   */
  @Test @Disabled
  public void testGetDy() {
    System.out.println("getDy");
    Being instance = new Being(DefaultMaxSize);
    int expResult = 0;
    int result = instance.getDy();
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of getDelta method, of class Being.
   */
  @Test @Disabled
  public void testGetDelta() {
    System.out.println("getDelta");
    Being instance = new Being(DefaultMaxSize);
    int[] expResult = null;
    int[] result = instance.getDelta();
    assertArrayEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of setSize method, of class Being.
   */
  @Test @Disabled
  public void testSetSize() {
    System.out.println("setSize");
    int size = 0;
    Being instance = new Being(DefaultMaxSize);
    instance.setSize(size);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of setX method, of class Being.
   */
  @Test @Disabled
  public void testSetX() {
    System.out.println("setX");
    int x = 0;
    Being instance = new Being(DefaultMaxSize);
    instance.setX(x);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of setY method, of class Being.
   */
  @Test @Disabled
  public void testSetY() {
    System.out.println("setY");
    int y = 0;
    Being instance = new Being(DefaultMaxSize);
    instance.setY(y);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of setColor method, of class Being.
   */
  @Test @Disabled
  public void testSetColor() {
    System.out.println("setColor");
    int color = 0;
    Being instance = new Being(DefaultMaxSize);
    instance.setColor(color);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of setDx method, of class Being.
   */
  @Test @Disabled
  public void testSetDx() {
    System.out.println("setDx");
    int dx = 0;
    Being instance = new Being(DefaultMaxSize);
    instance.setDx(dx);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of setDy method, of class Being.
   */
  @Test @Disabled
  public void testSetDy() {
    System.out.println("setDy");
    int dy = 0;
    Being instance = new Being(DefaultMaxSize);
    instance.setDy(dy);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of setDelta method, of class Being.
   */
  @Test @Disabled
  public void testSetDelta() {
    System.out.println("setDelta");
    int[] delta = null;
    Being instance = new Being(DefaultMaxSize);
    instance.setDelta(delta);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

}
