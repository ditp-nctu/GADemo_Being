/*
 * Copyright 2020 Jonathan Chang, Chun-yien <ccy@musicapoetica.org>.
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
package art.cctcc.c1642.being.server;

import processing.core.PApplet;
import processing.data.JSONObject;

/**
 *
 * @author Jonathan Chang, Chun-yien <ccy@musicapoetica.org>
 */
public class Response {

  static PApplet p = new PApplet();
  public final JSONObject jo;

  public Response(BeingDemoGAServerThread being, String query, String msg) {

    this.jo = new JSONObject()
            .put("query", query)
            //TODO
            .put("message", msg);
  }

  @Override
  public String toString() {

    return jo.toString();
  }
}
