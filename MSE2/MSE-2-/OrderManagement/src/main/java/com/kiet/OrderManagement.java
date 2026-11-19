package com.kiet;

import java.sql.*;
import java.util.Scanner;

public class OrderManagement {



    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        try {
            // Database Connection
            Connection con = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/ecommerce_db",
                    "postgres",
                    "shreeN@TH"
            );

            System.out.println("Database Connected Successfully!");

            int choice;

            do {
                System.out.println("\n===== ORDER MANAGEMENT SYSTEM =====");
                System.out.println("1. Insert Three Orders");
                System.out.println("2. Display Orders by Status");
                System.out.println("3. Increase Order Quantity");
                System.out.println("4. Update Order Status");
                System.out.println("5. Display Total Order Value");
                System.out.println("6. Delete Cancelled Order");
                System.out.println("0. Exit");

                System.out.print("Enter your choice: ");
                choice = sc.nextInt();

                switch (choice) {


                    case 1:

                        String insertQuery =
                                "INSERT INTO Orders VALUES (?, ?, ?, ?, ?,?)";

                        PreparedStatement ps1 =
                                con.prepareStatement(insertQuery);

                        for (int i = 1; i <= 3; i++) {

                            System.out.println("Enter Order " + i + " Details:");

                            System.out.print("Order ID: ");
                            int id = sc.nextInt();
                            sc.nextLine();

                            System.out.print("Customer Name: ");
                            String name = sc.nextLine();

                            System.out.print("Product Name: ");
                            String pname = sc.nextLine();



                            System.out.print("Quantity: ");
                            int qty = sc.nextInt();

                            System.out.print("Price: ");
                            double price = sc.nextDouble();
                            sc.nextLine();


                            System.out.print("Status: ");
                            String status = sc.nextLine();

                            ps1.setInt(1, id);
                            ps1.setString(2, name);
                            ps1.setString(3, pname);
                            ps1.setInt(4, qty);
                            ps1.setDouble(5, price);
                            ps1.setString(6, status);

                            try {
                                ps1.executeUpdate();
                                System.out.println("Order Inserted Successfully!");

                            } catch (SQLException e) {

                                if ("23505".equals(e.getSQLState())) {
                                    System.out.println("Duplicate Order ID! Please try again.");
                                } else {
                                    e.printStackTrace();
                                }
                            }

                        }
                        break;



                    case 2:

                        sc.nextLine();

                        System.out.print("Enter Status: ");
                        String sat = sc.nextLine();

                        String selectQuery =
                                "SELECT * FROM Orders " +
                                        "WHERE status = ? AND quantity > 2 " +
                                        "ORDER BY unit_price DESC";

                        PreparedStatement ps2 =
                                con.prepareStatement(selectQuery);

                        ps2.setString(1, sat);

                        ResultSet rs2 = ps2.executeQuery();

                        boolean found = false;

                        while (rs2.next()) {

                            found = true;
                            System.out.println(
                                    rs2.getInt("Order_id") + " " +
                                            rs2.getString("Customer_name") + " " +
                                            rs2.getString("Product_name") + " " +
                                            rs2.getInt("quantity") + " " +
                                            rs2.getDouble("unit_price") + " " +
                                            rs2.getString("Status")


                            );
                        }
                        if (!found) {
                            System.out.println("No matching orders found!");
                        }
                        rs2.close();
                        ps2.close();
                        break;



                    case 3:

                        System.out.print("Enter Order ID: ");
                        int oid = sc.nextInt();

                        System.out.print("Enter Quantity to Add: ");
                        int addQty = sc.nextInt();

                        String updateStock =
                                "UPDATE Orders SET quantity = quantity + ? " +
                                        "WHERE order_id = ?";

                        PreparedStatement ps3 =
                                con.prepareStatement(updateStock);

                        ps3.setInt(1, addQty);
                        ps3.setInt(2, oid);

                        int rows3 = ps3.executeUpdate();

                        if (rows3 > 0) {
                            System.out.println("Order Quantity Updated Successfully!");
                        } else {
                            System.out.println("Order Does Not Exist!");
                        }
                        break;





                    case 4:

                        System.out.print("Enter Order ID: ");
                        int searchId = sc.nextInt();
                        sc.nextLine();


                        System.out.print("Enter new status: ");
                        String newStatus = sc.nextLine();

                        if (!newStatus.equalsIgnoreCase("Pending") &&
                                !newStatus.equalsIgnoreCase("Shipped") &&
                                !newStatus.equalsIgnoreCase("Delivered")&&
                        !newStatus.equalsIgnoreCase("Cancelled") ) {

                            System.out.println("Invalid Status!");
                            break;
                        }

                        String checkQuery =
                                "SELECT * FROM Orders WHERE order_id = ?";

                        PreparedStatement ps4 = con.prepareStatement(checkQuery);

                        ps4.setInt(1, searchId);

                        ResultSet rs6 = ps4.executeQuery();

                        if (rs6.next()) {

                            System.out.println("\nOrder Found!");
                            System.out.println("Customer: " + rs6.getString("customer_name"));
                            System.out.println("Product: " + rs6.getString("product_name"));
                            System.out.println("Quantity: " + rs6.getInt("quantity"));


                            String updateStatus =
                                    "UPDATE Orders SET status = ? WHERE order_id = ?";

                            PreparedStatement ps5 = con.prepareStatement(updateStatus);

                            ps5.setString(1, newStatus);
                            ps5.setInt(2, searchId);

                            ps5.executeUpdate();

                            System.out.println("\nOrder Status Updated Successfully!");



                            String displayQuery =
                                    "SELECT * FROM Orders WHERE order_id = ?";

                            PreparedStatement ps6 = con.prepareStatement(displayQuery);

                            ps6.setInt(1, searchId);

                            ResultSet rs3 = ps6.executeQuery();

                            if (rs3.next()) {
                                System.out.println("\nUpdated Order Details:");
                                System.out.println("Order ID: " + rs3.getInt("order_id"));
                                System.out.println("Customer: " + rs3.getString("customer_name"));
                                System.out.println("Product: " + rs3.getString("product_name"));
                                System.out.println("Quantity: " + rs3.getInt("quantity"));
                                System.out.println("Price: " + rs3.getDouble("unit_price"));
                                System.out.println("Status: " + rs3.getString("status"));
                            }

                            rs3.close();
                            ps6.close();
                            ps5.close();

                        } else {
                            System.out.println("Order Not Found!");
                        }
                        break;



                    case 5:

                        String totalQuery =
                                "SELECT SUM(quantity * unit_price) AS total_value FROM Orders";

                        Statement st = con.createStatement();

                        ResultSet rs4 = st.executeQuery(totalQuery);

                        if (rs4.next()) {
                            System.out.println("Total Order Value: " +
                                    rs4.getDouble("total_value"));
                        }

                        rs4.close();
                        st.close();
                        break;
                    case 6:

                        System.out.print("Enter Order ID to delete: ");
                        int deleteId = sc.nextInt();

                        String deleteQuery =
                                "DELETE FROM Orders WHERE order_id = ? AND status = 'Cancelled'";

                        PreparedStatement ps7 = con.prepareStatement(deleteQuery);

                        ps7.setInt(1, deleteId);

                        int deleted = ps7.executeUpdate();

                        if (deleted > 0) {
                            System.out.println("Cancelled Order Deleted Successfully!");
                        } else {
                            System.out.println(
                                    "Order cannot be deleted! It does not exist or is not Cancelled."
                            );
                        }

                        ps7.close();
                        break;

                    case 0:
                        System.out.println("Exiting Application...");
                        break;


                    default:
                        System.out.println("Invalid Choice!");
                }

            } while (choice != 0);

            con.close();
            sc.close();

        } catch (SQLException e) {
            System.out.println(e);
        }
    }
}




