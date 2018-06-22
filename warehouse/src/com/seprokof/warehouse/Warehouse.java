package com.seprokof.warehouse;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents warehouse entity.
 * 
 * @author seprokof
 *
 */
public class Warehouse extends AbstractBaseStorage {
	private static final long serialVersionUID = 6613809657846909144L;

	private final Map<String, Good> nameToGood = new HashMap<>();

	/**
	 * Retrieves full information about stored goods.
	 * 
	 * @return the list of stored goods or empty list
	 */
	public Map<Good, Integer> getGoods() {
		return goods;
	}

	/**
	 * Retrieves information about product by it's name.
	 * 
	 * @param name
	 *            the name of the product
	 * @return product or <code>null</code> if there is no such product in the
	 *         store
	 * @thrown NullPointerException will be thrown if specified
	 *         <code>name</code> is <code>null</code>
	 */
	public Good getGoodByName(String name) {
		return nameToGood.get(name);
	}

	/**
	 * {@inheritDoc}
	 * 
	 */
	@Override
	public void addGood(Good product, Integer quantity) {
		super.addGood(product, quantity);
		nameToGood.put(product.getName(), product);
	}

	/**
	 * Transfers product from the {@link Provider} to the store.
	 * 
	 * @param provider
	 *            the provider
	 * @param product
	 *            the product
	 * @param quantity
	 *            how many instances of the product transfer
	 * @throws IllegalArgumentException
	 *             will be thrown if <code>provider</code>, <code>product</code>
	 *             or <code>quantity</code> is <code>null</code> or
	 *             <code>quantity</code> is below 1 or provider doesn't have
	 *             required quantity
	 */
	public void supply(Provider provider, Good product, Integer quantity) {
		provider.removeGood(product, quantity);
		addGood(product, quantity);
	}

	/**
	 * Transfers product from the {@link Warehouse} to the {@link Client}.
	 * 
	 * @param client
	 *            the client who will buy products
	 * @param product
	 *            the product
	 * @param quantity
	 *            how many instances of the product transfer
	 * @throws IllegalArgumentException
	 *             will be thrown if <code>client</code>, <code>product</code>
	 *             or <code>quantity</code> is <code>null</code> or
	 *             <code>quantity</code> is below 1 or provider doesn't have
	 *             required quantity
	 */
	public void sell(Client client, Good product, Integer quantity) {
		removeGood(product, quantity);
		client.addGood(product, quantity);
	}

}
