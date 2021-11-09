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
package art.cctcc.c1642.being;

import processing.core.PApplet;

/**
 *
 * @author Jonathan Chang, Chun-yien <ccy@cctcc.art>
 */
public class GADemoBeing extends PApplet {

  int beingNo;
  Being[] beings;

  @Override
  public void settings() {
//  size(canvas, canvas);
    fullScreen();
  }

  @Override
  public void setup() {
    rectMode(CENTER);
    stroke(255);
    noFill();
    beingNo = 100 * width / 3840;
    beings = new Being[beingNo];
    for (int i = 0; i < beingNo; i++) {
      beings[i] = new Being();
      var size = beings[i].getSize();
      beings[i].setX((int) random(width - size * 2) + size);
      beings[i].setY((int) random(height - size * 2) + size);
      beings[i].setColor((int) random(255));
    }
  }

  @Override
  public void draw() {
    background(128);
    for (int i = 0; i < beingNo; i++) {
      Being b = beings[i];
      stroke(b.getColor());
      //System.out.printf("size=%.2f, x=%.2f, y=%.2f\n", b.size, b.x, b.y);
      pushMatrix();
      translate(b.getX(), b.getY());
      float size = b.getSize();
      //float score = 0;
      int ring = 0;
      for (int j = 0; j < Being.max_ring; j++) {
        ring++;
        //System.out.printf("Ring#%d: size=%.2f ", j, size);
        //float scoreDelta = 0;      
        if (j % 2 == 0) {
          circle(0, 0, size);
          //if (j>0) {
          //  scoreDelta=delta[j-1];
          //}
        } else {
          rotate(PI / (float) b.getDelta()[j - 1]);
          rect(0, 0, size, size);
          //scoreDelta=abs(delta[j-1]-size*(sqrt(2)-1));
        }
        //System.out.printf("scoreDelta=%.2f ", scoreDelta);
        //score+=scoreDelta;
        //delta[j] = random(0, size/max_ring*2);
        //delta[j] = j%2==0?size*(1-1/sqrt(2)):0;
        size -= b.getDelta()[j];
        if (size < 0) {
          break;
        }
        //System.out.printf("delta = %.2f\n", delta[j]);
      }
      popMatrix();
      b.move();
      if (b.getX() + b.getSize() / 2 > width || b.getX() - b.getSize() / 2 < 0
              || b.getY() + b.getSize() / 2 > height || b.getY() - b.getSize() / 2 < 0) {
        b.reverseDir();
      }
      //System.out.printf(": ring=%d, %.2f(%.2f%%)\n", 
      //  ring, score/(ring-1), 1.0/(score/(ring-1)+1.0)*100);
    }
  }

  public static void main(String[] args) {
    System.setProperty("sun.java2d.uiScale", "1.0");
    PApplet.main(GADemoBeing.class);
  }
}
