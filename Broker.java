import java.util.List;

public class Broker extends User implements ApartmentObserver {

    public Broker(String name) {
        super(name);
    }

    // פעולה המתבצעת כשנכס נמחק
    @Override
    public void onApartmentDeleted(Apartment deletedApartment) {
        System.out.println("Broker " + getName() + " has been notified: Apartment " + deletedApartment.getAddress() + " was deleted.");
    }

    // מתווך יכול לערוך נכס (גודל, מחיר, סטטוס)
    public void updateApartment(Apartment apartment, double newSize, double newPrice, boolean newSoldStatus, List<Apartment> allApartments) {
        if (!allApartments.contains(apartment)) {
            System.out.println("Apartment not found in the list. Cannot update.");
            return;
        }
        // מבצעים עדכון נתונים
        apartment.setSize(newSize);
        apartment.setPrice(newPrice);
        apartment.setSold(newSoldStatus);

        // שומרים את השינויים לקובץ
        ApartmentCSVHandler.saveApartments(allApartments);
        System.out.println(getName() + " updated " + apartment);
    }

    @Override
    public void viewApartment(Apartment apartment) {

        super.viewApartment(apartment);

    }

}
