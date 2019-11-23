package com.hunt.service.impl.threadtest;

import com.hunt.service.impl.threadtest.Resource.MyThread;

public class Resource {
	
	public int age;
	private String name;
	public  int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	
	public synchronized int getResult() {
		age++;
		return age;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
	
	public static void main(String[] args) {
		Resource resource = new Resource();
		resource.setAge(1);
		MyThread myThread = new MyThread(resource);
		myThread.start();
		
		MyThread myThread1 = new MyThread(resource);
		myThread1.start();
		
		
		
	}
	
	
	public static class MyThread extends Thread{
		private Resource res;
		
		
		public MyThread(Resource res) {
			super();
			this.res = res;
		}

		@Override
		public void run() {
			while(true) {
				int result = res.getResult();
				System.out.println("run-->"+Thread.currentThread().getName()+"--->"+result);
				try {
					sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
	}
	

}
