package spacemadness.com.lunarconsole.utils;

public class IntReference {
    public int value;

    public IntReference(int value)
    {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IntReference that = (IntReference) o;
        return value == that.value;
    }

    @Override
    public String toString() {
        return Integer.toString(value);
    }
}
