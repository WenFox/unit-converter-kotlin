package converter

const val KELVIN_TO_CELSIUS = 273.15
const val CELSIUS_TO_FAHRENHEIT = 9.0 / 5.0 + 32
const val KELVIN_TO_FAHRENHEIT = 9 / 5 - 459.67


enum class Units(val shortName: String, val unitName: String, val multipleName: String) {
    METER("m", "meter", "meters"),
    CENTIMETER("cm", "centimeter", "centimeters"),
    MILLIMETER("mm", "millimeter", "millimeters"),
    MILE("mi", "mile", "miles"),
    YARD("yd", "yard", "yards"),
    FOOT("ft", "foot", "feet"),
    INCH("in", "inch", "inches"),
    KILOMETER("km", "kilometer", "kilometers"),
    GRAM("g", "gram", "grams"),
    KILOGRAM("kg", "kilogram", "kilograms"),
    MILLIGRAM("mg", "milligram", "milligrams"),
    POUND("lb", "pound", "pounds"),
    OUNCE("oz", "ounce", "ounces"),
    CELSIUS("dc", "c", "celsius"),
    FAHRENHEIT("df", "f", "fahrenheit"),
    KELVINS("kelvin", "k", "kelvins"),
    UNKNOWN("", "???", "???");

    companion object {
        fun getUnitByName(n: String): Units {
            val lowerCaseName = n.lowercase()
            for (enum in values()) {
                if (enum.shortName == lowerCaseName || enum.unitName == lowerCaseName || enum.multipleName == lowerCaseName) {
                    return enum
                }
            }
            return UNKNOWN
        }

        fun getUnitName(unit: Units, count: Double = 0.0): String {
            if (Converter.getUnitType(unit) != TYPE_TEMPERATURE) {
                return if (count == 1.0) unit.unitName else unit.multipleName
            } else {
                return when (unit) {
                    CELSIUS -> if (count == 1.0) "degree Celsius" else "degrees Celsius"
                    KELVINS -> if (count == 1.0) "kelvin" else "kelvins"
                    FAHRENHEIT -> if (count == 1.0) "degree Fahrenheit" else "degrees Fahrenheit"
                    else -> ""
                }
            }
        }
    }


}

class Converter {
    companion object {
        fun getUnitType(unit: Units): Int {
            return when (unit) {
                Units.METER, Units.KILOMETER, Units.CENTIMETER, Units.MILLIMETER, Units.MILE, Units.YARD, Units.FOOT, Units.INCH -> TYPE_DISTANCE
                Units.GRAM, Units.KILOGRAM, Units.MILLIGRAM, Units.POUND, Units.OUNCE -> TYPE_WEIGHT
                Units.CELSIUS, Units.KELVINS, Units.FAHRENHEIT -> TYPE_TEMPERATURE
                else -> TYPE_UNKNOWN
            }
        }
    }

    inner class Weight(private val unit: Units, private val value: Double) {
        private fun convertToGram(): Double? {
            return when (unit) {
                Units.GRAM -> value
                Units.KILOGRAM -> value * KILOGRAM_IN_GRAM
                Units.MILLIGRAM -> value * MILLIGRAM_IN_GRAM
                Units.POUND -> value * POUND_IN_GRAM
                Units.OUNCE -> value * OUNCE_IN_GRAM
                else -> null
            }
        }

        fun convertTo(toUnit: Units): Double? {
            val gram = convertToGram()!!
            return when (toUnit) {
                Units.GRAM -> gram
                Units.KILOGRAM -> gram / KILOGRAM_IN_GRAM
                Units.MILLIGRAM -> gram / MILLIGRAM_IN_GRAM
                Units.POUND -> gram / POUND_IN_GRAM
                Units.OUNCE -> gram / OUNCE_IN_GRAM
                else -> null
            }
        }
    }

    inner class Distance(private val unit: Units, private val value: Double) {
        private fun convertToMeter(): Double? {
            return when (unit) {
                Units.METER -> value
                Units.KILOMETER -> value * METER_IN_KILOMETER
                Units.CENTIMETER -> value * METER_IN_CENTIMETER
                Units.MILLIMETER -> value * METER_IN_MILLIMETER
                Units.MILE -> value * METER_IN_MILE
                Units.YARD -> value * METER_IN_YARD
                Units.FOOT -> value * METER_IN_FOOT
                Units.INCH -> value * METER_IN_INCH
                else -> null
            }
        }

        fun convertTo(toUnit: Units): Double? {
            val meters = convertToMeter()!!
            return when (toUnit) {
                Units.METER -> meters
                Units.KILOMETER -> meters / METER_IN_KILOMETER
                Units.CENTIMETER -> meters / METER_IN_CENTIMETER
                Units.MILLIMETER -> meters / METER_IN_MILLIMETER
                Units.MILE -> meters / METER_IN_MILE
                Units.YARD -> meters / METER_IN_YARD
                Units.FOOT -> meters / METER_IN_FOOT
                Units.INCH -> meters / METER_IN_INCH
                else -> null
            }
        }
    }

    inner class Temperature(private val unit: Units, private val value: Double) {
        fun convertToCelsius(): Double? {
            return when (unit) {
                Units.CELSIUS -> value
                Units.KELVINS -> value - KELVIN_TO_CELSIUS
                Units.FAHRENHEIT -> (value - 32) * 5 / 9
                else -> null
            }
        }

        fun convertToFahrenheit(): Double? {
            return when (unit) {
                Units.CELSIUS -> value * 9.0 / 5.0 + 32
                Units.KELVINS -> value * 9 / 5 - 459.67
                Units.FAHRENHEIT -> value
                else -> null
            }
        }

        fun convertToKelvins(): Double? {
            return when (unit) {
                Units.CELSIUS -> value + KELVIN_TO_CELSIUS
                Units.KELVINS -> value
                Units.FAHRENHEIT -> (value + 459.67) * 5 / 9
                else -> null
            }
        }
    }
}