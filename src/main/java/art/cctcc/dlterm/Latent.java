package art.cctcc.dlterm;

import art.cctcc.c1642.being.ex.UnexpectedGeneticCode;
import ga.chapter2.Individual;
import java.util.Arrays;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Latent extends Individual {

  static final int CODE_LEN = 62;
  private final UUID id;

  public Latent(UUID id, int chromosomeLength) {

    super(new int[chromosomeLength]);
    this.id = id;
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

  public Latent(int chromosomeLength) {

    super(chromosomeLength);
    this.id = UUID.randomUUID();
  }

  public void encodeGenes(double... latent_code) {

    var chromosome = Arrays.stream(latent_code)
            .mapToObj(code -> Long.toBinaryString(Double.doubleToLongBits(code)))
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

    return IntStream.range(0, this.getChromosomeLength() / CODE_LEN)
            .mapToObj(i -> IntStream.range(i * CODE_LEN, (i + 1) * CODE_LEN).mapToObj(j -> String.valueOf(this.getGene(j))).collect(Collectors.joining()))
            .mapToDouble(s -> Double.longBitsToDouble(Long.parseLong(s, 2)))
            .toArray();
  }

  public String getInfo() {

    var info = String.format("%6.3f", this.getFitness() * 100);
    return info;
  }
}
