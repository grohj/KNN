package nnTest

import matrix.Matrix
import nn.NeuralNetwork
import kotlin.random.Random
import kotlin.test.Test


data class TrainData(val input : Matrix, val output : Matrix)
class MatrixTests {

    @Test
    fun compute() {
        val nn = NeuralNetwork(listOf(2, 3, 2))
        val input = Matrix(2, 1).apply {
            data[0] = 0.1
            data[1] = 0.4
        }
        val output = nn.guess(input)
        print(output.toString())
    }

    @Test
    fun testTrain() {
        val nn = NeuralNetwork(listOf(2, 3,2), true)

        val t1 = listOf(TrainData(Matrix(2,1).apply {
            data[0] = .0
            data[1] = .0
        }, Matrix(2,1).apply {
            data[0] = 1.0
            data[1] = .0
        }),
        TrainData(Matrix(2,1).apply {
            data[0] = .0
            data[1] = 1.0
        }, Matrix(2,1).apply {
            data[0] = .0
            data[1] = 1.0
        }),
        TrainData(Matrix(2,1).apply {
            data[0] = .0
            data[1] = 1.0
        }, Matrix(2,1).apply {
            data[0] = .0
            data[1] = 1.0
        }),
        TrainData(Matrix(2,1).apply {
            data[0] = 1.0
            data[1] = 1.0
        }, Matrix(2,1).apply {
            data[0] = 1.0
            data[1] = .0
        }))


        for (i in 1..500000) {

            val input = t1[Random.nextInt(0, t1.size)]
            nn.train(input.input, input.output)
        }


        var counter = 0

        var batchCounter = 0
        for (i in 1..1000) {


            //TODO: batch learning
            if (batchCounter == 100) {
                batchCounter = 0


            }



            batchCounter++

            val input = t1[Random.nextInt(0, t1.size)]
            val guess = nn.guess(input.input)

            val nnMax = guess.data.withIndex().maxBy { it.value }
            val inputMax = input.input.data.withIndex().maxBy { it.value }

            if (nnMax?.index == inputMax?.index)
                counter++


        }

        print("Succesrate: ${counter / 1000.0}")







    }

    @Test
    fun print() {

        val nn = NeuralNetwork(listOf(2, 3, 2))
        println(nn.toString())
    }
}