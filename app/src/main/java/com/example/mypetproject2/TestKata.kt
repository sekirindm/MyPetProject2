package com.example.mypetproject2

import java.util.Locale
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sqrt

fun main() {
//    val arr = intArrayOf(43, 1, 5, 1)
//    for (i in arr.sortedArray())
//    println(
//        grow(
//            intArrayOf(1, 6, 3, 7, 7
//    )
//        )
//    )
//    print(
//        countPositivesSumNegatives(
//            arrayOf(
//                0, 2, 3, 0, 5, 6, 7, 8, 9, 10, -11, -12, -13, -14
//            )
//        ).toList()
//    )

    print(("bitcoin take over the world maybe who knows perhaps"))
//
//    print(romanToInt("IV"))
////    print(persistence(999))
//
//    val st = "bsjq".map { it } == "qbsj".map { it }
    val st = "abcde"
    val str = st.sumBy { it - 'a' + 1 }
//    var s1 = arrayOf<String>("hoqq", "bbllkw", "oox", "ejjuyyy", "plmiis", "xxxzgpsssa", "xxwwkktt", "znnnnfqknaz", "qqquuhii", "dvvvwz")
//    st.reduce { acc, c -> acc + c.toInt() }
//    var s2 = arrayOf<String>("cccooommaaqqoxii", "gggqaffhhh", "tttoowwwmmww")
//    print(predictAge(65, 60, 75, 55, 60, 63, 64, 45))


//    print(mirror("Hello world"))
//    print(deleteNth(intArrayOf(1, 1, 3, 3, 7, 2, 2, 2, 2), 3))
    var s = "a"
//    print(getCount("abracadabra"))
//    s.forEach {
//        s += if (it.code%2 == 1) {
//            it.uppercase()
//        } else {
//            it.lowercase()
//        }
//
//    }


}

fun mxdiflg(a1: Array<String>, a2: Array<String>): Int {
    return Math.abs(a1.max().length - a2.max().length)
}
//fun computeDepth(n: Int): Int {
//    var c = n
//
//}

fun prevMultOfThree(n: Int): Int? {
    var count = n
    while (n % 3 != 0) {
        count -= count.toString().takeLast(1).toInt()

    }
    if (count.toString().length == 1 && count % 3 != 0) {
        return null
    }
    return count
}

fun hero(bullets: Int, dragons: Int) = bullets / 2 > dragons

fun reverseLetter(str: String) = str.replace(Regex("[A-Za-z]"), "").reversed()
//fun wave(str: String): List<String> {
//
//   return
//}
//fun wave(str: String) = str.indices.map { str.take(it) + str.drop(it).capitalize(Locale.getDefault()) }.filter { it != str }

//fun persistence(num: Int) : Int {
//    var count = 0
//    num.toString().mapIndexed { index, c -> c }
//
//}
//fun mirror(text: String): String {
//    val s = text.split(" ")
//    return if (s.size > 1) {
//        "*********\n* ${s.first().reversed()} *\n* ${s.last().reversed()} *\n*********"
//    }else {
//        "************\n* ${text.reversed()} *\n************"
//    }
//}
//fun mirror(text: String): String {
//    var str = ""
//    val s = text.split(" ")
//    val maxLen = s.maxByOrNull { it.length }?.length ?: 0
//
//    for (i in s) {
//        val reversed = i.reversed()
//        val padding = " ".repeat(maxLen - reversed.length)
//        str += "\n* $reversed$padding *"
//    }
//
//    val border = "*".repeat(maxLen + 4)
//
//    return if (s.size > 1)  "$border$str\n$border" else "$border\n* ${text.reversed()} *\n$border"
//}

fun mirror(text: String): String {
    val lines = text.split(" ")
    val length = lines.map { it.length }.max() ?: 0
    val mirrored = lines.map { "* " + it.reversed().padEnd(length, ' ') + " *" }
    return mirrored.joinToString("\n", "*".repeat(length + 4) + "\n", "\n" + "*".repeat(length + 4))
}

operator fun kotlin.Int.Companion.invoke(s: String): Int = s.toInt()
//fun persistence(num: Int) = generateSequence(num) {
//    it.toString().map(Character::getNumericValue).reduce { mult, element -> mult * element }
//}.takeWhile { it > 9 }.count()
fun containAllRots(strng: String, arr: ArrayList<String>): Boolean {
    return arr.map { it.chars() == strng.chars() }.first()
}


//fun high(str: String) : String {
//    var st = ""
//    val splitStr = str.split(" ")
//
////    for (i in splitStr) {
////        when (i.max()) {
////
////        }
////    }
//    return splitStr.map { it.max()}.max()
//}

