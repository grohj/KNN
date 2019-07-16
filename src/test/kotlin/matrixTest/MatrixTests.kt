package matrixTest

import matrix.Matrix
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class MatrixTests {

    fun randomMatrix(rows: Int, cols: Int): Matrix = Matrix(rows, cols).let {
        it.copy(data = it.data.map { Random.nextDouble(-10.0, 10.0) }.toMutableList())
    }

    @Test
    fun testIndices() {
        val matrix = Matrix(4, 4)
        var seed = .0
        (0 until matrix.rows * matrix.cols).forEach {
            matrix.data[it] = seed++
        }
        seed = .0
        (0 until matrix.rows).forEach { x ->
            (0 until matrix.cols).forEach { y ->
                assertEquals(seed++, matrix[x, y])
            }
        }
        seed = .0
        (0 until matrix.rows).forEach { x ->
            (0 until matrix.cols).forEach { y ->
                matrix[x, y] = seed++
            }
        }
        seed = .0
        (0 until matrix.rows).forEach { x ->
            (0 until matrix.cols).forEach { y ->
                assertEquals(seed++, matrix[x, y])
            }
        }
    }

    @Test
    fun testPlus() {
        val m1 = randomMatrix(10, 5)
        val m2 = randomMatrix(10, 5)
        val sum = m1 + m2

        (0 until sum.rows).forEach { x ->
            (0 until sum.cols).forEach { y ->
                assertEquals(sum[x, y], m1[x, y] + m2[x, y])
            }
        }
    }

    @Test
    fun testMinus() {
        val m1 = randomMatrix(10, 10)
        val m2 = randomMatrix(10, 10)
        val sum = m1 - m2

        (0 until sum.rows).forEach { x ->
            (0 until sum.cols).forEach { y ->
                assertEquals(sum[x, y], m1[x, y] - m2[x, y])
            }
        }

    }

    @Test
    fun testMatrixMult() {
        val m1 = Matrix(2, 2, mutableListOf(1.0, 2.0, 2.0, 1.0))
        val m2 = Matrix(2, 2, mutableListOf(1.0, 2.0, 2.0, 1.0))
        val res = m1 * m2

        println(res.toString())
        val m3 = Matrix(50, 50)
        val m4 = Matrix(50, 1)
        m3 * m4
    }

    @Test
    fun testTranspose() {
        val m = randomMatrix(3, 2)
        assertTrue { m == m.T.T }
    }

    @Test
    fun testE() {
        val identity = Matrix.E(10)
        for (i in 0 until identity.rows)
            for (j in 0 until identity.cols)
                if (i == j)
                    assertEquals(identity[i, j], 1.0)
                else assertEquals(identity[i, j], 0.0)
    }

    @Test
    fun testHadamard() {
        val m1 = randomMatrix(3, 3)
        val m2 = randomMatrix(3, 3)

        val m3 = m1 hada (m2)

    }


}