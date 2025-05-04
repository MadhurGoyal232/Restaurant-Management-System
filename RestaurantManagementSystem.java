import java.io.*;
import java.util.*;

public class RestaurantManagementSystem {

    // MenuItem class
    static class MenuItem {
        int id;
        String name;
        double price;

        MenuItem(int id, String name, double price) {
            this.id = id;
            this.name = name;
            this.price = price;
        }

        public String toString() {
            return id + ". " + name + " - " + price + " rupees.";
        }
    }

    // Customer class
    static class Customer {
        int id;
        String name;

        Customer(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public String toString() {
            return id + ". " + name;
        }
    }

    // BST Node
    static class BSTNode {
        MenuItem data;
        BSTNode left, right;

        BSTNode(MenuItem data) {
            this.data = data;
        }
    }

    // Menu BST
    static class MenuBST {
        BSTNode root;

        void insert(MenuItem item) {
            root = insertRec(root, item);
        }

        private BSTNode insertRec(BSTNode root, MenuItem item) {
            if (root == null) return new BSTNode(item);
            if (item.id < root.data.id) root.left = insertRec(root.left, item);
            else if (item.id > root.data.id) root.right = insertRec(root.right, item);
            return root;
        }

        MenuItem search(int id) {
            return searchRec(root, id);
        }

        private MenuItem searchRec(BSTNode root, int id) {
            if (root == null) return null;
            if (id == root.data.id) return root.data;
            return id < root.data.id ? searchRec(root.left, id) : searchRec(root.right, id);
        }

        void inorder(BSTNode node) {
            if (node != null) {
                inorder(node.left);
                System.out.println(node.data);
                inorder(node.right);
            }
        }

        void displayMenu() {
            if (root == null) System.out.println("Menu is empty!");
            else inorder(root);
        }

        void sortMenu() {
            List<MenuItem> items = new ArrayList<>();
            inorderToList(root, items);
            items.sort(Comparator.comparingDouble(i -> i.price));
            items.forEach(System.out::println);
        }

        private void inorderToList(BSTNode node, List<MenuItem> list) {
            if (node != null) {
                inorderToList(node.left, list);
                list.add(node.data);
                inorderToList(node.right, list);
            }
        }

        void delete(int id) {
            root = deleteRec(root, id);
        }

        private BSTNode deleteRec(BSTNode root, int id) {
            if (root == null) return null;
            if (id < root.data.id) root.left = deleteRec(root.left, id);
            else if (id > root.data.id) root.right = deleteRec(root.right, id);
            else {
                if (root.left == null) return root.right;
                else if (root.right == null) return root.left;
                root.data = minValue(root.right);
                root.right = deleteRec(root.right, root.data.id);
            }
            return root;
        }

        private MenuItem minValue(BSTNode node) {
            MenuItem min = node.data;
            while (node.left != null) {
                min = node.left.data;
                node = node.left;
            }
            return min;
        }
    }

    // Order class
    static class Order {
        int orderId;
        int customerId;
        List<MenuItem> items;

        Order(int orderId, int customerId, List<MenuItem> items) {
            this.orderId = orderId;
            this.customerId = customerId;
            this.items = items;
        }

        public String toString() {
            return "Order#" + orderId + " for Customer ID: " + customerId + ", Items: " + items;
        }
    }

    static LinkedList<Customer> customers = new LinkedList<>();
    static MenuBST menu = new MenuBST();
    static Queue<Order> orderQueue = new LinkedList<>();
    static int orderCounter = 1;
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        loadMenuData();
        loadCustomerData();

        boolean running = true;
        while (running) {
            System.out.println("\n Restaurant Management System");
            System.out.println("1. Manage Menu");
            System.out.println("2. Manage Customers");
            System.out.println("3. Create Order");
            System.out.println("4. View/Manage Orders");
            System.out.println("5. Exit");
            System.out.print("Enter choice: ");
            int choice = getIntInput();

            switch (choice) {
                case 1 -> manageMenu();
                case 2 -> manageCustomers();
                case 3 -> createOrder();
                case 4 -> manageOrders();
                case 5 -> {
                    saveMenuData();
                    saveCustomerData();
                    System.out.println("Exiting...");
                    running = false;
                }
                default -> System.out.println("Invalid choice!");
            }
        }
    }

