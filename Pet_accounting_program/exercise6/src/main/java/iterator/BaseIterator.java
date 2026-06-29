package iterator;

public interface BaseIterator {
    public <T> T next();

    public boolean hasNext();

    public void reset();
}