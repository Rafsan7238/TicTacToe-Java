package Task2;

import java.awt.*;
import java.awt.event.*;
import java.util.Scanner;
import java.io.PrintWriter;
import java.net.Socket;
import javax.swing.*;

public class Client {

    private JFrame frame = new JFrame("Tic Tac Toe");
    private JLabel label = new JLabel("...");

    private Box[] board = new Box[9];
    private Box currentBox;

    private Socket s;
    private Scanner din;
    private PrintWriter dout;

    public Client() throws Exception {

        s = new Socket("localhost", 3333);
        din = new Scanner(s.getInputStream());
        dout = new PrintWriter(s.getOutputStream(), true);

        label.setBackground(Color.lightGray);
        frame.getContentPane().add(label, BorderLayout.SOUTH);

        var panel = new JPanel();
        panel.setBackground(Color.black);
        panel.setLayout(new GridLayout(3, 3, 2, 2));
        for (var i = 0; i < board.length; i++) {
            final int j = i;
            board[i] = new Box();
            board[i].addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent e) {
                    currentBox = board[j];
                    dout.println("MOVE " + j);
                }
            });
            panel.add(board[i]);
        }
        frame.getContentPane().add(panel, BorderLayout.CENTER);
    }

    public void khelo() throws Exception {
        try {
            var reply = din.nextLine();
            var mark = reply.charAt(8);
            var onnerMark = (mark == 'X' ? 'O' : 'X');
            frame.setTitle("Tic Tac Toe: Khelowar " + mark);
            while (din.hasNextLine()) {
                reply = din.nextLine();
                if (reply.startsWith("VALID_MOVE")) {
                    label.setText("Thik chal diyecho, wait...");
                    currentBox.setText(mark);
                    currentBox.repaint();
                } else if (reply.startsWith("OPPONENT_MOVED")) {
                    var loc = Integer.parseInt(reply.substring(15));
                    board[loc].setText(onnerMark);
                    board[loc].repaint();
                    label.setText("Opponent chal diyeche, ekhon tumi dao");
                } else if (reply.startsWith("MESSAGE")) {
                    label.setText(reply.substring(8));
                } else if (reply.startsWith("VICTORY")) {
                    JOptionPane.showMessageDialog(frame, "Jite geso! Treat dao!");
                    break;
                } else if (reply.startsWith("DEFEAT")) {
                    JOptionPane.showMessageDialog(frame, "Here geso! Bashay chole jao!");
                    break;
                } else if (reply.startsWith("TIE")) {
                    JOptionPane.showMessageDialog(frame, "Khela draw");
                    break;
                } else if (reply.startsWith("OTHER_PLAYER_LEFT")) {
                    JOptionPane.showMessageDialog(frame, "Arek player palay gese");
                    break;
                }
            }
            dout.println("QUIT");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            s.close();
            frame.dispose();
        }
    }

    static class Box extends JPanel {
        JLabel label = new JLabel();

        public Box() {
            setBackground(Color.white);
            setLayout(new GridBagLayout());
            label.setFont(new Font("Arial", Font.BOLD, 40));
            add(label);
        }

        public void setText(char text) {
            label.setForeground(text == 'X' ? Color.BLUE : Color.RED);
            label.setText(text + "");
        }
    }

    public static void main(String[] args) throws Exception {

        Client client = new Client();
        client.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        client.frame.setSize(320, 320);
        client.frame.setVisible(true);
        client.frame.setResizable(false);
        client.khelo();
    }
}