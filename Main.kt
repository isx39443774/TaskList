package tasklist

import kotlinx.datetime.*
import kotlinx.datetime.TimeZone
import java.time.LocalTime
import java.util.*
import com.squareup.moshi.*
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import java.io.File

var taskPriorityMap = mapOf(
    "C" to "\u001B[101m \u001B[0m",
    "H" to "\u001B[103m \u001B[0m",
    "N" to "\u001B[102m \u001B[0m",
    "L" to "\u001B[104m \u001B[0m",
)

var taskTimeTegMap = mapOf(
    "I" to "\u001B[102m \u001B[0m",
    "T" to "\u001B[103m \u001B[0m",
    "O" to "\u001B[101m \u001B[0m",
)

class Task() {
    var taskPriority: String = ""
    var taskDate: LocalDate? = null
    var taskTime: LocalTime? = null
    var taskStrings = mutableListOf<String>()
    fun addtaskPriority() {
        var tP = ""
        do {
            println("Input the task priority (C, H, N, L):")
            tP = readln().uppercase().trim()
        } while ((tP !in "CHNL".uppercase()) or (tP.isEmpty()))
        taskPriority = taskPriorityMap[tP.uppercase()].toString()
    }

    fun addtaskDate() {
        do {
            println("Input the date (yyyy-mm-dd):")
            val dateString = readln()
            try {
                val tD = dateString.split("-").map { it.toInt() }
                taskDate = LocalDate(tD[0], tD[1], tD[2])
                break
            } catch (e: Exception) {
                println("The input date is invalid")
            }
        } while (true)
    }

    fun addtaskTime() {
        do {
            println("Input the time (hh:mm):")
            val timeString = readln()
            try {
                val tT = timeString.split(":").map { it.toInt() }
                taskTime = LocalTime.of(tT[0], tT[1])
                break
            } catch (e: Exception) {
                println("The input time is invalid")
            }
        } while (true)
    }

    fun addTaskStrings() {
        val strings = MutableList(0) { "" }
        taskStrings.clear()
        println("Input a new task (enter a blank line to end):")
        val scan = Scanner(System.`in`)
        while (scan.hasNextLine()) {
            val str = scan.nextLine().trim()
            if (str != "") {
                strings.add(str)
            } else break
        }
        if (strings.size > 0) {
            strings.forEach {
                taskStrings.add(it)
            }
            strings.clear()
        } else {
            println("The task is blank")
        }
    }
}

