package art.cctcc.c1642.being;

import ga.chapter2.Individual;
import java.util.Random;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Being extends Individual {

  public static int max_ring = 20;
  public static Random r = new Random();
  public static int chromosomeLength = 10;

  private int size, x, y, color, dx, dy;
  private double[] delta = new double[max_ring];
  // int size, delta[max_ring], color

  public Being() {

    super(chromosomeLength);
    this.size = r.nextInt(256 - 64) + 64;
    int ring = 0;
    float current_size = this.size;
    for (int j = 0; j < max_ring; j++) {
      ring++;
      delta[j] = (Math.random() * (current_size / (max_ring - ring)));
      current_size -= delta[j];
    }
    changeDir(0);
  }

  public void move() {
    changeDir(0.99);
    this.x += dx;
    this.y += dy;
  }

  public void changeDir(double rate) {
    if (Math.random() > rate) {
      this.dx = (r.nextInt(2) + 1) * (r.nextInt(3) - 1);
      this.dy = (r.nextInt(2) + 1) * (r.nextInt(3) - 1);
    }
  }

  public void reverseDir() {
    this.dx = -this.dx;
    this.dy = -this.dy;
  }
}
