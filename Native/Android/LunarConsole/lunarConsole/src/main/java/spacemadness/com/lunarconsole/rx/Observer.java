package spacemadness.com.lunarconsole.rx;

public interface Observer<T> {
    void onChanged(T value);
}
