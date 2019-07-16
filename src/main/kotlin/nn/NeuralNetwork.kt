package nn

import matrix.Matrix
import kotlin.math.E
import kotlin.math.pow
import kotlin.random.Random
import kotlin.random.asJavaRandom

/**
 *
 */
typealias ActivationFunction = (x: Double) -> (Double)

class NeuralNetwork(val nodes: List<Int>, randomVals: Boolean = false) {

    var weights = mutableListOf<Matrix>()
    var biases = mutableListOf<Matrix>()
    var activationFunction = { x: Double -> 1 / (1 + E.pow(-x)) }
    var activationFunctionPrimed = { x: Double -> activationFunction(x) * (1 - activationFunction(x)) }
    var learningRate = 0.15

    val random = Random.asJavaRandom()

    init {
        for (i in 0 until nodes.size - 1) {
            weights.add(
                Matrix(
                    nodes[i + 1],
                    nodes[i],
                    MutableList(nodes[i + 1] * nodes[i]) { if (randomVals) random.nextGaussian() else .5 })
            )
            biases.add(
                Matrix(
                    nodes[i + 1],
                    1,
                    data = MutableList(nodes[i + 1]) { if (randomVals) random.nextGaussian() else .0 })
            )
        }
    }

    fun guess(input: Matrix): Matrix {
        var vector = input
        weights.forEachIndexed { index, weight ->
            vector = stepForwardWithActivation(weight, vector, biases[index])
        }
        return vector
    }

    fun neuronValues(input: Matrix): List<Matrix> {
        val res = mutableListOf<Matrix>()
        var vector = input
        weights.forEachIndexed { index, weight ->
            vector = stepForwardWithoutActivation(weight, vector, biases[index])
            res.add(vector)
        }
        return res
    }

    fun neuronActivations(input: Matrix): List<Matrix> {
        val res = mutableListOf<Matrix>()
        var vector = input
        weights.forEachIndexed { index, weight ->
            vector = stepForwardWithActivation(weight, vector, biases[index])
            res.add(vector)
        }
        return res
    }

//    fun train(input: Matrix, desiredOutput: Matrix) {
//        val zs = neuronValues(input)
//        val activations = zs.map { it.transform(activationFunction) }
//        val xN = activations.last()
//        val zNP = zs.last().let { it.copy(data = it.data.map(activationFunctionPrimed).toMutableList()) }
//        var delta = (xN - desiredOutput) hada zNP
//
//        biases[biases.lastIndex] += biases[biases.lastIndex] - (delta * learningRate)
//        weights[weights.lastIndex] =
//            weights[weights.lastIndex] - (delta * activations[activations.lastIndex - 1].T) * learningRate
//
//
//
//        for (i in weights.size - 2 downTo 0) {
//            val z = zs[i]
//            val sp = z.deepCopy().apply { transform(activationFunctionPrimed) }
//            delta = (weights[i + 1].T * delta) hada sp
//            weights[i] =
//                weights[i] - (weights[i] + delta * if (i <= 0) input.T else activations[i - 1].T) * learningRate
//            biases[i] = biases[i] - (delta * learningRate)
//        }
//
//    }


    fun stepForwardWithActivation(weights: Matrix, vector: Matrix, bias: Matrix) =
        ((weights * vector) + bias).transform(activationFunction)

    fun stepForwardWithoutActivation(weights: Matrix, vector: Matrix, bias: Matrix) = ((weights * vector) + bias)


    fun trainBatch(batch : List<Pair<Matrix,Matrix>>) {


        var weightDeltaSum = weights.map { it.copy(data = MutableList(it.rows*it.cols) {0.0}) }
        var biasDeltaSum = biases.map { it.copy(data = MutableList(it.rows*it.cols) {0.0}) }

        batch.forEach {
            val (weightDeltas, biasDeltas) = backpropagate(it.first, it.second)

            weightDeltas.forEachIndexed { index, delta -> weightDeltaSum[index] += delta }
            biasDeltas.forEachIndexed { index, delta -> biasDeltaSum[index] += delta }

        }

        weights = weights.mapIndexed { index, it -> it - ( weightDeltaSum[index] * (learningRate/batch.size)) }.toMutableList()
        biases = biases.mapIndexed { index, it -> it - ( biasDeltaSum[index] * (learningRate/batch.size)) }.toMutableList()

    }


    fun backpropagate(input : Matrix, y : Matrix) : Pair<List<Matrix>, List<Matrix>> {

        val weightDeltas = mutableListOf<Matrix>()
        val biasDeltas = mutableListOf<Matrix>()

        val zs = neuronValues(input)
        val activations = neuronActivations(input)
        val xN = activations.last()
        val zNP = zs.last().transform(activationFunctionPrimed)
        var delta = (xN - y) hada zNP

        biasDeltas.add(delta)
        weightDeltas.add(delta * activations[activations.lastIndex - 1].T)

        for (i in weights.size - 2 downTo 0) {
            val z = zs[i]
            val sp = z.transform(activationFunction)
            delta = (weights[i + 1].T * delta) hada sp

            biasDeltas.add(delta)
            weightDeltas.add(delta * if (i <= 0) input.T else activations[i - 1].T)
        }

        return weightDeltas.reversed() to biasDeltas.reversed()

    }


    override fun toString(): String {
        val out = StringBuilder("")
        weights.forEach { out.append(it.toString()) }
        return out.toString()
    }


}