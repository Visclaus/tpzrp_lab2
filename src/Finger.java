import java.util.List;

public class Finger {
    List<FingerEntry> fingerEntries;

    public Finger(List<FingerEntry> fingerEntries) {
        this.fingerEntries = fingerEntries;
    }
    public FingerEntry getEntry(int index) {
        return fingerEntries.get(index);
    }
}
