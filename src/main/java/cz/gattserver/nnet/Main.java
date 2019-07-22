package cz.gattserver.nnet;

import cz.gattserver.nnet.net.NNet;
import cz.gattserver.nnet.net.Sigmoid;

public class Main {

	public static void main(String[] args) {

		int imgSide = 4;
		int pixels = imgSide * imgSide;
		int batchSize = 100;

		NNet net = new NNet(new int[] { pixels, pixels, 2 }, new Sigmoid());

		for (int i = 0; i < 90000; i++) {
			double[][] trainBatchInputs = new double[batchSize][16];
			double[][] trainBatchOutputs = new double[batchSize][2];
			for (int b = 0; b < batchSize; b++) {
				boolean vertical = Math.random() > 0.5; 
				trainBatchOutputs[b][0] = vertical ? 1 : 0;
				trainBatchOutputs[b][1] = vertical ? 0 : 1;
				int coord = (int) (Math.random() * imgSide);
				for (int p = 0; p < pixels; p++) {
					int x = p % imgSide;
					int y = (int) (p / imgSide);
					trainBatchInputs[b][p] = (vertical && x == coord || !vertical && y == coord) ? 1 : 0;
				}
			}
			net.train(trainBatchInputs, trainBatchOutputs, 1, 0.1);
		}

	}

}
