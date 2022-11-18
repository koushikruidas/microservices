package com.kafka.messaging;

public class JavaTest {
	int i;
	static int j;
	{
		System.out.println("Instance Block 1. Value of i = "+i);
	}
	static {
		System.out.println("static block 1. value of j = "+j);
		method_2();
	}
	{
		i = 5;
	}
	static {
		j = 10;
	}
	JavaTest(){
		System.out.println("Welcome");
	}
	public static void main(String[] args) {
		JavaTest jt = new JavaTest();
		Abstructor obj = new Abstructor(10) {
			
			@Override
			public void test() {
				// TODO Auto-generated method stub
				
			}
		};
	}
	public void method_1() {
		System.out.println("Instance method. ");
	}
	static {
		System.out.println("static block 2. value of j = "+j);
	}
	{
		System.out.println("instance block 2. value of i = "+i);
		method_1();
	}
	public static void method_2() {
		System.out.println("Static method_2(). ");
	}
}
