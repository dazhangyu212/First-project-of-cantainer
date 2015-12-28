package com.mine.test;

public class Test {

	public static void main(String[] args) {
		for (int i = 0; i < TransContainer.mColumn; i++) {
			TransContainer trans = new TransContainer();
			trans.initView();
		}
//		TransContainer trans = new TransContainer();
//		trans.initData();
//		trans.selectLessObs(13, 2, 0);
	}

}
