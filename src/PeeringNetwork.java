import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;

public class PeeringNetwork {

    public ArrayList<ChordNode> nodes = new ArrayList<>();
    public LinkedList<ChordNode> activeNodes = new LinkedList<>();
    private final int n;

    public PeeringNetwork(int m) {
        this.n = Calculator.calculateNodeCnt(m);
        for (int i = 0; i < this.n; i++) {
            this.nodes.add(new ChordNode(i, m, this.n));
        }
        for (int i = 0; i < this.n; i++) {
            this.nodes.get(i).setNodes(nodes);
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        activeNodes.sort(Comparator.comparingInt(ChordNode::getId));
        String[] idArray = new String[this.n];
        for (int i = 0; i < this.n; i++) {
            idArray[i] = String.valueOf(i);
            for (ChordNode chordNode : activeNodes) {
                if (idArray[i].equals(String.valueOf(chordNode.id))) {
                    idArray[i] = "[" + idArray[i] + "]";
                }
            }
        }
        for (int i = 0; i < this.n / 2 + 1; i++) {
            if (i == 0 || i == this.n / 2) {
                builder.append(String.format("%" + (this.n + 1) + "s", idArray[i]));
                continue;
            }
            if (i < this.n / 4) {
                builder.append("\n");
                builder.append(String.format("%" + (this.n + 1 - 4 * i) + "s", idArray[this.n - i])).append(String.format("%" + (2 * 4 * i) + "s", idArray[i]));
                builder.append("\n");
            }
            if (i == this.n / 4) {
                builder.append("\n");
                builder.append(String.format("%2s", idArray[this.n - i])).append(String.format("%" + (2 * this.n - 2) + "s", idArray[i]));
                builder.append("\n");
            }
            if (i > this.n / 4) {
                builder.append("\n");
                int x = i - this.n / 4;
                builder.append(String.format("%" + (x * 4 + 1) + "s", idArray[this.n - i])).append(String.format("%" + (this.n - i - this.n / 2) * 2 * 4 + "s", idArray[i]));
                builder.append("\n");
            }
        }
        builder.append("\n\n\n--------Active_Nodes----------\n");
        for (ChordNode chordNode : activeNodes) {
            builder.append("\nNode id: ");
            builder.append(chordNode.id);
            builder.append("\n");
            builder.append("Finger table: \n");
            for (FingerEntry entry : chordNode.finger.fingerEntries) {
                builder.append(entry.toString());
                builder.append("\n");
            }
        }
        return builder.toString();
    }
}

