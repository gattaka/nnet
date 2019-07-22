package cz.gattserver.nnet.net;

public class SmoothReLU implements ActivationFunction {

	@Override
	public double activate(double potential) {
		return Math.log(1 + Math.pow(Math.E, potential));
	}

	@Override
	public double activatePrime(double potential) {
		return 1 / (1 + Math.pow(Math.E, -potential));
	}

}