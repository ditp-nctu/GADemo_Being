import java.util.Arrays;

int canvas = 400;

void settings() {
  size(canvas, canvas);
}

void setup() {
  rectMode(CENTER);
  stroke(255);
  noFill();
  frameRate(0.5);
  Being.balanced = false;
}

void draw() {
  background(0);
  Being.balanced = !Being.balanced;
  Being b = new Being(random(64, 255));  
  translate(canvas/2, canvas/2);
  float size = b.size;
  int ring = 0;

  for (int j=0; j<Being.max_ring; j++) {    
    ring++;      
    if (j%2==0) {
      circle(0, 0, size);
    } else {
      rotate(PI/(float)b.delta[j-1]);
      rect(0, 0, size, size);
    }
    size-=b.delta[j];
    if (size < 0) break;
  }
  System.out.printf("Size=%.2f, Ring=%d, delta=%s\n", 
    b.size, ring, Arrays.toString(b.delta));
}
