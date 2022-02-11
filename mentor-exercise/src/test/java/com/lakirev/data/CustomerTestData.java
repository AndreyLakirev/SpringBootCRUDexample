package com.lakirev.data;

import com.lakirev.customer.model.Customer;
import com.lakirev.customer.model.Product;
import com.lakirev.customer.model.Purchase;

import java.util.Arrays;

public class CustomerTestData {
    public static Customer CUSTOMER1 = new Customer("Dmitriy", 25, Arrays.asList(new Purchase(50, new Product("Milk"), null)
            , new Purchase(30, new Product("Bread"), null)
            , new Purchase(150, new Product("Butter"), null)));

    public static Customer CUSTOMER2 = new Customer("Aleksei", 33, Arrays.asList(new Purchase(20, new Product("Water"), null)
            , new Purchase(70, new Product("Chocolate"), null)
            , new Purchase(80, new Product("Juice"), null)));

    public static String CUSTOMER_JSON1 = "{\"name\": \"Dmitriy\", \"age\": 25, \"purchases\": [{\"cost\": 50, \"product\": {\"name\": \"Milk\"}}" +
            ", {\"cost\": 30, \"product\": {\"name\": \"Bread\"}}" +
            ", {\"cost\": 150, \"product\": {\"name\": \"Butter\"}}]}";

    public static String CUSTOMER_JSON_WITH_NULLS1 = "{\"id\": null, \"name\": \"Dmitriy\", \"age\": 25, \"purchases\": [{\"id\": null, \"cost\": 50, \"product\": {\"id\": null, \"name\": \"Milk\"}, \"date\": null}" +
            ", {\"id\": null, \"cost\": 30, \"product\": {\"id\": null, \"name\": \"Bread\"}, \"date\": null}" +
            ", {\"id\": null, \"cost\": 150, \"product\": {\"id\": null, \"name\": \"Butter\"}, \"date\": null}]}";

    public static String CUSTOMER_JSON2 = "{\"name\": \"Aleksei\", \"age\": 33, \"purchases\": [{\"cost\": 20, \"product\": {\"name\": \"Water\"}}" +
            ", {\"cost\": 70, \"product\": {\"name\": \"Chocolate\"}}" +
            ", {\"cost\": 80, \"product\": {\"name\": \"Juice\"}}]}";

    public static String CUSTOMER_JSON_LIST = "[" + CUSTOMER_JSON1 + ", " + CUSTOMER_JSON2 + "]";
}
