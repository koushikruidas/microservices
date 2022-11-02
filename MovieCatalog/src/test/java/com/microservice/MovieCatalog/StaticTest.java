package com.microservice.MovieCatalog;

public abstract class StaticTest {

	public static void main(String[] args) {
//		A a = new A();
//		
//		a.a();
//		B b = new B();
		System.out.println("Inside abstract class main");
//		b.b();
//		b.a();
//		System.out.println(a.i);
//		System.out.println(b.i);
	}
}

class B extends A {
	
	B(){
		this(12);
		System.out.println("No arg constructor");
	}
	
	B(int x){
		System.out.println("Inside arg constructor: "+x);
	}
	public void b() {
		System.out.println("Inside B");
	}
	
	public static void a() {
		System.out.println("Overriden a method");
	}
}

class A {
	static int i = 1;
	public static void a() {
		System.out.println("metheod a ");
	}
}