fun high(str: String): String {
    return str.split(' ').maxBy { it.sumOf { it - 'a' + 1 } }!!
}

//fun findShort(s: String):Int  {
//
//    return s.split(" ").minByOrNull { it.count() }!!.count()
//}

fun findShort(s: String): Int = s.split(" ").minOf{it}.count()

fun predictAge(
    age1: Int,
    age2: Int,
    age3: Int,
    age4: Int,
    age5: Int,
    age6: Int,
    age7: Int,
    age8: Int
): Int {
    val list = listOf(age1, age2, age3, age4, age5, age6, age7, age8)
    val sum = list.sumOf { it * it }
    return sqrt(sum.toDouble()).toInt() / 2
}

fun repeatStr(r: Int, str: String): String = str.repeat(r)


fun litres(time: Double): Int {
    return (time / 0.5).toInt()
}

fun romanToInt(s: String): Int {
    var count = 0
    s.map {
        count += when (it) {
            'I' -> 1
            'V' -> 5
            'X' -> 10
            'L' -> 50
            'C' -> 100
            'D' -> 500
            'M' -> 1000

            else -> {
                0
            }
        }
    }
    return count
}

fun convert(b: Boolean): String {
    return b.toString()
}

fun makeNegative(x: Int) = if (x > 0) -x else x

fun countPositivesSumNegatives(input: Array<Int>?): Array<Int> {
    if (input.isNullOrEmpty()) {
        return emptyList<Int>().toTypedArray()
    }
    val c = input.count { it > 0 }
    val s = input.filter { it < 0 }.sum()
    return arrayOf(c, s)
}

//fun twoSum(nums: IntArray, target: Int): IntArray {
//return nums.mapIndexed { index, i ->  }
//}
//fun evenOrOdd(number: Int) = if (number%2==0) "Even" else "Odd"

//fun getCount(str : String) : Int {
//    val volume = listOf("a", "e", "i", "o", "u")
//    return str.count { c -> volume.contains(c.toString()) }
//}
//fun getCount(str : String) = str.count { it in "aeiou" }
//fun noSpace(x: String) = x.replace(" ", "")
//fun past(h: Int, m: Int, s: Int) = s * 1000 + m * 60000 + h * 60 * 60000
//    print(s(listOf("1","-11","-12","22","100","-30","2"), 3))
//    val list = paronymList.random().first
//    println(evenNumbers(listOf(1,2,3,4,5,7,4,7,9,10,4,6,8), 4))
//    println(points(listOf("3:1", "3:1", "3:1", "3:4", "3:1", "3:1", "3:1", "3:1", "3:1", "3:1")))
//    println(leaderBoard("Dimas", 36097, 20000))
//////    println((36097 - 20000)/3)
//    println(makeComplement("CATG")) // gtca
//    println(solution(20))

//fun findSmallestInt(nums: List<Int>): Int {
//    return nums.min()
//}

//fun deleteNth(elements: IntArray, maxOcurrences: Int) = elements.distinct().toIntArray().flatMap { element ->
//    elements.toTypedArray().filter { it == element }.toIntArray().take(maxOcurrences)
//}

//fun s (items: List<String>, n: Int): List<Int> {
//   return items.filter { it.toInt()>0 }.take(n)
//}
//fun high(str: String): String {
//    return str.split(" ").sortedWith(compareBy { it.toString() }).toString()
//}
//
//fun capitalize(text: String): List<String> {
//    val list = mutableListOf<String>()
//////    text.filter { text.indices %2 == 0 }.uppercase()
////    var t = text
//
//
//    text.forEach {
//      t = if (it.code%2 == 1) {
//            it.uppercase()
//        } else {
//            it.lowercase()
//        }
//        list.add())
//    }
//    return list

//    for (i in text.indices) {
//        if (i %2 == 1) text.map {list.add( it.uppercase()) } else text.map { list.add(it.lowercase())}
//    }
//    return list
//}
//
//fun simpleMultiplication(n: Int) = if (n%2==0) n*8 else n*9


//fun stringExpansion(s: String): String {
//   s.forEach {
//
//   }
//}


//fun twiceAsOld(dadYearsOld: Int, sonYearsOld: Int) = abs(dadYearsOld - (sonYearsOld * 2))
//
//fun digitize(n: Long): IntArray = n.toString().reversed().map { it.toString().toInt() }.toIntArray()


