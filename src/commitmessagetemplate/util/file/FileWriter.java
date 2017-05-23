package commitmessagetemplate.util.file;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.ModalityState;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Computable;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiManager;
import com.intellij.util.IncorrectOperationException;
import commitmessagetemplate.util.Const;
import commitmessagetemplate.util.Logger;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * Created by CeH9 on 19.06.2016.
 */
public class FileWriter {

    public static PsiDirectory findCurrentDirectory(Project project, VirtualFile file) {
        final PsiDirectory[] result = new PsiDirectory[1];
        ApplicationManager.getApplication().invokeAndWait(() ->
                        result[0] = ApplicationManager.getApplication().runReadAction((Computable<PsiDirectory>) () -> {
                            if (file == null || project == null) {
                                return null;
                            }

                            if (file.isDirectory()) {
                                return PsiManager.getInstance(project).findDirectory(file);
                            } else {
                                return PsiManager.getInstance(project).findDirectory(file.getParent());
                            }
                        })
                , ModalityState.defaultModalityState());
        return result[0];
    }

    //=================================================================
    //  Low Level I/O
    //=================================================================
    public static boolean writeStringToFile(String text, String path) {
        return writeStringToFile(text, new File(path));
    }

    public static boolean writeStringToFile(String text, File file) {
        return wrapInWriteLater(() -> {
            try {
                file.getParentFile().mkdirs();
                file.createNewFile();
                Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), Const.charsets.UTF_8));

                out.write(text);

                out.flush();
                out.close();
                return true;
            } catch (Exception e) {
                Logger.log(e.getMessage());
                Logger.printStack(e);
                return false;
            }
        });
    }

    public static boolean copyFile(Path from, Path to) {
        return wrapInWriteLater(() -> {
            try {
                to.toFile().getParentFile().mkdirs();
                Files.copy(from, to, StandardCopyOption.REPLACE_EXISTING);
                return true;
            } catch (IOException e) {
                Logger.log("copyFile ex: " + e.getMessage());
                Logger.printStack(e);
                return false;
            }
        });
    }


    //=================================================================
    //  Delete
    //=================================================================
    public static boolean removeDirectory(PsiDirectory psiDirectory) {
        return wrapInWriteLater(() -> {
            try {
                psiDirectory.delete();
                return true;
            } catch (Exception e) {
                Logger.log("removeDirectory ex: " + e.getMessage());
                Logger.printStack(e);
                return false;
            }
        });
    }

    public static boolean removeDirectory(File dir) {
        return wrapInWriteLater(() -> {
            try {
                Files.walkFileTree(dir.toPath(), new FileVisitor<Path>() {
                    @Override
                    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                        return FileVisitResult.CONTINUE;
                    }

                    @Override
                    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                        Files.deleteIfExists(file);
                        return FileVisitResult.CONTINUE;
                    }

                    @Override
                    public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                        return FileVisitResult.CONTINUE;
                    }

                    @Override
                    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                        Files.deleteIfExists(dir);
                        return FileVisitResult.CONTINUE;
                    }
                });
                return true;
            } catch (IOException e) {
                Logger.log("removeDirectory ex: " + e.getMessage());
                Logger.printStack(e);
                return false;
            }
        });
    }

    public static boolean removeFile(File file) {
        return wrapInWriteLater(() -> {
            try {
                Files.delete(file.toPath());
                return true;
            } catch (IOException e) {
                Logger.log("removeFile ex: " + e.getMessage());
                Logger.printStack(e);
                return false;
            }
        });
    }

    public static boolean removeFile(PsiElement psiFile) {
        return wrapInWriteLater(() -> {
            try {
                psiFile.delete();
                return true;
            } catch (IncorrectOperationException e) {
                Logger.log("removeFile ex: " + e.getMessage());
                Logger.printStack(e);
                return false;
            }
        });
    }


    //=================================================================
    //  utils
    //=================================================================
    private static boolean wrapInWriteLater(Computable<Boolean> computable) {
        final boolean[] result = new boolean[1];

        ApplicationManager.getApplication().invokeAndWait(() ->
                result[0] = ApplicationManager.getApplication().runWriteAction(computable),
                ModalityState.defaultModalityState());

        return result[0];
    }

}
