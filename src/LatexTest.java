import javax.swing.*;
import java.awt.*;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.ArrayList;

public class LatexTest {

//    private static String MIMETEX_EXE = "c:\\Program Files (x86)\\mimetex\\mimetex.cgi";
    private static String MIMETEX_EXE = "../cFiles/mimetex.cgi";

    final private static int BUFFER_SIZE = 1024;

    /**
     * Convert LaTeX code to GIF
     *
     * @param latexString LaTeX code
     * @return GIF image, under byte[] format
     */
    public static byte[] getLaTeXImage(String latexString) {
        byte[] imageData = null;
        try {
            // mimetex is asked (on the command line) to convert
            // the LaTeX expression to .gif on standard output:
            Process proc = Runtime.getRuntime().exec(MIMETEX_EXE + " -d \"" + latexString + "\"");
            // get the output stream of the process:
            BufferedInputStream bis = (BufferedInputStream) proc.getInputStream();
            // read output process by bytes blocks (size: BUFFER_SIZE)
            // and stores the result in a byte[] Arraylist:
            int bytesRead;
            byte[] buffer = new byte[BUFFER_SIZE];
            ArrayList<byte[]> al = new ArrayList<byte[]>();
            while ((bytesRead = bis.read(buffer)) != -1) {
                al.add(buffer.clone());
            }
            // convert the Arraylist in an unique array:
            int nbOfArrays = al.size();
            if (nbOfArrays == 1) {
                imageData = buffer;
            } else {
                imageData = new byte[BUFFER_SIZE * nbOfArrays];
                byte[] temp;
                for (int k = 0; k < nbOfArrays; k++) {
                    temp = al.get(k);
                    for (int i = 0; i < BUFFER_SIZE; i++) {
                        imageData[BUFFER_SIZE * k + i] = temp[i];
                    }
                }
            }
            bis.close();
            proc.destroy();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imageData;
    }

    /**
     * demonstration main
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        JFrame jframe = new JFrame();
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jframe.setLayout(new BorderLayout());

        String LATEX_EXPRESSION_1 = "\\sigma_{thing}";
        byte[] imageData1 = getLaTeXImage(LATEX_EXPRESSION_1);
        JLabel button1 = new JLabel(new ImageIcon(imageData1));
        jframe.add(button1, BorderLayout.NORTH);

//        String LATEX_EXPRESSION_2 = "4$\\array{rccclBCB$&f&\\longr[75]^{\\alpha:{-1$f\\rightar~g}}&g\\\\3$\\gamma&\\longd[50]&&\\longd[50]&3$\\gamma\\\\&u&\\longr[75]_\\beta&v}";
//        byte[] imageData2 = getLaTeXImage(LATEX_EXPRESSION_2);
//        JLabel button2 = new JLabel(new ImageIcon(imageData2));
//        jframe.add(button2, BorderLayout.CENTER);
//
//        String LATEX_EXPRESSION_3 = "4$\\hspace{5}\\unitlength{1}\\picture(175,100){~(50,50){\\circle(100)}(1,50){\\overbrace{\\line(46)}^{4$\\;\\;a}}(52,50) {\\line(125)}~(50,52;115;2){\\mid}~(52,55){\\longleftar[60]}(130,56){\\longrightar[35]}~(116,58){r}~(c85,50;80;2){\\bullet} (c85,36){3$-q}~(c165,36){3$q}(42,30){\\underbrace{\\line(32)}_{1$a^2/r\\;\\;\\;}}~}";
//        byte[] imageData3 = getLaTeXImage(LATEX_EXPRESSION_3);
//        JLabel button3 = new JLabel(new ImageIcon(imageData3));
//        jframe.add(button3, BorderLayout.SOUTH);

        jframe.pack();
        jframe.setLocationRelativeTo(null);
        jframe.setVisible(true);
    }
}