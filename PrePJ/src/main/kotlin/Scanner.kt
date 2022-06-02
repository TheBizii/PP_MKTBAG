package ppmktbag

import models.*
import java.io.InputStream
import java.util.*

data class Token(val value: String, val lexeme: String, val startRow: Int, val startColumn: Int)

class Scanner(private val automaton: Automaton, private val stream: InputStream/*, private val debug: Boolean*/) {
    private var state = automaton.startState
    private var last: Int? = null
    private var buffer = LinkedList<Byte>()
    private var row = 1
    private var column = 1
    private var token = Token("EOF", "EOF", row, column)

    init {
    }

    fun start(): City? {
        //if (debug) printMethod("start")
        try {
            nextToken()
            val city = City("", mutableListOf(), mutableListOf())
            val roads = mutableListOf<Road>()
            val estates = mutableListOf<Estate>()
            cityDef(city)
            defintionBlocks(roads, estates)
            city.roads.addAll(roads)
            city.estates.addAll(estates)

            if (token.value != "EOF") throw Error("reject")
            println("accept")

            return city
        } catch (err: Error) {
            println(err)
        }

        return null
    }

    private fun cityDef(city: City) {
        //if (debug) printMethod("cityDef")
        if (token.value != "city") {
            throw Error("reject")
        }
        nextToken()
        if (token.value != "arrow") {
            throw Error("reject")
        }
        nextToken()
        if (token.value != "string") {
            throw Error("reject")
        }
        city.name = token.lexeme
        nextToken()
        if (token.value != "comma") {
            throw Error("reject")
        }
        nextToken()
    }

    private fun defintionBlocks(roads: MutableList<Road>, estates: MutableList<Estate>) {
        //if (debug) printMethod("definitionBlocks")
        val outVariables = mutableListOf<Variable>()
        variablesBlock(outVariables)
        roadsBlock(roads, outVariables)
        estatesBlock(estates, outVariables)
    }

    private fun variablesBlock(outVariables: MutableList<Variable>) {
        //if (debug) printMethod("variablesBlock")
        if (token.value != "variables") {
            throw Error("reject")
        }
        nextToken()
        if (token.value != "arrow") {
            throw Error("reject")
        }
        nextToken()
        if (token.value != "lcparen") {
            throw Error("reject")
        }
        nextToken()
        val inVariables = mutableListOf<Variable>()
        val oVariables = mutableListOf<Variable>()
        variableDefinitions(inVariables, oVariables)
        outVariables.clear()
        outVariables.addAll(oVariables)
        if (token.value != "rcparen") {
            throw Error("reject")
        }
        nextToken()
        if (token.value != "comma") {
            throw Error("reject")
        }
        nextToken()
    }

    private fun variableDefinitions(inVariables: MutableList<Variable>, outVariables: MutableList<Variable>) {
        //if (debug) printMethod("variableDefinitions")
        if (token.value == "var") {
            val oVariables = mutableListOf<Variable>()
            variableDef(inVariables, oVariables)
            outVariables.clear()
            outVariables.addAll(oVariables)
        } else {
            outVariables.clear()
            outVariables.addAll(inVariables)
        }
    }

    private fun variableDef(inVariables: MutableList<Variable>, outVariables: MutableList<Variable>) {
        //if (debug) printMethod("variableDef")
        val variable = Variable("", null)
        variable.name = token.lexeme
        nextToken()
        if (token.value != "as") {
            throw Error("reject")
        }
        nextToken()
        typeDef(variable)
        inVariables.add(variable)
        if (token.value != "comma") {
            throw Error("reject")
        }
        nextToken()
        val oVariables = mutableListOf<Variable>()
        variableDefinitions(inVariables, oVariables)
        outVariables.clear()
        outVariables.addAll(oVariables)
    }

    private fun typeDef(variable: Variable) {
        //if (debug) printMethod("typeDef")
        if (token.value == "rk_float") {
            nextToken()
            if (token.value != "arrow") {
                throw Error("reject")
            }
            nextToken()
            if (token.value != "float") {
                throw Error("reject")
            }
            variable.value = token.lexeme.toFloat()
            nextToken()
        } else if (token.value == "rk_string") {
            nextToken()
            if (token.value != "arrow") {
                throw Error("reject")
            }
            nextToken()
            if (token.value != "string") {
                throw Error("reject")
            }
            variable.value = token.lexeme
            nextToken()
        } else {
            throw Error("reject")
        }
    }

