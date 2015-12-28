package com.mine.test;

import java.util.ArrayList;

public class TransContainer {
	/**
	 * 行数，同时指代  T：额定堆存高度；
	 */
	private static int mRaw = 4;
	/**
	 * 列数，同时指代 r：一个贝中所包含的栈的数量；
	 */
	public static  int mColumn = 6;
	/**
	 * 现有的集装箱数量
	 */
	private int mCount = 16;
	private int transTimes = 0;
	/**
	 * 
	 */
	private int[][] mCantainers = new int[mRaw][mColumn];
	
	private StringBuilder mBuilder;

	private ArrayList<Integer> mCostList = new ArrayList<Integer>();
	
	private static ArrayList<Integer[]> mCase = new ArrayList<Integer[]>();
	
	private int mCost = 0;
	
	public void initView() {
		initData();
		takeCantainer();
		for (int y = 0; y < mColumn; y++) {
			for (int x = 0; x < mRaw; x++) {
				mBuilder.append("第"+x+"行；"+"第"+y+"列；"+"数值"+mCantainers[x][y]+"\n");
//				System.out.println("第"+x+"行；"+"第"+y+"列；"+"数值"+mCantainers[x][y]);
			}
		}
		mBuilder.append("倒箱次数"+transTimes);
		for (int i = 0; i < mCostList.size(); i++) {
			mCost += mCostList.get(i);
		}
		mBuilder.append("\n倒箱代价共计"+mCost+"\n*******************************************************************************");
//		System.out.println("倒箱次数"+transTimes+"\n");
//		transTimes = 0;
		System.out.println(mBuilder.toString());
	}
	
	private void countCost(int orgx,int orgy,int desx,int desy){
		int cost = Math.abs(desx - orgx)+2*(mRaw + 1)-(desy+orgy);
		mCostList.add(cost);
	}

