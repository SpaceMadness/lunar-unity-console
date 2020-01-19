package spacemadness.com.lunarconsole.concurrent

import spacemadness.com.lunarconsole.concurrent.Executor

data class Executors(val main: Executor, val state: Executor)