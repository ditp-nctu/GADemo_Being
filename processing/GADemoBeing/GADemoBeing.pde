int beingNo = 100;
Being[] beings = new Being[beingNo];

//void settings() {
//  size(canvas, canvas);
//}

void setup() {
  fullScreen();
  rectMode(CENTER);
  stroke(255);
  noFill();
  for (int i=0; i<beingNo; i++) {
    float size = random(64, 255);
    beings[i] = new Being(size, 
      random(width-size*2)+size, random(height-size*2)+size, 
      random(255));
  }
}

void draw() {
  background(128);
  for (int i=0; i<beingNo; i++) {
    Being b = beings[i];
    stroke(b.c);
    //System.out.printf("size=%.2f, x=%.2f, y=%.2f\n", b.size, b.x, b.y);
    pushMatrix();
    translate(b.x, b.y);
    float size=b.size;
    //float score = 0;
    int ring = 0;
    for (int j=0; j<Being.max_ring; j++) {    
      ring++;
      //System.out.printf("Ring#%d: size=%.2f ", j, size);
      //float scoreDelta = 0;      
      if (j%2==0) {
        circle(0, 0, size);
        //if (j>0) {
        //  scoreDelta=delta[j-1];
        //}
      } else {
        rotate(PI/(float)b.delta[j-1]);
        rect(0, 0, size, size);
        //scoreDelta=abs(delta[j-1]-size*(sqrt(2)-1));
      }
      //System.out.printf("scoreDelta=%.2f ", scoreDelta);
      //score+=scoreDelta;
      //delta[j] = random(0, size/max_ring*2);
      //delta[j] = j%2==0?size*(1-1/sqrt(2)):0;
      size-=b.delta[j];
      if (size < 0) break;
      //System.out.printf("delta = %.2f\n", delta[j]);
    }
    popMatrix();
    b.move();
    if (b.x+b.size/2 > width || b.x-b.size/2 < 0 || 
      b.y+b.size/2 > height || b.y-b.size/2 < 0) b.reverseDir();
    //System.out.printf(": ring=%d, %.2f(%.2f%%)\n", 
    //  ring, score/(ring-1), 1.0/(score/(ring-1)+1.0)*100);
  }
}
