import java.util.Random;

public class Being {
  public static boolean balanced;
  public static int MAX_RING=20;
  public static Random r = new Random();
  public float size;
  int[] delta = new int[MAX_RING];

  Being(float size) {
    this.size = size;        
    float current_size=this.size;
    for (int j=0; j<MAX_RING-1; j++) {
      delta[j] = (int)(balanced?
        (j%2==0 ? current_size*(1-1.0/Math.sqrt(2)):0):
        (Math.random()*(current_size/(MAX_RING-1-j)) + 1));
      current_size-=delta[j];
    }
  }
}
