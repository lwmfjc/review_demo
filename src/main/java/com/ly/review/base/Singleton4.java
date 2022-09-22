package com.ly.review.base;

public class Singleton4 {
	//为了防止重排序，需要添加volatile关键字
	private static volatile  Singleton4 INSTANCE;

	private Singleton4() {
	}

	/**
	 * double check
	 * @return
	 */
	public static  Singleton4 getInstance() {
		//2 先判断一次,对于后面的操作(此时已经创建了对象)能减少加锁次数
		if (INSTANCE == null) {
			//如果这里不加锁会导致线程安全问题，可能刚进了判断语句之后，执行权被剥夺了又创建好了对象，
			//所以判断及创建对象必须是原子操作
			synchronized ( Singleton4.class) {
				if (INSTANCE == null) {
					//用来模拟多线程被剥夺执行权
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					//如果这个地方不加volatile,会出现的问题是,指令重排 1,2,3是正常的,
					//会重排成1,3,2 然后别的线程去拿的时候，判断为非空，但是实际上运行的时候，发现里面的数据是空的

					//1 memory = allocate();//分配对象空间
					//2 instance(memory); //初始化对象
					//3 instance = memory; //设置instance指向刚刚分配的位置
					INSTANCE = new  Singleton4();
				}
			}
		}
		return INSTANCE;
	}
}
