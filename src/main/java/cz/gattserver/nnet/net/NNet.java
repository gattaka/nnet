package cz.gattserver.nnet.net;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import cz.gattserver.nnet.matrix.Matrix;
import cz.gattserver.nnet.matrix.MatrixUtils;

public class NNet {

	private int[] layersSizes;

	// pole matic vah dle vrstvy
	private Matrix[] weights;

	// pole vektorů biasů dle vrstvy
	private Matrix[] biases;

	private int triesCount = 0;
	private int successCount = 0;

	private ActivationFunction activationFunction;

	public NNet(int[] layersSizes, ActivationFunction activationFunction) {
		this.layersSizes = layersSizes;
		this.activationFunction = activationFunction;

		weights = new Matrix[layersSizes.length];
		biases = new Matrix[layersSizes.length];

		// Matice vah -- pro každý neuron vrstvy jsou
		// zapsány váhy neuronů (co řádek, to jeden neuron)
		// pokud pak matici vynásobím maticí vstupů
		// projdou se postupně sloupce matice (vstupy neuronu)
		// a provede se součet součinů jeho vah a vstupů
		//
		// | w11 w12 w13 | | x1 | | w11x1 + w12x2 + w13x3 | | b1 |
		// | w21 w22 w23 | . | x2 | + B = | w21x1 + w22x2 + w23x3 | + | b2 |
		// | x3 |

		// net init
		// pro každou vrstvu (kromě 0. ta indikuje pouze počet vstupů)
		for (int l = 1; l < layersSizes.length; l++) {
			int inputSize = layersSizes[l - 1];
			int layerSize = layersSizes[l];
			Matrix layerWeights = new Matrix(layerSize, inputSize);
			// sloupcový vektor
			Matrix layerBiases = new Matrix(layerSize, 1);
			weights[l] = layerWeights;
			biases[l] = layerBiases;
			// pro každý neuron z vrstvy
			for (int row = 0; row < layerSize; row++) {
				// pro každý vstup do neuronu
				for (int col = 0; col < inputSize; col++)
					layerWeights.set(row, col, Math.random() * 2 - 1);
				layerBiases.set(row, 0, Math.random() * 2 - 1);
			}
		}
	}

	public Matrix guess(Matrix inputs) {
		// pole vektorů potenciálů dle vrstvy
		Matrix[] potentials = new Matrix[layersSizes.length];
		// pole vektorů aktivací dle vrstvy
		Matrix[] activations = new Matrix[layersSizes.length];
		for (int l = 1; l < layersSizes.length; l++) {
			// z^l = w^l . a^(l-1) + b^l
			potentials[l] = weights[l].multiply(activations[l - 1]).add(biases[l]);
			// a^l = sigma(z^l)
			activations[l] = potentials[l].map(activationFunction::activate);
		}
		return activations[layersSizes.length - 1];
	}

	public void train(double[][] trainBatchInputs, double[][] trainBatchOutputs, double sensitivity, double maxError) {
		int batchSize = trainBatchInputs.length;

		// pole vektorů potenciálů dle vrstvy
		Matrix[] potentials = new Matrix[layersSizes.length];
		// pole vektorů aktivací dle dávky a vrstvy
		Matrix[][] activations = new Matrix[batchSize][layersSizes.length];
		// pole vektorů chyb dle dávky a vrstvy
		Matrix[][] errors = new Matrix[batchSize][layersSizes.length];

		int L = layersSizes.length - 1;

		// pro každý příklad z dávky
		for (int b = 0; b < batchSize; b++) {

			// sloupcový vektor vstupů
			Matrix inputs = MatrixUtils.fromFlatArray(layersSizes[0], 1, trainBatchInputs[b]);
			// sloupcový vektor známé hodnoty výstupu
			Matrix target = MatrixUtils.fromFlatArray(layersSizes[L], 1, trainBatchOutputs[b]);

			// potenciály a aktivace
			activations[b][0] = inputs;
			for (int l = 1; l < layersSizes.length; l++) {
				// z^l = w^l . a^(l-1) + b^l
				potentials[l] = weights[l].multiply(activations[b][l - 1]).add(biases[l]);
				// a^l = sigma(z^l)
				activations[b][l] = potentials[l].map(activationFunction::activate);
			}

			// chyba výsledné vrstvy
			// delta^L = (a^L - y) o sigma'(z^L)
			errors[b][L] = activations[b][L].subtract(target)
					.multiplyHadamard(potentials[L].map(activationFunction::activatePrime));

			// chyby vnitřních vrstev
			for (int l = L - 1; l > 0; l--) {
				// l = L - 1, L - 2, ..., 2 (1. je vstup)
				// delta^l = ((w^(l+1))^T . delta^(l+1)) o sigma'(z^l)
				errors[b][l] = weights[l + 1].transpose().multiply(errors[b][l + 1])
						.multiplyHadamard(potentials[l].map(activationFunction::activate));
			}

			triesCount++;

			// C = 1/2 * sum_j((y_j - a_j^L)^2)
			double cost = 0;
			for (int j = 0; j < layersSizes[L]; j++)
				cost += Math.pow(target.get(j, 0) - activations[b][L].get(j, 0), 2);
			cost /= 2;

			// učení
			if (cost < maxError)
				successCount++;
		}

		// aktualizuj váhy a biasy
		for (int l = L; l > 0; l--) {
			Matrix weightsDeltaSumMatrix = null;
			Matrix biasesDeltaSumVector = null;
			for (int b = 0; b < batchSize; b++) {
				Matrix deltaW = errors[b][l].multiply(activations[b][l - 1].transpose());
				weightsDeltaSumMatrix = b == 0 ? deltaW : weightsDeltaSumMatrix.add(deltaW);
				Matrix deltaB = errors[b][l];
				biasesDeltaSumVector = b == 0 ? deltaB : biasesDeltaSumVector.add(deltaB);
			}
			weightsDeltaSumMatrix = weightsDeltaSumMatrix.multiplyByScalar(sensitivity / batchSize);
			biasesDeltaSumVector = biasesDeltaSumVector.multiplyByScalar(sensitivity / batchSize);

			weights[l] = weights[l].subtract(weightsDeltaSumMatrix);
			biases[l] = biases[l].subtract(biasesDeltaSumVector);
		}

		System.out.println(String.format("%.1f", getSuccessRate()) + "% cycle: " + triesCount);
	}

	private void write(FileWriter fileWriter, String value) throws IOException {
		fileWriter.write(value);
		fileWriter.write(",");
	}

	private void writeArray(FileWriter fileWriter, double[] array) throws IOException {
		fileWriter.write("[");
		for (int i = 0; i < array.length; i++) {
			if (i != 0)
				fileWriter.write(",");
			fileWriter.write(String.valueOf(array[i]));
		}
		fileWriter.write("]");
	}

	public void writeConfig(File file) throws IOException {
		try (FileWriter fileWriter = new FileWriter(file)) {
			write(fileWriter, String.valueOf(layersSizes.length));
			for (int l = 1; l < layersSizes.length; l++)
				write(fileWriter, String.valueOf(layersSizes[l]));
			for (int l = 1; l < layersSizes.length; l++) {
				writeArray(fileWriter, weights[l].toArray());
				writeArray(fileWriter, biases[l].toArray());
			}
		}
	}

	public double getSuccessRate() {
		if (triesCount == 0)
			return 0;
		return (double) successCount / triesCount * 100;
	}

}
