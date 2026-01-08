import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.List;
import java.util.Scanner;
import java.util.Arrays;
import java.util.stream.Collectors;


// מחלקת Apartment המייצגת נכס במשרד התיווך
public class Apartment {
    private List<Integer> address; // כתובת גמישה התומכת בהיררכיה
    private double size; // גודל במ"ר
    private double price; // מחיר מבוקש
    private boolean isSold; // סטטוס - נמכר או לא
    private List<Apartment> subUnits; // דירות פנימיות
    private List<String> services; // שירותים נוספים

    // בנאי
    public Apartment(List<Integer> address, double size, double price) {
        this.address = new ArrayList<>(address);
        this.size = size;
        this.price = price;
        this.isSold = false;
        this.subUnits = new ArrayList<>();
        this.services = new ArrayList<>();

    }

    // חישוב מחיר למ"ר
    public double getPricePerSquareMeter() {
        return price / size;
    }

    public List<Integer> getMainAddress() {
        if (address.size() >= 2) {
            return address.subList(0, 2); // מחזיר את שני המספרים הראשונים
        } else {
            return new ArrayList<>(address); // במקרה של כתובת קצרה מדי, מחזיר את כל הכתובת
        }
    }

    // הוספת דירה פנימית
    public void addSubApartment(double size, double price) {
        List<Integer> newAddress = new ArrayList<>(this.address);
        newAddress.add(subUnits.size() + 1); // מוסיפים מספר לסוף הכתובת
        Apartment subApartment = new Apartment(newAddress, size, price);
        subUnits.add(subApartment);
    }

    // מחזירה דירה בהתאם לכתובת מלאה
    public Apartment getSubApartmentByAddress(List<Integer> fullAddress) {
        if (this.address.equals(fullAddress)) {
            return this;
        }
        for (Apartment sub : subUnits) {
            Apartment found = sub.getSubApartmentByAddress(fullAddress);
            if (found != null) {
                return found;
            }
        }
        return null; // אם לא נמצאה הדירה
    }
    public boolean hasSubApartment(List<Integer> fullAddress) {
        return getSubApartmentByAddress(fullAddress) != null;
    }

    public boolean removeSubApartment(List<Integer> fullAddress) {
        for (Iterator<Apartment> iterator = subUnits.iterator(); iterator.hasNext(); ) {
            Apartment sub = iterator.next();
            if (sub.getAddress().equals(fullAddress)) {
                iterator.remove();
                return true;
            }
            if (sub.removeSubApartment(fullAddress)) {
                return true;
            }
        }
        return false;
    }

    public static void printApartmentHierarchy(Apartment apartment, int level) {
        System.out.println(" ".repeat(level * 2) + "- " + apartment.getAddress());
        for (Apartment sub : apartment.getSubUnits()) {
            printApartmentHierarchy(sub, level + 1);
        }
    }

    // מוסיפה דירה חדשה בהתאם לכתובת מלאה
    public void addApartmentByAddress(List<Integer> fullAddress, double size, double price) {
        if (!fullAddress.subList(0, this.address.size()).equals(this.address)) {
            System.out.println("Error: Provided address does not match this apartment hierarchy.");
            return;
        }

        if (this.address.size() == fullAddress.size() - 1) {
            addSubApartment(size, price);
        } else {
            for (Apartment sub : subUnits) {
                if (fullAddress.subList(0, sub.getAddress().size()).equals(sub.getAddress())) {
                    sub.addApartmentByAddress(fullAddress, size, price);
                    return;
                }
            }
            System.out.println("Invalid address structure. Could not add apartment.");
        }
    }



    // Getters and Setters
    public List<Integer> getAddress() {
        return address;
    }

    public double getSize() {
        return size;
    }

    public double getPrice() {
        return price;
    }

    public boolean isSold() {
        return isSold;
    }

    public List<Apartment> getSubUnits() {
        return subUnits;
    }

    public void setSize(double size) {
        this.size = size;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setSold(boolean sold) {
        isSold = sold;
    }

    // הוספת שירות
    public void addService(String service) {
        services.add(service);
    }

    // הצגת שירותים מחוברים לדירה
    public List<String> getServices() {
        return services;
    }

    // נוסיף equals ו-hashCode כך ששוויון נקבע לפי כתובת
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Apartment apartment = (Apartment) o;
        return this.address.toString().equals(apartment.address.toString());
    }

    @Override
    public int hashCode() {
        return this.address.toString().hashCode();
    }

    @Override
    public String toString() {
        return "Apartment at " + address + " | Size: " + size + "m² | Price: $" + price + " | Price per sqm: $" + getPricePerSquareMeter() + " | Sold: " + (isSold ? "Yes" : "No");
    }

