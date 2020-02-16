package spacemadness.com.lunarconsole.reactive

import spacemadness.com.lunarconsole.core.Disposable

fun <T, R> combineLatest(
    vararg observables: Observable<T>,
    combiner: (values: List<T>) -> R
): Observable<R> {
    return object : Observable<R> {
        override fun subscribe(observer: Observer<R>): Disposable {
            // latest values for all observables
            val latestValues = mutableListOf<T>()

            // latest value was received for each observable
            val latestValuesUpdated = BooleanArray(observables.size)

            // total latest values received count
            var updatedCount = 0

            val subscriptions = observables.mapIndexed { index, observable ->
                observable.subscribe { value ->
                    // there was no value but got it now
                    if (!latestValuesUpdated[index]) {
                        latestValuesUpdated[index] = true
                        ++updatedCount
                    }

                    if (index < latestValues.size) {
                        latestValues[index] = value
                    } else {
                        // hack: duplicate the value in the list until it gets filled-up enough to set element at index
                        while (latestValues.size <= index) {
                            latestValues.add(value)
                        }
                    }

                    // if latest values are received - notify the observer
                    if (updatedCount == observables.size) {
                        val combinedValue = combiner(latestValues)
                        observer(combinedValue)
                    }
                }
            }

            val disposables = CompositeDisposable(subscriptions)
            return object : Disposable {
                override fun dispose() {
                    latestValues.clear()
                    disposables.dispose()
                }
            }
        }
    }
}