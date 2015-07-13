package com.ycsoft.commons.helper;

import java.util.Random;

/**
 * 获取随机值，帮助类
 *
 * @author hh
 * @date Dec 29, 2009 4:25:56 PM
 */
public class RandomHelper {

	/**
	 * 实例化随机对象实例，以当前的毫秒数为随机种子
	 */
	private static Random rand = new Random(System.currentTimeMillis());

	private RandomHelper(){}

	/**
	 * 取自此随机数生成器序列的在0(包括)和指定值max(不包括)之间均匀分布的int值
	 * @param max 最大值(不包含本身)
	 */
	public static int getNumInner( int max ) {
		return rand.nextInt( 3 );
	}

	/**
	 * 获取两则之间的随机数
	 * @param min 最小值(包含本身)
	 * @param max 最大值(包含本身)
	 * @return
	 */
	public static int getBetween( int min , int max ){
		return rand.nextInt( max ) % ( max - min + 1 ) + min ;
	}
}
