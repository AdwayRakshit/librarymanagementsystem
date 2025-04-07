package com.example.config;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;

public class Main {

    private JFrame frame;
    DButility db;
    private JTextField bookid;
    private JTextField studentname;
    private JTextField bookname;
    private JTextField authorname;
    private JTextField borrowdate;
    private JTextField status1;
    private JTable bookTable;
    private DefaultTableModel tableModel;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Main window = new Main();
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public Main() {
        db = new DButility();
        db.confif();
        initialize();
    }

    private void initialize() {
        frame = new JFrame();
        frame.setBounds(100, 100, 941, 546);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        JLabel lblNewLabel = new JLabel("Library Management  System");
        lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 21));
        lblNewLabel.setBounds(315, 24, 281, 34);
        frame.getContentPane().add(lblNewLabel);

        JLabel lblNewLabel_1 = new JLabel("Book ID");
        lblNewLabel_1.setBounds(123, 109, 46, 14);
        frame.getContentPane().add(lblNewLabel_1);

        bookid = new JTextField();
        bookid.setBounds(340, 109, 450, 20);
        frame.getContentPane().add(bookid);
        bookid.setColumns(10);

        JLabel lblNewLabel_2 = new JLabel("StudentName");
        lblNewLabel_2.setBounds(123, 137, 89, 14);
        frame.getContentPane().add(lblNewLabel_2);

        studentname = new JTextField();
        studentname.setBounds(340, 134, 450, 20);
        frame.getContentPane().add(studentname);
        studentname.setColumns(10);

        JLabel lblNewLabel_3 = new JLabel("BookName");
        lblNewLabel_3.setBounds(122, 168, 67, 14);
        frame.getContentPane().add(lblNewLabel_3);

        bookname = new JTextField();
        bookname.setBounds(340, 165, 450, 20);
        frame.getContentPane().add(bookname);
        bookname.setColumns(10);

        JLabel lblNewLabel_4 = new JLabel("Author");
        lblNewLabel_4.setBounds(123, 197, 46, 14);
        frame.getContentPane().add(lblNewLabel_4);

        authorname = new JTextField();
        authorname.setBounds(340, 194, 450, 20);
        frame.getContentPane().add(authorname);
        authorname.setColumns(10);

        JLabel lblNewLabel_5 = new JLabel("Borrowdate");
        lblNewLabel_5.setBounds(122, 228, 67, 14);
        frame.getContentPane().add(lblNewLabel_5);

        borrowdate = new JTextField();
        borrowdate.setBounds(340, 225, 450, 20);
        frame.getContentPane().add(borrowdate);
        borrowdate.setColumns(10);

        JLabel lblNewLabel_6 = new JLabel("Status");
        lblNewLabel_6.setBounds(122, 266, 67, 14);
        frame.getContentPane().add(lblNewLabel_6);

        status1 = new JTextField();
        status1.setBounds(340, 263, 453, 20);
        frame.getContentPane().add(status1);
        status1.setColumns(10);

        JButton btnNewButton_1 = new JButton("Add book");
        btnNewButton_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    int bookId = Integer.parseInt(bookid.getText());
                    String studentName = studentname.getText();
                    String bookName = bookname.getText();
                    String author = authorname.getText();
                    String borrowDate = borrowdate.getText();
                    String status = status1.getText();

                    db.addBookToLibraryRecords(bookId, studentName, bookName, author, borrowDate, status);
                    bookid.setText("");
                    studentname.setText("");
                    bookname.setText("");
                    authorname.setText("");
                    borrowdate.setText("");
                    status1.setText("");

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Invalid input. Please enter valid data.", "Error", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "An error occurred while adding the book.", "Error", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            }
        });
        btnNewButton_1.setBounds(59, 421, 89, 23);
        frame.getContentPane().add(btnNewButton_1);

        JButton btnNewButton_2 = new JButton("Delete Book");
        btnNewButton_2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Delete functionality to be implemented
            	deleteSelectedBook();
            }
        });
        btnNewButton_2.setBounds(792, 421, 123, 23);
        frame.getContentPane().add(btnNewButton_2);

        // View Button to display all books
        JButton btnNewButton = new JButton("View");
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                displayAllBooks();
            }
        });
        btnNewButton.setBounds(404, 421, 89, 23);
        frame.getContentPane().add(btnNewButton);

        // Table setup
        tableModel = new DefaultTableModel();
        tableModel.setColumnIdentifiers(new String[]{"Book ID", "Student Name", "Book Name", "Author", "Borrow Date", "Status"});
        bookTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(bookTable);
        scrollPane.setBounds(123, 300, 670, 100);
        frame.getContentPane().add(scrollPane);
    }

    private void displayAllBooks() {
        try {
            List<DButility.Book> books = db.loadAllBooks();
            tableModel.setRowCount(0); // Clear existing data

            for (DButility.Book book : books) {
                tableModel.addRow(new Object[]{
                    book.getBookId(),
                    book.getStudentName(),
                    book.getBookName(),
                    book.getAuthor(),
                    book.getBorrowDate(),
                    book.getStatus()
                });
            }
            JOptionPane.showMessageDialog(frame, "Loaded " + books.size() + " books", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(frame, "Error loading books: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
    private void deleteSelectedBook() {
        int selectedRow = bookTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(frame, 
                "Please select a book to delete", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        int bookId = (int) tableModel.getValueAt(selectedRow, 0);
        
        try {
            boolean deleted = db.deleteBook(bookId);
            if (deleted) {
                JOptionPane.showMessageDialog(frame, 
                    "Book deleted successfully", 
                    "Success", 
                    JOptionPane.INFORMATION_MESSAGE);
                displayAllBooks(); // Refresh the table
            } else {
                JOptionPane.showMessageDialog(frame, 
                    "Book not found or could not be deleted", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(frame, 
                "Database error: " + ex.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
}