	/**
	 * 移动集装箱
	 */
	private void takeCantainer() {
		for (int i = 0; i < mCount; i++) {
			int position = i+1;
			startMoving(position);
		}
	}
	/**
	 * 开始移动集装箱
	 * @param position 集装箱序号（优先级）
	 */
	private void startMoving(int position){
		for (int y = 0; y < mColumn; y++) {
			for (int x = 0; x < mRaw; x++) {
				if (position == mCantainers[x][y]) {
					hasObs(position,x, y);//判断position上方是否有阻塞物,并移除
					return;
				}
			}

		}
	}
	/**
	 * 判断是否有阻塞箱
	 * @param x 目标序号 行
	 * @param y 目标序号 列
	 * @return int[3] 阻塞箱的序号及位置信息
	 */
	private void hasObs(int position,int x,int y){
		int[] obs = null;
		for (int i = mRaw - 1 ; i > -1; i--) {
			if (mCantainers[i][y] != 0  ) {
				if ( mCantainers[i][y] != position) {
					
					obs =new int[3];
					obs[0] = mCantainers[i][y];
					obs[1] = i;
					obs[2] = y;
					
//				处理二号箱时出现直接移除现象
					
					//直接在此处执行办理操作
//				 int m = isColFull(x);
//				if (m == -1) {
//					System.out.println("空列"+m);
//				}else if (m > -1 && m < 3) {
//					System.out.println("有空位"+m);
//				}else if (m == 3) {
//					System.out.println("满栈"+m);
//				}
					isPriorOthers(obs[0], obs[1], obs[2]);
					isPrior(obs[0], obs[1], obs[2]);
				}else {
					mBuilder.append("Now remove mCantainers["+x+"]["+y+"]="+mCantainers[x][y]+"\n");
//					System.out.println("Now remove mCantainers["+x+"]["+y+"]="+mCantainers[x][y]);
					mCantainers[x][y] = 0;
					mBuilder.append("Than mCantainers["+x+"]["+y+"]="+mCantainers[x][y]+"\n");
//					System.out.println("Than mCantainers["+x+"]["+y+"]="+mCantainers[x][y]);
					return ;
				}
				
			}else if(mCantainers[i][y] == 0){
				//暂时不知道写什么
				mBuilder.append("current position="+position+";Than mCantainers["+i+"]["+y+"]="+mCantainers[i][y]+":not obstruction\n");
//				System.out.println("current position="+position+";Than mCantainers["+i+"]["+y+"]="+mCantainers[i][y]+"not obstruction");
			}
		}
		
	}
	/**
	 * 初始化栈列数据
	 */
	public void initData() {
		 int[][]	cantainers = {{10,14,4,7,15,12},{6,9,8,16,11,2},{13,5,0,1,0,3},{0,0,0,0,0,0}};
		 //int[mRaw][mColumn]
		 mBuilder = new StringBuilder();
		 mCantainers = cantainers;
	}
	/**
	 * 检测某列是否已满
	 * 启发式算法第一条
	 * 1）选取未到额定堆存高度(mRaw)的空箱位作为阻塞箱的堆存箱位；
	 * @param y 列
	 * @return top < 3 此列不满栈;top == 3 此列已满栈;top == -1 异常
	 */
	private int isColFull(int y){
		int top = 3;
		for (int i = 0; i < mRaw; i++) {
			if (mCantainers[i][y] == 0) {
				if (i == 0) {
					return -1;
				}
				return i-1;
			}else {
				top = i;
			}
		}
		return top;
	}
	/**
	 * 获取某一列最小值(优先)和最大值(非优先)
	 * @param y 列序号
	 * @return {min,max} {-1,-1} 未检测,出现异常;(0,0) 此列为空;
	 */
	private int[] getMinOrMax(int y){
		int[] group = {-1,-1};
		group[0] = mCantainers[0][y];//最小值
		group[1] = mCantainers[0][y];//最大值
		for (int i = 0; i < mRaw; i++) {
			if (mCantainers[i][y] != 0 && mCantainers[i][y] < group[0]) {
				group[0] = mCantainers[i][y];
			}
			if (mCantainers[i][y] > group[1]) {
				group[1] = mCantainers[i][y];
			}
		}
		return group;
	}
	/**
	 * 判断阻塞箱的序号优先级是否高于某一栈内的所有集装箱
	 * 2）若阻塞箱的优先级高于候选栈内所有集装箱的优先级，则优选此栈；
	 * @param obstructionNO 阻塞箱的序号
	 * @param x 阻塞箱所在行
	 * @param y 阻塞箱所在列 与目标集装箱同列
	 */
	private void isPrior(int obstructionNO,int x,int y){
		boolean hasMoved = false;
		for (int i = 0; i < mColumn; i++) {
			if (i != y) {
				int[] group = getMinOrMax(i);
				if (group[0] > 0 && obstructionNO < group[0]) {
					int top = isColFull(i);
					if (top > -1 && top < 3) {
						
//						System.out.println("mCantainers["+x+"]["+y+"]="+mCantainers[x][y]
//								+"转移至mCantainers["+top+"]["+i+"]="+mCantainers[top][i]
//								+"上方mCantainers["+(top+1)+"]["+i+"]="+mCantainers[top+1][i]);
						boolean mSame = true;
						for (int j = 0; j < mCase.size(); j++) {
							Integer[] temp = mCase.get(j);
							if (temp[0]==x & temp[1] == y & temp[2] == top+1 & i == temp[3]) {
								mSame &= false;
							}
						}
						if (!mSame) {
							continue;
						}else {
							mBuilder.append("mCantainers["+x+"]["+y+"]="+mCantainers[x][y]
									+"转移至mCantainers["+top+"]["+i+"]="+mCantainers[top][i]
									+"上方mCantainers["+(top+1)+"]["+i+"]="+mCantainers[top+1][i]+"\n");
							mCantainers[top+1][i] = mCantainers[x][y];
							mCantainers[x][y] = 0;
							Integer[] temp = {x,y,top+1,i};
							mCase.add(temp);
							mBuilder.append("now mCantainers["+x+"]["+y+"]="+mCantainers[x][y]
									+";now mCantainers["+(top+1)+"]["+i+"]="+mCantainers[top+1][i]+"\n");
//						System.out.println("now mCantainers["+x+"]["+y+"]="+mCantainers[x][y]
//								+";now mCantainers["+(top+1)+"]["+i+"]="+mCantainers[top+1][i]);
							transTimes++;
							countCost(x, y, top+1, i);
							
							mBuilder.append("倒箱第"+transTimes+"次\n");
//						System.out.println("倒箱第"+transTimes+"次");
							hasMoved |= true;
							return;
						}
					}else {
//						continue;
						mBuilder.append("列"+i+"满栈;top = "+top+"\n");
//						System.out.println("列"+i+"满栈;top = "+top);
					}
				}else if(group[0]==0 && group[1] == 0 ){
					//这一步雷同条件3 isColEmpty
					mBuilder.append("阻塞箱转移至空列\n");
					mBuilder.append("mCantainers["+x+"]["+y+"]="+mCantainers[x][y]
							+"转移至mCantainers[0]["+i+"]="+mCantainers[0][i]
							+"上方mCantainers[1]["+i+"]="+mCantainers[1][i]+"\n");
//					System.out.println("阻塞箱转移至空列");
//					System.out.println("mCantainers["+x+"]["+y+"]="+mCantainers[x][y]
//							+"转移至mCantainers[0]["+i+"]="+mCantainers[0][i]
//							+"上方mCantainers[1]["+i+"]="+mCantainers[1][i]);
					mCantainers[0][i] = mCantainers[x][y];
					mCantainers[x][y] = 0;
					mBuilder.append("now mCantainers["+x+"]["+y+"]="+mCantainers[x][y]
							+";now mCantainers[0]["+i+"]="+mCantainers[0][i]+"\n");
//					System.out.println("now mCantainers["+x+"]["+y+"]="+mCantainers[x][y]
//							+";now mCantainers[0]["+i+"]="+mCantainers[0][i]);
					countCost(x, y, 1, i);
					transTimes++;
					mBuilder.append("倒箱第"+transTimes+"次\n");
//					System.out.println("倒箱第"+transTimes+"次");
//					System.out.println("obstructionNO > group[0]:"+obstructionNO+">"+group[0]);
					hasMoved |= true;
					return;
				}else {
					mBuilder.append("obstructionNO > group[0]:"+obstructionNO+">"+group[0]+"\n");
//					System.out.println("obstructionNO > group[0]:"+obstructionNO+">"+group[0]);
				}
			}
		}
		/**
		 * 如果循环完成,阻塞箱却未被移除,则执行条件3
		 */
		if (!hasMoved) {
			isColEmptyOthers(obstructionNO, x, y);
			int col = isColEmpty(obstructionNO, x, y);
			if (col > -1 && col < mColumn) {
				mBuilder.append("阻塞箱转移至空列\n");
//				System.out.println("阻塞箱转移至空列");
				mBuilder.append("mCantainers["+x+"]["+y+"]="+mCantainers[x][y]
						+"转移至mCantainers[0]["+col+"]="+mCantainers[0][col]
						+"上方mCantainers[1]["+col+"]="+mCantainers[1][col]+"\n");
//				System.out.println("mCantainers["+x+"]["+y+"]="+mCantainers[x][y]
//						+"转移至mCantainers[0]["+col+"]="+mCantainers[0][col]
//						+"上方mCantainers[1]["+col+"]="+mCantainers[1][col]);
				mCantainers[1][col] = mCantainers[x][y];
				mCantainers[x][y] = 0;
				mBuilder.append("now mCantainers["+x+"]["+y+"]="+mCantainers[x][y]
						+";now mCantainers[1]["+col+"]="+mCantainers[1][col]+"\n");
//				System.out.println("now mCantainers["+x+"]["+y+"]="+mCantainers[x][y]
//						+";now mCantainers[1]["+col+"]="+mCantainers[1][col]);
				countCost(x, y, 1, col);
				transTimes++;
				
				mBuilder.append("倒箱第"+transTimes+"次\n");
//				System.out.println("倒箱第"+transTimes+"次");
			}else {
				mBuilder.append("转移到没有阻塞箱的优先级较高的集装箱上方\n");
//				System.out.println("转移到没有阻塞箱的优先级较高的集装箱上方");
				aboveNothingOthers(obstructionNO, x, y);
				int[] above =aboveNothing(obstructionNO,x,y);
				if (above == null) {
					selectLessObs(obstructionNO, x, y);
				}
			}
		}
	}
	/**
	 * 3）判断是否有空列，若有空列，则优选此空列栈；
	 * @param obstructionNO 阻塞箱的序号
	 * @param x 阻塞箱所在行
	 * @param y 阻塞箱所在列 与目标集装箱同列
	 * @return -1 所有列都不为空栈；0~n 空栈列序号
	 */
	private int isColEmpty(int obs,int x ,int y){
		for (int i = 0; i < mColumn; i++) {
			if (i!= y && mCantainers[0][i] == 0) {
				return i;
			}else {
				
			}
		} 
		return -1;
	}
	