    static void manageMenu() {
        while (true) {
            System.out.println("\n Menu Management");
            System.out.println("1. Add Item");
            System.out.println("2. View Menu");
            System.out.println("3. Search Item by ID");
            System.out.println("4. Sort Menu by Price");
            System.out.println("5. Update Item");
            System.out.println("6. Delete Item");
            System.out.println("7. Back");
            System.out.print("Enter choice: ");
            int choice = getIntInput();

            switch (choice) {
                case 1 -> {
                    System.out.print("Enter item ID: ");
                    int id = getIntInput();
                    System.out.print("Enter name: ");
                    String name = sc.nextLine();
                    System.out.print("Enter price: ");
                    double price = getDoubleInput();
                    menu.insert(new MenuItem(id, name, price));
                    System.out.println("Item added.");
                }
                case 2 -> menu.displayMenu();
                case 3 -> {
                    System.out.print("Enter ID to search: ");
                    MenuItem item = menu.search(getIntInput());
                    System.out.println(item != null ? item : "Item not found.");
                }
                case 4 -> menu.sortMenu();
                case 5 -> {
                    System.out.print("Enter ID to update: ");
                    int id = getIntInput();
                    MenuItem existing = menu.search(id);
                    if (existing == null) {
                        System.out.println("Item not found.");
                    } else {
                        menu.delete(id);
                        System.out.print("Enter new name: ");
                        String name = sc.nextLine();
                        System.out.print("Enter new price: ");
                        double price = getDoubleInput();
                        menu.insert(new MenuItem(id, name, price));
                        System.out.println("Item updated.");
                    }
                }
                case 6 -> {
                    System.out.print("Enter ID to delete: ");
                    int id = getIntInput();
                    menu.delete(id);
                    System.out.println("Item deleted.");
                }
                case 7 -> {
                    saveMenuData();
                    return;
                }
                default -> System.out.println("Invalid option!");
            }
        }
    }

    static void manageCustomers() {
        while (true) {
            System.out.println("\n👥 Customer Management");
            System.out.println("1. Add Customer");
            System.out.println("2. View Customers");
            System.out.println("3. Delete Customer");
            System.out.println("4. Search Customer by Name");
            System.out.println("5. Update Customer");
            System.out.println("6. Back");
            System.out.print("Enter choice: ");
            int choice = getIntInput();

            switch (choice) {
                case 1 -> {
                    System.out.print("Enter ID: ");
                    int id = getIntInput();
                    System.out.print("Enter name: ");
                    String name = sc.nextLine();
                    customers.add(new Customer(id, name));
                    System.out.println("Customer added.");
                }
                case 2 -> customers.forEach(System.out::println);
                case 3 -> {
                    System.out.print("Enter ID to delete: ");
                    int id = getIntInput();
                    boolean removed = customers.removeIf(c -> c.id == id);
                    System.out.println(removed ? "Deleted." : "Not found.");
                }
                case 4 -> {
                    System.out.print("Enter name to search: ");
                    String name = sc.nextLine();
                    customers.stream().filter(c -> c.name.toLowerCase().contains(name.toLowerCase())).forEach(System.out::println);
                }
                case 5 -> {
                    System.out.print("Enter ID to update: ");
                    int id = getIntInput();
                    for (Customer c : customers) {
                        if (c.id == id) {
                            System.out.print("Enter new name: ");
                            c.name = sc.nextLine();
                            System.out.println("Customer updated.");
                            return;
                        }
                    }
                    System.out.println("Customer not found.");
                }
                case 6 -> {
                    saveCustomerData();
                    return;
                }
                default -> System.out.println("Invalid option!");
            }
        }
    }

