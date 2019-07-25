package cz.gattserver.nnet.net;

import cz.gattserver.nnet.matrix.Matrix;

public interface SuccessCheck {

	boolean isSuccess(Matrix target, Matrix result);
}