//fun tankVol(h: Int, d: Int, vt: Int): Int {
//    val b = 1 - (h / (d / 2)).toDouble()
//    val a = acos(b)
//    val ost = d * (a - b * sqrt(1 - b))
//    return if (h < d / 2) {
//        vt - ost.toInt()
//    } else {
//        (Math.PI * d - ost).toInt()
//    }
//}

//fun stringExpansion(s: String): String {
//    s.mapIndexed { index, c -> if (index) }
//}
//fun invert(arr: IntArray): IntArray = arr.map { it.unaryMinus() }.toIntArray()

//fun feast(beast: String, dish: String)= beast.first() == dish.first() && beast.last() == dish.last()
//
//fun sum(numbers: IntArray)=numbers.filter { it>=0 }.sumBy { it }
//
//fun loveFun(flowerA: Int, flowerB: Int) = (flowerA%2==0) == (flowerB%2==1)
//
//fun smallEnough(a : IntArray, limit : Int)= a.all { it == limit || a.max() <= limit}


//fun evenNumbers(array: List<Int>, number: Int)= array.reversed().filter { it % 2 == 0 }.take(number).reversed()
//fun evenNumbers(array: List<Int>, number: Int) = array.filter{ it % 2 == 0 }.takeLast(number)

//fun extractFileName(dirtyFileName: String): String {
//    return dirtyFileName.split(Regex("_"), 2).drop(1).toString().reversed().split(Regex("[.]"), 2).drop(1).toString().reversed().replace("]", "").replace("[", "")
//
//}
//fun extractFileName(self:String)=self.substringAfter("_").substringBeforeLast(".")

//fun century(number: Int) = if (number%100 == 0) number/100 else number/100 + 1
//fun century(number: Int) = (number +99) / 100

//fun points(games: List<String>): Int {
//    var count = 0
//    games.map { count += if (it.first() > it.last()) 3 else if (it.first() < it.last()) 0 else 1 }
//    return count
//}
//fun points(games: List<String>) =
//    games.sumBy {
//        val (x, y) = it.split(":")
//        when {
//            x > y -> 3
//            x < y -> 0
//            else -> 1
//        }
//    }

//fun alphabetWar(fight: String): String {
//    var left = 0
//    var right = 0
//    fight.map {
//      left +=  when (it) {
//            'w' -> 4
//            'p' -> 3
//            'b' -> 2
//            's' -> 1
//          else -> 0
//        }
//        right += when(it) {
//            'm' -> 4
//            'q' -> 3
//            'd' -> 2
//            'z' -> 1
//            else -> 0
//        }
//    }
//    return if (left > right) {
//        "Left side wins!"
//    } else if (left < right) {
//        "Right side wins!"
//    } else {
//        "Let's fight again!"
//    }
//}

//fun r() {
//    val scope = CoroutineScope(Dispatchers.Default)
//    scope.launch {
//        coroutineScope {
//
//        }
//    }
//}


//fun reverseSeq(n: Int): List<Int> {
//    val list = mutableListOf<Int>()
//    var we = n
//    while (we!=0) {
//
//        list.add(we)
//        we-1
//
//    }
//    return list
//}
//fun reverseSeq(n: Int) = n.downTo(1).toList()

//fun calculateYears(years: Int): Array<Int> {
//    val dog = 15
//    val cat = 15
//    var list = arrayOf<Int>(0,0,0)
//    if (years == 1) {
//       list += arrayOf(years, cat, dog)
//    }
//    if (years ==2) {
//       list += arrayOf(years, cat+9, dog+9)
//
//    }
//    if(years>2) {
//        list += arrayOf(years,
//            cat+9+(4*(years-2)),
//            dog+9+(5*(years-2)))
//    }
//    return list;
//}

//fun calculateYears(years: Int) =
//    when(years) {
//        1 -> arrayOf(1, 15,15)
//        2 -> arrayOf(years, 24, 24)
//        else -> arrayOf(years, 24+(4*(years-2)), 24+(5*(years-2)))
//    }

//fun areaOrPerimeter(l: Int, w: Int) = if (l == w) l * w else (l + w) * 2

//fun include(arr: IntArray, item : Int) = item in arr

//fun getAge(yearsOld: String) = yearsOld.take(1).toInt()

//fun maps(x: IntArray): IntArray {  <---- my practice
//    var array = intArrayOf()
//    for (i in x.toList()) {
//        array += i * 2
//    }            |
//    return array |
//}                |
//                 |
//fun maps(x : IntArray) = x.map { it * 2 }.toIntArray() <---- best practice
//fun replace(s: String) = s.replace(Regex("[aeuioAEUIO]"), "!")

//public fun sum(mixed: List<Any>) : Int {
//    var sum = 0
//    for (i in mixed)
//        when (i) {
//            is String -> sum += i.toInt()
//            is Int -> sum += i
//        }
//    return sum
//}

