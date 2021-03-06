package edu.jonathan.lookforcardprices.comom;

import org.javamoney.moneta.Money;
import org.javamoney.moneta.convert.ExchangeRateType;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import javax.money.MonetaryAmount;
import javax.money.convert.CurrencyConversion;
import javax.money.convert.ExchangeRateProvider;
import javax.money.convert.MonetaryConversions;
import java.util.regex.Pattern;


public class MoneyUtil {

	public static final CurrencyUnit REAL = Monetary.getCurrency("BRL");
    public static final CurrencyUnit DOLLAR = Monetary.getCurrency("USD");
	public static final CurrencyUnit EURO = Monetary.getCurrency("EUR");

	public static Pattern MONEY_PATTERN = Pattern.compile("([+-]?[0-9|^.|^,]+)[\\.|,]([0-9]+)$");

	private static ExchangeRateProvider provider = null;

	public static MonetaryAmount dollarToReal(Money value){
		return toReal(value, DOLLAR);
	}

	public static MonetaryAmount euroToReal(Money value){
		return toReal(value, EURO);
	}

	private static MonetaryAmount toReal(Money value, CurrencyUnit fromCurrency){
		if(provider == null){
			provider = MonetaryConversions.getExchangeRateProvider(ExchangeRateType.ECB);
		}

		CurrencyConversion currencyConversion = provider.getCurrencyConversion(REAL);
		MonetaryAmount realAmount = Money.of(value.getNumber().doubleValue(), fromCurrency);
		return currencyConversion.apply(realAmount);
	}
}
