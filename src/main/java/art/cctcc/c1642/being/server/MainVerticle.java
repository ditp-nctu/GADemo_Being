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

import static art.cctcc.c1642.being.Constants.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import java.util.HashMap;
import java.util.Map;

public class MainVerticle extends AbstractVerticle {

  static final Logger logger = Logger.getGlobal();

  int port = 8001;

  @Override
  public void start(Promise<Void> startPromise) throws Exception {

    var router = Router.router(vertx);
    router.get("/being/:session").handler(this::_beingGA);
    vertx.createHttpServer()
            .requestHandler(router)
            .listen(port);
    logger.log(Level.INFO, " Server started on port {0}", port);
  }

  public void _beingGA(RoutingContext ctx) {

    var queryParams = ctx.queryParams();
    var msg = "ok";
    float width = BrowserScreenWidth, height = BrowserScreenHeight;
    try {
      width = Float.parseFloat(queryParams.get("width"));
      height = Float.parseFloat(queryParams.get("height"));
    } catch (NumberFormatException e) {
      msg = "Invalid query, using defaults.";
      logger.log(Level.INFO, " {0}", msg);
    }
    var session_id = queryParams.get("session");
    var query = ctx.request().query();

    BeingDemoGAServerThread thread = null;
    if (BeingDemoGAServerThread.sessions.containsKey(session_id)) {
      thread = BeingDemoGAServerThread.sessions.get(session_id);
    } else {
      thread = new BeingDemoGAServerThread(session_id,
              DefaultPopulationSize,
              DefaultMutationRate, DefaultCrossoverRate,
              width, height);
    }
    var response = thread.getResponse(query, msg);
    logger.log(Level.INFO, " response = {0}", response);
    ctx.response()
            .putHeader("content-type", "application/json")
            .end(response.toString());
  }
}
