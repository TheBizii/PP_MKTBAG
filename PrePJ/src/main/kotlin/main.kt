package ppmktbag

import java.io.File
import java.io.InputStream
import java.util.LinkedList

const val EOF_SYMBOL = -1
const val ERROR_STATE = 0
const val SKIP_VALUE = ""

const val NEWLINE = 10

interface Automaton {
  val states: Set<Int>
  val alphabet: IntRange
  fun next(state: Int, symbol: Int): Int
  fun value(state: Int): String
  val startState: Int
  val finalStates: Set<Int>
}

object Example : Automaton {
  override val states = setOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33)
  override val alphabet = 0 .. 255
  override val startState = 1
  override val finalStates = setOf(2, 4, 5, 7, 8, 11, 13, 15, 17, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33)

  private val numberOfStates = states.maxOrNull()!! + 1
  private val numberOfSymbols = alphabet.maxOrNull()!! + 1
  private val transitions = Array(numberOfStates) {IntArray(numberOfSymbols)}
  private val values: Array<String> = Array(numberOfStates) {""}

  private fun setTransition(from: Int, symbol: Char, to: Int) {
    transitions[from][symbol.toInt()] = to
  }

  private fun setTransition(from: Int, symbols: IntRange, to: Int) {
    for (symbol in symbols) {
      transitions[from][symbol] = to
    }
  }

  private fun setValue(state: Int, terminal: String) {
    values[state] = terminal
  }

  override fun next(state: Int, symbol: Int): Int =
    if (symbol == EOF_SYMBOL) ERROR_STATE
    else {
      assert(states.contains(state))
      assert(alphabet.contains(symbol))
      transitions[state][symbol]
    }

  override fun value(state: Int): String {
    assert(states.contains(state))
    return values[state]
  }

  init {
    /* RESERVED KEYWORDS */
    setTransition(1, 'c', 2)
    setTransition(2, 'i', 3)
    setTransition(3, 't', 4)
    setTransition(4, 'y', 5)

    setTransition(1, 'v', 6)
    setTransition(6, 'a', 7)
    setTransition(7, 'r', 8)
    setTransition(8, 'i', 9)
    setTransition(9, 'a', 10)
    setTransition(10, 'b', 11)
    setTransition(11, 'l', 12)
    setTransition(12, 'e', 13)
    setTransition(13, 's', 14)

    setTransition(1, 'e', 15)
    setTransition(15, 's', 16)
    setTransition(16, 't', 17)
    setTransition(17, 'a', 18)
    setTransition(18, 't', 19)
    setTransition(19, 'e', 20)
    setTransition(20, 's', 21)

    setTransition(1, 'r', 22)
    setTransition(22, 'o', 23)
    setTransition(23, 'a', 24)
    setTransition(24, 'd', 25)
    setTransition(25, 's', 26)

    setTransition(1, 'L', 27)
    setTransition(27, 'i', 28)
    setTransition(28, 'n', 29)
    setTransition(29, 'e', 30)

    setTransition(1, 'p', 31)
    setTransition(31, 'o', 32)
    setTransition(32, 'i', 33)
    setTransition(33, 'n', 34)
    setTransition(34, 't', 35)

    setTransition(1, 'P', 36)
    setTransition(36, 'o', 37)
    setTransition(37, 'l', 38)
    setTransition(38, 'y', 39)
    setTransition(39, 'g', 40)
    setTransition(40, 'o', 41)
    setTransition(41, 'n', 42)

    setTransition(1, 'a', 57)
    setTransition(57, 's', 58)
    /* END OF RESERVED KEYWORDS */

    /* SYMBOLS */
    setTransition(1, '-', 43)
    setTransition(43, '>', 44)

    setTransition(1, '{', 45)

    setTransition(1, '}', 46)

    setTransition(1, '[', 47)

    setTransition(1, ']', 48)

    setTransition(1, '(', 59)

    setTransition(1, ')', 60)
    /* END OF SYMBOLS */

    /* FLOAT */
    setTransition(1, 48..57, 50)
    setTransition(50, 48..57, 50)
    setTransition(50, '.', 51)
    setTransition(51, 48..57, 52)
    setTransition(52, 48..57, 52)
    /* END OF FLOAT */

    /* STRING */
    setTransition(1, '"', 53)
    setTransition(53, '"', 54)
    setTransition(53, 0..9, 55)
    setTransition(53, 11..12, 55)
    setTransition(53, 14..33, 55)
    setTransition(53, 35..127, 55)
    setTransition(55, 0..9, 55)
    setTransition(55, 11..12, 55)
    setTransition(55, 14..33, 55)
    setTransition(55, 35..127, 55)
    setTransition(55, '"', 56)
    /* END OF STRING */

    /* COMMA */
    setTransition(1, ',', 49)
    /* END OF COMMA */

    /* VARIABLE IDENTIFIER */
    setTransition(1, 65..75, 61)
    setTransition(1, 77..79, 61)
    setTransition(1, 81..90, 61)
    setTransition(1, 'b', 61)
    setTransition(1, 'd', 61)
    setTransition(1, 102..111, 61)
    setTransition(1, 'q', 61)
    setTransition(1, 115..117, 61)
    setTransition(1, 119..122, 61)
    setTransition(1, '_', 61)
    setTransition(61, 65..90, 61)
    setTransition(61, 97..122, 61)
    setTransition(61, 48..57, 61)
    setTransition(61, '_', 61)

    // c[^i]ty
    setTransition(2, 65..90, 61)
    setTransition(2, 97..104, 61)
    setTransition(2, 106..122, 61)
    setTransition(2, 48..57, 61)
    setTransition(2, '_', 61)
    // ci[^t]y
    setTransition(3, 65..90, 61)
    setTransition(3, 97..115, 61)
    setTransition(3, 117..122, 61)
    setTransition(3, 48..57, 61)
    setTransition(3, '_', 61)
    // cit[^y]
    setTransition(4, 65..90, 61)
    setTransition(4, 97..120, 61)
    setTransition(4, 'z', 61)
    setTransition(4, 48..57, 61)
    setTransition(4, '_', 61)
    // v[^a]riables
    setTransition(6, 65..90, 61)
    setTransition(6, 98..122, 61)
    setTransition(6, 48..57, 61)
    setTransition(6, '_', 61)
    // va[^r]iables
    setTransition(7, 65..90, 61)
    setTransition(7, 97..113, 61)
    setTransition(7, 115..122, 61)
    setTransition(7, 48..57, 61)
    setTransition(7, '_', 61)
    // var[^i]ables
    setTransition(8, 65..90, 61)
    setTransition(8, 97..104, 61)
    setTransition(8, 106..122, 61)
    setTransition(8, 48..57, 61)
    setTransition(8, '_', 61)
    // vari[^a]bles
    setTransition(9, 65..90, 61)
    setTransition(9, 98..122, 61)
    setTransition(9, 48..57, 61)
    setTransition(9, '_', 61)
    // varia[^b]les
    setTransition(10, 65..90, 61)
    setTransition(10, 'a', 61)
    setTransition(10, 99..122, 61)
    setTransition(10, 48..57, 61)
    setTransition(10, '_', 61)
    // variab[^l]es
    setTransition(11, 65..90, 61)
    setTransition(11, 97..107, 61)
    setTransition(11, 109..122, 61)
    setTransition(11, 48..57, 61)
    setTransition(11, '_', 61)
    // variabl[^e]s
    setTransition(12, 65..90, 61)
    setTransition(12, 97..100, 61)
    setTransition(12, 102..122, 61)
    setTransition(12, 48..57, 61)
    setTransition(12, '_', 61)
    // variable[^s]
    setTransition(13, 65..90, 61)
    setTransition(13, 97..114, 61)
    setTransition(13, 116..122, 61)
    setTransition(13, 48..57, 61)
    setTransition(13, '_', 61)
    // e[^s]tates
    setTransition(15, 65..90, 61)
    /* END OF VARIABLE IDENTIFIER */

    setValue(2, "float")
    setValue(4, "float")
    setValue(23, "variable")
    setValue(24, "variable")
    setValue(5, "plus")
    setValue(25, "minus")
    setValue(26, "times")
    setValue(27, "divide")
    setValue(28, "pow")
    setValue(29, "lparen")
    setValue(30, "rparen")
    setValue(31, "semi")
    setValue(7, "assign")
    setValue(11, "for")
    setValue(13, "do")
    setValue(15, "done")
    setValue(17, "to")
    setValue(22, "write")
  }
}

data class Token(val value: String, val lexeme: String, val startRow: Int, val startColumn: Int)

class Scanner(private val automaton: Automaton, private val stream: InputStream) {
  private var state = automaton.startState
  private var last: Int? = null
  private var buffer = LinkedList<Byte>()
  private var row = 1
  private var column = 1

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

  fun eof(): Boolean =
    last == EOF_SYMBOL

  fun getToken(): Token? {
    if (eof()) return null

    val startRow = row
    val startColumn = column
    buffer.clear()

    val value = getValue()
    return if (value == SKIP_VALUE)
      getToken()
    else
      Token(value, String(buffer.toByteArray()), startRow, startColumn)
  }
}

fun printTokens(scanner: Scanner) {
  val token = scanner.getToken()
  if (token != null) {
    print("${token.value}(\"${token.lexeme}\") ")
    printTokens(scanner)
  }
}

fun main(args: Array<String>) {
  for (file in args) {
    val scanner = Scanner(Example, File(file).inputStream())
    printTokens(scanner)
  }
}