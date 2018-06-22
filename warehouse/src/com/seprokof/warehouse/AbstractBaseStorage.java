package com.seprokof.warehouse;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Abstract class representing product storage. It is not thread safe.
 * 
 * @author seprokof
 *
 */
public abstract class AbstractBaseStorage implements Serializable {
	private static final long serialVersionUID = 3891565328954934477L;

	protected Map<Good, Integer> goods = new HashMap<>();

	/**
	 * Add product to the store.
	 * 
	 * @param product
	 *            product to add
	 * @param quantity
	 *            how many instances
	 * @throws IllegalArgumentException
	 *             will be thrown if the <code>product</code> or
	 *             <code>quantity</code> is <code>null</code> or
	 *             <code>quantity</code> is below 1
	 */
	public void addGood(Good product, Integer quantity) {
		if (product == null || quantity == null || quantity < 1) {
			throw new IllegalArgumentException("Either product or quality is incorrect");
		}
		goods.merge(product, quantity, (o, n) -> o + n);
	}

	/**
	 * Removes product from the store.
	 * 
	 * @param product
	 *            product to remove
	 * @param quantity
	 *            how many instances
	 * @throws IllegalArgumentException
	 *             will be thrown if <code>product</code> or
	 *             <code>quantity</code> is <code>null</code> or
	 *             <code>quantity</code> is below 1 or store doesn't have
	 *             required quantity
	 */
	public void removeGood(Good product, Integer quantity) {
		if (product == null || quantity == null || quantity < 1) {
			throw new IllegalArgumentException("Either product or quality is incorrect");
		}
		Integer current = goods.get(product);
		if (current == null || current < quantity) {
			throw new IllegalArgumentException("Insufficient good quantity :(");
		}
		goods.put(product, current - quantity);
	}

}