    public static void addNewApartment(Scanner scanner, List<Apartment> allApartments) {
        try {
            System.out.print("Enter full address for the new apartment (e.g., 4-5-1-2): ");
            String newAddressInput = scanner.nextLine();
            if (newAddressInput.isEmpty()) {
                throw new InvalidInputException("Error: Address cannot be empty.");
            }

            List<Integer> newAddressList = Arrays.stream(newAddressInput.split("-"))
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());

            System.out.print("Enter size for new apartment: ");
            if (!scanner.hasNextDouble()) {
                throw new InvalidInputException("Error: Size must be a valid number.");
            }
            double size = scanner.nextDouble();

            System.out.print("Enter price for new apartment: ");
            if (!scanner.hasNextDouble()) {
                throw new InvalidInputException("Error: Price must be a valid number.");
            }
            double price = scanner.nextDouble();
            scanner.nextLine(); // ניקוי הבאפר

            // בדיקה אם הדירה כבר קיימת
            boolean exists = allApartments.stream().anyMatch(apt -> apt.hasSubApartment(newAddressList));
            if (exists) {
                throw new InvalidInputException("Error: An apartment with this exact address already exists.");
            }

            // הוספת הדירה לפי ההיררכיה
            boolean added = false;
            for (Apartment apt : allApartments) {
                if (apt.getMainAddress().equals(newAddressList.subList(0, 2))) {
                    apt.addApartmentByAddress(newAddressList, size, price);
                    added = true;
                    break;
                }
            }

            // אם אין דירה ראשית תואמת, ניצור דירה חדשה
            if (!added) {
                Apartment newApartment = ApartmentFactory.createApartment(newAddressList, size, price);
                allApartments.add(newApartment);
            }

            System.out.println("Apartment added at: " + newAddressList);
            ApartmentCSVHandler.saveApartments(allApartments);
        } catch (InvalidInputException e) {
            System.out.println(e.getMessage());
            System.out.println("Returning to the main menu...");
        } catch (Exception e) {
            System.out.println("Unexpected error occurred: " + e.getMessage());
            System.out.println("Returning to the main menu...");
        }
    }

    public static void updateApartment(Scanner scanner, Broker broker, List<Apartment> allApartments) {
        // הצגת כל הדירות שניתן לעדכן
        System.out.println("Available apartments to update:");
        for (Apartment apt : allApartments) {
            Apartment.printApartmentHierarchy(apt, 0);
        }

        System.out.print("\nEnter full address of the apartment to update (X-Y-Z- ...): ");
        String addressInput = scanner.nextLine();
        List<Integer> addressList = Arrays.stream(addressInput.split("-"))
                .map(Integer::parseInt)
                .collect(Collectors.toList());

        // חיפוש הדירה לעדכון
        Apartment aptToUpdate = null;
        Apartment parentApartment = null;

        for (Apartment apt : allApartments) {
            if (apt.getAddress().equals(addressList)) {
                aptToUpdate = apt;
                break;
            }
            Apartment foundSubApartment = apt.getSubApartmentByAddress(addressList);
            if (foundSubApartment != null) {
                aptToUpdate = foundSubApartment;
                parentApartment = apt; // לשמירת ההיררכיה
                break;
            }
        }

        if (aptToUpdate == null) {
            System.out.println("Apartment not found.");
            return;
        }

        // קליטת נתונים חדשים מהמשתמש
        System.out.print("Enter new size: ");
        double newSize = scanner.nextDouble();
        System.out.print("Enter new price: ");
        double newPrice = scanner.nextDouble();
        System.out.print("Is it sold? (true/false): ");
        boolean newSoldStatus = scanner.nextBoolean();
        scanner.nextLine();

        // עדכון הדירה
        aptToUpdate.setSize(newSize);
        aptToUpdate.setPrice(newPrice);
        aptToUpdate.setSold(newSoldStatus);

        System.out.println("Apartment updated successfully: " + aptToUpdate);

        // שמירה בקובץ
        ApartmentCSVHandler.saveApartments(allApartments);
    }

    public static void deleteApartment(Scanner scanner, List<Apartment> allApartments, Seller seller) {
        try {
            System.out.println("Enter full address of the apartment to delete (e.g., 4-5-1-2): ");
            for (Apartment apt : allApartments) {
                Apartment.printApartmentHierarchy(apt, 0);
            }

            String addressInput = scanner.nextLine();
            if (addressInput.isEmpty()) {
                throw new InvalidInputException(" Error: Address cannot be empty.");
            }

            List<Integer> addressList = Arrays.stream(addressInput.split("-"))
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());

            Apartment aptToDelete = null;
            for (Apartment apt : allApartments) {
                aptToDelete = apt.getSubApartmentByAddress(addressList);
                if (aptToDelete != null) break;
            }

            if (aptToDelete == null) {
                throw new InvalidInputException(" Error: Apartment not found.");
            }

            seller.deleteApartment(aptToDelete, allApartments);
        } catch (InvalidInputException e) {
            System.out.println(e.getMessage());
            System.out.println("Returning to the main menu...");
        } catch (Exception e) {
            System.out.println("Unexpected error occurred: " + e.getMessage());
            System.out.println("Returning to the main menu...");
        }
    }



}