//    public fun sum(mixed: List<Any>) = mixed.sumBy { it.toString().toInt() }

//fun grow(arr: IntArray): Int {
//    var sum = 1
//    for (i in arr) {
//        sum *= i
//    }
//return sum
//}

//fun grow(arr: IntArray): Int = arr.reduce { a, b -> a * b }


// difference == 121; x == 40

//fun leaderBoard(user: String, userScore: Int, yourScore: Int): String {
//    val betaKata = 3
//    val difference = userScore - yourScore
//    val x = difference / betaKata
//    val u = difference%betaKata
//
//    if (userScore < yourScore) {
//        return "Winning!"
//    } else if (userScore == yourScore) {
//        return "Only need one!"
//    }
//    val result = "To beat $user's score, I must complete $x Beta kata and $u 8kyu kata."
//   return if (difference > 1000) {
//         result
//    } else {
//         "$result Dammit!"
//    }
//}

//fun leaderBoard(user:String, userScore:Int, yourScore:Int):String {
//    var str = ""
//    var x = 0
//    val betaKata = 3
//    val kue = 1
//    if (userScore > yourScore) {
//        val difference = userScore-yourScore
//
//        if (difference%betaKata == 0 && difference<1000) {
//            x += difference/betaKata
//            str = "To beat $user's score, I must complete $x Beta kata and $kue 8kyu kata."
//        } else  {
//            str = "To beat $user's score, I must complete $x Beta kata and ${kue+1} 8kyu kata."
//        }
//        return str
//        if (difference%betaKata == 0 && difference>1000) {
//            x += difference/betaKata
//            str = "To beat $user's score, I must complete $x Beta kata and $kue 8kyu kata. Dammit!"
//        } else {
//            str = "To beat $user's score, I must complete $x Beta kata and ${kue+1} 8kyu kata. Dammit!"
//        }
//        return str
//    }
//    if (userScore == yourScore) {
//        str += "Only need one!"
//    } else {
//        str += "Winning!"
//    }
//    return str
//}

//fun babyCount(x: String): Int? {
//    var t = 0
//    val r = arrayOf("f")
//    x.filter { it. }
//    return t
//}

//fun solution(number: Int): Int {
//    val x = number%3 == 0
//    val y = number%5 == 0
//    while ()
//    return x
//}

//fun duplicateCount(text: String): Int {
//   return text.count()
//}

//fun summation(n:Int)= n.downTo(1).sumOf { it }

//fun accum(s:String):String = s.mapIndexed { index, char -> char.uppercase() + char.toString().lowercase().repeat(index) }.joinToString("-")

//fun catMouse(s: String): String {
//    return if (s.length >= 5) {
//        "Escaped!"
//    } else {
//        "Caught!"
//    }
//}

//fun prevMultOfThree(n: Int): Int? {
//    var s = 0
//    while (n%3!=0) {
//        s  = (n.toString().removeRange(n.toString().lastIndex,n.toString().lastIndex+1 ).toInt())
//
//    }
//    return s
//}

//fun squareSum(n: Array<Int>) = n.reduce { acc, i -> acc.toDouble().pow(2).toInt() + i.toDouble().pow(2).toInt()}.toInt()
//fun squareSum(n: Array<Int>)= n.sumBy { it * it }
//fun makeComplement(dna : String) = dna.replace("A", "1").replace("C", "2").replace("T", "3").replace("G", "4").replace("1", "T").replace("2", "G").replace("3", "A").replace("4", "C")

//fun makeComplement(dna: String) = dna.map { when(it) {
//    'A' -> 'T'
//    'T' -> 'A'
//    'C' -> 'G'
//    'G' -> 'C'
//    else -> it
//} }.joinToString("")

//fun moveTen(s: String): String {КОРНЕВЫЕ
//   var a = "a"
//    var r = ""
//    for (i in s.toString()) {
//        if (i.toString() == "p" || i.toString() == "q" || i.toString() == "r" || i.toString() == "s" || i.toString() == "t" || i.toString() == "u" || i.toString() == "v" || i.toString() == "w" || i.toString() == "x" || i.toString() == "y" || i.toString() == "z") {
//
//            r = s.map { s.replace(it.toString(), a) + 10 }.toString()
//        }
//    }
//    return r
//}
//fun moveTen(s: String) = s.map { if (it < 'q') it + 10 else it - 16 }.joinToString("")
fun abbrevName(name: String): String {
    val s = name.split(" ")
    return (s.first().first() + "." + s.last().first()).uppercase()

}