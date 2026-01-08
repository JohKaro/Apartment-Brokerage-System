import java.util.List;

public class Buyer extends User {

    public Buyer(String name) {
        super(name);
    }

    // קונה יכול רק לצפות בנכסים
    public void viewApartment(Apartment apartment) {

        super.viewApartment(apartment);

    }
    // הוספת שירות לנכס שנרכש
    public void addServiceToApartment(Apartment apartment, String service) {
        apartment.addService(service);
        System.out.println(service + " has been added to the apartment at " + apartment.getAddress());
    }


}