	/**
	 * 4）若贝内某个栈中最高优先级的集装箱上方没有阻塞箱，则优选此栈
	 * 这一步会在判断所有栈不为空的情况下执行
	 * @param obs 阻塞箱的序号
	 * @param x 阻塞箱所在行
	 * @param y 阻塞箱所在列 与目标集装箱同列
	 * @return int[] length == 1 ，最高优先级上方有阻塞箱；null，全部不可用；符合条件返回int[3]数组
	 */
	private int[] aboveNothing(int obs ,int x,int y){
		int[] site =  {-1};	
		for (int i = 0; i < mColumn; i++) {
			if (i != y) {
				/**
				 * 某列中的最小值,0:value;1,所在行;2:所在列
				 */
				 int[] minValue = {mCantainers[0][i],0,i};
				for (int j = 0; j < mRaw; j++) {
					if (mCantainers[j][i] != 0 && minValue[0] > mCantainers[j][i]) {
						minValue[0] = mCantainers[j][i];
						minValue[1] = j;
						minValue[2] = i;
					}
				}
				int mX = minValue[1];
				int mY = minValue[2];
				if (mX+1 < mRaw ) {
					int above = mCantainers[mX+1][mY];
					if (above != 0) {
						mBuilder.append("贝内某个栈中最高优先级的集装箱上方有阻塞箱，列："+i+"\n");
//						System.out.println("贝内某个栈中最高优先级的集装箱上方有阻塞箱，列："+i);
					}else {
						mBuilder.append("贝内某个栈中最高优先级的集装箱上方没有阻塞箱，列："+i+"\n");
//						System.out.println("贝内某个栈中最高优先级的集装箱上方没有阻塞箱，列："+i);
						mBuilder.append("mCantainers["+x+"]["+y+"]="+mCantainers[x][y]
								+"转移至mCantainers["+mX+"]["+i+"]="+mCantainers[mX][i]
								+"上方mCantainers["+(mX+1)+"]["+i+"]="+mCantainers[mX+1][i]+"\n");
//						System.out.println("mCantainers["+x+"]["+y+"]="+mCantainers[x][y]
//								+"转移至mCantainers["+mX+"]["+i+"]="+mCantainers[mX][i]
//								+"上方mCantainers["+(mX+1)+"]["+i+"]="+mCantainers[mX+1][i]);
						mCantainers[mX+1][i] = mCantainers[x][y];
						mCantainers[x][y] = 0;
						mBuilder.append("now mCantainers["+x+"]["+y+"]="+mCantainers[x][y]
								+";now mCantainers["+(mX+1)+"]["+i+"]="+mCantainers[mX+1][i]+"\n");
//						System.out.println("now mCantainers["+x+"]["+y+"]="+mCantainers[x][y]
//								+";now mCantainers["+(mX+1)+"]["+i+"]="+mCantainers[mX+1][i]);
						countCost(x, y, mX+1, i);
						transTimes++;
						mBuilder.append("倒箱第"+transTimes+"次\n");
//						System.out.println("倒箱第"+transTimes+"次");
						return minValue;
					}
				}
			}
		}
		mBuilder.append("没有符合此条件的列，不可用\n");
		System.out.println("没有符合此条件的列，不可用");
		return null;
	}
//	5）若不存在满足上述规则的空箱位，则选取栈中最高优先级的集装箱上方阻塞箱最少的空箱位作为堆存箱位。
//	其中，若最上层集装箱的优先级比阻塞箱的优先级高，则优选其中优先级最低的栈；
//	若最上层集装箱的优先级比阻塞箱的优先级低，则优选其中优先级最高的栈。
	public  void selectLessObs(int obs , int x ,int y){
//		int[][]	cantainers = {{10,14,4,7,15,12},{6,9,8,16,11,2},{13,5,0,1,0,3},{0,0,0,0,0,0}};
//		mCantainers = cantainers;
		ArrayList<Integer[]> array = new ArrayList<Integer[]>();
		for (int i = 0; i < mColumn; i++) {
			Integer[] temp = {0,0,0,0};
			temp[0] = mCantainers[0][i];
			if (i != x) {
				for (int j = 0; j < mRaw; j++) {
					if (mCantainers[j][i] != 0 && mCantainers[j][i]<temp[0]) {
						temp[0] = mCantainers[j][i];
						temp[1] = j;
						temp[2] = i;
					}
				}
				for (int j = temp[1]+1; j < mRaw; j++) {
					if (mCantainers[j][i] != 0) {
						temp[3]++;
					}
				}
			}
			array.add(temp);
		}
		Integer[] temp = array.get(0) ;
		for (int i = 0; i < array.size(); i++) {
			if (temp[3] > array.get(i)[3]) {
				temp = array.get(i);
				System.out.println("条件 5 优先选中的情况array[i] = {"+array.get(i)[0]+","+array.get(i)[1]+","+array.get(i)[2]+","+array.get(i)[3]+"}");
			}else if(temp[3] == array.get(i)[3]) {
				//等待条件清晰。暂时无法添加
			}
		}
//		StringBuffer mBuilder = new StringBuffer();
		mBuilder.append("mCantainers["+x+"]["+y+"]="+mCantainers[x][y]
				+"转移至mCantainers["+(temp[1]+temp[3])+"]["+temp[2]+"]="+mCantainers[(temp[1]+temp[3])][temp[2]]
				+"上方mCantainers["+(temp[1]+temp[3]+1)+"]["+temp[2]+"]="+mCantainers[(temp[1]+temp[3])+1][temp[2]]+"\n");
//		System.out.println("mCantainers["+x+"]["+y+"]="+mCantainers[x][y]
//				+"转移至mCantainers["+mX+"]["+i+"]="+mCantainers[mX][i]
//				+"上方mCantainers["+(mX+1)+"]["+i+"]="+mCantainers[mX+1][i]);
		mCantainers[(temp[1]+temp[3])+1][temp[2]] = mCantainers[x][y];
		mCantainers[x][y] = 0;
		mBuilder.append("now mCantainers["+x+"]["+y+"]="+mCantainers[x][y]
				+";now mCantainers["+((temp[1]+temp[3])+1)+"]["+temp[2]+"]="+mCantainers[(temp[1]+temp[3])+1][temp[2]]+"\n");
//		System.out.println("now mCantainers["+x+"]["+y+"]="+mCantainers[x][y]
//				+";now mCantainers["+(mX+1)+"]["+i+"]="+mCantainers[mX+1][i]);
		countCost(x, y, (temp[1]+temp[3])+1, temp[2]);
//		System.out.println(mBuilder.toString());
	} 
	
