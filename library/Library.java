package FinalProject01.library;

import FinalProject01.exceptions.*;
import FinalProject01.item.Author;
import FinalProject01.item.Book;
import FinalProject01.item.LibraryItem;
import FinalProject01.user.Member;
import FinalProject01.user.Staff;
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
            throw new ItemException(item.getItemId(), "Item is currently not available.", "Cannot borrow item.");
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
            throw new ItemException(item.getItemId(), "Item was already marked as available.", "Cannot return item.");
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

        if(user.getClass().equals(Member.class)) {
            if(members.containsKey(user.getUserId())) {
                throw new UserException(user.getUserId(), "Cannot add duplicated member to the library", "Member already exists in the library!");
            }
            members.put(user.getUserId(), user);
        } else if(user.getClass().equals(Staff.class)){
            if(staffMembers.containsKey(user.getUserId())) {
                throw new UserException(user.getUserId(), "Cannot add duplicated staff to the library", "Staff already exists in the library!");
            }
            staffMembers.put(user.getUserId(), user);
        } else {
            throw new UserException(user.getUserId(), "Unknown user type", "");
        }


    }

    public void removeUser(String userId) {
        if(!members.containsKey(userId)) {
            throw new UserException(userId, "Cannot remove user that doesn't exist in the library", "User doesn't exit in the library!");
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
           throw new UserException(userId, "User doesn't exist in the library", "Couldn't find the user in the library");
        }

        return user;
    }

    public List<User> getMembers() {
        return new ArrayList<>(members.values());
    }

    public List<User> getStaffMembers() {
        return new ArrayList<>(staffMembers.values());
    }

    public void removeStaffMember(String staffMemberId) {
        if(!staffMembers.containsKey(staffMemberId)) {
            throw new UserException(staffMemberId, "Staff member doesn't exist in the library", "Couldn't find the saff member in the library");
        }

        staffMembers.remove(staffMemberId);
    }

    public void addItem(LibraryItem item) {
        if(items.containsKey(item.getItemId())) {
            throw new ItemException(item.getItemId(), "Cannot add duplicated Items to the library", "The item already exists in the library");
        }
        items.put(item.getItemId(), item);
    }

    public void removeItem(String itemId) {
        if(!items.containsKey(itemId)) {
           throw new ItemException(itemId, "Item doesn't exist in the library", "The item cannot be found in the library");
        }
        items.remove(itemId);
    }

    public void updateItem(LibraryItem item) {
        if (!items.containsKey(item.getItemId())) {
           throw new ItemException(item.getItemId(), "Cannot find the item to update", "Cannot update an item that's not in the library");
        }
        items.put(item.getItemId(), item);
    }

    public LibraryItem findItemById(String itemId) {
        LibraryItem item = items.get(itemId);
        if(item == null) {
           throw new ItemException(itemId, "Item is not in the library", "Couldn't find the item in the library");
        }

        return item;
    }

    public void addAuthor(Author author) {
        if(authors.containsKey(author.getAuthorId())) {
            throw new AuthorException(author.getAuthorId(), "Cannot add duplicated Author", "Author already exists in the library");
        }
        authors.put(author.getAuthorId(), author);
    }

    public void removeAuthor(String authorId) {
        if(authors.containsKey(authorId)) {
           throw new AuthorException(authorId, "Author doesn't exist in the library", "Author is not part of the library");
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
          throw new SearchException("No item found with the provided title", "The library contains no item with the title");
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
           throw new SearchException("No item found with the provided author", "The library contains no item by the author");
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
            throw new SearchException("No item found with the provided title", "The library contains no item with the title");
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
               throw new BorrowingRecordException("Borrowing record doesn't exist", "Couldn't find the borrowing record in the library");
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
