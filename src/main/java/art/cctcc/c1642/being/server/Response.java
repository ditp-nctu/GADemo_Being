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
import processing.data.JSONArray;
import processing.data.JSONObject;

/**
 *
 * @author Jonathan Chang, Chun-yien <ccy@musicapoetica.org>
 */
public class Response {

  static PApplet p = new PApplet();
  public final JSONObject jo;

  public Response(BeingDemoGAServerThread session, String query, String msg) {

    var population = new JSONArray();
    for (int i = 0; i < session.getGa().getElitismCount(); i++) {
      var being = session.getPopulation().getFittest(i);
      var joBeing = new JSONObject()
              .put("id", being.getId())
              .put("color", being.getColor())
              .put("size", being.getSize())
              .put("ring", being.getRing())
              .put("delta", being.getDelta())
              .put("clockwise", being.isClockwise())
              .put("qualified", session.getGa().beingQualifier.test(being));
      population.append(joBeing);
    }
    this.jo = new JSONObject()
            .put("session_id", session.getSession_id())
            .put("query", query)
            .put("terminated", session.isTerminated())
            .put("generation", session.getGeneration())
            .put("population", population)
            .put("qualifiedCount", session.getQualifiedCount())
            .put("message", msg);
  }

  @Override
  public String toString() {

    return jo.toString();
  }
}
