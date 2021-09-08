package ru.sber.io

import java.io.BufferedInputStream
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream


/**
 * Реализовать методы архивации и разархивации файла.
 * Для реализиации использовать ZipInputStream и ZipOutputStream.
 */
class Archivator {

    /**
     * Метод, который архивирует файл logfile.log в архив logfile.zip.
     * Архив должен располагаться в том же каталоге, что и исходной файл.
     */
    fun zipLogfile() {
        val sourceFilename = "io/logfile.log"
        val targetFilename = "io/unzippedLogfile.zip"
        var entry: ZipEntry
        var buffer: ByteArray

        try {
            FileInputStream(sourceFilename).use { fis ->
                FileOutputStream(targetFilename).use { fos ->
                    ZipOutputStream(fos).use { zos ->

                        buffer = ByteArray(fis.available())
                        fis.read(buffer)

                        entry = ZipEntry(sourceFilename)
                        zos.putNextEntry(entry)
                        zos.write(buffer)
                        zos.closeEntry()
                    }
                }
            }
        } catch (t: Throwable) {
            throw t
        }
    }

    /**
     * Метод, который извлекает файл из архива.
     * Извлечь из архива logfile.zip файл и сохарнить его в том же каталоге с именем unzippedLogfile.log
     */
    fun unzipLogfile() {
        val sourceFilename = "io/unzippedLogfile.zip"
        val targetFilename = "io/unzippedLogfile.log"
        val buffer = ByteArray(1024)
        var count: Int
        var zipEntry: ZipEntry?

        try {
            FileInputStream(sourceFilename).use { fis ->
                ZipInputStream(BufferedInputStream(fis)).use { zis ->
                    zipEntry = zis.nextEntry
                    if (zipEntry != null) {
                        FileOutputStream(targetFilename).use { fos ->

                            count = zis.read(buffer)
                            while (count != -1) {
                                fos.write(buffer, 0, count)
                                count = zis.read(buffer)
                            }
                            zis.closeEntry()
                        }
                    }
                }
            }
        } catch (t: Throwable) {
            throw t
        }
    }
}
