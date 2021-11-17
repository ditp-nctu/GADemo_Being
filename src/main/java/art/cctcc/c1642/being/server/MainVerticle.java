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

import art.cctcc.c1642.being.Constants;
import static art.cctcc.c1642.being.Constants.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.CorsHandler;
import java.util.HashMap;
import java.util.Map;

public class MainVerticle extends AbstractVerticle {

  static final Logger logger = Logger.getGlobal();
  static final Map<String, BeingDemoGAServerThread> sessions = new HashMap<>();
  int port = 8001;

  @Override
  public void start(Promise<Void> startPromise) throws Exception {

    var router = Router.router(vertx);
    router.route().handler(CorsHandler.create("*")
            .allowedMethod(io.vertx.core.http.HttpMethod.GET)
            .allowedHeader("Access-Control-Allow-Headers")
            .allowedHeader("Content-Type"));
    router.get("/being/:session_id")
            .handler(this::_beingGA);
    vertx.createHttpServer()
            .requestHandler(router)
            .listen(port);
    logger.log(Level.INFO, " Server started on port {0}", port);
  }

  public void _beingGA(RoutingContext ctx) {

    var msg = "ok";
    var session_id = ctx.pathParam("session_id");
    System.out.println("Accepting request: session_id=" + session_id);
    var queryParams = ctx.queryParams();
    var max_size = queryParams.contains("max_size")
            ? Integer.parseInt(queryParams.get("max_size"))
            : Constants.DefaultMaxSize;
    BeingDemoGAServerThread thread = null;
    if (sessions.containsKey(session_id)) {
      thread = sessions.get(session_id);
    } else {
      thread = new BeingDemoGAServerThread(session_id, DefaultPopulationSize,
              DefaultMutationRate, DefaultCrossoverRate, max_size);
      sessions.put(session_id, thread);
    }
    var response = thread.getResponse(ctx.request().query(), msg);
    logger.log(Level.INFO, " response session_id = {0}", thread.getSession_id());
    ctx.response()
            .putHeader("content-type", "application/json")
            .end(response.toString());
  }
}
