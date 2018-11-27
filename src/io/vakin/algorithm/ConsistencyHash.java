/**
 * 
 */
package io.vakin.algorithm;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * @description <br>
 * @author <a href="mailto:vakinge@gmail.com">vakin</a>
 * @date 2016年8月22日
 */
public class ConsistencyHash {
	private TreeMap<Long, String> virtualNodes = new TreeMap<Long, String>();

	// 设置虚拟节点数目
	private static final int VIRTUAL_NUM = 32;
	
	

	public ConsistencyHash(List<String> actualNodes) {
		refresh(actualNodes);
	}

	public void refresh(List<String> actualNodes){
		virtualNodes.clear();
		for (int i = 0; i < actualNodes.size(); i++) {
			String shardInfo = actualNodes.get(i);
			for (int j = 0; j < VIRTUAL_NUM; j++) {
				virtualNodes.put(hash(i + "-" + j), shardInfo);
			}
		}
	}
	
	/**
	 * 根据hash因子获取分配的真实节点
	 * 
	 * @param factor
	 * @return
	 */
	public String getActualNode(Object factor) {
		Long key = hash(factor.toString());
		SortedMap<Long, String> tailMap = virtualNodes.tailMap(key);
		if (tailMap.isEmpty()) {
			key = virtualNodes.firstKey();
		} else {
			key = tailMap.firstKey();
		}
		return virtualNodes.get(key);
	}

	private static Long hash(String key) {
        if(key == null)return 0L;
		ByteBuffer buf = ByteBuffer.wrap(key.getBytes());
		int seed = 0x1234ABCD;

		ByteOrder byteOrder = buf.order();
		buf.order(ByteOrder.LITTLE_ENDIAN);

		long m = 0xc6a4a7935bd1e995L;
		int r = 47;

		long h = seed ^ (buf.remaining() * m);

		long k;
		while (buf.remaining() >= 8) {
			k = buf.getLong();

			k *= m;
			k ^= k >>> r;
			k *= m;

			h ^= k;
			h *= m;
		}

		if (buf.remaining() > 0) {
			ByteBuffer finish = ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN);
			finish.put(buf).rewind();
			h ^= finish.getLong();
			h *= m;
		}

		h ^= h >>> r;
		h *= m;
		h ^= h >>> r;

		buf.order(byteOrder);
		return Math.abs(h);
	}
	
	public static void main(String[] args) {
		List<String> actualNodes = Arrays.asList("192.168.1.100","192.168.1.101","192.168.1.102","192.168.1.103");
		ConsistencyHash hash = new ConsistencyHash(actualNodes);
		for (int i = 0; i < VIRTUAL_NUM; i++) {
			System.out.println(hash.getActualNode(i));
		}
	}
	
}
