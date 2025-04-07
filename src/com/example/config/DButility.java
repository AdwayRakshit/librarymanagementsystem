package com.example.config;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

public class DButility {
	static {
		try {
			Class.forName("oracle.jdbc.OracleDriver");
		} catch (ClassNotFoundException e) {
			System.out.println("Not Loaded..." + e);
		}
	}

	public static Connection makeCon() throws SQLException {
		return DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "projectwork", "admin");
	}
	private Connection connection;

	public void confif() {
		try {
			connection = DButility.makeCon();
			System.out.println("Connected to the database!");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Failed to connect to the database.");
		}

	}
	
	
	public void addBookToLibraryRecords(int bookId, String studentName, String bookName, String author, Date borrowDate, String status) throws SQLException {
        String sql = "INSERT INTO LibraryRecords (BookID, StudentName, BookName, Author, BorrowDate, Status) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement pstmt = null;

        try {
            pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, bookId);
            pstmt.setString(2, studentName);
            pstmt.setString(3, bookName);
            pstmt.setString(4, author);
            pstmt.setDate(5, borrowDate);
            pstmt.setString(6, status);

            pstmt.executeUpdate();

        } finally {
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (SQLException e) {
                    // Log the exception (e.g., using a logging framework)
                    System.err.println("Error closing PreparedStatement: " + e.getMessage());
                }
            }
        }
    }

    // Overloaded method to accept a String borrowDate and convert it to SQL Date
    public void addBookToLibraryRecords(int bookId, String studentName, String bookName, String author, String borrowDate, String status) throws SQLException, java.text.ParseException {
        Date sqlDate = convertStringToSQLDate(borrowDate);
        addBookToLibraryRecords(bookId, studentName, bookName, author, sqlDate, status);
    }

    private Date convertStringToSQLDate(String borrowDate) throws java.text.ParseException {
        java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("dd-MMM-yy"); // Corrected format
        java.util.Date parsedDate = dateFormat.parse(borrowDate);
        return new Date(parsedDate.getTime());
    }
    
    //loadbooks
    
    public List<Book> loadAllBooks() throws SQLException {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT BookID, StudentName, BookName, Author, BorrowDate, Status FROM LibraryRecords";
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = connection.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                Book book = new Book(
                    rs.getInt("BookID"),
                    rs.getString("StudentName"),
                    rs.getString("BookName"),
                    rs.getString("Author"),
                    rs.getDate("BorrowDate"), // Assuming BorrowDate is a DATE column
                    rs.getString("Status")
                );
                books.add(book);
            }

        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    System.err.println("Error closing ResultSet: " + e.getMessage());
                }
            }
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (SQLException e) {
                    System.err.println("Error closing PreparedStatement: " + e.getMessage());
                }
            }
        }
        return books;
    }

    // Book class (assuming you have this):
    public static class Book {
        private int bookId;
        private String studentName;
        private String bookName;
        private String author;
        private java.sql.Date borrowDate;
        private String status;

        public Book(int bookId, String studentName, String bookName, String author, java.sql.Date borrowDate, String status) {
            this.bookId = bookId;
            this.studentName = studentName;
            this.bookName = bookName;
            this.author = author;
            this.borrowDate = borrowDate;
            this.status = status;
        }

        // Getters and setters (if needed)
        

        @Override
        public String toString() {
            return "Book{" +
                    "bookId=" + bookId +
                    ", studentName='" + studentName + '\'' +
                    ", bookName='" + bookName + '\'' +
                    ", author='" + author + '\'' +
                    ", borrowDate=" + borrowDate +
                    ", status='" + status + '\'' +
                    '}';
        }

        public int getBookId() {
            return bookId;
        }

        public String getStudentName() {
            return studentName;
        }

        public String getBookName() {
            return bookName;
        }

        public String getAuthor() {
            return author;
        }

        public java.sql.Date getBorrowDate() {
            return borrowDate;
        }

        public String getStatus() {
            return status;
        }


		
    }
    //delete
    public boolean deleteBook(int bookId) throws SQLException {
        String sql = "DELETE FROM LibraryRecords WHERE BookID = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, bookId);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        }
    }
   
            
        
    }


	
    
		
		
	


	


