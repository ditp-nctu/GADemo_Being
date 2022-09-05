package art.cctcc.dlterm;

import art.cctcc.c1642.being.ex.UnexpectedGeneticCode;
import ga.chapter2.Individual;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Latent extends Individual {

  static final int CODE_LEN = 64;

  private final UUID id;

  public Latent(UUID id, int chromosomeLength) {

    super(new int[chromosomeLength]);
    this.id = id;

    assert chromosomeLength % CODE_LEN == 0;
  }

  /**
   * Constructor for duplicate the source individual.
   *
   * @param source
   */
  public Latent(Latent source) {

    super(source.getChromosomeLength());
    this.id = UUID.randomUUID();
    IntStream.range(0, this.getChromosomeLength())
            .forEach(i -> this.setGene(i, source.getGene(i)));
  }

  public Latent(int latent_size) {

    super(latent_size * CODE_LEN);
    this.id = UUID.randomUUID();
  }

  public int getLatentLength() {

    return this.getChromosomeLength() / CODE_LEN;
  }

  public void encodeGenes(double... latent_code) {

    assert latent_code.length == this.getLatentLength();
    var chromosome = Arrays.stream(latent_code)
            .mapToLong(Double::doubleToRawLongBits)
            .mapToObj(Long::toBinaryString)
            .map(BigInteger::new)
            .map(s -> String.format("%0" + CODE_LEN + "d", s))
            .collect(Collectors.joining());
    for (var i = 0; i < this.getChromosomeLength(); i++) {
      switch (chromosome.toCharArray()[i]) {
        case '1' -> setGene(i, 1);
        case '0' -> setGene(i, 0);
        default -> throw new UnexpectedGeneticCode(chromosome + " at " + i);
      }
    }
  }

  public double[] decodeGenes() {

    return IntStream.range(0, this.getLatentLength())
            .mapToObj(i -> IntStream.range(i * CODE_LEN, (i + 1) * CODE_LEN).mapToObj(j -> String.valueOf(this.getGene(j))).collect(Collectors.joining()))
            .mapToDouble(s -> Double.longBitsToDouble(Long.parseUnsignedLong(s, 2)))
            .toArray();
  }

  public String getChromosomeCompact() {

    return IntStream.range(0, this.getChromosomeLength() / 4)
            .mapToObj(i -> IntStream.range(i * 4, (i + 1) * 4).mapToObj(j -> String.valueOf(this.getGene(j))).collect(Collectors.joining()))
            .mapToLong(s -> Long.parseUnsignedLong(s, 2))
            .mapToObj(Long::toHexString)
            .collect(Collectors.joining());
  }

  public String getInfo() {

    var info = String.format("[%s] (%s)",
            this.getChromosomeCompact(),
            this.getId());
    return info;
  }
}
