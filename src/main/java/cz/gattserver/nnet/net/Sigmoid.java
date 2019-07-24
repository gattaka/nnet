package cz.gattserver.nnet.net;

import java.math.BigDecimal;

public class Sigmoid implements ActivationFunction {

	@Override
	public BigDecimal activate(BigDecimal potential) {
		BigDecimal result = new BigDecimal(1 / (1 + Math.pow(Math.E, -potential.doubleValue())));
		return result;
	}

	@Override
	public BigDecimal activatePrime(BigDecimal potential) {
		BigDecimal result = activate(potential).multiply(BigDecimal.ONE.subtract(activate(potential)));
		return result;
	}

}