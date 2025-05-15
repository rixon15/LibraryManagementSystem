package FinalProject01.library;

import FinalProject01.item.Author;
import FinalProject01.item.Book;
import FinalProject01.item.LibraryItem;
import FinalProject01.user.Member;
import FinalProject01.user.User;

import java.security.InvalidParameterException;
import java.time.LocalDate;
import java.util.*;

public class Library implements Searchable {
    private final Map<String, LibraryItem> items;
    private final Map<String, User> members;
    private final Map<String, User> staffMembers;
    private final Map<String, Author> authors;
    private final BorrowingHistory borrowingHistory = new BorrowingHistory();


    public Library() {
       items = new HashMap<>();
       members = new HashMap<>();
       staffMembers = new HashMap<>();
       authors = new HashMap<>();

    }

    public List<LibraryItem> getAllItems() {
        return new ArrayList<>(items.values());
    }

    public void borrowItem(Member user, LibraryItem item, int borrowDuration) {
        if(!item.isAvailable()) {
            throw new ItemUnavailableException(item.getItemId(), "Item is currently not available.", "Cannot borrow item.");
        }

        if(user == null || item == null) {
            throw new IllegalArgumentException("User and Item cannot be null");
        }

        item.markAsBorrowed();
        borrowingHistory.addBorrowingRecord(item, user.getUserId(), borrowDuration);
        user.addBorrowedItem(item);
        System.out.println("The user " + user.getUserId() + " has borrowed the item " + item.getTitle());
    }

    public void returnItem(Member user, LibraryItem item) {
        if(item.isAvailable()) {
            throw new ItemUnavailableException(item.getItemId(), "Item was already marked as available.", "Cannot return item.");
        }
        if (user == null || item == null) {
            throw new IllegalArgumentException("User and Item cannot be null.");
        }

        borrowingHistory.markItemAsReturned(item.getItemId(), user.getUserId());

        user.removeBorrowedItem(item);
        item.markAsAvailable();
        System.out.println("The user " + user.getName() + " (ID: " + user.getUserId() + ") has returned the item '" + item.getTitle() + "' (ID: " + item.getItemId() + ")");
    }

    public void addUser(User user) {
        if(members.containsKey(user.getUserId())) {
            //throw new UserAlreadyExists exception
        }
        members.put(user.getUserId(), user);
    }

    public void removeUser(String userId) {
        if(!members.containsKey(userId)) {
            //throw new UserDoesntExists exception
        }
        members.remove(userId);
    }

    public User findUserById(String userId) {
        User user = null;
        if(members.containsKey(userId)) {
            user = members.get(userId);
        } else {
            user = staffMembers.get(userId);
        }

        if(user == null) {
            //trow new userDoesntExists exception
        }

        return user;
    }

    public List<User> getMembers() {
        return new ArrayList<>(members.values());
    }

    public List<User> getStaffMembers() {
        return new ArrayList<>(staffMembers.values());
    }

    public void addItem(LibraryItem item) {
        if(items.containsKey(item.getItemId())) {
            //throw new itemAlreadyExists exception
        }
        items.put(item.getItemId(), item);
    }

    public void removeItem(String itemId) {
        if(!items.containsKey(itemId)) {
            //throw new itemDoesntExist exception
        }
        items.remove(itemId);
    }

    public void updateItem(LibraryItem item) {
        if (!items.containsKey(item.getItemId())) {
            //throw new itemDoesntExist exception
        }
        items.put(item.getItemId(), item);
    }

    public LibraryItem findItemById(String itemId) {
        if(!items.containsKey(itemId)) {
            //throw new itemDoesntExist exception
        }
        return items.get(itemId);
    }

    public void addAuthor(Author author) {
        if(authors.containsKey(author.getAuthorId())) {
            //throw author already exists
        }
        authors.put(author.getAuthorId(), author);
    }

    public void removeAuthor(String authorId) {
        if(authors.containsKey(authorId)) {
            //throw author doesnt exist exception
        }
        authors.remove(authorId);
    }

    public List<Author> getAuthors() {
        return new ArrayList<>(authors.values());
    }


    @Override
    public List<LibraryItem> searchByTitle(String title) {
        List<LibraryItem> result = new ArrayList<>();

       for(LibraryItem item : items.values()) {
            if(item.getTitle().equals(title)) {
                result.add(item);
            }
       }

       if(result.isEmpty()) {
           //throw searchFailedException => no item found
       }
       return result;
    }

