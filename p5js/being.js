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



class Being {
    constructor(size, x, y, c) {
        this.delta = [];
        this.size = size;
        this.x = x;
        this.y = y;
        this.c = c;
        let current_size = this.size;
        for (let i = 0; i < max_ring; i++) {
            this.delta[i] = random(1, current_size / (max_ring - i));
            current_size -= this.delta[i];
        }
        this.changeDir(0);
    }

    move() {
        this.changeDir(0.95);
        this.x += this.dx;
        this.y += this.dy;
    }

    changeDir(rate) {
        if (random(1) > rate) {
            this.dx = random(-2, 2);
            this.dy = random(-2, 2);
        }
        //console.log(this.dx + ', ' + this.dy);
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
        this.move();
    }

}
