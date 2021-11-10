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
    stroke(255);
    noFill();
    for (let i = 0; i < beingNo; i++) {
        var size = random(64, 127);
        beings[i] = new Being(size, random(width - size * 2) + size, random(height - size * 2) + size, random(255));
    }
}

function draw() {
    background(128);
    for (let i = 0; i < beingNo; i++) {
        beings[i].draw();
    }
}

class Being {
    constructor(size, x, y, c) {
        this.delta = [];
        this.size = size;
        this.x = x;
        this.y = y;
        this.c = c;
        let current_size = this.size;
        for (let i = 0; i < max_ring; i++) {
            this.delta[i] = (random(current_size / (max_ring - i)));
            current_size -= this.delta[i];
        }
        this.changeDir(0);
    }

    move() {
        this.changeDir(0.99);
        this.x += this.dx;
        this.y += this.dy;
    }

    changeDir(rate) {
        if (random(1) > rate) {
            this.dx = (random(2) + 1) * (random(3) - 1);
            this.dy = (random(2) + 1) * (random(3) - 1);
        }
    }

    reverseDir(dir) {
        switch (dir) {
            case "x":
                this.dx = -this.dx;
                break;
            case "y":
                this.dy = -this.dy;
                break;
        }
    }

    draw() {
        stroke(this.c);
        push();
        translate(this.x, this.y);
        let size = this.size;
        for (let i = 0; i < max_ring; i++) {
            if (i % 2 == 0) {
                circle(0, 0, size);
            } else {
                rotate(PI / this.delta[i - 1]);
                rect(0, 0, size, size);
            }
            size -= this.delta[i];
            if (size < 0)
                break;
        }
        pop();
        this.move();
        if (this.x + this.size / 2 > width || this.x - this.size / 2 < 0)
            this.reverseDir("x");
        if (this.y + this.size / 2 > height || this.y - this.size / 2 < 0)
            this.reverseDir("y");
    }
}