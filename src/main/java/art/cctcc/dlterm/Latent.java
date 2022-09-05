package art.cctcc.dlterm;

import ga.real.Individual;
import java.util.Arrays;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Latent extends Individual {

  private final UUID id;
  private boolean elite;

  public Latent(UUID id, int chromosomeLength) {

    super(new double[chromosomeLength]);
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

    assert latent_code.length == this.getChromosomeLength();
    for (var i = 0; i < this.getChromosomeLength(); i++) {
      this.setGene(i, latent_code[i]);
    }
  }

  public double[] decodeGenes() {

    return this.getChromosome();
  }

  public String getChromosomeCompact() {

    return Arrays.stream(this.getChromosome())
            .mapToObj(gene -> String.valueOf((int) Math.log10(gene)))
            .collect(Collectors.joining(","));
  }

  public String getInfo() {

    var info = String.format("[%s] (%s)",
            this.getChromosomeCompact().substring(0, 20),
            this.getId());
    return info;
  }
}
