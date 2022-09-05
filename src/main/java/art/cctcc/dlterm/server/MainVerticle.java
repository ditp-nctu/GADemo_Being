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
package art.cctcc.dlterm.server;

import static art.cctcc.c1642.being.Constants.*;
import art.cctcc.c1642.being.ex.InvalidEvalSizeException;
import java.util.logging.Level;
import java.util.logging.Logger;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MainVerticle extends AbstractVerticle {

  private static final Logger LOGGER = Logger.getGlobal();
  private static final Map<String, DLTermGAServerThread> SESSIONS = new HashMap<>();
  private static final int PORT = 8001;

  @Override
  public void start(Promise<Void> startPromise) throws Exception {

    var router = Router.router(vertx);
    router.route().handler(CorsHandler.create("*")
            .allowedMethod(io.vertx.core.http.HttpMethod.GET)
            .allowedHeader("Access-Control-Allow-Headers")
            .allowedHeader("Content-Type")
    );
    router.post("/dl/:session_id")
            .handler(BodyHandler.create())
            .handler(this::_DLGA);
    vertx.createHttpServer()
            .requestHandler(router)
            .listen(PORT);
    LOGGER.log(Level.INFO, " Server started on port {0}", PORT);
  }

  public void _DLGA(RoutingContext ctx) {

    var msg = "ok";
    var session_id = ctx.pathParam("session_id");
    System.out.println("Accepting request: session_id=" + session_id);

    var thread = SESSIONS.get(session_id);
    JsonObject eval = null;
    try {
      var content = ctx.body().asJsonObject();
      if (Objects.isNull(thread)) {
        var latent_size = content.getInteger("latent_size");
        var population_size = content.getInteger("population_size", DefaultPopulationSize);
        var mutation_rate = content.getDouble("mutation_rate", DefaultMutationRate);
        var crossover_rate = content.getDouble("crossover_rate", DefaultCrossoverRate);
        var elitism_count = content.getInteger("elitism_count", population_size / 3);

        LOGGER.log(Level.INFO, "Creating new GA session.");
        LOGGER.log(Level.INFO, "latent_size = {0}", latent_size);
        LOGGER.log(Level.INFO, "population_size = {0}", population_size);
        LOGGER.log(Level.INFO, "mutation_rate = {0}", mutation_rate);
        LOGGER.log(Level.INFO, "crossover_rate = {0}", crossover_rate);
        LOGGER.log(Level.INFO, "elitism_count = {0}", elitism_count);

        thread = new DLTermGAServerThread(session_id, population_size,
                mutation_rate, crossover_rate, latent_size, elitism_count);
        SESSIONS.put(session_id, thread);
      } else {
        eval = content.getJsonObject("eval", null);
        if (Objects.nonNull(eval)) {
          if (eval.size() < thread.getGa().getPopulationSize())
            throw new InvalidEvalSizeException(thread.getPopulation().size(), eval.size());
          LOGGER.log(Level.INFO, "Eval size = {0}", eval.size());
        }
      }
    } catch (Exception ex) {
      msg = ex.getMessage();
      ex.printStackTrace();
      LOGGER.log(Level.WARNING, "Exception: {0}", msg);
    }
    var response = thread.getResponse(ctx.request().query(), msg, eval);
    LOGGER.log(Level.INFO, " response session_id = {0}", thread.getSession_id());
    if (thread.isTerminated()) {
      LOGGER.log(Level.INFO, " session terminated, session_id = {0}", thread.getSession_id());
      SESSIONS.remove(thread.getSession_id());
    }
    ctx.response()
            .putHeader("content-type", "application/json")
            .end(Buffer.buffer(response.toString()));
  }
}
