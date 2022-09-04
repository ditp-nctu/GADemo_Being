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
package art.cctcc.c1642.being.ex;

/**
 *
 * @author Jonathan Chang, Chun-yien <ccy@musicapoetica.org>
 */
public class InvalidEvalSizeException extends RuntimeException {

  /**
   * Creates a new instance of <code>InvalidEvalSizeException</code> without
   * detail message.
   */
  public InvalidEvalSizeException() {
  }

  /**
   * Constructs an instance of <code>InvalidEvalSizeException</code> with the
   * specified detail message.
   *
   * @param msg the detail message.
   */
  public InvalidEvalSizeException(String msg) {
    
    super(msg);
  }

  public InvalidEvalSizeException(int expected_size, int size) {
    
    super(String.format("Expected %d evals, but get %d.", expected_size, size));
  }
}
