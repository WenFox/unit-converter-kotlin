package converter

const val METER_IN_KILOMETER = 1000.0
const val METER_IN_CENTIMETER = 0.01
const val METER_IN_MILLIMETER = 0.001
const val METER_IN_MILE = 1609.35
const val METER_IN_YARD = 0.9144
const val METER_IN_FOOT = 0.3048
const val METER_IN_INCH = 0.0254

const val KILOGRAM_IN_GRAM = 1000
const val MILLIGRAM_IN_GRAM = 0.001
const val POUND_IN_GRAM = 453.592
const val OUNCE_IN_GRAM = 28.3495


const val TYPE_UNKNOWN = -1
const val TYPE_DISTANCE = 0
const val TYPE_WEIGHT = 1
const val TYPE_TEMPERATURE = 2


fun main() {
    var inputLine: String
    var resultValue = 0.0


    while (true) {
        print("Enter what you want to convert (or exit): ")
        inputLine = readLine()!!.lowercase()
        if (inputLine == "exit") break

        val (inputValue, inputUnit, inputToUnit) = parseInput(inputLine) ?: continue

        val unit = Units.getUnitByName(inputUnit)
        val toUnit = Units.getUnitByName(inputToUnit)

        val value = inputValue.toDouble()

        when (Converter.getUnitType(unit)) {
            TYPE_DISTANCE -> {
                if (value < 0) {
                    println("Length shouldn't be negative")
                    continue
                }
                resultValue = Converter().Distance(unit, value).convertTo(toUnit)!!
            }
            TYPE_WEIGHT -> {
                if (value < 0) {
                    println("Weight shouldn't be negative")
                    continue
                }
                resultValue = Converter().Weight(unit, value).convertTo(toUnit)!!
            }
            TYPE_TEMPERATURE -> {
                resultValue = when (toUnit) {
                    Units.KELVINS -> Converter().Temperature(unit, value)
                        .convertToKelvins()!!
                    Units.CELSIUS -> Converter().Temperature(unit, value)
                        .convertToCelsius()!!
                    else -> Converter().Temperature(unit, value).convertToFahrenheit()!!
                }
            }
            else -> {
                println("Parse error")
                continue
            }
        }

        println("$value ${Units.getUnitName(unit, value)} is $resultValue ${Units.getUnitName(toUnit, resultValue)}")
    }
}

fun verifyInput(list: MutableList<String>): Boolean {
    if (list.size != 4) {
        println("Parse error")
        return false
    }
    try {
        list[0].toDouble()
    } catch (e: NumberFormatException) {
        println("Parse error")
        return false
    }

    /*if (list[2] != "to" && list[2] != "in") {
        println("Parse error")
        return false
    }*/
    return true

}

fun parseInput(input: String): List<String>? {
    val list = input.replace("degrees", "").replace("degree", "").split(" ").toMutableList()
    list.removeAll { it == "" }
    if (verifyInput(list)) {
        val unit = Units.getUnitByName(list[1])
        val toUnit = Units.getUnitByName(list[3])
        if (unit == Units.UNKNOWN || toUnit == Units.UNKNOWN || Converter.getUnitType(unit) != Converter.getUnitType(
                toUnit
            )
        ) {
            println(
                "Conversion from ${Units.getUnitName(unit, 0.0)} to ${
                    Units.getUnitName(
                        toUnit,
                        0.0
                    )
                } is impossible"
            )
            return null
        }
        return listOf(list[0], list[1], list[3])
    }
    return null

}

