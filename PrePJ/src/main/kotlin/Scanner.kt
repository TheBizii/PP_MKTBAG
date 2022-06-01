package ppmktbag

import java.io.InputStream
import java.util.*

data class Token(val value: String, val lexeme: String, val startRow: Int, val startColumn: Int)

class Scanner(private val automaton: Automaton, private val stream: InputStream, private val debug: Boolean) {
  private var state = automaton.startState
  private var last: Int? = null
  private var buffer = LinkedList<Byte>()
  private var row = 1
  private var column = 1
  private var token = Token("EOF", "EOF", row, column)

  init {
    try {
      nextToken()
      start()
      if (token.value != "EOF") throw Error("reject")
      println("accept")
    } catch (err: Error) {
      println(err.message)
    }
  }

  private fun start() {
    if (debug) println("start")
    cityDef()
    defintionBlocks()
  }

  private fun cityDef() {
    if (debug) println("cityDef")
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
    nextToken()
    if (token.value != "comma") {
      throw Error("reject")
    }
    nextToken()
  }

  private fun defintionBlocks() {
    if (debug) println("definitionBlocks")
    variablesBlock()
    roadsBlock()
    estatesBlock()
  }

  private fun variablesBlock() {
    if (debug) println("variablesBlock")
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
    variableDefinitions()
    if (token.value != "rcparen") {
      throw Error("reject")
    }
    nextToken()
    if (token.value != "comma") {
      throw Error("reject")
    }
    nextToken()
  }

  private fun variableDefinitions() {
    if (debug) println("variableDefinitions")
    if (token.value == "var") {
      variableDef()
    }
  }

  private fun variableDef() {
    if (debug) println("variableDef")
    nextToken()
    if (token.value != "as") {
      throw Error("reject")
    }
    nextToken()
    typeDef()
    if (token.value != "comma") {
      throw Error("reject")
    }
    nextToken()
    variableDefinitions()
  }

  private fun typeDef() {
    if (debug) println("typeDef")
    if (token.value == "rk_float") {
      nextToken()
      if (token.value != "arrow") {
        throw Error("reject")
      }
      nextToken()
      if (token.value != "float") {
        throw Error("reject")
      }
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
      nextToken()
    } else {
      throw Error("reject")
    }
  }

  private fun roadsBlock() {
    if (debug) println("roadsBlock")
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
    roadDefinitions()
    if (token.value != "rcparen") {
      throw Error("reject")
    }
    nextToken()
    if (token.value != "comma") {
      throw Error("reject")
    }
    nextToken()
  }

  private fun roadDefinitions() {
    if (debug) println("roadDefinitions")
    if (token.value == "var" || token.value == "string") {
      roadDef()
    }
  }

  private fun roadDef() {
    if (debug) println("roadDef")
    nextToken()
    if (token.value != "as") {
      throw Error("reject")
    }
    nextToken()
    featureType()
    if (token.value != "arrow") {
      throw Error("reject")
    }
    nextToken()
    if (token.value != "lsparen") {
      throw Error("reject")
    }
    nextToken()
    pointList()
    if (token.value != "rsparen") {
      throw Error("reject")
    }
    nextToken()
    if (token.value != "comma") {
      throw Error("reject")
    }
    nextToken()
    roadDefinitions()
  }

  private fun estatesBlock() {
    if (debug) println("estatesBlock")
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
    estateDefinitions()
    if (token.value != "rcparen") {
      throw Error("reject")
    }
    nextToken()
    if (token.value != "comma") {
      throw Error("reject")
    }
    nextToken()
  }

  private fun estateDefinitions() {
    if (debug) println("estateDefinitions")
    if (token.value == "var" || token.value == "string") {
      estateDef()
    }
  }

  private fun estateDef() {
    if (debug) println("estateDef")
    nextToken()
    if (token.value != "as") {
      throw Error("reject")
    }
    nextToken()
    featureType()
    if (token.value != "arrow") {
      throw Error("reject")
    }
    nextToken()
    if (token.value != "lsparen") {
      throw Error("reject")
    }
    nextToken()
    pointList()
    if (token.value != "rsparen") {
      throw Error("reject")
    }
    nextToken()
    if (token.value != "comma") {
      throw Error("reject")
    }
    nextToken()
    estateDefinitions()
  }


  private fun featureType() {
    if (debug) println("featureType")
    if (token.value == "line") {
      nextToken()
    } else if (token.value == "polygon") {
      nextToken()
    } else {
      throw Error("reject")
    }
  }

  private fun pointList() {
    if (debug) println("pointList")
    point()
    point()
    additionalPoints()
  }

  private fun additionalPoints() {
    if (debug) println("additionalPoints")
    if (token.value == "point") {
      point()
    }
  }

  private fun point() {
    if (debug) println("point")
    if (token.value != "point") {
      throw Error("reject")
    }
    nextToken()
    if (token.value != "lparen") {
      throw Error("reject")
    }
    nextToken()
    pointArg()
    if (token.value != "comma") {
      throw Error("reject")
    }
    nextToken()
    pointArg()
    if (token.value != "rparen") {
      throw Error("reject")
    }
    nextToken()
    if (token.value != "comma") {
      throw Error("reject")
    }
    nextToken()
  }

  private fun pointArg() {
    if (debug) println("pointArg")
    if (token.value == "var") {
      nextToken()
    } else if (token.value == "float") {
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
        } else throw Error("Invalid pattern at ${row}:${column}")
      }
      state = nextState
      buffer.add(symbol.toByte())
      symbol = stream.read()
    }
  }

  private fun eof(): Boolean =
    last == EOF_SYMBOL

  fun getToken(): Token {
    if (eof()) return Token( "EOF", "EOF", row, column)

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
    if (debug) println(token.value)
  }
}
