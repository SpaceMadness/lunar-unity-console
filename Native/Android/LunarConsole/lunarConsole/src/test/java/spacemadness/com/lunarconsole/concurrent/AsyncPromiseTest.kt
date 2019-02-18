package spacemadness.com.lunarconsole.concurrent

import org.junit.Assert.assertTrue
import org.junit.Test
import spacemadness.com.lunarconsole.TestCase

class AsyncPromiseTest : TestCase() {
    @Test
    fun testCallbacks() {
        val promise = AsyncPromise<String>()

        // use this queue to dispatch value and error
        val dispatchQueue = BlockingDispatchQueue("dispatch")

        // subscribe to callbacks
        promise.then { value ->
            // should be executed on the same queue
            assertTrue(dispatchQueue.isCurrent)
            addResult("value: $value")
        }
        promise.catchError { error ->
            // should be executed on the same queue
            assertTrue(dispatchQueue.isCurrent)
            addResult("error: ${error.message}")
        }

        // notify on a separate queue
        dispatchQueue.dispatch {
            promise.resolve("test")
        }
        assertResults("value: test")

        dispatchQueue.dispatch {
            promise.reject(Exception("exception"))
        }
        assertResults("error: exception")
    }

    @Test
    fun testCallbacksCustomQueue() {
        val completionQueue = BlockingDispatchQueue("completion")

        // subscribe to callbacks
        val promise = AsyncPromise<String>(completionQueue)
        promise.then { value ->
            // should be executed on a "completion" queue
            assertTrue(completionQueue.isCurrent)
            addResult("value: $value")
        }
        promise.catchError { error ->
            // should be executed on a "completion" queue
            assertTrue(completionQueue.isCurrent)
            addResult("error: ${error.message}")
        }

        // notify on a separate queue
        val dispatchQueue = BlockingDispatchQueue("dispatch")
        dispatchQueue.dispatch {
            promise.resolve("test")
        }
        assertResults("value: test")

        dispatchQueue.dispatch {
            promise.reject(Exception("exception"))
        }
        assertResults("error: exception")
    }

    @Test
    fun testCallbacksThenException() {
        val promise = AsyncPromise<String>()
        promise.then {
            throw Exception("exception")
        }
        promise.catchError { error ->
            addResult("error: ${error.message}")
        }

        // notify on a separate queue
        val dispatchQueue = BlockingDispatchQueue("dispatch")
        dispatchQueue.dispatch {
            promise.resolve("test")
        }
        assertResults("error: exception")
    }

    @Test
    fun testCallbacksThenExceptionCustomQueue() {
        val completionQueue = BlockingDispatchQueue("completion")

        val promise = AsyncPromise<String>(completionQueue)
        promise.then {
            throw Exception("exception")
        }
        promise.catchError { error ->
            addResult("error: ${error.message}")
        }

        // notify on a separate queue
        val dispatchQueue = BlockingDispatchQueue("dispatch")
        dispatchQueue.dispatch {
            promise.resolve("test")
        }
        assertResults("error: exception")
    }

    @Test
    fun testCallbacksCatchException() {
        val promise = AsyncPromise<String>()
        promise.then {
            throw Exception("exception")
        }
        promise.catchError {
            throw Exception("another exception")
        }

        // notify on a separate queue
        val dispatchQueue = BlockingDispatchQueue("dispatch")
        dispatchQueue.dispatch {
            promise.resolve("test")
        }
        assertResults() // there will be no results but also no uncaught exceptions
    }

    @Test
    fun testCallbacksCatchExceptionCustomQueue() {
        val completionQueue = BlockingDispatchQueue("completion")

        val promise = AsyncPromise<String>(completionQueue)
        promise.then {
            throw Exception("exception")
        }
        promise.catchError {
            throw Exception("another exception")
        }

        // notify on a separate queue
        val dispatchQueue = BlockingDispatchQueue("dispatch")
        dispatchQueue.dispatch {
            promise.resolve("test")
        }
        assertResults() // there will be no results but also no uncaught exceptions
    }
}