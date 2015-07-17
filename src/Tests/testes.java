package Tests;



public class testes {
	public static void main(String[] args) {
		float num[] = {0,10,2,3};
		float m = num[1];
		
		for(int n = 2; n < num.length; n++){
			//m = (m*(n-1) + num[n])/n;
			m = (num[n]-m)/n + m;
		}
		
		System.out.println("Media: " + m);
	}
}