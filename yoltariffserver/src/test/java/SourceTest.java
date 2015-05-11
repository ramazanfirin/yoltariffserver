import org.neo4j.graphalgo.GraphAlgoFactory;
import org.neo4j.graphalgo.PathFinder;
import org.neo4j.graphalgo.WeightedPath;
import org.neo4j.graphalgo.impl.util.DoubleEvaluator;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.PathExpanders;
import org.neo4j.graphdb.Relationship;


public class SourceTest {
public static void main(String[] args) {
	PathFinder<WeightedPath> finder = GraphAlgoFactory.dijkstra(
		    PathExpanders.forTypeAndDirection( RelationType.WALK1, Direction.BOTH ), "cost" );

		Node nodeA=null;
		Node nodeB=null;
		WeightedPath path = finder.findSinglePath( nodeA, nodeB );

		// Get the weight for the found path
		path.weight();
}


class myEvaluator extends DoubleEvaluator{

	private String costpropertyName;
	public myEvaluator(String costpropertyName) {
		super(costpropertyName);
		// TODO Auto-generated constructor stub
	}
	@Override
	 public Double getCost( Relationship relationship, Direction direction ){
		Object costProp = relationship.getProperty( costpropertyName );
        if(costProp instanceof Double)
        {
            return (Double)costProp;
        } else if (costProp instanceof Integer)
        {
            return (double)(Integer)costProp;
        } else
        {
            return Double.parseDouble( costProp.toString() );
        }
		 
        //relationship.getEndNode().getProperty("time");
        
	 }
	
}
}
