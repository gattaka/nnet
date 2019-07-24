package cz.gattserver.nnet.net;

import java.math.BigDecimal;

public class SmoothReLU implements ActivationFunction {

	@Override
	public BigDecimal activate(BigDecimal potential) {
		double value = Math.log(1 + Math.pow(Math.E, potential.doubleValue()));
		if (Double.NEGATIVE_INFINITY == value)
			return new BigDecimal(Double.MIN_VALUE);
		if (Double.isInfinite(value))
			return new BigDecimal(Double.MAX_VALUE);
		if (Double.isNaN(value))
			return BigDecimal.ZERO;
		BigDecimal result = new BigDecimal(value);
		return result;
	}

	@Override
	public BigDecimal activatePrime(BigDecimal potential) {
		double value = 1 / (1 + Math.pow(Math.E, -potential.doubleValue()));
		if (Double.NEGATIVE_INFINITY == value)
			return new BigDecimal(Double.MIN_VALUE);
		if (Double.isInfinite(value))
			return new BigDecimal(Double.MAX_VALUE);
		if (Double.isNaN(value))
			return BigDecimal.ZERO;
		BigDecimal result = new BigDecimal(value);
		return result;
	}

}