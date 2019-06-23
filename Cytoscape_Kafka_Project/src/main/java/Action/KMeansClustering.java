package Action;

import Util.FilterUtil;
import Util.MathUtil;

import org.cytoscape.app.swing.CySwingAppAdapter;
import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;
import org.cytoscape.model.CyRow;
import org.cytoscape.model.CyTable;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.view.presentation.property.BasicVisualLexicon;

import org.json.simple.JSONArray;
import weka.attributeSelection.AttributeSelection;
import weka.attributeSelection.InfoGainAttributeEval;
import weka.attributeSelection.Ranker;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instances;

import javax.swing.*;
import java.util.*;

public class KMeansClustering {

    public MathUtil mathUtil;
    public CyNetwork network;
    public CyTable table;
    public CySwingAppAdapter adapter;
    public HashMap<String, CyNode> allNodes;
    public FilterUtil filter;
    public CyNetworkView networkView;
    public static int TRESHOLD = 10;

    public KMeansClustering(CySwingAppAdapter adapter){
        this.mathUtil = new MathUtil();
        this.adapter  = adapter;

        // Getting necessary components from network
        CyApplicationManager manager = adapter.getCyApplicationManager();
        networkView = manager.getCurrentNetworkView();
        network = networkView.getModel();
        table = network.getDefaultNodeTable();

        filter = new FilterUtil(network, table);
        allNodes = filter.getAllNodesWithId();
    }

    public ArrayList<ArrayList<String>> applyKMeansClustering(int maxClusterCount){
        ArrayList<ArrayList<String>> clusters = new ArrayList<>();

        try {
            int totalClusterCount = 0;
            Iterator<String> keyIterator = allNodes.keySet().iterator();
            while (keyIterator.hasNext()){
                String key = keyIterator.next();
                Integer clusterIndex = doesNodeFitsInAnyCluster(clusters, key);
                if(clusterIndex != null){
                    clusters.get(clusterIndex).add(key);
                }else {
                    clusters.add(new ArrayList<String>(){{add(key);}});
                    totalClusterCount++;
                    if(totalClusterCount > maxClusterCount){
                        clusters = mergeNearestClusters(clusters);
                    }
                }

            }
        }catch (Exception e){
            JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(),e.getMessage() + " -- " + e.toString(),
                    "Error", JOptionPane.INFORMATION_MESSAGE);
        }

