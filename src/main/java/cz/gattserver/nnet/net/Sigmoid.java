package cz.gattserver.nnet.net;

public class Sigmoid implements ActivationFunction {

	@Override
	public double activate(double potential) {
		return 1 / (1 + Math.pow(Math.E, -potential));
	}

	@Override
	public double activatePrime(double potential) {
		return activate(potential) * (1 - activate(potential));
	}

}