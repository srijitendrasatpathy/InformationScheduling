import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.io.File;
import java.util.Scanner;


public class InstructionScheduler<NODE> {
    // Using hashtable to store the nodes of a graph.
    private Map<NODE, List<NODE>> node = new LinkedHashMap<>();
    public java.util.Set<NODE> edges = new LinkedHashSet<>();

    //Method to represent the graph in the form of a string.
    public String toString () {
        StringBuffer s = new StringBuffer();
        for (NODE n: node.keySet())
            s.append("\n    " + n + " -> " + node.get(n));
        return s.toString();
    }

    //Method to add a node to the graph
    public void add(NODE vertex){
        if(node.containsKey(vertex))
            return;
        node.put(vertex,new ArrayList<>());
    }

    //Method to create edges between the already present nodes in the graph
    public void add(NODE from, NODE to){
        this.add(from);
        this.add(to);
        node.get(from).add(to);
    }
    //Method to remove vertex from the graph
    public void remove(NODE vertex){
        if(!(this.contains(vertex)))
            throw new IllegalArgumentException("Node does not exists!");
        node.remove(vertex);
    }
    //Method to remove an edge from the graph
    public void remove(NODE from, NODE to){
        if(!(this.contains(from)&&this.contains(to)))
            throw new IllegalArgumentException("Node does not exist!");
        node.get(from).remove(to);
    }

    //Method to check if the node is present in the graph
    public boolean contains (NODE vertex){
        return node.containsKey(vertex);
    }

    //Method to check calculate a nodes in-degree
    //Commenting this procedure because of no usability in new implementation
    public Map<NODE,Integer> inDegree(){
        Map<NODE,Integer> indegree = new LinkedHashMap<>();
        //Settings all the indegree values to 0 first.
        for(NODE n:node.keySet())
            indegree.put(n,0);
        for(NODE from:node.keySet()){
            for(NODE to:node.get(from)){
                //Counting the no.of.edges
                indegree.put(to,indegree.get(to)+1);
            }
        }
        return indegree;
    }

    //Reading file and populating graph.
    public void readFile(String fileName){
        int counter = 0;
        try{
            Scanner sc = new Scanner(new File(fileName));
            while(sc.hasNext()){
                if(counter == 0){
                    sc.nextLine();
                    counter++;
                }else{
                    String line = sc.nextLine();
                    String[] word = line.split("\\s");
                    if(Integer.parseInt(word[0]) == -1){
                        sc.close();
                        break;
                    }else{
                        this.add((NODE)(word[0]),(NODE)(word[1]));
                        this.edges.add((NODE)word[1]);
                    }
                }
            }
        }catch(java.io.FileNotFoundException e){
            e.printStackTrace();
        }catch(ClassCastException e){
            e.printStackTrace();
        }
    }

    public void schedule(int functionalUnits){
        int counter = 0;
        Map<NODE,Integer> indegree;
        while(!this.node.isEmpty()){
            counter = 0;
            indegree = inDegree();
            for(NODE n: indegree.keySet()){
                if(counter == functionalUnits )
                    break;
                else {
                    if (indegree.get(n) == 0) {
                        System.out.print(n + "\t    ");
                        node.remove(n);
                        counter++;
                    }
                }
            }
            System.out.println("");
        }
    }

    //Update readyList to push the instructions to the functional units
    /*
     * Issue with the function:
     * 1. How to assign null values in the array if there are less instructions than FU's available.
     */
    /*public void updateReadyList(NODE[] readyList){
        int nodeCounter = 0;
        try{
        for(NODE node : this.node.keySet()){
            if(nodeCounter == 4){
                break;
            }else {
                if (this.edges.contains(node)) {
                    continue;
                } else {
                    readyList[nodeCounter] = node;
                    nodeCounter++;
                }
            }
        }
    }catch(ClassCastException e){
            e.printStackTrace();
        }catch(ArrayStoreException e){
            e.printStackTrace();
        }
    }
*/
    //Main Program
    public static void main(String[] args) {
        int functionalUnit = Integer.parseInt(args[1]);
        //Integer[] readyList = new Integer[functionalUnit];
        InstructionScheduler<Integer> graph = new InstructionScheduler<>();
        graph.readFile(args[0]);
//        System.out.println("The current graph is :" + graph);
//        System.out.println("The nodes with indegrees >  are: "+graph.edges);
        for(int i=0; i < functionalUnit; i++){
            System.out.print("F.U. "+i+"\t");
        }
        System.out.println();
        graph.schedule(functionalUnit);

    }
}