        drawClusters(clusters);
        return clusters;
    }

    /*public ArrayList<ArrayList<String>> applyKMeansClustering(int maxClusterCount){
        ArrayList<ArrayList<String>> clusters = new ArrayList<>();

        //ArrayList<Integer> columnsWithHighInfoGain = applyInformationGain();
        int totalClusterCount = 0;
        for(CyNode node : allNodes){
            String nodeId = filter.getNodeId(node, adapter, "name");
            Integer clusterIndex = doesNodeFitsInAnyCluster(clusters, nodeId);
            if(clusterIndex != null){
                clusters.get(clusterIndex).add(nodeId);
            }else {
                clusters.add(new ArrayList<String>(){{add(nodeId);}});
                totalClusterCount++;
                if(totalClusterCount > maxClusterCount){
                    clusters = mergeNearestClusters(clusters);
                }
            }

        }

        drawClusters(clusters);
        return clusters;
    }*/

    public ArrayList<Integer> applyInformationGain(){
        ArrayList<Integer> columnsWithHighInfoGain = new ArrayList<>();

        AttributeSelection attributeSelection = new AttributeSelection();
        InfoGainAttributeEval infoGainAttributeEval = new InfoGainAttributeEval();
        Ranker search = new Ranker();
        attributeSelection.setEvaluator(infoGainAttributeEval);
        attributeSelection.setSearch(search);

        ArrayList<Attribute> attributes = new ArrayList<>();
        ArrayList<String> classValues = new ArrayList<String>(){{
            add("A");
            add("B");
            add("C");
            add("D");
            add("E");
            add("F");
        }};

        Set<String> attributeNames = table.getAllRows().get(1).getAllValues().keySet();
        Iterator<String> attributeNamesIterator = attributeNames.iterator();
        while (attributeNamesIterator.hasNext()){
            attributes.add(new Attribute(attributeNamesIterator.next(), new ArrayList<>()));
        }

        attributes.add(new Attribute("@@class@@", classValues));
        try {

            Instances dataSet = new Instances("TestInstances", attributes, 0);
            int rowCounter = 0;
            for(CyRow row : table.getAllRows()){
                Map<String, Object> rowValue = row.getAllValues();
                double[] instanceValues = new double[dataSet.numAttributes()];
                for(Object rowElement : rowValue.values()){
                    instanceValues[rowCounter] = dataSet.attribute(rowCounter).addStringValue(rowElement == null ? (String) null : rowElement.toString());
                    rowCounter++;
                }

                instanceValues[rowCounter] = dataSet.attribute(rowCounter).addStringValue("A");
                dataSet.add(new DenseInstance(1.0, instanceValues));
                rowCounter = 0;
            }

            attributeSelection.SelectAttributes(dataSet);

            double[][] attrRanks = attributeSelection.rankedAttributes();
        }catch (Exception e ){
            JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(),
                    e.getMessage() + " - " + e.toString(),
                    "Attribute Ranks", JOptionPane.INFORMATION_MESSAGE);
        }

        return columnsWithHighInfoGain;
    }

    public ArrayList<ArrayList<String>> mergeNearestClusters(ArrayList<ArrayList<String>> clusters){

        int minDistance = Integer.MAX_VALUE;
        int cluster1 = -1;
        int cluster2 = -1;

        for (int i=0; i<clusters.size()-1; i++){
            for (int j=i+1; j<clusters.size(); j++){
                int tempDistance = findMinDistanceBetweenClusters(clusters.get(i), clusters.get(j));
                if(tempDistance < minDistance){
                    minDistance = tempDistance;
                    cluster1 = i;
                    cluster2 = j;
                }
            }
        }

        if(cluster1 != -1){
            clusters.get(cluster1).addAll(clusters.get(cluster2));
            clusters.remove(cluster2);

        }

        return clusters;
    }

    public int findMinDistanceBetweenClusters(ArrayList<String> c1, ArrayList<String> c2){
        int minDistance = Integer.MAX_VALUE;

        for(String node1 : c1){
            for(String node2: c2){
                int tempDistance = calculateDistance(node1, node2);
                if(tempDistance < minDistance){
                    minDistance = tempDistance;
                }
            }
        }

        return minDistance;
    }

    public Integer doesNodeFitsInAnyCluster(ArrayList<ArrayList<String>> clusters, String key){
        Integer minDistance = Integer.MAX_VALUE;
        Integer clusterIndex = null;

        try {
            for(int j=0; j<clusters.size(); j++){
                for(int i=0; i<clusters.get(j).size(); i++){
                    int tempDistance = calculateDistance(clusters.get(j).get(i), key);
                    if(tempDistance < TRESHOLD){
                        if(minDistance > tempDistance){
                            minDistance = tempDistance;
                            clusterIndex = j;
                        }
                    }
                }
            }
        }catch (Exception e){
            JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(),"doesifts: " + e.toString() + " " + e.getMessage(),
                    "K-Means", JOptionPane.INFORMATION_MESSAGE);
        }

        return clusterIndex;
    }

    public int calculateDistance(String node1, String node2){
        int distance = 0;

        try {

            Set<String> attributeNames = table.getAllRows().get(1).getAllValues().keySet();
            Iterator<String> attributeIterator = attributeNames.iterator();
            while (attributeIterator.hasNext()){
                String attributeName = attributeIterator.next();
                if(attributeName.startsWith("*")){
                    int value1 = (filter.getValueById(node1, "name", attributeName) == null ? 0 :
                            Integer.parseInt(filter.getValueById(node1, "name", attributeName).toString())) ;
                    int value2 = (filter.getValueById(node2, "name", attributeName) == null ? 0 :
                            Integer.parseInt(filter.getValueById(node2, "name", attributeName).toString()));

                    distance += Math.abs(value1-value2);
                }
            }
        }catch (Exception e){
            JOptionPane.showMessageDialog(adapter.getCySwingApplication().getJFrame(),"calculatedistancee:" + e.toString(),
                    "K-Means", JOptionPane.INFORMATION_MESSAGE);
        }

        return distance;
    }

    public JSONArray getNodeTablAsJSONArray(){
        JSONArray nodeTable = new JSONArray();

        List<CyRow> rows = table.getAllRows();
        for (CyRow row : rows){
            nodeTable.add(row.getAllValues());
        }

        return nodeTable;
    }

    public Object getNodeAttributeById(String id, String attributeName, JSONArray table){
        Object attributeValue = null;

        for (int i=0; i<table.size(); i++){
            if(((Map<String, Object>) table.get(i)).get("id").equals(id)){
                attributeValue = ((Map<String, Object>) table.get(i)).get(attributeName);
            }
        }

        return attributeValue;
    }

    public void drawClusters(ArrayList<ArrayList<String>> clusters){
        int x = 0;
        int y = 0;

        for (int i=0; i<clusters.size(); i++){
            for (int j=0; j<clusters.get(i).size(); j++){
                networkView.getNodeView(allNodes.get(clusters.get(i).get(j))).setVisualProperty(BasicVisualLexicon.NODE_X_LOCATION, x);
                networkView.getNodeView(allNodes.get(clusters.get(i).get(j))).setVisualProperty(BasicVisualLexicon.NODE_X_LOCATION, y);
                x += (new Random().nextInt(5));
                y += (new Random().nextInt(5));
            }

            if(i != (clusters.size()-1)){
                x += 500;
                y += 500;
            }

            networkView.updateView();
        }

    }
}