    private fun roadsBlock(roads: MutableList<Road>, inVariables: MutableList<Variable>) {
        //if (debug) printMethod("roadsBlock")
        if (token.value != "roads") {
            throw Error("reject")
        }
        nextToken()
        if (token.value != "arrow") {
            throw Error("reject")
        }
        nextToken()
        if (token.value != "lcparen") {
            throw Error("reject")
        }
        nextToken()
        val outRoads = mutableListOf<Road>()
        val inRoads = mutableListOf<Road>()
        roadDefinitions(inRoads, outRoads, inVariables)
        if (token.value != "rcparen") {
            throw Error("reject")
        }
        nextToken()
        if (token.value != "comma") {
            throw Error("reject")
        }
        nextToken()
        roads.clear()
        roads.addAll(outRoads)
    }

    private fun roadDefinitions(
        inRoads: MutableList<Road>,
        outRoads: MutableList<Road>,
        inVariables: MutableList<Variable>
    ) {
        //if (debug) printMethod("roadDefinitions")
        if (token.value == "var" || token.value == "string") {
            val oRoads = mutableListOf<Road>()
            roadDef(inRoads, oRoads, inVariables)
            outRoads.clear()
            outRoads.addAll(oRoads)
        } else {
            outRoads.clear()
            outRoads.addAll(inRoads)
        }
    }

    private fun roadDef(inRoads: MutableList<Road>, outRoads: MutableList<Road>, inVariables: MutableList<Variable>) {
        //if (debug) printMethod("roadDef")

        val name = if (token.value == "var") {
            val variable = inVariables.find { it.name == token.lexeme }
            if (variable?.value !is String) {
                throw Error("reject")
            }
            variable.value.toString()
        } else {
            token.lexeme
        }
        nextToken()
        if (token.value != "as") {
            throw Error("reject")
        }
        nextToken()
        // TODO: Check if Polygons are necessary for roads
        // TODO: Delete 3 lines below
        if (token.value != "line") {
            throw Error("reject")
        }
        featureType()
        if (token.value != "arrow") {
            throw Error("reject")
        }
        nextToken()
        if (token.value != "lsparen") {
            throw Error("reject")
        }
        nextToken()
        val points = mutableListOf<Point>()
        pointList(points, inVariables)
        val road = Road(name, points)
        if (token.value != "rsparen") {
            throw Error("reject")
        }
        nextToken()
        if (token.value != "comma") {
            throw Error("reject")
        }
        nextToken()
        inRoads.add(road)
        val oRoads = mutableListOf<Road>()
        roadDefinitions(inRoads, oRoads, inVariables)
        outRoads.clear()
        outRoads.addAll(oRoads)
    }

    private fun estatesBlock(estates: MutableList<Estate>, inVariables: MutableList<Variable>) {
        //if (debug) printMethod("estatesBlock")
        if (token.value != "estates") {
            throw Error("reject")
        }
        nextToken()
        if (token.value != "arrow") {
            throw Error("reject")
        }
        nextToken()
        if (token.value != "lcparen") {
            throw Error("reject")
        }
        nextToken()
        val outEstates = mutableListOf<Estate>()
        val inEstates = mutableListOf<Estate>()
        estateDefinitions(inEstates, outEstates, inVariables)
        if (token.value != "rcparen") {
            throw Error("reject")
        }
        nextToken()
        if (token.value != "comma") {
            throw Error("reject")
        }
        nextToken()
        estates.clear()
        estates.addAll(outEstates)
    }

    private fun estateDefinitions(
        inEstates: MutableList<Estate>,
        outEstates: MutableList<Estate>,
        inVariables: MutableList<Variable>
    ) {
        //if (debug) printMethod("estateDefinitions")
        if (token.value == "var" || token.value == "string") {
            val oEstates = mutableListOf<Estate>()
            estateDef(inEstates, oEstates, inVariables)
            outEstates.clear()
            outEstates.addAll(oEstates)
        } else {
            outEstates.clear()
            outEstates.addAll(inEstates)
        }
    }