	/**
	 * 检测所有可能性
	 * 判断阻塞箱的序号优先级是否高于某一栈内的所有集装箱
	 * 2）若阻塞箱的优先级高于候选栈内所有集装箱的优先级，则优选此栈；
	 * @param obstructionNO 阻塞箱的序号
	 * @param x 阻塞箱所在行
	 * @param y 阻塞箱所在列 与目标集装箱同列
	 */
	public void isPriorOthers(int obstructionNO,int x,int y){
		int chances = 0;
		for (int i = 0; i < mColumn; i++) {
			if (i != y) {
				int[] group = getMinOrMax(i);
				if (group[0] > 0 && obstructionNO < group[0]) {
					int top = isColFull(i);
					if (top > -1 && top < 3) {
						chances++;
						mBuilder.append("===mCantainers["+x+"]["+y+"]="+mCantainers[x][y]
								+"转移至mCantainers["+top+"]["+i+"]="+mCantainers[top][i]
								+"上方mCantainers["+(top+1)+"]["+i+"]="+mCantainers[top+1][i]
								+"第"+chances+"种可能===\n");
//						System.out.println("===mCantainers["+x+"]["+y+"]="+mCantainers[x][y]
//								+"转移至mCantainers["+top+"]["+i+"]="+mCantainers[top][i]
//								+"上方mCantainers["+(top+1)+"]["+i+"]="+mCantainers[top+1][i]
//								+"第"+chances+"种可能===");
//						mCantainers[top+1][i] = mCantainers[x][y];
//						mCantainers[x][y] = 0;
//						System.out.println("now mCantainers["+x+"]["+y+"]="+mCantainers[x][y]
//								+";now mCantainers["+(top+1)+"]["+i+"]="+mCantainers[top+1][i]);
//						System.out.println("倒箱第"+transTimes+"次===");
					}else {
//						continue;
						mBuilder.append("列"+i+"满栈;top = "+top+"\n");
//						System.out.println("列"+i+"满栈;top = "+top);
					}
				}else if(group[0]==0 && group[1] == 0 ){
					//这一步雷同条件3 isColEmpty
					chances++;
					mBuilder.append("===阻塞箱转移至空列===\n");
					mBuilder.append("===mCantainers["+x+"]["+y+"]="+mCantainers[x][y]
							+"转移至mCantainers[0]["+i+"]="+mCantainers[0][i]
							+"上方mCantainers[1]["+i+"]="+mCantainers[1][i]
									+"第"+chances+"种可能===\n");
//					System.out.println("===阻塞箱转移至空列");
//					System.out.println("===mCantainers["+x+"]["+y+"]="+mCantainers[x][y]
//							+"转移至mCantainers[0]["+i+"]="+mCantainers[0][i]
//							+"上方mCantainers[1]["+i+"]="+mCantainers[1][i]
//									+"第"+chances+"种可能===");
//					mCantainers[0][i] = mCantainers[x][y];
//					mCantainers[x][y] = 0;
//					System.out.println("now mCantainers["+x+"]["+y+"]="+mCantainers[x][y]
//							+";now mCantainers[0]["+i+"]="+mCantainers[0][i]);
//					transTimes++;
//					System.out.println("倒箱第"+transTimes+"次===");
//					System.out.println("obstructionNO > group[0]:"+obstructionNO+">"+group[0]);
				}else {
					mBuilder.append("===obstructionNO > group[0]:"+obstructionNO+">"+group[0]+"===\n");
//					System.out.println("===obstructionNO > group[0]:"+obstructionNO+">"+group[0]+"===");
				}
			}
		}
		mBuilder.append("===符合条件2共"+chances+"种可能性===\n");
//		System.out.println("===符合条件2共"+chances+"种可能性===");
	}
	
