package org.pedrick.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;


/*=======================================================================

*FILE:         JUnZip.java
*
*DESCRIPTION:  Class for Zip/Unzip files and Folders
*REQUIREMENTS: 
*AUTHOR:       Patrick Facco
*VERSION:      1.0
*CREATED:      5-nov-2014
*LICENSE:      GNU/GPLv3
*COPYRIGTH:    Patrick Facco 2014
*This program is free software: you can redistribute it and/or modify
*it under the terms of the GNU General Public License as published by
*the Free Software Foundation, either version 3 of the License, or
*(at your option) any later version.
*
*This program is distributed in the hope that it will be useful,
*but WITHOUT ANY WARRANTY; without even the implied warranty of
*MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*GNU General Public License for more details.
*
*You should have received a copy of the GNU General Public License
*along with this program.  If not, see <http://www.gnu.org/licenses/>.
*========================================================================
* 
*/

public class JZip
{   
    
    private List<String> fileList;
    /**
     * Method that unzip the file with name toUnzip in the folder called outputDir
     * @param toUnzip path of file to unzip
     * @param outputDir path for output dir
     * @param preserve if true, the zip file is not deleted after unzipped
     * @throws FileNotFoundException if input file or output dir doesn't exists
     * @throws IOException if an read/write error occur
     */
    public void unzip(String toUnzip, String outputDir, boolean preserve)
            throws FileNotFoundException, IOException
    {
        String zipPath = toUnzip;
        
        BufferedOutputStream out;
        InputStream  in;
        ZipEntry entry;
        ZipFile zipFile;
        Enumeration entries;
        
        zipFile = new ZipFile(zipPath);
        ZipFile tmp = zipFile;
        entries = zipFile.entries();
        byte[] buffer = new byte[1024];
        
        if(zipFile.size() != 0)
        {
             //creo le directory
             while(entries.hasMoreElements())
             {
                 entry = (ZipEntry)entries.nextElement();
                 if(entry.isDirectory())
                 {
                     (new File(outputDir + "/" + entry.getName())).mkdir();  
                 }                
             }
             //estraggo i file
             entries = tmp.entries();
             while(entries.hasMoreElements())
             {
                 entry = (ZipEntry)entries.nextElement();
                 if(!entry.isDirectory())
                 {
                     in = zipFile.getInputStream(entry);
                     out = new BufferedOutputStream(new FileOutputStream(outputDir + "/" + entry.getName()));                     
                    
                     int len;
                     while((len = in.read(buffer)) >= 0)
		     {
                         out.write(buffer, 0, len);
		     }

                     in.close();
                     out.close();
                 }                
             }
             if(!preserve)
                new File(zipPath).delete();
          }            
    }        
    
    /**
     * Zip a directory or file
     * @param inputDir the path of input directory or file to zip
     * @param outputZip the path of zipped file
     * @throws java.io.FileNotFoundException
     */
    public void zip(String inputDir, String outputZip) throws FileNotFoundException, IOException
    {
        if(new File(inputDir).isDirectory())
        {
            zipDir(inputDir, outputZip);
        }
        else
        {
            zipFile(inputDir, outputZip);
        }
         
    }
    
    /**
     * Zip a directory
     * @param inputDir the path of directory to zip
     * @param outputZip the path for output zip file
     * @throws FileNotFoundException if paths not exist
     * @throws IOException if an error occurs opening files
     */
    private void zipDir(String inputDir, String outputZip)
            throws FileNotFoundException, IOException
    {
        fileList = new ArrayList<String>();
        //get list of file in input folder
        getFileList(new File(inputDir), inputDir);
        
        //create buffer and outputstreams
        byte[] buffer = new byte[1024];
        FileOutputStream fos = new FileOutputStream(outputZip);
    	ZipOutputStream zos = new ZipOutputStream(fos);
        
        for(String file: fileList)
        {
            ZipEntry ze= new ZipEntry(file);
            zos.putNextEntry(ze);
            FileInputStream in = new FileInputStream(inputDir + File.separator + file);
 
            int len;
            while ((len = in.read(buffer)) > 0) 
            {
                zos.write(buffer, 0, len);
            }
 
            in.close();
        }
        
        zos.closeEntry();
    	zos.close();
    }
    
    /**
     * Zip a file
     * @param inputFile the path of file to zip
     * @param outputZip the path for output zip file
     * @throws FileNotFoundException if paths not exist
     * @throws IOException if an error occurs opening files
     */    
    private void zipFile(String inputFile, String outputZip) 
            throws FileNotFoundException, IOException
    {
        byte[] buffer = new byte[1024];
        FileOutputStream fos = new FileOutputStream(outputZip);
    	ZipOutputStream zos = new ZipOutputStream(fos);
        String[] pathAsArray = inputFile.split(File.separator);
        ZipEntry ze= new ZipEntry(pathAsArray[pathAsArray.length - 1]);
        zos.putNextEntry(ze);
        FileInputStream in = new FileInputStream(inputFile); 
        int len;
    	while ((len = in.read(buffer)) > 0)
        {
            zos.write(buffer, 0, len);
    	}
        
        in.close();
    	zos.closeEntry();
 
    	zos.close();
    }
    
    /**
     * Get file list from directory recusively
     * @param cursor input file or folder     * 
     * @param inputDir path of directory that contains file(s)
     */
    private void getFileList(File cursor, String inputDir)
    {           
        if(cursor.isFile())
        {            
            fileList.add(getZipEntry(cursor.getAbsoluteFile().toString(), inputDir));
        }
        
        if(cursor.isDirectory())
        {
            String[] subNote = cursor.list();
            
            for(String filename : subNote)
            {
                getFileList(new File(cursor, filename), inputDir);
            }
        }        
    }

    /**
     * Generate zip entry path
     * @param src source file
     * @param inputDir directory that contains file
     * @return String a zip entry generated as a String
     */
    private String getZipEntry(String src, String inputDir)
    {                
        String toReturn = src.substring(inputDir.length()+1, src.length());
        return toReturn;
              
    }   
}