

import java.util.List;
import java.util.Scanner;
import java.util.Arrays;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // טוען את הדירות מהקובץ במקום ליצור דוגמאות ידניות
        List<Apartment> allApartments = ApartmentCSVHandler.loadApartments();

        // יצירת משתמשים
        Seller seller = new Seller("Yohanan");
        Broker broker = new Broker("Noa");
        Buyer buyer = new Buyer("Eldar");

        // חיבור המתווך למוכר לצורך קבלת התראות
        seller.addObserver(broker);

        while (true) {
            System.out.println("Welcome to the Brokers System!");
            System.out.println("Please select your role:");
            System.out.println("1. Seller");
            System.out.println("2. Buyer");
            System.out.println("3. Broker");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // לנקות את ה-\n מהבאפר

            switch (choice) {
                case 1:
                    handleSellerMenu(scanner, seller, allApartments);
                    break;
                case 2:
                    handleBuyerMenu(scanner, buyer, allApartments);
                    break;
                case 3:
                    handleBrokerMenu(scanner, broker, allApartments);
                    break;
                case 4:
                    System.out.println("Exiting system. Goodbye!");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void handleSellerMenu(Scanner scanner, Seller seller, List<Apartment> allApartments) {
        while (true) {
            System.out.println("\nSeller Menu:");
            System.out.println("1. Delete an apartment");
            System.out.println("2. View all apartments");
            System.out.println("3. Search apartments by radius");
            System.out.println("4. Back to main menu");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    Apartment.deleteApartment(scanner, allApartments, seller);
                    break;

                case 2:
                    printAllApartments(allApartments);
                    break;

                case 3:
                    handleSearchMenu(scanner, allApartments);
                    break;

                case 4:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void handleBuyerMenu(Scanner scanner, Buyer buyer, List<Apartment> allApartments) {
        while (true) {
            System.out.println("\nBuyer Menu:");
            System.out.println("1. View all apartments");
            System.out.println("2. Search apartments by radius");
            System.out.println("3. Buy an apartment");
            System.out.println("4. Add services to an already purchased apartment");
            System.out.println("5. Back to main menu");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    printAllApartments(allApartments);
                    break;

                case 2:
                    handleSearchMenu(scanner, allApartments);
                    break;

                case 3:
                    handleBuyApartment(scanner, buyer, allApartments);
                    break;

                case 4:
                    handleAddServicesToExistingApartment(scanner, buyer, allApartments);
                    break;

                case 5:
                    return;

                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }


    private static void handleBrokerMenu(Scanner scanner, Broker broker, List<Apartment> allApartments) {
        while (true) {
            System.out.println(" Broker Menu:");
            System.out.println("1. Update an apartment");
            System.out.println("2. Add a new apartment");
            System.out.println("3. View all apartments");
            System.out.println("4. Search apartments by radius");
            System.out.println("5. Back to main menu");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    Apartment.updateApartment(scanner, broker, allApartments);
                    break;

                case 2:
                    Apartment.addNewApartment(scanner, allApartments);
                    break;

                case 3:
                    printAllApartments(allApartments);
                    break;

                case 4:
                    handleSearchMenu(scanner, allApartments);
                    break;

                case 5:
                    return;

                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
    private static void handleSearchMenu(Scanner scanner, List<Apartment> allApartments) {
        try {
            System.out.print("Enter the center address for search (e.g., 4-5): ");
            String centerAddressInput = scanner.nextLine();
            List<Integer> centerAddress = Arrays.stream(centerAddressInput.split("-"))
                    .map(Integer::parseInt)
                    .toList();

            System.out.print("Enter the search radius (number of blocks): ");
            int radius = scanner.nextInt();
            scanner.nextLine(); // Clean buffer

            System.out.println("\nSearch Options:");
            System.out.println("1. Get average price per sqm in radius");
            System.out.println("2. Get sold apartments in radius");
            System.out.println("3. Get available apartments in radius");
            System.out.println("4. Get apartments by price per sqm");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            SearchStrategy strategy = null;
            switch (choice) {
                case 1:
                    double avgPrice = AveragePricePerSqmStrategy.calculate(allApartments, centerAddress, radius);
                    System.out.println("Average price per sqm: $" + avgPrice);
                    return;
                case 2:
                    strategy = new SoldApartmentsStrategy();
                    break;
                case 3:
                    strategy = new AvailableApartmentsStrategy();
                    break;
                case 4:
                    System.out.print("Enter reference price per sqm: ");
                    double referencePrice = scanner.nextDouble();
                    scanner.nextLine();
                    System.out.print("Compare price: higher/lower/equal? ");
                    String comparison = scanner.nextLine().trim().toLowerCase();
                    strategy = new PricePerSqmStrategy(referencePrice, comparison);
                    break;
                default:
                    System.out.println("Invalid choice. Returning to main menu...");
                    return;
            }

            List<Apartment> result = strategy.search(allApartments, centerAddress, radius);
            result.forEach(System.out::println);

        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
        }
    }




    private static void handleBuyApartment(Scanner scanner, Buyer buyer, List<Apartment> allApartments) {
        try {
            System.out.println("Available apartments for purchase:");
            for (Apartment apt : allApartments) {
                printApartmentHierarchy(apt, 0, true);
            }

            System.out.println("\nEnter the full address of the apartment you want to buy (e.g., 4-5-1-2): ");
            String addressInput = scanner.nextLine();
            List<Integer> addressList = Arrays.stream(addressInput.split("-"))
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());

            Apartment aptToBuy = null;
            for (Apartment apt : allApartments) {
                aptToBuy = apt.getSubApartmentByAddress(addressList);
                if (aptToBuy != null && !aptToBuy.isSold()) break;
            }

            if (aptToBuy == null || aptToBuy.isSold()) {
                throw new InvalidInputException("Error: Apartment not found or already sold.");
            }

            // סימון הדירה כנמכרת
            aptToBuy.setSold(true);
            System.out.println("Apartment purchased successfully: " + aptToBuy);

            // חיבור לתפריט השירותים
            handleAdditionalServices(scanner, buyer, aptToBuy);

            ApartmentCSVHandler.saveApartments(allApartments);
        } catch (InvalidInputException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
        }
    }



    private static void handleAdditionalServices(Scanner scanner, Buyer buyer, Apartment apartment) {
        System.out.println("Do you want to add extra services? (yes/no)");
        String response = scanner.nextLine().trim().toLowerCase();

        if (!response.equals("yes")) return;

        while (true) {
            System.out.println("\nSelect additional services:");
            System.out.println("1. Legal contract review (+$1000)");
            System.out.println("2. Pre-move cleaning (+$500)");
            System.out.println("3. Moving services (+$1500)");
            System.out.println("4. Interior design services (+$3000)");
            System.out.println("5. Done adding services");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    apartment = new LegalContractReview(apartment);
                    break;
                case 2:
                    apartment = new PreMoveCleaning(apartment);
                    break;
                case 3:
                    apartment = new MovingService(apartment);
                    break;
                case 4:
                    apartment = new InteriorDesign(apartment);
                    break;
                case 5:
                    System.out.println("Final apartment price with services: $" + apartment.getPrice());
                    System.out.println("Services added: " + apartment.getServices());
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void handleAddServicesToExistingApartment(Scanner scanner, Buyer buyer, List<Apartment> allApartments) {
        System.out.println("Enter the full address of the purchased apartment you want to add services to (e.g., 4-5-1-2): ");
        for (Apartment apt : allApartments) {
            if (apt.isSold()) { // רק דירות שנרכשו
                System.out.println(apt);
            }
        }

        String addressInput = scanner.nextLine();
        List<Integer> addressList = Arrays.stream(addressInput.split("-"))
                .map(Integer::parseInt)
                .collect(Collectors.toList());

        Apartment aptToModify = null;
        for (Apartment apt : allApartments) {
            aptToModify = apt.getSubApartmentByAddress(addressList);
            if (aptToModify != null && aptToModify.isSold()) break;
        }

        if (aptToModify == null || !aptToModify.isSold()) {
            System.out.println("Error: Apartment not found or not yet purchased.");
            return;
        }

        // הוספת שירותים לדירה שנרכשה
        handleAdditionalServices(scanner, buyer, aptToModify);
    }



    private static void printAllApartments(List<Apartment> allApartments) {
        if (allApartments.isEmpty()) {
            System.out.println("No apartments available.");
            return;
        }

        System.out.println("List of all apartments:");
        for (Apartment apt : allApartments) {
            printApartmentHierarchy(apt, 0, false);
        }
    }

    private static void printApartmentHierarchy(Apartment apartment, int level, boolean showOnlyForSale) {
        if (!showOnlyForSale || !apartment.isSold()) {
            System.out.println(" ".repeat(level * 2) + "- " + apartment.getAddress()
                    + " | Size: " + apartment.getSize()
                    + "m² | Price: $" + apartment.getPrice()
                    + " | Sold: " + (apartment.isSold() ? "Yes" : "No"));
        }

        // קריאה רקורסיבית להצגת תתי-דירות
        for (Apartment subApartment : apartment.getSubUnits()) {
            printApartmentHierarchy(subApartment, level + 1, showOnlyForSale);
        }
    }










}
