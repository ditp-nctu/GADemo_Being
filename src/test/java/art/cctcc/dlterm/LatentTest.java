/*
 * Copyright 2022 Jonathan Chang, Chun-yien <ccy@musicapoetica.org>.
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

import static art.cctcc.dlterm.Latent.CODE_LEN;
import java.util.Arrays;
import java.util.UUID;
import java.util.stream.DoubleStream;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Jonathan Chang, Chun-yien <ccy@musicapoetica.org>
 */
public class LatentTest {

  public LatentTest() {
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
   * Test of encodeGenes method, of class Latent.
   */
  @Test @Disabled
  public void testEncodeGenes() {
    System.out.println("encodeGenes");
    double[] latent_code = null;
    Latent instance = null;
    instance.encodeGenes(latent_code);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of decodeGenes method, of class Latent.
   */
  @Test
  public void testDecodeGenes() {
    System.out.println("decodeGenes");
    Latent instance = new Latent(CODE_LEN * 10);
    double[] expResult = DoubleStream.generate(Math::random).limit(10).toArray();
    instance.encodeGenes(expResult);
    double[] result = instance.decodeGenes();
    assertArrayEquals(expResult, result);
    System.out.println(instance.getInfo());
  }

  /**
   * Test of getInfo method, of class Latent.
   */
  @Test @Disabled
  public void testGetInfo() {
    System.out.println("getInfo");
    Latent instance = null;
    String expResult = "";
    String result = instance.getInfo();
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of getId method, of class Latent.
   */
  @Test @Disabled
  public void testGetId() {
    System.out.println("getId");
    Latent instance = null;
    UUID expResult = null;
    UUID result = instance.getId();
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

}