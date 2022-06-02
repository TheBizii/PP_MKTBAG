package models

data class Estate(var name: String, var points: MutableList<Point>) {
    override fun toString(): String {
        var ret = "\t\"$name\" as Polygon -> [\n"
        for (point in points) {
            ret += "\t\t$point\n"
        }
        ret += "\t],\n"

        return ret
    }
}