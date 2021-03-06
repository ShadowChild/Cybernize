package uk.co.innoxium.cybernize.archive.extractor;

import uk.co.innoxium.cybernize.archive.Archive;
import uk.co.innoxium.cybernize.archive.ArchiveItem;

import java.io.*;
import java.util.HashSet;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

public class ZipExtractor extends ArchiveExtractor {

    @Override
    public boolean validate(Archive archive) {

        int fileSignature = 0;
        try (RandomAccessFile raf = new RandomAccessFile(archive.archive, "r")) {

            fileSignature = raf.readInt();
        } catch (IOException e) {

            e.printStackTrace();
        }
        return fileSignature == 0x504B0304 || fileSignature == 0x504B0506 || fileSignature == 0x504B0708;
    }

    @Override
    public boolean extract(Archive archive) throws IOException {

        byte[] buffer = new byte[1024];

        //create output directory is not exists
        File folder = archive.outputDirectory;
        if(!folder.exists()) {

            folder.mkdirs();
        }

        //get the zip file content
        ZipInputStream zis = new ZipInputStream(new FileInputStream(archive.archive));
        //get the zipped file list entry
        ZipEntry ze = zis.getNextEntry();

        while(ze != null) {

            String fileName = ze.getName();
            File newFile = new File(folder, File.separatorChar + fileName);

            System.out.println("CYBERNIZE: file unzip: " + newFile.getAbsoluteFile());

            //create all non exists folders
            //else you will hit FileNotFoundException for compressed folder
            new File(newFile.getParent()).mkdirs();
            if(ze.isDirectory()) { newFile.mkdirs(); } else newFile.createNewFile();

            if(newFile.isFile()) {

                FileOutputStream fos = new FileOutputStream(newFile);

                int len;
                while((len = zis.read(buffer)) > 0) {

                    fos.write(buffer, 0, len);
                }

                fos.close();
            } else throw new IOException(String.format("File %s is not a file", newFile.getName()));
            ze = zis.getNextEntry();
        }

        zis.closeEntry();
        zis.close();

        System.out.printf("CYBERNIZE: Finished extracting file %s%n", archive.archive.getName());
        return true;
    }

    @Override
    public Set<ArchiveItem> getAllArchiveItems(Archive archive) throws IOException {

        Set<ArchiveItem> set = new HashSet<>();

        ZipFile zip = new ZipFile(archive.archive);
        zip.stream().iterator().forEachRemaining(action -> {

            set.add(new ArchiveItem(archive.archive.getName(), action.getName(), action.isDirectory()));
        });

        return set;
    }
}
