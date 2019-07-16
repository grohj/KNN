package matrix


inline fun <reified T> MutableList<T>.modifyEach(t: (T) -> T) =
    this.forEachIndexed { index, item -> this[index] = t(item) }

data class Matrix(val rows: Int, val cols: Int, var data: MutableList<Double> = MutableList(rows * cols, { .0 })) {

    val T: Matrix
        get() = transpose()

    operator fun get(x: Int, y: Int) = data[x * cols + y]
    operator fun set(x: Int, y: Int, value: Number) {
        data[x * cols + y] = value.toDouble()
    }

    operator fun timesAssign(x: Double) = data.modifyEach { it * x; }
    operator fun plusAssign(x: Double) = data.modifyEach { it + x }
    operator fun minusAssign(x: Double) = data.modifyEach { it - x }
    operator fun unaryMinus() = copy(data = data.onEach { -it })
    operator fun times(x: Double) = copy(data = data.onEach { it * x })
    operator fun times(x: Matrix): Matrix {
        if (cols != x.rows)
            throw Exception("Number of cols of left matrix has to be equal to rows of right matrix")
        val res = Matrix(rows, x.cols)
        (0 until res.rows).forEach { i ->
            (0 until res.cols).forEach { j ->
                (0 until cols).forEach { k ->
                    res[i, j] += this[i, k] * x[k, j]
                }
            }
        }
        return res
    }

    operator fun plus(x: Double) = copy(data = data.onEach { it + x })
    operator fun plus(x: Matrix) = copy(data = data.mapIndexed { i, d -> d + x.data[i] }.toMutableList())
    operator fun minus(x: Double) = copy(data = data.map { it - x }.toMutableList())
    operator fun minus(x: Matrix) = copy(data = data.mapIndexed { i, d -> d - x.data[i] }.toMutableList())

    operator fun plusAssign(x : Matrix) {
        if(this.rows ==  x.rows && this.cols == x.cols) {
            data = data.mapIndexed { index, d ->  d+ x.data[index]}.toMutableList()
        }
    }

    fun transpose(): Matrix {
        val res = Matrix(cols, rows)
        (0 until res.rows).forEach { i ->
            (0 until res.cols).forEach { j ->
                res[i, j] = this[j, i]
            }
        }
        return res
    }


    override fun toString(): String {
        val out = StringBuilder("rows: $rows, cols: $cols\n")
        (0 until this.rows).forEach { i ->
            out.append("| ")
            (0 until this.cols).forEach { j ->
                out.append("${this[i, j]} ")
            }
            out.append("|")
            out.append("\n")
        }
        return out.toString()
    }

    override fun equals(other: Any?): Boolean {
        val o = other as? Matrix ?: return false
        return cols == other.cols && rows == other.rows && data == o.data
    }

    fun transform(func: (Double) -> Double) = this.copy(data = data.map(func).toMutableList())

    infix fun hada(x: Matrix): Matrix {
        if (rows != x.rows || cols != x.cols)
            throw Exception("Rows and cols have to be equal in matrices")
        val res = Matrix(rows, cols)
        res.data = data.mapIndexed { index, d -> d * x.data[index] }.toMutableList()
        return res
    }


    companion object {
        fun E(n: Int) = Matrix(n, n).apply { (0 until n).forEach { this[it, it] = 1 } }
    }

    fun deepCopy() : Matrix = this.copy(data = data.map { it }.toMutableList())

}

inline fun <reified T : Number> Array<T>.toMatrix(rows : Int, cols : Int,
                                                  mapper : (T) -> Double) = Matrix(rows, cols, data = this.map(mapper).toMutableList())