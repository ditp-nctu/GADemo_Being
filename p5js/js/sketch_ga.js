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

var UHDScreenWidth = 3840;
var UHDScreenHeight = 2160;
//modify when deployed to server
var url = "http://" + (location.host || "localhost") + ":8001/being/";
//var url = "http://172.104.72.71:8001/being/";
var text_size
var beings;
var terminated;
var max_size;
var timer;
var bg = 100;
var inc = 1;
var generation;
var qualifiedCount;
var elitismCount;


async function fetchBeings(url, callback) {

  var response = await fetch(url);
  response.text()
          .then(text => callback(JSON.parse(text)));
}

function setup() {
  
  createCanvas(windowWidth, windowHeight);
  frameRate(30);
  rectMode(CENTER);
  strokeWeight(1);
  var ratio = width > height ?
          1.0 * width / UHDScreenHeight : 1.0 * height / UHDScreenHeight;
  text_size = 50 * ratio;
  beings = new Map();
  terminated = false;
  max_size = (255 * ratio) | 0;
  timer = 0;
  url += uuidv4() + "?max_size=" + max_size;
}

function draw() {
  if (beings.size > 0) ++timer;
  if (!terminated && (beings.size == 0 || generation > 0 || timer >= frameRate() * 3)) {
    if (timer >= frameRate() / 2) {
      timer = 0;
      var newBeings = new Map();
      fetchBeings(url, data => {
        console.log('generation#', data.generation);
        data.population.forEach(b => {
          var being;
          if (!beings.get(b.id)) {
            var x = random(width - b.size * 2) + b.size;
            var y = random(height - b.size * 2) + b.size;
            being = new Being(b.size, x, y, b.color, b.delta, b.ring,
                    b.clockwise, b.qualified);
          } else {
            being = beings.get(b.id);
            being.color = b.color;
            being.delta = b.delta;
            being.ring = b.ring;
            being.qualified = b.qualified;
          }
          newBeings.set(b.id, being);
        });
        beings = newBeings;
        generation = data.generation;
        qualifiedCount = data.qualifiedCount;
        elitismCount = data.population.length;
        if (data.terminated)
          terminated = true;
      });
    }
  }
  if (beings.size > 0) {
    if (generation > 0)
      bg += inc;
    background(bg);
    if (bg >= 255 || bg <= 0) {
      inc = -inc;
    }
    beings.forEach(drawBeing);
    if (elitismCount > 0) {
      noStroke();
      fill(bg > 128 ? 0 : 255);
      textSize(text_size);
      text("g=" + generation + ", t=" + qualifiedCount + "/" + elitismCount,
              10, text_size);
    }
  }
}

function drawBeing(being) {
  if (being.qualified) {
    stroke(being.c);
  } else {
    stroke(being.delta[0] * 2, being.c, being.size);
  }
  noFill();
  push();
  translate(being.x, being.y);
  let size = being.size;
  for (let i = 0; i < being.ring; i++) {
    if (i % 2 == 0) {
      circle(0, 0, size);
    } else {
      rotate(random(0.97, 1.03) * PI / being.delta[i - 1] * (being.clockwise ? 1 : -1));
      rect(0, 0, size, size);
    }
    if (i < being.ring - 1)
      size -= being.delta[i];
  }
  pop();
  being.move();
  if (being.x + being.size / 2 > width || being.x - being.size / 2 < 0)
    being.reverseDir("x");
  if (being.y + being.size / 2 > height || being.y - being.size / 2 < 0)
    being.reverseDir("y");
}