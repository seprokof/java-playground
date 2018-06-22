package com.seprokof.warehouse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

/**
 * Application entry point.
 * 
 * @author seprokof
 *
 */
public class Main {
	private static final String WH_FILE = "warehouse.ser";
	private static final String CL_FILE = "clients.ser";
	private static final String PR_FILE = "providers.ser";

	private static final String OPTION_TEMPLATE = "Press %s to %s";

	public static void main(String[] args) throws IOException {
		List<Warehouse> warehouses = deserializeOrDefault(WH_FILE, Warehouse.class);
		Warehouse wh = warehouses.isEmpty() ? new Warehouse() : warehouses.get(0);
		List<Provider> providers = deserializeOrDefault(PR_FILE, Provider.class);
		List<Client> clients = deserializeOrDefault(CL_FILE, Client.class);

		try (Scanner userInput = new Scanner(System.in)) {
			while (true) {
				System.out.println("************** Menu **************");
				System.out.println(String.format(OPTION_TEMPLATE, "1", "add a good to a store"));
				System.out.println(String.format(OPTION_TEMPLATE, "2", "sell good to a buyer"));
				System.out.println(String.format(OPTION_TEMPLATE, "3", "get information about the product"));
				System.out.println(String.format(OPTION_TEMPLATE, "4", "check if the good exists in the store"));
				System.out.println(String.format(OPTION_TEMPLATE, "5", "get information about providers"));
				System.out.println(String.format(OPTION_TEMPLATE, "6", "get information about buyers"));
				System.out
						.println(String.format(OPTION_TEMPLATE, "7", "get information about available goods in store"));
				System.out.println(String.format(OPTION_TEMPLATE, "8", "view the journal"));
				System.out.println(String.format(OPTION_TEMPLATE, "9", "supply good from provider"));
				System.out.println(String.format(OPTION_TEMPLATE, "10", "exit"));
				System.out.println("**********************************");

				if (!userInput.hasNextInt()) {
					userInput.next();
					continue;
				}

				int option = userInput.nextInt();
				userInput.nextLine();

				switch (option) {
				case 1:
					addGoodToStore(userInput, wh);
					break;
				case 2:
					sellGoodToBuyer(userInput, wh, clients);
					break;
				case 3:
					getProductInfo(userInput, wh);
					break;
				case 4:
					getProductInfo(userInput, wh);
					break;
				case 5:
					providers.forEach(System.out::println);
					break;
				case 6:
					clients.forEach(System.out::println);
					break;
				case 7:
					wh.getGoods().entrySet().forEach(e -> System.out.println(e.getKey() + " left " + e.getValue()));
					break;
				case 9:
					supplyGood(userInput, wh, providers);
					break;
				case 10:
					serializeQuiet(WH_FILE, Collections.singletonList(wh));
					serializeQuiet(CL_FILE, clients);
					serializeQuiet(PR_FILE, providers);
					return;
				default:
					// do nothing
					;
				}
			}
		}
	}

	private static void addGoodToStore(Scanner scanner, Warehouse warehouse) {
		System.out.println("Enter product name you want to add");
		String goodName = scanner.nextLine();
		Good g = warehouse.getGoodByName(goodName);
		if (g == null) {
			g = new Good();
			g.setName(goodName);
			System.out.println("Enter price");
			g.setPrice(readDouble(scanner));
		}
		System.out.println("Enter quantity");
		warehouse.addGood(g, readInteger(scanner));
	}

	private static void sellGoodToBuyer(Scanner scanner, Warehouse warehouse, List<Client> clients) {
		System.out.println("Enter product name you want to sell to a buyer");
		String goodName = scanner.nextLine();
		Good g = warehouse.getGoodByName(goodName);
		if (g == null) {
			System.err.println("There is no such product with the name " + goodName + " in a store");
			return;
		}

		System.out.println("Enter the buyer id to whom you want to sell this product");
		String clientId = scanner.nextLine();
		Client fake = new Client();
		fake.setId(clientId);
		Client client;
		int index = clients.indexOf(fake);
		if (index > -1) {
			client = clients.remove(index);
		} else {
			client = fake;
			System.out.println("Enter client name");
			client.setName(scanner.nextLine());
			System.out.println("Enter client surname");
			client.setSurname(scanner.nextLine());
		}

		try {
			System.out.println("Enter quantity");
			warehouse.sell(client, g, scanner.nextInt());
			clients.add(client);
		} catch (IllegalArgumentException e) {
			System.err.println(e.getMessage());
		}
	}

	private static void getProductInfo(Scanner scanner, Warehouse warehouse) {
		System.out.println("Enter product name");
		String goodName = scanner.nextLine();
		Good g = warehouse.getGoodByName(goodName);
		if (g == null) {
			System.err.println("There is no such product with the name " + goodName + " in a store");
			return;
		}
		System.out.println(g);
	}

	private static void supplyGood(Scanner scanner, Warehouse warehouse, List<Provider> providers) {
		System.out.println("Enter the provider id from whom you want to supply product");
		String providerId = scanner.nextLine();
		Provider fake = new Provider();
		fake.setId(providerId);

		int index = providers.indexOf(fake);
		if (index < 0) {
			System.err.println("There is no provider with such id " + providerId);
			return;
		}

		Provider provider = providers.remove(index);

		System.out.println("Enter product name you want to supply from a provider");
		String goodName = scanner.nextLine();
		Good g = provider.getGoodWithName(goodName);
		if (g == null) {
			System.err.println("Provider doesn't have product with name " + goodName);
			return;
		}

		try {
			System.out.println("Enter quantity");
			warehouse.supply(provider, g, readInteger(scanner));
			providers.add(provider);
		} catch (IllegalArgumentException e) {
			System.err.println(e.getMessage());
		}
	}

	private static Double readDouble(Scanner scanner) {
		if (scanner.hasNextDouble()) {
			return scanner.nextDouble();
		} else {
			scanner.next();
			return readDouble(scanner);
		}
	}

	private static Integer readInteger(Scanner scanner) {
		if (scanner.hasNextInt()) {
			return scanner.nextInt();
		} else {
			scanner.next();
			return readInteger(scanner);
		}
	}

	private static <T> List<T> deserializeOrDefault(String fullPathFile, Class<T> nestedType) {
		List<T> result;
		try {
			result = Serializer.deserialize(fullPathFile, nestedType);
		} catch (IOException e) {
			System.out.println("Unable to load from " + fullPathFile);
			result = new ArrayList<>();
		}
		return result;
	}

	private static void serializeQuiet(String fileName, List<?> objects) {
		try {
			Serializer.serialize(fileName, objects);
		} catch (IOException e) {
			System.err.println("Unable to write to " + fileName);
			e.printStackTrace();
		}
	}

}
