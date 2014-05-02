package org.spark.util;

import java.util.ArrayList;
import java.util.List;

public class Combination {

	int m, n;
	char set[];
	List<char[]> Set;

	public Combination(int m, int n) {
		this.m = m;
		this.n = n;
	}

	public Combination(char set[]) {
		this.set = set;
		Set = new ArrayList(0);
	}

	public static int gcd(int a, int b) {

		if (b == 0) {
			return a;
		} else {
			return gcd(b, a % b);
		}
	}

	public int C() {

		int a = 1;
		int b = 1;
		int temp;
		for (int i = 1; i <= m; i++) {
			a *= n - i + 1;
			b *= i;
			temp = Combination.gcd(a, b);
			a /= temp;
			b /= temp;
		}
		return a / b;

	}

	public void Combination_Set(int n) {
		int[] used = new int[set.length];
		Combination_Set(0, 0, n, used);
	}

	public void Combination_Set(int current, int p, int n, int used[]) {

		if (current == n) {
			char temp[] = new char[n];
			int j = 0;
			for (int i = 0; i < set.length; i++) {
				if (used[i] != 0) {
					temp[j] = set[i];
					j++;
				}
			}
			Set.add(temp);
			return;
		}
		for (int i = p; i < set.length; i++) {
			used[i] = 1;
			Combination_Set(current + 1, i + 1, n, used);
			used[i] = 0;
		}

	}

	public List<char[]> getCombinationList() {
		return Set;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		char[] p = { 'a', 'b', 'c', 'd' };
		Combination c = new Combination(p);
		c.Combination_Set(2);
		List<char[]> list = c.getCombinationList();
		for (char[] temp : list) {
			for (int i = 0; i < temp.length; i++) {
				System.out.print(temp[i]);
			}
			System.out.println();
		}
	}

}
