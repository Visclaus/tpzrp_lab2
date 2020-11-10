import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class ChordNode {
    public int m;
    public int n;
    public int id;
    private boolean isActive = false;
    public Finger finger = null;
    boolean isEmpty = true;
    public ChordNode predecessor = null;
    public ChordNode successor = null;
    private List<ChordNode> nodes;

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public ChordNode(int id, int m, int n) {
        this.id = id;
        this.m = m;
        this.n = n;
    }

    /* add/remove node block */

    private void removalUpdate(ChordNode deleted, ChordNode predecessor, int recordIndex) {
        if (deleted.equals(predecessor)) {
            return;
        }
        Finger table = predecessor.finger;
        FingerEntry record = table.getEntry(recordIndex);
        ChordNode node = record.getNode();
        if (node.equals(deleted)) {
            ChordNode successor = deleted.successor;
            record.setNode(successor);
            table.fingerEntries.set(recordIndex, record);
        }
        removalUpdate(deleted, predecessor.predecessor, recordIndex);

    }

    public void remove(int index, LinkedList<ChordNode> activeNodes) {
        if (activeNodes.stream().map(ChordNode::getId).noneMatch(id -> id == index)) {
            return;
        }
        ChordNode nodeToRemove = this.nodes.get(index);
        ChordNode successor = nodeToRemove.successor;
        ChordNode predecessor = nodeToRemove.predecessor;

        for (int i = 0; i < this.m; i++) {
            removalUpdate(nodeToRemove, predecessor, i);
        }

        predecessor.successor = successor;
        successor.predecessor = predecessor;

        this.nodes.set(index, new ChordNode(index, m, n));
        activeNodes.remove(nodeToRemove);
        this.isEmpty = activeNodes.isEmpty();
        if (!this.isEmpty) {
            activeNodes.sort(Comparator.comparingInt(ChordNode::getId));
        }

    }


    public void addNode(LinkedList<ChordNode> activeNodes) {
        if (isActive()) {
            return;
        }
        if (!activeNodes.isEmpty()) {
            ChordNode sampleNode = activeNodes.getFirst();
            init_finger_table(sampleNode);
            update_others();
        } else {
            isEmpty = false;
            List<FingerEntry> fingerEntries = new ArrayList<>();
            for (int i = 0; i < m; i++) {
                int start = Calculator.calculateStartIndex(this.id, i, n);
                int end = Calculator.calculateStartIndex(this.id, i + 1, n);
                fingerEntries.add(new FingerEntry(this.nodes.get(start), this.nodes.get(end), this));
            }
            this.setActive(true);
            this.predecessor = this;
            this.successor = this;
            this.updateFingerTable(fingerEntries);
        }
        activeNodes.add(this);
        activeNodes.sort(Comparator.comparingInt(ChordNode::getId));

    }

    public int getId() {
        return id;
    }

    public void init_finger_table(ChordNode sampleNode) {
        List<FingerEntry> fingerEntries = new ArrayList<>();
        int start = Calculator.calculateStartIndex(this.id, 0, n);
        int end = Calculator.calculateStartIndex(this.id, 1, n);
        ChordNode nodeSuccessor = sampleNode.findSuccessor(start);
        fingerEntries.add(new FingerEntry(this.nodes.get(start), this.nodes.get(end), nodeSuccessor));

        ChordNode successor = sampleNode.findSuccessor(this.id);
        ChordNode predecessor = successor.predecessor;

        this.setActive(true);
        this.successor = successor;
        this.predecessor = predecessor;
        successor.predecessor = this;
        predecessor.successor = this;

        for (int i = 1; i < this.m; i++) {
            start = Calculator.calculateStartIndex(this.id, i, this.n);
            end = Calculator.calculateStartIndex(this.id, i + 1, this.n);
            if (!findLeftHalfInterval(this.id, nodeSuccessor.id).contains(start)) {
                nodeSuccessor = sampleNode.findSuccessor(start);
            }
            fingerEntries.add(new FingerEntry(nodes.get(start), nodes.get(end), nodeSuccessor));
        }
        this.updateFingerTable(fingerEntries);
    }

    public void update_others() {
        for (int i = 0; i < m; i++) {
            int id = (this.id - (int) Math.pow(2, i) + n) % n;
            ChordNode predecessor;
            if (this.nodes.get(id).isActive()) {
                predecessor = this.nodes.get(id);
            } else {
                predecessor = findPredecessor(id);
            }
            predecessor.update_finger_table(this, i);
        }
    }

    public void update_finger_table(ChordNode s, int i) {
        if (s.id == this.id) {
            return;
        }
        if (findLeftHalfInterval(finger.getEntry(i).getStart().id, finger.getEntry(i).getNode().id).contains(s.id)) {
            finger.getEntry(i).setNode(s);
            ChordNode p = this.predecessor;
            p.update_finger_table(s, i);
        }
    }

    /* find successor block*/

    public ChordNode findSuccessor(int nodeId) {
        if (this.id == nodeId) {
            return this;
        }
        return findPredecessor(nodeId).successor;
    }

    public ChordNode findPredecessor(int nodeId) {
        ChordNode node = this;
        while (!findRightHalfInterval(node.id, node.successor.id).contains(nodeId)) {
            node = node.closestPrecedingFinger(nodeId);
        }
        return node;
    }

    public ChordNode closestPrecedingFinger(int nodeId) {
        for (int i = m - 1; i >= 0; i--) {
            ChordNode node = finger.getEntry(i).getNode();
            if (findInterval(this.id, nodeId).contains(node.id))
                return node;
        }
        return this;
    }

    public void updateFingerTable(List<FingerEntry> records) {
        this.finger = new Finger(records);
    }

    ArrayList<Integer> findLeftHalfInterval(int index1, int index2) {
        ArrayList<Integer> interval = new ArrayList<>();
        int totalCnt = 0;
        if (index1 > index2) {
            totalCnt = n - index1 + index2;
        }
        if (index1 < index2) {
            totalCnt = index2 - index1;
        }
        for (int i = 0; i < totalCnt; i++) {
            interval.add((index1 + i) % n);
        }
        return interval;
    }

    ArrayList<Integer> findRightHalfInterval(int index1, int index2) {
        ArrayList<Integer> interval = new ArrayList<>();
        int totalCnt = n;
        if (index1 > index2) {
            totalCnt = n - index1 + index2;
        }
        if (index1 < index2) {
            totalCnt = index2 - index1;
        }
        for (int i = 1; i <= totalCnt; i++) {
            interval.add((index1 + i) % n);
        }
        return interval;
    }

    ArrayList<Integer> findInterval(int index1, int index2) {
        ArrayList<Integer> interval = new ArrayList<>();
        int totalCnt = 0;
        if (index1 > index2) {
            totalCnt = n - index1 + index2;
        }
        if (index1 < index2) {
            totalCnt = index2 - index1;
        }
        for (int i = 1; i < totalCnt; i++) {
            interval.add((index1 + i) % n);
        }
        return interval;
    }

    public void setNodes(List<ChordNode> nodes) {
        this.nodes = nodes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChordNode chordNode = (ChordNode) o;
        return id == chordNode.id;
    }

    @Override
    public String toString() {
        return "ChordNode{" +
                "id=" + id +
                ", isActive=" + isActive +
                '}';
    }
}