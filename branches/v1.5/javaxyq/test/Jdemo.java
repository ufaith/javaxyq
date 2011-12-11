import java.io.File;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

public class Jdemo {
    public Jdemo(String s) {
        try {

            //System.getProperties().store(System.out, "");

            JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
            DiagnosticCollector diagnostics = new DiagnosticCollector();
            StandardJavaFileManager fileManager = compiler
                    .getStandardFileManager(diagnostics, null, null);
            String fileName = "MyButton1.java";

            File file = new File(System.getProperty("java.io.tmpdir"), fileName);
            PrintWriter pw = new PrintWriter(file);
            pw.println(s);
            pw.close();

            Iterable compilationUnits = fileManager
                    .getJavaFileObjectsFromStrings(Arrays.asList(file
                            .getAbsolutePath()));
            JavaCompiler.CompilationTask task = compiler.getTask(null,
                    fileManager, diagnostics, null, null, compilationUnits);
            boolean success = task.call();
            fileManager.close();
            System.out.println((success) ? "±‡“Î≥…π¶" : "±‡“Î ß∞‹");
            if(!success) {
            	List<Diagnostic> list = diagnostics.getDiagnostics();
            	for (Diagnostic dia : list) {
					System.out.println(dia);
				}
            }else {
            URLClassLoader classLoader = new URLClassLoader(
                    new URL[] { new File(System.getProperty("java.io.tmpdir"))
                            .toURI().toURL() });
            final JButton btn = (JButton) classLoader.loadClass("MyButton1")
                    .newInstance();
            btn.setText("∞¥≈•");
            System.out.println(btn);
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    JFrame frame = new JFrame("frame");
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    frame.setSize(640, 480);
                    frame.setLocationRelativeTo(null);
                    frame.getContentPane().add(btn);
                    frame.setVisible(true);
                }
            });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String args[]) {
    	System.out.println("\u6d3b");
//        new Jdemo(
//                "import javax.swing.JButton; \npublic class MyButton1 extends JButton {a}");
    }
}