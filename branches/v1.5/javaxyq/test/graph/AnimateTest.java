/*
 * JavaXYQ Source Code
 * by kylixs
 * at 2009-12-5
 * please visit http://javaxyq.googlecode.com
 * or mail to kylixs@qq.com
 */
package graph;

import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.javaxyq.util.TCPImageDecoder;
import com.javaxyq.widget.TCPFrame;

/**
 * @author dewitt
 * @date 2009-12-5 create
 */
public class AnimateTest extends JFrame {

	public AnimateTest() {
		super("Animate Painting Test" );
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		JPanel panel = new JPanel();
		try {
			for (int i = 0; i < 8; i++) {
				Image image = getSprite(i);
				JLabel label = new JLabel(new ImageIcon(image));
				panel.add(label);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		add(panel);
		setSize(400, 300);
	}
	private Image getSprite(int index) throws Exception {
		TCPImageDecoder decoder = new TCPImageDecoder("res/shape/char/0001/stand.tcp");
		Image image = decoder.readAnimation(index);
		return image;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO main
		new AnimateTest().setVisible(true);
	}

}
