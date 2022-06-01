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
  override val states = (1..75).toSet()
  override val alphabet = 0 .. 255
  override val startState = 1
  override val finalStates = setOf(2, 5, 6, 14, 15, 21, 22, 26, 27, 30, 31, 35, 36, 42, 44, 45, 46, 47, 48, 49, 50, 52,
    54, 56, 57, 58, 59, 60, 61, 66, 72, 73, 74, 75)

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

    setTransition(1, 'F', 62)
    setTransition(62, 'l', 63)
    setTransition(63, 'o', 64)
    setTransition(64, 'a', 65)
    setTransition(65, 't', 66)

    setTransition(1, 'S', 67)
    setTransition(67, 't', 68)
    setTransition(68, 'r', 69)
    setTransition(69, 'i', 70)
    setTransition(70, 'n', 71)
    setTransition(71, 'g', 72)
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
    // 70, 76, 80, 83
    setTransition(1, 65..69, 61)
    setTransition(1, 71..75, 61)
    setTransition(1, 77..79, 61)
    setTransition(1, 81..82, 61)
    setTransition(1, 84..90, 61)
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
    // city[a-zA-Z0-9_]+
    setTransition(5, 65..90, 61)
    setTransition(5, 97..122, 61)
    setTransition(5, 48..57, 61)
    setTransition(5, '_', 61)
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
    // variables[a-zA-Z0-9_]+
    setTransition(14, 65..90, 61)
    setTransition(14, 97..122, 61)
    setTransition(14, 48..57, 61)
    setTransition(14, '_', 61)
    // e[^s]tates
    setTransition(15, 65..90, 61)
    setTransition(15, 97..114, 61)
    setTransition(15, 116..122, 61)
    setTransition(15, 48..57, 61)
    setTransition(15, '_', 61)
    // es[^t]ates
    setTransition(16, 65..90, 61)
    setTransition(16, 97..115, 61)
    setTransition(16, 117..122, 61)
    setTransition(16, 48..57, 61)
    setTransition(16, '_', 61)
    // est[^a]tes
    setTransition(17, 65..90, 61)
    setTransition(17, 98..122, 61)
    setTransition(17, 48..57, 61)
    setTransition(17, '_', 61)
    // esta[^t]es
    setTransition(18, 65..90, 61)
    setTransition(18, 97..115, 61)
    setTransition(18, 117..122, 61)
    setTransition(18, 48..57, 61)
    setTransition(18, '_', 61)
    // estat[^e]s
    setTransition(19, 65..90, 61)
    setTransition(19, 97..100, 61)
    setTransition(19, 102..122, 61)
    setTransition(19, 48..57, 61)
    setTransition(19, '_', 61)
    // estate[^s]
    setTransition(20, 65..90, 61)
    setTransition(20, 97..114, 61)
    setTransition(20, 116..122, 61)
    setTransition(20, 48..57, 61)
    setTransition(20, '_', 61)
    // estates[a-zA-Z0-9_]+
    setTransition(21, 65..90, 61)
    setTransition(21, 97..122, 61)
    setTransition(21, 48..57, 61)
    setTransition(21, '_', 61)
    // r[^o]ads
    setTransition(22, 65..90, 61)
    setTransition(22, 97..110, 61)
    setTransition(22, 112..122, 61)
    setTransition(22, 48..57, 61)
    setTransition(22, '_', 61)
    // ro[^a]ds
    setTransition(23, 65..90, 61)
    setTransition(23, 98..122, 61)
    setTransition(23, 48..57, 61)
    setTransition(23, '_', 61)
    // roa[^d]s
    setTransition(24, 65..90, 61)
    setTransition(24, 97..99, 61)
    setTransition(24, 101..122, 61)
    setTransition(24, 48..57, 61)
    setTransition(24, '_', 61)
    // road[^s]
    setTransition(25, 65..90, 61)
    setTransition(25, 97..114, 61)
    setTransition(25, 116..122, 61)
    setTransition(25, 48..57, 61)
    setTransition(25, '_', 61)
    // roads[a-zA-Z0-9_]+
    setTransition(26, 65..90, 61)
    setTransition(26, 97..122, 61)
    setTransition(26, 48..57, 61)
    setTransition(26, '_', 61)
    // L[^i]ne
    setTransition(27, 65..90, 61)
    setTransition(27, 97..104, 61)
    setTransition(27, 106..122, 61)
    setTransition(27, 48..57, 61)
    setTransition(27, '_', 61)
    // Li[^n]e
    setTransition(28, 65..90, 61)
    setTransition(28, 97..109, 61)
    setTransition(28, 111..122, 61)
    setTransition(28, 48..57, 61)
    setTransition(28, '_', 61)
    // Lin[^e]
    setTransition(29, 65..90, 61)
    setTransition(29, 97..100, 61)
    setTransition(29, 102..122, 61)
    setTransition(29, 48..57, 61)
    setTransition(29, '_', 61)
    // Line[a-zA-Z0-9_]+
    setTransition(30, 65..90, 61)
    setTransition(30, 97..122, 61)
    setTransition(30, 48..57, 61)
    setTransition(30, '_', 61)
    // P[^o]int
    setTransition(31, 65..90, 61)
    setTransition(31, 97..110, 61)
    setTransition(31, 112..122, 61)
    setTransition(31, 48..57, 61)
    setTransition(31, '_', 61)
    // Po[^i]nt
    setTransition(32, 65..90, 61)
    setTransition(32, 97..104, 61)
    setTransition(32, 106..122, 61)
    setTransition(32, 48..57, 61)
    setTransition(32, '_', 61)
    // Poi[^n]t
    setTransition(33, 65..90, 61)
    setTransition(33, 97..109, 61)
    setTransition(33, 111..122, 61)
    setTransition(33, 48..57, 61)
    setTransition(33, '_', 61)
    // Poin[^t]
    setTransition(34, 65..90, 61)
    setTransition(34, 97..115, 61)
    setTransition(34, 117..122, 61)
    setTransition(34, 48..57, 61)
    setTransition(34, '_', 61)
    // Point[a-zA-Z0-9_]+
    setTransition(35, 65..90, 61)
    setTransition(35, 97..122, 61)
    setTransition(35, 48..57, 61)
    setTransition(35, '_', 61)
    // P[^o]lygon
    setTransition(36, 65..90, 61)
    setTransition(36, 97..110, 61)
    setTransition(36, 112..122, 61)
    setTransition(36, 48..57, 61)
    setTransition(36, '_', 61)
    // Po[^l]ygon
    setTransition(37, 65..90, 61)
    setTransition(37, 97..107, 61)
    setTransition(37, 109..122, 61)
    setTransition(37, 48..57, 61)
    setTransition(37, '_', 61)
    // Pol[^y]gon
    setTransition(38, 65..90, 61)
    setTransition(38, 97..120, 61)
    setTransition(38, 'z', 61)
    setTransition(38, 48..57, 61)
    setTransition(38, '_', 61)
    // Poly[^g]on
    setTransition(39, 65..90, 61)
    setTransition(39, 97..102, 61)
    setTransition(39, 104..122, 61)
    setTransition(39, 48..57, 61)
    setTransition(39, '_', 61)
    // Polyg[^o]n
    setTransition(40, 65..90, 61)
    setTransition(40, 97..110, 61)
    setTransition(40, 112..122, 61)
    setTransition(40, 48..57, 61)
    setTransition(40, '_', 61)
    // Polygo[^n]
    setTransition(41, 65..90, 61)
    setTransition(41, 97..109, 61)
    setTransition(41, 111..122, 61)
    setTransition(41, 48..57, 61)
    setTransition(41, '_', 61)
    // Polygon[a-zA-Z0-9_]+
    setTransition(42, 65..90, 61)
    setTransition(42, 97..122, 61)
    setTransition(42, 48..57, 61)
    setTransition(42, '_', 61)
    // a[^s]
    setTransition(57, 65..90, 61)
    setTransition(57, 97..114, 61)
    setTransition(57, 116..122, 61)
    setTransition(57, 48..57, 61)
    setTransition(57, '_', 61)
    // as[a-zA-Z0-9_]+
    setTransition(58, 65..90, 61)
    setTransition(58, 97..122, 61)
    setTransition(58, 48..57, 61)
    setTransition(58, '_', 61)
    // F[^l]oat
    setTransition(62, 65..90, 61)
    setTransition(62, 97..107, 61)
    setTransition(62, 109..122, 61)
    setTransition(62, 48..57, 61)
    setTransition(62, '_', 61)
    // Fl[^o]at
    setTransition(63, 65..90, 61)
    setTransition(63, 97..110, 61)
    setTransition(63, 112..122, 61)
    setTransition(63, 48..57, 61)
    setTransition(63, '_', 61)
    // Flo[^a]t
    setTransition(64, 65..90, 61)
    setTransition(64, 98..122, 61)
    setTransition(64, 48..57, 61)
    setTransition(64, '_', 61)
    // Floa[^t]
    setTransition(65, 65..90, 61)
    setTransition(65, 97..115, 61)
    setTransition(65, 117..122, 61)
    setTransition(65, 48..57, 61)
    setTransition(65, '_', 61)
    // Float[a-zA-Z0-9_]+
    setTransition(66, 65..90, 61)
    setTransition(66, 97..122, 61)
    setTransition(66, 48..57, 61)
    setTransition(66, '_', 61)
    // S[^t]ring
    setTransition(67, 65..90, 61)
    setTransition(67, 97..115, 61)
    setTransition(67, 117..122, 61)
    setTransition(67, 48..57, 61)
    setTransition(67, '_', 61)
    // St[^r]ing
    setTransition(68, 65..90, 61)
    setTransition(68, 97..113, 61)
    setTransition(68, 115..122, 61)
    setTransition(68, 48..57, 61)
    setTransition(68, '_', 61)
    // Str[^i]ng
    setTransition(69, 65..90, 61)
    setTransition(69, 97..104, 61)
    setTransition(69, 106..122, 61)
    setTransition(69, 48..57, 61)
    setTransition(69, '_', 61)
    // Stri[^n]g
    setTransition(70, 65..90, 61)
    setTransition(70, 97..109, 61)
    setTransition(70, 111..122, 61)
    setTransition(70, 48..57, 61)
    setTransition(70, '_', 61)
    // Strin[^g]
    setTransition(71, 65..90, 61)
    setTransition(71, 97..102, 61)
    setTransition(71, 104..122, 61)
    setTransition(71, 48..57, 61)
    setTransition(71, '_', 61)
    // String[a-zA-Z0-9_]+
    setTransition(72, 65..90, 61)
    setTransition(72, 97..122, 61)
    setTransition(72, 48..57, 61)
    setTransition(72, '_', 61)
    /* END OF VARIABLE IDENTIFIER */

    /* SKIP VALUES */
    setTransition(1, ' ', 73)
    setTransition(1, '\t', 74)
    setTransition(1, '\n', 75)
    setTransition(73, ' ', 73)
    setTransition(74, '\t', 74)
    setTransition(75, '\n', 75)
    /* END OF SKIP VALUES */

    setValue(2, "var")
    setValue(5, "city")
    setValue(6, "var")
    setValue(14, "variables")
    setValue(15, "var")
    setValue(21, "estates")
    setValue(22, "var")
    setValue(26, "roads")
    setValue(27, "var")
    setValue(30, "line")
    setValue(31, "var")
    setValue(35, "point")
    setValue(36, "var")
    setValue(42, "polygon")
    setValue(44, "arrow")
    setValue(45, "lcparen")
    setValue(46, "rcparen")
    setValue(47, "lsparen")
    setValue(48, "rsparen")
    setValue(49, "comma")
    setValue(50, "float")
    setValue(52, "float")
    setValue(54, "string")
    setValue(56, "string")
    setValue(57, "var")
    setValue(58, "as")
    setValue(59, "lparen")
    setValue(60, "rparen")
    setValue(61, "var")
    setValue(66, "rk_float")
    setValue(72, "rk_string")
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