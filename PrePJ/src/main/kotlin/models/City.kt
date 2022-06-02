package models

data class City(var name: String = "", var roads: MutableList<Road>, var estates: MutableList<Estate>) {
    override fun toString(): String {
        var ret = "city -> \"$name\",\n"
        ret += "variables -> {},\n"

        ret += "roads -> {\n"
        for (road in roads) {
            ret += road
        }
        ret += "},\n"

        ret += "estates -> {\n"
        for (estate in estates) {
            ret += estate
        }
        ret += "},"

        return ret
    }
}