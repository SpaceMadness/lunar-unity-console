package spacemadness.com.lunarconsole.core;

import spacemadness.com.lunarconsole.concurrent.DispatchQueue;

public class MutableLiveData<T> extends LiveData<T> {
	public MutableLiveData(T value) {
		super(value);
	}

	public MutableLiveData(T value, DispatchQueue dispatchQueue) {
		super(value, dispatchQueue);
	}

	@Override
	public void setValue(T value) {
		super.setValue(value);
	}
}
