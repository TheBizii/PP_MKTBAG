package models

data class Point(var x: Double, var y: Double) {
    override fun toString(): String {
        return "point($x, $y),"
    }
}