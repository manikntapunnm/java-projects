		import java.awt.Color;
		import java.awt.Dimension;
		import java.awt.Font;
		import java.awt.FontMetrics;
		import java.awt.Graphics;
		import java.awt.Toolkit;
		import java.awt.event.ActionEvent;
		import java.awt.event.ActionListener;
		import java.awt.event.KeyEvent;
		import java.awt.event.KeyListener;
        import java.io.File;
        import java.io.IOException;
        import javax.sound.sampled.AudioInputStream;
        import javax.sound.sampled.AudioSystem;
        import javax.sound.sampled.Clip;
        import javax.sound.sampled.LineUnavailableException;
        import javax.sound.sampled.UnsupportedAudioFileException;


import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;
		import javax.swing.JPanel;
		import javax.swing.Timer;
		public class SnakeGame extends JPanel implements KeyListener, ActionListener {
		  private static final long serialVersionUID = 1L;
		  
		  // size of the playing field
		  private final int WIDTH = 2000;
		  private final int HEIGHT = 1000;
		  private final int DOT_SIZE = 10;
		  private final int ALL_DOTS = 900;
		  private final int RAND_POS = 29;
		  private final int DELAY = 230;

		  private final int x[] = new int[ALL_DOTS];
		  private final int y[] = new int[ALL_DOTS];

		  private int dots;
		  private int apple_x;
		  private int apple_y;

		  private boolean leftDirection = false;
		  private boolean rightDirection = true;
		  private boolean upDirection = false;
		  private boolean downDirection = false;
		  private boolean inGame = true;

		  private Timer timer;
		  private ImageIcon dot;
		  private ImageIcon apple;
		  private ImageIcon head;

		  public SnakeGame() {
		    addKeyListener(this);
		    setBackground(Color.gray);
		    setFocusable(true);

		    setPreferredSize(new Dimension(WIDTH, HEIGHT));
		    loadImages();
		    initGame();
		  }

		  private void loadImages() {
		    ImageIcon iid = new ImageIcon("src/dot.png");
		    dot = iid;
		    ImageIcon iia = new ImageIcon("src/apple.png");
		    apple = iia;
		    ImageIcon iih = new ImageIcon("src/head.png");
		    head = iih;
		  }

		  private void initGame() {
		    dots = 3;

		    for (int z = 0; z < dots; z++) {
		      x[z] = 50 - z * 10;
		      y[z] = 50;
		    }
		    
		    locateApple();

		    timer = new Timer(DELAY, this);
		    timer.start();
		  }

		  @Override
		  protected void paintComponent(Graphics g) {
		    super.paintComponent(g);

		    doDrawing(g);
		  }
		  
		  private void doDrawing(Graphics g) {
		    if (inGame) {
		      g.drawImage(apple.getImage(), apple_x, apple_y, this);

		      for (int z = 0; z < dots; z++) {
		        if (z == 0) {
		          g.drawImage(head.getImage(), x[z], y[z], this);
		        } else {
		          g.drawImage(dot.getImage(), x[z], y[z], this);
		        }
		      }

		      Toolkit.getDefaultToolkit().sync();
		    } else {
		      gameOver(g);
		    }        
		  }
		  

		  private void gameOver(Graphics g) {
		    String msg = "Game Over";
		    Font small = new Font("Helvetica", Font.BOLD, 14);
		    FontMetrics metr = getFontMetrics(small);

		    g.setColor(Color.white);
		    g.setFont(small);
		    g.drawString(msg, (WIDTH - metr.stringWidth(msg)) / 2, HEIGHT / 2);
		  }

		  private void checkApple() {
		    if ((x[0] == apple_x) && (y[0] == apple_y)) {
		      dots++;
		      locateApple();
		      try {
		          // Load the sound file
		          File soundFile = new File("src/snakehit.wav");
		          AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
		          
		          // Get a clip to play the sound
		          Clip clip = AudioSystem.getClip();
		          clip.open(audioIn);
		          
		          // Play the sound
		          clip.start();
		        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
		            e.printStackTrace();
		        }
		      
		    }
		  }

		  private void move() {
		    for (int z = dots; z > 0; z--) {
		      x[z] = x[(z - 1)];
		      y[z] = y[(z - 1)];
		    }

		    if (leftDirection) {
		      x[0] -= DOT_SIZE;
		    }

		    if (rightDirection) {
		      x[0] += DOT_SIZE;
		    }

		    if (upDirection) {
		      y[0] -= DOT_SIZE;
		    }

		    if (downDirection) {
		      y[0] += DOT_SIZE;
		    }
		  }

		  private void checkCollision() {
		    for (int z = dots; z > 0; z--) {
		      if ((z > 4) && (x[0] == x[z]) && (y[0] == y[z])) {
		        inGame = false;
		      }
		    }

		    if (y[0] >= HEIGHT) {
		      inGame = false;
		    }

		    if (y[0] < 0) {
		      inGame = false;
		    }

		    if (x[0] >= WIDTH) {
		      inGame = false;
		    }

		    if (x[0] < 0) {
		      inGame = false;
		    }
		    
		    if (!inGame) {
		      timer.stop();
		    }
		  }

		  private void locateApple() {
		    int r = (int) (Math.random() * RAND_POS);
		    apple_x = ((r * DOT_SIZE));

		    r = (int) (Math.random() * RAND_POS);
		    apple_y = ((r * DOT_SIZE));
		  }

		  @Override
		  public void actionPerformed(ActionEvent e) {
		    if (inGame) {
		      checkApple();
		      checkCollision();
		      move();
		    }

		    repaint();
		  }

		  @Override
		  public void keyPressed(KeyEvent e) {
		    int key = e.getKeyCode();

		    if ((key == KeyEvent.VK_LEFT) && (!rightDirection)) {
		      leftDirection = true;
		      upDirection = false;
		      downDirection = false;
		    }

		    if ((key == KeyEvent.VK_RIGHT) && (!leftDirection)) {
		      rightDirection = true;
		      upDirection = false;
		      downDirection = false;
		    }

		    if ((key == KeyEvent.VK_UP) && (!downDirection)) {
		      upDirection = true;
		      rightDirection = false;
		      leftDirection = false;
		    }

		    if ((key == KeyEvent.VK_DOWN) && (!upDirection)) {
		      downDirection = true;
		      rightDirection = false;
		      leftDirection = false;
		    }
		  }

		  @Override
		  public void keyReleased(KeyEvent e) {
		  }

		  @Override
		  public void keyTyped(KeyEvent e) {
		  }
		




		

	}


