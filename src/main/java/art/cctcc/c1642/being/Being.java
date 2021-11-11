package art.cctcc.c1642.being;

import art.cctcc.c1642.being.ex.UnexpectedGeneticCode;
import ga.chapter2.Individual;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Being extends Individual {

  public static int max_ring = 25;
  public static int min_size = 64;
  public static int max_size = 256;
  public static Random r = new Random();
  public static int chromosomeLength = 8 + 8 * (max_ring - 1);

  private int size;
  private int x;
  private int y;
  private int color;
  private int dx;
  private int dy;
  private int ring;
  private int[] delta = new int[max_ring - 1];
  private boolean clockwise;
  // int size, delta[max_ring]

  public Being(int x, int y, int dx, int dy, int color, boolean clockwise) {

    super(new int[chromosomeLength]);
    this.x = x;
    this.y = y;
    this.dx = dx;
    this.dy = dy;
    this.color = color;
    this.clockwise = clockwise;
  }

  public Being() {

    super(chromosomeLength);
    this.size = r.nextInt(max_size - min_size) + min_size;
    this.color = r.nextInt(256);
    float current_size = this.size;
    this.ring = 0;
    for (int j = 0; j < max_ring - 1; j++) {
      delta[j] = (int) (Math.random() * (current_size / (max_ring - this.ring++))) + 1;
      current_size -= delta[j];
      if (current_size <= 0) {
        break;
      }
    }
    this.clockwise = Math.random() > 0.5;
//    System.out.printf("Ring=%d, delta=%s\n", ring, Arrays.toString(delta));
    this.changeDir(0);
  }

  public int refreshRing() {

    this.ring = 1;
    float current_size = this.size;
    for (int i = 0; i < max_ring - 1; i++) {
      current_size -= delta[i];
      if (current_size <= 0) {
        break;
      }
      ring++;
    }
    return this.ring;
  }

  public String getSizeGene() {

    return String.format("%08d", new BigInteger(Integer.toBinaryString(this.size)));
  }

  public String getDeltaGene() {

    return Arrays.stream(this.delta)
            .mapToObj(d -> String.format("%08d", new BigInteger(Integer.toBinaryString(d))))
            .collect(Collectors.joining());
  }

  public void encodeGenes() {

    var chromosome = this.getSizeGene() + this.getDeltaGene();
    for (var i = 0; i < chromosomeLength; i++) {
      switch (chromosome.toCharArray()[i]) {
        case '1' -> setGene(i, 1);
        case '0' -> setGene(i, 0);
        default -> throw new UnexpectedGeneticCode(chromosome + " at " + i);
      }
    }
  }

  public void decodeGenes() {

    var chromosome = IntStream.range(0, chromosomeLength)
            .mapToObj(this::getGene)
            .map(String::valueOf)
            .collect(Collectors.joining());
    this.size = Integer.parseInt(chromosome.substring(0, 8), 2);
    for (int i = 0; i < delta.length; i++) {
      delta[i] = Integer.parseInt(chromosome.substring(8 + i * 8, 8 + i * 8 + 8), 2);
    }
    this.refreshRing();
  }

  public void move() {

    changeDir(0.99);
    this.x += dx;
    this.y += dy;
  }

  private void changeDir(double rate) {

    if (Math.random() > rate) {
      this.dx = (r.nextInt(2) + 1) * (r.nextInt(3) - 1);
      this.dy = (r.nextInt(2) + 1) * (r.nextInt(3) - 1);
      this.color = r.nextInt(256);
    }
  }

  public void reverseDir(String direction) {

    switch (direction) {
      case "x" ->
        this.dx = -this.dx;
      case "y" ->
        this.dy = -this.dy;
    }
  }
}
