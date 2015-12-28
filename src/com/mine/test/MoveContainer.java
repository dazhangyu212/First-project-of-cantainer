package com.mine.test;


public class MoveContainer {

	
	/**
	 * 行数，同时指代  T：额定堆存高度；
	 */
	private int mRaw = 4;
	/**
	 * 列数，同时指代 r：一个贝中所包含的栈的数量；
	 */
	private int mColumn = 6;
	/**
	 * 现有的集装箱数量
	 */
	private int mCount = 16;
	/**
	 * 
	 */
	private int[][] mCantainers = new int[mRaw][mColumn];
	
	
	private int transTimes = 0;
	

	public void initView() {
		initData();
		takeCantainer();
		for (int y = 0; y < mColumn; y++) {
			for (int x = 0; x < mRaw; x++) {
				System.out.println("第"+x+"行；"+"第"+y+"列；"+"数值"+mCantainers[x][y]);
			}
		}
		System.out.println("倒箱次数"+transTimes);
//		transTimes = 0;
	}
	/**
	 * 初始化栈列数据
	 */
	private void initData() {
		 int[][]	cantainers = {{10,14,4,7,15,12},{6,9,8,16,11,2},{13,5,0,1,0,3},{0,0,0,0,0,0}};
		 mCantainers = cantainers;
	}
	/**
	 * 按顺序提取集装箱
	 */
	private void takeCantainer(){
		for (int i = 0; i < mCount; i++) {
			int position = i+1;
			System.out.println("移除集装箱的序号:"+position);
			ereasePosition(position);
		}
	}
	/**
	 * 擦除相应集装箱的序号
	 * @param position 要移除集装箱的序号
	 */
	private void ereasePosition(int position) {
		for (int y = 0; y < mColumn; y++) {
			for (int x = 0; x < mRaw; x++) {
				/**
				 * 先检测是不是要移除的集装箱序号
				 */
				if (position == mCantainers[x][y] && x < mRaw) {
					
					if ( isTop(x, y)) {
						System.out.println(mCantainers[x][y]+"在顶层");
						/*
						 * (x,y)同栈内的上方数字都是0时，该序号改为0
						 */
						mCantainers[x][y] =0;
						System.out.println("可直接移除:"+position);
					}else {
						/*
						 * (x,y)同栈内上方不为是0时，该序号改为需要移到他处
						 */
						getTopValue(y);
						moveTopAway(position,x,y);
						System.out.println("移除顶部后，取走"+mCantainers[x][y]);
						mCantainers[x][y] = 0;
						
					}
				}
			}
		}
		
	}
	/**
	 * 判断要移除的集装箱序号是否在顶层
	 * @param x
	 * @param y
	 * @return
	 */
	boolean isTop(int x,int y){
		boolean mTop = true;;
		for (int i = mRaw-1; i > x; i--) {
			if (mCantainers[i][y] ==0) {
				mTop = mTop & true;
			}else {
				mTop = mTop & false;
			}
		}
		return mTop;
	}
	/**
	 * 开始移除需要的集装箱序号position上方的集装箱
	 * @param position 序号
	 * @param x 当前检测的数组行
	 * @param y 当前检测的数组列
	 */
	private void moveTopAway(int position,int x,int y) {
		for (int i = mRaw-1; i > x; i--) {
			if (mCantainers[i][y] != 0) {
				System.out.println("mCantainers[i][y] = "+mCantainers[i][y]);
				for (int j = 0; j < mColumn; j++) {
					if (j != y) {
						int[] mPos = getTopValue(j);
						if (mPos[0] != 0 && mCantainers[i][y] != mCount ) {
							if (mCantainers[i][y] < mPos[0] && mPos[1]+1 <mRaw) {
								
								System.out.println("mCantainers["+i+"]["+y+"]="+mCantainers[i][y]+"转移至mCantainers["+mPos[1]+"]["+j+"]="
										+mCantainers[mPos[1]][j]+"上方");
								mCantainers[mPos[1]+1][j] = mCantainers[i][y];
								mCantainers[i][y] = 0;
								transTimes++;
								System.out.println("倒箱"+transTimes);
								return;
							}else {
								System.out.println("此列已满,转到下一列");
							}
							continue;//mCantainers[i][y] = 0 即表示箱子已被移走
						}else if(mPos[0] == 0){
							//mPos[0] == 0 表示此列为空,可以直接放置
							mCantainers[0][j] = mCantainers[i][y];
							System.out.println("mCantainers["+i+"]["+y+"]="+mCantainers[i][y]+"转移至mCantainers["+mPos[1]+"]["+y+"]="
									+mCantainers[0][j]+"上方");
							mCantainers[i][y] = 0;
							return;
						}else if (mCantainers[i][y] == mCount) {
							int flag = moveTheLast(mPos[1],mPos[2]);
							if (flag != -1) {
								int[] top = getTopValue(flag);
								System.out.println("mCantainers["+i+"]["+y+"]="+mCantainers[i][y]+"转移至mCantainers["+(top[1]+1)+"]["+j+"]="
										+mCantainers[top[1]+1][j]+"上方");
								mCantainers[top[1]+1][flag]  = mCantainers[i][y];
								mCantainers[i][y] = 0;
								transTimes++;
								System.out.println("倒箱"+transTimes);
								continue;
							}
						}
					}
				}
			}
		}
	}
	/**
	 * 移动优先级最差的集装箱
	 */
	private int moveTheLast(int x,int y) {
		int[] group = new int[mColumn];//放置除了最差优先级之外其他列的最高处的序号
		for (int i = 0; i < mColumn; i++) {
			int[] otherTop = getTopValue(i);//获取其他列的最大值
			if (otherTop[0] == 0) {
				mCantainers[0][i] = mCantainers[x][y];
				mCantainers[x][y] = 0;
				transTimes++;
				System.out.println("倒箱"+transTimes);
				return -1;
			}
			group[i] = mCantainers[x][y] - otherTop[0];
		}
		
		int min = group[0];
		
		int yOfMin = 0;
		for (int i = 0; i < group.length; i++) {
			if (group[i] != 0 && group[i] < min ) {
				min = group[i];
				yOfMin = i;
			}
		}
		return yOfMin;
	}
	/**
	 * 获取某一列最高处的集装箱序号
	 * @param y 列序号
	 * @return 返回0表示这一列已经空了; value[1] 所在行,value[0]最高处的值
	 */
	int[] getTopValue(int y){
		int[]  value = {0,0,0};
		for (int i = mRaw-1; i >-1; i--) {
			if (mCantainers[i][y] != 0 ) {
				value[1] = i;//栈顶的在哪一行
				value[0] = mCantainers[i][y];//得到最高处的一个马上结束
				value[2] = y;
				System.out.println("TopValue:value[0]="+value[0]+";value[1]="+value[1]+",value[2]="+value[2]);
				return value;
			}
		}
		return value;
				
	}
	
}