    @Override
    public List<LibraryItem> searchByAuthor(String authorName) {
       List<LibraryItem> result = new ArrayList<>();

       for(LibraryItem item : items.values()) {
           if(item.getClass().equals(Book.class)) {
              if(((Book) item).getAuthor().equals(authorName)) {
                  result.add(item);
              }
           }
       }

       if(result.isEmpty()) {
           //throw searchFailedException => no item found
       }

       return result;
    }

    @Override
    public List<LibraryItem> searchByGenre(String genreName) {
        List<LibraryItem> result = new ArrayList<>();

        for(LibraryItem item : items.values()) {
            if(item.getClass().equals(Book.class)) {
                if(((Book) item).getGenres().contains(genreName)) {
                    result.add(item);
                }
            }
        }

        if(result.isEmpty()) {
            //throw searchFailedException => nno item found
        }

        return result;
    }

    public static class BorrowingHistory {
        private final List<BorrowingRecord> borrowingRecords = new ArrayList<>();

        public void addBorrowingRecord(LibraryItem item, String memberId, int borrowDuration) {
                try {
                    borrowingRecords.add(new BorrowingRecord(memberId, item.getItemId(), LocalDate.now(), LocalDate.now().plusDays(borrowDuration)));
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }


        }

        public List<BorrowingRecord> getBorrowingHistory() {
            return new ArrayList<>(borrowingRecords);
        }

        public void markItemAsReturned(String itemId, String memberId) {
            Optional<BorrowingRecord> recordOptional = borrowingRecords.stream().filter(
                    borrowingRecord -> borrowingRecord.getItemId().equals(itemId)
                            && borrowingRecord.getMemberId().equals(memberId)
                            && borrowingRecord.getReturnDate() == null).findFirst();

            if(recordOptional.isPresent()) {
                recordOptional.get().setReturnDate(LocalDate.now());
            } else {
                //Throw BorrowingRecordNotFound exception
            }
        }

        public List<BorrowingRecord> searchByDate(LocalDate date) {
            if(date == null) {
                throw new InvalidParameterException("Invalid date");
            }
            List<BorrowingRecord> results = borrowingRecords.stream().filter(borrowingRecord -> borrowingRecord.getBorrowDate().equals(date)).toList();

            if(results.isEmpty()) {
                return List.of();
            } else {
                return results;
            }
        }

        public List<BorrowingRecord> searchByDueDate(LocalDate date) {
           if(date == null) {
               throw new InvalidParameterException("Invalid date");
           }
           List<BorrowingRecord> results = borrowingRecords.stream().filter(borrowingRecord -> borrowingRecord.getDueDate().equals(date)).toList();

           if(results.isEmpty()) {
               return List.of();
           } else {
               return results;
           }
        }

        public List<BorrowingRecord> searchByReturnDate(LocalDate date) {
            if(date == null) {
                throw new InvalidParameterException("Invalid date");
            }
            List <BorrowingRecord> results = borrowingRecords.stream().filter(borrowingRecord -> borrowingRecord.getReturnDate().equals(date)).toList();

            if(results.isEmpty()) {
                return List.of();
            } else {
                return results;
            }
        }

        public List<BorrowingRecord> searchByRecordId(String recordId) {
            if(recordId == null) {
                throw new InvalidParameterException("Invalid recordId");
            }
            List<BorrowingRecord> results = borrowingRecords.stream().filter(borrowingRecord -> borrowingRecord.getRecordId().equals(recordId)).toList();

            if(results.isEmpty()) {
                return List.of();
            } else {
                return results;
            }
        }

        public List<BorrowingRecord> searchByMemberId(String memberId) {
            if(memberId == null) {
                throw new InvalidParameterException("Invalid memberId");
            }
            List<BorrowingRecord> results = borrowingRecords.stream().filter(borrowingRecord -> borrowingRecord.getMemberId().equals(memberId)).toList();

            if(results.isEmpty()) {
                return List.of();
            } else {
                return results;
            }
        }

        public List<BorrowingRecord> searchByItemId(String itemId) {
            if(itemId == null) {
                throw new InvalidParameterException("Invalid itemId");
            }
            List<BorrowingRecord> results = borrowingRecords.stream().filter(borrowingRecord -> borrowingRecord.getItemId().equals(itemId)).toList();

            if(results.isEmpty()) {
                return List.of();
            } else {
                return results;
            }
        }

    }

}
