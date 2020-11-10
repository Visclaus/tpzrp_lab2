public class FingerEntry {
    private ChordNode start;
    private ChordNode end;
    private ChordNode node;

    @Override
    public String toString() {
        return "{" +
                "start=" + start.id +
                ", int=[" + start.id + ", " + end.id +
                ") , node=" + node.id +
                '}';
    }

    public ChordNode getStart() {
        return start;
    }

    public void setStart(ChordNode start) {
        this.start = start;
    }

    public ChordNode getEnd() {
        return end;
    }

    public void setEnd(ChordNode end) {
        this.end = end;
    }

    public ChordNode getNode() {
        return node;
    }

    public void setNode(ChordNode node) {
        this.node = node;
    }

    public FingerEntry(ChordNode start, ChordNode end, ChordNode node) {
        this.start = start;
        this.end = end;
        this.node = node;
    }
}
