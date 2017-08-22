package atmExr;

import java.util.ArrayList;
import java.util.Iterator;

public class Main {

	public static void main(String[] args) {
		//BankOfMoon moon = new BankOfMoon("Dayeon", "01076641989", "1660 Aurora ave n, Seattle, WA", 94201702);
		BankOfKorea korea = new BankOfKorea("HYun", "01075676192", "2000 34st, NewYork, NY", 94201703);
		
		//moon.setMember("dyk1989", "123", 300000);
		//korea.setMember("60billion", "123456", 500000);
		
		//moon.wire("94201702-2", 94201703, "94201703-2", 10000);
		//System.out.println(moon.balance("94201702-2", "dyk1989"));
		//moon.deposit("94201702-2", 500000);
		//System.out.println(moon.balance("94201702-2", "dyk1989"));
		//moon.withdraw("94201702-2", 30000);
		//System.out.println(moon.balance("94201702-2", "dyk1989"));
		//System.out.println(moon.statement("94201702-2"));
		
		
		BankOfMoon k = new BankOfMoon("DY","123","123ave",94201702);
		//k.setMember("kim1989", "123", 500000);
		//System.out.println(k.balance("94201702-3", "kim1989"));
		//k.withdraw("94201702-3", 500000);
		//k.wire("94201702-3", 94201703, "94201703-2", 100000);
		//System.out.println(k.balance("94201702-3", "kim1989"));
		System.out.println(korea.balance("94201703-2", "60billion"));
	}

}
