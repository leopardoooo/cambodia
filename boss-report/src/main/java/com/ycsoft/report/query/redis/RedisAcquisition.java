package com.ycsoft.report.query.redis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.ycsoft.commons.exception.ReportException;
import com.ycsoft.report.commons.ReportConstants;
import com.ycsoft.report.commons.SerializeUtil;
import com.ycsoft.report.dao.redis.JedisDao;
import com.ycsoft.report.query.cube.impl.AbstractDataSet;
import com.ycsoft.report.query.daq.DataReader;
import com.ycsoft.report.query.daq.translate.CacheAcquisition;
import com.ycsoft.report.query.daq.translate.CacheHeadCell;
import com.ycsoft.report.query.daq.translate.CacheKey;
import static com.ycsoft.report.commons.ReportConstants.memory_row_max;

/**
 * 使用redis执行cube运算
 * 当内存中计算的结果集大于1000条时，把结果转储到redis。
 * CacheKey的排序交给mongodb实现
 */
public class RedisAcquisition extends CacheAcquisition {
	
	private JedisDao jedisDao=null;
	private String query_id=null;;
	
	public RedisAcquisition(JedisDao jedisDao, String query_id){
		this.jedisDao=jedisDao;
		this.query_id= query_id;
	}
	/**
	 * RedisCacheKey含有在redis使用的键值
	 * @throws ReportException 
	 */
	@Override
	protected CacheKey createCacheKey(List<Object> keylist,List<Integer> sortlist) throws ReportException  {
		return new RedisCacheKey(keylist,sortlist,this.query_id);
	}

	/**
	 *  在redis的cube运算，当计算键值>memory_row_max时，使用redis缓存合并数据
	 * @return 
	 */
	@Override
	public DataReader translate(String query_cache_id, AbstractDataSet dataset)
			throws ReportException {
		
		List<CacheHeadCell> headlist=this.createCacheHeadCell(dataset);
		int no_crosshead_start=this.getNoCrossheadStart(headlist);
		//初始化排序器
		sort=new CacheKeySortByDB(no_crosshead_start,this.query_id);
		
		DataReader dr=super.translate(query_cache_id, dataset);
		//计算结果还在内存中
		if(this.useredis){
			if(this.getMenoryMap().size()>0){
				//this.mergeSet();
				mergeSetNoBacth();
			}
			return new RedisDR();
		}else{
			return dr;
		}
	}


	 private CacheKeySortByDB sort=null; 
	 private boolean useredis=false;
	/**
	 * 当java内存中运算的cube运算键值达到memory_row_max限额时，
	 * 把运算键值提交给redis缓存合并；清空java内存中运算的cube运算键值
	 */
	@Override
	protected boolean isMenoryMax() throws ReportException {
		if(this.getMenoryMap().size()>memory_row_max){
			//this.mergeSet();
			mergeSetNoBacth();
			this.getMenoryMap().clear();
			if(!this.useredis){
				this.useredis=true;
			}
		}
		return false;
	}
	
