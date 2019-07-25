package cz.gattserver.nnet.net;

public interface ActivationFunction {

	double activate(double potential);

	double activatePrime(double potential);
}