package pl.javastart.library.model;

import pl.javastart.library.exception.PublicationsAlreadyExistException;
import pl.javastart.library.exception.UserAlreadyExistException;

import java.io.Serializable;
import java.util.*;

public class Library implements Serializable {

    private Map<String, Publication> publications = new HashMap<>();
    private Map<String, LibraryUser> users = new HashMap<>();

    public Optional<Publication> findPublicationByTitle(String title){
        return Optional.ofNullable(publications.get(title));
    }


    public Map<String, Publication> getPublications() {
        return publications;
    }

    public Collection<Publication> getSortedPublications(Comparator<Publication> comparator){
        ArrayList<Publication> list = new ArrayList<>(this.publications.values());
        list.sort(comparator);
        return list;
    }

    public Map<String, LibraryUser> getUsers() {
        return users;
    }

    public void addPublication(Publication publication) {
        if(publications.containsKey(publication.getTitle())) {
            throw new PublicationsAlreadyExistException(
                    "Publikacja o takim tytule już istnieje " + publication.getTitle()
            );
        }
        publications.put(publication.getTitle(), publication);
    }

    public void addUser(LibraryUser user) {
        if (users.containsKey(user.getPesel())) {
            throw new UserAlreadyExistException(
                    "Użytkownik ze wskazanym peselem już istnieje " + user.getPesel()
            );

        }
        users.put(user.getPesel(), user);
    }

    public Collection<LibraryUser> getSortedUsers(Comparator<LibraryUser> comparator){
        ArrayList<LibraryUser> list = new ArrayList<>(this.users.values());
        list.sort(comparator);
        return list;
    }

    public boolean removePublication(Publication pub) {
        if(publications.containsValue(pub)){
            publications.remove(pub.getTitle());
            return true;
        }else{
            return false;
        }
    }





}
