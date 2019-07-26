package cz.gattserver.nnet.net;

import cz.gattserver.nnet.matrix.Matrix;

public interface OutputActivationFunction {

	Matrix activate(Matrix potential);

	Matrix activatePrime(Matrix potential);
}