    private fun estateDef(
        inEstates: MutableList<Estate>,
        outEstates: MutableList<Estate>,
        inVariables: MutableList<Variable>
    ) {
        //if (debug) printMethod("estateDef")
        val name = if (token.value == "var") {
            val variable = inVariables.find { it.name == token.lexeme }
            if (variable?.value !is String) {
                throw Error("reject")
            }
            variable.value.toString()
        } else {
            token.lexeme
        }
        nextToken()
        if (token.value != "as") {
            throw Error("reject")
        }
        nextToken()
        // TODO: Check if Polygons are necessary for roads
        // TODO: Delete 3 lines below
        if (token.value != "polygon") {
            throw Error("reject")
        }
        featureType()
        if (token.value != "arrow") {
            throw Error("reject")
        }
        nextToken()
        if (token.value != "lsparen") {
            throw Error("reject")
        }
        nextToken()
        val points = mutableListOf<Point>()
        pointList(points, inVariables)
        val estate = Estate(name, points)
        if (token.value != "rsparen") {
            throw Error("reject")
        }
        nextToken()
        if (token.value != "comma") {
            throw Error("reject")
        }
        nextToken()
        inEstates.add(estate)
        val oEstates = mutableListOf<Estate>()
        estateDefinitions(inEstates, oEstates, inVariables)
        outEstates.clear()
        outEstates.addAll(oEstates)
    }


    private fun featureType() {
        //if (debug) printMethod("featureType")
        if (token.value == "line") {
            nextToken()
        } else if (token.value == "polygon") {
            nextToken()
        } else {
            throw Error("reject")
        }
    }

    private fun pointList(outPoints: MutableList<Point>, inVariables: MutableList<Variable>) {
        //if (debug) printMethod("pointList")
        val pointOne = Point(0.0, 0.0)
        val pointTwo = Point(0.0, 0.0)
        val otherPoints = mutableListOf<Point>()
        val inPoints = mutableListOf<Point>()
        point(pointOne, inVariables)
        point(pointTwo, inVariables)
        additionalPoints(otherPoints, inVariables, inPoints)

        outPoints.add(pointOne)
        outPoints.add(pointTwo)
        outPoints.addAll(otherPoints)
    }

    private fun additionalPoints(
        otherPoints: MutableList<Point>,
        inVariables: MutableList<Variable>,
        inPoints: MutableList<Point>
    ) {
        //if (debug) printMethod("additionalPoints")
        if (token.value == "point") {
            val point = Point(0.0, 0.0)
            point(point, inVariables)
            inPoints.add(point)
            additionalPoints(otherPoints, inVariables, inPoints)
        } else {
            otherPoints.clear()
            otherPoints.addAll(inPoints)
        }
    }

    private fun point(point: Point, inVariables: MutableList<Variable>) {
        //if (debug) printMethod("point")
        if (token.value != "point") {
            throw Error("reject")
        }
        nextToken()
        if (token.value != "lparen") {
            throw Error("reject")
        }
        nextToken()
        val x = Variable("x", 0.0)
        pointArg(x, inVariables)
        point.x = x.value.toString().toDouble()
        if (token.value != "comma") {
            throw Error("reject")
        }
        nextToken()
        val y = Variable("y", 0.0)
        pointArg(y, inVariables)
        point.y = y.value.toString().toDouble()
        if (token.value != "rparen") {
            throw Error("reject")
        }
        nextToken()
        if (token.value != "comma") {
            throw Error("reject")
        }
        nextToken()
    }

    private fun pointArg(x: Variable, inVariables: MutableList<Variable>) {
        //if (debug) printMethod("pointArg")
        if (token.value == "var") {
            val variable = inVariables.find { it.name == token.lexeme }
            if (variable?.value !is Float) {
                throw Error("reject")
            }
            x.value = variable.value
            nextToken()
        } else if (token.value == "float") {
            x.value = token.lexeme.toDouble()
            nextToken()
        } else {
            throw Error("reject")
        }
    }

    private fun updatePosition(symbol: Int) {
        if (symbol == NEWLINE) {
            row += 1
            column = 1
        } else {
            column += 1
        }
    }

    private fun getValue(): String {
        var symbol = last ?: stream.read()
        state = automaton.startState

        while (true) {
            updatePosition(symbol)

            val nextState = automaton.next(state, symbol)
            if (nextState == ERROR_STATE) {
                if (automaton.finalStates.contains(state)) {
                    last = symbol
                    return automaton.value(state)
                } else throw Error("Invalid pattern at ${row}:${column} ${token.lexeme}")
            }
            state = nextState
            buffer.add(symbol.toByte())
            symbol = stream.read()
        }
    }

    private fun eof(): Boolean =
        last == EOF_SYMBOL

    fun getToken(): Token {
        if (eof()) return Token("EOF", "EOF", row, column)

        val startRow = row
        val startColumn = column
        buffer.clear()

        val value = getValue()
        return if (value == SKIP_VALUE)
            getToken()
        else
            Token(value, String(buffer.toByteArray()), startRow, startColumn)
    }

    private fun nextToken() {
        token = getToken()
        //if (debug) println(token.value)
    }

    /*private fun printMethod(smth:String) {
      println("METHOD: $smth")
    }*/
}
