package cinema

import java.util.*
import kotlin.math.roundToInt

const val MARK = "S"
const val PAYED = "B"
const val FULL_COST = 10
const val LOW_COST = 8
const val HUNDRED = 100.0

fun main() {
    val (lines, seats) = inputCinemaParams()
    val cinema = createCinemaHole(lines, seats)
    choose(cinema)
}

fun choose(cinema: MutableList<MutableList<String>>) {
    do {
        menuPrint()
        when (readln().toInt()){
            1 -> seatsPrint(cinema)
            2 -> seatAdd(cinema)
            3 -> statistics(cinema)
            else -> break
        }
    } while (true)
}

fun menuPrint() {
    println()
    println("1. Show the seats")
    println("2. Buy a ticket")
    println("3. Statistics")
    println("0. Exit")
}

fun seatAdd(cinema: MutableList<MutableList<String>>) {
    val (row, seat) = inputTicket()
    try {
        paySeat(cinema, row, seat)
    } catch (e: InputMismatchException) {
        println("That ticket has already been purchased!")
        seatAdd(cinema)
    } catch (e: IndexOutOfBoundsException) {
        println("Wrong input!")
        seatAdd(cinema)
    }
    println("Ticket price: $${ticketPrice(cinema.size, cinema.first().size, row)}")
}

fun statistics(cinema: MutableList<MutableList<String>>) {
    var payed = 0
    var currentIncome = 0
    var totalIncome = 0
    for (row in cinema.indices) {
        for (seat in cinema[row].indices) {
            val price = ticketPrice(cinema.size, cinema.first().size, row)
            totalIncome += price
            if (cinema[row][seat] == PAYED) {
                payed++
                currentIncome += price
            }
        }
    }
    val percentage = payed.toDouble() * HUNDRED / (cinema.size * cinema.first().size)
    println()
    println("Number of purchased tickets: $payed")
    println("Percentage: ${formatTwoDecimals(percentage)}%")
    println("Current income: $$currentIncome")
    println("Total income: $$totalIncome")
}

fun inputCinemaParams(): List<Int> {
    println("Enter the number of rows:")
    val lines = readln().toInt()
    println("Enter the number of seats in each row:")
    val seats = readln().toInt()
    println()
    return listOf(lines, seats)
}

fun createCinemaHole(lines: Int, seats: Int): MutableList<MutableList<String>> {
    return MutableList(lines) { MutableList(seats) { MARK } }
}

fun inputTicket(): List<Int> {
    println()
    println("Enter a row number:")
    val row = readln().toInt() - 1
    println("Enter a seat number in that row:")
    val seat = readln().toInt() - 1
    return listOf(row, seat)
}

fun ticketPrice(lines: Int, seats: Int, row: Int): Int {
    return if (lines * seats > 60 && row + 1 > lines / 2) {
        LOW_COST
    } else {
        FULL_COST
    }
}

fun seatsPrint(cinema: MutableList<MutableList<String>>) {
    println()
    println("Cinema:")
    for (i in 0..cinema.first().size) {
        print(if (i == 0) " " else " $i")
    }
    println()
    for (i in cinema.indices) {
        println("${i + 1} ${cinema[i].joinToString(" ")}")
    }
}

fun formatTwoDecimals(percentage: Double): String {
    return String.format(locale = Locale.US,"%.2f", percentage)
}

fun paySeat(cinema: MutableList<MutableList<String>>, row: Int, seat: Int) {
    if (cinema[row][seat] == PAYED) {
        throw InputMismatchException()
    } else {
        cinema[row][seat] = PAYED
    }
}