package neural

import random

class Neuron {
    var weights: DoubleArray
    private var bias: Double

    constructor(numWeights: Int) {
        this.weights = DoubleArray(numWeights) { random(-1.0, 1.0) }
        this.bias = random(-1.0, 1.0)
    }

    private constructor(weights: DoubleArray, bias: Double) {
        this.weights = weights
        this.bias = bias
    }

    fun copy(): Neuron {
        return Neuron(weights.copyOf(), bias)
    }

    operator fun invoke(inputs: DoubleArray, lastLayer: Boolean): Double {
        val sum = (0 until inputs.size).sumByDouble { weights[it] * inputs[it] } + bias
        return if (lastLayer)
            sum
        else
            //Math.tanh(sum) // tanh
            return Math.max(sum, 0.0) // Relu
            //return 1.0 / (1.0 + Math.exp(-sum)) // Sigmoid
    }

    fun mutate(mutatePower: Double) {
        for (i in 0 until weights.size)
            weights[i] += random(-mutatePower, mutatePower)
        bias += random(-mutatePower, mutatePower)
    }

    fun crossover(mate: Net, layerIdx: Int, neuronIdx: Int, crossoverPower: Double) {
        val mateNeuron: Neuron = mate.layers[layerIdx].neurons[neuronIdx]
        for (i in 0 until weights.size)
            weights[i] = lerp(weights[i], mateNeuron.weights[i], random(0.0, crossoverPower))
        bias = lerp(bias, mateNeuron.bias, random(0.0, crossoverPower))
    }

    private fun lerp(a: Double, b: Double, p: Double): Double {
        return a + (b - a) * p
    }

    override fun toString(): String {
        val sb = StringBuilder()
        sb.append("[")
        for ((i, w) in weights.withIndex()) {
            sb.append(w.toString())
            if (i < weights.size - 1) sb.append(", ")
        }
        sb.append("]")
        return sb.toString()
    }
}