    static void createOrder() {
        System.out.print("Enter customer ID: ");
        int custId = getIntInput();
        if (customers.stream().noneMatch(c -> c.id == custId)) {
            System.out.println("Customer not found!");
            return;
        }

        List<MenuItem> items = new ArrayList<>();
        while (true) {
            System.out.print("Enter item ID (0 to finish): ");
            int id = getIntInput();
            if (id == 0) break;
            MenuItem item = menu.search(id);
            if (item != null) items.add(item);
            else System.out.println("Invalid item ID.");
        }

        if (!items.isEmpty()) {
            orderQueue.add(new Order(orderCounter++, custId, items));
            System.out.println("Order placed.");
        } else {
            System.out.println("No items selected.");
        }
    }

    static void manageOrders() {
        while (true) {
            System.out.println("\n Order Management");
            System.out.println("1. View Orders");
            System.out.println("2. Delete Order by ID");
            System.out.println("3. Update Order by ID");
            System.out.println("4. Clear All Orders");
            System.out.println("5. Back");
            System.out.print("Enter choice: ");
            int choice = getIntInput();

            switch (choice) {
                case 1 -> orderQueue.forEach(System.out::println);
                case 2 -> {
                    System.out.print("Enter Order ID to delete: ");
                    int id = getIntInput();
                    boolean removed = orderQueue.removeIf(o -> o.orderId == id);
                    System.out.println(removed ? "Order deleted." : "Order not found.");
                }
                case 3 -> {
                    System.out.print("Enter Order ID to update: ");
                    int id = getIntInput();
                    for (Order o : orderQueue) {
                        if (o.orderId == id) {
                            List<MenuItem> newItems = new ArrayList<>();
                            while (true) {
                                System.out.print("Enter new item ID (0 to finish): ");
                                int itemId = getIntInput();
                                if (itemId == 0) break;
                                MenuItem item = menu.search(itemId);
                                if (item != null) newItems.add(item);
                                else System.out.println("Invalid item.");
                            }
                            if (!newItems.isEmpty()) {
                                o.items = newItems;
                                System.out.println("Order updated.");
                            } else {
                                System.out.println("No items selected.");
                            }
                            return;
                        }
                    }
                    System.out.println("Order not found.");
                }
                case 4 -> {
                    orderQueue.clear();
                    System.out.println("All orders cleared.");
                }
                case 5 -> {
                    return;
                }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    // Input Helpers
    static int getIntInput() {
        while (true) {
            try {
                return Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.print("Invalid number. Try again: ");
            }
        }
    }

    static double getDoubleInput() {
        while (true) {
            try {
                return Double.parseDouble(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.print("Invalid price. Try again: ₹");
            }
        }
    }

    // File I/O
    static void saveMenuData() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("menu.txt"))) {
            writeMenuToFile(writer, menu.root);
        } catch (IOException e) {
            System.out.println("Error saving menu.");
        }
    }

    static void writeMenuToFile(BufferedWriter writer, BSTNode node) throws IOException {
        if (node != null) {
            writer.write(node.data.id + "," + node.data.name + "," + node.data.price + "\n");
            writeMenuToFile(writer, node.left);
            writeMenuToFile(writer, node.right);
        }
    }

    static void loadMenuData() {
        try (BufferedReader reader = new BufferedReader(new FileReader("menu.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                try {
                    String[] data = line.split(",");
                    menu.insert(new MenuItem(Integer.parseInt(data[0]), data[1], Double.parseDouble(data[2])));
                } catch (Exception e) {
                    System.out.println("Skipping invalid menu entry.");
                }
            }
        } catch (IOException e) {
            System.out.println("No menu data found.");
        }
    }

    static void saveCustomerData() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("customers.txt"))) {
            for (Customer c : customers) writer.write(c.id + "," + c.name + "\n");
        } catch (IOException e) {
            System.out.println("Error saving customers.");
        }
    }

    static void loadCustomerData() {
        try (BufferedReader reader = new BufferedReader(new FileReader("customers.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                try {
                    String[] data = line.split(",");
                    customers.add(new Customer(Integer.parseInt(data[0]), data[1]));
                } catch (Exception e) {
                    System.out.println("Skipping invalid customer entry.");
                }
            }
        } catch (IOException e) {
            System.out.println("No customer data found.");
        }
    }
}



