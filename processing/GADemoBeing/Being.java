import java.util.Random;

public class Being {
  public static int max_ring=20;
  public static Random r = new Random();
  public float size, x, y, c, dx, dy;
  int[] delta = new int[max_ring];

  Being(float size, float x, float y, float c) {
    this.size = size;
    this.x = x;
    this.y = y;
    this.c = c;
    int ring = 0;
    float current_size=this.size;
    for (int j=0; j<max_ring-1; j++) {
      ring++;
      delta[j] = (int)(Math.random()*(current_size/(max_ring-ring)))+1;
      current_size-=delta[j];
    }
    changeDir(0);
  }

  public void move() {
    changeDir(0.99);
    this.x+=dx;
    this.y+=dy;
  }

  public void changeDir(double rate) {
    if (Math.random() > rate) {
      this.dx=(r.nextInt(2)+1)*(r.nextInt(3)-1);
      this.dy=(r.nextInt(2)+1)*(r.nextInt(3)-1);
    }
  }

  public void reverseDir() {
    this.dx = -this.dx;
    this.dy = -this.dy;
    move();
  }

  public byte[] encode() {
    return null;
  }

  public void decode(byte[] chromosome) {
  }
}
