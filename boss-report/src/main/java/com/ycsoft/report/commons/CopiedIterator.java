package com.ycsoft.report.commons;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

/**
 * Map的迭代器副本
 * @param <K>
 */
public class CopiedIterator<K> implements Iterator<K> {
	private Iterator<K> iterator = null;

	private Map<K,?> map=null;
	
	private K o=null;
	public CopiedIterator(Map<K, ?> map) {
		this.map=map;
		LinkedList<K> list = new LinkedList<K>();
		Iterator<K> itr = map.keySet().iterator();
		while (itr.hasNext()) {
			list.add(itr.next());
		}
		this.iterator = list.iterator();
	}

	public boolean hasNext() {
		return this.iterator.hasNext();
	}

	public void remove() {
		this.map.remove(o);
	}

	public K next() {
		this.o=this.iterator.next();
		return this.o;
	}
}
