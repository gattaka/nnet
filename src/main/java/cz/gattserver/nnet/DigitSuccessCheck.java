package cz.gattserver.nnet;

import cz.gattserver.nnet.matrix.Matrix;
import cz.gattserver.nnet.net.SuccessCheck;

public class DigitSuccessCheck implements SuccessCheck {
	
	@Override
	public boolean isSuccess(Matrix target, Matrix result) {
		if (target.getRows() != result.getRows() || target.getCols() != 1 || result.getCols() != 1)
			throw new IllegalStateException("Target and result must have same rows a 1 column");

		double targetMax = 0;
		int targetMaxIndex = 0;
		for (int i = 0; i < target.getRows(); i++) {
			double value = target.get(i, 0);
			if (value > targetMax) {
				targetMax = value;
				targetMaxIndex = i;
			}
		}

		double resultMax = 0;
		int resultMaxIndex = 0;
		for (int i = 0; i < result.getRows(); i++) {
			double value = result.get(i, 0);
			if (value > resultMax) {
				resultMax = value;
				resultMaxIndex = i;
			}
		}

		return targetMaxIndex == resultMaxIndex;
	}
}
