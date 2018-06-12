package edu.jonathan.lookforcardprices.comom;

import org.javamoney.moneta.Money;
import org.javamoney.moneta.convert.ExchangeRateType;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import javax.money.MonetaryAmount;
import javax.money.convert.*;
import java.util.regex.Pattern;


public class MoneyUtil {

	private static final CurrencyUnit REAL = Monetary.getCurrency("BRL");
	private static final CurrencyUnit EURO = Monetary.getCurrency("EUR");
	private static final CurrencyUnit DOLLAR = Monetary.getCurrency("USD");

	public static Pattern MONEY_PATTERN = Pattern.compile("([+-]?[0-9|^.|^,]+)[\\.|,]([0-9]+)$");

	private static ExchangeRateProvider provider = null;

	static{
		provider = MonetaryConversions.getExchangeRateProvider(ExchangeRateType.IMF);
		/*
		Matcher matcher = pattern.matcher("15.20");

		matcher.matches();
		System.out.println("0: " + matcher.group(0));
		System.out.println("1: " + matcher.group(1));
		System.out.println("2: " + matcher.group(2));
		*/
	}

	public static MonetaryAmount dollarToReal(double value){
		CurrencyConversion currencyConversion = provider.getCurrencyConversion(REAL);
		MonetaryAmount realAmount = Money.of(value, DOLLAR);
		return currencyConversion.apply(realAmount);
	}

}
