import java.util.ArrayList;
import java.util.List;

public class Seller extends User {
    private List<ApartmentObserver> observers; // רשימת מתווכים (משקיפים)

    public Seller(String name) {
        super(name);
        this.observers = new ArrayList<>();
    }

    // הוספת מתווך לרשימת המשקיפים
    public void addObserver(ApartmentObserver observer) {
        observers.add(observer);
    }

    // הסרת מתווך מהרשימה
    public void removeObserver(ApartmentObserver observer) {
        observers.remove(observer);
    }

    // עדכון כל המתווכים שהנכס נמחק
    private void notifyObservers(Apartment deletedApartment) {
        for (ApartmentObserver observer : observers) {
            observer.onApartmentDeleted(deletedApartment);
        }
    }

    // מוכר יכול למחוק נכס מהמערכת
    public void deleteApartment(Apartment aptToDelete, List<Apartment> allApartments) {
        if (aptToDelete == null || allApartments == null) {
            System.out.println("Cannot delete a null apartment or from a null list.");
            return;
        }

        // מסירים את הנכס מהרשימה
        boolean removed = allApartments.remove(aptToDelete);
        if (removed) {
            // שמירה מחדש של הקובץ
            ApartmentCSVHandler.saveApartments(allApartments);
            System.out.println(getName() + " deleted the apartment: " + aptToDelete);

            // עדכון כל המתווכים הרשומים
            notifyObservers(aptToDelete);
        } else {
            System.out.println("Apartment not found in the list. No deletion occurred.");
        }
    }
}
