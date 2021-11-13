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
  public static int min_size = 32;
  public static int max_size = 256;
  public static Random r = new Random();
  public static int chromosomeLength;

  private int size;
  private int x;
  private int y;
  private int color;
  private int dx;
  private int dy;
  private int ring;
  private int[] delta;
  private boolean clockwise;

  public Being(int x, int y, int dx, int dy, int color, boolean clockwise) {

    super(new int[chromosomeLength = 8 + 8 * (max_ring - 1)]);
    this.x = x;
    this.y = y;
    this.dx = dx;
    this.dy = dy;
    this.color = color;
    this.clockwise = clockwise;
    this.delta = new int[max_ring - 1];
  }

  public Being(Being original) {

    this(original.getX(), original.getY(), original.getDx(), original.getDy(),
            original.getColor(), original.isClockwise());
  }

  public Being() {

    super(chromosomeLength = 8 + 8 * (max_ring - 1));
    this.size = r.nextInt(max_size - min_size) + min_size;
    this.color = r.nextInt(256);
    int current_size = this.size;
    delta = new int[max_ring - 1];
    for (int j = 0; j < max_ring - 1; j++) {
      delta[j] = r.nextInt(current_size / (max_ring - this.ring++)) + 1;
      current_size -= delta[j];
    }
    this.refreshRing();
    this.clockwise = r.nextDouble() > 0.5;
    this.changeDir(0);
  }

  public int refreshRing() {

    this.ring = 1;
    float current_size = this.size;
    for (int i = 0; i < max_ring - 1; i++) {
      current_size -= delta[i];
      if (current_size <= min_size / 2.0) {
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

    changeDir(0.98);
    this.x += dx;
    this.y += dy;
  }

  private void changeDir(double rate) {

    if (r.nextDouble() > rate) {
      this.dx = (r.nextInt(2) + 1) * (r.nextInt(3) - 1);
      this.dy = (r.nextInt(2) + 1) * (r.nextInt(3) - 1);
    }
  }

  public void reverseDir(String direction) {

    switch (direction) {
      case "x" -> {
        this.dx = -this.dx;
      }
      case "y" -> {
        this.dy = -this.dy;
      }
    }
    move();
  }

  public String getInfo() {

    var info = String.format("%6.3f %4d %3d {%s}",
            this.getFitness() * 100, this.getSize(), this.getRing(),
            Arrays.stream(delta).mapToObj(String::valueOf).collect(Collectors.joining(", ")));
    return info;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 19 * hash + this.size;
    return hash;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final Being other = (Being) obj;
    if (this.size != other.size) {
      return false;
    }
    return true;
  }

}
