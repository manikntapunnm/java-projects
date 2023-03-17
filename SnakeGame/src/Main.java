

	public class Main {
		  public static void main(String[] args) {
		    javax.swing.SwingUtilities.invokeLater(new Runnable() {
		      public void run() {
		        SnakeGame snake = new SnakeGame();
		        javax.swing.JFrame frame = new javax.swing.JFrame("Snake Game");
		        frame.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
		        frame.add(snake);
		        frame.pack();
		        frame.setLocationRelativeTo(null);
		        frame.setVisible(true);
		      }
		    });
		  }
		}



