package uiComponents;

import resources.AdminResource;
import model.Customer;
import model.room.IRoom;
import model.room.Room;
import model.room.RoomType;

import java.util.Collections;
import java.util.Collection;
import java.util.Scanner;

public class AdminMenu {
    private static AdminResource adminResource = AdminResource.getAdminResource();
    static final Scanner scanner = new Scanner(System.in);

    public static void adminMenu(){
        try{
            printMenu();
                int selection = Integer.parseInt(scanner.nextLine());
                if (selection == 1){
                    seeAllCustomers();
                } else if (selection == 2){
                    seeAllRooms();
                } else if (selection == 3){
                    seeAllReservations();
                } else if (selection == 4){
                    addARoom();
                } else if (selection == 5) {
                    MainMenu.printMenu();
                } else {
                    System.out.println("Please choose a number between 1 - 5");
                }
        } catch (Exception ex) {
            System.out.println("Invalid input");
        }
    }

    public static void seeAllCustomers() {
        Collection<Customer> customers = adminResource.getAllCustomers();

        if (customers.isEmpty()){
            System.out.println("No customers found");
        } else {
            adminResource.getAllCustomers().forEach(System.out::println);
        }
    }

    public static void seeAllRooms() {
        Collection<IRoom> rooms = adminResource.getAllRooms();
        if (rooms.isEmpty()){
            System.out.println("No rooms found");
        } else {
            for (IRoom room : rooms) {
                System.out.println(room);
            }
        }
    }

    public static void seeAllReservations() {
        adminResource.displayAllReservations();
    }

    private static void addARoom(){
        final Scanner scanner = new Scanner(System.in);
        System.out.println("Enter Room Number: ");
        String roomNumber = scanner.nextLine();

        System.out.println("Enter Room Price: ");
        final double roomPrice = checkRoomPrice(scanner);

        System.out.println("Enter Room type: 1 for single, 2 for double");
        final RoomType roomType = checkRoomType(scanner);

        final Room room = new Room(roomNumber, roomPrice, roomType);
        adminResource.addRoom(Collections.singletonList(room));
        System.out.println("Room added successfully!");

        System.out.println("Would you like to add another room?");
        addAnotherRoom();
    }

    private static double checkRoomPrice(Scanner scanner) {
        try {
            return Double.parseDouble(scanner.nextLine());
        } catch (NumberFormatException ex) {
            System.out.println("Invalid room price! Please enter a valid double number ex. (100.10)");
            return checkRoomPrice(scanner);
        }
    }
    private static RoomType checkRoomType(Scanner scanner) {
        String input;
        do {
            input = scanner.nextLine();
            if (!input.equals("1") && !input.equals("2")) {
                System.out.println("Invalid room type! Please enter a valid room type 1 or 2");
            }
        } while (!input.equals("1") && !input.equals("2"));
        return RoomType.getRoomSize(input);
    }

    private static void addAnotherRoom(){
        final Scanner scanner = new Scanner(System.in);
        try {
            String anotherRoom = scanner.nextLine().toUpperCase();
            while (!(anotherRoom.equals("Y")) && !(anotherRoom.equals("N"))){
                System.out.println("Please enter Y (Yes) or N (No)");
                anotherRoom = scanner.nextLine().toUpperCase();
            }
            if (anotherRoom.equals("Y")){
                addARoom();
            } else {
                printMenu();
            }

        } catch (StringIndexOutOfBoundsException ste)  {
            addAnotherRoom();
        }
    }

    private static void printMenu(){
        System.out.println("1. See all customers\n" +
                "2. See all rooms\n" +
                "3. See all reservations\n" +
                "4. Add a room\n" +
                "5. Back to main menu\n" +
                "-------------------------------------------------\n" +
                "Please select a number from the menu options:\n");
    }
}
