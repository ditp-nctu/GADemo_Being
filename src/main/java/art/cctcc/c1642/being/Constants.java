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

import java.util.Random;

/**
 *
 * @author Jonathan Chang, Chun-yien <ccy@musicapoetica.org>
 */
public class Constants {

  public static Random r = new Random();

  public static final float UHDScreenWidth = 3840;
  public static final float UHDScreenHeight = 2160;
  public static final float BrowserScreenWidth = 1024;
  public static final float BrowserScreenHeight = 768;

  public static final int DefaultPopulationSize = 200;
  public static final double DefaultMutationRate = 0.95;
  public static final double DefaultCrossoverRate = 0.95;
  public static int max_ring = 25;
  public static int min_ring = 10;
  public static int min_size = 32;
  public static int max_size = 256;
  public static int chromosomeLength = 8 + 8 * (max_ring - 1);
}
