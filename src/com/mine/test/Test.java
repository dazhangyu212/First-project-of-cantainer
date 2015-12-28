package com.mine.test;

public class Test {

	public static void main(String[] args) {
		long start = System.currentTimeMillis();
//		for (int i = 0; i < TransContainer.mColumn; i++) {
			TransContainer trans = new TransContainer();
			trans.initView();
//		}
		long end = System.currentTimeMillis();
		System.out.println("程序运行时长："+((end - start)/1000.0)+"s");
//		TransContainer trans = new TransContainer();
//		trans.initData();
//		trans.selectLessObs(13, 2, 0);
	}

}