	/**
	 * 检测所有可能性
	 * 3）判断是否有空列，若有空列，则优选此空列栈；
	 * @param obstructionNO 阻塞箱的序号
	 * @param x 阻塞箱所在行
	 * @param y 阻塞箱所在列 与目标集装箱同列
	 * @return 有多少种可能
	 */
	private int isColEmptyOthers(int obs,int x ,int y){
		int chances = 0;
		for (int i = 0; i < mColumn; i++) {
			if (i!= y && mCantainers[0][i] == 0) {
				chances++;
			}else {
				
			}
		} 
		mBuilder.append("===符合条件3共"+chances+"种可能===\n");
//		System.out.println("===符合条件3共"+chances+"种可能");
		return chances;
	}
	
	/**
	 * 检测其他可能性
	 * 4）若贝内某个栈中最高优先级的集装箱上方没有阻塞箱，则优选此栈
	 * 这一步会在判断所有栈不为空的情况下执行
	 * @param obs 阻塞箱的序号
	 * @param x 阻塞箱所在行
	 * @param y 阻塞箱所在列 与目标集装箱同列
	 * @return int[] length == 1 ，最高优先级上方有阻塞箱；null，全部不可用；符合条件返回int[3]数组
	 */
	private int[] aboveNothingOthers(int obs ,int x,int y){
		int chances = 0;
		int[] site =  {-1};	
		for (int i = 0; i < mColumn; i++) {
			if (i != y) {
				/**
				 * 某列中的最小值,0:value;1,所在行;2:所在列
				 */
				 int[] minValue = {mCantainers[0][i],0,i};
				for (int j = 0; j < mRaw; j++) {
					if (mCantainers[j][i] != 0 && minValue[0] > mCantainers[j][i]) {
						minValue[0] = mCantainers[j][i];
						minValue[1] = j;
						minValue[2] = i;
					}
				}
				int mX = minValue[1];
				int mY = minValue[2];
				if (mX+1 < mRaw ) {
					int above = mCantainers[mX+1][mY];
					if (above != 0) {
						mBuilder.append("===贝内某个栈中最高优先级的集装箱上方有阻塞箱，列："+i+"===\n");
//						System.out.println("贝内某个栈中最高优先级的集装箱上方有阻塞箱，列："+i);
					}else {
						mBuilder.append("===贝内某个栈中最高优先级的集装箱上方没有阻塞箱，列："+i+"===\n");
//						System.out.println("===贝内某个栈中最高优先级的集装箱上方没有阻塞箱，列："+i);
						mBuilder.append("===mCantainers["+x+"]["+y+"]="+mCantainers[x][y]
								+"转移至mCantainers["+mX+"]["+i+"]="+mCantainers[mX][i]
								+"上方mCantainers["+(mX+1)+"]["+i+"]="+mCantainers[mX+1][i]+"===\n");
//						System.out.println("===mCantainers["+x+"]["+y+"]="+mCantainers[x][y]
//								+"转移至mCantainers["+mX+"]["+i+"]="+mCantainers[mX][i]
//								+"上方mCantainers["+(mX+1)+"]["+i+"]="+mCantainers[mX+1][i]+"===");
						chances++;
//						mCantainers[mX+1][i] = mCantainers[x][y];
//						mCantainers[x][y] = 0;
//						System.out.println("now mCantainers["+x+"]["+y+"]="+mCantainers[x][y]
//								+";now mCantainers["+(mX+1)+"]["+i+"]="+mCantainers[mX+1][i]);
//						transTimes++;
						mBuilder.append("===符合条件4共"+chances+"种可能===\n");
//						System.out.println("===符合条件4共"+chances+"种可能===");
						return minValue;
					}
				}
			}
		}
		return null;
	}
	
}
