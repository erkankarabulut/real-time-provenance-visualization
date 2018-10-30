package Base;

import App.CytoVisProject;
import Util.BackwardDependency;
import Util.BackwardDependencyVol2;
import org.cytoscape.app.swing.CySwingAppAdapter;
import org.cytoscape.model.CyEdge;
import org.cytoscape.model.CyTable;
import org.cytoscape.model.events.AddedEdgesEvent;
import org.cytoscape.model.events.AddedEdgesListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class EdgesAddedListener implements AddedEdgesListener {

    private CytoVisProject cytoVisProject;
    private BackwardDependencyVol2 backwardDependencyVol2;
    private BackwardDependency backwardDependency;
    private CySwingAppAdapter adapter;

    public EdgesAddedListener(CytoVisProject cytoVisProject){
        this.cytoVisProject     = cytoVisProject;
        this.backwardDependencyVol2 = cytoVisProject.getMyControlPanel().getBackwardDependencyVol2();
        this.adapter            = cytoVisProject.getMyControlPanel().getAdapter();
        this.backwardDependency = cytoVisProject.getMyControlPanel().getBackwardDependency();
    }

    @Override
    public void handleEvent(AddedEdgesEvent addedEdgesEvent) {
        String sourceNodeId         = new String();
        String destNodeId           = new String();
        CyTable currentEdgeTable    = adapter.getCyApplicationManager().getCurrentNetwork().getDefaultEdgeTable();

        long startTime = new Date().getTime();

        for (CyEdge edge : addedEdgesEvent.getPayloadCollection()){
            backwardDependencyVol2.updateState(currentEdgeTable.getRow(edge.getSUID()).get("Source", String.class), currentEdgeTable.getRow(edge.getSUID()).get("Destination", String.class));
        }

        System.out.println("[" + new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" ).format(new Date()) + "] Total time to run BDM: "
                + (new Date().getTime() - startTime));
    }
}