	/**
	 * 本地缓存单条提交redis合并运算
	 * @throws ReportException
	 */
	private void mergeSetNoBacth() throws ReportException{
		try{

			//cube缓存redis的keys
			List<CacheKey> sortdbkeys=new ArrayList<CacheKey>(ReportConstants.cache_batch_size);
			
			Iterator<CacheKey> it= this.getMenoryMap().keySet().iterator();
			while(it.hasNext()){
				//提取一个内存中键值
				RedisCacheKey key= (RedisCacheKey) it.next();
				byte[] rediskey=key.getRediskey().getBytes();
				
				byte[] redisvalue=this.jedisDao.get(rediskey);
				ArrayList<Double> localValue=(ArrayList<Double>) this.getMenoryMap().get(key);
				if(redisvalue==null){
					//缓存不存在则提交缓存
					this.jedisDao.set(rediskey, SerializeUtil.serialize(localValue));
					sortdbkeys.add(key);
				}else{
					//合并本地
					ArrayList<Double>  serverValue= (ArrayList<Double>) SerializeUtil.unserialize(redisvalue); 
					for(int j=0;j<localValue.size();j++){
						localValue.set(j,localValue.get(j).doubleValue()+serverValue.get(j).doubleValue());
					}
					this.jedisDao.set(rediskey, SerializeUtil.serialize(localValue));
				}
				//提交服务器
				if(sortdbkeys.size()>=ReportConstants.cache_batch_size||!it.hasNext()){
					sort.openWriteMode();
					sort.writeCacheKey(sortdbkeys);
					sortdbkeys.clear();
				}
				
			}
		}catch(ReportException e){
			sort.close();
			throw e;
		}catch(Exception e){
			sort.close();
			throw new ReportException(e);
		}
	}
	/**
	 * 本地缓存批量提交redis合并运算
	 * <p> 存在bug
	 * @throws ReportException 
	 */
	private void mergeSet() throws ReportException {
		try{
		//批量提交数量
		int batch_idx=0;
		//cube缓存redis的keys
		ArrayList<byte[]> rediskeys=new ArrayList<byte[]>(ReportConstants.cache_batch_size);
		//cube缓存redis的keysvalues
		ArrayList<byte[]> rediskeyvalues=new ArrayList<byte[]>(ReportConstants.cache_batch_size*2);
		//新计算出来的CacheKey列表
		List<CacheKey> newcechekeys=new ArrayList<CacheKey>(ReportConstants.cache_batch_size);
		Map<Integer,CacheKey> newcechekeyMap=new HashMap<Integer,CacheKey>(ReportConstants.cache_batch_size);
		
		Iterator<CacheKey> it= this.getMenoryMap().keySet().iterator();
		while(it.hasNext()){
			//提取一个内存中键值
			RedisCacheKey key= (RedisCacheKey) it.next();
			rediskeys.add(key.getRediskey().getBytes());
			newcechekeyMap.put(batch_idx, key);
			batch_idx++;
			//当从内存提取键值达到批量提交限额或元素迭代完时,提交redis缓存并合并相同的key对应的计算结果
			if(batch_idx>=ReportConstants.cache_batch_size||!it.hasNext()){
				batch_idx=0;//从新计数
				//从redi获取keys对应的values，如果获取的值为空，说明redis上不存在这个值的缓存
				List<byte[]> redisvalues=this.jedisDao.mget(rediskeys.toArray(new byte[rediskeys.size()][]));
				
				for(int i=0;i<rediskeys.size();i++){
					byte[] value=redisvalues.get(i);
					ArrayList<Double> cubeValue=(ArrayList<Double>) this.getMenoryMap().get(key);
					if(value!=null){
						//如果value不为空，redis获取的值和本地相同key的值相加
						List<Double> tempCubeValue= (List<Double>) SerializeUtil.unserialize(value); 
						
						
						
						for(int j=0;j<cubeValue.size();j++){

							cubeValue.set(j,cubeValue.get(j).doubleValue()+tempCubeValue.get(j).doubleValue());
						}
					}else{
						//value为空，说明CacheKey是新计算出来的
						newcechekeys.add(newcechekeyMap.get(i));
					
					}
					
					rediskeyvalues.add(rediskeys.get(i));
					rediskeyvalues.add(SerializeUtil.serialize(cubeValue));
				}
				this.jedisDao.mset(rediskeyvalues.toArray(new byte[rediskeyvalues.size()][]));
				rediskeys.clear();
				rediskeyvalues.clear();
				//新生成的cachekey提交sortdb保存
				if(newcechekeys.size()>=ReportConstants.cache_batch_size||!it.hasNext()){
					sort.openWriteMode();
					sort.writeCacheKey(newcechekeys);
					newcechekeys.clear();
				}
				newcechekeyMap.clear();
			}
		}
		}catch(ReportException e){
			sort.close();
			throw e;
		}catch(Exception e){
			sort.close();
			throw new ReportException(e);
		}
	}
	


	/**
	 * 使用sort_db排序key并从redis的cube缓存中提取计算结果
	 */
	class RedisDR implements DataReader{
		
		private int row_idx=-1;
		private int row_max=0;
		
		private List<RedisCacheKey> redisCacheKeys=new ArrayList<RedisCacheKey>();
		
		public void close() throws ReportException {
			sort.close();
		}

		public Object getObject(int i) throws ReportException {
			return redisCacheKeys.get(this.row_idx).getKeys().get(i-1);
		}

		public String getString(int i) throws ReportException {
			return redisCacheKeys.get(this.row_idx).getKeys().get(i-1).toString();
		}

		public boolean next() throws ReportException {
			this.row_idx++;
			if(row_idx>=this.row_max){
				
				redisCacheKeys.clear();
				List<byte[]> rediskeys=new ArrayList<byte[]>(ReportConstants.cache_batch_size);
				Map<Integer,RedisCacheKey> cachekeyMap=new HashMap<Integer,RedisCacheKey>(ReportConstants.cache_batch_size);
				for(int i=0;i<ReportConstants.cache_batch_size;i++){
					//从sortDB读取排序好的RedisCacheKey
					RedisCacheKey key= sort.readCacheKey();
					if(key==null){ break;}
					rediskeys.add(key.getRediskey().getBytes());
					cachekeyMap.put(i, key);
					
				}
				if(rediskeys.size()>0){
					//从redis缓存中提取计算结果
					byte[][] bytekeys=rediskeys.toArray(new byte[rediskeys.size()][]);
					List<byte[]> values=jedisDao.mget(bytekeys);
					for(int i=0;i<values.size();i++ ){
						RedisCacheKey key=cachekeyMap.get(i);
						key.getKeys().addAll((ArrayList<Double>)SerializeUtil.unserialize(values.get(i)));
						redisCacheKeys.add(key);
					}
					//删除已读取的缓存计算结果数据
					jedisDao.del(bytekeys);
				}
				row_max=redisCacheKeys.size();
				this.row_idx=0;
			}
			if(row_max<=0)
				return false;
			else
				return true;
		}

		public void open() throws ReportException {
			sort.openReadMode();
			row_idx=-1;
			row_max=0;
		}
		
	}
	
}
