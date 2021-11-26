import java.util.Random;

public class Being {
  public static boolean balanced;
  public static int max_ring=20;
  public static Random r = new Random();
  public float size;
  int[] delta = new int[max_ring];

  Being(float size) {
    this.size = size;    
    int ring = 0;
    float current_size=this.size;
    for (int j=0; j<max_ring-1; j++) {
      ring++;
      delta[j] = (int)(balanced?
        (j%2==0 ? current_size*(1-1.0/Math.sqrt(2)):0):
        (Math.random()*(current_size/(max_ring-ring)) + 1));
      current_size-=delta[j];
    }
  }
}
