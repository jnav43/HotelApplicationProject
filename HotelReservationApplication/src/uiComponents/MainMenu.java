package uiComponents;

import resources.HotelResource;
import model.room.IRoom;
import model.Reservation;
import service.ReservationService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Scanner;
import java.util.regex.Pattern;

public class MainMenu {


    private static final HotelResource hotelResource = HotelResource.getHotelResource();
    static final Scanner scanner = new Scanner(System.in);
    private static final String defaultDateFormat = "MM/dd/yyyy";

    public static void mainMenu() {
        boolean keepRuning = true;
        try {
            while (keepRuning) {
                printMenu();
                String selection = scanner.nextLine();
                if (selection.equals("1")) {
                    findAndReserveRoom();
                } else if (selection.equals("2")) {
                    seeMyReservation();
                } else if (selection.equals("3")) {
                    createAccount();
                } else if (selection.equals("4")) {
                    AdminMenu.adminMenu();
                } else if (selection.equals("5")) {
                    keepRuning = false;
                } else {
                    System.out.println("Please choose a number between 1 - 5");
                }

            }
        } catch (Exception ex) {
            System.out.println("Invalid input");
        }
    }

    private static void findAndReserveRoom() throws ParseException {
        Collection<IRoom> Rooms = ReservationService.getReservationService().getAllRooms();
        Scanner scanner = new Scanner(System.in);
        if (Rooms.isEmpty()) {
            System.out.println("No Available rooms to book");
            printMenu();
        } else {
            System.out.println("What is you check in Date (05/25/2001)");
            Date checkIn = checkDate(scanner);

            System.out.println("What is you check out Date (05/25/2001)");
            Date checkOut = checkDate(scanner);

            if (checkIn != null && checkOut != null) {
                Collection<IRoom> availableRooms = hotelResource.findARoom(checkIn, checkOut);

                if (availableRooms.isEmpty()) {
                    System.out.println("No rooms found. ");
                    Collection<IRoom> alternativeRooms = hotelResource.findAlternativeRooms(checkIn, checkOut);
                    if (alternativeRooms.isEmpty()) {
                        System.out.println("No alternative rooms found");
                    } else {
                        Date altCheckIn = hotelResource.alternativeDate(checkIn);
                        Date altCheckOut = hotelResource.alternativeDate(checkOut);
                        System.out.println("We've found rooms on alternative dates: "
                        + "\nCheck-In: " + altCheckIn.toString() + " Check-Out: " + altCheckOut.toString());
                        printRooms(alternativeRooms);
                        reserveRoom(scanner, altCheckIn, altCheckOut, alternativeRooms);
                    }
                } else {
                    System.out.println("Available rooms:");
                    printRooms(availableRooms);
                    reserveRoom(scanner, checkIn, checkOut, availableRooms);
                }
            }
        }
    }

    private static void reserveRoom(final Scanner scanner, final Date checkInDate, final Date checkOutDate,
                                    final Collection<IRoom> rooms) {
        System.out.println("Would you like to book? y/n");
        final String book = scanner.nextLine().toUpperCase();

        if ("Y".equals(book)) {
            System.out.println("Do you have an account with us? y/n");
            final String haveAccount = scanner.nextLine().toUpperCase();

            if ("Y".equals(haveAccount)) {
                System.out.println("Enter Email format: jacob@gmail.com.com");
                final String customerEmail = scanner.nextLine();

                if (hotelResource.getCustomer(customerEmail) == null) {
                    System.out.println("Customer not found.\nYou may need to create a new account.");
                } else {
                    System.out.println("What room number would you like to reserve?");
                    final String roomNumber = scanner.nextLine();

                    if (rooms.stream().anyMatch(room -> room.getRoomNumber().equals(roomNumber))) {
                        final IRoom room = hotelResource.getRoom(roomNumber);

                        final Reservation reservation = hotelResource
                                .bookARoom(customerEmail, room, checkInDate, checkOutDate);
                        System.out.println("Reservation created successfully!");
                        System.out.println(reservation);
                    } else {
                        System.out.println("Error: room number not available.\nStart reservation again.");
                    }
                }

                printMenu();
            } else {
                System.out.println("Please, create an account.");
                printMenu();
            }
        } else if ("N".equals(book)){
            printMenu();
        } else {
            reserveRoom(scanner, checkInDate, checkOutDate, rooms);
        }
    }


    private static void printRooms(Collection<IRoom> Rooms){
        if (Rooms.isEmpty()){
            System.out.println("Room not found");
        } else {
            for (IRoom room : Rooms){
                System.out.println(room);
            }
        }
    }

    private static Date checkDate(Scanner scanner) throws ParseException {
        try {
            return new SimpleDateFormat("MM/dd/yyyy").parse(scanner.nextLine());
        } catch (ParseException e) {
            System.out.println("Invalid date");
            findAndReserveRoom();
        }
        return null;
    }

    private static void seeMyReservation() {
        Scanner scanner = new Scanner(System.in);
        final String emailRegex = "^(.+)@(.+).com$";
        final Pattern pattern = Pattern.compile(emailRegex);
        String email;
        do {
            System.out.println("Please enter your email. Format: jacob@gmail.com");
            email = scanner.nextLine();
        } while (!pattern.matcher(email).matches());

        if (hotelResource.getCustomersReservations(email) == null){
            System.out.println("No reservations found");
        } else {
            printReservations(hotelResource.getCustomersReservations(email));
        }
        mainMenu();
    }

    private static void printReservations(Collection<Reservation> reservations) {
        if (reservations.isEmpty() || reservations == null) {
            System.out.println("Reservation not found");
        } else {
            for (Reservation reservation : reservations) {
                System.out.println(reservation);
            }
        }
    }

    private static void createAccount() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Please enter email. Format: jacob@gmail.com");
        String customerEmail = scanner.nextLine();

        System.out.println("Please enter first name.");
        String firstName = scanner.nextLine();

        System.out.println("Please enter last name.");
        String lastName = scanner.nextLine();

        try {
            hotelResource.createCustomer(customerEmail, firstName, lastName);
            System.out.println("Customer successfully created");
            printMenu();
        } catch (IllegalArgumentException ex) {
            System.out.println(ex.getLocalizedMessage());
            createAccount();
        }
    }

    public static void printMenu() {
        System.out.println("1. Find and reserve room\n" +
                "2. See my reservations\n" +
                "3. Create an account\n" +
                "4. Admin\n" +
                "5. Exit\n" +
                "-------------------------------------------------\n" +
                "Please select a number from the menu options:\n");
    }
}