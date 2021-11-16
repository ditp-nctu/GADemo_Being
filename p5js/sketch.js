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

var beingNo = 100;
var beings = [];
var max_ring = 20;

function setup() {
    createCanvas(windowWidth, windowHeight);
    rectMode(CENTER);
    strokeWeight(1);
    noFill();
    for (let i = 0; i < beingNo; i++) {
        var size = random(64, 127);
        beings[i] = new Being(size, random(width - size * 2) + size, random(height - size * 2) + size, random(255));
    }
}

function draw() {
    background(128);
    for (let i = 0; i < beingNo; i++) {
        drawBeing(beings[i]);
    }
}


function drawBeing(being) {
    stroke(being.c);
    push();
    translate(being.x, being.y);
    let size = being.size;
    for (let i = 0; i < max_ring; i++) {
        if (i % 2 == 0) {
            circle(0, 0, size);
        } else {
            rotate(PI / being.delta[i - 1]);
            rect(0, 0, size, size);
        }
        size -= being.delta[i];
        if (size < 0)
            break;
    }
    pop();
    being.move();
    if (being.x + being.size / 2 > width || being.x - being.size / 2 < 0)
        being.reverseDir("x");
    if (being.y + being.size / 2 > height || being.y - being.size / 2 < 0)
        being.reverseDir("y");
}