package model;

import api.AdminResource;
import api.HotelResource;
import service.ReservationService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Scanner;
import java.util.logging.SimpleFormatter;
import java.util.regex.Pattern;

public class MainMenu {


    private static HotelResource hotelResource = HotelResource.getHotelResource();
    static final Scanner scanner = new Scanner(System.in);
    private static String defaultDateFormat = "MM/DD/YYYY";

    public static void mainMenu() {
        boolean keepRuning = true;
        try {
            while (keepRuning) {
                printMenu();
                int selection = Integer.parseInt(scanner.nextLine());
                if (selection == 1) {
                    findAndReserveRoom();
                } else if (selection == 2) {
                    seeMyReservation();
                } else if (selection == 3) {
                    createAccount();
                } else if (selection == 4) {
                    AdminMenu.adminMenu();
                } else if (selection == 5) {
                    keepRuning = false;
                } else {
                    System.out.println("Please choose a number between 1 - 5");
                }

            }
        } catch (Exception ex) {
            System.out.println("Invalid input");
        }
    }

    private static void findAndReserveRoom() {
        AdminMenu.seeAllRooms();
        Collection<IRoom> Rooms = ReservationService.getReservationService().getAllRooms();
        if (Rooms.isEmpty()) {
            printMenu();
        } else {
            System.out.println("Would you like to book? (Y/N)");
            String book = scanner.nextLine().toUpperCase();
            while (!(book.equals("Y")) && !(book.equals("N"))) {
                System.out.println("Please enter Y (Yes) or N (No)");
                book = scanner.nextLine().toUpperCase();
            }
            if (book.equals("Y")) {
                System.out.println("Enter Check-in Date: mm/dd/yyy example: 05/25/2001");
                Date checkIn = checkDate(scanner);

                System.out.println("Enter Check-out Date: mm/dd/yyy example: 05/25/2001");
                Date checkOut = checkDate(scanner);

                System.out.println("Enter email. (format = jacob@gmail.com)");
                String email = scanner.nextLine();
                if (hotelResource.getCustomer(email) == null) {
                    System.out.println("Customer not found. You may need to create an account.");
                } else {
                    System.out.println("What room number would you like to reserve");
                    final String roomNumber = scanner.nextLine();
                    boolean found = false;

                    for (IRoom room : Rooms) {
                        if (room.getRoomNumber().equals(roomNumber)) {
                            found = true;
                            hotelResource.bookARoom(email, room, checkIn, checkOut);
                            System.out.println("Room Successfully Reserved!");
                            break;
                        }
                    }
                    if (!found) {
                        System.out.println("Room not found. Please restart reservation");
                    }
                }
            } else if (book.equals("N")) {
                printMenu();
            }
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

    private static Date checkDate(Scanner scanner) {
        String input = scanner.nextLine();
        try {
            return new SimpleDateFormat(defaultDateFormat).parse(input);
        } catch (ParseException ex) {
            System.out.println("Invalid date.");
            return checkDate(scanner);
        }
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