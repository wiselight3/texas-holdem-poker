import enums.PhaseType;
import enums.PlayerType;
import junit.framework.Assert;
import junit.framework.TestCase;

/**
 * Created by IntelliJ IDEA.
 * User: oye
 * Date: 27.09.11
 * Time: 11:35
 * To change this template use File  Settings | File Templates.
 */
public class PLayerTests extends TestCase {

    public void test_get_id_returns_int () {
       Player player = new Player(0, PlayerType.BLUFFER, PhaseType.PHASE1PLAYER );
        assertEquals(0, player.getId());

    }

}