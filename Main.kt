package tasklist

import kotlin.system.exitProcess


fun main() {
    // write your code here
    val lTasks = ListOfTasks()
    while (true) {
        println("Input an action (add, print, end):")
        val action = readln()
        if ("add".equals(action, ignoreCase = true)) {
            println("Input a new task (enter a blank line to end):")
            val newTask = Task()
            var firstLine = true
            while (true) {
                val line = readln().trim()
                if (line.isEmpty() && firstLine) {
                    println("The task is blank")
                    break
                } else if (line.isEmpty()) {
                    lTasks.addTask(newTask)
                    break
                } else {
                    newTask.addStep(line)
                }
                firstLine = false
            }
        } else if ("print".equals(action, ignoreCase = true)) {
            lTasks.printTasks()
        } else if ("end".equals(action, ignoreCase = true)) {
            println("Tasklist exiting!")
            exitProcess(0)
        } else {
            println("The input action is invalid")
        }
    }
}

class Task {
    private var steps = mutableListOf<String>()

    fun addStep(line: String) {
        steps.add(line)
    }

    fun printSteps() {
        for (index in steps.indices) {
            if (index == 0) {
                println(steps[index])
            } else {
                println("   ${steps[index]}")
            }
        }
    }
}

class ListOfTasks {
    private var listTasks = mutableListOf<Task>()
    fun addTask(task: Task) {
        listTasks.add(task)
    }

    fun printTasks() {
        if (listTasks.size != 0) {
            for (index in listTasks.indices) {
                if (index < 9) {
                    print("${index + 1}  ")
                    listTasks[index].printSteps()
                } else {
                    print("${index + 1} ")
                    listTasks[index].printSteps()
                }
                println()
            }
        } else {
            println("No tasks have been input")
        }
    }
}


/* Part 1
fun main() {
    // write your code here
    val tasks = mutableListOf<Task>()
    println("Input the tasks (enter a blank line to end):")
    while (true) {
        val input = readln().trim()
        if (input.isEmpty()) {
            break
        } else {
            val task = Task(input)
            tasks.add(task)
        }
    }
    if (tasks.size == 0) {
        println("No tasks have been input")
    } else {
        for (index in tasks.indices) {
            if (index < 9) {
                println("${index + 1}  ${tasks[index].whatToDo}")
            } else {
                println("${index + 1} ${tasks[index].whatToDo}")
            }
        }
    }
}


data class Task(val whatToDo: String)
*/