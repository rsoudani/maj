/*
 * Copyright 2016 Richard Cartwright
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/*
 * $Log: AAFTestRead.java,v $
 * Revision 1.5  2010/11/08 16:46:59  vizigoth
 * Making more output visible for file analysis.
 *
 * Revision 1.4  2010/06/14 17:12:16  vizigoth
 * Significant progress towards writing valid AAF files with MAJ.
 *
 * Revision 1.3  2010/05/14 18:29:05  vizigoth
 * First version to output something AAF-like!
 *
 * Revision 1.2  2010/03/19 10:10:11  vizigoth
 * Added comment headers, tidied up and added a factory with a readPreface method.
 *
 */

package tv.amwa.maj.io.aaf;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Iterator;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.poifs.filesystem.DirectoryEntry;
import org.apache.poi.poifs.filesystem.DirectoryNode;
import org.apache.poi.poifs.filesystem.DocumentEntry;
import org.apache.poi.poifs.filesystem.DocumentNode;
import org.apache.poi.poifs.filesystem.Entry;
import org.apache.poi.poifs.filesystem.POIFSDocument;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.poifs.property.DirectoryProperty;
import org.apache.poi.poifs.storage.BlockWritable;
import org.apache.poi.poifs.storage.SmallDocumentBlock;

@Deprecated // Old prototype code included for reference only
@Slf4j
public class AAFTestRead {

	static int indent = 0;
	
	static final String makeIndent() {
		
		char[] spaces = new char[indent];
		
		for ( int x = 0 ; x < indent ; x++ )
			spaces[x] = ' ';
		
		return new String(spaces);
	}
	
	@SuppressWarnings("unchecked")
	public final static void entryIterator(
			DirectoryEntry directory) 
		throws IOException {
		
		for (Iterator<Entry> iter = directory.getEntries(); iter.hasNext(); )
		{
		    Entry entry = (Entry)iter.next();

		    if (entry instanceof DirectoryEntry)
		    {
		        //log.info(makeIndent() + "Directory :" + entry.getName());
		        
		        DirectoryNode dirNode = (DirectoryNode) entry;
		        log.info(makeIndent() + dirNode.getPath().toString() + " " +
		        		dirNode.getStorageClsid().toString());
		        
		        for ( Iterator jerry = dirNode.getViewableIterator() ; jerry.hasNext() ; ) {
		        	
		        	Object theNext = jerry.next();
		        	// log.info("dirNode#" + theNext.toString());
		        	
		        	if (theNext instanceof DirectoryProperty) {
		        	
		        		@SuppressWarnings("unused")
						DirectoryProperty dirProperty = (DirectoryProperty) theNext;
		        		// log.info("dirProperty#" + dirProperty.getShortDescription() + ": " + dirProperty.getSize());
		        	}
		        	
		        }
		        
		        indent += 2;
		        entryIterator((DirectoryEntry) entry);
		    }
		    else if (entry instanceof DocumentEntry)
		    {
		    	log.info(makeIndent() + "Document : " + entry.getName());
		    	
		    	log.info(makeIndent() + "Documnent class: " + entry.getClass().getName());
		    	
		    	for ( Iterator frank = ((DocumentNode) entry).getViewableIterator() ; frank.hasNext() ; ) {
		    		
		    		Object theNextDoc = frank.next();
		    		
		    		log.info("docNode#" + theNextDoc.toString());
		    		/*
		    		if (theNextDoc instanceof DocumentProperty) {
		    			
		    			DocumentProperty docProp = (DocumentProperty) theNextDoc;
		    			log.info("docPropertyName: " + docProp.getName() + " " + docProp.getShortDescription());
		    			
		    			for ( Iterator ben = docProp.getViewableIterator() ; ben.hasNext() ; ) {
		    				log.info("HELLO: " + ben.next().toString());
		    			}
		    		}*/

		    		if (theNextDoc instanceof POIFSDocument) {
		    			
		    			POIFSDocument theDoc = (POIFSDocument) theNextDoc;
		    			
		    			log.info("docName: " + theDoc.getShortDescription());
		    			log.info("docBlocks: " + theDoc.countBlocks() + " " + theDoc.getSmallBlocks().length);
		    			
		    			BlockWritable[] blocks = theDoc.getSmallBlocks();
		    			for ( int x = 0 ; x < blocks.length ; x++ ) {
		    				log.info("Block " + x + ": " + blocks[x].toString());
		    			
		    				if (blocks[x] instanceof SmallDocumentBlock) {
		    					
		    					SmallDocumentBlock block = (SmallDocumentBlock) blocks[x];
		    					log.info(Arrays.toString(block.getData()));
		    				}
		    			}
		    		}
		    	}
		    	
		    }
		    else
		    {
		        // currently, either an Entry is a DirectoryEntry or a DocumentEntry,
			// but in the future, there may be other entry subinterfaces. The
			// internal data structure certainly allows for a lot more entry types.
		    	log.info("Unknown entry of type " + entry.getClass().getName());
		    }
		}

		indent -= 2;
	}
	
	public final static void main(
			String[] args) {
		
		// need an open InputStream; for a file-based system, this would be appropriate:
		InputStream stream = null;
		try {
			stream = new FileInputStream(args[0]);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		POIFSFileSystem fs = null;
		try
		{
		    fs = new POIFSFileSystem(stream);
		}
		catch (IOException e)
		{
		    log.warn(e.getMessage());
		    e.printStackTrace();
		}
		DirectoryEntry root = fs.getRoot();
		
		log.info("Root: " + root.getName());
		
		try {
			entryIterator(root);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
