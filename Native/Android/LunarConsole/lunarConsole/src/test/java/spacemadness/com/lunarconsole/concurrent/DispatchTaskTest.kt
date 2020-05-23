package spacemadness.com.lunarconsole.concurrent

import org.junit.Test
import spacemadness.com.lunarconsole.TestCase

class DispatchTaskTest : TestCase() {
    @Test
    fun testSchedule() {
        val queue = ImmediateDispatchQueue(dispatchImmediately = false)
        val task = object : DispatchTask() {
            override fun execute() {
                addResult("executed")
            }
        }
        queue.dispatch(task)

        queue.dispatchTasks()
        assertResults("executed")
    }

    @Test
    fun testCancel() {
        val queue = ImmediateDispatchQueue(dispatchImmediately = false)
        val task = object : DispatchTask() {
            override fun execute() {
                addResult("executed")
            }
        }
        queue.dispatch(task)
        task.cancel() // cancel task before the queue dispatches it

        queue.dispatchTasks()
        assertResults()

        // dispatch task again
        queue.dispatch(task)

        queue.dispatchTasks()
        assertResults("executed")
    }

    @Test
    fun testScheduleMultiple() {
        val queue = ImmediateDispatchQueue(dispatchImmediately = false)
        val task = object : DispatchTask() {
            override fun execute() {
                addResult("executed")
            }
        }
        queue.dispatch(task)
        queue.dispatch(task)

        queue.dispatchTasks()
        assertResults("executed", "executed")
    }

    @Test
    fun testScheduleMultipleOnce() {
        val queue = ImmediateDispatchQueue(dispatchImmediately = false)
        val task = object : DispatchTask() {
            override fun execute() {
                addResult("executed")
            }
        }
        queue.dispatchOnce(task)
        queue.dispatchOnce(task)

        queue.dispatchTasks()
        assertResults("executed")
    }
}