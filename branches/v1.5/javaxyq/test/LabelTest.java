import javax.swing.JFrame;
import javax.swing.JLabel;

/*
 * JavaXYQ Source Code
 * by kylixs
 * at 2010-5-20
 * please visit http://javaxyq.googlecode.com
 * or mail to kylixs@qq.com
 */

/**
 * @author Administrator
 * @date 2010-5-20 create
 */
public class LabelTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		JFrame f = new JFrame("Label Demo");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setSize(300, 200);
		JLabel label = new JLabel();
		f.add(label);
	}

}
