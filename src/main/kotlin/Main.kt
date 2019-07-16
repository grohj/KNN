import idx.IDX
import matrix.toMatrix
import nn.NeuralNetwork


val INPUT_SIZE = 28*28
fun main(args : Array<String>) {



    val reader = IDX("mnist/train-images-idx3-ubyte", "mnist/train-labels-idx1-ubyte")


    val nn = NeuralNetwork(listOf(28*28, 128, 10), randomVals = true)

    val epochs = 5
    val minibatchSize = 32

    println("preparing data")
    var data = reader.asSequence().take(3000).toList()
    println("${data.size} items prepared")



    for (i in 0..epochs) {
        val batches = data.shuffled().chunked(minibatchSize)
        batches.forEachIndexed {  index, it ->

            println("batch : $index")
            val convertedBatch = it.map { it.second.toMatrix(INPUT_SIZE, 1) { it.toDouble() / 255.0 } to it.first.mnistResult().toMatrix(10,1) {it.toDouble()} }
            nn.trainBatch(convertedBatch)
        }

    }

    val repeat = 100
    var correct = 0
    val test = data.shuffled()
    repeat(repeat) {
        val next = test[it]
        val res = nn.guess(next.second.toMatrix(INPUT_SIZE, 1) { (it.toDouble() / 255.0) })

        val max = res.data.indexOf(res.data.max())

        if(next.first == max+1)
            correct++


        println("guessed: ${max+1}, should be next ${next.first}")
    }
//
    print("$correct / $repeat")
}

fun printNumber(number : Array<Int>) {
    for (i in 0 until number.size) {
        if(number[i] != 0)
            print("█")
        else print(" ")

        if(i % 28 == 0)
            println()
    }
    println()
}
fun Int.mnistResult() = Array<Int> (10)
    {
        if(it == this) 1
        else 0
    }
