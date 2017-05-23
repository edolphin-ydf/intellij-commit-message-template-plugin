package commitmessagetemplate.util.file;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.ModalityState;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.util.Computable;
import commitmessagetemplate.util.Const;
import commitmessagetemplate.util.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

/**
 * Created by Arsen on 04.09.2016.
 */
public class FileReaderUtil {

    public static String readFile(String path) {
        return readFile(new File(path));
    }

    public static String readFile(File file) {
        final String[] result = new String[1];
        ApplicationManager.getApplication().invokeAndWait(() ->
                        result[0] = ApplicationManager.getApplication().runReadAction((Computable<String>) () -> {
                            try {
                                BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), Const.charsets.UTF_8));
                                StringBuilder sb = new StringBuilder();
                                String line = br.readLine();

                                while (line != null) {
                                    sb.append(line);
                                    sb.append("\n");
                                    line = br.readLine();
                                }

                                br.close();
                                return sb.toString();
                            } catch (Exception e) {
                                Logger.log("Error read file " + e.getMessage());
                                Logger.printStack(e);
                                return null;
                            }
                        })
                , ModalityState.defaultModalityState());
        return result[0];
    }

    public static FileChooserDescriptor getDirectoryDescriptor() {
        FileChooserDescriptor descriptor = new FileChooserDescriptor(false, true, false, false, false, false);
        return descriptor;
    }

}
