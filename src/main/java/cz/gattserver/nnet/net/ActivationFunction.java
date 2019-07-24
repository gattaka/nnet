package cz.gattserver.nnet.net;

import java.math.BigDecimal;

public interface ActivationFunction {

	BigDecimal activate(BigDecimal potential);

	BigDecimal activatePrime(BigDecimal potential);
}