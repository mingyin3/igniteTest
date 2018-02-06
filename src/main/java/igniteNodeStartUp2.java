import org.apache.ignite.IgniteException;
import org.apache.ignite.Ignition;

/**
 * Created by myin2 on 9/6/17.
 */
public class igniteNodeStartUp2 {
    public static void main(String[] args ) throws IgniteException{
        //Ignition.start("/Users/myin2/Downloads/apache-ignite-fabric-2.0.0-bin/examples/config/example-ignite.xml");
        Ignition.start("/Users/myin2/Downloads/igniteTest/src/main/config/persistent-store-node-2.xml");


    }
}
