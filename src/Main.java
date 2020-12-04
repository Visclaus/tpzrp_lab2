
public class Main {
    public static void main(String[] args) {
        PeeringNetwork network = new PeeringNetwork(4);
        network.nodes.get(0).addNode(network.activeNodes);
        network.nodes.get(1).addNode(network.activeNodes);
        network.nodes.get(3).addNode(network.activeNodes);
        network.nodes.get(6).addNode(network.activeNodes);
        System.out.println(network);
        network.nodes.get(6).remove(6, network.activeNodes);
        System.out.println(network);
    }
}
