package art.cctcc.dlterm;

import static art.cctcc.c1642.being.Constants.*;
import art.cctcc.c1642.being.ex.UnexpectedGeneticCode;
import ga.chapter2.Individual;
import java.math.BigInteger;
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

  public Latent(UUID id, int chromosomeLength) {

    super(new int[chromosomeLength]);
    this.id = id;
  }

  public Latent(Latent original) {

    super(original.getChromosomeLength());
    this.id = UUID.randomUUID();
    //TODO
  }

  public Latent(int chromosomeLength) {

    super(chromosomeLength);
    this.id = UUID.randomUUID();
  }

  public void encodeGenes() {

    var chromosome = ""; //TODO
    for (var i = 0; i < DefaultChromosomeLength; i++) {
      switch (chromosome.toCharArray()[i]) {
        case '1' -> setGene(i, 1);
        case '0' -> setGene(i, 0);
        default -> throw new UnexpectedGeneticCode(chromosome + " at " + i);
      }
    }
  }

  public void decodeGenes() {

    var chromosome = IntStream.range(0, DefaultChromosomeLength)
            .mapToObj(this::getGene)
            .map(String::valueOf)
            .collect(Collectors.joining());
  }

  public String getInfo() {

    var info = String.format("%6.3f", this.getFitness() * 100);
    return info;
  }
}
