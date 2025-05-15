package FinalProject01.user;


import FinalProject01.item.LibraryItem;

import java.util.ArrayList;
import java.util.List;

public class Member extends User {
    private final MembershipType membershipType;
    private List<LibraryItem> borrowedItems = new ArrayList<>();


    public Member(String name, String email, MembershipType membershipType) {
        super(name, email);
        this.membershipType = membershipType;
    }

    public MembershipType getMembershipType() {
        return membershipType;
    }

    public List<LibraryItem> getBorrowedItems() {
        return new ArrayList<>(borrowedItems);
    }

    public void addBorrowedItem(LibraryItem borrowedItem) {
        if (borrowedItems == null) {
            throw new IllegalArgumentException("borrowedItem cannot be null");
        }
        borrowedItems.add(borrowedItem);

    }

    public void removeBorrowedItem(LibraryItem borrowedItem) {
        if(borrowedItem == null) {
            throw new IllegalArgumentException("borrowedItem cannot be null");
        }
        borrowedItems.remove(borrowedItem);

    }


    @Override
    public void displayUserInfo() {
        System.out.println("User Id: " + getUserId());
        System.out.println("Name: " + getName());
        System.out.println("Email: " + getEmail());
        System.out.println("Membership Type: " + membershipType);
        System.out.println("Borrowed items: " + borrowedItems);
    }
}
