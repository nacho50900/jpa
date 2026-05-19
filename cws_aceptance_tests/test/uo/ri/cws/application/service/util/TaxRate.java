package uo.ri.cws.application.service.util;

public class TaxRate {

	private static double segments[] = { 12450, 20200, 35200, 60000, 300000 };
	private static double rates[] =    {  0.19,  0.24,  0.30,  0.37,   0.45, 0.47 };
	
	public static double forSalary(double annualSalary) {
		for (int i = 0; i < segments.length; i++) {
			if (annualSalary < segments[i]) {
				return rates[i];
			}
		}
		return rates[ rates.length - 1 ];
	}

}