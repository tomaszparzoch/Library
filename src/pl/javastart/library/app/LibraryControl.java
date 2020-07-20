package pl.javastart.library.app;

import pl.javastart.library.exception.*;
import pl.javastart.library.io.ConsolePrinter;
import pl.javastart.library.io.DataReader;
import pl.javastart.library.io.file.FileManager;
import pl.javastart.library.io.file.FileManagerBuilder;
import pl.javastart.library.model.*;


import java.util.Comparator;
import java.util.InputMismatchException;


class LibraryControl {

    private ConsolePrinter printer = new ConsolePrinter();
    private DataReader dataReader = new DataReader(printer);
    private FileManager fileManager;

    private Library library;

    LibraryControl(){
        fileManager = new FileManagerBuilder(printer, dataReader).build();
        try {
            library = fileManager.importData();
            printer.printLine("Zaimportowano dane z pliku");
        }catch (DataImportException | InvalidDataException e){
            printer.printLine(e.getMessage());
            printer.printLine("Zainicjowano nową bazę.");
            library = new Library();

        }
    }

    public void controlLoop(){
        Option option;

        do{
            printOptions();
            option = getOption();
            switch (option) {
                case ADD_BOOK:
                    addBook();
                    break;
                case ADD_MAGAZINE:
                    addMagazine();
                    break;
                case PRINT_BOOKS:
                    printBook();
                    break;
                case PRINT_MAGAZINES:
                    printMagazine();
                    break;
                case DELETE_BOOK:
                    deleteBook();
                    break;
                case DELETE_MAGAZINE:
                    deleteMagazine();
                    break;
                case ADD_USER:
                    addUser();
                    break;
                case PRINT_USERS:
                    printUsers();
                    break;
                case FIND_BOOK:
                    findBook();
                    break;
                case EXIT:
                    exit();
                    break;
                default:
                    printer.printLine("Błędna opcja, wprowadź ponownie.");

            }
        }while(option != Option.EXIT);
    }

    private void findBook() {
        printer.printLine("Podaj tytuł publikacji");
        String title = dataReader.getString();
        String notFoundMessage = "Brak publikacji o takim tytule";
        library.findPublicationByTitle(title)
                .map(Publication::toString)
                .ifPresentOrElse(System.out::println, () -> System.out.println(notFoundMessage));
    }

    private void printUsers() {
        printer.printUsers(library.getSortedUsers(Comparator.comparing(User::getLastName, String.CASE_INSENSITIVE_ORDER)));
        
    }

    private void addUser() {
        LibraryUser libraryUser = dataReader.createLibraryUser();
        try{
            library.addUser(libraryUser);
        }catch (UserAlreadyExistException e){
            printer.printLine(e.getMessage());
        }


    }


    private Option getOption() {
        boolean optionOK = false;
        Option option = null;
        while (!optionOK){
            try{
                option = Option.createFromInt(dataReader.getInt());
                optionOK = true;
            }catch (NoSuchOptionException e){
                printer.printLine(e.getMessage());
            }catch (InputMismatchException e){
                printer.printLine("Wprowadzono wartość, która nie jest liczbą, podaj ponownie:");
            }
        }
        return option;
    }

    private void printMagazine() {
        printer.printMagazines(library.getSortedPublications(Comparator.comparing(Publication::getTitle, String.CASE_INSENSITIVE_ORDER)));

    }

    private void addMagazine() {
        try {
            Magazine magazine = dataReader.readAndCreateMagazine();
            library.addPublication(magazine);

        } catch (InputMismatchException e) {
            printer.printLine("Nie udało się utworzyć magazynu, niepoprawne dane.");
        } catch (ArrayIndexOutOfBoundsException e) {
            printer.printLine("Osiągnieto limit pojemności, nie można dodać kolejnego magazynu.");
        }
    }

    private void deleteMagazine() {
        try{
            Magazine magazine = dataReader.readAndCreateMagazine();
            if(library.removePublication(magazine)){
                printer.printLine("Usunięto magazyn");
            }else{
                printer.printLine("Brak wskazanego magazynu");
            }
        }catch (InputMismatchException e){
            printer.printLine("Nie udało się usunąć magazynu, niepoprawne dane");
        }

    }

private void exit() {
        try{
            fileManager.exportData(library);
            printer.printLine("Eksport danych do pliku zakończony powodzeniem");
        }catch (DataExportException e) {
            printer.printLine(e.getMessage());

        }
        printer.printLine("Koniec programu");
        dataReader.close();
    }

    private void printBook() {
        printer.printBooks(library.getSortedPublications(Comparator.comparing(Publication::getTitle, String.CASE_INSENSITIVE_ORDER)));
    }

    private void addBook(){
        try{
            Book book = dataReader.readAndCreateBook();
            library.addPublication(book);
        }catch (InputMismatchException e){
            printer.printLine("Nie udało się utworzyć książki, niepoprawne dane.");
        }catch (ArrayIndexOutOfBoundsException e){
            printer.printLine("Osiągnieto limit pojemności, nie można dodać kolejnej książki.");
        }

    }

    private void deleteBook() {
        try{
            Magazine magazine = dataReader.readAndCreateMagazine();
            if(library.removePublication(magazine)){
                printer.printLine("Usunięto książkę");
            }else{
                printer.printLine("Brak wskazanej książki");
            }
        }catch (InputMismatchException e){
            printer.printLine("Nie udało się usunąć książki, niepoprawne dane");
        }
    }

    private void printOptions() {
        printer.printLine("Wybierz opcję:");
        for (Option value : Option.values()) {
            printer.printLine(value.toString());

        }
    }


    enum Option {
        EXIT(0, "wyjście z programu"),
        ADD_BOOK(1, "Dodanie książki"),
        ADD_MAGAZINE(2, "Dodanie magazynu"),
        PRINT_BOOKS(3, "Wyświetl dostępne książki"),
        PRINT_MAGAZINES(4, "Wyświetl dostępne magazyny"),
        DELETE_BOOK(5, "Usuń książkę"),
        DELETE_MAGAZINE(6, "Usuń magazyn"),
        ADD_USER(7, "Dodaj użytkownika"),
        PRINT_USERS(8, "Wyświetl użytkowników"),
        FIND_BOOK(9, "Wyszukaj książkę po tytule");


        private final int value;
        private final String description;

        Option(int value, String description) {
            this.value = value;
            this.description = description;
        }

        public int getValue() {
            return value;
        }

        public String getDescription() {
            return description;
        }

        @Override
        public String toString() {
            return value + " - " + description;
        }

        static Option createFromInt(int option) throws NoSuchOptionException {
            try{
                return Option.values()[option];
            }catch (ArrayIndexOutOfBoundsException e) {
                throw new NoSuchOptionException("Brak opcji o ID " + option);
            }

        }

    }

}