fun main() {
    val noTasksMessage = "No tasks have been input"
    val tasks = mutableListOf<Task>()
    var taskTimeTeg: String? = "TimeTeg"

    fun splitStrToEqualsParts(inputString: String, partLength: Int, addSpacesToLastStr: Boolean): List<String> {
        val stringsList = mutableListOf<String>()
        var partsCount = inputString.length / partLength
        if (inputString.length.toFloat() % partLength.toFloat() > 0) partsCount += 1
        for (i in 1 until partsCount) stringsList.add(inputString.substring((i - 1) * partLength until (i - 1) * partLength + partLength))
        stringsList.add(inputString.substring((partsCount - 1) * partLength))
        if (addSpacesToLastStr) {
            stringsList[stringsList.lastIndex] = stringsList[stringsList.lastIndex].padEnd(44)// + spacesStr
        }
        return stringsList
    }

    fun printTaskList() {
        val TASK_STR_LENGTH = 44
        val STR_SEPAROTOR = "+----+------------+-------+---+---+--------------------------------------------+"
        var spaces = ""
        val currentDate = Clock.System.now().toLocalDateTime(TimeZone.of("UTC+0")).date
        println(
            STR_SEPAROTOR + "\n| N  |    Date    | Time  | P | D |                   Task                     |\n"
                    + STR_SEPAROTOR
        )
        for (i in 0..tasks.size - 1) {
            val taskDate = tasks[i].taskDate
            val numberOfDays = currentDate.daysUntil(taskDate!!)
            val strings = mutableListOf<String>()
            when {
                numberOfDays == 0 -> taskTimeTeg = taskTimeTegMap["T"].toString()

                numberOfDays < 0 -> taskTimeTeg = taskTimeTegMap["O"].toString()

                numberOfDays > 0 -> taskTimeTeg = taskTimeTegMap["I"].toString()
            }
            if (i < 9) spaces = "  "
            else spaces = " "
            for (taskString in tasks[i].taskStrings) {
                splitStrToEqualsParts(taskString, TASK_STR_LENGTH, true).forEach {
                    strings.add(it)
                }
            }
            println("| ${i + 1}$spaces| ${tasks[i].taskDate} | ${tasks[i].taskTime} | ${tasks[i].taskPriority} | ${taskTimeTeg} |${strings[0]}|")
            for (k in 1..strings.lastIndex) {
                println("|    |            |       |   |   |${strings[k]}|")
            }
            println(STR_SEPAROTOR)
        }
    }

    fun editTask() {
        var i: Int? = null
        printTaskList()
        do {
            println("Input the task number (1-${tasks.lastIndex + 1}):")
            val s = readln()
            try {
                i = s.toInt() - 1
                if (i in 0..tasks.lastIndex) {
                    do {
                        println("Input a field to edit (priority, date, time, task):")
                        when (readln()) {
                            "priority" -> tasks[i].addtaskPriority()
                            "date" -> tasks[i].addtaskDate()
                            "time" -> tasks[i].addtaskTime()
                            "task" -> tasks[i].addTaskStrings()
                            else -> {
                                println("Invalid field")
                                continue
                            }
                        }
                        println("The task is changed")
                        break
                    } while (true)
                } else {
                    println("Invalid task number")
                    continue
                }
            } catch (e: Exception) {
                println("Invalid task number")
                continue
            }
        } while (i !in 0..tasks.lastIndex)
    }

    fun deleteTask() {
        var i: Int? = null
        printTaskList()
        do {
            println("Input the task number (1-${tasks.lastIndex + 1}):")
            val s = readln()
            try {
                i = s.toInt() - 1
                if (i in 0..tasks.lastIndex) {
                    tasks.removeAt(i!!)
                    println("The task is deleted")
                    break
                } else {
                    println("Invalid task number")
                    continue
                }
            } catch (e: Exception) {
                println("Invalid task number")
                continue
            }
        } while (true)
    }

    class LocalDateTimeAdapter {
        @ToJson
        fun toJson(ldt: LocalDateTime): String {
            return ldt.toString()
        }

        @FromJson
        fun fromJson(ldt: String): LocalDateTime {
            return LocalDateTime.parse(ldt)
        }
    }

    class LocalDateAdapter {
        @ToJson
        fun toJson(ld: LocalDate): String {
            return ld.toString()
        }

        @FromJson
        fun fromJson(ld: String): LocalDate {
            return LocalDate.parse(ld)
        }
    }

    class LocalTimeAdapter {
        @ToJson
        fun toJson(lt: LocalTime): String {
            return lt.toString()
        }

        @FromJson
        fun fromJson(lt: String): LocalTime {
            return LocalTime.parse(lt)
        }
    }

    fun saveInfo() {
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .add(LocalDateAdapter())
            .add(LocalDateTimeAdapter())
            .add(LocalTimeAdapter())
            .build()!!

        val type = Types.newParameterizedType(MutableList::class.java, Task::class.java)!!
        val jsonAdapter = moshi.adapter<MutableList<Task>>(type)!!

        val myFile = File("tasklist.json")
        myFile.writeText(jsonAdapter.toJson(tasks))
    }

    fun checkAndLoadTasksFile() {
        var file = File("tasklist.json")
        if (file.exists()) {
            val moshi = Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .add(LocalDateAdapter())
                .add(LocalDateTimeAdapter())
                .add(LocalTimeAdapter())
                .build()!!

            val type = Types.newParameterizedType(MutableList::class.java, Task::class.java)!!
            val jsonAdapter = moshi.adapter<MutableList<Task>>(type)!!
            val fileRead = file.readText()
            val taskList = jsonAdapter.fromJson(fileRead)


            if (taskList != null) {
                for (task in taskList) {
                    tasks.add(task)
                }
            }
        }
    }
    checkAndLoadTasksFile()
    while (true) {
        println("Input an action (add, print, edit, delete, end):")
        val inp = readln()
        when (inp.lowercase()) {
            "add" -> {
                val task = Task()
                task.addtaskPriority()
                task.addtaskDate()
                task.addtaskTime()
                task.addTaskStrings()
                tasks.add(task)
            }

            "print" -> {
                if (tasks.isEmpty()) println(noTasksMessage)
                else printTaskList()
            }

            "edit" -> {
                if (tasks.isEmpty()) println(noTasksMessage)
                else editTask()
            }

            "delete" -> {
                if (tasks.isEmpty()) println(noTasksMessage)
                else deleteTask()
            }

            "end" -> {
                println("Tasklist exiting!")
                saveInfo()
                break
            }

            else -> println("The input action is invalid")
        }
    }
}